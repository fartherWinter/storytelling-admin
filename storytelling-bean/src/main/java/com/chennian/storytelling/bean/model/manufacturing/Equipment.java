package com.chennian.storytelling.bean.model.manufacturing;

import com.fasterxml.jackson.annotation.JsonFormat;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

import java.math.BigDecimal;
import java.util.Date;

/**
 * 设备实体类
 * 
 * @author storytelling
 * @date 2024-01-01
 */
public class Equipment {
    
    /** 设备ID */
    private Long equipmentId;
    
    /** 设备编号 */
    private String equipmentCode;
    
    /** 设备名称 */
    private String equipmentName;
    
    /** 设备类型 */
    private String equipmentType;
    
    /** 设备分类 */
    private String equipmentCategory;
    
    /** 设备状态（0-停机 1-运行 2-维护 3-故障 4-待机 5-报废） */
    private Integer equipmentStatus;
    
    /** 生产线ID */
    private Long lineId;
    
    /** 生产线名称 */
    private String lineName;
    
    /** 车间ID */
    private Long workshopId;
    
    /** 车间名称 */
    private String workshopName;
    
    /** 设备位置 */
    private String location;
    
    /** 制造商 */
    private String manufacturer;
    
    /** 设备型号 */
    private String model;
    
    /** 序列号 */
    private String serialNumber;
    
    /** 购买日期 */
    @JsonFormat(pattern = "yyyy-MM-dd")
    private Date purchaseDate;
    
    /** 投产日期 */
    @JsonFormat(pattern = "yyyy-MM-dd")
    private Date commissioningDate;
    
    /** 购买价格 */
    private BigDecimal purchasePrice;
    
    /** 当前价值 */
    private BigDecimal currentValue;
    
    /** 折旧年限 */
    private Integer depreciationYears;
    
    /** 年折旧率 */
    private BigDecimal depreciationRate;
    
    /** 设计产能 */
    private Integer designCapacity;
    
    /** 实际产能 */
    private Integer actualCapacity;
    
    /** 产能利用率 */
    private BigDecimal capacityUtilization;
    
    /** 设备效率 */
    private BigDecimal efficiency;
    
    /** 负责人ID */
    private Long managerId;
    
    /** 负责人姓名 */
    private String managerName;
    
    /** 操作员ID */
    private Long operatorId;
    
    /** 操作员姓名 */
    private String operatorName;
    
    /** 维护人员ID */
    private Long maintainerId;
    
    /** 维护人员姓名 */
    private String maintainerName;
    
    /** 供应商ID */
    private Long supplierId;
    
    /** 供应商名称 */
    private String supplierName;
    
    /** 保修期（月） */
    private Integer warrantyPeriod;
    
    /** 保修到期日期 */
    @JsonFormat(pattern = "yyyy-MM-dd")
    private Date warrantyExpireDate;
    
    /** 最后维护时间 */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date lastMaintenanceTime;
    
    /** 下次维护时间 */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date nextMaintenanceTime;
    
    /** 维护周期（天） */
    private Integer maintenanceCycle;
    
    /** 维护成本 */
    private BigDecimal maintenanceCost;
    
    /** 累计运行时间（小时） */
    private Double totalRunningHours;
    
    /** 累计停机时间（小时） */
    private Double totalDowntime;
    
    /** 故障次数 */
    private Integer faultCount;
    
    /** 最后故障时间 */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date lastFaultTime;
    
    /** 平均故障间隔时间(MTBF) */
    private Double mtbf;
    
    /** 平均修复时间(MTTR) */
    private Double mttr;
    
    /** 设备可用率 */
    private BigDecimal availabilityRate;
    
    /** 设备综合效率(OEE) */
    private BigDecimal oeeRate;
    
    /** 能耗等级 */
    private String energyLevel;
    
    /** 额定功率（KW） */
    private BigDecimal ratedPower;
    
    /** 实际功耗（KW） */
    private BigDecimal actualPowerConsumption;
    
    /** 技术参数 */
    private String technicalParameters;
    
    /** 操作规程 */
    private String operatingProcedures;
    
    /** 安全要求 */
    private String safetyRequirements;
    
    /** 环保要求 */
    private String environmentalRequirements;
    
    /** 状态变更原因 */
    private String statusChangeReason;
    
    /** 状态变更时间 */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date statusChangeTime;
    
    /** 设备描述 */
    private String description;
    
    /** 备注 */
    private String remark;
    
    /** 创建者 */
    private String createBy;
    
    /** 创建时间 */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date createTime;
    
    /** 更新者 */
    private String updateBy;
    
    /** 更新时间 */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date updateTime;
    
    /** 删除标志（0代表存在 2代表删除） */
    private String delFlag;
    
