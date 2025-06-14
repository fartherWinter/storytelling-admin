package com.chennian.storytelling.bean.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;
import jakarta.validation.constraints.NotNull;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

/**
 * 工作流报表DTO
 * 
 * @author chennian
 */
@Data
@ApiModel("工作流报表DTO")
public class WorkflowReportDTO {

    /**
     * 报表查询参数DTO
     */
    @Data
    @ApiModel("报表查询参数DTO")
    public static class ReportQueryDTO {
        @ApiModelProperty("开始日期")
        @NotNull
        private LocalDate startDate;
        
        @ApiModelProperty("结束日期")
        @NotNull
        private LocalDate endDate;
        
        @ApiModelProperty("流程定义键列表")
        private List<String> processKeys;
        
        @ApiModelProperty("部门ID列表")
        private List<String> departmentIds;
        
        @ApiModelProperty("用户ID列表")
        private List<String> userIds;
        
        @ApiModelProperty("报表类型")
        private String reportType; // efficiency, workload, quality, cost
        
        @ApiModelProperty("分组维度")
        private String groupBy; // day, week, month, department, user, process
        
        @ApiModelProperty("统计指标")
        private List<String> metrics;
    }
    
    /**
     * 效率分析报表DTO
     */
    @Data
    @ApiModel("效率分析报表DTO")
    public static class EfficiencyReportDTO {
        @ApiModelProperty("流程定义键")
        private String processKey;
        
        @ApiModelProperty("流程名称")
        private String processName;
        
        @ApiModelProperty("总流程数")
        private Integer totalProcesses;
        
        @ApiModelProperty("已完成流程数")
        private Long completedProcesses;
        
        @ApiModelProperty("平均处理时间（天）")
        private Double averageProcessingTime;
        
        @ApiModelProperty("效率率（%）")
        private Double efficiencyRate;
        
        @ApiModelProperty("平均完成时间（小时）")
        private Double avgCompletionTime;
        
        @ApiModelProperty("最短完成时间（小时）")
        private Double minCompletionTime;
        
        @ApiModelProperty("最长完成时间（小时）")
        private Double maxCompletionTime;
        
        @ApiModelProperty("完成数量")
        private Integer completedCount;
        
        @ApiModelProperty("超时数量")
        private Integer overtimeCount;
        
        @ApiModelProperty("效率评分")
        private Double efficiencyScore;
        
        @ApiModelProperty("趋势数据")
        private List<TrendDataDTO> trends;
        
        @ApiModelProperty("瓶颈环节")
        private List<BottleneckDTO> bottlenecks;
    }
    
    /**
     * 趋势数据DTO
     */
    @Data
    @ApiModel("趋势数据DTO")
    public static class TrendDataDTO {
        @ApiModelProperty("日期")
        private LocalDate date;
        
        @ApiModelProperty("数值")
        private Double value;
        
        @ApiModelProperty("标签")
        private String label;
        
        @ApiModelProperty("额外数据")
        private Map<String, Object> extra;
    }
    
    /**
     * 瓶颈环节DTO
     */
    @Data
    @ApiModel("瓶颈环节DTO")
    public static class BottleneckDTO {
        @ApiModelProperty("环节名称")
        private String taskName;
        
        @ApiModelProperty("平均处理时间（小时）")
        private Double avgProcessTime;
        
        @ApiModelProperty("等待时间（小时）")
        private Double avgWaitTime;
        
        @ApiModelProperty("处理人数")
        private Integer handlerCount;
        
        @ApiModelProperty("建议")
        private String suggestion;
    }
    
    /**
     * 工作负载报表DTO
     */
    @Data
    @ApiModel("工作负载报表DTO")
    public static class WorkloadReportDTO {
        @ApiModelProperty("用户ID")
        private String userId;
        
        @ApiModelProperty("用户名")
        private String userName;
        
        @ApiModelProperty("部门")
        private String department;
        
        @ApiModelProperty("待办任务数")
        private Integer pendingTasks;
        
        @ApiModelProperty("已完成任务数")
        private Integer completedTasks;
        
        @ApiModelProperty("总任务数")
        private Integer totalTasks;
        
        @ApiModelProperty("平均完成时间（小时）")
        private Double avgCompletionTime;
        
