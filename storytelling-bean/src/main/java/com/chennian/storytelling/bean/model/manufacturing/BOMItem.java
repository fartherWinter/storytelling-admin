package com.chennian.storytelling.bean.model.manufacturing;

import com.fasterxml.jackson.annotation.JsonFormat;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

import java.math.BigDecimal;
import java.util.Date;

/**
 * BOM明细实体类
 * 
 * @author storytelling
 * @date 2024-01-01
 */
public class BOMItem {
    
    /** BOM明细ID */
    private Long bomItemId;
    
    /** BOM ID */
    private Long bomId;
    
    /** 物料ID */
    private Long materialId;
    
    /** 物料编号 */
    private String materialCode;
    
    /** 物料名称 */
    private String materialName;
    
    /** 物料规格 */
    private String materialSpec;
    
    /** 物料单位 */
    private String materialUnit;
    
    /** 物料类型（1-原材料 2-半成品 3-成品 4-辅料 5-包装材料） */
    private Integer materialType;
    
    /** 需求数量 */
    private BigDecimal requiredQuantity;
    
    /** 基础用量 */
    private BigDecimal baseUsage;
    
    /** 损耗率 */
    private BigDecimal lossRate;
    
    /** 实际用量 */
    private BigDecimal actualUsage;
    
    /** 替代料ID */
    private Long substituteMaterialId;
    
    /** 替代料编号 */
    private String substituteMaterialCode;
    
    /** 替代料名称 */
    private String substituteMaterialName;
    
    /** 替代比例 */
    private BigDecimal substituteRatio;
    
    /** 是否关键物料（0-否 1-是） */
    private Integer isKeyMaterial;
    
    /** 是否可选物料（0-否 1-是） */
    private Integer isOptional;
    
    /** 工序编号 */
    private String processCode;
    
    /** 工序名称 */
    private String processName;
    
    /** 工序顺序 */
    private Integer processSequence;
    
    /** 投料时间 */
    private Integer feedingTime;
    
    /** 投料方式（1-一次投料 2-分批投料 3-连续投料） */
    private Integer feedingMethod;
    
    /** 存储位置 */
    private String storageLocation;
    
    /** 供应商ID */
    private Long supplierId;
    
    /** 供应商名称 */
    private String supplierName;
    
    /** 采购周期（天） */
    private Integer purchaseCycle;
    
    /** 最小库存 */
    private BigDecimal minStock;
    
    /** 最大库存 */
    private BigDecimal maxStock;
    
    /** 安全库存 */
    private BigDecimal safetyStock;
    
    /** 标准成本 */
    private BigDecimal standardCost;
    
    /** 当前成本 */
    private BigDecimal currentCost;
    
    /** 生效日期 */
    @JsonFormat(pattern = "yyyy-MM-dd")
    private Date effectiveDate;
    
    /** 失效日期 */
    @JsonFormat(pattern = "yyyy-MM-dd")
    private Date expiryDate;
    
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
    
    // Getter and Setter methods
    public Long getBomItemId() {
        return bomItemId;
    }
    
    public void setBomItemId(Long bomItemId) {
        this.bomItemId = bomItemId;
    }
    
    public Long getBomId() {
        return bomId;
    }
    
    public void setBomId(Long bomId) {
        this.bomId = bomId;
    }
    
    public Long getMaterialId() {
        return materialId;
    }
    
    public void setMaterialId(Long materialId) {
        this.materialId = materialId;
    }
    
    public String getMaterialCode() {
        return materialCode;
    }
    
    public void setMaterialCode(String materialCode) {
        this.materialCode = materialCode;
    }
    
    public String getMaterialName() {
        return materialName;
    }
    
    public void setMaterialName(String materialName) {
        this.materialName = materialName;
    }
    
    public String getMaterialSpec() {
        return materialSpec;
    }
    
    public void setMaterialSpec(String materialSpec) {
        this.materialSpec = materialSpec;
    }
    
    public String getMaterialUnit() {
        return materialUnit;
    }
    
    public void setMaterialUnit(String materialUnit) {
        this.materialUnit = materialUnit;
    }
    
    public Integer getMaterialType() {
        return materialType;
    }
    
    public void setMaterialType(Integer materialType) {
        this.materialType = materialType;
    }
    
    public BigDecimal getRequiredQuantity() {
        return requiredQuantity;
    }
    
    public void setRequiredQuantity(BigDecimal requiredQuantity) {
        this.requiredQuantity = requiredQuantity;
    }
    
    public BigDecimal getBaseUsage() {
        return baseUsage;
    }
    
    public void setBaseUsage(BigDecimal baseUsage) {
        this.baseUsage = baseUsage;
    }
    
    public BigDecimal getLossRate() {
        return lossRate;
    }
    
    public void setLossRate(BigDecimal lossRate) {
        this.lossRate = lossRate;
    }
    
    public BigDecimal getActualUsage() {
        return actualUsage;
    }
    
    public void setActualUsage(BigDecimal actualUsage) {
        this.actualUsage = actualUsage;
    }
    
    public Long getSubstituteMaterialId() {
        return substituteMaterialId;
    }
    
    public void setSubstituteMaterialId(Long substituteMaterialId) {
        this.substituteMaterialId = substituteMaterialId;
    }
    
    public String getSubstituteMaterialCode() {
        return substituteMaterialCode;
    }
    
