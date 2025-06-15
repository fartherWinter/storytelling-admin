package com.chennian.storytelling.bean.model.workflow;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

import java.io.Serializable;
import java.util.Date;

/**
 * 工作流任务实体类
 * 
 * @author storytelling
 * @date 2024-01-01
 */
@ApiModel("工作流任务")
@TableName("wf_task")
public class WfTask implements Serializable {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty("任务ID")
    @TableId(value = "id", type = IdType.ASSIGN_ID)
    private String id;

    @ApiModelProperty("流程实例ID")
    private String processInstanceId;

    @ApiModelProperty("流程定义ID")
    private String processDefinitionId;

    @ApiModelProperty("流程定义键")
    private String processDefinitionKey;

    @ApiModelProperty("流程定义名称")
    private String processDefinitionName;

    @ApiModelProperty("任务定义键")
    private String taskDefinitionKey;

    @ApiModelProperty("任务名称")
    private String taskName;

    @ApiModelProperty("任务描述")
    private String taskDescription;

    @ApiModelProperty("任务类型(USER_TASK,SERVICE_TASK,SCRIPT_TASK,MANUAL_TASK)")
    private String taskType;

    @ApiModelProperty("任务状态(CREATED,READY,RESERVED,IN_PROGRESS,SUSPENDED,COMPLETED,FAILED,ERROR,EXITED,OBSOLETE)")
    private String taskStatus;

    @ApiModelProperty("分配人ID")
    private String assignee;

    @ApiModelProperty("分配人姓名")
    private String assigneeName;

    @ApiModelProperty("候选人(多个用逗号分隔)")
    private String candidateUsers;

    @ApiModelProperty("候选组(多个用逗号分隔)")
    private String candidateGroups;

    @ApiModelProperty("委托人ID")
    private String owner;

    @ApiModelProperty("委托人姓名")
    private String ownerName;

    @ApiModelProperty("父任务ID")
    private String parentTaskId;

    @ApiModelProperty("优先级(1-低,2-中,3-高,4-紧急)")
    private Integer priority;

    @ApiModelProperty("到期时间")
    private Date dueDate;

    @ApiModelProperty("跟进时间")
    private Date followUpDate;

    @ApiModelProperty("表单键")
    private String formKey;

    @ApiModelProperty("任务变量(JSON格式)")
    private String taskVariables;

    @ApiModelProperty("执行ID")
    private String executionId;

    @ApiModelProperty("活动实例ID")
    private String activityInstanceId;

    @ApiModelProperty("租户ID")
    private String tenantId;

    @ApiModelProperty("业务键")
    private String businessKey;

    @ApiModelProperty("创建时间")
    private Date createTime;

    @ApiModelProperty("开始时间")
    private Date startTime;

    @ApiModelProperty("认领时间")
    private Date claimTime;

    @ApiModelProperty("结束时间")
    private Date endTime;

    @ApiModelProperty("持续时间(毫秒)")
    private Long duration;

    @ApiModelProperty("删除原因")
    private String deleteReason;

    @ApiModelProperty("挂起时间")
    private Date suspensionTime;

    @ApiModelProperty("创建人")
    private String createdBy;

    @ApiModelProperty("更新人")
    private String updatedBy;

    @ApiModelProperty("更新时间")
    private Date updatedTime;

