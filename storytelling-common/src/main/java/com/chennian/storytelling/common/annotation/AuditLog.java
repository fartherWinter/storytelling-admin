package com.chennian.storytelling.common.annotation;

import java.lang.annotation.*;

/**
 * 审计日志注解
 * 用于标记需要记录操作日志的接口
 *
 * @author chennian
 * @date 2024/01/01
 */
@Target({ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface AuditLog {

    /**
     * 操作模块
     */
    String module() default "";

    /**
     * 操作类型
     */
    OperationType type() default OperationType.OTHER;

    /**
     * 操作描述
     */
    String description() default "";

    /**
     * 是否记录请求参数
     */
    boolean recordParams() default true;

    /**
     * 是否记录返回结果
     */
    boolean recordResult() default false;

    /**
     * 是否记录异常信息
     */
    boolean recordException() default true;

    /**
     * 操作类型枚举
     */
    enum OperationType {
        /**
         * 查询
         */
        SELECT("查询"),
        /**
         * 新增
         */
        INSERT("新增"),
        /**
         * 修改
         */
        UPDATE("修改"),
        /**
         * 删除
         */
        DELETE("删除"),
        /**
         * 登录
         */
        LOGIN("登录"),
        /**
         * 登出
         */
        LOGOUT("登出"),
        /**
         * 导入
         */
        IMPORT("导入"),
        /**
         * 导出
         */
        EXPORT("导出"),
        /**
         * 上传
         */
        UPLOAD("上传"),
        /**
         * 下载
         */
        DOWNLOAD("下载"),
        /**
         * 其他
         */
        OTHER("其他");

        private final String description;

        OperationType(String description) {
            this.description = description;
        }

        public String getDescription() {
            return description;
        }
    }
}