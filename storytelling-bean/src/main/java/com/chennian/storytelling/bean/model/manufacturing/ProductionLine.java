package com.chennian.storytelling.bean.model.manufacturing;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.fasterxml.jackson.annotation.JsonFormat;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

import java.math.BigDecimal;
import java.util.Date;

/**
 * 生产线实体类
 * 
 * @author storytelling
 * @date 2024-01-01
 */
@TableName("production_line")
public class ProductionLine {
    
    /** 生产线ID */
    @TableId(value = "line_id", type = IdType.AUTO)
    private Long lineId;
    
    /** 生产线编号 */
    @TableField("line_code")
    private String lineCode;
    
    /** 生产线名称 */
    @TableField("line_name")
    private String lineName;
    
    /** 生产线类型 */
    @TableField("line_type")
    private String lineType;
    
    /** 生产线状态（0-停机 1-运行 2-维护 3-故障 4-待机） */
    @TableField("line_status")
    private Integer lineStatus;
    
    /** 车间ID */
    @TableField("workshop_id")
    private Long workshopId;
    
    /** 车间名称 */
    @TableField("workshop_name")
    private String workshopName;
    
    /** 生产线位置 */
    @TableField("location")
    private String location;
    
    /** 设计产能（件/小时） */
    @TableField("design_capacity")
    private Integer designCapacity;
    
    /** 实际产能（件/小时） */
    @TableField("actual_capacity")
    private Integer actualCapacity;
    
    /** 产能利用率 */
    @TableField("capacity_utilization")
    private BigDecimal capacityUtilization;
    
    /** 负责人ID */
    private Long managerId;
    
    /** 负责人姓名 */
    private String managerName;
    
    /** 班组长ID */
    private Long teamLeaderId;
    
    /** 班组长姓名 */
    private String teamLeaderName;
    
    /** 员工数量 */
    private Integer employeeCount;
    
    /** 设备数量 */
    private Integer equipmentCount;
    
    /** 投产日期 */
    @JsonFormat(pattern = "yyyy-MM-dd")
    private Date commissioningDate;
    
    /** 最后维护时间 */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date lastMaintenanceTime;
    
    /** 下次维护时间 */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date nextMaintenanceTime;
    
    /** 维护周期（天） */
    private Integer maintenanceCycle;
    
    /** 运行时间（小时） */
    private Double runningHours;
    
    /** 停机时间（小时） */
    private Double downtime;
    
    /** 设备综合效率(OEE) */
    private BigDecimal oeeRate;
    
    /** 可用率 */
    private BigDecimal availabilityRate;
    
    /** 性能效率 */
    private BigDecimal performanceRate;
    
    /** 质量合格率 */
    private BigDecimal qualityRate;
    
    /** 状态变更原因 */
    private String statusChangeReason;
    
    /** 状态变更时间 */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date statusChangeTime;
    
    /** 技术参数 */
    private String technicalParameters;
    
    /** 工艺流程 */
    private String processFlow;
    
    /** 安全要求 */
    private String safetyRequirements;
    
    /** 环保要求 */
    private String environmentalRequirements;
    
    /** 生产线描述 */
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
    
    public Long getLineId() {
        return lineId;
    }
    
    public void setLineId(Long lineId) {
        this.lineId = lineId;
    }
    
    public String getLineCode() {
        return lineCode;
    }
    
    public void setLineCode(String lineCode) {
        this.lineCode = lineCode;
    }
    
    public String getLineName() {
        return lineName;
    }
    
    public void setLineName(String lineName) {
        this.lineName = lineName;
    }
    
    public String getLineType() {
        return lineType;
    }
    
    public void setLineType(String lineType) {
        this.lineType = lineType;
    }
    
    public Integer getLineStatus() {
        return lineStatus;
    }
    
