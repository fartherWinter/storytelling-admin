package com.chennian.storytelling.bean.vo;

import lombok.Data;

import java.io.Serializable;
import java.util.List;
import java.util.Map;

/**
 * 库存统计VO
 * 
 * @author chennian
 * @date 2023/12/15
 */
@Data
public class InventoryStatisticsVO implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 总库存产品数量
     */
    private Integer totalProducts;

    /**
     * 总库存价值
     */
    private Double totalValue;

    /**
     * 库存预警产品数量
     */
    private Integer alertProducts;

    /**
     * 库存周转率
     */
    private Double turnoverRate;

    /**
     * 库存分类统计
     */
    private List<Map<String, Object>> categoryStats;

    /**
     * 库存趋势数据
     */
    private List<Map<String, Object>> trendData;

    /**
     * 库存状态分布
     */
    private Map<String, Integer> statusDistribution;
}