package com.chennian.storytelling.service.impl;

import com.chennian.storytelling.bean.model.ApiRequestLog;
import com.chennian.storytelling.repository.ApiRequestLogRepository;
import com.chennian.storytelling.service.ApiRequestLogService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;

/**
 * API请求日志服务实现类
 * 使用MongoDB持久化存储
 *
 * @author chennian
 */

@Service
public class ApiRequestLogServiceImpl implements ApiRequestLogService {

    private static final Logger logger = LoggerFactory.getLogger(ApiRequestLogServiceImpl.class);
    
    @Autowired
    private ApiRequestLogRepository apiRequestLogRepository;

    @Override
    public int saveApiRequestLog(ApiRequestLog apiRequestLog) {
        if (apiRequestLog == null) {
            return 0;
        }
        
        try {
            // 设置请求时间（如果未设置）
            if (apiRequestLog.getRequestTime() == null) {
                apiRequestLog.setRequestTime(new Date());
            }
            
            // 保存到MongoDB
            ApiRequestLog savedLog = apiRequestLogRepository.save(apiRequestLog);
            logger.info("保存API请求日志到MongoDB: ID={}, URL={}, 用户ID={}", 
                    savedLog.getId(), savedLog.getUrl(), savedLog.getUserId());
            
            return 1;
        } catch (Exception e) {
            logger.error("保存API请求日志失败: {}", e.getMessage(), e);
            return 0;
        }
    }

    @Override
    public ApiRequestLog getApiRequestLogById(Long id) {
        if (id == null) {
            return null;
        }
        
        try {
            // MongoDB使用String类型的ID，需要转换
            Optional<ApiRequestLog> logOptional = apiRequestLogRepository.findById(String.valueOf(id));
            return logOptional.orElse(null);
        } catch (Exception e) {
            logger.error("根据ID查询API请求日志失败: {}", e.getMessage(), e);
            return null;
        }
    }

    @Override
    public List<ApiRequestLog> getApiRequestLogsByUserId(String userId) {
        if (userId == null || userId.isEmpty()) {
            return new ArrayList<>();
        }
        
        try {
            return apiRequestLogRepository.findByUserId(userId);
        } catch (Exception e) {
            logger.error("根据用户ID查询API请求日志失败: {}", e.getMessage(), e);
            return new ArrayList<>();
        }
    }

    @Override
    public List<ApiRequestLog> getApiRequestLogsByTimeRange(Long startTime, Long endTime) {
        if (startTime == null || endTime == null) {
            return new ArrayList<>();
        }
        
        try {
            Date start = new Date(startTime);
            Date end = new Date(endTime);
            
            return apiRequestLogRepository.findByRequestTimeBetween(start, end);
        } catch (Exception e) {
            logger.error("根据时间范围查询API请求日志失败: {}", e.getMessage(), e);
            return new ArrayList<>();
        }
    }

    @Override
    public List<ApiRequestLog> getApiRequestLogsByUrl(String requestUrl) {
        if (requestUrl == null || requestUrl.isEmpty()) {
            return new ArrayList<>();
        }
        
        try {
            return apiRequestLogRepository.findByUrl(requestUrl);
        } catch (Exception e) {
            logger.error("根据URL查询API请求日志失败: {}", e.getMessage(), e);
            return new ArrayList<>();
        }
    }

    @Override
    public List<ApiRequestLog> getFailedApiRequestLogs() {
        try {
            return apiRequestLogRepository.findBySuccessStatusFalse();
        } catch (Exception e) {
            logger.error("查询失败的API请求日志失败: {}", e.getMessage(), e);
            return new ArrayList<>();
        }
    }

    @Override
    public int deleteApiRequestLogsBefore(Long timestamp) {
        if (timestamp == null) {    
            return 0;
        }
        
        try {
            Date thresholdDate = new Date(timestamp);
            long deletedCount = apiRequestLogRepository.deleteByRequestTimeBefore(thresholdDate);
            
            logger.info("删除{}条过期的API请求日志", deletedCount);
            return (int) deletedCount;
        } catch (Exception e) {
            logger.error("删除过期API请求日志失败: {}", e.getMessage(), e);
            return 0;
        }
    }
}