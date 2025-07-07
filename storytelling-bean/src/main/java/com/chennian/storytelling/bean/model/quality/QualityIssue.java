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
 * 质量问题实体类
 *
 * @author storytelling
 * @date 2024-01-01
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@TableName("quality_issue")
public class QualityIssue implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 问题ID
     */
    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    /**
     * 问题编号
     */
    @TableField("issue_no")
    private String issueNo;

    /**
     * 问题标题
     */
    @TableField("issue_title")
    private String issueTitle;

    /**
     * 问题描述
     */
    @TableField("description")
    private String description;

    /**
     * 问题类型（1-产品质量问题 2-工艺质量问题 3-服务质量问题 4-管理质量问题 5-其他）
     */
    @TableField("issue_type")
    private Integer issueType;

    /**
     * 问题等级（1-轻微 2-一般 3-严重 4-重大）
     */
    @TableField("issue_level")
    private Integer issueLevel;

    /**
     * 问题状态（0-新建 1-分析中 2-处理中 3-验证中 4-已关闭）
     */
    @TableField("status")
    private Integer status;

    /**
     * 产品编号
     */
    @TableField("product_no")
    private String productNo;

    /**
     * 产品名称
     */
    @TableField("product_name")
    private String productName;

    /**
     * 批次号
     */
    @TableField("batch_no")
    private String batchNo;

    /**
     * 发现阶段（1-设计阶段 2-采购阶段 3-生产阶段 4-检验阶段 5-使用阶段）
     */
    @TableField("discovery_stage")
    private Integer discoveryStage;

    /**
     * 发现人ID
     */
    @TableField("discoverer_id")
    private Long discovererId;

    /**
     * 发现人姓名
     */
    @TableField("discoverer_name")
    private String discovererName;

    /**
     * 发现时间
     */
    @TableField("discovery_time")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime discoveryTime;

    /**
     * 发现地点
     */
    @TableField("discovery_location")
    private String discoveryLocation;

    /**
     * 影响范围
     */
    @TableField("impact_scope")
    private String impactScope;

    /**
     * 影响数量
     */
    @TableField("impact_quantity")
    private Double impactQuantity;

    /**
     * 经济损失
     */
    @TableField("economic_loss")
    private BigDecimal economicLoss;

    /**
     * 原因分析
     */
    @TableField("cause_analysis")
    private String causeAnalysis;

    /**
     * 根本原因
     */
    @TableField("root_cause")
    private String rootCause;

    /**
     * 责任部门
     */
    @TableField("responsible_department")
    private String responsibleDepartment;

    /**
     * 责任人ID
     */
    @TableField("responsible_id")
    private Long responsibleId;

    /**
     * 责任人姓名
     */
    @TableField("responsible_name")
    private String responsibleName;

    /**
     * 纠正措施
     */
    @TableField("corrective_action")
    private String correctiveAction;

    /**
     * 预防措施
     */
    @TableField("preventive_action")
    private String preventiveAction;

    /**
     * 处理措施
     */
    @TableField("treatment_measures")
    private String treatmentMeasures;

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
     * 验证结果
     */
    @TableField("verification_result")
    private String verificationResult;

    /**
     * 关闭时间
     */
    @TableField("closure_time")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime closureTime;

    /**
     * 附件路径
     */
    @TableField("attachment_path")
    private String attachmentPath;

    /**
     * 问题备注
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