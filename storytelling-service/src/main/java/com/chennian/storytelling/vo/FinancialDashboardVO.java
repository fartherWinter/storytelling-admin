package com.chennian.storytelling.vo;

import lombok.Data;

/**
 * 财务仪表盘VO
 * @author chen
 * @date 2023/6/15
 */
@Data
public class FinancialDashboardVO {
    
    /**
     * 收入概览
     */
    private RevenueOverviewVO revenueOverview;
    
    /**
     * 利润概览
     */
    private ProfitOverviewVO profitOverview;
    
    /**
     * 现金流概览
     */
    private CashflowOverviewVO cashflowOverview;
    
    /**
     * 费用概览
     */
    private ExpensesOverviewVO expensesOverview;
    
    /**
     * 资产负债概览
     */
    private BalanceSheetOverviewVO balanceSheetOverview;
    
    /**
     * 收入概览VO
     */
    @Data
    public static class RevenueOverviewVO {
        /**
         * 本月收入
         */
        private Double currentMonthRevenue;
        
        /**
         * 上月收入
         */
        private Double lastMonthRevenue;
        
        /**
         * 同比增长
         */
        private String yearOnYearGrowth;
        
        /**
         * 年度目标完成率
         */
        private String annualTargetCompletionRate;
    }
    
    /**
     * 利润概览VO
     */
    @Data
    public static class ProfitOverviewVO {
        /**
         * 本月利润
         */
        private Double currentMonthProfit;
        
        /**
         * 上月利润
         */
        private Double lastMonthProfit;
        
        /**
         * 同比增长
         */
        private String yearOnYearGrowth;
        
        /**
         * 毛利率
         */
        private String grossProfitMargin;
        
        /**
         * 净利率
         */
        private String netProfitMargin;
    }
    
    /**
     * 现金流概览VO
     */
    @Data
    public static class CashflowOverviewVO {
        /**
         * 经营活动现金流
         */
        private Double operatingCashflow;
        
        /**
         * 投资活动现金流
         */
        private Double investingCashflow;
        
        /**
         * 筹资活动现金流
         */
        private Double financingCashflow;
        
        /**
         * 现金净增加额
         */
        private Double netCashIncrease;
    }
    
    /**
     * 费用概览VO
     */
    @Data
    public static class ExpensesOverviewVO {
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
        private Double researchExpenses;
        
        /**
         * 财务费用
         */
        private Double financialExpenses;
    }
    
    /**
     * 资产负债概览VO
     */
    @Data
    public static class BalanceSheetOverviewVO {
        /**
         * 总资产
         */
        private Double totalAssets;
        
        /**
         * 总负债
         */
        private Double totalLiabilities;
        
        /**
         * 所有者权益
         */
        private Double ownersEquity;
        
        /**
         * 资产负债率
         */
        private String debtToAssetRatio;
    }
}