package com.chennian.storytelling.payment.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import javax.validation.constraints.DecimalMin;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Map;

/**
 * 创建支付请求
 * 
 * @author chennian
 * @since 2024-01-01
 */
@Data
@Schema(description = "创建支付请求")
public class PaymentCreateRequest {

    @Schema(description = "业务订单号")
    @NotBlank(message = "业务订单号不能为空")
    private String businessOrderNo;

    @Schema(description = "用户ID")
    @NotNull(message = "用户ID不能为空")
    private Long userId;

    @Schema(description = "支付金额")
    @NotNull(message = "支付金额不能为空")
    @DecimalMin(value = "0.01", message = "支付金额必须大于0")
    private BigDecimal paymentAmount;

    @Schema(description = "货币类型")
    @NotBlank(message = "货币类型不能为空")
    private String currency;

    @Schema(description = "支付渠道")
    @NotBlank(message = "支付渠道不能为空")
    private String paymentChannel;

    @Schema(description = "支付方式")
    @NotBlank(message = "支付方式不能为空")
    private String paymentMethod;

    @Schema(description = "商品标题")
    @NotBlank(message = "商品标题不能为空")
    private String subject;

    @Schema(description = "商品描述")
    private String body;

    @Schema(description = "订单过期时间")
    private LocalDateTime expireTime;

    @Schema(description = "异步通知地址")
    private String notifyUrl;

    @Schema(description = "同步回调地址")
    private String returnUrl;

    @Schema(description = "客户端IP")
    private String clientIp;

    @Schema(description = "设备信息")
    private String deviceInfo;

    @Schema(description = "扩展参数")
    private Map<String, Object> extraParams;

    @Schema(description = "备注")
    private String remark;
}