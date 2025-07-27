package com.chennian.storytelling.payment.controller;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.chennian.storytelling.common.result.Result;
import com.chennian.storytelling.payment.entity.RefundOrder;
import com.chennian.storytelling.payment.service.RefundOrderService;
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
 * 退款订单控制器
 * 
 * @author chennian
 * @since 2024-01-01
 */
@Slf4j
@RestController
@RequestMapping("/api/payment/refund")
@RequiredArgsConstructor
@Validated
@Tag(name = "退款订单管理", description = "退款订单相关接口")
public class RefundOrderController {

    private final RefundOrderService refundOrderService;

    @PostMapping
    @Operation(summary = "创建退款订单", description = "创建新的退款订单")
    public Result<RefundOrder> createRefundOrder(
            @Valid @RequestBody RefundOrder refundOrder) {
        try {
            RefundOrder createdOrder = refundOrderService.createRefundOrder(refundOrder);
            return Result.success(createdOrder);
        } catch (Exception e) {
            log.error("创建退款订单失败", e);
            return Result.error("创建退款订单失败: " + e.getMessage());
        }
    }

    @PutMapping("/{id}")
    @Operation(summary = "更新退款订单", description = "更新退款订单信息")
    public Result<Boolean> updateRefundOrder(
            @Parameter(description = "退款订单ID") @PathVariable @NotNull Long id,
            @Valid @RequestBody RefundOrder refundOrder) {
        try {
            refundOrder.setId(id);
            boolean result = refundOrderService.updateRefundOrder(refundOrder);
            return Result.success(result);
        } catch (Exception e) {
            log.error("更新退款订单失败: {}", id, e);
            return Result.error("更新退款订单失败: " + e.getMessage());
        }
    }

    @GetMapping("/{id}")
    @Operation(summary = "根据ID查询", description = "根据退款订单ID查询退款订单")
    public Result<RefundOrder> getById(
            @Parameter(description = "退款订单ID") @PathVariable @NotNull Long id) {
        try {
            RefundOrder refundOrder = refundOrderService.getById(id);
            if (refundOrder != null) {
                return Result.success(refundOrder);
            } else {
                return Result.error("退款订单不存在");
            }
        } catch (Exception e) {
            log.error("查询退款订单失败: {}", id, e);
            return Result.error("查询退款订单失败: " + e.getMessage());
        }
    }

    @GetMapping("/no/{refundOrderNo}")
    @Operation(summary = "根据退款订单号查询", description = "根据退款订单号查询退款订单")
    public Result<RefundOrder> getByRefundOrderNo(
            @Parameter(description = "退款订单号") @PathVariable @NotBlank String refundOrderNo) {
        try {
            RefundOrder refundOrder = refundOrderService.getByRefundOrderNo(refundOrderNo);
            if (refundOrder != null) {
                return Result.success(refundOrder);
            } else {
                return Result.error("退款订单不存在");
            }
        } catch (Exception e) {
            log.error("查询退款订单失败: {}", refundOrderNo, e);
            return Result.error("查询退款订单失败: " + e.getMessage());
        }
    }

    @GetMapping("/payment-order/{paymentOrderId}")
    @Operation(summary = "根据支付订单ID查询", description = "根据支付订单ID查询退款订单列表")
    public Result<List<RefundOrder>> listByPaymentOrderId(
            @Parameter(description = "支付订单ID") @PathVariable @NotNull Long paymentOrderId) {
        try {
            List<RefundOrder> orders = refundOrderService.listByPaymentOrderId(paymentOrderId);
            return Result.success(orders);
        } catch (Exception e) {
            log.error("查询退款订单失败: {}", paymentOrderId, e);
            return Result.error("查询退款订单失败: " + e.getMessage());
        }
    }

