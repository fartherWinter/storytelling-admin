package com.chennian.storytelling.payment.controller;

import com.chennian.storytelling.common.result.Result;
import com.chennian.storytelling.payment.dto.*;
import com.chennian.storytelling.payment.service.PaymentService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

/**
 * 支付服务控制器
 * 
 * @author chennian
 * @since 2024-01-01
 */
@Slf4j
@RestController
@RequestMapping("/api/payment")
@RequiredArgsConstructor
@Validated
@Tag(name = "支付服务", description = "支付相关接口")
public class PaymentController {

    private final PaymentService paymentService;

    @PostMapping("/create")
    @Operation(summary = "创建支付", description = "创建支付订单")
    public Result<PaymentCreateResponse> createPayment(
            @Valid @RequestBody PaymentCreateRequest request) {
        try {
            PaymentCreateResponse response = paymentService.createPayment(request);
            return Result.success(response);
        } catch (Exception e) {
            log.error("创建支付失败", e);
            return Result.error("创建支付失败: " + e.getMessage());
        }
    }

    @PostMapping("/initiate")
    @Operation(summary = "发起支付", description = "发起支付请求")
    public Result<PaymentInitiateResponse> initiatePayment(
            @Valid @RequestBody PaymentInitiateRequest request) {
        try {
            PaymentInitiateResponse response = paymentService.initiatePayment(request);
            return Result.success(response);
        } catch (Exception e) {
            log.error("发起支付失败", e);
            return Result.error("发起支付失败: " + e.getMessage());
        }
    }

    @GetMapping("/query/{paymentOrderNo}")
    @Operation(summary = "查询支付", description = "查询支付订单状态")
    public Result<PaymentQueryResponse> queryPayment(
            @Parameter(description = "支付订单号") @PathVariable @NotBlank String paymentOrderNo) {
        try {
            PaymentQueryResponse response = paymentService.queryPayment(paymentOrderNo);
            return Result.success(response);
        } catch (Exception e) {
            log.error("查询支付失败: {}", paymentOrderNo, e);
            return Result.error("查询支付失败: " + e.getMessage());
        }
    }

    @PostMapping("/cancel/{paymentOrderNo}")
    @Operation(summary = "取消支付", description = "取消支付订单")
    public Result<Boolean> cancelPayment(
            @Parameter(description = "支付订单号") @PathVariable @NotBlank String paymentOrderNo,
            @Parameter(description = "取消原因") @RequestParam(required = false) String cancelReason) {
        try {
            boolean result = paymentService.cancelPayment(paymentOrderNo, cancelReason);
            return Result.success(result);
        } catch (Exception e) {
            log.error("取消支付失败: {}", paymentOrderNo, e);
            return Result.error("取消支付失败: " + e.getMessage());
        }
    }

    @PostMapping("/close/{paymentOrderNo}")
    @Operation(summary = "关闭支付", description = "关闭支付订单")
    public Result<Boolean> closePayment(
            @Parameter(description = "支付订单号") @PathVariable @NotBlank String paymentOrderNo) {
        try {
            boolean result = paymentService.closePayment(paymentOrderNo);
            return Result.success(result);
        } catch (Exception e) {
            log.error("关闭支付失败: {}", paymentOrderNo, e);
            return Result.error("关闭支付失败: " + e.getMessage());
        }
    }

    @PostMapping("/notify/{channel}")
    @Operation(summary = "支付通知", description = "处理支付渠道的异步通知")
    public String handlePaymentNotify(
            @Parameter(description = "支付渠道") @PathVariable @NotBlank String channel,
            HttpServletRequest request) {
        try {
            return paymentService.handlePaymentNotify(channel, request);
        } catch (Exception e) {
            log.error("处理支付通知失败: {}", channel, e);
            return "FAIL";
        }
    }

    @PostMapping("/callback/{channel}")
    @Operation(summary = "支付回调", description = "处理支付渠道的同步回调")
    public Result<PaymentCallbackResponse> handlePaymentCallback(
            @Parameter(description = "支付渠道") @PathVariable @NotBlank String channel,
            @Valid @RequestBody PaymentCallbackRequest request) {
        try {
            PaymentCallbackResponse response = paymentService.handlePaymentCallback(channel, request);
            return Result.success(response);
        } catch (Exception e) {
            log.error("处理支付回调失败: {}", channel, e);
            return Result.error("处理支付回调失败: " + e.getMessage());
        }
    }

