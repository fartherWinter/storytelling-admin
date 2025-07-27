package com.chennian.storytelling.common.mq;

import org.apache.rocketmq.client.consumer.DefaultMQPushConsumer;
import org.apache.rocketmq.client.consumer.listener.ConsumeConcurrentlyContext;
import org.apache.rocketmq.client.consumer.listener.ConsumeConcurrentlyStatus;
import org.apache.rocketmq.client.consumer.listener.MessageListenerConcurrently;
import org.apache.rocketmq.client.exception.MQClientException;
import org.apache.rocketmq.client.producer.DefaultMQProducer;
import org.apache.rocketmq.common.message.MessageExt;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;

import javax.annotation.PreDestroy;
import java.util.List;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * 消息队列配置
 * 支持RocketMQ的生产者和消费者配置
 * 
 * @author chennian
 * @since 2024-01-01
 */
@Configuration
public class MessageQueueConfig {

    @Value("${rocketmq.name-server:localhost:9876}")
    private String nameServer;

    @Value("${rocketmq.producer.group:storytelling_producer_group}")
    private String producerGroup;

    @Value("${rocketmq.consumer.group:storytelling_consumer_group}")
    private String consumerGroup;

    @Value("${rocketmq.producer.send-msg-timeout:3000}")
    private int sendMsgTimeout;

    @Value("${rocketmq.producer.retry-times:2}")
    private int retryTimes;

    @Value("${rocketmq.consumer.consume-thread-min:20}")
    private int consumeThreadMin;

    @Value("${rocketmq.consumer.consume-thread-max:64}")
    private int consumeThreadMax;

    /**
     * RocketMQ生产者配置
     */
    @Bean
    @Primary
    public DefaultMQProducer defaultMQProducer() throws MQClientException {
        DefaultMQProducer producer = new DefaultMQProducer();
        producer.setProducerGroup(producerGroup);
        producer.setNamesrvAddr(nameServer);
        producer.setSendMsgTimeout(sendMsgTimeout);
        producer.setRetryTimesWhenSendFailed(retryTimes);
        producer.setRetryTimesWhenSendAsyncFailed(retryTimes);
        producer.setMaxMessageSize(4 * 1024 * 1024); // 4MB
        producer.setCompressMsgBodyOverHowmuch(4096); // 4KB
        producer.start();
        return producer;
    }

    /**
     * 支付相关消息生产者
     */
    @Bean("paymentProducer")
    public DefaultMQProducer paymentProducer() throws MQClientException {
        DefaultMQProducer producer = new DefaultMQProducer();
        producer.setProducerGroup("payment_producer_group");
        producer.setNamesrvAddr(nameServer);
        producer.setSendMsgTimeout(sendMsgTimeout);
        producer.setRetryTimesWhenSendFailed(retryTimes);
        producer.start();
        return producer;
    }

    /**
     * 订单相关消息生产者
     */
    @Bean("orderProducer")
    public DefaultMQProducer orderProducer() throws MQClientException {
        DefaultMQProducer producer = new DefaultMQProducer();
        producer.setProducerGroup("order_producer_group");
        producer.setNamesrvAddr(nameServer);
        producer.setSendMsgTimeout(sendMsgTimeout);
        producer.setRetryTimesWhenSendFailed(retryTimes);
        producer.start();
        return producer;
    }

    /**
     * 用户相关消息生产者
     */
    @Bean("userProducer")
    public DefaultMQProducer userProducer() throws MQClientException {
        DefaultMQProducer producer = new DefaultMQProducer();
        producer.setProducerGroup("user_producer_group");
        producer.setNamesrvAddr(nameServer);
        producer.setSendMsgTimeout(sendMsgTimeout);
        producer.setRetryTimesWhenSendFailed(retryTimes);
        producer.start();
        return producer;
    }

    /**
     * 通用消息消费者
     */
    @Bean
    public DefaultMQPushConsumer defaultMQPushConsumer() throws MQClientException {
        DefaultMQPushConsumer consumer = new DefaultMQPushConsumer();
        consumer.setConsumerGroup(consumerGroup);
        consumer.setNamesrvAddr(nameServer);
        consumer.setConsumeThreadMin(consumeThreadMin);
        consumer.setConsumeThreadMax(consumeThreadMax);
        consumer.setConsumeMessageBatchMaxSize(1);
        
        // 订阅主题
        consumer.subscribe("PAYMENT_TOPIC", "*");
        consumer.subscribe("ORDER_TOPIC", "*");
        consumer.subscribe("USER_TOPIC", "*");
        consumer.subscribe("NOTIFICATION_TOPIC", "*");
        
        // 设置消息监听器
        consumer.setMessageListener(new MessageListenerConcurrently() {
            @Override
            public ConsumeConcurrentlyStatus consumeMessage(
                    List<MessageExt> messages,
                    ConsumeConcurrentlyContext context) {
                for (MessageExt message : messages) {
                    try {
                        // 处理消息的逻辑将在具体的消息处理器中实现
                        System.out.println("Received message: " + new String(message.getBody()));
                    } catch (Exception e) {
                        e.printStackTrace();
                        return ConsumeConcurrentlyStatus.RECONSUME_LATER;
                    }
                }
                return ConsumeConcurrentlyStatus.CONSUME_SUCCESS;
            }
        });
        
        consumer.start();
        return consumer;
    }

