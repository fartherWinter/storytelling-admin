package com.chennian.storytelling.bean.vo;

import lombok.Data;

import java.math.BigDecimal;

/**
 * 优惠券统计信息视图对象
 *
 * @author chennian
 * @date 2025-01-27
 */
@Data
public class MallCouponStatisticsVO {

    /**
     * 总优惠券数量
     */
    private Integer totalCoupons;

    /**
     * 已发布优惠券数量
     */
    private Integer publishedCoupons;

    /**
     * 未发布优惠券数量
     */
    private Integer unpublishedCoupons;

    /**
     * 已停用优惠券数量
     */
    private Integer disabledCoupons;

    /**
     * 总发行数量
     */
    private Integer totalIssued;

    /**
     * 总领取数量
     */
    private Integer totalReceived;

    /**
     * 总使用数量
     */
    private Integer totalUsed;

    /**
     * 总过期数量
     */
    private Integer totalExpired;

    /**
     * 领取率（%）
     */
    private BigDecimal receiveRate;

    /**
     * 使用率（%）
     */
    private BigDecimal useRate;

    /**
     * 过期率（%）
     */
    private BigDecimal expireRate;

    /**
     * 总优惠金额
     */
    private BigDecimal totalDiscountAmount;

    /**
     * 本月新增优惠券数量
     */
    private Integer monthlyNewCoupons;

    /**
     * 本月领取数量
     */
    private Integer monthlyReceived;

    /**
     * 本月使用数量
     */
    private Integer monthlyUsed;

    /**
     * 本月优惠金额
     */
    private BigDecimal monthlyDiscountAmount;

}