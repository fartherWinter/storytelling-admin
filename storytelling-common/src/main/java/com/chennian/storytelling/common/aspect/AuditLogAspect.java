package com.chennian.storytelling.common.aspect;

import cn.dev33.satoken.stp.StpUtil;
import com.chennian.storytelling.common.annotation.AuditLog;
import com.chennian.storytelling.common.entity.AuditLogEntity;
import com.chennian.storytelling.common.utils.IpUtils;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.AfterThrowing;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import jakarta.servlet.http.HttpServletRequest;
import java.lang.reflect.Method;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.concurrent.CompletableFuture;

/**
 * 审计日志切面
 *
 * @author chennian
 * @date 2024/01/01
 */
@Aspect
@Component
@Slf4j
public class AuditLogAspect {

    @Autowired
    private MongoTemplate mongoTemplate;

    @Autowired
    private ObjectMapper objectMapper;

    /**
     * 环绕通知，记录操作日志
     */
    @Around("@annotation(auditLog)")
    public Object around(ProceedingJoinPoint point, AuditLog auditLog) throws Throwable {
        long startTime = System.currentTimeMillis();
        Object result = null;
        Exception exception = null;

        try {
            result = point.proceed();
            return result;
        } catch (Exception e) {
            exception = e;
            throw e;
        } finally {
            long endTime = System.currentTimeMillis();
            long executionTime = endTime - startTime;
            
            // 异步记录日志
            final JoinPoint finalPoint = point;
            final AuditLog finalAuditLog = auditLog;
            final Object finalResult = result;
            final Exception finalException = exception;
            final long finalExecutionTime = executionTime;
            CompletableFuture.runAsync(() -> {
                try {
                    saveAuditLog(finalPoint, finalAuditLog, finalResult, finalException, finalExecutionTime);
                } catch (Exception e) {
                    log.error("保存审计日志失败", e);
                }
            });
        }
    }

    /**
     * 异常通知，记录异常日志
     */
    @AfterThrowing(pointcut = "@annotation(auditLog)", throwing = "e")
    public void afterThrowing(JoinPoint point, AuditLog auditLog, Exception e) {
        // 异步记录异常日志
        final JoinPoint finalPoint = point;
        final AuditLog finalAuditLog = auditLog;
        final Exception finalException = e;
        CompletableFuture.runAsync(() -> {
            try {
                saveAuditLog(finalPoint, finalAuditLog, null, finalException, 0L);
            } catch (Exception ex) {
                log.error("保存异常审计日志失败", ex);
            }
        });
    }

    /**
     * 保存审计日志
     */
    private void saveAuditLog(JoinPoint point, AuditLog auditLog, Object result, Exception exception, Long executionTime) {
        try {
            ServletRequestAttributes attributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
            if (attributes == null) {
                return;
            }

            HttpServletRequest request = attributes.getRequest();
            MethodSignature signature = (MethodSignature) point.getSignature();
            Method method = signature.getMethod();

            AuditLogEntity logEntity = new AuditLogEntity();
            
            // 设置用户信息
            try {
                Object loginId = StpUtil.getLoginIdDefaultNull();
                if (loginId != null) {
                    logEntity.setUserId(Long.valueOf(loginId.toString()));
                    // 这里可以根据用户ID查询用户名，暂时使用ID作为用户名
                    logEntity.setUsername(loginId.toString());
                }
            } catch (Exception e) {
                log.debug("获取用户信息失败", e);
            }

            // 设置操作信息
            logEntity.setModule(auditLog.module())
                    .setOperationType(auditLog.type().getDescription())
                    .setDescription(auditLog.description())
                    .setMethod(request.getMethod())
                    .setRequestUrl(request.getRequestURL().toString());

            // 设置请求参数
            if (auditLog.recordParams()) {
                String params = getRequestParams(point, request);
                logEntity.setRequestParams(params);
            }

            // 设置返回结果
            if (auditLog.recordResult() && result != null) {
                try {
                    String resultStr = objectMapper.writeValueAsString(result);
                    // 限制结果长度，避免过长
                    if (resultStr.length() > 2000) {
                        resultStr = resultStr.substring(0, 2000) + "...";
                    }
                    logEntity.setResult(resultStr);
                } catch (Exception e) {
                    logEntity.setResult("序列化结果失败: " + e.getMessage());
                }
            }

            // 设置异常信息
            if (exception != null && auditLog.recordException()) {
                String exceptionStr = exception.getClass().getSimpleName() + ": " + exception.getMessage();
                if (exceptionStr.length() > 1000) {
                    exceptionStr = exceptionStr.substring(0, 1000) + "...";
                }
                logEntity.setException(exceptionStr);
            }

            // 设置客户端信息
            logEntity.setClientIp(IpUtils.getIpAddr(request))
                    .setUserAgent(request.getHeader("User-Agent"));

            // 设置执行信息
            logEntity.setExecutionTime(executionTime)
                    .setStatus(exception == null ? AuditLogEntity.Status.SUCCESS.getCode() : AuditLogEntity.Status.FAILURE.getCode())
                    .setCreateTime(LocalDateTime.now());

            // 保存到MongoDB
            mongoTemplate.save(logEntity, "audit_log");
            
            log.debug("审计日志保存成功: {}", logEntity.getId());
            
        } catch (Exception e) {
            log.error("保存审计日志异常", e);
        }
    }

