package com.chennian.storytelling.payment.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Map;

/**
 * 支付查询响应
 * 
 * @author chennian
 * @since 2024-01-01
 */
@Data
@Schema(description = "支付查询响应")
public class PaymentQueryResponse {

    @Schema(description = "支付订单号")
    private String paymentOrderNo;

    @Schema(description = "业务订单号")
    private String businessOrderNo;

    @Schema(description = "第三方交易号")
    private String thirdTradeNo;

    @Schema(description = "用户ID")
    private Long userId;

    @Schema(description = "支付金额")
    private BigDecimal paymentAmount;

    @Schema(description = "实际支付金额")
    private BigDecimal actualAmount;

    @Schema(description = "手续费")
    private BigDecimal fee;

    @Schema(description = "货币类型")
    private String currency;

    @Schema(description = "支付状态")
    private String paymentStatus;

    @Schema(description = "支付渠道")
    private String paymentChannel;

    @Schema(description = "支付方式")
    private String paymentMethod;

    @Schema(description = "商品标题")
    private String subject;

    @Schema(description = "商品描述")
    private String body;

    @Schema(description = "创建时间")
    private LocalDateTime createTime;

    @Schema(description = "支付时间")
    private LocalDateTime paymentTime;

    @Schema(description = "订单过期时间")
    private LocalDateTime expireTime;

    @Schema(description = "关闭时间")
    private LocalDateTime closeTime;

    @Schema(description = "第三方响应")
    private String thirdResponse;

    @Schema(description = "扩展参数")
    private Map<String, Object> extraParams;

    @Schema(description = "备注")
    private String remark;
}