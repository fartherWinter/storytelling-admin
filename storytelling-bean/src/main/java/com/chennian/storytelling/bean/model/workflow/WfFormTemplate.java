package com.chennian.storytelling.bean.model.workflow;

import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * 工作流表单模板实体类
 * 
 * @author chennian
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@TableName("wf_form_template")
public class WfFormTemplate implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 主键ID
     */
    @TableId(value = "id", type = IdType.ASSIGN_ID)
    private Long id;

    /**
     * 表单ID（唯一标识）
     */
    @TableField("form_id")
    private String formId;

    /**
     * 表单名称
     */
    @TableField("form_name")
    private String formName;

    /**
     * 表单描述
     */
    @TableField("form_description")
    private String formDescription;

    /**
     * 关联流程键
     */
    @TableField("process_key")
    private String processKey;

    /**
     * 版本号
     */
    @TableField("version")
    private Integer version;

    /**
     * 表单配置（JSON格式）
     */
    @TableField("form_config")
    private String formConfig;

    /**
     * 表单字段（JSON格式）
     */
    @TableField("form_fields")
    private String formFields;

    /**
     * 表单布局（JSON格式）
     */
    @TableField("form_layout")
    private String formLayout;

    /**
     * 表单规则（JSON格式）
     */
    @TableField("form_rules")
    private String formRules;

    /**
     * 表单分类
     */
    @TableField("category")
    private String category;

    /**
     * 是否启用（0-禁用，1-启用）
     */
    @TableField("enabled")
    private Integer enabled;

    /**
     * 是否公开（0-私有，1-公开）
     */
    @TableField("is_public")
    private Integer isPublic;

    /**
     * 排序号
     */
    @TableField("sort_order")
    private Integer sortOrder;

    /**
     * 备注
     */
    @TableField("remark")
    private String remark;

    /**
     * 创建者ID
     */
    @TableField(value = "creator_id", fill = FieldFill.INSERT)
    private Long creatorId;

    /**
     * 创建者姓名
     */
    @TableField(value = "creator_name", fill = FieldFill.INSERT)
    private String creatorName;

    /**
     * 创建时间
     */
    @TableField(value = "created_time", fill = FieldFill.INSERT)
    private LocalDateTime createdTime;

    /**
     * 更新者ID
     */
    @TableField(value = "updater_id", fill = FieldFill.INSERT_UPDATE)
    private Long updaterId;

    /**
     * 更新者姓名
     */
    @TableField(value = "updater_name", fill = FieldFill.INSERT_UPDATE)
    private String updaterName;

    /**
     * 更新时间
     */
    @TableField(value = "updated_time", fill = FieldFill.INSERT_UPDATE)
    private LocalDateTime updatedTime;

    /**
     * 是否删除（0-未删除，1-已删除）
     */
    @TableField("deleted")
    @TableLogic
    private Integer deleted;

    /**
     * 租户ID
     */
    @TableField("tenant_id")
    private Long tenantId;

    public WfFormTemplate() {
    }
}