    @PostMapping("/refund/apply")
    @Operation(summary = "申请退款", description = "申请退款")
    public Result<RefundApplyResponse> applyRefund(
            @Valid @RequestBody RefundApplyRequest request) {
        try {
            RefundApplyResponse response = paymentService.applyRefund(request);
            return Result.success(response);
        } catch (Exception e) {
            log.error("申请退款失败", e);
            return Result.error("申请退款失败: " + e.getMessage());
        }
    }

    @PostMapping("/refund/process/{refundOrderNo}")
    @Operation(summary = "处理退款", description = "处理退款订单")
    public Result<RefundProcessResponse> processRefund(
            @Parameter(description = "退款订单号") @PathVariable @NotBlank String refundOrderNo) {
        try {
            RefundProcessResponse response = paymentService.processRefund(refundOrderNo);
            return Result.success(response);
        } catch (Exception e) {
            log.error("处理退款失败: {}", refundOrderNo, e);
            return Result.error("处理退款失败: " + e.getMessage());
        }
    }

    @GetMapping("/refund/query/{refundOrderNo}")
    @Operation(summary = "查询退款", description = "查询退款订单状态")
    public Result<RefundQueryResponse> queryRefund(
            @Parameter(description = "退款订单号") @PathVariable @NotBlank String refundOrderNo) {
        try {
            RefundQueryResponse response = paymentService.queryRefund(refundOrderNo);
            return Result.success(response);
        } catch (Exception e) {
            log.error("查询退款失败: {}", refundOrderNo, e);
            return Result.error("查询退款失败: " + e.getMessage());
        }
    }

    @PostMapping("/refund/notify/{channel}")
    @Operation(summary = "退款通知", description = "处理退款渠道的异步通知")
    public String handleRefundNotify(
            @Parameter(description = "支付渠道") @PathVariable @NotBlank String channel,
            HttpServletRequest request) {
        try {
            return paymentService.handleRefundNotify(channel, request);
        } catch (Exception e) {
            log.error("处理退款通知失败: {}", channel, e);
            return "FAIL";
        }
    }

    @PostMapping("/sync/payment/{paymentOrderNo}")
    @Operation(summary = "同步支付状态", description = "同步第三方支付状态")
    public Result<Boolean> syncPaymentStatus(
            @Parameter(description = "支付订单号") @PathVariable @NotBlank String paymentOrderNo) {
        try {
            boolean result = paymentService.syncPaymentStatus(paymentOrderNo);
            return Result.success(result);
        } catch (Exception e) {
            log.error("同步支付状态失败: {}", paymentOrderNo, e);
            return Result.error("同步支付状态失败: " + e.getMessage());
        }
    }

    @PostMapping("/sync/refund/{refundOrderNo}")
    @Operation(summary = "同步退款状态", description = "同步第三方退款状态")
    public Result<Boolean> syncRefundStatus(
            @Parameter(description = "退款订单号") @PathVariable @NotBlank String refundOrderNo) {
        try {
            boolean result = paymentService.syncRefundStatus(refundOrderNo);
            return Result.success(result);
        } catch (Exception e) {
            log.error("同步退款状态失败: {}", refundOrderNo, e);
            return Result.error("同步退款状态失败: " + e.getMessage());
        }
    }

    @PostMapping("/verify-sign")
    @Operation(summary = "验证支付签名", description = "验证支付请求签名")
    public Result<Boolean> verifyPaymentSign(
            @Valid @RequestBody PaymentSignVerifyRequest request) {
        try {
            boolean result = paymentService.verifyPaymentSign(request);
            return Result.success(result);
        } catch (Exception e) {
            log.error("验证支付签名失败", e);
            return Result.error("验证支付签名失败: " + e.getMessage());
        }
    }

    @PostMapping("/generate-sign")
    @Operation(summary = "生成支付签名", description = "生成支付请求签名")
    public Result<String> generatePaymentSign(
            @Valid @RequestBody PaymentSignGenerateRequest request) {
        try {
            String sign = paymentService.generatePaymentSign(request);
            return Result.success(sign);
        } catch (Exception e) {
            log.error("生成支付签名失败", e);
            return Result.error("生成支付签名失败: " + e.getMessage());
        }
    }

