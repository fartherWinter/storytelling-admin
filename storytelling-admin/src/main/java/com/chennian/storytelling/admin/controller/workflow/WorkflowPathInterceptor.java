package com.chennian.storytelling.admin.controller.workflow;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.util.regex.Pattern;

/**
 * 工作流路径拦截器
 * 提供路径验证、版本控制、性能监控等功能
 * 
 * @author chennian
 */
@Component
public class WorkflowPathInterceptor implements HandlerInterceptor {

    private static final Logger logger = LoggerFactory.getLogger(WorkflowPathInterceptor.class);

    @Autowired
    private WorkflowApiConfig apiConfig;

    // 工作流路径匹配模式
    private static final Pattern WORKFLOW_PATH_PATTERN = Pattern.compile(".*/sys/v\\d+/workflow/.*");
    
    // 危险路径模式（防止路径遍历攻击）
    private static final Pattern DANGEROUS_PATH_PATTERN = Pattern.compile(".*(\\.\\.|\\.\\.|%2e%2e|%252e%252e).*");
    
    // 请求开始时间的线程本地变量
    private static final ThreadLocal<Long> REQUEST_START_TIME = new ThreadLocal<>();

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        String requestPath = request.getRequestURI();
        String method = request.getMethod();
        
        // 记录请求开始时间
        REQUEST_START_TIME.set(System.currentTimeMillis());
        
        // 只处理工作流相关的请求
        if (!isWorkflowRequest(requestPath)) {
            return true;
        }
        
        // 记录请求日志
        if (apiConfig.isRequestLoggingEnabled()) {
            logger.info("Workflow API Request: {} {} from {}", method, requestPath, getClientIp(request));
        }
        
        // 路径安全验证
        if (!isSecurePath(requestPath)) {
            logger.warn("Dangerous path detected: {} from {}", requestPath, getClientIp(request));
            response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            response.getWriter().write("{\"error\":\"Invalid path\",\"code\":400}");
            return false;
        }
        
        // 路径长度验证
        if (apiConfig.isPathValidationEnabled() && !apiConfig.isValidPathLength(requestPath)) {
            logger.warn("Path too long: {} (length: {}) from {}", requestPath, requestPath.length(), getClientIp(request));
            response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            response.getWriter().write("{\"error\":\"Path too long\",\"code\":400}");
            return false;
        }
        
        // API版本验证
        if (apiConfig.isVersionEnabled()) {
            String version = extractVersionFromPath(requestPath);
            if (version != null && !apiConfig.isSupportedVersion(version)) {
                logger.warn("Unsupported API version: {} from {}", version, getClientIp(request));
                response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
                response.getWriter().write("{\"error\":\"Unsupported API version\",\"code\":400}");
                return false;
            }
        }
        
        // 设置响应头
        response.setHeader("X-API-Version", apiConfig.getVersion());
        response.setHeader("X-Request-ID", generateRequestId());
        
