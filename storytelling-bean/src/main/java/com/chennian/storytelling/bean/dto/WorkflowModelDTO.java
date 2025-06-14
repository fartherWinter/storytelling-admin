package com.chennian.storytelling.bean.dto;

import java.io.Serializable;
import java.util.Date;

/**
 * 工作流模型数据传输对象
 * 
 * @author chennian
 */
public class WorkflowModelDTO implements Serializable {

    private static final long serialVersionUID = 1L;

    /** 模型ID */
    private String id;
    
    /** 模型名称 */
    private String name;
    
    /** 模型键 */
    private String key;
    
    /** 模型分类 */
    private String category;
    
    /** 版本号 */
    private Integer version;
    
    /** 元数据信息 */
    private String metaInfo;
    
    /** 模型编辑器JSON数据 */
    private String editorJson;
    
    /** 模型XML内容 */
    private String xml;
    
    /** 部署ID */
    private String deploymentId;
    
    /** 流程定义ID */
    private String processDefinitionId;
    
    /** 描述信息 */
    private String description;
    
    /** 创建时间 */
    private Date createTime;
    
    /** 最后更新时间 */
    private Date lastUpdateTime;
    
    /** 创建人 */
    private String createBy;
    
    /** 是否已部署 */
    private Boolean deployed;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public Integer getVersion() {
        return version;
    }

    public void setVersion(Integer version) {
        this.version = version;
    }

    public String getMetaInfo() {
        return metaInfo;
    }

    public void setMetaInfo(String metaInfo) {
        this.metaInfo = metaInfo;
    }

    public String getEditorJson() {
        return editorJson;
    }

    public void setEditorJson(String editorJson) {
        this.editorJson = editorJson;
    }

    public String getXml() {
        return xml;
    }

    public void setXml(String xml) {
        this.xml = xml;
    }

    public String getDeploymentId() {
        return deploymentId;
    }

    public void setDeploymentId(String deploymentId) {
        this.deploymentId = deploymentId;
    }

    public String getProcessDefinitionId() {
        return processDefinitionId;
    }

    public void setProcessDefinitionId(String processDefinitionId) {
        this.processDefinitionId = processDefinitionId;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    public Date getLastUpdateTime() {
        return lastUpdateTime;
    }

    public void setLastUpdateTime(Date lastUpdateTime) {
        this.lastUpdateTime = lastUpdateTime;
    }

    public String getCreateBy() {
        return createBy;
    }

    public void setCreateBy(String createBy) {
        this.createBy = createBy;
    }

    public Boolean getDeployed() {
        return deployed;
    }

    public void setDeployed(Boolean deployed) {
        this.deployed = deployed;
    }
}