package com.chennian.storytelling.dao.asset;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.chennian.storytelling.bean.model.asset.AssetCategory;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 资产类别Mapper接口
 * 
 * @author storytelling
 * @date 2024-01-01
 */
@Mapper
public interface AssetCategoryMapper extends BaseMapper<AssetCategory> {

    /**
     * 根据类别编码查询类别
     * 
     * @param categoryCode 类别编码
     * @return 资产类别
     */
    AssetCategory selectByCategoryCode(@Param("categoryCode") String categoryCode);

    /**
     * 根据上级类别ID查询子类别列表
     * 
     * @param parentId 上级类别ID
     * @return 子类别列表
     */
    List<AssetCategory> selectByParentId(@Param("parentId") Long parentId);

    /**
     * 查询所有根类别
     * 
     * @return 根类别列表
     */
    List<AssetCategory> selectRootCategories();

    /**
     * 查询类别树形结构
     * 
     * @return 类别树
     */
    List<AssetCategory> selectCategoryTree();

    /**
     * 根据状态查询类别列表
     * 
     * @param status 状态
     * @return 类别列表
     */
    List<AssetCategory> selectByStatus(@Param("status") Integer status);
}