    @GetMapping("/payment-order-no/{paymentOrderNo}")
    @Operation(summary = "根据支付订单号查询", description = "根据支付订单号查询退款订单列表")
    public Result<List<RefundOrder>> listByPaymentOrderNo(
            @Parameter(description = "支付订单号") @PathVariable @NotBlank String paymentOrderNo) {
        try {
            List<RefundOrder> orders = refundOrderService.listByPaymentOrderNo(paymentOrderNo);
            return Result.success(orders);
        } catch (Exception e) {
            log.error("查询退款订单失败: {}", paymentOrderNo, e);
            return Result.error("查询退款订单失败: " + e.getMessage());
        }
    }

    @GetMapping("/business/{businessOrderNo}")
    @Operation(summary = "根据业务订单号查询", description = "根据业务订单号查询退款订单列表")
    public Result<List<RefundOrder>> listByBusinessOrderNo(
            @Parameter(description = "业务订单号") @PathVariable @NotBlank String businessOrderNo) {
        try {
            List<RefundOrder> orders = refundOrderService.listByBusinessOrderNo(businessOrderNo);
            return Result.success(orders);
        } catch (Exception e) {
            log.error("查询退款订单失败: {}", businessOrderNo, e);
            return Result.error("查询退款订单失败: " + e.getMessage());
        }
    }

    @GetMapping("/third/{thirdRefundNo}")
    @Operation(summary = "根据第三方退款单号查询", description = "根据第三方退款单号查询退款订单")
    public Result<RefundOrder> getByThirdRefundNo(
            @Parameter(description = "第三方退款单号") @PathVariable @NotBlank String thirdRefundNo) {
        try {
            RefundOrder refundOrder = refundOrderService.getByThirdRefundNo(thirdRefundNo);
            if (refundOrder != null) {
                return Result.success(refundOrder);
            } else {
                return Result.error("退款订单不存在");
            }
        } catch (Exception e) {
            log.error("查询退款订单失败: {}", thirdRefundNo, e);
            return Result.error("查询退款订单失败: " + e.getMessage());
        }
    }

    @GetMapping("/user/{userId}")
    @Operation(summary = "查询用户退款订单", description = "查询指定用户的所有退款订单")
    public Result<List<RefundOrder>> listByUserId(
            @Parameter(description = "用户ID") @PathVariable @NotNull Long userId) {
        try {
            List<RefundOrder> orders = refundOrderService.listByUserId(userId);
            return Result.success(orders);
        } catch (Exception e) {
            log.error("查询用户退款订单失败: {}", userId, e);
            return Result.error("查询用户退款订单失败: " + e.getMessage());
        }
    }

    @GetMapping("/refund-status/{refundStatus}")
    @Operation(summary = "根据退款状态查询", description = "根据退款状态查询退款订单列表")
    public Result<List<RefundOrder>> listByRefundStatus(
            @Parameter(description = "退款状态") @PathVariable @NotBlank String refundStatus) {
        try {
            List<RefundOrder> orders = refundOrderService.listByRefundStatus(refundStatus);
            return Result.success(orders);
        } catch (Exception e) {
            log.error("查询退款订单失败: {}", refundStatus, e);
            return Result.error("查询退款订单失败: " + e.getMessage());
        }
    }

    @GetMapping("/audit-status/{auditStatus}")
    @Operation(summary = "根据审核状态查询", description = "根据审核状态查询退款订单列表")
    public Result<List<RefundOrder>> listByAuditStatus(
            @Parameter(description = "审核状态") @PathVariable @NotBlank String auditStatus) {
        try {
            List<RefundOrder> orders = refundOrderService.listByAuditStatus(auditStatus);
            return Result.success(orders);
        } catch (Exception e) {
            log.error("查询退款订单失败: {}", auditStatus, e);
            return Result.error("查询退款订单失败: " + e.getMessage());
        }
    }

    @GetMapping("/channel/{paymentChannel}")
    @Operation(summary = "根据支付渠道查询", description = "根据支付渠道查询退款订单列表")
    public Result<List<RefundOrder>> listByPaymentChannel(
            @Parameter(description = "支付渠道") @PathVariable @NotBlank String paymentChannel) {
        try {
            List<RefundOrder> orders = refundOrderService.listByPaymentChannel(paymentChannel);
            return Result.success(orders);
        } catch (Exception e) {
            log.error("查询退款订单失败: {}", paymentChannel, e);
            return Result.error("查询退款订单失败: " + e.getMessage());
        }
    }

