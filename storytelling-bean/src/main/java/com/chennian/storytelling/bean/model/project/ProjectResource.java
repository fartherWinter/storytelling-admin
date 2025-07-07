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
 * 项目资源实体类
 *
 * @author storytelling
 * @date 2024-01-01
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@TableName("project_resource")
public class ProjectResource implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 资源ID
     */
    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    /**
     * 项目ID
     */
    @TableField("project_id")
    private Long projectId;

    /**
     * 资源名称
     */
    @TableField("resource_name")
    private String resourceName;

    /**
     * 资源类型（1-人力资源 2-设备资源 3-材料资源 4-资金资源 5-其他）
     */
    @TableField("resource_type")
    private Integer resourceType;

    /**
     * 资源编号
     */
    @TableField("resource_no")
    private String resourceNo;

    /**
     * 资源描述
     */
    @TableField("description")
    private String description;

    /**
     * 资源状态（0-空闲 1-使用中 2-维护中 3-已损坏 4-已报废）
     */
    @TableField("status")
    private Integer status;

    /**
     * 资源单位
     */
    @TableField("unit")
    private String unit;

    /**
     * 计划数量
     */
    @TableField("planned_quantity")
    private Double plannedQuantity;

    /**
     * 实际数量
     */
    @TableField("actual_quantity")
    private Double actualQuantity;

    /**
     * 可用数量
     */
    @TableField("available_quantity")
    private Double availableQuantity;

    /**
     * 分配数量
     */
    @TableField("allocated_quantity")
    private Double allocatedQuantity;
    /**
     * 单价
     */
    @TableField("unit_price")
    private BigDecimal unitPrice;

    /**
     * 总成本
     */
    @TableField("total_cost")
    private BigDecimal totalCost;

    /**
     * 供应商
     */
    @TableField("supplier")
    private String supplier;

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
     * 分配时间
     */
    @TableField("allocation_time")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime allocationTime;

    /**
     * 计划使用开始时间
     */
    @TableField("planned_start_time")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime plannedStartTime;

    /**
     * 计划使用结束时间
     */
    @TableField("planned_end_time")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime plannedEndTime;

    /**
     * 实际使用开始时间
     */
    @TableField("actual_start_time")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime actualStartTime;

    /**
     * 实际使用结束时间
     */
    @TableField("actual_end_time")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime actualEndTime;

    /**
     * 使用效率（%）
     */
    @TableField("efficiency")
    private Double efficiency;

    /**
     * 资源位置
     */
    @TableField("location")
    private String location;

    /**
     * 资源规格
     */
    @TableField("specifications")
    private String specifications;

    /**
     * 资源备注
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