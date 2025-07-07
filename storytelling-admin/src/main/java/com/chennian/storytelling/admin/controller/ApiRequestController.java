package com.chennian.storytelling.admin.controller;

import com.chennian.storytelling.common.annotation.EventTrack;
import com.chennian.storytelling.common.enums.BusinessType;
import com.chennian.storytelling.common.enums.ModelType;
import com.chennian.storytelling.common.enums.OperatorType;
import com.chennian.storytelling.bean.model.ApiRequestLog;
import com.chennian.storytelling.service.ApiRequestLogService;
import org.flowable.engine.RuntimeService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import jakarta.servlet.http.HttpServletRequest;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * API请求控制器
 * 提供接口用于发起对Flask项目的API请求
 *
 * @author chennian
 */
@RestController
@RequestMapping("/api/flask")
public class ApiRequestController {

    private static final Logger logger = LoggerFactory.getLogger(ApiRequestController.class);

    @Autowired
    private RuntimeService runtimeService;

    @Autowired
    private ApiRequestLogService apiRequestLogService;

    /**
     * 发起单个API请求
     *
     * @param requestParams 请求参数
     * @param request HTTP请求对象，用于获取客户端IP
     * @return 请求结果
     */
    @EventTrack(title = ModelType.SYSTEM, businessType = BusinessType.OTHER, operatorType = OperatorType.MANAGE, description = "发起单个API请求")
    @PostMapping("/request")
    public ResponseEntity<Map<String, Object>> requestApi(
            @RequestBody Map<String, Object> requestParams,
            HttpServletRequest request) {

        String userIp = getClientIp(request);
        String userId = (String) requestParams.getOrDefault("userId", "anonymous");
        String userName = (String) requestParams.getOrDefault("userName", "匿名用户");

        logger.info("收到API请求: {}, 用户ID: {}, 用户名: {}, IP: {}", 
                requestParams.get("apiEndpoint"), userId, userName, userIp);

        // 准备流程变量
        Map<String, Object> variables = new HashMap<>();
        variables.put("apiEndpoint", requestParams.get("apiEndpoint"));
        variables.put("requestMethod", requestParams.get("requestMethod"));
        variables.put("requestBody", requestParams.get("requestBody"));
        variables.put("apiBaseUrl", requestParams.get("apiBaseUrl"));
        variables.put("userId", userId);
        variables.put("userName", userName);
        variables.put("userIp", userIp);

        // 启动流程实例
        String processInstanceId = runtimeService.startProcessInstanceByKey(
                "apiRequestProcess", variables).getId();

        // 等待流程完成（实际项目中可能需要异步处理）
        // 这里简化处理，直接从流程变量中获取结果
        Map<String, Object> processVariables = runtimeService.getVariables(processInstanceId);

        // 构建响应
        Map<String, Object> response = new HashMap<>();
        response.put("processInstanceId", processInstanceId);
        response.put("success", processVariables.get("apiRequestSuccess"));
        response.put("responseCode", processVariables.get("apiResponseCode"));
        response.put("responseBody", processVariables.get("apiResponseBody"));
        response.put("processTime", processVariables.get("apiRequestTime"));

        if (processVariables.containsKey("apiErrorMessage")) {
            response.put("errorMessage", processVariables.get("apiErrorMessage"));
        }

        return ResponseEntity.ok(response);
    }

