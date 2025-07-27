package com.chennian.storytelling.service.mall.Impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.chennian.storytelling.bean.model.mall.MallProductReview;
import com.chennian.storytelling.common.utils.PageParam;
import com.chennian.storytelling.dao.mapper.mall.MallProductReviewMapper;
import com.chennian.storytelling.service.mall.MallProductReviewService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

/**
 * 商品评价 Service实现类
 * 
 * @author chennian
 * @date 2025-01-27
 */
@Slf4j
@Service
public class MallProductReviewServiceImpl extends ServiceImpl<MallProductReviewMapper, MallProductReview> implements MallProductReviewService {
    
    @Autowired
    private MallProductReviewMapper mallProductReviewMapper;
    
    @Override
    public IPage<MallProductReview> findByPage(PageParam<MallProductReview> page, MallProductReview review) {
        try {
            Page<MallProductReview> pageParam = new Page<>(page.getCurrent(), page.getSize());
            return mallProductReviewMapper.selectReviewPage(pageParam, review);
        } catch (Exception e) {
            log.error("分页查询评价列表失败", e);
            throw new RuntimeException("分页查询评价列表失败: " + e.getMessage());
        }
    }
    
    @Override
    public IPage<MallProductReview> getReviewsByProductId(Long productId, Integer reviewType, Integer hasImage, Integer pageNum, Integer pageSize) {
        try {
            if (productId == null) {
                throw new IllegalArgumentException("商品ID不能为空");
            }
            
            Page<MallProductReview> page = new Page<>(pageNum != null ? pageNum : 1, pageSize != null ? pageSize : 10);
            return mallProductReviewMapper.selectReviewsByProductId(page, productId, reviewType, hasImage);
        } catch (Exception e) {
            log.error("根据商品ID获取评价列表失败, productId: {}", productId, e);
            throw new RuntimeException("获取评价列表失败: " + e.getMessage());
        }
    }
    
