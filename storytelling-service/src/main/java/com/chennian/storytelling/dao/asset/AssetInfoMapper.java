package com.chennian.storytelling.dao.asset;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.chennian.storytelling.bean.model.asset.AssetInfo;
import org.apache.ibatis.annotations.MapKey;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * 资产信息Mapper接口
 * 
 * @author storytelling
 * @date 2024-01-01
 */
@Mapper
public interface AssetInfoMapper extends BaseMapper<AssetInfo> {

    /**
     * 根据资产编号查询资产
     * 
     * @param assetNo 资产编号
     * @return 资产信息
     */
    AssetInfo selectByAssetNo(@Param("assetNo") String assetNo);

    /**
     * 根据类别ID查询资产列表
     * 
     * @param categoryId 类别ID
     * @return 资产列表
     */
    List<AssetInfo> selectByCategoryId(@Param("categoryId") Long categoryId);

    /**
     * 根据部门ID查询资产列表
     * 
     * @param departmentId 部门ID
     * @return 资产列表
     */
    List<AssetInfo> selectByDepartmentId(@Param("departmentId") Long departmentId);

    /**
     * 根据责任人ID查询资产列表
     * 
     * @param responsiblePersonId 责任人ID
     * @return 资产列表
     */
    List<AssetInfo> selectByResponsiblePersonId(@Param("responsiblePersonId") Long responsiblePersonId);

    /**
     * 根据资产状态查询资产列表
     * 
     * @param status 资产状态
     * @return 资产列表
     */
    List<AssetInfo> selectByStatus(@Param("status") Integer status);

    /**
     * 查询即将到期的资产（根据使用年限）
     * 
     * @param months 提前几个月
     * @return 资产列表
     */
    List<AssetInfo> selectExpiringAssets(@Param("months") Integer months);

    /**
     * 查询资产统计信息
     * 
     * @return 统计信息
     */
    @MapKey("category_name")
    List<Map<String, Object>> selectAssetStatistics();

    /**
     * 查询资产价值统计
     * 
     * @return 价值统计
     */
    @MapKey("department_name")
    List<Map<String, Object>> selectAssetValueStatistics();

    /**
     * 根据购买日期范围查询资产
     * 
     * @param startDate 开始日期
     * @param endDate 结束日期
     * @return 资产列表
     */
    List<AssetInfo> selectByPurchaseDateRange(@Param("startDate") Date startDate, @Param("endDate") Date endDate);

    /**
     * 查询需要折旧的资产
     * 
     * @return 资产列表
     */
    List<AssetInfo> selectAssetsForDepreciation();

    /**
     * 批量更新资产折旧信息
     * 
     * @param assets 资产列表
     * @return 更新数量
     */
    int batchUpdateDepreciation(@Param("assets") List<AssetInfo> assets);
}