package com.chennian.storytelling.bean.vo.workflow;

import lombok.Data;

import java.math.BigDecimal;

/**
 * @author by chennian
 * @date 2025/5/19.
 */
@Data

public class SupplierDistribution {
    /**
     * 供应商名称
     */
    private String supplierName;
    /**
     * 供应商应付账款
     */
    private BigDecimal amount;
}
