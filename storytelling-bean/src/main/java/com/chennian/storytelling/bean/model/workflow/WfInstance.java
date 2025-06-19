package com.chennian.storytelling.bean.model.workflow;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;

/**
 * 工作流实例实体类
 * 
 * @author storytelling
 * @date 2024-01-01
 */
@Data
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

    @ApiModelProperty("备注")
    public String remark;
}