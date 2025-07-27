package com.chennian.storytelling.api.feign;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.chennian.storytelling.bean.model.mall.MallProduct;
import com.chennian.storytelling.common.response.ServerResponseEntity;
import com.chennian.storytelling.common.utils.PageParam;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 商品服务Feign客户端
 * 调用 storytelling-product-service 微服务
 * 
 * @author chennian
 * @date 2025-01-27
 */
@FeignClient(name = "storytelling-product-service", path = "/product")
public interface ProductServiceClient {

    /**
     * 分页查询商品列表
     */
    @PostMapping("/page")
    ServerResponseEntity<IPage<MallProduct>> getProductPage(
            @RequestBody PageParam<MallProduct> page,
            @RequestParam(required = false) MallProduct mallProduct);
    
    /**
     * 根据ID查询商品详情
     */
    @GetMapping("/{productId}")
    ServerResponseEntity<MallProduct> getProductById(@PathVariable Long productId);
    
    /**
     * 根据分类ID查询商品列表
     */
    @PostMapping("/category/{categoryId}")
    ServerResponseEntity<IPage<MallProduct>> getProductsByCategoryId(
            @PathVariable Long categoryId,
            @RequestBody PageParam<MallProduct> page);
    
    /**
     * 搜索商品
     */
    @PostMapping("/search")
    ServerResponseEntity<IPage<MallProduct>> searchProducts(
            @RequestParam String keyword,
            @RequestBody PageParam<MallProduct> page);
    
    /**
     * 获取推荐商品
     */
    @GetMapping("/recommend")
    ServerResponseEntity<List<MallProduct>> getRecommendProducts(
            @RequestParam(defaultValue = "10") Integer limit);
    
    /**
     * 获取热销商品
     */
    @GetMapping("/hot")
    ServerResponseEntity<List<MallProduct>> getHotProducts(
            @RequestParam(defaultValue = "10") Integer limit);
    
    /**
     * 获取新品商品
     */
    @GetMapping("/new")
    ServerResponseEntity<List<MallProduct>> getNewProducts(
            @RequestParam(defaultValue = "10") Integer limit);
    
    /**
     * 创建商品
     */
    @PostMapping
    ServerResponseEntity<String> createProduct(@RequestBody MallProduct mallProduct);
    
    /**
     * 更新商品
     */
    @PutMapping
    ServerResponseEntity<String> updateProduct(@RequestBody MallProduct mallProduct);
    
    /**
     * 删除商品
     */
    @DeleteMapping("/{productId}")
    ServerResponseEntity<String> deleteProduct(@PathVariable Long productId);
    
    /**
     * 上架商品
     */
    @PutMapping("/{productId}/on-shelf")
    ServerResponseEntity<String> onShelf(@PathVariable Long productId);
    
    /**
     * 下架商品
     */
    @PutMapping("/{productId}/off-shelf")
    ServerResponseEntity<String> offShelf(@PathVariable Long productId);
    
    /**
     * 更新商品库存
     */
    @PutMapping("/{productId}/stock")
    ServerResponseEntity<String> updateStock(
            @PathVariable Long productId,
            @RequestParam Integer quantity);
    
    /**
     * 增加商品浏览量
     */
    @PostMapping("/{productId}/views")
    ServerResponseEntity<Void> increaseViews(@PathVariable Long productId);
}