    @GetMapping("/page")
    @Operation(summary = "分页查询退款订单", description = "分页查询退款订单列表")
    public Result<IPage<RefundOrder>> pageRefundOrders(
            @Parameter(description = "当前页") @RequestParam(defaultValue = "1") @Positive int current,
            @Parameter(description = "页大小") @RequestParam(defaultValue = "10") @Positive int size,
            @Parameter(description = "退款状态") @RequestParam(required = false) String refundStatus,
            @Parameter(description = "审核状态") @RequestParam(required = false) String auditStatus,
            @Parameter(description = "支付渠道") @RequestParam(required = false) String paymentChannel,
            @Parameter(description = "用户ID") @RequestParam(required = false) Long userId) {
        try {
            IPage<RefundOrder> page = refundOrderService.pageRefundOrders(current, size, refundStatus, auditStatus, paymentChannel, userId);
            return Result.success(page);
        } catch (Exception e) {
            log.error("分页查询退款订单失败", e);
            return Result.error("分页查询退款订单失败: " + e.getMessage());
        }
    }

    @PutMapping("/status")
    @Operation(summary = "更新退款状态", description = "更新退款订单状态")
    public Result<Boolean> updateRefundStatus(
            @Parameter(description = "退款订单号") @RequestParam @NotBlank String refundOrderNo,
            @Parameter(description = "退款状态") @RequestParam @NotBlank String refundStatus) {
        try {
            boolean result = refundOrderService.updateRefundStatus(refundOrderNo, refundStatus);
            return Result.success(result);
        } catch (Exception e) {
            log.error("更新退款状态失败: {}", refundOrderNo, e);
            return Result.error("更新退款状态失败: " + e.getMessage());
        }
    }

    @PutMapping("/audit-info")
    @Operation(summary = "更新审核信息", description = "更新退款订单审核信息")
    public Result<Boolean> updateAuditInfo(
            @Parameter(description = "退款订单号") @RequestParam @NotBlank String refundOrderNo,
            @Parameter(description = "审核状态") @RequestParam @NotBlank String auditStatus,
            @Parameter(description = "审核人ID") @RequestParam(required = false) Long auditUserId,
            @Parameter(description = "审核备注") @RequestParam(required = false) String auditRemark) {
        try {
            boolean result = refundOrderService.updateAuditInfo(refundOrderNo, auditStatus, auditUserId, auditRemark);
            return Result.success(result);
        } catch (Exception e) {
            log.error("更新审核信息失败: {}", refundOrderNo, e);
            return Result.error("更新审核信息失败: " + e.getMessage());
        }
    }

    @PutMapping("/third-info")
    @Operation(summary = "更新第三方退款信息", description = "更新第三方退款单号等信息")
    public Result<Boolean> updateThirdRefundInfo(
            @Parameter(description = "退款订单号") @RequestParam @NotBlank String refundOrderNo,
            @Parameter(description = "第三方退款单号") @RequestParam(required = false) String thirdRefundNo,
            @Parameter(description = "第三方响应") @RequestParam(required = false) String thirdResponse) {
        try {
            boolean result = refundOrderService.updateThirdRefundInfo(refundOrderNo, thirdRefundNo, thirdResponse);
            return Result.success(result);
        } catch (Exception e) {
            log.error("更新第三方退款信息失败: {}", refundOrderNo, e);
            return Result.error("更新第三方退款信息失败: " + e.getMessage());
        }
    }

    @PostMapping("/apply")
    @Operation(summary = "申请退款", description = "申请退款订单")
    public Result<Boolean> applyRefund(
            @Parameter(description = "退款订单号") @RequestParam @NotBlank String refundOrderNo,
            @Parameter(description = "申请原因") @RequestParam(required = false) String applyReason) {
        try {
            boolean result = refundOrderService.applyRefund(refundOrderNo, applyReason);
            return Result.success(result);
        } catch (Exception e) {
            log.error("申请退款失败: {}", refundOrderNo, e);
            return Result.error("申请退款失败: " + e.getMessage());
        }
    }

