package com.chennian.storytelling.bean.vo;

import lombok.Data;

import java.math.BigDecimal;
import java.util.Map;

/**
 * 库存统计数据VO
 * @author chennian
 */
@Data
public class InventoryStatisticsVO {
    
    /**
     * 总库存数量
     */
    private Integer totalQuantity;
    
    /**
     * 总库存价值
     */
    private BigDecimal totalValue;
    
    /**
     * 库存状态分布
     * key: 状态编码 (0-正常, 1-不足, 2-过多)
     * value: 数量
     */
    private Map<String, Integer> statusDistribution;
    
    /**
     * 库存周转率
     */
    private BigDecimal turnoverRate;
    
    /**
     * 平均库存量
     */
    private Integer averageQuantity;
    
    /**
     * 库存商品数量
     */
    private Integer productCount;
    
    /**
     * 库存预警商品数量
     */
    private Integer warningCount;
}