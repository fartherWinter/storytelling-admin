package com.chennian.storytelling.common.mq;

import com.alibaba.fastjson.JSON;
import org.apache.rocketmq.client.producer.DefaultMQProducer;
import org.apache.rocketmq.client.producer.SendCallback;
import org.apache.rocketmq.client.producer.SendResult;
import org.apache.rocketmq.common.message.Message;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;

import java.nio.charset.StandardCharsets;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

/**
 * 异步消息处理器
 * 提供统一的异步消息发送和处理能力
 * 
 * @author chennian
 * @since 2024-01-01
 */
@Component
public class AsyncMessageProcessor {

    private static final Logger logger = LoggerFactory.getLogger(AsyncMessageProcessor.class);

    @Autowired
    private DefaultMQProducer defaultMQProducer;

    @Autowired
    @Qualifier("paymentProducer")
    private DefaultMQProducer paymentProducer;

    @Autowired
    @Qualifier("orderProducer")
    private DefaultMQProducer orderProducer;

    @Autowired
    @Qualifier("userProducer")
    private DefaultMQProducer userProducer;

    @Autowired
    @Qualifier("asyncProcessorThreadPool")
    private ThreadPoolExecutor asyncProcessorThreadPool;

    @Autowired
    @Qualifier("paymentAsyncThreadPool")
    private ThreadPoolExecutor paymentAsyncThreadPool;

    @Autowired
    @Qualifier("orderAsyncThreadPool")
    private ThreadPoolExecutor orderAsyncThreadPool;

    /**
     * 异步发送消息
     * 
     * @param topic 主题
     * @param tag 标签
     * @param message 消息内容
     * @param keys 消息键
     * @return CompletableFuture<SendResult>
     */
    public CompletableFuture<SendResult> sendMessageAsync(String topic, String tag, Object message, String keys) {
        CompletableFuture<SendResult> future = new CompletableFuture<>();
        
        try {
            String messageBody = JSON.toJSONString(message);
            Message msg = new Message(topic, tag, keys, messageBody.getBytes(StandardCharsets.UTF_8));
            
            defaultMQProducer.send(msg, new SendCallback() {
                @Override
                public void onSuccess(SendResult sendResult) {
                    logger.info("Message sent successfully: topic={}, tag={}, keys={}, msgId={}", 
                               topic, tag, keys, sendResult.getMsgId());
                    future.complete(sendResult);
                }

                @Override
                public void onException(Throwable e) {
                    logger.error("Failed to send message: topic={}, tag={}, keys={}", topic, tag, keys, e);
                    future.completeExceptionally(e);
                }
            });
        } catch (Exception e) {
            logger.error("Error sending message: topic={}, tag={}, keys={}", topic, tag, keys, e);
            future.completeExceptionally(e);
        }
        
        return future;
    }

    /**
     * 发送支付相关消息
     * 
     * @param tag 标签
     * @param message 消息内容
     * @param keys 消息键
     * @return CompletableFuture<SendResult>
     */
    public CompletableFuture<SendResult> sendPaymentMessage(String tag, Object message, String keys) {
        return sendMessageWithProducer(paymentProducer, MessageQueueConfig.Topics.PAYMENT_TOPIC, tag, message, keys);
    }

    /**
     * 发送订单相关消息
     * 
     * @param tag 标签
     * @param message 消息内容
     * @param keys 消息键
     * @return CompletableFuture<SendResult>
     */
    public CompletableFuture<SendResult> sendOrderMessage(String tag, Object message, String keys) {
        return sendMessageWithProducer(orderProducer, MessageQueueConfig.Topics.ORDER_TOPIC, tag, message, keys);
    }

    /**
     * 发送用户相关消息
     * 
     * @param tag 标签
     * @param message 消息内容
     * @param keys 消息键
     * @return CompletableFuture<SendResult>
     */
    public CompletableFuture<SendResult> sendUserMessage(String tag, Object message, String keys) {
        return sendMessageWithProducer(userProducer, MessageQueueConfig.Topics.USER_TOPIC, tag, message, keys);
    }

    /**
     * 使用指定生产者发送消息
     */
    private CompletableFuture<SendResult> sendMessageWithProducer(DefaultMQProducer producer, String topic, 
                                                                  String tag, Object message, String keys) {
        CompletableFuture<SendResult> future = new CompletableFuture<>();
        
        try {
            String messageBody = JSON.toJSONString(message);
            Message msg = new Message(topic, tag, keys, messageBody.getBytes(StandardCharsets.UTF_8));
            
            producer.send(msg, new SendCallback() {
                @Override
                public void onSuccess(SendResult sendResult) {
                    logger.info("Message sent successfully: topic={}, tag={}, keys={}, msgId={}", 
                               topic, tag, keys, sendResult.getMsgId());
                    future.complete(sendResult);
                }

                @Override
                public void onException(Throwable e) {
                    logger.error("Failed to send message: topic={}, tag={}, keys={}", topic, tag, keys, e);
                    future.completeExceptionally(e);
                }
            });
        } catch (Exception e) {
            logger.error("Error sending message: topic={}, tag={}, keys={}", topic, tag, keys, e);
            future.completeExceptionally(e);
        }
        
        return future;
    }

