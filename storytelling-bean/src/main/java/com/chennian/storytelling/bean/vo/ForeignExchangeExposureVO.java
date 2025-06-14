package com.chennian.storytelling.bean.vo;

import lombok.Data;

import java.util.Date;
import java.util.Map;

/**
 * 外汇风险敞口VO
 * @author chen
 * @date 2023/6/15
 */
@Data
public class ForeignExchangeExposureVO {
    /**
     * 基本信息
     */
    private BasicInfo basicInfo;
    
    /**
     * 风险敞口汇总
     */
    private ExposureSummary exposureSummary;
    
    /**
     * 敞口明细
     */
    private ExposureDetails exposureDetails;
    
    /**
     * 风险分析
     */
    private RiskAnalysis riskAnalysis;
    
    /**
     * 基本信息
     */
    @Data
    public static class BasicInfo {
        /**
         * 币种
         */
        private String currency;
        
        /**
         * 截止日期
         */
        private Date asOfDate;
        
        /**
         * 基准币种
         */
        private String baseCurrency;
        
        /**
         * 汇率
         */
        private Double exchangeRate;
    }
    
    /**
     * 风险敞口汇总
     */
    @Data
    public static class ExposureSummary {
        /**
         * 资产敞口
         */
        private Double assetExposure;
        
        /**
         * 负债敞口
         */
        private Double liabilityExposure;
        
        /**
         * 净敞口
         */
        private Double netExposure;
        
        /**
         * 已对冲金额
         */
        private Double hedgedAmount;
        
        /**
         * 未对冲敞口
         */
        private Double unhedgedExposure;
    }
    
    /**
     * 敞口明细
     */
    @Data
    public static class ExposureDetails {
        /**
         * 应收账款
         */
        private AccountsReceivable accountsReceivable;
        
        /**
         * 应付账款
         */
        private AccountsPayable accountsPayable;
        
        /**
         * 银行存款
         */
        private BankDeposits bankDeposits;
        
        /**
         * 银行贷款
         */
        private BankLoans bankLoans;
        
        /**
         * 应收账款
         */
        @Data
        public static class AccountsReceivable {
            /**
             * 金额
             */
            private Double amount;
            
            /**
             * 到期日分布
             */
            private String maturityDistribution;
        }
        
        /**
         * 应付账款
         */
        @Data
        public static class AccountsPayable {
            /**
             * 金额
             */
            private Double amount;
            
            /**
             * 到期日分布
             */
            private String maturityDistribution;
        }
        
        /**
         * 银行存款
         */
        @Data
        public static class BankDeposits {
            /**
             * 金额
             */
            private Double amount;
        }
        
        /**
         * 银行贷款
         */
        @Data
        public static class BankLoans {
            /**
             * 金额
             */
            private Double amount;
            
            /**
             * 到期日分布
             */
            private String maturityDistribution;
        }
    }
    
    /**
     * 风险分析
     */
    @Data
    public static class RiskAnalysis {
        /**
         * 汇率上升5%影响
         */
        private Double rateUp5pctImpact;
        
        /**
         * 汇率下降5%影响
         */
        private Double rateDown5pctImpact;
        
        /**
         * 汇率上升10%影响
         */
        private Double rateUp10pctImpact;
        
        /**
         * 汇率下降10%影响
         */
        private Double rateDown10pctImpact;
        
        /**
         * 风险评级
         */
        private String riskRating;
    }
}