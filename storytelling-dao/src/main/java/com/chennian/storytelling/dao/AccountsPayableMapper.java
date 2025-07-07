package com.chennian.storytelling.dao;

import com.chennian.storytelling.bean.vo.*;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

/**
 * 应付账款数据访问接口
 * @author chen
 * @date 2023/6/15
 */
@Mapper
public interface AccountsPayableMapper {
    
    /**
     * 获取应付账款总额
     * @return 应付账款总额
     */
    Double getPayablesTotalAmount();
    
    /**
     * 获取应付账款账龄分布
     * @return 账龄分布数据
     */
    List<AgingDistribution> getAgingDistribution();
    
    /**
     * 获取供应商应付账款分布
     * @return 供应商分布数据
     */
    List<SupplierDistribution> getSupplierDistribution();
    
    /**
     * 获取付款计划
     * @return 付款计划数据
     */
    List<PaymentPlan> getPaymentPlan();
    
    /**
     * 获取应付账款统计指标
     * @return 统计指标数据
     */
    PayablesSummaryVO.PayablesMetricsVO getPayablesMetrics();
    
    /**
     * 获取应付账款账龄分析数据
     * @return 应付账款账龄分析数据列表
     */
    List<PayablesAgingVO> getPayablesAgingData();
    
    /**
     * 获取供应商付款信息
     * @param supplierId 供应商ID
     * @return 供应商付款信息
     */
    SupplierPaymentInfoVO getSupplierPaymentData(@Param("supplierId") Long supplierId);
    
    /**
     * 获取供应商最近付款记录
     * @param supplierId 供应商ID
     * @return 最近付款记录列表
     */
    List<SupplierPaymentInfoVO.PaymentRecord> getSupplierRecentPayments(@Param("supplierId") Long supplierId);
    
    /**
     * 获取供应商待付发票
     * @param supplierId 供应商ID
     * @return 待付发票列表
     */
    List<SupplierPaymentInfoVO.PendingInvoice> getSupplierPendingInvoices(@Param("supplierId") Long supplierId);
    
    /**
     * 记录应付账款付款
     * @param supplierId 供应商ID
     * @param invoiceNo 发票号
     * @param amount 金额
     * @return 影响行数
     */
    int recordPayment(@Param("supplierId") Long supplierId, @Param("invoiceNo") String invoiceNo, @Param("amount") Double amount);
}