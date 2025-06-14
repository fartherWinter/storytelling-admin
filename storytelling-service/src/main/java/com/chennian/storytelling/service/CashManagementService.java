package com.chennian.storytelling.service;

import com.chennian.storytelling.bean.vo.CashPoolStatusVO;
import com.chennian.storytelling.bean.vo.ForeignExchangeExposureVO;
import com.chennian.storytelling.bean.vo.InvestmentPortfolioStatusVO;

import java.util.List;
import java.util.Map;

/**
 * 现金与资金管理服务接口
 * 负责银行对账、现金流预测、资金转账、资金池管理、外汇风险管理、投资组合管理等功能
 * 该接口继承了基础现金管理服务和高级资金管理服务接口，以保持向后兼容性
 * @author chen
 * @date 2023/6/15
 */
public interface CashManagementService extends BasicCashManagementService, AdvancedFundManagementService {
    
    // 该接口通过继承BasicCashManagementService和AdvancedFundManagementService获得所有方法
    // 不需要重复定义方法
}