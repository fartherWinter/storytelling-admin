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
 * 项目风险实体类
 *
 * @author storytelling
 * @date 2024-01-01
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@TableName("project_risk")
public class ProjectRisk implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 风险ID
     */
    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    /**
     * 项目ID
     */
    @TableField("project_id")
    private Long projectId;

    /**
     * 风险编号
     */
    @TableField("risk_no")
    private String riskNo;

    /**
     * 风险名称
     */
    @TableField("risk_name")
    private String riskName;

    /**
     * 风险描述
     */
    @TableField("description")
    private String description;

    /**
     * 风险类型（1-技术风险 2-管理风险 3-商业风险 4-外部风险 5-其他）
     */
    @TableField("risk_type")
    private Integer riskType;

    /**
     * 风险等级（1-低 2-中 3-高 4-极高）
     */
    @TableField("risk_level")
    private Long riskLevel;

    /**
     * 风险状态（0-识别 1-评估 2-应对 3-监控 4-关闭）
     */
    @TableField("status")
    private Integer status;

    /**
     * 发生概率（0-100%）
     */
    @TableField("probability")
    private Double probability;

    /**
     * 影响程度（1-很小 2-小 3-中等 4-大 5-很大）
     */
    @TableField("impact")
    private Integer impact;

    /**
     * 风险值（概率 * 影响程度）
     */
    @TableField("risk_value")
    private Double riskValue;

    /**
     * 潜在损失
     */
    @TableField("potential_loss")
    private BigDecimal potentialLoss;

    /**
     * 风险来源
     */
    @TableField("risk_source")
    private String riskSource;

    /**
     * 触发条件
     */
    @TableField("trigger_conditions")
    private String triggerConditions;

    /**
     * 应对策略（1-规避 2-减轻 3-转移 4-接受）
     */
    @TableField("response_strategy")
    private Integer responseStrategy;

    /**
     * 应对措施
     */
    @TableField("response_measures")
    private String responseMeasures;

    /**
     * 应急计划
     */
    @TableField("contingency_plan")
    private String contingencyPlan;

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
     * 识别时间
     */
    @TableField("identification_time")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime identificationTime;

    /**
     * 预计发生时间
     */
    @TableField("expected_occurrence_time")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime expectedOccurrenceTime;

    /**
     * 实际发生时间
     */
    @TableField("actual_occurrence_time")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime actualOccurrenceTime;

    /**
     * 关闭时间
     */
    @TableField("closure_time")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime closureTime;

    /**
     * 监控频率（天）
     */
    @TableField("monitoring_frequency")
    private Integer monitoringFrequency;

    /**
     * 最后评估时间
     */
    @TableField("last_assessment_time")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime lastAssessmentTime;

    /**
     * 风险备注
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