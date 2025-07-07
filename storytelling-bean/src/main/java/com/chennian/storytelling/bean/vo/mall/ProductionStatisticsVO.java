package com.chennian.storytelling.bean.vo.mall;

import java.math.BigDecimal;
import java.util.List;

/**
 * 生产统计VO
 * 
 * @author chennian
 * @date 2024-01-01
 */
public class ProductionStatisticsVO {
    
    /** 总产量 */
    private Integer totalOutput;
    
    /** 计划产量 */
    private Integer plannedOutput;
    
    /** 实际产量 */
    private Integer actualOutput;
    
    /** 合格产量 */
    private Integer qualifiedOutput;
    
    /** 不合格产量 */
    private Integer defectiveOutput;
    
    /** 计划达成率(%) */
    private BigDecimal planAchievementRate;
    
    /** 产品合格率(%) */
    private BigDecimal qualificationRate;
    
    /** 生产订单数量 */
    private Integer orderCount;
    
    /** 完成订单数量 */
    private Integer completedOrderCount;
    
    /** 进行中订单数量 */
    private Integer inProgressOrderCount;
    
    /** 延期订单数量 */
    private Integer delayedOrderCount;
    
    /** 生产线数量 */
    private Integer productionLineCount;
    
    /** 活跃生产线数量 */
    private Integer activeLineCount;
    
    /** 设备数量 */
    private Integer equipmentCount;
    
    /** 运行设备数量 */
    private Integer runningEquipmentCount;
    
    /** 总工作时间(小时) */
    private BigDecimal totalWorkingHours;
    
    /** 总停机时间(小时) */
    private BigDecimal totalDowntime;
    
    /** 设备利用率(%) */
    private BigDecimal equipmentUtilizationRate;
    
    /** 人员数量 */
    private Integer personnelCount;
    
    /** 在岗人员数量 */
    private Integer onDutyPersonnelCount;
    
    /** 人员出勤率(%) */
    private BigDecimal attendanceRate;
    
    /** 统计开始日期 */
    private String startDate;
    
    /** 统计结束日期 */
    private String endDate;
    
    /** 产品统计列表 */
    private List<ProductStatistics> productStatistics;
    
    /** 生产线统计列表 */
    private List<ProductionLineStatistics> lineStatistics;
    
    /** 日期统计趋势 */
    private List<DateStatistics> dateStatistics;
    
