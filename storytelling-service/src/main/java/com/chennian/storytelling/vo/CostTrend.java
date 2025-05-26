package com.chennian.storytelling.vo;

import lombok.Data;

/**
 * 成本趋势
 * @author chen
 */
@Data
public class CostTrend {
    /**
     * 期间
     */
    private String period;

    /**
     * 总成本
     */
    private Double totalCost;

    /**
     * 直接材料成本
     */
    private Double directMaterialCost;

    /**
     * 直接人工成本
     */
    private Double directLaborCost;

    /**
     * 制造费用
     */
    private Double manufacturingOverheadCost;

    /**
     * 单位成本
     */
    private Double unitCost;

    /**
     * 生产数量
     */
    private Integer productionVolume;

    /**
     * 上月成本
     */
    private Double lastMonthCost;

    /**
     * 环比变动
     */
    private String monthOnMonthChange;

    /**
     * 去年同期成本
     */
    private Double lastYearCost;

    /**
     * 同比变动
     */
    private String yearOnYearChange;
}
