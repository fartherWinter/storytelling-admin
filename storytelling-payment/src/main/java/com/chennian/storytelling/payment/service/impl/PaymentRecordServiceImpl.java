package com.chennian.storytelling.payment.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.chennian.storytelling.payment.entity.PaymentRecord;
import com.chennian.storytelling.payment.mapper.PaymentRecordMapper;
import com.chennian.storytelling.payment.service.PaymentRecordService;
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
 * 支付记录服务实现类
 * 
 * @author chennian
 * @since 2024-01-01
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class PaymentRecordServiceImpl extends ServiceImpl<PaymentRecordMapper, PaymentRecord> implements PaymentRecordService {

    private final PaymentRecordMapper paymentRecordMapper;

    @Override
    public PaymentRecord getByPaymentOrderId(Long paymentOrderId) {
        if (paymentOrderId == null) {
            return null;
        }
        return paymentRecordMapper.selectOne(
            new LambdaQueryWrapper<PaymentRecord>()
                .eq(PaymentRecord::getPaymentOrderId, paymentOrderId)
                .orderByDesc(PaymentRecord::getCreateTime)
                .last("LIMIT 1")
        );
    }

    @Override
    public PaymentRecord getByPaymentOrderNo(String paymentOrderNo) {
        if (!StringUtils.hasText(paymentOrderNo)) {
            return null;
        }
        return paymentRecordMapper.selectOne(
            new LambdaQueryWrapper<PaymentRecord>()
                .eq(PaymentRecord::getPaymentOrderNo, paymentOrderNo)
                .orderByDesc(PaymentRecord::getCreateTime)
                .last("LIMIT 1")
        );
    }

    @Override
    public List<PaymentRecord> listByRecordType(String recordType) {
        if (!StringUtils.hasText(recordType)) {
            return List.of();
        }
        return paymentRecordMapper.selectList(
            new LambdaQueryWrapper<PaymentRecord>()
                .eq(PaymentRecord::getRecordType, recordType)
                .orderByDesc(PaymentRecord::getCreateTime)
        );
    }

    @Override
    public List<PaymentRecord> listByOperationType(String operationType) {
        if (!StringUtils.hasText(operationType)) {
            return List.of();
        }
        return paymentRecordMapper.selectList(
            new LambdaQueryWrapper<PaymentRecord>()
                .eq(PaymentRecord::getOperationType, operationType)
                .orderByDesc(PaymentRecord::getCreateTime)
        );
    }

    @Override
    public List<PaymentRecord> listByStatus(String status) {
        if (!StringUtils.hasText(status)) {
            return List.of();
        }
        return paymentRecordMapper.selectList(
            new LambdaQueryWrapper<PaymentRecord>()
                .eq(PaymentRecord::getStatus, status)
                .orderByDesc(PaymentRecord::getCreateTime)
        );
    }

    @Override
    public List<PaymentRecord> listByPaymentChannel(String paymentChannel) {
        if (!StringUtils.hasText(paymentChannel)) {
            return List.of();
        }
        return paymentRecordMapper.selectList(
            new LambdaQueryWrapper<PaymentRecord>()
                .eq(PaymentRecord::getPaymentChannel, paymentChannel)
                .orderByDesc(PaymentRecord::getCreateTime)
        );
    }

    @Override
    public List<PaymentRecord> listFailedRecords() {
        return paymentRecordMapper.selectList(
            new LambdaQueryWrapper<PaymentRecord>()
                .eq(PaymentRecord::getStatus, "FAILED")
                .orderByDesc(PaymentRecord::getCreateTime)
        );
    }

    @Override
    public List<PaymentRecord> listRetryRecords() {
        return paymentRecordMapper.selectList(
            new LambdaQueryWrapper<PaymentRecord>()
                .eq(PaymentRecord::getStatus, "RETRY")
                .or()
                .gt(PaymentRecord::getRetryCount, 0)
                .orderByDesc(PaymentRecord::getCreateTime)
        );
    }

    @Override
    public IPage<PaymentRecord> pagePaymentRecords(int current, int size, String recordType, String operationType, String status, String paymentChannel) {
        LambdaQueryWrapper<PaymentRecord> wrapper = new LambdaQueryWrapper<>();
        
        if (StringUtils.hasText(recordType)) {
            wrapper.eq(PaymentRecord::getRecordType, recordType);
        }
        if (StringUtils.hasText(operationType)) {
            wrapper.eq(PaymentRecord::getOperationType, operationType);
        }
        if (StringUtils.hasText(status)) {
            wrapper.eq(PaymentRecord::getStatus, status);
        }
        if (StringUtils.hasText(paymentChannel)) {
            wrapper.eq(PaymentRecord::getPaymentChannel, paymentChannel);
        }
        
        wrapper.orderByDesc(PaymentRecord::getCreateTime);
        
        return paymentRecordMapper.selectPage(new Page<>(current, size), wrapper);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public PaymentRecord createPaymentRecord(PaymentRecord paymentRecord) {
        if (paymentRecord == null) {
            throw new IllegalArgumentException("支付记录不能为空");
        }
        
        // 设置默认值
        if (paymentRecord.getRetryCount() == null) {
            paymentRecord.setRetryCount(0);
        }
        if (paymentRecord.getProcessTime() == null) {
            paymentRecord.setProcessTime(0L);
        }
        if (!StringUtils.hasText(paymentRecord.getStatus())) {
            paymentRecord.setStatus("PROCESSING");
        }
        
        paymentRecord.setCreateTime(LocalDateTime.now());
        paymentRecord.setUpdateTime(LocalDateTime.now());
        
        paymentRecordMapper.insert(paymentRecord);
        return paymentRecord;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean updatePaymentRecord(PaymentRecord paymentRecord) {
        if (paymentRecord == null || paymentRecord.getId() == null) {
            return false;
        }
        
        paymentRecord.setUpdateTime(LocalDateTime.now());
        return paymentRecordMapper.updateById(paymentRecord) > 0;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean recordPaymentRequest(Long paymentOrderId, String paymentOrderNo, String operationType, String requestData, String paymentChannel) {
        PaymentRecord record = new PaymentRecord();
        record.setPaymentOrderId(paymentOrderId);
        record.setPaymentOrderNo(paymentOrderNo);
        record.setRecordType("REQUEST");
        record.setOperationType(operationType);
        record.setRequestData(requestData);
        record.setPaymentChannel(paymentChannel);
        record.setStatus("PROCESSING");
        record.setRetryCount(0);
        record.setProcessTime(0L);
        
        return createPaymentRecord(record) != null;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean recordPaymentResponse(Long paymentOrderId, String paymentOrderNo, String operationType, String responseData, String status, Long processTime) {
        PaymentRecord record = new PaymentRecord();
        record.setPaymentOrderId(paymentOrderId);
        record.setPaymentOrderNo(paymentOrderNo);
        record.setRecordType("RESPONSE");
        record.setOperationType(operationType);
        record.setResponseData(responseData);
        record.setStatus(status);
        record.setProcessTime(processTime);
        record.setRetryCount(0);
        
        return createPaymentRecord(record) != null;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean recordRefundRequest(Long paymentOrderId, String paymentOrderNo, String requestData, String paymentChannel) {
        return recordPaymentRequest(paymentOrderId, paymentOrderNo, "REFUND", requestData, paymentChannel);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean recordRefundResponse(Long paymentOrderId, String paymentOrderNo, String responseData, String status, Long processTime) {
        return recordPaymentResponse(paymentOrderId, paymentOrderNo, "REFUND", responseData, status, processTime);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean recordQueryRequest(Long paymentOrderId, String paymentOrderNo, String requestData, String paymentChannel) {
        return recordPaymentRequest(paymentOrderId, paymentOrderNo, "QUERY", requestData, paymentChannel);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean recordQueryResponse(Long paymentOrderId, String paymentOrderNo, String responseData, String status, Long processTime) {
        return recordPaymentResponse(paymentOrderId, paymentOrderNo, "QUERY", responseData, status, processTime);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean increaseRetryCount(Long recordId) {
        if (recordId == null) {
            return false;
        }
        
        return paymentRecordMapper.update(null,
            new LambdaUpdateWrapper<PaymentRecord>()
                .eq(PaymentRecord::getId, recordId)
                .setSql("retry_count = retry_count + 1")
                .set(PaymentRecord::getUpdateTime, LocalDateTime.now())
        ) > 0;
    }

    @Override
    public Map<String, Long> countByOperationType() {
        return paymentRecordMapper.countByOperationType();
    }

    @Override
    public Map<String, Long> countByStatus() {
        return paymentRecordMapper.countByStatus();
    }

    @Override
    public Map<String, Long> countByPaymentChannel() {
        return paymentRecordMapper.countByPaymentChannel();
    }

    @Override
    public Double getAverageProcessTime() {
        return paymentRecordMapper.getAverageProcessTime();
    }

    @Override
    public Double getSuccessRate() {
        return paymentRecordMapper.getSuccessRate();
    }

    @Override
    public List<PaymentRecord> listSlowOperations(Long threshold) {
        if (threshold == null || threshold <= 0) {
            threshold = 5000L; // 默认5秒
        }
        
        return paymentRecordMapper.selectList(
            new LambdaQueryWrapper<PaymentRecord>()
                .gt(PaymentRecord::getProcessTime, threshold)
                .orderByDesc(PaymentRecord::getProcessTime)
        );
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public int deleteExpiredRecords(int days) {
        if (days <= 0) {
            days = 30; // 默认删除30天前的记录
        }
        
        LocalDateTime expireTime = LocalDateTime.now().minusDays(days);
        
        return paymentRecordMapper.delete(
            new LambdaQueryWrapper<PaymentRecord>()
                .lt(PaymentRecord::getCreateTime, expireTime)
        );
    }

    @Override
    public List<PaymentRecord> exportPaymentRecords(LocalDateTime startTime, LocalDateTime endTime, String recordType, String operationType, String status) {
        LambdaQueryWrapper<PaymentRecord> wrapper = new LambdaQueryWrapper<>();
        
        if (startTime != null) {
            wrapper.ge(PaymentRecord::getCreateTime, startTime);
        }
        if (endTime != null) {
            wrapper.le(PaymentRecord::getCreateTime, endTime);
        }
        if (StringUtils.hasText(recordType)) {
            wrapper.eq(PaymentRecord::getRecordType, recordType);
        }
        if (StringUtils.hasText(operationType)) {
            wrapper.eq(PaymentRecord::getOperationType, operationType);
        }
        if (StringUtils.hasText(status)) {
            wrapper.eq(PaymentRecord::getStatus, status);
        }
        
        wrapper.orderByDesc(PaymentRecord::getCreateTime);
        
        return paymentRecordMapper.selectList(wrapper);
    }

    @Override
    public PaymentRecord getRecordDetail(Long recordId) {
        if (recordId == null) {
            return null;
        }
        return paymentRecordMapper.selectById(recordId);
    }

    @Override
    public boolean validateRecordIntegrity(Long recordId) {
        if (recordId == null) {
            return false;
        }
        
        PaymentRecord record = paymentRecordMapper.selectById(recordId);
        if (record == null) {
            return false;
        }
        
        // 验证必要字段
        if (record.getPaymentOrderId() == null || 
            !StringUtils.hasText(record.getPaymentOrderNo()) ||
            !StringUtils.hasText(record.getRecordType()) ||
            !StringUtils.hasText(record.getOperationType()) ||
            !StringUtils.hasText(record.getStatus())) {
            return false;
        }
        
        // 验证记录类型和数据的一致性
        if ("REQUEST".equals(record.getRecordType()) && !StringUtils.hasText(record.getRequestData())) {
            return false;
        }
        if ("RESPONSE".equals(record.getRecordType()) && !StringUtils.hasText(record.getResponseData())) {
            return false;
        }
        
        return true;
    }
}