        return true;
    }

    @Override
    public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler, ModelAndView modelAndView) throws Exception {
        // 后处理逻辑（如果需要）
    }

    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) throws Exception {
        String requestPath = request.getRequestURI();
        
        // 只处理工作流相关的请求
        if (!isWorkflowRequest(requestPath)) {
            return;
        }
        
        // 性能监控
        if (apiConfig.isPerformanceMonitoringEnabled()) {
            Long startTime = REQUEST_START_TIME.get();
            if (startTime != null) {
                long duration = System.currentTimeMillis() - startTime;
                
                // 记录性能日志
                if (duration > 1000) { // 超过1秒的请求
                    logger.warn("Slow workflow API request: {} {} took {}ms", 
                        request.getMethod(), requestPath, duration);
                } else if (apiConfig.isRequestLoggingEnabled()) {
                    logger.debug("Workflow API request completed: {} {} in {}ms", 
                        request.getMethod(), requestPath, duration);
                }
                
                // 设置响应时间头
                response.setHeader("X-Response-Time", duration + "ms");
            }
        }
        
        // 异常处理
        if (ex != null) {
            logger.error("Workflow API request failed: {} {} - {}", 
                request.getMethod(), requestPath, ex.getMessage(), ex);
        }
        
        // 清理线程本地变量
        REQUEST_START_TIME.remove();
    }

    /**
     * 判断是否为工作流请求
     */
    private boolean isWorkflowRequest(String path) {
        return path != null && (path.contains("/workflow/") || WORKFLOW_PATH_PATTERN.matcher(path).matches());
    }

    /**
     * 验证路径安全性
     */
    private boolean isSecurePath(String path) {
        if (path == null) {
            return false;
        }
        
        // 检查路径遍历攻击
        if (DANGEROUS_PATH_PATTERN.matcher(path.toLowerCase()).matches()) {
            return false;
        }
        
        // 检查其他危险字符
        String[] dangerousChars = {"<", ">", "\"", "'", "&", ";", "|", "`"};
        for (String dangerousChar : dangerousChars) {
            if (path.contains(dangerousChar)) {
                return false;
            }
        }
        
        return true;
    }

    /**
     * 从路径中提取API版本
     */
    private String extractVersionFromPath(String path) {
        if (path == null) {
            return null;
        }
        
        // 匹配 /api/v1/workflow 或 /api/v2/workflow 等模式
        Pattern versionPattern = Pattern.compile(".*/sys/(v\\d+)/workflow.*");
        java.util.regex.Matcher matcher = versionPattern.matcher(path);
        
        if (matcher.matches()) {
            return matcher.group(1);
        }
        
        return null;
    }

    /**
     * 获取客户端IP地址
     */
    private String getClientIp(HttpServletRequest request) {
        String xForwardedFor = request.getHeader("X-Forwarded-For");
        if (xForwardedFor != null && !xForwardedFor.isEmpty() && !"unknown".equalsIgnoreCase(xForwardedFor)) {
            return xForwardedFor.split(",")[0].trim();
        }
        
        String xRealIp = request.getHeader("X-Real-IP");
        if (xRealIp != null && !xRealIp.isEmpty() && !"unknown".equalsIgnoreCase(xRealIp)) {
            return xRealIp;
        }
        
        return request.getRemoteAddr();
    }

    /**
     * 生成请求ID
     */
    private String generateRequestId() {
        return "WF-" + System.currentTimeMillis() + "-" + Thread.currentThread().getId();
    }

    /**
     * 验证请求参数
     */
    public static class RequestValidator {
        
        /**
         * 验证分页参数
         */
        public static boolean isValidPageParams(Integer page, Integer size) {
            return (page == null || page >= 0) && (size == null || (size > 0 && size <= 100));
        }
        
        /**
         * 验证排序参数
         */
        public static boolean isValidSortParam(String sort) {
            if (sort == null || sort.trim().isEmpty()) {
                return true;
            }
            
            // 允许的排序字段和方向
            String[] allowedFields = {"id", "name", "createTime", "updateTime", "status", "priority"};
            String[] allowedDirections = {"asc", "desc"};
            
            String[] parts = sort.split(",");
            for (String part : parts) {
                String[] fieldAndDirection = part.trim().split(":");
                if (fieldAndDirection.length != 2) {
                    return false;
                }
                
                String field = fieldAndDirection[0].trim();
                String direction = fieldAndDirection[1].trim().toLowerCase();
                
                boolean validField = false;
                for (String allowedField : allowedFields) {
                    if (allowedField.equals(field)) {
                        validField = true;
                        break;
                    }
                }
                
                boolean validDirection = false;
                for (String allowedDirection : allowedDirections) {
                    if (allowedDirection.equals(direction)) {
                        validDirection = true;
                        break;
                    }
                }
                
                if (!validField || !validDirection) {
                    return false;
                }
            }
            
            return true;
        }
        
        /**
         * 验证搜索关键字
         */
        public static boolean isValidSearchKeyword(String keyword) {
            if (keyword == null) {
                return true;
            }
            
            // 长度限制
            if (keyword.length() > 100) {
                return false;
            }
            
            // 危险字符检查
            String[] dangerousChars = {"<", ">", "script", "javascript", "vbscript", "onload", "onerror"};
            String lowerKeyword = keyword.toLowerCase();
            for (String dangerousChar : dangerousChars) {
                if (lowerKeyword.contains(dangerousChar)) {
                    return false;
                }
            }
            
            return true;
        }
    }
}