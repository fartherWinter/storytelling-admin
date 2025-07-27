package com.chennian.storytelling.payment.controller;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.chennian.storytelling.common.result.Result;
import com.chennian.storytelling.payment.entity.PaymentRecord;
import com.chennian.storytelling.payment.service.PaymentRecordService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Positive;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

/**
 * 支付记录控制器
 * 
 * @author chennian
 * @since 2024-01-01
 */
@Slf4j
@RestController
@RequestMapping("/api/payment/record")
@RequiredArgsConstructor
@Validated
@Tag(name = "支付记录管理", description = "支付记录相关接口")
public class PaymentRecordController {

    private final PaymentRecordService paymentRecordService;

    @PostMapping
    @Operation(summary = "创建支付记录", description = "创建新的支付记录")
    public Result<PaymentRecord> createPaymentRecord(
            @Valid @RequestBody PaymentRecord paymentRecord) {
        try {
            PaymentRecord createdRecord = paymentRecordService.createPaymentRecord(paymentRecord);
            return Result.success(createdRecord);
        } catch (Exception e) {
            log.error("创建支付记录失败", e);
            return Result.error("创建支付记录失败: " + e.getMessage());
        }
    }

    @PutMapping("/{id}")
    @Operation(summary = "更新支付记录", description = "更新支付记录信息")
    public Result<Boolean> updatePaymentRecord(
            @Parameter(description = "记录ID") @PathVariable @NotNull Long id,
            @Valid @RequestBody PaymentRecord paymentRecord) {
        try {
            paymentRecord.setId(id);
            boolean result = paymentRecordService.updatePaymentRecord(paymentRecord);
            return Result.success(result);
        } catch (Exception e) {
            log.error("更新支付记录失败: {}", id, e);
            return Result.error("更新支付记录失败: " + e.getMessage());
        }
    }

    @GetMapping("/{id}")
    @Operation(summary = "根据ID查询", description = "根据记录ID查询支付记录")
    public Result<PaymentRecord> getById(
            @Parameter(description = "记录ID") @PathVariable @NotNull Long id) {
        try {
            PaymentRecord record = paymentRecordService.getById(id);
            if (record != null) {
                return Result.success(record);
            } else {
                return Result.error("支付记录不存在");
            }
        } catch (Exception e) {
            log.error("查询支付记录失败: {}", id, e);
            return Result.error("查询支付记录失败: " + e.getMessage());
        }
    }

    @GetMapping("/payment-order/{paymentOrderId}")
    @Operation(summary = "根据支付订单ID查询", description = "根据支付订单ID查询支付记录列表")
    public Result<List<PaymentRecord>> listByPaymentOrderId(
            @Parameter(description = "支付订单ID") @PathVariable @NotNull Long paymentOrderId) {
        try {
            List<PaymentRecord> records = paymentRecordService.listByPaymentOrderId(paymentOrderId);
            return Result.success(records);
        } catch (Exception e) {
            log.error("查询支付记录失败: {}", paymentOrderId, e);
            return Result.error("查询支付记录失败: " + e.getMessage());
        }
    }

    @GetMapping("/payment-order-no/{paymentOrderNo}")
    @Operation(summary = "根据支付订单号查询", description = "根据支付订单号查询支付记录列表")
    public Result<List<PaymentRecord>> listByPaymentOrderNo(
            @Parameter(description = "支付订单号") @PathVariable @NotBlank String paymentOrderNo) {
        try {
            List<PaymentRecord> records = paymentRecordService.listByPaymentOrderNo(paymentOrderNo);
            return Result.success(records);
        } catch (Exception e) {
            log.error("查询支付记录失败: {}", paymentOrderNo, e);
            return Result.error("查询支付记录失败: " + e.getMessage());
        }
    }

    @GetMapping("/record-type/{recordType}")
    @Operation(summary = "根据记录类型查询", description = "根据记录类型查询支付记录列表")
    public Result<List<PaymentRecord>> listByRecordType(
            @Parameter(description = "记录类型") @PathVariable @NotBlank String recordType) {
        try {
            List<PaymentRecord> records = paymentRecordService.listByRecordType(recordType);
            return Result.success(records);
        } catch (Exception e) {
            log.error("查询支付记录失败: {}", recordType, e);
            return Result.error("查询支付记录失败: " + e.getMessage());
        }
    }

