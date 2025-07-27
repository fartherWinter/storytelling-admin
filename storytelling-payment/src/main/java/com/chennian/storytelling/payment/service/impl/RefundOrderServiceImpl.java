package com.chennian.storytelling.payment.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.chennian.storytelling.payment.entity.RefundOrder;
import com.chennian.storytelling.payment.mapper.RefundOrderMapper;
import com.chennian.storytelling.payment.service.RefundOrderService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.UUID;

/**
 * 退款订单服务实现类
 * 
 * @author chennian
 * @since 2024-01-01
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class RefundOrderServiceImpl extends ServiceImpl<RefundOrderMapper, RefundOrder> implements RefundOrderService {

    private final RefundOrderMapper refundOrderMapper;

    @Override
    public RefundOrder getByRefundOrderNo(String refundOrderNo) {
        if (!StringUtils.hasText(refundOrderNo)) {
            return null;
        }
        return refundOrderMapper.selectOne(
            new LambdaQueryWrapper<RefundOrder>()
                .eq(RefundOrder::getRefundOrderNo, refundOrderNo)
        );
    }

    @Override
    public RefundOrder getByPaymentOrderId(Long paymentOrderId) {
        if (paymentOrderId == null) {
            return null;
        }
        return refundOrderMapper.selectOne(
            new LambdaQueryWrapper<RefundOrder>()
                .eq(RefundOrder::getPaymentOrderId, paymentOrderId)
                .orderByDesc(RefundOrder::getCreateTime)
                .last("LIMIT 1")
        );
    }

    @Override
    public RefundOrder getByPaymentOrderNo(String paymentOrderNo) {
        if (!StringUtils.hasText(paymentOrderNo)) {
            return null;
        }
        return refundOrderMapper.selectOne(
            new LambdaQueryWrapper<RefundOrder>()
                .eq(RefundOrder::getPaymentOrderNo, paymentOrderNo)
                .orderByDesc(RefundOrder::getCreateTime)
                .last("LIMIT 1")
        );
    }

    @Override
    public RefundOrder getByBusinessOrderNo(String businessOrderNo) {
        if (!StringUtils.hasText(businessOrderNo)) {
            return null;
        }
        return refundOrderMapper.selectOne(
            new LambdaQueryWrapper<RefundOrder>()
                .eq(RefundOrder::getBusinessOrderNo, businessOrderNo)
                .orderByDesc(RefundOrder::getCreateTime)
                .last("LIMIT 1")
        );
    }

    @Override
    public RefundOrder getByThirdRefundNo(String thirdRefundNo) {
        if (!StringUtils.hasText(thirdRefundNo)) {
            return null;
        }
        return refundOrderMapper.selectOne(
            new LambdaQueryWrapper<RefundOrder>()
                .eq(RefundOrder::getThirdRefundNo, thirdRefundNo)
        );
    }

    @Override
    public List<RefundOrder> listByUserId(Long userId) {
        if (userId == null) {
            return List.of();
        }
        return refundOrderMapper.selectList(
            new LambdaQueryWrapper<RefundOrder>()
                .eq(RefundOrder::getUserId, userId)
                .orderByDesc(RefundOrder::getCreateTime)
        );
    }

    @Override
    public List<RefundOrder> listByRefundStatus(String refundStatus) {
        if (!StringUtils.hasText(refundStatus)) {
            return List.of();
        }
        return refundOrderMapper.selectList(
            new LambdaQueryWrapper<RefundOrder>()
                .eq(RefundOrder::getRefundStatus, refundStatus)
                .orderByDesc(RefundOrder::getCreateTime)
        );
    }

    @Override
    public List<RefundOrder> listByAuditStatus(String auditStatus) {
        if (!StringUtils.hasText(auditStatus)) {
            return List.of();
        }
        return refundOrderMapper.selectList(
            new LambdaQueryWrapper<RefundOrder>()
                .eq(RefundOrder::getAuditStatus, auditStatus)
                .orderByDesc(RefundOrder::getCreateTime)
        );
    }

    @Override
    public List<RefundOrder> listByPaymentChannel(String paymentChannel) {
        if (!StringUtils.hasText(paymentChannel)) {
            return List.of();
        }
        return refundOrderMapper.selectList(
            new LambdaQueryWrapper<RefundOrder>()
                .eq(RefundOrder::getPaymentChannel, paymentChannel)
                .orderByDesc(RefundOrder::getCreateTime)
        );
    }

    @Override
    public List<RefundOrder> listPendingAuditOrders() {
        return refundOrderMapper.selectList(
            new LambdaQueryWrapper<RefundOrder>()
                .eq(RefundOrder::getAuditStatus, "PENDING")
                .orderByAsc(RefundOrder::getApplyTime)
        );
    }

    @Override
    public List<RefundOrder> listPendingProcessOrders() {
        return refundOrderMapper.selectList(
            new LambdaQueryWrapper<RefundOrder>()
                .eq(RefundOrder::getRefundStatus, "PROCESSING")
                .eq(RefundOrder::getAuditStatus, "APPROVED")
                .orderByAsc(RefundOrder::getApplyTime)
        );
    }

    @Override
    public IPage<RefundOrder> pageRefundOrders(int current, int size, String refundStatus, String auditStatus, String paymentChannel, Long userId) {
        LambdaQueryWrapper<RefundOrder> wrapper = new LambdaQueryWrapper<>();
        
        if (StringUtils.hasText(refundStatus)) {
            wrapper.eq(RefundOrder::getRefundStatus, refundStatus);
        }
        if (StringUtils.hasText(auditStatus)) {
            wrapper.eq(RefundOrder::getAuditStatus, auditStatus);
        }
        if (StringUtils.hasText(paymentChannel)) {
            wrapper.eq(RefundOrder::getPaymentChannel, paymentChannel);
        }
        if (userId != null) {
            wrapper.eq(RefundOrder::getUserId, userId);
        }
        
        wrapper.orderByDesc(RefundOrder::getCreateTime);
        
        return refundOrderMapper.selectPage(new Page<>(current, size), wrapper);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public RefundOrder createRefundOrder(RefundOrder refundOrder) {
        if (refundOrder == null) {
            throw new IllegalArgumentException("退款订单不能为空");
        }
        
        // 生成退款订单号
        if (!StringUtils.hasText(refundOrder.getRefundOrderNo())) {
            refundOrder.setRefundOrderNo(generateRefundOrderNo());
        }
        
        // 设置默认值
        if (!StringUtils.hasText(refundOrder.getRefundStatus())) {
            refundOrder.setRefundStatus("PENDING");
        }
        if (!StringUtils.hasText(refundOrder.getAuditStatus())) {
            refundOrder.setAuditStatus("PENDING");
        }
        if (refundOrder.getApplyTime() == null) {
            refundOrder.setApplyTime(LocalDateTime.now());
        }
        
        refundOrder.setCreateTime(LocalDateTime.now());
        refundOrder.setUpdateTime(LocalDateTime.now());
        
        refundOrderMapper.insert(refundOrder);
        return refundOrder;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean updateRefundOrder(RefundOrder refundOrder) {
        if (refundOrder == null || refundOrder.getId() == null) {
            return false;
        }
        
        refundOrder.setUpdateTime(LocalDateTime.now());
        return refundOrderMapper.updateById(refundOrder) > 0;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean updateRefundStatus(String refundOrderNo, String refundStatus) {
        if (!StringUtils.hasText(refundOrderNo) || !StringUtils.hasText(refundStatus)) {
            return false;
        }
        
        return refundOrderMapper.update(null,
            new LambdaUpdateWrapper<RefundOrder>()
                .eq(RefundOrder::getRefundOrderNo, refundOrderNo)
                .set(RefundOrder::getRefundStatus, refundStatus)
                .set(RefundOrder::getUpdateTime, LocalDateTime.now())
        ) > 0;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean updateAuditInfo(String refundOrderNo, String auditStatus, Long auditUserId, String auditRemark) {
        if (!StringUtils.hasText(refundOrderNo) || !StringUtils.hasText(auditStatus)) {
            return false;
        }
        
        LambdaUpdateWrapper<RefundOrder> wrapper = new LambdaUpdateWrapper<RefundOrder>()
            .eq(RefundOrder::getRefundOrderNo, refundOrderNo)
            .set(RefundOrder::getAuditStatus, auditStatus)
            .set(RefundOrder::getAuditTime, LocalDateTime.now())
            .set(RefundOrder::getUpdateTime, LocalDateTime.now());
        
        if (auditUserId != null) {
            wrapper.set(RefundOrder::getAuditUserId, auditUserId);
        }
        if (StringUtils.hasText(auditRemark)) {
            wrapper.set(RefundOrder::getAuditRemark, auditRemark);
        }
        
        return refundOrderMapper.update(null, wrapper) > 0;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean updateThirdRefundInfo(String refundOrderNo, String thirdRefundNo, String thirdTransactionNo) {
        if (!StringUtils.hasText(refundOrderNo)) {
            return false;
        }
        
        LambdaUpdateWrapper<RefundOrder> wrapper = new LambdaUpdateWrapper<RefundOrder>()
            .eq(RefundOrder::getRefundOrderNo, refundOrderNo)
            .set(RefundOrder::getUpdateTime, LocalDateTime.now());
        
        if (StringUtils.hasText(thirdRefundNo)) {
            wrapper.set(RefundOrder::getThirdRefundNo, thirdRefundNo);
        }
        if (StringUtils.hasText(thirdTransactionNo)) {
            wrapper.set(RefundOrder::getThirdTransactionNo, thirdTransactionNo);
        }
        
        return refundOrderMapper.update(null, wrapper) > 0;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean applyRefund(String refundOrderNo, String refundReason, String description) {
        if (!StringUtils.hasText(refundOrderNo)) {
            return false;
        }
        
        LambdaUpdateWrapper<RefundOrder> wrapper = new LambdaUpdateWrapper<RefundOrder>()
            .eq(RefundOrder::getRefundOrderNo, refundOrderNo)
            .set(RefundOrder::getRefundStatus, "PENDING")
            .set(RefundOrder::getAuditStatus, "PENDING")
            .set(RefundOrder::getApplyTime, LocalDateTime.now())
            .set(RefundOrder::getUpdateTime, LocalDateTime.now());
        
        if (StringUtils.hasText(refundReason)) {
            wrapper.set(RefundOrder::getRefundReason, refundReason);
        }
        if (StringUtils.hasText(description)) {
            wrapper.set(RefundOrder::getDescription, description);
        }
        
        return refundOrderMapper.update(null, wrapper) > 0;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean auditRefund(String refundOrderNo, String auditStatus, Long auditUserId, String auditRemark) {
        return updateAuditInfo(refundOrderNo, auditStatus, auditUserId, auditRemark);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean processRefund(String refundOrderNo) {
        if (!StringUtils.hasText(refundOrderNo)) {
            return false;
        }
        
        return refundOrderMapper.update(null,
            new LambdaUpdateWrapper<RefundOrder>()
                .eq(RefundOrder::getRefundOrderNo, refundOrderNo)
                .set(RefundOrder::getRefundStatus, "PROCESSING")
                .set(RefundOrder::getRefundTime, LocalDateTime.now())
                .set(RefundOrder::getUpdateTime, LocalDateTime.now())
        ) > 0;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean cancelRefund(String refundOrderNo, String cancelReason) {
        if (!StringUtils.hasText(refundOrderNo)) {
            return false;
        }
        
        LambdaUpdateWrapper<RefundOrder> wrapper = new LambdaUpdateWrapper<RefundOrder>()
            .eq(RefundOrder::getRefundOrderNo, refundOrderNo)
            .set(RefundOrder::getRefundStatus, "CANCELLED")
            .set(RefundOrder::getUpdateTime, LocalDateTime.now());
        
        if (StringUtils.hasText(cancelReason)) {
            wrapper.set(RefundOrder::getDescription, cancelReason);
        }
        
        return refundOrderMapper.update(null, wrapper) > 0;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean handleRefundSuccess(String refundOrderNo, BigDecimal actualRefundAmount, String thirdRefundNo, String thirdTransactionNo) {
        if (!StringUtils.hasText(refundOrderNo)) {
            return false;
        }
        
        LambdaUpdateWrapper<RefundOrder> wrapper = new LambdaUpdateWrapper<RefundOrder>()
            .eq(RefundOrder::getRefundOrderNo, refundOrderNo)
            .set(RefundOrder::getRefundStatus, "SUCCESS")
            .set(RefundOrder::getSuccessTime, LocalDateTime.now())
            .set(RefundOrder::getUpdateTime, LocalDateTime.now());
        
        if (actualRefundAmount != null) {
            wrapper.set(RefundOrder::getActualRefundAmount, actualRefundAmount);
        }
        if (StringUtils.hasText(thirdRefundNo)) {
            wrapper.set(RefundOrder::getThirdRefundNo, thirdRefundNo);
        }
        if (StringUtils.hasText(thirdTransactionNo)) {
            wrapper.set(RefundOrder::getThirdTransactionNo, thirdTransactionNo);
        }
        
        return refundOrderMapper.update(null, wrapper) > 0;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean handleRefundFailed(String refundOrderNo, String failReason) {
        if (!StringUtils.hasText(refundOrderNo)) {
            return false;
        }
        
        LambdaUpdateWrapper<RefundOrder> wrapper = new LambdaUpdateWrapper<RefundOrder>()
            .eq(RefundOrder::getRefundOrderNo, refundOrderNo)
            .set(RefundOrder::getRefundStatus, "FAILED")
            .set(RefundOrder::getUpdateTime, LocalDateTime.now());
        
        if (StringUtils.hasText(failReason)) {
            wrapper.set(RefundOrder::getDescription, failReason);
        }
        
        return refundOrderMapper.update(null, wrapper) > 0;
    }

    @Override
    public BigDecimal getTotalRefundAmount() {
        return refundOrderMapper.getTotalRefundAmount();
    }

    @Override
    public Map<String, BigDecimal> getRefundAmountByChannel() {
        return refundOrderMapper.getRefundAmountByChannel();
    }

    @Override
    public Map<String, Long> getRefundCountByStatus() {
        return refundOrderMapper.getRefundCountByStatus();
    }

    @Override
    public BigDecimal getUserTotalRefundAmount(Long userId) {
        if (userId == null) {
            return BigDecimal.ZERO;
        }
        return refundOrderMapper.getUserTotalRefundAmount(userId);
    }

    @Override
    public Long getUserRefundCount(Long userId) {
        if (userId == null) {
            return 0L;
        }
        return refundOrderMapper.getUserRefundCount(userId);
    }

    @Override
    public BigDecimal getPaymentOrderRefundAmount(Long paymentOrderId) {
        if (paymentOrderId == null) {
            return BigDecimal.ZERO;
        }
        return refundOrderMapper.getPaymentOrderRefundAmount(paymentOrderId);
    }

    @Override
    public boolean canRefund(String refundOrderNo) {
        if (!StringUtils.hasText(refundOrderNo)) {
            return false;
        }
        
        RefundOrder refundOrder = getByRefundOrderNo(refundOrderNo);
        if (refundOrder == null) {
            return false;
        }
        
        // 只有待处理或审核通过的订单可以退款
        return "PENDING".equals(refundOrder.getRefundStatus()) || 
               ("PROCESSING".equals(refundOrder.getRefundStatus()) && "APPROVED".equals(refundOrder.getAuditStatus()));
    }

    @Override
    public BigDecimal getRefundableAmount(Long paymentOrderId) {
        if (paymentOrderId == null) {
            return BigDecimal.ZERO;
        }
        
        // 这里需要根据支付订单计算可退款金额
        // 实际实现中需要查询支付订单的原始金额，减去已退款金额
        BigDecimal refundedAmount = getPaymentOrderRefundAmount(paymentOrderId);
        // TODO: 获取支付订单的原始金额
        // BigDecimal originalAmount = paymentOrderService.getPaymentAmount(paymentOrderId);
        // return originalAmount.subtract(refundedAmount);
        
        return BigDecimal.ZERO;
    }

    @Override
    public String generateRefundOrderNo() {
        // 生成格式：RF + 年月日 + 6位随机数
        String dateStr = LocalDateTime.now().format(java.time.format.DateTimeFormatter.ofPattern("yyyyMMdd"));
        String randomStr = String.valueOf((int) ((Math.random() * 9 + 1) * 100000));
        return "RF" + dateStr + randomStr;
    }

    @Override
    public boolean validateRefundOrder(RefundOrder refundOrder) {
        if (refundOrder == null) {
            return false;
        }
        
        // 验证必要字段
        if (!StringUtils.hasText(refundOrder.getRefundOrderNo()) ||
            refundOrder.getPaymentOrderId() == null ||
            !StringUtils.hasText(refundOrder.getPaymentOrderNo()) ||
            refundOrder.getUserId() == null ||
            refundOrder.getRefundAmount() == null ||
            refundOrder.getRefundAmount().compareTo(BigDecimal.ZERO) <= 0) {
            return false;
        }
        
        return true;
    }

    @Override
    public BigDecimal calculateRefundFee(BigDecimal refundAmount, String paymentChannel) {
        if (refundAmount == null || refundAmount.compareTo(BigDecimal.ZERO) <= 0) {
            return BigDecimal.ZERO;
        }
        
        // 根据不同支付渠道计算退款手续费
        // 这里是示例实现，实际应该根据渠道配置计算
        BigDecimal feeRate = BigDecimal.ZERO;
        
        if ("ALIPAY".equals(paymentChannel)) {
            feeRate = new BigDecimal("0.006"); // 0.6%
        } else if ("WECHAT".equals(paymentChannel)) {
            feeRate = new BigDecimal("0.006"); // 0.6%
        } else if ("UNIONPAY".equals(paymentChannel)) {
            feeRate = new BigDecimal("0.008"); // 0.8%
        }
        
        return refundAmount.multiply(feeRate).setScale(2, BigDecimal.ROUND_HALF_UP);
    }

    @Override
    public String getRefundProgress(String refundOrderNo) {
        if (!StringUtils.hasText(refundOrderNo)) {
            return "未知状态";
        }
        
        RefundOrder refundOrder = getByRefundOrderNo(refundOrderNo);
        if (refundOrder == null) {
            return "退款订单不存在";
        }
        
        String refundStatus = refundOrder.getRefundStatus();
        String auditStatus = refundOrder.getAuditStatus();
        
        if ("PENDING".equals(refundStatus)) {
            if ("PENDING".equals(auditStatus)) {
                return "退款申请已提交，等待审核";
            } else if ("APPROVED".equals(auditStatus)) {
                return "审核通过，等待退款处理";
            } else if ("REJECTED".equals(auditStatus)) {
                return "审核未通过";
            }
        } else if ("PROCESSING".equals(refundStatus)) {
            return "退款处理中";
        } else if ("SUCCESS".equals(refundStatus)) {
            return "退款成功";
        } else if ("FAILED".equals(refundStatus)) {
            return "退款失败";
        } else if ("CANCELLED".equals(refundStatus)) {
            return "退款已取消";
        }
        
        return "未知状态";
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean syncThirdRefundStatus(String refundOrderNo) {
        if (!StringUtils.hasText(refundOrderNo)) {
            return false;
        }
        
        RefundOrder refundOrder = getByRefundOrderNo(refundOrderNo);
        if (refundOrder == null) {
            return false;
        }
        
        // TODO: 调用第三方接口查询退款状态
        // 这里是示例实现，实际需要根据不同支付渠道调用相应的查询接口
        
        log.info("同步第三方退款状态: {}", refundOrderNo);
        return true;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public int batchProcessPendingAuditOrders() {
        List<RefundOrder> pendingOrders = listPendingAuditOrders();
        int processedCount = 0;
        
        for (RefundOrder order : pendingOrders) {
            // TODO: 实现自动审核逻辑
            // 这里是示例实现，实际需要根据业务规则进行审核
            if (order.getRefundAmount().compareTo(new BigDecimal("1000")) <= 0) {
                if (auditRefund(order.getRefundOrderNo(), "APPROVED", null, "自动审核通过")) {
                    processedCount++;
                }
            }
        }
        
        return processedCount;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public int batchProcessPendingRefundOrders() {
        List<RefundOrder> pendingOrders = listPendingProcessOrders();
        int processedCount = 0;
        
        for (RefundOrder order : pendingOrders) {
            // TODO: 实现自动退款处理逻辑
            // 这里是示例实现，实际需要调用第三方支付接口进行退款
            if (processRefund(order.getRefundOrderNo())) {
                processedCount++;
            }
        }
        
        return processedCount;
    }
}