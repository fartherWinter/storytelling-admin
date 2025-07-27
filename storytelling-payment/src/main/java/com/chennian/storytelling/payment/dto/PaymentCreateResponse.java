package com.chennian.storytelling.payment.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Map;

/**
 * 创建支付响应
 * 
 * @author chennian
 * @since 2024-01-01
 */
@Data
@Schema(description = "创建支付响应")
public class PaymentCreateResponse {

    @Schema(description = "支付订单号")
    private String paymentOrderNo;

    @Schema(description = "业务订单号")
    private String businessOrderNo;

    @Schema(description = "支付金额")
    private BigDecimal paymentAmount;

    @Schema(description = "支付状态")
    private String paymentStatus;

    @Schema(description = "支付渠道")
    private String paymentChannel;

    @Schema(description = "支付方式")
    private String paymentMethod;

    @Schema(description = "创建时间")
    private LocalDateTime createTime;

    @Schema(description = "订单过期时间")
    private LocalDateTime expireTime;

    @Schema(description = "支付URL或二维码")
    private String paymentUrl;

    @Schema(description = "支付参数")
    private Map<String, Object> paymentParams;

    @Schema(description = "扩展信息")
    private Map<String, Object> extraInfo;
}