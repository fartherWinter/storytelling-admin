package com.chennian.storytelling.service;

import com.chennian.storytelling.bean.vo.BalanceSheetVO;
import com.chennian.storytelling.bean.vo.CashFlowStatementVO;
import com.chennian.storytelling.bean.vo.ConsolidatedReportVO.ConsolidatedBalanceSheetVO;
import com.chennian.storytelling.bean.vo.ConsolidatedReportVO.ConsolidatedCashFlowStatementVO;
import com.chennian.storytelling.bean.vo.ConsolidatedReportVO.ConsolidatedIncomeStatementVO;
import com.chennian.storytelling.bean.vo.IncomeStatementVO;
import com.chennian.storytelling.bean.vo.ProfitAnalysisVO;

import java.util.List;

/**
 * 财务报表服务接口
 * 负责资产负债表、利润表、现金流量表和合并报表等功能
 * @author chen
 * @date 2023/6/15
 */
public interface FinancialReportService {
    
    /**
     * 获取资产负债表
     * @param period 期间
     * @return 资产负债表数据
     */
    BalanceSheetVO getBalanceSheet(String period);
    
    /**
     * 获取利润表
     * @param period 期间
     * @return 利润表数据
     */
    IncomeStatementVO getIncomeStatement(String period);
    
    /**
     * 获取现金流量表
     * @param period 期间
     * @return 现金流量表数据
     */
    CashFlowStatementVO getCashFlowStatement(String period);
    
    /**
     * 获取利润分析
     * @param period 期间
     * @param dimension 分析维度
     * @return 利润分析数据
     */
    ProfitAnalysisVO getProfitAnalysis(String period, String dimension);
    
    /**
     * 获取合并资产负债表
     * @param period 期间
     * @param orgIds 组织ID列表
     * @return 合并资产负债表数据
     */
    ConsolidatedBalanceSheetVO getConsolidatedBalanceSheet(String period, List<String> orgIds);
    
    /**
     * 获取合并利润表
     * @param period 期间
     * @param orgIds 组织ID列表
     * @return 合并利润表数据
     */
    ConsolidatedIncomeStatementVO getConsolidatedIncomeStatement(String period, List<String> orgIds);
    
    /**
     * 获取合并现金流量表
     * @param period 期间
     * @param orgIds 组织ID列表
     * @return 合并现金流量表数据
     */
    ConsolidatedCashFlowStatementVO getConsolidatedCashFlowStatement(String period, List<String> orgIds);
}