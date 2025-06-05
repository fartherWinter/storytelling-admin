package com.chennian.storytelling.service.mq.consumer;

import com.chennian.storytelling.bean.model.mall.MallOrder;
import com.chennian.storytelling.bean.model.mall.MallSubOrder;
import com.chennian.storytelling.service.common.EmailService;
import com.chennian.storytelling.service.config.RabbitMQConfig;

import java.util.List;
import com.chennian.storytelling.service.example.AsyncTaskService;
import com.chennian.storytelling.service.mall.MallOrderService;
import com.chennian.storytelling.service.mall.MallProductService;
import com.chennian.storytelling.service.mall.MallSubOrderService;
import com.chennian.storytelling.service.AccountsReceivableService;
import com.chennian.storytelling.common.enums.MallOrderStatus;
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
    
    @Autowired
    private MallOrderService mallOrderService;
    
    @Autowired
    private MallProductService mallProductService;
    
    @Autowired
    private MallSubOrderService mallSubOrderService;
    
    @Autowired
    private AccountsReceivableService accountsReceivableService;

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
            
            // 获取订单信息
            MallOrder order = mallOrderService.getById(orderId);
            if (order == null) {
                logger.error("订单不存在, 订单ID: {}", orderId);
                return;
            }
            
            // 1. 更新订单状态为待发货
            logger.info("开始更新订单状态, 订单ID: {}", orderId);
            
            int updateResult = mallOrderService.updateOrderStatus(orderId, MallOrderStatus.PENDING_DELIVERY.getCode());
            if (updateResult > 0) {
                logger.info("订单状态更新完成, 订单ID: {}", orderId);
            } else {
                logger.error("订单状态更新失败, 订单ID: {}", orderId);
                return;
            }

            // 2. 处理库存扣减 - 从子订单获取商品信息
            logger.info("开始处理库存扣减, 订单ID: {}", orderId);
            try {
                // 获取订单的所有子订单（商品明细）
                List<MallSubOrder> subOrders = mallSubOrderService.selectSubOrdersByOrderId(orderId);
                if (subOrders == null || subOrders.isEmpty()) {
                    logger.error("订单子订单为空, 订单ID: {}", orderId);
                    mallOrderService.updateOrderStatus(orderId, MallOrderStatus.PENDING_PAYMENT.getCode());
                    return;
                }
                
                // 逐个处理每个商品的库存扣减
                for (MallSubOrder subOrder : subOrders) {
                    Long productId = subOrder.getProductId();
                    Integer quantity = subOrder.getProductQuantity();
                    
                    logger.info("处理商品库存扣减, 订单ID: {}, 商品ID: {}, 商品名称: {}, 数量: {}", 
                               orderId, productId, subOrder.getProductName(), quantity);
                    
                    boolean stockResult = mallProductService.updateStock(productId, quantity) > 0;
                    if (!stockResult) {
                        logger.error("库存扣减失败, 订单ID: {}, 商品ID: {}, 商品名称: {}, 数量: {}", 
                                   orderId, productId, subOrder.getProductName(), quantity);
                        // 库存扣减失败，回滚订单状态
                        mallOrderService.updateOrderStatus(orderId, MallOrderStatus.PENDING_PAYMENT.getCode());
                        return;
                    }
                }
                logger.info("所有商品库存扣减完成, 订单ID: {}", orderId);
            } catch (Exception e) {
                logger.error("库存处理异常, 订单ID: {}", orderId, e);
                // 库存处理异常，回滚订单状态
                mallOrderService.updateOrderStatus(orderId, MallOrderStatus.PENDING_PAYMENT.getCode());
                return;
            }

            // 3. 生成应收账款记录（发票）
            logger.info("开始生成发票, 订单ID: {}", orderId);
            try {
                // 生成发票号
                String invoiceNo = "INV" + System.currentTimeMillis();
                
                // 记录应收账款
                if (order.getUserId() != null && order.getTotalAmount() != null) {
                    int invoiceResult = accountsReceivableService.recordReceivableCollection(
                            order.getUserId(), invoiceNo, order.getTotalAmount().doubleValue());
                    if (invoiceResult > 0) {
                        logger.info("发票生成完成, 订单ID: {}, 发票号: {}, 金额: {}", 
                                   orderId, invoiceNo, order.getTotalAmount());
                    } else {
                        logger.warn("发票生成失败, 订单ID: {}, 但不影响订单处理", orderId);
                    }
                }
            } catch (Exception e) {
                logger.error("发票生成异常, 订单ID: {}, 错误信息: {}", orderId, e.getMessage(), e);
                // 发票生成失败不影响订单处理，只记录日志
            }
            
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