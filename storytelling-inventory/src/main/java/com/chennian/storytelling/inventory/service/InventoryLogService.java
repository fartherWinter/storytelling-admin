package com.chennian.storytelling.inventory.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.chennian.storytelling.inventory.entity.InventoryLog;

import java.time.LocalDateTime;
import java.util.List;

/**
 * 库存操作日志服务接口
 * 
 * @author chennian
 * @since 2024-01-01
 */
public interface InventoryLogService {

    /**
     * 根据日志ID查询库存操作日志
     * 
     * @param logId 日志ID
     * @return 库存操作日志
     */
    InventoryLog getInventoryLogById(Long logId);

    /**
     * 分页查询库存操作日志列表
     * 
     * @param page 分页参数
     * @param inventoryId 库存ID
     * @param productId 商品ID
     * @param warehouseId 仓库ID
     * @param skuCode SKU编码
     * @param operationType 操作类型
     * @param operationResult 操作结果
     * @param businessNo 关联业务单号
     * @param businessType 关联业务类型
     * @param operatorId 操作人ID
     * @param operatorName 操作人姓名
     * @param startTime 开始时间
     * @param endTime 结束时间
     * @return 库存操作日志分页列表
     */
    IPage<InventoryLog> getInventoryLogPage(Page<InventoryLog> page, Long inventoryId, Long productId,
                                          Long warehouseId, String skuCode, Integer operationType,
                                          Integer operationResult, String businessNo, Integer businessType,
                                          Long operatorId, String operatorName, LocalDateTime startTime, LocalDateTime endTime);

    /**
     * 根据库存ID查询操作日志列表
     * 
     * @param inventoryId 库存ID
     * @return 操作日志列表
     */
    List<InventoryLog> getInventoryLogListByInventoryId(Long inventoryId);

    /**
     * 根据商品ID查询操作日志列表
     * 
     * @param productId 商品ID
     * @return 操作日志列表
     */
    List<InventoryLog> getInventoryLogListByProductId(Long productId);

    /**
     * 根据仓库ID查询操作日志列表
     * 
     * @param warehouseId 仓库ID
     * @return 操作日志列表
     */
    List<InventoryLog> getInventoryLogListByWarehouseId(Long warehouseId);

    /**
     * 根据业务单号查询操作日志列表
     * 
     * @param businessNo 业务单号
     * @return 操作日志列表
     */
    List<InventoryLog> getInventoryLogListByBusinessNo(String businessNo);

    /**
     * 根据操作类型查询操作日志列表
     * 
     * @param operationType 操作类型
     * @param startTime 开始时间
     * @param endTime 结束时间
     * @return 操作日志列表
     */
    List<InventoryLog> getInventoryLogListByOperationType(Integer operationType, LocalDateTime startTime, LocalDateTime endTime);

    /**
     * 根据操作人查询操作日志列表
     * 
     * @param operatorId 操作人ID
     * @param startTime 开始时间
     * @param endTime 结束时间
     * @return 操作日志列表
     */
    List<InventoryLog> getInventoryLogListByOperator(Long operatorId, LocalDateTime startTime, LocalDateTime endTime);

    /**
     * 根据操作结果查询操作日志列表
     * 
     * @param operationResult 操作结果
     * @param startTime 开始时间
     * @param endTime 结束时间
     * @return 操作日志列表
     */
    List<InventoryLog> getInventoryLogListByOperationResult(Integer operationResult, LocalDateTime startTime, LocalDateTime endTime);

    /**
     * 创建库存操作日志
     * 
     * @param inventoryLog 库存操作日志
     * @return 是否成功
     */
    boolean createInventoryLog(InventoryLog inventoryLog);

    /**
     * 批量创建库存操作日志
     * 
     * @param inventoryLogList 库存操作日志列表
     * @return 是否成功
     */
    boolean batchCreateInventoryLog(List<InventoryLog> inventoryLogList);

    /**
     * 删除库存操作日志
     * 
     * @param logId 日志ID
     * @return 是否成功
     */
    boolean deleteInventoryLog(Long logId);

    /**
     * 批量删除库存操作日志
     * 
     * @param logIds 日志ID列表
     * @return 是否成功
     */
    boolean batchDeleteInventoryLog(List<Long> logIds);

