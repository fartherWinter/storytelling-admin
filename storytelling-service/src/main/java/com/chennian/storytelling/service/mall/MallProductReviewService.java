package com.chennian.storytelling.service.mall;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.IService;
import com.chennian.storytelling.bean.model.mall.MallProductReview;
import com.chennian.storytelling.common.utils.PageParam;
import org.springframework.web.multipart.MultipartFile;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

/**
 * 商品评价 Service接口
 * 
 * @author chennian
 * @date 2025-01-27
 */
public interface MallProductReviewService extends IService<MallProductReview> {
    
    /**
     * 分页查询评价列表
     * 
     * @param page 分页参数
     * @param review 查询条件
     * @return 评价分页数据
     */
    IPage<MallProductReview> findByPage(PageParam<MallProductReview> page, MallProductReview review);
    
    /**
     * 根据商品ID获取评价列表
     * 
     * @param productId 商品ID
     * @param reviewType 评价类型（可选）
     * @param hasImage 是否有图（可选）
     * @param pageNum 页码
     * @param pageSize 页大小
     * @return 评价分页数据
     */
    IPage<MallProductReview> getReviewsByProductId(Long productId, Integer reviewType, Integer hasImage, Integer pageNum, Integer pageSize);
    
    /**
     * 根据用户ID获取评价列表
     * 
     * @param userId 用户ID
     * @param pageNum 页码
     * @param pageSize 页大小
     * @return 评价分页数据
     */
    IPage<MallProductReview> getReviewsByUserId(Long userId, Integer pageNum, Integer pageSize);
    
    /**
     * 创建评价
     * 
     * @param review 评价信息
     * @return 是否成功
     */
    boolean createReview(MallProductReview review);
    
    /**
     * 更新评价
     * 
     * @param review 评价信息
     * @return 是否成功
     */
    boolean updateReview(MallProductReview review);
    
    /**
     * 删除评价
     * 
     * @param reviewId 评价ID
     * @return 是否成功
     */
    boolean deleteReview(Long reviewId);
    
    /**
     * 批量删除评价
     * 
     * @param reviewIds 评价ID列表
     * @return 是否成功
     */
    boolean batchDeleteReviews(List<Long> reviewIds);
    
    /**
     * 审核评价
     * 
     * @param reviewId 评价ID
     * @param status 审核状态
     * @param auditRemark 审核备注
     * @param auditBy 审核人
     * @return 是否成功
     */
    boolean auditReview(Long reviewId, Integer status, String auditRemark, String auditBy);
    
    /**
     * 批量审核评价
     * 
     * @param reviewIds 评价ID列表
     * @param status 审核状态
     * @param auditRemark 审核备注
     * @param auditBy 审核人
     * @return 是否成功
     */
    boolean batchAuditReviews(List<Long> reviewIds, Integer status, String auditRemark, String auditBy);
    
    /**
     * 商家回复评价
     * 
     * @param reviewId 评价ID
     * @param merchantReply 商家回复内容
     * @return 是否成功
     */
    boolean replyReview(Long reviewId, String merchantReply);
    
    /**
     * 点赞评价
     * 
     * @param reviewId 评价ID
     * @param userId 用户ID
     * @return 是否成功
     */
    boolean likeReview(Long reviewId, Long userId);
    
    /**
     * 取消点赞评价
     * 
     * @param reviewId 评价ID
     * @param userId 用户ID
     * @return 是否成功
     */
    boolean unlikeReview(Long reviewId, Long userId);
    
    /**
     * 获取商品评价统计
     * 
     * @param productId 商品ID
     * @return 评价统计数据
     */
    Map<String, Object> getProductReviewStats(Long productId);
    
    /**
     * 获取商品平均评分
     * 
     * @param productId 商品ID
     * @return 平均评分
     */
    BigDecimal getProductAverageRating(Long productId);
    
    /**
     * 获取商品评价数量
     * 
     * @param productId 商品ID
     * @return 评价数量
     */
    Long getProductReviewCount(Long productId);
    
    /**
     * 获取商品好评率
     * 
     * @param productId 商品ID
     * @return 好评率（百分比）
     */
    BigDecimal getProductPositiveRate(Long productId);
    
