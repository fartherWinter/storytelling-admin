package com.chennian.storytelling.bean.vo;

import lombok.Data;
import java.util.List;
import java.util.Map;

/**
 * 基于活动的成本分析VO
 * @author chen
 * @date 2023/6/15
 */
@Data
public class ActivityBasedCostAnalysisVO {
    /**
     * 活动成本池
     */
    private List<ActivityCostPool> activityCostPool;
    
    /**
     * 成本动因
     */
    private List<CostDriver> costDrivers;
    
    /**
     * 单位成本动因率
     */
    private List<UnitCostDriverRate> unitCostDriverRate;
    
    /**
     * 产品ABC成本分析
     */
    private List<ProductABCAnalysis> productABCAnalysis;
    
    /**
     * 产品ABC成本
     */
    private Map<String, ProductABC> productABC;
    
    /**
     * 活动价值分析
     */
    private List<ActivityValueAnalysis> activityValueAnalysis;
    
    /**
     * 活动成本池
     */
    @Data
    public static class ActivityCostPool {
        /**
         * 活动ID
         */
        private Long activityId;
        
        /**
         * 活动名称
         */
        private String activityName;
        
        /**
         * 成本金额
         */
        private Double costAmount;
    }
    
    /**
     * 成本动因
     */
    @Data
    public static class CostDriver {
        /**
         * 动因ID
         */
        private Long driverId;
        
        /**
         * 动因名称
         */
        private String driverName;
        
        /**
         * 动因单位
         */
        private String driverUnit;
        
        /**
         * 动因数量
         */
        private Integer driverQuantity;
    }
    
    /**
     * 单位成本动因率
     */
    @Data
    public static class UnitCostDriverRate {
        /**
         * 活动ID
         */
        private Long activityId;
        
        /**
         * 活动名称
         */
        private String activityName;
        
        /**
         * 动因ID
         */
        private Long driverId;
        
        /**
         * 动因名称
         */
        private String driverName;
        
        /**
         * 费率
         */
        private Double rate;
    }
    
    /**
     * 产品ABC成本分析
     */
    @Data
    public static class ProductABCAnalysis {
        /**
         * 产品ID
         */
        private Long productId;
        
        /**
         * 产品名称
         */
        private String productName;
        
        /**
         * 活动ID
         */
        private Long activityId;
        
        /**
         * 活动名称
         */
        private String activityName;
        
        /**
         * 动因数量
         */
        private Integer driverQuantity;
        
        /**
         * 活动成本
         */
        private Double activityCost;
        
        /**
         * 传统成本
         */
        private Double traditionalCost;
        
        /**
         * 成本差异
         */
        private Double costDifference;
        
        /**
         * 成本差异百分比
         */
        private Double costDifferencePercentage;
    }
    
    /**
     * 活动价值分析
     */
    @Data
    public static class ActivityValueAnalysis {
        /**
         * 活动ID
         */
        private Long activityId;
        
        /**
         * 活动名称
         */
        private String activityName;
        
        /**
         * 成本金额
         */
        private Double costAmount;
        
        /**
         * 增值百分比
         */
        private Double valueAddedPercentage;
        
        /**
         * 非增值百分比
         */
        private Double nonValueAddedPercentage;
        
        /**
         * 价值分类
         */
        private String valueClassification;
        
        /**
         * 改进潜力
         */
        private String improvementPotential;
    }
    
    /**
     * 产品ABC成本
     */
    @Data
    public static class ProductABC {
        /**
         * 产品名称
         */
        private String productName;
        
        /**
         * 传统成本
         */
        private Double traditionalCost;
        
        /**
         * ABC成本
         */
        private Double abcCost;
        
        /**
         * 成本差异
         */
        private Double costVariance;
        
        /**
         * 差异率
         */
        private String varianceRate;
    }
}