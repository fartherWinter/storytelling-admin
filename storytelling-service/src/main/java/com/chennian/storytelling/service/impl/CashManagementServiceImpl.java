package com.chennian.storytelling.service.impl;

import com.chennian.storytelling.service.AdvancedFundManagementService;
import com.chennian.storytelling.service.BasicCashManagementService;
import com.chennian.storytelling.service.CashManagementService;
import com.chennian.storytelling.vo.CashPoolStatusVO;
import com.chennian.storytelling.vo.ForeignExchangeExposureVO;
import com.chennian.storytelling.vo.InvestmentPortfolioStatusVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Map;

/**
 * 现金与资金管理服务实现类
 * 通过组合方式实现CashManagementService接口，委托具体实现给BasicCashManagementService和AdvancedFundManagementService
 * @author chen
 * @date 2023/6/15
 */
@Service
public class CashManagementServiceImpl implements CashManagementService {

    @Autowired
    private BasicCashManagementService basicCashManagementService;
    
    @Autowired
    private AdvancedFundManagementService advancedFundManagementService;
    
    /**
     * 获取银行对账单 - 委托给基础现金管理服务
     * @param accountNo 账户号
     * @param period 期间
     * @return 银行对账数据
     */
    @Override
    public Map<String, Object> getBankReconciliation(String accountNo, String period) {
        return basicCashManagementService.getBankReconciliation(accountNo, period);
    }

    /**
     * 获取现金流预测 - 委托给基础现金管理服务
     * @param startDate 开始日期
     * @param endDate 结束日期
     * @return 现金流预测数据
     */
    @Override
    public Map<String, Object> getCashFlowForecast(String startDate, String endDate) {
        return basicCashManagementService.getCashFlowForecast(startDate, endDate);
    }

    /**
     * 记录资金转账 - 委托给基础现金管理服务
     * @param fromAccount 源账户
     * @param toAccount 目标账户
     * @param amount 金额
     * @return 结果
     */
    @Override
    public int recordFundTransfer(String fromAccount, String toAccount, Double amount) {
        return basicCashManagementService.recordFundTransfer(fromAccount, toAccount, amount);
    }
    
    /**
     * 获取资金池状态 - 委托给高级资金管理服务
     * @param poolId 资金池ID
     * @return 资金池状态数据
     */
    @Override
    public CashPoolStatusVO getCashPoolStatus(String poolId) {
        return advancedFundManagementService.getCashPoolStatus(poolId);
    }
    
    /**
     * 资金池内部调拨 - 委托给高级资金管理服务
     * @param fromSubAccount 源子账户
     * @param toSubAccount 目标子账户
     * @param amount 金额
     * @param remarks 备注
     * @return 结果
     */
    @Override
    public int allocateCashPoolFunds(String fromSubAccount, String toSubAccount, Double amount, String remarks) {
        return advancedFundManagementService.allocateCashPoolFunds(fromSubAccount, toSubAccount, amount, remarks);
    }
    
    /**
     * 获取外汇风险敞口 - 委托给高级资金管理服务
     * @param currency 币种
     * @param asOfDate 截止日期
     * @return 外汇风险敞口数据
     */
    @Override
    public ForeignExchangeExposureVO getForeignExchangeExposure(String currency, String asOfDate) {
        return advancedFundManagementService.getForeignExchangeExposure(currency, asOfDate);
    }
    
    /**
     * 执行外汇对冲操作 - 委托给高级资金管理服务
     * @param currency 币种
     * @param amount 金额
     * @param hedgeType 对冲类型（远期、期权等）
     * @param rate 汇率
     * @return 结果
     */
    @Override
    public int executeFxHedging(String currency, Double amount, String hedgeType, Double rate) {
        return advancedFundManagementService.executeFxHedging(currency, amount, hedgeType, rate);
    }
    
    /**
     * 获取投资组合状态 - 委托给高级资金管理服务
     * @param portfolioId 投资组合ID
     * @return 投资组合状态数据
     */
    @Override
    public InvestmentPortfolioStatusVO getInvestmentPortfolioStatus(String portfolioId) {
        return advancedFundManagementService.getInvestmentPortfolioStatus(portfolioId);
    }
    
    /**
     * 执行投资交易 - 委托给高级资金管理服务
     * @param portfolioId 投资组合ID
     * @param instrumentId 投资工具ID
     * @param tradeType 交易类型（买入、卖出）
     * @param amount 金额
     * @param price 价格
     * @return 结果
     */
    @Override
    public int executeInvestmentTransaction(String portfolioId, String instrumentId, String tradeType, Double amount, Double price) {
        return advancedFundManagementService.executeInvestmentTransaction(portfolioId, instrumentId, tradeType, amount, price);
    }
}