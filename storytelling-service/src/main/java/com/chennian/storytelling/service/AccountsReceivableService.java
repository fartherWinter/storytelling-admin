package com.chennian.storytelling.service;

import com.chennian.storytelling.bean.vo.CustomerCreditInfoVO;
import com.chennian.storytelling.bean.vo.ReceivablesAgingVO;
import com.chennian.storytelling.bean.vo.ReceivablesSummaryVO;

import java.util.List;

/**
 * 应收账款管理服务接口
 * 负责应收账款相关功能
 * @author chen
 * @date 2023/6/15
 */
public interface AccountsReceivableService {
    
    /**
     * 获取应收账款汇总信息
     * @return 应收账款汇总数据
     */
    ReceivablesSummaryVO getReceivablesSummary();
    
    /**
     * 获取应收账款账龄分析
     * @return 应收账款账龄数据
     */
    List<ReceivablesAgingVO> getReceivablesAging();
    
    /**
     * 获取客户信用信息
     * @param customerId 客户ID
     * @return 客户信用信息
     */
    CustomerCreditInfoVO getCustomerCreditInfo(Long customerId);
    
    /**
     * 记录应收账款收款
     * @param customerId 客户ID
     * @param invoiceNo 发票号
     * @param amount 金额
     * @return 结果
     */
    int recordReceivableCollection(Long customerId, String invoiceNo, Double amount);
}