    @PostMapping("/audit")
    @Operation(summary = "审核退款", description = "审核退款订单")
    public Result<Boolean> auditRefund(
            @Parameter(description = "退款订单号") @RequestParam @NotBlank String refundOrderNo,
            @Parameter(description = "审核结果") @RequestParam @NotBlank String auditResult,
            @Parameter(description = "审核人ID") @RequestParam @NotNull Long auditUserId,
            @Parameter(description = "审核备注") @RequestParam(required = false) String auditRemark) {
        try {
            boolean result = refundOrderService.auditRefund(refundOrderNo, auditResult, auditUserId, auditRemark);
            return Result.success(result);
        } catch (Exception e) {
            log.error("审核退款失败: {}", refundOrderNo, e);
            return Result.error("审核退款失败: " + e.getMessage());
        }
    }

    @PostMapping("/process")
    @Operation(summary = "处理退款", description = "处理退款订单")
    public Result<Boolean> processRefund(
            @Parameter(description = "退款订单号") @RequestParam @NotBlank String refundOrderNo) {
        try {
            boolean result = refundOrderService.processRefund(refundOrderNo);
            return Result.success(result);
        } catch (Exception e) {
            log.error("处理退款失败: {}", refundOrderNo, e);
            return Result.error("处理退款失败: " + e.getMessage());
        }
    }

    @PostMapping("/cancel")
    @Operation(summary = "取消退款", description = "取消退款订单")
    public Result<Boolean> cancelRefund(
            @Parameter(description = "退款订单号") @RequestParam @NotBlank String refundOrderNo,
            @Parameter(description = "取消原因") @RequestParam(required = false) String cancelReason) {
        try {
            boolean result = refundOrderService.cancelRefund(refundOrderNo, cancelReason);
            return Result.success(result);
        } catch (Exception e) {
            log.error("取消退款失败: {}", refundOrderNo, e);
            return Result.error("取消退款失败: " + e.getMessage());
        }
    }

    @PostMapping("/success")
    @Operation(summary = "处理退款成功", description = "处理退款成功的订单")
    public Result<Boolean> handleRefundSuccess(
            @Parameter(description = "退款订单号") @RequestParam @NotBlank String refundOrderNo) {
        try {
            boolean result = refundOrderService.handleRefundSuccess(refundOrderNo, java.time.LocalDateTime.now());
            return Result.success(result);
        } catch (Exception e) {
            log.error("处理退款成功失败: {}", refundOrderNo, e);
            return Result.error("处理退款成功失败: " + e.getMessage());
        }
    }

    @PostMapping("/failed")
    @Operation(summary = "处理退款失败", description = "处理退款失败的订单")
    public Result<Boolean> handleRefundFailed(
            @Parameter(description = "退款订单号") @RequestParam @NotBlank String refundOrderNo,
            @Parameter(description = "失败原因") @RequestParam(required = false) String failReason) {
        try {
            boolean result = refundOrderService.handleRefundFailed(refundOrderNo, failReason);
            return Result.success(result);
        } catch (Exception e) {
            log.error("处理退款失败失败: {}", refundOrderNo, e);
            return Result.error("处理退款失败失败: " + e.getMessage());
        }
    }

    @GetMapping("/stats/total-amount")
    @Operation(summary = "统计退款总金额", description = "统计所有退款订单的总金额")
    public Result<BigDecimal> getTotalRefundAmount() {
        try {
            BigDecimal totalAmount = refundOrderService.getTotalRefundAmount();
            return Result.success(totalAmount);
        } catch (Exception e) {
            log.error("统计退款总金额失败", e);
            return Result.error("统计退款总金额失败: " + e.getMessage());
        }
    }

