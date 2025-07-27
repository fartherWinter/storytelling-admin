package com.chennian.storytelling.inventory.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.chennian.storytelling.inventory.entity.InventoryRecord;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.time.LocalDateTime;
import java.util.List;

/**
 * 库存变动记录Mapper接口
 * 
 * @author chennian
 * @since 2024-01-01
 */
@Mapper
public interface InventoryRecordMapper extends BaseMapper<InventoryRecord> {

    /**
     * 分页查询库存变动记录列表
     * 
     * @param page 分页参数
     * @param inventoryId 库存ID
     * @param productId 商品ID
     * @param warehouseId 仓库ID
     * @param skuCode SKU编码
     * @param changeType 变动类型
     * @param changeReason 变动原因
     * @param businessNo 关联业务单号
     * @param businessType 关联业务类型
     * @param operatorId 操作人ID
     * @param startTime 开始时间
     * @param endTime 结束时间
     * @return 库存变动记录分页列表
     */
    IPage<InventoryRecord> selectInventoryRecordPage(Page<InventoryRecord> page,
                                                   @Param("inventoryId") Long inventoryId,
                                                   @Param("productId") Long productId,
                                                   @Param("warehouseId") Long warehouseId,
                                                   @Param("skuCode") String skuCode,
                                                   @Param("changeType") Integer changeType,
                                                   @Param("changeReason") Integer changeReason,
                                                   @Param("businessNo") String businessNo,
                                                   @Param("businessType") Integer businessType,
                                                   @Param("operatorId") Long operatorId,
                                                   @Param("startTime") LocalDateTime startTime,
                                                   @Param("endTime") LocalDateTime endTime);

    /**
     * 根据库存ID查询变动记录列表
     * 
     * @param inventoryId 库存ID
     * @return 变动记录列表
     */
    List<InventoryRecord> selectByInventoryId(@Param("inventoryId") Long inventoryId);

    /**
     * 根据商品ID查询变动记录列表
     * 
     * @param productId 商品ID
     * @return 变动记录列表
     */
    List<InventoryRecord> selectByProductId(@Param("productId") Long productId);

    /**
     * 根据仓库ID查询变动记录列表
     * 
     * @param warehouseId 仓库ID
     * @return 变动记录列表
     */
    List<InventoryRecord> selectByWarehouseId(@Param("warehouseId") Long warehouseId);

    /**
     * 根据业务单号查询变动记录列表
     * 
     * @param businessNo 业务单号
     * @return 变动记录列表
     */
    List<InventoryRecord> selectByBusinessNo(@Param("businessNo") String businessNo);

    /**
     * 根据变动类型查询变动记录列表
     * 
     * @param changeType 变动类型
     * @param startTime 开始时间
     * @param endTime 结束时间
     * @return 变动记录列表
     */
    List<InventoryRecord> selectByChangeType(@Param("changeType") Integer changeType,
                                           @Param("startTime") LocalDateTime startTime,
                                           @Param("endTime") LocalDateTime endTime);

    /**
     * 根据操作人查询变动记录列表
     * 
     * @param operatorId 操作人ID
     * @param startTime 开始时间
     * @param endTime 结束时间
     * @return 变动记录列表
     */
    List<InventoryRecord> selectByOperator(@Param("operatorId") Long operatorId,
                                         @Param("startTime") LocalDateTime startTime,
                                         @Param("endTime") LocalDateTime endTime);

    /**
     * 获取库存变动统计信息
     * 
     * @param inventoryId 库存ID
     * @param startTime 开始时间
     * @param endTime 结束时间
     * @return 变动统计信息
     */
    java.util.Map<String, Object> getInventoryChangeStatistics(@Param("inventoryId") Long inventoryId,
                                                              @Param("startTime") LocalDateTime startTime,
                                                              @Param("endTime") LocalDateTime endTime);

    /**
     * 获取商品库存变动统计信息
     * 
     * @param productId 商品ID
     * @param startTime 开始时间
     * @param endTime 结束时间
     * @return 变动统计信息
     */
    java.util.Map<String, Object> getProductInventoryChangeStatistics(@Param("productId") Long productId,
                                                                     @Param("startTime") LocalDateTime startTime,
                                                                     @Param("endTime") LocalDateTime endTime);

    /**
     * 获取仓库库存变动统计信息
     * 
     * @param warehouseId 仓库ID
     * @param startTime 开始时间
     * @param endTime 结束时间
     * @return 变动统计信息
     */
    java.util.Map<String, Object> getWarehouseInventoryChangeStatistics(@Param("warehouseId") Long warehouseId,
                                                                       @Param("startTime") LocalDateTime startTime,
                                                                       @Param("endTime") LocalDateTime endTime);

    /**
     * 获取入库统计信息
     * 
     * @param warehouseId 仓库ID
     * @param startTime 开始时间
     * @param endTime 结束时间
     * @return 入库统计信息
     */
    java.util.Map<String, Object> getInboundStatistics(@Param("warehouseId") Long warehouseId,
                                                      @Param("startTime") LocalDateTime startTime,
                                                      @Param("endTime") LocalDateTime endTime);

    /**
     * 获取出库统计信息
     * 
     * @param warehouseId 仓库ID
     * @param startTime 开始时间
     * @param endTime 结束时间
     * @return 出库统计信息
     */
    java.util.Map<String, Object> getOutboundStatistics(@Param("warehouseId") Long warehouseId,
                                                       @Param("startTime") LocalDateTime startTime,
                                                       @Param("endTime") LocalDateTime endTime);

    /**
     * 获取调拨统计信息
     * 
     * @param warehouseId 仓库ID
     * @param startTime 开始时间
     * @param endTime 结束时间
     * @return 调拨统计信息
     */
    java.util.Map<String, Object> getTransferStatistics(@Param("warehouseId") Long warehouseId,
                                                       @Param("startTime") LocalDateTime startTime,
                                                       @Param("endTime") LocalDateTime endTime);

    /**
     * 获取盘点统计信息
     * 
     * @param warehouseId 仓库ID
     * @param startTime 开始时间
     * @param endTime 结束时间
     * @return 盘点统计信息
     */
    java.util.Map<String, Object> getInventoryCheckStatistics(@Param("warehouseId") Long warehouseId,
                                                             @Param("startTime") LocalDateTime startTime,
                                                             @Param("endTime") LocalDateTime endTime);

    /**
     * 获取库存周转率
     * 
     * @param inventoryId 库存ID
     * @param startTime 开始时间
     * @param endTime 结束时间
     * @return 库存周转率
     */
    java.math.BigDecimal getInventoryTurnoverRate(@Param("inventoryId") Long inventoryId,
                                                 @Param("startTime") LocalDateTime startTime,
                                                 @Param("endTime") LocalDateTime endTime);

    /**
     * 获取商品库存周转率
     * 
     * @param productId 商品ID
     * @param startTime 开始时间
     * @param endTime 结束时间
     * @return 库存周转率
     */
    java.math.BigDecimal getProductInventoryTurnoverRate(@Param("productId") Long productId,
                                                        @Param("startTime") LocalDateTime startTime,
                                                        @Param("endTime") LocalDateTime endTime);

    /**
     * 批量删除库存变动记录
     * 
     * @param recordIds 记录ID列表
     * @return 删除数量
     */
    int batchDeleteByIds(@Param("recordIds") List<Long> recordIds);

    /**
     * 根据时间范围删除库存变动记录
     * 
     * @param endTime 结束时间
     * @return 删除数量
     */
    int deleteByTimeRange(@Param("endTime") LocalDateTime endTime);
}