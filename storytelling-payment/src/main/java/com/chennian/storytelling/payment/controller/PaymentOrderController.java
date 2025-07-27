package com.chennian.storytelling.payment.controller;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.chennian.storytelling.common.result.Result;
import com.chennian.storytelling.payment.entity.PaymentOrder;
import com.chennian.storytelling.payment.service.PaymentOrderService;
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
import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

/**
 * 支付订单控制器
 * 
 * @author chennian
 * @since 2024-01-01
 */
@Slf4j
@RestController
@RequestMapping("/api/payment/order")
@RequiredArgsConstructor
@Validated
@Tag(name = "支付订单管理", description = "支付订单相关接口")
public class PaymentOrderController {

    private final PaymentOrderService paymentOrderService;

    @PostMapping
    @Operation(summary = "创建支付订单", description = "创建新的支付订单")
    public Result<PaymentOrder> createPaymentOrder(
            @Valid @RequestBody PaymentOrder paymentOrder) {
        try {
            PaymentOrder createdOrder = paymentOrderService.createPaymentOrder(paymentOrder);
            return Result.success(createdOrder);
        } catch (Exception e) {
            log.error("创建支付订单失败", e);
            return Result.error("创建支付订单失败: " + e.getMessage());
        }
    }

    @GetMapping("/no/{paymentOrderNo}")
    @Operation(summary = "根据订单号查询", description = "根据支付订单号查询订单信息")
    public Result<PaymentOrder> getByPaymentOrderNo(
            @Parameter(description = "支付订单号") @PathVariable @NotBlank String paymentOrderNo) {
        try {
            PaymentOrder paymentOrder = paymentOrderService.getByPaymentOrderNo(paymentOrderNo);
            if (paymentOrder != null) {
                return Result.success(paymentOrder);
            } else {
                return Result.error("支付订单不存在");
            }
        } catch (Exception e) {
            log.error("查询支付订单失败: {}", paymentOrderNo, e);
            return Result.error("查询支付订单失败: " + e.getMessage());
        }
    }

    @GetMapping("/business/{businessOrderNo}")
    @Operation(summary = "根据业务订单号查询", description = "根据业务订单号查询支付订单")
    public Result<PaymentOrder> getByBusinessOrderNo(
            @Parameter(description = "业务订单号") @PathVariable @NotBlank String businessOrderNo) {
        try {
            PaymentOrder paymentOrder = paymentOrderService.getByBusinessOrderNo(businessOrderNo);
            if (paymentOrder != null) {
                return Result.success(paymentOrder);
            } else {
                return Result.error("支付订单不存在");
            }
        } catch (Exception e) {
            log.error("查询支付订单失败: {}", businessOrderNo, e);
            return Result.error("查询支付订单失败: " + e.getMessage());
        }
    }

    @GetMapping("/third/{thirdTransactionNo}")
    @Operation(summary = "根据第三方交易号查询", description = "根据第三方交易号查询支付订单")
    public Result<PaymentOrder> getByThirdTransactionNo(
            @Parameter(description = "第三方交易号") @PathVariable @NotBlank String thirdTransactionNo) {
        try {
            PaymentOrder paymentOrder = paymentOrderService.getByThirdTransactionNo(thirdTransactionNo);
            if (paymentOrder != null) {
                return Result.success(paymentOrder);
            } else {
                return Result.error("支付订单不存在");
            }
        } catch (Exception e) {
            log.error("查询支付订单失败: {}", thirdTransactionNo, e);
            return Result.error("查询支付订单失败: " + e.getMessage());
        }
    }

    @GetMapping("/user/{userId}")
    @Operation(summary = "查询用户支付订单", description = "查询指定用户的所有支付订单")
    public Result<List<PaymentOrder>> listByUserId(
            @Parameter(description = "用户ID") @PathVariable @NotNull Long userId) {
        try {
            List<PaymentOrder> orders = paymentOrderService.listByUserId(userId);
            return Result.success(orders);
        } catch (Exception e) {
            log.error("查询用户支付订单失败: {}", userId, e);
            return Result.error("查询用户支付订单失败: " + e.getMessage());
        }
    }

    @GetMapping("/status/{paymentStatus}")
    @Operation(summary = "根据支付状态查询", description = "根据支付状态查询订单列表")
    public Result<List<PaymentOrder>> listByPaymentStatus(
            @Parameter(description = "支付状态") @PathVariable @NotBlank String paymentStatus) {
        try {
            List<PaymentOrder> orders = paymentOrderService.listByPaymentStatus(paymentStatus);
            return Result.success(orders);
        } catch (Exception e) {
            log.error("查询支付订单失败: {}", paymentStatus, e);
            return Result.error("查询支付订单失败: " + e.getMessage());
        }
    }

