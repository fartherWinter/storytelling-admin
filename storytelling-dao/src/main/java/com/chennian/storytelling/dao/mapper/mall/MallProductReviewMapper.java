package com.chennian.storytelling.dao.mapper.mall;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.chennian.storytelling.bean.model.mall.MallProductReview;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

/**
 * 商品评价 Mapper接口
 * 
 * @author chennian
 * @date 2025-01-27
 */
@Mapper
public interface MallProductReviewMapper extends BaseMapper<MallProductReview> {
    
    /**
     * 分页查询评价列表
     * 
     * @param page 分页参数
     * @param review 查询条件
     * @return 评价分页数据
     */
    IPage<MallProductReview> selectReviewPage(Page<MallProductReview> page, @Param("review") MallProductReview review);
    
    /**
     * 根据商品ID获取评价列表
     * 
     * @param page 分页参数
     * @param productId 商品ID
     * @param reviewType 评价类型
     * @param hasImage 是否有图
     * @return 评价分页数据
     */
    IPage<MallProductReview> selectReviewsByProductId(Page<MallProductReview> page, 
                                                      @Param("productId") Long productId,
                                                      @Param("reviewType") Integer reviewType,
                                                      @Param("hasImage") Integer hasImage);
    
    /**
     * 根据用户ID获取评价列表
     * 
     * @param page 分页参数
     * @param userId 用户ID
     * @return 评价分页数据
     */
    IPage<MallProductReview> selectReviewsByUserId(Page<MallProductReview> page, @Param("userId") Long userId);
    
    /**
     * 批量审核评价
     * 
     * @param reviewIds 评价ID列表
     * @param status 审核状态
     * @param auditRemark 审核备注
     * @param auditBy 审核人
     * @return 更新数量
     */
    int batchAuditReviews(@Param("reviewIds") List<Long> reviewIds, 
                          @Param("status") Integer status,
                          @Param("auditRemark") String auditRemark,
                          @Param("auditBy") String auditBy);
    
    /**
     * 商家回复评价
     * 
     * @param reviewId 评价ID
     * @param merchantReply 商家回复内容
     * @return 更新数量
     */
    int replyReview(@Param("reviewId") Long reviewId, @Param("merchantReply") String merchantReply);
    
    /**
     * 更新评价点赞数
     * 
     * @param reviewId 评价ID
     * @param increment 增量（正数为增加，负数为减少）
     * @return 更新数量
     */
    int updateLikeCount(@Param("reviewId") Long reviewId, @Param("increment") Integer increment);
    
    /**
     * 获取商品评价统计
     * 
     * @param productId 商品ID
     * @return 评价统计数据
     */
    Map<String, Object> selectProductReviewStats(@Param("productId") Long productId);
    
    /**
     * 获取商品平均评分
     * 
     * @param productId 商品ID
     * @return 平均评分
     */
    BigDecimal selectProductAverageRating(@Param("productId") Long productId);
    
    /**
     * 获取商品评价数量
     * 
     * @param productId 商品ID
     * @return 评价数量
     */
    Long selectProductReviewCount(@Param("productId") Long productId);
    
    /**
     * 获取商品好评数量
     * 
     * @param productId 商品ID
     * @return 好评数量
     */
    Long selectProductPositiveCount(@Param("productId") Long productId);
    
    /**
     * 检查用户是否已评价商品
     * 
     * @param userId 用户ID
     * @param productId 商品ID
     * @param orderId 订单ID
     * @return 评价数量
     */
    int checkUserReviewed(@Param("userId") Long userId, 
                          @Param("productId") Long productId,
                          @Param("orderId") Long orderId);
    
    /**
     * 获取热门评价
     * 
     * @param productId 商品ID
     * @param limit 数量限制
     * @return 热门评价列表
     */
    List<MallProductReview> selectHotReviews(@Param("productId") Long productId, @Param("limit") Integer limit);
    
    /**
     * 获取最新评价
     * 
     * @param productId 商品ID
     * @param limit 数量限制
     * @return 最新评价列表
     */
    List<MallProductReview> selectLatestReviews(@Param("productId") Long productId, @Param("limit") Integer limit);
    
    /**
     * 获取有图评价
     * 
     * @param page 分页参数
     * @param productId 商品ID
     * @return 有图评价分页数据
     */
    IPage<MallProductReview> selectImageReviews(Page<MallProductReview> page, @Param("productId") Long productId);
    
    /**
     * 获取评价标签统计
     * 
     * @param productId 商品ID
     * @return 评价标签统计
     */
    List<Map<String, Object>> selectReviewTagStats(@Param("productId") Long productId);
    
    /**
     * 根据商品ID删除评价
     * 
     * @param productId 商品ID
     * @return 删除数量
     */
    int deleteReviewsByProductId(@Param("productId") Long productId);
}