    /**
     * 获取用户是否已评价商品
     * 
     * @param userId 用户ID
     * @param productId 商品ID
     * @param orderId 订单ID（可选）
     * @return 是否已评价
     */
    boolean hasUserReviewed(Long userId, Long productId, Long orderId);
    
    /**
     * 获取热门评价
     * 
     * @param productId 商品ID
     * @param limit 数量限制
     * @return 热门评价列表
     */
    List<MallProductReview> getHotReviews(Long productId, Integer limit);
    
    /**
     * 获取最新评价
     * 
     * @param productId 商品ID
     * @param limit 数量限制
     * @return 最新评价列表
     */
    List<MallProductReview> getLatestReviews(Long productId, Integer limit);
    
    /**
     * 获取有图评价
     * 
     * @param productId 商品ID
     * @param pageNum 页码
     * @param pageSize 页大小
     * @return 有图评价分页数据
     */
    IPage<MallProductReview> getImageReviews(Long productId, Integer pageNum, Integer pageSize);
    
    /**
     * 获取评价标签统计
     * 
     * @param productId 商品ID
     * @return 评价标签统计
     */
    Map<String, Integer> getReviewTagStats(Long productId);
    
    /**
     * 分页查询评价列表（Controller专用）
     * 
     * @param page 分页参数
     * @param review 查询条件
     * @return 评价分页数据
     */
    IPage<MallProductReview> getReviewPage(PageParam<MallProductReview> page, MallProductReview review);
    
    /**
     * 根据商品ID获取评价列表（Controller专用）
     * 
     * @param productId 商品ID
     * @param page 分页参数
     * @param reviewType 评价类型（可选）
     * @param hasImage 是否有图（可选）
     * @return 评价分页数据
     */
    IPage<MallProductReview> getReviewsByProductId(Long productId, PageParam<MallProductReview> page, Integer reviewType, Integer hasImage);
    
    /**
     * 根据用户ID获取评价列表（Controller专用）
     * 
     * @param userId 用户ID
     * @param page 分页参数
     * @return 评价分页数据
     */
    IPage<MallProductReview> getReviewsByUserId(Long userId, PageParam<MallProductReview> page);
    
    /**
     * 上传评价图片
     * 
     * @param files 图片文件数组
     * @return 图片URL列表
     */
    List<String> uploadReviewImages(MultipartFile[] files);
    
    /**
     * 上传评价视频
     * 
     * @param file 视频文件
     * @return 视频URL
     */
    String uploadReviewVideo(MultipartFile file);
    
    /**
     * 点赞评价（Controller专用）
     * 
     * @param reviewId 评价ID
     * @param username 用户名
     * @return 是否成功
     */
    boolean likeReview(Long reviewId, String username);
    
    /**
     * 取消点赞评价（Controller专用）
     * 
     * @param reviewId 评价ID
     * @param username 用户名
     * @return 是否成功
     */
    boolean unlikeReview(Long reviewId, String username);
    
    /**
     * 商家回复评价（Controller专用）
     * 
     * @param reviewId 评价ID
     * @param replyContent 回复内容
     * @param username 用户名
     * @return 是否成功
     */
    boolean replyReview(Long reviewId, String replyContent, String username);
    
    /**
     * 获取评价标签（Controller专用）
     * 
     * @param productId 商品ID
     * @return 评价标签数据
     */
    Map<String, Object> getReviewTags(Long productId);
    
    /**
     * 获取待审核评价列表
     * 
     * @param page 分页参数
     * @return 待审核评价分页数据
     */
    IPage<MallProductReview> getPendingAuditReviews(PageParam<MallProductReview> page);
    
    /**
     * 获取用户可评价的订单商品
     * 
     * @param userId 用户ID
     * @return 可评价商品列表
     */
    List<Map<String, Object>> getReviewableProducts(Long userId);
    
    /**
     * 检查用户是否可以评价商品
     * 
     * @param userId 用户ID
     * @param productId 商品ID
     * @param orderId 订单ID
     * @return 是否可以评价
     */
    Boolean checkReviewable(Long userId, Long productId, Long orderId);
}