    /**
     * 异步处理线程池配置
     */
    @Bean("asyncProcessorThreadPool")
    public ThreadPoolExecutor asyncProcessorThreadPool() {
        return new ThreadPoolExecutor(
                10, // 核心线程数
                50, // 最大线程数
                60L, // 空闲线程存活时间
                TimeUnit.SECONDS,
                new LinkedBlockingQueue<>(1000), // 队列容量
                new ThreadFactory() {
                    private final AtomicInteger threadNumber = new AtomicInteger(1);
                    @Override
                    public Thread newThread(Runnable r) {
                        Thread t = new Thread(r, "async-processor-" + threadNumber.getAndIncrement());
                        t.setDaemon(false);
                        return t;
                    }
                },
                new ThreadPoolExecutor.CallerRunsPolicy() // 拒绝策略
        );
    }

    /**
     * 支付异步处理线程池
     */
    @Bean("paymentAsyncThreadPool")
    public ThreadPoolExecutor paymentAsyncThreadPool() {
        return new ThreadPoolExecutor(
                5,
                20,
                60L,
                TimeUnit.SECONDS,
                new LinkedBlockingQueue<>(500),
                new ThreadFactory() {
                    private final AtomicInteger threadNumber = new AtomicInteger(1);
                    @Override
                    public Thread newThread(Runnable r) {
                        Thread t = new Thread(r, "payment-async-" + threadNumber.getAndIncrement());
                        t.setDaemon(false);
                        return t;
                    }
                },
                new ThreadPoolExecutor.CallerRunsPolicy()
        );
    }

    /**
     * 订单异步处理线程池
     */
    @Bean("orderAsyncThreadPool")
    public ThreadPoolExecutor orderAsyncThreadPool() {
        return new ThreadPoolExecutor(
                5,
                20,
                60L,
                TimeUnit.SECONDS,
                new LinkedBlockingQueue<>(500),
                new ThreadFactory() {
                    private final AtomicInteger threadNumber = new AtomicInteger(1);
                    @Override
                    public Thread newThread(Runnable r) {
                        Thread t = new Thread(r, "order-async-" + threadNumber.getAndIncrement());
                        t.setDaemon(false);
                        return t;
                    }
                },
                new ThreadPoolExecutor.CallerRunsPolicy()
        );
    }

    /**
     * 消息主题常量
     */
    public static class Topics {
        public static final String PAYMENT_TOPIC = "PAYMENT_TOPIC";
        public static final String ORDER_TOPIC = "ORDER_TOPIC";
        public static final String USER_TOPIC = "USER_TOPIC";
        public static final String NOTIFICATION_TOPIC = "NOTIFICATION_TOPIC";
        public static final String INVENTORY_TOPIC = "INVENTORY_TOPIC";
    }

    /**
     * 消息标签常量
     */
    public static class Tags {
        // 支付相关标签
        public static final String PAYMENT_CREATED = "PAYMENT_CREATED";
        public static final String PAYMENT_SUCCESS = "PAYMENT_SUCCESS";
        public static final String PAYMENT_FAILED = "PAYMENT_FAILED";
        public static final String REFUND_APPLIED = "REFUND_APPLIED";
        public static final String REFUND_SUCCESS = "REFUND_SUCCESS";
        
        // 订单相关标签
        public static final String ORDER_CREATED = "ORDER_CREATED";
        public static final String ORDER_PAID = "ORDER_PAID";
        public static final String ORDER_CANCELLED = "ORDER_CANCELLED";
        public static final String ORDER_COMPLETED = "ORDER_COMPLETED";
        
        // 用户相关标签
        public static final String USER_REGISTERED = "USER_REGISTERED";
        public static final String USER_LOGIN = "USER_LOGIN";
        public static final String USER_PROFILE_UPDATED = "USER_PROFILE_UPDATED";
        
        // 通知相关标签
        public static final String SMS_NOTIFICATION = "SMS_NOTIFICATION";
        public static final String EMAIL_NOTIFICATION = "EMAIL_NOTIFICATION";
        public static final String PUSH_NOTIFICATION = "PUSH_NOTIFICATION";
    }

    @PreDestroy
    public void destroy() {
        // 在应用关闭时清理资源
    }
}