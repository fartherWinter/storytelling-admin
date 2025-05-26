package com.chennian.storytelling.bean.vo;

import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

/**
 * 客户统计数据VO
 * @author chennian
 */
@Data
public class CustomerStatisticsVO implements Serializable {
    
    private static final long serialVersionUID = 1L;
    
    /**
     * 客户总数
     */
    private Long totalCustomers;
    
    /**
     * 本月新增客户数
     */
    private Integer newCustomersThisMonth;
    
    /**
     * 活跃客户数
     */
    private Integer activeCustomers;
    
    /**
     * 客户类型分布
     */
    private Map<String, Integer> customerTypeDistribution;
    
    /**
     * 客户等级分布
     */
    private Map<String, Integer> customerLevelDistribution;
    
    /**
     * 客户地区分布
     */
    private Map<String, Integer> customerRegionDistribution;
    
    /**
     * 客户消费金额统计
     */
    private BigDecimal totalSalesAmount;
    
    /**
     * 月度客户增长趋势
     */
    private List<Map<String, Object>> monthlyGrowthTrend;
    
    /**
     * 客户跟进统计
     */
    private Map<String, Integer> followupStatistics;
}