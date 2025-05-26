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
 * 销售订单表
 * @author chen
 * @TableName erp_sales_order
 */
@TableName(value ="erp_sales_order")
@Data
public class SalesOrder implements Serializable {
    /**
     * 销售订单ID
     */
    @TableId(value = "order_id", type = IdType.AUTO)
    private Long orderId;

    /**
     * 订单编号
     */
    @TableField(value = "order_no")
    private String orderNo;

    /**
     * 客户ID
     */
    @TableField(value = "customer_id")
    private Long customerId;

    /**
     * 订单总金额
     */
    @TableField(value = "total_amount")
    private BigDecimal totalAmount;

    /**
     * 订单状态（0草稿 1已确认 2已发货 3已完成 4已取消）
     */
    @TableField(value = "status")
    private String status;

    /**
     * 预计交付日期
     */
    @TableField(value = "expected_delivery_date")
    private Date expectedDeliveryDate;

    /**
     * 实际交付日期
     */
    @TableField(value = "actual_delivery_date")
    private Date actualDeliveryDate;

    /**
     * 销售员ID
     */
    @TableField(value = "salesperson_id")
    private Long salespersonId;

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
     * 收货地址
     */
    @TableField(value = "shipping_address")
    private String shippingAddress;

    /**
     * 收货联系人
     */
    @TableField(value = "shipping_contact")
    private String shippingContact;

    /**
     * 收货电话
     */
    @TableField(value = "shipping_phone")
    private String shippingPhone;

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

@TableField(exist = false)
private String customerName;

/**
 * 订单明细列表
 */
@TableField(exist = false)
private List<SalesOrderItem> items;
}