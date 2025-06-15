package com.chennian.storytelling.bean.model.workflow;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

import java.io.Serializable;
import java.util.Date;

/**
 * 工作流实例实体类
 * 
 * @author storytelling
 * @date 2024-01-01
 */
@ApiModel("工作流实例")
@TableName("wf_instance")
public class WfInstance implements Serializable {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty("实例ID")
    @TableId(value = "id", type = IdType.ASSIGN_ID)
    private String id;

    @ApiModelProperty("流程定义ID")
    @TableField(value = "process_definition_id")
    private String processDefinitionId;

    @ApiModelProperty("流程定义键")
    @TableField(value = "process_definition_key")
    private String processDefinitionKey;

    @ApiModelProperty("流程定义名称")
    @TableField(value = "process_definition_name")
    private String processDefinitionName;

    @ApiModelProperty("流程定义版本")
    @TableField(value = "process_definition_version")
    private Integer processDefinitionVersion;

    @ApiModelProperty("业务键")
    @TableField(value = "business_key")
    private String businessKey;

    @ApiModelProperty("实例名称")
    @TableField(value = "instance_name")
    private String instanceName;

    @ApiModelProperty("实例描述")
    @TableField(value = "instance_description")
    private String instanceDescription;

    @ApiModelProperty("发起人ID")
    @TableField(value = "start_user_id")
    private String startUserId;

    @ApiModelProperty("发起人姓名")
    @TableField(value = "start_user_name")
    private String startUserName;

    @ApiModelProperty("发起部门ID")
    @TableField(value = "start_dept_id")
    private String startDeptId;

    @ApiModelProperty("发起部门名称")
    @TableField(value = "start_dept_name")
    private String startDeptName;

    @ApiModelProperty("实例状态(RUNNING,COMPLETED,SUSPENDED,TERMINATED)")
    @TableField(value = "instance_status")
    private String instanceStatus;

    @ApiModelProperty("当前节点ID")
    @TableField(value = "current_node_id")
    private String currentNodeId;

    @ApiModelProperty("当前节点名称")
    @TableField(value = "current_node_name")
    private String currentNodeName;

    @ApiModelProperty("流程变量(JSON格式)")
    @TableField(value = "process_variables")
    private String processVariables;

    @ApiModelProperty("优先级(1-低,2-中,3-高,4-紧急)")
    @TableField(value = "priority")
    private Integer priority;

    @ApiModelProperty("预期完成时间")
    @TableField(value = "expected_end_time")
    private Date expectedEndTime;

    @ApiModelProperty("实际完成时间")
    @TableField(value = "actual_end_time")
    private Date actualEndTime;

    @ApiModelProperty("开始时间")
    @TableField(value = "start_time")
    private Date startTime;

    @ApiModelProperty("结束时间")
    @TableField(value = "end_time")
    private Date endTime;

    @ApiModelProperty("持续时间(毫秒)")
    @TableField(value = "duration")
    private Long duration;

    @ApiModelProperty("删除原因")
    @TableField(value = "delete_reason")
    private String deleteReason;

    @ApiModelProperty("租户ID")
    @TableField(value = "tenant_id")
    private String tenantId;

    @ApiModelProperty("创建时间")
    @TableField(value = "created_time")
    private Date createdTime;

    @ApiModelProperty("更新时间")
    @TableField(value = "updated_time")
    private Date updatedTime;

    @ApiModelProperty("创建人")
    private String createdBy;

    @ApiModelProperty("更新人")
    private String updatedBy;

