package com.chennian.storytelling.vo;

import lombok.Data;

import java.util.Date;
import java.util.List;

/**
 * 产品成本分析VO
 *
 * @author chen
 * @date 2023/6/15
 */
@Data
public class ProductCostAnalysisVO {
    /**
     * 产品ID
     */
    private Long productId;

    /**
     * 产品编码
     */
    private String productCode;

    /**
     * 产品名称
     */
    private String productName;

    /**
     * 产品类别
     */
    private String productCategory;

    /**
     * 计量单位
     */
    private String unitOfMeasure;

    /**
     * 状态
     */
    private String status;

    /**
     * 上市日期
     */
    private Date launchDate;

    /**
     * 成本汇总
     */
    private CostSummary costSummary;

    /**
     * 成本构成
     */
    private List<CostComposition> costComposition;

    /**
     * 成本趋势
     */
    private List<CostTrend> costTrends;

    /**
     * 成本优化建议
     */
    private List<CostOptimization> costOptimizations;
}
