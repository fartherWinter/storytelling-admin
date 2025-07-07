package com.chennian.storytelling.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.chennian.storytelling.bean.model.InventoryLog;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

/**
 * 库存操作日志Mapper接口
 */
@Mapper
public interface InventoryLogMapper extends BaseMapper<InventoryLog> {

    /**
     * 根据产品ID和仓库ID查询库存操作日志
     * @param productId 产品ID
     * @param warehouseId 仓库ID
     * @param startTime 开始时间
     * @param endTime 结束时间
     * @return 日志列表
     */
    @Select("<script>" +
            "SELECT * FROM erp_inventory_log " +
            "WHERE 1=1 " +
            "<if test='productId != null'>" +
            "AND product_id = #{productId} " +
            "</if>" +
            "<if test='warehouseId != null'>" +
            "AND warehouse_id = #{warehouseId} " +
            "</if>" +
            "<if test='startTime != null'>" +
            "AND operation_time >= #{startTime} " +
            "</if>" +
            "<if test='endTime != null'>" +
            "AND operation_time <= #{endTime} " +
            "</if>" +
            "ORDER BY operation_time DESC" +
            "</script>")
    List<InventoryLog> selectByConditions(@Param("productId") Long productId,
                                         @Param("warehouseId") Long warehouseId,
                                         @Param("startTime") LocalDateTime startTime,
                                         @Param("endTime") LocalDateTime endTime);

    /**
     * 根据操作类型统计库存变化
     * @param operationType 操作类型
     * @param startTime 开始时间
     * @param endTime 结束时间
     * @param warehouseId 仓库ID（可选）
     * @return 统计结果
     */
    @Select("<script>" +
            "SELECT " +
            "operation_type, " +
            "COUNT(*) as operation_count, " +
            "SUM(ABS(change_quantity)) as total_quantity, " +
            "SUM(CASE WHEN change_quantity > 0 THEN change_quantity ELSE 0 END) as in_quantity, " +
            "SUM(CASE WHEN change_quantity < 0 THEN ABS(change_quantity) ELSE 0 END) as out_quantity " +
            "FROM erp_inventory_log " +
            "WHERE operation_time BETWEEN #{startTime} AND #{endTime} " +
            "<if test='operationType != null'>" +
            "AND operation_type = #{operationType} " +
            "</if>" +
            "<if test='warehouseId != null'>" +
            "AND warehouse_id = #{warehouseId} " +
            "</if>" +
            "GROUP BY operation_type " +
            "ORDER BY operation_type" +
            "</script>")
    List<Map<String, Object>> selectOperationStatistics(@Param("operationType") Integer operationType,
                                                       @Param("startTime") LocalDateTime startTime,
                                                       @Param("endTime") LocalDateTime endTime,
                                                       @Param("warehouseId") Long warehouseId);

    /**
     * 查询产品的库存变化趋势
     * @param productId 产品ID
     * @param warehouseId 仓库ID
     * @param startTime 开始时间
     * @param endTime 结束时间
     * @return 趋势数据
     */
    @Select("SELECT " +
            "DATE(operation_time) as operation_date, " +
            "SUM(CASE WHEN change_quantity > 0 THEN change_quantity ELSE 0 END) as daily_in, " +
            "SUM(CASE WHEN change_quantity < 0 THEN ABS(change_quantity) ELSE 0 END) as daily_out, " +
            "SUM(change_quantity) as daily_net_change " +
            "FROM erp_inventory_log " +
            "WHERE product_id = #{productId} " +
            "AND warehouse_id = #{warehouseId} " +
            "AND operation_time BETWEEN #{startTime} AND #{endTime} " +
            "GROUP BY DATE(operation_time) " +
            "ORDER BY operation_date")
    List<Map<String, Object>> selectInventoryTrend(@Param("productId") Long productId,
                                                  @Param("warehouseId") Long warehouseId,
                                                  @Param("startTime") LocalDateTime startTime,
                                                  @Param("endTime") LocalDateTime endTime);

    /**
     * 查询指定产品和仓库的最后一次库存操作
     * @param productId 产品ID
     * @param warehouseId 仓库ID
     * @return 最后一次操作记录
     */
    @Select("SELECT * FROM erp_inventory_log " +
            "WHERE product_id = #{productId} AND warehouse_id = #{warehouseId} " +
            "ORDER BY operation_time DESC LIMIT 1")
    InventoryLog selectLastOperation(@Param("productId") Long productId, @Param("warehouseId") Long warehouseId);

    /**
     * 根据关联单据号查询库存操作日志
     * @param documentNumber 单据号
     * @return 日志列表
     */
    @Select("SELECT * FROM erp_inventory_log WHERE document_number = #{documentNumber} ORDER BY operation_time DESC")
    List<InventoryLog> selectByDocumentNumber(@Param("documentNumber") String documentNumber);

    /**
     * 查询操作人的操作统计
     * @param operatorId 操作人ID
     * @param startTime 开始时间
     * @param endTime 结束时间
     * @return 统计结果
     */
    @Select("SELECT " +
            "operation_type, " +
            "COUNT(*) as operation_count, " +
            "SUM(ABS(change_quantity)) as total_quantity " +
            "FROM erp_inventory_log " +
            "WHERE operator_id = #{operatorId} " +
            "AND operation_time BETWEEN #{startTime} AND #{endTime} " +
            "GROUP BY operation_type " +
            "ORDER BY operation_type")
    List<Map<String, Object>> selectOperatorStatistics(@Param("operatorId") Long operatorId,
                                                      @Param("startTime") LocalDateTime startTime,
                                                      @Param("endTime") LocalDateTime endTime);

    /**
     * 计算指定产品和仓库的理论库存
     * @param productId 产品ID
     * @param warehouseId 仓库ID
     * @return 理论库存数量
     */
    @Select("SELECT COALESCE(SUM(change_quantity), 0) FROM erp_inventory_log " +
            "WHERE product_id = #{productId} AND warehouse_id = #{warehouseId}")
    Integer calculateTheoreticalStock(@Param("productId") Long productId, @Param("warehouseId") Long warehouseId);

    /**
     * 删除指定时间之前的日志记录
     * @param beforeTime 时间点
     * @return 删除数量
     */
    @Select("DELETE FROM erp_inventory_log WHERE operation_time < #{beforeTime}")
    int deleteLogsBefore(@Param("beforeTime") LocalDateTime beforeTime);
}