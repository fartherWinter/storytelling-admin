package com.chennian.storytelling.service;

import java.util.List;

import com.chennian.storytelling.bean.dto.WorkflowReportDTO;

/**
 * 工作流报表服务接口
 * 
 * @author chennian
 */
public interface WorkflowReportService {

    /**
     * 获取流程效率报表
     * 
     * @param queryParams 查询参数
     * @return 效率分析数据
     */
    WorkflowReportDTO.EfficiencyAnalysisDTO getProcessEfficiencyReport(WorkflowReportDTO.ReportQueryDTO queryParams);
    
    /**
     * 获取工作负载报表
     * 
     * @param queryParams 查询参数
     * @return 工作负载分析数据
     */
    WorkflowReportDTO.WorkloadAnalysisDTO getWorkloadReport(WorkflowReportDTO.ReportQueryDTO queryParams);
    
    /**
     * 获取流程质量分析报表
     * 
     * @param queryParams 查询参数
     * @return 质量分析数据
     */
    WorkflowReportDTO.QualityAnalysisDTO getQualityReport(WorkflowReportDTO.ReportQueryDTO queryParams);
    
    /**
     * 获取成本分析报表
     * 
     * @param queryParams 查询参数
     * @return 成本分析数据
     */
    WorkflowReportDTO.CostAnalysisDTO getCostReport(WorkflowReportDTO.ReportQueryDTO queryParams);
    
    /**
     * 获取综合分析报告
     * 
     * @param queryParams 查询参数
     * @return 综合分析数据
     */
    WorkflowReportDTO.ComprehensiveAnalysisDTO getComprehensiveReport(WorkflowReportDTO.ReportQueryDTO queryParams);
    
    /**
     * 导出报表
     * 
     * @param exportParams 导出参数
     * @return 下载链接
     */
    String exportReport(WorkflowReportDTO.ReportExportDTO exportParams);
    
    /**
     * 获取报表模板列表
     * 
     * @return 报表模板列表
     */
    List<WorkflowReportDTO.ReportTemplateDTO> getReportTemplates();
    
    /**
     * 获取实时数据看板
     * 
     * @return 实时数据看板
     */
    WorkflowReportDTO.RealtimeDashboardDTO getRealtimeDashboard();
}