    @Override
    public IPage<MallProductReview> getReviewsByUserId(Long userId, Integer pageNum, Integer pageSize) {
        try {
            if (userId == null) {
                throw new IllegalArgumentException("用户ID不能为空");
            }
            
            Page<MallProductReview> page = new Page<>(pageNum != null ? pageNum : 1, pageSize != null ? pageSize : 10);
            return mallProductReviewMapper.selectReviewsByUserId(page, userId);
        } catch (Exception e) {
            log.error("根据用户ID获取评价列表失败, userId: {}", userId, e);
            throw new RuntimeException("获取评价列表失败: " + e.getMessage());
        }
    }
    
    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean createReview(MallProductReview review) {
        try {
            if (review == null) {
                throw new IllegalArgumentException("评价信息不能为空");
            }
            if (review.getProductId() == null) {
                throw new IllegalArgumentException("商品ID不能为空");
            }
            if (review.getUserId() == null) {
                throw new IllegalArgumentException("用户ID不能为空");
            }
            if (!StringUtils.hasText(review.getContent())) {
                throw new IllegalArgumentException("评价内容不能为空");
            }
            
            // 检查用户是否已评价过该商品
            if (hasUserReviewed(review.getUserId(), review.getProductId(), review.getOrderId())) {
                throw new IllegalArgumentException("用户已评价过该商品");
            }
            
            // 设置评价类型
            if (review.getRating() != null) {
                if (review.getRating().compareTo(new BigDecimal("4")) >= 0) {
                    review.setReviewType(1); // 好评
                } else if (review.getRating().compareTo(new BigDecimal("3")) >= 0) {
                    review.setReviewType(2); // 中评
                } else {
                    review.setReviewType(3); // 差评
                }
            }
            
            // 设置是否有图/视频
            review.setHasImage(StringUtils.hasText(review.getImages()) ? 1 : 0);
            review.setHasVideo(StringUtils.hasText(review.getVideo()) ? 1 : 0);
            
            // 设置默认值
            if (review.getStatus() == null) {
                review.setStatus(0); // 待审核
            }
            if (review.getLikeCount() == null) {
                review.setLikeCount(0);
            }
            if (review.getReplyCount() == null) {
                review.setReplyCount(0);
            }
            
            review.setCreateTime(LocalDateTime.now());
            review.setUpdateTime(LocalDateTime.now());
            
            return save(review);
        } catch (Exception e) {
            log.error("创建评价失败", e);
            throw new RuntimeException("创建评价失败: " + e.getMessage());
        }
    }
    
    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean updateReview(MallProductReview review) {
        try {
            if (review == null || review.getId() == null) {
                throw new IllegalArgumentException("评价信息或ID不能为空");
            }
            
            review.setUpdateTime(LocalDateTime.now());
            return updateById(review);
        } catch (Exception e) {
            log.error("更新评价失败, reviewId: {}", review != null ? review.getId() : null, e);
            throw new RuntimeException("更新评价失败: " + e.getMessage());
        }
    }
    
    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean deleteReview(Long reviewId) {
        try {
            if (reviewId == null) {
                throw new IllegalArgumentException("评价ID不能为空");
            }
            return removeById(reviewId);
        } catch (Exception e) {
            log.error("删除评价失败, reviewId: {}", reviewId, e);
            throw new RuntimeException("删除评价失败: " + e.getMessage());
        }
    }
    
    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean batchDeleteReviews(List<Long> reviewIds) {
        try {
            if (reviewIds == null || reviewIds.isEmpty()) {
                throw new IllegalArgumentException("评价ID列表不能为空");
            }
            return removeByIds(reviewIds);
        } catch (Exception e) {
            log.error("批量删除评价失败, reviewIds: {}", reviewIds, e);
            throw new RuntimeException("批量删除评价失败: " + e.getMessage());
        }
    }
    
    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean auditReview(Long reviewId, Integer status, String auditRemark, String auditBy) {
        try {
            if (reviewId == null) {
                throw new IllegalArgumentException("评价ID不能为空");
            }
            if (status == null) {
                throw new IllegalArgumentException("审核状态不能为空");
            }
            
            MallProductReview review = new MallProductReview();
            review.setId(reviewId);
            review.setStatus(status);
            review.setAuditRemark(auditRemark);
            review.setAuditBy(auditBy);
            review.setAuditTime(LocalDateTime.now());
            review.setUpdateTime(LocalDateTime.now());
            
            return updateById(review);
        } catch (Exception e) {
            log.error("审核评价失败, reviewId: {}, status: {}", reviewId, status, e);
            throw new RuntimeException("审核评价失败: " + e.getMessage());
        }
    }
    
    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean batchAuditReviews(List<Long> reviewIds, Integer status, String auditRemark, String auditBy) {
        try {
            if (reviewIds == null || reviewIds.isEmpty()) {
                throw new IllegalArgumentException("评价ID列表不能为空");
            }
            if (status == null) {
                throw new IllegalArgumentException("审核状态不能为空");
            }
            
            int result = mallProductReviewMapper.batchAuditReviews(reviewIds, status, auditRemark, auditBy);
            return result > 0;
        } catch (Exception e) {
            log.error("批量审核评价失败, reviewIds: {}, status: {}", reviewIds, status, e);
            throw new RuntimeException("批量审核评价失败: " + e.getMessage());
        }
    }
    
    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean replyReview(Long reviewId, String merchantReply) {
        try {
            if (reviewId == null) {
                throw new IllegalArgumentException("评价ID不能为空");
            }
            if (!StringUtils.hasText(merchantReply)) {
                throw new IllegalArgumentException("商家回复内容不能为空");
            }
            
            int result = mallProductReviewMapper.replyReview(reviewId, merchantReply);
            return result > 0;
        } catch (Exception e) {
            log.error("商家回复评价失败, reviewId: {}", reviewId, e);
            throw new RuntimeException("商家回复评价失败: " + e.getMessage());
        }
    }
    
    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean likeReview(Long reviewId, Long userId) {
        try {
            if (reviewId == null) {
                throw new IllegalArgumentException("评价ID不能为空");
            }
            if (userId == null) {
                throw new IllegalArgumentException("用户ID不能为空");
            }
            
            // 检查用户是否已经点赞过，避免重复点赞
            // 这里应该有一个评价点赞表来记录用户点赞记录
            boolean hasLiked = mallProductReviewMapper.checkUserLiked(reviewId, userId);
            if (hasLiked) {
                throw new IllegalArgumentException("用户已点赞过该评价");
            }
            
            // 记录用户点赞
            mallProductReviewMapper.insertUserLike(reviewId, userId);
            
            // 更新点赞数量
            int result = mallProductReviewMapper.updateLikeCount(reviewId, 1);
            return result > 0;
        } catch (Exception e) {
            log.error("点赞评价失败, reviewId: {}, userId: {}", reviewId, userId, e);
            throw new RuntimeException("点赞评价失败: " + e.getMessage());
        }
    }
    
    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean unlikeReview(Long reviewId, Long userId) {
        try {
            if (reviewId == null) {
                throw new IllegalArgumentException("评价ID不能为空");
            }
            if (userId == null) {
                throw new IllegalArgumentException("用户ID不能为空");
            }
            
            int result = mallProductReviewMapper.updateLikeCount(reviewId, -1);
            return result > 0;
        } catch (Exception e) {
            log.error("取消点赞评价失败, reviewId: {}, userId: {}", reviewId, userId, e);
            throw new RuntimeException("取消点赞评价失败: " + e.getMessage());
        }
    }
    
