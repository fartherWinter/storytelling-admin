package com.chennian.storytelling.service.impl;

import com.chennian.storytelling.dao.AccountsReceivableMapper;
import com.chennian.storytelling.service.AccountsReceivableService;
import com.chennian.storytelling.vo.CustomerCreditInfoVO;
import com.chennian.storytelling.vo.ReceivablesAgingVO;
import com.chennian.storytelling.vo.ReceivablesSummaryVO;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 应收账款管理服务实现类
 * 负责应收账款相关功能
 * @author chen
 * @date 2023/6/15
 */
@Service
public class AccountsReceivableServiceImpl implements AccountsReceivableService {

    private final AccountsReceivableMapper accountsReceivableMapper;
    
    public AccountsReceivableServiceImpl(AccountsReceivableMapper accountsReceivableMapper) {
        this.accountsReceivableMapper = accountsReceivableMapper;
    }

    /**
     * 获取应收账款汇总信息
     * @return 应收账款汇总数据
     */
    @Override
    public ReceivablesSummaryVO getReceivablesSummary() {
        ReceivablesSummaryVO summaryVO = new ReceivablesSummaryVO();
        
        // 设置应收账款总额
        Double totalAmount = accountsReceivableMapper.getReceivablesTotalAmount();
        summaryVO.setTotalAmount(totalAmount);
        
        // 设置账龄分布
        Map<String, Object> agingDistribution = new HashMap<>();
        List<Map<String, Object>> agingData = accountsReceivableMapper.getAgingDistribution();
        for (Map<String, Object> data : agingData) {
            agingDistribution.put(String.valueOf(data.get("period")), data.get("amount"));
        }
        summaryVO.setAgingDistribution(agingDistribution);
        
        // 设置客户分布
        Map<String, Object> customerDistribution = new HashMap<>();
        List<Map<String, Object>> customerData = accountsReceivableMapper.getCustomerDistribution();
        for (Map<String, Object> data : customerData) {
            customerDistribution.put(String.valueOf(data.get("customer_name")), data.get("amount"));
        }
        summaryVO.setCustomerDistribution(customerDistribution);
        
        // 设置收款计划
        Map<String, Object> collectionPlan = new HashMap<>();
        List<Map<String, Object>> planData = accountsReceivableMapper.getCollectionPlan();
        for (Map<String, Object> data : planData) {
            collectionPlan.put(String.valueOf(data.get("period")), data.get("amount"));
        }
        summaryVO.setCollectionPlan(collectionPlan);
        
        // 设置统计指标
        Map<String, Object> metricsData = accountsReceivableMapper.getReceivablesMetrics();
        ReceivablesSummaryVO.ReceivablesMetricsVO metrics = new ReceivablesSummaryVO.ReceivablesMetricsVO();
        metrics.setAverageCollectionDays(String.valueOf(metricsData.get("average_collection_days")));
        metrics.setCustomerCount(metricsData.get("customer_count"));
        metrics.setCurrentMonthReceivable(metricsData.get("current_month_receivable"));
        metrics.setCurrentMonthReceived(metricsData.get("current_month_received"));
        summaryVO.setMetrics(metrics);
        
        return summaryVO;
    }
    
    /**
     * 获取应收账款账龄分析
     * @return 应收账款账龄数据
     */
    @Override
    public List<ReceivablesAgingVO> getReceivablesAging() {
        List<ReceivablesAgingVO> agingVOList = new ArrayList<>();
        
        // 从数据库获取账龄分析数据
        List<Map<String, Object>> agingDataList = accountsReceivableMapper.getReceivablesAgingData();
        
        // 将数据转换为VO对象
        for (Map<String, Object> agingData : agingDataList) {
            ReceivablesAgingVO agingVO = new ReceivablesAgingVO();
            
            agingVO.setCustomerName(String.valueOf(agingData.get("customer_name")));
            agingVO.setCustomerCode(String.valueOf(agingData.get("customer_code")));
            agingVO.setTotalAmount((Double) agingData.get("total_amount"));
            agingVO.setWithin30Days((Double) agingData.get("within_30_days"));
            agingVO.setDays31To60((Double) agingData.get("days_31_to_60"));
            agingVO.setDays61To90((Double) agingData.get("days_61_to_90"));
            agingVO.setOver90Days((Double) agingData.get("over_90_days"));
            agingVO.setContactPerson(String.valueOf(agingData.get("contact_person")));
            agingVO.setContactPhone(String.valueOf(agingData.get("contact_phone")));
            
            agingVOList.add(agingVO);
        }
        
        return agingVOList;
    }
    
