package com.chennian.storytelling.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.chennian.storytelling.bean.bo.MallCouponSearchBo;
import com.chennian.storytelling.bean.model.mall.MallCoupon;
import com.chennian.storytelling.bean.vo.MallCouponStatisticsVO;
import com.chennian.storytelling.bean.vo.MallCouponVO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.math.BigDecimal;
import java.util.List;

/**
 * 商城优惠券Mapper接口
 * 
 * @author chennian
 * @date 2025-01-27
 */
@Mapper
public interface MallCouponMapper extends BaseMapper<MallCoupon> {
    
    /**
     * 分页查询优惠券列表
     * 
     * @param page 分页参数
     * @param searchBo 搜索条件
     * @return 优惠券分页列表
     */
    IPage<MallCouponVO> selectCouponPage(Page<MallCouponVO> page, @Param("searchBo") MallCouponSearchBo searchBo);
    
    /**
     * 根据ID查询优惠券详情
     * 
     * @param couponId 优惠券ID
     * @return 优惠券详情
     */
    MallCouponVO selectCouponById(@Param("couponId") Long couponId);
    
    /**
     * 查询可领取的优惠券列表
     * 
     * @param userId 用户ID
     * @return 可领取的优惠券列表
     */
    List<MallCouponVO> selectAvailableCoupons(@Param("userId") Long userId);
    
    /**
     * 查询用户可用于指定订单的优惠券
     * 
     * @param userId 用户ID
     * @param orderAmount 订单金额
     * @return 可用优惠券列表
     */
    List<MallCouponVO> selectUsableCouponsForOrder(@Param("userId") Long userId, @Param("orderAmount") BigDecimal orderAmount);
    
    /**
     * 查询优惠券统计信息
     * 
     * @return 统计信息
     */
    MallCouponStatisticsVO getCouponStatistics();
    
    /**
     * 更新优惠券库存
     * 
     * @param couponId 优惠券ID
     * @param quantity 减少数量
     * @return 影响行数
     */
    int updateCouponStock(@Param("couponId") Long couponId, @Param("quantity") Integer quantity);
    
    /**
     * 查询过期的优惠券ID列表
     * 
     * @return 过期的优惠券ID列表
     */
    List<Long> selectExpiredCouponIds();
    
}