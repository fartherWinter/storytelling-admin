package com.chennian.storytelling.report.util;

import com.chennian.storytelling.bean.model.ReportTemplate;
import com.itextpdf.text.*;
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfWriter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.ByteArrayOutputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * PDF导出工具类
 * 提供PDF格式的报表导出功能
 *
 * @author chennian
 */
public class PdfExporter {

    private static final Logger logger = LoggerFactory.getLogger(PdfExporter.class);

    /**
     * 导出PDF格式报表
     *
     * @param data     报表数据
     * @param template 报表模板
     * @return PDF文件字节数组
     */
    public static byte[] exportToPdf(List<Map<String, Object>> data, ReportTemplate template) throws Exception {
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        
        // 创建文档
        Document document = new Document(PageSize.A4, 50, 50, 50, 50);
        PdfWriter writer = PdfWriter.getInstance(document, outputStream);
        document.open();
        
        // 添加标题
        Font titleFont = new Font(Font.FontFamily.HELVETICA, 18, Font.BOLD);
        Paragraph title = new Paragraph(template.getTemplateName(), titleFont);
        title.setAlignment(Element.ALIGN_CENTER);
        title.setSpacingAfter(20);
        document.add(title);
        
        // 添加描述（如果有）
        if (template.getDescription() != null && !template.getDescription().isEmpty()) {
            Font descFont = new Font(Font.FontFamily.HELVETICA, 12, Font.ITALIC);
            Paragraph desc = new Paragraph(template.getDescription(), descFont);
            desc.setAlignment(Element.ALIGN_CENTER);
            desc.setSpacingAfter(20);
            document.add(desc);
        }
        
        // 解析列配置
        List<String> columns = parseColumnConfig(template.getColumnConfig());
        
        // 创建表格
        PdfPTable table = new PdfPTable(columns.size());
        table.setWidthPercentage(100);
        
        // 设置表头
        Font headerFont = new Font(Font.FontFamily.HELVETICA, 12, Font.BOLD, BaseColor.WHITE);
        for (String column : columns) {
            PdfPCell cell = new PdfPCell(new Phrase(column, headerFont));
            cell.setBackgroundColor(BaseColor.DARK_GRAY);
            cell.setHorizontalAlignment(Element.ALIGN_CENTER);
            cell.setPadding(5);
            table.addCell(cell);
        }
        
        // 填充数据
        Font dataFont = new Font(Font.FontFamily.HELVETICA, 10);
        for (Map<String, Object> rowData : data) {
            for (String column : columns) {
                Object value = rowData.get(column);
                String cellValue = (value != null) ? value.toString() : "";
                PdfPCell cell = new PdfPCell(new Phrase(cellValue, dataFont));
                cell.setPadding(5);
                table.addCell(cell);
            }
        }
        
        document.add(table);
        
        // 添加页脚
        Paragraph footer = new Paragraph("报表生成时间: " + new java.util.Date(), 
                new Font(Font.FontFamily.HELVETICA, 8));
        footer.setAlignment(Element.ALIGN_RIGHT);
        document.add(footer);
        
        document.close();
        writer.close();
        
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
}