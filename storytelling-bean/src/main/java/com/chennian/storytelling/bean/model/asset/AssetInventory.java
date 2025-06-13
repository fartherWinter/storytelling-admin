package com.chennian.storytelling.bean.model.asset;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.fasterxml.jackson.annotation.JsonFormat;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

/**
 * 资产盘点记录实体类
 * 
 * @author storytelling
 * @date 2024-01-01
 */
@TableName("asset_inventory")
public class AssetInventory implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 盘点记录ID
     */
    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    /**
     * 盘点批次号
     */
    private String inventoryBatchNo;

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
     * 账面数量
     */
    private Integer bookQuantity;

    /**
     * 实盘数量
     */
    private Integer actualQuantity;

    /**
     * 盈亏数量
     */
    private Integer differenceQuantity;

    /**
     * 账面价值
     */
    private BigDecimal bookValue;

    /**
     * 实盘价值
     */
    private BigDecimal actualValue;

    /**
     * 盈亏价值
     */
    private BigDecimal differenceValue;

    /**
     * 盘点日期
     */
    @JsonFormat(pattern = "yyyy-MM-dd")
    private Date inventoryDate;

    /**
     * 盘点人员
     */
    private String inventoryPersonnel;

    /**
     * 盘点结果（1盘盈 2盘亏 3账实相符）
     */
    private Integer inventoryResult;

    /**
     * 差异原因
     */
    private String differenceReason;

    /**
     * 处理方式
     */
    private String handleMethod;

    /**
     * 盘点状态（1待盘点 2已盘点 3已处理）
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

    public String getInventoryBatchNo() {
        return inventoryBatchNo;
    }

    public void setInventoryBatchNo(String inventoryBatchNo) {
        this.inventoryBatchNo = inventoryBatchNo;
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

    public Integer getBookQuantity() {
        return bookQuantity;
    }

    public void setBookQuantity(Integer bookQuantity) {
        this.bookQuantity = bookQuantity;
    }

    public Integer getActualQuantity() {
        return actualQuantity;
    }

    public void setActualQuantity(Integer actualQuantity) {
        this.actualQuantity = actualQuantity;
    }

    public Integer getDifferenceQuantity() {
        return differenceQuantity;
    }

    public void setDifferenceQuantity(Integer differenceQuantity) {
        this.differenceQuantity = differenceQuantity;
    }

    public BigDecimal getBookValue() {
        return bookValue;
    }

    public void setBookValue(BigDecimal bookValue) {
        this.bookValue = bookValue;
    }

    public BigDecimal getActualValue() {
        return actualValue;
    }

    public void setActualValue(BigDecimal actualValue) {
        this.actualValue = actualValue;
    }

    public BigDecimal getDifferenceValue() {
        return differenceValue;
    }

    public void setDifferenceValue(BigDecimal differenceValue) {
        this.differenceValue = differenceValue;
    }

    public Date getInventoryDate() {
        return inventoryDate;
    }

    public void setInventoryDate(Date inventoryDate) {
        this.inventoryDate = inventoryDate;
    }

    public String getInventoryPersonnel() {
        return inventoryPersonnel;
    }

    public void setInventoryPersonnel(String inventoryPersonnel) {
        this.inventoryPersonnel = inventoryPersonnel;
    }

    public Integer getInventoryResult() {
        return inventoryResult;
    }

    public void setInventoryResult(Integer inventoryResult) {
        this.inventoryResult = inventoryResult;
    }

    public String getDifferenceReason() {
        return differenceReason;
    }

    public void setDifferenceReason(String differenceReason) {
        this.differenceReason = differenceReason;
    }

    public String getHandleMethod() {
        return handleMethod;
    }

    public void setHandleMethod(String handleMethod) {
        this.handleMethod = handleMethod;
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
        return "AssetInventory{" +
                "id=" + id +
                ", inventoryBatchNo='" + inventoryBatchNo + '\'' +
                ", assetId=" + assetId +
                ", assetNo='" + assetNo + '\'' +
                ", assetName='" + assetName + '\'' +
                ", bookQuantity=" + bookQuantity +
                ", actualQuantity=" + actualQuantity +
                ", differenceQuantity=" + differenceQuantity +
                ", bookValue=" + bookValue +
                ", actualValue=" + actualValue +
                ", differenceValue=" + differenceValue +
                ", inventoryDate=" + inventoryDate +
                ", inventoryPersonnel='" + inventoryPersonnel + '\'' +
                ", inventoryResult=" + inventoryResult +
                ", differenceReason='" + differenceReason + '\'' +
                ", handleMethod='" + handleMethod + '\'' +
                ", status=" + status +
                ", remark='" + remark + '\'' +
                ", createTime=" + createTime +
                ", updateTime=" + updateTime +
                ", createBy='" + createBy + '\'' +
                ", updateBy='" + updateBy + '\'' +
                '}';
    }
}