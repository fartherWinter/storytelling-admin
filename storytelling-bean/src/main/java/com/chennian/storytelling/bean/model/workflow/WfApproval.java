package com.chennian.storytelling.bean.model.workflow;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;

/**
 * 工作流审批实体类
 * 
 * @author chennian
 */
@Data
@TableName("wf_approval")
public class WfApproval implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 主键ID
     */
    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    /**
     * 流程实例ID
     */
    @TableField("process_instance_id")
    private String processInstanceId;

    /**
     * 流程定义标识
     */
    @TableField("process_key")
    private String processKey;

    /**
     * 流程定义名称
     */
    @TableField("process_name")
    private String processName;

    /**
     * 任务ID
     */
    @TableField("task_id")
    private String taskId;

    /**
     * 任务名称
     */
    @TableField("task_name")
    private String taskName;

    /**
     * 任务定义标识
     */
    @TableField("task_definition_key")
    private String taskDefinitionKey;

    /**
     * 审批人ID
     */
    @TableField("approver_id")
    private Long approverId;

    /**
     * 审批人名称
     */
    @TableField("approver_name")
    private String approverName;

    /**
     * 审批状态（0-待审批，1-已同意，2-已驳回，3-已转办，4-已委托，5-已终止）
     */
    @TableField("status")
    private String status;

    /**
     * 审批意见
     */
    @TableField("comment")
    private String comment;

    /**
     * 审批时间
     */
    @TableField("approval_time")
    private Date approvalTime;

    /**
     * 到期时间
     */
    @TableField("due_time")
    private Date dueTime;

    /**
     * 委托人ID
     */
    @TableField("delegator_id")
    private Long delegatorId;

    /**
     * 委托人名称
     */
    @TableField("delegator_name")
    private String delegatorName;

    /**
     * 转办前处理人ID
     */
    @TableField("previous_handler_id")
    private Long previousHandlerId;

    /**
     * 转办前处理人名称
     */
    @TableField("previous_handler_name")
    private String previousHandlerName;

    /**
     * 抄送人ID列表（逗号分隔）
     */
    @TableField("cc_user_ids")
    private String ccUserIds;

    /**
     * 抄送人名称列表（逗号分隔）
     */
    @TableField("cc_user_names")
    private String ccUserNames;

    /**
     * 业务标识
     */
    @TableField("business_key")
    private String businessKey;

    /**
     * 业务类型
     */
    @TableField("business_type")
    private String businessType;

    /**
     * 表单数据（JSON格式）
     */
    @TableField("form_data")
    private String formData;

    /**
     * 创建时间
     */
    @TableField("create_time")
    private Date createTime;

    /**
     * 更新时间
     */
    @TableField("update_time")
    private Date updateTime;

    /**
     * 创建人ID
     */
    @TableField("create_by")
    private String createBy;

    /**
     * 更新人ID
     */
    @TableField("update_by")
    private String updateBy;

    /**
     * 备注
     */
    @TableField("remark")
    private String remark;
} 