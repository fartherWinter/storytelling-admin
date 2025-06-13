package com.chennian.storytelling.bean.model.asset;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.fasterxml.jackson.annotation.JsonFormat;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

/**
 * 资产维护记录实体类
 * 
 * @author storytelling
 * @date 2024-01-01
 */
@TableName("asset_maintenance")
public class AssetMaintenance implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 维护记录ID
     */
    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    /**
     * 资产ID
     */
    private Long assetId;

    /**
     * 资产编号
     */
    private String assetNo;

    /**
     * 资产名称
     */
    private String assetName;

    /**
     * 维护类型（1保养 2维修 3检查 4升级）
     */
    private Integer maintenanceType;

    /**
     * 维护日期
     */
    @JsonFormat(pattern = "yyyy-MM-dd")
    private Date maintenanceDate;

    /**
     * 维护内容
     */
    private String maintenanceContent;

    /**
     * 故障描述
     */
    private String faultDescription;

    /**
     * 维护结果
     */
    private String maintenanceResult;

    /**
     * 维护费用
     */
    private BigDecimal maintenanceCost;

    /**
     * 维护人员
     */
    private String maintenancePersonnel;

    /**
     * 维护单位
     */
    private String maintenanceUnit;

    /**
     * 下次维护日期
     */
    @JsonFormat(pattern = "yyyy-MM-dd")
    private Date nextMaintenanceDate;

    /**
     * 维护状态（1待维护 2维护中 3已完成）
     */
    private Integer status;

    /**
     * 备注
     */
    private String remark;

    /**
     * 创建时间
     */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date createTime;

    /**
     * 更新时间
     */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date updateTime;

    /**
     * 创建人
     */
    private String createBy;

    /**
     * 更新人
     */
    private String updateBy;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getAssetId() {
        return assetId;
    }

    public void setAssetId(Long assetId) {
        this.assetId = assetId;
    }

    public String getAssetNo() {
        return assetNo;
    }

    public void setAssetNo(String assetNo) {
        this.assetNo = assetNo;
    }

    public String getAssetName() {
        return assetName;
    }

    public void setAssetName(String assetName) {
        this.assetName = assetName;
    }

    public Integer getMaintenanceType() {
        return maintenanceType;
    }

    public void setMaintenanceType(Integer maintenanceType) {
        this.maintenanceType = maintenanceType;
    }

    public Date getMaintenanceDate() {
        return maintenanceDate;
    }

    public void setMaintenanceDate(Date maintenanceDate) {
        this.maintenanceDate = maintenanceDate;
    }

    public String getMaintenanceContent() {
        return maintenanceContent;
    }

    public void setMaintenanceContent(String maintenanceContent) {
        this.maintenanceContent = maintenanceContent;
    }

    public String getFaultDescription() {
        return faultDescription;
    }

    public void setFaultDescription(String faultDescription) {
        this.faultDescription = faultDescription;
    }

    public String getMaintenanceResult() {
        return maintenanceResult;
    }

    public void setMaintenanceResult(String maintenanceResult) {
        this.maintenanceResult = maintenanceResult;
    }

    public BigDecimal getMaintenanceCost() {
        return maintenanceCost;
    }

    public void setMaintenanceCost(BigDecimal maintenanceCost) {
        this.maintenanceCost = maintenanceCost;
    }

    public String getMaintenancePersonnel() {
        return maintenancePersonnel;
    }

    public void setMaintenancePersonnel(String maintenancePersonnel) {
        this.maintenancePersonnel = maintenancePersonnel;
    }

    public String getMaintenanceUnit() {
        return maintenanceUnit;
    }

    public void setMaintenanceUnit(String maintenanceUnit) {
        this.maintenanceUnit = maintenanceUnit;
    }

    public Date getNextMaintenanceDate() {
        return nextMaintenanceDate;
    }

    public void setNextMaintenanceDate(Date nextMaintenanceDate) {
        this.nextMaintenanceDate = nextMaintenanceDate;
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    public Date getUpdateTime() {
        return updateTime;
    }

    public void setUpdateTime(Date updateTime) {
        this.updateTime = updateTime;
    }

    public String getCreateBy() {
        return createBy;
    }

    public void setCreateBy(String createBy) {
        this.createBy = createBy;
    }

    public String getUpdateBy() {
        return updateBy;
    }

    public void setUpdateBy(String updateBy) {
        this.updateBy = updateBy;
    }

    @Override
    public String toString() {
        return "AssetMaintenance{" +
                "id=" + id +
                ", assetId=" + assetId +
                ", assetNo='" + assetNo + '\'' +
                ", assetName='" + assetName + '\'' +
                ", maintenanceType=" + maintenanceType +
                ", maintenanceDate=" + maintenanceDate +
                ", maintenanceContent='" + maintenanceContent + '\'' +
                ", faultDescription='" + faultDescription + '\'' +
                ", maintenanceResult='" + maintenanceResult + '\'' +
                ", maintenanceCost=" + maintenanceCost +
                ", maintenancePersonnel='" + maintenancePersonnel + '\'' +
                ", maintenanceUnit='" + maintenanceUnit + '\'' +
                ", nextMaintenanceDate=" + nextMaintenanceDate +
                ", status=" + status +
                ", remark='" + remark + '\'' +
                ", createTime=" + createTime +
                ", updateTime=" + updateTime +
                ", createBy='" + createBy + '\'' +
                ", updateBy='" + updateBy + '\'' +
                '}';
    }
}