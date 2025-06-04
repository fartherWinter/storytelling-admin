package com.chennian.storytelling.bean.vo;

import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * 用户优惠券视图对象
 *
 * @author chennian
 * @date 2025-01-27
 */
@Data
public class MallUserCouponVO {

    /**
     * 主键ID
     */
    private Long id;

    /**
     * 用户ID
     */
    private Long userId;

    /**
     * 优惠券ID
     */
    private Long couponId;

    /**
     * 优惠券编码
     */
    private String couponCode;

    /**
     * 优惠券名称
     */
    private String couponName;

    /**
     * 优惠券描述
     */
    private String couponDescription;

    /**
     * 优惠券类型：1-满减券，2-折扣券，3-固定金额券
     */
    private Integer couponType;

    /**
     * 优惠券类型名称
     */
    private String couponTypeName;

    /**
     * 优惠金额
     */
    private BigDecimal discountAmount;

    /**
     * 折扣率
     */
    private BigDecimal discountRate;

    /**
     * 使用门槛金额
     */
    private BigDecimal minAmount;

    /**
     * 最大优惠金额
     */
    private BigDecimal maxDiscountAmount;

    /**
     * 状态：0-未使用，1-已使用，2-已过期
     */
    private Integer status;

    /**
     * 状态名称
     */
    private String statusName;

    /**
     * 领取时间
     */
    private LocalDateTime receiveTime;

    /**
     * 使用时间
     */
    private LocalDateTime useTime;

    /**
     * 过期时间
     */
    private LocalDateTime expireTime;

    /**
     * 使用的订单ID
     */
    private Long orderId;

    /**
     * 是否可使用
     */
    private Boolean canUse;

    /**
     * 剩余天数
     */
    private Long remainDays;

}