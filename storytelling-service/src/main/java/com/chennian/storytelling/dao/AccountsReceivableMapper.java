package com.chennian.storytelling.dao;

import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

/**
 * 应收账款数据访问接口
 * @author chen
 * @date 2023/6/15
 */
public interface AccountsReceivableMapper {
    
    /**
     * 获取应收账款总额
     * @return 应收账款总额
     */
    Double getReceivablesTotalAmount();
    
    /**
     * 获取账龄分布
     * @return 账龄分布数据
     */
    List<Map<String, Object>> getAgingDistribution();
    
    /**
     * 获取客户分布
     * @return 客户分布数据
     */
    List<Map<String, Object>> getCustomerDistribution();
    
    /**
     * 获取收款计划
     * @return 收款计划数据
     */
    List<Map<String, Object>> getCollectionPlan();
    
    /**
     * 获取应收账款统计指标
     * @return 统计指标数据
     */
    Map<String, Object> getReceivablesMetrics();
    
    /**
     * 获取应收账款账龄分析数据
     * @return 账龄分析数据列表
     */
    List<Map<String, Object>> getReceivablesAgingData();
    
    /**
     * 获取客户付款信息
     * @param customerId 客户ID
     * @return 客户付款信息
     */
    Map<String, Object> getCustomerCreditData(@Param("customerId") Long customerId);
    
    /**
     * 获取客户最近收款记录
     * @param customerId 客户ID
     * @return 最近收款记录列表
     */
    List<Map<String, Object>> getCustomerRecentCollections(@Param("customerId") Long customerId);
    
    /**
     * 获取客户待收发票
     * @param customerId 客户ID
     * @return 待收发票列表
     */
    List<Map<String, Object>> getCustomerPendingInvoices(@Param("customerId") Long customerId);
    
    /**
     * 记录收款
     * @param customerId 客户ID
     * @param invoiceNo 发票号
     * @param amount 金额
     * @return 影响行数
     */
    int recordCollection(@Param("customerId") Long customerId, @Param("invoiceNo") String invoiceNo, @Param("amount") Double amount);
}