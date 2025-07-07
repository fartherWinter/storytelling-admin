package com.chennian.storytelling.bean.model.mall;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * 商品评价实体类
 * 
 * @author chennian
 * @date 2025-01-27
 */
@Data
@EqualsAndHashCode(callSuper = false)
@ToString
@TableName("mall_product_review")
public class MallProductReview implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 评价ID
     */
    @TableId(value = "id", type = IdType.ASSIGN_ID)
    private Long id;

    /**
     * 商品ID
     */
    private Long productId;

    /**
     * SKU ID
     */
    private Long skuId;

    /**
     * 订单ID
     */
    private Long orderId;

    /**
     * 用户ID
     */
    private Long userId;

    /**
     * 用户昵称
     */
    private String userNickname;

    /**
     * 用户头像
     */
    private String userAvatar;

    /**
     * 评价内容
     */
    private String content;

    /**
     * 评价图片，多张图片用逗号分隔
     */
    private String images;

    /**
     * 评价视频
     */
    private String video;

    /**
     * 综合评分（1-5分）
     */
    private BigDecimal rating;

    /**
     * 商品质量评分（1-5分）
     */
    private BigDecimal qualityRating;

    /**
     * 物流服务评分（1-5分）
     */
    private BigDecimal logisticsRating;

    /**
     * 服务态度评分（1-5分）
     */
    private BigDecimal serviceRating;

    /**
     * 评价类型: 1-好评, 2-中评, 3-差评
     */
    private Integer reviewType;

    /**
     * 是否匿名: 0-否, 1-是
     */
    private Integer isAnonymous;

    /**
     * 是否有图: 0-否, 1-是
     */
    private Integer hasImage;

    /**
     * 是否有视频: 0-否, 1-是
     */
    private Integer hasVideo;

    /**
     * 点赞数
     */
    private Integer likeCount;

    /**
     * 回复数
     */
    private Integer replyCount;

    /**
     * 商家回复内容
     */
    private String merchantReply;

    /**
     * 商家回复时间
     */
    private LocalDateTime merchantReplyTime;

    /**
     * 状态: 0-待审核, 1-已通过, 2-已拒绝, 3-已删除
     */
    private Integer status;

    /**
     * 审核备注
     */
    private String auditRemark;

    /**
     * 审核时间
     */
    private LocalDateTime auditTime;

    /**
     * 审核人
     */
    private String auditBy;

    /**
     * 创建时间
     */
    private LocalDateTime createTime;

    /**
     * 更新时间
     */
    private LocalDateTime updateTime;
}