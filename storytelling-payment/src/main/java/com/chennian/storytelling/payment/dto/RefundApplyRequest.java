package com.chennian.storytelling.payment.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import javax.validation.constraints.DecimalMin;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.math.BigDecimal;
import java.util.Map;

/**
 * 退款申请请求
 * 
 * @author chennian
 * @since 2024-01-01
 */
@Data
@Schema(description = "退款申请请求")
public class RefundApplyRequest {

    @Schema(description = "支付订单号")
    @NotBlank(message = "支付订单号不能为空")
    private String paymentOrderNo;

    @Schema(description = "退款金额")
    @NotNull(message = "退款金额不能为空")
    @DecimalMin(value = "0.01", message = "退款金额必须大于0")
    private BigDecimal refundAmount;

    @Schema(description = "退款原因")
    @NotBlank(message = "退款原因不能为空")
    private String refundReason;

    @Schema(description = "申请人ID")
    private Long applyUserId;

    @Schema(description = "异步通知地址")
    private String notifyUrl;

    @Schema(description = "扩展参数")
    private Map<String, Object> extraParams;

    @Schema(description = "备注")
    private String remark;
}