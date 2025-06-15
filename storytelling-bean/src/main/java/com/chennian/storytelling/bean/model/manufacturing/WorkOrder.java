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
 * 工单实体类
 * 
 * @author storytelling
 * @date 2024-01-01
 */
@TableName("work_order")
public class WorkOrder {
    
    /** 工单ID */
    @TableId(value = "work_order_id", type = IdType.AUTO)
    private Long workOrderId;
    
    /** 工单编号 */
    @TableField("work_order_code")
    private String workOrderCode;
    
    /** 工单名称 */
    @TableField("work_order_name")
    private String workOrderName;
    
    /** 生产订单ID */
    @TableField("production_order_id")
    private Long productionOrderId;
    
    /** 生产订单编号 */
    @TableField("production_order_code")
    private String productionOrderCode;
    
    /** 产品ID */
    @TableField("product_id")
    private Long productId;
    
    /** 产品名称 */
    @TableField("product_name")
    private String productName;
    
    /** 产品规格 */
    @TableField("product_spec")
    private String productSpec;
    
    /** 工序名称 */
    @TableField("process_name")
    private String processName;
    
    /** 工序编号 */
    @TableField("process_code")
    private String processCode;
    
    /** 工单数量 */
    @TableField("work_order_quantity")
    private Integer workOrderQuantity;
    
    /** 已完成数量 */
    private Integer completedQuantity;
    
    /** 合格数量 */
    private Integer qualifiedQuantity;
    
    /** 不合格数量 */
    private Integer unqualifiedQuantity;
    
    /** 计划开始时间 */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date planStartTime;
    
    /** 计划结束时间 */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date planEndTime;
    
    /** 实际开始时间 */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date actualStartTime;
    
    /** 实际结束时间 */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date actualEndTime;
    
    /** 工单状态（0-待派工 1-已派工 2-进行中 3-已完工 4-已取消） */
    private Integer workOrderStatus;
    
    /** 优先级（1-低 2-中 3-高 4-紧急） */
    private Integer priority;
    
    /** 生产线ID */
    private Long productionLineId;
    
    /** 生产线名称 */
    private String productionLineName;
    
    /** 设备ID */
    private Long equipmentId;
    
    /** 设备名称 */
    private String equipmentName;
    
    /** 操作员ID */
    private Long operatorId;
    
    /** 操作员姓名 */
    private String operatorName;
    
    /** 计划工时 */
    private Double planWorkHours;
    
    /** 实际工时 */
    private Double actualWorkHours;
    
    /** 标准工时 */
    private Double standardWorkHours;
    
    /** 工时效率 */
    private BigDecimal workHourEfficiency;
    
    /** 计划成本 */
    private BigDecimal planCost;
    
    /** 实际成本 */
    private BigDecimal actualCost;
    
    /** 完成进度 */
    private BigDecimal progress;
    
    /** 质量要求 */
    private String qualityRequirement;
    
    /** 工艺要求 */
    private String processRequirement;
    
    /** 技术要求 */
    private String technicalRequirement;
    
    /** 安全要求 */
    private String safetyRequirement;
    
    /** 派工时间 */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date assignTime;
    
    /** 派工人ID */
    private Long assignerId;
    
    /** 派工人姓名 */
    private String assignerName;
    
    /** 工单描述 */
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
    
    public Long getWorkOrderId() {
        return workOrderId;
    }
    
    public void setWorkOrderId(Long workOrderId) {
        this.workOrderId = workOrderId;
    }
    
    public String getWorkOrderCode() {
        return workOrderCode;
    }
    
    public void setWorkOrderCode(String workOrderCode) {
        this.workOrderCode = workOrderCode;
    }
    
    public String getWorkOrderName() {
        return workOrderName;
    }
    
    public void setWorkOrderName(String workOrderName) {
        this.workOrderName = workOrderName;
    }
    
