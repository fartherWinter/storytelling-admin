package com.chennian.storytelling.admin.controller;


import com.chennian.storytelling.common.annotation.EventTrack;
import com.chennian.storytelling.common.enums.BusinessType;
import com.chennian.storytelling.common.enums.ModelType;
import com.chennian.storytelling.common.enums.OperatorType;
import com.chennian.storytelling.common.response.ServerResponseEntity;
import com.chennian.storytelling.service.ProductionAnalysisService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

/**
 * 生产分析控制器
 * @author chennian
 * @date 2024/1/15
 */
@RestController
@RequestMapping("/erp/manufacturing/analysis")
@Tag(name = "生产分析")
public class ProductionAnalysisController {

    private final ProductionAnalysisService productionAnalysisService;

    public ProductionAnalysisController(ProductionAnalysisService productionAnalysisService) {
        this.productionAnalysisService = productionAnalysisService;
    }

    /**
     * 生产统计
     */
    @GetMapping("/statistics")
    @Operation(summary = "生产统计")
    @EventTrack(title = ModelType.SYSTEM, businessType = BusinessType.SEARCH, operatorType = OperatorType.MANAGE, description = "生产统计")
    public ServerResponseEntity<Map<String, Object>> statistics(
            @RequestParam(required = false) String startDate,
            @RequestParam(required = false) String endDate,
            @RequestParam(required = false) String dimension) {
        Map<String, Object> statistics = productionAnalysisService.getProductionStatistics(startDate, endDate, dimension);
        return ServerResponseEntity.success(statistics);
    }

    /**
     * 生产效率分析
     */
    @GetMapping("/efficiency")
    @Operation(summary = "生产效率分析")
    @EventTrack(title = ModelType.SYSTEM, businessType = BusinessType.SEARCH, operatorType = OperatorType.MANAGE, description = "生产效率分析")
    public ServerResponseEntity<Map<String, Object>> efficiency(
            @RequestParam(required = false) String startDate,
            @RequestParam(required = false) String endDate) {
        Map<String, Object> efficiency = productionAnalysisService.getProductionEfficiencyAnalysis(startDate, endDate);
        return ServerResponseEntity.success(efficiency);
    }

    /**
     * 质量分析
     */
    @GetMapping("/quality")
    @Operation(summary = "质量分析")
    @EventTrack(title = ModelType.SYSTEM, businessType = BusinessType.SEARCH, operatorType = OperatorType.MANAGE, description = "质量分析")
    public ServerResponseEntity<Map<String, Object>> qualityAnalysis(
            @RequestParam(required = false) String startDate,
            @RequestParam(required = false) String endDate,
            @RequestParam(required = false) Long workOrderId) {
        Map<String, Object> result = productionAnalysisService.getQualityAnalysisReport(startDate, endDate, workOrderId);
        return ServerResponseEntity.success(result);
    }

    /**
     * 成本分析
     */
    @GetMapping("/cost")
    @Operation(summary = "成本分析")
    @EventTrack(title = ModelType.SYSTEM, businessType = BusinessType.SEARCH, operatorType = OperatorType.MANAGE, description = "成本分析")
    public ServerResponseEntity<Map<String, Object>> costAnalysis(
            @RequestParam(required = false) String startDate,
            @RequestParam(required = false) String endDate,
            @RequestParam(required = false) Long productId) {
        Map<String, Object> result = productionAnalysisService.getCostAnalysisReport(startDate, endDate, productId);
        return ServerResponseEntity.success(result);
    }

    /**
     * 产能利用率分析
     */
    @GetMapping("/capacity")
    @Operation(summary = "产能利用率分析")
    @EventTrack(title = ModelType.SYSTEM, businessType = BusinessType.SEARCH, operatorType = OperatorType.MANAGE, description = "产能利用率分析")
    public ServerResponseEntity<Map<String, Object>> capacityUtilization(
            @RequestParam(required = false) String startDate,
            @RequestParam(required = false) String endDate,
            @RequestParam(required = false) Long lineId) {
        Map<String, Object> result = productionAnalysisService.getCapacityUtilizationAnalysis(startDate, endDate, lineId);
        return ServerResponseEntity.success(result);
    }

    /**
     * 设备OEE分析
     */
    @GetMapping("/oee")
    @Operation(summary = "设备OEE分析")
    @EventTrack(title = ModelType.SYSTEM, businessType = BusinessType.SEARCH, operatorType = OperatorType.MANAGE, description = "设备OEE分析")
    public ServerResponseEntity<Map<String, Object>> oeeAnalysis(
            @RequestParam(required = false) String startDate,
            @RequestParam(required = false) String endDate,
            @RequestParam(required = false) Long equipmentId) {
        Map<String, Object> result = productionAnalysisService.getEquipmentOEEAnalysis(equipmentId, startDate, endDate);
        return ServerResponseEntity.success(result);
    }

