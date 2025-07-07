package com.chennian.storytelling.bean.vo;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

/**
 * 质量趋势分析VO
 * 
 * @author storytelling
 * @date 2024-01-01
 */
public class QualityTrendVO {
    
    /** 分析类型 */
    private String analysisType;
    
    /** 时间范围 */
    private String timeRange;
    
    /** 趋势数据点 */
    private List<TrendDataPoint> trendPoints;
    
    /** 合格率趋势 */
    private List<Map<String, Object>> qualificationRateTrend;
    
    /** 质量成本趋势 */
    private List<Map<String, Object>> qualityCostTrend;
    
    /** 问题数量趋势 */
    private List<Map<String, Object>> issueTrend;
    
    /** 改进项目趋势 */
    private List<Map<String, Object>> improvementTrend;
    
    /** 预测数据 */
    private List<Map<String, Object>> forecastData;
    
    /** 趋势分析结论 */
    private String analysisConclusion;
    
    /** 改进建议 */
    private List<String> recommendations;
    
    /** 关键指标对比 */
    private Map<String, Object> keyIndicatorComparison;
    
    public String getAnalysisType() {
        return analysisType;
    }
    
    public void setAnalysisType(String analysisType) {
        this.analysisType = analysisType;
    }
    
    public String getTimeRange() {
        return timeRange;
    }
    
    public void setTimeRange(String timeRange) {
        this.timeRange = timeRange;
    }
    
    public List<TrendDataPoint> getTrendPoints() {
        return trendPoints;
    }
    
    public void setTrendPoints(List<TrendDataPoint> trendPoints) {
        this.trendPoints = trendPoints;
    }
    
    public List<Map<String, Object>> getQualificationRateTrend() {
        return qualificationRateTrend;
    }
    
    public void setQualificationRateTrend(List<Map<String, Object>> qualificationRateTrend) {
        this.qualificationRateTrend = qualificationRateTrend;
    }
    
    public List<Map<String, Object>> getQualityCostTrend() {
        return qualityCostTrend;
    }
    
    public void setQualityCostTrend(List<Map<String, Object>> qualityCostTrend) {
        this.qualityCostTrend = qualityCostTrend;
    }
    
    public List<Map<String, Object>> getIssueTrend() {
        return issueTrend;
    }
    
    public void setIssueTrend(List<Map<String, Object>> issueTrend) {
        this.issueTrend = issueTrend;
    }
    
    public List<Map<String, Object>> getImprovementTrend() {
        return improvementTrend;
    }
    
    public void setImprovementTrend(List<Map<String, Object>> improvementTrend) {
        this.improvementTrend = improvementTrend;
    }
    
    public List<Map<String, Object>> getForecastData() {
        return forecastData;
    }
    
    public void setForecastData(List<Map<String, Object>> forecastData) {
        this.forecastData = forecastData;
    }
    
    public String getAnalysisConclusion() {
        return analysisConclusion;
    }
    
    public void setAnalysisConclusion(String analysisConclusion) {
        this.analysisConclusion = analysisConclusion;
    }
    
    public List<String> getRecommendations() {
        return recommendations;
    }
    
    public void setRecommendations(List<String> recommendations) {
        this.recommendations = recommendations;
    }
    
    public Map<String, Object> getKeyIndicatorComparison() {
        return keyIndicatorComparison;
    }
    
    public void setKeyIndicatorComparison(Map<String, Object> keyIndicatorComparison) {
        this.keyIndicatorComparison = keyIndicatorComparison;
    }
    
    /**
     * 趋势数据点内部类
     */
    public static class TrendDataPoint {
        /** 时间点 */
        private String timePoint;
        
        /** 数值 */
        private BigDecimal value;
        
        /** 变化率 */
        private BigDecimal changeRate;
        
        /** 标签 */
        private String label;
        
        public String getTimePoint() {
            return timePoint;
        }
        
        public void setTimePoint(String timePoint) {
            this.timePoint = timePoint;
        }
        
        public BigDecimal getValue() {
            return value;
        }
        
        public void setValue(BigDecimal value) {
            this.value = value;
        }
        
        public BigDecimal getChangeRate() {
            return changeRate;
        }
        
        public void setChangeRate(BigDecimal changeRate) {
            this.changeRate = changeRate;
        }
        
        public String getLabel() {
            return label;
        }
        
        public void setLabel(String label) {
            this.label = label;
        }
    }
}