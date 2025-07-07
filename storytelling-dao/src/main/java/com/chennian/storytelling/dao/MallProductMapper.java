package com.chennian.storytelling.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.chennian.storytelling.bean.model.mall.MallProduct;
import com.chennian.storytelling.bean.vo.MallProductVO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.math.BigDecimal;
import java.util.List;

/**
 * 商城商品Mapper接口
 * 
 * @author chennian
 * @date 2025-01-27
 */
@Mapper
public interface MallProductMapper extends BaseMapper<MallProduct> {
    
    /**
     * 分页查询商品列表
     *
     * @param page 分页参数
     * @param categoryId 分类ID
     * @param keyword 关键词
     * @param minPrice 最低价格
     * @param maxPrice 最高价格
     * @param orderBy 排序字段
     * @param isAsc 是否升序
     * @return 商品分页列表
     */
    IPage<MallProductVO> selectProductPage(
        Page<MallProductVO> page,
        @Param("categoryId") Long categoryId,
        @Param("keyword") String keyword,
        @Param("minPrice") BigDecimal minPrice,
        @Param("maxPrice") BigDecimal maxPrice,
        @Param("orderBy") String orderBy,
        @Param("isAsc") Boolean isAsc
    );
    
    /**
     * 查询商品详情
     *
     * @param productId 商品ID
     * @return 商品详情
     */
    MallProductVO selectProductById(@Param("productId") Long productId);
    
    /**
     * 批量更新商品库存
     *
     * @param productStocks 商品库存列表，key为商品ID，value为库存变化量（正数增加，负数减少）
     * @return 更新成功的记录数
     */
    int batchUpdateStock(@Param("productStocks") List<MallProduct> productStocks);
    
    /**
     * 查询热销商品
     *
     * @param limit 限制数量
     * @return 热销商品列表
     */
    List<MallProductVO> selectHotProducts(@Param("limit") Integer limit);
    
    /**
     * 查询推荐商品
     *
     * @param categoryId 分类ID
     * @param limit 限制数量
     * @return 推荐商品列表
     */
    List<MallProductVO> selectRecommendProducts(
        @Param("categoryId") Long categoryId,
        @Param("limit") Integer limit
    );
    
    /**
     * 更新商品评分
     *
     * @param productId 商品ID
     * @param rating 评分
     * @return 是否更新成功
     */
    boolean updateProductRating(
        @Param("productId") Long productId,
        @Param("rating") BigDecimal rating
    );
    
    /**
     * 批量更新商品状态
     *
     * @param productIds 商品ID列表
     * @param status 商品状态
     * @param operator 操作人
     * @return 更新成功的记录数
     */
    int batchUpdateStatus(
        @Param("productIds") List<Long> productIds,
        @Param("status") Integer status,
        @Param("operator") String operator
    );
    
    /**
     * 查询商品销量统计
     *
     * @param categoryId 分类ID
     * @param startDate 开始日期
     * @param endDate 结束日期
     * @return 商品销量统计列表
     */
    List<MallProductVO> selectProductSalesStats(
        @Param("categoryId") Long categoryId,
        @Param("startDate") String startDate,
        @Param("endDate") String endDate
    );
    
    /**
     * 查询库存预警商品
     *
     * @param threshold 库存阈值
     * @return 库存预警商品列表
     */
    List<MallProductVO> selectLowStockProducts(@Param("threshold") Integer threshold);
    
    /**
     * 更新商品浏览量
     *
     * @param productId 商品ID
     * @return 是否更新成功
     */
    boolean incrementViewCount(@Param("productId") Long productId);
}