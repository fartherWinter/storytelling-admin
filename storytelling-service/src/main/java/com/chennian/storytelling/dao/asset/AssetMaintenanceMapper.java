package com.chennian.storytelling.dao.asset;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.chennian.storytelling.bean.model.asset.AssetMaintenance;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * 资产维护记录Mapper接口
 * 
 * @author storytelling
 * @date 2024-01-01
 */
@Mapper
public interface AssetMaintenanceMapper extends BaseMapper<AssetMaintenance> {

    /**
     * 根据资产ID查询维护记录
     * 
     * @param assetId 资产ID
     * @return 维护记录列表
     */
    List<AssetMaintenance> selectByAssetId(@Param("assetId") Long assetId);

    /**
     * 根据维护类型查询维护记录
     * 
     * @param maintenanceType 维护类型
     * @return 维护记录列表
     */
    List<AssetMaintenance> selectByMaintenanceType(@Param("maintenanceType") Integer maintenanceType);

    /**
     * 根据维护日期范围查询维护记录
     * 
     * @param startDate 开始日期
     * @param endDate 结束日期
     * @return 维护记录列表
     */
    List<AssetMaintenance> selectByMaintenanceDateRange(@Param("startDate") Date startDate, @Param("endDate") Date endDate);

    /**
     * 根据维护状态查询维护记录
     * 
     * @param status 维护状态
     * @return 维护记录列表
     */
    List<AssetMaintenance> selectByStatus(@Param("status") Integer status);

    /**
     * 查询即将到期的维护任务
     * 
     * @param days 提前几天
     * @return 维护记录列表
     */
    List<AssetMaintenance> selectUpcomingMaintenance(@Param("days") Integer days);

    /**
     * 查询维护统计信息
     * 
     * @param startDate 开始日期
     * @param endDate 结束日期
     * @return 统计信息
     */
    List<Map<String, Object>> selectMaintenanceStatistics(@Param("startDate") Date startDate, @Param("endDate") Date endDate);

    /**
     * 查询维护费用统计
     * 
     * @param startDate 开始日期
     * @param endDate 结束日期
     * @return 费用统计
     */
    List<Map<String, Object>> selectMaintenanceCostStatistics(@Param("startDate") Date startDate, @Param("endDate") Date endDate);

    /**
     * 根据维护人员查询维护记录
     * 
     * @param maintenancePersonnel 维护人员
     * @return 维护记录列表
     */
    List<AssetMaintenance> selectByMaintenancePersonnel(@Param("maintenancePersonnel") String maintenancePersonnel);
}