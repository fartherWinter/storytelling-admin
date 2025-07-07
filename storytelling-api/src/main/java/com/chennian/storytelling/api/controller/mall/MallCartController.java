package com.chennian.storytelling.api.controller.mall;

import com.chennian.storytelling.bean.model.mall.MallCart;
import com.chennian.storytelling.common.enums.MallResponseEnum;
import com.chennian.storytelling.common.response.ServerResponseEntity;
import com.chennian.storytelling.service.mall.MallCartService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 商城购物车Controller
 *
 * @author chennian
 * @date 2025-01-27
 */
@Api(tags = "商城购物车管理")
@RestController
@RequestMapping("/mall/cart")
public class MallCartController {

    @Autowired
    private MallCartService mallCartService;

    /**
     * 根据用户ID查询购物车列表
     */
    @ApiOperation("根据用户ID查询购物车列表")
    @GetMapping("/user/{userId}")
    public ServerResponseEntity<List<MallCart>> getCartByUserId(
            @ApiParam("用户ID") @PathVariable Long userId) {
        List<MallCart> result = mallCartService.selectCartByUserId(userId);
        return ServerResponseEntity.success(result);
    }

    /**
     * 根据用户ID查询选中的购物车商品
     */
    @ApiOperation("根据用户ID查询选中的购物车商品")
    @GetMapping("/user/{userId}/selected")
    public ServerResponseEntity<List<MallCart>> getSelectedCartByUserId(
            @ApiParam("用户ID") @PathVariable Long userId) {
        List<MallCart> result = mallCartService.selectSelectedCartByUserId(userId);
        return ServerResponseEntity.success(result);
    }

    /**
     * 添加商品到购物车
     */
    @ApiOperation("添加商品到购物车")
    @PostMapping
    public ServerResponseEntity<String> addToCart(@RequestBody MallCart mallCart) {
        boolean success = mallCartService.addToCart(mallCart);
        if (success) {
            return ServerResponseEntity.success(MallResponseEnum.CART_ADD_SUCCESS.getMessage());
        } else {
            return ServerResponseEntity.fail(MallResponseEnum.CART_ADD_FAIL.getCode(), MallResponseEnum.CART_ADD_FAIL.getMessage());
        }
    }

    /**
     * 更新购物车商品数量
     */
    @ApiOperation("更新购物车商品数量")
    @PutMapping("/{cartId}/quantity")
    public ServerResponseEntity<String> updateCartQuantity(
            @ApiParam("购物车ID") @PathVariable Long cartId,
            @ApiParam("商品数量") @RequestParam Integer quantity) {
        if (quantity <= 0) {
            return ServerResponseEntity.fail(MallResponseEnum.QUANTITY_INVALID.getCode(), MallResponseEnum.QUANTITY_INVALID.getMessage());
        }

        int success = mallCartService.updateCartQuantity(cartId, quantity);
        if (success > 0) {
            return ServerResponseEntity.success(MallResponseEnum.CART_UPDATE_QUANTITY_SUCCESS.getMessage());
        } else {
            return ServerResponseEntity.fail(MallResponseEnum.CART_UPDATE_QUANTITY_FAIL.getCode(), MallResponseEnum.CART_UPDATE_QUANTITY_FAIL.getMessage());
        }
    }

    /**
     * 更新购物车商品选中状态
     */
    @ApiOperation("更新购物车商品选中状态")
    @PutMapping("/{cartId}/selected")
    public ServerResponseEntity<String> updateCartSelected(
            @ApiParam("购物车ID") @PathVariable Long cartId,
            @ApiParam("是否选中") @RequestParam Integer isSelected) {
        int success = mallCartService.updateCartSelected(cartId, isSelected);
        if (success > 0) {
            return ServerResponseEntity.success(MallResponseEnum.CART_UPDATE_SELECTED_SUCCESS.getMessage());
        } else {
            return ServerResponseEntity.fail(MallResponseEnum.CART_UPDATE_SELECTED_FAIL.getCode(), MallResponseEnum.CART_UPDATE_SELECTED_FAIL.getMessage());
        }
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
        int success = mallCartService.batchUpdateCartSelected(userId, cartIds, isSelected);
        if (success > 0) {
            return ServerResponseEntity.success(MallResponseEnum.CART_BATCH_UPDATE_SELECTED_SUCCESS.getMessage());
        } else {
            return ServerResponseEntity.fail(MallResponseEnum.CART_BATCH_UPDATE_SELECTED_FAIL.getCode(), MallResponseEnum.CART_BATCH_UPDATE_SELECTED_FAIL.getMessage());
        }
    }

