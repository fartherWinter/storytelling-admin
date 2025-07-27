package com.chennian.storytelling.inventory.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.chennian.storytelling.inventory.entity.InventoryLog;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.time.LocalDateTime;
import java.util.List;

/**
 * 库存操作日志Mapper接口
 * 
 * @author chennian
 * @since 2024-01-01
 */
@Mapper
public interface InventoryLogMapper extends BaseMapper<InventoryLog> {

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
    IPage<InventoryLog> selectInventoryLogPage(Page<InventoryLog> page,
                                             @Param("inventoryId") Long inventoryId,
                                             @Param("productId") Long productId,
                                             @Param("warehouseId") Long warehouseId,
                                             @Param("skuCode") String skuCode,
                                             @Param("operationType") Integer operationType,
                                             @Param("operationResult") Integer operationResult,
                                             @Param("businessNo") String businessNo,
                                             @Param("businessType") Integer businessType,
                                             @Param("operatorId") Long operatorId,
                                             @Param("operatorName") String operatorName,
                                             @Param("startTime") LocalDateTime startTime,
                                             @Param("endTime") LocalDateTime endTime);

    /**
     * 根据库存ID查询操作日志列表
     * 
     * @param inventoryId 库存ID
     * @return 操作日志列表
     */
    List<InventoryLog> selectByInventoryId(@Param("inventoryId") Long inventoryId);

    /**
     * 根据商品ID查询操作日志列表
     * 
     * @param productId 商品ID
     * @return 操作日志列表
     */
    List<InventoryLog> selectByProductId(@Param("productId") Long productId);

    /**
     * 根据仓库ID查询操作日志列表
     * 
     * @param warehouseId 仓库ID
     * @return 操作日志列表
     */
    List<InventoryLog> selectByWarehouseId(@Param("warehouseId") Long warehouseId);

    /**
     * 根据业务单号查询操作日志列表
     * 
     * @param businessNo 业务单号
     * @return 操作日志列表
     */
    List<InventoryLog> selectByBusinessNo(@Param("businessNo") String businessNo);

    /**
     * 根据操作类型查询操作日志列表
     * 
     * @param operationType 操作类型
     * @param startTime 开始时间
     * @param endTime 结束时间
     * @return 操作日志列表
     */
    List<InventoryLog> selectByOperationType(@Param("operationType") Integer operationType,
                                           @Param("startTime") LocalDateTime startTime,
                                           @Param("endTime") LocalDateTime endTime);

    /**
     * 根据操作人查询操作日志列表
     * 
     * @param operatorId 操作人ID
     * @param startTime 开始时间
     * @param endTime 结束时间
     * @return 操作日志列表
     */
    List<InventoryLog> selectByOperator(@Param("operatorId") Long operatorId,
                                      @Param("startTime") LocalDateTime startTime,
                                      @Param("endTime") LocalDateTime endTime);

    /**
     * 根据操作结果查询操作日志列表
     * 
     * @param operationResult 操作结果
     * @param startTime 开始时间
     * @param endTime 结束时间
     * @return 操作日志列表
     */
    List<InventoryLog> selectByOperationResult(@Param("operationResult") Integer operationResult,
                                             @Param("startTime") LocalDateTime startTime,
                                             @Param("endTime") LocalDateTime endTime);

    /**
     * 获取操作统计信息
     * 
     * @param inventoryId 库存ID
     * @param startTime 开始时间
     * @param endTime 结束时间
     * @return 操作统计信息
     */
    java.util.Map<String, Object> getOperationStatistics(@Param("inventoryId") Long inventoryId,
                                                        @Param("startTime") LocalDateTime startTime,
                                                        @Param("endTime") LocalDateTime endTime);

    /**
     * 获取操作人统计信息
     * 
     * @param startTime 开始时间
     * @param endTime 结束时间
     * @return 操作人统计信息
     */
    List<java.util.Map<String, Object>> getOperatorStatistics(@Param("startTime") LocalDateTime startTime,
                                                             @Param("endTime") LocalDateTime endTime);

    /**
     * 获取操作类型统计信息
     * 
     * @param startTime 开始时间
     * @param endTime 结束时间
     * @return 操作类型统计信息
     */
    List<java.util.Map<String, Object>> getOperationTypeStatistics(@Param("startTime") LocalDateTime startTime,
                                                                  @Param("endTime") LocalDateTime endTime);

    /**
     * 获取操作结果统计信息
     * 
     * @param startTime 开始时间
     * @param endTime 结束时间
     * @return 操作结果统计信息
     */
    java.util.Map<String, Object> getOperationResultStatistics(@Param("startTime") LocalDateTime startTime,
                                                              @Param("endTime") LocalDateTime endTime);

    /**
     * 获取错误操作统计信息
     * 
     * @param startTime 开始时间
     * @param endTime 结束时间
     * @return 错误操作统计信息
     */
    List<java.util.Map<String, Object>> getErrorOperationStatistics(@Param("startTime") LocalDateTime startTime,
                                                                   @Param("endTime") LocalDateTime endTime);

    /**
     * 获取慢操作统计信息
     * 
     * @param executionTime 执行时间阈值（毫秒）
     * @param startTime 开始时间
     * @param endTime 结束时间
     * @return 慢操作统计信息
     */
    List<java.util.Map<String, Object>> getSlowOperationStatistics(@Param("executionTime") Long executionTime,
                                                                  @Param("startTime") LocalDateTime startTime,
                                                                  @Param("endTime") LocalDateTime endTime);

    /**
     * 获取平均执行时间
     * 
     * @param operationType 操作类型
     * @param startTime 开始时间
     * @param endTime 结束时间
     * @return 平均执行时间
     */
    Double getAverageExecutionTime(@Param("operationType") Integer operationType,
                                 @Param("startTime") LocalDateTime startTime,
                                 @Param("endTime") LocalDateTime endTime);

    /**
     * 获取操作频率统计
     * 
     * @param startTime 开始时间
     * @param endTime 结束时间
     * @param groupBy 分组方式：hour、day、month
     * @return 操作频率统计
     */
    List<java.util.Map<String, Object>> getOperationFrequencyStatistics(@Param("startTime") LocalDateTime startTime,
                                                                        @Param("endTime") LocalDateTime endTime,
                                                                        @Param("groupBy") String groupBy);

    /**
     * 批量删除库存操作日志
     * 
     * @param logIds 日志ID列表
     * @return 删除数量
     */
    int batchDeleteByIds(@Param("logIds") List<Long> logIds);

    /**
     * 根据时间范围删除库存操作日志
     * 
     * @param endTime 结束时间
     * @return 删除数量
     */
    int deleteByTimeRange(@Param("endTime") LocalDateTime endTime);

    /**
     * 清理过期日志
     * 
     * @param retentionDays 保留天数
     * @return 删除数量
     */
    int cleanExpiredLogs(@Param("retentionDays") Integer retentionDays);
}