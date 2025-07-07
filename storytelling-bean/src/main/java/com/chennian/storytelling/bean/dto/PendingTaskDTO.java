package com.chennian.storytelling.bean.dto;

import java.io.Serializable;
import java.util.Date;

/**
 * 待处理任务统计DTO
 * 
 * @author storytelling
 * @date 2024-01-01
 */
public class PendingTaskDTO implements Serializable {
    
    private static final long serialVersionUID = 1L;
    
    /** 任务ID */
    private String id;
    
    /** 任务名称 */
    private String name;
    
    /** 分配人 */
    private String assignee;
    
    /** 流程实例ID */
    private String processInstanceId;
    
    /** 流程定义键 */
    private String processDefinitionKey;
    
    /** 流程定义名称 */
    private String processDefinitionName;
    
    /** 创建时间 */
    private Date createTime;
    
    /** 到期时间 */
    private Date dueDate;
    
    /** 优先级 */
    private Integer priority;
    
    /** 任务状态 */
    private String status;
    
    /** 等待时长(分钟) */
    private Long waitingMinutes;
    
    /** 备注 */
    private String remark;
    
    public PendingTaskDTO() {
    }
    
    public PendingTaskDTO(String id, String name, String assignee) {
        this.id = id;
        this.name = name;
        this.assignee = assignee;
    }
    
    public String getId() {
        return id;
    }
    
    public void setId(String id) {
        this.id = id;
    }
    
    public String getName() {
        return name;
    }
    
    public void setName(String name) {
        this.name = name;
    }
    
    public String getAssignee() {
        return assignee;
    }
    
    public void setAssignee(String assignee) {
        this.assignee = assignee;
    }
    
    public String getProcessInstanceId() {
        return processInstanceId;
    }
    
    public void setProcessInstanceId(String processInstanceId) {
        this.processInstanceId = processInstanceId;
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
    
    public Date getCreateTime() {
        return createTime;
    }
    
    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }
    
    public Date getDueDate() {
        return dueDate;
    }
    
    public void setDueDate(Date dueDate) {
        this.dueDate = dueDate;
    }
    
    public Integer getPriority() {
        return priority;
    }
    
    public void setPriority(Integer priority) {
        this.priority = priority;
    }
    
    public String getStatus() {
        return status;
    }
    
    public void setStatus(String status) {
        this.status = status;
    }
    
    public Long getWaitingMinutes() {
        return waitingMinutes;
    }
    
    public void setWaitingMinutes(Long waitingMinutes) {
        this.waitingMinutes = waitingMinutes;
    }
    
    public String getRemark() {
        return remark;
    }
    
    public void setRemark(String remark) {
        this.remark = remark;
    }
    
    @Override
    public String toString() {
        return "PendingTaskDTO{" +
                "id='" + id + '\'' +
                ", name='" + name + '\'' +
                ", assignee='" + assignee + '\'' +
                ", processInstanceId='" + processInstanceId + '\'' +
                ", processDefinitionKey='" + processDefinitionKey + '\'' +
                ", processDefinitionName='" + processDefinitionName + '\'' +
                ", createTime=" + createTime +
                ", dueDate=" + dueDate +
                ", priority=" + priority +
                ", status='" + status + '\'' +
                ", waitingMinutes=" + waitingMinutes +
                ", remark='" + remark + '\'' +
                '}';
    }
}