    @GetMapping("/stats/amount-by-channel")
    @Operation(summary = "按渠道统计退款金额", description = "按支付渠道统计退款金额")
    public Result<Map<String, BigDecimal>> getRefundAmountByChannel() {
        try {
            Map<String, BigDecimal> stats = refundOrderService.getRefundAmountByChannel();
            return Result.success(stats);
        } catch (Exception e) {
            log.error("按渠道统计退款金额失败", e);
            return Result.error("按渠道统计退款金额失败: " + e.getMessage());
        }
    }

    @GetMapping("/stats/count-by-status")
    @Operation(summary = "按状态统计退款订单数量", description = "按退款状态统计订单数量")
    public Result<Map<String, Long>> getRefundCountByStatus() {
        try {
            Map<String, Long> stats = refundOrderService.getRefundCountByStatus();
            return Result.success(stats);
        } catch (Exception e) {
            log.error("按状态统计退款订单数量失败", e);
            return Result.error("按状态统计退款订单数量失败: " + e.getMessage());
        }
    }

    @GetMapping("/user/{userId}/total-amount")
    @Operation(summary = "查询用户退款总金额", description = "查询指定用户的退款总金额")
    public Result<BigDecimal> getUserTotalRefundAmount(
            @Parameter(description = "用户ID") @PathVariable @NotNull Long userId) {
        try {
            BigDecimal totalAmount = refundOrderService.getUserTotalRefundAmount(userId);
            return Result.success(totalAmount);
        } catch (Exception e) {
            log.error("查询用户退款总金额失败: {}", userId, e);
            return Result.error("查询用户退款总金额失败: " + e.getMessage());
        }
    }

    @GetMapping("/user/{userId}/refund-count")
    @Operation(summary = "查询用户退款次数", description = "查询指定用户的退款次数")
    public Result<Long> getUserRefundCount(
            @Parameter(description = "用户ID") @PathVariable @NotNull Long userId) {
        try {
            Long count = refundOrderService.getUserRefundCount(userId);
            return Result.success(count);
        } catch (Exception e) {
            log.error("查询用户退款次数失败: {}", userId, e);
            return Result.error("查询用户退款次数失败: " + e.getMessage());
        }
    }

    @GetMapping("/payment-order/{paymentOrderNo}/total-refund")
    @Operation(summary = "查询支付订单退款总金额", description = "查询指定支付订单的退款总金额")
    public Result<BigDecimal> getPaymentOrderTotalRefundAmount(
            @Parameter(description = "支付订单号") @PathVariable @NotBlank String paymentOrderNo) {
        try {
            BigDecimal totalAmount = refundOrderService.getPaymentOrderTotalRefundAmount(paymentOrderNo);
            return Result.success(totalAmount);
        } catch (Exception e) {
            log.error("查询支付订单退款总金额失败: {}", paymentOrderNo, e);
            return Result.error("查询支付订单退款总金额失败: " + e.getMessage());
        }
    }

    @GetMapping("/can-refund/{paymentOrderNo}")
    @Operation(summary = "检查是否可以退款", description = "检查支付订单是否可以退款")
    public Result<Boolean> canRefund(
            @Parameter(description = "支付订单号") @PathVariable @NotBlank String paymentOrderNo) {
        try {
            boolean canRefund = refundOrderService.canRefund(paymentOrderNo);
            return Result.success(canRefund);
        } catch (Exception e) {
            log.error("检查是否可以退款失败: {}", paymentOrderNo, e);
            return Result.error("检查是否可以退款失败: " + e.getMessage());
        }
    }

    @GetMapping("/refundable-amount/{paymentOrderNo}")
    @Operation(summary = "获取可退款金额", description = "获取支付订单的可退款金额")
    public Result<BigDecimal> getRefundableAmount(
            @Parameter(description = "支付订单号") @PathVariable @NotBlank String paymentOrderNo) {
        try {
            BigDecimal refundableAmount = refundOrderService.getRefundableAmount(paymentOrderNo);
            return Result.success(refundableAmount);
        } catch (Exception e) {
            log.error("获取可退款金额失败: {}", paymentOrderNo, e);
            return Result.error("获取可退款金额失败: " + e.getMessage());
        }
    }