        @ApiModelProperty("平均每用户任务数")
        private Double averageTasksPerUser;
        
        @ApiModelProperty("工作负载评分")
        private Integer workloadScore;
        
        @ApiModelProperty("效率百分比")
        private Integer efficiency;
        
        @ApiModelProperty("超时任务数")
        private Integer overtimeTasks;
        
        @ApiModelProperty("用户工作负载列表")
        private List<UserWorkloadDTO> userWorkloads;
    }
    
    /**
     * 质量分析报表DTO
     */
    @Data
    @ApiModel("质量分析报表DTO")
    public static class QualityReportDTO {
        @ApiModelProperty("流程定义键")
        private String processKey;
        
        @ApiModelProperty("流程名称")
        private String processName;
        
        @ApiModelProperty("总流程数")
        private Integer totalProcesses;
        
        @ApiModelProperty("错误流程数")
        private Integer errorProcesses;
        
        @ApiModelProperty("返工流程数")
        private Integer reworkProcesses;
        
        @ApiModelProperty("质量评分")
        private Double qualityScore;
        
        @ApiModelProperty("质量指标")
        private List<QualityMetricsDTO> qualityMetrics;
        
        @ApiModelProperty("一次通过率")
        private Double firstPassRate;
        
        @ApiModelProperty("返工率")
        private Double reworkRate;
        
        @ApiModelProperty("错误率")
        private Double errorRate;
        
        @ApiModelProperty("客户满意度")
        private Double customerSatisfaction;
        
        @ApiModelProperty("常见问题")
        private List<QualityIssueDTO> commonIssues;
    }
    
    /**
     * 质量问题DTO
     */
    @Data
    @ApiModel("质量问题DTO")
    public static class QualityIssueDTO {
        @ApiModelProperty("问题类型")
        private String issueType;
        
        @ApiModelProperty("问题描述")
        private String description;
        
        @ApiModelProperty("发生次数")
        private Integer occurrenceCount;
        
        @ApiModelProperty("影响程度")
        private String impactLevel; // low, medium, high
        
        @ApiModelProperty("建议解决方案")
        private String suggestion;
    }
    
    /**
     * 成本分析报表DTO
     */
    @Data
    @ApiModel("成本分析报表DTO")
    public static class CostReportDTO {
        @ApiModelProperty("流程定义键")
        private String processKey;
        
        @ApiModelProperty("流程名称")
        private String processName;
        
        @ApiModelProperty("人力成本")
        private Double laborCost;
        
        @ApiModelProperty("系统成本")
        private Double systemCost;
        
        @ApiModelProperty("其他成本")
        private Double otherCost;
        
        @ApiModelProperty("总成本")
        private Double totalCost;
        
        @ApiModelProperty("单流程成本")
        private Double costPerProcess;
        
        @ApiModelProperty("单个流程平均成本")
        private Double avgCostPerProcess;
        
        @ApiModelProperty("成本趋势")
        private List<CostTrendDTO> costTrends;
        
        @ApiModelProperty("成本分解")
        private Map<String, Double> costBreakdown;
    }
    
    /**
     * 综合分析报表DTO
     */
    @Data
    @ApiModel("综合分析报表DTO")
    public static class ComprehensiveReportDTO {
        @ApiModelProperty("报表标题")
        private String title;
        
        @ApiModelProperty("生成时间")
        private LocalDateTime generateTime;
        
        @ApiModelProperty("查询参数")
        private ReportQueryDTO queryParams;
        
        @ApiModelProperty("效率分析")
        private List<EfficiencyReportDTO> efficiencyAnalysis;
        
        @ApiModelProperty("工作负载分析")
        private List<WorkloadReportDTO> workloadAnalysis;
        
        @ApiModelProperty("质量分析")
        private List<QualityReportDTO> qualityAnalysis;
        
        @ApiModelProperty("成本分析")
        private List<CostReportDTO> costAnalysis;
        
        @ApiModelProperty("关键指标")
        private Map<String, Object> keyMetrics;
        
        @ApiModelProperty("改进建议")
        private List<String> improvements;
    }
    
    /**
     * 报表导出参数DTO
     */
    @Data
    @ApiModel("报表导出参数DTO")
    public static class ExportParamsDTO {
        @ApiModelProperty("报表类型")
        @NotBlank
        private String reportType;
        