    public void setSubstituteMaterialCode(String substituteMaterialCode) {
        this.substituteMaterialCode = substituteMaterialCode;
    }
    
    public String getSubstituteMaterialName() {
        return substituteMaterialName;
    }
    
    public void setSubstituteMaterialName(String substituteMaterialName) {
        this.substituteMaterialName = substituteMaterialName;
    }
    
    public BigDecimal getSubstituteRatio() {
        return substituteRatio;
    }
    
    public void setSubstituteRatio(BigDecimal substituteRatio) {
        this.substituteRatio = substituteRatio;
    }
    
    public Integer getIsKeyMaterial() {
        return isKeyMaterial;
    }
    
    public void setIsKeyMaterial(Integer isKeyMaterial) {
        this.isKeyMaterial = isKeyMaterial;
    }
    
    public Integer getIsOptional() {
        return isOptional;
    }
    
    public void setIsOptional(Integer isOptional) {
        this.isOptional = isOptional;
    }
    
    public String getProcessCode() {
        return processCode;
    }
    
    public void setProcessCode(String processCode) {
        this.processCode = processCode;
    }
    
    public String getProcessName() {
        return processName;
    }
    
    public void setProcessName(String processName) {
        this.processName = processName;
    }
    
    public Integer getProcessSequence() {
        return processSequence;
    }
    
    public void setProcessSequence(Integer processSequence) {
        this.processSequence = processSequence;
    }
    
    public Integer getFeedingTime() {
        return feedingTime;
    }
    
    public void setFeedingTime(Integer feedingTime) {
        this.feedingTime = feedingTime;
    }
    
    public Integer getFeedingMethod() {
        return feedingMethod;
    }
    
    public void setFeedingMethod(Integer feedingMethod) {
        this.feedingMethod = feedingMethod;
    }
    
    public String getStorageLocation() {
        return storageLocation;
    }
    
    public void setStorageLocation(String storageLocation) {
        this.storageLocation = storageLocation;
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
    
    public Integer getPurchaseCycle() {
        return purchaseCycle;
    }
    
    public void setPurchaseCycle(Integer purchaseCycle) {
        this.purchaseCycle = purchaseCycle;
    }
    
    public BigDecimal getMinStock() {
        return minStock;
    }
    
    public void setMinStock(BigDecimal minStock) {
        this.minStock = minStock;
    }
    
    public BigDecimal getMaxStock() {
        return maxStock;
    }
    
    public void setMaxStock(BigDecimal maxStock) {
        this.maxStock = maxStock;
    }
    
    public BigDecimal getSafetyStock() {
        return safetyStock;
    }
    
    public void setSafetyStock(BigDecimal safetyStock) {
        this.safetyStock = safetyStock;
    }
    
    public BigDecimal getStandardCost() {
        return standardCost;
    }
    
    public void setStandardCost(BigDecimal standardCost) {
        this.standardCost = standardCost;
    }
    
    public BigDecimal getCurrentCost() {
        return currentCost;
    }
    
    public void setCurrentCost(BigDecimal currentCost) {
        this.currentCost = currentCost;
    }
    
    public Date getEffectiveDate() {
        return effectiveDate;
    }
    
    public void setEffectiveDate(Date effectiveDate) {
        this.effectiveDate = effectiveDate;
    }
    
    public Date getExpiryDate() {
        return expiryDate;
    }
    
    public void setExpiryDate(Date expiryDate) {
        this.expiryDate = expiryDate;
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
    
    @Override
    public String toString() {
        return new ToStringBuilder(this, ToStringStyle.MULTI_LINE_STYLE)
            .append("bomItemId", getBomItemId())
            .append("bomId", getBomId())
            .append("materialId", getMaterialId())
            .append("materialCode", getMaterialCode())
            .append("materialName", getMaterialName())
            .append("materialSpec", getMaterialSpec())
            .append("materialUnit", getMaterialUnit())
            .append("materialType", getMaterialType())
            .append("requiredQuantity", getRequiredQuantity())
            .append("baseUsage", getBaseUsage())
            .append("lossRate", getLossRate())
            .append("actualUsage", getActualUsage())
            .append("substituteMaterialId", getSubstituteMaterialId())
            .append("substituteMaterialCode", getSubstituteMaterialCode())
            .append("substituteMaterialName", getSubstituteMaterialName())
            .append("substituteRatio", getSubstituteRatio())
            .append("isKeyMaterial", getIsKeyMaterial())
            .append("isOptional", getIsOptional())
            .append("processCode", getProcessCode())
            .append("processName", getProcessName())
            .append("processSequence", getProcessSequence())
            .append("feedingTime", getFeedingTime())
            .append("feedingMethod", getFeedingMethod())
            .append("storageLocation", getStorageLocation())
            .append("supplierId", getSupplierId())
            .append("supplierName", getSupplierName())
            .append("purchaseCycle", getPurchaseCycle())
            .append("minStock", getMinStock())
            .append("maxStock", getMaxStock())
            .append("safetyStock", getSafetyStock())
            .append("standardCost", getStandardCost())
            .append("currentCost", getCurrentCost())
            .append("effectiveDate", getEffectiveDate())
            .append("expiryDate", getExpiryDate())
            .append("remark", getRemark())
            .append("createBy", getCreateBy())
            .append("createTime", getCreateTime())
            .append("updateBy", getUpdateBy())
            .append("updateTime", getUpdateTime())
            .toString();
    }
}