package com.chennian.storytelling.service.config;

import org.springframework.amqp.core.*;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
import org.springframework.amqp.support.converter.MessageConverter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * RabbitMQ配置类
 * 定义交换机、队列及其绑定关系
 */
@Configuration
public class RabbitMQConfig {

    // 定义交换机名称
    public static final String ORDER_EXCHANGE = "order.exchange";
    public static final String EMAIL_EXCHANGE = "email.exchange";
    public static final String STATISTICS_EXCHANGE = "statistics.exchange";
    
    // 定义队列名称
    public static final String ORDER_PROCESSING_QUEUE = "order.processing.queue";
    public static final String EMAIL_NOTIFICATION_QUEUE = "email.notification.queue";
    public static final String DATA_STATISTICS_QUEUE = "data.statistics.queue";
    
    // 定义路由键
    public static final String ORDER_ROUTING_KEY = "order.processing";
    public static final String EMAIL_ROUTING_KEY = "email.notification";
    public static final String STATISTICS_ROUTING_KEY = "data.statistics";
    
    /**
     * 订单处理交换机
     */
    @Bean
    public DirectExchange orderExchange() {
        return new DirectExchange(ORDER_EXCHANGE);
    }
    
    /**
     * 邮件通知交换机
     */
    @Bean
    public DirectExchange emailExchange() {
        return new DirectExchange(EMAIL_EXCHANGE);
    }
    
    /**
     * 数据统计交换机
     */
    @Bean
    public DirectExchange statisticsExchange() {
        return new DirectExchange(STATISTICS_EXCHANGE);
    }
    
    /**
     * 订单处理队列
     */
    @Bean
    public Queue orderProcessingQueue() {
        return QueueBuilder.durable(ORDER_PROCESSING_QUEUE).build();
    }
    
    /**
     * 邮件通知队列
     */
    @Bean
    public Queue emailNotificationQueue() {
        return QueueBuilder.durable(EMAIL_NOTIFICATION_QUEUE).build();
    }
    
    /**
     * 数据统计队列
     */
    @Bean
    public Queue dataStatisticsQueue() {
        return QueueBuilder.durable(DATA_STATISTICS_QUEUE).build();
    }
    
    /**
     * 将订单处理队列绑定到订单交换机
     */
    @Bean
    public Binding orderBinding() {
        return BindingBuilder.bind(orderProcessingQueue()).to(orderExchange()).with(ORDER_ROUTING_KEY);
    }
    
    /**
     * 将邮件通知队列绑定到邮件交换机
     */
    @Bean
    public Binding emailBinding() {
        return BindingBuilder.bind(emailNotificationQueue()).to(emailExchange()).with(EMAIL_ROUTING_KEY);
    }
    
    /**
     * 将数据统计队列绑定到统计交换机
     */
    @Bean
    public Binding statisticsBinding() {
        return BindingBuilder.bind(dataStatisticsQueue()).to(statisticsExchange()).with(STATISTICS_ROUTING_KEY);
    }
    
    /**
     * 消息转换器，使用JSON格式
     */
    @Bean
    public MessageConverter jsonMessageConverter() {
        return new Jackson2JsonMessageConverter();
    }
    
    /**
     * 配置RabbitTemplate
     */
    @Bean
    public RabbitTemplate rabbitTemplate(ConnectionFactory connectionFactory) {
        RabbitTemplate rabbitTemplate = new RabbitTemplate(connectionFactory);
        rabbitTemplate.setMessageConverter(jsonMessageConverter());
        return rabbitTemplate;
    }
}