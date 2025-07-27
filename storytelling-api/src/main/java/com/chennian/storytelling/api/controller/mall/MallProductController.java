package com.chennian.storytelling.api.controller.mall;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.chennian.storytelling.api.feign.ProductServiceClient;
import com.chennian.storytelling.bean.model.mall.MallProduct;
import com.chennian.storytelling.common.response.ServerResponseEntity;
import com.chennian.storytelling.common.utils.PageParam;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 商城商品API网关Controller
 * 通过Feign客户端调用商品微服务
 * 
 * @author chennian
 * @date 2025-01-27
 */
@Api(tags = "商城商品管理")
@RestController
@RequestMapping("/mall/product")
public class MallProductController {

    @Autowired
    private ProductServiceClient productServiceClient;

    /**
     * 分页查询商品列表
     */
    @ApiOperation("分页查询商品列表")
    @PostMapping("/page")
    public ServerResponseEntity<IPage<MallProduct>> getProductPage(
            @RequestBody PageParam<MallProduct> page,
            @ApiParam("查询条件") MallProduct mallProduct) {
        return productServiceClient.getProductPage(page, mallProduct);
    }
    
    /**
     * 根据ID查询商品详情
     */
    @ApiOperation("根据ID查询商品详情")
    @GetMapping("/{productId}")
    public ServerResponseEntity<MallProduct> getProductById(
            @ApiParam("商品ID") @PathVariable Long productId) {
        // 增加浏览量
        productServiceClient.increaseViews(productId);
        return productServiceClient.getProductById(productId);
    }
    
    /**
     * 根据分类ID查询商品列表
     */
    @ApiOperation("根据分类ID查询商品列表")
    @PostMapping("/category/{categoryId}")
    public ServerResponseEntity<IPage<MallProduct>> getProductsByCategoryId(
            @ApiParam("分类ID") @PathVariable Long categoryId,
            @RequestBody PageParam<MallProduct> page) {
        return productServiceClient.getProductsByCategoryId(categoryId, page);
    }
    
    /**
     * 搜索商品
     */
    @ApiOperation("搜索商品")
    @PostMapping("/search")
    public ServerResponseEntity<IPage<MallProduct>> searchProducts(
            @ApiParam("搜索关键词") @RequestParam String keyword,
            @RequestBody PageParam<MallProduct> page) {
        return productServiceClient.searchProducts(keyword, page);
    }
    
    /**
     * 获取推荐商品
     */
    @ApiOperation("获取推荐商品")
    @GetMapping("/recommend")
    public ServerResponseEntity<List<MallProduct>> getRecommendProducts(
            @ApiParam("限制数量") @RequestParam(defaultValue = "10") Integer limit) {
        return productServiceClient.getRecommendProducts(limit);
    }
    
    /**
     * 获取热销商品
     */
    @ApiOperation("获取热销商品")
    @GetMapping("/hot")
    public ServerResponseEntity<List<MallProduct>> getHotProducts(
            @ApiParam("限制数量") @RequestParam(defaultValue = "10") Integer limit) {
        return productServiceClient.getHotProducts(limit);
    }
    
    /**
     * 获取新品商品
     */
    @ApiOperation("获取新品商品")
    @GetMapping("/new")
    public ServerResponseEntity<List<MallProduct>> getNewProducts(
            @ApiParam("限制数量") @RequestParam(defaultValue = "10") Integer limit) {
        return productServiceClient.getNewProducts(limit);
    }
    
    /**
     * 创建商品
     */
    @ApiOperation("创建商品")
    @PostMapping
    public ServerResponseEntity<String> createProduct(@RequestBody MallProduct mallProduct) {
        return productServiceClient.createProduct(mallProduct);
    }
    
    /**
     * 更新商品
     */
    @ApiOperation("更新商品")
    @PutMapping
    public ServerResponseEntity<String> updateProduct(@RequestBody MallProduct mallProduct) {
        return productServiceClient.updateProduct(mallProduct);
    }
    
    /**
     * 删除商品
     */
    @ApiOperation("删除商品")
    @DeleteMapping("/{productId}")
    public ServerResponseEntity<String> deleteProduct(
            @ApiParam("商品ID") @PathVariable Long productId) {
        return productServiceClient.deleteProduct(productId);
    }
    
    /**
     * 上架商品
     */
    @ApiOperation("上架商品")
    @PutMapping("/{productId}/on-shelf")
    public ServerResponseEntity<String> onShelf(
            @ApiParam("商品ID") @PathVariable Long productId) {
        return productServiceClient.onShelf(productId);
    }
    
    /**
     * 下架商品
     */
    @ApiOperation("下架商品")
    @PutMapping("/{productId}/off-shelf")
    public ServerResponseEntity<String> offShelf(
            @ApiParam("商品ID") @PathVariable Long productId) {
        return productServiceClient.offShelf(productId);
    }
    
    /**
     * 更新商品库存
     */
    @ApiOperation("更新商品库存")
    @PutMapping("/{productId}/stock")
    public ServerResponseEntity<String> updateStock(
            @ApiParam("商品ID") @PathVariable Long productId,
            @ApiParam("库存变化量") @RequestParam Integer quantity) {
        return productServiceClient.updateStock(productId, quantity);
    }

}