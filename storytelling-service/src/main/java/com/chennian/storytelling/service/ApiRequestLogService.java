package com.chennian.storytelling.service;

import com.chennian.storytelling.bean.model.ApiRequestLog;

import java.util.List;

/**
 * API请求日志服务接口
 * 用于管理API请求日志
 *
 * @author chennian
 */
public interface ApiRequestLogService {

    /**
     * 保存API请求日志
     *
     * @param apiRequestLog API请求日志对象
     * @return 影响的行数
     */
    int saveApiRequestLog(ApiRequestLog apiRequestLog);

    /**
     * 根据ID查询API请求日志
     *
     * @param id 日志ID
     * @return API请求日志对象
     */
    ApiRequestLog getApiRequestLogById(Long id);

    /**
     * 查询用户的API请求日志列表
     *
     * @param userId 用户ID
     * @return API请求日志列表
     */
    List<ApiRequestLog> getApiRequestLogsByUserId(String userId);

    /**
     * 查询指定时间范围内的API请求日志
     *
     * @param startTime 开始时间戳
     * @param endTime   结束时间戳
     * @return API请求日志列表
     */
    List<ApiRequestLog> getApiRequestLogsByTimeRange(Long startTime, Long endTime);

    /**
     * 查询指定URL的API请求日志
     *
     * @param requestUrl 请求URL
     * @return API请求日志列表
     */
    List<ApiRequestLog> getApiRequestLogsByUrl(String requestUrl);

    /**
     * 查询失败的API请求日志
     *
     * @return API请求日志列表
     */
    List<ApiRequestLog> getFailedApiRequestLogs();

    /**
     * 删除指定时间之前的API请求日志
     *
     * @param timestamp 时间戳
     * @return 影响的行数
     */
    int deleteApiRequestLogsBefore(Long timestamp);
}