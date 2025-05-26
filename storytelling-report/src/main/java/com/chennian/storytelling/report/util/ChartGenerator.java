package com.chennian.storytelling.report.util;

import com.chennian.storytelling.common.exception.ServiceException;
import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartUtils;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.chart.plot.SpiderWebPlot;
import org.jfree.data.category.DefaultCategoryDataset;
import org.jfree.data.general.DefaultPieDataset;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 图表生成工具类
 * 提供各种类型图表的生成和导出功能
 *
 * @author chennian
 */
public class ChartGenerator {

    private static final Logger logger = LoggerFactory.getLogger(ChartGenerator.class);

    /**
     * 生成图表数据
     *
     * @param chartType 图表类型
     * @param data      数据列表
     * @param params    图表参数
     * @return 图表数据
     */
    public static Map<String, Object> generateChartData(String chartType, List<Map<String, Object>> data, Map<String, Object> params) {
        Map<String, Object> result = new HashMap<>();
        
        // 获取图表标题
        String title = params != null && params.containsKey("title") ? 
                params.get("title").toString() : "数据可视化图表";
        
        // 获取X轴和Y轴字段
        String xField = params != null && params.containsKey("xField") ? 
                params.get("xField").toString() : "name";
        String yField = params != null && params.containsKey("yField") ? 
                params.get("yField").toString() : "value";
        
        // 根据图表类型生成不同的数据结构
        switch (chartType.toLowerCase()) {
            case "line":
            case "bar":
                result.put("type", chartType);
                result.put("title", title);
                result.put("xAxis", extractFieldValues(data, xField));
                result.put("series", extractSeriesData(data, xField, yField));
                break;
                
            case "pie":
                result.put("type", "pie");
                result.put("title", title);
                result.put("series", extractPieData(data, xField, yField));
                break;
                
            case "scatter":
                result.put("type", "scatter");
                result.put("title", title);
                result.put("data", extractScatterData(data, xField, yField));
                break;
                
            case "radar":
                result.put("type", "radar");
                result.put("title", title);
                result.put("indicator", extractRadarIndicators(data, xField));
                result.put("series", extractRadarData(data, xField, yField));
                break;
                
            default:
                throw new ServiceException("不支持的图表类型: " + chartType);
        }
        
        return result;
    }

    /**
     * 导出图表
     *
     * @param chartType    图表类型
     * @param chartData    图表数据
     * @param exportFormat 导出格式
     * @return 图表文件字节数组
     */
    public static byte[] exportChart(String chartType, Map<String, Object> chartData, String exportFormat) throws Exception {
        // 创建JFreeChart对象
        JFreeChart chart = createJFreeChart(chartType, chartData);
        
        // 导出为图片
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        
        switch (exportFormat.toLowerCase()) {
            case "png":
                ChartUtils.writeChartAsPNG(outputStream, chart, 800, 600);
                break;
                
            case "jpg":
            case "jpeg":
                ChartUtils.writeChartAsJPEG(outputStream, chart, 800, 600);
                break;
                
            case "pdf":
                // 使用iText将图表转换为PDF
                BufferedImage image = chart.createBufferedImage(800, 600);
                com.itextpdf.text.Document document = new com.itextpdf.text.Document();
                com.itextpdf.text.pdf.PdfWriter.getInstance(document, outputStream);
                document.open();
                com.itextpdf.text.Image pdfImage = com.itextpdf.text.Image.getInstance(image, null);
                document.add(pdfImage);
                document.close();
                break;
                
            default:
                throw new ServiceException("不支持的导出格式: " + exportFormat);
        }
        
        return outputStream.toByteArray();
    }

