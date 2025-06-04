package com.chennian.storytelling.service.mall.Impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.chennian.storytelling.bean.dto.MallCouponUseDto;
import com.chennian.storytelling.bean.enums.CouponTypeEnum;
import com.chennian.storytelling.bean.enums.UserCouponStatusEnum;
import com.chennian.storytelling.bean.model.mall.MallCoupon;
import com.chennian.storytelling.bean.model.mall.MallUserCoupon;
import com.chennian.storytelling.bean.vo.MallUserCouponVO;
import com.chennian.storytelling.dao.MallCouponMapper;
import com.chennian.storytelling.dao.MallUserCouponMapper;
import com.chennian.storytelling.service.mall.MallUserCouponService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDateTime;
import java.util.List;

/**
 * 商城用户优惠券Service实现类
 * 
 * @author chennian
 * @date 2025-01-27
 */
@Slf4j
@Service
public class MallUserCouponServiceImpl extends ServiceImpl<MallUserCouponMapper, MallUserCoupon> implements MallUserCouponService {
    
    @Autowired
    private MallUserCouponMapper mallUserCouponMapper;
    
    @Autowired
    private MallCouponMapper mallCouponMapper;
    
    @Override
    public IPage<MallUserCouponVO> selectUserCouponPage(Long userId, Integer status, Integer pageNum, Integer pageSize) {
        Page<MallUserCouponVO> page = new Page<>(pageNum, pageSize);
        return mallUserCouponMapper.selectUserCouponPage(page, userId, status);
    }
    
    @Override
    public MallUserCouponVO selectUserCouponById(Long userCouponId) {
        return mallUserCouponMapper.selectUserCouponById(userCouponId);
    }
    
    @Override
    @Transactional(rollbackFor = Exception.class)
    public Boolean receiveCoupon(Long userId, Long couponId) {
        try {
            // 查询优惠券信息
            MallCoupon coupon = mallCouponMapper.selectById(couponId);
            if (coupon == null) {
                log.warn("优惠券不存在，couponId: {}", couponId);
                return false;
            }
            
            // 检查优惠券状态和有效期
            LocalDateTime now = LocalDateTime.now();
            if (coupon.getStatus() != 1) {
                log.warn("优惠券未发布，无法领取，couponId: {}", couponId);
                return false;
            }
            
            if (now.isBefore(coupon.getValidStartTime()) || now.isAfter(coupon.getValidEndTime())) {
                log.warn("优惠券不在有效期内，couponId: {}", couponId);
                return false;
            }
            
            // 检查库存
            if (coupon.getTotalCount()-coupon.getReceivedCount() <= 0) {
                log.warn("优惠券库存不足，couponId: {}", couponId);
                return false;
            }
            
            // 检查用户领取限制
            if (coupon.getLimitPerUser() != null && coupon.getLimitPerUser() > 0) {
                Integer receivedCount = this.countUserReceivedCoupons(userId, couponId);
                if (receivedCount >= coupon.getLimitPerUser()) {
                    log.warn("用户已达到领取限制，userId: {}, couponId: {}", userId, couponId);
                    return false;
                }
            }
            
            // 减少优惠券库存
            int updateResult = mallCouponMapper.updateCouponStock(couponId, 1);
            if (updateResult <= 0) {
                log.warn("更新优惠券库存失败，couponId: {}", couponId);
                return false;
            }
            
            // 创建用户优惠券记录
            MallUserCoupon userCoupon = new MallUserCoupon();
            userCoupon.setUserId(userId);
            userCoupon.setCouponId(couponId);
            userCoupon.setCouponCode(generateCouponCode());
            userCoupon.setStatus(UserCouponStatusEnum.UNUSED.getCode());
            userCoupon.setReceiveTime(now);
            userCoupon.setExpireTime(coupon.getValidEndTime());
            userCoupon.setCreateTime(now);
            userCoupon.setUpdateTime(now);
            
            return this.save(userCoupon);
        } catch (Exception e) {
            log.error("领取优惠券失败，userId: {}, couponId: {}", userId, couponId, e);
            throw new RuntimeException("领取优惠券失败");
        }
    }
    
    @Override
    @Transactional(rollbackFor = Exception.class)
    public Boolean useCoupon(MallCouponUseDto useDto) {
        try {
            // 查询用户优惠券
            MallUserCoupon userCoupon = this.getById(useDto.getUserCouponId());
            if (userCoupon == null) {
                log.warn("用户优惠券不存在，userCouponId: {}", useDto.getUserCouponId());
                return false;
            }
            
            // 验证用户ID
            if (!userCoupon.getUserId().equals(useDto.getUserId())) {
                log.warn("用户优惠券不属于当前用户，userCouponId: {}, userId: {}", useDto.getUserCouponId(), useDto.getUserId());
                return false;
            }
            
            // 验证优惠券是否可用
            if (!this.validateCouponUsable(useDto.getUserCouponId(), useDto.getOrderAmount())) {
                log.warn("优惠券不可用，userCouponId: {}", useDto.getUserCouponId());
                return false;
            }
            
            // 更新用户优惠券状态
            LambdaUpdateWrapper<MallUserCoupon> updateWrapper = new LambdaUpdateWrapper<>();
            updateWrapper.eq(MallUserCoupon::getId, useDto.getUserCouponId())
                        .eq(MallUserCoupon::getStatus, UserCouponStatusEnum.UNUSED.getCode())
                        .set(MallUserCoupon::getStatus, UserCouponStatusEnum.USED.getCode())
                        .set(MallUserCoupon::getUseTime, LocalDateTime.now())
                        .set(MallUserCoupon::getOrderId, useDto.getOrderId())
                        .set(MallUserCoupon::getUpdateTime, LocalDateTime.now());
            
            return this.update(updateWrapper);
        } catch (Exception e) {
            log.error("使用优惠券失败，useDto: {}", useDto, e);
            throw new RuntimeException("使用优惠券失败");
        }
    }
    
