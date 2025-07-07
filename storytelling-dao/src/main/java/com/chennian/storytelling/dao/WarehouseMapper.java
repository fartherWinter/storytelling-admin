package com.chennian.storytelling.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.chennian.storytelling.bean.model.Warehouse;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;

import java.util.List;
import java.util.Map;

/**
 * 仓库Mapper接口
 */
@Mapper
public interface WarehouseMapper extends BaseMapper<Warehouse> {

    /**
     * 根据仓库编码查询仓库
     * @param warehouseCode 仓库编码
     * @return 仓库信息
     */
    @Select("SELECT * FROM erp_warehouse WHERE warehouse_code = #{warehouseCode}")
    Warehouse selectByWarehouseCode(@Param("warehouseCode") String warehouseCode);

    /**
     * 根据仓库名称查询仓库
     * @param warehouseName 仓库名称
     * @return 仓库信息
     */
    @Select("SELECT * FROM erp_warehouse WHERE warehouse_name = #{warehouseName}")
    Warehouse selectByWarehouseName(@Param("warehouseName") String warehouseName);

    /**
     * 查询启用状态的仓库列表
     * @return 仓库列表
     */
    @Select("SELECT * FROM erp_warehouse WHERE status = 1 ORDER BY is_default DESC, warehouse_code ASC")
    List<Warehouse> selectActiveWarehouses();

    /**
     * 根据仓库类型查询仓库列表
     * @param warehouseType 仓库类型
     * @return 仓库列表
     */
    @Select("SELECT * FROM erp_warehouse WHERE warehouse_type = #{warehouseType} AND status = 1 ORDER BY warehouse_code ASC")
    List<Warehouse> selectByWarehouseType(@Param("warehouseType") Integer warehouseType);

    /**
     * 查询默认仓库
     * @return 默认仓库
     */
    @Select("SELECT * FROM erp_warehouse WHERE is_default = 1 AND status = 1 LIMIT 1")
    Warehouse selectDefaultWarehouse();

    /**
     * 设置默认仓库（先清除所有默认标记，再设置指定仓库为默认）
     * @param warehouseId 仓库ID
     * @return 更新结果
     */
    @Update("UPDATE erp_warehouse SET is_default = CASE WHEN id = #{warehouseId} THEN 1 ELSE 0 END")
    int setDefaultWarehouse(@Param("warehouseId") Long warehouseId);

    /**
     * 批量更新仓库状态
     * @param warehouseIds 仓库ID列表
     * @param status 状态
     * @return 更新数量
     */
    @Update("<script>" +
            "UPDATE erp_warehouse SET status = #{status}, update_time = NOW() " +
            "WHERE id IN " +
            "<foreach collection='warehouseIds' item='id' open='(' separator=',' close=')'>" +
            "#{id}" +
            "</foreach>" +
            "</script>")
    int batchUpdateStatus(@Param("warehouseIds") List<Long> warehouseIds, @Param("status") Integer status);

    /**
     * 查询仓库统计信息
     * @return 统计结果
     */
    @Select("SELECT " +
            "warehouse_type, " +
            "COUNT(*) as warehouse_count, " +
            "SUM(CASE WHEN status = 1 THEN 1 ELSE 0 END) as active_count, " +
            "SUM(CASE WHEN status = 0 THEN 1 ELSE 0 END) as inactive_count, " +
            "SUM(area) as total_area, " +
            "SUM(capacity) as total_capacity " +
            "FROM erp_warehouse " +
            "GROUP BY warehouse_type " +
            "ORDER BY warehouse_type")
    List<Map<String, Object>> selectWarehouseStatistics();

    /**
     * 根据关键字搜索仓库（仓库编码、名称、地址）
     * @param keyword 关键字
     * @return 仓库列表
     */
    @Select("<script>" +
            "SELECT * FROM erp_warehouse " +
            "WHERE status = 1 " +
            "<if test='keyword != null and keyword != \"\"'>" +
            "AND (warehouse_code LIKE CONCAT('%', #{keyword}, '%') " +
            "OR warehouse_name LIKE CONCAT('%', #{keyword}, '%') " +
            "OR address LIKE CONCAT('%', #{keyword}, '%'))" +
            "</if>" +
            "ORDER BY is_default DESC, warehouse_code ASC" +
            "</script>")
    List<Warehouse> searchWarehouses(@Param("keyword") String keyword);

    /**
     * 检查仓库编码是否存在
     * @param warehouseCode 仓库编码
     * @param excludeId 排除的仓库ID（用于更新时检查）
     * @return 存在数量
     */
    @Select("<script>" +
            "SELECT COUNT(*) FROM erp_warehouse " +
            "WHERE warehouse_code = #{warehouseCode} " +
            "<if test='excludeId != null'>" +
            "AND id != #{excludeId}" +
            "</if>" +
            "</script>")
    int checkWarehouseCodeExists(@Param("warehouseCode") String warehouseCode, @Param("excludeId") Long excludeId);

    /**
     * 检查仓库名称是否存在
     * @param warehouseName 仓库名称
     * @param excludeId 排除的仓库ID（用于更新时检查）
     * @return 存在数量
     */
    @Select("<script>" +
            "SELECT COUNT(*) FROM erp_warehouse " +
            "WHERE warehouse_name = #{warehouseName} " +
            "<if test='excludeId != null'>" +
            "AND id != #{excludeId}" +
            "</if>" +
            "</script>")
    int checkWarehouseNameExists(@Param("warehouseName") String warehouseName, @Param("excludeId") Long excludeId);

    /**
     * 查询仓库的库存概览
     * @param warehouseId 仓库ID
     * @return 库存概览
     */
    @Select("SELECT " +
            "w.id as warehouse_id, " +
            "w.warehouse_name, " +
            "w.warehouse_code, " +
            "COUNT(i.id) as product_count, " +
            "SUM(i.quantity) as total_quantity, " +
            "SUM(CASE WHEN i.quantity <= p.min_stock THEN 1 ELSE 0 END) as low_stock_count, " +
            "SUM(CASE WHEN i.quantity >= p.max_stock THEN 1 ELSE 0 END) as high_stock_count " +
            "FROM erp_warehouse w " +
            "LEFT JOIN erp_inventory i ON w.id = i.warehouse_id " +
            "LEFT JOIN erp_product p ON i.product_id = p.id " +
            "WHERE w.id = #{warehouseId} " +
            "GROUP BY w.id, w.warehouse_name, w.warehouse_code")
    Map<String, Object> selectWarehouseInventoryOverview(@Param("warehouseId") Long warehouseId);
}