    /**
     * 创建JFreeChart对象
     *
     * @param chartType 图表类型
     * @param chartData 图表数据
     * @return JFreeChart对象
     */
    private static JFreeChart createJFreeChart(String chartType, Map<String, Object> chartData) {
        String title = (String) chartData.get("title");
        
        switch (chartType.toLowerCase()) {
            case "line":
                DefaultCategoryDataset lineDataset = createCategoryDataset(chartData);
                return ChartFactory.createLineChart(
                        title,
                        "类别",
                        "值",
                        lineDataset,
                        PlotOrientation.VERTICAL,
                        true,
                        true,
                        false
                );
                
            case "bar":
                DefaultCategoryDataset barDataset = createCategoryDataset(chartData);
                return ChartFactory.createBarChart(
                        title,
                        "类别",
                        "值",
                        barDataset,
                        PlotOrientation.VERTICAL,
                        true,
                        true,
                        false
                );
                
            case "pie":
                DefaultPieDataset pieDataset = createPieDataset(chartData);
                return ChartFactory.createPieChart(
                        title,
                        pieDataset,
                        true,
                        true,
                        false
                );
                
            case "scatter":
                // 简化处理，实际应使用XYDataset
                DefaultCategoryDataset scatterDataset = createCategoryDataset(chartData);
                return ChartFactory.createLineChart(
                        title,
                        "X轴",
                        "Y轴",
                        scatterDataset,
                        PlotOrientation.VERTICAL,
                        true,
                        true,
                        false
                );
                
            case "radar":
                // JFreeChart不直接支持雷达图，使用SpiderWebPlot
                DefaultCategoryDataset radarDataset = createCategoryDataset(chartData);
                SpiderWebPlot spiderWebPlot = new SpiderWebPlot(radarDataset);
                return new JFreeChart(
                        title,
                        JFreeChart.DEFAULT_TITLE_FONT,
                        spiderWebPlot,
                        true
                );
                
            default:
                throw new ServiceException("不支持的图表类型: " + chartType);
        }
    }

    /**
     * 创建类别数据集
     *
     * @param chartData 图表数据
     * @return 类别数据集
     */
    private static DefaultCategoryDataset createCategoryDataset(Map<String, Object> chartData) {
        DefaultCategoryDataset dataset = new DefaultCategoryDataset();
        
        List<String> xAxis = (List<String>) chartData.get("xAxis");
        List<Map<String, Object>> series = (List<Map<String, Object>>) chartData.get("series");
        
        if (series != null) {
            for (Map<String, Object> serie : series) {
                String name = (String) serie.get("name");
                List<Number> data = (List<Number>) serie.get("data");
                
                for (int i = 0; i < data.size(); i++) {
                    String category = (i < xAxis.size()) ? xAxis.get(i) : String.valueOf(i);
                    dataset.addValue(data.get(i), name, category);
                }
            }
        }
        
        return dataset;
    }

    /**
     * 创建饼图数据集
     *
     * @param chartData 图表数据
     * @return 饼图数据集
     */
    private static DefaultPieDataset createPieDataset(Map<String, Object> chartData) {
        DefaultPieDataset dataset = new DefaultPieDataset();
        
        List<Map<String, Object>> series = (List<Map<String, Object>>) chartData.get("series");
        
        if (series != null && !series.isEmpty()) {
            List<Map<String, Object>> data = (List<Map<String, Object>>) series.get(0).get("data");
            
            for (Map<String, Object> item : data) {
                String name = (String) item.get("name");
                Number value = (Number) item.get("value");
                dataset.setValue(name, value);
            }
        }
        
        return dataset;
    }

    /**
     * 提取字段值列表
     *
     * @param data  数据列表
     * @param field 字段名
     * @return 字段值列表
     */
    private static List<String> extractFieldValues(List<Map<String, Object>> data, String field) {
        List<String> values = new java.util.ArrayList<>();
        
        for (Map<String, Object> item : data) {
            Object value = item.get(field);
            values.add(value != null ? value.toString() : "");
        }
        
        return values;
    }

    /**
     * 提取系列数据
     *
     * @param data   数据列表
     * @param xField X轴字段
     * @param yField Y轴字段
     * @return 系列数据
     */
    private static List<Map<String, Object>> extractSeriesData(List<Map<String, Object>> data, String xField, String yField) {
        List<Map<String, Object>> series = new java.util.ArrayList<>();
        Map<String, Object> serie = new HashMap<>();
        
        serie.put("name", "数据系列");
        
        List<Number> values = new java.util.ArrayList<>();
        for (Map<String, Object> item : data) {
            Object value = item.get(yField);
            if (value instanceof Number) {
                values.add((Number) value);
            } else if (value != null) {
                try {
                    values.add(Double.parseDouble(value.toString()));
                } catch (NumberFormatException e) {
                    values.add(0);
                }
            } else {
                values.add(0);
            }
        }
        
        serie.put("data", values);
        series.add(serie);
        
        return series;
    }

