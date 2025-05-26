package com.chennian.storytelling.workflow.domain;

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