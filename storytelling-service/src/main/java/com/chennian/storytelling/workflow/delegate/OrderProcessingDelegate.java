package com.chennian.storytelling.workflow.delegate;

import org.flowable.engine.delegate.DelegateExecution;
import org.flowable.engine.delegate.JavaDelegate;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

/**
 * 订单处理委托类
 * 
 * @author chennian
 */
@Component
public class OrderProcessingDelegate implements JavaDelegate {

    private static final Logger logger = LoggerFactory.getLogger(OrderProcessingDelegate.class);

    @Override
    public void execute(DelegateExecution execution) {
        // 获取流程变量
        String businessKey = (String) execution.getVariable("businessKey");
        String businessType = (String) execution.getVariable("businessType");
        Double orderAmount = (Double) execution.getVariable("orderAmount");
        
        logger.info("处理订单: {}, 业务类型: {}, 订单金额: {}", businessKey, businessType, orderAmount);
        
        // 这里可以调用销售订单服务进行实际的订单处理
        // 例如：更新订单状态、生成发货单等
        
        // 设置流程变量，标记订单处理完成
        execution.setVariable("orderProcessed", true);
        execution.setVariable("processTime", System.currentTimeMillis());
    }
}