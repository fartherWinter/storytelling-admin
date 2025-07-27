package com.chennian.storytelling.payment.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.chennian.storytelling.common.exception.BusinessException;
import com.chennian.storytelling.common.util.IdGenerator;
import com.chennian.storytelling.payment.entity.PaymentOrder;
import com.chennian.storytelling.payment.mapper.PaymentOrderMapper;
import com.chennian.storytelling.payment.service.PaymentChannelService;
import com.chennian.storytelling.payment.service.PaymentOrderService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

/**
 * 支付订单服务实现类
 *
 * @author chennian
 * @since 2024-01-01
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class PaymentOrderServiceImpl extends ServiceImpl<PaymentOrderMapper, PaymentOrder> implements PaymentOrderService {

    private final PaymentChannelService paymentChannelService;

    @Override
    @Transactional(rollbackFor = Exception.class)
    public PaymentOrder createPaymentOrder(PaymentOrder paymentOrder) {
        log.info("创建支付订单: {}", paymentOrder);
        
        // 验证支付订单
        if (!validatePaymentOrder(paymentOrder)) {
            throw new BusinessException("支付订单验证失败");
        }
        
        // 生成支付订单号
        if (!StringUtils.hasText(paymentOrder.getPaymentOrderNo())) {
            paymentOrder.setPaymentOrderNo(generatePaymentOrderNo());
        }
        
        // 设置默认值
        paymentOrder.setPaymentStatus("PENDING");
        paymentOrder.setRefundStatus("NONE");
        paymentOrder.setRefundedAmount(BigDecimal.ZERO);
        paymentOrder.setRefundableAmount(paymentOrder.getPaymentAmount());
        
        // 设置过期时间
        setExpireTime(paymentOrder, 30); // 默认30分钟
        
        // 保存订单
        if (!save(paymentOrder)) {
            throw new BusinessException("创建支付订单失败");
        }
        
        log.info("支付订单创建成功: {}", paymentOrder.getPaymentOrderNo());
        return paymentOrder;
    }

    @Override
    public PaymentOrder getByPaymentOrderNo(String paymentOrderNo) {
        return getOne(new LambdaQueryWrapper<PaymentOrder>()
                .eq(PaymentOrder::getPaymentOrderNo, paymentOrderNo)
                .eq(PaymentOrder::getDeleted, false));
    }

    @Override
    public List<PaymentOrder> getByBusinessOrderNo(String businessOrderNo) {
        return list(new LambdaQueryWrapper<PaymentOrder>()
                .eq(PaymentOrder::getBusinessOrderNo, businessOrderNo)
                .eq(PaymentOrder::getDeleted, false)
                .orderByDesc(PaymentOrder::getCreateTime));
    }

    @Override
    public PaymentOrder getByThirdPartyTradeNo(String thirdPartyTradeNo) {
        return getOne(new LambdaQueryWrapper<PaymentOrder>()
                .eq(PaymentOrder::getThirdPartyTradeNo, thirdPartyTradeNo)
                .eq(PaymentOrder::getDeleted, false));
    }

    @Override
    public IPage<PaymentOrder> getPageByUserId(Page<PaymentOrder> page, Long userId) {
        return page(page, new LambdaQueryWrapper<PaymentOrder>()
                .eq(PaymentOrder::getUserId, userId)
                .eq(PaymentOrder::getDeleted, false)
                .orderByDesc(PaymentOrder::getCreateTime));
    }

    @Override
    public List<PaymentOrder> getByPaymentStatus(String paymentStatus) {
        return list(new LambdaQueryWrapper<PaymentOrder>()
                .eq(PaymentOrder::getPaymentStatus, paymentStatus)
                .eq(PaymentOrder::getDeleted, false)
                .orderByDesc(PaymentOrder::getCreateTime));
    }

    @Override
    public List<PaymentOrder> getByPaymentChannel(String paymentChannel) {
        return list(new LambdaQueryWrapper<PaymentOrder>()
                .eq(PaymentOrder::getPaymentChannel, paymentChannel)
                .eq(PaymentOrder::getDeleted, false)
                .orderByDesc(PaymentOrder::getCreateTime));
    }

    @Override
    public List<PaymentOrder> getTimeoutOrders() {
        return baseMapper.selectTimeoutOrders();
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean updatePaymentStatus(Long id, String paymentStatus) {
        PaymentOrder paymentOrder = new PaymentOrder();
        paymentOrder.setId(id);
        paymentOrder.setPaymentStatus(paymentStatus);
        paymentOrder.setUpdateTime(LocalDateTime.now());
        return updateById(paymentOrder);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean updateThirdPartyInfo(Long id, String thirdPartyTradeNo, String thirdPartyOrderNo,
                                       BigDecimal actualAmount, LocalDateTime paymentTime) {
        return baseMapper.updateThirdPartyInfo(id, thirdPartyTradeNo, thirdPartyOrderNo, actualAmount, paymentTime) > 0;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean updateRefundInfo(Long id, String refundStatus, BigDecimal refundedAmount, BigDecimal refundableAmount) {
        return baseMapper.updateRefundInfo(id, refundStatus, refundedAmount, refundableAmount) > 0;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean handlePaymentSuccess(String paymentOrderNo, String thirdPartyTradeNo, String thirdPartyOrderNo,
                                      BigDecimal actualAmount, LocalDateTime paymentTime) {
        log.info("处理支付成功: orderNo={}, tradeNo={}", paymentOrderNo, thirdPartyTradeNo);
        
        PaymentOrder paymentOrder = getByPaymentOrderNo(paymentOrderNo);
        if (paymentOrder == null) {
            log.error("支付订单不存在: {}", paymentOrderNo);
            return false;
        }
        
        if (!"PENDING".equals(paymentOrder.getPaymentStatus())) {
            log.warn("支付订单状态不正确: orderNo={}, status={}", paymentOrderNo, paymentOrder.getPaymentStatus());
            return false;
        }
        
        // 更新支付信息
        paymentOrder.setPaymentStatus("SUCCESS");
        paymentOrder.setThirdPartyTradeNo(thirdPartyTradeNo);
        paymentOrder.setThirdPartyOrderNo(thirdPartyOrderNo);
        paymentOrder.setActualAmount(actualAmount);
        paymentOrder.setPaymentTime(paymentTime);
        paymentOrder.setUpdateTime(LocalDateTime.now());
        
        boolean result = updateById(paymentOrder);
        if (result) {
            log.info("支付成功处理完成: {}", paymentOrderNo);
        }
        
        return result;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean handlePaymentFailed(String paymentOrderNo, String errorCode, String errorMessage) {
        log.info("处理支付失败: orderNo={}, errorCode={}", paymentOrderNo, errorCode);
        
        PaymentOrder paymentOrder = getByPaymentOrderNo(paymentOrderNo);
        if (paymentOrder == null) {
            log.error("支付订单不存在: {}", paymentOrderNo);
            return false;
        }
        
        paymentOrder.setPaymentStatus("FAILED");
        paymentOrder.setErrorCode(errorCode);
        paymentOrder.setErrorMessage(errorMessage);
        paymentOrder.setUpdateTime(LocalDateTime.now());
        
        return updateById(paymentOrder);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean handlePaymentTimeout(String paymentOrderNo) {
        log.info("处理支付超时: {}", paymentOrderNo);
        
        PaymentOrder paymentOrder = getByPaymentOrderNo(paymentOrderNo);
        if (paymentOrder == null) {
            log.error("支付订单不存在: {}", paymentOrderNo);
            return false;
        }
        
        if (!"PENDING".equals(paymentOrder.getPaymentStatus())) {
            log.warn("支付订单状态不正确: orderNo={}, status={}", paymentOrderNo, paymentOrder.getPaymentStatus());
            return false;
        }
        
        paymentOrder.setPaymentStatus("TIMEOUT");
        paymentOrder.setUpdateTime(LocalDateTime.now());
        
        return updateById(paymentOrder);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public int batchHandleTimeoutOrders() {
        log.info("批量处理超时订单");
        return baseMapper.batchUpdateTimeoutOrders();
    }

    @Override
    public Map<String, Object> statisticsPaymentAmount(LocalDateTime startTime, LocalDateTime endTime) {
        return baseMapper.statisticsPaymentAmount(startTime, endTime);
    }

    @Override
    public List<Map<String, Object>> statisticsPaymentAmountByChannel(LocalDateTime startTime, LocalDateTime endTime) {
        return baseMapper.statisticsPaymentAmountByChannel(startTime, endTime);
    }

    @Override
    public List<Map<String, Object>> statisticsOrderCountByStatus(LocalDateTime startTime, LocalDateTime endTime) {
        return baseMapper.statisticsOrderCountByStatus(startTime, endTime);
    }

    @Override
    public BigDecimal getUserTotalPaymentAmount(Long userId) {
        return baseMapper.getUserTotalPaymentAmount(userId);
    }

    @Override
    public Long getUserPaymentCount(Long userId) {
        return baseMapper.getUserPaymentCount(userId);
    }

    @Override
    public boolean canPay(String paymentOrderNo) {
        PaymentOrder paymentOrder = getByPaymentOrderNo(paymentOrderNo);
        if (paymentOrder == null) {
            return false;
        }
        
        // 检查订单状态
        if (!"PENDING".equals(paymentOrder.getPaymentStatus())) {
            return false;
        }
        
        // 检查是否过期
        if (paymentOrder.getExpireTime() != null && paymentOrder.getExpireTime().isBefore(LocalDateTime.now())) {
            return false;
        }
        
        return true;
    }

    @Override
    public boolean canRefund(String paymentOrderNo) {
        PaymentOrder paymentOrder = getByPaymentOrderNo(paymentOrderNo);
        if (paymentOrder == null) {
            return false;
        }
        
        // 只有支付成功的订单才能退款
        if (!"SUCCESS".equals(paymentOrder.getPaymentStatus())) {
            return false;
        }
        
        // 检查可退款金额
        return paymentOrder.getRefundableAmount().compareTo(BigDecimal.ZERO) > 0;
    }

    @Override
    public BigDecimal getRefundableAmount(String paymentOrderNo) {
        PaymentOrder paymentOrder = getByPaymentOrderNo(paymentOrderNo);
        if (paymentOrder == null) {
            return BigDecimal.ZERO;
        }
        
        return paymentOrder.getRefundableAmount() != null ? paymentOrder.getRefundableAmount() : BigDecimal.ZERO;
    }

    @Override
    public String generatePaymentOrderNo() {
        return "PAY" + IdGenerator.generateId();
    }

    @Override
    public boolean validatePaymentOrder(PaymentOrder paymentOrder) {
        if (paymentOrder == null) {
            return false;
        }
        
        // 验证必填字段
        if (!StringUtils.hasText(paymentOrder.getBusinessOrderNo()) ||
            !StringUtils.hasText(paymentOrder.getBusinessType()) ||
            paymentOrder.getUserId() == null ||
            !StringUtils.hasText(paymentOrder.getPaymentChannel()) ||
            !StringUtils.hasText(paymentOrder.getPaymentMethod()) ||
            paymentOrder.getPaymentAmount() == null ||
            paymentOrder.getPaymentAmount().compareTo(BigDecimal.ZERO) <= 0) {
            return false;
        }
        
        // 验证支付渠道是否可用
        if (!paymentChannelService.isAmountInLimit(paymentOrder.getPaymentChannel(), paymentOrder.getPaymentAmount())) {
            return false;
        }
        
        return true;
    }

    @Override
    public BigDecimal calculateFee(BigDecimal paymentAmount, String paymentChannel) {
        return paymentChannelService.calculateFee(paymentChannel, paymentAmount);
    }

    @Override
    public void setExpireTime(PaymentOrder paymentOrder, Integer timeoutMinutes) {
        if (timeoutMinutes != null && timeoutMinutes > 0) {
            paymentOrder.setExpireTime(LocalDateTime.now().plusMinutes(timeoutMinutes));
        }
    }
}