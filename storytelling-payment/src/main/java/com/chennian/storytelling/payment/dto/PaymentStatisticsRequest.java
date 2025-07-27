package com.chennian.storytelling.payment.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.List;

/**
 * 支付统计请求
 * 
 * @author chennian
 * @since 2024-01-01
 */
@Data
@Schema(description = "支付统计请求")
public class PaymentStatisticsRequest {

    @Schema(description = "开始时间")
    private LocalDateTime startTime;

    @Schema(description = "结束时间")
    private LocalDateTime endTime;

    @Schema(description = "统计类型")
    private String statisticsType;

    @Schema(description = "统计维度")
    private List<String> dimensions;

    @Schema(description = "支付渠道")
    private List<String> paymentChannels;

    @Schema(description = "支付方式")
    private List<String> paymentMethods;

    @Schema(description = "支付状态")
    private List<String> paymentStatuses;

    @Schema(description = "用户ID列表")
    private List<Long> userIds;

    @Schema(description = "商户号列表")
    private List<String> merchantNos;

    @Schema(description = "货币类型")
    private List<String> currencies;

    @Schema(description = "分组方式")
    private String groupBy;

    @Schema(description = "排序方式")
    private String orderBy;

    @Schema(description = "是否包含详情")
    private Boolean includeDetails;
}