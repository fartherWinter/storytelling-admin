package com.chennian.storytelling.bean.model.quality;

import com.baomidou.mybatisplus.annotation.*;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * 质量改进实体类
 *
 * @author storytelling
 * @date 2024-01-01
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@TableName("quality_improvement")
public class QualityImprovement implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 改进ID
     */
    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    /**
     * 改进编号
     */
    @TableField("improvement_no")
    private String improvementNo;

    /**
     * 改进项目名称
     */
    @TableField("project_name")
    private String projectName;

    /**
     * 改进类型（1-工艺改进 2-产品改进 3-管理改进 4-设备改进 5-其他）
     */
    @TableField("improvement_type")
    private Integer improvementType;

    /**
     * 改进状态（0-计划中 1-实施中 2-验证中 3-已完成 4-已取消）
     */
    @TableField("status")
    private Integer status;

    /**
     * 优先级（1-低 2-中 3-高 4-紧急）
     */
    @TableField("priority")
    private Integer priority;

    /**
     * 改进背景
     */
    @TableField("background")
    private String background;

    /**
     * 现状描述
     */
    @TableField("current_situation")
    private String currentSituation;

    /**
     * 问题分析
     */
    @TableField("problem_analysis")
    private String problemAnalysis;

    /**
     * 改进目标
     */
    @TableField("improvement_goal")
    private String improvementGoal;

    /**
     * 改进方案
     */
    @TableField("improvement_plan")
    private String improvementPlan;

    /**
     * 实施步骤
     */
    @TableField("implementation_steps")
    private String implementationSteps;

    /**
     * 预期效果
     */
    @TableField("expected_effect")
    private String expectedEffect;

    /**
     * 实际效果
     */
    @TableField("actual_effect")
    private String actualEffect;

    /**
     * 投入成本
     */
    @TableField("investment_cost")
    private BigDecimal investmentCost;

    /**
     * 预期收益
     */
    @TableField("expected_benefit")
    private BigDecimal expectedBenefit;

    /**
     * 实际收益
     */
    @TableField("actual_benefit")
    private BigDecimal actualBenefit;

    /**
     * 投资回报率（%）
     */
    @TableField("roi")
    private Double roi;

    /**
     * 项目负责人ID
     */
    @TableField("project_manager_id")
    private Long projectManagerId;

    /**
     * 项目负责人姓名
     */
    @TableField("project_manager_name")
    private String projectManagerName;

    /**
     * 团队成员
     */
    @TableField("team_members")
    private String teamMembers;

    /**
     * 相关部门
     */
    @TableField("related_departments")
    private String relatedDepartments;

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
     * 进度（%）
     */
    @TableField("progress")
    private Double progress;

    /**
     * 里程碑
     */
    @TableField("milestones")
    private String milestones;

    /**
     * 风险评估
     */
    @TableField("risk_assessment")
    private String riskAssessment;

    /**
     * 风险应对措施
     */
    @TableField("risk_mitigation")
    private String riskMitigation;

    /**
     * 验证方法
     */
    @TableField("verification_method")
    private String verificationMethod;

    /**
     * 验证结果
     */
    @TableField("verification_result")
    private String verificationResult;

    /**
     * 验证人ID
     */
    @TableField("verifier_id")
    private Long verifierId;

    /**
     * 验证人姓名
     */
    @TableField("verifier_name")
    private String verifierName;

    /**
     * 验证时间
     */
    @TableField("verification_time")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime verificationTime;

    /**
     * 经验教训
     */
    @TableField("lessons_learned")
    private String lessonsLearned;

    /**
     * 标准化措施
     */
    @TableField("standardization_measures")
    private String standardizationMeasures;

    /**
     * 推广计划
     */
    @TableField("promotion_plan")
    private String promotionPlan;

    /**
     * 附件路径
     */
    @TableField("attachment_path")
    private String attachmentPath;

    /**
     * 改进备注
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