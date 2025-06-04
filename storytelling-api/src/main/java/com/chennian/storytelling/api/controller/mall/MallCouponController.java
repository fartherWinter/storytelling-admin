package com.chennian.storytelling.api.controller.mall;

import com.chennian.storytelling.bean.dto.MallCouponUseDto;
import com.chennian.storytelling.bean.vo.MallCouponVO;
import com.chennian.storytelling.bean.vo.MallUserCouponVO;
import com.chennian.storytelling.common.enums.MallResponseEnum;
import com.chennian.storytelling.common.response.ServerResponseEntity;
import com.chennian.storytelling.service.mall.MallCouponService;
import com.chennian.storytelling.service.mall.MallUserCouponService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import lombok.extern.slf4j.Slf4j;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.List;

/**
 * 优惠券使用控制器
 *
 * @author chennian
 * @date 2025-01-27
 */
@Api(tags = "优惠券使用")
@RestController
@RequestMapping("/api/coupon")
@Slf4j
@Validated
public class MallCouponController {

    private final MallCouponService mallCouponService;
    private final MallUserCouponService mallUserCouponService;

     public MallCouponController(MallCouponService mallCouponService, MallUserCouponService mallUserCouponService) {
         this.mallCouponService = mallCouponService;
         this.mallUserCouponService = mallUserCouponService;
     }


    /**
     * 查询可领取的优惠券列表
     */
    @ApiOperation("查询可领取的优惠券列表")
    @GetMapping("/available")
    public ServerResponseEntity<List<MallCouponVO>> getAvailableCoupons(
            @ApiParam("用户ID") @RequestParam(required = false) Long userId) {
        try {
            List<MallCouponVO> list = mallCouponService.selectAvailableCoupons(userId);
            return ServerResponseEntity.success(list);
        } catch (Exception e) {
            log.error("查询可领取优惠券失败", e);
            return ServerResponseEntity.fail(MallResponseEnum.COUPON_NOT_EXIST.getCode(), MallResponseEnum.COUPON_NOT_EXIST.getMessage());
        }
    }

    /**
     * 领取优惠券
     */
    @ApiOperation("领取优惠券")
    @PostMapping("/receive/{couponId}")
    public ServerResponseEntity<String> receiveCoupon(
            @ApiParam("优惠券ID") @PathVariable Long couponId,
            @ApiParam("用户ID") @RequestParam Long userId) {
        try {
            boolean result = mallUserCouponService.receiveCoupon(userId, couponId);
            if (result) {
                return ServerResponseEntity.success(MallResponseEnum.COUPON_RECEIVE_SUCCESS.getMessage());
            }
            return ServerResponseEntity.fail(MallResponseEnum.COUPON_RECEIVE_FAIL.getCode(), MallResponseEnum.COUPON_RECEIVE_FAIL.getMessage());
        } catch (Exception e) {
            log.error("领取优惠券失败，couponId: {}, userId: {}", couponId, userId, e);
            return ServerResponseEntity.fail(MallResponseEnum.COUPON_RECEIVE_FAIL.getCode(), MallResponseEnum.COUPON_RECEIVE_FAIL.getMessage());
        }
    }

    /**
     * 查询用户的优惠券列表
     */
    @ApiOperation("查询用户的优惠券列表")
    @GetMapping("/user/{userId}")
    public ServerResponseEntity<List<MallUserCouponVO>> getUserCoupons(
            @ApiParam("用户ID") @PathVariable Long userId,
            @ApiParam("状态：0-未使用，1-已使用，2-已过期") @RequestParam(required = false) Integer status) {
        try {
            var page = mallUserCouponService.selectUserCouponPage(userId, status, 1, 100);
            List<MallUserCouponVO> list = page.getRecords();
            return ServerResponseEntity.success(list);
        } catch (Exception e) {
            log.error("查询用户优惠券失败，userId: {}", userId, e);
            return ServerResponseEntity.fail(MallResponseEnum.COUPON_NOT_EXIST.getCode(), MallResponseEnum.COUPON_NOT_EXIST.getMessage());
        }
    }

