package com.chennian.storytelling.report.util;

import com.alibaba.excel.EasyExcel;
import com.alibaba.excel.ExcelWriter;
import com.alibaba.excel.write.metadata.WriteSheet;
import com.chennian.storytelling.bean.model.ReportTemplate;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 * Excel导出工具类
 * 提供Excel、CSV、HTML格式的报表导出功能
 *
 * @author chennian
 */
public class ExcelExporter {

    private static final Logger logger = LoggerFactory.getLogger(ExcelExporter.class);

    /**
     * 导出Excel格式报表
     *
     * @param data     报表数据
     * @param template 报表模板
     * @return Excel文件字节数组
     */
    public static byte[] exportToExcel(List<Map<String, Object>> data, ReportTemplate template) throws IOException {
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        
        try (Workbook workbook = new XSSFWorkbook()) {
            Sheet sheet = workbook.createSheet(template.getTemplateName());
            
            // 创建标题样式
            CellStyle headerStyle = workbook.createCellStyle();
            headerStyle.setFillForegroundColor(IndexedColors.GREY_25_PERCENT.getIndex());
            headerStyle.setFillPattern(FillPatternType.SOLID_FOREGROUND);
            Font headerFont = workbook.createFont();
            headerFont.setBold(true);
            headerStyle.setFont(headerFont);
            
            // 解析列配置
            List<String> columns = parseColumnConfig(template.getColumnConfig());
            
            // 创建表头
            Row headerRow = sheet.createRow(0);
            for (int i = 0; i < columns.size(); i++) {
                Cell cell = headerRow.createCell(i);
                cell.setCellValue(columns.get(i));
                cell.setCellStyle(headerStyle);
                sheet.autoSizeColumn(i);
            }
            
            // 填充数据
            for (int i = 0; i < data.size(); i++) {
                Row row = sheet.createRow(i + 1);
                Map<String, Object> rowData = data.get(i);
                
                for (int j = 0; j < columns.size(); j++) {
                    String column = columns.get(j);
                    Cell cell = row.createCell(j);
                    
                    Object value = rowData.get(column);
                    if (value != null) {
                        if (value instanceof Number) {
                            cell.setCellValue(((Number) value).doubleValue());
                        } else if (value instanceof Boolean) {
                            cell.setCellValue((Boolean) value);
                        } else {
                            cell.setCellValue(value.toString());
                        }
                    }
                }
            }
            
            // 自动调整列宽
            for (int i = 0; i < columns.size(); i++) {
                sheet.autoSizeColumn(i);
            }
            
            workbook.write(outputStream);
        }
        
        return outputStream.toByteArray();
    }

    /**
     * 导出CSV格式报表
     *
     * @param data     报表数据
     * @param template 报表模板
     * @return CSV文件字节数组
     */
    public static byte[] exportToCsv(List<Map<String, Object>> data, ReportTemplate template) throws IOException {
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        
        // 解析列配置
        List<String> columns = parseColumnConfig(template.getColumnConfig());
        
        // 写入表头
        StringBuilder header = new StringBuilder();
        for (int i = 0; i < columns.size(); i++) {
            if (i > 0) {
                header.append(",");
            }
            header.append(escapeCSV(columns.get(i)));
        }
        header.append("\n");
        outputStream.write(header.toString().getBytes());
        
        // 写入数据
        for (Map<String, Object> rowData : data) {
            StringBuilder row = new StringBuilder();
            for (int i = 0; i < columns.size(); i++) {
                if (i > 0) {
                    row.append(",");
                }
                
                String column = columns.get(i);
                Object value = rowData.get(column);
                if (value != null) {
                    row.append(escapeCSV(value.toString()));
                }
            }
            row.append("\n");
            outputStream.write(row.toString().getBytes());
        }
        
        return outputStream.toByteArray();
    }

    /**
     * 导出HTML格式报表
     *
     * @param data     报表数据
     * @param template 报表模板
     * @return HTML文件字节数组
     */
    public static byte[] exportToHtml(List<Map<String, Object>> data, ReportTemplate template) throws IOException {
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        
        // 解析列配置
        List<String> columns = parseColumnConfig(template.getColumnConfig());
        
        // 构建HTML
        StringBuilder html = new StringBuilder();
        html.append("<!DOCTYPE html>\n");
        html.append("<html>\n");
        html.append("<head>\n");
        html.append("<meta charset=\"UTF-8\">\n");
        html.append("<title>").append(template.getTemplateName()).append("</title>\n");
        html.append("<style>\n");
        html.append("table { border-collapse: collapse; width: 100%; }\n");
        html.append("th, td { border: 1px solid #ddd; padding: 8px; text-align: left; }\n");
        html.append("th { background-color: #f2f2f2; font-weight: bold; }\n");
        html.append("tr:nth-child(even) { background-color: #f9f9f9; }\n");
        html.append("</style>\n");
        html.append("</head>\n");
        html.append("<body>\n");
        
        // 添加标题
        html.append("<h1>").append(template.getTemplateName()).append("</h1>\n");
        
        // 添加表格
        html.append("<table>\n");
        
        // 表头
        html.append("<tr>\n");
        for (String column : columns) {
            html.append("<th>").append(column).append("</th>\n");
        }
        html.append("</tr>\n");
        
        // 数据行
        for (Map<String, Object> rowData : data) {
            html.append("<tr>\n");
            for (String column : columns) {
                html.append("<td>");
                Object value = rowData.get(column);
                if (value != null) {
                    html.append(value);
                }
                html.append("</td>\n");
            }
            html.append("</tr>\n");
        }
        
        html.append("</table>\n");
        html.append("</body>\n");
        html.append("</html>");
        
        outputStream.write(html.toString().getBytes());
        return outputStream.toByteArray();
    }

    /**
     * 解析列配置
     *
     * @param columnConfig 列配置JSON字符串
     * @return 列名列表
     */
    private static List<String> parseColumnConfig(String columnConfig) {
        // 这里简化处理，实际应解析JSON配置
        // 如果没有配置，使用默认列
        if (columnConfig == null || columnConfig.isEmpty()) {
            List<String> defaultColumns = new ArrayList<>();
            defaultColumns.add("id");
            defaultColumns.add("name");
            defaultColumns.add("value");
            defaultColumns.add("date");
            return defaultColumns;
        }
        
        // TODO: 解析JSON配置获取列名列表
        return new ArrayList<>();
    }

    /**
     * 转义CSV字段值
     *
     * @param value 字段值
     * @return 转义后的值
     */
    private static String escapeCSV(String value) {
        if (value == null) {
            return "";
        }
        
        // 如果包含逗号、引号或换行符，需要用引号包围并转义引号
        if (value.contains(",") || value.contains("\"") || value.contains("\n")) {
            return "\"" + value.replace("\"", "\"\"") + "\"";
        }
        
        return value;
    }
}