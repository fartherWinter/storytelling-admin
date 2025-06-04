package com.chennian.storytelling.service.mall;

import com.baomidou.mybatisplus.extension.service.IService;
import com.chennian.storytelling.bean.model.mall.MallCart;

import java.util.List;

/**
 * 商城购物车Service接口
 * 
 * @author chennian
 * @date 2025-01-27
 */
public interface MallCartService extends IService<MallCart> {
    
    /**
     * 根据用户ID查询购物车列表
     * 
     * @param userId 用户ID
     * @return 购物车列表
     */
    List<MallCart> selectCartByUserId(Long userId);
    
    /**
     * 根据用户ID查询选中的购物车商品
     * 
     * @param userId 用户ID
     * @return 选中的购物车列表
     */
    List<MallCart> selectSelectedCartByUserId(Long userId);
    
    /**
     * 添加商品到购物车
     * 
     * @param mallCart 购物车信息
     * @return 结果
     */
    Boolean addToCart(MallCart mallCart);
    
    /**
     * 更新购物车商品数量
     * 
     * @param cartId 购物车ID
     * @param quantity 商品数量
     * @return 结果
     */
    int updateCartQuantity(Long cartId, Integer quantity);
    
    /**
     * 更新购物车商品选中状态
     * 
     * @param cartId 购物车ID
     * @param isSelected 是否选中
     * @return 结果
     */
    int updateCartSelected(Long cartId, Integer isSelected);
    
    /**
     * 批量更新购物车商品选中状态
     * 
     * @param userId 用户ID
     * @param cartIds 购物车ID列表
     * @param isSelected 是否选中
     * @return 结果
     */
    int batchUpdateCartSelected(Long userId, List<Long> cartIds, Integer isSelected);
    
    /**
     * 全选/取消全选购物车商品
     * 
     * @param userId 用户ID
     * @param isSelected 是否选中
     * @return 结果
     */
    int selectAllCart(Long userId, Integer isSelected);
    
    /**
     * 删除购物车商品
     * 
     * @param cartId 购物车ID
     * @return 结果
     */
    int deleteCartItem(Long cartId);
    
    /**
     * 批量删除购物车商品
     * 
     * @param userId 用户ID
     * @param cartIds 购物车ID列表
     * @return 结果
     */
    int batchDeleteCartItems(Long userId, List<Long> cartIds);
    
    /**
     * 清空用户购物车
     * 
     * @param userId 用户ID
     * @return 结果
     */
    int clearCart(Long userId);
    
    /**
     * 清空用户选中的购物车商品
     * 
     * @param userId 用户ID
     * @return 结果
     */
    int clearSelectedCart(Long userId);
    
    /**
     * 检查商品是否已在购物车中
     * 
     * @param userId 用户ID
     * @param productId 商品ID
     * @param specification 商品规格
     * @return 购物车商品
     */
    MallCart checkProductInCart(Long userId, Long productId, String specification);
    
    /**
     * 获取用户购物车商品总数量
     * 
     * @param userId 用户ID
     * @return 商品总数量
     */
    Integer getCartItemCount(Long userId);
    
    /**
     * 获取用户选中购物车商品总数量
     * 
     * @param userId 用户ID
     * @return 选中商品总数量
     */
    Integer getSelectedCartItemCount(Long userId);
}