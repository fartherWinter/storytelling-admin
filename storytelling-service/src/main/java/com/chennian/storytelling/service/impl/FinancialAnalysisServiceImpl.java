package com.chennian.storytelling.service.impl;

import com.chennian.storytelling.dao.FinancialAnalysisMapper;
import com.chennian.storytelling.service.FinancialAnalysisService;
import com.chennian.storytelling.vo.AuditTrailVO;
import com.chennian.storytelling.vo.FinancialDashboardVO;
import com.chennian.storytelling.vo.FinancialRatioAnalysisVO;
import com.chennian.storytelling.vo.FinancialTrendAnalysisVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * 财务分析与决策支持服务实现类
 * 负责财务仪表盘、财务比率分析、财务趋势分析和审计跟踪等功能
 * @author chen
 * @date 2023/6/15
 */
@Service
public class FinancialAnalysisServiceImpl implements FinancialAnalysisService {

    @Autowired
    private FinancialAnalysisMapper financialAnalysisMapper;

    /**
     * 获取财务仪表盘
     * @return 财务仪表盘数据
     */
    @Override
    public FinancialDashboardVO getFinancialDashboard() {
        FinancialDashboardVO dashboardVO = new FinancialDashboardVO();
        
        // 从数据库获取收入概览数据
        Map<String, Object> revenueData = financialAnalysisMapper.getRevenueOverviewData();
        FinancialDashboardVO.RevenueOverviewVO revenueVO = new FinancialDashboardVO.RevenueOverviewVO();
        if (revenueData != null) {
            revenueVO.setCurrentMonthRevenue((Double) revenueData.get("本月收入"));
            revenueVO.setLastMonthRevenue((Double) revenueData.get("上月收入"));
            revenueVO.setYearOnYearGrowth((String) revenueData.get("同比增长"));
            revenueVO.setAnnualTargetCompletionRate((String) revenueData.get("年度目标完成率"));
        }
        dashboardVO.setRevenueOverview(revenueVO);
        
        // 从数据库获取利润概览数据
        Map<String, Object> profitData = financialAnalysisMapper.getProfitOverviewData();
        FinancialDashboardVO.ProfitOverviewVO profitVO = new FinancialDashboardVO.ProfitOverviewVO();
        if (profitData != null) {
            profitVO.setCurrentMonthProfit((Double) profitData.get("本月利润"));
            profitVO.setLastMonthProfit((Double) profitData.get("上月利润"));
            profitVO.setYearOnYearGrowth((String) profitData.get("同比增长"));
            profitVO.setGrossProfitMargin((String) profitData.get("毛利率"));
            profitVO.setNetProfitMargin((String) profitData.get("净利率"));
        }
        dashboardVO.setProfitOverview(profitVO);
        
        // 从数据库获取现金流概览数据
        Map<String, Object> cashflowData = financialAnalysisMapper.getCashflowOverviewData();
        FinancialDashboardVO.CashflowOverviewVO cashflowVO = new FinancialDashboardVO.CashflowOverviewVO();
        if (cashflowData != null) {
            cashflowVO.setOperatingCashflow((Double) cashflowData.get("经营活动现金流"));
            cashflowVO.setInvestingCashflow((Double) cashflowData.get("投资活动现金流"));
            cashflowVO.setFinancingCashflow((Double) cashflowData.get("筹资活动现金流"));
            cashflowVO.setNetCashIncrease((Double) cashflowData.get("现金净增加额"));
        }
        dashboardVO.setCashflowOverview(cashflowVO);
        
        // 从数据库获取费用概览数据
        Map<String, Object> expensesData = financialAnalysisMapper.getExpensesOverviewData();
        FinancialDashboardVO.ExpensesOverviewVO expensesVO = new FinancialDashboardVO.ExpensesOverviewVO();
        if (expensesData != null) {
            expensesVO.setSellingExpenses((Double) expensesData.get("销售费用"));
            expensesVO.setAdministrativeExpenses((Double) expensesData.get("管理费用"));
            expensesVO.setResearchExpenses((Double) expensesData.get("研发费用"));
            expensesVO.setFinancialExpenses((Double) expensesData.get("财务费用"));
        }
        dashboardVO.setExpensesOverview(expensesVO);
        
        // 从数据库获取资产负债概览数据
        Map<String, Object> balanceSheetData = financialAnalysisMapper.getBalanceSheetOverviewData();
        FinancialDashboardVO.BalanceSheetOverviewVO balanceSheetVO = new FinancialDashboardVO.BalanceSheetOverviewVO();
        if (balanceSheetData != null) {
            balanceSheetVO.setTotalAssets((Double) balanceSheetData.get("总资产"));
            balanceSheetVO.setTotalLiabilities((Double) balanceSheetData.get("总负债"));
            balanceSheetVO.setOwnersEquity((Double) balanceSheetData.get("所有者权益"));
            balanceSheetVO.setDebtToAssetRatio((String) balanceSheetData.get("资产负债率"));
        }
        dashboardVO.setBalanceSheetOverview(balanceSheetVO);
        
        return dashboardVO;
    }

