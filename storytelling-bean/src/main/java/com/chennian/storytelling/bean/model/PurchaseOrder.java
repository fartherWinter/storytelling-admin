package com.chennian.storytelling.bean.model;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

/**
 * 采购订单表
 * @author chen
 * @TableName erp_purchase_order
 */
@TableName(value ="erp_purchase_order")
@Data
public class PurchaseOrder implements Serializable {
    /**
     * 采购订单ID
     */
    @TableId(value = "order_id", type = IdType.AUTO)
    private Long orderId;

    /**
     * 订单编号
     */
    @TableField(value = "order_no")
    private String orderNo;

    /**
     * 供应商ID
     */
    @TableField(value = "supplier_id")
    private Long supplierId;

    /**
     * 订单总金额
     */
    @TableField(value = "total_amount")
    private BigDecimal totalAmount;

    /**
     * 订单状态（0草稿 1已提交 2已审核 3已完成 4已取消）
     */
    @TableField(value = "status")
    private String status;

    /**
     * 预计到货日期
     */
    @TableField(value = "expected_delivery_date")
    private Date expectedDeliveryDate;

    /**
     * 实际到货日期
     */
    @TableField(value = "actual_delivery_date")
    private Date actualDeliveryDate;

    /**
     * 采购员ID
     */
    @TableField(value = "purchaser_id")
    private Long purchaserId;

    /**
     * 审核人ID
     */
    @TableField(value = "approver_id")
    private Long approverId;

    /**
     * 审核时间
     */
    @TableField(value = "approve_time")
    private Date approveTime;

    /**
     * 备注
     */
    @TableField(value = "remark")
    private String remark;

    /**
     * 创建者
     */
    @TableField(value = "create_by")
    private String createBy;

    /**
     * 创建时间
     */
    @TableField(value = "create_time")
    private Date createTime;

    /**
     * 更新者
     */
    @TableField(value = "update_by")
    private String updateBy;

    /**
     * 更新时间
     */
    @TableField(value = "update_time")
    private Date updateTime;

    @TableField(exist = false)
    private static final long serialVersionUID = 1L;
    
    /**
     * 订单明细列表
     */
    @TableField(exist = false)
    private List<PurchaseOrderItem> items;
}