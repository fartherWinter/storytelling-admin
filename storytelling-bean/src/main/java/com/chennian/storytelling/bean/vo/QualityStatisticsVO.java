package com.chennian.storytelling.bean.vo;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

/**
 * 质量统计VO
 * 
 * @author storytelling
 * @date 2024-01-01
 */
public class QualityStatisticsVO {
    
    /** 总检验次数 */
    private Integer totalInspections;
    
    /** 合格次数 */
    private Integer qualifiedCount;
    
    /** 不合格次数 */
    private Integer unqualifiedCount;
    
    /** 合格率 */
    private BigDecimal qualificationRate;
    
    /** 质量问题总数 */
    private Integer totalIssues;
    
    /** 已解决问题数 */
    private Integer resolvedIssues;
    
    /** 待解决问题数 */
    private Integer pendingIssues;
    
    /** 质量改进项目数 */
    private Integer improvementProjects;
    
    /** 已完成改进项目数 */
    private Integer completedImprovements;
    
    /** 质量成本 */
    private BigDecimal qualityCost;
    
    /** 预防成本 */
    private BigDecimal preventionCost;
    
    /** 评价成本 */
    private BigDecimal appraisalCost;
    
    /** 内部失效成本 */
    private BigDecimal internalFailureCost;
    
    /** 外部失效成本 */
    private BigDecimal externalFailureCost;
    
    /** 按产品分组的统计 */
    private List<Map<String, Object>> productStatistics;
    
    /** 按供应商分组的统计 */
    private List<Map<String, Object>> supplierStatistics;
    
    /** 按检验类型分组的统计 */
    private List<Map<String, Object>> inspectionTypeStatistics;
    
    /** 质量趋势数据 */
    private List<Map<String, Object>> trendData;
    
    public Integer getTotalInspections() {
        return totalInspections;
    }
    
    public void setTotalInspections(Integer totalInspections) {
        this.totalInspections = totalInspections;
    }
    
    public Integer getQualifiedCount() {
        return qualifiedCount;
    }
    
    public void setQualifiedCount(Integer qualifiedCount) {
        this.qualifiedCount = qualifiedCount;
    }
    
    public Integer getUnqualifiedCount() {
        return unqualifiedCount;
    }
    
    public void setUnqualifiedCount(Integer unqualifiedCount) {
        this.unqualifiedCount = unqualifiedCount;
    }
    
    public BigDecimal getQualificationRate() {
        return qualificationRate;
    }
    
    public void setQualificationRate(BigDecimal qualificationRate) {
        this.qualificationRate = qualificationRate;
    }
    
    public Integer getTotalIssues() {
        return totalIssues;
    }
    
    public void setTotalIssues(Integer totalIssues) {
        this.totalIssues = totalIssues;
    }
    
    public Integer getResolvedIssues() {
        return resolvedIssues;
    }
    
    public void setResolvedIssues(Integer resolvedIssues) {
        this.resolvedIssues = resolvedIssues;
    }
    
    public Integer getPendingIssues() {
        return pendingIssues;
    }
    
    public void setPendingIssues(Integer pendingIssues) {
        this.pendingIssues = pendingIssues;
    }
    
    public Integer getImprovementProjects() {
        return improvementProjects;
    }
    
    public void setImprovementProjects(Integer improvementProjects) {
        this.improvementProjects = improvementProjects;
    }
    
    public Integer getCompletedImprovements() {
        return completedImprovements;
    }
    
    public void setCompletedImprovements(Integer completedImprovements) {
        this.completedImprovements = completedImprovements;
    }
    
    public BigDecimal getQualityCost() {
        return qualityCost;
    }
    
    public void setQualityCost(BigDecimal qualityCost) {
        this.qualityCost = qualityCost;
    }
    
    public BigDecimal getPreventionCost() {
        return preventionCost;
    }
    
    public void setPreventionCost(BigDecimal preventionCost) {
        this.preventionCost = preventionCost;
    }
    
    public BigDecimal getAppraisalCost() {
        return appraisalCost;
    }
    
    public void setAppraisalCost(BigDecimal appraisalCost) {
        this.appraisalCost = appraisalCost;
    }
    
    public BigDecimal getInternalFailureCost() {
        return internalFailureCost;
    }
    
    public void setInternalFailureCost(BigDecimal internalFailureCost) {
        this.internalFailureCost = internalFailureCost;
    }
    
    public BigDecimal getExternalFailureCost() {
        return externalFailureCost;
    }
    
    public void setExternalFailureCost(BigDecimal externalFailureCost) {
        this.externalFailureCost = externalFailureCost;
    }
    
    public List<Map<String, Object>> getProductStatistics() {
        return productStatistics;
    }
    
    public void setProductStatistics(List<Map<String, Object>> productStatistics) {
        this.productStatistics = productStatistics;
    }
    
    public List<Map<String, Object>> getSupplierStatistics() {
        return supplierStatistics;
    }
    
    public void setSupplierStatistics(List<Map<String, Object>> supplierStatistics) {
        this.supplierStatistics = supplierStatistics;
    }
    
    public List<Map<String, Object>> getInspectionTypeStatistics() {
        return inspectionTypeStatistics;
    }
    
    public void setInspectionTypeStatistics(List<Map<String, Object>> inspectionTypeStatistics) {
        this.inspectionTypeStatistics = inspectionTypeStatistics;
    }
    
    public List<Map<String, Object>> getTrendData() {
        return trendData;
    }
    
    public void setTrendData(List<Map<String, Object>> trendData) {
        this.trendData = trendData;
    }
}