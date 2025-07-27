package com.chennian.storytelling.inventory.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.chennian.storytelling.inventory.entity.Inventory;

import java.util.List;

/**
 * 库存服务接口
 * 
 * @author chennian
 * @since 2024-01-01
 */
public interface InventoryService {

    /**
     * 根据库存ID查询库存
     * 
     * @param inventoryId 库存ID
     * @return 库存信息
     */
    Inventory getInventoryById(Long inventoryId);

    /**
     * 根据商品ID和仓库ID查询库存
     * 
     * @param productId 商品ID
     * @param warehouseId 仓库ID
     * @return 库存信息
     */
    Inventory getInventoryByProductIdAndWarehouseId(Long productId, Long warehouseId);

    /**
     * 根据SKU编码查询库存
     * 
     * @param skuCode SKU编码
     * @return 库存信息
     */
    Inventory getInventoryBySkuCode(String skuCode);

    /**
     * 分页查询库存列表
     * 
     * @param page 分页参数
     * @param productId 商品ID
     * @param warehouseId 仓库ID
     * @param skuCode SKU编码
     * @param productName 商品名称
     * @param status 库存状态
     * @param isLowStock 是否低库存
     * @param isOutOfStock 是否缺货
     * @return 库存分页列表
     */
    IPage<Inventory> getInventoryPage(Page<Inventory> page, Long productId, Long warehouseId,
                                    String skuCode, String productName, Integer status,
                                    Boolean isLowStock, Boolean isOutOfStock);

    /**
     * 根据商品ID查询库存列表
     * 
     * @param productId 商品ID
     * @return 库存列表
     */
    List<Inventory> getInventoryListByProductId(Long productId);

    /**
     * 根据仓库ID查询库存列表
     * 
     * @param warehouseId 仓库ID
     * @return 库存列表
     */
    List<Inventory> getInventoryListByWarehouseId(Long warehouseId);

    /**
     * 获取低库存商品列表
     * 
     * @param warehouseId 仓库ID（可选）
     * @return 低库存商品列表
     */
    List<Inventory> getLowStockList(Long warehouseId);

    /**
     * 获取缺货商品列表
     * 
     * @param warehouseId 仓库ID（可选）
     * @return 缺货商品列表
     */
    List<Inventory> getOutOfStockList(Long warehouseId);

    /**
     * 获取库存预警商品列表
     * 
     * @param warehouseId 仓库ID（可选）
     * @return 库存预警商品列表
     */
    List<Inventory> getWarningStockList(Long warehouseId);

    /**
     * 创建库存
     * 
     * @param inventory 库存信息
     * @return 是否成功
     */
    boolean createInventory(Inventory inventory);

    /**
     * 更新库存
     * 
     * @param inventory 库存信息
     * @return 是否成功
     */
    boolean updateInventory(Inventory inventory);

    /**
     * 删除库存
     * 
     * @param inventoryId 库存ID
     * @return 是否成功
     */
    boolean deleteInventory(Long inventoryId);

    /**
     * 批量删除库存
     * 
     * @param inventoryIds 库存ID列表
     * @return 是否成功
     */
    boolean batchDeleteInventory(List<Long> inventoryIds);

    /**
     * 启用库存
     * 
     * @param inventoryId 库存ID
     * @return 是否成功
     */
    boolean enableInventory(Long inventoryId);

    /**
     * 禁用库存
     * 
     * @param inventoryId 库存ID
     * @return 是否成功
     */
    boolean disableInventory(Long inventoryId);

    /**
     * 批量更新库存状态
     * 
     * @param inventoryIds 库存ID列表
     * @param status 状态
     * @return 是否成功
     */
    boolean batchUpdateInventoryStatus(List<Long> inventoryIds, Integer status);

    /**
     * 入库操作
     * 
     * @param inventoryId 库存ID
     * @param quantity 入库数量
     * @param businessNo 业务单号
     * @param businessType 业务类型
     * @param remark 备注
     * @return 是否成功
     */
    boolean inbound(Long inventoryId, Integer quantity, String businessNo, Integer businessType, String remark);