    public WfTask() {
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
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

    public String getTaskDefinitionKey() {
        return taskDefinitionKey;
    }

    public void setTaskDefinitionKey(String taskDefinitionKey) {
        this.taskDefinitionKey = taskDefinitionKey;
    }

    public String getTaskName() {
        return taskName;
    }

    public void setTaskName(String taskName) {
        this.taskName = taskName;
    }

    public String getTaskDescription() {
        return taskDescription;
    }

    public void setTaskDescription(String taskDescription) {
        this.taskDescription = taskDescription;
    }

    public String getTaskType() {
        return taskType;
    }

    public void setTaskType(String taskType) {
        this.taskType = taskType;
    }

    public String getTaskStatus() {
        return taskStatus;
    }

    public void setTaskStatus(String taskStatus) {
        this.taskStatus = taskStatus;
    }

    public String getAssignee() {
        return assignee;
    }

    public void setAssignee(String assignee) {
        this.assignee = assignee;
    }

    public String getAssigneeName() {
        return assigneeName;
    }

    public void setAssigneeName(String assigneeName) {
        this.assigneeName = assigneeName;
    }

    public String getCandidateUsers() {
        return candidateUsers;
    }

    public void setCandidateUsers(String candidateUsers) {
        this.candidateUsers = candidateUsers;
    }

    public String getCandidateGroups() {
        return candidateGroups;
    }

    public void setCandidateGroups(String candidateGroups) {
        this.candidateGroups = candidateGroups;
    }

    public String getOwner() {
        return owner;
    }

    public void setOwner(String owner) {
        this.owner = owner;
    }

    public String getOwnerName() {
        return ownerName;
    }

    public void setOwnerName(String ownerName) {
        this.ownerName = ownerName;
    }

    public String getParentTaskId() {
        return parentTaskId;
    }

    public void setParentTaskId(String parentTaskId) {
        this.parentTaskId = parentTaskId;
    }

    public Integer getPriority() {
        return priority;
    }

    public void setPriority(Integer priority) {
        this.priority = priority;
    }

    public Date getDueDate() {
        return dueDate;
    }

    public void setDueDate(Date dueDate) {
        this.dueDate = dueDate;
    }

    public Date getFollowUpDate() {
        return followUpDate;
    }

    public void setFollowUpDate(Date followUpDate) {
        this.followUpDate = followUpDate;
    }

    public String getFormKey() {
        return formKey;
    }

    public void setFormKey(String formKey) {
        this.formKey = formKey;
    }

    public String getTaskVariables() {
        return taskVariables;
    }

    public void setTaskVariables(String taskVariables) {
        this.taskVariables = taskVariables;
    }

    public String getExecutionId() {
        return executionId;
    }

    public void setExecutionId(String executionId) {
        this.executionId = executionId;
    }

    public String getActivityInstanceId() {
        return activityInstanceId;
    }

    public void setActivityInstanceId(String activityInstanceId) {
        this.activityInstanceId = activityInstanceId;
    }

    public String getTenantId() {
        return tenantId;
    }

    public void setTenantId(String tenantId) {
        this.tenantId = tenantId;
    }

    public String getBusinessKey() {
        return businessKey;
    }

    public void setBusinessKey(String businessKey) {
        this.businessKey = businessKey;
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    public Date getStartTime() {
        return startTime;
    }

    public void setStartTime(Date startTime) {
        this.startTime = startTime;
    }

    public Date getClaimTime() {
        return claimTime;
    }

    public void setClaimTime(Date claimTime) {
        this.claimTime = claimTime;
    }

    public Date getEndTime() {
        return endTime;
    }

    public void setEndTime(Date endTime) {
        this.endTime = endTime;
    }

    public Long getDuration() {
        return duration;
    }

    public void setDuration(Long duration) {
        this.duration = duration;
    }

    public String getDeleteReason() {
        return deleteReason;
    }

    public void setDeleteReason(String deleteReason) {
        this.deleteReason = deleteReason;
    }

    public Date getSuspensionTime() {
        return suspensionTime;
    }

    public void setSuspensionTime(Date suspensionTime) {
        this.suspensionTime = suspensionTime;
    }

    public String getCreatedBy() {
        return createdBy;
    }

    public void setCreatedBy(String createdBy) {
        this.createdBy = createdBy;
    }

    public String getUpdatedBy() {
        return updatedBy;
    }

    public void setUpdatedBy(String updatedBy) {
        this.updatedBy = updatedBy;
    }

    public Date getUpdatedTime() {
        return updatedTime;
    }

    public void setUpdatedTime(Date updatedTime) {
        this.updatedTime = updatedTime;
    }

    @Override
    public String toString() {
        return "WfTask{" +
                "id='" + id + '\'' +
                ", processInstanceId='" + processInstanceId + '\'' +
                ", processDefinitionId='" + processDefinitionId + '\'' +
                ", processDefinitionKey='" + processDefinitionKey + '\'' +
                ", processDefinitionName='" + processDefinitionName + '\'' +
                ", taskDefinitionKey='" + taskDefinitionKey + '\'' +
                ", taskName='" + taskName + '\'' +
                ", taskDescription='" + taskDescription + '\'' +
                ", taskType='" + taskType + '\'' +
                ", taskStatus='" + taskStatus + '\'' +
                ", assignee='" + assignee + '\'' +
                ", assigneeName='" + assigneeName + '\'' +
                ", candidateUsers='" + candidateUsers + '\'' +
                ", candidateGroups='" + candidateGroups + '\'' +
                ", owner='" + owner + '\'' +
                ", ownerName='" + ownerName + '\'' +
                ", parentTaskId='" + parentTaskId + '\'' +
                ", priority=" + priority +
                ", dueDate=" + dueDate +
                ", followUpDate=" + followUpDate +
                ", formKey='" + formKey + '\'' +
                ", taskVariables='" + taskVariables + '\'' +
                ", executionId='" + executionId + '\'' +
                ", activityInstanceId='" + activityInstanceId + '\'' +
                ", tenantId='" + tenantId + '\'' +
                ", businessKey='" + businessKey + '\'' +
                ", createTime=" + createTime +
                ", startTime=" + startTime +
                ", claimTime=" + claimTime +
                ", endTime=" + endTime +
                ", duration=" + duration +
                ", deleteReason='" + deleteReason + '\'' +
                ", suspensionTime=" + suspensionTime +
                ", createdBy='" + createdBy + '\'' +
                ", updatedBy='" + updatedBy + '\'' +
                ", updatedTime=" + updatedTime +
                '}';
    }
}