        @ApiModelProperty("导出格式")
        @NotBlank
        private String format; // excel, pdf, csv
        
        @ApiModelProperty("查询参数")
        @NotNull
        private ReportQueryDTO queryParams;
        
        @ApiModelProperty("包含图表")
        private Boolean includeCharts = true;
        
        @ApiModelProperty("文件名")
        private String fileName;
        
        @ApiModelProperty("模板ID")
        private String templateId;
    }
    
    /**
     * 实时数据看板DTO
     */
    @Data
    @ApiModel("实时数据看板DTO")
    public static class DashboardDTO {
        @ApiModelProperty("总流程数")
        private Integer totalProcesses;
        
        @ApiModelProperty("运行中流程数")
        private Integer runningProcesses;
        
        @ApiModelProperty("待办任务数")
        private Integer pendingTasks;
        
        @ApiModelProperty("今日完成任务数")
        private Integer todayCompletedTasks;
        
        @ApiModelProperty("平均处理时间")
        private Double avgProcessTime;
        
        @ApiModelProperty("系统负载")
        private Double systemLoad;
        
        @ApiModelProperty("活跃用户数")
        private Integer activeUsers;
        
        @ApiModelProperty("异常流程数")
        private Integer exceptionProcesses;
        
        @ApiModelProperty("实时趋势")
        private List<TrendDataDTO> realTimeTrends;
        
        @ApiModelProperty("热门流程")
        private List<Map<String, Object>> popularProcesses;
    }
    
    /**
     * 效率分析DTO（服务接口返回类型）
     */
    @Data
    @ApiModel("效率分析DTO")
    public static class EfficiencyAnalysisDTO {
        @ApiModelProperty("报表标题")
        private String title;
        
        @ApiModelProperty("生成时间")
        private LocalDateTime generateTime;
        
        @ApiModelProperty("查询参数")
        private ReportQueryDTO queryParams;
        
        @ApiModelProperty("效率分析数据")
        private List<EfficiencyReportDTO> data;
        
        @ApiModelProperty("统计摘要")
        private SummaryDTO summary;
        
        @ApiModelProperty("图表数据")
        private ChartDataDTO chartData;
    }
    
    /**
     * 工作负载分析DTO（服务接口返回类型）
     */
    @Data
    @ApiModel("工作负载分析DTO")
    public static class WorkloadAnalysisDTO {
        @ApiModelProperty("报表标题")
        private String title;
        
        @ApiModelProperty("生成时间")
        private LocalDateTime generateTime;
        
        @ApiModelProperty("查询参数")
        private ReportQueryDTO queryParams;
        
        @ApiModelProperty("工作负载数据")
        private List<WorkloadReportDTO> data;
        
        @ApiModelProperty("统计摘要")
        private SummaryDTO summary;
        
        @ApiModelProperty("图表数据")
        private ChartDataDTO chartData;
    }
    
    /**
     * 质量分析DTO（服务接口返回类型）
     */
    @Data
    @ApiModel("质量分析DTO")
    public static class QualityAnalysisDTO {
        @ApiModelProperty("报表标题")
        private String title;
        
        @ApiModelProperty("生成时间")
        private LocalDateTime generateTime;
        
        @ApiModelProperty("查询参数")
        private ReportQueryDTO queryParams;
        
        @ApiModelProperty("质量分析数据")
        private List<QualityReportDTO> data;
        
        @ApiModelProperty("统计摘要")
        private SummaryDTO summary;
        
        @ApiModelProperty("图表数据")
        private ChartDataDTO chartData;
    }
    
    /**
     * 成本分析DTO（服务接口返回类型）
     */
    @Data
    @ApiModel("成本分析DTO")
    public static class CostAnalysisDTO {
        @ApiModelProperty("报表标题")
        private String title;
        
        @ApiModelProperty("生成时间")
        private LocalDateTime generateTime;
        
        @ApiModelProperty("查询参数")
        private ReportQueryDTO queryParams;
        
        @ApiModelProperty("成本分析数据")
        private List<CostReportDTO> data;
        
        @ApiModelProperty("统计摘要")
        private SummaryDTO summary;
        