    /**
     * 获取财务比率分析
     * @param period 期间
     * @return 财务比率分析数据
     */
    @Override
    public FinancialRatioAnalysisVO getFinancialRatioAnalysis(String period) {
        FinancialRatioAnalysisVO ratioAnalysisVO = new FinancialRatioAnalysisVO();
        ratioAnalysisVO.setPeriod(period);
        
        // 从数据库获取盈利能力比率数据
        Map<String, Object> profitabilityData = financialAnalysisMapper.getProfitabilityRatiosData(period);
        FinancialRatioAnalysisVO.ProfitabilityRatiosVO profitabilityRatiosVO = new FinancialRatioAnalysisVO.ProfitabilityRatiosVO();
        if (profitabilityData != null) {
            profitabilityRatiosVO.setGrossProfitMargin((String) profitabilityData.get("毛利率"));
            profitabilityRatiosVO.setNetProfitMargin((String) profitabilityData.get("净利率"));
            profitabilityRatiosVO.setReturnOnEquity((String) profitabilityData.get("净资产收益率"));
            profitabilityRatiosVO.setReturnOnAssets((String) profitabilityData.get("总资产收益率"));
            profitabilityRatiosVO.setOperatingProfitMargin((String) profitabilityData.get("营业利润率"));
        }
        ratioAnalysisVO.setProfitabilityRatios(profitabilityRatiosVO);
        
        // 从数据库获取偿债能力比率数据
        Map<String, Object> solvencyData = financialAnalysisMapper.getSolvencyRatiosData(period);
        FinancialRatioAnalysisVO.SolvencyRatiosVO solvencyRatiosVO = new FinancialRatioAnalysisVO.SolvencyRatiosVO();
        if (solvencyData != null) {
            solvencyRatiosVO.setDebtToAssetRatio((String) solvencyData.get("资产负债率"));
            solvencyRatiosVO.setCurrentRatio((Double) solvencyData.get("流动比率"));
            solvencyRatiosVO.setQuickRatio((Double) solvencyData.get("速动比率"));
            solvencyRatiosVO.setInterestCoverageRatio((Double) solvencyData.get("利息保障倍数"));
        }
        ratioAnalysisVO.setSolvencyRatios(solvencyRatiosVO);
        
        // 从数据库获取营运能力比率数据
        Map<String, Object> operationData = financialAnalysisMapper.getOperationRatiosData(period);
        FinancialRatioAnalysisVO.OperationRatiosVO operationRatiosVO = new FinancialRatioAnalysisVO.OperationRatiosVO();
        if (operationData != null) {
            operationRatiosVO.setAccountsReceivableTurnover((Double) operationData.get("应收账款周转率"));
            operationRatiosVO.setInventoryTurnover((Double) operationData.get("存货周转率"));
            operationRatiosVO.setTotalAssetsTurnover((Double) operationData.get("总资产周转率"));
            operationRatiosVO.setFixedAssetsTurnover((Double) operationData.get("固定资产周转率"));
        }
        ratioAnalysisVO.setOperationRatios(operationRatiosVO);
        
        // 从数据库获取发展能力比率数据
        Map<String, Object> growthData = financialAnalysisMapper.getGrowthRatiosData(period);
        FinancialRatioAnalysisVO.GrowthRatiosVO growthRatiosVO = new FinancialRatioAnalysisVO.GrowthRatiosVO();
        if (growthData != null) {
            growthRatiosVO.setRevenueGrowthRate((String) growthData.get("营业收入增长率"));
            growthRatiosVO.setNetProfitGrowthRate((String) growthData.get("净利润增长率"));
            growthRatiosVO.setTotalAssetsGrowthRate((String) growthData.get("总资产增长率"));
            growthRatiosVO.setNetAssetsGrowthRate((String) growthData.get("净资产增长率"));
        }
        ratioAnalysisVO.setGrowthRatios(growthRatiosVO);
        
        // 从数据库获取现金流比率数据
        Map<String, Object> cashFlowData = financialAnalysisMapper.getCashFlowRatiosData(period);
        FinancialRatioAnalysisVO.CashFlowRatiosVO cashFlowRatiosVO = new FinancialRatioAnalysisVO.CashFlowRatiosVO();
        if (cashFlowData != null) {
            cashFlowRatiosVO.setOperatingCashFlowRatio((Double) cashFlowData.get("经营现金流量比率"));
            cashFlowRatiosVO.setCashFlowToLiabilitiesRatio((Double) cashFlowData.get("现金流量负债比率"));
            cashFlowRatiosVO.setCashFlowToNetProfitRatio((Double) cashFlowData.get("现金流量净利润比率"));
            cashFlowRatiosVO.setTotalAssetsCashRecoveryRate((String) cashFlowData.get("全部资产现金回收率"));
        }
        ratioAnalysisVO.setCashFlowRatios(cashFlowRatiosVO);
        
        return ratioAnalysisVO;
    }

