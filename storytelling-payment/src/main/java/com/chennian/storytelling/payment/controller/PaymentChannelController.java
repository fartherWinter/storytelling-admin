package com.chennian.storytelling.payment.controller;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.chennian.storytelling.common.result.Result;
import com.chennian.storytelling.payment.entity.PaymentChannel;
import com.chennian.storytelling.payment.service.PaymentChannelService;
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
 * 支付渠道控制器
 * 
 * @author chennian
 * @since 2024-01-01
 */
@Slf4j
@RestController
@RequestMapping("/api/payment/channel")
@RequiredArgsConstructor
@Validated
@Tag(name = "支付渠道管理", description = "支付渠道相关接口")
public class PaymentChannelController {

    private final PaymentChannelService paymentChannelService;

    @PostMapping
    @Operation(summary = "创建支付渠道", description = "创建新的支付渠道")
    public Result<PaymentChannel> createPaymentChannel(
            @Valid @RequestBody PaymentChannel paymentChannel) {
        try {
            PaymentChannel createdChannel = paymentChannelService.createPaymentChannel(paymentChannel);
            return Result.success(createdChannel);
        } catch (Exception e) {
            log.error("创建支付渠道失败", e);
            return Result.error("创建支付渠道失败: " + e.getMessage());
        }
    }

    @PutMapping("/{id}")
    @Operation(summary = "更新支付渠道", description = "更新支付渠道信息")
    public Result<Boolean> updatePaymentChannel(
            @Parameter(description = "渠道ID") @PathVariable @NotNull Long id,
            @Valid @RequestBody PaymentChannel paymentChannel) {
        try {
            paymentChannel.setId(id);
            boolean result = paymentChannelService.updatePaymentChannel(paymentChannel);
            return Result.success(result);
        } catch (Exception e) {
            log.error("更新支付渠道失败: {}", id, e);
            return Result.error("更新支付渠道失败: " + e.getMessage());
        }
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "删除支付渠道", description = "删除指定的支付渠道")
    public Result<Boolean> deletePaymentChannel(
            @Parameter(description = "渠道ID") @PathVariable @NotNull Long id) {
        try {
            boolean result = paymentChannelService.deletePaymentChannel(id);
            return Result.success(result);
        } catch (Exception e) {
            log.error("删除支付渠道失败: {}", id, e);
            return Result.error("删除支付渠道失败: " + e.getMessage());
        }
    }

    @GetMapping("/{id}")
    @Operation(summary = "根据ID查询", description = "根据渠道ID查询支付渠道")
    public Result<PaymentChannel> getById(
            @Parameter(description = "渠道ID") @PathVariable @NotNull Long id) {
        try {
            PaymentChannel channel = paymentChannelService.getById(id);
            if (channel != null) {
                return Result.success(channel);
            } else {
                return Result.error("支付渠道不存在");
            }
        } catch (Exception e) {
            log.error("查询支付渠道失败: {}", id, e);
            return Result.error("查询支付渠道失败: " + e.getMessage());
        }
    }

    @GetMapping("/code/{channelCode}")
    @Operation(summary = "根据渠道编码查询", description = "根据渠道编码查询支付渠道")
    public Result<PaymentChannel> getByChannelCode(
            @Parameter(description = "渠道编码") @PathVariable @NotBlank String channelCode) {
        try {
            PaymentChannel channel = paymentChannelService.getByChannelCode(channelCode);
            if (channel != null) {
                return Result.success(channel);
            } else {
                return Result.error("支付渠道不存在");
            }
        } catch (Exception e) {
            log.error("查询支付渠道失败: {}", channelCode, e);
            return Result.error("查询支付渠道失败: " + e.getMessage());
        }
    }

    @GetMapping("/type/{channelType}")
    @Operation(summary = "根据渠道类型查询", description = "根据渠道类型查询支付渠道列表")
    public Result<List<PaymentChannel>> listByChannelType(
            @Parameter(description = "渠道类型") @PathVariable @NotBlank String channelType) {
        try {
            List<PaymentChannel> channels = paymentChannelService.listByChannelType(channelType);
            return Result.success(channels);
        } catch (Exception e) {
            log.error("查询支付渠道失败: {}", channelType, e);
            return Result.error("查询支付渠道失败: " + e.getMessage());
        }
    }

