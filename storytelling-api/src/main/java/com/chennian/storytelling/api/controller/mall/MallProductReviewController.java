package com.chennian.storytelling.api.controller.mall;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.chennian.security.admin.util.SecurityUtils;
import com.chennian.storytelling.bean.model.mall.MallProductReview;
import com.chennian.storytelling.common.response.ServerResponseEntity;
import com.chennian.storytelling.common.utils.PageParam;
import com.chennian.storytelling.service.mall.MallProductReviewService;
import com.chennian.storytelling.common.enums.MallResponseEnum;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;

import org.apache.poi.hpsf.Date;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

/**
 * 商品评价Controller
 * 
 * @author chennian
 * @date 2025-01-27
 */
@Api(tags = "商品评价管理")
@RestController
@RequestMapping("/mall/product-review")
public class MallProductReviewController {

    private final MallProductReviewService mallProductReviewService;

    public MallProductReviewController(MallProductReviewService mallProductReviewService) {
        this.mallProductReviewService = mallProductReviewService;
    }

    /**
     * 分页查询评价列表
     */
    @ApiOperation("分页查询评价列表")
    @PostMapping("/page")
    public ServerResponseEntity<IPage<MallProductReview>> getReviewPage(
            @RequestBody PageParam<MallProductReview> page,
            @ApiParam("查询条件") MallProductReview review) {
        try {
            IPage<MallProductReview> reviewPage = mallProductReviewService.getReviewPage(page, review);
            return ServerResponseEntity.success(reviewPage);
        } catch (Exception e) {
            return ServerResponseEntity.fail(MallResponseEnum.GET_LIST_FAIL);
        }
    }
    
    /**
     * 根据商品ID获取评价列表
     */
    @ApiOperation("根据商品ID获取评价列表")
    @PostMapping("/product/{productId}")
    public ServerResponseEntity<IPage<MallProductReview>> getReviewsByProductId(
            @ApiParam("商品ID") @PathVariable Long productId,
            @RequestBody PageParam<MallProductReview> page,
            @ApiParam("评价类型") @RequestParam(required = false) Integer reviewType,
            @ApiParam("是否有图") @RequestParam(required = false) Integer hasImage) {
        try {
            IPage<MallProductReview> reviews = mallProductReviewService.getReviewsByProductId(
                productId, page, reviewType, hasImage);
            return ServerResponseEntity.success(reviews);
        } catch (Exception e) {
            return ServerResponseEntity.fail(MallResponseEnum.REVIEW_LIST_FAIL);
        }
    }
    
    /**
     * 根据用户ID获取评价列表
     */
    @ApiOperation("根据用户ID获取评价列表")
    @PostMapping("/user/{userId}")
    public ServerResponseEntity<IPage<MallProductReview>> getReviewsByUserId(
            @ApiParam("用户ID") @PathVariable Long userId,
            @RequestBody PageParam<MallProductReview> page) {
        try {
            IPage<MallProductReview> reviews = mallProductReviewService.getReviewsByUserId(userId, page);
            return ServerResponseEntity.success(reviews);
        } catch (Exception e) {
            return ServerResponseEntity.fail(MallResponseEnum.REVIEW_LIST_FAIL);
        }
    }
    
    /**
     * 根据ID查询评价详情
     */
    @ApiOperation("根据ID查询评价详情")
    @GetMapping("/{reviewId}")
    public ServerResponseEntity<MallProductReview> getReviewById(
            @ApiParam("评价ID") @PathVariable Long reviewId) {
        try {
            MallProductReview review = mallProductReviewService.getById(reviewId);
            if (review == null) {
                return ServerResponseEntity.fail(MallResponseEnum.REVIEW_NOT_FOUND);
            }
            return ServerResponseEntity.success(review);
        } catch (Exception e) {
            return ServerResponseEntity.fail(MallResponseEnum.GET_DETAIL_FAIL);
        }
    }
    
    /**
     * 创建商品评价
     */
    @ApiOperation("创建商品评价")
    @PostMapping
    public ServerResponseEntity<String> createReview(@RequestBody MallProductReview review) {
        try {
            // 设置创建者信息
            review.setUserId(SecurityUtils.getUserId());
            review.setCreateTime(LocalDateTime.now());
            
            // 调用服务层创建评价
            boolean result = mallProductReviewService.createReview(review);
            if (result) {
                return ServerResponseEntity.success("评价提交成功");
            } else {
                return ServerResponseEntity.fail(MallResponseEnum.REVIEW_CREATE_FAIL);
            }
        } catch (Exception e) {
            return ServerResponseEntity.fail(MallResponseEnum.REVIEW_CREATE_FAIL);
        }
    }
    
