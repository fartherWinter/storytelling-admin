package com.chennian.storytelling.bean.model.workflow;

import com.baomidou.mybatisplus.annotation.*;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * 工作流表单数据实体类
 * 
 * @author chennian
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@ApiModel("工作流表单数据")
@TableName("wf_form_data")
public class WfFormData implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 主键ID
     */
    @ApiModelProperty("主键ID")
    @TableId(value = "id", type = IdType.ASSIGN_ID)
    private Long id;

    /**
     * 表单ID
     */
    @ApiModelProperty("表单ID")
    @TableField("form_id")
    private String formId;

    /**
     * 流程实例ID
     */
    @ApiModelProperty("流程实例ID")
    @TableField("instance_id")
    private String instanceId;

    /**
     * 任务ID
     */
    @ApiModelProperty("任务ID")
    @TableField("task_id")
    private String taskId;

    /**
     * 表单数据（JSON格式）
     */
    @ApiModelProperty("表单数据")
    @TableField("form_data")
    private String formData;

    /**
     * 数据版本
     */
    @ApiModelProperty("数据版本")
    @TableField("version")
    private Integer version;

    /**
     * 数据状态：0-草稿，1-已提交，2-已审核
     */
    @ApiModelProperty("数据状态")
    @TableField("status")
    private Integer status;

    /**
     * 业务键
     */
    @ApiModelProperty("业务键")
    @TableField("business_key")
    private String businessKey;

    /**
     * 创建者ID
     */
    @ApiModelProperty("创建者ID")
    @TableField("creator_id")
    private String creatorId;

    /**
     * 创建者姓名
     */
    @ApiModelProperty("创建者姓名")
    @TableField("creator_name")
    private String creatorName;

    /**
     * 创建时间
     */
    @ApiModelProperty("创建时间")
    @TableField(value = "create_time", fill = FieldFill.INSERT)
    private LocalDateTime createTime;

    /**
     * 更新者ID
     */
    @ApiModelProperty("更新者ID")
    @TableField("updater_id")
    private String updaterId;

    /**
     * 更新者姓名
     */
    @ApiModelProperty("更新者姓名")
    @TableField("updater_name")
    private String updaterName;

    /**
     * 更新时间
     */
    @ApiModelProperty("更新时间")
    @TableField(value = "update_time", fill = FieldFill.INSERT_UPDATE)
    private LocalDateTime updateTime;

    /**
     * 是否删除：0-未删除，1-已删除
     */
    @ApiModelProperty("是否删除")
    @TableField("deleted")
    @TableLogic
    private Integer deleted;

    /**
     * 备注
     */
    @ApiModelProperty("备注")
    @TableField("remark")
    private String remark;

    public WfFormData() {
    }

}