    @GetMapping("/channel/config/{channelCode}")
    @Operation(summary = "获取支付渠道配置", description = "获取指定支付渠道的配置信息")
    public Result<Map<String, Object>> getPaymentChannelConfig(
            @Parameter(description = "渠道编码") @PathVariable @NotBlank String channelCode) {
        try {
            Map<String, Object> config = paymentService.getPaymentChannelConfig(channelCode);
            return Result.success(config);
        } catch (Exception e) {
            log.error("获取支付渠道配置失败: {}", channelCode, e);
            return Result.error("获取支付渠道配置失败: " + e.getMessage());
        }
    }

    @GetMapping("/channel/available")
    @Operation(summary = "获取可用支付渠道", description = "获取当前可用的支付渠道列表")
    public Result<List<Map<String, Object>>> getAvailablePaymentChannels() {
        try {
            List<Map<String, Object>> channels = paymentService.getAvailablePaymentChannels();
            return Result.success(channels);
        } catch (Exception e) {
            log.error("获取可用支付渠道失败", e);
            return Result.error("获取可用支付渠道失败: " + e.getMessage());
        }
    }

    @GetMapping("/channel/support-methods/{channelCode}")
    @Operation(summary = "获取支付渠道支持的支付方式", description = "获取指定支付渠道支持的支付方式")
    public Result<List<String>> getPaymentChannelSupportMethods(
            @Parameter(description = "渠道编码") @PathVariable @NotBlank String channelCode) {
        try {
            List<String> methods = paymentService.getPaymentChannelSupportMethods(channelCode);
            return Result.success(methods);
        } catch (Exception e) {
            log.error("获取支付渠道支持的支付方式失败: {}", channelCode, e);
            return Result.error("获取支付渠道支持的支付方式失败: " + e.getMessage());
        }
    }

    @GetMapping("/channel/limits/{channelCode}")
    @Operation(summary = "获取支付渠道限额", description = "获取指定支付渠道的限额信息")
    public Result<Map<String, Object>> getPaymentChannelLimits(
            @Parameter(description = "渠道编码") @PathVariable @NotBlank String channelCode) {
        try {
            Map<String, Object> limits = paymentService.getPaymentChannelLimits(channelCode);
            return Result.success(limits);
        } catch (Exception e) {
            log.error("获取支付渠道限额失败: {}", channelCode, e);
            return Result.error("获取支付渠道限额失败: " + e.getMessage());
        }
    }

    @GetMapping("/calculate-fee")
    @Operation(summary = "计算支付手续费", description = "计算支付手续费")
    public Result<BigDecimal> calculatePaymentFee(
            @Parameter(description = "支付金额") @RequestParam @NotNull BigDecimal amount,
            @Parameter(description = "支付渠道") @RequestParam @NotBlank String channelCode) {
        try {
            BigDecimal fee = paymentService.calculatePaymentFee(amount, channelCode);
            return Result.success(fee);
        } catch (Exception e) {
            log.error("计算支付手续费失败", e);
            return Result.error("计算支付手续费失败: " + e.getMessage());
        }
    }

    @GetMapping("/calculate-refund-fee")
    @Operation(summary = "计算退款手续费", description = "计算退款手续费")
    public Result<BigDecimal> calculateRefundFee(
            @Parameter(description = "退款金额") @RequestParam @NotNull BigDecimal amount,
            @Parameter(description = "支付渠道") @RequestParam @NotBlank String channelCode) {
        try {
            BigDecimal fee = paymentService.calculateRefundFee(amount, channelCode);
            return Result.success(fee);
        } catch (Exception e) {
            log.error("计算退款手续费失败", e);
            return Result.error("计算退款手续费失败: " + e.getMessage());
        }
    }

    @GetMapping("/recommend-channel")
    @Operation(summary = "获取推荐支付渠道", description = "根据条件获取推荐的支付渠道")
    public Result<String> getRecommendedPaymentChannel(
            @Parameter(description = "支付金额") @RequestParam @NotNull BigDecimal amount,
            @Parameter(description = "支付方式") @RequestParam @NotBlank String paymentMethod,
            @Parameter(description = "用户ID") @RequestParam(required = false) Long userId) {
        try {
            String channelCode = paymentService.getRecommendedPaymentChannel(amount, paymentMethod, userId);
            return Result.success(channelCode);
        } catch (Exception e) {
            log.error("获取推荐支付渠道失败", e);
            return Result.error("获取推荐支付渠道失败: " + e.getMessage());
        }
    }

