package com.chennian.storytelling.dao.asset;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.chennian.storytelling.bean.model.asset.AssetInventory;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * 资产盘点记录Mapper接口
 * 
 * @author storytelling
 * @date 2024-01-01
 */
@Mapper
public interface AssetInventoryMapper extends BaseMapper<AssetInventory> {

    /**
     * 根据盘点批次号查询盘点记录
     * 
     * @param inventoryBatchNo 盘点批次号
     * @return 盘点记录列表
     */
    List<AssetInventory> selectByInventoryBatchNo(@Param("inventoryBatchNo") String inventoryBatchNo);

    /**
     * 根据资产ID查询盘点记录
     * 
     * @param assetId 资产ID
     * @return 盘点记录列表
     */
    List<AssetInventory> selectByAssetId(@Param("assetId") Long assetId);

    /**
     * 根据盘点日期范围查询盘点记录
     * 
     * @param startDate 开始日期
     * @param endDate 结束日期
     * @return 盘点记录列表
     */
    List<AssetInventory> selectByInventoryDateRange(@Param("startDate") Date startDate, @Param("endDate") Date endDate);

    /**
     * 根据盘点结果查询盘点记录
     * 
     * @param inventoryResult 盘点结果
     * @return 盘点记录列表
     */
    List<AssetInventory> selectByInventoryResult(@Param("inventoryResult") Integer inventoryResult);

    /**
     * 根据盘点状态查询盘点记录
     * 
     * @param status 盘点状态
     * @return 盘点记录列表
     */
    List<AssetInventory> selectByStatus(@Param("status") Integer status);

    /**
     * 查询盘点统计信息
     * 
     * @param inventoryBatchNo 盘点批次号
     * @return 统计信息
     */
    List<Map<String, Object>> selectInventoryStatistics(@Param("inventoryBatchNo") String inventoryBatchNo);

    /**
     * 查询盘点差异统计
     * 
     * @param startDate 开始日期
     * @param endDate 结束日期
     * @return 差异统计
     */
    List<Map<String, Object>> selectInventoryDifferenceStatistics(@Param("startDate") Date startDate, @Param("endDate") Date endDate);

    /**
     * 根据盘点人员查询盘点记录
     * 
     * @param inventoryPersonnel 盘点人员
     * @return 盘点记录列表
     */
    List<AssetInventory> selectByInventoryPersonnel(@Param("inventoryPersonnel") String inventoryPersonnel);

    /**
     * 查询最新的盘点批次号
     * 
     * @return 最新批次号
     */
    String selectLatestInventoryBatchNo();
}