    @GetMapping("/channel/{paymentChannel}")
    @Operation(summary = "根据支付渠道查询", description = "根据支付渠道查询订单列表")
    public Result<List<PaymentOrder>> listByPaymentChannel(
            @Parameter(description = "支付渠道") @PathVariable @NotBlank String paymentChannel) {
        try {
            List<PaymentOrder> orders = paymentOrderService.listByPaymentChannel(paymentChannel);
            return Result.success(orders);
        } catch (Exception e) {
            log.error("查询支付订单失败: {}", paymentChannel, e);
            return Result.error("查询支付订单失败: " + e.getMessage());
        }
    }

    @GetMapping("/timeout")
    @Operation(summary = "查询超时订单", description = "查询超时未支付的订单")
    public Result<List<PaymentOrder>> listTimeoutOrders() {
        try {
            List<PaymentOrder> orders = paymentOrderService.listTimeoutOrders();
            return Result.success(orders);
        } catch (Exception e) {
            log.error("查询超时订单失败", e);
            return Result.error("查询超时订单失败: " + e.getMessage());
        }
    }

    @GetMapping("/page")
    @Operation(summary = "分页查询支付订单", description = "分页查询支付订单列表")
    public Result<IPage<PaymentOrder>> pagePaymentOrders(
            @Parameter(description = "当前页") @RequestParam(defaultValue = "1") @Positive int current,
            @Parameter(description = "页大小") @RequestParam(defaultValue = "10") @Positive int size,
            @Parameter(description = "支付状态") @RequestParam(required = false) String paymentStatus,
            @Parameter(description = "支付渠道") @RequestParam(required = false) String paymentChannel,
            @Parameter(description = "用户ID") @RequestParam(required = false) Long userId) {
        try {
            IPage<PaymentOrder> page = paymentOrderService.pagePaymentOrders(current, size, paymentStatus, paymentChannel, userId);
            return Result.success(page);
        } catch (Exception e) {
            log.error("分页查询支付订单失败", e);
            return Result.error("分页查询支付订单失败: " + e.getMessage());
        }
    }

    @PutMapping("/status")
    @Operation(summary = "更新支付状态", description = "更新支付订单状态")
    public Result<Boolean> updatePaymentStatus(
            @Parameter(description = "支付订单号") @RequestParam @NotBlank String paymentOrderNo,
            @Parameter(description = "支付状态") @RequestParam @NotBlank String paymentStatus) {
        try {
            boolean result = paymentOrderService.updatePaymentStatus(paymentOrderNo, paymentStatus);
            return Result.success(result);
        } catch (Exception e) {
            log.error("更新支付状态失败: {}", paymentOrderNo, e);
            return Result.error("更新支付状态失败: " + e.getMessage());
        }
    }

    @PutMapping("/third-info")
    @Operation(summary = "更新第三方交易信息", description = "更新第三方交易号和支付号")
    public Result<Boolean> updateThirdTransactionInfo(
            @Parameter(description = "支付订单号") @RequestParam @NotBlank String paymentOrderNo,
            @Parameter(description = "第三方交易号") @RequestParam(required = false) String thirdTransactionNo,
            @Parameter(description = "第三方支付号") @RequestParam(required = false) String thirdPaymentNo) {
        try {
            boolean result = paymentOrderService.updateThirdTransactionInfo(paymentOrderNo, thirdTransactionNo, thirdPaymentNo);
            return Result.success(result);
        } catch (Exception e) {
            log.error("更新第三方交易信息失败: {}", paymentOrderNo, e);
            return Result.error("更新第三方交易信息失败: " + e.getMessage());
        }
    }

    @PutMapping("/refund-info")
    @Operation(summary = "更新退款信息", description = "更新订单退款相关信息")
    public Result<Boolean> updateRefundInfo(
            @Parameter(description = "支付订单号") @RequestParam @NotBlank String paymentOrderNo,
            @Parameter(description = "退款金额") @RequestParam @NotNull BigDecimal refundAmount,
            @Parameter(description = "退款状态") @RequestParam @NotBlank String refundStatus) {
        try {
            boolean result = paymentOrderService.updateRefundInfo(paymentOrderNo, refundAmount, refundStatus);
            return Result.success(result);
        } catch (Exception e) {
            log.error("更新退款信息失败: {}", paymentOrderNo, e);
            return Result.error("更新退款信息失败: " + e.getMessage());
        }
    }

