package com.chennian.storytelling.bean.dto;

import jakarta.validation.constraints.*;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * 优惠券数据传输对象
 *
 * @author chennian
 * @date 2025-01-27
 */
@Data
public class MallCouponDto {

    /**
     * 优惠券ID（更新时使用）
     */
    private Long id;

    /**
     * 优惠券名称
     */
    @NotBlank(message = "优惠券名称不能为空")
    @Size(max = 100, message = "优惠券名称长度不能超过100个字符")
    private String name;

    /**
     * 优惠券描述
     */
    @Size(max = 500, message = "优惠券描述长度不能超过500个字符")
    private String description;

    /**
     * 优惠券类型：1-满减券，2-折扣券，3-固定金额券
     */
    @NotNull(message = "优惠券类型不能为空")
    @Min(value = 1, message = "优惠券类型值无效")
    @Max(value = 3, message = "优惠券类型值无效")
    private Integer type;

    /**
     * 优惠金额（满减券和固定金额券使用）
     */
    @DecimalMin(value = "0.01", message = "优惠金额必须大于0")
    private BigDecimal discountAmount;

    /**
     * 折扣率（折扣券使用，如0.8表示8折）
     */
    @DecimalMin(value = "0.01", message = "折扣率必须大于0")
    @DecimalMax(value = "0.99", message = "折扣率必须小于1")
    private BigDecimal discountRate;

    /**
     * 使用门槛金额
     */
    @DecimalMin(value = "0", message = "使用门槛金额不能为负数")
    private BigDecimal minAmount;

    /**
     * 最大优惠金额（折扣券使用）
     */
    @DecimalMin(value = "0.01", message = "最大优惠金额必须大于0")
    private BigDecimal maxDiscountAmount;

    /**
     * 发行总数量
     */
    @NotNull(message = "发行总数量不能为空")
    @Min(value = 1, message = "发行总数量必须大于0")
    private Integer totalCount;

    /**
     * 每人限领数量
     */
    @Min(value = 1, message = "每人限领数量必须大于0")
    private Integer limitPerUser;

    /**
     * 有效期开始时间
     */
    @NotNull(message = "有效期开始时间不能为空")
    private LocalDateTime validStartTime;

    /**
     * 有效期结束时间
     */
    @NotNull(message = "有效期结束时间不能为空")
    private LocalDateTime validEndTime;

}