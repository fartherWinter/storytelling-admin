package com.chennian.storytelling.repository;

import com.chennian.storytelling.bean.model.ApiRequestLog;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Date;
import java.util.List;

/**
 * API请求日志MongoDB Repository
 * @author chen
 */
@Repository
public interface ApiRequestLogRepository extends MongoRepository<ApiRequestLog, String> {

    /**
     * 根据用户ID查询日志
     */
    List<ApiRequestLog> findByUserId(String userId);

    /**
     * 根据请求URL查询日志
     */
    List<ApiRequestLog> findByUrl(String url);

    /**
     * 根据时间范围查询日志
     */
    List<ApiRequestLog> findByRequestTimeBetween(Date startTime, Date endTime);

    /**
     * 查询失败的请求日志
     */
    List<ApiRequestLog> findBySuccessStatusFalse();

    /**
     * 根据流程实例ID查询日志
     */
    List<ApiRequestLog> findByProcessInstanceId(String processInstanceId);

    /**
     * 根据状态码查询日志
     */
    List<ApiRequestLog> findByStatusCode(Integer statusCode);

    /**
     * 根据客户端IP查询日志
     */
    List<ApiRequestLog> findByClientIp(String clientIp);

    /**
     * 删除指定时间之前的日志
     */
    @Query(value = "{'requestTime': {$lt: ?0}}", delete = true)
    long deleteByRequestTimeBefore(Date date);

    /**
     * 统计用户请求次数
     */
    long countByUserId(String userId);

    /**
     * 统计指定时间范围内的请求次数
     */
    long countByRequestTimeBetween(Date startTime, Date endTime);

    /**
     * 查询最近的日志记录
     */
    List<ApiRequestLog> findTop10ByOrderByRequestTimeDesc();
}