    /**
     * 生产趋势分析
     */
    @GetMapping("/trend")
    @Operation(summary = "生产趋势分析")
    @EventTrack(title = ModelType.SYSTEM, businessType = BusinessType.SEARCH, operatorType = OperatorType.MANAGE, description = "生产趋势分析")
    public ServerResponseEntity<Map<String, Object>> trendAnalysis(
            @RequestParam(required = false) String startDate,
            @RequestParam(required = false) String endDate,
            @RequestParam(required = false) String period) {
        Map<String, Object> result = productionAnalysisService.getProductionTrendAnalysis(period, "daily");
        return ServerResponseEntity.success(result);
    }

    /**
     * 异常分析
     */
    @GetMapping("/exception")
    @Operation(summary = "异常分析")
    @EventTrack(title = ModelType.SYSTEM, businessType = BusinessType.SEARCH, operatorType = OperatorType.MANAGE, description = "异常分析")
    public ServerResponseEntity<Map<String, Object>> exceptionAnalysis(
            @RequestParam(required = false) String startDate,
            @RequestParam(required = false) String endDate,
            @RequestParam(required = false) Long workOrderId,
            @RequestParam(required = false) String exceptionType) {
        Map<String, Object> result = productionAnalysisService.getAnomalyAnalysisReport(startDate, endDate, workOrderId);
        return ServerResponseEntity.success(result);
    }

    /**
     * 绩效指标分析
     */
    @GetMapping("/performance")
    @Operation(summary = "绩效指标分析")
    @EventTrack(title = ModelType.SYSTEM, businessType = BusinessType.SEARCH, operatorType = OperatorType.MANAGE, description = "绩效指标分析")
    public ServerResponseEntity<Map<String, Object>> performanceAnalysis(
            @RequestParam(required = false) String startDate,
            @RequestParam(required = false) String endDate,
            @RequestParam(required = false) Long departmentId,
            @RequestParam(required = false) String indicatorType) {
        Map<String, Object> result = productionAnalysisService.getPerformanceIndicatorAnalysis(startDate, endDate, indicatorType);
        return ServerResponseEntity.success(result);
    }

    /**
     * 预测分析
     */
    @GetMapping("/forecast")
    @Operation(summary = "预测分析")
    @EventTrack(title = ModelType.SYSTEM, businessType = BusinessType.SEARCH, operatorType = OperatorType.MANAGE, description = "预测分析")
    public ServerResponseEntity<Map<String, Object>> forecastAnalysis(
            @RequestParam(required = false) String forecastType,
            @RequestParam(required = false) Integer forecastPeriod,
            @RequestParam(required = false) Long targetId) {
        Map<String, Object> parameters = new java.util.HashMap<>();
        parameters.put("forecastPeriod", forecastPeriod);
        parameters.put("targetId", targetId);
        Map<String, Object> result = productionAnalysisService.getPredictiveAnalysis(forecastType, parameters);
        return ServerResponseEntity.success(result);
    }

    /**
     * 智能优化建议
     */
    @GetMapping("/optimization")
    @Operation(summary = "智能优化建议")
    @EventTrack(title = ModelType.SYSTEM, businessType = BusinessType.SEARCH, operatorType = OperatorType.MANAGE, description = "智能优化建议")
    public ServerResponseEntity<Map<String, Object>> optimizationSuggestions(
            @RequestParam(required = false) String analysisType,
            @RequestParam(required = false) Long targetId) {
        Map<String, Object> context = new java.util.HashMap<>();
        context.put("targetId", targetId);
        List<Map<String, Object>> suggestions = productionAnalysisService.getOptimizationRecommendations(analysisType, context);
        Map<String, Object> result = new java.util.HashMap<>();
        result.put("suggestions", suggestions);
        return ServerResponseEntity.success(result);
    }

    /**
     * 实时监控数据
     */
    @GetMapping("/realtime")
    @Operation(summary = "实时监控数据")
    @EventTrack(title = ModelType.SYSTEM, businessType = BusinessType.SEARCH, operatorType = OperatorType.MANAGE, description = "实时监控数据")
    public ServerResponseEntity<Map<String, Object>> realtimeMonitoring(
            @RequestParam(required = false) String monitorType,
            @RequestParam(required = false) Long targetId) {
        Map<String, Object> result = productionAnalysisService.getRealTimeMonitoringData();
        return ServerResponseEntity.success(result);
    }

    /**
     * 导出生产报表
     */
    @GetMapping("/export")
    @Operation(summary = "导出生产报表")
    @EventTrack(title = ModelType.SYSTEM, businessType = BusinessType.EXPORT, operatorType = OperatorType.MANAGE, description = "导出生产报表")
    public ServerResponseEntity<Map<String, Object>> exportProduction(
            @RequestParam(required = false) String startDate,
            @RequestParam(required = false) String endDate,
            @RequestParam(required = false) Long productId,
            @RequestParam String reportType) {
        // 导出功能暂时返回模拟数据
        Map<String, Object> result = new java.util.HashMap<>();
        result.put("reportType", reportType);
        result.put("startDate", startDate);
        result.put("endDate", endDate);
        result.put("productId", productId);
        result.put("downloadUrl", "/api/reports/production_" + System.currentTimeMillis() + ".xlsx");
        return ServerResponseEntity.success(result);
    }
}