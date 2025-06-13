package com.chennian.storytelling.bean.vo;

import lombok.Data;

/**
 * 成本优化建议
 * @author chen
 */
@Data
public class CostOptimization {
    /**
     * 优化ID
     */
    private Long optimizationId;

    /**
     * 领域
     */
    private String area;

    /**
     * 建议
     */
    private String suggestion;

    /**
     * 潜在节约
     */
    private Double potentialSaving;

    /**
     * 实施难度
     */
    private String implementationDifficulty;

    /**
     * 优先级
     */
    private Integer priority;

    /**
     * 材料成本优化
     */
    private String materialCostOptimization;

    /**
     * 工艺优化
     */
    private String processOptimization;

    /**
     * 批量生产
     */
    private String batchProduction;

}
