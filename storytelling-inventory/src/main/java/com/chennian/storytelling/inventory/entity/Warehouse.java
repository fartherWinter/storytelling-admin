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
import java.util.List;

/**
 * 仓库实体类
 * 
 * @author chennian
 * @since 2024-01-01
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@TableName("warehouse")
@Schema(name = "Warehouse", description = "仓库信息")
public class Warehouse implements Serializable {

    private static final long serialVersionUID = 1L;

    @Schema(description = "仓库ID")
    @TableId(value = "warehouse_id", type = IdType.ASSIGN_ID)
    private Long warehouseId;

    @Schema(description = "仓库编码")
    @TableField("warehouse_code")
    @NotBlank(message = "仓库编码不能为空")
    @Size(max = 50, message = "仓库编码长度不能超过50个字符")
    private String warehouseCode;

    @Schema(description = "仓库名称")
    @TableField("warehouse_name")
    @NotBlank(message = "仓库名称不能为空")
    @Size(max = 100, message = "仓库名称长度不能超过100个字符")
    private String warehouseName;

    @Schema(description = "仓库类型：1-主仓，2-分仓，3-虚拟仓，4-退货仓")
    @TableField("warehouse_type")
    @NotNull(message = "仓库类型不能为空")
    @Min(value = 1, message = "仓库类型值不正确")
    @Max(value = 4, message = "仓库类型值不正确")
    private Integer warehouseType;

    @Schema(description = "所属区域ID")
    @TableField("region_id")
    private Long regionId;

    @Schema(description = "所属区域名称")
    @TableField("region_name")
    @Size(max = 100, message = "所属区域名称长度不能超过100个字符")
    private String regionName;

    @Schema(description = "省份")
    @TableField("province")
    @Size(max = 50, message = "省份长度不能超过50个字符")
    private String province;

    @Schema(description = "城市")
    @TableField("city")
    @Size(max = 50, message = "城市长度不能超过50个字符")
    private String city;

    @Schema(description = "区县")
    @TableField("district")
    @Size(max = 50, message = "区县长度不能超过50个字符")
    private String district;

    @Schema(description = "详细地址")
    @TableField("address")
    @Size(max = 200, message = "详细地址长度不能超过200个字符")
    private String address;

    @Schema(description = "邮政编码")
    @TableField("postal_code")
    @Size(max = 10, message = "邮政编码长度不能超过10个字符")
    private String postalCode;

    @Schema(description = "经度")
    @TableField("longitude")
    @DecimalMin(value = "-180.0", message = "经度值不正确")
    @DecimalMax(value = "180.0", message = "经度值不正确")
    @Digits(integer = 3, fraction = 6, message = "经度格式不正确")
    private java.math.BigDecimal longitude;

    @Schema(description = "纬度")
    @TableField("latitude")
    @DecimalMin(value = "-90.0", message = "纬度值不正确")
    @DecimalMax(value = "90.0", message = "纬度值不正确")
    @Digits(integer = 2, fraction = 6, message = "纬度格式不正确")
    private java.math.BigDecimal latitude;

    @Schema(description = "联系人")
    @TableField("contact_person")
    @Size(max = 50, message = "联系人长度不能超过50个字符")
    private String contactPerson;

    @Schema(description = "联系电话")
    @TableField("contact_phone")
    @Size(max = 20, message = "联系电话长度不能超过20个字符")
    @Pattern(regexp = "^[0-9-+()\\s]*$", message = "联系电话格式不正确")
    private String contactPhone;

    @Schema(description = "联系邮箱")
    @TableField("contact_email")
    @Size(max = 100, message = "联系邮箱长度不能超过100个字符")
    @Email(message = "联系邮箱格式不正确")
    private String contactEmail;

    @Schema(description = "仓库面积（平方米）")
    @TableField("area")
    @DecimalMin(value = "0.0", message = "仓库面积不能小于0")
    @Digits(integer = 10, fraction = 2, message = "仓库面积格式不正确")
    private java.math.BigDecimal area;

    @Schema(description = "仓库容量")
    @TableField("capacity")
    @Min(value = 0, message = "仓库容量不能小于0")
    private Integer capacity;

    @Schema(description = "当前使用容量")
    @TableField("used_capacity")
    @Min(value = 0, message = "当前使用容量不能小于0")
    private Integer usedCapacity;

    @Schema(description = "仓库状态：0-停用，1-启用，2-维护中")
    @TableField("status")
    @NotNull(message = "仓库状态不能为空")
    @Min(value = 0, message = "仓库状态值不正确")
    @Max(value = 2, message = "仓库状态值不正确")
    private Integer status;

    @Schema(description = "是否默认仓库：0-否，1-是")
    @TableField("is_default")
    @NotNull(message = "是否默认仓库不能为空")
    @Min(value = 0, message = "是否默认仓库值不正确")
    @Max(value = 1, message = "是否默认仓库值不正确")
    private Integer isDefault;

    @Schema(description = "是否支持自动分配：0-否，1-是")
    @TableField("is_auto_allocate")
    @NotNull(message = "是否支持自动分配不能为空")
    @Min(value = 0, message = "是否支持自动分配值不正确")
    @Max(value = 1, message = "是否支持自动分配值不正确")
    private Integer isAutoAllocate;

    @Schema(description = "排序")
    @TableField("sort_order")
    @Min(value = 0, message = "排序值不能小于0")
    private Integer sortOrder;

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
    @Schema(description = "库存商品数量")
    @TableField(exist = false)
    private Integer productCount;

    @Schema(description = "总库存数量")
    @TableField(exist = false)
    private Integer totalStock;

    @Schema(description = "容量使用率")
    @TableField(exist = false)
    private java.math.BigDecimal capacityUsageRate;

    @Schema(description = "子仓库列表")
    @TableField(exist = false)
    private List<Warehouse> children;

    @Schema(description = "仓库类型名称")
    @TableField(exist = false)
    private String warehouseTypeName;
}