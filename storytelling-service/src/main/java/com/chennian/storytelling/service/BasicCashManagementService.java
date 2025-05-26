package com.chennian.storytelling.service;

import java.util.Map;

/**
 * 基础现金管理服务接口
 * 负责银行对账、现金流预测、资金转账等基础现金管理功能
 * @author chen
 * @date 2023/6/15
 */
public interface BasicCashManagementService {
    
    /**
     * 获取银行对账单
     * @param accountNo 账户号
     * @param period 期间
     * @return 银行对账数据
     */
    Map<String, Object> getBankReconciliation(String accountNo, String period);
    
    /**
     * 获取现金流预测
     * @param startDate 开始日期
     * @param endDate 结束日期
     * @return 现金流预测数据
     */
    Map<String, Object> getCashFlowForecast(String startDate, String endDate);
    
    /**
     * 记录资金转账
     * @param fromAccount 源账户
     * @param toAccount 目标账户
     * @param amount 金额
     * @return 结果
     */
    int recordFundTransfer(String fromAccount, String toAccount, Double amount);
}