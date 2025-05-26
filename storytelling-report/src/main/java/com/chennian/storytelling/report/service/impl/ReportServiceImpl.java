package com.chennian.storytelling.report.service.impl;

import com.chennian.storytelling.bean.model.ReportRecord;
import com.chennian.storytelling.bean.model.ReportTemplate;
import com.chennian.storytelling.common.exception.ServiceException;
import com.chennian.storytelling.report.mapper.ReportRecordMapper;
import com.chennian.storytelling.report.mapper.ReportTemplateMapper;
import com.chennian.storytelling.report.service.ReportService;
import com.chennian.storytelling.report.util.ChartGenerator;
import com.chennian.storytelling.report.util.ExcelExporter;
import com.chennian.storytelling.report.util.PdfExporter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.*;

/**
 * 报表服务实现类
 * 提供报表模板管理、报表生成和导出功能的具体实现
 *
 * @author chennian
 */
@Service
public class ReportServiceImpl implements ReportService {

    private static final Logger logger = LoggerFactory.getLogger(ReportServiceImpl.class);

    @Autowired
    private ReportTemplateMapper reportTemplateMapper;

    @Autowired
    private ReportRecordMapper reportRecordMapper;

    @Value("${report.storage-path:/data/reports}")
    private String reportStoragePath;

    @Value("${report.export-formats:xlsx,pdf,csv,html}")
    private List<String> supportedFormats;

    @Value("${report.visualization.chart-types:line,bar,pie,scatter,radar}")
    private List<String> supportedChartTypes;

    @Override
    public List<ReportTemplate> listTemplates(String templateName, String templateType, String status) {
        return reportTemplateMapper.selectTemplateList(templateName, templateType, status);
    }

    @Override
    public ReportTemplate getTemplateById(Long id) {
        ReportTemplate template = reportTemplateMapper.selectTemplateById(id);
        if (template == null) {
            throw new ServiceException("报表模板不存在");
        }
        return template;
    }

    @Override
    @Transactional
    public ReportTemplate createTemplate(ReportTemplate template) {
        // 设置创建时间
        template.setCreateTime(new Date());
        template.setUpdateTime(new Date());
        
        // 默认状态为正常
        if (template.getStatus() == null) {
            template.setStatus("0");
        }
        
        // 默认删除标志为未删除
        template.setDelFlag("0");
        
        reportTemplateMapper.insertTemplate(template);
        return template;
    }

    @Override
    @Transactional
    public ReportTemplate updateTemplate(ReportTemplate template) {
        ReportTemplate existingTemplate = getTemplateById(template.getId());
        if (existingTemplate == null) {
            throw new ServiceException("要更新的报表模板不存在");
        }
        
        // 设置更新时间
        template.setUpdateTime(new Date());
        
        reportTemplateMapper.updateTemplate(template);
        return getTemplateById(template.getId());
    }

    @Override
    @Transactional
    public void deleteTemplate(Long id) {
        ReportTemplate template = getTemplateById(id);
        if (template == null) {
            throw new ServiceException("要删除的报表模板不存在");
        }
        
        // 逻辑删除
        template.setDelFlag("1");
        reportTemplateMapper.updateTemplate(template);
    }

