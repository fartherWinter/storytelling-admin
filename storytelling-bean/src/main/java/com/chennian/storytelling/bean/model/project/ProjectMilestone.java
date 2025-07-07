package com.chennian.storytelling.bean.model.project;

import com.baomidou.mybatisplus.annotation.*;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * 项目里程碑实体类
 *
 * @author storytelling
 * @date 2024-01-01
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@TableName("project_milestone")
public class ProjectMilestone implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 里程碑ID
     */
    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    /**
     * 项目ID
     */
    @TableField("project_id")
    private Long projectId;

    /**
     * 里程碑名称
     */
    @TableField("milestone_name")
    private String milestoneName;

    /**
     * 里程碑描述
     */
    @TableField("description")
    private String description;

    /**
     * 里程碑类型（1-项目启动 2-需求确认 3-设计完成 4-开发完成 5-测试完成 6-项目交付 7-其他）
     */
    @TableField("milestone_type")
    private Integer milestoneType;

    /**
     * 里程碑状态（0-未开始 1-进行中 2-已完成 3-已延期）
     */
    @TableField("status")
    private Integer status;

    /**
     * 重要程度（1-一般 2-重要 3-关键）
     */
    @TableField("importance")
    private Integer importance;

    /**
     * 计划完成时间
     */
    @TableField("planned_completion_time")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime plannedCompletionTime;

    /**
     * 实际完成时间
     */
    @TableField("actual_completion_time")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime actualCompletionTime;

    /**
     * 负责人ID
     */
    @TableField("responsible_id")
    private Long responsibleId;

    /**
     * 负责人姓名
     */
    @TableField("responsible_name")
    private String responsibleName;

    /**
     * 完成标准
     */
    @TableField("completion_criteria")
    private String completionCriteria;

    /**
     * 交付物
     */
    @TableField("deliverables")
    private String deliverables;

    /**
     * 达成说明
     */
    @TableField("achievement_notes")
    private String achievementNotes;

    /**
     * 风险评估
     */
    @TableField("risk_assessment")
    private String riskAssessment;

    /**
     * 依赖关系
     */
    @TableField("dependencies")
    private String dependencies;

    /**
     * 里程碑备注
     */
    @TableField("remarks")
    private String remarks;

    /**
     * 创建者
     */
    @TableField(value = "create_by", fill = FieldFill.INSERT)
    private String createBy;

    /**
     * 创建时间
     */
    @TableField(value = "create_time", fill = FieldFill.INSERT)
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime createTime;

    /**
     * 更新者
     */
    @TableField(value = "update_by", fill = FieldFill.INSERT_UPDATE)
    private String updateBy;

    /**
     * 更新时间
     */
    @TableField(value = "update_time", fill = FieldFill.INSERT_UPDATE)
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime updateTime;

    /**
     * 删除标志（0代表存在 2代表删除）
     */
    @TableField("del_flag")
    @TableLogic
    private String delFlag;
}