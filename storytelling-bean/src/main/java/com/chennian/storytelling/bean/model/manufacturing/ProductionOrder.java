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
 * 生产订单实体类
 * 
 * @author storytelling
 * @date 2024-01-01
 */
@TableName("production_order")
public class ProductionOrder {
    
    /** 订单ID */
    @TableId(value = "order_id", type = IdType.AUTO)
    private Long orderId;
    
    /** 订单编号 */
    @TableField("order_code")
    private String orderCode;
    
    /** 订单名称 */
    @TableField("order_name")
    private String orderName;
    
    /** 生产计划ID */
    @TableField("plan_id")
    private Long planId;
    
    /** 生产计划编号 */
    @TableField("plan_code")
    private String planCode;
    
    /** 产品ID */
    @TableField("product_id")
    private Long productId;
    
    /** 产品名称 */
    @TableField("product_name")
    private String productName;
    
    /** 产品规格 */
    @TableField("product_spec")
    private String productSpec;
    
    /** 订单数量 */
    @TableField("order_quantity")
    private Integer orderQuantity;
    
    /** 已完成数量 */
    @TableField("completed_quantity")
    private Integer completedQuantity;
    
    /** 合格数量 */
    @TableField("qualified_quantity")
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
    
    /** 订单状态（0-待开工 1-生产中 2-已暂停 3-已完工 4-已取消） */
    private Integer orderStatus;
    
    /** 优先级（1-低 2-中 3-高 4-紧急） */
    private Integer priority;
    
    /** 生产线ID */
    private Long productionLineId;
    
    /** 生产线名称 */
    private String productionLineName;
    
    /** 负责人ID */
    private Long managerId;
    
    /** 负责人姓名 */
    private String managerName;
    
    /** 计划成本 */
    private BigDecimal planCost;
    
    /** 实际成本 */
    private BigDecimal actualCost;
    
    /** 完成进度 */
    private BigDecimal progress;
    
    /** 暂停原因 */
    private String pauseReason;
    
    /** 暂停时间 */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date pauseTime;
    
    /** 质量要求 */
    private String qualityRequirement;
    
    /** 工艺要求 */
    private String processRequirement;
    
    /** 订单描述 */
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
    
    public Long getOrderId() {
        return orderId;
    }
    
    public void setOrderId(Long orderId) {
        this.orderId = orderId;
    }
    
    public String getOrderCode() {
        return orderCode;
    }
    
    public void setOrderCode(String orderCode) {
        this.orderCode = orderCode;
    }
    
    public String getOrderName() {
        return orderName;
    }
    
    public void setOrderName(String orderName) {
        this.orderName = orderName;
    }
    
    public Long getPlanId() {
        return planId;
    }
    
    public void setPlanId(Long planId) {
        this.planId = planId;
    }
    
    public String getPlanCode() {
        return planCode;
    }
    
    public void setPlanCode(String planCode) {
        this.planCode = planCode;
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
    
    public Integer getOrderQuantity() {
        return orderQuantity;
    }
    
    public void setOrderQuantity(Integer orderQuantity) {
        this.orderQuantity = orderQuantity;
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
    
    public Integer getOrderStatus() {
        return orderStatus;
    }
    
    public void setOrderStatus(Integer orderStatus) {
        this.orderStatus = orderStatus;
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
    
    public String getPauseReason() {
        return pauseReason;
    }
    
    public void setPauseReason(String pauseReason) {
        this.pauseReason = pauseReason;
    }
    
    public Date getPauseTime() {
        return pauseTime;
    }
    
    public void setPauseTime(Date pauseTime) {
        this.pauseTime = pauseTime;
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
            .append("orderId", getOrderId())
            .append("orderCode", getOrderCode())
            .append("orderName", getOrderName())
            .append("planId", getPlanId())
            .append("planCode", getPlanCode())
            .append("productId", getProductId())
            .append("productName", getProductName())
            .append("productSpec", getProductSpec())
            .append("orderQuantity", getOrderQuantity())
            .append("completedQuantity", getCompletedQuantity())
            .append("qualifiedQuantity", getQualifiedQuantity())
            .append("unqualifiedQuantity", getUnqualifiedQuantity())
            .append("planStartTime", getPlanStartTime())
            .append("planEndTime", getPlanEndTime())
            .append("actualStartTime", getActualStartTime())
            .append("actualEndTime", getActualEndTime())
            .append("orderStatus", getOrderStatus())
            .append("priority", getPriority())
            .append("productionLineId", getProductionLineId())
            .append("productionLineName", getProductionLineName())
            .append("managerId", getManagerId())
            .append("managerName", getManagerName())
            .append("planCost", getPlanCost())
            .append("actualCost", getActualCost())
            .append("progress", getProgress())
            .append("pauseReason", getPauseReason())
            .append("pauseTime", getPauseTime())
            .append("qualityRequirement", getQualityRequirement())
            .append("processRequirement", getProcessRequirement())
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