    @Override
    @Transactional
    public ReportRecord generateReport(Long templateId, String reportName, String reportFormat, Map<String, Object> params) {
        // 验证模板是否存在
        ReportTemplate template = getTemplateById(templateId);
        if (template == null) {
            throw new ServiceException("报表模板不存在");
        }
        
        // 验证报表格式是否支持
        if (!supportedFormats.contains(reportFormat.toLowerCase())) {
            throw new ServiceException("不支持的报表格式: " + reportFormat);
        }
        
        // 创建报表记录
        ReportRecord record = new ReportRecord();
        record.setReportName(reportName);
        record.setTemplateId(templateId);
        record.setTemplateName(template.getTemplateName());
        record.setReportFormat(reportFormat);
        record.setReportParams(params != null ? params.toString() : null);
        record.setStatus("0"); // 生成中
        record.setCreateTime(new Date());
        record.setUpdateTime(new Date());
        record.setGenerateTime(new Date());
        record.setDelFlag("0");
        record.setDownloadCount(0);
        
        // 设置过期时间（默认7天后）
        Calendar calendar = Calendar.getInstance();
        calendar.add(Calendar.DAY_OF_MONTH, 7);
        record.setExpireTime(calendar.getTime());
        
        // 保存记录
        reportRecordMapper.insertReportRecord(record);
        
        try {
            // 确保存储目录存在
            Path storagePath = Paths.get(reportStoragePath);
            if (!Files.exists(storagePath)) {
                Files.createDirectories(storagePath);
            }
            
            // 生成报表文件路径
            String fileName = "report_" + record.getId() + "_" + System.currentTimeMillis() + "." + reportFormat.toLowerCase();
            Path filePath = storagePath.resolve(fileName);
            
            // 根据报表格式生成报表
            byte[] reportData = generateReportData(template, params, reportFormat);
            Files.write(filePath, reportData);
            
            // 更新报表记录
            record.setFilePath(filePath.toString());
            record.setFileSize((long) reportData.length);
            record.setStatus("1"); // 已完成
            reportRecordMapper.updateReportRecord(record);
            
            return record;
        } catch (Exception e) {
            logger.error("生成报表失败", e);
            
            // 更新报表记录为失败状态
            record.setStatus("2"); // 失败
            record.setErrorMsg(e.getMessage());
            reportRecordMapper.updateReportRecord(record);
            
            throw new ServiceException("生成报表失败: " + e.getMessage());
        }
    }

    /**
     * 根据模板和参数生成报表数据
     */
    private byte[] generateReportData(ReportTemplate template, Map<String, Object> params, String format) throws Exception {
        // 获取报表数据
        List<Map<String, Object>> reportData = fetchReportData(template, params);
        
        // 根据格式生成报表
        switch (format.toLowerCase()) {
            case "xlsx":
                return ExcelExporter.exportToExcel(reportData, template);
            case "pdf":
                return PdfExporter.exportToPdf(reportData, template);
            case "csv":
                return ExcelExporter.exportToCsv(reportData, template);
            case "html":
                return ExcelExporter.exportToHtml(reportData, template);
            default:
                throw new ServiceException("不支持的报表格式: " + format);
        }
    }

    /**
     * 根据模板和参数获取报表数据
     */
    private List<Map<String, Object>> fetchReportData(ReportTemplate template, Map<String, Object> params) {
        // 根据数据源类型获取数据
        String dataSourceType = template.getDataSourceType();
        if (dataSourceType == null) {
            throw new ServiceException("模板数据源类型未定义");
        }
        
        // 这里简化处理，实际应根据不同数据源类型实现不同的数据获取逻辑
        // 例如：SQL查询、API调用、文件解析等
        List<Map<String, Object>> data = new ArrayList<>();
        
        // 模拟数据（实际项目中应替换为真实数据获取逻辑）
        Map<String, Object> row1 = new HashMap<>();
        row1.put("id", 1);
        row1.put("name", "示例数据1");
        row1.put("value", 100);
        row1.put("date", new Date());
        
        Map<String, Object> row2 = new HashMap<>();
        row2.put("id", 2);
        row2.put("name", "示例数据2");
        row2.put("value", 200);
        row2.put("date", new Date());
        
        data.add(row1);
        data.add(row2);
        
        return data;
    }

    @Override
    public List<ReportRecord> listReports(String reportName, Long templateId, String status) {
        return reportRecordMapper.selectReportRecordList(reportName, templateId, status);
    }

    @Override
    public ReportRecord getReportById(Long id) {
        ReportRecord record = reportRecordMapper.selectReportRecordById(id);
        if (record == null) {
            throw new ServiceException("报表记录不存在");
        }
        return record;
    }

