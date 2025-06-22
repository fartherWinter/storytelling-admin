package com.chennian.storytelling.bean.dto;

import java.io.Serializable;
import java.util.Date;

/**
 * 任务数据传输对象
 * 
 * @author chennian
 */
public class TaskDTO implements Serializable {

    private static final long serialVersionUID = 1L;

    /** 任务ID */
    private String taskId;
    
    /** 任务名称 */
    private String taskName;
    
    /** 任务定义KEY */
    private String taskKey;
    
    /** 流程实例ID */
    private String processInstanceId;
    
    /** 流程定义ID */
    private String processDefinitionId;
    
    /** 流程定义名称 */
    private String processDefinitionName;
    
    /** 流程定义KEY */
    private String processDefinitionKey;
    
    /** 业务键 */
    private String businessKey;
    
    /** 业务类型 */
    private String businessType;
    
    /** 任务处理人 */
    private String assignee;
    
    /** 任务创建时间 */
    private Date createTime;
    
    /** 任务状态 */
    private String status;
    
    /** 流程标题 */
    private String title;
    
    /** 审批意见 */
    private String comment;
    
    /** 电子签名图片（Base64或URL） */
    private String signatureImage;
    /** 签名时间 */
    private Date signatureTime;

    public String getTaskId() {
        return taskId;
    }

    public void setTaskId(String taskId) {
        this.taskId = taskId;
    }

    public String getTaskName() {
        return taskName;
    }

    public void setTaskName(String taskName) {
        this.taskName = taskName;
    }

    public String getTaskKey() {
        return taskKey;
    }

    public void setTaskKey(String taskKey) {
        this.taskKey = taskKey;
    }

    public String getProcessInstanceId() {
        return processInstanceId;
    }

    public void setProcessInstanceId(String processInstanceId) {
        this.processInstanceId = processInstanceId;
    }

    public String getProcessDefinitionId() {
        return processDefinitionId;
    }

    public void setProcessDefinitionId(String processDefinitionId) {
        this.processDefinitionId = processDefinitionId;
    }

    public String getProcessDefinitionName() {
        return processDefinitionName;
    }

    public void setProcessDefinitionName(String processDefinitionName) {
        this.processDefinitionName = processDefinitionName;
    }

    public String getProcessDefinitionKey() {
        return processDefinitionKey;
    }

    public void setProcessDefinitionKey(String processDefinitionKey) {
        this.processDefinitionKey = processDefinitionKey;
    }

    public String getBusinessKey() {
        return businessKey;
    }

    public void setBusinessKey(String businessKey) {
        this.businessKey = businessKey;
    }

    public String getBusinessType() {
        return businessType;
    }

    public void setBusinessType(String businessType) {
        this.businessType = businessType;
    }

    public String getAssignee() {
        return assignee;
    }

    public void setAssignee(String assignee) {
        this.assignee = assignee;
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    public String getSignatureImage() {
        return signatureImage;
    }
    public void setSignatureImage(String signatureImage) {
        this.signatureImage = signatureImage;
    }
    public Date getSignatureTime() {
        return signatureTime;
    }
    public void setSignatureTime(Date signatureTime) {
        this.signatureTime = signatureTime;
    }
}