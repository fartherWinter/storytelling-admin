package com.chennian.storytelling.product.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.chennian.storytelling.product.entity.Product;

import java.math.BigDecimal;
import java.util.List;

/**
 * 商品服务接口
 * 
 * @author chennian
 * @since 2024-01-01
 */
public interface ProductService {

    /**
     * 根据商品ID查询商品
     * 
     * @param productId 商品ID
     * @return 商品信息
     */
    Product getById(Long productId);

    /**
     * 根据商品编码查询商品
     * 
     * @param productCode 商品编码
     * @return 商品信息
     */
    Product getByProductCode(String productCode);

    /**
     * 分页查询商品列表
     * 
     * @param page 分页参数
     * @param productName 商品名称（模糊查询）
     * @param categoryId 分类ID
     * @param brandId 品牌ID
     * @param status 商品状态
     * @param minPrice 最低价格
     * @param maxPrice 最高价格
     * @param isRecommend 是否推荐
     * @param isHot 是否热销
     * @param isNew 是否新品
     * @return 商品分页列表
     */
    IPage<Product> getProductPage(Page<Product> page,
                                String productName,
                                Long categoryId,
                                Long brandId,
                                Integer status,
                                BigDecimal minPrice,
                                BigDecimal maxPrice,
                                Integer isRecommend,
                                Integer isHot,
                                Integer isNew);

    /**
     * 根据分类ID查询商品列表
     * 
     * @param categoryId 分类ID
     * @param status 商品状态
     * @return 商品列表
     */
    List<Product> getByCategoryId(Long categoryId, Integer status);

    /**
     * 根据品牌ID查询商品列表
     * 
     * @param brandId 品牌ID
     * @param status 商品状态
     * @return 商品列表
     */
    List<Product> getByBrandId(Long brandId, Integer status);

    /**
     * 获取推荐商品列表
     * 
     * @param limit 限制数量
     * @return 推荐商品列表
     */
    List<Product> getRecommendProducts(Integer limit);

    /**
     * 获取热销商品列表
     * 
     * @param limit 限制数量
     * @return 热销商品列表
     */
    List<Product> getHotProducts(Integer limit);

    /**
     * 获取新品商品列表
     * 
     * @param limit 限制数量
     * @return 新品商品列表
     */
    List<Product> getNewProducts(Integer limit);

    /**
     * 创建商品
     * 
     * @param product 商品信息
     * @return 创建结果
     */
    boolean createProduct(Product product);

    /**
     * 更新商品
     * 
     * @param product 商品信息
     * @return 更新结果
     */
    boolean updateProduct(Product product);

    /**
     * 删除商品
     * 
     * @param productId 商品ID
     * @return 删除结果
     */
    boolean deleteProduct(Long productId);

    /**
     * 批量删除商品
     * 
     * @param productIds 商品ID列表
     * @return 删除结果
     */
    boolean batchDeleteProducts(List<Long> productIds);

    /**
     * 更新商品状态
     * 
     * @param productId 商品ID
     * @param status 状态
     * @return 更新结果
     */
    boolean updateStatus(Long productId, Integer status);

    /**
     * 批量更新商品状态
     * 
     * @param productIds 商品ID列表
     * @param status 状态
     * @return 更新结果
     */
    boolean batchUpdateStatus(List<Long> productIds, Integer status);

    /**
     * 上架商品
     * 
     * @param productId 商品ID
     * @return 上架结果
     */
    boolean publishProduct(Long productId);

    /**
     * 下架商品
     * 
     * @param productId 商品ID
     * @return 下架结果
     */
    boolean unpublishProduct(Long productId);

    /**
     * 批量上架商品
     * 
     * @param productIds 商品ID列表
     * @return 上架结果
     */
    boolean batchPublishProducts(List<Long> productIds);

    /**
     * 批量下架商品
     * 
     * @param productIds 商品ID列表
     * @return 下架结果
     */
    boolean batchUnpublishProducts(List<Long> productIds);

    /**
     * 更新商品销量
     * 
     * @param productId 商品ID
     * @param quantity 销量增量
     * @return 更新结果
     */
    boolean updateSalesCount(Long productId, Integer quantity);

    /**
     * 更新商品浏览量
     * 
     * @param productId 商品ID
     * @return 更新结果
     */
    boolean updateViewCount(Long productId);

    /**
     * 更新商品收藏量
     * 
     * @param productId 商品ID
     * @param increment 增量（正数增加，负数减少）
     * @return 更新结果
     */
    boolean updateFavoriteCount(Long productId, Integer increment);

    /**
     * 更新商品评论数
     * 
     * @param productId 商品ID
     * @param increment 增量（正数增加，负数减少）
     * @return 更新结果
     */
    boolean updateCommentCount(Long productId, Integer increment);

    /**
     * 更新商品评分
     * 
     * @param productId 商品ID
     * @param rating 评分
     * @return 更新结果
     */
    boolean updateRating(Long productId, BigDecimal rating);

    /**
     * 搜索商品
     * 
     * @param page 分页参数
     * @param keyword 关键词
     * @param categoryId 分类ID
     * @param brandId 品牌ID
     * @param minPrice 最低价格
     * @param maxPrice 最高价格
     * @param sortField 排序字段
     * @param sortOrder 排序方式
     * @return 商品分页列表
     */
    IPage<Product> searchProducts(Page<Product> page,
                                String keyword,
                                Long categoryId,
                                Long brandId,
                                BigDecimal minPrice,
                                BigDecimal maxPrice,
                                String sortField,
                                String sortOrder);

    /**
     * 检查商品编码是否存在
     * 
     * @param productCode 商品编码
     * @param excludeId 排除的商品ID
     * @return 是否存在
     */
    boolean existsByProductCode(String productCode, Long excludeId);

    /**
     * 检查商品名称是否存在
     * 
     * @param productName 商品名称
     * @param excludeId 排除的商品ID
     * @return 是否存在
     */
    boolean existsByProductName(String productName, Long excludeId);
}