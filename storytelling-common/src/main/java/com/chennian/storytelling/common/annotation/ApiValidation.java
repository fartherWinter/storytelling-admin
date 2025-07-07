package com.chennian.storytelling.common.annotation;

import java.lang.annotation.*;

/**
 * API参数验证注解
 * 用于标记需要进行参数验证的接口
 *
 * @author chennian
 * @date 2024/01/01
 */
@Target({ElementType.METHOD, ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface ApiValidation {

    /**
     * 是否启用参数验证，默认启用
     */
    boolean enabled() default true;

    /**
     * 验证失败时的错误消息
     */
    String message() default "参数验证失败";

    /**
     * 是否验证请求头
     */
    boolean validateHeaders() default false;

    /**
     * 是否验证请求体大小
     */
    boolean validateBodySize() default true;

    /**
     * 最大请求体大小（字节），默认10MB
     */
    long maxBodySize() default 10 * 1024 * 1024;

    /**
     * 是否验证SQL注入
     */
    boolean validateSqlInjection() default true;

    /**
     * 是否验证XSS攻击
     */
    boolean validateXss() default true;

    /**
     * 是否验证文件上传类型
     */
    boolean validateFileType() default false;

    /**
     * 允许的文件类型
     */
    String[] allowedFileTypes() default {};
}