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
 * 工作流表单字段选项实体类
 * 
 * @author chennian
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@ApiModel("工作流表单字段选项")
@TableName("wf_field_option")
public class WfFieldOption implements Serializable {

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
     * 字段ID
     */
    @ApiModelProperty("字段ID")
    @TableField("field_id")
    private String fieldId;

    /**
     * 选项值
     */
    @ApiModelProperty("选项值")
    @TableField("option_value")
    private String optionValue;

    /**
     * 选项标签
     */
    @ApiModelProperty("选项标签")
    @TableField("option_label")
    private String optionLabel;

    /**
     * 选项描述
     */
    @ApiModelProperty("选项描述")
    @TableField("option_description")
    private String optionDescription;

    /**
     * 排序号
     */
    @ApiModelProperty("排序号")
    @TableField("sort_order")
    private Integer sortOrder;

    /**
     * 是否禁用：0-启用，1-禁用
     */
    @ApiModelProperty("是否禁用")
    @TableField("disabled")
    private Integer disabled;

    /**
     * 是否默认选中：0-否，1-是
     */
    @ApiModelProperty("是否默认选中")
    @TableField("is_default")
    private Integer isDefault;

    /**
     * 选项分组
     */
    @ApiModelProperty("选项分组")
    @TableField("option_group")
    private String optionGroup;

    /**
     * 扩展属性（JSON格式）
     */
    @ApiModelProperty("扩展属性")
    @TableField("extra_props")
    private String extraProps;

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

    public WfFieldOption() {
    }

}