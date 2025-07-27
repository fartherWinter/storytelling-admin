package com.chennian.storytelling.payment.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Map;

/**
 * 退款处理响应
 * 
 * @author chennian
 * @since 2024-01-01
 */
@Data
@Schema(description = "退款处理响应")
public class RefundProcessResponse {

    @Schema(description = "退款订单号")
    private String refundOrderNo;

    @Schema(description = "第三方退款单号")
    private String thirdRefundNo;

    @Schema(description = "退款状态")
    private String refundStatus;

    @Schema(description = "退款金额")
    private BigDecimal refundAmount;

    @Schema(description = "实际退款金额")
    private BigDecimal actualRefundAmount;

    @Schema(description = "退款手续费")
    private BigDecimal refundFee;

    @Schema(description = "处理时间")
    private LocalDateTime processTime;

    @Schema(description = "预计到账时间")
    private LocalDateTime expectedArrivalTime;

    @Schema(description = "处理消息")
    private String message;

    @Schema(description = "扩展信息")
    private Map<String, Object> extraInfo;
}