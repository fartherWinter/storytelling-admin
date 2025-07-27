package com.chennian.storytelling.payment.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.util.Map;

/**
 * 支付回调请求
 * 
 * @author chennian
 * @since 2024-01-01
 */
@Data
@Schema(description = "支付回调请求")
public class PaymentCallbackRequest {

    @Schema(description = "支付订单号")
    private String paymentOrderNo;

    @Schema(description = "第三方交易号")
    private String thirdTradeNo;

    @Schema(description = "支付状态")
    private String paymentStatus;

    @Schema(description = "回调参数")
    private Map<String, Object> callbackParams;

    @Schema(description = "签名")
    private String sign;

    @Schema(description = "原始数据")
    private String rawData;
}