        @ApiModelProperty("图表数据")
        private ChartDataDTO chartData;
    }
    
    /**
     * 综合分析DTO（服务接口返回类型）
     */
    @Data
    @ApiModel("综合分析DTO")
    public static class ComprehensiveAnalysisDTO {
        @ApiModelProperty("报表标题")
        private String title;
        
        @ApiModelProperty("生成时间")
        private LocalDateTime generateTime;
        
        @ApiModelProperty("查询参数")
        private ReportQueryDTO queryParams;
        
        @ApiModelProperty("效率分析")
        private List<EfficiencyReportDTO> efficiencyAnalysis;
        
        @ApiModelProperty("工作负载分析")
        private List<WorkloadReportDTO> workloadAnalysis;
        
        @ApiModelProperty("质量分析")
        private List<QualityReportDTO> qualityAnalysis;
        
        @ApiModelProperty("成本分析")
        private List<CostReportDTO> costAnalysis;
        
        @ApiModelProperty("关键指标")
        private KeyMetricsDTO keyMetrics;
        
        @ApiModelProperty("改进建议")
        private List<String> improvements;
        
        @ApiModelProperty("图表数据")
        private ComprehensiveChartDataDTO chartData;
    }
    
    /**
     * 报表导出DTO（服务接口参数类型）
     */
    @Data
    @ApiModel("报表导出DTO")
    public static class ReportExportDTO {
        @ApiModelProperty("报表类型")
        @NotBlank
        private String reportType;
        
        @ApiModelProperty("导出格式")
        @NotBlank
        private String format; // excel, pdf, csv
        
        @ApiModelProperty("查询参数")
        @NotNull
        private ReportQueryDTO queryParams;
        
        @ApiModelProperty("包含图表")
        private Boolean includeCharts = true;
        
        @ApiModelProperty("文件名")
        private String fileName;
        
        @ApiModelProperty("模板ID")
        private String templateId;
        
        @ApiModelProperty("导出选项")
        private Map<String, Object> exportOptions;
    }
    
    /**
     * 报表模板DTO
     */
    @Data
    @ApiModel("报表模板DTO")
    public static class ReportTemplateDTO {
        @ApiModelProperty("模板ID")
        private Long id;
        
        @ApiModelProperty("模板标识")
        private String templateId;
        
        @ApiModelProperty("模板名称")
        private String templateName;
        
        @ApiModelProperty("模板描述")
        private String description;
        
        @ApiModelProperty("模板类型")
        private String templateType;
        
        @ApiModelProperty("模板配置")
        private String templateConfig;
        
        @ApiModelProperty("是否启用")
        private Boolean enabled;
        
        @ApiModelProperty("创建时间")
        private LocalDateTime createTime;
        
        @ApiModelProperty("更新时间")
        private LocalDateTime updateTime;
        
        @ApiModelProperty("创建人")
        private String createBy;
        
        @ApiModelProperty("更新人")
        private String updateBy;
    }
    
    /**
     * 用户工作负载DTO
     */
    @Data
    @ApiModel("用户工作负载DTO")
    public static class UserWorkloadDTO {
        @ApiModelProperty("用户名")
        private String userName;
        
        @ApiModelProperty("任务数量")
        private Integer taskCount;
        
        @ApiModelProperty("待办任务数")
        private Integer pendingCount;
        
        public UserWorkloadDTO() {}
        
        public UserWorkloadDTO(String userName, Integer taskCount, Integer pendingCount) {
            this.userName = userName;
            this.taskCount = taskCount;
            this.pendingCount = pendingCount;
        }
    }
    
    /**
     * 成本趋势DTO
     */
    @Data
    @ApiModel("成本趋势DTO")
    public static class CostTrendDTO {
        @ApiModelProperty("月份")
        private String month;
        
        @ApiModelProperty("成本")
        private Double cost;
        
        public CostTrendDTO() {}
        
        public CostTrendDTO(String month, Double cost) {
            this.month = month;
            this.cost = cost;
        }
    }
    
    /**
     * 实时仪表板DTO（服务接口返回类型）
     */
    @Data
    @ApiModel("实时仪表板DTO")
    public static class RealtimeDashboardDTO {
        @ApiModelProperty("总流程数")
        private Integer totalProcesses;
        
