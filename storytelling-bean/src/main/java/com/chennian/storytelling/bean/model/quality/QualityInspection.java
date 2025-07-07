package com.chennian.storytelling.bean.model.quality;

import com.baomidou.mybatisplus.annotation.*;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.chennian.storytelling.bean.enums.DataStatusEnum;
import com.chennian.storytelling.bean.enums.DelFlagEnum;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * 质量检验实体类
 *
 * @author storytelling
 * @date 2024-01-01
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@TableName("quality_inspection")
public class QualityInspection implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 检验ID
     */
    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    /**
     * 检验编号
     */
    @TableField("inspection_no")
    private String inspectionNo;

    /**
     * 检验名称
     */
    @TableField("inspection_name")
    private String inspectionName;

    /**
     * 检验类型（1-进料检验 2-过程检验 3-成品检验 4-出货检验 5-其他）
     */
    @TableField("inspection_type")
    private Integer inspectionType;

    /**
     * 检验状态（0-待检验 1-检验中 2-检验完成 3-检验异常）
     */
    @TableField("status")
    private Integer status;
    
    /**
     * 获取检验状态枚举
     */
    public DataStatusEnum getStatusEnum() {
        return DataStatusEnum.getByCode(this.status);
    }
    
    /**
     * 设置检验状态枚举
     */
    public void setStatusEnum(DataStatusEnum statusEnum) {
        this.status = statusEnum != null ? statusEnum.getCode() : null;
    }

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
     * 供应商
     */
    @TableField("supplier")
    private String supplier;

    /**
     * 检验数量
     */
    @TableField("inspection_quantity")
    private Double inspectionQuantity;

    /**
     * 抽样数量
     */
    @TableField("sample_quantity")
    private Double sampleQuantity;

    /**
     * 合格数量
     */
    @TableField("qualified_quantity")
    private Double qualifiedQuantity;

    /**
     * 不合格数量
     */
    @TableField("unqualified_quantity")
    private Double unqualifiedQuantity;

    /**
     * 合格率（%）
     */
    @TableField("qualification_rate")
    private Double qualificationRate;

    /**
     * 检验标准
     */
    @TableField("inspection_standard")
    private String inspectionStandard;

    /**
     * 检验方法
     */
    @TableField("inspection_method")
    private String inspectionMethod;

    /**
     * 检验设备
     */
    @TableField("inspection_equipment")
    private String inspectionEquipment;

    /**
     * 检验员ID
     */
    @TableField("inspector_id")
    private Long inspectorId;

    /**
     * 检验员姓名
     */
    @TableField("inspector_name")
    private String inspectorName;

    /**
     * 检验开始时间
     */
    @TableField("inspection_start_time")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime inspectionStartTime;

    /**
     * 检验结束时间
     */
    @TableField("inspection_end_time")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime inspectionEndTime;

    /**
     * 检验结果（1-合格 2-不合格 3-让步接收）
     */
    @TableField("inspection_result")
    private Integer inspectionResult;

    /**
     * 不合格原因
     */
    @TableField("unqualified_reason")
    private String unqualifiedReason;

    /**
     * 处理措施
     */
    @TableField("treatment_measures")
    private String treatmentMeasures;

    /**
     * 检验费用
     */
    @TableField("inspection_cost")
    private BigDecimal inspectionCost;

    /**
     * 检验报告
     */
    @TableField("inspection_report")
    private String inspectionReport;

    /**
     * 检验备注
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
    private Integer delFlag;
    
    /**
     * 获取删除标志枚举
     */
    public DelFlagEnum getDelFlagEnum() {
        return DelFlagEnum.getByCode(this.delFlag);
    }
    
    /**
     * 设置删除标志枚举
     */
    public void setDelFlagEnum(DelFlagEnum delFlagEnum) {
        this.delFlag = delFlagEnum != null ? delFlagEnum.getCode() : null;
    }
}