    /**
     * 根据时间范围删除库存操作日志
     * 
     * @param endTime 结束时间
     * @return 是否成功
     */
    boolean deleteInventoryLogByTimeRange(LocalDateTime endTime);

    /**
     * 清理过期日志
     * 
     * @param retentionDays 保留天数
     * @return 是否成功
     */
    boolean cleanExpiredLogs(Integer retentionDays);

    /**
     * 获取操作统计信息
     * 
     * @param inventoryId 库存ID
     * @param startTime 开始时间
     * @param endTime 结束时间
     * @return 操作统计信息
     */
    java.util.Map<String, Object> getOperationStatistics(Long inventoryId, LocalDateTime startTime, LocalDateTime endTime);

    /**
     * 获取操作人统计信息
     * 
     * @param startTime 开始时间
     * @param endTime 结束时间
     * @return 操作人统计信息
     */
    List<java.util.Map<String, Object>> getOperatorStatistics(LocalDateTime startTime, LocalDateTime endTime);

    /**
     * 获取操作类型统计信息
     * 
     * @param startTime 开始时间
     * @param endTime 结束时间
     * @return 操作类型统计信息
     */
    List<java.util.Map<String, Object>> getOperationTypeStatistics(LocalDateTime startTime, LocalDateTime endTime);

    /**
     * 获取操作结果统计信息
     * 
     * @param startTime 开始时间
     * @param endTime 结束时间
     * @return 操作结果统计信息
     */
    java.util.Map<String, Object> getOperationResultStatistics(LocalDateTime startTime, LocalDateTime endTime);

    /**
     * 获取错误操作统计信息
     * 
     * @param startTime 开始时间
     * @param endTime 结束时间
     * @return 错误操作统计信息
     */
    List<java.util.Map<String, Object>> getErrorOperationStatistics(LocalDateTime startTime, LocalDateTime endTime);

    /**
     * 获取慢操作统计信息
     * 
     * @param executionTime 执行时间阈值（毫秒）
     * @param startTime 开始时间
     * @param endTime 结束时间
     * @return 慢操作统计信息
     */
    List<java.util.Map<String, Object>> getSlowOperationStatistics(Long executionTime, LocalDateTime startTime, LocalDateTime endTime);

    /**
     * 获取平均执行时间
     * 
     * @param operationType 操作类型
     * @param startTime 开始时间
     * @param endTime 结束时间
     * @return 平均执行时间
     */
    Double getAverageExecutionTime(Integer operationType, LocalDateTime startTime, LocalDateTime endTime);

    /**
     * 获取操作频率统计
     * 
     * @param startTime 开始时间
     * @param endTime 结束时间
     * @param groupBy 分组方式：hour、day、month
     * @return 操作频率统计
     */
    List<java.util.Map<String, Object>> getOperationFrequencyStatistics(LocalDateTime startTime, LocalDateTime endTime, String groupBy);

    /**
     * 记录库存操作日志
     * 
     * @param inventoryId 库存ID
     * @param operationType 操作类型
     * @param operationDesc 操作描述
     * @param beforeData 操作前数据
     * @param afterData 操作后数据
     * @param operationResult 操作结果
     * @param errorMessage 错误信息
     * @param businessNo 业务单号
     * @param businessType 业务类型
     * @param executionTime 执行时间
     * @param remark 备注
     * @return 是否成功
     */
    boolean recordInventoryLog(Long inventoryId, Integer operationType, String operationDesc,
                             String beforeData, String afterData, Integer operationResult,
                             String errorMessage, String businessNo, Integer businessType,
                             Long executionTime, String remark);

    /**
     * 导出库存操作日志
     * 
     * @param inventoryId 库存ID
     * @param operationType 操作类型
     * @param operationResult 操作结果
     * @param startTime 开始时间
     * @param endTime 结束时间
     * @return 导出文件路径
     */
    String exportInventoryLog(Long inventoryId, Integer operationType, Integer operationResult,
                            LocalDateTime startTime, LocalDateTime endTime);

    /**
     * 生成库存操作报表
     * 
     * @param startTime 开始时间
     * @param endTime 结束时间
     * @param reportType 报表类型：1-操作统计，2-错误统计，3-性能统计
     * @return 报表数据
     */
    java.util.Map<String, Object> generateInventoryLogReport(LocalDateTime startTime, LocalDateTime endTime, Integer reportType);
}