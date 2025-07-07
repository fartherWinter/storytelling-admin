package com.chennian.storytelling.bean.dto;

import java.io.Serializable;
import java.util.Date;

/**
 * 实例统计DTO
 * 
 * @author storytelling
 * @date 2024-01-01
 */
public class InstanceStatisticsDTO implements Serializable {
    
    private static final long serialVersionUID = 1L;
    
    /** 流程定义键 */
    private String processDefinitionKey;
    
    /** 流程定义名称 */
    private String processDefinitionName;
    
    /** 发起人 */
    private String startUser;
    
    /** 发起部门 */
    private String dept;
    
    /** 实例状态 */
    private String status;
    
    /** 统计数量 */
    private Long count;
    
    /** 平均持续时间(分钟) */
    private Long avgDuration;
    
    /** 百分比 */
    private Double percentage;
    
    /** 备注 */
    private String remark;
    
    /** 总数量 */
    private Integer totalCount;
    
    /** 运行中数量 */
    private Integer runningCount;
    
    /** 已完成数量 */
    private Integer completedCount;
    
    /** 暂停数量 */
    private Integer suspendedCount;
    
    /** 终止数量 */
    private Integer terminatedCount;
    
    /** 今日启动数量 */
    private Integer todayStartedCount;
    
    /** 今日完成数量 */
    private Integer todayCompletedCount;
    
    /** 逾期数量 */
    private Integer overdueCount;
    
    /** 高优先级数量 */
    private Integer highPriorityCount;
    
    /** 平均处理时间 */
    private Double avgProcessingTime;
    
    public InstanceStatisticsDTO() {
    }
    
    public InstanceStatisticsDTO(String processDefinitionKey, String status, Long count) {
        this.processDefinitionKey = processDefinitionKey;
        this.status = status;
        this.count = count;
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
    
    public String getStartUser() {
        return startUser;
    }
    
    public void setStartUser(String startUser) {
        this.startUser = startUser;
    }
    
    public String getDept() {
        return dept;
    }
    
    public void setDept(String dept) {
        this.dept = dept;
    }
    
    public String getStatus() {
        return status;
    }
    
    public void setStatus(String status) {
        this.status = status;
    }
    
    public Long getCount() {
        return count;
    }
    
    public void setCount(Long count) {
        this.count = count;
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
    
    public Integer getRunningCount() {
        return runningCount;
    }
    
    public void setRunningCount(Integer runningCount) {
        this.runningCount = runningCount;
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
    
    public Integer getTerminatedCount() {
        return terminatedCount;
    }
    
    public void setTerminatedCount(Integer terminatedCount) {
        this.terminatedCount = terminatedCount;
    }
    
    public Integer getTodayStartedCount() {
        return todayStartedCount;
    }
    
    public void setTodayStartedCount(Integer todayStartedCount) {
        this.todayStartedCount = todayStartedCount;
    }
    
    public Integer getTodayCompletedCount() {
        return todayCompletedCount;
    }
    
    public void setTodayCompletedCount(Integer todayCompletedCount) {
        this.todayCompletedCount = todayCompletedCount;
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
    
    public Double getAvgProcessingTime() {
        return avgProcessingTime;
    }
    
    public void setAvgProcessingTime(Double avgProcessingTime) {
        this.avgProcessingTime = avgProcessingTime;
    }
    
    @Override
    public String toString() {
        return "InstanceStatisticsDTO{" +
                "processDefinitionKey='" + processDefinitionKey + '\'' +
                ", processDefinitionName='" + processDefinitionName + '\'' +
                ", startUser='" + startUser + '\'' +
                ", dept='" + dept + '\'' +
                ", status='" + status + '\'' +
                ", count=" + count +
                ", avgDuration=" + avgDuration +
                ", percentage=" + percentage +
                ", remark='" + remark + '\'' +
                ", totalCount=" + totalCount +
                ", runningCount=" + runningCount +
                ", completedCount=" + completedCount +
                ", suspendedCount=" + suspendedCount +
                ", terminatedCount=" + terminatedCount +
                ", todayStartedCount=" + todayStartedCount +
                ", todayCompletedCount=" + todayCompletedCount +
                ", overdueCount=" + overdueCount +
                ", highPriorityCount=" + highPriorityCount +
                ", avgProcessingTime=" + avgProcessingTime +
                '}';
    }
}