package com.chennian.storytelling.dao;

import com.chennian.storytelling.bean.vo.CashPoolStatusVO;
import com.chennian.storytelling.bean.vo.ForeignExchangeExposureVO;
import com.chennian.storytelling.bean.vo.InvestmentPortfolioStatusVO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

/**
 * 高级资金管理Mapper接口
 *
 * @author chen
 * @date 2023/6/15
 */
@Mapper
public interface AdvancedFundManagementMapper {

    /**
     * 获取资金池状态
     *
     * @param poolId 资金池ID
     * @return 资金池状态数据
     */
    CashPoolStatusVO getCashPoolStatus(@Param("poolId") String poolId);

    /**
     * 资金池内部调拨
     *
     * @param fromSubAccount 源子账户
     * @param toSubAccount   目标子账户
     * @param amount         金额
     * @param remarks        备注
     * @return 影响行数
     */
    int allocateCashPoolFunds(@Param("fromSubAccount") String fromSubAccount,
                              @Param("toSubAccount") String toSubAccount,
                              @Param("amount") Double amount,
                              @Param("remarks") String remarks);

    /**
     * 获取外汇风险敞口
     *
     * @param currency 币种
     * @param asOfDate 截止日期
     * @return 外汇风险敞口数据
     */
    ForeignExchangeExposureVO getForeignExchangeExposure(@Param("currency") String currency,
                                                         @Param("asOfDate") String asOfDate);

    /**
     * 获取外汇敞口明细
     *
     * @param currency 币种
     * @param asOfDate 截止日期
     */
    ForeignExchangeExposureVO.ExposureDetails getExposureDetails(@Param("currency") String currency,
                                                                 @Param("asOfDate") String asOfDate);

    /**
     * 获取风险分析
     *
     * @param currency 币种
     * @param asOfDate 截止日期
     */
    ForeignExchangeExposureVO.RiskAnalysis getRiskAnalysis(@Param("currency") String currency,
                                                           @Param("asOfDate") String asOfDate);

    /**
     * 执行外汇对冲操作
     *
     * @param currency  币种
     * @param amount    金额
     * @param hedgeType 对冲类型（远期、期权等）
     * @param rate      汇率
     * @return 影响行数
     */
    int executeFxHedging(@Param("currency") String currency,
                         @Param("amount") Double amount,
                         @Param("hedgeType") String hedgeType,
                         @Param("rate") Double rate);

    /**
     * 获取投资组合状态
     *
     * @param portfolioId 投资组合ID
     * @return 投资组合状态数据
     */
    InvestmentPortfolioStatusVO getInvestmentPortfolioStatus(@Param("portfolioId") String portfolioId);

    /**
     * 执行投资交易
     *
     * @param portfolioId  投资组合ID
     * @param instrumentId 投资工具ID
     * @param tradeType    交易类型（买入、卖出）
     * @param amount       金额
     * @param price        价格
     * @return 影响行数
     */
    int executeInvestmentTransaction(@Param("portfolioId") String portfolioId,
                                     @Param("instrumentId") String instrumentId,
                                     @Param("tradeType") String tradeType,
                                     @Param("amount") Double amount,
                                     @Param("price") Double price);

    /**
     * 获取子账户余额
     *
     * @param subAccountId 子账户ID
     * @return 子账户余额
     */
    double getSubAccountBalance(@Param("subAccountId") String subAccountId);

    /**
     * 获取投资工具持仓
     *
     * @param portfolioId  投资组合ID
     * @param instrumentId 投资工具ID
     * @return 持仓金额
     */
    double getInstrumentHolding(@Param("portfolioId") String portfolioId,
                                @Param("instrumentId") String instrumentId);

    /**
     * 获取投资组合可用资金
     *
     * @param portfolioId 投资组合ID
     * @return 可用资金
     */
    double getPortfolioAvailableFunds(@Param("portfolioId") String portfolioId);
}