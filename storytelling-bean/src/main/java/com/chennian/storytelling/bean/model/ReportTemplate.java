package com.chennian.storytelling.bean.model;

import java.io.Serializable;
import java.util.Date;

/**
 * 报表模板实体类
 * 用于定义报表的基本结构和配置
 *
 * @author chennian
 */
public class ReportTemplate implements Serializable {

    private static final long serialVersionUID = 1L;

    /** 模板ID */
    private Long id;
    
    /** 模板名称 */
    private String templateName;
    
    /** 模板编码 */
    private String templateCode;
    
    /** 模板描述 */
    private String description;
    
    /** 模板类型（预设/自定义） */
    private String templateType;
    
    /** 数据源类型（SQL/API/文件） */
    private String dataSourceType;
    
    /** 数据源配置（JSON格式） */
    private String dataSourceConfig;
    
    /** 报表参数配置（JSON格式） */
    private String paramConfig;
    
    /** 报表列配置（JSON格式） */
    private String columnConfig;
    
    /** 报表样式配置（JSON格式） */
    private String styleConfig;
    
    /** 可视化配置（JSON格式） */
    private String visualConfig;
    
    /** 创建者 */
    private String createBy;
    
    /** 创建时间 */
    private Date createTime;
    
    /** 更新者 */
    private String updateBy;
    
    /** 更新时间 */
    private Date updateTime;
    
    /** 备注 */
    private String remark;
    
    /** 状态（0正常 1停用） */
    private String status;
    
    /** 删除标志（0代表存在 1代表删除） */
    private String delFlag;

    // Getters and Setters
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getTemplateName() {
        return templateName;
    }

    public void setTemplateName(String templateName) {
        this.templateName = templateName;
    }

    public String getTemplateCode() {
        return templateCode;
    }

    public void setTemplateCode(String templateCode) {
        this.templateCode = templateCode;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getTemplateType() {
        return templateType;
    }

    public void setTemplateType(String templateType) {
        this.templateType = templateType;
    }

    public String getDataSourceType() {
        return dataSourceType;
    }

    public void setDataSourceType(String dataSourceType) {
        this.dataSourceType = dataSourceType;
    }

    public String getDataSourceConfig() {
        return dataSourceConfig;
    }

    public void setDataSourceConfig(String dataSourceConfig) {
        this.dataSourceConfig = dataSourceConfig;
    }

    public String getParamConfig() {
        return paramConfig;
    }

    public void setParamConfig(String paramConfig) {
        this.paramConfig = paramConfig;
    }

    public String getColumnConfig() {
        return columnConfig;
    }

    public void setColumnConfig(String columnConfig) {
        this.columnConfig = columnConfig;
    }

    public String getStyleConfig() {
        return styleConfig;
    }

    public void setStyleConfig(String styleConfig) {
        this.styleConfig = styleConfig;
    }

    public String getVisualConfig() {
        return visualConfig;
    }

    public void setVisualConfig(String visualConfig) {
        this.visualConfig = visualConfig;
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

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getDelFlag() {
        return delFlag;
    }

    public void setDelFlag(String delFlag) {
        this.delFlag = delFlag;
    }

    @Override
    public String toString() {
        return "ReportTemplate{" +
                "id=" + id +
                ", templateName='" + templateName + '\'' +
                ", templateCode='" + templateCode + '\'' +
                ", templateType='" + templateType + '\'' +
                ", dataSourceType='" + dataSourceType + '\'' +
                ", status='" + status + '\'' +
                "}";
    }
}