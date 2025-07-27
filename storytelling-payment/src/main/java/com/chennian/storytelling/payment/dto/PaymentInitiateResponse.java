package com.chennian.storytelling.payment.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.util.Map;

/**
 * 发起支付响应
 * 
 * @author chennian
 * @since 2024-01-01
 */
@Data
@Schema(description = "发起支付响应")
public class PaymentInitiateResponse {

    @Schema(description = "支付订单号")
    private String paymentOrderNo;

    @Schema(description = "第三方交易号")
    private String thirdTradeNo;

    @Schema(description = "支付状态")
    private String paymentStatus;

    @Schema(description = "支付URL")
    private String paymentUrl;

    @Schema(description = "支付二维码")
    private String qrCode;

    @Schema(description = "支付表单HTML")
    private String paymentForm;

    @Schema(description = "支付参数")
    private Map<String, Object> paymentParams;

    @Schema(description = "跳转类型：redirect-重定向，form-表单提交，qrcode-二维码")
    private String redirectType;

    @Schema(description = "是否需要用户确认")
    private Boolean needConfirm;

    @Schema(description = "扩展信息")
    private Map<String, Object> extraInfo;
}