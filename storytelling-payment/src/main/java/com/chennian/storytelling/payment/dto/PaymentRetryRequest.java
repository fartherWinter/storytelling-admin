package com.chennian.storytelling.payment.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import javax.validation.constraints.NotBlank;
import java.util.Map;

/**
 * 支付重试请求
 * 
 * @author chennian
 * @since 2024-01-01
 */
@Data
@Schema(description = "支付重试请求")
public class PaymentRetryRequest {

    @Schema(description = "支付订单号")
    @NotBlank(message = "支付订单号不能为空")
    private String paymentOrderNo;

    @Schema(description = "重试类型")
    private String retryType;

    @Schema(description = "重试原因")
    private String retryReason;

    @Schema(description = "操作人ID")
    private Long operatorId;

    @Schema(description = "是否强制重试")
    private Boolean forceRetry;

    @Schema(description = "扩展参数")
    private Map<String, Object> extraParams;

    @Schema(description = "备注")
    private String remark;
}