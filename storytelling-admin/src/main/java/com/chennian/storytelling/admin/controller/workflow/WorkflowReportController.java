package com.chennian.storytelling.admin.controller.workflow;

import com.chennian.storytelling.bean.dto.WorkflowReportDTO;
import com.chennian.storytelling.common.response.ServerResponseEntity;
import com.chennian.storytelling.service.WorkflowReportService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

/**
 * 工作流报表分析控制器
 * 提供数据分析和报表生成功能
 * 
 * @author chennian
 */
@Api(tags = "工作流报表分析")
@Slf4j
@RestController
@RequestMapping("/sys/workflow/reports")
@RequiredArgsConstructor
public class WorkflowReportController {

    private final WorkflowReportService workflowReportService;

    /**
     * 获取流程效率报表
     */
    @ApiOperation("获取流程效率报表")
    @GetMapping("/process-efficiency")
    public ServerResponseEntity<WorkflowReportDTO.EfficiencyAnalysisDTO> getProcessEfficiencyReport(
            @ApiParam("报表查询参数") @Valid WorkflowReportDTO.ReportQueryDTO queryParams) {
        
        try {
            // 从数据库获取流程效率报表数据
            WorkflowReportDTO.EfficiencyAnalysisDTO efficiencyData = workflowReportService.getProcessEfficiencyReport(queryParams);
            
            return ServerResponseEntity.success(efficiencyData);
            
        } catch (Exception e) {
            log.error("获取流程效率报表失败", e);
            return ServerResponseEntity.showFailMsg("获取流程效率报表失败: " + e.getMessage());
        }
    }
    
    /**
     * 获取工作负载报表
     */
    @ApiOperation("获取工作负载报表")
    @GetMapping("/workload")
    public ServerResponseEntity<WorkflowReportDTO.WorkloadAnalysisDTO> getWorkloadReport(
            @ApiParam("报表查询参数") @Valid WorkflowReportDTO.ReportQueryDTO queryParams) {
        
        try {
            // 从数据库获取工作负载报表数据
            WorkflowReportDTO.WorkloadAnalysisDTO workloadData = workflowReportService.getWorkloadReport(queryParams);
            
            return ServerResponseEntity.success(workloadData);
            
        } catch (Exception e) {
            log.error("获取工作负载报表失败", e);
            return ServerResponseEntity.showFailMsg("获取工作负载报表失败: " + e.getMessage());
        }
    }
    
    /**
     * 获取流程质量分析报表
     */
    @ApiOperation("获取流程质量分析报表")
    @GetMapping("/quality")
    public ServerResponseEntity<WorkflowReportDTO.QualityAnalysisDTO> getQualityReport(
            @ApiParam("报表查询参数") @Valid WorkflowReportDTO.ReportQueryDTO queryParams) {
        
        try {
            // 从数据库获取流程质量分析报表数据
            WorkflowReportDTO.QualityAnalysisDTO qualityData = workflowReportService.getQualityReport(queryParams);
            
            return ServerResponseEntity.success(qualityData);
            
        } catch (Exception e) {
            log.error("获取流程质量分析报表失败", e);
            return ServerResponseEntity.showFailMsg("获取流程质量分析报表失败: " + e.getMessage());
        }
    }
    
    /**
     * 获取成本分析报表
     */
    @ApiOperation("获取成本分析报表")
    @GetMapping("/cost")
    public ServerResponseEntity<WorkflowReportDTO.CostAnalysisDTO> getCostReport(
            @ApiParam("报表查询参数") @Valid WorkflowReportDTO.ReportQueryDTO queryParams) {
        
        try {
            // 从数据库获取成本分析报表数据
            WorkflowReportDTO.CostAnalysisDTO costData = workflowReportService.getCostReport(queryParams);
            
            return ServerResponseEntity.success(costData);
            
        } catch (Exception e) {
            log.error("获取成本分析报表失败", e);
            return ServerResponseEntity.showFailMsg("获取成本分析报表失败: " + e.getMessage());
        }
    }
    
