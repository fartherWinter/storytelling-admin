package com.chennian.storytelling.service;

import com.chennian.storytelling.bean.vo.AuditTrailVO;
import com.chennian.storytelling.bean.vo.FinancialDashboardVO;
import com.chennian.storytelling.bean.vo.FinancialRatioAnalysisVO;
import com.chennian.storytelling.bean.vo.FinancialTrendAnalysisVO;

import java.util.List;

/**
 * 财务分析与决策支持服务接口
 * 负责财务仪表盘、财务比率分析、财务趋势分析和审计跟踪等功能
 * @author chen
 * @date 2023/6/15
 */
public interface FinancialAnalysisService {
    
    /**
     * 获取财务仪表盘
     * @return 财务仪表盘数据
     */
    FinancialDashboardVO getFinancialDashboard();
    
    /**
     * 获取财务比率分析
     * @param period 期间
     * @return 财务比率分析数据
     */
    FinancialRatioAnalysisVO getFinancialRatioAnalysis(String period);
    
    /**
     * 获取财务趋势分析
     * @param startPeriod 开始期间
     * @param endPeriod 结束期间
     * @return 财务趋势分析数据
     */
    FinancialTrendAnalysisVO getFinancialTrendAnalysis(String startPeriod, String endPeriod);
    
    /**
     * 获取审计跟踪
     * @param module 模块
     * @param startDate 开始日期
     * @param endDate 结束日期
     * @return 审计跟踪数据
     */
    List<AuditTrailVO> getAuditTrail(String module, String startDate, String endDate);
}