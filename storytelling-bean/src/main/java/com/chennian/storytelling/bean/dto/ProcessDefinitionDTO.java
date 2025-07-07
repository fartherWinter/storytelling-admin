package com.chennian.storytelling.bean.dto;

import java.io.Serializable;

/**
 * 流程定义数据传输对象
 * 
 * @author chennian
 */
public class ProcessDefinitionDTO implements Serializable {

    private static final long serialVersionUID = 1L;

    /** 流程名称 */
    private String name;
    
    /** 流程分类 */
    private String category;
    
    /** 资源名称 */
    private String resourceName;
    
    /** 部署文件内容 */
    private String deploymentFile;
    
    /** 流程描述 */
    private String description;
    
    /** 流程定义ID */
    private String id;
    
    /** 流程定义键 */
    private String key;
    
    /** 流程版本 */
    private int version;
    
    /** 部署ID */
    private String deploymentId;
    
    /** 是否挂起 */
    private boolean suspended;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public int getVersion() {
        return version;
    }

    public void setVersion(int version) {
        this.version = version;
    }

    public String getDeploymentId() {
        return deploymentId;
    }

    public void setDeploymentId(String deploymentId) {
        this.deploymentId = deploymentId;
    }

    public boolean isSuspended() {
        return suspended;
    }

    public void setSuspended(boolean suspended) {
        this.suspended = suspended;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public String getResourceName() {
        return resourceName;
    }

    public void setResourceName(String resourceName) {
        this.resourceName = resourceName;
    }

    public String getDeploymentFile() {
        return deploymentFile;
    }

    public void setDeploymentFile(String deploymentFile) {
        this.deploymentFile = deploymentFile;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
}