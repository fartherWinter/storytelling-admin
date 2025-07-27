package com.chennian.storytelling.payment.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.Map;

/**
 * 支付重试响应
 * 
 * @author chennian
 * @since 2024-01-01
 */
@Data
@Schema(description = "支付重试响应")
public class PaymentRetryResponse {

    @Schema(description = "重试结果")
    private Boolean success;

    @Schema(description = "支付订单号")
    private String paymentOrderNo;

    @Schema(description = "重试次数")
    private Integer retryCount;

    @Schema(description = "重试状态")
    private String retryStatus;

    @Schema(description = "重试消息")
    private String message;

    @Schema(description = "下次重试时间")
    private LocalDateTime nextRetryTime;

    @Schema(description = "重试结果数据")
    private Map<String, Object> resultData;

    @Schema(description = "扩展信息")
    private Map<String, Object> extraInfo;
}