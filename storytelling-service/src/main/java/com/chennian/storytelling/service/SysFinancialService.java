package com.chennian.storytelling.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.chennian.storytelling.bean.model.FinancialTransaction;
import com.chennian.storytelling.common.utils.PageParam;

import java.util.List;
import java.util.Map;

/**
 * 财务管理服务接口
 * @author by chennian
 * @date 2025/5/11.
 */
public interface SysFinancialService {
    
    /**
     * 分页查询财务交易记录
     * 
     * @param page 分页参数
     * @param transaction 查询条件
     * @return 交易记录分页数据
     */
    IPage<FinancialTransaction> findByPage(PageParam<FinancialTransaction> page, FinancialTransaction transaction);
    
    /**
     * 根据ID查询交易记录
     * 
     * @param transactionId 交易ID
     * @return 交易记录信息
     */
    FinancialTransaction selectTransactionById(Long transactionId);
    
    /**
     * 新增交易记录
     * 
     * @param transaction 交易记录信息
     * @return 结果
     */
    int insertTransaction(FinancialTransaction transaction);
    
    /**
     * 修改交易记录
     * 
     * @param transaction 交易记录信息
     * @return 结果
     */
    int updateTransaction(FinancialTransaction transaction);
    
    /**
     * 批量删除交易记录
     * 
     * @param transactionIds 需要删除的交易ID数组
     * @return 结果
     */
    int deleteTransactionByIds(Long[] transactionIds);
    
    /**
     * 获取收入支出统计报表
     * 
     * @param dateRange 日期范围
     * @return 统计数据
     */
    Map<String, Object> getIncomeExpenseReport(String dateRange);
    
    /**
     * 获取利润分析报表
     * 
     * @param dateRange 日期范围
     * @return 统计数据
     */
    Map<String, Object> getProfitReport(String dateRange);
    
    /**
     * 获取现金流量表
     * 
     * @param dateRange 日期范围
     * @return 统计数据
     */
    Map<String, Object> getCashFlowReport(String dateRange);
    
    /**
     * 获取应收账款列表
     * 
     * @return 应收账款数据
     */
    List<Map<String, Object>> getReceivableList();
    
    /**
     * 获取应付账款列表
     * 
     * @return 应付账款数据
     */
    List<Map<String, Object>> getPayableList();
}