        @ApiModelProperty("运行中流程数")
        private Integer runningProcesses;
        
        @ApiModelProperty("待办任务数")
        private Integer pendingTasks;
        
        @ApiModelProperty("今日完成任务数")
        private Integer todayCompletedTasks;
        
        @ApiModelProperty("平均处理时间")
        private Double avgProcessTime;
        
        @ApiModelProperty("平均处理时间（天）")
        private Double averageProcessingTime;
        
        @ApiModelProperty("系统负载")
        private Double systemLoad;
        
        @ApiModelProperty("活跃用户数")
        private Integer activeUsers;
        
        @ApiModelProperty("异常流程数")
        private Integer exceptionProcesses;
        
        @ApiModelProperty("实时趋势")
        private List<TrendDataDTO> realTimeTrends;
        
        @ApiModelProperty("热门流程")
        private List<Map<String, Object>> popularProcesses;
        
        @ApiModelProperty("性能指标")
        private Map<String, Object> performanceMetrics;
        
        @ApiModelProperty("告警信息")
        private List<Map<String, Object>> alerts;
        
        @ApiModelProperty("更新时间")
        private LocalDateTime updateTime;
        
        @ApiModelProperty("最近活动")
        private List<Map<String, Object>> recentActivities;
    }
    
    /**
     * 统计摘要DTO
     */
    @Data
    @ApiModel("统计摘要DTO")
    public static class SummaryDTO {
        @ApiModelProperty("总流程数")
        private Integer totalProcesses;
        
        @ApiModelProperty("已完成流程数")
        private Long completedProcesses;
        
        @ApiModelProperty("平均处理时间（天）")
        private Double averageProcessingTime;
        
        @ApiModelProperty("效率率（%）")
        private Double efficiencyRate;
        
        @ApiModelProperty("总任务数")
        private Integer totalTasks;
        
        @ApiModelProperty("待办任务数")
        private Integer pendingTasks;
        
        @ApiModelProperty("已完成任务数")
        private Integer completedTasks;
        
        @ApiModelProperty("平均工作负载")
        private Double avgWorkload;
        
        @ApiModelProperty("平均每用户任务数")
        private Double averageTasksPerUser;
        
        @ApiModelProperty("错误流程数")
        private Integer errorProcesses;
        
        @ApiModelProperty("返工流程数")
        private Integer reworkProcesses;
        
        @ApiModelProperty("质量评分")
        private Double qualityScore;
        
        @ApiModelProperty("质量指标")
        private List<QualityMetricsDTO> qualityMetrics;
        
        @ApiModelProperty("总成本")
        private Double totalCost;
        
        @ApiModelProperty("人工成本")
        private Double laborCost;
        
        @ApiModelProperty("系统成本")
        private Double systemCost;
        
        @ApiModelProperty("其他成本")
        private Double otherCost;
        
        @ApiModelProperty("每流程成本")
        private Double costPerProcess;
    }
    
    /**
     * 图表数据DTO
     */
    @Data
    @ApiModel("图表数据DTO")
    public static class ChartDataDTO {
        @ApiModelProperty("趋势数据")
        private List<TrendDataDTO> trendData;
        
        @ApiModelProperty("任务分布")
        private TaskDistributionDTO taskDistribution;
        
        @ApiModelProperty("效率趋势")
        private List<TrendDataDTO> efficiencyTrends;
        
        @ApiModelProperty("瓶颈环节")
        private List<BottleneckDTO> bottlenecks;
        
        @ApiModelProperty("工作负载分布")
        private List<UserWorkloadDTO> workloadDistribution;
        
        @ApiModelProperty("质量分布")
        private QualityDistributionDTO qualityDistribution;
        
        @ApiModelProperty("质量指标")
        private List<QualityMetricsDTO> qualityMetrics;
        
        @ApiModelProperty("成本分解")
        private CostBreakdownDTO costBreakdown;
        
        @ApiModelProperty("成本趋势")
        private List<CostTrendDTO> costTrends;
    }
    
    /**
     * 关键指标DTO
     */
    @Data
    @ApiModel("关键指标DTO")
    public static class KeyMetricsDTO {
        @ApiModelProperty("总流程数")
        private Integer totalProcesses;
        