    @Override
    public List<MallUserCouponVO> selectUsableCouponsForOrder(Long userId, BigDecimal orderAmount) {
        return mallUserCouponMapper.selectUsableCouponsForOrder(userId, orderAmount);
    }
    
    @Override
    public BigDecimal calculateDiscountAmount(Long userCouponId, BigDecimal orderAmount) {
        MallUserCoupon userCoupon = this.getById(userCouponId);
        if (userCoupon == null) {
            return BigDecimal.ZERO;
        }
        
        MallCoupon coupon = mallCouponMapper.selectById(userCoupon.getCouponId());
        if (coupon == null) {
            return BigDecimal.ZERO;
        }
        
        return calculateDiscountAmount(coupon, orderAmount);
    }
    
    @Override
    public Boolean validateCouponUsable(Long userCouponId, BigDecimal orderAmount) {
        MallUserCoupon userCoupon = this.getById(userCouponId);
        if (userCoupon == null) {
            return false;
        }
        
        // 检查状态
        if (!UserCouponStatusEnum.UNUSED.getCode().equals(userCoupon.getStatus())) {
            return false;
        }
        
        // 检查过期时间
        if (LocalDateTime.now().isAfter(userCoupon.getExpireTime())) {
            return false;
        }
        
        // 检查使用门槛
        MallCoupon coupon = mallCouponMapper.selectById(userCoupon.getCouponId());
        if (coupon != null && coupon.getMinAmount() != null) {
            return orderAmount.compareTo(coupon.getMinAmount()) >= 0;
        }
        
        return true;
    }
    
    @Override
    public Integer countUserReceivedCoupons(Long userId, Long couponId) {
        return mallUserCouponMapper.countUserReceivedCoupons(userId, couponId);
    }
    
    @Override
    @Transactional(rollbackFor = Exception.class)
    public void checkAndUpdateExpiredUserCoupons() {
        try {
            List<Long> expiredUserCouponIds = mallUserCouponMapper.selectExpiredUserCouponIds();
            if (!expiredUserCouponIds.isEmpty()) {
                LambdaUpdateWrapper<MallUserCoupon> updateWrapper = new LambdaUpdateWrapper<>();
                updateWrapper.in(MallUserCoupon::getId, expiredUserCouponIds)
                            .set(MallUserCoupon::getStatus, UserCouponStatusEnum.EXPIRED.getCode())
                            .set(MallUserCoupon::getUpdateTime, LocalDateTime.now());
                
                this.update(updateWrapper);
                log.info("更新过期用户优惠券状态，数量: {}", expiredUserCouponIds.size());
            }
        } catch (Exception e) {
            log.error("检查并更新过期用户优惠券失败", e);
        }
    }
    
    @Override
    public MallUserCouponVO selectUsedCouponByOrderId(Long orderId) {
        return mallUserCouponMapper.selectUsedCouponByOrderId(orderId);
    }
    
    @Override
    @Transactional(rollbackFor = Exception.class)
    public Boolean cancelUseCoupon(Long orderId) {
        try {
            LambdaUpdateWrapper<MallUserCoupon> updateWrapper = new LambdaUpdateWrapper<>();
            updateWrapper.eq(MallUserCoupon::getOrderId, orderId)
                        .eq(MallUserCoupon::getStatus, UserCouponStatusEnum.USED.getCode())
                        .set(MallUserCoupon::getStatus, UserCouponStatusEnum.UNUSED.getCode())
                        .set(MallUserCoupon::getUseTime, null)
                        .set(MallUserCoupon::getOrderId, null)
                        .set(MallUserCoupon::getUpdateTime, LocalDateTime.now());
            
            return this.update(updateWrapper);
        } catch (Exception e) {
            log.error("取消使用优惠券失败，orderId: {}", orderId, e);
            throw new RuntimeException("取消使用优惠券失败");
        }
    }
    
    /**
     * 计算优惠金额
     */
    private BigDecimal calculateDiscountAmount(MallCoupon coupon, BigDecimal orderAmount) {
        if (coupon.getMinAmount() != null && orderAmount.compareTo(coupon.getMinAmount()) < 0) {
            return BigDecimal.ZERO;
        }
        
        BigDecimal discountAmount = BigDecimal.ZERO;
        
        if (CouponTypeEnum.FULL_REDUCTION.getCode().equals(coupon.getType()) 
            || CouponTypeEnum.FIXED_AMOUNT.getCode().equals(coupon.getType())) {
            // 满减券和固定金额券
            discountAmount = coupon.getDiscountAmount();
        } else if (CouponTypeEnum.DISCOUNT.getCode().equals(coupon.getType())) {
            // 折扣券
            discountAmount = orderAmount.multiply(BigDecimal.ONE.subtract(coupon.getDiscountRate()))
                                      .setScale(2, RoundingMode.HALF_UP);
        }
        
        // 检查最大优惠金额限制
        if (coupon.getMaxDiscountAmount() != null && discountAmount.compareTo(coupon.getMaxDiscountAmount()) > 0) {
            discountAmount = coupon.getMaxDiscountAmount();
        }
        
        // 优惠金额不能超过订单金额
        if (discountAmount.compareTo(orderAmount) > 0) {
            discountAmount = orderAmount;
        }
        
        return discountAmount;
    }
    
    /**
     * 生成优惠券编码
     */
    private String generateCouponCode() {
        return "UC" + System.currentTimeMillis() + String.valueOf((int)(Math.random() * 10000));
    }
    
}