    @Override
    public Map<String, Object> getProductReviewStats(Long productId) {
        try {
            if (productId == null) {
                throw new IllegalArgumentException("商品ID不能为空");
            }
            
            Map<String, Object> stats = mallProductReviewMapper.selectProductReviewStats(productId);
            if (stats == null) {
                stats = new HashMap<>();
                stats.put("totalCount", 0L);
                stats.put("positiveCount", 0L);
                stats.put("neutralCount", 0L);
                stats.put("negativeCount", 0L);
                stats.put("averageRating", BigDecimal.ZERO);
                stats.put("positiveRate", BigDecimal.ZERO);
            }
            
            return stats;
        } catch (Exception e) {
            log.error("获取商品评价统计失败, productId: {}", productId, e);
            throw new RuntimeException("获取商品评价统计失败: " + e.getMessage());
        }
    }
    
    @Override
    public BigDecimal getProductAverageRating(Long productId) {
        try {
            if (productId == null) {
                throw new IllegalArgumentException("商品ID不能为空");
            }
            
            BigDecimal averageRating = mallProductReviewMapper.selectProductAverageRating(productId);
            return averageRating != null ? averageRating : BigDecimal.ZERO;
        } catch (Exception e) {
            log.error("获取商品平均评分失败, productId: {}", productId, e);
            throw new RuntimeException("获取商品平均评分失败: " + e.getMessage());
        }
    }
    
    @Override
    public Long getProductReviewCount(Long productId) {
        try {
            if (productId == null) {
                throw new IllegalArgumentException("商品ID不能为空");
            }
            
            Long count = mallProductReviewMapper.selectProductReviewCount(productId);
            return count != null ? count : 0L;
        } catch (Exception e) {
            log.error("获取商品评价数量失败, productId: {}", productId, e);
            throw new RuntimeException("获取商品评价数量失败: " + e.getMessage());
        }
    }
    
