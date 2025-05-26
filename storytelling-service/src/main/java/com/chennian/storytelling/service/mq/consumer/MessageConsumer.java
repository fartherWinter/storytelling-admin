package com.chennian.storytelling.service.mq.consumer;

import com.chennian.storytelling.service.common.EmailService;
import com.chennian.storytelling.service.config.RabbitMQConfig;
import com.chennian.storytelling.service.example.AsyncTaskService;
import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

/**
 * 消息消费者服务
 * 负责处理从RabbitMQ队列接收的异步任务消息
 */
@Component
public class MessageConsumer {

    @Autowired
    private EmailService emailService;

    private static final Logger logger = LoggerFactory.getLogger(MessageConsumer.class);

    /**
     * 处理订单相关的异步任务
     *
     * @param orderId 订单ID
     */
    @RabbitListener(queues = RabbitMQConfig.ORDER_PROCESSING_QUEUE)
    public void processOrderTask(Long orderId) {
        try {
            logger.info("接收到订单处理任务, 订单ID: {}", orderId);
            // 模拟订单处理逻辑
            logger.info("开始更新订单状态, 订单ID: {}", orderId);
            // 实际的订单状态更新代码
            Thread.sleep(100); // 模拟耗时操作
            logger.info("订单状态更新完成, 订单ID: {}", orderId);

            logger.info("开始处理库存, 订单ID: {}", orderId);
            // 实际的库存处理代码
            Thread.sleep(150); // 模拟耗时操作
            logger.info("库存处理完成, 订单ID: {}", orderId);

            logger.info("开始生成发票, 订单ID: {}", orderId);
            // 实际的发票生成代码
            Thread.sleep(200); // 模拟耗时操作
            logger.info("发票生成完成, 订单ID: {}", orderId);
            
            logger.info("订单处理任务完成, 订单ID: {}", orderId);
        } catch (Exception e) {
            logger.error("处理订单任务时发生错误, 订单ID: {}, 错误信息: {}", orderId, e.getMessage(), e);
        }
    }

    /**
     * 处理邮件通知相关的异步任务
     *
     * @param emailMessage 邮件消息对象
     */
    @RabbitListener(queues = RabbitMQConfig.EMAIL_NOTIFICATION_QUEUE)
    public void processEmailNotification(AsyncTaskService.EmailMessage emailMessage) {
        try {
            logger.info("接收到邮件通知任务: 收件人: {}, 主题: {}", emailMessage.getTo(), emailMessage.getSubject());
            // 1. 构建邮件内容 (已在消息中)
            String to = emailMessage.getTo();
            String subject = emailMessage.getSubject();
            String content = emailMessage.getContent();
            // 2. 调用邮件服务发送邮件
            emailService.sendSimpleMail(to, subject, content);
           
            logger.info("邮件通知任务完成: {}", emailMessage);
        } catch (Exception e) {
            logger.error("处理邮件通知任务时发生错误: {}, 错误信息: {}", emailMessage, e.getMessage(), e);
        }
    }

    /**
     * 处理数据统计相关的异步任务
     *
     * @param statisticsTask 统计任务对象
     */
    @RabbitListener(queues = RabbitMQConfig.DATA_STATISTICS_QUEUE)
    public void processDataStatistics(Object statisticsTask) {
        try {
            logger.info("接收到数据统计任务: {}", statisticsTask);
            // TODO: 实现数据统计逻辑
            // 1. 收集数据
            // 2. 执行统计分析
            // 3. 生成报表
            
            logger.info("数据统计任务完成: {}", statisticsTask);
        } catch (Exception e) {
            logger.error("处理数据统计任务时发生错误: {}, 错误信息: {}", statisticsTask, e.getMessage(), e);
        }
    }
}