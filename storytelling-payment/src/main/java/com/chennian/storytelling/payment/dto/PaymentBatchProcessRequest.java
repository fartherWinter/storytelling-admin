package com.chennian.storytelling.payment.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import javax.validation.constraints.NotEmpty;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

/**
 * 支付批量处理请求
 * 
 * @author chennian
 * @since 2024-01-01
 */
@Data
@Schema(description = "支付批量处理请求")
public class PaymentBatchProcessRequest {

    @Schema(description = "支付订单号列表")
    @NotEmpty(message = "支付订单号列表不能为空")
    private List<String> paymentOrderNos;

    @Schema(description = "处理类型")
    private String processType;

    @Schema(description = "处理操作")
    private String operation;

    @Schema(description = "过滤条件")
    private FilterCondition filterCondition;

    @Schema(description = "操作人ID")
    private Long operatorId;

    @Schema(description = "是否异步处理")
    private Boolean async;

    @Schema(description = "批次大小")
    private Integer batchSize;

    @Schema(description = "扩展参数")
    private Map<String, Object> extraParams;

    @Schema(description = "备注")
    private String remark;

    /**
     * 过滤条件
     */
    @Data
    @Schema(description = "过滤条件")
    public static class FilterCondition {
        @Schema(description = "支付状态列表")
        private List<String> paymentStatuses;
        
        @Schema(description = "支付渠道列表")
        private List<String> paymentChannels;
        
        @Schema(description = "支付方式列表")
        private List<String> paymentMethods;
        
        @Schema(description = "创建时间开始")
        private LocalDateTime createTimeStart;
        
        @Schema(description = "创建时间结束")
        private LocalDateTime createTimeEnd;
        
        @Schema(description = "用户ID列表")
        private List<Long> userIds;
        
        @Schema(description = "商户号列表")
        private List<String> merchantNos;
        
        @Schema(description = "最小金额")
        private java.math.BigDecimal minAmount;
        
        @Schema(description = "最大金额")
        private java.math.BigDecimal maxAmount;
    }
}