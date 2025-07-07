package com.chennian.storytelling.bean.vo.mall;

import java.math.BigDecimal;
import java.util.List;

/**
 * 设备利用率分析VO
 * 
 * @author chennian
 * @date 2024-01-01
 */
public class EquipmentUtilizationVO {
    
    /** 设备总数 */
    private Integer totalEquipmentCount;
    
    /** 运行设备数 */
    private Integer runningEquipmentCount;
    
    /** 停机设备数 */
    private Integer stoppedEquipmentCount;
    
    /** 维护设备数 */
    private Integer maintenanceEquipmentCount;
    
    /** 平均利用率(%) */
    private BigDecimal averageUtilizationRate;
    
    /** 总运行时间(小时) */
    private BigDecimal totalRunningTime;
    
    /** 总计划时间(小时) */
    private BigDecimal totalPlannedTime;
    
    /** 总停机时间(小时) */
    private BigDecimal totalDowntime;
    
    /** 总维护时间(小时) */
    private BigDecimal totalMaintenanceTime;
    
    /** 设备效率(%) */
    private BigDecimal equipmentEfficiency;
    
    /** 可用率(%) */
    private BigDecimal availabilityRate;
    
    /** 性能效率(%) */
    private BigDecimal performanceEfficiency;
    
    /** 质量率(%) */
    private BigDecimal qualityRate;
    
    /** OEE综合效率(%) */
    private BigDecimal oeeRate;
    
    /** 设备利用率趋势 */
    private List<UtilizationTrendData> utilizationTrend;
    
    /** 设备详细利用率 */
    private List<EquipmentUtilizationDetail> equipmentDetails;
    
    /** 设备状态分布 */
    private List<EquipmentStatusDistribution> statusDistribution;
    
    public EquipmentUtilizationVO() {
    }
    
    public Integer getTotalEquipmentCount() {
        return totalEquipmentCount;
    }
    
    public void setTotalEquipmentCount(Integer totalEquipmentCount) {
        this.totalEquipmentCount = totalEquipmentCount;
    }
    
    public Integer getRunningEquipmentCount() {
        return runningEquipmentCount;
    }
    
    public void setRunningEquipmentCount(Integer runningEquipmentCount) {
        this.runningEquipmentCount = runningEquipmentCount;
    }
    
    public Integer getStoppedEquipmentCount() {
        return stoppedEquipmentCount;
    }
    
    public void setStoppedEquipmentCount(Integer stoppedEquipmentCount) {
        this.stoppedEquipmentCount = stoppedEquipmentCount;
    }
    
    public Integer getMaintenanceEquipmentCount() {
        return maintenanceEquipmentCount;
    }
    
    public void setMaintenanceEquipmentCount(Integer maintenanceEquipmentCount) {
        this.maintenanceEquipmentCount = maintenanceEquipmentCount;
    }
    
    public BigDecimal getAverageUtilizationRate() {
        return averageUtilizationRate;
    }
    
    public void setAverageUtilizationRate(BigDecimal averageUtilizationRate) {
        this.averageUtilizationRate = averageUtilizationRate;
    }
    
    public BigDecimal getTotalRunningTime() {
        return totalRunningTime;
    }
    
    public void setTotalRunningTime(BigDecimal totalRunningTime) {
        this.totalRunningTime = totalRunningTime;
    }
    
    public BigDecimal getTotalPlannedTime() {
        return totalPlannedTime;
    }
    
    public void setTotalPlannedTime(BigDecimal totalPlannedTime) {
        this.totalPlannedTime = totalPlannedTime;
    }
    
    public BigDecimal getTotalDowntime() {
        return totalDowntime;
    }
    
    public void setTotalDowntime(BigDecimal totalDowntime) {
        this.totalDowntime = totalDowntime;
    }
    
    public BigDecimal getTotalMaintenanceTime() {
        return totalMaintenanceTime;
    }
    
    public void setTotalMaintenanceTime(BigDecimal totalMaintenanceTime) {
        this.totalMaintenanceTime = totalMaintenanceTime;
    }
    
    public BigDecimal getEquipmentEfficiency() {
        return equipmentEfficiency;
    }
    
    public void setEquipmentEfficiency(BigDecimal equipmentEfficiency) {
        this.equipmentEfficiency = equipmentEfficiency;
    }
    
    public BigDecimal getAvailabilityRate() {
        return availabilityRate;
    }
    
    public void setAvailabilityRate(BigDecimal availabilityRate) {
        this.availabilityRate = availabilityRate;
    }
    
    public BigDecimal getPerformanceEfficiency() {
        return performanceEfficiency;
    }
    
    public void setPerformanceEfficiency(BigDecimal performanceEfficiency) {
        this.performanceEfficiency = performanceEfficiency;
    }
    
    public BigDecimal getQualityRate() {
        return qualityRate;
    }
    
    public void setQualityRate(BigDecimal qualityRate) {
        this.qualityRate = qualityRate;
    }
    
    public BigDecimal getOeeRate() {
        return oeeRate;
    }
    
