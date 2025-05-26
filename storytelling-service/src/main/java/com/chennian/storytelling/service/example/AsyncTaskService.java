package com.chennian.storytelling.service.example;

import com.chennian.storytelling.service.config.RabbitMQConfig;
import com.chennian.storytelling.service.mq.producer.MessageProducer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * 异步任务服务示例
 * 演示如何将同步操作改造为使用RabbitMQ的异步处理方式
 */
@Service
public class AsyncTaskService {

    private static final Logger logger = LoggerFactory.getLogger(AsyncTaskService.class);

    @Autowired
    private MessageProducer messageProducer;

    /**
     * 处理订单（异步方式）
     * 将订单处理任务发送到消息队列，由消费者异步处理
     *
     * @param orderId 订单ID
     */
    public void processOrderAsync(Long orderId) {
        logger.info("接收到订单处理请求，订单ID: {}, 转为异步处理", orderId);
        
        // 发送订单处理消息到RabbitMQ队列
        messageProducer.sendOrderProcessingMessage(orderId);
        
        logger.info("订单处理请求已加入队列，订单ID: {}", orderId);
    }

    /**
     * 发送邮件通知（异步方式）
     * 将邮件发送任务提交到消息队列，由消费者异步处理
     *
     * @param to      收件人
     * @param subject 邮件主题
     * @param content 邮件内容
     */
    public void sendEmailAsync(String to, String subject, String content) {
        logger.info("接收到邮件发送请求，收件人: {}, 主题: {}, 转为异步处理", to, subject);
        
        // 构建邮件消息对象
        EmailMessage emailMessage = new EmailMessage(to, subject, content);
        
        // 发送邮件通知消息到RabbitMQ队列
        messageProducer.sendEmailNotification(emailMessage);
        
        logger.info("邮件发送请求已加入队列，收件人: {}", to);
    }

    /**
     * 执行数据统计分析（异步方式）
     * 将数据统计任务提交到消息队列，由消费者异步处理
     *
     * @param startDate 开始日期
     * @param endDate   结束日期
     * @param type      统计类型
     */
    public void runDataStatisticsAsync(String startDate, String endDate, String type) {
        logger.info("接收到数据统计请求，时间范围: {} 至 {}, 类型: {}, 转为异步处理", startDate, endDate, type);
        
        // 构建统计任务对象
        StatisticsTask task = new StatisticsTask(startDate, endDate, type);
        
        // 发送数据统计任务到RabbitMQ队列
        messageProducer.sendDataStatisticsTask(task);
        
        logger.info("数据统计请求已加入队列，类型: {}", type);
    }

    /**
     * 邮件消息内部类
     */
    public static class EmailMessage {
        private String to;
        private String subject;
        private String content;

        public EmailMessage(String to, String subject, String content) {
            this.to = to;
            this.subject = subject;
            this.content = content;
        }

        public String getTo() {
            return to;
        }

        public String getSubject() {
            return subject;
        }

        public String getContent() {
            return content;
        }

        @Override
        public String toString() {
            return "EmailMessage{" +
                    "to='" + to + '\'' +
                    ", subject='" + subject + '\'' +
                    ", contentLength=" + (content != null ? content.length() : 0) +
                    '}';
        }
    }

    /**
     * 统计任务内部类
     */
    public static class StatisticsTask {
        private String startDate;
        private String endDate;
        private String type;

        public StatisticsTask(String startDate, String endDate, String type) {
            this.startDate = startDate;
            this.endDate = endDate;
            this.type = type;
        }

        public String getStartDate() {
            return startDate;
        }

        public String getEndDate() {
            return endDate;
        }

        public String getType() {
            return type;
        }

        @Override
        public String toString() {
            return "StatisticsTask{" +
                    "startDate='" + startDate + '\'' +
                    ", endDate='" + endDate + '\'' +
                    ", type='" + type + '\'' +
                    '}';
        }
    }
}