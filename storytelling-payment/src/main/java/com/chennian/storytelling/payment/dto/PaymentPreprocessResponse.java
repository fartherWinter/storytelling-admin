package com.chennian.storytelling.payment.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.util.Map;

/**
 * 支付预处理响应
 * 
 * @author chennian
 * @since 2024-01-01
 */
@Data
@Schema(description = "支付预处理响应")
public class PaymentPreprocessResponse {

    @Schema(description = "处理结果")
    private Boolean success;

    @Schema(description = "支付订单号")
    private String paymentOrderNo;

    @Schema(description = "处理状态")
    private String processStatus;

    @Schema(description = "处理消息")
    private String message;

    @Schema(description = "下一步操作")
    private String nextAction;

    @Schema(description = "处理结果数据")
    private Map<String, Object> resultData;

    @Schema(description = "扩展信息")
    private Map<String, Object> extraInfo;
}