package com.chennian.storytelling.vo;

import lombok.Data;

import java.util.Map;

/**
 * 资产负债表VO
 * @author chen
 * @date 2023/6/15
 */
@Data
public class BalanceSheetVO {
    /**
     * 期间
     */
    private String period;
    
    /**
     * 资产
     */
    private Assets assets;
    
    /**
     * 负债
     */
    private Liabilities liabilities;
    
    /**
     * 所有者权益
     */
    private Equity equity;
    
    /**
     * 资产类
     */
    @Data
    public static class Assets {
        /**
         * 流动资产
         */
        private CurrentAssets currentAssets;
        
        /**
         * 非流动资产
         */
        private NonCurrentAssets nonCurrentAssets;
        
        /**
         * 资产总计
         */
        private Double totalAssets;
    }
    
    /**
     * 流动资产类
     */
    @Data
    public static class CurrentAssets {
        /**
         * 货币资金
         */
        private Double cash;
        
        /**
         * 应收账款
         */
        private Double accountsReceivable;
        
        /**
         * 预付款项
         */
        private Double prepayments;
        
        /**
         * 存货
         */
        private Double inventory;
        
        /**
         * 其他流动资产
         */
        private Double otherCurrentAssets;
        
        /**
         * 流动资产合计
         */
        private Double totalCurrentAssets;
    }
    
    /**
     * 非流动资产类
     */
    @Data
    public static class NonCurrentAssets {
        /**
         * 固定资产
         */
        private Double fixedAssets;
        
        /**
         * 无形资产
         */
        private Double intangibleAssets;
        
        /**
         * 长期股权投资
         */
        private Double longTermInvestments;
        
        /**
         * 递延所得税资产
         */
        private Double deferredTaxAssets;
        
        /**
         * 其他非流动资产
         */
        private Double otherNonCurrentAssets;
        
        /**
         * 非流动资产合计
         */
        private Double totalNonCurrentAssets;
    }
    
    /**
     * 负债类
     */
    @Data
    public static class Liabilities {
        /**
         * 流动负债
         */
        private CurrentLiabilities currentLiabilities;
        
        /**
         * 非流动负债
         */
        private NonCurrentLiabilities nonCurrentLiabilities;
        
        /**
         * 负债合计
         */
        private Double totalLiabilities;
    }
    
    /**
     * 流动负债类
     */
    @Data
    public static class CurrentLiabilities {
        /**
         * 短期借款
         */
        private Double shortTermLoans;
        
        /**
         * 应付账款
         */
        private Double accountsPayable;
        
        /**
         * 预收款项
         */
        private Double advanceFromCustomers;
        
        /**
         * 应付职工薪酬
         */
        private Double employeeBenefitsPayable;
        
        /**
         * 应交税费
         */
        private Double taxPayable;
        
        /**
         * 其他流动负债
         */
        private Double otherCurrentLiabilities;
        
        /**
         * 流动负债合计
         */
        private Double totalCurrentLiabilities;
    }
    
    /**
     * 非流动负债类
     */
    @Data
    public static class NonCurrentLiabilities {
        /**
         * 长期借款
         */
        private Double longTermLoans;
        
        /**
         * 递延所得税负债
         */
        private Double deferredTaxLiabilities;
        
        /**
         * 其他非流动负债
         */
        private Double otherNonCurrentLiabilities;
        
        /**
         * 非流动负债合计
         */
        private Double totalNonCurrentLiabilities;
    }
    
    /**
     * 所有者权益类
     */
    @Data
    public static class Equity {
        /**
         * 实收资本
         */
        private Double paidInCapital;
        
        /**
         * 资本公积
         */
        private Double capitalReserve;
        
        /**
         * 盈余公积
         */
        private Double surplusReserve;
        
        /**
         * 未分配利润
         */
        private Double retainedEarnings;
        
        /**
         * 所有者权益合计
         */
        private Double totalEquity;
    }
}