    /**
     * 查询用户可用于指定订单的优惠券
     */
    @ApiOperation("查询用户可用于指定订单的优惠券")
    @GetMapping("/user/{userId}/usable")
    public ServerResponseEntity<List<MallUserCouponVO>> getUsableCoupons(
            @ApiParam("用户ID") @PathVariable Long userId,
            @ApiParam("订单金额") @RequestParam java.math.BigDecimal orderAmount) {
        try {
            List<MallUserCouponVO> list = mallUserCouponService.selectUsableCouponsForOrder(userId, orderAmount);
            return ServerResponseEntity.success(list);
        } catch (Exception e) {
            log.error("查询可用优惠券失败，userId: {}, orderAmount: {}", userId, orderAmount, e);
            return ServerResponseEntity.fail(MallResponseEnum.COUPON_NOT_EXIST.getCode(), MallResponseEnum.COUPON_NOT_EXIST.getMessage());
        }
    }

    /**
     * 使用优惠券
     */
    @ApiOperation("使用优惠券")
    @PostMapping("/use")
    public ServerResponseEntity<String> useCoupon(@RequestBody MallCouponUseDto useDto) {
        try {
            boolean result = mallUserCouponService.useCoupon(useDto);
            if (result) {
                return ServerResponseEntity.success(MallResponseEnum.COUPON_USE_SUCCESS.getMessage());
            }
            return ServerResponseEntity.fail(MallResponseEnum.COUPON_USE_FAIL.getCode(), MallResponseEnum.COUPON_USE_FAIL.getMessage());
        } catch (Exception e) {
            log.error("使用优惠券失败，useDto: {}", useDto, e);
            return ServerResponseEntity.fail(MallResponseEnum.COUPON_USE_FAIL.getCode(), MallResponseEnum.COUPON_USE_FAIL.getMessage());
        }
    }

    /**
     * 计算优惠券优惠金额
     */
    @ApiOperation("计算优惠券优惠金额")
    @PostMapping("/calculate")
    public ServerResponseEntity<java.math.BigDecimal> calculateDiscount(
            @ApiParam("用户优惠券ID") @RequestParam Long userCouponId,
            @ApiParam("订单金额") @RequestParam java.math.BigDecimal orderAmount) {
        try {
            BigDecimal discountAmount = mallUserCouponService.calculateDiscountAmount(userCouponId, orderAmount);
            return ServerResponseEntity.success(discountAmount);
        } catch (Exception e) {
            log.error("计算优惠金额失败，userCouponId: {}, orderAmount: {}", userCouponId, orderAmount, e);
            return ServerResponseEntity.fail(MallResponseEnum.COUPON_NOT_EXIST.getCode(), MallResponseEnum.COUPON_NOT_EXIST.getMessage());
        }
    }

    /**
     * 验证优惠券是否可用
     */
    @ApiOperation("验证优惠券是否可用")
    @PostMapping("/validate")
    public ServerResponseEntity<Boolean> validateCoupon(
            @ApiParam("用户优惠券ID") @RequestParam Long userCouponId,
            @ApiParam("订单金额") @RequestParam java.math.BigDecimal orderAmount) {
        try {
            boolean isValid = mallUserCouponService.validateCouponUsable(userCouponId, orderAmount);
            return ServerResponseEntity.success(isValid);
        } catch (Exception e) {
            log.error("验证优惠券失败，userCouponId: {}, orderAmount: {}", userCouponId, orderAmount, e);
            return ServerResponseEntity.fail(MallResponseEnum.COUPON_NOT_EXIST.getCode(), MallResponseEnum.COUPON_NOT_EXIST.getMessage());
        }
    }

