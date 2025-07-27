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
 * 库存操作日志实体类
 * 
 * @author chennian
 * @since 2024-01-01
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@TableName("inventory_log")
@Schema(name = "InventoryLog", description = "库存操作日志")
public class InventoryLog implements Serializable {

    private static final long serialVersionUID = 1L;

    @Schema(description = "日志ID")
    @TableId(value = "log_id", type = IdType.ASSIGN_ID)
    private Long logId;

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

    @Schema(description = "操作类型：1-查询，2-新增，3-修改，4-删除，5-入库，6-出库，7-调拨，8-盘点，9-锁定，10-解锁")
    @TableField("operation_type")
    @NotNull(message = "操作类型不能为空")
    @Min(value = 1, message = "操作类型值不正确")
    @Max(value = 10, message = "操作类型值不正确")
    private Integer operationType;

    @Schema(description = "操作描述")
    @TableField("operation_desc")
    @NotBlank(message = "操作描述不能为空")
    @Size(max = 200, message = "操作描述长度不能超过200个字符")
    private String operationDesc;

    @Schema(description = "操作前数据（JSON格式）")
    @TableField("before_data")
    @Size(max = 2000, message = "操作前数据长度不能超过2000个字符")
    private String beforeData;

    @Schema(description = "操作后数据（JSON格式）")
    @TableField("after_data")
    @Size(max = 2000, message = "操作后数据长度不能超过2000个字符")
    private String afterData;

    @Schema(description = "操作结果：1-成功，2-失败")
    @TableField("operation_result")
    @NotNull(message = "操作结果不能为空")
    @Min(value = 1, message = "操作结果值不正确")
    @Max(value = 2, message = "操作结果值不正确")
    private Integer operationResult;

    @Schema(description = "错误信息")
    @TableField("error_message")
    @Size(max = 500, message = "错误信息长度不能超过500个字符")
    private String errorMessage;

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

    @Schema(description = "操作IP地址")
    @TableField("operator_ip")
    @Size(max = 50, message = "操作IP地址长度不能超过50个字符")
    private String operatorIp;

    @Schema(description = "用户代理")
    @TableField("user_agent")
    @Size(max = 500, message = "用户代理长度不能超过500个字符")
    private String userAgent;

    @Schema(description = "请求方法")
    @TableField("request_method")
    @Size(max = 10, message = "请求方法长度不能超过10个字符")
    private String requestMethod;

    @Schema(description = "请求URL")
    @TableField("request_url")
    @Size(max = 500, message = "请求URL长度不能超过500个字符")
    private String requestUrl;

    @Schema(description = "请求参数")
    @TableField("request_params")
    @Size(max = 2000, message = "请求参数长度不能超过2000个字符")
    private String requestParams;

    @Schema(description = "响应数据")
    @TableField("response_data")
    @Size(max = 2000, message = "响应数据长度不能超过2000个字符")
    private String responseData;

    @Schema(description = "执行时间（毫秒）")
    @TableField("execution_time")
    @Min(value = 0, message = "执行时间不能小于0")
    private Long executionTime;

    @Schema(description = "操作时间")
    @TableField("operate_time")
    @NotNull(message = "操作时间不能为空")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime operateTime;

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

    @Schema(description = "操作类型名称")
    @TableField(exist = false)
    private String operationTypeName;

    @Schema(description = "操作结果名称")
    @TableField(exist = false)
    private String operationResultName;

    @Schema(description = "关联业务类型名称")
    @TableField(exist = false)
    private String businessTypeName;
}