    @GetMapping("/payment-method/{paymentMethod}")
    @Operation(summary = "根据支付方式查询", description = "根据支付方式查询支付渠道列表")
    public Result<List<PaymentChannel>> listByPaymentMethod(
            @Parameter(description = "支付方式") @PathVariable @NotBlank String paymentMethod) {
        try {
            List<PaymentChannel> channels = paymentChannelService.listByPaymentMethod(paymentMethod);
            return Result.success(channels);
        } catch (Exception e) {
            log.error("查询支付渠道失败: {}", paymentMethod, e);
            return Result.error("查询支付渠道失败: " + e.getMessage());
        }
    }

    @GetMapping("/currency/{currencyType}")
    @Operation(summary = "根据货币类型查询", description = "根据货币类型查询支付渠道列表")
    public Result<List<PaymentChannel>> listByCurrencyType(
            @Parameter(description = "货币类型") @PathVariable @NotBlank String currencyType) {
        try {
            List<PaymentChannel> channels = paymentChannelService.listByCurrencyType(currencyType);
            return Result.success(channels);
        } catch (Exception e) {
            log.error("查询支付渠道失败: {}", currencyType, e);
            return Result.error("查询支付渠道失败: " + e.getMessage());
        }
    }

    @GetMapping("/merchant/{merchantNo}")
    @Operation(summary = "根据商户号查询", description = "根据商户号查询支付渠道列表")
    public Result<List<PaymentChannel>> listByMerchantNo(
            @Parameter(description = "商户号") @PathVariable @NotBlank String merchantNo) {
        try {
            List<PaymentChannel> channels = paymentChannelService.listByMerchantNo(merchantNo);
            return Result.success(channels);
        } catch (Exception e) {
            log.error("查询支付渠道失败: {}", merchantNo, e);
            return Result.error("查询支付渠道失败: " + e.getMessage());
        }
    }

    @GetMapping("/enabled")
    @Operation(summary = "查询启用的渠道", description = "查询所有启用的支付渠道")
    public Result<List<PaymentChannel>> listEnabledChannels() {
        try {
            List<PaymentChannel> channels = paymentChannelService.listEnabledChannels();
            return Result.success(channels);
        } catch (Exception e) {
            log.error("查询启用的支付渠道失败", e);
            return Result.error("查询启用的支付渠道失败: " + e.getMessage());
        }
    }

    @GetMapping("/sandbox")
    @Operation(summary = "查询沙箱环境渠道", description = "查询沙箱环境的支付渠道")
    public Result<List<PaymentChannel>> listSandboxChannels() {
        try {
            List<PaymentChannel> channels = paymentChannelService.listSandboxChannels();
            return Result.success(channels);
        } catch (Exception e) {
            log.error("查询沙箱环境支付渠道失败", e);
            return Result.error("查询沙箱环境支付渠道失败: " + e.getMessage());
        }
    }

    @GetMapping("/production")
    @Operation(summary = "查询生产环境渠道", description = "查询生产环境的支付渠道")
    public Result<List<PaymentChannel>> listProductionChannels() {
        try {
            List<PaymentChannel> channels = paymentChannelService.listProductionChannels();
            return Result.success(channels);
        } catch (Exception e) {
            log.error("查询生产环境支付渠道失败", e);
            return Result.error("查询生产环境支付渠道失败: " + e.getMessage());
        }
    }

