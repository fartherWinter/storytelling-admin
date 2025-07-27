package com.chennian.storytelling.common.mq;

import org.apache.rocketmq.common.message.MessageExt;

/**
 * 消息处理器接口
 * 定义消息处理的统一规范
 * 
 * @author chennian
 * @since 2024-01-01
 */
public interface MessageHandler {

    /**
     * 处理消息
     * 
     * @param message 消息
     * @return 处理结果
     */
    boolean handleMessage(MessageExt message);

    /**
     * 获取支持的主题
     * 
     * @return 主题名称
     */
    String getSupportedTopic();

    /**
     * 获取支持的标签
     * 
     * @return 标签名称，支持通配符
     */
    String getSupportedTag();

    /**
     * 获取处理器优先级
     * 数值越小优先级越高
     * 
     * @return 优先级
     */
    default int getPriority() {
        return 100;
    }

    /**
     * 是否支持该消息
     * 
     * @param message 消息
     * @return 是否支持
     */
    default boolean supports(MessageExt message) {
        String topic = message.getTopic();
        String tag = message.getTags();
        
        boolean topicMatch = getSupportedTopic().equals("*") || getSupportedTopic().equals(topic);
        boolean tagMatch = getSupportedTag().equals("*") || getSupportedTag().equals(tag);
        
        return topicMatch && tagMatch;
    }

    /**
     * 处理失败时的回调
     * 
     * @param message 消息
     * @param throwable 异常
     */
    default void onFailure(MessageExt message, Throwable throwable) {
        // 默认实现：记录日志
        System.err.println("Message handling failed: " + throwable.getMessage());
    }

    /**
     * 处理成功时的回调
     * 
     * @param message 消息
     */
    default void onSuccess(MessageExt message) {
        // 默认实现：记录日志
        System.out.println("Message handled successfully: " + message.getMsgId());
    }
}