    @PostMapping("/generate-order-no")
    @Operation(summary = "生成退款订单号", description = "生成新的退款订单号")
    public Result<String> generateRefundOrderNo() {
        try {
            String orderNo = refundOrderService.generateRefundOrderNo();
            return Result.success(orderNo);
        } catch (Exception e) {
            log.error("生成退款订单号失败", e);
            return Result.error("生成退款订单号失败: " + e.getMessage());
        }
    }

    @PostMapping("/validate")
    @Operation(summary = "验证退款订单", description = "验证退款订单信息是否有效")
    public Result<Boolean> validateRefundOrder(
            @Valid @RequestBody RefundOrder refundOrder) {
        try {
            boolean isValid = refundOrderService.validateRefundOrder(refundOrder);
            return Result.success(isValid);
        } catch (Exception e) {
            log.error("验证退款订单失败", e);
            return Result.error("验证退款订单失败: " + e.getMessage());
        }
    }

    @GetMapping("/calculate-fee")
    @Operation(summary = "计算退款手续费", description = "计算退款手续费")
    public Result<BigDecimal> calculateRefundFee(
            @Parameter(description = "退款金额") @RequestParam @NotNull BigDecimal refundAmount,
            @Parameter(description = "支付渠道") @RequestParam @NotBlank String paymentChannel) {
        try {
            BigDecimal fee = refundOrderService.calculateRefundFee(refundAmount, paymentChannel);
            return Result.success(fee);
        } catch (Exception e) {
            log.error("计算退款手续费失败", e);
            return Result.error("计算退款手续费失败: " + e.getMessage());
        }
    }

    @GetMapping("/progress/{refundOrderNo}")
    @Operation(summary = "查询退款进度", description = "查询退款订单的处理进度")
    public Result<Map<String, Object>> getRefundProgress(
            @Parameter(description = "退款订单号") @PathVariable @NotBlank String refundOrderNo) {
        try {
            Map<String, Object> progress = refundOrderService.getRefundProgress(refundOrderNo);
            return Result.success(progress);
        } catch (Exception e) {
            log.error("查询退款进度失败: {}", refundOrderNo, e);
            return Result.error("查询退款进度失败: " + e.getMessage());
        }
    }

    @PostMapping("/sync-status/{refundOrderNo}")
    @Operation(summary = "同步第三方退款状态", description = "同步第三方退款状态")
    public Result<Boolean> syncThirdRefundStatus(
            @Parameter(description = "退款订单号") @PathVariable @NotBlank String refundOrderNo) {
        try {
            boolean result = refundOrderService.syncThirdRefundStatus(refundOrderNo);
            return Result.success(result);
        } catch (Exception e) {
            log.error("同步第三方退款状态失败: {}", refundOrderNo, e);
            return Result.error("同步第三方退款状态失败: " + e.getMessage());
        }
    }

    @PostMapping("/batch-process-pending-audit")
    @Operation(summary = "批量处理待审核订单", description = "批量处理待审核的退款订单")
    public Result<Integer> batchProcessPendingAuditOrders() {
        try {
            int count = refundOrderService.batchProcessPendingAuditOrders();
            return Result.success(count);
        } catch (Exception e) {
            log.error("批量处理待审核订单失败", e);
            return Result.error("批量处理待审核订单失败: " + e.getMessage());
        }
    }

    @PostMapping("/batch-process-pending")
    @Operation(summary = "批量处理待处理订单", description = "批量处理待处理的退款订单")
    public Result<Integer> batchProcessPendingOrders() {
        try {
            int count = refundOrderService.batchProcessPendingOrders();
            return Result.success(count);
        } catch (Exception e) {
            log.error("批量处理待处理订单失败", e);
            return Result.error("批量处理待处理订单失败: " + e.getMessage());
        }
    }
}