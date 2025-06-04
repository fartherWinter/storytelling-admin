package com.chennian.storytelling.controller.mall;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.chennian.storytelling.bean.model.mall.MallProduct;
import com.chennian.storytelling.common.enums.MallResponseEnum;
import com.chennian.storytelling.common.response.ServerResponseEntity;
import com.chennian.storytelling.common.utils.PageParam;
import com.chennian.storytelling.service.mall.MallProductService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 商城商品Controller
 * 
 * @author chennian
 * @date 2025-01-27
 */
@Api(tags = "商城商品管理")
@RestController
@RequestMapping("/mall/product")
public class MallProductController {
    
    @Autowired
    private MallProductService mallProductService;
    
    /**
     * 分页查询商品列表
     */
    @ApiOperation("分页查询商品列表")
    @PostMapping("/page")
    public ServerResponseEntity<IPage<MallProduct>> getProductPage(
            @RequestBody PageParam<MallProduct> page,
            @ApiParam("查询条件") MallProduct mallProduct) {
        IPage<MallProduct> result = mallProductService.findByPage(page, mallProduct);
        return ServerResponseEntity.success(result);
    }
    
    /**
     * 根据ID查询商品详情
     */
    @ApiOperation("根据ID查询商品详情")
    @GetMapping("/{productId}")
    public ServerResponseEntity<MallProduct> getProductById(
            @ApiParam("商品ID") @PathVariable Long productId) {
        // 增加浏览量
        mallProductService.increaseViews(productId);
        MallProduct product = mallProductService.selectProductById(productId);
        return ServerResponseEntity.success(product);
    }
    
    /**
     * 根据分类ID查询商品列表
     */
    @ApiOperation("根据分类ID查询商品列表")
    @PostMapping("/category/{categoryId}")
    public ServerResponseEntity<IPage<MallProduct>> getProductsByCategoryId(
            @ApiParam("分类ID") @PathVariable Long categoryId,
            @RequestBody PageParam<MallProduct> page) {
        IPage<MallProduct> result = mallProductService.selectProductsByCategoryId(categoryId, page);
        return ServerResponseEntity.success(result);
    }
    
    /**
     * 搜索商品
     */
    @ApiOperation("搜索商品")
    @PostMapping("/search")
    public ServerResponseEntity<IPage<MallProduct>> searchProducts(
            @ApiParam("搜索关键词") @RequestParam String keyword,
            @RequestBody PageParam<MallProduct> page) {
        IPage<MallProduct> result = mallProductService.searchProducts(keyword, page);
        return ServerResponseEntity.success(result);
    }
    
    /**
     * 获取推荐商品
     */
    @ApiOperation("获取推荐商品")
    @GetMapping("/recommend")
    public ServerResponseEntity<List<MallProduct>> getRecommendProducts(
            @ApiParam("限制数量") @RequestParam(defaultValue = "10") Integer limit) {
        List<MallProduct> result = mallProductService.getRecommendProducts(limit);
        return ServerResponseEntity.success(result);
    }
    
    /**
     * 获取热销商品
     */
    @ApiOperation("获取热销商品")
    @GetMapping("/hot")
    public ServerResponseEntity<List<MallProduct>> getHotProducts(
            @ApiParam("限制数量") @RequestParam(defaultValue = "10") Integer limit) {
        List<MallProduct> result = mallProductService.getHotProducts(limit);
        return ServerResponseEntity.success(result);
    }
    
    /**
     * 获取新品商品
     */
    @ApiOperation("获取新品商品")
    @GetMapping("/new")
    public ServerResponseEntity<List<MallProduct>> getNewProducts(
            @ApiParam("限制数量") @RequestParam(defaultValue = "10") Integer limit) {
        List<MallProduct> result = mallProductService.getNewProducts(limit);
        return ServerResponseEntity.success(result);
    }
    
