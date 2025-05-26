package com.chennian.storytelling.bean.vo;

import lombok.Data;

import java.io.Serializable;
import java.util.Map;

/**
 * AI销售预测VO
 * 
 * @author chennian
 * @date 2023/12/15
 */
@Data
public class AiSalesForecastVO implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 预测周期（daily, weekly, monthly, yearly）
     */
    private String forecastPeriod;

    /**
     * 置信水平
     */
    private Double confidenceLevel;

    /**
     * 预测数据
     */
    private Map<String, Object> forecastData;

    /**
     * 趋势分析
     */
    private Map<String, Object> trendAnalysis;
}