    public Long getProductionOrderId() {
        return productionOrderId;
    }
    
    public void setProductionOrderId(Long productionOrderId) {
        this.productionOrderId = productionOrderId;
    }
    
    public String getProductionOrderCode() {
        return productionOrderCode;
    }
    
    public void setProductionOrderCode(String productionOrderCode) {
        this.productionOrderCode = productionOrderCode;
    }
    
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
    
    public String getProductSpec() {
        return productSpec;
    }
    
    public void setProductSpec(String productSpec) {
        this.productSpec = productSpec;
    }
    
    public String getProcessName() {
        return processName;
    }
    
    public void setProcessName(String processName) {
        this.processName = processName;
    }
    
    public String getProcessCode() {
        return processCode;
    }
    
    public void setProcessCode(String processCode) {
        this.processCode = processCode;
    }
    
    public Integer getWorkOrderQuantity() {
        return workOrderQuantity;
    }
    
    public void setWorkOrderQuantity(Integer workOrderQuantity) {
        this.workOrderQuantity = workOrderQuantity;
    }
    
    public Integer getCompletedQuantity() {
        return completedQuantity;
    }
    
    public void setCompletedQuantity(Integer completedQuantity) {
        this.completedQuantity = completedQuantity;
    }
    
    public Integer getQualifiedQuantity() {
        return qualifiedQuantity;
    }
    
    public void setQualifiedQuantity(Integer qualifiedQuantity) {
        this.qualifiedQuantity = qualifiedQuantity;
    }
    
    public Integer getUnqualifiedQuantity() {
        return unqualifiedQuantity;
    }
    
    public void setUnqualifiedQuantity(Integer unqualifiedQuantity) {
        this.unqualifiedQuantity = unqualifiedQuantity;
    }
    
    public Date getPlanStartTime() {
        return planStartTime;
    }
    
    public void setPlanStartTime(Date planStartTime) {
        this.planStartTime = planStartTime;
    }
    
    public Date getPlanEndTime() {
        return planEndTime;
    }
    
    public void setPlanEndTime(Date planEndTime) {
        this.planEndTime = planEndTime;
    }
    
    public Date getActualStartTime() {
        return actualStartTime;
    }
    
    public void setActualStartTime(Date actualStartTime) {
        this.actualStartTime = actualStartTime;
    }
    
    public Date getActualEndTime() {
        return actualEndTime;
    }
    
    public void setActualEndTime(Date actualEndTime) {
        this.actualEndTime = actualEndTime;
    }
    
    public Integer getWorkOrderStatus() {
        return workOrderStatus;
    }
    
    public void setWorkOrderStatus(Integer workOrderStatus) {
        this.workOrderStatus = workOrderStatus;
    }
    
    public Integer getPriority() {
        return priority;
    }
    
    public void setPriority(Integer priority) {
        this.priority = priority;
    }
    
    public Long getProductionLineId() {
        return productionLineId;
    }
    
    public void setProductionLineId(Long productionLineId) {
        this.productionLineId = productionLineId;
    }
    
    public String getProductionLineName() {
        return productionLineName;
    }
    
    public void setProductionLineName(String productionLineName) {
        this.productionLineName = productionLineName;
    }
    
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
    
    public Double getPlanWorkHours() {
        return planWorkHours;
    }
    
    public void setPlanWorkHours(Double planWorkHours) {
        this.planWorkHours = planWorkHours;
    }
    
    public Double getActualWorkHours() {
        return actualWorkHours;
    }
    
    public void setActualWorkHours(Double actualWorkHours) {
        this.actualWorkHours = actualWorkHours;
    }
    
    public Double getStandardWorkHours() {
        return standardWorkHours;
    }
    
    public void setStandardWorkHours(Double standardWorkHours) {
        this.standardWorkHours = standardWorkHours;
    }
    
    public BigDecimal getWorkHourEfficiency() {
        return workHourEfficiency;
    }
    
