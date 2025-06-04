package com.chennian.storytelling.service.mall.Impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.chennian.storytelling.bean.bo.MallCouponSearchBo;
import com.chennian.storytelling.bean.dto.MallCouponDto;
import com.chennian.storytelling.bean.enums.CouponStatusEnum;
import com.chennian.storytelling.bean.enums.CouponTypeEnum;
import com.chennian.storytelling.bean.enums.UserCouponStatusEnum;
import com.chennian.storytelling.bean.model.mall.MallCoupon;
import com.chennian.storytelling.bean.model.mall.MallUserCoupon;
import com.chennian.storytelling.bean.vo.MallCouponStatisticsVO;
import com.chennian.storytelling.bean.vo.MallCouponVO;
import com.chennian.storytelling.dao.MallCouponMapper;
import com.chennian.storytelling.dao.MallUserCouponMapper;
import com.chennian.storytelling.service.mall.MallCouponService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

/**
 * 商城优惠券Service实现类
 * 
 * @author chennian
 * @date 2025-01-27
 */
@Slf4j
@Service
public class MallCouponServiceImpl extends ServiceImpl<MallCouponMapper, MallCoupon> implements MallCouponService {
    
    @Autowired
    private MallCouponMapper mallCouponMapper;
    
    @Autowired
    private MallUserCouponMapper mallUserCouponMapper;
    
    @Override
    public IPage<MallCouponVO> selectCouponPage(MallCouponSearchBo searchBo) {
        Page<MallCouponVO> page = new Page<>(searchBo.getPageNum(), searchBo.getPageSize());
        return mallCouponMapper.selectCouponPage(page, searchBo);
    }
    
    @Override
    public MallCouponVO selectCouponById(Long couponId) {
        return mallCouponMapper.selectCouponById(couponId);
    }
    
    @Override
    @Transactional(rollbackFor = Exception.class)
    public Boolean createCoupon(MallCouponDto couponDto) {
        try {
            MallCoupon mallCoupon = new MallCoupon();
            BeanUtils.copyProperties(couponDto, mallCoupon);
            
            // 设置默认值
            mallCoupon.setStatus(CouponStatusEnum.UNPUBLISHED.getCode());
            mallCoupon.setCreateTime(LocalDateTime.now());
            mallCoupon.setUpdateTime(LocalDateTime.now());
            
            // 验证优惠券数据
            if (!validateCouponData(mallCoupon)) {
                return false;
            }
            
            return this.save(mallCoupon);
        } catch (Exception e) {
            log.error("创建优惠券失败", e);
            throw new RuntimeException("创建优惠券失败");
        }
    }
    
    @Override
    @Transactional(rollbackFor = Exception.class)
    public Boolean updateCoupon(MallCouponDto couponDto) {
        try {
            MallCoupon mallCoupon = new MallCoupon();
            BeanUtils.copyProperties(couponDto, mallCoupon);
            mallCoupon.setUpdateTime(LocalDateTime.now());
            
            // 验证优惠券数据
            if (!validateCouponData(mallCoupon)) {
                return false;
            }
            
            return this.updateById(mallCoupon);
        } catch (Exception e) {
            log.error("更新优惠券失败，couponId: {}", couponDto.getId(), e);
            throw new RuntimeException("更新优惠券失败");
        }
    }
    
    @Override
    @Transactional(rollbackFor = Exception.class)
    public Boolean deleteCoupon(Long couponId) {
        try {
            // 检查是否有用户已领取
            LambdaQueryWrapper<MallUserCoupon> queryWrapper = new LambdaQueryWrapper<>();
            queryWrapper.eq(MallUserCoupon::getCouponId, couponId);
            long count = mallUserCouponMapper.selectCount(queryWrapper);
            
            if (count > 0) {
                log.warn("优惠券已被用户领取，无法删除，couponId: {}", couponId);
                return false;
            }
            
            return this.removeById(couponId);
        } catch (Exception e) {
            log.error("删除优惠券失败，couponId: {}", couponId, e);
            throw new RuntimeException("删除优惠券失败");
        }
    }
    
    @Override
    @Transactional(rollbackFor = Exception.class)
    public Boolean publishCoupon(Long couponId) {
        try {
            LambdaUpdateWrapper<MallCoupon> updateWrapper = new LambdaUpdateWrapper<>();
            updateWrapper.eq(MallCoupon::getId, couponId)
                        .set(MallCoupon::getStatus, CouponStatusEnum.PUBLISHED.getCode())
                        .set(MallCoupon::getUpdateTime, LocalDateTime.now());
            
            return this.update(updateWrapper);
        } catch (Exception e) {
            log.error("发布优惠券失败，couponId: {}", couponId, e);
            throw new RuntimeException("发布优惠券失败");
        }
    }
    
    @Override
    @Transactional(rollbackFor = Exception.class)
    public Boolean disableCoupon(Long couponId) {
        try {
            LambdaUpdateWrapper<MallCoupon> updateWrapper = new LambdaUpdateWrapper<>();
            updateWrapper.eq(MallCoupon::getId, couponId)
                        .set(MallCoupon::getStatus, CouponStatusEnum.DISABLED.getCode())
                        .set(MallCoupon::getUpdateTime, LocalDateTime.now());
            
            return this.update(updateWrapper);
        } catch (Exception e) {
            log.error("停用优惠券失败，couponId: {}", couponId, e);
            throw new RuntimeException("停用优惠券失败");
        }
    }
    
