package com.chennian.storytelling.bean.vo;

import java.math.BigDecimal;
import java.util.List;

/**
 * 生产效率分析VO
 * 
 * @author chennian
 * @date 2024-01-01
 */
public class ProductionEfficiencyVO {
    
    /** 总产量 */
    private Integer totalOutput;
    
    /** 计划产量 */
    private Integer plannedOutput;
    
    /** 实际产量 */
    private Integer actualOutput;
    
    /** 合格产量 */
    private Integer qualifiedOutput;
    
    /** 生产效率(%) */
    private BigDecimal productionEfficiency;
    
    /** 计划达成率(%) */
    private BigDecimal planAchievementRate;
    
    /** 产品合格率(%) */
    private BigDecimal qualificationRate;
    
    /** 平均生产时间(小时) */
    private BigDecimal averageProductionTime;
    
    /** 设备利用率(%) */
    private BigDecimal equipmentUtilizationRate;
    
    /** 人员效率(%) */
    private BigDecimal personnelEfficiency;
    
    /** 生产线数量 */
    private Integer productionLineCount;
    
    /** 工作时间(小时) */
    private BigDecimal workingHours;
    
    /** 停机时间(小时) */
    private BigDecimal downtime;
    
    /** 效率趋势数据 */
    private List<EfficiencyTrendData> efficiencyTrend;
    
    /** 生产线效率对比 */
    private List<ProductionLineEfficiency> lineEfficiencyComparison;
    
    public ProductionEfficiencyVO() {
    }
    
    public Integer getTotalOutput() {
        return totalOutput;
    }
    
    public void setTotalOutput(Integer totalOutput) {
        this.totalOutput = totalOutput;
    }
    
    public Integer getPlannedOutput() {
        return plannedOutput;
    }
    
    public void setPlannedOutput(Integer plannedOutput) {
        this.plannedOutput = plannedOutput;
    }
    
    public Integer getActualOutput() {
        return actualOutput;
    }
    
    public void setActualOutput(Integer actualOutput) {
        this.actualOutput = actualOutput;
    }
    
    public Integer getQualifiedOutput() {
        return qualifiedOutput;
    }
    
    public void setQualifiedOutput(Integer qualifiedOutput) {
        this.qualifiedOutput = qualifiedOutput;
    }
    
    public BigDecimal getProductionEfficiency() {
        return productionEfficiency;
    }
    
    public void setProductionEfficiency(BigDecimal productionEfficiency) {
        this.productionEfficiency = productionEfficiency;
    }
    
    public BigDecimal getPlanAchievementRate() {
        return planAchievementRate;
    }
    
    public void setPlanAchievementRate(BigDecimal planAchievementRate) {
        this.planAchievementRate = planAchievementRate;
    }
    
    public BigDecimal getQualificationRate() {
        return qualificationRate;
    }
    
    public void setQualificationRate(BigDecimal qualificationRate) {
        this.qualificationRate = qualificationRate;
    }
    
    public BigDecimal getAverageProductionTime() {
        return averageProductionTime;
    }
    
    public void setAverageProductionTime(BigDecimal averageProductionTime) {
        this.averageProductionTime = averageProductionTime;
    }
    
    public BigDecimal getEquipmentUtilizationRate() {
        return equipmentUtilizationRate;
    }
    
    public void setEquipmentUtilizationRate(BigDecimal equipmentUtilizationRate) {
        this.equipmentUtilizationRate = equipmentUtilizationRate;
    }
    
    public BigDecimal getPersonnelEfficiency() {
        return personnelEfficiency;
    }
    
    public void setPersonnelEfficiency(BigDecimal personnelEfficiency) {
        this.personnelEfficiency = personnelEfficiency;
    }
    
    public Integer getProductionLineCount() {
        return productionLineCount;
    }
    
    public void setProductionLineCount(Integer productionLineCount) {
        this.productionLineCount = productionLineCount;
    }
    
    public BigDecimal getWorkingHours() {
        return workingHours;
    }
    
    public void setWorkingHours(BigDecimal workingHours) {
        this.workingHours = workingHours;
    }
    
    public BigDecimal getDowntime() {
        return downtime;
    }
    
    public void setDowntime(BigDecimal downtime) {
        this.downtime = downtime;
    }
    
    public List<EfficiencyTrendData> getEfficiencyTrend() {
        return efficiencyTrend;
    }
    
    public void setEfficiencyTrend(List<EfficiencyTrendData> efficiencyTrend) {
        this.efficiencyTrend = efficiencyTrend;
    }
    
    public List<ProductionLineEfficiency> getLineEfficiencyComparison() {
        return lineEfficiencyComparison;
    }
    
    public void setLineEfficiencyComparison(List<ProductionLineEfficiency> lineEfficiencyComparison) {
        this.lineEfficiencyComparison = lineEfficiencyComparison;
    }
    
    /**
     * 效率趋势数据内部类
     */
    public static class EfficiencyTrendData {
        private String date;
        private BigDecimal efficiency;
        private Integer output;
        
        public String getDate() {
            return date;
        }
        
        public void setDate(String date) {
            this.date = date;
        }
        
        public BigDecimal getEfficiency() {
            return efficiency;
        }
        
        public void setEfficiency(BigDecimal efficiency) {
            this.efficiency = efficiency;
        }
        
        public Integer getOutput() {
            return output;
        }
        
        public void setOutput(Integer output) {
            this.output = output;
        }
    }
    
    /**
     * 生产线效率内部类
     */
    public static class ProductionLineEfficiency {
        private Long lineId;
        private String lineName;
        private BigDecimal efficiency;
        private Integer output;
        private BigDecimal utilizationRate;
        
        public Long getLineId() {
            return lineId;
        }
        
        public void setLineId(Long lineId) {
            this.lineId = lineId;
        }
        
        public String getLineName() {
            return lineName;
        }
        
        public void setLineName(String lineName) {
            this.lineName = lineName;
        }
        
        public BigDecimal getEfficiency() {
            return efficiency;
        }
        
        public void setEfficiency(BigDecimal efficiency) {
            this.efficiency = efficiency;
        }
        
        public Integer getOutput() {
            return output;
        }
        
        public void setOutput(Integer output) {
            this.output = output;
        }
        
        public BigDecimal getUtilizationRate() {
            return utilizationRate;
        }
        
        public void setUtilizationRate(BigDecimal utilizationRate) {
            this.utilizationRate = utilizationRate;
        }
    }
    
    @Override
    public String toString() {
        return "ProductionEfficiencyVO{" +
                "totalOutput=" + totalOutput +
                ", plannedOutput=" + plannedOutput +
                ", actualOutput=" + actualOutput +
                ", qualifiedOutput=" + qualifiedOutput +
                ", productionEfficiency=" + productionEfficiency +
                ", planAchievementRate=" + planAchievementRate +
                ", qualificationRate=" + qualificationRate +
                ", averageProductionTime=" + averageProductionTime +
                ", equipmentUtilizationRate=" + equipmentUtilizationRate +
                ", personnelEfficiency=" + personnelEfficiency +
                ", productionLineCount=" + productionLineCount +
                ", workingHours=" + workingHours +
                ", downtime=" + downtime +
                '}';
    }
}