package com.chennian.storytelling.bean.model.asset;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.fasterxml.jackson.annotation.JsonFormat;

import java.io.Serializable;
import java.util.Date;

/**
 * 资产类别实体类
 * 
 * @author storytelling
 * @date 2024-01-01
 */
@TableName("asset_category")
public class AssetCategory implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 类别ID
     */
    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    /**
     * 类别编码
     */
    @TableField(value = "category_code")
    private String categoryCode;

    /**
     * 类别名称
     */
    @TableField(value = "category_name")
    private String categoryName;

    /**
     * 上级类别ID
     */
    @TableField(value = "parent_id")
    private Long parentId;

    /**
     * 上级类别名称
     */
    @TableField(value = "parent_name")
    private String parentName;

    /**
     * 类别描述
     */
    @TableField(value = "description")
    private String description;

    /**
     * 排序
     */
    @TableField(value = "order_num")
    private Integer orderNum;

    /**
     * 状态（1正常 0停用）
     */
    @TableField(value = "status")
    private Integer status;

    /**
     * 层级
     */
    @TableField(value = "level")
    private Integer level;

    /**
     * 路径
     */
    @TableField(value = "path")
    private String path;

    /**
     * 创建时间
     */
    @TableField(value = "create_time")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date createTime;

    /**
     * 更新时间
     */
    @TableField(value = "update_time")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date updateTime;

    /**
     * 创建人
     */
    @TableField(value = "create_by")
    private String createBy;

    /**
     * 更新人
     */
    @TableField(value = "update_by")
    private String updateBy;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getCategoryCode() {
        return categoryCode;
    }

    public void setCategoryCode(String categoryCode) {
        this.categoryCode = categoryCode;
    }

    public String getCategoryName() {
        return categoryName;
    }

    public void setCategoryName(String categoryName) {
        this.categoryName = categoryName;
    }

    public Long getParentId() {
        return parentId;
    }

    public void setParentId(Long parentId) {
        this.parentId = parentId;
    }

    public String getParentName() {
        return parentName;
    }

    public void setParentName(String parentName) {
        this.parentName = parentName;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Integer getOrderNum() {
        return orderNum;
    }

    public void setOrderNum(Integer orderNum) {
        this.orderNum = orderNum;
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public Integer getLevel() {
        return level;
    }

    public void setLevel(Integer level) {
        this.level = level;
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
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
        return "AssetCategory{" +
                "id=" + id +
                ", categoryCode='" + categoryCode + '\'' +
                ", categoryName='" + categoryName + '\'' +
                ", parentId=" + parentId +
                ", parentName='" + parentName + '\'' +
                ", description='" + description + '\'' +
                ", orderNum=" + orderNum +
                ", status=" + status +
                ", level=" + level +
                ", path='" + path + '\'' +
                ", createTime=" + createTime +
                ", updateTime=" + updateTime +
                ", createBy='" + createBy + '\'' +
                ", updateBy='" + updateBy + '\'' +
                '}';
    }
}