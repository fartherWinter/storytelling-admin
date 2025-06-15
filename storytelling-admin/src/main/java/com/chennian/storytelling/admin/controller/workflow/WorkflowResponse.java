package com.chennian.storytelling.admin.controller.workflow;

import lombok.extern.slf4j.Slf4j;

import java.util.HashMap;
import java.util.Map;

/**
 * 工作流统一响应工具类
 * 提供标准化的响应格式
 * 
 * @author chennian
 */
@Slf4j
public class WorkflowResponse {

    /**
     * 成功响应
     */
    public static Map<String, Object> success() {
        Map<String, Object> result = new HashMap<>();
        result.put("success", true);
        result.put("message", "操作成功");
        return result;
    }

    /**
     * 成功响应（带数据）
     */
    public static Map<String, Object> success(Object data) {
        Map<String, Object> result = new HashMap<>();
        result.put("success", true);
        result.put("data", data);
        result.put("message", "操作成功");
        return result;
    }

    /**
     * 成功响应（带数据和消息）
     */
    public static Map<String, Object> success(Object data, String message) {
        Map<String, Object> result = new HashMap<>();
        result.put("success", true);
        result.put("data", data);
        result.put("message", message);
        return result;
    }

    /**
     * 失败响应
     */
    public static Map<String, Object> error(String message) {
        Map<String, Object> result = new HashMap<>();
        result.put("success", false);
        result.put("message", message);
        return result;
    }

    /**
     * 失败响应（带异常信息）
     */
    public static Map<String, Object> error(String message, Exception e) {
        Map<String, Object> result = new HashMap<>();
        result.put("success", false);
        result.put("message", message);
        result.put("error", e.getMessage());
        log.error(message, e);
        return result;
    }

    /**
     * 分页响应
     */
    public static Map<String, Object> page(Object data, long total, int page, int size) {
        Map<String, Object> result = new HashMap<>();
        result.put("success", true);
        result.put("data", data);
        result.put("total", total);
        result.put("page", page);
        result.put("size", size);
        result.put("message", "查询成功");
        return result;
    }

    /**
     * 批量操作响应
     */
    public static Map<String, Object> batch(String operation, int totalCount, int successCount, int failureCount) {
        Map<String, Object> result = new HashMap<>();
        result.put("success", failureCount == 0);
        result.put("operation", operation);
        result.put("totalCount", totalCount);
        result.put("successCount", successCount);
        result.put("failureCount", failureCount);
        
        if (failureCount == 0) {
            result.put("message", String.format("%s操作全部成功，共处理%d项", operation, totalCount));
        } else {
            result.put("message", String.format("%s操作完成，成功%d项，失败%d项", operation, successCount, failureCount));
        }
        
        return result;
    }

    /**
     * 操作结果响应（带操作类型和目标ID）
     */
    public static Map<String, Object> operation(String operation, String targetId, String message) {
        Map<String, Object> result = new HashMap<>();
        result.put("success", true);
        result.put("operation", operation);
        result.put("targetId", targetId);
        result.put("message", message);
        return result;
    }

    /**
     * 统计响应
     */
    public static Map<String, Object> statistics(Object statistics, String message) {
        Map<String, Object> result = new HashMap<>();
        result.put("success", true);
        result.put("statistics", statistics);
        result.put("message", message);
        return result;
    }
}