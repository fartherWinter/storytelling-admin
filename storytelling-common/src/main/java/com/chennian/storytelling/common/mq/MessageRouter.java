package com.chennian.storytelling.common.mq;

import org.apache.rocketmq.common.message.MessageExt;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

/**
 * 消息路由器
 * 根据消息的主题和标签将消息路由到对应的处理器
 * 
 * @author chennian
 * @since 2024-01-01
 */
@Component
public class MessageRouter {

    private static final Logger logger = LoggerFactory.getLogger(MessageRouter.class);

    @Autowired(required = false)
    private List<MessageHandler> messageHandlers = new ArrayList<>();

    private final ConcurrentMap<String, List<MessageHandler>> topicHandlerMap = new ConcurrentHashMap<>();
    private final ConcurrentMap<String, MessageHandler> exactMatchCache = new ConcurrentHashMap<>();

    @PostConstruct
    public void init() {
        // 按优先级排序处理器
        messageHandlers.sort(Comparator.comparingInt(MessageHandler::getPriority));
        
        // 构建主题到处理器的映射
        for (MessageHandler handler : messageHandlers) {
            String topic = handler.getSupportedTopic();
            topicHandlerMap.computeIfAbsent(topic, k -> new ArrayList<>()).add(handler);
            
            logger.info("Registered message handler: {} for topic: {}, tag: {}, priority: {}",
                       handler.getClass().getSimpleName(),
                       handler.getSupportedTopic(),
                       handler.getSupportedTag(),
                       handler.getPriority());
        }
        
        logger.info("Message router initialized with {} handlers", messageHandlers.size());
    }

    /**
     * 路由消息到对应的处理器
     * 
     * @param message 消息
     * @return 处理结果
     */
    public boolean routeMessage(MessageExt message) {
        String topic = message.getTopic();
        String tag = message.getTags();
        String cacheKey = topic + ":" + tag;
        
        try {
            // 先尝试从缓存中获取精确匹配的处理器
            MessageHandler cachedHandler = exactMatchCache.get(cacheKey);
            if (cachedHandler != null) {
                return handleMessage(message, cachedHandler);
            }
            
            // 查找支持该消息的处理器
            MessageHandler handler = findHandler(message);
            if (handler != null) {
                // 缓存精确匹配的处理器
                exactMatchCache.put(cacheKey, handler);
                return handleMessage(message, handler);
            }
            
            logger.warn("No handler found for message: topic={}, tag={}, msgId={}", 
                       topic, tag, message.getMsgId());
            return false;
            
        } catch (Exception e) {
            logger.error("Error routing message: topic={}, tag={}, msgId={}", 
                        topic, tag, message.getMsgId(), e);
            return false;
        }
    }

    /**
     * 查找支持该消息的处理器
     * 
     * @param message 消息
     * @return 处理器
     */
    private MessageHandler findHandler(MessageExt message) {
        String topic = message.getTopic();
        
        // 先查找精确匹配的主题处理器
        List<MessageHandler> topicHandlers = topicHandlerMap.get(topic);
        if (topicHandlers != null) {
            for (MessageHandler handler : topicHandlers) {
                if (handler.supports(message)) {
                    return handler;
                }
            }
        }
        
        // 查找通配符主题处理器
        List<MessageHandler> wildcardHandlers = topicHandlerMap.get("*");
        if (wildcardHandlers != null) {
            for (MessageHandler handler : wildcardHandlers) {
                if (handler.supports(message)) {
                    return handler;
                }
            }
        }
        
        return null;
    }

    /**
     * 使用指定处理器处理消息
     * 
     * @param message 消息
     * @param handler 处理器
     * @return 处理结果
     */
    private boolean handleMessage(MessageExt message, MessageHandler handler) {
        try {
            logger.debug("Handling message with {}: topic={}, tag={}, msgId={}",
                        handler.getClass().getSimpleName(),
                        message.getTopic(),
                        message.getTags(),
                        message.getMsgId());
            
            boolean result = handler.handleMessage(message);
            
            if (result) {
                handler.onSuccess(message);
                logger.debug("Message handled successfully: msgId={}", message.getMsgId());
            } else {
                logger.warn("Message handling returned false: msgId={}", message.getMsgId());
            }
            
            return result;
            
        } catch (Exception e) {
            handler.onFailure(message, e);
            logger.error("Error handling message with {}: msgId={}", 
                        handler.getClass().getSimpleName(), message.getMsgId(), e);
            return false;
        }
    }

    /**
     * 获取所有注册的处理器
     * 
     * @return 处理器列表
     */
    public List<MessageHandler> getAllHandlers() {
        return new ArrayList<>(messageHandlers);
    }

    /**
     * 获取指定主题的处理器
     * 
     * @param topic 主题
     * @return 处理器列表
     */
    public List<MessageHandler> getHandlersByTopic(String topic) {
        List<MessageHandler> handlers = topicHandlerMap.get(topic);
        return handlers != null ? new ArrayList<>(handlers) : new ArrayList<>();
    }

    /**
     * 清除缓存
     */
    public void clearCache() {
        exactMatchCache.clear();
        logger.info("Message router cache cleared");
    }

    /**
     * 获取路由统计信息
     * 
     * @return 统计信息
     */
    public RouterStatistics getStatistics() {
        return new RouterStatistics(
                messageHandlers.size(),
                topicHandlerMap.size(),
                exactMatchCache.size()
        );
    }

    /**
     * 路由统计信息
     */
    public static class RouterStatistics {
        private final int totalHandlers;
        private final int topicCount;
        private final int cacheSize;

        public RouterStatistics(int totalHandlers, int topicCount, int cacheSize) {
            this.totalHandlers = totalHandlers;
            this.topicCount = topicCount;
            this.cacheSize = cacheSize;
        }

        public int getTotalHandlers() {
            return totalHandlers;
        }

        public int getTopicCount() {
            return topicCount;
        }

        public int getCacheSize() {
            return cacheSize;
        }

        @Override
        public String toString() {
            return String.format("RouterStatistics{totalHandlers=%d, topicCount=%d, cacheSize=%d}",
                               totalHandlers, topicCount, cacheSize);
        }
    }
}