    // Getter and Setter methods
    
    public Long getEquipmentId() {
        return equipmentId;
    }
    
    public void setEquipmentId(Long equipmentId) {
        this.equipmentId = equipmentId;
    }
    
    public String getEquipmentCode() {
        return equipmentCode;
    }
    
    public void setEquipmentCode(String equipmentCode) {
        this.equipmentCode = equipmentCode;
    }
    
    public String getEquipmentName() {
        return equipmentName;
    }
    
    public void setEquipmentName(String equipmentName) {
        this.equipmentName = equipmentName;
    }
    
    public String getEquipmentType() {
        return equipmentType;
    }
    
    public void setEquipmentType(String equipmentType) {
        this.equipmentType = equipmentType;
    }
    
    public String getEquipmentCategory() {
        return equipmentCategory;
    }
    
    public void setEquipmentCategory(String equipmentCategory) {
        this.equipmentCategory = equipmentCategory;
    }
    
    public Integer getEquipmentStatus() {
        return equipmentStatus;
    }
    
    public void setEquipmentStatus(Integer equipmentStatus) {
        this.equipmentStatus = equipmentStatus;
    }
    
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
    
    public Long getWorkshopId() {
        return workshopId;
    }
    
    public void setWorkshopId(Long workshopId) {
        this.workshopId = workshopId;
    }
    
    public String getWorkshopName() {
        return workshopName;
    }
    
    public void setWorkshopName(String workshopName) {
        this.workshopName = workshopName;
    }
    
    public String getLocation() {
        return location;
    }
    
    public void setLocation(String location) {
        this.location = location;
    }
    
    public String getManufacturer() {
        return manufacturer;
    }
    
    public void setManufacturer(String manufacturer) {
        this.manufacturer = manufacturer;
    }
    
    public String getModel() {
        return model;
    }
    
    public void setModel(String model) {
        this.model = model;
    }
    
    public String getSerialNumber() {
        return serialNumber;
    }
    
    public void setSerialNumber(String serialNumber) {
        this.serialNumber = serialNumber;
    }
    
    public Date getPurchaseDate() {
        return purchaseDate;
    }
    
    public void setPurchaseDate(Date purchaseDate) {
        this.purchaseDate = purchaseDate;
    }
    
    public Date getCommissioningDate() {
        return commissioningDate;
    }
    
    public void setCommissioningDate(Date commissioningDate) {
        this.commissioningDate = commissioningDate;
    }
    
    public BigDecimal getPurchasePrice() {
        return purchasePrice;
    }
    
    public void setPurchasePrice(BigDecimal purchasePrice) {
        this.purchasePrice = purchasePrice;
    }
    
    public BigDecimal getCurrentValue() {
        return currentValue;
    }
    
    public void setCurrentValue(BigDecimal currentValue) {
        this.currentValue = currentValue;
    }
    
    public Integer getDepreciationYears() {
        return depreciationYears;
    }
    
    public void setDepreciationYears(Integer depreciationYears) {
        this.depreciationYears = depreciationYears;
    }
    
    public BigDecimal getDepreciationRate() {
        return depreciationRate;
    }
    
    public void setDepreciationRate(BigDecimal depreciationRate) {
        this.depreciationRate = depreciationRate;
    }
    
    public Integer getDesignCapacity() {
        return designCapacity;
    }
    
    public void setDesignCapacity(Integer designCapacity) {
        this.designCapacity = designCapacity;
    }
    
    public Integer getActualCapacity() {
        return actualCapacity;
    }
    
    public void setActualCapacity(Integer actualCapacity) {
        this.actualCapacity = actualCapacity;
    }
    
    public BigDecimal getCapacityUtilization() {
        return capacityUtilization;
    }
    
    public void setCapacityUtilization(BigDecimal capacityUtilization) {
        this.capacityUtilization = capacityUtilization;
    }
    
    public BigDecimal getEfficiency() {
        return efficiency;
    }
    
    public void setEfficiency(BigDecimal efficiency) {
        this.efficiency = efficiency;
    }
    
    public Long getManagerId() {
        return managerId;
    }
    
    public void setManagerId(Long managerId) {
        this.managerId = managerId;
    }
    
    public String getManagerName() {
        return managerName;
    }
    
    public void setManagerName(String managerName) {
        this.managerName = managerName;
    }
    
    public Long getOperatorId() {
        return operatorId;
    }
    
    public void setOperatorId(Long operatorId) {
        this.operatorId = operatorId;
    }
    
    public String getOperatorName() {
        return operatorName;
    }
    
    public void setOperatorName(String operatorName) {
        this.operatorName = operatorName;
    }
    
    public Long getMaintainerId() {
        return maintainerId;
    }
    
