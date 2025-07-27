package com.chennian.storytelling.payment.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Map;

/**
 * 退款查询响应
 * 
 * @author chennian
 * @since 2024-01-01
 */
@Data
@Schema(description = "退款查询响应")
public class RefundQueryResponse {

    @Schema(description = "退款订单号")
    private String refundOrderNo;

    @Schema(description = "支付订单号")
    private String paymentOrderNo;

    @Schema(description = "业务订单号")
    private String businessOrderNo;

    @Schema(description = "第三方退款单号")
    private String thirdRefundNo;

    @Schema(description = "用户ID")
    private Long userId;

    @Schema(description = "退款金额")
    private BigDecimal refundAmount;

    @Schema(description = "实际退款金额")
    private BigDecimal actualRefundAmount;

    @Schema(description = "退款手续费")
    private BigDecimal refundFee;

    @Schema(description = "退款状态")
    private String refundStatus;

    @Schema(description = "审核状态")
    private String auditStatus;

    @Schema(description = "支付渠道")
    private String paymentChannel;

    @Schema(description = "退款原因")
    private String refundReason;

    @Schema(description = "创建时间")
    private LocalDateTime createTime;

    @Schema(description = "审核时间")
    private LocalDateTime auditTime;

    @Schema(description = "退款时间")
    private LocalDateTime refundTime;

    @Schema(description = "完成时间")
    private LocalDateTime finishTime;

    @Schema(description = "审核人ID")
    private Long auditUserId;

    @Schema(description = "审核备注")
    private String auditRemark;

    @Schema(description = "第三方响应")
    private String thirdResponse;

    @Schema(description = "扩展参数")
    private Map<String, Object> extraParams;

    @Schema(description = "备注")
    private String remark;
}