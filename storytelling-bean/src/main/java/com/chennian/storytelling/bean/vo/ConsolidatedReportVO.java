package com.chennian.storytelling.bean.vo;

import lombok.Data;

import java.util.List;

/**
 * 合并报表VO基类
 * @author chen
 * @date 2023/6/15
 */
@Data
public class ConsolidatedReportVO {
    /**
     * 期间
     */
    private String period;
    
    /**
     * 合并组织
     */
    private List<String> organizations;
    
    /**
     * 合并资产负债表
     */
    @Data
    public static class ConsolidatedBalanceSheetVO extends ConsolidatedReportVO {
        /**
         * 资产
         */
        private BalanceSheetVO.Assets assets;
        
        /**
         * 负债
         */
        private BalanceSheetVO.Liabilities liabilities;
        
        /**
         * 所有者权益
         */
        private BalanceSheetVO.Equity equity;
    }
    
    /**
     * 合并利润表
     */
    @Data
    public static class ConsolidatedIncomeStatementVO extends ConsolidatedReportVO {
        /**
         * 营业收入
         */
        private Double operatingRevenue;
        
        /**
         * 营业成本
         */
        private Double operatingCost;
        
        /**
         * 税金及附加
         */
        private Double taxesAndSurcharges;
        
        /**
         * 销售费用
         */
        private Double sellingExpenses;
        
        /**
         * 管理费用
         */
        private Double administrativeExpenses;
        
        /**
         * 研发费用
         */
        private Double researchAndDevelopmentExpenses;
        
        /**
         * 财务费用
         */
        private Double financialExpenses;
        
        /**
         * 其他收益
         */
        private Double otherIncome;
        
        /**
         * 投资收益
         */
        private Double investmentIncome;
        
        /**
         * 营业利润
         */
        private Double operatingProfit;
        
        /**
         * 营业外收入
         */
        private Double nonOperatingIncome;
        
        /**
         * 营业外支出
         */
        private Double nonOperatingExpenses;
        
        /**
         * 利润总额
         */
        private Double totalProfit;
        
        /**
         * 所得税费用
         */
        private Double incomeTaxExpense;
        
        /**
         * 净利润
         */
        private Double netProfit;
    }
    
    /**
     * 合并现金流量表
     */
    @Data
    public static class ConsolidatedCashFlowStatementVO extends ConsolidatedReportVO {
        /**
         * 经营活动现金流量
         */
        private CashFlowStatementVO.OperatingActivities operatingActivities;
        
        /**
         * 投资活动现金流量
         */
        private CashFlowStatementVO.InvestingActivities investingActivities;
        
        /**
         * 筹资活动现金流量
         */
        private CashFlowStatementVO.FinancingActivities financingActivities;
        
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
    }
}