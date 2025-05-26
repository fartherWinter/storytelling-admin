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
 * 库存信息表
 * @author chen
 * @TableName erp_inventory
 */
@TableName(value ="erp_inventory")
@Data
public class Inventory implements Serializable {
    /**
     * 库存ID
     */
    @TableId(value = "inventory_id", type = IdType.AUTO)
    private Long inventoryId;

    /**
     * 产品ID
     */
    @TableField(value = "product_id")
    private Long productId;

    /**
     * 仓库ID
     */
    @TableField(value = "warehouse_id")
    private Long warehouseId;

    /**
     * 库位编码
     */
    @TableField(value = "location_code")
    private String locationCode;

    /**
     * 当前库存数量
     */
    @TableField(value = "quantity")
    private Integer quantity;

    /**
     * 锁定库存数量（已下单未出库）
     */
    @TableField(value = "locked_quantity")
    private Integer lockedQuantity;

    /**
     * 可用库存数量
     */
    @TableField(value = "available_quantity")
    private Integer availableQuantity;

    /**
     * 库存单位
     */
    @TableField(value = "unit")
    private String unit;

    /**
     * 库存成本
     */
    @TableField(value = "cost")
    private BigDecimal cost;

    /**
     * 批次号
     */
    @TableField(value = "batch_no")
    private String batchNo;

    /**
     * 生产日期
     */
    @TableField(value = "production_date")
    private Date productionDate;

    /**
     * 过期日期
     */
    @TableField(value = "expiry_date")
    private Date expiryDate;

    /**
     * 状态（0正常 1锁定 2待检 3报废）
     */
    @TableField(value = "status")
    private String status;

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
    private Integer transferQuantity;
    @TableField(exist = false)
    private Long targetWarehouseId;
}