    /**
     * 异步处理任务
     * 
     * @param task 任务
     * @return CompletableFuture<Void>
     */
    public CompletableFuture<Void> processAsync(Runnable task) {
        return CompletableFuture.runAsync(task, asyncProcessorThreadPool)
                .exceptionally(throwable -> {
                    logger.error("Async task execution failed", throwable);
                    return null;
                });
    }

    /**
     * 异步处理支付相关任务
     * 
     * @param task 任务
     * @return CompletableFuture<Void>
     */
    public CompletableFuture<Void> processPaymentAsync(Runnable task) {
        return CompletableFuture.runAsync(task, paymentAsyncThreadPool)
                .exceptionally(throwable -> {
                    logger.error("Payment async task execution failed", throwable);
                    return null;
                });
    }

    /**
     * 异步处理订单相关任务
     * 
     * @param task 任务
     * @return CompletableFuture<Void>
     */
    public CompletableFuture<Void> processOrderAsync(Runnable task) {
        return CompletableFuture.runAsync(task, orderAsyncThreadPool)
                .exceptionally(throwable -> {
                    logger.error("Order async task execution failed", throwable);
                    return null;
                });
    }

    /**
     * 异步处理带返回值的任务
     * 
     * @param task 任务
     * @param <T> 返回值类型
     * @return CompletableFuture<T>
     */
    public <T> CompletableFuture<T> processAsyncWithResult(java.util.function.Supplier<T> task) {
        return CompletableFuture.supplyAsync(task, asyncProcessorThreadPool)
                .exceptionally(throwable -> {
                    logger.error("Async task with result execution failed", throwable);
                    return null;
                });
    }

    /**
     * 批量异步处理任务
     * 
     * @param tasks 任务列表
     * @return CompletableFuture<Void>
     */
    public CompletableFuture<Void> processBatchAsync(java.util.List<Runnable> tasks) {
        CompletableFuture<?>[] futures = tasks.stream()
                .map(task -> CompletableFuture.runAsync(task, asyncProcessorThreadPool))
                .toArray(CompletableFuture[]::new);
        
        return CompletableFuture.allOf(futures)
                .exceptionally(throwable -> {
                    logger.error("Batch async tasks execution failed", throwable);
                    return null;
                });
    }

    /**
     * 延迟异步处理任务
     * 
     * @param task 任务
     * @param delay 延迟时间
     * @param timeUnit 时间单位
     * @return CompletableFuture<Void>
     */
    public CompletableFuture<Void> processAsyncWithDelay(Runnable task, long delay, TimeUnit timeUnit) {
        return CompletableFuture.runAsync(() -> {
            try {
                timeUnit.sleep(delay);
                task.run();
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
                logger.error("Delayed async task was interrupted", e);
            }
        }, asyncProcessorThreadPool)
        .exceptionally(throwable -> {
            logger.error("Delayed async task execution failed", throwable);
            return null;
        });
    }

    /**
     * 获取线程池状态信息
     * 
     * @return 线程池状态
     */
    public ThreadPoolStatus getThreadPoolStatus() {
        return new ThreadPoolStatus(
                asyncProcessorThreadPool.getActiveCount(),
                asyncProcessorThreadPool.getCorePoolSize(),
                asyncProcessorThreadPool.getMaximumPoolSize(),
                asyncProcessorThreadPool.getPoolSize(),
                asyncProcessorThreadPool.getQueue().size(),
                asyncProcessorThreadPool.getCompletedTaskCount(),
                asyncProcessorThreadPool.getTaskCount()
        );
    }

    /**
     * 线程池状态信息
     */
    public static class ThreadPoolStatus {
        private final int activeCount;
        private final int corePoolSize;
        private final int maximumPoolSize;
        private final int poolSize;
        private final int queueSize;
        private final long completedTaskCount;
        private final long taskCount;

        public ThreadPoolStatus(int activeCount, int corePoolSize, int maximumPoolSize, 
                               int poolSize, int queueSize, long completedTaskCount, long taskCount) {
            this.activeCount = activeCount;
            this.corePoolSize = corePoolSize;
            this.maximumPoolSize = maximumPoolSize;
            this.poolSize = poolSize;
            this.queueSize = queueSize;
            this.completedTaskCount = completedTaskCount;
            this.taskCount = taskCount;
        }

        // Getters
        public int getActiveCount() { return activeCount; }
        public int getCorePoolSize() { return corePoolSize; }
        public int getMaximumPoolSize() { return maximumPoolSize; }
        public int getPoolSize() { return poolSize; }
        public int getQueueSize() { return queueSize; }
        public long getCompletedTaskCount() { return completedTaskCount; }
        public long getTaskCount() { return taskCount; }

        @Override
        public String toString() {
            return String.format("ThreadPoolStatus{activeCount=%d, corePoolSize=%d, maximumPoolSize=%d, " +
                               "poolSize=%d, queueSize=%d, completedTaskCount=%d, taskCount=%d}",
                               activeCount, corePoolSize, maximumPoolSize, poolSize, queueSize, 
                               completedTaskCount, taskCount);
        }
    }
}