package com.chennian.storytelling.bean.vo;

import com.chennian.storytelling.bean.model.mall.MallSubOrder;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * 商城子订单VO
 * 
 * @author chennian
 * @date 2025-01-27
 */
@Data
@EqualsAndHashCode(callSuper = true)
public class MallSubOrderVO extends MallSubOrder {
    
    /**
     * 订单状态名称
     */
    private String statusName;
    
    /**
     * 商品分类ID
     */
    private Long categoryId;
    
    /**
     * 商品分类名称
     */
    private String categoryName;
    
    /**
     * 商品品牌ID
     */
    private Long brandId;
    
    /**
     * 商品品牌名称
     */
    private String brandName;
    
    /**
     * 商品规格，JSON格式
     */
    private String specifications;
    
    /**
     * 商品评分
     */
    private BigDecimal productRating;
    
    /**
     * 商品评价数量
     */
    private Integer reviewCount;
    
    /**
     * 商品销量
     */
    private Integer salesCount;
    
    /**
     * 退款状态
     */
    private Integer refundStatus;
    
    /**
     * 退款金额
     */
    private BigDecimal refundAmount;
    
    /**
     * 退款原因
     */
    private String refundReason;
    
    /**
     * 退款申请时间
     */
    private LocalDateTime refundApplyTime;
    
    /**
     * 退款处理时间
     */
    private LocalDateTime refundHandleTime;
    
    /**
     * 物流公司
     */
    private String logisticsCompany;
    
    /**
     * 物流单号
     */
    private String logisticsNo;
    
    /**
     * 发货时间
     */
    private LocalDateTime deliveryTime;
    
    /**
     * 收货时间
     */
    private LocalDateTime receiveTime;
    
    /**
     * 评价状态：0-未评价，1-已评价
     */
    private Integer reviewStatus;
    
    /**
     * 评价时间
     */
    private LocalDateTime reviewTime;
    
    /**
     * 评价内容
     */
    private String reviewContent;
    
    /**
     * 评价图片，JSON格式
     */
    private String reviewImages;
    
    /**
     * 评分
     */
    private BigDecimal rating;
    
    /**
     * 商家回复
     */
    private String sellerReply;
    
    /**
     * 商家回复时间
     */
    private LocalDateTime replyTime;
} 