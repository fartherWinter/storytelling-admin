package com.chennian.storytelling.vo;

import lombok.Data;

/**
 * 利润表VO
 * @author chen
 * @date 2023/6/15
 */
@Data
public class IncomeStatementVO {
    /**
     * 期间
     */
    private String period;
    
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