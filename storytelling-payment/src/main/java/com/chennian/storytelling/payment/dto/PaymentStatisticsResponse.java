package com.chennian.storytelling.payment.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

/**
 * 支付统计响应
 * 
 * @author chennian
 * @since 2024-01-01
 */
@Data
@Schema(description = "支付统计响应")
public class PaymentStatisticsResponse {

    @Schema(description = "统计时间范围")
    private String timeRange;

    @Schema(description = "总订单数")
    private Long totalOrders;

    @Schema(description = "成功订单数")
    private Long successOrders;

    @Schema(description = "失败订单数")
    private Long failedOrders;

    @Schema(description = "待支付订单数")
    private Long pendingOrders;

    @Schema(description = "总金额")
    private BigDecimal totalAmount;

    @Schema(description = "成功金额")
    private BigDecimal successAmount;

    @Schema(description = "失败金额")
    private BigDecimal failedAmount;

    @Schema(description = "待支付金额")
    private BigDecimal pendingAmount;

    @Schema(description = "总手续费")
    private BigDecimal totalFee;

    @Schema(description = "成功率")
    private BigDecimal successRate;

    @Schema(description = "平均支付金额")
    private BigDecimal avgPaymentAmount;

    @Schema(description = "平均处理时间（秒）")
    private BigDecimal avgProcessTime;

    @Schema(description = "按渠道统计")
    private List<ChannelStatistics> channelStatistics;

    @Schema(description = "按支付方式统计")
    private List<MethodStatistics> methodStatistics;

    @Schema(description = "按时间统计")
    private List<TimeStatistics> timeStatistics;

    @Schema(description = "扩展统计数据")
    private Map<String, Object> extraStatistics;

    @Schema(description = "统计生成时间")
    private LocalDateTime generateTime;

    /**
     * 渠道统计
     */
    @Data
    @Schema(description = "渠道统计")
    public static class ChannelStatistics {
        @Schema(description = "渠道编码")
        private String channelCode;
        
        @Schema(description = "渠道名称")
        private String channelName;
        
        @Schema(description = "订单数")
        private Long orderCount;
        
        @Schema(description = "成功订单数")
        private Long successCount;
        
        @Schema(description = "总金额")
        private BigDecimal totalAmount;
        
        @Schema(description = "成功金额")
        private BigDecimal successAmount;
        
        @Schema(description = "成功率")
        private BigDecimal successRate;
        
        @Schema(description = "手续费")
        private BigDecimal fee;
    }

    /**
     * 支付方式统计
     */
    @Data
    @Schema(description = "支付方式统计")
    public static class MethodStatistics {
        @Schema(description = "支付方式")
        private String paymentMethod;
        
        @Schema(description = "订单数")
        private Long orderCount;
        
        @Schema(description = "成功订单数")
        private Long successCount;
        
        @Schema(description = "总金额")
        private BigDecimal totalAmount;
        
        @Schema(description = "成功金额")
        private BigDecimal successAmount;
        
        @Schema(description = "成功率")
        private BigDecimal successRate;
    }

    /**
     * 时间统计
     */
    @Data
    @Schema(description = "时间统计")
    public static class TimeStatistics {
        @Schema(description = "时间点")
        private String timePoint;
        
        @Schema(description = "订单数")
        private Long orderCount;
        
        @Schema(description = "成功订单数")
        private Long successCount;
        
        @Schema(description = "总金额")
        private BigDecimal totalAmount;
        
        @Schema(description = "成功金额")
        private BigDecimal successAmount;
        
        @Schema(description = "成功率")
        private BigDecimal successRate;
    }
}