    public void setMaintainerId(Long maintainerId) {
        this.maintainerId = maintainerId;
    }
    
    public String getMaintainerName() {
        return maintainerName;
    }
    
    public void setMaintainerName(String maintainerName) {
        this.maintainerName = maintainerName;
    }
    
    public Long getSupplierId() {
        return supplierId;
    }
    
    public void setSupplierId(Long supplierId) {
        this.supplierId = supplierId;
    }
    
    public String getSupplierName() {
        return supplierName;
    }
    
    public void setSupplierName(String supplierName) {
        this.supplierName = supplierName;
    }
    
    public Integer getWarrantyPeriod() {
        return warrantyPeriod;
    }
    
    public void setWarrantyPeriod(Integer warrantyPeriod) {
        this.warrantyPeriod = warrantyPeriod;
    }
    
    public Date getWarrantyExpireDate() {
        return warrantyExpireDate;
    }
    
    public void setWarrantyExpireDate(Date warrantyExpireDate) {
        this.warrantyExpireDate = warrantyExpireDate;
    }
    
    public Date getLastMaintenanceTime() {
        return lastMaintenanceTime;
    }
    
    public void setLastMaintenanceTime(Date lastMaintenanceTime) {
        this.lastMaintenanceTime = lastMaintenanceTime;
    }
    
    public Date getNextMaintenanceTime() {
        return nextMaintenanceTime;
    }
    
    public void setNextMaintenanceTime(Date nextMaintenanceTime) {
        this.nextMaintenanceTime = nextMaintenanceTime;
    }
    
    public Integer getMaintenanceCycle() {
        return maintenanceCycle;
    }
    
    public void setMaintenanceCycle(Integer maintenanceCycle) {
        this.maintenanceCycle = maintenanceCycle;
    }
    
    public BigDecimal getMaintenanceCost() {
        return maintenanceCost;
    }
    
    public void setMaintenanceCost(BigDecimal maintenanceCost) {
        this.maintenanceCost = maintenanceCost;
    }
    
    public Double getTotalRunningHours() {
        return totalRunningHours;
    }
    
    public void setTotalRunningHours(Double totalRunningHours) {
        this.totalRunningHours = totalRunningHours;
    }
    
    public Double getTotalDowntime() {
        return totalDowntime;
    }
    
    public void setTotalDowntime(Double totalDowntime) {
        this.totalDowntime = totalDowntime;
    }
    
    public Integer getFaultCount() {
        return faultCount;
    }
    
    public void setFaultCount(Integer faultCount) {
        this.faultCount = faultCount;
    }
    
    public Date getLastFaultTime() {
        return lastFaultTime;
    }
    
    public void setLastFaultTime(Date lastFaultTime) {
        this.lastFaultTime = lastFaultTime;
    }
    
    public Double getMtbf() {
        return mtbf;
    }
    
    public void setMtbf(Double mtbf) {
        this.mtbf = mtbf;
    }
    
    public Double getMttr() {
        return mttr;
    }
    
    public void setMttr(Double mttr) {
        this.mttr = mttr;
    }
    
    public BigDecimal getAvailabilityRate() {
        return availabilityRate;
    }
    
    public void setAvailabilityRate(BigDecimal availabilityRate) {
        this.availabilityRate = availabilityRate;
    }
    
    public BigDecimal getOeeRate() {
        return oeeRate;
    }
    
    public void setOeeRate(BigDecimal oeeRate) {
        this.oeeRate = oeeRate;
    }
    
    public String getEnergyLevel() {
        return energyLevel;
    }
    
    public void setEnergyLevel(String energyLevel) {
        this.energyLevel = energyLevel;
    }
    
    public BigDecimal getRatedPower() {
        return ratedPower;
    }
    
    public void setRatedPower(BigDecimal ratedPower) {
        this.ratedPower = ratedPower;
    }
    
    public BigDecimal getActualPowerConsumption() {
        return actualPowerConsumption;
    }
    
    public void setActualPowerConsumption(BigDecimal actualPowerConsumption) {
        this.actualPowerConsumption = actualPowerConsumption;
    }
    
    public String getTechnicalParameters() {
        return technicalParameters;
    }
    
    public void setTechnicalParameters(String technicalParameters) {
        this.technicalParameters = technicalParameters;
    }
    
    public String getOperatingProcedures() {
        return operatingProcedures;
    }
    
    public void setOperatingProcedures(String operatingProcedures) {
        this.operatingProcedures = operatingProcedures;
    }
    
    public String getSafetyRequirements() {
        return safetyRequirements;
    }
    
    public void setSafetyRequirements(String safetyRequirements) {
        this.safetyRequirements = safetyRequirements;
    }
    
    public String getEnvironmentalRequirements() {
        return environmentalRequirements;
    }
    