    @Override
    public BigDecimal getProductPositiveRate(Long productId) {
        try {
            if (productId == null) {
                throw new IllegalArgumentException("商品ID不能为空");
            }
            
            Long totalCount = getProductReviewCount(productId);
            if (totalCount == 0) {
                return BigDecimal.ZERO;
            }
            
            Long positiveCount = mallProductReviewMapper.selectProductPositiveCount(productId);
            if (positiveCount == null) {
                positiveCount = 0L;
            }
            
            return new BigDecimal(positiveCount)
                    .divide(new BigDecimal(totalCount), 4, RoundingMode.HALF_UP)
                    .multiply(new BigDecimal("100"));
        } catch (Exception e) {
            log.error("获取商品好评率失败, productId: {}", productId, e);
            throw new RuntimeException("获取商品好评率失败: " + e.getMessage());
        }
    }
    
    @Override
    public boolean hasUserReviewed(Long userId, Long productId, Long orderId) {
        try {
            if (userId == null) {
                throw new IllegalArgumentException("用户ID不能为空");
            }
            if (productId == null) {
                throw new IllegalArgumentException("商品ID不能为空");
            }
            
            int count = mallProductReviewMapper.checkUserReviewed(userId, productId, orderId);
            return count > 0;
        } catch (Exception e) {
            log.error("检查用户是否已评价失败, userId: {}, productId: {}, orderId: {}", userId, productId, orderId, e);
            throw new RuntimeException("检查用户是否已评价失败: " + e.getMessage());
        }
    }
    
    @Override
    public List<MallProductReview> getHotReviews(Long productId, Integer limit) {
        try {
            if (productId == null) {
                throw new IllegalArgumentException("商品ID不能为空");
            }
            
            return mallProductReviewMapper.selectHotReviews(productId, limit != null ? limit : 10);
        } catch (Exception e) {
            log.error("获取热门评价失败, productId: {}", productId, e);
            throw new RuntimeException("获取热门评价失败: " + e.getMessage());
        }
    }
    
    @Override
    public List<MallProductReview> getLatestReviews(Long productId, Integer limit) {
        try {
            if (productId == null) {
                throw new IllegalArgumentException("商品ID不能为空");
            }
            
            return mallProductReviewMapper.selectLatestReviews(productId, limit != null ? limit : 10);
        } catch (Exception e) {
            log.error("获取最新评价失败, productId: {}", productId, e);
            throw new RuntimeException("获取最新评价失败: " + e.getMessage());
        }
    }
    
    @Override
    public IPage<MallProductReview> getImageReviews(Long productId, Integer pageNum, Integer pageSize) {
        try {
            if (productId == null) {
                throw new IllegalArgumentException("商品ID不能为空");
            }
            
            Page<MallProductReview> page = new Page<>(pageNum != null ? pageNum : 1, pageSize != null ? pageSize : 10);
            return mallProductReviewMapper.selectImageReviews(page, productId);
        } catch (Exception e) {
            log.error("获取有图评价失败, productId: {}", productId, e);
            throw new RuntimeException("获取有图评价失败: " + e.getMessage());
        }
    }
    
    @Override
    public Map<String, Integer> getReviewTagStats(Long productId) {
        try {
            if (productId == null) {
                throw new IllegalArgumentException("商品ID不能为空");
            }
            
            List<Map<String, Object>> tagStatsList = mallProductReviewMapper.selectReviewTagStats(productId);
            return tagStatsList.stream()
                    .collect(Collectors.toMap(
                            item -> (String) item.get("tag"),
                            item -> ((Number) item.get("count")).intValue()
                    ));
        } catch (Exception e) {
            log.error("获取评价标签统计失败, productId: {}", productId, e);
            throw new RuntimeException("获取评价标签统计失败: " + e.getMessage());
        }
    }
    
    // ==================== Controller专用方法实现 ====================
    
    @Override
    public IPage<MallProductReview> getReviewPage(PageParam<MallProductReview> page, MallProductReview review) {
        return findByPage(page, review);
    }
    
