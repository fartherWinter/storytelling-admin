package com.chennian.storytelling.service.mall;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.IService;
import com.chennian.storytelling.bean.dto.MallCouponUseDto;
import com.chennian.storytelling.bean.model.mall.MallUserCoupon;
import com.chennian.storytelling.bean.vo.MallUserCouponVO;

import java.math.BigDecimal;
import java.util.List;

/**
 * 商城用户优惠券Service接口
 * 
 * @author chennian
 * @date 2025-01-27
 */
public interface MallUserCouponService extends IService<MallUserCoupon> {
    
    /**
     * 分页查询用户优惠券列表
     * 
     * @param userId 用户ID
     * @param status 状态（可选）
     * @param pageNum 页码
     * @param pageSize 每页大小
     * @return 用户优惠券分页列表
     */
    IPage<MallUserCouponVO> selectUserCouponPage(Long userId, Integer status, Integer pageNum, Integer pageSize);
    
    /**
     * 根据ID查询用户优惠券详情
     * 
     * @param userCouponId 用户优惠券ID
     * @return 用户优惠券详情
     */
    MallUserCouponVO selectUserCouponById(Long userCouponId);
    
    /**
     * 用户领取优惠券
     * 
     * @param userId 用户ID
     * @param couponId 优惠券ID
     * @return 结果
     */
    Boolean receiveCoupon(Long userId, Long couponId);
    
    /**
     * 使用优惠券
     * 
     * @param useDto 使用优惠券信息
     * @return 结果
     */
    Boolean useCoupon(MallCouponUseDto useDto);
    
    /**
     * 查询用户可用于指定订单的优惠券
     * 
     * @param userId 用户ID
     * @param orderAmount 订单金额
     * @return 可用优惠券列表
     */
    List<MallUserCouponVO> selectUsableCouponsForOrder(Long userId, BigDecimal orderAmount);
    
    /**
     * 计算优惠券优惠金额
     * 
     * @param userCouponId 用户优惠券ID
     * @param orderAmount 订单金额
     * @return 优惠金额
     */
    BigDecimal calculateDiscountAmount(Long userCouponId, BigDecimal orderAmount);
    
    /**
     * 验证优惠券是否可用
     * 
     * @param userCouponId 用户优惠券ID
     * @param orderAmount 订单金额
     * @return 是否可用
     */
    Boolean validateCouponUsable(Long userCouponId, BigDecimal orderAmount);
    
    /**
     * 查询用户已领取的优惠券数量
     * 
     * @param userId 用户ID
     * @param couponId 优惠券ID
     * @return 已领取数量
     */
    Integer countUserReceivedCoupons(Long userId, Long couponId);
    
    /**
     * 检查并更新过期的用户优惠券
     */
    void checkAndUpdateExpiredUserCoupons();
    
    /**
     * 根据订单ID查询使用的优惠券
     * 
     * @param orderId 订单ID
     * @return 使用的优惠券
     */
    MallUserCouponVO selectUsedCouponByOrderId(Long orderId);
    
    /**
     * 取消使用优惠券（订单取消时调用）
     * 
     * @param orderId 订单ID
     * @return 结果
     */
    Boolean cancelUseCoupon(Long orderId);
    
}