package com.chennian.storytelling.workflow.delegate;

import com.chennian.storytelling.bean.model.ApiRequestLog;
import com.chennian.storytelling.service.ApiRequestLogService;
import org.flowable.engine.delegate.DelegateExecution;
import org.flowable.engine.delegate.JavaDelegate;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.time.Duration;
import java.util.*;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * 批量API请求委托类
 * 用于批量请求Flask项目中的接口并记录用户信息
 * 
 * @author chennian
 */
@Component
public class BatchApiRequestDelegate implements JavaDelegate {

    private static final Logger logger = LoggerFactory.getLogger(BatchApiRequestDelegate.class);
    
    // 默认的Flask项目API基础URL
    private static final String DEFAULT_API_BASE_URL = "http://localhost:5000";
    
    // HTTP客户端，使用Java 11内置的HttpClient
    private final HttpClient httpClient;
    
    // 线程池，用于并行处理多个API请求
    private final ExecutorService executorService;
    
    @Autowired
    private ApiRequestLogService apiRequestLogService;
    
    public BatchApiRequestDelegate() {
        // 配置HTTP客户端，设置超时时间
        this.httpClient = HttpClient.newBuilder()
                .version(HttpClient.Version.HTTP_2)
                .connectTimeout(Duration.ofSeconds(10))
                .build();
        
        // 创建固定大小的线程池
        this.executorService = Executors.newFixedThreadPool(10);
    }
    
    @Override
    public void execute(DelegateExecution execution) {
        // 获取流程变量
        @SuppressWarnings("unchecked")
        List<Map<String, Object>> apiRequests = (List<Map<String, Object>>) execution.getVariable("apiRequests");
        String userId = (String) execution.getVariable("userId");
        String userName = (String) execution.getVariable("userName");
        String userIp = (String) execution.getVariable("userIp");
        
        // 如果没有API请求列表，记录警告并返回
        if (apiRequests == null || apiRequests.isEmpty()) {
            logger.warn("未提供API请求列表，无法执行批量请求");
            execution.setVariable("batchRequestSuccess", false);
            execution.setVariable("batchErrorMessage", "未提供API请求列表");
            return;
        }
        
        // 获取API基础URL
        String baseUrl = (String) execution.getVariable("apiBaseUrl");
        if (baseUrl == null || baseUrl.isEmpty()) {
            baseUrl = DEFAULT_API_BASE_URL;
        }
        
        logger.info("开始执行批量API请求，共{}个请求，用户ID: {}, 用户名: {}, IP: {}", 
                apiRequests.size(), userId, userName, userIp);
        
        // 创建请求结果列表
        List<Map<String, Object>> requestResults = new ArrayList<>();
        List<CompletableFuture<Map<String, Object>>> futures = new ArrayList<>();
        
        // 并行处理所有API请求
        for (Map<String, Object> requestInfo : apiRequests) {
            String finalBaseUrl = baseUrl;
            CompletableFuture<Map<String, Object>> future = CompletableFuture.supplyAsync(() -> {
                return processApiRequest(requestInfo, finalBaseUrl, userId, userName, userIp, execution);
            }, executorService);
            
            futures.add(future);
        }
        
        // 等待所有请求完成
        CompletableFuture<Void> allFutures = CompletableFuture.allOf(
                futures.toArray(new CompletableFuture[0]));
        
        try {
            // 等待所有请求完成
            allFutures.join();
            
            // 收集所有请求结果
            for (CompletableFuture<Map<String, Object>> future : futures) {
                requestResults.add(future.get());
            }
            
            // 设置流程变量
            execution.setVariable("batchRequestResults", requestResults);
            execution.setVariable("batchRequestSuccess", true);
            execution.setVariable("batchRequestTime", System.currentTimeMillis());
            
            logger.info("批量API请求完成，共{}个请求", requestResults.size());
            
        } catch (Exception e) {
            logger.error("批量API请求失败: {}", e.getMessage(), e);
            execution.setVariable("batchRequestSuccess", false);
            execution.setVariable("batchErrorMessage", e.getMessage());
        }
    }
    