    /**
     * 上传评价图片
     */
    @ApiOperation("上传评价图片")
    @PostMapping("/upload/images")
    public ServerResponseEntity<List<String>> uploadReviewImages(
            @ApiParam("图片文件") @RequestParam("files") MultipartFile[] files) {
        try {
            List<String> imageUrls = mallProductReviewService.uploadReviewImages(files);
            return ServerResponseEntity.success(imageUrls);
        } catch (Exception e) {
            return ServerResponseEntity.fail(MallResponseEnum.REVIEW_UPLOAD_IMAGE_FAIL);
        }
    }
    
    /**
     * 上传评价视频
     */
    @ApiOperation("上传评价视频")
    @PostMapping("/upload/video")
    public ServerResponseEntity<String> uploadReviewVideo(
            @ApiParam("视频文件") @RequestParam("file") MultipartFile file) {
        try {
            String videoUrl = mallProductReviewService.uploadReviewVideo(file);
            return ServerResponseEntity.success(videoUrl);
        } catch (Exception e) {
            return ServerResponseEntity.fail(MallResponseEnum.REVIEW_UPLOAD_VIDEO_FAIL);
        }
    }
    
    /**
     * 点赞评价
     */
    @ApiOperation("点赞评价")
    @PostMapping("/{reviewId}/like")
    public ServerResponseEntity<String> likeReview(
            @ApiParam("评价ID") @PathVariable Long reviewId,
            @ApiParam("用户ID") @RequestParam Long userId) {
        try {
            String username = SecurityUtils.getUserName();
            boolean result = mallProductReviewService.likeReview(reviewId, username);
            if (result) {
                return ServerResponseEntity.success("点赞成功");
            } else {
                return ServerResponseEntity.fail(MallResponseEnum.REVIEW_LIKE_FAIL);
            }
        } catch (Exception e) {
            return ServerResponseEntity.fail(MallResponseEnum.REVIEW_LIKE_FAIL);
        }
    }
    
    /**
     * 取消点赞评价
     */
    @ApiOperation("取消点赞评价")
    @DeleteMapping("/{reviewId}/like")
    public ServerResponseEntity<String> unlikeReview(
            @ApiParam("评价ID") @PathVariable Long reviewId,
            @ApiParam("用户ID") @RequestParam Long userId) {
        try {
            String username = SecurityUtils.getUserName();
            boolean result = mallProductReviewService.unlikeReview(reviewId, username);
            if (result) {
                return ServerResponseEntity.success("取消点赞成功");
            } else {
                return ServerResponseEntity.fail(MallResponseEnum.REVIEW_UNLIKE_FAIL);
            }
        } catch (Exception e) {
            return ServerResponseEntity.fail(MallResponseEnum.REVIEW_UNLIKE_FAIL);
        }
    }
    
    /**
     * 商家回复评价
     */
    @ApiOperation("商家回复评价")
    @PostMapping("/{reviewId}/reply")
    public ServerResponseEntity<String> replyReview(
            @ApiParam("评价ID") @PathVariable Long reviewId,
            @ApiParam("回复内容") @RequestParam String replyContent) {
        try {
            String username = SecurityUtils.getUserName();
            boolean result = mallProductReviewService.replyReview(reviewId, replyContent, username);
            if (result) {
                return ServerResponseEntity.success("回复成功");
            } else {
                return ServerResponseEntity.fail(MallResponseEnum.REVIEW_REPLY_FAIL);
            }
        } catch (Exception e) {
            return ServerResponseEntity.fail(MallResponseEnum.REVIEW_REPLY_FAIL);
        }
    }
    
    /**
     * 审核评价
     */
    @ApiOperation("审核评价")
    @PutMapping("/{reviewId}/audit")
    public ServerResponseEntity<String> auditReview(
            @ApiParam("评价ID") @PathVariable Long reviewId,
            @ApiParam("审核状态") @RequestParam Integer status,
            @ApiParam("审核备注") @RequestParam(required = false) String auditRemark) {
        try {
            String auditor = SecurityUtils.getUserName();
            boolean result = mallProductReviewService.auditReview(reviewId, status, auditRemark, auditor);
            if (result) {
                return ServerResponseEntity.success("审核成功");
            } else {
                return ServerResponseEntity.fail(MallResponseEnum.REVIEW_AUDIT_FAIL);
            }
        } catch (Exception e) {
            return ServerResponseEntity.fail(MallResponseEnum.REVIEW_AUDIT_FAIL);
        }
    }
    
