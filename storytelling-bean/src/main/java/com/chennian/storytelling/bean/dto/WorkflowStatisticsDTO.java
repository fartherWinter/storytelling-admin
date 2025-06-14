package com.chennian.storytelling.bean.dto;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.Map;

/**
 * 工作流统计数据传输对象
 * 
 * @author chennian
 */
public class WorkflowStatisticsDTO implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 统计时间范围开始
     */
    private LocalDateTime startTime;

    /**
     * 统计时间范围结束
     */
    private LocalDateTime endTime;

    /**
     * 总流程实例数
     */
    private Long totalProcessInstances;

    /**
     * 运行中的流程实例数
     */
    private Long runningProcessInstances;

    /**
     * 已完成的流程实例数
     */
    private Long completedProcessInstances;

    /**
     * 已终止的流程实例数
     */
    private Long terminatedProcessInstances;

    /**
     * 总任务数
     */
    private Long totalTasks;

    /**
     * 待办任务数
     */
    private Long pendingTasks;

    /**
     * 已完成任务数
     */
    private Long completedTasks;

    /**
     * 超时任务数
     */
    private Long overdueTasks;

    /**
     * 平均流程处理时长（毫秒）
     */
    private Long averageProcessDuration;

    /**
     * 平均任务处理时长（毫秒）
     */
    private Long averageTaskDuration;

    /**
     * 按流程定义分组的统计
     * Key: 流程定义Key, Value: 实例数量
     */
    private Map<String, Long> processDefinitionStats;

    /**
     * 按用户分组的任务统计
     * Key: 用户ID, Value: 任务数量
     */
    private Map<String, Long> userTaskStats;

    /**
     * 按业务类型分组的统计
     * Key: 业务类型, Value: 实例数量
     */
    private Map<String, Long> businessTypeStats;

    /**
     * 流程完成率
     */
    private Double completionRate;

    /**
     * 任务完成率
     */
    private Double taskCompletionRate;

    // 构造函数
    public WorkflowStatisticsDTO() {}

    public WorkflowStatisticsDTO(LocalDateTime startTime, LocalDateTime endTime) {
        this.startTime = startTime;
        this.endTime = endTime;
    }

    // Getter和Setter方法
    public LocalDateTime getStartTime() {
        return startTime;
    }

    public void setStartTime(LocalDateTime startTime) {
        this.startTime = startTime;
    }

    public LocalDateTime getEndTime() {
        return endTime;
    }

    public void setEndTime(LocalDateTime endTime) {
        this.endTime = endTime;
    }

    public Long getTotalProcessInstances() {
        return totalProcessInstances;
    }

    public void setTotalProcessInstances(Long totalProcessInstances) {
        this.totalProcessInstances = totalProcessInstances;
    }

    public Long getRunningProcessInstances() {
        return runningProcessInstances;
    }

    public void setRunningProcessInstances(Long runningProcessInstances) {
        this.runningProcessInstances = runningProcessInstances;
    }

    public Long getCompletedProcessInstances() {
        return completedProcessInstances;
    }

    public void setCompletedProcessInstances(Long completedProcessInstances) {
        this.completedProcessInstances = completedProcessInstances;
    }

    public Long getTerminatedProcessInstances() {
        return terminatedProcessInstances;
    }

    public void setTerminatedProcessInstances(Long terminatedProcessInstances) {
        this.terminatedProcessInstances = terminatedProcessInstances;
    }

    public Long getTotalTasks() {
        return totalTasks;
    }

    public void setTotalTasks(Long totalTasks) {
        this.totalTasks = totalTasks;
    }

    public Long getPendingTasks() {
        return pendingTasks;
    }

    public void setPendingTasks(Long pendingTasks) {
        this.pendingTasks = pendingTasks;
    }

    public Long getCompletedTasks() {
        return completedTasks;
    }

    public void setCompletedTasks(Long completedTasks) {
        this.completedTasks = completedTasks;
    }

    public Long getOverdueTasks() {
        return overdueTasks;
    }

    public void setOverdueTasks(Long overdueTasks) {
        this.overdueTasks = overdueTasks;
    }

    public Long getAverageProcessDuration() {
        return averageProcessDuration;
    }

    public void setAverageProcessDuration(Long averageProcessDuration) {
        this.averageProcessDuration = averageProcessDuration;
    }

    public Long getAverageTaskDuration() {
        return averageTaskDuration;
    }

    public void setAverageTaskDuration(Long averageTaskDuration) {
        this.averageTaskDuration = averageTaskDuration;
    }

    public Map<String, Long> getProcessDefinitionStats() {
        return processDefinitionStats;
    }

    public void setProcessDefinitionStats(Map<String, Long> processDefinitionStats) {
        this.processDefinitionStats = processDefinitionStats;
    }

    public Map<String, Long> getUserTaskStats() {
        return userTaskStats;
    }

    public void setUserTaskStats(Map<String, Long> userTaskStats) {
        this.userTaskStats = userTaskStats;
    }

    public Map<String, Long> getBusinessTypeStats() {
        return businessTypeStats;
    }

    public void setBusinessTypeStats(Map<String, Long> businessTypeStats) {
        this.businessTypeStats = businessTypeStats;
    }

    public Double getCompletionRate() {
        return completionRate;
    }

    public void setCompletionRate(Double completionRate) {
        this.completionRate = completionRate;
    }

    public Double getTaskCompletionRate() {
        return taskCompletionRate;
    }

    public void setTaskCompletionRate(Double taskCompletionRate) {
        this.taskCompletionRate = taskCompletionRate;
    }

    @Override
    public String toString() {
        return "WorkflowStatisticsDTO{" +
                "startTime=" + startTime +
                ", endTime=" + endTime +
                ", totalProcessInstances=" + totalProcessInstances +
                ", runningProcessInstances=" + runningProcessInstances +
                ", completedProcessInstances=" + completedProcessInstances +
                ", terminatedProcessInstances=" + terminatedProcessInstances +
                ", totalTasks=" + totalTasks +
                ", pendingTasks=" + pendingTasks +
                ", completedTasks=" + completedTasks +
                ", overdueTasks=" + overdueTasks +
                ", averageProcessDuration=" + averageProcessDuration +
                ", averageTaskDuration=" + averageTaskDuration +
                ", completionRate=" + completionRate +
                ", taskCompletionRate=" + taskCompletionRate +
                '}';
    }


}