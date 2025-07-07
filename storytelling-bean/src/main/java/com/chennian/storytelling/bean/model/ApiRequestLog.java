package com.chennian.storytelling.bean.model;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;
import org.springframework.data.mongodb.core.index.Indexed;
import java.io.Serializable;
import java.util.Date;
import lombok.Data;

/**
 * API请求日志
 * @author chen
 * MongoDB文档模型
 */
@Document(collection = "api_request_log")
@Data
public class ApiRequestLog implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 主键ID
     */
    @Id
    private String id;

    /**
     * 请求ID
     */
    @Field("request_id")
    @Indexed
    private String requestId;

    /**
     * 用户ID
     */
    @Field("user_id")
    @Indexed
    private String userId;

    /**
     * 用户名
     */
    @Field("username")
    @Indexed
    private String username;

    /**
     * 请求方法
     */
    @Field("method")
    private String method;

    /**
     * 请求URL
     */
    @Field("url")
    @Indexed
    private String url;

    /**
     * 请求参数
     */
    @Field("params")
    private String params;

    /**
     * 请求体
     */
    @Field("request_body")
    private String requestBody;

    /**
     * 响应状态码
     */
    @Field("status_code")
    @Indexed
    private Integer statusCode;

    /**
     * 响应体
     */
    @Field("response_body")
    private String responseBody;

    /**
     * 请求时间
     */
    @Field("request_time")
    @Indexed
    private Date requestTime;

    /**
     * 响应时间
     */
    @Field("response_time")
    private Date responseTime;

    /**
     * 处理时长（毫秒）
     */
    @Field("duration")
    private Long duration;

    /**
     * 客户端IP
     */
    @Field("client_ip")
    @Indexed
    private String clientIp;

    /**
     * 用户代理
     */
    @Field("user_agent")
    private String userAgent;

    /**
     * 异常信息
     */
    @Field("error_message")
    private String errorMessage;

    /**
     * 流程实例ID
     */
    @Field("process_instance_id")
    @Indexed
    private String processInstanceId;

    /**
     * 处理时长（毫秒）
     */
    @Field("processing_time")
    private Long processingTime;

    /**
     * 成功状态
     */
    @Field("success_status")
    @Indexed
    private Boolean successStatus;

    /**
     * 请求URL
     */
    @Field(value = "request_url")
    private String requestUrl;


}