    public void setLineStatus(Integer lineStatus) {
        this.lineStatus = lineStatus;
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
    
    public Long getTeamLeaderId() {
        return teamLeaderId;
    }
    
    public void setTeamLeaderId(Long teamLeaderId) {
        this.teamLeaderId = teamLeaderId;
    }
    
    public String getTeamLeaderName() {
        return teamLeaderName;
    }
    
    public void setTeamLeaderName(String teamLeaderName) {
        this.teamLeaderName = teamLeaderName;
    }
    
    public Integer getEmployeeCount() {
        return employeeCount;
    }
    
    public void setEmployeeCount(Integer employeeCount) {
        this.employeeCount = employeeCount;
    }
    
    public Integer getEquipmentCount() {
        return equipmentCount;
    }
    
    public void setEquipmentCount(Integer equipmentCount) {
        this.equipmentCount = equipmentCount;
    }
    
    public Date getCommissioningDate() {
        return commissioningDate;
    }
    
    public void setCommissioningDate(Date commissioningDate) {
        this.commissioningDate = commissioningDate;
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
    
    public Double getRunningHours() {
        return runningHours;
    }
    
    public void setRunningHours(Double runningHours) {
        this.runningHours = runningHours;
    }
    
    public Double getDowntime() {
        return downtime;
    }
    
    public void setDowntime(Double downtime) {
        this.downtime = downtime;
    }
    
    public BigDecimal getOeeRate() {
        return oeeRate;
    }
    
    public void setOeeRate(BigDecimal oeeRate) {
        this.oeeRate = oeeRate;
    }
    
    public BigDecimal getAvailabilityRate() {
        return availabilityRate;
    }
    
    public void setAvailabilityRate(BigDecimal availabilityRate) {
        this.availabilityRate = availabilityRate;
    }
    
    public BigDecimal getPerformanceRate() {
        return performanceRate;
    }
    
    public void setPerformanceRate(BigDecimal performanceRate) {
        this.performanceRate = performanceRate;
    }
    
    public BigDecimal getQualityRate() {
        return qualityRate;
    }
    
    public void setQualityRate(BigDecimal qualityRate) {
        this.qualityRate = qualityRate;
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
    
    public String getTechnicalParameters() {
        return technicalParameters;
    }
    
    public void setTechnicalParameters(String technicalParameters) {
        this.technicalParameters = technicalParameters;
    }
    
    public String getProcessFlow() {
        return processFlow;
    }
    
    public void setProcessFlow(String processFlow) {
        this.processFlow = processFlow;
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
            .append("lineId", getLineId())
            .append("lineCode", getLineCode())
            .append("lineName", getLineName())
            .append("lineType", getLineType())
            .append("lineStatus", getLineStatus())
            .append("workshopId", getWorkshopId())
            .append("workshopName", getWorkshopName())
            .append("location", getLocation())
            .append("designCapacity", getDesignCapacity())
            .append("actualCapacity", getActualCapacity())
            .append("capacityUtilization", getCapacityUtilization())
            .append("managerId", getManagerId())
            .append("managerName", getManagerName())
            .append("teamLeaderId", getTeamLeaderId())
            .append("teamLeaderName", getTeamLeaderName())
            .append("employeeCount", getEmployeeCount())
            .append("equipmentCount", getEquipmentCount())
            .append("commissioningDate", getCommissioningDate())
            .append("lastMaintenanceTime", getLastMaintenanceTime())
            .append("nextMaintenanceTime", getNextMaintenanceTime())
            .append("maintenanceCycle", getMaintenanceCycle())
            .append("runningHours", getRunningHours())
            .append("downtime", getDowntime())
            .append("oeeRate", getOeeRate())
            .append("availabilityRate", getAvailabilityRate())
            .append("performanceRate", getPerformanceRate())
            .append("qualityRate", getQualityRate())
            .append("statusChangeReason", getStatusChangeReason())
            .append("statusChangeTime", getStatusChangeTime())
            .append("technicalParameters", getTechnicalParameters())
            .append("processFlow", getProcessFlow())
            .append("safetyRequirements", getSafetyRequirements())
            .append("environmentalRequirements", getEnvironmentalRequirements())
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