    @GetMapping("/page")
    @Operation(summary = "分页查询支付渠道", description = "分页查询支付渠道列表")
    public Result<IPage<PaymentChannel>> pagePaymentChannels(
            @Parameter(description = "当前页") @RequestParam(defaultValue = "1") @Positive int current,
            @Parameter(description = "页大小") @RequestParam(defaultValue = "10") @Positive int size,
            @Parameter(description = "渠道类型") @RequestParam(required = false) String channelType,
            @Parameter(description = "支付方式") @RequestParam(required = false) String paymentMethod,
            @Parameter(description = "是否启用") @RequestParam(required = false) Boolean enabled) {
        try {
            IPage<PaymentChannel> page = paymentChannelService.pagePaymentChannels(current, size, channelType, paymentMethod, enabled);
            return Result.success(page);
        } catch (Exception e) {
            log.error("分页查询支付渠道失败", e);
            return Result.error("分页查询支付渠道失败: " + e.getMessage());
        }
    }

    @GetMapping("/exists/code/{channelCode}")
    @Operation(summary = "检查渠道编码是否存在", description = "检查指定的渠道编码是否已存在")
    public Result<Boolean> existsByChannelCode(
            @Parameter(description = "渠道编码") @PathVariable @NotBlank String channelCode) {
        try {
            boolean exists = paymentChannelService.existsByChannelCode(channelCode);
            return Result.success(exists);
        } catch (Exception e) {
            log.error("检查渠道编码是否存在失败: {}", channelCode, e);
            return Result.error("检查渠道编码是否存在失败: " + e.getMessage());
        }
    }

    @GetMapping("/exists/name/{channelName}")
    @Operation(summary = "检查渠道名称是否存在", description = "检查指定的渠道名称是否已存在")
    public Result<Boolean> existsByChannelName(
            @Parameter(description = "渠道名称") @PathVariable @NotBlank String channelName) {
        try {
            boolean exists = paymentChannelService.existsByChannelName(channelName);
            return Result.success(exists);
        } catch (Exception e) {
            log.error("检查渠道名称是否存在失败: {}", channelName, e);
            return Result.error("检查渠道名称是否存在失败: " + e.getMessage());
        }
    }

    @PutMapping("/status/{id}")
    @Operation(summary = "更新渠道状态", description = "更新支付渠道的启用状态")
    public Result<Boolean> updateChannelStatus(
            @Parameter(description = "渠道ID") @PathVariable @NotNull Long id,
            @Parameter(description = "是否启用") @RequestParam @NotNull Boolean enabled) {
        try {
            boolean result = paymentChannelService.updateChannelStatus(id, enabled);
            return Result.success(result);
        } catch (Exception e) {
            log.error("更新渠道状态失败: {}", id, e);
            return Result.error("更新渠道状态失败: " + e.getMessage());
        }
    }

    @PutMapping("/sort/{id}")
    @Operation(summary = "更新渠道排序", description = "更新支付渠道的排序值")
    public Result<Boolean> updateChannelSort(
            @Parameter(description = "渠道ID") @PathVariable @NotNull Long id,
            @Parameter(description = "排序值") @RequestParam @NotNull Integer sortOrder) {
        try {
            boolean result = paymentChannelService.updateChannelSort(id, sortOrder);
            return Result.success(result);
        } catch (Exception e) {
            log.error("更新渠道排序失败: {}", id, e);
            return Result.error("更新渠道排序失败: " + e.getMessage());
        }
    }

    @PutMapping("/batch-status")
    @Operation(summary = "批量更新渠道状态", description = "批量更新支付渠道的启用状态")
    public Result<Boolean> batchUpdateChannelStatus(
            @Parameter(description = "渠道ID列表") @RequestParam @NotNull List<Long> ids,
            @Parameter(description = "是否启用") @RequestParam @NotNull Boolean enabled) {
        try {
            boolean result = paymentChannelService.batchUpdateChannelStatus(ids, enabled);
            return Result.success(result);
        } catch (Exception e) {
            log.error("批量更新渠道状态失败", e);
            return Result.error("批量更新渠道状态失败: " + e.getMessage());
        }
    }

