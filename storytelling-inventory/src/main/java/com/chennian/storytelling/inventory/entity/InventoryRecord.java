package com.chennian.storytelling.inventory.entity;

import com.baomidou.mybatisplus.annotation.*;
import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import javax.validation.constraints.*;
import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * 库存变动记录实体类
 * 
 * @author chennian
 * @since 2024-01-01
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@TableName("inventory_record")
@Schema(name = "InventoryRecord", description = "库存变动记录")
public class InventoryRecord implements Serializable {

    private static final long serialVersionUID = 1L;

    @Schema(description = "记录ID")
    @TableId(value = "record_id", type = IdType.ASSIGN_ID)
    private Long recordId;

    @Schema(description = "库存ID")
    @TableField("inventory_id")
    @NotNull(message = "库存ID不能为空")
    private Long inventoryId;

    @Schema(description = "商品ID")
    @TableField("product_id")
    @NotNull(message = "商品ID不能为空")
    private Long productId;

    @Schema(description = "仓库ID")
    @TableField("warehouse_id")
    @NotNull(message = "仓库ID不能为空")
    private Long warehouseId;

    @Schema(description = "SKU编码")
    @TableField("sku_code")
    @NotBlank(message = "SKU编码不能为空")
    @Size(max = 50, message = "SKU编码长度不能超过50个字符")
    private String skuCode;

    @Schema(description = "变动类型：1-入库，2-出库，3-调拨，4-盘点，5-锁定，6-解锁，7-预占，8-释放")
    @TableField("change_type")
    @NotNull(message = "变动类型不能为空")
    @Min(value = 1, message = "变动类型值不正确")
    @Max(value = 8, message = "变动类型值不正确")
    private Integer changeType;

    @Schema(description = "变动原因：1-采购入库，2-销售出库，3-退货入库，4-退货出库，5-调拨入库，6-调拨出库，7-盘盈，8-盘亏，9-订单锁定，10-订单解锁，11-预占库存，12-释放预占")
    @TableField("change_reason")
    @NotNull(message = "变动原因不能为空")
    @Min(value = 1, message = "变动原因值不正确")
    @Max(value = 12, message = "变动原因值不正确")
    private Integer changeReason;

    @Schema(description = "变动前库存数量")
    @TableField("before_stock")
    @NotNull(message = "变动前库存数量不能为空")
    @Min(value = 0, message = "变动前库存数量不能小于0")
    private Integer beforeStock;

    @Schema(description = "变动数量（正数为增加，负数为减少）")
    @TableField("change_quantity")
    @NotNull(message = "变动数量不能为空")
    private Integer changeQuantity;

    @Schema(description = "变动后库存数量")
    @TableField("after_stock")
    @NotNull(message = "变动后库存数量不能为空")
    @Min(value = 0, message = "变动后库存数量不能小于0")
    private Integer afterStock;

    @Schema(description = "变动前可用库存数量")
    @TableField("before_available_stock")
    @NotNull(message = "变动前可用库存数量不能为空")
    @Min(value = 0, message = "变动前可用库存数量不能小于0")
    private Integer beforeAvailableStock;

    @Schema(description = "变动后可用库存数量")
    @TableField("after_available_stock")
    @NotNull(message = "变动后可用库存数量不能为空")
    @Min(value = 0, message = "变动后可用库存数量不能小于0")
    private Integer afterAvailableStock;

    @Schema(description = "变动前锁定库存数量")
    @TableField("before_locked_stock")
    @NotNull(message = "变动前锁定库存数量不能为空")
    @Min(value = 0, message = "变动前锁定库存数量不能小于0")
    private Integer beforeLockedStock;

    @Schema(description = "变动后锁定库存数量")
    @TableField("after_locked_stock")
    @NotNull(message = "变动后锁定库存数量不能为空")
    @Min(value = 0, message = "变动后锁定库存数量不能小于0")
    private Integer afterLockedStock;

    @Schema(description = "关联业务单号")
    @TableField("business_no")
    @Size(max = 100, message = "关联业务单号长度不能超过100个字符")
    private String businessNo;

    @Schema(description = "关联业务类型：1-采购单，2-销售单，3-调拨单，4-盘点单，5-退货单")
    @TableField("business_type")
    @Min(value = 1, message = "关联业务类型值不正确")
    @Max(value = 5, message = "关联业务类型值不正确")
    private Integer businessType;

    @Schema(description = "操作人ID")
    @TableField("operator_id")
    private Long operatorId;

    @Schema(description = "操作人姓名")
    @TableField("operator_name")
    @Size(max = 50, message = "操作人姓名长度不能超过50个字符")
    private String operatorName;

    @Schema(description = "操作时间")
    @TableField("operate_time")
    @NotNull(message = "操作时间不能为空")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime operateTime;

    @Schema(description = "成本价")
    @TableField("cost_price")
    @DecimalMin(value = "0.00", message = "成本价不能小于0")
    @Digits(integer = 10, fraction = 2, message = "成本价格式不正确")
    private java.math.BigDecimal costPrice;

    @Schema(description = "变动金额")
    @TableField("change_amount")
    @Digits(integer = 12, fraction = 2, message = "变动金额格式不正确")
    private java.math.BigDecimal changeAmount;

    @Schema(description = "备注")
    @TableField("remark")
    @Size(max = 500, message = "备注长度不能超过500个字符")
    private String remark;

    @Schema(description = "创建时间")
    @TableField(value = "create_time", fill = FieldFill.INSERT)
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime createTime;

    @Schema(description = "创建者")
    @TableField(value = "create_by", fill = FieldFill.INSERT)
    @Size(max = 50, message = "创建者长度不能超过50个字符")
    private String createBy;

    // 非数据库字段
    @Schema(description = "商品名称")
    @TableField(exist = false)
    private String productName;

    @Schema(description = "仓库名称")
    @TableField(exist = false)
    private String warehouseName;

    @Schema(description = "变动类型名称")
    @TableField(exist = false)
    private String changeTypeName;

    @Schema(description = "变动原因名称")
    @TableField(exist = false)
    private String changeReasonName;

    @Schema(description = "关联业务类型名称")
    @TableField(exist = false)
    private String businessTypeName;
}