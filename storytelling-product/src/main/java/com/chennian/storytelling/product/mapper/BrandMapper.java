package com.chennian.storytelling.product.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.chennian.storytelling.product.entity.Brand;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 品牌Mapper接口
 * 
 * @author chennian
 * @since 2024-01-01
 */
@Mapper
public interface BrandMapper extends BaseMapper<Brand> {

    /**
     * 根据品牌编码查询品牌
     * 
     * @param brandCode 品牌编码
     * @return 品牌信息
     */
    Brand selectByBrandCode(@Param("brandCode") String brandCode);

    /**
     * 分页查询品牌列表
     * 
     * @param page 分页参数
     * @param brandName 品牌名称（模糊查询）
     * @param status 品牌状态
     * @param isRecommend 是否推荐
     * @return 品牌分页列表
     */
    IPage<Brand> selectBrandPage(Page<Brand> page,
                               @Param("brandName") String brandName,
                               @Param("status") Integer status,
                               @Param("isRecommend") Integer isRecommend);

    /**
     * 获取所有可用品牌
     * 
     * @return 品牌列表
     */
    List<Brand> selectAllAvailable();

    /**
     * 获取推荐品牌列表
     * 
     * @param limit 限制数量
     * @return 推荐品牌列表
     */
    List<Brand> selectRecommendBrands(@Param("limit") Integer limit);

    /**
     * 批量更新品牌状态
     * 
     * @param brandIds 品牌ID列表
     * @param status 状态
     * @param updateBy 更新者
     * @return 更新结果
     */
    int batchUpdateStatus(@Param("brandIds") List<Long> brandIds,
                         @Param("status") Integer status,
                         @Param("updateBy") String updateBy);

    /**
     * 检查品牌编码是否存在
     * 
     * @param brandCode 品牌编码
     * @param excludeId 排除的品牌ID
     * @return 是否存在
     */
    boolean existsByBrandCode(@Param("brandCode") String brandCode,
                             @Param("excludeId") Long excludeId);

    /**
     * 检查品牌名称是否存在
     * 
     * @param brandName 品牌名称
     * @param excludeId 排除的品牌ID
     * @return 是否存在
     */
    boolean existsByBrandName(@Param("brandName") String brandName,
                             @Param("excludeId") Long excludeId);

    /**
     * 检查品牌是否有商品
     * 
     * @param brandId 品牌ID
     * @return 是否有商品
     */
    boolean hasProducts(@Param("brandId") Long brandId);

    /**
     * 获取品牌下的商品数量
     * 
     * @param brandId 品牌ID
     * @return 商品数量
     */
    Integer getProductCount(@Param("brandId") Long brandId);

    /**
     * 获取最大排序值
     * 
     * @return 最大排序值
     */
    Integer getMaxSortOrder();
}