    /**
     * 提取饼图数据
     *
     * @param data   数据列表
     * @param xField 名称字段
     * @param yField 值字段
     * @return 饼图数据
     */
    private static List<Map<String, Object>> extractPieData(List<Map<String, Object>> data, String xField, String yField) {
        List<Map<String, Object>> series = new java.util.ArrayList<>();
        Map<String, Object> serie = new HashMap<>();
        
        serie.put("name", "数据系列");
        
        List<Map<String, Object>> pieData = new java.util.ArrayList<>();
        for (Map<String, Object> item : data) {
            Map<String, Object> pieItem = new HashMap<>();
            
            Object name = item.get(xField);
            Object value = item.get(yField);
            
            pieItem.put("name", name != null ? name.toString() : "");
            
            if (value instanceof Number) {
                pieItem.put("value", value);
            } else if (value != null) {
                try {
                    pieItem.put("value", Double.parseDouble(value.toString()));
                } catch (NumberFormatException e) {
                    pieItem.put("value", 0);
                }
            } else {
                pieItem.put("value", 0);
            }
            
            pieData.add(pieItem);
        }
        
        serie.put("data", pieData);
        series.add(serie);
        
        return series;
    }

    /**
     * 提取散点图数据
     *
     * @param data   数据列表
     * @param xField X轴字段
     * @param yField Y轴字段
     * @return 散点图数据
     */
    private static List<List<Number>> extractScatterData(List<Map<String, Object>> data, String xField, String yField) {
        List<List<Number>> scatterData = new java.util.ArrayList<>();
        
        for (Map<String, Object> item : data) {
            List<Number> point = new java.util.ArrayList<>();
            
            Object xValue = item.get(xField);
            Object yValue = item.get(yField);
            
            // X值
            if (xValue instanceof Number) {
                point.add((Number) xValue);
            } else if (xValue != null) {
                try {
                    point.add(Double.parseDouble(xValue.toString()));
                } catch (NumberFormatException e) {
                    point.add(0);
                }
            } else {
                point.add(0);
            }
            
            // Y值
            if (yValue instanceof Number) {
                point.add((Number) yValue);
            } else if (yValue != null) {
                try {
                    point.add(Double.parseDouble(yValue.toString()));
                } catch (NumberFormatException e) {
                    point.add(0);
                }
            } else {
                point.add(0);
            }
            
            scatterData.add(point);
        }
        
        return scatterData;
    }

    /**
     * 提取雷达图指标
     *
     * @param data   数据列表
     * @param xField 指标字段
     * @return 雷达图指标
     */
    private static List<Map<String, Object>> extractRadarIndicators(List<Map<String, Object>> data, String xField) {
        List<Map<String, Object>> indicators = new java.util.ArrayList<>();
        
        for (Map<String, Object> item : data) {
            Map<String, Object> indicator = new HashMap<>();
            
            Object name = item.get(xField);
            indicator.put("name", name != null ? name.toString() : "");
            indicator.put("max", 100); // 默认最大值
            
            indicators.add(indicator);
        }
        
        return indicators;
    }

    /**
     * 提取雷达图数据
     *
     * @param data   数据列表
     * @param xField 指标字段
     * @param yField 值字段
     * @return 雷达图数据
     */
    private static List<Map<String, Object>> extractRadarData(List<Map<String, Object>> data, String xField, String yField) {
        List<Map<String, Object>> series = new java.util.ArrayList<>();
        Map<String, Object> serie = new HashMap<>();
        
        serie.put("name", "数据系列");
        
        List<Number> values = new java.util.ArrayList<>();
        for (Map<String, Object> item : data) {
            Object value = item.get(yField);
            
            if (value instanceof Number) {
                values.add((Number) value);
            } else if (value != null) {
                try {
                    values.add(Double.parseDouble(value.toString()));
                } catch (NumberFormatException e) {
                    values.add(0);
                }
            } else {
                values.add(0);
            }
        }
        
        serie.put("data", values);
        series.add(serie);
        
        return series;
    }
}