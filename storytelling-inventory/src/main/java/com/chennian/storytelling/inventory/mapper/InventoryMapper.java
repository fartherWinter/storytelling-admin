package com.chennian.storytelling.inventory.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.chennian.storytelling.inventory.entity.Inventory;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 库存Mapper接口
 * 
 * @author chennian
 * @since 2024-01-01
 */
@Mapper
public interface InventoryMapper extends BaseMapper<Inventory> {

    /**
     * 根据商品ID和仓库ID查询库存
     * 
     * @param productId 商品ID
     * @param warehouseId 仓库ID
     * @return 库存信息
     */
    Inventory selectByProductIdAndWarehouseId(@Param("productId") Long productId, @Param("warehouseId") Long warehouseId);

    /**
     * 根据SKU编码查询库存
     * 
     * @param skuCode SKU编码
     * @return 库存信息
     */
    Inventory selectBySkuCode(@Param("skuCode") String skuCode);

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
    IPage<Inventory> selectInventoryPage(Page<Inventory> page,
                                       @Param("productId") Long productId,
                                       @Param("warehouseId") Long warehouseId,
                                       @Param("skuCode") String skuCode,
                                       @Param("productName") String productName,
                                       @Param("status") Integer status,
                                       @Param("isLowStock") Boolean isLowStock,
                                       @Param("isOutOfStock") Boolean isOutOfStock);

    /**
     * 根据商品ID查询库存列表
     * 
     * @param productId 商品ID
     * @return 库存列表
     */
    List<Inventory> selectByProductId(@Param("productId") Long productId);

    /**
     * 根据仓库ID查询库存列表
     * 
     * @param warehouseId 仓库ID
     * @return 库存列表
     */
    List<Inventory> selectByWarehouseId(@Param("warehouseId") Long warehouseId);

    /**
     * 获取低库存商品列表
     * 
     * @param warehouseId 仓库ID（可选）
     * @return 低库存商品列表
     */
    List<Inventory> selectLowStockList(@Param("warehouseId") Long warehouseId);

    /**
     * 获取缺货商品列表
     * 
     * @param warehouseId 仓库ID（可选）
     * @return 缺货商品列表
     */
    List<Inventory> selectOutOfStockList(@Param("warehouseId") Long warehouseId);

    /**
     * 获取库存预警商品列表
     * 
     * @param warehouseId 仓库ID（可选）
     * @return 库存预警商品列表
     */
    List<Inventory> selectWarningStockList(@Param("warehouseId") Long warehouseId);

    /**
     * 批量更新库存状态
     * 
     * @param inventoryIds 库存ID列表
     * @param status 状态
     * @param updateBy 更新者
     * @return 更新数量
     */
    int batchUpdateStatus(@Param("inventoryIds") List<Long> inventoryIds,
                         @Param("status") Integer status,
                         @Param("updateBy") String updateBy);

    /**
     * 锁定库存
     * 
     * @param inventoryId 库存ID
     * @param lockQuantity 锁定数量
     * @param version 版本号
     * @return 更新数量
     */
    int lockStock(@Param("inventoryId") Long inventoryId,
                  @Param("lockQuantity") Integer lockQuantity,
                  @Param("version") Integer version);

    /**
     * 解锁库存
     * 
     * @param inventoryId 库存ID
     * @param unlockQuantity 解锁数量
     * @param version 版本号
     * @return 更新数量
     */
    int unlockStock(@Param("inventoryId") Long inventoryId,
                    @Param("unlockQuantity") Integer unlockQuantity,
                    @Param("version") Integer version);

    /**
     * 扣减库存
     * 
     * @param inventoryId 库存ID
     * @param quantity 扣减数量
     * @param version 版本号
     * @return 更新数量
     */
    int deductStock(@Param("inventoryId") Long inventoryId,
                    @Param("quantity") Integer quantity,
                    @Param("version") Integer version);

    /**
     * 增加库存
     * 
     * @param inventoryId 库存ID
     * @param quantity 增加数量
     * @param version 版本号
     * @return 更新数量
     */
    int addStock(@Param("inventoryId") Long inventoryId,
                 @Param("quantity") Integer quantity,
                 @Param("version") Integer version);

    /**
     * 预占库存
     * 
     * @param inventoryId 库存ID
     * @param reserveQuantity 预占数量
     * @param version 版本号
     * @return 更新数量
     */
    int reserveStock(@Param("inventoryId") Long inventoryId,
                     @Param("reserveQuantity") Integer reserveQuantity,
                     @Param("version") Integer version);

    /**
     * 释放预占库存
     * 
     * @param inventoryId 库存ID
     * @param releaseQuantity 释放数量
     * @param version 版本号
     * @return 更新数量
     */
    int releaseReservedStock(@Param("inventoryId") Long inventoryId,
                            @Param("releaseQuantity") Integer releaseQuantity,
                            @Param("version") Integer version);

    /**
     * 检查SKU编码是否存在
     * 
     * @param skuCode SKU编码
     * @param inventoryId 库存ID（排除自己）
     * @return 是否存在
     */
    boolean existsBySkuCode(@Param("skuCode") String skuCode, @Param("inventoryId") Long inventoryId);

    /**
     * 检查商品和仓库组合是否存在
     * 
     * @param productId 商品ID
     * @param warehouseId 仓库ID
     * @param inventoryId 库存ID（排除自己）
     * @return 是否存在
     */
    boolean existsByProductIdAndWarehouseId(@Param("productId") Long productId,
                                           @Param("warehouseId") Long warehouseId,
                                           @Param("inventoryId") Long inventoryId);

    /**
     * 获取商品的总库存数量
     * 
     * @param productId 商品ID
     * @return 总库存数量
     */
    Integer getTotalStockByProductId(@Param("productId") Long productId);

    /**
     * 获取商品的可用库存数量
     * 
     * @param productId 商品ID
     * @return 可用库存数量
     */
    Integer getAvailableStockByProductId(@Param("productId") Long productId);

    /**
     * 获取仓库的库存商品数量
     * 
     * @param warehouseId 仓库ID
     * @return 库存商品数量
     */
    Integer getStockProductCountByWarehouseId(@Param("warehouseId") Long warehouseId);

    /**
     * 获取仓库的总库存数量
     * 
     * @param warehouseId 仓库ID
     * @return 总库存数量
     */
    Integer getTotalStockByWarehouseId(@Param("warehouseId") Long warehouseId);

    /**
     * 获取库存统计信息
     * 
     * @param warehouseId 仓库ID（可选）
     * @return 库存统计信息
     */
    java.util.Map<String, Object> getInventoryStatistics(@Param("warehouseId") Long warehouseId);
}