package com.chennian.storytelling.bean.vo.workflow;

import lombok.Data;

import java.util.Date;

/**
 * 成本中心分析VO
 * @author chen
 * @date 2023/6/15
 */
@Data
public class CostCenterAnalysisVO {
    /**
     * 基本信息
     */
    private BasicInfo basicInfo;
    
    /**
     * 成本汇总
     */
    private CostSummary costSummary;
    
    /**
     * 成本明细
     */
    private CostDetails costDetails;
    
    /**
     * 成本分析
     */
    private CostAnalysis costAnalysis;
    
    /**
     * 基本信息
     */
    @Data
    public static class BasicInfo {
        /**
         * 成本中心ID
         */
        private Long costCenterId;
        
        /**
         * 成本中心编码
         */
        private String costCenterCode;
        
        /**
         * 成本中心名称
         */
        private String costCenterName;
        
        /**
         * 部门ID
         */
        private Long departmentId;
        
        /**
         * 部门名称
         */
        private String departmentName;
        
        /**
         * 管理者ID
         */
        private Long managerId;
        
        /**
         * 管理者名称
         */
        private String managerName;
        
        /**
         * 创建日期
         */
        private Date createDate;
        
        /**
         * 状态
         */
        private String status;
    }
    
    /**
     * 成本汇总
     */
    @Data
    public static class CostSummary {
        /**
         * 总成本
         */
        private Double totalCost;
        
        /**
         * 直接成本
         */
        private Double directCost;
        
        /**
         * 间接成本
         */
        private Double indirectCost;
        
        /**
         * 人工成本
         */
        private Double laborCost;
        
        /**
         * 材料成本
         */
        private Double materialCost;
        
        /**
         * 管理费用
         */
        private Double overheadCost;
        
        /**
         * 其他成本
         */
        private Double otherCost;
    }
    
    /**
     * 成本明细
     */
    @Data
    public static class CostDetails {
        /**
         * 成本项目ID
         */
        private Long costItemId;
        
        /**
         * 成本项目名称
         */
        private String costItemName;
        
        /**
         * 成本类型
         */
        private String costType;
        
        /**
         * 成本类别
         */
        private String costCategory;
        
        /**
         * 金额
         */
        private Double amount;
        
        /**
         * 预算金额
         */
        private Double budgetAmount;
        
        /**
         * 差异
         */
        private Double variance;
        
        /**
          * 差异百分比
          */
         private Double variancePercentage;
    }
    
    /**
     * 成本分析
     */
    @Data
    public static class CostAnalysis {
        /**
         * 成本中心ID
         */
        private Long costCenterId;
        
        /**
         * 成本中心名称
         */
        private String costCenterName;
        
        /**
         * 总成本
         */
        private Double totalCost;
        
        /**
         * 上期成本
         */
        private Double previousPeriodCost;
        
        /**
         * 成本差异
         */
        private Double costVariance;
        
        /**
         * 成本差异百分比
         */
        private Double costVariancePercentage;
        
        /**
         * 年度累计成本
         */
        private Double ytdCost;
        
        /**
         * 年度预算
         */
        private Double annualBudget;
        
        /**
         * 预算使用百分比
         */
        private Double budgetUtilizationPercentage;
        
        /**
         * 单位成本
         */
        private Double costPerUnit;
        
        /**
         * 效率指数
         */
        private Double efficiencyIndex;
        
        /**
         * 其他费用
         */
        private Double otherExpenses;
    }
    

}