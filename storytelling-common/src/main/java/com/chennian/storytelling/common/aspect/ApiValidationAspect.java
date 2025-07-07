package com.chennian.storytelling.common.aspect;

import com.chennian.storytelling.common.annotation.ApiValidation;
import com.chennian.storytelling.common.exception.ServiceException;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;

import jakarta.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.util.Arrays;
import java.util.regex.Pattern;

/**
 * API参数验证切面
 *
 * @author chennian
 * @date 2024/01/01
 */
@Aspect
@Component
@Slf4j
public class ApiValidationAspect {

    /**
     * SQL注入检测正则表达式
     */
    private static final Pattern SQL_INJECTION_PATTERN = Pattern.compile(
        "(?i)(union|select|insert|update|delete|drop|create|alter|exec|execute|script|javascript|vbscript|onload|onerror|onclick)",
        Pattern.CASE_INSENSITIVE
    );

    /**
     * XSS攻击检测正则表达式
     */
    private static final Pattern XSS_PATTERN = Pattern.compile(
        "(?i)(<script[^>]*>.*?</script>|<iframe[^>]*>.*?</iframe>|javascript:|vbscript:|onload=|onerror=|onclick=|onmouseover=)",
        Pattern.CASE_INSENSITIVE
    );

    @Before("@annotation(apiValidation)")
    public void validateBefore(JoinPoint point, ApiValidation apiValidation) {
        if (!apiValidation.enabled()) {
            return;
        }

        try {
            ServletRequestAttributes attributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
            if (attributes == null) {
                return;
            }

            HttpServletRequest request = attributes.getRequest();
            
            // 验证请求体大小
            if (apiValidation.validateBodySize()) {
                validateBodySize(request, apiValidation.maxBodySize());
            }

            // 验证SQL注入
            if (apiValidation.validateSqlInjection()) {
                validateSqlInjection(request);
            }

            // 验证XSS攻击
            if (apiValidation.validateXss()) {
                validateXss(request);
            }

            // 验证文件上传类型
            if (apiValidation.validateFileType() && request instanceof MultipartHttpServletRequest) {
                validateFileType((MultipartHttpServletRequest) request, apiValidation.allowedFileTypes());
            }

            // 验证请求头
            if (apiValidation.validateHeaders()) {
                validateHeaders(request);
            }

        } catch (ServiceException e) {
            throw e;
        } catch (Exception e) {
            log.error("API参数验证异常", e);
            throw new ServiceException("参数验证失败");
        }
    }

    /**
     * 验证请求体大小
     */
    private void validateBodySize(HttpServletRequest request, long maxSize) {
        int contentLength = request.getContentLength();
        if (contentLength > maxSize) {
            log.warn("请求体大小超出限制: {} > {}", contentLength, maxSize);
            throw new ServiceException("请求体大小超出限制");
        }
    }

    /**
     * 验证SQL注入
     */
    private void validateSqlInjection(HttpServletRequest request) {
        // 检查所有参数
        request.getParameterMap().forEach((key, values) -> {
            for (String value : values) {
                if (value != null && SQL_INJECTION_PATTERN.matcher(value).find()) {
                    log.warn("检测到SQL注入攻击: 参数={}, 值={}", key, value);
                    throw new ServiceException("检测到非法参数");
                }
            }
        });

        // 检查请求URI
        String requestURI = request.getRequestURI();
        if (requestURI != null && SQL_INJECTION_PATTERN.matcher(requestURI).find()) {
            log.warn("检测到SQL注入攻击: URI={}", requestURI);
            throw new ServiceException("检测到非法请求");
        }
    }

    /**
     * 验证XSS攻击
     */
    private void validateXss(HttpServletRequest request) {
        // 检查所有参数
        request.getParameterMap().forEach((key, values) -> {
            for (String value : values) {
                if (value != null && XSS_PATTERN.matcher(value).find()) {
                    log.warn("检测到XSS攻击: 参数={}, 值={}", key, value);
                    throw new ServiceException("检测到非法参数");
                }
            }
        });

        // 检查User-Agent
        String userAgent = request.getHeader("User-Agent");
        if (userAgent != null && XSS_PATTERN.matcher(userAgent).find()) {
            log.warn("检测到XSS攻击: User-Agent={}", userAgent);
            throw new ServiceException("检测到非法请求头");
        }
    }

    /**
     * 验证文件上传类型
     */
    private void validateFileType(MultipartHttpServletRequest request, String[] allowedTypes) {
        if (allowedTypes.length == 0) {
            return;
        }

        request.getFileMap().forEach((key, file) -> {
            if (file != null && !file.isEmpty()) {
                String filename = file.getOriginalFilename();
                if (filename != null) {
                    String extension = getFileExtension(filename).toLowerCase();
                    boolean isAllowed = Arrays.stream(allowedTypes)
                        .anyMatch(type -> type.toLowerCase().equals(extension));
                    
                    if (!isAllowed) {
                        log.warn("不允许的文件类型: 文件={}, 扩展名={}, 允许类型={}", 
                            filename, extension, Arrays.toString(allowedTypes));
                        throw new ServiceException("不允许的文件类型: " + extension);
                    }
                }
            }
        });
    }

    /**
     * 验证请求头
     */
    private void validateHeaders(HttpServletRequest request) {
        // 检查Content-Type
        String contentType = request.getContentType();
        if (contentType != null) {
            // 检查是否包含可疑的Content-Type
            if (contentType.contains("script") || contentType.contains("javascript")) {
                log.warn("可疑的Content-Type: {}", contentType);
                throw new ServiceException("不支持的Content-Type");
            }
        }

        // 检查Referer（防止CSRF）
        String referer = request.getHeader("Referer");
        if (referer != null && (referer.contains("javascript:") || referer.contains("data:"))) {
            log.warn("可疑的Referer: {}", referer);
            throw new ServiceException("非法的Referer");
        }
    }

    /**
     * 获取文件扩展名
     */
    private String getFileExtension(String filename) {
        int lastDotIndex = filename.lastIndexOf('.');
        if (lastDotIndex > 0 && lastDotIndex < filename.length() - 1) {
            return filename.substring(lastDotIndex + 1);
        }
        return "";
    }
}