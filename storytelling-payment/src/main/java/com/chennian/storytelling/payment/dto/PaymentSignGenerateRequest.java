package com.chennian.storytelling.payment.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import javax.validation.constraints.NotBlank;
import java.util.Map;

/**
 * 支付签名生成请求
 * 
 * @author chennian
 * @since 2024-01-01
 */
@Data
@Schema(description = "支付签名生成请求")
public class PaymentSignGenerateRequest {

    @Schema(description = "支付渠道")
    @NotBlank(message = "支付渠道不能为空")
    private String paymentChannel;

    @Schema(description = "签名参数")
    private Map<String, Object> signParams;

    @Schema(description = "签名类型")
    private String signType;

    @Schema(description = "私钥")
    private String privateKey;
}