    public void setOeeRate(BigDecimal oeeRate) {
        this.oeeRate = oeeRate;
    }
    
    public List<UtilizationTrendData> getUtilizationTrend() {
        return utilizationTrend;
    }
    
    public void setUtilizationTrend(List<UtilizationTrendData> utilizationTrend) {
        this.utilizationTrend = utilizationTrend;
    }
    
    public List<EquipmentUtilizationDetail> getEquipmentDetails() {
        return equipmentDetails;
    }
    
    public void setEquipmentDetails(List<EquipmentUtilizationDetail> equipmentDetails) {
        this.equipmentDetails = equipmentDetails;
    }
    
    public List<EquipmentStatusDistribution> getStatusDistribution() {
        return statusDistribution;
    }
    
    public void setStatusDistribution(List<EquipmentStatusDistribution> statusDistribution) {
        this.statusDistribution = statusDistribution;
    }
    
    /**
     * 利用率趋势数据内部类
     */
    public static class UtilizationTrendData {
        private String date;
        private BigDecimal utilizationRate;
        private BigDecimal runningTime;
        private BigDecimal downtime;
        
        public String getDate() {
            return date;
        }
        
        public void setDate(String date) {
            this.date = date;
        }
        
        public BigDecimal getUtilizationRate() {
            return utilizationRate;
        }
        
        public void setUtilizationRate(BigDecimal utilizationRate) {
            this.utilizationRate = utilizationRate;
        }
        
        public BigDecimal getRunningTime() {
            return runningTime;
        }
        
        public void setRunningTime(BigDecimal runningTime) {
            this.runningTime = runningTime;
        }
        
        public BigDecimal getDowntime() {
            return downtime;
        }
        
        public void setDowntime(BigDecimal downtime) {
            this.downtime = downtime;
        }
    }
    
    /**
     * 设备利用率详情内部类
     */
    public static class EquipmentUtilizationDetail {
        private Long equipmentId;
        private String equipmentName;
        private String equipmentCode;
        private BigDecimal utilizationRate;
        private BigDecimal runningTime;
        private BigDecimal downtime;
        private String status;
        private BigDecimal oeeRate;
        
        public Long getEquipmentId() {
            return equipmentId;
        }
        
        public void setEquipmentId(Long equipmentId) {
            this.equipmentId = equipmentId;
        }
        
        public String getEquipmentName() {
            return equipmentName;
        }
        
        public void setEquipmentName(String equipmentName) {
            this.equipmentName = equipmentName;
        }
        
        public String getEquipmentCode() {
            return equipmentCode;
        }
        
        public void setEquipmentCode(String equipmentCode) {
            this.equipmentCode = equipmentCode;
        }
        
        public BigDecimal getUtilizationRate() {
            return utilizationRate;
        }
        
        public void setUtilizationRate(BigDecimal utilizationRate) {
            this.utilizationRate = utilizationRate;
        }
        
        public BigDecimal getRunningTime() {
            return runningTime;
        }
        
        public void setRunningTime(BigDecimal runningTime) {
            this.runningTime = runningTime;
        }
        
        public BigDecimal getDowntime() {
            return downtime;
        }
        
        public void setDowntime(BigDecimal downtime) {
            this.downtime = downtime;
        }
        
        public String getStatus() {
            return status;
        }
        
        public void setStatus(String status) {
            this.status = status;
        }
        
        public BigDecimal getOeeRate() {
            return oeeRate;
        }
        
        public void setOeeRate(BigDecimal oeeRate) {
            this.oeeRate = oeeRate;
        }
    }
    
    /**
     * 设备状态分布内部类
     */
    public static class EquipmentStatusDistribution {
        private String status;
        private Integer count;
        private BigDecimal percentage;
        
        public String getStatus() {
            return status;
        }
        
        public void setStatus(String status) {
            this.status = status;
        }
        
        public Integer getCount() {
            return count;
        }
        
        public void setCount(Integer count) {
            this.count = count;
        }
        
        public BigDecimal getPercentage() {
            return percentage;
        }
        
        public void setPercentage(BigDecimal percentage) {
            this.percentage = percentage;
        }
    }
    
    @Override
    public String toString() {
        return "EquipmentUtilizationVO{" +
                "totalEquipmentCount=" + totalEquipmentCount +
                ", runningEquipmentCount=" + runningEquipmentCount +
                ", stoppedEquipmentCount=" + stoppedEquipmentCount +
                ", maintenanceEquipmentCount=" + maintenanceEquipmentCount +
                ", averageUtilizationRate=" + averageUtilizationRate +
                ", totalRunningTime=" + totalRunningTime +
                ", totalPlannedTime=" + totalPlannedTime +
                ", totalDowntime=" + totalDowntime +
                ", totalMaintenanceTime=" + totalMaintenanceTime +
                ", equipmentEfficiency=" + equipmentEfficiency +
                ", availabilityRate=" + availabilityRate +
                ", performanceEfficiency=" + performanceEfficiency +
                ", qualityRate=" + qualityRate +
                ", oeeRate=" + oeeRate +
                '}';
    }
}