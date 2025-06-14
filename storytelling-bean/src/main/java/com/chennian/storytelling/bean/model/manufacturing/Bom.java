package com.chennian.storytelling.bean.model.manufacturing;

import com.fasterxml.jackson.annotation.JsonFormat;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

import java.math.BigDecimal;
import java.util.Date;

/**
 * BOM物料清单实体类
 * 
 * @author storytelling
 * @date 2024-01-01
 */
public class Bom {
    
    /** BOM ID */
    private Long bomId;
    
    /** BOM编号 */
    private String bomCode;
    
    /** BOM名称 */
    private String bomName;
    
    /** BOM版本 */
    private String bomVersion;
    
    /** BOM类型（1-标准BOM 2-工程BOM 3-制造BOM 4-成本BOM） */
    private Integer bomType;
    
    /** BOM状态（0-草稿 1-审核中 2-已审核 3-已发布 4-已冻结 5-已作废） */
    private Integer bomStatus;
    
    /** 产品ID */
    private Long productId;
    
    /** 产品编号 */
    private String productCode;
    
    /** 产品名称 */
    private String productName;
    
    /** 产品规格 */
    private String productSpec;
    
    /** 产品单位 */
    private String productUnit;
    
    /** 基础数量 */
    private BigDecimal baseQuantity;
    
    /** 生产批量 */
    private BigDecimal batchSize;
    
    /** 损耗率 */
    private BigDecimal lossRate;
    
    /** 工艺路线ID */
    private Long routingId;
    
    /** 工艺路线名称 */
    private String routingName;
    
    /** 生产线ID */
    private Long lineId;
    
    /** 生产线名称 */
    private String lineName;
    
    /** 车间ID */
    private Long workshopId;
    
    /** 车间名称 */
    private String workshopName;
    
    /** 设计部门ID */
    private Long designDeptId;
    
    /** 设计部门名称 */
    private String designDeptName;
    
    /** 设计人员ID */
    private Long designerId;
    
    /** 设计人员姓名 */
    private String designerName;
    
    /** 工艺部门ID */
    private Long processDeptId;
    
    /** 工艺部门名称 */
    private String processDeptName;
    
    /** 工艺人员ID */
    private Long processEngineerId;
    
    /** 工艺人员姓名 */
    private String processEngineerName;
    
    /** 审核人员ID */
    private Long reviewerId;
    
    /** 审核人员姓名 */
    private String reviewerName;
    
    /** 审核时间 */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date reviewTime;
    
    /** 发布人员ID */
    private Long publisherId;
    
    /** 发布人员姓名 */
    private String publisherName;
    
    /** 发布时间 */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date publishTime;
    
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

    /** 版本 */
    private String version;

    public String getVersion() {
        return version;
    }

    public void setVersion(String version) {
        this.version = version;
    }

    // Getter and Setter methods
    public Long getBomId() {
        return bomId;
    }
    
    public void setBomId(Long bomId) {
        this.bomId = bomId;
    }
    
    public String getBomCode() {
        return bomCode;
    }
    
    public void setBomCode(String bomCode) {
        this.bomCode = bomCode;
    }
    
    public String getBomName() {
        return bomName;
    }
    
    public void setBomName(String bomName) {
        this.bomName = bomName;
    }
    
    public String getBomVersion() {
        return bomVersion;
    }
    
    public void setBomVersion(String bomVersion) {
        this.bomVersion = bomVersion;
    }
    
    public Integer getBomType() {
        return bomType;
    }
    
    public void setBomType(Integer bomType) {
        this.bomType = bomType;
    }
    
    public Integer getBomStatus() {
        return bomStatus;
    }
    
    public void setBomStatus(Integer bomStatus) {
        this.bomStatus = bomStatus;
    }
    
    public Long getProductId() {
        return productId;
    }
    
    public void setProductId(Long productId) {
        this.productId = productId;
    }
    
    public String getProductCode() {
        return productCode;
    }
    
