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
 * 库存实体类
 * 
 * @author chennian
 * @since 2024-01-01
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@TableName("inventory")
@Schema(name = "Inventory", description = "库存信息")
public class Inventory implements Serializable {

    private static final long serialVersionUID = 1L;

    @Schema(description = "库存ID")
    @TableId(value = "inventory_id", type = IdType.ASSIGN_ID)
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

    @Schema(description = "商品名称")
    @TableField("product_name")
    @NotBlank(message = "商品名称不能为空")
    @Size(max = 200, message = "商品名称长度不能超过200个字符")
    private String productName;

    @Schema(description = "规格属性")
    @TableField("spec_attrs")
    @Size(max = 500, message = "规格属性长度不能超过500个字符")
    private String specAttrs;

    @Schema(description = "当前库存数量")
    @TableField("current_stock")
    @NotNull(message = "当前库存数量不能为空")
    @Min(value = 0, message = "当前库存数量不能小于0")
    private Integer currentStock;

    @Schema(description = "可用库存数量")
    @TableField("available_stock")
    @NotNull(message = "可用库存数量不能为空")
    @Min(value = 0, message = "可用库存数量不能小于0")
    private Integer availableStock;

    @Schema(description = "锁定库存数量")
    @TableField("locked_stock")
    @NotNull(message = "锁定库存数量不能为空")
    @Min(value = 0, message = "锁定库存数量不能小于0")
    private Integer lockedStock;

    @Schema(description = "预占库存数量")
    @TableField("reserved_stock")
    @NotNull(message = "预占库存数量不能为空")
    @Min(value = 0, message = "预占库存数量不能小于0")
    private Integer reservedStock;

    @Schema(description = "安全库存数量")
    @TableField("safety_stock")
    @NotNull(message = "安全库存数量不能为空")
    @Min(value = 0, message = "安全库存数量不能小于0")
    private Integer safetyStock;

    @Schema(description = "最小库存数量")
    @TableField("min_stock")
    @NotNull(message = "最小库存数量不能为空")
    @Min(value = 0, message = "最小库存数量不能小于0")
    private Integer minStock;

    @Schema(description = "最大库存数量")
    @TableField("max_stock")
    @NotNull(message = "最大库存数量不能为空")
    @Min(value = 0, message = "最大库存数量不能小于0")
    private Integer maxStock;

    @Schema(description = "成本价")
    @TableField("cost_price")
    @DecimalMin(value = "0.00", message = "成本价不能小于0")
    @Digits(integer = 10, fraction = 2, message = "成本价格式不正确")
    private java.math.BigDecimal costPrice;

    @Schema(description = "库存状态：0-正常，1-冻结，2-异常")
    @TableField("status")
    @NotNull(message = "库存状态不能为空")
    @Min(value = 0, message = "库存状态值不正确")
    @Max(value = 2, message = "库存状态值不正确")
    private Integer status;

    @Schema(description = "是否启用库存预警：0-否，1-是")
    @TableField("is_alert_enabled")
    @NotNull(message = "是否启用库存预警不能为空")
    @Min(value = 0, message = "是否启用库存预警值不正确")
    @Max(value = 1, message = "是否启用库存预警值不正确")
    private Integer isAlertEnabled;

    @Schema(description = "最后盘点时间")
    @TableField("last_check_time")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime lastCheckTime;

    @Schema(description = "版本号（乐观锁）")
    @TableField("version")
    @Version
    private Integer version;

    @Schema(description = "备注")
    @TableField("remark")
    @Size(max = 500, message = "备注长度不能超过500个字符")
    private String remark;

    @Schema(description = "创建时间")
    @TableField(value = "create_time", fill = FieldFill.INSERT)
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime createTime;

    @Schema(description = "更新时间")
    @TableField(value = "update_time", fill = FieldFill.INSERT_UPDATE)
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime updateTime;

    @Schema(description = "创建者")
    @TableField(value = "create_by", fill = FieldFill.INSERT)
    @Size(max = 50, message = "创建者长度不能超过50个字符")
    private String createBy;

    @Schema(description = "更新者")
    @TableField(value = "update_by", fill = FieldFill.INSERT_UPDATE)
    @Size(max = 50, message = "更新者长度不能超过50个字符")
    private String updateBy;

    @Schema(description = "删除标志：0-未删除，1-已删除")
    @TableField("deleted")
    @TableLogic
    private Integer deleted;

    // 非数据库字段
    @Schema(description = "仓库名称")
    @TableField(exist = false)
    private String warehouseName;

    @Schema(description = "库存预警状态：0-正常，1-低库存，2-缺货")
    @TableField(exist = false)
    private Integer alertStatus;

    @Schema(description = "库存周转率")
    @TableField(exist = false)
    private java.math.BigDecimal turnoverRate;

    @Schema(description = "库存金额")
    @TableField(exist = false)
    private java.math.BigDecimal stockAmount;
}