    public ProductionStatisticsVO() {
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
    
    public Integer getDefectiveOutput() {
        return defectiveOutput;
    }
    
    public void setDefectiveOutput(Integer defectiveOutput) {
        this.defectiveOutput = defectiveOutput;
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
    
    public Integer getOrderCount() {
        return orderCount;
    }
    
    public void setOrderCount(Integer orderCount) {
        this.orderCount = orderCount;
    }
    
    public Integer getCompletedOrderCount() {
        return completedOrderCount;
    }
    
    public void setCompletedOrderCount(Integer completedOrderCount) {
        this.completedOrderCount = completedOrderCount;
    }
    
    public Integer getInProgressOrderCount() {
        return inProgressOrderCount;
    }
    
    public void setInProgressOrderCount(Integer inProgressOrderCount) {
        this.inProgressOrderCount = inProgressOrderCount;
    }
    
    public Integer getDelayedOrderCount() {
        return delayedOrderCount;
    }
    
    public void setDelayedOrderCount(Integer delayedOrderCount) {
        this.delayedOrderCount = delayedOrderCount;
    }
    
    public Integer getProductionLineCount() {
        return productionLineCount;
    }
    
    public void setProductionLineCount(Integer productionLineCount) {
        this.productionLineCount = productionLineCount;
    }
    
    public Integer getActiveLineCount() {
        return activeLineCount;
    }
    
    public void setActiveLineCount(Integer activeLineCount) {
        this.activeLineCount = activeLineCount;
    }
    
    public Integer getEquipmentCount() {
        return equipmentCount;
    }
    
    public void setEquipmentCount(Integer equipmentCount) {
        this.equipmentCount = equipmentCount;
    }
    
    public Integer getRunningEquipmentCount() {
        return runningEquipmentCount;
    }
    
    public void setRunningEquipmentCount(Integer runningEquipmentCount) {
        this.runningEquipmentCount = runningEquipmentCount;
    }
    
    public BigDecimal getTotalWorkingHours() {
        return totalWorkingHours;
    }
    
    public void setTotalWorkingHours(BigDecimal totalWorkingHours) {
        this.totalWorkingHours = totalWorkingHours;
    }
    
    public BigDecimal getTotalDowntime() {
        return totalDowntime;
    }
    
    public void setTotalDowntime(BigDecimal totalDowntime) {
        this.totalDowntime = totalDowntime;
    }
    
    public BigDecimal getEquipmentUtilizationRate() {
        return equipmentUtilizationRate;
    }
    
    public void setEquipmentUtilizationRate(BigDecimal equipmentUtilizationRate) {
        this.equipmentUtilizationRate = equipmentUtilizationRate;
    }
    
    public Integer getPersonnelCount() {
        return personnelCount;
    }
    
    public void setPersonnelCount(Integer personnelCount) {
        this.personnelCount = personnelCount;
    }
    
    public Integer getOnDutyPersonnelCount() {
        return onDutyPersonnelCount;
    }
    
    public void setOnDutyPersonnelCount(Integer onDutyPersonnelCount) {
        this.onDutyPersonnelCount = onDutyPersonnelCount;
    }
    
    public BigDecimal getAttendanceRate() {
        return attendanceRate;
    }
    
    public void setAttendanceRate(BigDecimal attendanceRate) {
        this.attendanceRate = attendanceRate;
    }
    
    public String getStartDate() {
        return startDate;
    }
    
    public void setStartDate(String startDate) {
        this.startDate = startDate;
    }
    
    public String getEndDate() {
        return endDate;
    }
    
    public void setEndDate(String endDate) {
        this.endDate = endDate;
    }
    
    public List<ProductStatistics> getProductStatistics() {
        return productStatistics;
    }
    
    public void setProductStatistics(List<ProductStatistics> productStatistics) {
        this.productStatistics = productStatistics;
    }
    
    public List<ProductionLineStatistics> getLineStatistics() {
        return lineStatistics;
    }
    
    public void setLineStatistics(List<ProductionLineStatistics> lineStatistics) {
        this.lineStatistics = lineStatistics;
    }
    
    public List<DateStatistics> getDateStatistics() {
        return dateStatistics;
    }
    
    public void setDateStatistics(List<DateStatistics> dateStatistics) {
        this.dateStatistics = dateStatistics;
    }
    
    /**
     * 产品统计内部类
     */
    public static class ProductStatistics {
        private Long productId;
        private String productName;
        private String productCode;
        private Integer plannedOutput;
        private Integer actualOutput;
        private Integer qualifiedOutput;
        private BigDecimal qualificationRate;
        private BigDecimal planAchievementRate;
        
        public Long getProductId() {
            return productId;
        }
        
        public void setProductId(Long productId) {
            this.productId = productId;
        }
        
        public String getProductName() {
            return productName;
        }
        
        public void setProductName(String productName) {
            this.productName = productName;
        }
        
        public String getProductCode() {
            return productCode;
        }
        
        public void setProductCode(String productCode) {
            this.productCode = productCode;
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
        
        public BigDecimal getQualificationRate() {
            return qualificationRate;
        }
        
        public void setQualificationRate(BigDecimal qualificationRate) {
            this.qualificationRate = qualificationRate;
        }
        
        public BigDecimal getPlanAchievementRate() {
            return planAchievementRate;
        }
        
        public void setPlanAchievementRate(BigDecimal planAchievementRate) {
            this.planAchievementRate = planAchievementRate;
        }
    }
    
    /**
     * 生产线统计内部类
     */
    public static class ProductionLineStatistics {
        private Long lineId;
        private String lineName;
        private Integer plannedOutput;
        private Integer actualOutput;
        private Integer qualifiedOutput;
        private BigDecimal utilizationRate;
        private BigDecimal efficiency;
        private String status;
        
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
        
        public BigDecimal getUtilizationRate() {
            return utilizationRate;
        }
        
        public void setUtilizationRate(BigDecimal utilizationRate) {
            this.utilizationRate = utilizationRate;
        }
        
        public BigDecimal getEfficiency() {
            return efficiency;
        }
        
        public void setEfficiency(BigDecimal efficiency) {
            this.efficiency = efficiency;
        }
        
        public String getStatus() {
            return status;
        }
        
        public void setStatus(String status) {
            this.status = status;
        }
    }
    
    /**
     * 日期统计内部类
     */
    public static class DateStatistics {
        private String date;
        private Integer plannedOutput;
        private Integer actualOutput;
        private Integer qualifiedOutput;
        private BigDecimal planAchievementRate;
        private BigDecimal qualificationRate;
        
        public String getDate() {
            return date;
        }
        
        public void setDate(String date) {
            this.date = date;
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
    }
    
    @Override
    public String toString() {
        return "ProductionStatisticsVO{" +
                "totalOutput=" + totalOutput +
                ", plannedOutput=" + plannedOutput +
                ", actualOutput=" + actualOutput +
                ", qualifiedOutput=" + qualifiedOutput +
                ", defectiveOutput=" + defectiveOutput +
                ", planAchievementRate=" + planAchievementRate +
                ", qualificationRate=" + qualificationRate +
                ", orderCount=" + orderCount +
                ", completedOrderCount=" + completedOrderCount +
                ", inProgressOrderCount=" + inProgressOrderCount +
                ", delayedOrderCount=" + delayedOrderCount +
                ", productionLineCount=" + productionLineCount +
                ", activeLineCount=" + activeLineCount +
                ", equipmentCount=" + equipmentCount +
                ", runningEquipmentCount=" + runningEquipmentCount +
                ", totalWorkingHours=" + totalWorkingHours +
                ", totalDowntime=" + totalDowntime +
                ", equipmentUtilizationRate=" + equipmentUtilizationRate +
                ", personnelCount=" + personnelCount +
                ", onDutyPersonnelCount=" + onDutyPersonnelCount +
                ", attendanceRate=" + attendanceRate +
                ", startDate='" + startDate + '\'' +
                ", endDate='" + endDate + '\'' +
                '}';
    }
}