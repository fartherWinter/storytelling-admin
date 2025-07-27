package com.chennian.storytelling.inventory.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.chennian.storytelling.inventory.entity.Warehouse;

import java.util.List;

/**
 * 仓库服务接口
 * 
 * @author chennian
 * @since 2024-01-01
 */
public interface WarehouseService {

    /**
     * 根据仓库ID查询仓库
     * 
     * @param warehouseId 仓库ID
     * @return 仓库信息
     */
    Warehouse getWarehouseById(Long warehouseId);

    /**
     * 根据仓库编码查询仓库
     * 
     * @param warehouseCode 仓库编码
     * @return 仓库信息
     */
    Warehouse getWarehouseByCode(String warehouseCode);

    /**
     * 分页查询仓库列表
     * 
     * @param page 分页参数
     * @param warehouseCode 仓库编码
     * @param warehouseName 仓库名称
     * @param warehouseType 仓库类型
     * @param region 所属区域
     * @param status 状态
     * @return 仓库分页列表
     */
    IPage<Warehouse> getWarehousePage(Page<Warehouse> page, String warehouseCode, String warehouseName,
                                    Integer warehouseType, String region, Integer status);

    /**
     * 获取所有可用仓库
     * 
     * @return 可用仓库列表
     */
    List<Warehouse> getAllAvailableWarehouses();

    /**
     * 根据仓库类型查询仓库列表
     * 
     * @param warehouseType 仓库类型
     * @return 仓库列表
     */
    List<Warehouse> getWarehouseListByType(Integer warehouseType);

    /**
     * 根据所属区域查询仓库列表
     * 
     * @param region 所属区域
     * @return 仓库列表
     */
    List<Warehouse> getWarehouseListByRegion(String region);

    /**
     * 获取默认仓库
     * 
     * @return 默认仓库
     */
    Warehouse getDefaultWarehouse();

    /**
     * 获取支持自动分配的仓库列表
     * 
     * @return 支持自动分配的仓库列表
     */
    List<Warehouse> getAutoAllocateWarehouses();

    /**
     * 根据父仓库ID查询子仓库列表
     * 
     * @param parentWarehouseId 父仓库ID
     * @return 子仓库列表
     */
    List<Warehouse> getSubWarehouses(Long parentWarehouseId);

    /**
     * 创建仓库
     * 
     * @param warehouse 仓库信息
     * @return 是否成功
     */
    boolean createWarehouse(Warehouse warehouse);

    /**
     * 更新仓库
     * 
     * @param warehouse 仓库信息
     * @return 是否成功
     */
    boolean updateWarehouse(Warehouse warehouse);

    /**
     * 删除仓库
     * 
     * @param warehouseId 仓库ID
     * @return 是否成功
     */
    boolean deleteWarehouse(Long warehouseId);

    /**
     * 批量删除仓库
     * 
     * @param warehouseIds 仓库ID列表
     * @return 是否成功
     */
    boolean batchDeleteWarehouse(List<Long> warehouseIds);

    /**
     * 启用仓库
     * 
     * @param warehouseId 仓库ID
     * @return 是否成功
     */
    boolean enableWarehouse(Long warehouseId);

    /**
     * 禁用仓库
     * 
     * @param warehouseId 仓库ID
     * @return 是否成功
     */
    boolean disableWarehouse(Long warehouseId);

    /**
     * 批量更新仓库状态
     * 
     * @param warehouseIds 仓库ID列表
     * @param status 状态
     * @return 是否成功
     */
    boolean batchUpdateWarehouseStatus(List<Long> warehouseIds, Integer status);

    /**
     * 设置默认仓库
     * 
     * @param warehouseId 仓库ID
     * @return 是否成功
     */
    boolean setDefaultWarehouse(Long warehouseId);

    /**
     * 更新仓库排序
     * 
     * @param warehouseId 仓库ID
     * @param sortOrder 排序值
     * @return 是否成功
     */
    boolean updateWarehouseSortOrder(Long warehouseId, Integer sortOrder);

    /**
     * 移动仓库（更改父仓库）
     * 
     * @param warehouseId 仓库ID
     * @param newParentWarehouseId 新父仓库ID
     * @return 是否成功
     */
    boolean moveWarehouse(Long warehouseId, Long newParentWarehouseId);

    /**
     * 检查仓库编码是否存在
     * 
     * @param warehouseCode 仓库编码
     * @param warehouseId 仓库ID（排除自己）
     * @return 是否存在
     */
    boolean existsByWarehouseCode(String warehouseCode, Long warehouseId);

    /**
     * 检查仓库名称是否存在
     * 
     * @param warehouseName 仓库名称
     * @param warehouseId 仓库ID（排除自己）
     * @return 是否存在
     */
    boolean existsByWarehouseName(String warehouseName, Long warehouseId);

    /**
     * 检查仓库是否有库存
     * 
     * @param warehouseId 仓库ID
     * @return 是否有库存
     */
    boolean hasInventory(Long warehouseId);

    /**
     * 检查是否有子仓库
     * 
     * @param warehouseId 仓库ID
     * @return 是否有子仓库
     */
    boolean hasSubWarehouses(Long warehouseId);

    /**
     * 获取仓库下的库存商品数量
     * 
     * @param warehouseId 仓库ID
     * @return 库存商品数量
     */
    Integer getInventoryProductCount(Long warehouseId);

    /**
     * 获取仓库下的总库存数量
     * 
     * @param warehouseId 仓库ID
     * @return 总库存数量
     */
    Integer getTotalInventoryCount(Long warehouseId);

    /**
     * 获取仓库的容量使用率
     * 
     * @param warehouseId 仓库ID
     * @return 容量使用率
     */
    java.math.BigDecimal getCapacityUsageRate(Long warehouseId);

    /**
     * 获取仓库层级路径
     * 
     * @param warehouseId 仓库ID
     * @return 层级路径
     */
    String getWarehousePath(Long warehouseId);

    /**
     * 构建仓库树
     * 
     * @param parentWarehouseId 父仓库ID（null表示根节点）
     * @return 仓库树
     */
    List<Warehouse> buildWarehouseTree(Long parentWarehouseId);

    /**
     * 生成仓库路径
     * 
     * @param warehouseId 仓库ID
     * @return 仓库路径
     */
    String generateWarehousePath(Long warehouseId);

    /**
     * 更新子仓库路径
     * 
     * @param warehouseId 仓库ID
     * @return 是否成功
     */
    boolean updateSubWarehousePaths(Long warehouseId);

    /**
     * 获取仓库统计信息
     * 
     * @return 仓库统计信息
     */
    java.util.Map<String, Object> getWarehouseStatistics();

    /**
     * 自动分配仓库
     * 
     * @param productId 商品ID
     * @param quantity 数量
     * @param region 区域（可选）
     * @return 分配的仓库
     */
    Warehouse autoAllocateWarehouse(Long productId, Integer quantity, String region);

    /**
     * 获取仓库库存容量信息
     * 
     * @param warehouseId 仓库ID
     * @return 容量信息
     */
    java.util.Map<String, Object> getWarehouseCapacityInfo(Long warehouseId);

    /**
     * 检查仓库容量是否充足
     * 
     * @param warehouseId 仓库ID
     * @param additionalQuantity 额外数量
     * @return 是否充足
     */
    boolean checkWarehouseCapacity(Long warehouseId, Integer additionalQuantity);
}