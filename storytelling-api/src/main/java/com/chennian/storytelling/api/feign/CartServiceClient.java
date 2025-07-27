package com.chennian.storytelling.api.feign;

import com.chennian.storytelling.bean.model.mall.MallCart;
import com.chennian.storytelling.common.response.ServerResponseEntity;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 购物车服务Feign客户端
 * 调用 storytelling-cart-service 微服务
 * 
 * @author chennian
 * @date 2025-01-27
 */
@FeignClient(name = "storytelling-cart-service", path = "/cart")
public interface CartServiceClient {

    /**
     * 根据用户ID查询购物车列表
     */
    @GetMapping("/user/{userId}")
    ServerResponseEntity<List<MallCart>> getCartByUserId(@PathVariable Long userId);

    /**
     * 根据用户ID查询选中的购物车商品
     */
    @GetMapping("/user/{userId}/selected")
    ServerResponseEntity<List<MallCart>> getSelectedCartByUserId(@PathVariable Long userId);

    /**
     * 添加商品到购物车
     */
    @PostMapping
    ServerResponseEntity<String> addToCart(@RequestBody MallCart mallCart);

    /**
     * 更新购物车商品数量
     */
    @PutMapping("/{cartId}/quantity")
    ServerResponseEntity<String> updateCartQuantity(
            @PathVariable Long cartId,
            @RequestParam Integer quantity);

    /**
     * 更新购物车商品选中状态
     */
    @PutMapping("/{cartId}/selected")
    ServerResponseEntity<String> updateCartSelected(
            @PathVariable Long cartId,
            @RequestParam Integer isSelected);

    /**
     * 批量更新购物车商品选中状态
     */
    @PutMapping("/batch-selected")
    ServerResponseEntity<String> batchUpdateCartSelected(
            @RequestParam Long userId,
            @RequestBody List<Long> cartIds,
            @RequestParam Integer isSelected);

    /**
     * 全选/取消全选购物车商品
     */
    @PutMapping("/user/{userId}/select-all")
    ServerResponseEntity<String> selectAllCart(
            @PathVariable Long userId,
            @RequestParam Boolean isSelected);

    /**
     * 删除购物车商品
     */
    @DeleteMapping("/{cartId}")
    ServerResponseEntity<String> deleteCartItem(@PathVariable Long cartId);

    /**
     * 批量删除购物车商品
     */
    @DeleteMapping("/batch")
    ServerResponseEntity<String> batchDeleteCartItems(
            @RequestParam Long userId,
            @RequestBody List<Long> cartIds);

    /**
     * 清空用户购物车
     */
    @DeleteMapping("/user/{userId}/clear")
    ServerResponseEntity<String> clearCart(@PathVariable Long userId);

    /**
     * 清空用户选中的购物车商品
     */
    @DeleteMapping("/user/{userId}/clear-selected")
    ServerResponseEntity<String> clearSelectedCart(@PathVariable Long userId);

    /**
     * 检查商品是否已在购物车中
     */
    @GetMapping("/check")
    ServerResponseEntity<MallCart> checkProductInCart(
            @RequestParam Long userId,
            @RequestParam Long productId,
            @RequestParam(required = false) String specification);

    /**
     * 获取用户购物车商品总数量
     */
    @GetMapping("/user/{userId}/count")
    ServerResponseEntity<Integer> getCartItemCount(@PathVariable Long userId);

    /**
     * 获取用户选中购物车商品总数量
     */
    @GetMapping("/user/{userId}/selected-count")
    ServerResponseEntity<Integer> getSelectedCartItemCount(@PathVariable Long userId);
}