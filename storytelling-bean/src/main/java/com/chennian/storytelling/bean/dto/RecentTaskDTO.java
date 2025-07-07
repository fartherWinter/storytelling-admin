package com.chennian.storytelling.bean.dto;

import java.io.Serializable;
import java.util.Date;

/**
 * 最近任务DTO
 * 
 * @author storytelling
 * @date 2024-01-01
 */
public class RecentTaskDTO implements Serializable {
    
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
    
    /** 完成时间 */
    private Date endTime;
    
    /** 任务状态 */
    private String status;
    
    /** 持续时间(分钟) */
    private Long durationMinutes;
    
    /** 业务键 */
    private String businessKey;
    
    /** 备注 */
    private String remark;
    
    public RecentTaskDTO() {
    }
    
    public RecentTaskDTO(String id, String name, String assignee) {
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
    
    public Date getEndTime() {
        return endTime;
    }
    
    public void setEndTime(Date endTime) {
        this.endTime = endTime;
    }
    
    public String getStatus() {
        return status;
    }
    
    public void setStatus(String status) {
        this.status = status;
    }
    
    public Long getDurationMinutes() {
        return durationMinutes;
    }
    
    public void setDurationMinutes(Long durationMinutes) {
        this.durationMinutes = durationMinutes;
    }
    
    public String getBusinessKey() {
        return businessKey;
    }
    
    public void setBusinessKey(String businessKey) {
        this.businessKey = businessKey;
    }
    
    public String getRemark() {
        return remark;
    }
    
    public void setRemark(String remark) {
        this.remark = remark;
    }
    
    @Override
    public String toString() {
        return "RecentTaskDTO{" +
                "id='" + id + '\'' +
                ", name='" + name + '\'' +
                ", assignee='" + assignee + '\'' +
                ", processInstanceId='" + processInstanceId + '\'' +
                ", processDefinitionKey='" + processDefinitionKey + '\'' +
                ", processDefinitionName='" + processDefinitionName + '\'' +
                ", createTime=" + createTime +
                ", endTime=" + endTime +
                ", status='" + status + '\'' +
                ", durationMinutes=" + durationMinutes +
                ", businessKey='" + businessKey + '\'' +
                ", remark='" + remark + '\'' +
                '}';
    }
}