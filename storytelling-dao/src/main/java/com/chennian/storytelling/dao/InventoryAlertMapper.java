package com.chennian.storytelling.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.chennian.storytelling.bean.model.InventoryAlert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

/**
 * 库存预警Mapper接口
 */
@Mapper
public interface InventoryAlertMapper extends BaseMapper<InventoryAlert> {

    /**
     * 根据产品ID和仓库ID查询未处理的预警
     * @param productId 产品ID
     * @param warehouseId 仓库ID
     * @return 预警列表
     */
    @Select("SELECT * FROM erp_inventory_alert WHERE product_id = #{productId} AND warehouse_id = #{warehouseId} AND status = 0")
    List<InventoryAlert> selectUnhandledAlerts(@Param("productId") Long productId, @Param("warehouseId") Long warehouseId);

    /**
     * 根据预警级别查询预警数量
     * @param alertLevel 预警级别
     * @param warehouseId 仓库ID（可选）
     * @return 预警数量
     */
    @Select("<script>" +
            "SELECT COUNT(*) FROM erp_inventory_alert " +
            "WHERE alert_level = #{alertLevel} AND status = 0 " +
            "<if test='warehouseId != null'>" +
            "AND warehouse_id = #{warehouseId}" +
            "</if>" +
            "</script>")
    Long countByAlertLevel(@Param("alertLevel") Integer alertLevel, @Param("warehouseId") Long warehouseId);

    /**
     * 根据预警类型查询预警数量
     * @param alertType 预警类型
     * @param warehouseId 仓库ID（可选）
     * @return 预警数量
     */
    @Select("<script>" +
            "SELECT COUNT(*) FROM erp_inventory_alert " +
            "WHERE alert_type = #{alertType} AND status = 0 " +
            "<if test='warehouseId != null'>" +
            "AND warehouse_id = #{warehouseId}" +
            "</if>" +
            "</script>")
    Long countByAlertType(@Param("alertType") Integer alertType, @Param("warehouseId") Long warehouseId);

    /**
     * 查询指定时间范围内的预警统计
     * @param startTime 开始时间
     * @param endTime 结束时间
     * @param warehouseId 仓库ID（可选）
     * @return 统计结果
     */
    @Select("<script>" +
            "SELECT " +
            "alert_type, " +
            "alert_level, " +
            "COUNT(*) as count " +
            "FROM erp_inventory_alert " +
            "WHERE create_time BETWEEN #{startTime} AND #{endTime} " +
            "<if test='warehouseId != null'>" +
            "AND warehouse_id = #{warehouseId}" +
            "</if>" +
            "GROUP BY alert_type, alert_level " +
            "ORDER BY alert_type, alert_level" +
            "</script>")
    List<Map<String, Object>> selectAlertStatistics(@Param("startTime") LocalDateTime startTime, 
                                                   @Param("endTime") LocalDateTime endTime, 
                                                   @Param("warehouseId") Long warehouseId);

    /**
     * 批量更新预警状态
     * @param alertIds 预警ID列表
     * @param status 状态
     * @param handledBy 处理人
     * @param handledTime 处理时间
     * @param handledRemark 处理备注
     * @return 更新数量
     */
    @Select("<script>" +
            "UPDATE erp_inventory_alert SET " +
            "status = #{status}, " +
            "handled_by = #{handledBy}, " +
            "handled_time = #{handledTime}, " +
            "handled_remark = #{handledRemark}, " +
            "update_time = NOW() " +
            "WHERE id IN " +
            "<foreach collection='alertIds' item='id' open='(' separator=',' close=')'>" +
            "#{id}" +
            "</foreach>" +
            "</script>")
    int batchUpdateStatus(@Param("alertIds") List<Long> alertIds, 
                         @Param("status") Integer status, 
                         @Param("handledBy") Long handledBy, 
                         @Param("handledTime") LocalDateTime handledTime, 
                         @Param("handledRemark") String handledRemark);

    /**
     * 删除过期的已处理预警
     * @param expiredTime 过期时间
     * @return 删除数量
     */
    @Select("DELETE FROM erp_inventory_alert WHERE status IN (1, 2) AND handled_time < #{expiredTime}")
    int deleteExpiredAlerts(@Param("expiredTime") LocalDateTime expiredTime);
}