    /**
     * 获取财务趋势分析
     * @param startPeriod 开始期间
     * @param endPeriod 结束期间
     * @return 财务趋势分析数据
     */
    @Override
    public FinancialTrendAnalysisVO getFinancialTrendAnalysis(String startPeriod, String endPeriod) {
        FinancialTrendAnalysisVO trendAnalysisVO = new FinancialTrendAnalysisVO();
        trendAnalysisVO.setStartPeriod(startPeriod);
        trendAnalysisVO.setEndPeriod(endPeriod);
        
        // 从数据库获取收入趋势数据
        List<Map<String, Object>> revenueTrendData = financialAnalysisMapper.getRevenueTrendData(startPeriod, endPeriod);
        List<FinancialTrendAnalysisVO.RevenueTrendVO> revenueTrendList = new ArrayList<>();
        if (revenueTrendData != null && !revenueTrendData.isEmpty()) {
            for (Map<String, Object> data : revenueTrendData) {
                FinancialTrendAnalysisVO.RevenueTrendVO revenueTrendVO = new FinancialTrendAnalysisVO.RevenueTrendVO();
                revenueTrendVO.setPeriod((String) data.get("period"));
                revenueTrendVO.setValue((Double) data.get("value"));
                revenueTrendList.add(revenueTrendVO);
            }
        }
        trendAnalysisVO.setRevenueTrend(revenueTrendList);
        
        // 从数据库获取利润趋势数据
        List<Map<String, Object>> profitTrendData = financialAnalysisMapper.getProfitTrendData(startPeriod, endPeriod);
        List<FinancialTrendAnalysisVO.ProfitTrendVO> profitTrendList = new ArrayList<>();
        if (profitTrendData != null && !profitTrendData.isEmpty()) {
            for (Map<String, Object> data : profitTrendData) {
                FinancialTrendAnalysisVO.ProfitTrendVO profitTrendVO = new FinancialTrendAnalysisVO.ProfitTrendVO();
                profitTrendVO.setPeriod((String) data.get("period"));
                profitTrendVO.setValue((Double) data.get("value"));
                profitTrendList.add(profitTrendVO);
            }
        }
        trendAnalysisVO.setProfitTrend(profitTrendList);
        
        // 从数据库获取费用趋势数据
        List<Map<String, Object>> expenseTrendData = financialAnalysisMapper.getExpenseTrendData(startPeriod, endPeriod);
        List<FinancialTrendAnalysisVO.ExpenseTrendVO> expenseTrendList = new ArrayList<>();
        if (expenseTrendData != null && !expenseTrendData.isEmpty()) {
            for (Map<String, Object> data : expenseTrendData) {
                FinancialTrendAnalysisVO.ExpenseTrendVO expenseTrendVO = new FinancialTrendAnalysisVO.ExpenseTrendVO();
                expenseTrendVO.setPeriod((String) data.get("period"));
                expenseTrendVO.setSellingExpenses((Double) data.get("销售费用"));
                expenseTrendVO.setAdministrativeExpenses((Double) data.get("管理费用"));
                expenseTrendVO.setResearchExpenses((Double) data.get("研发费用"));
                expenseTrendVO.setFinancialExpenses((Double) data.get("财务费用"));
                expenseTrendList.add(expenseTrendVO);
            }
        }
        trendAnalysisVO.setExpenseTrend(expenseTrendList);
        
        return trendAnalysisVO;
    }

    /**
     * 获取审计跟踪
     * @param module 模块
     * @param startDate 开始日期
     * @param endDate 结束日期
     * @return 审计跟踪数据
     */
    @Override
    public List<AuditTrailVO> getAuditTrail(String module, String startDate, String endDate) {
        // 从数据库获取审计跟踪数据
        List<Map<String, Object>> auditTrailData = financialAnalysisMapper.getAuditTrailData(module, startDate, endDate);
        List<AuditTrailVO> auditTrailList = new ArrayList<>();
        
        if (auditTrailData != null && !auditTrailData.isEmpty()) {
            for (Map<String, Object> data : auditTrailData) {
                AuditTrailVO auditTrailVO = new AuditTrailVO();
                auditTrailVO.setId((Integer) data.get("id"));
                auditTrailVO.setModule((String) data.get("module"));
                auditTrailVO.setOperation((String) data.get("operation"));
                auditTrailVO.setOperator((String) data.get("operator"));
                auditTrailVO.setOperateTime((String) data.get("operateTime"));
                auditTrailVO.setIp((String) data.get("ip"));
                auditTrailList.add(auditTrailVO);
            }
        }
        
        return auditTrailList;
    }
}