    public WfInstance() {
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
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

    public Integer getProcessDefinitionVersion() {
        return processDefinitionVersion;
    }

    public void setProcessDefinitionVersion(Integer processDefinitionVersion) {
        this.processDefinitionVersion = processDefinitionVersion;
    }

    public String getBusinessKey() {
        return businessKey;
    }

    public void setBusinessKey(String businessKey) {
        this.businessKey = businessKey;
    }

    public String getInstanceName() {
        return instanceName;
    }

    public void setInstanceName(String instanceName) {
        this.instanceName = instanceName;
    }

    public String getInstanceDescription() {
        return instanceDescription;
    }

    public void setInstanceDescription(String instanceDescription) {
        this.instanceDescription = instanceDescription;
    }

    public String getStartUserId() {
        return startUserId;
    }

    public void setStartUserId(String startUserId) {
        this.startUserId = startUserId;
    }

    public String getStartUserName() {
        return startUserName;
    }

    public void setStartUserName(String startUserName) {
        this.startUserName = startUserName;
    }

    public String getStartDeptId() {
        return startDeptId;
    }

    public void setStartDeptId(String startDeptId) {
        this.startDeptId = startDeptId;
    }

    public String getStartDeptName() {
        return startDeptName;
    }

    public void setStartDeptName(String startDeptName) {
        this.startDeptName = startDeptName;
    }

    public String getInstanceStatus() {
        return instanceStatus;
    }

    public void setInstanceStatus(String instanceStatus) {
        this.instanceStatus = instanceStatus;
    }

    public String getCurrentNodeId() {
        return currentNodeId;
    }

    public void setCurrentNodeId(String currentNodeId) {
        this.currentNodeId = currentNodeId;
    }

    public String getCurrentNodeName() {
        return currentNodeName;
    }

    public void setCurrentNodeName(String currentNodeName) {
        this.currentNodeName = currentNodeName;
    }

    public String getProcessVariables() {
        return processVariables;
    }

    public void setProcessVariables(String processVariables) {
        this.processVariables = processVariables;
    }

    public Integer getPriority() {
        return priority;
    }

    public void setPriority(Integer priority) {
        this.priority = priority;
    }

    public Date getExpectedEndTime() {
        return expectedEndTime;
    }

    public void setExpectedEndTime(Date expectedEndTime) {
        this.expectedEndTime = expectedEndTime;
    }

    public Date getActualEndTime() {
        return actualEndTime;
    }

    public void setActualEndTime(Date actualEndTime) {
        this.actualEndTime = actualEndTime;
    }

    public Date getStartTime() {
        return startTime;
    }

    public void setStartTime(Date startTime) {
        this.startTime = startTime;
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

    public String getTenantId() {
        return tenantId;
    }

    public void setTenantId(String tenantId) {
        this.tenantId = tenantId;
    }

    public Date getCreatedTime() {
        return createdTime;
    }

    public void setCreatedTime(Date createdTime) {
        this.createdTime = createdTime;
    }

    public Date getUpdatedTime() {
        return updatedTime;
    }

    public void setUpdatedTime(Date updatedTime) {
        this.updatedTime = updatedTime;
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

    @Override
    public String toString() {
        return "WfInstance{" +
                "id='" + id + '\'' +
                ", processDefinitionId='" + processDefinitionId + '\'' +
                ", processDefinitionKey='" + processDefinitionKey + '\'' +
                ", processDefinitionName='" + processDefinitionName + '\'' +
                ", processDefinitionVersion=" + processDefinitionVersion +
                ", businessKey='" + businessKey + '\'' +
                ", instanceName='" + instanceName + '\'' +
                ", instanceDescription='" + instanceDescription + '\'' +
                ", startUserId='" + startUserId + '\'' +
                ", startUserName='" + startUserName + '\'' +
                ", startDeptId='" + startDeptId + '\'' +
                ", startDeptName='" + startDeptName + '\'' +
                ", instanceStatus='" + instanceStatus + '\'' +
                ", currentNodeId='" + currentNodeId + '\'' +
                ", currentNodeName='" + currentNodeName + '\'' +
                ", processVariables='" + processVariables + '\'' +
                ", priority=" + priority +
                ", expectedEndTime=" + expectedEndTime +
                ", actualEndTime=" + actualEndTime +
                ", startTime=" + startTime +
                ", endTime=" + endTime +
                ", duration=" + duration +
                ", deleteReason='" + deleteReason + '\'' +
                ", tenantId='" + tenantId + '\'' +
                ", createdTime=" + createdTime +
                ", updatedTime=" + updatedTime +
                ", createdBy='" + createdBy + '\'' +
                ", updatedBy='" + updatedBy + '\'' +
                '}';
    }
}