    @Override
    public void downloadReport(Long id, HttpServletResponse response) {
        ReportRecord record = getReportById(id);
        if (record == null) {
            throw new ServiceException("报表记录不存在");
        }
        
        // 检查报表状态
        if (!"1".equals(record.getStatus())) {
            throw new ServiceException("报表尚未生成完成，无法下载");
        }
        
        // 检查文件是否存在
        String filePath = record.getFilePath();
        File file = new File(filePath);
        if (!file.exists()) {
            throw new ServiceException("报表文件不存在");
        }
        
        // 设置响应头
        String fileName = record.getReportName() + "." + record.getReportFormat().toLowerCase();
        response.setContentType(getContentType(record.getReportFormat()));
        response.setHeader("Content-Disposition", "attachment; filename=" + fileName);
        
        // 写入响应
        try (FileInputStream fis = new FileInputStream(file);
             OutputStream os = response.getOutputStream()) {
            
            byte[] buffer = new byte[1024];
            int length;
            while ((length = fis.read(buffer)) > 0) {
                os.write(buffer, 0, length);
            }
            os.flush();
            
            // 更新下载次数
            record.setDownloadCount(record.getDownloadCount() + 1);
            record.setUpdateTime(new Date());
            reportRecordMapper.updateReportRecord(record);
            
        } catch (IOException e) {
            logger.error("下载报表失败", e);
            throw new ServiceException("下载报表失败: " + e.getMessage());
        }
    }

    /**
     * 根据报表格式获取内容类型
     */
    private String getContentType(String format) {
        switch (format.toLowerCase()) {
            case "xlsx":
                return "application/vnd.openxmlformats-officedocument.spreadsheetml.sheet";
            case "pdf":
                return "application/pdf";
            case "csv":
                return "text/csv";
            case "html":
                return "text/html";
            default:
                return "application/octet-stream";
        }
    }

    @Override
    public Map<String, Object> generateChart(String chartType, Long templateId, Map<String, Object> params) {
        // 验证图表类型是否支持
        if (!supportedChartTypes.contains(chartType.toLowerCase())) {
            throw new ServiceException("不支持的图表类型: " + chartType);
        }
        
        // 获取数据
        List<Map<String, Object>> data;
        if (templateId != null) {
            ReportTemplate template = getTemplateById(templateId);
            data = fetchReportData(template, params);
        } else {
            // 如果没有指定模板，直接使用参数中的数据
            if (params == null || !params.containsKey("data")) {
                throw new ServiceException("未指定模板时，必须在参数中提供数据");
            }
            data = (List<Map<String, Object>>) params.get("data");
        }
        
        // 生成图表数据
        return ChartGenerator.generateChartData(chartType, data, params);
    }

    @Override
    public void exportChart(String chartType, String exportFormat, Long templateId, Map<String, Object> params, HttpServletResponse response) {
        // 生成图表数据
        Map<String, Object> chartData = generateChart(chartType, templateId, params);
        
        // 设置响应头
        String fileName = "chart_" + chartType + "_" + System.currentTimeMillis() + "." + exportFormat.toLowerCase();
        response.setContentType(getContentType(exportFormat));
        response.setHeader("Content-Disposition", "attachment; filename=" + fileName);
        
        try {
            // 导出图表
            byte[] chartBytes = ChartGenerator.exportChart(chartType, chartData, exportFormat);
            
            // 写入响应
            response.getOutputStream().write(chartBytes);
            response.getOutputStream().flush();
            
        } catch (Exception e) {
            logger.error("导出图表失败", e);
            throw new ServiceException("导出图表失败: " + e.getMessage());
        }
    }

    @Override
    public List<String> getSupportedFormats() {
        return supportedFormats;
    }

    @Override
    public List<String> getSupportedChartTypes() {
        return supportedChartTypes;
    }
}