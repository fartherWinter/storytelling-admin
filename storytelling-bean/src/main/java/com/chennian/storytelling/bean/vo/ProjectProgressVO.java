package com.chennian.storytelling.bean.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

/**
 * 项目进度分析VO
 *
 * @author storytelling
 * @date 2024-01-01
 */
@Data
@Schema(description = "项目进度分析VO")
public class ProjectProgressVO {

    @Schema(description = "项目ID")
    private Long projectId;

    @Schema(description = "项目名称")
    private String projectName;

    @Schema(description = "项目编号")
    private String projectCode;

    @Schema(description = "当前进度")
    private BigDecimal currentProgress;

    @Schema(description = "计划进度")
    private BigDecimal plannedProgress;

    @Schema(description = "进度偏差")
    private BigDecimal progressVariance;

    @Schema(description = "进度偏差率")
    private BigDecimal progressVarianceRate;

    @Schema(description = "项目状态")
    private Integer status;

    @Schema(description = "项目状态名称")
    private String statusName;

    @Schema(description = "计划开始时间")
    private LocalDateTime plannedStartTime;

    @Schema(description = "计划结束时间")
    private LocalDateTime plannedEndTime;

    @Schema(description = "实际开始时间")
    private LocalDateTime actualStartTime;

    @Schema(description = "预计结束时间")
    private LocalDateTime estimatedEndTime;

    @Schema(description = "剩余天数")
    private Integer remainingDays;

    @Schema(description = "是否超期")
    private Boolean isOverdue;

    @Schema(description = "超期天数")
    private Integer overdueDays;

    @Schema(description = "关键路径任务")
    private List<CriticalPathTask> criticalPathTasks;

    @Schema(description = "任务进度分析")
    private List<TaskProgress> taskProgressList;

    @Schema(description = "里程碑进度")
    private List<MilestoneProgress> milestoneProgressList;

    @Schema(description = "团队绩效")
    private List<TeamPerformance> teamPerformanceList;

    @Schema(description = "进度趋势")
    private List<ProgressTrend> progressTrends;

    @Schema(description = "风险影响分析")
    private RiskImpactAnalysis riskImpactAnalysis;

    @Schema(description = "资源使用情况")
    private ResourceUtilization resourceUtilization;

    @Schema(description = "质量指标")
    private QualityMetrics qualityMetrics;

    @Schema(description = "成本绩效")
    private CostPerformance costPerformance;

    /**
     * 关键路径任务
     */
    @Data
    @Schema(description = "关键路径任务")
    public static class CriticalPathTask {
        @Schema(description = "任务ID")
        private Long taskId;

        @Schema(description = "任务名称")
        private String taskName;

        @Schema(description = "任务编号")
        private String taskCode;

        @Schema(description = "当前进度")
        private BigDecimal progress;

        @Schema(description = "计划开始时间")
        private LocalDateTime plannedStartTime;

        @Schema(description = "计划结束时间")
        private LocalDateTime plannedEndTime;

        @Schema(description = "负责人")
        private String assigneeName;

        @Schema(description = "是否延期")
        private Boolean isDelayed;

        @Schema(description = "延期天数")
        private Integer delayDays;

        @Schema(description = "风险等级")
        private Integer riskLevel;
    }

    /**
     * 任务进度
     */
    @Data
    @Schema(description = "任务进度")
    public static class TaskProgress {
        @Schema(description = "任务ID")
        private Long taskId;

        @Schema(description = "任务名称")
        private String taskName;

        @Schema(description = "任务类型")
        private Integer taskType;

        @Schema(description = "任务类型名称")
        private String taskTypeName;

        @Schema(description = "当前进度")
        private BigDecimal progress;

        @Schema(description = "计划进度")
        private BigDecimal plannedProgress;

        @Schema(description = "进度状态")
        private String progressStatus; // AHEAD, ON_TRACK, BEHIND, CRITICAL

        @Schema(description = "负责人")
        private String assigneeName;

        @Schema(description = "优先级")
        private Integer priority;

        @Schema(description = "预计完成时间")
        private LocalDateTime estimatedCompletionTime;
    }

    /**
     * 里程碑进度
     */
    @Data
    @Schema(description = "里程碑进度")
    public static class MilestoneProgress {
        @Schema(description = "里程碑ID")
        private Long milestoneId;

