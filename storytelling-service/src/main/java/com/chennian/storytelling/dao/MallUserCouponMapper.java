package com.chennian.storytelling.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.chennian.storytelling.bean.model.mall.MallUserCoupon;
import com.chennian.storytelling.bean.vo.MallUserCouponVO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.math.BigDecimal;
import java.util.List;

/**
 * 商城用户优惠券Mapper接口
 * 
 * @author chennian
 * @date 2025-01-27
 */
@Mapper
public interface MallUserCouponMapper extends BaseMapper<MallUserCoupon> {
    
    /**
     * 分页查询用户优惠券列表
     * 
     * @param page 分页参数
     * @param userId 用户ID
     * @param status 状态
     * @return 用户优惠券分页列表
     */
    IPage<MallUserCouponVO> selectUserCouponPage(Page<MallUserCouponVO> page, @Param("userId") Long userId, @Param("status") Integer status);
    
    /**
     * 根据ID查询用户优惠券详情
     * 
     * @param userCouponId 用户优惠券ID
     * @return 用户优惠券详情
     */
    MallUserCouponVO selectUserCouponById(@Param("userCouponId") Long userCouponId);
    
    /**
     * 查询用户可用于指定订单的优惠券
     * 
     * @param userId 用户ID
     * @param orderAmount 订单金额
     * @return 可用优惠券列表
     */
    List<MallUserCouponVO> selectUsableCouponsForOrder(@Param("userId") Long userId, @Param("orderAmount") BigDecimal orderAmount);
    
    /**
     * 查询用户已领取的优惠券数量
     * 
     * @param userId 用户ID
     * @param couponId 优惠券ID
     * @return 已领取数量
     */
    Integer countUserReceivedCoupons(@Param("userId") Long userId, @Param("couponId") Long couponId);
    
    /**
     * 查询过期的用户优惠券ID列表
     * 
     * @return 过期的用户优惠券ID列表
     */
    List<Long> selectExpiredUserCouponIds();
    
    /**
     * 根据订单ID查询使用的优惠券
     * 
     * @param orderId 订单ID
     * @return 使用的优惠券
     */
    MallUserCouponVO selectUsedCouponByOrderId(@Param("orderId") Long orderId);
    
    /**
     * 根据优惠券编码查询用户优惠券
     * 
     * @param couponCode 优惠券编码
     * @param userId 用户ID
     * @return 用户优惠券
     */
    MallUserCoupon selectByCouponCodeAndUserId(@Param("couponCode") String couponCode, @Param("userId") Long userId);
    
}