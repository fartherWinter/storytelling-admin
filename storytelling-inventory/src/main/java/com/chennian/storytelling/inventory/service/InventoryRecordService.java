package com.chennian.storytelling.inventory.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.chennian.storytelling.inventory.entity.InventoryRecord;

import java.time.LocalDateTime;
import java.util.List;

/**
 * 库存变动记录服务接口
 * 
 * @author chennian
 * @since 2024-01-01
 */
public interface InventoryRecordService {

    /**
     * 根据记录ID查询库存变动记录
     * 
     * @param recordId 记录ID
     * @return 库存变动记录
     */
    InventoryRecord getInventoryRecordById(Long recordId);

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
    IPage<InventoryRecord> getInventoryRecordPage(Page<InventoryRecord> page, Long inventoryId, Long productId,
                                                Long warehouseId, String skuCode, Integer changeType,
                                                Integer changeReason, String businessNo, Integer businessType,
                                                Long operatorId, LocalDateTime startTime, LocalDateTime endTime);

    /**
     * 根据库存ID查询变动记录列表
     * 
     * @param inventoryId 库存ID
     * @return 变动记录列表
     */
    List<InventoryRecord> getInventoryRecordListByInventoryId(Long inventoryId);

    /**
     * 根据商品ID查询变动记录列表
     * 
     * @param productId 商品ID
     * @return 变动记录列表
     */
    List<InventoryRecord> getInventoryRecordListByProductId(Long productId);

    /**
     * 根据仓库ID查询变动记录列表
     * 
     * @param warehouseId 仓库ID
     * @return 变动记录列表
     */
    List<InventoryRecord> getInventoryRecordListByWarehouseId(Long warehouseId);

    /**
     * 根据业务单号查询变动记录列表
     * 
     * @param businessNo 业务单号
     * @return 变动记录列表
     */
    List<InventoryRecord> getInventoryRecordListByBusinessNo(String businessNo);

    /**
     * 根据变动类型查询变动记录列表
     * 
     * @param changeType 变动类型
     * @param startTime 开始时间
     * @param endTime 结束时间
     * @return 变动记录列表
     */
    List<InventoryRecord> getInventoryRecordListByChangeType(Integer changeType, LocalDateTime startTime, LocalDateTime endTime);

    /**
     * 根据操作人查询变动记录列表
     * 
     * @param operatorId 操作人ID
     * @param startTime 开始时间
     * @param endTime 结束时间
     * @return 变动记录列表
     */
    List<InventoryRecord> getInventoryRecordListByOperator(Long operatorId, LocalDateTime startTime, LocalDateTime endTime);

    /**
     * 创建库存变动记录
     * 
     * @param inventoryRecord 库存变动记录
     * @return 是否成功
     */
    boolean createInventoryRecord(InventoryRecord inventoryRecord);

    /**
     * 批量创建库存变动记录
     * 
     * @param inventoryRecordList 库存变动记录列表
     * @return 是否成功
     */
    boolean batchCreateInventoryRecord(List<InventoryRecord> inventoryRecordList);

    /**
     * 删除库存变动记录
     * 
     * @param recordId 记录ID
     * @return 是否成功
     */
    boolean deleteInventoryRecord(Long recordId);

    /**
     * 批量删除库存变动记录
     * 
     * @param recordIds 记录ID列表
     * @return 是否成功
     */
    boolean batchDeleteInventoryRecord(List<Long> recordIds);

    /**
     * 根据时间范围删除库存变动记录
     * 
     * @param endTime 结束时间
     * @return 是否成功
     */
    boolean deleteInventoryRecordByTimeRange(LocalDateTime endTime);

    /**
     * 获取库存变动统计信息
     * 
     * @param inventoryId 库存ID
     * @param startTime 开始时间
     * @param endTime 结束时间
     * @return 变动统计信息
     */
    java.util.Map<String, Object> getInventoryChangeStatistics(Long inventoryId, LocalDateTime startTime, LocalDateTime endTime);

    /**
     * 获取商品库存变动统计信息
     * 
     * @param productId 商品ID
     * @param startTime 开始时间
     * @param endTime 结束时间
     * @return 变动统计信息
     */
    java.util.Map<String, Object> getProductInventoryChangeStatistics(Long productId, LocalDateTime startTime, LocalDateTime endTime);

    /**
     * 获取仓库库存变动统计信息
     * 
     * @param warehouseId 仓库ID
     * @param startTime 开始时间
     * @param endTime 结束时间
     * @return 变动统计信息
     */
    java.util.Map<String, Object> getWarehouseInventoryChangeStatistics(Long warehouseId, LocalDateTime startTime, LocalDateTime endTime);

    /**
     * 获取入库统计信息
     * 
     * @param warehouseId 仓库ID
     * @param startTime 开始时间
     * @param endTime 结束时间
     * @return 入库统计信息
     */
    java.util.Map<String, Object> getInboundStatistics(Long warehouseId, LocalDateTime startTime, LocalDateTime endTime);

    /**
     * 获取出库统计信息
     * 
     * @param warehouseId 仓库ID
     * @param startTime 开始时间
     * @param endTime 结束时间
     * @return 出库统计信息
     */
    java.util.Map<String, Object> getOutboundStatistics(Long warehouseId, LocalDateTime startTime, LocalDateTime endTime);

    /**
     * 获取调拨统计信息
     * 
     * @param warehouseId 仓库ID
     * @param startTime 开始时间
     * @param endTime 结束时间
     * @return 调拨统计信息
     */
    java.util.Map<String, Object> getTransferStatistics(Long warehouseId, LocalDateTime startTime, LocalDateTime endTime);

    /**
     * 获取盘点统计信息
     * 
     * @param warehouseId 仓库ID
     * @param startTime 开始时间
     * @param endTime 结束时间
     * @return 盘点统计信息
     */
    java.util.Map<String, Object> getInventoryCheckStatistics(Long warehouseId, LocalDateTime startTime, LocalDateTime endTime);

    /**
     * 获取库存周转率
     * 
     * @param inventoryId 库存ID
     * @param startTime 开始时间
     * @param endTime 结束时间
     * @return 库存周转率
     */
    java.math.BigDecimal getInventoryTurnoverRate(Long inventoryId, LocalDateTime startTime, LocalDateTime endTime);

    /**
     * 获取商品库存周转率
     * 
     * @param productId 商品ID
     * @param startTime 开始时间
     * @param endTime 结束时间
     * @return 库存周转率
     */
    java.math.BigDecimal getProductInventoryTurnoverRate(Long productId, LocalDateTime startTime, LocalDateTime endTime);

    /**
     * 导出库存变动记录
     * 
     * @param inventoryId 库存ID
     * @param productId 商品ID
     * @param warehouseId 仓库ID
     * @param changeType 变动类型
     * @param startTime 开始时间
     * @param endTime 结束时间
     * @return 导出文件路径
     */
    String exportInventoryRecord(Long inventoryId, Long productId, Long warehouseId, Integer changeType,
                               LocalDateTime startTime, LocalDateTime endTime);

    /**
     * 生成库存变动报表
     * 
     * @param warehouseId 仓库ID
     * @param startTime 开始时间
     * @param endTime 结束时间
     * @param reportType 报表类型：1-日报，2-周报，3-月报
     * @return 报表数据
     */
    java.util.Map<String, Object> generateInventoryChangeReport(Long warehouseId, LocalDateTime startTime,
                                                               LocalDateTime endTime, Integer reportType);
}