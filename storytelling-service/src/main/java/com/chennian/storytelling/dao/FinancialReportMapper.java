package com.chennian.storytelling.dao;

import com.chennian.storytelling.bean.vo.BalanceSheetVO;
import com.chennian.storytelling.bean.vo.CashFlowStatementVO;
import com.chennian.storytelling.bean.vo.ConsolidatedReportVO.ConsolidatedBalanceSheetVO;
import com.chennian.storytelling.bean.vo.ConsolidatedReportVO.ConsolidatedCashFlowStatementVO;
import com.chennian.storytelling.bean.vo.ConsolidatedReportVO.ConsolidatedIncomeStatementVO;
import com.chennian.storytelling.bean.vo.IncomeStatementVO;
import com.chennian.storytelling.bean.vo.ProfitAnalysisVO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 财务报表数据访问接口
 * @author chen
 * @date 2023/6/15
 */
@Mapper
public interface FinancialReportMapper {
    
    /**
     * 获取资产负债表数据
     * @param period 期间
     * @return 资产负债表数据
     */
    BalanceSheetVO getBalanceSheet(@Param("period") String period);
    
    /**
     * 获取利润表数据
     * @param period 期间
     * @return 利润表数据
     */
    IncomeStatementVO getIncomeStatement(@Param("period") String period);
    
    /**
     * 获取现金流量表数据
     * @param period 期间
     * @return 现金流量表数据
     */
    CashFlowStatementVO getCashFlowStatement(@Param("period") String period);
    
    /**
     * 获取产品维度利润分析数据
     * @param period 期间
     * @return 产品维度利润分析数据
     */
    List<ProfitAnalysisVO.ProductProfit> getProductProfitAnalysis(@Param("period") String period);
    
    /**
     * 获取地区维度利润分析数据
     * @param period 期间
     * @return 地区维度利润分析数据
     */
    List<ProfitAnalysisVO.RegionProfit> getRegionProfitAnalysis(@Param("period") String period);
    
    /**
     * 获取客户维度利润分析数据
     * @param period 期间
     * @return 客户维度利润分析数据
     */
    List<ProfitAnalysisVO.CustomerProfit> getCustomerProfitAnalysis(@Param("period") String period);
    
    /**
     * 获取合并资产负债表数据
     * @param period 期间
     * @param orgIds 组织ID列表
     * @return 合并资产负债表数据
     */
    ConsolidatedBalanceSheetVO getConsolidatedBalanceSheet(@Param("period") String period, @Param("orgIds") List<String> orgIds);
    
    /**
     * 获取合并利润表数据
     * @param period 期间
     * @param orgIds 组织ID列表
     * @return 合并利润表数据
     */
    ConsolidatedIncomeStatementVO getConsolidatedIncomeStatement(@Param("period") String period, @Param("orgIds") List<String> orgIds);
    
    /**
     * 获取合并现金流量表数据
     * @param period 期间
     * @param orgIds 组织ID列表
     * @return 合并现金流量表数据
     */
    ConsolidatedCashFlowStatementVO getConsolidatedCashFlowStatement(@Param("period") String period, @Param("orgIds") List<String> orgIds);
}