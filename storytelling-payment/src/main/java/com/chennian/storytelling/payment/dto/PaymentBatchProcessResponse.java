package com.chennian.storytelling.payment.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

/**
 * 支付批量处理响应
 * 
 * @author chennian
 * @since 2024-01-01
 */
@Data
@Schema(description = "支付批量处理响应")
public class PaymentBatchProcessResponse {

    @Schema(description = "批次ID")
    private String batchId;

    @Schema(description = "处理结果")
    private Boolean success;

    @Schema(description = "总数量")
    private Integer totalCount;

    @Schema(description = "成功数量")
    private Integer successCount;

    @Schema(description = "失败数量")
    private Integer failedCount;

    @Schema(description = "跳过数量")
    private Integer skippedCount;

    @Schema(description = "处理状态")
    private String processStatus;

    @Schema(description = "处理消息")
    private String message;

    @Schema(description = "开始时间")
    private LocalDateTime startTime;

    @Schema(description = "结束时间")
    private LocalDateTime endTime;

    @Schema(description = "处理耗时（毫秒）")
    private Long processDuration;

    @Schema(description = "成功处理的订单")
    private List<ProcessResult> successResults;

    @Schema(description = "失败处理的订单")
    private List<ProcessResult> failedResults;

    @Schema(description = "跳过处理的订单")
    private List<ProcessResult> skippedResults;

    @Schema(description = "处理进度")
    private Integer progress;

    @Schema(description = "是否异步处理")
    private Boolean async;

    @Schema(description = "扩展信息")
    private Map<String, Object> extraInfo;

    /**
     * 处理结果
     */
    @Data
    @Schema(description = "处理结果")
    public static class ProcessResult {
        @Schema(description = "支付订单号")
        private String paymentOrderNo;
        
        @Schema(description = "处理状态")
        private String status;
        
        @Schema(description = "处理消息")
        private String message;
        
        @Schema(description = "错误代码")
        private String errorCode;
        
        @Schema(description = "处理时间")
        private LocalDateTime processTime;
        
        @Schema(description = "结果数据")
        private Map<String, Object> resultData;
    }
}