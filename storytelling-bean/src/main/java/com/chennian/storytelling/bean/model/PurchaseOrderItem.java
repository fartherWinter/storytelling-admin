package com.chennian.storytelling.bean.model;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

/**
 * 采购订单明细表
 * @author chen
 * @TableName erp_purchase_order_item
 */
@TableName(value ="erp_purchase_order_item")
@Data
public class PurchaseOrderItem implements Serializable {
    /**
     * 明细ID
     */
    @TableId(value = "item_id", type = IdType.AUTO)
    private Long itemId;

    /**
     * 采购订单ID
     */
    @TableField(value = "order_id")
    private Long orderId;

    /**
     * 产品ID
     */
    @TableField(value = "product_id")
    private Long productId;

    /**
     * 产品名称
     */
    @TableField(value = "product_name")
    private String productName;

    /**
     * 产品编码
     */
    @TableField(value = "product_code")
    private String productCode;

    /**
     * 采购数量
     */
    @TableField(value = "quantity")
    private Integer quantity;

    /**
     * 单位
     */
    @TableField(value = "unit")
    private String unit;

    /**
     * 单价
     */
    @TableField(value = "unit_price")
    private BigDecimal unitPrice;

    /**
     * 总金额
     */
    @TableField(value = "total_price")
    private BigDecimal totalPrice;

    /**
     * 已入库数量
     */
    @TableField(value = "received_quantity")
    private Integer receivedQuantity;

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
}