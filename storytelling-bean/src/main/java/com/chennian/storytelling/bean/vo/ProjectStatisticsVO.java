package com.chennian.storytelling.bean.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

/**
 * 项目统计VO
 *
 * @author storytelling
 * @date 2024-01-01
 */
@Data
@Schema(description = "项目统计VO")
public class ProjectStatisticsVO {

    @Schema(description = "项目总数")
    private Integer totalProjects;

    @Schema(description = "进行中项目数")
    private Integer activeProjects;

    @Schema(description = "已完成项目数")
    private Integer completedProjects;

    @Schema(description = "已暂停项目数")
    private Integer pausedProjects;

    @Schema(description = "已取消项目数")
    private Integer cancelledProjects;

    @Schema(description = "超期项目数")
    private Integer overdueProjects;

    @Schema(description = "即将到期项目数")
    private Integer dueSoonProjects;

    @Schema(description = "项目完成率")
    private BigDecimal completionRate;

    @Schema(description = "平均项目进度")
    private BigDecimal averageProgress;

    @Schema(description = "总预算")
    private BigDecimal totalBudget;

    @Schema(description = "已使用预算")
    private BigDecimal usedBudget;

    @Schema(description = "预算使用率")
    private BigDecimal budgetUtilizationRate;

    @Schema(description = "按状态统计")
    private List<StatusStatistics> statusStatistics;

    @Schema(description = "按类型统计")
    private List<TypeStatistics> typeStatistics;

    @Schema(description = "按优先级统计")
    private List<PriorityStatistics> priorityStatistics;

    @Schema(description = "按部门统计")
    private List<DepartmentStatistics> departmentStatistics;

    @Schema(description = "项目进度趋势")
    private List<ProgressTrend> progressTrends;

    @Schema(description = "项目成本趋势")
    private List<CostTrend> costTrends;

    @Schema(description = "风险统计")
    private RiskStatistics riskStatistics;

    @Schema(description = "资源统计")
    private ResourceStatistics resourceStatistics;

    @Schema(description = "任务统计")
    private TaskStatistics taskStatistics;

    @Schema(description = "里程碑统计")
    private MilestoneStatistics milestoneStatistics;

    /**
     * 状态统计
     */
    @Data
    @Schema(description = "状态统计")
    public static class StatusStatistics {
        @Schema(description = "状态")
        private Integer status;

        @Schema(description = "状态名称")
        private String statusName;

        @Schema(description = "项目数量")
        private Integer count;

        @Schema(description = "占比")
        private BigDecimal percentage;
    }

    /**
     * 类型统计
     */
    @Data
    @Schema(description = "类型统计")
    public static class TypeStatistics {
        @Schema(description = "类型")
        private Integer type;

        @Schema(description = "类型名称")
        private String typeName;

        @Schema(description = "项目数量")
        private Integer count;

        @Schema(description = "占比")
        private BigDecimal percentage;
    }

    /**
     * 优先级统计
     */
    @Data
    @Schema(description = "优先级统计")
    public static class PriorityStatistics {
        @Schema(description = "优先级")
        private Integer priority;

        @Schema(description = "优先级名称")
        private String priorityName;

        @Schema(description = "项目数量")
        private Integer count;

        @Schema(description = "占比")
        private BigDecimal percentage;
    }

    /**
     * 部门统计
     */
    @Data
    @Schema(description = "部门统计")
    public static class DepartmentStatistics {
        @Schema(description = "部门ID")
        private Long deptId;

        @Schema(description = "部门名称")
        private String deptName;

        @Schema(description = "项目数量")
        private Integer count;

        @Schema(description = "占比")
        private BigDecimal percentage;

        @Schema(description = "完成项目数")
        private Integer completedCount;

        @Schema(description = "完成率")
        private BigDecimal completionRate;
    }

    /**
     * 进度趋势
     */
    @Data
    @Schema(description = "进度趋势")
    public static class ProgressTrend {
        @Schema(description = "时间")
        private String period;

        @Schema(description = "平均进度")
        private BigDecimal averageProgress;

        @Schema(description = "完成项目数")
        private Integer completedProjects;

        @Schema(description = "新增项目数")
        private Integer newProjects;
    }

    /**
     * 成本趋势
     */
    @Data
    @Schema(description = "成本趋势")
    public static class CostTrend {
        @Schema(description = "时间")
        private String period;

        @Schema(description = "预算总额")
        private BigDecimal totalBudget;

        @Schema(description = "实际花费")
        private BigDecimal actualCost;

        @Schema(description = "成本偏差")
        private BigDecimal costVariance;

        @Schema(description = "成本偏差率")
        private BigDecimal costVarianceRate;
    }

    /**
     * 风险统计
     */
    @Data
    @Schema(description = "风险统计")
    public static class RiskStatistics {
        @Schema(description = "风险总数")
        private Integer totalRisks;

        @Schema(description = "高风险数量")
        private Integer highRisks;

        @Schema(description = "中风险数量")
        private Integer mediumRisks;

        @Schema(description = "低风险数量")
        private Integer lowRisks;

        @Schema(description = "已处理风险数")
        private Integer resolvedRisks;

        @Schema(description = "风险处理率")
        private BigDecimal riskResolutionRate;
    }

    /**
     * 资源统计
     */
    @Data
    @Schema(description = "资源统计")
    public static class ResourceStatistics {
        @Schema(description = "资源总数")
        private Integer totalResources;

        @Schema(description = "已分配资源数")
        private Integer allocatedResources;

        @Schema(description = "可用资源数")
        private Integer availableResources;

        @Schema(description = "资源使用率")
        private BigDecimal resourceUtilizationRate;

        @Schema(description = "资源成本")
        private BigDecimal resourceCost;
    }

    /**
     * 任务统计
     */
    @Data
    @Schema(description = "任务统计")
    public static class TaskStatistics {
        @Schema(description = "任务总数")
        private Integer totalTasks;

        @Schema(description = "已完成任务数")
        private Integer completedTasks;

        @Schema(description = "进行中任务数")
        private Integer activeTasks;

        @Schema(description = "超期任务数")
        private Integer overdueTasks;

        @Schema(description = "任务完成率")
        private BigDecimal taskCompletionRate;

        @Schema(description = "平均任务工时")
        private BigDecimal averageTaskHours;
    }

    /**
     * 里程碑统计
     */
    @Data
    @Schema(description = "里程碑统计")
    public static class MilestoneStatistics {
        @Schema(description = "里程碑总数")
        private Integer totalMilestones;

        @Schema(description = "已达成里程碑数")
        private Integer achievedMilestones;

        @Schema(description = "未达成里程碑数")
        private Integer unachievedMilestones;

        @Schema(description = "超期里程碑数")
        private Integer overdueMilestones;

        @Schema(description = "里程碑达成率")
        private BigDecimal milestoneAchievementRate;
    }
}