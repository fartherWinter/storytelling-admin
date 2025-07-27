package com.chennian.storytelling.inventory.service.impl;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.chennian.storytelling.common.exception.BusinessException;
import com.chennian.storytelling.common.util.SecurityUtils;
import com.chennian.storytelling.inventory.entity.InventoryLog;
import com.chennian.storytelling.inventory.mapper.InventoryLogMapper;
import com.chennian.storytelling.inventory.service.InventoryLogService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 库存操作日志服务实现类
 * 
 * @author chennian
 * @since 2024-01-01
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class InventoryLogServiceImpl extends ServiceImpl<InventoryLogMapper, InventoryLog> implements InventoryLogService {

    private final InventoryLogMapper inventoryLogMapper;

    @Override
    public InventoryLog getInventoryLogById(Long logId) {
        if (logId == null) {
            throw new BusinessException("日志ID不能为空");
        }
        return inventoryLogMapper.selectById(logId);
    }

    @Override
    public IPage<InventoryLog> getInventoryLogPage(Page<InventoryLog> page, Long inventoryId, Long productId,
                                                 Long warehouseId, String skuCode, Integer operationType,
                                                 Integer operationResult, String businessNo, Integer businessType,
                                                 Long operatorId, String operatorName, LocalDateTime startTime, LocalDateTime endTime) {
        return inventoryLogMapper.selectInventoryLogPage(page, inventoryId, productId, warehouseId, skuCode,
                operationType, operationResult, businessNo, businessType, operatorId, operatorName, startTime, endTime);
    }

    @Override
    public List<InventoryLog> getInventoryLogListByInventoryId(Long inventoryId) {
        if (inventoryId == null) {
            throw new BusinessException("库存ID不能为空");
        }
        return inventoryLogMapper.selectInventoryLogListByInventoryId(inventoryId);
    }

    @Override
    public List<InventoryLog> getInventoryLogListByProductId(Long productId) {
        if (productId == null) {
            throw new BusinessException("商品ID不能为空");
        }
        return inventoryLogMapper.selectInventoryLogListByProductId(productId);
    }

    @Override
    public List<InventoryLog> getInventoryLogListByWarehouseId(Long warehouseId) {
        if (warehouseId == null) {
            throw new BusinessException("仓库ID不能为空");
        }
        return inventoryLogMapper.selectInventoryLogListByWarehouseId(warehouseId);
    }

    @Override
    public List<InventoryLog> getInventoryLogListByBusinessNo(String businessNo) {
        if (!StringUtils.hasText(businessNo)) {
            throw new BusinessException("业务单号不能为空");
        }
        return inventoryLogMapper.selectInventoryLogListByBusinessNo(businessNo);
    }

    @Override
    public List<InventoryLog> getInventoryLogListByOperationType(Integer operationType, LocalDateTime startTime, LocalDateTime endTime) {
        if (operationType == null) {
            throw new BusinessException("操作类型不能为空");
        }
        return inventoryLogMapper.selectInventoryLogListByOperationType(operationType, startTime, endTime);
    }

    @Override
    public List<InventoryLog> getInventoryLogListByOperator(Long operatorId, LocalDateTime startTime, LocalDateTime endTime) {
        if (operatorId == null) {
            throw new BusinessException("操作人ID不能为空");
        }
        return inventoryLogMapper.selectInventoryLogListByOperator(operatorId, startTime, endTime);
    }

    @Override
    public List<InventoryLog> getInventoryLogListByOperationResult(Integer operationResult, LocalDateTime startTime, LocalDateTime endTime) {
        if (operationResult == null) {
            throw new BusinessException("操作结果不能为空");
        }
        return inventoryLogMapper.selectInventoryLogListByOperationResult(operationResult, startTime, endTime);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean createInventoryLog(InventoryLog inventoryLog) {
        if (inventoryLog == null) {
            throw new BusinessException("库存操作日志不能为空");
        }
        
        // 校验必填字段
        validateInventoryLog(inventoryLog);
        
        // 设置创建信息
        inventoryLog.setCreateTime(LocalDateTime.now());
        inventoryLog.setCreateBy(SecurityUtils.getCurrentUserId());
        
        // 如果操作人信息为空，使用当前用户
        if (inventoryLog.getOperatorId() == null) {
            inventoryLog.setOperatorId(SecurityUtils.getCurrentUserId());
        }
        if (!StringUtils.hasText(inventoryLog.getOperatorName())) {
            inventoryLog.setOperatorName(SecurityUtils.getCurrentUsername());
        }
        if (inventoryLog.getOperationTime() == null) {
            inventoryLog.setOperationTime(LocalDateTime.now());
        }
        
        // 获取请求信息
        fillRequestInfo(inventoryLog);
        
        return inventoryLogMapper.insert(inventoryLog) > 0;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean batchCreateInventoryLog(List<InventoryLog> inventoryLogList) {
        if (CollectionUtils.isEmpty(inventoryLogList)) {
            throw new BusinessException("库存操作日志列表不能为空");
        }
        
        for (InventoryLog inventoryLog : inventoryLogList) {
            createInventoryLog(inventoryLog);
        }
        
        return true;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean deleteInventoryLog(Long logId) {
        if (logId == null) {
            throw new BusinessException("日志ID不能为空");
        }
        
        return inventoryLogMapper.deleteById(logId) > 0;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean batchDeleteInventoryLog(List<Long> logIds) {
        if (CollectionUtils.isEmpty(logIds)) {
            throw new BusinessException("日志ID列表不能为空");
        }
        
        return inventoryLogMapper.batchDeleteInventoryLog(logIds) > 0;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean deleteInventoryLogByTimeRange(LocalDateTime endTime) {
        if (endTime == null) {
            throw new BusinessException("结束时间不能为空");
        }
        
        return inventoryLogMapper.deleteInventoryLogByTimeRange(endTime) > 0;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean cleanExpiredLogs(Integer retentionDays) {
        if (retentionDays == null || retentionDays <= 0) {
            throw new BusinessException("保留天数必须大于0");
        }
        
        return inventoryLogMapper.cleanExpiredLogs(retentionDays) > 0;
    }

    @Override
    public Map<String, Object> getOperationStatistics(Long inventoryId, LocalDateTime startTime, LocalDateTime endTime) {
        return inventoryLogMapper.getOperationStatistics(inventoryId, startTime, endTime);
    }

    @Override
    public List<Map<String, Object>> getOperatorStatistics(LocalDateTime startTime, LocalDateTime endTime) {
        return inventoryLogMapper.getOperatorStatistics(startTime, endTime);
    }

    @Override
    public List<Map<String, Object>> getOperationTypeStatistics(LocalDateTime startTime, LocalDateTime endTime) {
        return inventoryLogMapper.getOperationTypeStatistics(startTime, endTime);
    }

    @Override
    public Map<String, Object> getOperationResultStatistics(LocalDateTime startTime, LocalDateTime endTime) {
        return inventoryLogMapper.getOperationResultStatistics(startTime, endTime);
    }

    @Override
    public List<Map<String, Object>> getErrorOperationStatistics(LocalDateTime startTime, LocalDateTime endTime) {
        return inventoryLogMapper.getErrorOperationStatistics(startTime, endTime);
    }

    @Override
    public List<Map<String, Object>> getSlowOperationStatistics(Long executionTime, LocalDateTime startTime, LocalDateTime endTime) {
        if (executionTime == null || executionTime <= 0) {
            throw new BusinessException("执行时间阈值必须大于0");
        }
        return inventoryLogMapper.getSlowOperationStatistics(executionTime, startTime, endTime);
    }

    @Override
    public Double getAverageExecutionTime(Integer operationType, LocalDateTime startTime, LocalDateTime endTime) {
        Double avgTime = inventoryLogMapper.getAverageExecutionTime(operationType, startTime, endTime);
        return avgTime != null ? avgTime : 0.0;
    }

    @Override
    public List<Map<String, Object>> getOperationFrequencyStatistics(LocalDateTime startTime, LocalDateTime endTime, String groupBy) {
        if (!StringUtils.hasText(groupBy)) {
            groupBy = "day";
        }
        if (!"hour".equals(groupBy) && !"day".equals(groupBy) && !"month".equals(groupBy)) {
            throw new BusinessException("分组方式只支持：hour、day、month");
        }
        return inventoryLogMapper.getOperationFrequencyStatistics(startTime, endTime, groupBy);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean recordInventoryLog(Long inventoryId, Integer operationType, String operationDesc,
                                    String beforeData, String afterData, Integer operationResult,
                                    String errorMessage, String businessNo, Integer businessType,
                                    Long executionTime, String remark) {
        InventoryLog inventoryLog = new InventoryLog();
        inventoryLog.setInventoryId(inventoryId);
        inventoryLog.setOperationType(operationType);
        inventoryLog.setOperationDesc(operationDesc);
        inventoryLog.setBeforeData(beforeData);
        inventoryLog.setAfterData(afterData);
        inventoryLog.setOperationResult(operationResult);
        inventoryLog.setErrorMessage(errorMessage);
        inventoryLog.setBusinessNo(businessNo);
        inventoryLog.setBusinessType(businessType);
        inventoryLog.setExecutionTime(executionTime != null ? executionTime : 0L);
        inventoryLog.setRemark(remark);
        
        return createInventoryLog(inventoryLog);
    }

    @Override
    public String exportInventoryLog(Long inventoryId, Integer operationType, Integer operationResult,
                                   LocalDateTime startTime, LocalDateTime endTime) {
        // 构建导出文件名
        String fileName = "inventory_log_" + LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyyMMdd_HHmmss")) + ".xlsx";
        
        // 这里应该实现具体的导出逻辑，比如使用 EasyExcel 等工具
        // 为了示例，这里只返回文件名
        log.info("导出库存操作日志，文件名：{}", fileName);
        
        // 记录操作日志
        recordInventoryLog(inventoryId, 31, "导出库存操作日志",
                null, null, 1, null, null, null, 0L, "导出库存操作日志到文件：" + fileName);
        
        return fileName;
    }

    @Override
    public Map<String, Object> generateInventoryLogReport(LocalDateTime startTime, LocalDateTime endTime, Integer reportType) {
        if (startTime == null || endTime == null) {
            throw new BusinessException("开始时间和结束时间不能为空");
        }
        if (reportType == null) {
            throw new BusinessException("报表类型不能为空");
        }
        
        Map<String, Object> report = new HashMap<>();
        report.put("reportType", reportType);
        report.put("startTime", startTime);
        report.put("endTime", endTime);
        report.put("generateTime", LocalDateTime.now());
        
        switch (reportType) {
            case 1: // 操作统计报表
                report.put("title", "库存操作统计报表");
                report.put("operationStatistics", getOperationStatistics(null, startTime, endTime));
                report.put("operatorStatistics", getOperatorStatistics(startTime, endTime));
                report.put("operationTypeStatistics", getOperationTypeStatistics(startTime, endTime));
                report.put("operationResultStatistics", getOperationResultStatistics(startTime, endTime));
                break;
            case 2: // 错误统计报表
                report.put("title", "库存操作错误统计报表");
                report.put("errorStatistics", getErrorOperationStatistics(startTime, endTime));
                report.put("errorResultStatistics", getOperationResultStatistics(startTime, endTime));
                break;
            case 3: // 性能统计报表
                report.put("title", "库存操作性能统计报表");
                report.put("slowOperationStatistics", getSlowOperationStatistics(1000L, startTime, endTime));
                report.put("averageExecutionTime", getAverageExecutionTime(null, startTime, endTime));
                report.put("operationFrequencyStatistics", getOperationFrequencyStatistics(startTime, endTime, "day"));
                break;
            default:
                throw new BusinessException("不支持的报表类型：" + reportType);
        }
        
        // 记录操作日志
        recordInventoryLog(null, 32, "生成库存操作报表",
                null, report.toString(), 1, null, null, null, 0L, "生成库存操作报表，类型：" + reportType);
        
        return report;
    }

    /**
     * 校验库存操作日志
     */
    private void validateInventoryLog(InventoryLog inventoryLog) {
        if (inventoryLog.getOperationType() == null) {
            throw new BusinessException("操作类型不能为空");
        }
        if (!StringUtils.hasText(inventoryLog.getOperationDesc())) {
            throw new BusinessException("操作描述不能为空");
        }
        if (inventoryLog.getOperationResult() == null) {
            throw new BusinessException("操作结果不能为空");
        }
        if (inventoryLog.getExecutionTime() != null && inventoryLog.getExecutionTime() < 0) {
            throw new BusinessException("执行时间不能小于0");
        }
    }

    /**
     * 填充请求信息
     */
    private void fillRequestInfo(InventoryLog inventoryLog) {
        try {
            ServletRequestAttributes attributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
            if (attributes != null) {
                HttpServletRequest request = attributes.getRequest();
                
                // 设置操作IP
                if (!StringUtils.hasText(inventoryLog.getOperationIp())) {
                    inventoryLog.setOperationIp(getClientIpAddress(request));
                }
                
                // 设置用户代理
                if (!StringUtils.hasText(inventoryLog.getUserAgent())) {
                    inventoryLog.setUserAgent(request.getHeader("User-Agent"));
                }
                
                // 设置请求方法
                if (!StringUtils.hasText(inventoryLog.getRequestMethod())) {
                    inventoryLog.setRequestMethod(request.getMethod());
                }
                
                // 设置请求URL
                if (!StringUtils.hasText(inventoryLog.getRequestUrl())) {
                    inventoryLog.setRequestUrl(request.getRequestURL().toString());
                }
                
                // 设置请求参数
                if (!StringUtils.hasText(inventoryLog.getRequestParams())) {
                    inventoryLog.setRequestParams(getRequestParams(request));
                }
            }
        } catch (Exception e) {
            log.warn("填充请求信息失败", e);
        }
    }

    /**
     * 获取客户端IP地址
     */
    private String getClientIpAddress(HttpServletRequest request) {
        String ip = request.getHeader("X-Forwarded-For");
        if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getHeader("Proxy-Client-IP");
        }
        if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getHeader("WL-Proxy-Client-IP");
        }
        if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getHeader("HTTP_CLIENT_IP");
        }
        if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getHeader("HTTP_X_FORWARDED_FOR");
        }
        if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getRemoteAddr();
        }
        
        // 如果是多级代理，取第一个IP
        if (ip != null && ip.contains(",")) {
            ip = ip.split(",")[0].trim();
        }
        
        return ip;
    }

    /**
     * 获取请求参数
     */
    private String getRequestParams(HttpServletRequest request) {
        try {
            Map<String, String[]> parameterMap = request.getParameterMap();
            if (parameterMap.isEmpty()) {
                return null;
            }
            
            StringBuilder params = new StringBuilder();
            for (Map.Entry<String, String[]> entry : parameterMap.entrySet()) {
                if (params.length() > 0) {
                    params.append("&");
                }
                params.append(entry.getKey()).append("=");
                String[] values = entry.getValue();
                if (values.length == 1) {
                    params.append(values[0]);
                } else {
                    params.append(String.join(",", values));
                }
            }
            
            return params.toString();
        } catch (Exception e) {
            log.warn("获取请求参数失败", e);
            return null;
        }
    }
}