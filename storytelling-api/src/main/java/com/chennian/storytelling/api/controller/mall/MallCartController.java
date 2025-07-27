package com.chennian.storytelling.api.controller.mall;

import com.chennian.storytelling.api.feign.CartServiceClient;
import com.chennian.storytelling.bean.model.mall.MallCart;
import com.chennian.storytelling.common.response.ServerResponseEntity;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 商城购物车Controller - API网关
 * 通过Feign客户端调用购物车微服务
 *
 * @author chennian
 * @date 2025-01-27
 */
@Api(tags = "商城购物车管理")
@RestController
@RequestMapping("/mall/cart")
public class MallCartController {

    @Autowired
    private CartServiceClient cartServiceClient;

    /**
     * 根据用户ID查询购物车列表
     */
    @ApiOperation("根据用户ID查询购物车列表")
    @GetMapping("/user/{userId}")
    public ServerResponseEntity<List<MallCart>> getCartByUserId(
            @ApiParam("用户ID") @PathVariable Long userId) {
        return cartServiceClient.getCartByUserId(userId);
    }

    /**
     * 根据用户ID查询选中的购物车商品
     */
    @ApiOperation("根据用户ID查询选中的购物车商品")
    @GetMapping("/user/{userId}/selected")
    public ServerResponseEntity<List<MallCart>> getSelectedCartByUserId(
            @ApiParam("用户ID") @PathVariable Long userId) {
        return cartServiceClient.getSelectedCartByUserId(userId);
    }

    /**
     * 添加商品到购物车
     */
    @ApiOperation("添加商品到购物车")
    @PostMapping
    public ServerResponseEntity<String> addToCart(@RequestBody MallCart mallCart) {
        return cartServiceClient.addToCart(mallCart);
    }

    /**
     * 更新购物车商品数量
     */
    @ApiOperation("更新购物车商品数量")
    @PutMapping("/{cartId}/quantity")
    public ServerResponseEntity<String> updateCartQuantity(
            @ApiParam("购物车ID") @PathVariable Long cartId,
            @ApiParam("商品数量") @RequestParam Integer quantity) {
        return cartServiceClient.updateCartQuantity(cartId, quantity);
    }

    /**
     * 更新购物车商品选中状态
     */
    @ApiOperation("更新购物车商品选中状态")
    @PutMapping("/{cartId}/selected")
    public ServerResponseEntity<String> updateCartSelected(
            @ApiParam("购物车ID") @PathVariable Long cartId,
            @ApiParam("是否选中") @RequestParam Integer isSelected) {
        return cartServiceClient.updateCartSelected(cartId, isSelected);
    }

    /**
     * 批量更新购物车商品选中状态
     */
    @ApiOperation("批量更新购物车商品选中状态")
    @PutMapping("/batch-selected")
    public ServerResponseEntity<String> batchUpdateCartSelected(
            @ApiParam("用户ID") @RequestParam Long userId,
            @ApiParam("购物车ID列表") @RequestBody List<Long> cartIds,
            @ApiParam("是否选中") @RequestParam Integer isSelected) {
        return cartServiceClient.batchUpdateCartSelected(userId, cartIds, isSelected);
    }

    /**
     * 全选/取消全选购物车商品
     */
    @ApiOperation("全选/取消全选购物车商品")
    @PutMapping("/user/{userId}/select-all")
    public ServerResponseEntity<String> selectAllCart(
            @ApiParam("用户ID") @PathVariable Long userId,
            @ApiParam("是否选中") @RequestParam Boolean isSelected) {
        return cartServiceClient.selectAllCart(userId, isSelected);
    }

    /**
     * 删除购物车商品
     */
    @ApiOperation("删除购物车商品")
    @DeleteMapping("/{cartId}")
    public ServerResponseEntity<String> deleteCartItem(
            @ApiParam("购物车ID") @PathVariable Long cartId) {
        return cartServiceClient.deleteCartItem(cartId);
    }

    /**
     * 批量删除购物车商品
     */
    @ApiOperation("批量删除购物车商品")
    @DeleteMapping("/batch")
    public ServerResponseEntity<String> batchDeleteCartItems(
            @ApiParam("用户ID") @RequestParam Long userId,
            @ApiParam("购物车ID列表") @RequestBody List<Long> cartIds) {
        return cartServiceClient.batchDeleteCartItems(userId, cartIds);
    }

    /**
     * 清空用户购物车
     */
    @ApiOperation("清空用户购物车")
    @DeleteMapping("/user/{userId}/clear")
    public ServerResponseEntity<String> clearCart(
            @ApiParam("用户ID") @PathVariable Long userId) {
        return cartServiceClient.clearCart(userId);
    }

    /**
     * 清空用户选中的购物车商品
     */
    @ApiOperation("清空用户选中的购物车商品")
    @DeleteMapping("/user/{userId}/clear-selected")
    public ServerResponseEntity<String> clearSelectedCart(
            @ApiParam("用户ID") @PathVariable Long userId) {
        return cartServiceClient.clearSelectedCart(userId);
    }

    /**
     * 检查商品是否已在购物车中
     */
    @ApiOperation("检查商品是否已在购物车中")
    @GetMapping("/check")
    public ServerResponseEntity<MallCart> checkProductInCart(
            @ApiParam("用户ID") @RequestParam Long userId,
            @ApiParam("商品ID") @RequestParam Long productId,
            @ApiParam("商品规格") @RequestParam(required = false) String specification) {
        return cartServiceClient.checkProductInCart(userId, productId, specification);
    }

    /**
     * 获取用户购物车商品总数量
     */
    @ApiOperation("获取用户购物车商品总数量")
    @GetMapping("/user/{userId}/count")
    public ServerResponseEntity<Integer> getCartItemCount(
            @ApiParam("用户ID") @PathVariable Long userId) {
        return cartServiceClient.getCartItemCount(userId);
    }

    /**
     * 获取用户选中购物车商品总数量
     */
    @ApiOperation("获取用户选中购物车商品总数量")
    @GetMapping("/user/{userId}/selected-count")
    public ServerResponseEntity<Integer> getSelectedCartItemCount(
            @ApiParam("用户ID") @PathVariable Long userId) {
        return cartServiceClient.getSelectedCartItemCount(userId);
    }
}