    @Override
    public IPage<MallProductReview> getReviewsByProductId(Long productId, PageParam<MallProductReview> page, Integer reviewType, Integer hasImage) {
        try {
            Page<MallProductReview> pageParam = new Page<>(page.getPageNum(), page.getPageSize());
            
            LambdaQueryWrapper<MallProductReview> queryWrapper = new LambdaQueryWrapper<>();
            queryWrapper.eq(MallProductReview::getProductId, productId)
                    .eq(MallProductReview::getAuditStatus, 1); // 只查询已审核通过的评价
            
            if (reviewType != null) {
                queryWrapper.eq(MallProductReview::getReviewType, reviewType);
            }
            
            if (hasImage != null && hasImage == 1) {
                queryWrapper.isNotNull(MallProductReview::getImages)
                        .ne(MallProductReview::getImages, "");
            }
            
            queryWrapper.orderByDesc(MallProductReview::getCreateTime);
            
            return page(pageParam, queryWrapper);
        } catch (Exception e) {
            log.error("根据商品ID获取评价失败, productId: {}", productId, e);
            throw new RuntimeException("根据商品ID获取评价失败: " + e.getMessage());
        }
    }
    
    @Override
    public IPage<MallProductReview> getReviewsByUserId(Long userId, PageParam<MallProductReview> page) {
        try {
            Page<MallProductReview> pageParam = new Page<>(page.getPageNum(), page.getPageSize());
            
            LambdaQueryWrapper<MallProductReview> queryWrapper = new LambdaQueryWrapper<>();
            queryWrapper.eq(MallProductReview::getUserId, userId)
                    .orderByDesc(MallProductReview::getCreateTime);
            
            return page(pageParam, queryWrapper);
        } catch (Exception e) {
            log.error("根据用户ID获取评价失败, userId: {}", userId, e);
            throw new RuntimeException("根据用户ID获取评价失败: " + e.getMessage());
        }
    }
    
    @Override
    @Transactional(rollbackFor = Exception.class)
    public List<String> uploadReviewImages(MultipartFile[] files) {
        try {
            if (files == null || files.length == 0) {
                throw new IllegalArgumentException("上传文件不能为空");
            }
            
            List<String> imageUrls = new ArrayList<>();
            for (MultipartFile file : files) {
                if (file.isEmpty()) {
                    continue;
                }
                
                // 验证文件类型
                String contentType = file.getContentType();
                if (contentType == null || !contentType.startsWith("image/")) {
                    throw new IllegalArgumentException("只能上传图片文件");
                }
                
                // 验证文件大小（限制5MB）
                if (file.getSize() > 5 * 1024 * 1024) {
                    throw new IllegalArgumentException("图片文件大小不能超过5MB");
                }
                
                // 生成文件名
                String originalFilename = file.getOriginalFilename();
                String extension = originalFilename != null ? originalFilename.substring(originalFilename.lastIndexOf(".")) : ".jpg";
                String fileName = "review_" + System.currentTimeMillis() + "_" + UUID.randomUUID().toString().substring(0, 8) + extension;
                
                // 这里应该调用文件上传服务，暂时返回模拟URL
                String imageUrl = "/uploads/reviews/images/" + fileName;
                imageUrls.add(imageUrl);
                
                log.info("评价图片上传成功: {}", imageUrl);
            }
            
            return imageUrls;
        } catch (Exception e) {
            log.error("上传评价图片失败", e);
            throw new RuntimeException("上传评价图片失败: " + e.getMessage());
        }
    }
    
