package com.chennian.storytelling.dao;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

/**
 * 财务分析数据访问接口
 * @author chen
 * @date 2023/6/15
 */
@Mapper
public interface FinancialAnalysisMapper {
    
    /**
     * 获取财务仪表盘数据
     * @return 财务仪表盘数据
     */
    Map<String, Object> getFinancialDashboardData();
    
    /**
     * 获取收入概览数据
     * @return 收入概览数据
     */
    Map<String, Object> getRevenueOverviewData();
    
    /**
     * 获取利润概览数据
     * @return 利润概览数据
     */
    Map<String, Object> getProfitOverviewData();
    
    /**
     * 获取现金流概览数据
     * @return 现金流概览数据
     */
    Map<String, Object> getCashflowOverviewData();
    
    /**
     * 获取费用概览数据
     * @return 费用概览数据
     */
    Map<String, Object> getExpensesOverviewData();
    
    /**
     * 获取资产负债概览数据
     * @return 资产负债概览数据
     */
    Map<String, Object> getBalanceSheetOverviewData();
    
    /**
     * 获取财务比率分析数据
     * @param period 期间
     * @return 财务比率分析数据
     */
    Map<String, Object> getFinancialRatioAnalysisData(@Param("period") String period);
    
    /**
     * 获取盈利能力比率数据
     * @param period 期间
     * @return 盈利能力比率数据
     */
    Map<String, Object> getProfitabilityRatiosData(@Param("period") String period);
    
    /**
     * 获取偿债能力比率数据
     * @param period 期间
     * @return 偿债能力比率数据
     */
    Map<String, Object> getSolvencyRatiosData(@Param("period") String period);
    
    /**
     * 获取营运能力比率数据
     * @param period 期间
     * @return 营运能力比率数据
     */
    Map<String, Object> getOperationRatiosData(@Param("period") String period);
    
    /**
     * 获取发展能力比率数据
     * @param period 期间
     * @return 发展能力比率数据
     */
    Map<String, Object> getGrowthRatiosData(@Param("period") String period);
    
    /**
     * 获取现金流比率数据
     * @param period 期间
     * @return 现金流比率数据
     */
    Map<String, Object> getCashFlowRatiosData(@Param("period") String period);
    
    /**
     * 获取财务趋势分析数据 - 收入趋势
     * @param startPeriod 开始期间
     * @param endPeriod 结束期间
     * @return 收入趋势数据列表
     */
    List<Map<String, Object>> getRevenueTrendData(@Param("startPeriod") String startPeriod, @Param("endPeriod") String endPeriod);
    
    /**
     * 获取财务趋势分析数据 - 利润趋势
     * @param startPeriod 开始期间
     * @param endPeriod 结束期间
     * @return 利润趋势数据列表
     */
    List<Map<String, Object>> getProfitTrendData(@Param("startPeriod") String startPeriod, @Param("endPeriod") String endPeriod);
    
    /**
     * 获取财务趋势分析数据 - 费用趋势
     * @param startPeriod 开始期间
     * @param endPeriod 结束期间
     * @return 费用趋势数据列表
     */
    List<Map<String, Object>> getExpenseTrendData(@Param("startPeriod") String startPeriod, @Param("endPeriod") String endPeriod);
    
    /**
     * 获取审计跟踪数据
     * @param module 模块
     * @param startDate 开始日期
     * @param endDate 结束日期
     * @return 审计跟踪数据列表
     */
    List<Map<String, Object>> getAuditTrailData(@Param("module") String module, 
                                              @Param("startDate") String startDate, 
                                              @Param("endDate") String endDate);
}