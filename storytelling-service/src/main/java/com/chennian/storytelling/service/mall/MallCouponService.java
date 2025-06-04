package com.chennian.storytelling.service.mall;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.IService;
import com.chennian.storytelling.bean.bo.MallCouponSearchBo;
import com.chennian.storytelling.bean.dto.MallCouponDto;
import com.chennian.storytelling.bean.model.mall.MallCoupon;
import com.chennian.storytelling.bean.vo.MallCouponStatisticsVO;
import com.chennian.storytelling.bean.vo.MallCouponVO;

import java.math.BigDecimal;
import java.util.List;

/**
 * 商城优惠券Service接口
 * 
 * @author chennian
 * @date 2025-01-27
 */
public interface MallCouponService extends IService<MallCoupon> {
    
    /**
     * 分页查询优惠券列表
     * 
     * @param searchBo 搜索条件
     * @return 优惠券分页列表
     */
    IPage<MallCouponVO> selectCouponPage(MallCouponSearchBo searchBo);
    
    /**
     * 根据ID查询优惠券详情
     * 
     * @param couponId 优惠券ID
     * @return 优惠券详情
     */
    MallCouponVO selectCouponById(Long couponId);
    
    /**
     * 创建优惠券
     * 
     * @param couponDto 优惠券信息
     * @return 结果
     */
    Boolean createCoupon(MallCouponDto couponDto);
    
    /**
     * 更新优惠券
     * 
     * @param couponDto 优惠券信息
     * @return 结果
     */
    Boolean updateCoupon(MallCouponDto couponDto);
    
    /**
     * 删除优惠券
     * 
     * @param couponId 优惠券ID
     * @return 结果
     */
    Boolean deleteCoupon(Long couponId);
    
    /**
     * 发布优惠券
     * 
     * @param couponId 优惠券ID
     * @return 结果
     */
    Boolean publishCoupon(Long couponId);
    
    /**
     * 停用优惠券
     * 
     * @param couponId 优惠券ID
     * @return 结果
     */
    Boolean disableCoupon(Long couponId);
    
    /**
     * 查询可领取的优惠券列表
     * 
     * @param userId 用户ID
     * @return 可领取的优惠券列表
     */
    List<MallCouponVO> selectAvailableCoupons(Long userId);
    
    /**
     * 领取优惠券
     * 
     * @param userId 用户ID
     * @param couponId 优惠券ID
     * @return 结果
     */
    Boolean receiveCoupon(Long userId, Long couponId);
    
    /**
     * 查询用户可用于指定订单的优惠券
     * 
     * @param userId 用户ID
     * @param orderAmount 订单金额
     * @return 可用优惠券列表
     */
    List<MallCouponVO> selectUsableCouponsForOrder(Long userId, BigDecimal orderAmount);
    
    /**
     * 计算优惠券优惠金额
     * 
     * @param couponId 优惠券ID
     * @param orderAmount 订单金额
     * @return 优惠金额
     */
    BigDecimal calculateDiscountAmount(Long couponId, BigDecimal orderAmount);
    
    /**
     * 验证优惠券是否可用
     * 
     * @param userCouponId 用户优惠券ID
     * @param orderAmount 订单金额
     * @return 是否可用
     */
    Boolean validateCouponUsable(Long userCouponId, BigDecimal orderAmount);
    
    /**
     * 查询优惠券统计信息
     * 
     * @return 统计信息
     */
    MallCouponStatisticsVO getCouponStatistics();
    
    /**
     * 检查优惠券是否过期并更新状态
     */
    void checkAndUpdateExpiredCoupons();
    
}