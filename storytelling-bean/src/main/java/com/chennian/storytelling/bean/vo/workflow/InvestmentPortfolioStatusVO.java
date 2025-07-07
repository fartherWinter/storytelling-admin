package com.chennian.storytelling.bean.vo.workflow;

import lombok.Data;

import java.util.Date;

/**
 * 投资组合状态VO
 * @author chen
 * @date 2023/6/15
 */
@Data
public class InvestmentPortfolioStatusVO {
    /**
     * 基本信息
     */
    private BasicInfo basicInfo;
    
    /**
     * 投资组合汇总
     */
    private PortfolioSummary portfolioSummary;
    
    /**
     * 资产配置
     */
    private AssetAllocation assetAllocation;
    
    /**
     * 投资明细
     */
    private InvestmentDetails investmentDetails;
    
    /**
     * 绩效分析
     */
    private PerformanceAnalysis performanceAnalysis;
    
    /**
     * 基本信息
     */
    @Data
    public static class BasicInfo {
        /**
         * 投资组合ID
         */
        private String portfolioId;
        
        /**
         * 投资组合名称
         */
        private String portfolioName;
        
        /**
         * 创建日期
         */
        private Date createDate;
        
        /**
         * 币种
         */
        private String currency;
        
        /**
         * 风险等级
         */
        private String riskLevel;
    }
    
    /**
     * 投资组合汇总
     */
    @Data
    public static class PortfolioSummary {
        /**
         * 初始投资金额
         */
        private Double initialInvestment;
        
        /**
         * 当前市值
         */
        private Double currentMarketValue;
        
        /**
         * 累计收益
         */
        private Double totalReturn;
        
        /**
         * 收益率
         */
        private Double returnRate;
        
        /**
         * 年化收益率
         */
        private Double annualizedReturnRate;
    }
    
    /**
     * 资产配置
     */
    @Data
    public static class AssetAllocation {
        /**
         * 货币市场
         */
        private Double moneyMarket;
        
        /**
         * 债券
         */
        private Double bonds;
        
        /**
         * 股票
         */
        private Double stocks;
        
        /**
         * 另类投资
         */
        private Double alternatives;
    }
    
    /**
     * 投资明细
     */
    @Data
    public static class InvestmentDetails {
        /**
         * 货币市场
         */
        private MoneyMarket moneyMarket;
        
        /**
         * 债券
         */
        private Bonds bonds;
        
        /**
         * 股票
         */
        private Stocks stocks;
        
        /**
         * 另类投资
         */
        private Alternatives alternatives;
        
        /**
         * 货币市场
         */
        @Data
        public static class MoneyMarket {
            /**
             * 银行存款
             */
            private Double bankDeposits;
            
            /**
             * 货币基金
             */
            private Double moneyMarketFunds;
        }
        
        /**
         * 债券
         */
        @Data
        public static class Bonds {
            /**
             * 国债
             */
            private Double governmentBonds;
            
            /**
             * 企业债
             */
            private Double corporateBonds;
        }
        
        /**
         * 股票
         */
        @Data
        public static class Stocks {
            /**
             * 蓝筹股
             */
            private Double blueChips;
            
            /**
             * 指数基金
             */
            private Double indexFunds;
        }
        
        /**
         * 另类投资
         */
        @Data
        public static class Alternatives {
            /**
             * 房地产基金
             */
            private Double realEstateFunds;
            
            /**
             * 私募股权
             */
            private Double privateEquity;
        }
    }
    
    /**
     * 绩效分析
     */
    @Data
    public static class PerformanceAnalysis {
        /**
         * 夏普比率
         */
        private Double sharpeRatio;
        
        /**
         * 最大回撤
         */
        private Double maxDrawdown;
        
        /**
         * 波动率
         */
        private Double volatility;
        
        /**
         * 贝塔系数
         */
        private Double beta;
    }
}