    @GetMapping("/operation-type/{operationType}")
    @Operation(summary = "根据操作类型查询", description = "根据操作类型查询支付记录列表")
    public Result<List<PaymentRecord>> listByOperationType(
            @Parameter(description = "操作类型") @PathVariable @NotBlank String operationType) {
        try {
            List<PaymentRecord> records = paymentRecordService.listByOperationType(operationType);
            return Result.success(records);
        } catch (Exception e) {
            log.error("查询支付记录失败: {}", operationType, e);
            return Result.error("查询支付记录失败: " + e.getMessage());
        }
    }

    @GetMapping("/status/{status}")
    @Operation(summary = "根据状态查询", description = "根据状态查询支付记录列表")
    public Result<List<PaymentRecord>> listByStatus(
            @Parameter(description = "状态") @PathVariable @NotBlank String status) {
        try {
            List<PaymentRecord> records = paymentRecordService.listByStatus(status);
            return Result.success(records);
        } catch (Exception e) {
            log.error("查询支付记录失败: {}", status, e);
            return Result.error("查询支付记录失败: " + e.getMessage());
        }
    }

    @GetMapping("/channel/{paymentChannel}")
    @Operation(summary = "根据支付渠道查询", description = "根据支付渠道查询支付记录列表")
    public Result<List<PaymentRecord>> listByPaymentChannel(
            @Parameter(description = "支付渠道") @PathVariable @NotBlank String paymentChannel) {
        try {
            List<PaymentRecord> records = paymentRecordService.listByPaymentChannel(paymentChannel);
            return Result.success(records);
        } catch (Exception e) {
            log.error("查询支付记录失败: {}", paymentChannel, e);
            return Result.error("查询支付记录失败: " + e.getMessage());
        }
    }

    @GetMapping("/failed")
    @Operation(summary = "查询失败记录", description = "查询失败的支付记录")
    public Result<List<PaymentRecord>> listFailedRecords() {
        try {
            List<PaymentRecord> records = paymentRecordService.listFailedRecords();
            return Result.success(records);
        } catch (Exception e) {
            log.error("查询失败记录失败", e);
            return Result.error("查询失败记录失败: " + e.getMessage());
        }
    }

    @GetMapping("/need-retry")
    @Operation(summary = "查询需要重试的记录", description = "查询需要重试的支付记录")
    public Result<List<PaymentRecord>> listNeedRetryRecords() {
        try {
            List<PaymentRecord> records = paymentRecordService.listNeedRetryRecords();
            return Result.success(records);
        } catch (Exception e) {
            log.error("查询需要重试的记录失败", e);
            return Result.error("查询需要重试的记录失败: " + e.getMessage());
        }
    }

    @GetMapping("/page")
    @Operation(summary = "分页查询支付记录", description = "分页查询支付记录列表")
    public Result<IPage<PaymentRecord>> pagePaymentRecords(
            @Parameter(description = "当前页") @RequestParam(defaultValue = "1") @Positive int current,
            @Parameter(description = "页大小") @RequestParam(defaultValue = "10") @Positive int size,
            @Parameter(description = "记录类型") @RequestParam(required = false) String recordType,
            @Parameter(description = "操作类型") @RequestParam(required = false) String operationType,
            @Parameter(description = "状态") @RequestParam(required = false) String status,
            @Parameter(description = "支付渠道") @RequestParam(required = false) String paymentChannel) {
        try {
            IPage<PaymentRecord> page = paymentRecordService.pagePaymentRecords(current, size, recordType, operationType, status, paymentChannel);
            return Result.success(page);
        } catch (Exception e) {
            log.error("分页查询支付记录失败", e);
            return Result.error("分页查询支付记录失败: " + e.getMessage());
        }
    }

    @PostMapping("/record-request")
    @Operation(summary = "记录请求", description = "记录支付请求信息")
    public Result<Boolean> recordPaymentRequest(
            @Parameter(description = "支付订单号") @RequestParam @NotBlank String paymentOrderNo,
            @Parameter(description = "操作类型") @RequestParam @NotBlank String operationType,
            @Parameter(description = "请求参数") @RequestParam @NotBlank String requestParams) {
        try {
            boolean result = paymentRecordService.recordPaymentRequest(paymentOrderNo, operationType, requestParams);
            return Result.success(result);
        } catch (Exception e) {
            log.error("记录支付请求失败: {}", paymentOrderNo, e);
            return Result.error("记录支付请求失败: " + e.getMessage());
        }
    }

