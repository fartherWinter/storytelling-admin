package com.chennian.storytelling.bean.vo;

import lombok.Data;

/**
 * 财务比率分析VO
 * @author chen
 * @date 2023/6/15
 */
@Data
public class FinancialRatioAnalysisVO {
    
    /**
     * 期间
     */
    private String period;
    
    /**
     * 盈利能力比率
     */
    private ProfitabilityRatiosVO profitabilityRatios;
    
    /**
     * 偿债能力比率
     */
    private SolvencyRatiosVO solvencyRatios;
    
    /**
     * 营运能力比率
     */
    private OperationRatiosVO operationRatios;
    
    /**
     * 发展能力比率
     */
    private GrowthRatiosVO growthRatios;
    
    /**
     * 现金流比率
     */
    private CashFlowRatiosVO cashFlowRatios;
    
    /**
     * 盈利能力比率VO
     */
    @Data
    public static class ProfitabilityRatiosVO {
        /**
         * 毛利率
         */
        private String grossProfitMargin;
        
        /**
         * 净利率
         */
        private String netProfitMargin;
        
        /**
         * 净资产收益率
         */
        private String returnOnEquity;
        
        /**
         * 总资产收益率
         */
        private String returnOnAssets;
        
        /**
         * 营业利润率
         */
        private String operatingProfitMargin;
    }
    
    /**
     * 偿债能力比率VO
     */
    @Data
    public static class SolvencyRatiosVO {
        /**
         * 资产负债率
         */
        private String debtToAssetRatio;
        
        /**
         * 流动比率
         */
        private Double currentRatio;
        
        /**
         * 速动比率
         */
        private Double quickRatio;
        
        /**
         * 利息保障倍数
         */
        private Double interestCoverageRatio;
    }
    
    /**
     * 营运能力比率VO
     */
    @Data
    public static class OperationRatiosVO {
        /**
         * 应收账款周转率
         */
        private Double accountsReceivableTurnover;
        
        /**
         * 存货周转率
         */
        private Double inventoryTurnover;
        
        /**
         * 总资产周转率
         */
        private Double totalAssetsTurnover;
        
        /**
         * 固定资产周转率
         */
        private Double fixedAssetsTurnover;
    }
    
    /**
     * 发展能力比率VO
     */
    @Data
    public static class GrowthRatiosVO {
        /**
         * 营业收入增长率
         */
        private String revenueGrowthRate;
        
        /**
         * 净利润增长率
         */
        private String netProfitGrowthRate;
        
        /**
         * 总资产增长率
         */
        private String totalAssetsGrowthRate;
        
        /**
         * 净资产增长率
         */
        private String netAssetsGrowthRate;
    }
    
    /**
     * 现金流比率VO
     */
    @Data
    public static class CashFlowRatiosVO {
        /**
         * 经营现金流量比率
         */
        private Double operatingCashFlowRatio;
        
        /**
         * 现金流量负债比率
         */
        private Double cashFlowToLiabilitiesRatio;
        
        /**
         * 现金流量净利润比率
         */
        private Double cashFlowToNetProfitRatio;
        
        /**
         * 全部资产现金回收率
         */
        private String totalAssetsCashRecoveryRate;
    }
}