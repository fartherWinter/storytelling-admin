package com.chennian.storytelling.report.service;

import com.chennian.storytelling.bean.model.ReportRecord;
import com.chennian.storytelling.bean.model.ReportTemplate;

import javax.servlet.http.HttpServletResponse;
import java.util.List;
import java.util.Map;

/**
 * 报表服务接口
 * 提供报表模板管理、报表生成和导出功能
 *
 * @author chennian
 */
public interface ReportService {

    /**
     * 获取报表模板列表
     *
     * @param templateName 模板名称（可选）
     * @param templateType 模板类型（可选）
     * @param status       状态（可选）
     * @return 报表模板列表
     */
    List<ReportTemplate> listTemplates(String templateName, String templateType, String status);

    /**
     * 根据ID获取报表模板
     *
     * @param id 模板ID
     * @return 报表模板
     */
    ReportTemplate getTemplateById(Long id);

    /**
     * 创建报表模板
     *
     * @param template 报表模板信息
     * @return 创建后的报表模板
     */
    ReportTemplate createTemplate(ReportTemplate template);

    /**
     * 更新报表模板
     *
     * @param template 报表模板信息
     * @return 更新后的报表模板
     */
    ReportTemplate updateTemplate(ReportTemplate template);

    /**
     * 删除报表模板
     *
     * @param id 模板ID
     */
    void deleteTemplate(Long id);

    /**
     * 生成报表
     *
     * @param templateId   模板ID
     * @param reportName   报表名称
     * @param reportFormat 报表格式
     * @param params       报表参数
     * @return 报表记录
     */
    ReportRecord generateReport(Long templateId, String reportName, String reportFormat, Map<String, Object> params);

    /**
     * 获取报表记录列表
     *
     * @param reportName 报表名称（可选）
     * @param templateId 模板ID（可选）
     * @param status     状态（可选）
     * @return 报表记录列表
     */
    List<ReportRecord> listReports(String reportName, Long templateId, String status);

    /**
     * 根据ID获取报表记录
     *
     * @param id 记录ID
     * @return 报表记录
     */
    ReportRecord getReportById(Long id);

    /**
     * 下载报表
     *
     * @param id       报表记录ID
     * @param response HTTP响应对象
     */
    void downloadReport(Long id, HttpServletResponse response);

    /**
     * 生成数据可视化图表
     *
     * @param chartType  图表类型
     * @param templateId 模板ID（可选）
     * @param params     图表参数
     * @return 图表数据
     */
    Map<String, Object> generateChart(String chartType, Long templateId, Map<String, Object> params);

    /**
     * 导出数据可视化图表
     *
     * @param chartType    图表类型
     * @param exportFormat 导出格式
     * @param templateId   模板ID（可选）
     * @param params       图表参数
     * @param response     HTTP响应对象
     */
    void exportChart(String chartType, String exportFormat, Long templateId, Map<String, Object> params, HttpServletResponse response);

    /**
     * 获取支持的报表格式
     *
     * @return 支持的报表格式列表
     */
    List<String> getSupportedFormats();

    /**
     * 获取支持的图表类型
     *
     * @return 支持的图表类型列表
     */
    List<String> getSupportedChartTypes();
}