    public void setWorkHourEfficiency(BigDecimal workHourEfficiency) {
        this.workHourEfficiency = workHourEfficiency;
    }
    
    public BigDecimal getPlanCost() {
        return planCost;
    }
    
    public void setPlanCost(BigDecimal planCost) {
        this.planCost = planCost;
    }
    
    public BigDecimal getActualCost() {
        return actualCost;
    }
    
    public void setActualCost(BigDecimal actualCost) {
        this.actualCost = actualCost;
    }
    
    public BigDecimal getProgress() {
        return progress;
    }
    
    public void setProgress(BigDecimal progress) {
        this.progress = progress;
    }
    
    public String getQualityRequirement() {
        return qualityRequirement;
    }
    
    public void setQualityRequirement(String qualityRequirement) {
        this.qualityRequirement = qualityRequirement;
    }
    
    public String getProcessRequirement() {
        return processRequirement;
    }
    
    public void setProcessRequirement(String processRequirement) {
        this.processRequirement = processRequirement;
    }
    
    public String getTechnicalRequirement() {
        return technicalRequirement;
    }
    
    public void setTechnicalRequirement(String technicalRequirement) {
        this.technicalRequirement = technicalRequirement;
    }
    
    public String getSafetyRequirement() {
        return safetyRequirement;
    }
    
    public void setSafetyRequirement(String safetyRequirement) {
        this.safetyRequirement = safetyRequirement;
    }
    
    public Date getAssignTime() {
        return assignTime;
    }
    
    public void setAssignTime(Date assignTime) {
        this.assignTime = assignTime;
    }
    
    public Long getAssignerId() {
        return assignerId;
    }
    
    public void setAssignerId(Long assignerId) {
        this.assignerId = assignerId;
    }
    
    public String getAssignerName() {
        return assignerName;
    }
    
    public void setAssignerName(String assignerName) {
        this.assignerName = assignerName;
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
            .append("workOrderId", getWorkOrderId())
            .append("workOrderCode", getWorkOrderCode())
            .append("workOrderName", getWorkOrderName())
            .append("productionOrderId", getProductionOrderId())
            .append("productionOrderCode", getProductionOrderCode())
            .append("productId", getProductId())
            .append("productName", getProductName())
            .append("productSpec", getProductSpec())
            .append("processName", getProcessName())
            .append("processCode", getProcessCode())
            .append("workOrderQuantity", getWorkOrderQuantity())
            .append("completedQuantity", getCompletedQuantity())
            .append("qualifiedQuantity", getQualifiedQuantity())
            .append("unqualifiedQuantity", getUnqualifiedQuantity())
            .append("planStartTime", getPlanStartTime())
            .append("planEndTime", getPlanEndTime())
            .append("actualStartTime", getActualStartTime())
            .append("actualEndTime", getActualEndTime())
            .append("workOrderStatus", getWorkOrderStatus())
            .append("priority", getPriority())
            .append("productionLineId", getProductionLineId())
            .append("productionLineName", getProductionLineName())
            .append("equipmentId", getEquipmentId())
            .append("equipmentName", getEquipmentName())
            .append("operatorId", getOperatorId())
            .append("operatorName", getOperatorName())
            .append("planWorkHours", getPlanWorkHours())
            .append("actualWorkHours", getActualWorkHours())
            .append("standardWorkHours", getStandardWorkHours())
            .append("workHourEfficiency", getWorkHourEfficiency())
            .append("planCost", getPlanCost())
            .append("actualCost", getActualCost())
            .append("progress", getProgress())
            .append("qualityRequirement", getQualityRequirement())
            .append("processRequirement", getProcessRequirement())
            .append("technicalRequirement", getTechnicalRequirement())
            .append("safetyRequirement", getSafetyRequirement())
            .append("assignTime", getAssignTime())
            .append("assignerId", getAssignerId())
            .append("assignerName", getAssignerName())
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