package com.chennian.storytelling.bean.vo;

import lombok.Data;

/**
 * 成本构成
 * @author chen
 */
@Data
public class CostComposition {
    /**
     * 成本类型
     */
    private String costType;

    /**
     * 成本项目
     */
    private String costItem;

    /**
     * 金额
     */
    private Double amount;

    /**
     * 百分比
     */
    private Double percentage;

    /**
     * 标准金额
     */
    private Double standardAmount;

    /**
     * 差异
     */
    private Double variance;

    /**
     * 差异百分比
     */
    private Double variancePercentage;
}