    @Override
    public List<MallCouponVO> selectAvailableCoupons(Long userId) {
        return mallCouponMapper.selectAvailableCoupons(userId);
    }
    
    @Override
    @Transactional(rollbackFor = Exception.class)
    public Boolean receiveCoupon(Long userId, Long couponId) {
        try {
            // 查询优惠券信息
            MallCoupon coupon = this.getById(couponId);
            if (coupon == null) {
                log.warn("优惠券不存在，couponId: {}", couponId);
                return false;
            }
            
            // 检查优惠券状态
            if (!CouponStatusEnum.PUBLISHED.getCode().equals(coupon.getStatus())) {
                log.warn("优惠券未发布，无法领取，couponId: {}", couponId);
                return false;
            }
            
            // 检查有效期
            LocalDateTime now = LocalDateTime.now();
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
                Integer receivedCount = mallUserCouponMapper.countUserReceivedCoupons(userId, couponId);
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
            
            return mallUserCouponMapper.insert(userCoupon) > 0;
        } catch (Exception e) {
            log.error("领取优惠券失败，userId: {}, couponId: {}", userId, couponId, e);
            throw new RuntimeException("领取优惠券失败");
        }
    }
    
    @Override
    public List<MallCouponVO> selectUsableCouponsForOrder(Long userId, BigDecimal orderAmount) {
        return mallCouponMapper.selectUsableCouponsForOrder(userId, orderAmount);
    }
    
    @Override
    public BigDecimal calculateDiscountAmount(Long couponId, BigDecimal orderAmount) {
        MallCoupon coupon = this.getById(couponId);
        if (coupon == null) {
            return BigDecimal.ZERO;
        }
        
        return calculateDiscountAmount(coupon, orderAmount);
    }
    
    @Override
    public Boolean validateCouponUsable(Long userCouponId, BigDecimal orderAmount) {
        MallUserCoupon userCoupon = mallUserCouponMapper.selectById(userCouponId);
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
        MallCoupon coupon = this.getById(userCoupon.getCouponId());
        if (coupon != null && coupon.getMinAmount() != null) {
            return orderAmount.compareTo(coupon.getMinAmount()) >= 0;
        }
        
        return true;
    }
    
    @Override
    public MallCouponStatisticsVO getCouponStatistics() {
        return mallCouponMapper.getCouponStatistics();
    }
    
    @Override
    @Transactional(rollbackFor = Exception.class)
    public void checkAndUpdateExpiredCoupons() {
        try {
            List<Long> expiredCouponIds = mallCouponMapper.selectExpiredCouponIds();
            if (!expiredCouponIds.isEmpty()) {
                LambdaUpdateWrapper<MallCoupon> updateWrapper = new LambdaUpdateWrapper<>();
                updateWrapper.in(MallCoupon::getId, expiredCouponIds)
                            .set(MallCoupon::getStatus, CouponStatusEnum.DISABLED.getCode())
                            .set(MallCoupon::getUpdateTime, LocalDateTime.now());
                
                this.update(updateWrapper);
                log.info("更新过期优惠券状态，数量: {}", expiredCouponIds.size());
            }
        } catch (Exception e) {
            log.error("检查并更新过期优惠券失败", e);
        }
    }
    
    /**
     * 验证优惠券数据
     */
    private Boolean validateCouponData(MallCoupon coupon) {
        // 验证优惠券类型
        if (CouponTypeEnum.getByCode(coupon.getType()) == null) {
            log.warn("无效的优惠券类型: {}", coupon.getType());
            return false;
        }
        
        // 验证时间
        if (coupon.getValidStartTime().isAfter(coupon.getValidEndTime())) {
            log.warn("优惠券开始时间不能晚于结束时间");
            return false;
        }
        
        // 验证折扣券的折扣值
        if (CouponTypeEnum.DISCOUNT.getCode().equals(coupon.getType())) {
            if (coupon.getDiscountRate() == null || coupon.getDiscountRate().compareTo(BigDecimal.ZERO) <= 0
                || coupon.getDiscountRate().compareTo(BigDecimal.ONE) >= 0) {
                log.warn("折扣券的折扣值必须在0-1之间");
                return false;
            }
        }
        
        // 验证满减券和固定金额券的金额
        if (CouponTypeEnum.FULL_REDUCTION.getCode().equals(coupon.getType()) 
            || CouponTypeEnum.FIXED_AMOUNT.getCode().equals(coupon.getType())) {
            if (coupon.getDiscountAmount() == null || coupon.getDiscountAmount().compareTo(BigDecimal.ZERO) <= 0) {
                log.warn("满减券和固定金额券的金额必须大于0");
                return false;
            }
        }
        
        return true;
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
        return "CPN" + System.currentTimeMillis() + UUID.randomUUID().toString().substring(0, 8).toUpperCase();
    }
    
}