    /**
     * 查询优惠券详情
     */
    @ApiOperation("查询优惠券详情")
    @GetMapping("/detail/{id}")
    public ServerResponseEntity<MallCouponVO> getCouponDetail(
            @ApiParam("优惠券ID") @PathVariable Long id) {
        try {
            MallCouponVO couponVO = mallCouponService.selectCouponById(id);
            return ServerResponseEntity.success(couponVO);
        } catch (Exception e) {
            log.error("查询优惠券详情失败，ID: {}", id, e);
            return ServerResponseEntity.fail(MallResponseEnum.COUPON_NOT_EXIST.getCode(), MallResponseEnum.COUPON_NOT_EXIST.getMessage());
        }
    }

    /**
     * 查询用户优惠券详情
     */
    @ApiOperation("查询用户优惠券详情")
    @GetMapping("/user-coupon/{id}")
    public ServerResponseEntity<MallUserCouponVO> getUserCouponDetail(
            @ApiParam("用户优惠券ID") @PathVariable Long id) {
        try {
            MallUserCouponVO userCouponVO = mallUserCouponService.selectUserCouponById(id);
            return ServerResponseEntity.success(userCouponVO);
        } catch (Exception e) {
            log.error("查询用户优惠券详情失败，ID: {}", id, e);
            return ServerResponseEntity.fail(MallResponseEnum.COUPON_NOT_EXIST.getCode(), MallResponseEnum.COUPON_NOT_EXIST.getMessage());
        }
    }

    /**
     * 查询用户已领取的优惠券数量
     */
    @ApiOperation("查询用户已领取的优惠券数量")
    @GetMapping("/user/{userId}/received-count")
    public ServerResponseEntity<Integer> getUserReceivedCount(
            @ApiParam("用户ID") @PathVariable Long userId,
            @ApiParam("优惠券ID") @RequestParam Long couponId) {
        try {
            Integer count = mallUserCouponService.countUserReceivedCoupons(userId, couponId);
            return ServerResponseEntity.success(count);
        } catch (Exception e) {
            log.error("查询用户已领取优惠券数量失败，userId: {}, couponId: {}", userId, couponId, e);
            return ServerResponseEntity.fail(MallResponseEnum.COUPON_NOT_EXIST.getCode(), MallResponseEnum.COUPON_NOT_EXIST.getMessage());
        }
    }

    /**
     * 根据订单ID查询使用的优惠券
     */
    @ApiOperation("根据订单ID查询使用的优惠券")
    @GetMapping("/order/{orderId}/used-coupon")
    public ServerResponseEntity<MallUserCouponVO> getUsedCouponByOrderId(
            @ApiParam("订单ID") @PathVariable Long orderId) {
        try {
            MallUserCouponVO userCouponVO = mallUserCouponService.selectUsedCouponByOrderId(orderId);
            return ServerResponseEntity.success(userCouponVO);
        } catch (Exception e) {
            log.error("查询订单使用的优惠券失败，orderId: {}", orderId, e);
            return ServerResponseEntity.fail(MallResponseEnum.COUPON_NOT_EXIST.getCode(), MallResponseEnum.COUPON_NOT_EXIST.getMessage());
        }
    }

    /**
     * 取消使用优惠券（订单取消时调用）
     */
    @ApiOperation("取消使用优惠券")
    @PostMapping("/cancel-use/{orderId}")
    public ServerResponseEntity<String> cancelUseCoupon(
            @ApiParam("订单ID") @PathVariable Long orderId) {
        try {
            boolean result = mallUserCouponService.cancelUseCoupon(orderId);
            if (result) {
                return ServerResponseEntity.success("取消使用优惠券成功");
            }
            return ServerResponseEntity.fail(MallResponseEnum.COUPON_USE_FAIL.getCode(), MallResponseEnum.COUPON_USE_FAIL.getMessage());
        } catch (Exception e) {
            log.error("取消使用优惠券失败，orderId: {}", orderId, e);
            return ServerResponseEntity.fail(MallResponseEnum.COUPON_USE_FAIL.getCode(), MallResponseEnum.COUPON_USE_FAIL.getMessage());
        }
    }

}