package com.chennian.storytelling.service.impl;

import com.chennian.storytelling.dao.AdvancedFundManagementMapper;
import com.chennian.storytelling.service.AdvancedFundManagementService;
import com.chennian.storytelling.bean.vo.CashPoolStatusVO;
import com.chennian.storytelling.bean.vo.ForeignExchangeExposureVO;
import com.chennian.storytelling.bean.vo.InvestmentPortfolioStatusVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * 高级资金管理服务实现类
 * 负责资金池管理、外汇风险管理、投资组合管理等高级功能
 * @author chen
 * @date 2023/6/15
 */
@Service
public class AdvancedFundManagementServiceImpl implements AdvancedFundManagementService {

    @Autowired
    private AdvancedFundManagementMapper advancedFundManagementMapper;

    /**
     * 获取资金池状态
     * @param poolId 资金池ID
     * @return 资金池状态数据
     */
    @Override
    public CashPoolStatusVO getCashPoolStatus(String poolId) {
        // 从数据库获取资金池状态
        return advancedFundManagementMapper.getCashPoolStatus(poolId);
    }

    /**
     * 资金池内部调拨
     * @param fromSubAccount 源子账户
     * @param toSubAccount 目标子账户
     * @param amount 金额
     * @param remarks 备注
     * @return 结果
     */
    @Override
    public int allocateCashPoolFunds(String fromSubAccount, String toSubAccount, Double amount, String remarks) {
        // 检查子账户是否存在
        if (fromSubAccount == null || fromSubAccount.isEmpty() || toSubAccount == null || toSubAccount.isEmpty()) {
            return -1; // 子账户不存在
        }
        
        // 检查金额是否有效
        if (amount == null || amount <= 0) {
            return -2; // 金额无效
        }
        
        // 检查源子账户余额是否充足
        double fromSubAccountBalance = advancedFundManagementMapper.getSubAccountBalance(fromSubAccount);
        if (fromSubAccountBalance < amount) {
            return -3; // 余额不足
        }
        
        // 执行调拨操作 - 调用数据库
        return advancedFundManagementMapper.allocateCashPoolFunds(fromSubAccount, toSubAccount, amount, remarks);
    }

    /**
     * 获取外汇风险敞口
     * @param currency 币种
     * @param asOfDate 截止日期
     * @return 外汇风险敞口数据
     */
    @Override
    public ForeignExchangeExposureVO getForeignExchangeExposure(String currency, String asOfDate) {
        // 从数据库获取外汇风险敞口
        return advancedFundManagementMapper.getForeignExchangeExposure(currency, asOfDate);
    }

    /**
     * 执行外汇对冲操作
     * @param currency 币种
     * @param amount 金额
     * @param hedgeType 对冲类型（远期、期权等）
     * @param rate 汇率
     * @return 结果
     */
    @Override
    public int executeFxHedging(String currency, Double amount, String hedgeType, Double rate) {
        // 检查参数是否有效
        if (currency == null || currency.isEmpty()) {
            return -1; // 币种无效
        }
        
        if (amount == null || amount <= 0) {
            return -2; // 金额无效
        }
        
        if (hedgeType == null || hedgeType.isEmpty()) {
            return -3; // 对冲类型无效
        }
        
        if (rate == null || rate <= 0) {
            return -4; // 汇率无效
        }
        
        // 检查对冲类型是否支持
        if (!hedgeType.equals("远期") && !hedgeType.equals("期权") && !hedgeType.equals("掉期")) {
            return -5; // 不支持的对冲类型
        }
        
        // 执行对冲操作 - 调用数据库
        return advancedFundManagementMapper.executeFxHedging(currency, amount, hedgeType, rate);
    }

    /**
     * 获取投资组合状态
     * @param portfolioId 投资组合ID
     * @return 投资组合状态数据
     */
    @Override
    public InvestmentPortfolioStatusVO getInvestmentPortfolioStatus(String portfolioId) {
        // 从数据库获取投资组合状态
        return advancedFundManagementMapper.getInvestmentPortfolioStatus(portfolioId);
    }

    /**
     * 执行投资交易
     * @param portfolioId 投资组合ID
     * @param instrumentId 投资工具ID
     * @param tradeType 交易类型（买入、卖出）
     * @param amount 金额
     * @param price 价格
     * @return 结果
     */
    @Override
    public int executeInvestmentTransaction(String portfolioId, String instrumentId, String tradeType, Double amount, Double price) {
        // 检查参数是否有效
        if (portfolioId == null || portfolioId.isEmpty()) {
            return -1; // 投资组合ID无效
        }
        
        if (instrumentId == null || instrumentId.isEmpty()) {
            return -2; // 投资工具ID无效
        }
        
        if (tradeType == null || tradeType.isEmpty()) {
            return -3; // 交易类型无效
        }
        
        if (amount == null || amount <= 0) {
            return -4; // 金额无效
        }
        
        if (price == null || price <= 0) {
            return -5; // 价格无效
        }
        
        // 检查交易类型是否支持
        if (!tradeType.equals("买入") && !tradeType.equals("卖出")) {
            return -6; // 不支持的交易类型
        }
        
        // 如果是卖出，检查持仓是否足够
        if (tradeType.equals("卖出")) {
            double holding = advancedFundManagementMapper.getInstrumentHolding(portfolioId, instrumentId);
            if (holding < amount) {
                return -7; // 持仓不足
            }
        }
        
        // 如果是买入，检查可用资金是否足够
        if (tradeType.equals("买入")) {
            double availableFunds = advancedFundManagementMapper.getPortfolioAvailableFunds(portfolioId);
            if (availableFunds < amount * price) {
                return -8; // 资金不足
            }
        }
        
        // 执行交易操作 - 调用数据库
        return advancedFundManagementMapper.executeInvestmentTransaction(portfolioId, instrumentId, tradeType, amount, price);
    }
    

}