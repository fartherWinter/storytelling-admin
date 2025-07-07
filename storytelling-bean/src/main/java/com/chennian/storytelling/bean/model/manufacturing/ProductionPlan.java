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
 * 生产计划实体类
 * 
 * @author storytelling
 * @date 2024-01-01
 */
@TableName("production_plan")
public class ProductionPlan {
    
    /** 计划ID */
    @TableId(value = "plan_id", type = IdType.AUTO)
    private Long planId;
    
    /** 计划编号 */
    @TableField("plan_code")
    private String planCode;
    
    /** 计划名称 */
    @TableField("plan_name")
    private String planName;
    
    /** 产品ID */
    @TableField("product_id")
    private Long productId;
    
    /** 产品名称 */
    @TableField("product_name")
    private String productName;
    
    /** 产品规格 */
    @TableField("product_spec")
    private String productSpec;
    
    /** 计划数量 */
    @TableField("plan_quantity")
    private Integer planQuantity;
    
    /** 已完成数量 */
    @TableField("completed_quantity")
    private Integer completedQuantity;
    
    /** 计划开始时间 */
    @TableField("plan_start_time")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date planStartTime;
    
    /** 计划结束时间 */
    @TableField("plan_end_time")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date planEndTime;
    
    /** 实际开始时间 */
    @TableField("actual_start_time")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date actualStartTime;
    
    /** 实际结束时间 */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date actualEndTime;
    
    /** 计划状态（0-待审核 1-已审核 2-执行中 3-已完成 4-已取消） */
    private Integer planStatus;
    
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
    
    /** 审核状态（0-待审核 1-审核通过 2-审核拒绝） */
    private Integer auditStatus;
    
    /** 审核人ID */
    private Long auditorId;
    
    /** 审核人姓名 */
    private String auditorName;
    
    /** 审核时间 */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date auditTime;
    
    /** 审核备注 */
    private String auditRemark;
    
    /** 计划描述 */
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
    
    public String getPlanName() {
        return planName;
    }
    
    public void setPlanName(String planName) {
        this.planName = planName;
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
    
    public Integer getPlanQuantity() {
        return planQuantity;
    }
    
    public void setPlanQuantity(Integer planQuantity) {
        this.planQuantity = planQuantity;
    }
    
    public Integer getCompletedQuantity() {
        return completedQuantity;
    }
    
    public void setCompletedQuantity(Integer completedQuantity) {
        this.completedQuantity = completedQuantity;
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
    
    public Integer getPlanStatus() {
        return planStatus;
    }
    
    public void setPlanStatus(Integer planStatus) {
        this.planStatus = planStatus;
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
    
    public Integer getAuditStatus() {
        return auditStatus;
    }
    
    public void setAuditStatus(Integer auditStatus) {
        this.auditStatus = auditStatus;
    }
    
    public Long getAuditorId() {
        return auditorId;
    }
    
    public void setAuditorId(Long auditorId) {
        this.auditorId = auditorId;
    }
    
    public String getAuditorName() {
        return auditorName;
    }
    
    public void setAuditorName(String auditorName) {
        this.auditorName = auditorName;
    }
    
    public Date getAuditTime() {
        return auditTime;
    }
    
    public void setAuditTime(Date auditTime) {
        this.auditTime = auditTime;
    }
    
    public String getAuditRemark() {
        return auditRemark;
    }
    
    public void setAuditRemark(String auditRemark) {
        this.auditRemark = auditRemark;
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
            .append("planId", getPlanId())
            .append("planCode", getPlanCode())
            .append("planName", getPlanName())
            .append("productId", getProductId())
            .append("productName", getProductName())
            .append("productSpec", getProductSpec())
            .append("planQuantity", getPlanQuantity())
            .append("completedQuantity", getCompletedQuantity())
            .append("planStartTime", getPlanStartTime())
            .append("planEndTime", getPlanEndTime())
            .append("actualStartTime", getActualStartTime())
            .append("actualEndTime", getActualEndTime())
            .append("planStatus", getPlanStatus())
            .append("priority", getPriority())
            .append("productionLineId", getProductionLineId())
            .append("productionLineName", getProductionLineName())
            .append("managerId", getManagerId())
            .append("managerName", getManagerName())
            .append("planCost", getPlanCost())
            .append("actualCost", getActualCost())
            .append("progress", getProgress())
            .append("auditStatus", getAuditStatus())
            .append("auditorId", getAuditorId())
            .append("auditorName", getAuditorName())
            .append("auditTime", getAuditTime())
            .append("auditRemark", getAuditRemark())
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