    @PostMapping("/record-response")
    @Operation(summary = "记录响应", description = "记录支付响应信息")
    public Result<Boolean> recordPaymentResponse(
            @Parameter(description = "支付订单号") @RequestParam @NotBlank String paymentOrderNo,
            @Parameter(description = "操作类型") @RequestParam @NotBlank String operationType,
            @Parameter(description = "响应结果") @RequestParam @NotBlank String responseResult) {
        try {
            boolean result = paymentRecordService.recordPaymentResponse(paymentOrderNo, operationType, responseResult);
            return Result.success(result);
        } catch (Exception e) {
            log.error("记录支付响应失败: {}", paymentOrderNo, e);
            return Result.error("记录支付响应失败: " + e.getMessage());
        }
    }

    @PostMapping("/record-refund-request")
    @Operation(summary = "记录退款请求", description = "记录退款请求信息")
    public Result<Boolean> recordRefundRequest(
            @Parameter(description = "退款订单号") @RequestParam @NotBlank String refundOrderNo,
            @Parameter(description = "请求参数") @RequestParam @NotBlank String requestParams) {
        try {
            boolean result = paymentRecordService.recordRefundRequest(refundOrderNo, requestParams);
            return Result.success(result);
        } catch (Exception e) {
            log.error("记录退款请求失败: {}", refundOrderNo, e);
            return Result.error("记录退款请求失败: " + e.getMessage());
        }
    }

    @PostMapping("/record-refund-response")
    @Operation(summary = "记录退款响应", description = "记录退款响应信息")
    public Result<Boolean> recordRefundResponse(
            @Parameter(description = "退款订单号") @RequestParam @NotBlank String refundOrderNo,
            @Parameter(description = "响应结果") @RequestParam @NotBlank String responseResult) {
        try {
            boolean result = paymentRecordService.recordRefundResponse(refundOrderNo, responseResult);
            return Result.success(result);
        } catch (Exception e) {
            log.error("记录退款响应失败: {}", refundOrderNo, e);
            return Result.error("记录退款响应失败: " + e.getMessage());
        }
    }

    @PostMapping("/record-query-request")
    @Operation(summary = "记录查询请求", description = "记录查询请求信息")
    public Result<Boolean> recordQueryRequest(
            @Parameter(description = "订单号") @RequestParam @NotBlank String orderNo,
            @Parameter(description = "查询类型") @RequestParam @NotBlank String queryType,
            @Parameter(description = "请求参数") @RequestParam @NotBlank String requestParams) {
        try {
            boolean result = paymentRecordService.recordQueryRequest(orderNo, queryType, requestParams);
            return Result.success(result);
        } catch (Exception e) {
            log.error("记录查询请求失败: {}", orderNo, e);
            return Result.error("记录查询请求失败: " + e.getMessage());
        }
    }

    @PostMapping("/record-query-response")
    @Operation(summary = "记录查询响应", description = "记录查询响应信息")
    public Result<Boolean> recordQueryResponse(
            @Parameter(description = "订单号") @RequestParam @NotBlank String orderNo,
            @Parameter(description = "查询类型") @RequestParam @NotBlank String queryType,
            @Parameter(description = "响应结果") @RequestParam @NotBlank String responseResult) {
        try {
            boolean result = paymentRecordService.recordQueryResponse(orderNo, queryType, responseResult);
            return Result.success(result);
        } catch (Exception e) {
            log.error("记录查询响应失败: {}", orderNo, e);
            return Result.error("记录查询响应失败: " + e.getMessage());
        }
    }

    @PutMapping("/increment-retry/{id}")
    @Operation(summary = "增加重试次数", description = "增加支付记录的重试次数")
    public Result<Boolean> incrementRetryCount(
            @Parameter(description = "记录ID") @PathVariable @NotNull Long id) {
        try {
            boolean result = paymentRecordService.incrementRetryCount(id);
            return Result.success(result);
        } catch (Exception e) {
            log.error("增加重试次数失败: {}", id, e);
            return Result.error("增加重试次数失败: " + e.getMessage());
        }
    }

    @GetMapping("/stats/operation-type")
    @Operation(summary = "统计操作类型分布", description = "统计各操作类型的记录数量")
    public Result<Map<String, Long>> getOperationTypeStats() {
        try {
            Map<String, Long> stats = paymentRecordService.getOperationTypeStats();
            return Result.success(stats);
        } catch (Exception e) {
            log.error("统计操作类型分布失败", e);
            return Result.error("统计操作类型分布失败: " + e.getMessage());
        }
    }

    @GetMapping("/stats/status")
    @Operation(summary = "统计状态分布", description = "统计各状态的记录数量")
    public Result<Map<String, Long>> getStatusStats() {
        try {
            Map<String, Long> stats = paymentRecordService.getStatusStats();
            return Result.success(stats);
        } catch (Exception e) {
            log.error("统计状态分布失败", e);
            return Result.error("统计状态分布失败: " + e.getMessage());
        }
    }

