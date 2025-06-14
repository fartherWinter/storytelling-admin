package com.chennian.storytelling.service;

import java.util.List;
import java.util.Map;

/**
 * 生产统计分析服务接口
 * 
 * @author storytelling
 * @date 2024-01-01
 */
public interface ProductionAnalysisService {

    /**
     * 生产统计分析
     */
    Map<String, Object> getProductionStatistics(String startDate, String endDate, String dimension);
    
    /**
     * 生产效率分析
     */
    Map<String, Object> getProductionEfficiencyAnalysis(String startDate, String endDate);
    
    /**
     * 质量分析报告
     */
    Map<String, Object> getQualityAnalysisReport(String startDate, String endDate, Long workOrderId);
    
    /**
     * 成本分析报告
     */
    Map<String, Object> getCostAnalysisReport(String startDate, String endDate, Long bomId);
    
    /**
     * 产能利用率分析
     */
    Map<String, Object> getCapacityUtilizationAnalysis(String startDate, String endDate, Long lineId);
    
    /**
     * 设备OEE分析
     */
    Map<String, Object> getEquipmentOEEAnalysis(Long equipmentId, String startDate, String endDate);
    
    /**
     * 生产趋势分析
     */
    Map<String, Object> getProductionTrendAnalysis(String period, String granularity);
    
    /**
     * 异常分析报告
     */
    Map<String, Object> getAnomalyAnalysisReport(String startDate, String endDate, Long workOrderId);
    
    /**
     * 绩效指标分析
     */
    Map<String, Object> getPerformanceIndicatorAnalysis(String startDate, String endDate, String indicatorType);
    
    /**
     * 预测分析
     */
    Map<String, Object> getPredictiveAnalysis(String analysisType, Map<String, Object> parameters);
    
    /**
     * 智能优化建议
     */
    List<Map<String, Object>> getOptimizationRecommendations(String optimizationType, Map<String, Object> context);
    
    /**
     * 实时监控数据
     */
    Map<String, Object> getRealTimeMonitoringData();
    
    /**
     * 风险管理改进
     */
    Map<String, Object> getRiskManagementImprovement(Map<String, Object> riskData);
    
    /**
     * 产品创新推荐
     */
    List<Map<String, Object>> getProductInnovationRecommendations(Map<String, Object> marketData);
    
    /**
     * 市场扩展机会
     */
    List<Map<String, Object>> getMarketExpansionOpportunities(Map<String, Object> marketAnalysis);
    
    /**
     * 技术升级建议
     */
    List<Map<String, Object>> getTechnologyUpgradeRecommendations(Map<String, Object> technologyAssessment);
    
    /**
     * 培训需求分析
     */
    Map<String, Object> getTrainingNeedsAnalysis(Map<String, Object> skillsData);
    
    /**
     * 绩效激励优化
     */
    Map<String, Object> getPerformanceIncentiveOptimization(Map<String, Object> performanceData);
    
    /**
     * 工作流优化
     */
    Map<String, Object> getWorkflowOptimization(Map<String, Object> workflowData);
    
    /**
     * 沟通改进
     */
    Map<String, Object> getCommunicationImprovement(Map<String, Object> communicationData);
    
    /**
     * 决策优化
     */
    Map<String, Object> getDecisionMakingOptimization(Map<String, Object> decisionData);


    // 继续保留原有的getRiskManagementImprovement方法实现
    Map<String, Object> getRiskManagementImprovement(String startDate, String endDate, Long departmentId);
}