    @GetMapping("/max-sort")
    @Operation(summary = "获取最大排序值", description = "获取当前最大的排序值")
    public Result<Integer> getMaxSortOrder() {
        try {
            Integer maxSort = paymentChannelService.getMaxSortOrder();
            return Result.success(maxSort);
        } catch (Exception e) {
            log.error("获取最大排序值失败", e);
            return Result.error("获取最大排序值失败: " + e.getMessage());
        }
    }

    @PostMapping("/validate/{id}")
    @Operation(summary = "验证渠道配置", description = "验证支付渠道配置是否正确")
    public Result<Boolean> validateChannelConfig(
            @Parameter(description = "渠道ID") @PathVariable @NotNull Long id) {
        try {
            boolean isValid = paymentChannelService.validateChannelConfig(id);
            return Result.success(isValid);
        } catch (Exception e) {
            log.error("验证渠道配置失败: {}", id, e);
            return Result.error("验证渠道配置失败: " + e.getMessage());
        }
    }

    @PostMapping("/test-connection/{id}")
    @Operation(summary = "测试渠道连接", description = "测试支付渠道连接是否正常")
    public Result<Boolean> testChannelConnection(
            @Parameter(description = "渠道ID") @PathVariable @NotNull Long id) {
        try {
            boolean isConnected = paymentChannelService.testChannelConnection(id);
            return Result.success(isConnected);
        } catch (Exception e) {
            log.error("测试渠道连接失败: {}", id, e);
            return Result.error("测试渠道连接失败: " + e.getMessage());
        }
    }

    @GetMapping("/supported-methods/{id}")
    @Operation(summary = "获取支持的支付方式", description = "获取渠道支持的支付方式列表")
    public Result<List<String>> getSupportedPaymentMethods(
            @Parameter(description = "渠道ID") @PathVariable @NotNull Long id) {
        try {
            List<String> methods = paymentChannelService.getSupportedPaymentMethods(id);
            return Result.success(methods);
        } catch (Exception e) {
            log.error("获取支持的支付方式失败: {}", id, e);
            return Result.error("获取支持的支付方式失败: " + e.getMessage());
        }
    }

    @GetMapping("/supported-currencies/{id}")
    @Operation(summary = "获取支持的货币类型", description = "获取渠道支持的货币类型列表")
    public Result<List<String>> getSupportedCurrencyTypes(
            @Parameter(description = "渠道ID") @PathVariable @NotNull Long id) {
        try {
            List<String> currencies = paymentChannelService.getSupportedCurrencyTypes(id);
            return Result.success(currencies);
        } catch (Exception e) {
            log.error("获取支持的货币类型失败: {}", id, e);
            return Result.error("获取支持的货币类型失败: " + e.getMessage());
        }
    }

    @GetMapping("/check-amount-limit/{id}")
    @Operation(summary = "检查金额限制", description = "检查金额是否在渠道限制范围内")
    public Result<Boolean> checkAmountLimit(
            @Parameter(description = "渠道ID") @PathVariable @NotNull Long id,
            @Parameter(description = "金额") @RequestParam @NotNull BigDecimal amount) {
        try {
            boolean isValid = paymentChannelService.checkAmountLimit(id, amount);
            return Result.success(isValid);
        } catch (Exception e) {
            log.error("检查金额限制失败: {}", id, e);
            return Result.error("检查金额限制失败: " + e.getMessage());
        }
    }

    @GetMapping("/calculate-fee/{id}")
    @Operation(summary = "计算手续费", description = "计算指定渠道的手续费")
    public Result<BigDecimal> calculateChannelFee(
            @Parameter(description = "渠道ID") @PathVariable @NotNull Long id,
            @Parameter(description = "金额") @RequestParam @NotNull BigDecimal amount) {
        try {
            BigDecimal fee = paymentChannelService.calculateChannelFee(id, amount);
            return Result.success(fee);
        } catch (Exception e) {
            log.error("计算手续费失败: {}", id, e);
            return Result.error("计算手续费失败: " + e.getMessage());
        }
    }

