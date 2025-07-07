package com.chennian.storytelling.bean.model;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * 库存操作日志实体类
 */
@Data
@EqualsAndHashCode(callSuper = false)
@ToString
@TableName("erp_inventory_log")
public class InventoryLog implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 日志ID
     */
    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    /**
     * 产品ID
     */
    private Long productId;

    /**
     * 产品名称
     */
    private String productName;

    /**
     * 产品编码
     */
    private String productCode;

    /**
     * 仓库ID
     */
    private Long warehouseId;

    /**
     * 仓库名称
     */
    private String warehouseName;

    /**
     * 操作类型：1-入库，2-出库，3-调拨，4-盘点，5-报损，6-退货
     */
    private Integer operationType;

    /**
     * 操作前库存数量
     */
    private Integer beforeQuantity;

    /**
     * 操作后库存数量
     */
    private Integer afterQuantity;

    /**
     * 变更数量（正数为增加，负数为减少）
     */
    private Integer changeQuantity;

    /**
     * 关联单据类型：1-采购单，2-销售单，3-调拨单，4-盘点单，5-报损单
     */
    private Integer documentType;

    /**
     * 关联单据号
     */
    private String documentNumber;

    /**
     * 操作原因
     */
    private String reason;

    /**
     * 操作备注
     */
    private String remark;

    /**
     * 操作人
     */
    private Long operatorId;

    /**
     * 操作人姓名
     */
    private String operatorName;

    /**
     * 操作时间
     */
    private LocalDateTime operationTime;

    /**
     * 创建时间
     */
    private LocalDateTime createTime;
}