package com.chennian.storytelling.bean.dto;

import jakarta.validation.constraints.*;
import lombok.Data;


import java.math.BigDecimal;

/**
 * 优惠券使用数据传输对象
 *
 * @author chennian
 * @date 2025-01-27
 */
@Data
public class MallCouponUseDto {

    /**
     * 用户优惠券ID
     */
    @NotNull(message = "用户优惠券ID不能为空")
    private Long userCouponId;

    /**
     * 订单ID
     */
    @NotNull(message = "订单ID不能为空")
    private Long orderId;

    /**
     * 订单金额
     */
    @NotNull(message = "订单金额不能为空")
    @DecimalMin(value = "0.01", message = "订单金额必须大于0")
    private BigDecimal orderAmount;

    /**
     * 用户ID
     */
    @NotNull(message = "用户ID不能为空")
    private Long userId;

}