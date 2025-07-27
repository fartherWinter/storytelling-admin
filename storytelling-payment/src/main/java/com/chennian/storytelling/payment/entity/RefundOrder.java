package com.chennian.storytelling.payment.entity;

import com.baomidou.mybatisplus.annotation.*;
import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import javax.validation.constraints.*;
import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * 退款订单实体类
 *
 * @author chennian
 * @since 2024-01-01
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@TableName("refund_order")
@Schema(description = "退款订单")
public class RefundOrder {

    @Schema(description = "主键ID")
    @TableId(value = "id", type = IdType.ASSIGN_ID)
    private Long id;

    @Schema(description = "退款订单号")
    @TableField("refund_order_no")
    @NotBlank(message = "退款订单号不能为空")
    @Size(max = 64, message = "退款订单号长度不能超过64个字符")
    private String refundOrderNo;

    @Schema(description = "支付订单ID")
    @TableField("payment_order_id")
    @NotNull(message = "支付订单ID不能为空")
    private Long paymentOrderId;

    @Schema(description = "支付订单号")
    @TableField("payment_order_no")
    @NotBlank(message = "支付订单号不能为空")
    @Size(max = 64, message = "支付订单号长度不能超过64个字符")
    private String paymentOrderNo;

    @Schema(description = "业务订单号")
    @TableField("business_order_no")
    @NotBlank(message = "业务订单号不能为空")
    @Size(max = 64, message = "业务订单号长度不能超过64个字符")
    private String businessOrderNo;

    @Schema(description = "业务类型")
    @TableField("business_type")
    @NotBlank(message = "业务类型不能为空")
    @Size(max = 32, message = "业务类型长度不能超过32个字符")
    private String businessType;

    @Schema(description = "用户ID")
    @TableField("user_id")
    @NotNull(message = "用户ID不能为空")
    private Long userId;

    @Schema(description = "支付渠道")
    @TableField("payment_channel")
    @NotBlank(message = "支付渠道不能为空")
    @Size(max = 32, message = "支付渠道长度不能超过32个字符")
    private String paymentChannel;

    @Schema(description = "支付方式")
    @TableField("payment_method")
    @NotBlank(message = "支付方式不能为空")
    @Size(max = 32, message = "支付方式长度不能超过32个字符")
    private String paymentMethod;

    @Schema(description = "退款类型")
    @TableField("refund_type")
    @NotBlank(message = "退款类型不能为空")
    @Size(max = 16, message = "退款类型长度不能超过16个字符")
    private String refundType;

    @Schema(description = "退款原因")
    @TableField("refund_reason")
    @NotBlank(message = "退款原因不能为空")
    @Size(max = 256, message = "退款原因长度不能超过256个字符")
    private String refundReason;

    @Schema(description = "原支付金额（分）")
    @TableField("original_amount")
    @NotNull(message = "原支付金额不能为空")
    @DecimalMin(value = "0.01", message = "原支付金额必须大于0")
    private BigDecimal originalAmount;

    @Schema(description = "退款金额（分）")
    @TableField("refund_amount")
    @NotNull(message = "退款金额不能为空")
    @DecimalMin(value = "0.01", message = "退款金额必须大于0")
    private BigDecimal refundAmount;

    @Schema(description = "实际退款金额（分）")
    @TableField("actual_refund_amount")
    private BigDecimal actualRefundAmount;

    @Schema(description = "退款手续费（分）")
    @TableField("refund_fee")
    private BigDecimal refundFee;

    @Schema(description = "货币类型")
    @TableField("currency")
    @NotBlank(message = "货币类型不能为空")
    @Size(max = 8, message = "货币类型长度不能超过8个字符")
    private String currency;

    @Schema(description = "退款状态")
    @TableField("refund_status")
    @NotBlank(message = "退款状态不能为空")
    @Size(max = 16, message = "退款状态长度不能超过16个字符")
    private String refundStatus;

    @Schema(description = "第三方退款单号")
    @TableField("third_party_refund_no")
    @Size(max = 128, message = "第三方退款单号长度不能超过128个字符")
    private String thirdPartyRefundNo;

    @Schema(description = "第三方交易号")
    @TableField("third_party_trade_no")
    @Size(max = 128, message = "第三方交易号长度不能超过128个字符")
    private String thirdPartyTradeNo;

    @Schema(description = "退款描述")
    @TableField("description")
    @Size(max = 512, message = "退款描述长度不能超过512个字符")
    private String description;

    @Schema(description = "异步通知地址")
    @TableField("notify_url")
    @Size(max = 512, message = "异步通知地址长度不能超过512个字符")
    private String notifyUrl;

    @Schema(description = "申请时间")
    @TableField("apply_time")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime applyTime;

    @Schema(description = "退款时间")
    @TableField("refund_time")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime refundTime;

    @Schema(description = "到账时间")
    @TableField("arrival_time")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime arrivalTime;

    @Schema(description = "审核状态")
    @TableField("audit_status")
    @Size(max = 16, message = "审核状态长度不能超过16个字符")
    private String auditStatus;

    @Schema(description = "审核人")
    @TableField("audit_by")
    @Size(max = 64, message = "审核人长度不能超过64个字符")
    private String auditBy;

    @Schema(description = "审核时间")
    @TableField("audit_time")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime auditTime;

    @Schema(description = "审核备注")
    @TableField("audit_remark")
    @Size(max = 512, message = "审核备注长度不能超过512个字符")
    private String auditRemark;

    @Schema(description = "客户端IP")
    @TableField("client_ip")
    @Size(max = 64, message = "客户端IP长度不能超过64个字符")
    private String clientIp;

    @Schema(description = "设备信息")
    @TableField("device_info")
    @Size(max = 256, message = "设备信息长度不能超过256个字符")
    private String deviceInfo;

    @Schema(description = "扩展参数")
    @TableField("extra_params")
    private String extraParams;

    @Schema(description = "备注")
    @TableField("remark")
    @Size(max = 512, message = "备注长度不能超过512个字符")
    private String remark;

    @Schema(description = "版本号")
    @TableField("version")
    @Version
    private Integer version;

    @Schema(description = "是否删除")
    @TableField("is_deleted")
    @TableLogic
    private Boolean isDeleted;

    @Schema(description = "创建时间")
    @TableField(value = "create_time", fill = FieldFill.INSERT)
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime createTime;

    @Schema(description = "更新时间")
    @TableField(value = "update_time", fill = FieldFill.INSERT_UPDATE)
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime updateTime;

    @Schema(description = "创建人")
    @TableField(value = "create_by", fill = FieldFill.INSERT)
    private String createBy;

    @Schema(description = "更新人")
    @TableField(value = "update_by", fill = FieldFill.INSERT_UPDATE)
    private String updateBy;
}