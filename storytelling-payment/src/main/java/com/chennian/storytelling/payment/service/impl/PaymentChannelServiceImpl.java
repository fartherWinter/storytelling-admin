package com.chennian.storytelling.payment.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.chennian.storytelling.common.exception.BusinessException;
import com.chennian.storytelling.payment.entity.PaymentChannel;
import com.chennian.storytelling.payment.mapper.PaymentChannelMapper;
import com.chennian.storytelling.payment.service.PaymentChannelService;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

/**
 * 支付渠道服务实现类
 *
 * @author chennian
 * @since 2024-01-01
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class PaymentChannelServiceImpl extends ServiceImpl<PaymentChannelMapper, PaymentChannel> implements PaymentChannelService {

    private final ObjectMapper objectMapper;

    @Override
    @Cacheable(value = "paymentChannel", key = "#channelCode")
    public PaymentChannel getByChannelCode(String channelCode) {
        return getOne(new LambdaQueryWrapper<PaymentChannel>()
                .eq(PaymentChannel::getChannelCode, channelCode)
                .eq(PaymentChannel::getDeleted, false));
    }

    @Override
    public List<PaymentChannel> getByChannelType(String channelType) {
        return list(new LambdaQueryWrapper<PaymentChannel>()
                .eq(PaymentChannel::getChannelType, channelType)
                .eq(PaymentChannel::getDeleted, false)
                .orderByAsc(PaymentChannel::getSort));
    }

    @Override
    public List<PaymentChannel> getByPaymentMethod(String paymentMethod) {
        return list(new LambdaQueryWrapper<PaymentChannel>()
                .like(PaymentChannel::getPaymentMethods, paymentMethod)
                .eq(PaymentChannel::getDeleted, false)
                .orderByAsc(PaymentChannel::getSort));
    }

    @Override
    public List<PaymentChannel> getByCurrency(String currency) {
        return list(new LambdaQueryWrapper<PaymentChannel>()
                .like(PaymentChannel::getSupportedCurrencies, currency)
                .eq(PaymentChannel::getDeleted, false)
                .orderByAsc(PaymentChannel::getSort));
    }

    @Override
    public List<PaymentChannel> getByMerchantNo(String merchantNo) {
        return list(new LambdaQueryWrapper<PaymentChannel>()
                .eq(PaymentChannel::getMerchantNo, merchantNo)
                .eq(PaymentChannel::getDeleted, false)
                .orderByAsc(PaymentChannel::getSort));
    }

    @Override
    @Cacheable(value = "paymentChannels", key = "'enabled'")
    public List<PaymentChannel> getEnabledChannels() {
        return list(new LambdaQueryWrapper<PaymentChannel>()
                .eq(PaymentChannel::getEnabled, true)
                .eq(PaymentChannel::getDeleted, false)
                .orderByAsc(PaymentChannel::getSort));
    }

    @Override
    public List<PaymentChannel> getSandboxChannels() {
        return list(new LambdaQueryWrapper<PaymentChannel>()
                .eq(PaymentChannel::getSandbox, true)
                .eq(PaymentChannel::getDeleted, false)
                .orderByAsc(PaymentChannel::getSort));
    }

    @Override
    public List<PaymentChannel> getProductionChannels() {
        return list(new LambdaQueryWrapper<PaymentChannel>()
                .eq(PaymentChannel::getSandbox, false)
                .eq(PaymentChannel::getDeleted, false)
                .orderByAsc(PaymentChannel::getSort));
    }

    @Override
    public IPage<PaymentChannel> getChannelPage(Page<PaymentChannel> page) {
        return page(page, new LambdaQueryWrapper<PaymentChannel>()
                .eq(PaymentChannel::getDeleted, false)
                .orderByAsc(PaymentChannel::getSort));
    }

    @Override
    public boolean existsByChannelCode(String channelCode) {
        return count(new LambdaQueryWrapper<PaymentChannel>()
                .eq(PaymentChannel::getChannelCode, channelCode)
                .eq(PaymentChannel::getDeleted, false)) > 0;
    }

    @Override
    public boolean existsByChannelName(String channelName) {
        return count(new LambdaQueryWrapper<PaymentChannel>()
                .eq(PaymentChannel::getChannelName, channelName)
                .eq(PaymentChannel::getDeleted, false)) > 0;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    @CacheEvict(value = {"paymentChannel", "paymentChannels"}, allEntries = true)
    public boolean updateChannelStatus(Long id, Boolean enabled) {
        PaymentChannel channel = new PaymentChannel();
        channel.setId(id);
        channel.setEnabled(enabled);
        channel.setUpdateTime(LocalDateTime.now());
        return updateById(channel);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    @CacheEvict(value = {"paymentChannel", "paymentChannels"}, allEntries = true)
    public boolean updateChannelSort(Long id, Integer sort) {
        PaymentChannel channel = new PaymentChannel();
        channel.setId(id);
        channel.setSort(sort);
        channel.setUpdateTime(LocalDateTime.now());
        return updateById(channel);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    @CacheEvict(value = {"paymentChannel", "paymentChannels"}, allEntries = true)
    public int batchUpdateChannelStatus(List<Long> ids, Boolean enabled) {
        return baseMapper.batchUpdateChannelStatus(ids, enabled);
    }

    @Override
    public Integer getMaxSort() {
        return baseMapper.getMaxSort();
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    @CacheEvict(value = {"paymentChannel", "paymentChannels"}, allEntries = true)
    public PaymentChannel createPaymentChannel(PaymentChannel paymentChannel) {
        log.info("创建支付渠道: {}", paymentChannel.getChannelCode());
        
        // 验证渠道配置
        if (!validateChannelConfig(paymentChannel)) {
            throw new BusinessException("支付渠道配置验证失败");
        }
        
        // 检查渠道编码是否已存在
        if (existsByChannelCode(paymentChannel.getChannelCode())) {
            throw new BusinessException("渠道编码已存在: " + paymentChannel.getChannelCode());
        }
        
        // 设置排序
        if (paymentChannel.getSort() == null) {
            Integer maxSort = getMaxSort();
            paymentChannel.setSort(maxSort != null ? maxSort + 1 : 1);
        }
        
        // 设置默认值
        if (paymentChannel.getEnabled() == null) {
            paymentChannel.setEnabled(true);
        }
        if (paymentChannel.getSandbox() == null) {
            paymentChannel.setSandbox(false);
        }
        
        if (!save(paymentChannel)) {
            throw new BusinessException("创建支付渠道失败");
        }
        
        log.info("支付渠道创建成功: {}", paymentChannel.getChannelCode());
        return paymentChannel;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    @CacheEvict(value = {"paymentChannel", "paymentChannels"}, allEntries = true)
    public boolean updatePaymentChannel(PaymentChannel paymentChannel) {
        log.info("更新支付渠道: {}", paymentChannel.getChannelCode());
        
        // 验证渠道配置
        if (!validateChannelConfig(paymentChannel)) {
            throw new BusinessException("支付渠道配置验证失败");
        }
        
        paymentChannel.setUpdateTime(LocalDateTime.now());
        return updateById(paymentChannel);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    @CacheEvict(value = {"paymentChannel", "paymentChannels"}, allEntries = true)
    public boolean deletePaymentChannel(Long id) {
        PaymentChannel channel = new PaymentChannel();
        channel.setId(id);
        channel.setDeleted(true);
        channel.setUpdateTime(LocalDateTime.now());
        return updateById(channel);
    }

    @Override
    public boolean validateChannelConfig(PaymentChannel paymentChannel) {
        if (paymentChannel == null) {
            return false;
        }
        
        // 验证必填字段
        if (!StringUtils.hasText(paymentChannel.getChannelCode()) ||
            !StringUtils.hasText(paymentChannel.getChannelName()) ||
            !StringUtils.hasText(paymentChannel.getChannelType()) ||
            !StringUtils.hasText(paymentChannel.getMerchantNo())) {
            return false;
        }
        
        // 验证金额限制
        if (paymentChannel.getMinAmount() != null && paymentChannel.getMaxAmount() != null) {
            if (paymentChannel.getMinAmount().compareTo(paymentChannel.getMaxAmount()) > 0) {
                return false;
            }
        }
        
        // 验证手续费率
        if (paymentChannel.getFeeRate() != null) {
            if (paymentChannel.getFeeRate().compareTo(BigDecimal.ZERO) < 0 ||
                paymentChannel.getFeeRate().compareTo(new BigDecimal("100")) > 0) {
                return false;
            }
        }
        
        return true;
    }

    @Override
    public boolean testChannelConnection(String channelCode) {
        PaymentChannel channel = getByChannelCode(channelCode);
        if (channel == null || !channel.getEnabled()) {
            return false;
        }
        
        try {
            // 这里可以实现具体的连接测试逻辑
            // 例如调用第三方支付接口进行连通性测试
            log.info("测试支付渠道连接: {}", channelCode);
            return true;
        } catch (Exception e) {
            log.error("支付渠道连接测试失败: {}", channelCode, e);
            return false;
        }
    }

    @Override
    public List<String> getChannelPaymentMethods(String channelCode) {
        PaymentChannel channel = getByChannelCode(channelCode);
        if (channel == null || !StringUtils.hasText(channel.getPaymentMethods())) {
            return Collections.emptyList();
        }
        
        return Arrays.asList(channel.getPaymentMethods().split(","));
    }

    @Override
    public List<String> getChannelCurrencies(String channelCode) {
        PaymentChannel channel = getByChannelCode(channelCode);
        if (channel == null || !StringUtils.hasText(channel.getSupportedCurrencies())) {
            return Collections.emptyList();
        }
        
        return Arrays.asList(channel.getSupportedCurrencies().split(","));
    }

    @Override
    public boolean isAmountInLimit(String channelCode, BigDecimal amount) {
        PaymentChannel channel = getByChannelCode(channelCode);
        if (channel == null) {
            return false;
        }
        
        // 检查最小金额
        if (channel.getMinAmount() != null && amount.compareTo(channel.getMinAmount()) < 0) {
            return false;
        }
        
        // 检查最大金额
        if (channel.getMaxAmount() != null && amount.compareTo(channel.getMaxAmount()) > 0) {
            return false;
        }
        
        return true;
    }

    @Override
    public BigDecimal calculateFee(String channelCode, BigDecimal amount) {
        PaymentChannel channel = getByChannelCode(channelCode);
        if (channel == null || channel.getFeeRate() == null) {
            return BigDecimal.ZERO;
        }
        
        // 计算手续费：金额 * 费率 / 100
        BigDecimal fee = amount.multiply(channel.getFeeRate()).divide(new BigDecimal("100"), 2, RoundingMode.HALF_UP);
        
        // 检查最小手续费
        if (channel.getMinFee() != null && fee.compareTo(channel.getMinFee()) < 0) {
            fee = channel.getMinFee();
        }
        
        // 检查最大手续费
        if (channel.getMaxFee() != null && fee.compareTo(channel.getMaxFee()) > 0) {
            fee = channel.getMaxFee();
        }
        
        return fee;
    }

    @Override
    public String getChannelConfig(String channelCode, String configKey) {
        PaymentChannel channel = getByChannelCode(channelCode);
        if (channel == null || !StringUtils.hasText(channel.getExtConfig())) {
            return null;
        }
        
        try {
            Map<String, Object> extConfig = objectMapper.readValue(channel.getExtConfig(), new TypeReference<Map<String, Object>>() {});
            Object value = extConfig.get(configKey);
            return value != null ? value.toString() : null;
        } catch (Exception e) {
            log.error("解析渠道扩展配置失败: {}", channelCode, e);
            return null;
        }
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    @CacheEvict(value = "paymentChannel", key = "#channelCode")
    public boolean setChannelConfig(String channelCode, String configKey, String configValue) {
        PaymentChannel channel = getByChannelCode(channelCode);
        if (channel == null) {
            return false;
        }
        
        try {
            Map<String, Object> extConfig = new HashMap<>();
            if (StringUtils.hasText(channel.getExtConfig())) {
                extConfig = objectMapper.readValue(channel.getExtConfig(), new TypeReference<Map<String, Object>>() {});
            }
            
            extConfig.put(configKey, configValue);
            channel.setExtConfig(objectMapper.writeValueAsString(extConfig));
            channel.setUpdateTime(LocalDateTime.now());
            
            return updateById(channel);
        } catch (Exception e) {
            log.error("设置渠道配置失败: {}", channelCode, e);
            return false;
        }
    }

    @Override
    public Map<String, Object> getChannelExtConfig(String channelCode) {
        PaymentChannel channel = getByChannelCode(channelCode);
        if (channel == null || !StringUtils.hasText(channel.getExtConfig())) {
            return Collections.emptyMap();
        }
        
        try {
            return objectMapper.readValue(channel.getExtConfig(), new TypeReference<Map<String, Object>>() {});
        } catch (Exception e) {
            log.error("解析渠道扩展配置失败: {}", channelCode, e);
            return Collections.emptyMap();
        }
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    @CacheEvict(value = "paymentChannel", key = "#channelCode")
    public boolean setChannelExtConfig(String channelCode, Map<String, Object> extConfig) {
        PaymentChannel channel = getByChannelCode(channelCode);
        if (channel == null) {
            return false;
        }
        
        try {
            channel.setExtConfig(objectMapper.writeValueAsString(extConfig));
            channel.setUpdateTime(LocalDateTime.now());
            return updateById(channel);
        } catch (Exception e) {
            log.error("设置渠道扩展配置失败: {}", channelCode, e);
            return false;
        }
    }

    @Override
    @CacheEvict(value = "paymentChannel", key = "#channelCode")
    public void refreshChannelCache(String channelCode) {
        log.info("刷新渠道缓存: {}", channelCode);
    }

    @Override
    @CacheEvict(value = {"paymentChannel", "paymentChannels"}, allEntries = true)
    public void clearChannelCache() {
        log.info("清空渠道缓存");
    }

    @Override
    public List<PaymentChannel> getRecommendedChannels(String paymentMethod, BigDecimal amount, String currency) {
        return getEnabledChannels().stream()
                .filter(channel -> {
                    // 检查支付方式
                    if (StringUtils.hasText(paymentMethod) && StringUtils.hasText(channel.getPaymentMethods())) {
                        if (!channel.getPaymentMethods().contains(paymentMethod)) {
                            return false;
                        }
                    }
                    
                    // 检查金额限制
                    if (amount != null && !isAmountInLimit(channel.getChannelCode(), amount)) {
                        return false;
                    }
                    
                    // 检查货币类型
                    if (StringUtils.hasText(currency) && StringUtils.hasText(channel.getSupportedCurrencies())) {
                        if (!channel.getSupportedCurrencies().contains(currency)) {
                            return false;
                        }
                    }
                    
                    return true;
                })
                .sorted(Comparator.comparing(PaymentChannel::getSort))
                .collect(Collectors.toList());
    }
}