        @ApiModelProperty("已完成流程数")
        private Long completedProcesses;
        
        @ApiModelProperty("平均处理时间（天）")
        private Double averageProcessingTime;
        
        @ApiModelProperty("效率率（%）")
        private Double efficiencyRate;
        
        @ApiModelProperty("总任务数")
        private Integer totalTasks;
        
        @ApiModelProperty("待办任务数")
        private Integer pendingTasks;
        
        @ApiModelProperty("已完成任务数")
        private Integer completedTasks;
        
        @ApiModelProperty("平均每用户任务数")
        private Double averageTasksPerUser;
        
        @ApiModelProperty("平均工作负载")
        private Double avgWorkload;
        
        @ApiModelProperty("错误流程数")
        private Integer errorProcesses;
        
        @ApiModelProperty("返工流程数")
        private Integer reworkProcesses;
        
        @ApiModelProperty("质量评分")
        private Double qualityScore;
        
        @ApiModelProperty("总成本")
        private Double totalCost;
        
        @ApiModelProperty("人工成本")
        private Double laborCost;
        
        @ApiModelProperty("系统成本")
        private Double systemCost;
        
        @ApiModelProperty("每流程成本")
        private Double costPerProcess;
    }
    
    /**
     * 成本分解DTO
     */
    @Data
    @ApiModel("成本分解DTO")
    public static class CostBreakdownDTO {
        @ApiModelProperty("人工成本")
        private Double labor;
        
        @ApiModelProperty("系统成本")
        private Double system;
        
        @ApiModelProperty("其他成本")
        private Double other;
    }
    
    /**
     * 质量分布DTO
     */
    @Data
    @ApiModel("质量分布DTO")
    public static class QualityDistributionDTO {
        @ApiModelProperty("已完成流程数")
        private Integer completed;
        
        @ApiModelProperty("错误流程数")
        private Integer error;
        
        @ApiModelProperty("返工流程数")
        private Integer rework;
    }
    
    /**
     * 质量指标DTO
     */
    @Data
    @ApiModel("质量指标DTO")
    public static class QualityMetricsDTO {
        @ApiModelProperty("准确率（%）")
        private Double accuracyRate;
        
        @ApiModelProperty("及时率（%）")
        private Double timelyRate;
        
        @ApiModelProperty("完整率（%）")
        private Double completenessRate;
        
        @ApiModelProperty("一次通过率（%）")
        private Double firstPassRate;
        
        @ApiModelProperty("返工率（%）")
        private Double reworkRate;
        
        @ApiModelProperty("错误率（%）")
        private Double errorRate;
    }
    
    /**
     * 任务分布DTO
     */
    @Data
    @ApiModel("任务分布DTO")
    public static class TaskDistributionDTO {
        @ApiModelProperty("待办任务数")
        private Integer pending;
        
        @ApiModelProperty("已完成任务数")
        private Integer completed;
        
        @ApiModelProperty("进行中任务数")
        private Integer inProgress;
        
        @ApiModelProperty("总任务数")
        private Integer total;
    }
    
    /**
     * 综合图表数据DTO
     */
    @Data
    @ApiModel("综合图表数据DTO")
    public static class ComprehensiveChartDataDTO {
        @ApiModelProperty("效率分析数据")
        private ChartDataDTO efficiency;
        
        @ApiModelProperty("工作负载分析数据")
        private ChartDataDTO workload;
        
        @ApiModelProperty("质量分析数据")
        private ChartDataDTO quality;
        
        @ApiModelProperty("成本分析数据")
        private ChartDataDTO cost;
        
        @ApiModelProperty("效率趋势")
        private List<TrendDataDTO> efficiencyTrends;
        
        @ApiModelProperty("工作负载趋势")
        private List<TrendDataDTO> workloadTrends;
        
        @ApiModelProperty("质量趋势")
        private List<TrendDataDTO> qualityTrends;
        
        @ApiModelProperty("成本趋势")
        private List<CostTrendDTO> costTrends;
        
        @ApiModelProperty("流程分布")
        private Map<String, Integer> processDistribution;
        
        @ApiModelProperty("部门对比")
        private Map<String, Object> departmentComparison;
    }
}