    @PostMapping("/success")
    @Operation(summary = "处理支付成功", description = "处理支付成功的订单")
    public Result<Boolean> handlePaymentSuccess(
            @Parameter(description = "支付订单号") @RequestParam @NotBlank String paymentOrderNo) {
        try {
            boolean result = paymentOrderService.handlePaymentSuccess(paymentOrderNo, java.time.LocalDateTime.now());
            return Result.success(result);
        } catch (Exception e) {
            log.error("处理支付成功失败: {}", paymentOrderNo, e);
            return Result.error("处理支付成功失败: " + e.getMessage());
        }
    }

    @PostMapping("/failed")
    @Operation(summary = "处理支付失败", description = "处理支付失败的订单")
    public Result<Boolean> handlePaymentFailed(
            @Parameter(description = "支付订单号") @RequestParam @NotBlank String paymentOrderNo,
            @Parameter(description = "失败原因") @RequestParam(required = false) String failReason) {
        try {
            boolean result = paymentOrderService.handlePaymentFailed(paymentOrderNo, failReason);
            return Result.success(result);
        } catch (Exception e) {
            log.error("处理支付失败失败: {}", paymentOrderNo, e);
            return Result.error("处理支付失败失败: " + e.getMessage());
        }
    }

    @PostMapping("/timeout")
    @Operation(summary = "处理支付超时", description = "处理支付超时的订单")
    public Result<Boolean> handlePaymentTimeout(
            @Parameter(description = "支付订单号") @RequestParam @NotBlank String paymentOrderNo) {
        try {
            boolean result = paymentOrderService.handlePaymentTimeout(paymentOrderNo);
            return Result.success(result);
        } catch (Exception e) {
            log.error("处理支付超时失败: {}", paymentOrderNo, e);
            return Result.error("处理支付超时失败: " + e.getMessage());
        }
    }

    @PostMapping("/batch-timeout")
    @Operation(summary = "批量处理超时订单", description = "批量处理超时未支付的订单")
    public Result<Integer> batchProcessTimeoutOrders() {
        try {
            int count = paymentOrderService.batchProcessTimeoutOrders();
            return Result.success(count);
        } catch (Exception e) {
            log.error("批量处理超时订单失败", e);
            return Result.error("批量处理超时订单失败: " + e.getMessage());
        }
    }

    @GetMapping("/stats/total-amount")
    @Operation(summary = "统计支付总金额", description = "统计所有支付订单的总金额")
    public Result<BigDecimal> getTotalPaymentAmount() {
        try {
            BigDecimal totalAmount = paymentOrderService.getTotalPaymentAmount();
            return Result.success(totalAmount);
        } catch (Exception e) {
            log.error("统计支付总金额失败", e);
            return Result.error("统计支付总金额失败: " + e.getMessage());
        }
    }

    @GetMapping("/stats/amount-by-channel")
    @Operation(summary = "按渠道统计支付金额", description = "按支付渠道统计支付金额")
    public Result<Map<String, BigDecimal>> getPaymentAmountByChannel() {
        try {
            Map<String, BigDecimal> stats = paymentOrderService.getPaymentAmountByChannel();
            return Result.success(stats);
        } catch (Exception e) {
            log.error("按渠道统计支付金额失败", e);
            return Result.error("按渠道统计支付金额失败: " + e.getMessage());
        }
    }

    @GetMapping("/stats/count-by-status")
    @Operation(summary = "按状态统计订单数量", description = "按支付状态统计订单数量")
    public Result<Map<String, Long>> getPaymentCountByStatus() {
        try {
            Map<String, Long> stats = paymentOrderService.getPaymentCountByStatus();
            return Result.success(stats);
        } catch (Exception e) {
            log.error("按状态统计订单数量失败", e);
            return Result.error("按状态统计订单数量失败: " + e.getMessage());
        }
    }

    @GetMapping("/user/{userId}/total-amount")
    @Operation(summary = "查询用户支付总金额", description = "查询指定用户的支付总金额")
    public Result<BigDecimal> getUserTotalPaymentAmount(
            @Parameter(description = "用户ID") @PathVariable @NotNull Long userId) {
        try {
            BigDecimal totalAmount = paymentOrderService.getUserTotalPaymentAmount(userId);
            return Result.success(totalAmount);
        } catch (Exception e) {
            log.error("查询用户支付总金额失败: {}", userId, e);
            return Result.error("查询用户支付总金额失败: " + e.getMessage());
        }
    }