    @Override
    @Transactional(rollbackFor = Exception.class)
    public String uploadReviewVideo(MultipartFile file) {
        try {
            if (file == null || file.isEmpty()) {
                throw new IllegalArgumentException("上传文件不能为空");
            }
            
            // 验证文件类型
            String contentType = file.getContentType();
            if (contentType == null || !contentType.startsWith("video/")) {
                throw new IllegalArgumentException("只能上传视频文件");
            }
            
            // 验证文件大小（限制50MB）
            if (file.getSize() > 50 * 1024 * 1024) {
                throw new IllegalArgumentException("视频文件大小不能超过50MB");
            }
            
            // 生成文件名
            String originalFilename = file.getOriginalFilename();
            String extension = originalFilename != null ? originalFilename.substring(originalFilename.lastIndexOf(".")) : ".mp4";
            String fileName = "review_video_" + System.currentTimeMillis() + "_" + UUID.randomUUID().toString().substring(0, 8) + extension;
            
            // 这里应该调用文件上传服务，暂时返回模拟URL
            String videoUrl = "/uploads/reviews/videos/" + fileName;
            
            log.info("评价视频上传成功: {}", videoUrl);
            return videoUrl;
        } catch (Exception e) {
            log.error("上传评价视频失败", e);
            throw new RuntimeException("上传评价视频失败: " + e.getMessage());
        }
    }
    
    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean likeReview(Long reviewId, String username) {
        try {
            if (reviewId == null) {
                throw new IllegalArgumentException("评价ID不能为空");
            }
            if (!StringUtils.hasText(username)) {
                throw new IllegalArgumentException("用户名不能为空");
            }
            
            // 这里应该根据username获取userId，暂时使用模拟逻辑
            Long userId = getUserIdByUsername(username);
            
            // 检查评价是否存在
            MallProductReview review = getById(reviewId);
            if (review == null) {
                throw new IllegalArgumentException("评价不存在");
            }
            
            // 这里应该实现点赞逻辑，包括检查是否已点赞、更新点赞数等
            // 暂时返回成功
            log.info("用户点赞评价成功, reviewId: {}, userId: {}", reviewId, userId);
            return true;
        } catch (Exception e) {
            log.error("点赞评价失败, reviewId: {}, username: {}", reviewId, username, e);
            throw new RuntimeException("点赞评价失败: " + e.getMessage());
        }
    }
    
    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean unlikeReview(Long reviewId, String username) {
        try {
            if (reviewId == null) {
                throw new IllegalArgumentException("评价ID不能为空");
            }
            if (!StringUtils.hasText(username)) {
                throw new IllegalArgumentException("用户名不能为空");
            }
            
            // 这里应该根据username获取userId，暂时使用模拟逻辑
            Long userId = getUserIdByUsername(username);
            
            // 检查评价是否存在
            MallProductReview review = getById(reviewId);
            if (review == null) {
                throw new IllegalArgumentException("评价不存在");
            }
            
            // 这里应该实现取消点赞逻辑，包括检查是否已点赞、更新点赞数等
            // 暂时返回成功
            log.info("用户取消点赞评价成功, reviewId: {}, userId: {}", reviewId, userId);
            return true;
        } catch (Exception e) {
            log.error("取消点赞评价失败, reviewId: {}, username: {}", reviewId, username, e);
            throw new RuntimeException("取消点赞评价失败: " + e.getMessage());
        }
    }
    
    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean replyReview(Long reviewId, String replyContent, String username) {
        try {
            if (reviewId == null) {
                throw new IllegalArgumentException("评价ID不能为空");
            }
            if (!StringUtils.hasText(replyContent)) {
                throw new IllegalArgumentException("回复内容不能为空");
            }
            if (!StringUtils.hasText(username)) {
                throw new IllegalArgumentException("用户名不能为空");
            }
            
            // 检查评价是否存在
            MallProductReview review = getById(reviewId);
            if (review == null) {
                throw new IllegalArgumentException("评价不存在");
            }
            
            // 构建回复内容，包含回复人信息
            String merchantReply = String.format("[%s] %s", username, replyContent);
            
            // 更新评价的商家回复
            review.setMerchantReply(merchantReply);
            review.setReplyTime(LocalDateTime.now());
            
            boolean result = updateById(review);
            if (result) {
                log.info("商家回复评价成功, reviewId: {}, username: {}", reviewId, username);
            }
            
            return result;
        } catch (Exception e) {
            log.error("商家回复评价失败, reviewId: {}, username: {}", reviewId, username, e);
            throw new RuntimeException("商家回复评价失败: " + e.getMessage());
        }
    }
    
