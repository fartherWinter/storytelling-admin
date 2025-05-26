package com.chennian.storytelling.vo;

import lombok.Data;

import java.math.BigDecimal;

/**
 * 成本汇总
 * @author chen
 */
@Data
public class CostSummary {
    /**
     * 总成本
     */
    private BigDecimal totalCost;

    /**
     * 直接材料成本
     */
    private BigDecimal directMaterialCost;

    /**
     * 直接人工成本
     */
    private BigDecimal directLaborCost;

    /**
     * 制造费用
     */
    private BigDecimal manufacturingOverheadCost;

    /**
     * 非制造成本
     */
    private BigDecimal nonManufacturingCost;

    /**
     * 标准成本
     */
    private BigDecimal standardCost;

    /**
     * 实际成本
     */
    private BigDecimal actualCost;

    /**
     * 成本差异
     */
    private BigDecimal costVariance;

    /**
     * 成本差异百分比
     */
    private Double costVariancePercentage;

}