    public void setProductCode(String productCode) {
        this.productCode = productCode;
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
    
    public String getProductUnit() {
        return productUnit;
    }
    
    public void setProductUnit(String productUnit) {
        this.productUnit = productUnit;
    }
    
    public BigDecimal getBaseQuantity() {
        return baseQuantity;
    }
    
    public void setBaseQuantity(BigDecimal baseQuantity) {
        this.baseQuantity = baseQuantity;
    }
    
    public BigDecimal getBatchSize() {
        return batchSize;
    }
    
    public void setBatchSize(BigDecimal batchSize) {
        this.batchSize = batchSize;
    }
    
    public BigDecimal getLossRate() {
        return lossRate;
    }
    
    public void setLossRate(BigDecimal lossRate) {
        this.lossRate = lossRate;
    }
    
    public Long getRoutingId() {
        return routingId;
    }
    
    public void setRoutingId(Long routingId) {
        this.routingId = routingId;
    }
    
    public String getRoutingName() {
        return routingName;
    }
    
    public void setRoutingName(String routingName) {
        this.routingName = routingName;
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
    
    public Long getDesignDeptId() {
        return designDeptId;
    }
    
    public void setDesignDeptId(Long designDeptId) {
        this.designDeptId = designDeptId;
    }
    
    public String getDesignDeptName() {
        return designDeptName;
    }
    
    public void setDesignDeptName(String designDeptName) {
        this.designDeptName = designDeptName;
    }
    
    public Long getDesignerId() {
        return designerId;
    }
    
    public void setDesignerId(Long designerId) {
        this.designerId = designerId;
    }
    
    public String getDesignerName() {
        return designerName;
    }
    
    public void setDesignerName(String designerName) {
        this.designerName = designerName;
    }
    
    public Long getProcessDeptId() {
        return processDeptId;
    }
    
    public void setProcessDeptId(Long processDeptId) {
        this.processDeptId = processDeptId;
    }
    
    public String getProcessDeptName() {
        return processDeptName;
    }
    
    public void setProcessDeptName(String processDeptName) {
        this.processDeptName = processDeptName;
    }
    
    public Long getProcessEngineerId() {
        return processEngineerId;
    }
    
    public void setProcessEngineerId(Long processEngineerId) {
        this.processEngineerId = processEngineerId;
    }
    
    public String getProcessEngineerName() {
        return processEngineerName;
    }
    
    public void setProcessEngineerName(String processEngineerName) {
        this.processEngineerName = processEngineerName;
    }
    
    public Long getReviewerId() {
        return reviewerId;
    }
    
    public void setReviewerId(Long reviewerId) {
        this.reviewerId = reviewerId;
    }
    
    public String getReviewerName() {
        return reviewerName;
    }
    
    public void setReviewerName(String reviewerName) {
        this.reviewerName = reviewerName;
    }
    
    public Date getReviewTime() {
        return reviewTime;
    }
    
    public void setReviewTime(Date reviewTime) {
        this.reviewTime = reviewTime;
    }
    
    public Long getPublisherId() {
        return publisherId;
    }
    
    public void setPublisherId(Long publisherId) {
        this.publisherId = publisherId;
    }
    
    public String getPublisherName() {
        return publisherName;
    }
    
    public void setPublisherName(String publisherName) {
        this.publisherName = publisherName;
    }
    
    public Date getPublishTime() {
        return publishTime;
    }
    
    public void setPublishTime(Date publishTime) {
        this.publishTime = publishTime;
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
            .append("bomId", getBomId())
            .append("bomCode", getBomCode())
            .append("bomName", getBomName())
            .append("bomVersion", getBomVersion())
            .append("bomType", getBomType())
            .append("bomStatus", getBomStatus())
            .append("productId", getProductId())
            .append("productCode", getProductCode())
            .append("productName", getProductName())
            .append("productSpec", getProductSpec())
            .append("productUnit", getProductUnit())
            .append("baseQuantity", getBaseQuantity())
            .append("batchSize", getBatchSize())
            .append("lossRate", getLossRate())
            .append("routingId", getRoutingId())
            .append("routingName", getRoutingName())
            .append("lineId", getLineId())
            .append("lineName", getLineName())
            .append("workshopId", getWorkshopId())
            .append("workshopName", getWorkshopName())
            .append("designDeptId", getDesignDeptId())
            .append("designDeptName", getDesignDeptName())
            .append("designerId", getDesignerId())
            .append("designerName", getDesignerName())
            .append("processDeptId", getProcessDeptId())
            .append("processDeptName", getProcessDeptName())
            .append("processEngineerId", getProcessEngineerId())
            .append("processEngineerName", getProcessEngineerName())
            .append("reviewerId", getReviewerId())
            .append("reviewerName", getReviewerName())
            .append("reviewTime", getReviewTime())
            .append("publisherId", getPublisherId())
            .append("publisherName", getPublisherName())
            .append("publishTime", getPublishTime())
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