package com.chennian.storytelling.bean.dto;

import java.io.Serializable;

/**
 * 任务统计信息DTO
 * 
 * @author storytelling
 * @date 2024-01-01
 */
public class TaskStatisticsDTO implements Serializable {
    
    private static final long serialVersionUID = 1L;
    
    /** 状态 */
    private String status;
    
    /** 数量 */
    private Integer count;
    
    /** 分配人 */
    private String assignee;
    
    /** 任务类型 */
    private String taskType;
    
    /** 流程定义键 */
    private String processDefinitionKey;
    
    /** 流程定义名称 */
    private String processDefinitionName;
    
    /** 平均持续时间(毫秒) */
    private Long avgDuration;
    
    /** 百分比 */
    private Double percentage;
    
    /** 备注 */
    private String remark;
    
    /** 总任务数 */
    private Integer totalCount;
    
    /** 已创建任务数 */
    private Integer createdCount;
    
    /** 就绪任务数 */
    private Integer readyCount;
    
    /** 进行中任务数 */
    private Integer inProgressCount;
    
    /** 已完成任务数 */
    private Integer completedCount;
    
    /** 暂停任务数 */
    private Integer suspendedCount;
    
    /** 逾期任务数 */
    private Integer overdueCount;
    
    /** 高优先级任务数 */
    private Integer highPriorityCount;
    
    /** 我的任务数 */
    private Integer myTasksCount;
    
    /** 今日完成任务数 */
    private Integer todayCompletedCount;
    
    public TaskStatisticsDTO() {
    }
    
    public TaskStatisticsDTO(String status, Integer count) {
        this.status = status;
        this.count = count;
    }
    
    public TaskStatisticsDTO(String assignee, Integer count, String remark) {
        this.assignee = assignee;
        this.count = count;
        this.remark = remark;
    }
    
    public String getStatus() {
        return status;
    }
    
    public void setStatus(String status) {
        this.status = status;
    }
    
    public Integer getCount() {
        return count;
    }
    
    public void setCount(Integer count) {
        this.count = count;
    }
    
    public String getAssignee() {
        return assignee;
    }
    
    public void setAssignee(String assignee) {
        this.assignee = assignee;
    }
    
    public String getTaskType() {
        return taskType;
    }
    
    public void setTaskType(String taskType) {
        this.taskType = taskType;
    }
    
    public String getProcessDefinitionKey() {
        return processDefinitionKey;
    }
    
    public void setProcessDefinitionKey(String processDefinitionKey) {
        this.processDefinitionKey = processDefinitionKey;
    }
    
    public String getProcessDefinitionName() {
        return processDefinitionName;
    }
    
    public void setProcessDefinitionName(String processDefinitionName) {
        this.processDefinitionName = processDefinitionName;
    }
    
    public Long getAvgDuration() {
        return avgDuration;
    }
    
    public void setAvgDuration(Long avgDuration) {
        this.avgDuration = avgDuration;
    }
    
    public Double getPercentage() {
        return percentage;
    }
    
    public void setPercentage(Double percentage) {
        this.percentage = percentage;
    }
    
    public String getRemark() {
        return remark;
    }
    
    public void setRemark(String remark) {
        this.remark = remark;
    }
    
    public Integer getTotalCount() {
        return totalCount;
    }
    
    public void setTotalCount(Integer totalCount) {
        this.totalCount = totalCount;
    }
    
    public Integer getCreatedCount() {
        return createdCount;
    }
    
    public void setCreatedCount(Integer createdCount) {
        this.createdCount = createdCount;
    }
    
    public Integer getReadyCount() {
        return readyCount;
    }
    
    public void setReadyCount(Integer readyCount) {
        this.readyCount = readyCount;
    }
    
    public Integer getInProgressCount() {
        return inProgressCount;
    }
    
    public void setInProgressCount(Integer inProgressCount) {
        this.inProgressCount = inProgressCount;
    }
    
    public Integer getCompletedCount() {
        return completedCount;
    }
    
    public void setCompletedCount(Integer completedCount) {
        this.completedCount = completedCount;
    }
    
    public Integer getSuspendedCount() {
        return suspendedCount;
    }
    
    public void setSuspendedCount(Integer suspendedCount) {
        this.suspendedCount = suspendedCount;
    }
    
    public Integer getOverdueCount() {
        return overdueCount;
    }
    
    public void setOverdueCount(Integer overdueCount) {
        this.overdueCount = overdueCount;
    }
    
    public Integer getHighPriorityCount() {
        return highPriorityCount;
    }
    
    public void setHighPriorityCount(Integer highPriorityCount) {
        this.highPriorityCount = highPriorityCount;
    }
    
    public Integer getMyTasksCount() {
        return myTasksCount;
    }
    
    public void setMyTasksCount(Integer myTasksCount) {
        this.myTasksCount = myTasksCount;
    }
    
    public Integer getTodayCompletedCount() {
        return todayCompletedCount;
    }
    
    public void setTodayCompletedCount(Integer todayCompletedCount) {
        this.todayCompletedCount = todayCompletedCount;
    }
    
    @Override
    public String toString() {
        return "TaskStatisticsDTO{" +
                "status='" + status + '\'' +
                ", count=" + count +
                ", assignee='" + assignee + '\'' +
                ", taskType='" + taskType + '\'' +
                ", processDefinitionKey='" + processDefinitionKey + '\'' +
                ", processDefinitionName='" + processDefinitionName + '\'' +
                ", avgDuration=" + avgDuration +
                ", percentage=" + percentage +
                ", remark='" + remark + '\'' +
                ", totalCount=" + totalCount +
                ", createdCount=" + createdCount +
                ", readyCount=" + readyCount +
                ", inProgressCount=" + inProgressCount +
                ", completedCount=" + completedCount +
                ", suspendedCount=" + suspendedCount +
                ", overdueCount=" + overdueCount +
                ", highPriorityCount=" + highPriorityCount +
                ", myTasksCount=" + myTasksCount +
                ", todayCompletedCount=" + todayCompletedCount +
                '}';
    }
}