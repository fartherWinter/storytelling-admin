package com.chennian.storytelling.bean.vo.workflow;

import lombok.Data;

import java.math.BigDecimal;

/**
 * @author by chennian
 * @date 2025/5/19.
 */
@Data
public class PaymentPlan {
    /**
     * 获取付款计划日期区间
     */
    private String paymentPeriod;
    /**
     * 获取付款计划金额
     */
    private BigDecimal amount;
}