    /**
     * 获取综合分析报告
     */
    @ApiOperation("获取综合分析报告")
    @GetMapping("/comprehensive")
    public ServerResponseEntity<WorkflowReportDTO.ComprehensiveAnalysisDTO> getComprehensiveReport(
            @ApiParam("报表查询参数") @Valid WorkflowReportDTO.ReportQueryDTO queryParams) {
        
        try {
            // 从数据库获取综合分析报告数据
            WorkflowReportDTO.ComprehensiveAnalysisDTO comprehensiveData = workflowReportService.getComprehensiveReport(queryParams);
            
            return ServerResponseEntity.success(comprehensiveData);
            
        } catch (Exception e) {
            log.error("获取综合分析报告失败", e);
            return ServerResponseEntity.showFailMsg("获取综合分析报告失败: " + e.getMessage());
        }
    }
    
    /**
     * 生成综合分析报告
     */
    @ApiOperation("生成综合分析报告")
    @PostMapping("/comprehensive")
    public ServerResponseEntity<String> generateComprehensiveReport(@RequestBody WorkflowReportDTO.ReportExportDTO exportParams) {
        try {
            String downUrl = workflowReportService.exportReport(exportParams);
            log.info("综合分析报告生成成功: reportId={}", exportParams.getTemplateId());
            return ServerResponseEntity.success(downUrl);
            
        } catch (Exception e) {
            log.error("综合分析报告生成失败", e);
            return ServerResponseEntity.showFailMsg("报告生成失败: " + e.getMessage());
        }
    }
    
    /**
     * 导出报表
     */
    @ApiOperation("导出报表")
    @PostMapping("/export")
    public ServerResponseEntity<Map<String, Object>> exportReport(
            @ApiParam("报表导出参数") @Valid @RequestBody WorkflowReportDTO.ReportExportDTO exportParams) {
        
        Map<String, Object> response = new HashMap<>();
        
        try {
            // 实现实际的报表导出逻辑
            String downloadUrl = workflowReportService.exportReport(exportParams);
            
            response.put("success", true);
            response.put("message", "报表导出成功");
            response.put("downloadUrl", downloadUrl);
            return ServerResponseEntity.success(response);
            
        } catch (Exception e) {
            log.error("报表导出失败", e);
            return ServerResponseEntity.showFailMsg("报表导出失败: " + e.getMessage());
        }
    }
    
    /**
     * 获取报表模板列表
     */
    @ApiOperation("获取报表模板列表")
    @GetMapping("/templates")
    public ServerResponseEntity<List<WorkflowReportDTO.ReportTemplateDTO>> getReportTemplates() {
        Map<String, Object> response = new HashMap<>();
        
        try {
            // 从数据库获取报表模板列表
            List<WorkflowReportDTO.ReportTemplateDTO> templates = workflowReportService.getReportTemplates();
            return ServerResponseEntity.success(templates);
            
        } catch (Exception e) {
            log.error("获取报表模板列表失败", e);
            return ServerResponseEntity.showFailMsg("获取报表模板列表失败: " + e.getMessage());
        }
    }
    
    /**
     * 获取实时数据看板
     */
    @ApiOperation("获取实时数据看板")
    @GetMapping("/dashboard/realtime")
    public ServerResponseEntity<WorkflowReportDTO.RealtimeDashboardDTO> getRealtimeDashboard() {
        Map<String, Object> response = new HashMap<>();
        
        try {
            // 从数据库获取实时数据看板数据
            WorkflowReportDTO.RealtimeDashboardDTO dashboardData = workflowReportService.getRealtimeDashboard();
            return ServerResponseEntity.success(dashboardData);
            
        } catch (Exception e) {
            log.error("获取实时数据看板失败", e);
            return ServerResponseEntity.showFailMsg("获取实时数据看板失败: " + e.getMessage());
        }
    }
}