    @PostMapping("/preprocess")
    @Operation(summary = "预处理支付结果", description = "预处理支付结果")
    public Result<PaymentPreprocessResponse> preprocessPaymentResult(
            @Valid @RequestBody PaymentPreprocessRequest request) {
        try {
            PaymentPreprocessResponse response = paymentService.preprocessPaymentResult(request);
            return Result.success(response);
        } catch (Exception e) {
            log.error("预处理支付结果失败", e);
            return Result.error("预处理支付结果失败: " + e.getMessage());
        }
    }

    @PostMapping("/postprocess")
    @Operation(summary = "后处理支付结果", description = "后处理支付结果")
    public Result<PaymentPostprocessResponse> postprocessPaymentResult(
            @Valid @RequestBody PaymentPostprocessRequest request) {
        try {
            PaymentPostprocessResponse response = paymentService.postprocessPaymentResult(request);
            return Result.success(response);
        } catch (Exception e) {
            log.error("后处理支付结果失败", e);
            return Result.error("后处理支付结果失败: " + e.getMessage());
        }
    }

    @PostMapping("/handle-exception")
    @Operation(summary = "处理支付异常", description = "处理支付过程中的异常")
    public Result<PaymentExceptionResponse> handlePaymentException(
            @Valid @RequestBody PaymentExceptionRequest request) {
        try {
            PaymentExceptionResponse response = paymentService.handlePaymentException(request);
            return Result.success(response);
        } catch (Exception e) {
            log.error("处理支付异常失败", e);
            return Result.error("处理支付异常失败: " + e.getMessage());
        }
    }

    @PostMapping("/handle-refund-exception")
    @Operation(summary = "处理退款异常", description = "处理退款过程中的异常")
    public Result<RefundExceptionResponse> handleRefundException(
            @Valid @RequestBody RefundExceptionRequest request) {
        try {
            RefundExceptionResponse response = paymentService.handleRefundException(request);
            return Result.success(response);
        } catch (Exception e) {
            log.error("处理退款异常失败", e);
            return Result.error("处理退款异常失败: " + e.getMessage());
        }
    }

    @PostMapping("/retry/payment/{paymentOrderNo}")
    @Operation(summary = "重试支付", description = "重试失败的支付订单")
    public Result<Boolean> retryPayment(
            @Parameter(description = "支付订单号") @PathVariable @NotBlank String paymentOrderNo) {
        try {
            boolean result = paymentService.retryPayment(paymentOrderNo);
            return Result.success(result);
        } catch (Exception e) {
            log.error("重试支付失败: {}", paymentOrderNo, e);
            return Result.error("重试支付失败: " + e.getMessage());
        }
    }

    @PostMapping("/retry/refund/{refundOrderNo}")
    @Operation(summary = "重试退款", description = "重试失败的退款订单")
    public Result<Boolean> retryRefund(
            @Parameter(description = "退款订单号") @PathVariable @NotBlank String refundOrderNo) {
        try {
            boolean result = paymentService.retryRefund(refundOrderNo);
            return Result.success(result);
        } catch (Exception e) {
            log.error("重试退款失败: {}", refundOrderNo, e);
            return Result.error("重试退款失败: " + e.getMessage());
        }
    }

    @PostMapping("/batch/timeout-orders")
    @Operation(summary = "批量处理超时订单", description = "批量处理超时的支付订单")
    public Result<Integer> batchProcessTimeoutOrders() {
        try {
            int count = paymentService.batchProcessTimeoutOrders();
            return Result.success(count);
        } catch (Exception e) {
            log.error("批量处理超时订单失败", e);
            return Result.error("批量处理超时订单失败: " + e.getMessage());
        }
    }

    @PostMapping("/batch/sync-payment-status")
    @Operation(summary = "批量同步支付状态", description = "批量同步支付订单状态")
    public Result<Integer> batchSyncPaymentStatus() {
        try {
            int count = paymentService.batchSyncPaymentStatus();
            return Result.success(count);
        } catch (Exception e) {
            log.error("批量同步支付状态失败", e);
            return Result.error("批量同步支付状态失败: " + e.getMessage());
        }
    }

    @PostMapping("/batch/sync-refund-status")
    @Operation(summary = "批量同步退款状态", description = "批量同步退款订单状态")
    public Result<Integer> batchSyncRefundStatus() {
        try {
            int count = paymentService.batchSyncRefundStatus();
            return Result.success(count);
        } catch (Exception e) {
            log.error("批量同步退款状态失败", e);
            return Result.error("批量同步退款状态失败: " + e.getMessage());
        }
    }
}