    @GetMapping("/stats/channel")
    @Operation(summary = "统计渠道分布", description = "统计各支付渠道的记录数量")
    public Result<Map<String, Long>> getChannelStats() {
        try {
            Map<String, Long> stats = paymentRecordService.getChannelStats();
            return Result.success(stats);
        } catch (Exception e) {
            log.error("统计渠道分布失败", e);
            return Result.error("统计渠道分布失败: " + e.getMessage());
        }
    }

    @GetMapping("/stats/avg-process-time")
    @Operation(summary = "统计平均处理时间", description = "统计平均处理时间（毫秒）")
    public Result<Double> getAverageProcessTime() {
        try {
            Double avgTime = paymentRecordService.getAverageProcessTime();
            return Result.success(avgTime);
        } catch (Exception e) {
            log.error("统计平均处理时间失败", e);
            return Result.error("统计平均处理时间失败: " + e.getMessage());
        }
    }

    @GetMapping("/stats/success-rate")
    @Operation(summary = "统计成功率", description = "统计操作成功率")
    public Result<Double> getSuccessRate() {
        try {
            Double successRate = paymentRecordService.getSuccessRate();
            return Result.success(successRate);
        } catch (Exception e) {
            log.error("统计成功率失败", e);
            return Result.error("统计成功率失败: " + e.getMessage());
        }
    }

    @GetMapping("/slow-operations")
    @Operation(summary = "查询慢操作记录", description = "查询处理时间超过阈值的记录")
    public Result<List<PaymentRecord>> getSlowOperationRecords(
            @Parameter(description = "时间阈值（毫秒）") @RequestParam(defaultValue = "5000") @Positive long thresholdMs) {
        try {
            List<PaymentRecord> records = paymentRecordService.getSlowOperationRecords(thresholdMs);
            return Result.success(records);
        } catch (Exception e) {
            log.error("查询慢操作记录失败", e);
            return Result.error("查询慢操作记录失败: " + e.getMessage());
        }
    }

    @DeleteMapping("/expired")
    @Operation(summary = "删除过期记录", description = "删除指定天数之前的记录")
    public Result<Integer> deleteExpiredRecords(
            @Parameter(description = "保留天数") @RequestParam(defaultValue = "90") @Positive int retentionDays) {
        try {
            int deletedCount = paymentRecordService.deleteExpiredRecords(retentionDays);
            return Result.success(deletedCount);
        } catch (Exception e) {
            log.error("删除过期记录失败", e);
            return Result.error("删除过期记录失败: " + e.getMessage());
        }
    }

    @GetMapping("/export")
    @Operation(summary = "导出支付记录", description = "导出指定条件的支付记录")
    public Result<String> exportPaymentRecords(
            @Parameter(description = "开始时间") @RequestParam(required = false) LocalDateTime startTime,
            @Parameter(description = "结束时间") @RequestParam(required = false) LocalDateTime endTime,
            @Parameter(description = "记录类型") @RequestParam(required = false) String recordType,
            @Parameter(description = "状态") @RequestParam(required = false) String status) {
        try {
            String exportPath = paymentRecordService.exportPaymentRecords(startTime, endTime, recordType, status);
            return Result.success(exportPath);
        } catch (Exception e) {
            log.error("导出支付记录失败", e);
            return Result.error("导出支付记录失败: " + e.getMessage());
        }
    }

    @GetMapping("/detail/{id}")
    @Operation(summary = "获取记录详情", description = "获取支付记录的详细信息")
    public Result<Map<String, Object>> getRecordDetail(
            @Parameter(description = "记录ID") @PathVariable @NotNull Long id) {
        try {
            Map<String, Object> detail = paymentRecordService.getRecordDetail(id);
            return Result.success(detail);
        } catch (Exception e) {
            log.error("获取记录详情失败: {}", id, e);
            return Result.error("获取记录详情失败: " + e.getMessage());
        }
    }

    @PostMapping("/validate/{id}")
    @Operation(summary = "验证记录完整性", description = "验证支付记录的完整性")
    public Result<Boolean> validateRecordIntegrity(
            @Parameter(description = "记录ID") @PathVariable @NotNull Long id) {
        try {
            boolean isValid = paymentRecordService.validateRecordIntegrity(id);
            return Result.success(isValid);
        } catch (Exception e) {
            log.error("验证记录完整性失败: {}", id, e);
            return Result.error("验证记录完整性失败: " + e.getMessage());
        }
    }
}