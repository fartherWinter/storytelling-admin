package com.chennian.storytelling.payment.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Map;

/**
 * 退款申请响应
 * 
 * @author chennian
 * @since 2024-01-01
 */
@Data
@Schema(description = "退款申请响应")
public class RefundApplyResponse {

    @Schema(description = "退款订单号")
    private String refundOrderNo;

    @Schema(description = "支付订单号")
    private String paymentOrderNo;

    @Schema(description = "退款金额")
    private BigDecimal refundAmount;

    @Schema(description = "退款状态")
    private String refundStatus;

    @Schema(description = "审核状态")
    private String auditStatus;

    @Schema(description = "创建时间")
    private LocalDateTime createTime;

    @Schema(description = "预计处理时间")
    private LocalDateTime expectedProcessTime;

    @Schema(description = "扩展信息")
    private Map<String, Object> extraInfo;
}