    public void setEnvironmentalRequirements(String environmentalRequirements) {
        this.environmentalRequirements = environmentalRequirements;
    }
    
    public String getStatusChangeReason() {
        return statusChangeReason;
    }
    
    public void setStatusChangeReason(String statusChangeReason) {
        this.statusChangeReason = statusChangeReason;
    }
    
    public Date getStatusChangeTime() {
        return statusChangeTime;
    }
    
    public void setStatusChangeTime(Date statusChangeTime) {
        this.statusChangeTime = statusChangeTime;
    }
    
    public String getDescription() {
        return description;
    }
    
    public void setDescription(String description) {
        this.description = description;
    }
    
    public String getRemark() {
        return remark;
    }
    
    public void setRemark(String remark) {
        this.remark = remark;
    }
    
    public String getCreateBy() {
        return createBy;
    }
    
    public void setCreateBy(String createBy) {
        this.createBy = createBy;
    }
    
    public Date getCreateTime() {
        return createTime;
    }
    
    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }
    
    public String getUpdateBy() {
        return updateBy;
    }
    
    public void setUpdateBy(String updateBy) {
        this.updateBy = updateBy;
    }
    
    public Date getUpdateTime() {
        return updateTime;
    }
    
    public void setUpdateTime(Date updateTime) {
        this.updateTime = updateTime;
    }
    
    public String getDelFlag() {
        return delFlag;
    }
    
    public void setDelFlag(String delFlag) {
        this.delFlag = delFlag;
    }
    
    @Override
    public String toString() {
        return new ToStringBuilder(this, ToStringStyle.MULTI_LINE_STYLE)
            .append("equipmentId", getEquipmentId())
            .append("equipmentCode", getEquipmentCode())
            .append("equipmentName", getEquipmentName())
            .append("equipmentType", getEquipmentType())
            .append("equipmentCategory", getEquipmentCategory())
            .append("equipmentStatus", getEquipmentStatus())
            .append("lineId", getLineId())
            .append("lineName", getLineName())
            .append("workshopId", getWorkshopId())
            .append("workshopName", getWorkshopName())
            .append("location", getLocation())
            .append("manufacturer", getManufacturer())
            .append("model", getModel())
            .append("serialNumber", getSerialNumber())
            .append("purchaseDate", getPurchaseDate())
            .append("commissioningDate", getCommissioningDate())
            .append("purchasePrice", getPurchasePrice())
            .append("currentValue", getCurrentValue())
            .append("depreciationYears", getDepreciationYears())
            .append("depreciationRate", getDepreciationRate())
            .append("designCapacity", getDesignCapacity())
            .append("actualCapacity", getActualCapacity())
            .append("capacityUtilization", getCapacityUtilization())
            .append("efficiency", getEfficiency())
            .append("managerId", getManagerId())
            .append("managerName", getManagerName())
            .append("operatorId", getOperatorId())
            .append("operatorName", getOperatorName())
            .append("maintainerId", getMaintainerId())
            .append("maintainerName", getMaintainerName())
            .append("supplierId", getSupplierId())
            .append("supplierName", getSupplierName())
            .append("warrantyPeriod", getWarrantyPeriod())
            .append("warrantyExpireDate", getWarrantyExpireDate())
            .append("lastMaintenanceTime", getLastMaintenanceTime())
            .append("nextMaintenanceTime", getNextMaintenanceTime())
            .append("maintenanceCycle", getMaintenanceCycle())
            .append("maintenanceCost", getMaintenanceCost())
            .append("totalRunningHours", getTotalRunningHours())
            .append("totalDowntime", getTotalDowntime())
            .append("faultCount", getFaultCount())
            .append("lastFaultTime", getLastFaultTime())
            .append("mtbf", getMtbf())
            .append("mttr", getMttr())
            .append("availabilityRate", getAvailabilityRate())
            .append("oeeRate", getOeeRate())
            .append("energyLevel", getEnergyLevel())
            .append("ratedPower", getRatedPower())
            .append("actualPowerConsumption", getActualPowerConsumption())
            .append("technicalParameters", getTechnicalParameters())
            .append("operatingProcedures", getOperatingProcedures())
            .append("safetyRequirements", getSafetyRequirements())
            .append("environmentalRequirements", getEnvironmentalRequirements())
            .append("statusChangeReason", getStatusChangeReason())
            .append("statusChangeTime", getStatusChangeTime())
            .append("description", getDescription())
            .append("remark", getRemark())
            .append("createBy", getCreateBy())
            .append("createTime", getCreateTime())
            .append("updateBy", getUpdateBy())
            .append("updateTime", getUpdateTime())
            .append("delFlag", getDelFlag())
            .toString();
    }
}