package com.chennian.storytelling.bean.vo;

import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * 优惠券视图对象
 *
 * @author chennian
 * @date 2025-01-27
 */
@Data
public class MallCouponVO {

    /**
     * 优惠券ID
     */
    private Long id;

    /**
     * 优惠券名称
     */
    private String name;

    /**
     * 优惠券描述
     */
    private String description;

    /**
     * 优惠券类型：1-满减券，2-折扣券，3-固定金额券
     */
    private Integer type;

    /**
     * 优惠券类型名称
     */
    private String typeName;

    /**
     * 优惠金额（满减券和固定金额券使用）
     */
    private BigDecimal discountAmount;

    /**
     * 折扣率（折扣券使用，如0.8表示8折）
     */
    private BigDecimal discountRate;

    /**
     * 使用门槛金额
     */
    private BigDecimal minAmount;

    /**
     * 最大优惠金额（折扣券使用）
     */
    private BigDecimal maxDiscountAmount;

    /**
     * 发行总数量
     */
    private Integer totalCount;

    /**
     * 已领取数量
     */
    private Integer receivedCount;

    /**
     * 已使用数量
     */
    private Integer usedCount;

    /**
     * 剩余数量
     */
    private Integer remainCount;

    /**
     * 每人限领数量
     */
    private Integer limitPerUser;

    /**
     * 有效期开始时间
     */
    private LocalDateTime validStartTime;

    /**
     * 有效期结束时间
     */
    private LocalDateTime validEndTime;

    /**
     * 状态：0-未发布，1-已发布，2-已停用
     */
    private Integer status;

    /**
     * 状态名称
     */
    private String statusName;

    /**
     * 是否可领取
     */
    private Boolean canReceive;

    /**
     * 用户已领取数量
     */
    private Integer userReceivedCount;

    /**
     * 创建时间
     */
    private LocalDateTime createTime;

    /**
     * 创建人
     */
    private String createBy;

}