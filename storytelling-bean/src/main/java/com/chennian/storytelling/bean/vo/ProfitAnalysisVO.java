package com.chennian.storytelling.bean.vo;

import lombok.Data;

import java.util.List;

/**
 * 利润分析VO
 * @author chen
 * @date 2023/6/15
 */
@Data
public class ProfitAnalysisVO {
    /**
     * 期间
     */
    private String period;
    
    /**
     * 分析维度
     */
    private String dimension;
    
    /**
     * 产品利润分析
     */
    private List<ProductProfit> productProfits;
    
    /**
     * 地区利润分析
     */
    private List<RegionProfit> regionProfits;
    
    /**
     * 客户利润分析
     */
    private List<CustomerProfit> customerProfits;
    
    /**
     * 产品利润分析项
     */
    @Data
    public static class ProductProfit {
        /**
         * 产品名称
         */
        private String productName;
        
        /**
         * 销售收入
         */
        private Double salesRevenue;
        
        /**
         * 销售成本
         */
        private Double salesCost;
        
        /**
         * 毛利
         */
        private Double grossProfit;
        
        /**
         * 毛利率
         */
        private String grossProfitRate;
    }
    
    /**
     * 地区利润分析项
     */
    @Data
    public static class RegionProfit {
        /**
         * 地区名称
         */
        private String regionName;
        
        /**
         * 销售收入
         */
        private Double salesRevenue;
        
        /**
         * 销售成本
         */
        private Double salesCost;
        
        /**
         * 毛利
         */
        private Double grossProfit;
        
        /**
         * 毛利率
         */
        private String grossProfitRate;
    }
    
    /**
     * 客户利润分析项
     */
    @Data
    public static class CustomerProfit {
        /**
         * 客户名称
         */
        private String customerName;
        
        /**
         * 销售收入
         */
        private Double salesRevenue;
        
        /**
         * 销售成本
         */
        private Double salesCost;
        
        /**
         * 毛利
         */
        private Double grossProfit;
        
        /**
         * 毛利率
         */
        private String grossProfitRate;
    }
}