    /**
     * 发起批量API请求
     *
     * @param requestParams 请求参数
     * @param request HTTP请求对象，用于获取客户端IP
     * @return 请求结果
     */
    @EventTrack(title = ModelType.SYSTEM, businessType = BusinessType.OTHER, operatorType = OperatorType.MANAGE, description = "发起批量API请求")
    @PostMapping("/batch-request")
    public ResponseEntity<Map<String, Object>> batchRequestApi(
            @RequestBody Map<String, Object> requestParams,
            HttpServletRequest request) {

        String userIp = getClientIp(request);
        String userId = (String) requestParams.getOrDefault("userId", "anonymous");
        String userName = (String) requestParams.getOrDefault("userName", "匿名用户");

        @SuppressWarnings("unchecked")
        List<Map<String, Object>> apiRequests = (List<Map<String, Object>>) requestParams.get("apiRequests");

        logger.info("收到批量API请求: {}个请求, 用户ID: {}, 用户名: {}, IP: {}", 
                apiRequests != null ? apiRequests.size() : 0, userId, userName, userIp);

        // 准备流程变量
        Map<String, Object> variables = new HashMap<>();
        variables.put("apiRequests", apiRequests);
        variables.put("apiBaseUrl", requestParams.get("apiBaseUrl"));
        variables.put("userId", userId);
        variables.put("userName", userName);
        variables.put("userIp", userIp);

        // 启动流程实例
        String processInstanceId = runtimeService.startProcessInstanceByKey(
                "batchApiRequestProcess", variables).getId();

        // 等待流程完成（实际项目中可能需要异步处理）
        // 这里简化处理，直接从流程变量中获取结果
        Map<String, Object> processVariables = runtimeService.getVariables(processInstanceId);

        // 构建响应
        Map<String, Object> response = new HashMap<>();
        response.put("processInstanceId", processInstanceId);
        response.put("success", processVariables.get("batchRequestSuccess"));
        response.put("results", processVariables.get("batchRequestResults"));
        response.put("processTime", processVariables.get("batchRequestTime"));

        if (processVariables.containsKey("batchErrorMessage")) {
            response.put("errorMessage", processVariables.get("batchErrorMessage"));
        }

        return ResponseEntity.ok(response);
    }

    /**
     * 查询用户的API请求日志
     *
     * @param userId 用户ID
     * @return 日志列表
     */
    @EventTrack(title = ModelType.SYSTEM, businessType = BusinessType.SEARCH, operatorType = OperatorType.MANAGE, description = "查询用户的API请求日志")
    @GetMapping("/logs/user/{userId}")
    public ResponseEntity<List<ApiRequestLog>> getUserApiRequestLogs(@PathVariable String userId) {
        List<ApiRequestLog> logs = apiRequestLogService.getApiRequestLogsByUserId(userId);
        return ResponseEntity.ok(logs);
    }

    /**
     * 查询指定URL的API请求日志
     *
     * @param url 请求URL
     * @return 日志列表
     */
    @EventTrack(title = ModelType.SYSTEM, businessType = BusinessType.SEARCH, operatorType = OperatorType.MANAGE, description = "查询指定URL的API请求日志")
    @GetMapping("/logs/url")
    public ResponseEntity<List<ApiRequestLog>> getUrlApiRequestLogs(@RequestParam String url) {
        List<ApiRequestLog> logs = apiRequestLogService.getApiRequestLogsByUrl(url);
        return ResponseEntity.ok(logs);
    }

    /**
     * 查询失败的API请求日志
     *
     * @return 日志列表
     */
    @EventTrack(title = ModelType.SYSTEM, businessType = BusinessType.SEARCH, operatorType = OperatorType.MANAGE, description = "查询失败的API请求日志")
    @GetMapping("/logs/failed")
    public ResponseEntity<List<ApiRequestLog>> getFailedApiRequestLogs() {
        List<ApiRequestLog> logs = apiRequestLogService.getFailedApiRequestLogs();
        return ResponseEntity.ok(logs);
    }

    /**
     * 获取客户端IP地址
     *
     * @param request HTTP请求
     * @return IP地址
     */
    private String getClientIp(HttpServletRequest request) {
        String ip = request.getHeader("X-Forwarded-For");
        if (ip == null || ip.isEmpty() || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getHeader("Proxy-Client-IP");
        }
        if (ip == null || ip.isEmpty() || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getHeader("WL-Proxy-Client-IP");
        }
        if (ip == null || ip.isEmpty() || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getHeader("HTTP_CLIENT_IP");
        }
        if (ip == null || ip.isEmpty() || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getHeader("HTTP_X_FORWARDED_FOR");
        }
        if (ip == null || ip.isEmpty() || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getRemoteAddr();
        }
        return ip;
    }
}