    /**
     * 获取请求参数
     */
    private String getRequestParams(JoinPoint point, HttpServletRequest request) {
        try {
            StringBuilder params = new StringBuilder();
            
            // 获取方法参数
            Object[] args = point.getArgs();
            if (args != null && args.length > 0) {
                params.append("方法参数: ");
                for (int i = 0; i < args.length; i++) {
                    if (args[i] != null) {
                        // 过滤敏感参数
                        if (isSensitiveParam(args[i])) {
                            params.append("[敏感信息已隐藏]");
                        } else {
                            try {
                                String argStr = objectMapper.writeValueAsString(args[i]);
                                if (argStr.length() > 500) {
                                    argStr = argStr.substring(0, 500) + "...";
                                }
                                params.append(argStr);
                            } catch (Exception e) {
                                params.append(args[i].getClass().getSimpleName());
                            }
                        }
                    } else {
                        params.append("null");
                    }
                    if (i < args.length - 1) {
                        params.append(", ");
                    }
                }
            }

            // 获取请求参数
            if (!request.getParameterMap().isEmpty()) {
                if (params.length() > 0) {
                    params.append("; ");
                }
                params.append("请求参数: ");
                request.getParameterMap().forEach((key, values) -> {
                    if (isSensitiveParamName(key)) {
                        params.append(key).append("=[敏感信息已隐藏], ");
                    } else {
                        params.append(key).append("=").append(Arrays.toString(values)).append(", ");
                    }
                });
            }

            String result = params.toString();
            // 限制参数长度
            if (result.length() > 2000) {
                result = result.substring(0, 2000) + "...";
            }
            return result;
            
        } catch (Exception e) {
            log.warn("获取请求参数失败", e);
            return "获取参数失败: " + e.getMessage();
        }
    }

    /**
     * 判断是否为敏感参数
     */
    private boolean isSensitiveParam(Object param) {
        if (param == null) {
            return false;
        }
        
        String className = param.getClass().getSimpleName().toLowerCase();
        return className.contains("password") || 
               className.contains("token") || 
               className.contains("secret") ||
               className.contains("key");
    }

    /**
     * 判断是否为敏感参数名
     */
    private boolean isSensitiveParamName(String paramName) {
        if (paramName == null) {
            return false;
        }
        
        String lowerName = paramName.toLowerCase();
        return lowerName.contains("password") || 
               lowerName.contains("pwd") ||
               lowerName.contains("token") || 
               lowerName.contains("secret") ||
               lowerName.contains("key") ||
               lowerName.contains("auth");
    }
}