    /**
     * 获取客户信用信息
     * @param customerId 客户ID
     * @return 客户信用信息
     */
    @Override
    public CustomerCreditInfoVO getCustomerCreditInfo(Long customerId) {
        CustomerCreditInfoVO creditInfoVO = new CustomerCreditInfoVO();
        
        // 从数据库获取客户信用基本信息
        Map<String, Object> creditData = accountsReceivableMapper.getCustomerCreditData(customerId);
        
        if (creditData != null && !creditData.isEmpty()) {
            // 设置基本信息
            creditInfoVO.setCustomerName(String.valueOf(creditData.get("customer_name")));
            creditInfoVO.setCustomerCode(String.valueOf(creditData.get("customer_code")));
            creditInfoVO.setTotalAmount((Double) creditData.get("total_amount"));
            creditInfoVO.setReceivedAmount((Double) creditData.get("received_amount"));
            creditInfoVO.setCreditLimit((Double) creditData.get("credit_limit"));
            creditInfoVO.setRemainingCreditLimit((Double) creditData.get("remaining_credit_limit"));
            creditInfoVO.setCreditRating(String.valueOf(creditData.get("credit_rating")));
            creditInfoVO.setPaymentTerms(String.valueOf(creditData.get("payment_terms")));
            
            // 获取最近收款记录
            List<Map<String, Object>> recentCollectionsData = accountsReceivableMapper.getCustomerRecentCollections(customerId);
            List<CustomerCreditInfoVO.CollectionRecord> recentCollections = new ArrayList<>();
            
            for (Map<String, Object> collectionData : recentCollectionsData) {
                CustomerCreditInfoVO.CollectionRecord record = new CustomerCreditInfoVO.CollectionRecord();
                record.setDate(String.valueOf(collectionData.get("date")));
                record.setAmount((Double) collectionData.get("amount"));
                record.setInvoiceNo(String.valueOf(collectionData.get("invoice_no")));
                recentCollections.add(record);
            }
            creditInfoVO.setRecentCollections(recentCollections);
            
            // 获取待收发票
            List<Map<String, Object>> pendingInvoicesData = accountsReceivableMapper.getCustomerPendingInvoices(customerId);
            List<CustomerCreditInfoVO.PendingInvoice> pendingInvoices = new ArrayList<>();
            
            for (Map<String, Object> invoiceData : pendingInvoicesData) {
                CustomerCreditInfoVO.PendingInvoice invoice = new CustomerCreditInfoVO.PendingInvoice();
                invoice.setInvoiceNo(String.valueOf(invoiceData.get("invoice_no")));
                invoice.setDate(String.valueOf(invoiceData.get("date")));
                invoice.setAmount((Double) invoiceData.get("amount"));
                invoice.setDueDate(String.valueOf(invoiceData.get("due_date")));
                pendingInvoices.add(invoice);
            }
            creditInfoVO.setPendingInvoices(pendingInvoices);
        } else {
            // 默认返回空数据
            creditInfoVO.setCustomerName("未知客户");
            creditInfoVO.setCustomerCode("Unknown");
            creditInfoVO.setTotalAmount(0.00);
            creditInfoVO.setReceivedAmount(0.00);
            creditInfoVO.setCreditLimit(0.00);
            creditInfoVO.setRemainingCreditLimit(0.00);
            creditInfoVO.setCreditRating("未知");
            creditInfoVO.setPaymentTerms("未知");
            creditInfoVO.setRecentCollections(new ArrayList<>());
            creditInfoVO.setPendingInvoices(new ArrayList<>());
        }
        
        return creditInfoVO;
    }
    
    /**
     * 记录应收账款收款
     * @param customerId 客户ID
     * @param invoiceNo 发票号
     * @param amount 金额
     * @return 结果
     */
    @Override
    public int recordReceivableCollection(Long customerId, String invoiceNo, Double amount) {
        // 使用Mapper接口调用数据库进行实际的收款记录操作
        return accountsReceivableMapper.recordCollection(customerId, invoiceNo, amount);
    }
}