package com.chennian.storytelling.service.impl;

import com.chennian.storytelling.dao.FinancialReportMapper;
import com.chennian.storytelling.service.FinancialReportService;
import com.chennian.storytelling.vo.BalanceSheetVO;
import com.chennian.storytelling.vo.CashFlowStatementVO;
import com.chennian.storytelling.vo.ConsolidatedReportVO.ConsolidatedBalanceSheetVO;
import com.chennian.storytelling.vo.ConsolidatedReportVO.ConsolidatedCashFlowStatementVO;
import com.chennian.storytelling.vo.ConsolidatedReportVO.ConsolidatedIncomeStatementVO;
import com.chennian.storytelling.vo.IncomeStatementVO;
import com.chennian.storytelling.vo.ProfitAnalysisVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * 财务报表服务实现类
 * 负责资产负债表、利润表、现金流量表和合并报表等功能
 * @author chen
 * @date 2023/6/15
 */
@Service
public class FinancialReportServiceImpl implements FinancialReportService {

    @Autowired
    private FinancialReportMapper financialReportMapper;

    /**
     * 获取资产负债表
     * @param period 期间
     * @return 资产负债表数据
     */
    @Override
    public BalanceSheetVO getBalanceSheet(String period) {
        // 从数据库获取资产负债表数据
        return financialReportMapper.getBalanceSheet(period);
    }
    
    /**
     * 获取利润表
     * @param period 期间
     * @return 利润表数据
     */
    @Override
    public IncomeStatementVO getIncomeStatement(String period) {
        // 从数据库获取利润表数据
        return financialReportMapper.getIncomeStatement(period);
    }
    
    /**
     * 获取现金流量表
     * @param period 期间
     * @return 现金流量表数据
     */
    @Override
    public CashFlowStatementVO getCashFlowStatement(String period) {
        // 从数据库获取现金流量表数据
        return financialReportMapper.getCashFlowStatement(period);
    }
    
    /**
     * 获取利润分析
     * @param period 期间
     * @param dimension 分析维度
     * @return 利润分析数据
     */
    @Override
    public ProfitAnalysisVO getProfitAnalysis(String period, String dimension) {
        ProfitAnalysisVO profitAnalysisVO = new ProfitAnalysisVO();
        profitAnalysisVO.setPeriod(period);
        profitAnalysisVO.setDimension(dimension);
        
        // 根据维度从数据库获取不同的利润分析数据
        if ("product".equals(dimension)) {
            // 按产品维度分析
            profitAnalysisVO.setProductProfits(financialReportMapper.getProductProfitAnalysis(period));
        } else if ("region".equals(dimension)) {
            // 按地区维度分析
            profitAnalysisVO.setRegionProfits(financialReportMapper.getRegionProfitAnalysis(period));
        } else if ("customer".equals(dimension)) {
            // 按客户维度分析
            profitAnalysisVO.setCustomerProfits(financialReportMapper.getCustomerProfitAnalysis(period));
        }
        
        return profitAnalysisVO;
    }
    
    /**
     * 获取合并资产负债表
     * @param period 期间
     * @param orgIds 组织ID列表
     * @return 合并资产负债表数据
     */
    @Override
    public ConsolidatedBalanceSheetVO getConsolidatedBalanceSheet(String period, List<String> orgIds) {
        // 从数据库获取合并资产负债表数据
        return financialReportMapper.getConsolidatedBalanceSheet(period, orgIds);
    }
    
    /**
     * 获取合并利润表
     * @param period 期间
     * @param orgIds 组织ID列表
     * @return 合并利润表数据
     */
    @Override
    public ConsolidatedIncomeStatementVO getConsolidatedIncomeStatement(String period, List<String> orgIds) {
        // 从数据库获取合并利润表数据
        return financialReportMapper.getConsolidatedIncomeStatement(period, orgIds);
    }
    
    /**
     * 获取合并现金流量表
     * @param period 期间
     * @param orgIds 组织ID列表
     * @return 合并现金流量表数据
     */
    @Override
    public ConsolidatedCashFlowStatementVO getConsolidatedCashFlowStatement(String period, List<String> orgIds) {
        // 从数据库获取合并现金流量表数据
        return financialReportMapper.getConsolidatedCashFlowStatement(period, orgIds);
    }
}