        @Schema(description = "里程碑名称")
        private String milestoneName;

        @Schema(description = "计划时间")
        private LocalDateTime plannedTime;

        @Schema(description = "实际时间")
        private LocalDateTime actualTime;

        @Schema(description = "状态")
        private Integer status;

        @Schema(description = "状态名称")
        private String statusName;

        @Schema(description = "完成度")
        private BigDecimal completionRate;

        @Schema(description = "是否关键里程碑")
        private Boolean isCritical;

        @Schema(description = "延期天数")
        private Integer delayDays;
    }

    /**
     * 团队绩效
     */
    @Data
    @Schema(description = "团队绩效")
    public static class TeamPerformance {
        @Schema(description = "成员ID")
        private Long memberId;

        @Schema(description = "成员姓名")
        private String memberName;

        @Schema(description = "分配任务数")
        private Integer assignedTasks;

        @Schema(description = "完成任务数")
        private Integer completedTasks;

        @Schema(description = "任务完成率")
        private BigDecimal taskCompletionRate;

        @Schema(description = "平均任务完成时间")
        private BigDecimal averageCompletionTime;

        @Schema(description = "工作效率")
        private BigDecimal efficiency;

        @Schema(description = "质量评分")
        private BigDecimal qualityScore;

        @Schema(description = "超期任务数")
        private Integer overdueTasks;
    }

    /**
     * 进度趋势
     */
    @Data
    @Schema(description = "进度趋势")
    public static class ProgressTrend {
        @Schema(description = "日期")
        private String date;

        @Schema(description = "计划进度")
        private BigDecimal plannedProgress;

        @Schema(description = "实际进度")
        private BigDecimal actualProgress;

        @Schema(description = "累计偏差")
        private BigDecimal cumulativeVariance;

        @Schema(description = "完成任务数")
        private Integer completedTasks;

        @Schema(description = "新增任务数")
        private Integer newTasks;
    }

    /**
     * 风险影响分析
     */
    @Data
    @Schema(description = "风险影响分析")
    public static class RiskImpactAnalysis {
        @Schema(description = "高风险任务数")
        private Integer highRiskTasks;

        @Schema(description = "中风险任务数")
        private Integer mediumRiskTasks;

        @Schema(description = "低风险任务数")
        private Integer lowRiskTasks;

        @Schema(description = "风险对进度的影响")
        private BigDecimal riskImpactOnProgress;

        @Schema(description = "预计延期天数")
        private Integer estimatedDelayDays;

        @Schema(description = "风险缓解措施数")
        private Integer mitigationActions;
    }

    /**
     * 资源使用情况
     */
    @Data
    @Schema(description = "资源使用情况")
    public static class ResourceUtilization {
        @Schema(description = "人力资源使用率")
        private BigDecimal humanResourceUtilization;

        @Schema(description = "设备资源使用率")
        private BigDecimal equipmentUtilization;

        @Schema(description = "材料资源使用率")
        private BigDecimal materialUtilization;

        @Schema(description = "资源瓶颈")
        private List<String> resourceBottlenecks;

        @Schema(description = "资源冲突数")
        private Integer resourceConflicts;
    }

    /**
     * 质量指标
     */
    @Data
    @Schema(description = "质量指标")
    public static class QualityMetrics {
        @Schema(description = "缺陷数量")
        private Integer defectCount;

        @Schema(description = "缺陷密度")
        private BigDecimal defectDensity;

        @Schema(description = "返工率")
        private BigDecimal reworkRate;

        @Schema(description = "客户满意度")
        private BigDecimal customerSatisfaction;

        @Schema(description = "质量评分")
        private BigDecimal qualityScore;
    }

    /**
     * 成本绩效
     */
    @Data
    @Schema(description = "成本绩效")
    public static class CostPerformance {
        @Schema(description = "预算")
        private BigDecimal budget;

        @Schema(description = "实际成本")
        private BigDecimal actualCost;

        @Schema(description = "成本偏差")
        private BigDecimal costVariance;

        @Schema(description = "成本偏差率")
        private BigDecimal costVarianceRate;

        @Schema(description = "成本绩效指数")
        private BigDecimal costPerformanceIndex;

        @Schema(description = "进度绩效指数")
        private BigDecimal schedulePerformanceIndex;

        @Schema(description = "预计完工成本")
        private BigDecimal estimateAtCompletion;
    }
}