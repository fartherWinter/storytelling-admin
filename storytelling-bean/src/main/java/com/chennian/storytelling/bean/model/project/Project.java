package com.chennian.storytelling.bean.model.project;

import com.baomidou.mybatisplus.annotation.*;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * 项目信息实体类
 *
 * @author storytelling
 * @date 2024-01-01
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@TableName("project")
public class Project implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 项目ID
     */
    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    /**
     * 项目编号
     */
    @TableField("project_no")
    private String projectNo;

    /**
     * 项目名称
     */
    @TableField("project_name")
    private String projectName;

    /**
     * 项目描述
     */
    @TableField("description")
    private String description;

    /**
     * 项目类型（1-内部项目 2-外部项目 3-研发项目 4-维护项目）
     */
    @TableField("project_type")
    private Integer projectType;

    /**
     * 项目状态（0-未开始 1-进行中 2-已暂停 3-已完成 4-已取消 6-已归档）
     */
    @TableField("status")
    private Integer status;

    /**
     * 优先级（1-低 2-中 3-高 4-紧急）
     */
    @TableField("priority")
    private Integer priority;

    /**
     * 项目经理ID
     */
    @TableField("manager_id")
    private Long managerId;

    /**
     * 项目经理姓名
     */
    @TableField("manager_name")
    private String managerName;

    /**
     * 负责部门ID
     */
    @TableField("dept_id")
    private Long deptId;

    /**
     * 负责部门名称
     */
    @TableField("dept_name")
    private String deptName;

    /**
     * 计划开始时间
     */
    @TableField("planned_start_time")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime plannedStartTime;

    /**
     * 计划结束时间
     */
    @TableField("planned_end_time")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime plannedEndTime;

    /**
     * 实际开始时间
     */
    @TableField("actual_start_time")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime actualStartTime;

    /**
     * 实际结束时间
     */
    @TableField("actual_end_time")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime actualEndTime;

    /**
     * 项目进度（0-100）
     */
    @TableField("progress")
    private Double progress;

    /**
     * 预算金额
     */
    @TableField("budget_amount")
    private BigDecimal budgetAmount;

    /**
     * 实际花费
     */
    @TableField("actual_cost")
    private BigDecimal actualCost;

    /**
     * 项目目标
     */
    @TableField("objectives")
    private String objectives;

    /**
     * 项目范围
     */
    @TableField("scope")
    private String scope;

    /**
     * 风险评估
     */
    @TableField("risk_assessment")
    private String riskAssessment;

    /**
     * 项目备注
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