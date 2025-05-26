package com.chennian.storytelling.bean.vo;

import lombok.Data;

import java.io.Serializable;
import java.util.List;
import java.util.Map;

/**
 * AI客户洞察VO
 * 
 * @author chennian
 * @date 2023/12/15
 */
@Data
public class AiCustomerInsightVO implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 分析日期时间戳
     */
    private Long analysisDate;

    /**
     * 客户分群
     */
    private List<Map<String, Object>> customerSegments;

    /**
     * 行为模式
     */
    private Map<String, Object> behaviorPatterns;

    /**
     * 推荐行动
     */
    private List<String> recommendedActions;
}