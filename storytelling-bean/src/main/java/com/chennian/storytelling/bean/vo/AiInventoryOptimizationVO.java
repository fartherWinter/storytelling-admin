package com.chennian.storytelling.bean.vo;

import lombok.Data;

import java.io.Serializable;
import java.util.List;
import java.util.Map;

/**
 * AI库存优化VO
 * 
 * @author chennian
 * @date 2023/12/15
 */
@Data
public class AiInventoryOptimizationVO implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 分析日期时间戳
     */
    private Long analysisDate;

    /**
     * 库存优化建议
     */
    private List<Map<String, Object>> recommendations;

    /**
     * 成本节约分析
     */
    private Map<String, Object> costSavings;
}