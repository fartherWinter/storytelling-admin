package com.chennian.storytelling.service;

import com.chennian.storytelling.bean.vo.CashPoolStatusVO;
import com.chennian.storytelling.bean.vo.ForeignExchangeExposureVO;
import com.chennian.storytelling.bean.vo.InvestmentPortfolioStatusVO;

/**
 * 高级资金管理服务接口
 * 负责资金池管理、外汇风险管理、投资组合管理等高级功能
 * @author chen
 * @date 2023/6/15
 */
public interface AdvancedFundManagementService {
    
    /**
     * 获取资金池状态
     * @param poolId 资金池ID
     * @return 资金池状态数据
     */
    CashPoolStatusVO getCashPoolStatus(String poolId);
    
    /**
     * 资金池内部调拨
     * @param fromSubAccount 源子账户
     * @param toSubAccount 目标子账户
     * @param amount 金额
     * @param remarks 备注
     * @return 结果
     */
    int allocateCashPoolFunds(String fromSubAccount, String toSubAccount, Double amount, String remarks);
    
    /**
     * 获取外汇风险敞口
     * @param currency 币种
     * @param asOfDate 截止日期
     * @return 外汇风险敞口数据
     */
    ForeignExchangeExposureVO getForeignExchangeExposure(String currency, String asOfDate);
    
    /**
     * 执行外汇对冲操作
     * @param currency 币种
     * @param amount 金额
     * @param hedgeType 对冲类型（远期、期权等）
     * @param rate 汇率
     * @return 结果
     */
    int executeFxHedging(String currency, Double amount, String hedgeType, Double rate);
    
    /**
     * 获取投资组合状态
     * @param portfolioId 投资组合ID
     * @return 投资组合状态数据
     */
    InvestmentPortfolioStatusVO getInvestmentPortfolioStatus(String portfolioId);
    
    /**
     * 执行投资交易
     * @param portfolioId 投资组合ID
     * @param instrumentId 投资工具ID
     * @param tradeType 交易类型（买入、卖出）
     * @param amount 金额
     * @param price 价格
     * @return 结果
     */
    int executeInvestmentTransaction(String portfolioId, String instrumentId, String tradeType, Double amount, Double price);
}