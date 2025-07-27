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
 * 支付订单实体类
 *
 * @author chennian
 * @since 2024-01-01
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@TableName("payment_order")
@Schema(description = "支付订单")
public class PaymentOrder {

    @Schema(description = "主键ID")
    @TableId(value = "id", type = IdType.ASSIGN_ID)
    private Long id;

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

    @Schema(description = "支付金额（分）")
    @TableField("payment_amount")
    @NotNull(message = "支付金额不能为空")
    @DecimalMin(value = "0.01", message = "支付金额必须大于0")
    private BigDecimal paymentAmount;

    @Schema(description = "实际支付金额（分）")
    @TableField("actual_amount")
    private BigDecimal actualAmount;

    @Schema(description = "优惠金额（分）")
    @TableField("discount_amount")
    private BigDecimal discountAmount;

    @Schema(description = "手续费（分）")
    @TableField("fee_amount")
    private BigDecimal feeAmount;

    @Schema(description = "货币类型")
    @TableField("currency")
    @NotBlank(message = "货币类型不能为空")
    @Size(max = 8, message = "货币类型长度不能超过8个字符")
    private String currency;

    @Schema(description = "支付状态")
    @TableField("payment_status")
    @NotBlank(message = "支付状态不能为空")
    @Size(max = 16, message = "支付状态长度不能超过16个字符")
    private String paymentStatus;

    @Schema(description = "第三方交易号")
    @TableField("third_party_trade_no")
    @Size(max = 128, message = "第三方交易号长度不能超过128个字符")
    private String thirdPartyTradeNo;

    @Schema(description = "第三方订单号")
    @TableField("third_party_order_no")
    @Size(max = 128, message = "第三方订单号长度不能超过128个字符")
    private String thirdPartyOrderNo;

    @Schema(description = "支付主题")
    @TableField("subject")
    @NotBlank(message = "支付主题不能为空")
    @Size(max = 256, message = "支付主题长度不能超过256个字符")
    private String subject;

    @Schema(description = "支付描述")
    @TableField("description")
    @Size(max = 512, message = "支付描述长度不能超过512个字符")
    private String description;

    @Schema(description = "支付超时时间")
    @TableField("timeout_express")
    private Integer timeoutExpress;

    @Schema(description = "异步通知地址")
    @TableField("notify_url")
    @Size(max = 512, message = "异步通知地址长度不能超过512个字符")
    private String notifyUrl;

    @Schema(description = "同步返回地址")
    @TableField("return_url")
    @Size(max = 512, message = "同步返回地址长度不能超过512个字符")
    private String returnUrl;

    @Schema(description = "支付时间")
    @TableField("payment_time")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime paymentTime;

    @Schema(description = "过期时间")
    @TableField("expire_time")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime expireTime;

    @Schema(description = "退款状态")
    @TableField("refund_status")
    @Size(max = 16, message = "退款状态长度不能超过16个字符")
    private String refundStatus;

    @Schema(description = "已退款金额（分）")
    @TableField("refunded_amount")
    private BigDecimal refundedAmount;

    @Schema(description = "可退款金额（分）")
    @TableField("refundable_amount")
    private BigDecimal refundableAmount;

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