package com.chennian.storytelling.vo;

import lombok.Data;

/**
 * 现金流量表VO
 * @author chen
 * @date 2023/6/15
 */
@Data
public class CashFlowStatementVO {
    /**
     * 期间
     */
    private String period;
    
    /**
     * 经营活动现金流量
     */
    private OperatingActivities operatingActivities;
    
    /**
     * 投资活动现金流量
     */
    private InvestingActivities investingActivities;
    
    /**
     * 筹资活动现金流量
     */
    private FinancingActivities financingActivities;
    
    /**
     * 现金及现金等价物净增加额
     */
    private Double netIncreaseInCashAndCashEquivalents;
    
    /**
     * 期初现金及现金等价物余额
     */
    private Double initialCashAndCashEquivalents;
    
    /**
     * 期末现金及现金等价物余额
     */
    private Double finalCashAndCashEquivalents;
    
    /**
     * 经营活动现金流量类
     */
    @Data
    public static class OperatingActivities {
        /**
         * 销售商品、提供劳务收到的现金
         */
        private Double cashReceivedFromSales;
        
        /**
         * 收到的税费返还
         */
        private Double taxRefunds;
        
        /**
         * 收到其他与经营活动有关的现金
         */
        private Double otherCashReceivedRelatingToOperatingActivities;
        
        /**
         * 经营活动现金流入小计
         */
        private Double subtotalOfCashInflowsFromOperatingActivities;
        
        /**
         * 购买商品、接受劳务支付的现金
         */
        private Double cashPaidForGoods;
        
        /**
         * 支付给职工以及为职工支付的现金
         */
        private Double cashPaidToEmployees;
        
        /**
         * 支付的各项税费
         */
        private Double paymentsOfTaxes;
        
        /**
         * 支付其他与经营活动有关的现金
         */
        private Double otherCashPaidRelatingToOperatingActivities;
        
        /**
         * 经营活动现金流出小计
         */
        private Double subtotalOfCashOutflowsFromOperatingActivities;
        
        /**
         * 经营活动产生的现金流量净额
         */
        private Double netCashFlowsFromOperatingActivities;
    }
    
    /**
     * 投资活动现金流量类
     */
    @Data
    public static class InvestingActivities {
        /**
         * 收回投资收到的现金
         */
        private Double cashReceivedFromDisposalOfInvestments;
        
        /**
         * 取得投资收益收到的现金
         */
        private Double cashReceivedFromReturnsOnInvestments;
        
        /**
         * 处置固定资产、无形资产和其他长期资产收回的现金净额
         */
        private Double netCashReceivedFromDisposalOfFixedAssets;
        
        /**
         * 投资活动现金流入小计
         */
        private Double subtotalOfCashInflowsFromInvestingActivities;
        
        /**
         * 购建固定资产、无形资产和其他长期资产支付的现金
         */
        private Double cashPaidToAcquireFixedAssets;
        
        /**
         * 投资支付的现金
         */
        private Double cashPaidForInvestments;
        
        /**
         * 投资活动现金流出小计
         */
        private Double subtotalOfCashOutflowsFromInvestingActivities;
        
        /**
         * 投资活动产生的现金流量净额
         */
        private Double netCashFlowsFromInvestingActivities;
    }
    
    /**
     * 筹资活动现金流量类
     */
    @Data
    public static class FinancingActivities {
        /**
         * 吸收投资收到的现金
         */
        private Double cashReceivedFromInvestors;
        
        /**
         * 取得借款收到的现金
         */
        private Double cashReceivedFromBorrowings;
        
        /**
         * 筹资活动现金流入小计
         */
        private Double subtotalOfCashInflowsFromFinancingActivities;
        
        /**
         * 偿还债务支付的现金
         */
        private Double cashRepaymentsOfBorrowings;
        
        /**
         * 分配股利、利润或偿付利息支付的现金
         */
        private Double cashPaymentsForDistribution;
        
        /**
         * 筹资活动现金流出小计
         */
        private Double subtotalOfCashOutflowsFromFinancingActivities;
        
        /**
         * 筹资活动产生的现金流量净额
         */
        private Double netCashFlowsFromFinancingActivities;
    }
}