    @GetMapping("/config/{id}")
    @Operation(summary = "获取渠道配置", description = "获取支付渠道的配置信息")
    public Result<Map<String, Object>> getChannelConfig(
            @Parameter(description = "渠道ID") @PathVariable @NotNull Long id) {
        try {
            Map<String, Object> config = paymentChannelService.getChannelConfig(id);
            return Result.success(config);
        } catch (Exception e) {
            log.error("获取渠道配置失败: {}", id, e);
            return Result.error("获取渠道配置失败: " + e.getMessage());
        }
    }

    @PutMapping("/config/{id}")
    @Operation(summary = "设置渠道配置", description = "设置支付渠道的配置信息")
    public Result<Boolean> setChannelConfig(
            @Parameter(description = "渠道ID") @PathVariable @NotNull Long id,
            @RequestBody @NotNull Map<String, Object> config) {
        try {
            boolean result = paymentChannelService.setChannelConfig(id, config);
            return Result.success(result);
        } catch (Exception e) {
            log.error("设置渠道配置失败: {}", id, e);
            return Result.error("设置渠道配置失败: " + e.getMessage());
        }
    }

    @GetMapping("/ext-config/{id}")
    @Operation(summary = "获取扩展配置", description = "获取支付渠道的扩展配置")
    public Result<Map<String, Object>> getChannelExtConfig(
            @Parameter(description = "渠道ID") @PathVariable @NotNull Long id) {
        try {
            Map<String, Object> extConfig = paymentChannelService.getChannelExtConfig(id);
            return Result.success(extConfig);
        } catch (Exception e) {
            log.error("获取扩展配置失败: {}", id, e);
            return Result.error("获取扩展配置失败: " + e.getMessage());
        }
    }

    @PutMapping("/ext-config/{id}")
    @Operation(summary = "设置扩展配置", description = "设置支付渠道的扩展配置")
    public Result<Boolean> setChannelExtConfig(
            @Parameter(description = "渠道ID") @PathVariable @NotNull Long id,
            @RequestBody @NotNull Map<String, Object> extConfig) {
        try {
            boolean result = paymentChannelService.setChannelExtConfig(id, extConfig);
            return Result.success(result);
        } catch (Exception e) {
            log.error("设置扩展配置失败: {}", id, e);
            return Result.error("设置扩展配置失败: " + e.getMessage());
        }
    }

    @PostMapping("/refresh-cache/{id}")
    @Operation(summary = "刷新渠道缓存", description = "刷新指定渠道的缓存")
    public Result<Boolean> refreshChannelCache(
            @Parameter(description = "渠道ID") @PathVariable @NotNull Long id) {
        try {
            boolean result = paymentChannelService.refreshChannelCache(id);
            return Result.success(result);
        } catch (Exception e) {
            log.error("刷新渠道缓存失败: {}", id, e);
            return Result.error("刷新渠道缓存失败: " + e.getMessage());
        }
    }

    @PostMapping("/clear-cache")
    @Operation(summary = "清空所有缓存", description = "清空所有渠道的缓存")
    public Result<Boolean> clearAllChannelCache() {
        try {
            boolean result = paymentChannelService.clearAllChannelCache();
            return Result.success(result);
        } catch (Exception e) {
            log.error("清空所有缓存失败", e);
            return Result.error("清空所有缓存失败: " + e.getMessage());
        }
    }

    @GetMapping("/recommend")
    @Operation(summary = "获取推荐渠道", description = "根据条件获取推荐的支付渠道")
    public Result<List<PaymentChannel>> getRecommendedChannels(
            @Parameter(description = "支付方式") @RequestParam(required = false) String paymentMethod,
            @Parameter(description = "货币类型") @RequestParam(required = false) String currencyType,
            @Parameter(description = "支付金额") @RequestParam(required = false) BigDecimal amount) {
        try {
            List<PaymentChannel> channels = paymentChannelService.getRecommendedChannels(paymentMethod, currencyType, amount);
            return Result.success(channels);
        } catch (Exception e) {
            log.error("获取推荐渠道失败", e);
            return Result.error("获取推荐渠道失败: " + e.getMessage());
        }
    }
}