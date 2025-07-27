package com.chennian.storytelling.inventory.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.chennian.storytelling.inventory.entity.Warehouse;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 仓库Mapper接口
 * 
 * @author chennian
 * @since 2024-01-01
 */
@Mapper
public interface WarehouseMapper extends BaseMapper<Warehouse> {

    /**
     * 根据仓库编码查询仓库
     * 
     * @param warehouseCode 仓库编码
     * @return 仓库信息
     */
    Warehouse selectByWarehouseCode(@Param("warehouseCode") String warehouseCode);

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
    IPage<Warehouse> selectWarehousePage(Page<Warehouse> page,
                                        @Param("warehouseCode") String warehouseCode,
                                        @Param("warehouseName") String warehouseName,
                                        @Param("warehouseType") Integer warehouseType,
                                        @Param("region") String region,
                                        @Param("status") Integer status);

    /**
     * 获取所有可用仓库
     * 
     * @return 可用仓库列表
     */
    List<Warehouse> selectAllAvailable();

    /**
     * 根据仓库类型查询仓库列表
     * 
     * @param warehouseType 仓库类型
     * @return 仓库列表
     */
    List<Warehouse> selectByWarehouseType(@Param("warehouseType") Integer warehouseType);

    /**
     * 根据所属区域查询仓库列表
     * 
     * @param region 所属区域
     * @return 仓库列表
     */
    List<Warehouse> selectByRegion(@Param("region") String region);

    /**
     * 获取默认仓库
     * 
     * @return 默认仓库
     */
    Warehouse selectDefaultWarehouse();

    /**
     * 获取支持自动分配的仓库列表
     * 
     * @return 支持自动分配的仓库列表
     */
    List<Warehouse> selectAutoAllocateWarehouses();

    /**
     * 批量更新仓库状态
     * 
     * @param warehouseIds 仓库ID列表
     * @param status 状态
     * @param updateBy 更新者
     * @return 更新数量
     */
    int batchUpdateStatus(@Param("warehouseIds") List<Long> warehouseIds,
                         @Param("status") Integer status,
                         @Param("updateBy") String updateBy);

    /**
     * 设置默认仓库
     * 
     * @param warehouseId 仓库ID
     * @param updateBy 更新者
     * @return 更新数量
     */
    int setDefaultWarehouse(@Param("warehouseId") Long warehouseId,
                           @Param("updateBy") String updateBy);

    /**
     * 清除默认仓库
     * 
     * @param updateBy 更新者
     * @return 更新数量
     */
    int clearDefaultWarehouse(@Param("updateBy") String updateBy);

    /**
     * 更新仓库排序
     * 
     * @param warehouseId 仓库ID
     * @param sortOrder 排序值
     * @param updateBy 更新者
     * @return 更新数量
     */
    int updateSortOrder(@Param("warehouseId") Long warehouseId,
                       @Param("sortOrder") Integer sortOrder,
                       @Param("updateBy") String updateBy);

    /**
     * 检查仓库编码是否存在
     * 
     * @param warehouseCode 仓库编码
     * @param warehouseId 仓库ID（排除自己）
     * @return 是否存在
     */
    boolean existsByWarehouseCode(@Param("warehouseCode") String warehouseCode,
                                 @Param("warehouseId") Long warehouseId);

    /**
     * 检查仓库名称是否存在
     * 
     * @param warehouseName 仓库名称
     * @param warehouseId 仓库ID（排除自己）
     * @return 是否存在
     */
    boolean existsByWarehouseName(@Param("warehouseName") String warehouseName,
                                 @Param("warehouseId") Long warehouseId);

    /**
     * 检查仓库是否有库存
     * 
     * @param warehouseId 仓库ID
     * @return 是否有库存
     */
    boolean hasInventory(@Param("warehouseId") Long warehouseId);

    /**
     * 获取仓库下的库存商品数量
     * 
     * @param warehouseId 仓库ID
     * @return 库存商品数量
     */
    Integer getInventoryProductCount(@Param("warehouseId") Long warehouseId);

    /**
     * 获取仓库下的总库存数量
     * 
     * @param warehouseId 仓库ID
     * @return 总库存数量
     */
    Integer getTotalInventoryCount(@Param("warehouseId") Long warehouseId);

    /**
     * 获取仓库的容量使用率
     * 
     * @param warehouseId 仓库ID
     * @return 容量使用率
     */
    java.math.BigDecimal getCapacityUsageRate(@Param("warehouseId") Long warehouseId);

    /**
     * 获取最大排序值
     * 
     * @return 最大排序值
     */
    Integer getMaxSortOrder();

    /**
     * 获取仓库统计信息
     * 
     * @return 仓库统计信息
     */
    java.util.Map<String, Object> getWarehouseStatistics();

    /**
     * 根据父仓库ID查询子仓库列表
     * 
     * @param parentWarehouseId 父仓库ID
     * @return 子仓库列表
     */
    List<Warehouse> selectByParentWarehouseId(@Param("parentWarehouseId") Long parentWarehouseId);

    /**
     * 检查是否有子仓库
     * 
     * @param warehouseId 仓库ID
     * @return 是否有子仓库
     */
    boolean hasSubWarehouses(@Param("warehouseId") Long warehouseId);

    /**
     * 获取仓库层级路径
     * 
     * @param warehouseId 仓库ID
     * @return 层级路径
     */
    String getWarehousePath(@Param("warehouseId") Long warehouseId);

    /**
     * 更新仓库层级路径
     * 
     * @param warehouseId 仓库ID
     * @param warehousePath 层级路径
     * @param updateBy 更新者
     * @return 更新数量
     */
    int updateWarehousePath(@Param("warehouseId") Long warehouseId,
                           @Param("warehousePath") String warehousePath,
                           @Param("updateBy") String updateBy);
}