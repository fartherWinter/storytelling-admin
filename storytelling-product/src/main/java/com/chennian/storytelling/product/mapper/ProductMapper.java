package com.chennian.storytelling.product.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.chennian.storytelling.product.entity.Product;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.math.BigDecimal;
import java.util.List;

/**
 * 商品Mapper接口
 * 
 * @author chennian
 * @since 2024-01-01
 */
@Mapper
public interface ProductMapper extends BaseMapper<Product> {

    /**
     * 根据商品编码查询商品
     * 
     * @param productCode 商品编码
     * @return 商品信息
     */
    Product selectByProductCode(@Param("productCode") String productCode);

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
    IPage<Product> selectProductPage(Page<Product> page,
                                   @Param("productName") String productName,
                                   @Param("categoryId") Long categoryId,
                                   @Param("brandId") Long brandId,
                                   @Param("status") Integer status,
                                   @Param("minPrice") BigDecimal minPrice,
                                   @Param("maxPrice") BigDecimal maxPrice,
                                   @Param("isRecommend") Integer isRecommend,
                                   @Param("isHot") Integer isHot,
                                   @Param("isNew") Integer isNew);

    /**
     * 根据分类ID查询商品列表
     * 
     * @param categoryId 分类ID
     * @param status 商品状态
     * @return 商品列表
     */
    List<Product> selectByCategoryId(@Param("categoryId") Long categoryId,
                                   @Param("status") Integer status);

    /**
     * 根据品牌ID查询商品列表
     * 
     * @param brandId 品牌ID
     * @param status 商品状态
     * @return 商品列表
     */
    List<Product> selectByBrandId(@Param("brandId") Long brandId,
                                @Param("status") Integer status);

    /**
     * 获取推荐商品列表
     * 
     * @param limit 限制数量
     * @return 推荐商品列表
     */
    List<Product> selectRecommendProducts(@Param("limit") Integer limit);

    /**
     * 获取热销商品列表
     * 
     * @param limit 限制数量
     * @return 热销商品列表
     */
    List<Product> selectHotProducts(@Param("limit") Integer limit);

    /**
     * 获取新品商品列表
     * 
     * @param limit 限制数量
     * @return 新品商品列表
     */
    List<Product> selectNewProducts(@Param("limit") Integer limit);

    /**
     * 更新商品销量
     * 
     * @param productId 商品ID
     * @param quantity 销量增量
     * @return 更新结果
     */
    int updateSalesCount(@Param("productId") Long productId,
                        @Param("quantity") Integer quantity);

    /**
     * 更新商品浏览量
     * 
     * @param productId 商品ID
     * @return 更新结果
     */
    int updateViewCount(@Param("productId") Long productId);

    /**
     * 更新商品收藏量
     * 
     * @param productId 商品ID
     * @param increment 增量（正数增加，负数减少）
     * @return 更新结果
     */
    int updateFavoriteCount(@Param("productId") Long productId,
                           @Param("increment") Integer increment);

    /**
     * 更新商品评论数
     * 
     * @param productId 商品ID
     * @param increment 增量（正数增加，负数减少）
     * @return 更新结果
     */
    int updateCommentCount(@Param("productId") Long productId,
                          @Param("increment") Integer increment);

    /**
     * 更新商品评分
     * 
     * @param productId 商品ID
     * @param rating 评分
     * @return 更新结果
     */
    int updateRating(@Param("productId") Long productId,
                    @Param("rating") BigDecimal rating);

    /**
     * 批量更新商品状态
     * 
     * @param productIds 商品ID列表
     * @param status 状态
     * @param updateBy 更新者
     * @return 更新结果
     */
    int batchUpdateStatus(@Param("productIds") List<Long> productIds,
                         @Param("status") Integer status,
                         @Param("updateBy") String updateBy);

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
                                @Param("keyword") String keyword,
                                @Param("categoryId") Long categoryId,
                                @Param("brandId") Long brandId,
                                @Param("minPrice") BigDecimal minPrice,
                                @Param("maxPrice") BigDecimal maxPrice,
                                @Param("sortField") String sortField,
                                @Param("sortOrder") String sortOrder);

    /**
     * 检查商品编码是否存在
     * 
     * @param productCode 商品编码
     * @param excludeId 排除的商品ID
     * @return 是否存在
     */
    boolean existsByProductCode(@Param("productCode") String productCode,
                               @Param("excludeId") Long excludeId);

    /**
     * 检查商品名称是否存在
     * 
     * @param productName 商品名称
     * @param excludeId 排除的商品ID
     * @return 是否存在
     */
    boolean existsByProductName(@Param("productName") String productName,
                               @Param("excludeId") Long excludeId);
}