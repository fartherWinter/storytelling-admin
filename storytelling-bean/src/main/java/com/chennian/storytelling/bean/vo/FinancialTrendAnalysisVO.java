package com.chennian.storytelling.bean.vo;

import lombok.Data;
import java.util.List;

/**
 * 财务趋势分析VO
 * @author chen
 * @date 2023/6/15
 */
@Data
public class FinancialTrendAnalysisVO {
    
    /**
     * 开始期间
     */
    private String startPeriod;
    
    /**
     * 结束期间
     */
    private String endPeriod;
    
    /**
     * 收入趋势
     */
    private List<RevenueTrendVO> revenueTrend;
    
    /**
     * 利润趋势
     */
    private List<ProfitTrendVO> profitTrend;
    
    /**
     * 费用趋势
     */
    private List<ExpenseTrendVO> expenseTrend;
    
    /**
     * 收入趋势VO
     */
    @Data
    public static class RevenueTrendVO {
        /**
         * 期间
         */
        private String period;
        
        /**
         * 收入值
         */
        private Double value;
    }
    
    /**
     * 利润趋势VO
     */
    @Data
    public static class ProfitTrendVO {
        /**
         * 期间
         */
        private String period;
        
        /**
         * 利润值
         */
        private Double value;
    }
    
    /**
     * 费用趋势VO
     */
    @Data
    public static class ExpenseTrendVO {
        /**
         * 期间
         */
        private String period;
        
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
}