    /**
     * 删除评价
     */
    @ApiOperation("删除评价")
    @DeleteMapping("/{reviewId}")
    public ServerResponseEntity<String> deleteReview(
            @ApiParam("评价ID") @PathVariable Long reviewId) {
        try {
            boolean result = mallProductReviewService.removeById(reviewId);
            if (result) {
                return ServerResponseEntity.success("删除成功");
            } else {
                return ServerResponseEntity.fail(MallResponseEnum.REVIEW_DELETE_FAIL);
            }
        } catch (Exception e) {
            return ServerResponseEntity.fail(MallResponseEnum.REVIEW_DELETE_FAIL);
        }
    }
    
    /**
     * 获取商品评价统计
     */
    @ApiOperation("获取商品评价统计")
    @GetMapping("/product/{productId}/stats")
    public ServerResponseEntity<Map<String, Object>> getProductReviewStats(
            @ApiParam("商品ID") @PathVariable Long productId) {
        try {
            Map<String, Object> stats = mallProductReviewService.getProductReviewStats(productId);
            return ServerResponseEntity.success(stats);
        } catch (Exception e) {
            return ServerResponseEntity.fail(MallResponseEnum.GET_STATS_FAIL);
        }
    }
    
    /**
     * 获取评价标签统计
     */
    @ApiOperation("获取评价标签统计")
    @GetMapping("/product/{productId}/tags")
    public ServerResponseEntity<Map<String, Object>> getReviewTags(
            @ApiParam("商品ID") @PathVariable Long productId) {
        try {
            Map<String, Object> tags = mallProductReviewService.getReviewTags(productId);
            return ServerResponseEntity.success(tags);
        } catch (Exception e) {
            return ServerResponseEntity.fail(MallResponseEnum.GET_TAGS_FAIL);
        }
    }
    
    /**
     * 获取待审核评价列表
     */
    @ApiOperation("获取待审核评价列表")
    @PostMapping("/pending-audit")
    public ServerResponseEntity<IPage<MallProductReview>> getPendingAuditReviews(
            @RequestBody PageParam<MallProductReview> page) {
        try {
            IPage<MallProductReview> pendingReviews = mallProductReviewService.getPendingAuditReviews(page);
            return ServerResponseEntity.success(pendingReviews);
        } catch (Exception e) {
            return ServerResponseEntity.fail(MallResponseEnum.GET_PENDING_AUDIT_FAIL);
        }
    }
    
    /**
     * 批量审核评价
     */
    @ApiOperation("批量审核评价")
    @PutMapping("/batch/audit")
    public ServerResponseEntity<String> batchAuditReviews(
            @ApiParam("评价ID列表") @RequestParam List<Long> reviewIds,
            @ApiParam("审核状态") @RequestParam Integer status,
            @ApiParam("审核备注") @RequestParam(required = false) String auditRemark) {
        try {
            String auditor = SecurityUtils.getUserName();
            boolean result = mallProductReviewService.batchAuditReviews(reviewIds, status, auditRemark, auditor);
            if (result) {
                return ServerResponseEntity.success("批量审核成功");
            } else {
                return ServerResponseEntity.fail(MallResponseEnum.REVIEW_BATCH_AUDIT_FAIL);
            }
        } catch (Exception e) {
            return ServerResponseEntity.fail(MallResponseEnum.REVIEW_BATCH_AUDIT_FAIL);
        }
    }
    
    /**
     * 获取用户可评价的订单商品
     */
    @ApiOperation("获取用户可评价的订单商品")
    @GetMapping("/user/{userId}/reviewable")
    public ServerResponseEntity<List<Map<String, Object>>> getReviewableProducts(
            @ApiParam("用户ID") @PathVariable Long userId) {
        try {
            List<Map<String, Object>> reviewableProducts = mallProductReviewService.getReviewableProducts(userId);
            return ServerResponseEntity.success(reviewableProducts);
        } catch (Exception e) {
            return ServerResponseEntity.fail(MallResponseEnum.GET_REVIEWABLE_PRODUCTS_FAIL);
        }
    }
    
    /**
     * 检查用户是否可以评价商品
     */
    @ApiOperation("检查用户是否可以评价商品")
    @GetMapping("/check-reviewable")
    public ServerResponseEntity<Boolean> checkReviewable(
            @ApiParam("用户ID") @RequestParam Long userId,
            @ApiParam("商品ID") @RequestParam Long productId,
            @ApiParam("订单ID") @RequestParam Long orderId) {
        try {
            Boolean canReview = mallProductReviewService.checkReviewable(userId, productId, orderId);
            return ServerResponseEntity.success(canReview);
        } catch (Exception e) {
            return ServerResponseEntity.fail(MallResponseEnum.CHECK_REVIEWABLE_FAIL);
        }
    }
}