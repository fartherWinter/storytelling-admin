package com.chennian.storytelling.dao.asset;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.chennian.storytelling.bean.model.asset.AssetDepreciation;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

/**
 * 资产折旧记录Mapper接口
 * 
 * @author storytelling
 * @date 2024-01-01
 */
@Mapper
public interface AssetDepreciationMapper extends BaseMapper<AssetDepreciation> {

    /**
     * 分页查询折旧记录
     */
    IPage<AssetDepreciation> selectDepreciationPage(Page<AssetDepreciation> page, @Param("depreciation") AssetDepreciation depreciation);

    /**
     * 根据资产ID查询最新折旧记录
     */
    AssetDepreciation selectLatestByAssetId(@Param("assetId") Long assetId);

    /**
     * 根据折旧年月查询记录
     */
    List<AssetDepreciation> selectByDepreciationMonth(@Param("depreciationMonth") String depreciationMonth);

    /**
     * 批量插入折旧记录
     */
    int batchInsert(@Param("list") List<AssetDepreciation> depreciationList);

    /**
     * 根据资产ID和折旧年月查询记录
     */
    AssetDepreciation selectByAssetIdAndMonth(@Param("assetId") Long assetId, @Param("depreciationMonth") String depreciationMonth);

    /**
     * 查询折旧统计信息
     */
    List<Map<String, Object>> selectDepreciationStatistics(@Param("startMonth") String startMonth, @Param("endMonth") String endMonth);

    /**
     * 根据资产ID列表查询折旧记录
     */
    List<AssetDepreciation> selectByAssetIds(@Param("assetIds") Long[] assetIds, @Param("depreciationMonth") String depreciationMonth);

    /**
     * 更新折旧状态
     */
    int updateDepreciationStatus(@Param("id") Long id, @Param("status") Integer status);

    /**
     * 查询需要计算折旧的资产
     */
    List<Map<String, Object>> selectAssetsForDepreciation(@Param("depreciationMonth") String depreciationMonth);
}