package com.chennian.storytelling.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.chennian.storytelling.bean.model.quality.QualityImprovement;
import com.chennian.storytelling.bean.model.quality.QualityInspection;
import com.chennian.storytelling.bean.model.quality.QualityIssue;
import com.chennian.storytelling.bean.model.quality.QualityStandard;
import com.chennian.storytelling.bean.vo.QualityStatisticsVO;
import com.chennian.storytelling.bean.vo.QualityTrendVO;
import com.chennian.storytelling.common.utils.PageParam;
import com.storytelling.common.core.domain.AjaxResult;

import java.util.List;
import java.util.Map;

/**
 * 质量管理服务接口
 * 
 * @author storytelling
 * @date 2024-01-01
 */
public interface QualityService {

    // ==================== 质量检验管理 ====================
    
    /**
     * 查询质量检验列表
     */
    IPage<QualityInspection> getInspectionList(QualityInspection inspection, PageParam<QualityInspection> page);
    
    /**
     * 根据ID查询质量检验信息
     */
    QualityInspection getInspectionById(Long id);
    
    /**
     * 新增质量检验
     */
    void addInspection(QualityInspection inspection);
    
    /**
     * 修改质量检验信息
     */
    void updateInspection(QualityInspection inspection);
    
    /**
     * 删除质量检验
     */
    void deleteInspection(Long[] ids);
    
    /**
     * 提交检验结果
     */
    AjaxResult submitInspectionResult(Long inspectionId, Integer result, String reason);
    
    /**
     * 检验统计分析
     */
    AjaxResult getInspectionStatistics(String startDate, String endDate, Integer inspectionType);
    
    /**
     * 合格率趋势分析
     */
    AjaxResult getQualificationRateTrend(String startDate, String endDate, Long productId);
    
    // ==================== 质量标准管理 ====================
    
    /**
     * 查询质量标准列表
     */
    IPage<QualityStandard> getStandardList(QualityStandard standard, PageParam<QualityStandard> page);
    
    /**
     * 根据ID查询质量标准信息
     */
    QualityStandard getStandardById(Long id);
    
    /**
     * 新增质量标准
     */
    void addStandard(QualityStandard standard);
    
    /**
     * 修改质量标准信息
     */
    void updateStandard(QualityStandard standard);
    
    /**
     * 删除质量标准
     */
    void deleteStandard(Long[] ids);
    
    /**
     * 标准版本管理
     */
    AjaxResult createStandardVersion(Long standardId, String newVersion);
    
    /**
     * 根据产品ID查询质量标准
     */
    List<QualityStandard> getStandardsByProductId(Long productId);
    
    // ==================== 质量问题管理 ====================
    
    /**
     * 查询质量问题列表
     */
    IPage<QualityIssue> getIssueList(PageParam<QualityIssue> page, QualityIssue issue);
    
    /**
     * 根据ID查询质量问题信息
     */
    QualityIssue getIssueById(Long id);
    
    /**
     * 新增质量问题
     */
    void addIssue(QualityIssue issue);
    
    /**
     * 修改质量问题信息
     */
    void updateIssue(QualityIssue issue);
    
    /**
     * 删除质量问题
     */
    void deleteIssue(Long[] ids);
    
    /**
     * 问题分配处理
     */
    void assignIssue(Long issueId, String handleResult, String remark);
    
    /**
     * 问题处理结果提交
     */
    AjaxResult submitIssueResult(Long issueId, String handleResult, String correctiveAction, String preventiveAction);
    
    /**
     * 问题关闭
     */
    AjaxResult closeIssue(Long issueId, String closeReason);
    
    /**
     * 质量问题统计
     */
    AjaxResult getIssueStatistics(String startDate, String endDate, Integer issueType, Integer issueLevel);
    
    // ==================== 质量改进管理 ====================
    
    /**
     * 查询质量改进列表
     */
    IPage<QualityImprovement> getImprovementList(PageParam<QualityImprovement> page, QualityImprovement improvement);
    
    /**
     * 根据ID查询质量改进信息
     */
    QualityImprovement getImprovementById(Long id);
    
    /**
     * 新增质量改进
     */
    void addImprovement(QualityImprovement improvement);
    
    /**
     * 修改质量改进信息
     */
    void updateImprovement(QualityImprovement improvement);
    
    /**
     * 删除质量改进
     */
    void deleteImprovement(Long[] ids);
    
    /**
     * 改进计划执行
     */
    void executeImprovement(Long improvementId);
    
    /**
     * 改进效果评估
     */
    void evaluateImprovement(Long improvementId, String executeResult, String remark);
    
    /**
     * 改进项目统计
     */
    AjaxResult getImprovementStatistics(String startDate, String endDate, Integer improvementType);
    
    // ==================== 质量统计分析 ====================
    
    /**
     * 质量综合分析
     */
    AjaxResult getQualityAnalysis(String startDate, String endDate);
    
    /**
     * 供应商质量分析
     */
    AjaxResult getSupplierQualityAnalysis(String startDate, String endDate, Long supplierId);
    
    /**
     * 产品质量分析
     */
    AjaxResult getProductQualityAnalysis(String startDate, String endDate, Long productId);
    
    /**
     * 质量成本分析
     */
    AjaxResult getQualityCostAnalysis(String startDate, String endDate);
    
    /**
     * 质量趋势分析
     */
    QualityTrendVO getQualityTrendAnalysis(String startDate, String endDate, String analysisType);
    
    /**
     * 质量统计
     */
    QualityStatisticsVO getQualityStatistics(String startDate, String endDate, Long productId);
    
    /**
     * 批量检验
     */
    void batchInspection(List<QualityInspection> inspections);
    
    /**
     * 质量合格率分析
     */
    Map<String, Object> getPassRateAnalysis(String startDate, String endDate, Long productId, Long supplierId);
    
    /**
     * 导出质量报表
     */
    Map<String, Object> exportQualityReport(String startDate, String endDate, Long productId, String reportType);
}