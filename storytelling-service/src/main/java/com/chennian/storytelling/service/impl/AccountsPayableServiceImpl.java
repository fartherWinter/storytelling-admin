package com.chennian.storytelling.service.impl;

import com.chennian.storytelling.dao.AccountsPayableMapper;
import com.chennian.storytelling.service.AccountsPayableService;
import com.chennian.storytelling.bean.vo.*;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 应付账款管理服务实现类
 * 负责应付账款相关功能
 * @author chen
 * @date 2023/6/15
 */
@Service
public class AccountsPayableServiceImpl implements AccountsPayableService {

    private final AccountsPayableMapper accountsPayableMapper;
    
    public AccountsPayableServiceImpl(AccountsPayableMapper accountsPayableMapper) {
        this.accountsPayableMapper = accountsPayableMapper;
    }

    /**
     * 获取应付账款汇总信息
     * @return 应付账款汇总数据
     */
    @Override
    public PayablesSummaryVO getPayablesSummary() {
        PayablesSummaryVO summaryVO = new PayablesSummaryVO();
        
        // 应付账款总额
        Double totalAmount = accountsPayableMapper.getPayablesTotalAmount();
        summaryVO.setTotalAmount(totalAmount != null ? totalAmount : 0.00);
        
        // 账龄分布
        List<AgingDistribution> agingDistribution = accountsPayableMapper.getAgingDistribution();
        summaryVO.setAgingDistribution(agingDistribution);

        // 供应商分布
        List<SupplierDistribution> supplierDistribution = accountsPayableMapper.getSupplierDistribution();
        summaryVO.setSupplierDistribution(supplierDistribution);
        
        // 付款计划
        List<PaymentPlan> paymentPlan = accountsPayableMapper.getPaymentPlan();
        summaryVO.setPaymentPlan(paymentPlan);
        
        // 统计指标
        PayablesSummaryVO.PayablesMetricsVO payablesMetrics = accountsPayableMapper.getPayablesMetrics();

        // 处理平均付款周期，将数字转换为字符串形式
        Object avgPaymentDays = payablesMetrics.getAveragePaymentDays();
        payablesMetrics.setAveragePaymentDays(avgPaymentDays != null ? avgPaymentDays + "天" : "0天");
        summaryVO.setMetrics(payablesMetrics);
        
        return summaryVO;
    }
    
    /**
     * 获取应付账款账龄分析
     * @return 应付账款账龄数据
     */
    @Override
    public List<PayablesAgingVO> getPayablesAging() {
        // 从数据库获取应付账款账龄分析数据
        return accountsPayableMapper.getPayablesAgingData();

    }
    
    /**
     * 获取供应商付款信息
     * @param supplierId 供应商ID
     * @return 供应商付款信息
     */
    @Override
    public SupplierPaymentInfoVO getSupplierPaymentInfo(Long supplierId) {
        // 从数据库获取供应商基本付款信息
        SupplierPaymentInfoVO paymentInfoVO = accountsPayableMapper.getSupplierPaymentData(supplierId);
        if (paymentInfoVO != null) {
            // 获取最近付款记录
            List<SupplierPaymentInfoVO.PaymentRecord> supplierRecentPayments = accountsPayableMapper.getSupplierRecentPayments(supplierId);

            paymentInfoVO.setRecentPayments(supplierRecentPayments);
            
            // 获取待付发票
            List<SupplierPaymentInfoVO.PendingInvoice> pendingInvoices = accountsPayableMapper.getSupplierPendingInvoices(supplierId);
            paymentInfoVO.setPendingInvoices(pendingInvoices);
        } else {
            // 设置默认空数据
            paymentInfoVO.setSupplierName("未知供应商");
            paymentInfoVO.setSupplierCode("Unknown");
            paymentInfoVO.setTotalAmount(0.00);
            paymentInfoVO.setPaidAmount(0.00);
            paymentInfoVO.setPaymentTerms("未知");
            paymentInfoVO.setCreditLimit(0.00);
            paymentInfoVO.setRemainingCreditLimit(0.00);
            paymentInfoVO.setRecentPayments(new ArrayList<>());
            paymentInfoVO.setPendingInvoices(new ArrayList<>());
        }
        
        return paymentInfoVO;
    }
    
    /**
     * 记录应付账款付款
     * @param supplierId 供应商ID
     * @param invoiceNo 发票号
     * @param amount 金额
     * @return 结果
     */
    @Override
    public int recordPayablePayment(Long supplierId, String invoiceNo, Double amount) {
        // 调用Mapper接口记录付款
        int result = accountsPayableMapper.recordPayment(supplierId, invoiceNo, amount);
        
        // 记录付款日志
        System.out.println("记录应付账款付款 - 供应商ID: " + supplierId + ", 发票号: " + invoiceNo + ", 金额: " + amount + ", 结果: " + (result > 0 ? "成功" : "失败"));
        
        return result;
    }
}