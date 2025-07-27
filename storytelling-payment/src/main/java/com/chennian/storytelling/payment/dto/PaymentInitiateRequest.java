package com.chennian.storytelling.payment.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import javax.validation.constraints.NotBlank;
import java.util.Map;

/**
 * 发起支付请求
 * 
 * @author chennian
 * @since 2024-01-01
 */
@Data
@Schema(description = "发起支付请求")
public class PaymentInitiateRequest {

    @Schema(description = "支付订单号")
    @NotBlank(message = "支付订单号不能为空")
    private String paymentOrderNo;

    @Schema(description = "客户端IP")
    private String clientIp;

    @Schema(description = "设备信息")
    private String deviceInfo;

    @Schema(description = "用户代理")
    private String userAgent;

    @Schema(description = "扩展参数")
    private Map<String, Object> extraParams;
}