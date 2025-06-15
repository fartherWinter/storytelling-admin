package com.chennian.storytelling.admin.controller.workflow;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

/**
 * 工作流API配置类
 * 管理API版本、环境配置和全局设置
 * 
 * @author chennian
 */
@Component
@ConfigurationProperties(prefix = "workflow.api")
public class WorkflowApiConfig {

    /**
     * API版本
     */
    private String version = "v1";

    /**
     * API基础路径前缀
     */
    private String basePath = "/api";

    /**
     * 工作流模块路径
     */
    private String workflowPath = "/workflow";

    /**
     * 是否启用API版本控制
     */
    private boolean versionEnabled = true;

    /**
     * 是否启用路径验证
     */
    private boolean pathValidationEnabled = true;

    /**
     * 是否启用API文档
     */
    private boolean documentationEnabled = true;

    /**
     * 最大路径长度限制
     */
    private int maxPathLength = 255;

    /**
     * 支持的API版本列表
     */
    private String[] supportedVersions = {"v1", "v2"};

    /**
     * 默认分页大小
     */
    private int defaultPageSize = 20;

    /**
     * 最大分页大小
     */
    private int maxPageSize = 100;

    /**
     * 请求超时时间（毫秒）
     */
    private long requestTimeout = 30000;

    /**
     * 是否启用请求日志
     */
    private boolean requestLoggingEnabled = true;

    /**
     * 是否启用性能监控
     */
    private boolean performanceMonitoringEnabled = true;

    /**
     * 获取完整的API基础路径
     */
    public String getFullBasePath() {
        if (versionEnabled) {
            return basePath + "/" + version + workflowPath;
        }
        return basePath + workflowPath;
    }

    /**
     * 检查版本是否支持
     */
    public boolean isSupportedVersion(String version) {
        if (supportedVersions == null || version == null) {
            return false;
        }
        for (String supportedVersion : supportedVersions) {
            if (supportedVersion.equals(version)) {
                return true;
            }
        }
        return false;
    }

    /**
     * 获取版本化的路径
     */
    public String getVersionedPath(String path) {
        if (!versionEnabled || path == null) {
            return path;
        }
        return getFullBasePath() + path;
    }

    /**
     * 验证路径长度
     */
    public boolean isValidPathLength(String path) {
        return path != null && path.length() <= maxPathLength;
    }

    /**
     * 获取有效的分页大小
     */
    public int getValidPageSize(Integer requestedSize) {
        if (requestedSize == null || requestedSize <= 0) {
            return defaultPageSize;
        }
        return Math.min(requestedSize, maxPageSize);
    }

    // Getters and Setters
    public String getVersion() {
        return version;
    }

    public void setVersion(String version) {
        this.version = version;
    }

    public String getBasePath() {
        return basePath;
    }

    public void setBasePath(String basePath) {
        this.basePath = basePath;
    }

    public String getWorkflowPath() {
        return workflowPath;
    }

    public void setWorkflowPath(String workflowPath) {
        this.workflowPath = workflowPath;
    }

    public boolean isVersionEnabled() {
        return versionEnabled;
    }

    public void setVersionEnabled(boolean versionEnabled) {
        this.versionEnabled = versionEnabled;
    }

    public boolean isPathValidationEnabled() {
        return pathValidationEnabled;
    }

    public void setPathValidationEnabled(boolean pathValidationEnabled) {
        this.pathValidationEnabled = pathValidationEnabled;
    }

    public boolean isDocumentationEnabled() {
        return documentationEnabled;
    }

    public void setDocumentationEnabled(boolean documentationEnabled) {
        this.documentationEnabled = documentationEnabled;
    }

    public int getMaxPathLength() {
        return maxPathLength;
    }

    public void setMaxPathLength(int maxPathLength) {
        this.maxPathLength = maxPathLength;
    }

    public String[] getSupportedVersions() {
        return supportedVersions;
    }

    public void setSupportedVersions(String[] supportedVersions) {
        this.supportedVersions = supportedVersions;
    }

    public int getDefaultPageSize() {
        return defaultPageSize;
    }

    public void setDefaultPageSize(int defaultPageSize) {
        this.defaultPageSize = defaultPageSize;
    }

    public int getMaxPageSize() {
        return maxPageSize;
    }

    public void setMaxPageSize(int maxPageSize) {
        this.maxPageSize = maxPageSize;
    }

    public long getRequestTimeout() {
        return requestTimeout;
    }

    public void setRequestTimeout(long requestTimeout) {
        this.requestTimeout = requestTimeout;
    }

    public boolean isRequestLoggingEnabled() {
        return requestLoggingEnabled;
    }

    public void setRequestLoggingEnabled(boolean requestLoggingEnabled) {
        this.requestLoggingEnabled = requestLoggingEnabled;
    }

    public boolean isPerformanceMonitoringEnabled() {
        return performanceMonitoringEnabled;
    }

    public void setPerformanceMonitoringEnabled(boolean performanceMonitoringEnabled) {
        this.performanceMonitoringEnabled = performanceMonitoringEnabled;
    }
}