    /**
     * 出库操作
     * 
     * @param inventoryId 库存ID
     * @param quantity 出库数量
     * @param businessNo 业务单号
     * @param businessType 业务类型
     * @param remark 备注
     * @return 是否成功
     */
    boolean outbound(Long inventoryId, Integer quantity, String businessNo, Integer businessType, String remark);

    /**
     * 锁定库存
     * 
     * @param inventoryId 库存ID
     * @param lockQuantity 锁定数量
     * @param businessNo 业务单号
     * @param businessType 业务类型
     * @param remark 备注
     * @return 是否成功
     */
    boolean lockStock(Long inventoryId, Integer lockQuantity, String businessNo, Integer businessType, String remark);

    /**
     * 解锁库存
     * 
     * @param inventoryId 库存ID
     * @param unlockQuantity 解锁数量
     * @param businessNo 业务单号
     * @param businessType 业务类型
     * @param remark 备注
     * @return 是否成功
     */
    boolean unlockStock(Long inventoryId, Integer unlockQuantity, String businessNo, Integer businessType, String remark);

    /**
     * 预占库存
     * 
     * @param inventoryId 库存ID
     * @param reserveQuantity 预占数量
     * @param businessNo 业务单号
     * @param businessType 业务类型
     * @param remark 备注
     * @return 是否成功
     */
    boolean reserveStock(Long inventoryId, Integer reserveQuantity, String businessNo, Integer businessType, String remark);

    /**
     * 释放预占库存
     * 
     * @param inventoryId 库存ID
     * @param releaseQuantity 释放数量
     * @param businessNo 业务单号
     * @param businessType 业务类型
     * @param remark 备注
     * @return 是否成功
     */
    boolean releaseReservedStock(Long inventoryId, Integer releaseQuantity, String businessNo, Integer businessType, String remark);

    /**
     * 调拨库存
     * 
     * @param fromInventoryId 源库存ID
     * @param toInventoryId 目标库存ID
     * @param quantity 调拨数量
     * @param businessNo 业务单号
     * @param remark 备注
     * @return 是否成功
     */
    boolean transferStock(Long fromInventoryId, Long toInventoryId, Integer quantity, String businessNo, String remark);

    /**
     * 盘点库存
     * 
     * @param inventoryId 库存ID
     * @param actualQuantity 实际数量
     * @param businessNo 业务单号
     * @param remark 备注
     * @return 是否成功
     */
    boolean inventoryCheck(Long inventoryId, Integer actualQuantity, String businessNo, String remark);

    /**
     * 批量入库操作
     * 
     * @param inventoryList 库存列表（包含入库数量）
     * @param businessNo 业务单号
     * @param businessType 业务类型
     * @param remark 备注
     * @return 是否成功
     */
    boolean batchInbound(List<Inventory> inventoryList, String businessNo, Integer businessType, String remark);

    /**
     * 批量出库操作
     * 
     * @param inventoryList 库存列表（包含出库数量）
     * @param businessNo 业务单号
     * @param businessType 业务类型
     * @param remark 备注
     * @return 是否成功
     */
    boolean batchOutbound(List<Inventory> inventoryList, String businessNo, Integer businessType, String remark);

    /**
     * 检查SKU编码是否存在
     * 
     * @param skuCode SKU编码
     * @param inventoryId 库存ID（排除自己）
     * @return 是否存在
     */
    boolean existsBySkuCode(String skuCode, Long inventoryId);

    /**
     * 检查商品和仓库组合是否存在
     * 
     * @param productId 商品ID
     * @param warehouseId 仓库ID
     * @param inventoryId 库存ID（排除自己）
     * @return 是否存在
     */
    boolean existsByProductIdAndWarehouseId(Long productId, Long warehouseId, Long inventoryId);

    /**
     * 获取商品的总库存数量
     * 
     * @param productId 商品ID
     * @return 总库存数量
     */
    Integer getTotalStockByProductId(Long productId);

    /**
     * 获取商品的可用库存数量
     * 
     * @param productId 商品ID
     * @return 可用库存数量
     */
    Integer getAvailableStockByProductId(Long productId);

    /**
     * 获取库存统计信息
     * 
     * @param warehouseId 仓库ID（可选）
     * @return 库存统计信息
     */
    java.util.Map<String, Object> getInventoryStatistics(Long warehouseId);
}