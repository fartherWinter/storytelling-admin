package com.chennian.storytelling.workflow.delegate;

import org.flowable.engine.delegate.DelegateExecution;
import org.flowable.engine.delegate.JavaDelegate;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.time.Duration;
import java.util.HashMap;
import java.util.Map;

/**
 * API请求委托类
 * 用于请求Flask项目中的接口并记录用户信息
 * 
 * @author chennian
 */
@Component
public class ApiRequestDelegate implements JavaDelegate {

    private static final Logger logger = LoggerFactory.getLogger(ApiRequestDelegate.class);
    
    // 默认的Flask项目API基础URL
    private static final String DEFAULT_API_BASE_URL = "http://localhost:5000";
    
    // HTTP客户端，使用Java 11内置的HttpClient
    private final HttpClient httpClient;
    
    public ApiRequestDelegate() {
        // 配置HTTP客户端，设置超时时间
        this.httpClient = HttpClient.newBuilder()
                .version(HttpClient.Version.HTTP_2)
                .connectTimeout(Duration.ofSeconds(10))
                .build();
    }
    
    @Override
    public void execute(DelegateExecution execution) {
        // 获取流程变量
        String apiEndpoint = (String) execution.getVariable("apiEndpoint");
        String requestMethod = (String) execution.getVariable("requestMethod");
        String requestBody = (String) execution.getVariable("requestBody");
        String userId = (String) execution.getVariable("userId");
        String userName = (String) execution.getVariable("userName");
        String userIp = (String) execution.getVariable("userIp");
        
        // 如果没有指定API端点，使用默认值
        if (apiEndpoint == null || apiEndpoint.isEmpty()) {
            logger.warn("未指定API端点，将使用默认端点");
            apiEndpoint = "/api/default";
        }
        
        // 如果没有指定请求方法，默认使用GET
        if (requestMethod == null || requestMethod.isEmpty()) {
            requestMethod = "GET";
        }
        
        // 构建完整的API URL
        String baseUrl = (String) execution.getVariable("apiBaseUrl");
        if (baseUrl == null || baseUrl.isEmpty()) {
            baseUrl = DEFAULT_API_BASE_URL;
        }
        
        String fullUrl = baseUrl + apiEndpoint;
        
        try {
            // 记录请求信息
            logger.info("准备请求API: {}, 方法: {}, 用户ID: {}, 用户名: {}, IP: {}", 
                    fullUrl, requestMethod, userId, userName, userIp);
            
            // 构建请求
            HttpRequest request = buildHttpRequest(fullUrl, requestMethod, requestBody, userId, userName, userIp);
            
            // 发送请求并获取响应
            HttpResponse<String> response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());
            
            // 处理响应
            int statusCode = response.statusCode();
            String responseBody = response.body();
            
            // 记录响应信息
            logger.info("API响应状态码: {}", statusCode);
            logger.debug("API响应内容: {}", responseBody);
            
            // 将响应信息设置为流程变量
            execution.setVariable("apiResponseCode", statusCode);
            execution.setVariable("apiResponseBody", responseBody);
            execution.setVariable("apiRequestSuccess", statusCode >= 200 && statusCode < 300);
            execution.setVariable("apiRequestTime", System.currentTimeMillis());
            
            // 记录用户请求日志（实际项目中可能会存储到数据库）
            recordUserRequestLog(execution, fullUrl, requestMethod, statusCode, userId, userName, userIp);
            
        } catch (Exception e) {
            logger.error("API请求失败: {}", e.getMessage(), e);
            
            // 设置错误信息
            execution.setVariable("apiRequestSuccess", false);
            execution.setVariable("apiErrorMessage", e.getMessage());
            execution.setVariable("apiRequestTime", System.currentTimeMillis());
        }
    }
    
    /**
     * 构建HTTP请求
     */
    private HttpRequest buildHttpRequest(String url, String method, String body, String userId, String userName, String userIp) {
        HttpRequest.Builder requestBuilder = HttpRequest.newBuilder()
                .uri(URI.create(url))
                .header("Content-Type", "application/json")
                .header("Accept", "application/json")
                .header("X-User-ID", userId != null ? userId : "unknown")
                .header("X-User-Name", userName != null ? userName : "unknown")
                .header("X-User-IP", userIp != null ? userIp : "unknown")
                .timeout(Duration.ofSeconds(30));
        
        // 根据请求方法设置请求体
        switch (method.toUpperCase()) {
            case "POST":
                return requestBuilder.POST(HttpRequest.BodyPublishers.ofString(body != null ? body : "")).build();
            case "PUT":
                return requestBuilder.PUT(HttpRequest.BodyPublishers.ofString(body != null ? body : "")).build();
            case "DELETE":
                return requestBuilder.DELETE().build();
            case "GET":
            default:
                return requestBuilder.GET().build();
        }
    }
    
    /**
     * 记录用户请求日志
     * 在实际项目中，这里可以将日志保存到数据库
     */
    private void recordUserRequestLog(DelegateExecution execution, String url, String method, 
                                     int statusCode, String userId, String userName, String userIp) {
        // 创建日志记录
        Map<String, Object> requestLog = new HashMap<>();
        requestLog.put("url", url);
        requestLog.put("method", method);
        requestLog.put("statusCode", statusCode);
        requestLog.put("userId", userId);
        requestLog.put("userName", userName);
        requestLog.put("userIp", userIp);
        requestLog.put("timestamp", System.currentTimeMillis());
        requestLog.put("processInstanceId", execution.getProcessInstanceId());
        
        // 记录日志信息
        logger.info("用户请求日志: {}", requestLog);
        
        // 将日志信息设置为流程变量，以便后续处理
        execution.setVariable("apiRequestLog", requestLog);
    }
}