    /**
     * 创建商品
     */
    @ApiOperation("创建商品")
    @PostMapping
    public ServerResponseEntity<String> createProduct(@RequestBody MallProduct mallProduct) {
        int result = mallProductService.createProduct(mallProduct);
        if (result > 0) {
            return ServerResponseEntity.success(MallResponseEnum.PRODUCT_CREATE_SUCCESS.getMessage());
        }
        return ServerResponseEntity.fail(MallResponseEnum.PRODUCT_CREATE_FAIL.getCode(), MallResponseEnum.PRODUCT_CREATE_FAIL.getMessage());
    }
    
    /**
     * 更新商品
     */
    @ApiOperation("更新商品")
    @PutMapping
    public ServerResponseEntity<String> updateProduct(@RequestBody MallProduct mallProduct) {
        int result = mallProductService.updateProduct(mallProduct);
        if (result > 0) {
            return ServerResponseEntity.success(MallResponseEnum.PRODUCT_UPDATE_SUCCESS.getMessage());
        }
        return ServerResponseEntity.fail(MallResponseEnum.PRODUCT_UPDATE_FAIL.getCode(), MallResponseEnum.PRODUCT_UPDATE_FAIL.getMessage());
    }
    
    /**
     * 删除商品
     */
    @ApiOperation("删除商品")
    @DeleteMapping("/{productId}")
    public ServerResponseEntity<String> deleteProduct(
            @ApiParam("商品ID") @PathVariable Long productId) {
        int result = mallProductService.deleteProduct(productId);
        if (result > 0) {
            return ServerResponseEntity.success(MallResponseEnum.PRODUCT_DELETE_SUCCESS.getMessage());
        }
        return ServerResponseEntity.fail(MallResponseEnum.PRODUCT_DELETE_FAIL.getCode(), MallResponseEnum.PRODUCT_DELETE_FAIL.getMessage());
    }
    
    /**
     * 上架商品
     */
    @ApiOperation("上架商品")
    @PutMapping("/{productId}/on-shelf")
    public ServerResponseEntity<String> onShelf(
            @ApiParam("商品ID") @PathVariable Long productId) {
        int result = mallProductService.onShelf(productId);
        if (result > 0) {
            return ServerResponseEntity.success(MallResponseEnum.PRODUCT_ON_SHELF_SUCCESS.getMessage());
        }
        return ServerResponseEntity.fail(MallResponseEnum.PRODUCT_ON_SHELF_FAIL.getCode(), MallResponseEnum.PRODUCT_ON_SHELF_FAIL.getMessage());
    }
    
    /**
     * 下架商品
     */
    @ApiOperation("下架商品")
    @PutMapping("/{productId}/off-shelf")
    public ServerResponseEntity<String> offShelf(
            @ApiParam("商品ID") @PathVariable Long productId) {
        int result = mallProductService.offShelf(productId);
        if (result > 0) {
            return ServerResponseEntity.success(MallResponseEnum.PRODUCT_OFF_SHELF_SUCCESS.getMessage());
        }
        return ServerResponseEntity.fail(MallResponseEnum.PRODUCT_OFF_SHELF_FAIL.getCode(), MallResponseEnum.PRODUCT_OFF_SHELF_FAIL.getMessage());
    }
    
    /**
     * 更新商品库存
     */
    @ApiOperation("更新商品库存")
    @PutMapping("/{productId}/stock")
    public ServerResponseEntity<String> updateStock(
            @ApiParam("商品ID") @PathVariable Long productId,
            @ApiParam("库存变化量") @RequestParam Integer quantity) {
        int result = mallProductService.updateStock(productId, quantity);
        if (result > 0) {
            return ServerResponseEntity.success(MallResponseEnum.PRODUCT_STOCK_UPDATE_SUCCESS.getMessage());
        }
        return ServerResponseEntity.fail(MallResponseEnum.PRODUCT_STOCK_UPDATE_FAIL.getCode(), MallResponseEnum.PRODUCT_STOCK_UPDATE_FAIL.getMessage());
    }

}