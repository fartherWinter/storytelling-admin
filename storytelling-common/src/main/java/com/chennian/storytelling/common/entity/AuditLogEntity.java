package com.chennian.storytelling.common.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;
import lombok.experimental.Accessors;

import java.time.LocalDateTime;

/**
 * 审计日志实体
 *
 * @author chennian
 * @date 2024/01/01
 */
@Data
@Accessors(chain = true)
@TableName("audit_log")
public class AuditLogEntity {

    /**
     * 主键ID
     */
    @TableId(type = IdType.ASSIGN_ID)
    private Long id;

    /**
     * 操作用户ID
     */
    private Long userId;

    /**
     * 操作用户名
     */
    private String username;

    /**
     * 操作模块
     */
    private String module;

    /**
     * 操作类型
     */
    private String operationType;

    /**
     * 操作描述
     */
    private String description;

    /**
     * 请求方法
     */
    private String method;

    /**
     * 请求URL
     */
    private String requestUrl;

    /**
     * 请求参数
     */
    private String requestParams;

    /**
     * 返回结果
     */
    private String result;

    /**
     * 异常信息
     */
    private String exception;

    /**
     * 客户端IP
     */
    private String clientIp;

    /**
     * 用户代理
     */
    private String userAgent;

    /**
     * 执行时间（毫秒）
     */
    private Long executionTime;

    /**
     * 操作状态（0-失败，1-成功）
     */
    private Integer status;

    /**
     * 创建时间
     */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime createTime;

    /**
     * 操作状态枚举
     */
    public enum Status {
        /**
         * 失败
         */
        FAILURE(0, "失败"),
        /**
         * 成功
         */
        SUCCESS(1, "成功");

        private final Integer code;
        private final String description;

        Status(Integer code, String description) {
            this.code = code;
            this.description = description;
        }

        public Integer getCode() {
            return code;
        }

        public String getDescription() {
            return description;
        }
    }
}