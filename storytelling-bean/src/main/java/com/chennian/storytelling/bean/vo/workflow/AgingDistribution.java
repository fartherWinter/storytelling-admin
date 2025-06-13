package com.chennian.storytelling.bean.vo.workflow;

import lombok.Data;

import java.math.BigDecimal;

/**
 * @author by chennian
 * @date 2025/5/19.
 */
@Data
public class AgingDistribution {

    /**
     * 账龄区间分
     */
    private String agingPeriod;

    /**
     * 对应账龄区间内未付(Unpaid)账单的总金额
     */
    private BigDecimal amount;
}
