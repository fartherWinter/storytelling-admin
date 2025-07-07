package com.chennian.storytelling.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.chennian.storytelling.bean.model.mall.MallBrand;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 商城品牌Mapper接口
 * 
 * @author chennian
 * @date 2024-01-01
 */
@Mapper
public interface MallBrandMapper extends BaseMapper<MallBrand> {
    
    /**
     * 分页查询品牌列表
     *
     * @param page 分页参数
     * @param brandName 品牌名称
     * @param status 状态
     * @param isRecommend 是否推荐
     * @return 品牌分页列表
     */
    IPage<MallBrand> selectBrandPage(
        Page<MallBrand> page,
        @Param("brandName") String brandName,
        @Param("status") Integer status,
        @Param("isRecommend") Integer isRecommend
    );
    
    /**
     * 根据状态查询品牌列表
     *
     * @param status 状态
     * @return 品牌列表
     */
    List<MallBrand> selectByStatus(@Param("status") Integer status);
    
    /**
     * 根据推荐状态查询品牌列表
     *
     * @param isRecommend 是否推荐
     * @param limit 限制数量
     * @return 品牌列表
     */
    List<MallBrand> selectRecommendBrands(@Param("isRecommend") Integer isRecommend, @Param("limit") Integer limit);
    
    /**
     * 根据品牌名称模糊查询
     *
     * @param brandName 品牌名称
     * @return 品牌列表
     */
    List<MallBrand> selectByBrandNameLike(@Param("brandName") String brandName);
    
    /**
     * 获取品牌统计信息
     *
     * @return 统计信息
     */
    List<MallBrand> selectBrandStatistics();
    
    /**
     * 批量更新品牌状态
     *
     * @param ids 品牌ID列表
     * @param status 状态
     * @return 更新数量
     */
    int batchUpdateStatus(@Param("ids") List<Long> ids, @Param("status") Integer status);
}