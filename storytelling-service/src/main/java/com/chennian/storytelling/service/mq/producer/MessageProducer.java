package com.chennian.storytelling.service.mq.producer;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * 消息生产者服务
 * 负责将消息发送到指定的RabbitMQ交换机
 */
@Component
public class MessageProducer {

    private static final Logger logger = LoggerFactory.getLogger(MessageProducer.class);

    @Autowired
    private RabbitTemplate rabbitTemplate;

    /**
     * 发送消息到指定交换机
     *
     * @param exchange   交换机名称
     * @param routingKey 路由键
     * @param message    消息内容
     */
    public void sendMessage(String exchange, String routingKey, Object message) {
        logger.info("发送消息到交换机: {}, 路由键: {}, 消息内容: {}", exchange, routingKey, message);
        rabbitTemplate.convertAndSend(exchange, routingKey, message);
    }

    /**
     * 发送订单处理消息
     *
     * @param orderId 订单ID
     */
    public void sendOrderProcessingMessage(Long orderId) {
        logger.info("发送订单处理消息, 订单ID: {}", orderId);
        rabbitTemplate.convertAndSend(
                "order.exchange",
                "order.processing",
                orderId
        );
    }

    /**
     * 发送邮件通知消息
     *
     * @param emailMessage 邮件消息对象
     */
    public void sendEmailNotification(Object emailMessage) {
        logger.info("发送邮件通知消息: {}", emailMessage);
        rabbitTemplate.convertAndSend(
                "email.exchange",
                "email.notification",
                emailMessage
        );
    }

    /**
     * 发送数据统计任务消息
     *
     * @param statisticsTask 统计任务对象
     */
    public void sendDataStatisticsTask(Object statisticsTask) {
        logger.info("发送数据统计任务: {}", statisticsTask);
        rabbitTemplate.convertAndSend(
                "statistics.exchange",
                "data.statistics",
                statisticsTask
        );
    }
}