    /**
     * 全选/取消全选购物车商品
     */
    @ApiOperation("全选/取消全选购物车商品")
    @PutMapping("/user/{userId}/select-all")
    public ServerResponseEntity<String> selectAllCart(
            @ApiParam("用户ID") @PathVariable Long userId,
            @ApiParam("是否选中") @RequestParam Integer isSelected) {
        int success = mallCartService.selectAllCart(userId, isSelected);
        if (success > 0) {
            return ServerResponseEntity.success(MallResponseEnum.CART_SELECT_ALL_SUCCESS.getMessage());
        } else {
            return ServerResponseEntity.fail(MallResponseEnum.CART_SELECT_ALL_FAIL.getCode(), MallResponseEnum.CART_SELECT_ALL_FAIL.getMessage());
        }
    }

    /**
     * 删除购物车商品
     */
    @ApiOperation("删除购物车商品")
    @DeleteMapping("/{cartId}")
    public ServerResponseEntity<String> deleteCartItem(
            @ApiParam("购物车ID") @PathVariable Long cartId) {
        int success = mallCartService.deleteCartItem(cartId);
        if (success > 0) {
            return ServerResponseEntity.success(MallResponseEnum.CART_DELETE_SUCCESS.getMessage());
        } else {
            return ServerResponseEntity.fail(MallResponseEnum.CART_DELETE_FAIL.getCode(), MallResponseEnum.CART_DELETE_FAIL.getMessage());
        }
    }

    /**
     * 批量删除购物车商品
     */
    @ApiOperation("批量删除购物车商品")
    @DeleteMapping("/batch")
    public ServerResponseEntity<String> batchDeleteCartItems(
            @ApiParam("用户ID") @RequestParam Long userId,
            @ApiParam("购物车ID列表") @RequestBody List<Long> cartIds) {
        int success = mallCartService.batchDeleteCartItems(userId, cartIds);
        if (success > 0) {
            return ServerResponseEntity.success(MallResponseEnum.CART_BATCH_DELETE_SUCCESS.getMessage());
        } else {
            return ServerResponseEntity.fail(MallResponseEnum.CART_BATCH_DELETE_FAIL.getCode(), MallResponseEnum.CART_BATCH_DELETE_FAIL.getMessage());
        }
    }

    /**
     * 清空用户购物车
     */
    @ApiOperation("清空用户购物车")
    @DeleteMapping("/user/{userId}/clear")
    public ServerResponseEntity<String> clearCart(
            @ApiParam("用户ID") @PathVariable Long userId) {
        int success = mallCartService.clearCart(userId);
        if (success > 0) {
            return ServerResponseEntity.success(MallResponseEnum.CART_CLEAR_SUCCESS.getMessage());
        } else {
            return ServerResponseEntity.fail(MallResponseEnum.CART_CLEAR_FAIL.getCode(), MallResponseEnum.CART_CLEAR_FAIL.getMessage());
        }
    }

    /**
     * 清空用户选中的购物车商品
     */
    @ApiOperation("清空用户选中的购物车商品")
    @DeleteMapping("/user/{userId}/clear-selected")
    public ServerResponseEntity<String> clearSelectedCart(
            @ApiParam("用户ID") @PathVariable Long userId) {
        int success = mallCartService.clearSelectedCart(userId);
        if (success > 0) {
            return ServerResponseEntity.success(MallResponseEnum.CART_CLEAR_SELECTED_SUCCESS.getMessage());
        } else {
            return ServerResponseEntity.fail(MallResponseEnum.CART_CLEAR_SELECTED_FAIL.getCode(), MallResponseEnum.CART_CLEAR_SELECTED_FAIL.getMessage());
        }
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
        MallCart result = mallCartService.checkProductInCart(userId, productId, specification);
        return ServerResponseEntity.success(result);
    }

    /**
     * 获取用户购物车商品总数量
     */
    @ApiOperation("获取用户购物车商品总数量")
    @GetMapping("/user/{userId}/count")
    public ServerResponseEntity<Integer> getCartItemCount(
            @ApiParam("用户ID") @PathVariable Long userId) {
        Integer count = mallCartService.getCartItemCount(userId);
        return ServerResponseEntity.success(count);
    }

    /**
     * 获取用户选中购物车商品总数量
     */
    @ApiOperation("获取用户选中购物车商品总数量")
    @GetMapping("/user/{userId}/selected-count")
    public ServerResponseEntity<Integer> getSelectedCartItemCount(
            @ApiParam("用户ID") @PathVariable Long userId) {
        Integer count = mallCartService.getSelectedCartItemCount(userId);
        return ServerResponseEntity.success(count);
    }
}