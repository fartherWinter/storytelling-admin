package com.chennian.storytelling.bean.dto;

import java.io.Serializable;

/**
 * 平均处理时间DTO
 * 
 * @author storytelling
 * @date 2024-01-01
 */
public class AverageProcessingTimeDTO implements Serializable {
    
    private static final long serialVersionUID = 1L;
    
    /** 流程定义键 */
    private String processDefinitionKey;
    
    /** 流程定义名称 */
    private String processDefinitionName;
    
    /** 分配人 */
    private String assignee;
    
    /** 平均处理时间(分钟) */
    private Long avgTime;
    
    /** 处理任务数量 */
    private Long count;
    
    /** 时间段 */
    private String period;
    
    /** 备注 */
    private String remark;
    
    public AverageProcessingTimeDTO() {
    }
    
    public AverageProcessingTimeDTO(String processDefinitionKey, String assignee, Long avgTime) {
        this.processDefinitionKey = processDefinitionKey;
        this.assignee = assignee;
        this.avgTime = avgTime;
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
    
    public String getAssignee() {
        return assignee;
    }
    
    public void setAssignee(String assignee) {
        this.assignee = assignee;
    }
    
    public Long getAvgTime() {
        return avgTime;
    }
    
    public void setAvgTime(Long avgTime) {
        this.avgTime = avgTime;
    }
    
    public Long getCount() {
        return count;
    }
    
    public void setCount(Long count) {
        this.count = count;
    }
    
    public String getPeriod() {
        return period;
    }
    
    public void setPeriod(String period) {
        this.period = period;
    }
    
    public String getRemark() {
        return remark;
    }
    
    public void setRemark(String remark) {
        this.remark = remark;
    }
    
    @Override
    public String toString() {
        return "AverageProcessingTimeDTO{" +
                "processDefinitionKey='" + processDefinitionKey + '\'' +
                ", processDefinitionName='" + processDefinitionName + '\'' +
                ", assignee='" + assignee + '\'' +
                ", avgTime=" + avgTime +
                ", count=" + count +
                ", period='" + period + '\'' +
                ", remark='" + remark + '\'' +
                '}';
    }
}