    @Override
    public Map<String, Object> getReviewTags(Long productId) {
        try {
            if (productId == null) {
                throw new IllegalArgumentException("商品ID不能为空");
            }
            
            Map<String, Integer> tagStats = getReviewTagStats(productId);
            Map<String, Object> result = new HashMap<>();
            result.put("tags", tagStats);
            result.put("totalTags", tagStats.size());
            
            return result;
        } catch (Exception e) {
            log.error("获取评价标签失败, productId: {}", productId, e);
            throw new RuntimeException("获取评价标签失败: " + e.getMessage());
        }
    }
    
    @Override
    public IPage<MallProductReview> getPendingAuditReviews(PageParam<MallProductReview> page) {
        try {
            Page<MallProductReview> pageParam = new Page<>(page.getPageNum(), page.getPageSize());
            
            // 查询待审核状态的评价（假设审核状态：0-待审核，1-已通过，2-已拒绝）
            LambdaQueryWrapper<MallProductReview> queryWrapper = new LambdaQueryWrapper<>();
            queryWrapper.eq(MallProductReview::getAuditStatus, 0)
                    .orderByDesc(MallProductReview::getCreateTime);
            
            return page(pageParam, queryWrapper);
        } catch (Exception e) {
            log.error("获取待审核评价列表失败", e);
            throw new RuntimeException("获取待审核评价列表失败: " + e.getMessage());
        }
    }
    
    @Override
    public List<Map<String, Object>> getReviewableProducts(Long userId) {
        try {
            if (userId == null) {
                throw new IllegalArgumentException("用户ID不能为空");
            }
            
            // 这里应该查询用户已完成订单但未评价的商品
            // 暂时返回模拟数据
            List<Map<String, Object>> reviewableProducts = new ArrayList<>();
            
            // 模拟数据
            Map<String, Object> product1 = new HashMap<>();
            product1.put("orderId", 1001L);
            product1.put("productId", 2001L);
            product1.put("productName", "测试商品1");
            product1.put("productImage", "/images/product1.jpg");
            product1.put("orderTime", LocalDateTime.now().minusDays(7));
            reviewableProducts.add(product1);
            
            Map<String, Object> product2 = new HashMap<>();
            product2.put("orderId", 1002L);
            product2.put("productId", 2002L);
            product2.put("productName", "测试商品2");
            product2.put("productImage", "/images/product2.jpg");
            product2.put("orderTime", LocalDateTime.now().minusDays(3));
            reviewableProducts.add(product2);
            
            log.info("获取用户可评价商品成功, userId: {}, count: {}", userId, reviewableProducts.size());
            return reviewableProducts;
        } catch (Exception e) {
            log.error("获取用户可评价商品失败, userId: {}", userId, e);
            throw new RuntimeException("获取用户可评价商品失败: " + e.getMessage());
        }
    }
    
    @Override
    public Boolean checkReviewable(Long userId, Long productId, Long orderId) {
        try {
            if (userId == null) {
                throw new IllegalArgumentException("用户ID不能为空");
            }
            if (productId == null) {
                throw new IllegalArgumentException("商品ID不能为空");
            }
            if (orderId == null) {
                throw new IllegalArgumentException("订单ID不能为空");
            }
            
            // 检查用户是否已评价过该商品
            boolean hasReviewed = hasUserReviewed(userId, productId, orderId);
            if (hasReviewed) {
                return false;
            }
            
            // 这里应该检查订单状态是否为已完成
            // 暂时返回true，表示可以评价
            return true;
        } catch (Exception e) {
            log.error("检查评价权限失败, userId: {}, productId: {}, orderId: {}", userId, productId, orderId, e);
            throw new RuntimeException("检查评价权限失败: " + e.getMessage());
        }
    }
    
    /**
     * 根据用户名获取用户ID（辅助方法）
     * 
     * @param username 用户名
     * @return 用户ID
     */
    private Long getUserIdByUsername(String username) {
        // 这里应该调用用户服务获取用户ID
        // 暂时返回模拟ID
        return 1000L + username.hashCode() % 1000;
    }
}