package com.chennian.storytelling.bean.model;

import java.io.Serializable;
import java.util.Date;

/**
 * API请求日志实体类
 * 用于记录用户请求Flask项目API的信息
 *
 * @author chennian
 */
public class ApiRequestLog implements Serializable {

    private static final long serialVersionUID = 1L;

    /** 日志ID */
    private Long id;
    
    /** 请求URL */
    private String requestUrl;
    
    /** 请求方法 */
    private String requestMethod;
    
    /** 请求参数 */
    private String requestParams;
    
    /** 响应状态码 */
    private Integer responseCode;
    
    /** 响应内容 */
    private String responseContent;
    
    /** 用户ID */
    private String userId;
    
    /** 用户名 */
    private String userName;
    
    /** 用户IP */
    private String userIp;
    
    /** 请求时间 */
    private Date requestTime;
    
    /** 处理时间(毫秒) */
    private Long processingTime;
    
    /** 流程实例ID */
    private String processInstanceId;
    
    /** 是否成功 */
    private Boolean success;
    
    /** 错误信息 */
    private String errorMessage;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getRequestUrl() {
        return requestUrl;
    }

    public void setRequestUrl(String requestUrl) {
        this.requestUrl = requestUrl;
    }

    public String getRequestMethod() {
        return requestMethod;
    }

    public void setRequestMethod(String requestMethod) {
        this.requestMethod = requestMethod;
    }

    public String getRequestParams() {
        return requestParams;
    }

    public void setRequestParams(String requestParams) {
        this.requestParams = requestParams;
    }

    public Integer getResponseCode() {
        return responseCode;
    }

    public void setResponseCode(Integer responseCode) {
        this.responseCode = responseCode;
    }

    public String getResponseContent() {
        return responseContent;
    }

    public void setResponseContent(String responseContent) {
        this.responseContent = responseContent;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getUserIp() {
        return userIp;
    }

    public void setUserIp(String userIp) {
        this.userIp = userIp;
    }

    public Date getRequestTime() {
        return requestTime;
    }

    public void setRequestTime(Date requestTime) {
        this.requestTime = requestTime;
    }

    public Long getProcessingTime() {
        return processingTime;
    }

    public void setProcessingTime(Long processingTime) {
        this.processingTime = processingTime;
    }

    public String getProcessInstanceId() {
        return processInstanceId;
    }

    public void setProcessInstanceId(String processInstanceId) {
        this.processInstanceId = processInstanceId;
    }

    public Boolean getSuccess() {
        return success;
    }

    public void setSuccess(Boolean success) {
        this.success = success;
    }

    public String getErrorMessage() {
        return errorMessage;
    }

    public void setErrorMessage(String errorMessage) {
        this.errorMessage = errorMessage;
    }

    @Override
    public String toString() {
        return "ApiRequestLog{" +
                "id=" + id +
                ", requestUrl='" + requestUrl + '\'' +
                ", requestMethod='" + requestMethod + '\'' +
                ", userId='" + userId + '\'' +
                ", userName='" + userName + '\'' +
                ", userIp='" + userIp + '\'' +
                ", requestTime=" + requestTime +
                ", success=" + success +
                '}';
    }
}