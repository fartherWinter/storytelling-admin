package com.chennian.storytelling.service.impl;

import com.chennian.storytelling.bean.model.ApiRequestLog;
import com.chennian.storytelling.service.ApiRequestLogService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;
import java.util.concurrent.atomic.AtomicLong;
import java.util.stream.Collectors;

/**
 * API请求日志服务实现类
 * 注：实际项目中应该使用数据库存储，这里使用内存存储作为示例
 *
 * @author chennian
 */
@Service
public class ApiRequestLogServiceImpl implements ApiRequestLogService {

    private static final Logger logger = LoggerFactory.getLogger(ApiRequestLogServiceImpl.class);
    
    // 使用内存存储日志（实际项目中应该使用数据库）
    private final ConcurrentMap<Long, ApiRequestLog> logMap = new ConcurrentHashMap<>();
    private final AtomicLong idGenerator = new AtomicLong(1);

    @Override
    public int saveApiRequestLog(ApiRequestLog apiRequestLog) {
        if (apiRequestLog == null) {
            return 0;
        }
        
        // 设置ID和请求时间（如果未设置）
        if (apiRequestLog.getId() == null) {
            apiRequestLog.setId(idGenerator.getAndIncrement());
        }
        
        if (apiRequestLog.getRequestTime() == null) {
            apiRequestLog.setRequestTime(new Date());
        }
        
        // 保存日志
        logMap.put(apiRequestLog.getId(), apiRequestLog);
        logger.info("保存API请求日志: {}", apiRequestLog);
        
        return 1;
    }

    @Override
    public ApiRequestLog getApiRequestLogById(Long id) {
        if (id == null) {
            return null;
        }
        return logMap.get(id);
    }

    @Override
    public List<ApiRequestLog> getApiRequestLogsByUserId(String userId) {
        if (userId == null || userId.isEmpty()) {
            return new ArrayList<>();
        }
        
        return logMap.values().stream()
                .filter(log -> userId.equals(log.getUserId()))
                .collect(Collectors.toList());
    }

    @Override
    public List<ApiRequestLog> getApiRequestLogsByTimeRange(Long startTime, Long endTime) {
        if (startTime == null || endTime == null) {
            return new ArrayList<>();
        }
        
        Date start = new Date(startTime);
        Date end = new Date(endTime);
        
        return logMap.values().stream()
                .filter(log -> log.getRequestTime() != null && 
                        !log.getRequestTime().before(start) && 
                        !log.getRequestTime().after(end))
                .collect(Collectors.toList());
    }

    @Override
    public List<ApiRequestLog> getApiRequestLogsByUrl(String requestUrl) {
        if (requestUrl == null || requestUrl.isEmpty()) {
            return new ArrayList<>();
        }
        
        return logMap.values().stream()
                .filter(log -> requestUrl.equals(log.getRequestUrl()))
                .collect(Collectors.toList());
    }

    @Override
    public List<ApiRequestLog> getFailedApiRequestLogs() {
        return logMap.values().stream()
                .filter(log -> log.getSuccess() != null && !log.getSuccess())
                .collect(Collectors.toList());
    }

    @Override
    public int deleteApiRequestLogsBefore(Long timestamp) {
        if (timestamp == null) {
            return 0;
        }
        
        Date thresholdDate = new Date(timestamp);
        List<Long> idsToRemove = logMap.values().stream()
                .filter(log -> log.getRequestTime() != null && log.getRequestTime().before(thresholdDate))
                .map(ApiRequestLog::getId)
                .collect(Collectors.toList());
        
        int count = 0;
        for (Long id : idsToRemove) {
            if (logMap.remove(id) != null) {
                count++;
            }
        }
        
        logger.info("删除{}条过期API请求日志", count);
        return count;
    }
}