package com.chennian.storytelling.report.controller;

import com.chennian.storytelling.bean.model.ReportRecord;
import com.chennian.storytelling.bean.model.ReportTemplate;
import com.chennian.storytelling.common.response.ServerResponseEntity;
import com.chennian.storytelling.report.service.ReportService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import java.util.List;
import java.util.Map;

/**
 * 报表中心控制器
 * 提供报表模板管理、报表生成和导出功能
 *
 * @author chennian
 */
@RestController
@RequestMapping("/api")
@Tag(name = "报表中心", description = "报表模板管理、报表生成和导出相关接口")
public class ReportController {

    @Autowired
    private ReportService reportService;

    /**
     * 获取报表模板列表
     */
    @GetMapping("/templates")
    @Operation(summary = "获取报表模板列表")
    public ServerResponseEntity<List<ReportTemplate>> listTemplates(
            @RequestParam(required = false) String templateName,
            @RequestParam(required = false) String templateType,
            @RequestParam(required = false) String status) {
        List<ReportTemplate> templates = reportService.listTemplates(templateName, templateType, status);
        return ServerResponseEntity.success(templates);
    }

    /**
     * 获取报表模板详情
     */
    @GetMapping("/templates/{id}")
    @Operation(summary = "获取报表模板详情")
    public ServerResponseEntity<ReportTemplate> getTemplate(@PathVariable Long id) {
        ReportTemplate template = reportService.getTemplateById(id);
        return ServerResponseEntity.success(template);
    }

    /**
     * 创建报表模板
     */
    @PostMapping("/templates")
    @Operation(summary = "创建报表模板")
    public ServerResponseEntity<ReportTemplate> createTemplate(@RequestBody ReportTemplate template) {
        ReportTemplate createdTemplate = reportService.createTemplate(template);
        return ServerResponseEntity.success(createdTemplate);
    }

    /**
     * 更新报表模板
     */
    @PutMapping("/templates/{id}")
    @Operation(summary = "更新报表模板")
    public ServerResponseEntity<ReportTemplate> updateTemplate(
            @PathVariable Long id, 
            @RequestBody ReportTemplate template) {
        template.setId(id);
        ReportTemplate updatedTemplate = reportService.updateTemplate(template);
        return ServerResponseEntity.success(updatedTemplate);
    }

    /**
     * 删除报表模板
     */
    @DeleteMapping("/templates/{id}")
    @Operation(summary = "删除报表模板")
    public ServerResponseEntity<Void> deleteTemplate(@PathVariable Long id) {
        reportService.deleteTemplate(id);
        return ServerResponseEntity.success();
    }

    /**
     * 生成报表
     */
    @PostMapping("/reports/generate")
    @Operation(summary = "生成报表")
    public ServerResponseEntity<ReportRecord> generateReport(
            @RequestParam Long templateId,
            @RequestParam String reportName,
            @RequestParam String reportFormat,
            @RequestBody(required = false) Map<String, Object> params) {
        ReportRecord record = reportService.generateReport(templateId, reportName, reportFormat, params);
        return ServerResponseEntity.success(record);
    }

    /**
     * 获取报表记录列表
     */
    @GetMapping("/reports")
    @Operation(summary = "获取报表记录列表")
    public ServerResponseEntity<List<ReportRecord>> listReports(
            @RequestParam(required = false) String reportName,
            @RequestParam(required = false) Long templateId,
            @RequestParam(required = false) String status) {
        List<ReportRecord> records = reportService.listReports(reportName, templateId, status);
        return ServerResponseEntity.success(records);
    }

    /**
     * 获取报表记录详情
     */
    @GetMapping("/reports/{id}")
    @Operation(summary = "获取报表记录详情")
    public ServerResponseEntity<ReportRecord> getReport(@PathVariable Long id) {
        ReportRecord record = reportService.getReportById(id);
        return ServerResponseEntity.success(record);
    }

    /**
     * 下载报表
     */
    @GetMapping("/reports/{id}/download")
    @Operation(summary = "下载报表")
    public void downloadReport(@PathVariable Long id, HttpServletResponse response) {
        reportService.downloadReport(id, response);
    }

    /**
     * 获取数据可视化图表
     */
    @PostMapping("/visualization/chart")
    @Operation(summary = "获取数据可视化图表")
    public ServerResponseEntity<Map<String, Object>> generateChart(
            @RequestParam String chartType,
            @RequestParam(required = false) Long templateId,
            @RequestBody Map<String, Object> params) {
        Map<String, Object> chartData = reportService.generateChart(chartType, templateId, params);
        return ServerResponseEntity.success(chartData);
    }

    /**
     * 导出数据可视化图表
     */
    @PostMapping("/visualization/export")
    @Operation(summary = "导出数据可视化图表")
    public void exportChart(
            @RequestParam String chartType,
            @RequestParam String exportFormat,
            @RequestParam(required = false) Long templateId,
            @RequestBody Map<String, Object> params,
            HttpServletResponse response) {
        reportService.exportChart(chartType, exportFormat, templateId, params, response);
    }

    /**
     * 获取支持的报表格式
     */
    @GetMapping("/formats")
    @Operation(summary = "获取支持的报表格式")
    public ServerResponseEntity<List<String>> getSupportedFormats() {
        List<String> formats = reportService.getSupportedFormats();
        return ServerResponseEntity.success(formats);
    }

    /**
     * 获取支持的图表类型
     */
    @GetMapping("/chart-types")
    @Operation(summary = "获取支持的图表类型")
    public ServerResponseEntity<List<String>> getSupportedChartTypes() {
        List<String> chartTypes = reportService.getSupportedChartTypes();
        return ServerResponseEntity.success(chartTypes);
    }
}