    /**
     * 处理单个API请求
     */
    private Map<String, Object> processApiRequest(Map<String, Object> requestInfo, String baseUrl, 
                                                String userId, String userName, String userIp,
                                                DelegateExecution execution) {
        Map<String, Object> result = new HashMap<>();
        result.put("requestInfo", requestInfo);
        
        long startTime = System.currentTimeMillis();
        
        try {
            // 获取请求信息
            String apiEndpoint = (String) requestInfo.get("apiEndpoint");
            String requestMethod = (String) requestInfo.get("requestMethod");
            String requestBody = (String) requestInfo.get("requestBody");
            String requestId = (String) requestInfo.get("requestId");
            
            // 如果没有指定API端点，使用默认值
            if (apiEndpoint == null || apiEndpoint.isEmpty()) {
                apiEndpoint = "/api/default";
            }
            
            // 如果没有指定请求方法，默认使用GET
            if (requestMethod == null || requestMethod.isEmpty()) {
                requestMethod = "GET";
            }
            
            // 构建完整的API URL
            String fullUrl = baseUrl + apiEndpoint;
            
            // 记录请求信息
            logger.info("处理API请求: {}, 方法: {}, 请求ID: {}, 用户ID: {}", 
                    fullUrl, requestMethod, requestId, userId);
            
            // 构建请求
            HttpRequest request = buildHttpRequest(fullUrl, requestMethod, requestBody, userId, userName, userIp);
            
            // 发送请求并获取响应
            HttpResponse<String> response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());
            
            // 处理响应
            int statusCode = response.statusCode();
            String responseBody = response.body();
            
            // 记录响应信息
            logger.info("API响应状态码: {}, 请求ID: {}", statusCode, requestId);
            logger.debug("API响应内容: {}", responseBody);
            
            // 设置结果
            result.put("statusCode", statusCode);
            result.put("responseBody", responseBody);
            result.put("success", statusCode >= 200 && statusCode < 300);
            result.put("requestId", requestId);
            
            // 记录用户请求日志
            recordApiRequestLog(fullUrl, requestMethod, requestBody, statusCode, responseBody, 
                    userId, userName, userIp, execution.getProcessInstanceId(), true, null, startTime);
            
        } catch (Exception e) {
            logger.error("API请求处理失败: {}", e.getMessage(), e);
            
            // 设置错误信息
            result.put("success", false);
            result.put("errorMessage", e.getMessage());
            
            // 记录失败的请求日志
            String apiEndpoint = (String) requestInfo.get("apiEndpoint");
            String requestMethod = (String) requestInfo.get("requestMethod");
            String requestBody = (String) requestInfo.get("requestBody");
            String fullUrl = baseUrl + (apiEndpoint != null ? apiEndpoint : "/api/default");
            
            recordApiRequestLog(fullUrl, requestMethod, requestBody, 500, null, 
                    userId, userName, userIp, execution.getProcessInstanceId(), false, e.getMessage(), startTime);
        }
        
        result.put("processingTime", System.currentTimeMillis() - startTime);
        return result;
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
     * 记录API请求日志
     */
    private void recordApiRequestLog(String url, String method, String requestParams, 
                                   int responseCode, String responseContent, 
                                   String userId, String userName, String userIp,
                                   String processInstanceId, boolean success, String errorMessage,
                                   long startTime) {
        try {
            // 创建日志对象
            ApiRequestLog log = new ApiRequestLog();
            log.setRequestUrl(url);
            log.setRequestMethod(method);
            log.setRequestParams(requestParams);
            log.setResponseCode(responseCode);
            log.setResponseContent(responseContent);
            log.setUserId(userId);
            log.setUserName(userName);
            log.setUserIp(userIp);
            log.setRequestTime(new Date());
            log.setProcessingTime(System.currentTimeMillis() - startTime);
            log.setProcessInstanceId(processInstanceId);
            log.setSuccess(success);
            log.setErrorMessage(errorMessage);
            
            // 保存日志
            apiRequestLogService.saveApiRequestLog(log);
            
        } catch (Exception e) {
            logger.error("记录API请求日志失败: {}", e.getMessage(), e);
        }
    }
}