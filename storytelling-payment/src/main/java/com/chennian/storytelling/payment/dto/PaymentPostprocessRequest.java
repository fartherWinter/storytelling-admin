package com.chennian.storytelling.payment.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import javax.validation.constraints.NotBlank;
import java.util.Map;

/**
 * 支付后处理请求
 * 
 * @author chennian
 * @since 2024-01-01
 */
@Data
@Schema(description = "支付后处理请求")
public class PaymentPostprocessRequest {

    @Schema(description = "支付订单号")
    @NotBlank(message = "支付订单号不能为空")
    private String paymentOrderNo;

    @Schema(description = "支付状态")
    private String paymentStatus;

    @Schema(description = "第三方交易号")
    private String thirdTradeNo;

    @Schema(description = "第三方响应")
    private String thirdResponse;

    @Schema(description = "支付渠道")
    private String paymentChannel;

    @Schema(description = "处理类型")
    private String processType;

    @Schema(description = "扩展参数")
    private Map<String, Object> extraParams;
}