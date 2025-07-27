package com.chennian.storytelling.product.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.chennian.storytelling.product.entity.Brand;

import java.util.List;

/**
 * 品牌服务接口
 * 
 * @author chennian
 * @since 2024-01-01
 */
public interface BrandService {

    /**
     * 根据品牌ID查询品牌
     * 
     * @param brandId 品牌ID
     * @return 品牌信息
     */
    Brand getById(Long brandId);

    /**
     * 根据品牌编码查询品牌
     * 
     * @param brandCode 品牌编码
     * @return 品牌信息
     */
    Brand getByBrandCode(String brandCode);

    /**
     * 分页查询品牌列表
     * 
     * @param page 分页参数
     * @param brandName 品牌名称（模糊查询）
     * @param status 品牌状态
     * @param isRecommend 是否推荐
     * @return 品牌分页列表
     */
    IPage<Brand> getBrandPage(Page<Brand> page,
                            String brandName,
                            Integer status,
                            Integer isRecommend);

    /**
     * 获取所有可用品牌
     * 
     * @return 品牌列表
     */
    List<Brand> getAllAvailable();

    /**
     * 获取推荐品牌列表
     * 
     * @param limit 限制数量
     * @return 推荐品牌列表
     */
    List<Brand> getRecommendBrands(Integer limit);

    /**
     * 创建品牌
     * 
     * @param brand 品牌信息
     * @return 创建结果
     */
    boolean createBrand(Brand brand);

    /**
     * 更新品牌
     * 
     * @param brand 品牌信息
     * @return 更新结果
     */
    boolean updateBrand(Brand brand);

    /**
     * 删除品牌
     * 
     * @param brandId 品牌ID
     * @return 删除结果
     */
    boolean deleteBrand(Long brandId);

    /**
     * 批量删除品牌
     * 
     * @param brandIds 品牌ID列表
     * @return 删除结果
     */
    boolean batchDeleteBrands(List<Long> brandIds);

    /**
     * 启用品牌
     * 
     * @param brandId 品牌ID
     * @return 启用结果
     */
    boolean enableBrand(Long brandId);

    /**
     * 禁用品牌
     * 
     * @param brandId 品牌ID
     * @return 禁用结果
     */
    boolean disableBrand(Long brandId);

    /**
     * 批量更新品牌状态
     * 
     * @param brandIds 品牌ID列表
     * @param status 状态
     * @return 更新结果
     */
    boolean batchUpdateStatus(List<Long> brandIds, Integer status);

    /**
     * 更新品牌排序
     * 
     * @param brandId 品牌ID
     * @param sortOrder 排序值
     * @return 更新结果
     */
    boolean updateSortOrder(Long brandId, Integer sortOrder);

    /**
     * 检查品牌编码是否存在
     * 
     * @param brandCode 品牌编码
     * @param excludeId 排除的品牌ID
     * @return 是否存在
     */
    boolean existsByBrandCode(String brandCode, Long excludeId);

    /**
     * 检查品牌名称是否存在
     * 
     * @param brandName 品牌名称
     * @param excludeId 排除的品牌ID
     * @return 是否存在
     */
    boolean existsByBrandName(String brandName, Long excludeId);

    /**
     * 检查品牌是否有商品
     * 
     * @param brandId 品牌ID
     * @return 是否有商品
     */
    boolean hasProducts(Long brandId);

    /**
     * 获取品牌下的商品数量
     * 
     * @param brandId 品牌ID
     * @return 商品数量
     */
    Integer getProductCount(Long brandId);
}