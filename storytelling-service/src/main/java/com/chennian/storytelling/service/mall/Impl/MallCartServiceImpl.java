package com.chennian.storytelling.service.mall.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.chennian.storytelling.bean.model.mall.MallCart;
import com.chennian.storytelling.dao.MallCartMapper;
import com.chennian.storytelling.service.mall.MallCartService;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.time.LocalDateTime;
import java.util.List;

/**
 * 商城购物车Service实现类
 * 
 * @author chennian
 * @date 2025-01-27
 */
@Service
public class MallCartServiceImpl extends ServiceImpl<MallCartMapper, MallCart> implements MallCartService {
    
    @Override
    public List<MallCart> selectCartByUserId(Long userId) {
        LambdaQueryWrapper<MallCart> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(MallCart::getUserId, userId)
                   .orderByDesc(MallCart::getCreateTime);
        return this.list(queryWrapper);
    }
    
    @Override
    public List<MallCart> selectSelectedCartByUserId(Long userId) {
        LambdaQueryWrapper<MallCart> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(MallCart::getUserId, userId)
                   .eq(MallCart::getIsSelected, 1)
                   .orderByDesc(MallCart::getCreateTime);
        return this.list(queryWrapper);
    }
    
    @Override
    public Boolean addToCart(MallCart mallCart) {
        // 检查商品是否已在购物车中
        MallCart existingCart = checkProductInCart(
            mallCart.getUserId(), 
            mallCart.getProductId(), 
            mallCart.getSpecification()
        );
        
        if (existingCart != null) {
            // 如果已存在，更新数量
            int newQuantity = existingCart.getQuantity() + mallCart.getQuantity();
            return updateCartQuantity(existingCart.getId(), newQuantity) > 0;
        } else {
            // 如果不存在，新增
            mallCart.setCreateTime(LocalDateTime.now());
            mallCart.setUpdateTime(LocalDateTime.now());
            // 默认选中
            mallCart.setIsSelected(1);
            return this.save(mallCart);
        }
    }
    
    @Override
    public int updateCartQuantity(Long cartId, Integer quantity) {
        LambdaUpdateWrapper<MallCart> updateWrapper = new LambdaUpdateWrapper<>();
        updateWrapper.eq(MallCart::getId, cartId)
                    .set(MallCart::getQuantity, quantity)
                    .set(MallCart::getUpdateTime, LocalDateTime.now());
        return this.update(updateWrapper) ? 1 : 0;
    }
    
    @Override
    public int updateCartSelected(Long cartId, Integer isSelected) {
        LambdaUpdateWrapper<MallCart> updateWrapper = new LambdaUpdateWrapper<>();
        updateWrapper.eq(MallCart::getId, cartId)
                    .set(MallCart::getIsSelected, isSelected)
                    .set(MallCart::getUpdateTime, LocalDateTime.now());
        return this.update(updateWrapper) ? 1 : 0;
    }
    
    @Override
    public int batchUpdateCartSelected(Long userId, List<Long> cartIds, Integer isSelected) {
        if (cartIds == null || cartIds.isEmpty()) {
            return 0;
        }
        
        LambdaUpdateWrapper<MallCart> updateWrapper = new LambdaUpdateWrapper<>();
        updateWrapper.eq(MallCart::getUserId, userId)
                    .in(MallCart::getId, cartIds)
                    .set(MallCart::getIsSelected, isSelected)
                    .set(MallCart::getUpdateTime, LocalDateTime.now());
        return this.update(updateWrapper) ? cartIds.size() : 0;
    }
    
    @Override
    public int selectAllCart(Long userId, Integer isSelected) {
        LambdaUpdateWrapper<MallCart> updateWrapper = new LambdaUpdateWrapper<>();
        updateWrapper.eq(MallCart::getUserId, userId)
                    .set(MallCart::getIsSelected, isSelected)
                    .set(MallCart::getUpdateTime, LocalDateTime.now());
        return this.update(updateWrapper) ? 1 : 0;
    }
    
    @Override
    public int deleteCartItem(Long cartId) {
        return this.removeById(cartId) ? 1 : 0;
    }
    
    @Override
    public int batchDeleteCartItems(Long userId, List<Long> cartIds) {
        if (cartIds == null || cartIds.isEmpty()) {
            return 0;
        }
        
        LambdaQueryWrapper<MallCart> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(MallCart::getUserId, userId)
                   .in(MallCart::getId, cartIds);
        return this.remove(queryWrapper) ? cartIds.size() : 0;
    }
    
    @Override
    public int clearCart(Long userId) {
        LambdaQueryWrapper<MallCart> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(MallCart::getUserId, userId);
        return this.remove(queryWrapper) ? 1 : 0;
    }
    
    @Override
    public int clearSelectedCart(Long userId) {
        LambdaQueryWrapper<MallCart> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(MallCart::getUserId, userId)
                   .eq(MallCart::getIsSelected, 1);
        return this.remove(queryWrapper) ? 1 : 0;
    }
    
    @Override
    public MallCart checkProductInCart(Long userId, Long productId, String specification) {
        LambdaQueryWrapper<MallCart> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(MallCart::getUserId, userId)
                   .eq(MallCart::getProductId, productId);
        
        // 如果规格为空，查询规格也为空的记录
        if (!StringUtils.hasText(specification)) {
            queryWrapper.and(wrapper -> wrapper.isNull(MallCart::getSpecification)
                                              .or()
                                              .eq(MallCart::getSpecification, ""));
        } else {
            queryWrapper.eq(MallCart::getSpecification, specification);
        }
        
        return this.getOne(queryWrapper);
    }
    
    @Override
    public Integer getCartItemCount(Long userId) {
        LambdaQueryWrapper<MallCart> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(MallCart::getUserId, userId)
                   .select(MallCart::getQuantity);
        
        List<MallCart> cartList = this.list(queryWrapper);
        return cartList.stream().mapToInt(MallCart::getQuantity).sum();
    }
    
    @Override
    public Integer getSelectedCartItemCount(Long userId) {
        LambdaQueryWrapper<MallCart> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(MallCart::getUserId, userId)
                   .eq(MallCart::getIsSelected, 1)
                   .select(MallCart::getQuantity);
        
        List<MallCart> cartList = this.list(queryWrapper);
        return cartList.stream().mapToInt(MallCart::getQuantity).sum();
    }
}