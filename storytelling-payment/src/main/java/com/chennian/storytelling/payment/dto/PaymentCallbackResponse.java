package com.chennian.storytelling.payment.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.util.Map;

/**
 * 支付回调响应
 * 
 * @author chennian
 * @since 2024-01-01
 */
@Data
@Schema(description = "支付回调响应")
public class PaymentCallbackResponse {

    @Schema(description = "处理结果")
    private Boolean success;

    @Schema(description = "支付订单号")
    private String paymentOrderNo;

    @Schema(description = "支付状态")
    private String paymentStatus;

    @Schema(description = "处理消息")
    private String message;

    @Schema(description = "跳转URL")
    private String redirectUrl;

    @Schema(description = "扩展信息")
    private Map<String, Object> extraInfo;
}