    @GetMapping("/user/{userId}/payment-count")
    @Operation(summary = "查询用户支付次数", description = "查询指定用户的支付次数")
    public Result<Long> getUserPaymentCount(
            @Parameter(description = "用户ID") @PathVariable @NotNull Long userId) {
        try {
            Long count = paymentOrderService.getUserPaymentCount(userId);
            return Result.success(count);
        } catch (Exception e) {
            log.error("查询用户支付次数失败: {}", userId, e);
            return Result.error("查询用户支付次数失败: " + e.getMessage());
        }
    }

    @GetMapping("/can-pay/{paymentOrderNo}")
    @Operation(summary = "检查是否可支付", description = "检查订单是否可以进行支付")
    public Result<Boolean> canPay(
            @Parameter(description = "支付订单号") @PathVariable @NotBlank String paymentOrderNo) {
        try {
            boolean canPay = paymentOrderService.canPay(paymentOrderNo);
            return Result.success(canPay);
        } catch (Exception e) {
            log.error("检查是否可支付失败: {}", paymentOrderNo, e);
            return Result.error("检查是否可支付失败: " + e.getMessage());
        }
    }

    @GetMapping("/can-refund/{paymentOrderNo}")
    @Operation(summary = "检查是否可退款", description = "检查订单是否可以进行退款")
    public Result<Boolean> canRefund(
            @Parameter(description = "支付订单号") @PathVariable @NotBlank String paymentOrderNo) {
        try {
            boolean canRefund = paymentOrderService.canRefund(paymentOrderNo);
            return Result.success(canRefund);
        } catch (Exception e) {
            log.error("检查是否可退款失败: {}", paymentOrderNo, e);
            return Result.error("检查是否可退款失败: " + e.getMessage());
        }
    }

    @GetMapping("/refundable-amount/{paymentOrderNo}")
    @Operation(summary = "获取可退款金额", description = "获取订单的可退款金额")
    public Result<BigDecimal> getRefundableAmount(
            @Parameter(description = "支付订单号") @PathVariable @NotBlank String paymentOrderNo) {
        try {
            BigDecimal refundableAmount = paymentOrderService.getRefundableAmount(paymentOrderNo);
            return Result.success(refundableAmount);
        } catch (Exception e) {
            log.error("获取可退款金额失败: {}", paymentOrderNo, e);
            return Result.error("获取可退款金额失败: " + e.getMessage());
        }
    }

    @PostMapping("/generate-order-no")
    @Operation(summary = "生成支付订单号", description = "生成新的支付订单号")
    public Result<String> generatePaymentOrderNo() {
        try {
            String orderNo = paymentOrderService.generatePaymentOrderNo();
            return Result.success(orderNo);
        } catch (Exception e) {
            log.error("生成支付订单号失败", e);
            return Result.error("生成支付订单号失败: " + e.getMessage());
        }
    }

    @PostMapping("/validate")
    @Operation(summary = "验证支付订单", description = "验证支付订单信息是否有效")
    public Result<Boolean> validatePaymentOrder(
            @Valid @RequestBody PaymentOrder paymentOrder) {
        try {
            boolean isValid = paymentOrderService.validatePaymentOrder(paymentOrder);
            return Result.success(isValid);
        } catch (Exception e) {
            log.error("验证支付订单失败", e);
            return Result.error("验证支付订单失败: " + e.getMessage());
        }
    }

    @GetMapping("/calculate-fee")
    @Operation(summary = "计算手续费", description = "计算支付手续费")
    public Result<BigDecimal> calculateFee(
            @Parameter(description = "支付金额") @RequestParam @NotNull BigDecimal amount,
            @Parameter(description = "支付渠道") @RequestParam @NotBlank String paymentChannel) {
        try {
            BigDecimal fee = paymentOrderService.calculateFee(amount, paymentChannel);
            return Result.success(fee);
        } catch (Exception e) {
            log.error("计算手续费失败", e);
            return Result.error("计算手续费失败: " + e.getMessage());
        }
    }

    @PutMapping("/expire-time")
    @Operation(summary = "设置订单过期时间", description = "设置支付订单的过期时间")
    public Result<Boolean> setOrderExpireTime(
            @Parameter(description = "支付订单号") @RequestParam @NotBlank String paymentOrderNo,
            @Parameter(description = "过期时间（分钟）") @RequestParam @Positive int expireMinutes) {
        try {
            boolean result = paymentOrderService.setOrderExpireTime(paymentOrderNo, expireMinutes);
            return Result.success(result);
        } catch (Exception e) {
            log.error("设置订单过期时间失败: {}", paymentOrderNo, e);
            return Result.error("设置订单过期时间失败: " + e.getMessage());
        }
    }
}