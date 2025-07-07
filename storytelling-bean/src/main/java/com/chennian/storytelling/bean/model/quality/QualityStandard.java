package com.chennian.storytelling.bean.model.quality;

import com.baomidou.mybatisplus.annotation.*;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * 质量标准实体类
 *
 * @author storytelling
 * @date 2024-01-01
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@TableName("quality_standard")
public class QualityStandard implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 标准ID
     */
    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    /**
     * 标准编号
     */
    @TableField("standard_no")
    private String standardNo;

    /**
     * 标准名称
     */
    @TableField("standard_name")
    private String standardName;

    /**
     * 标准类型（1-国家标准 2-行业标准 3-企业标准 4-国际标准 5-其他）
     */
    @TableField("standard_type")
    private Integer standardType;

    /**
     * 标准状态（0-草案 1-生效 2-修订中 3-废止）
     */
    @TableField("status")
    private Integer status;

    /**
     * 适用范围
     */
    @TableField("applicable_scope")
    private String applicableScope;

    /**
     * 产品类别
     */
    @TableField("product_category")
    private String productCategory;

    /**
     * 标准版本
     */
    @TableField("version")
    private String version;

    /**
     * 标准描述
     */
    @TableField("description")
    private String description;

    /**
     * 技术要求
     */
    @TableField("technical_requirements")
    private String technicalRequirements;

    /**
     * 检验方法
     */
    @TableField("test_methods")
    private String testMethods;

    /**
     * 检验规则
     */
    @TableField("inspection_rules")
    private String inspectionRules;

    /**
     * 标志包装
     */
    @TableField("marking_packaging")
    private String markingPackaging;

    /**
     * 运输储存
     */
    @TableField("transport_storage")
    private String transportStorage;

    /**
     * 发布机构
     */
    @TableField("issuing_organization")
    private String issuingOrganization;

    /**
     * 发布日期
     */
    @TableField("issue_date")
    @JsonFormat(pattern = "yyyy-MM-dd")
    private LocalDateTime issueDate;

    /**
     * 实施日期
     */
    @TableField("implementation_date")
    @JsonFormat(pattern = "yyyy-MM-dd")
    private LocalDateTime implementationDate;

    /**
     * 废止日期
     */
    @TableField("abolition_date")
    @JsonFormat(pattern = "yyyy-MM-dd")
    private LocalDateTime abolitionDate;

    /**
     * 替代标准
     */
    @TableField("replacement_standard")
    private String replacementStandard;

    /**
     * 被替代标准
     */
    @TableField("replaced_standard")
    private String replacedStandard;

    /**
     * 引用标准
     */
    @TableField("referenced_standards")
    private String referencedStandards;

    /**
     * 关键词
     */
    @TableField("keywords")
    private String keywords;

    /**
     * 附件路径
     */
    @TableField("attachment_path")
    private String attachmentPath;

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
     * 审核人ID
     */
    @TableField("reviewer_id")
    private Long reviewerId;

    /**
     * 审核人姓名
     */
    @TableField("reviewer_name")
    private String reviewerName;

    /**
     * 审核时间
     */
    @TableField("review_time")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime reviewTime;

    /**
     * 标准备注
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