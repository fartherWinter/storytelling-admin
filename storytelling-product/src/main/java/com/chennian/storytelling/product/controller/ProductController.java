package com.chennian.storytelling.product.controller;

import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.chennian.storytelling.common.result.Result;
import com.chennian.storytelling.product.entity.Product;
import com.chennian.storytelling.product.service.ProductService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.math.BigDecimal;
import java.util.List;

/**
 * 商品控制器
 * 
 * @author chennian
 * @since 2024-01-01
 */
@Slf4j
@RestController
@RequestMapping("/api/product")
@RequiredArgsConstructor
@Validated
@Tag(name = "商品管理", description = "商品相关接口")
public class ProductController {

    private final ProductService productService;

    @GetMapping("/{productId}")
    @Operation(summary = "根据ID获取商品信息")
    public Result<Product> getProduct(
            @Parameter(description = "商品ID") @PathVariable Long productId) {
        Product product = productService.getById(productId);
        return Result.success(product);
    }

    @GetMapping("/code/{productCode}")
    @Operation(summary = "根据编码获取商品信息")
    public Result<Product> getProductByCode(
            @Parameter(description = "商品编码") @PathVariable String productCode) {
        Product product = productService.getByProductCode(productCode);
        return Result.success(product);
    }

    @GetMapping("/page")
    @Operation(summary = "分页查询商品列表")
    public Result<IPage<Product>> getProductPage(
            @Parameter(description = "页码") @RequestParam(defaultValue = "1") Integer current,
            @Parameter(description = "每页大小") @RequestParam(defaultValue = "10") Integer size,
            @Parameter(description = "商品名称") @RequestParam(required = false) String productName,
            @Parameter(description = "分类ID") @RequestParam(required = false) Long categoryId,
            @Parameter(description = "品牌ID") @RequestParam(required = false) Long brandId,
            @Parameter(description = "状态") @RequestParam(required = false) Integer status,
            @Parameter(description = "最低价格") @RequestParam(required = false) BigDecimal minPrice,
            @Parameter(description = "最高价格") @RequestParam(required = false) BigDecimal maxPrice,
            @Parameter(description = "是否推荐") @RequestParam(required = false) Integer isRecommend,
            @Parameter(description = "是否热销") @RequestParam(required = false) Integer isHot,
            @Parameter(description = "是否新品") @RequestParam(required = false) Integer isNew) {
        Page<Product> page = new Page<>(current, size);
        IPage<Product> result = productService.getProductPage(page, productName, categoryId, brandId, 
                status, minPrice, maxPrice, isRecommend, isHot, isNew);
        return Result.success(result);
    }

    @GetMapping("/category/{categoryId}")
    @Operation(summary = "根据分类ID获取商品列表")
    public Result<List<Product>> getProductsByCategory(
            @Parameter(description = "分类ID") @PathVariable Long categoryId,
            @Parameter(description = "限制数量") @RequestParam(required = false) Integer limit) {
        List<Product> products = productService.getProductsByCategory(categoryId, limit);
        return Result.success(products);
    }

    @GetMapping("/brand/{brandId}")
    @Operation(summary = "根据品牌ID获取商品列表")
    public Result<List<Product>> getProductsByBrand(
            @Parameter(description = "品牌ID") @PathVariable Long brandId,
            @Parameter(description = "限制数量") @RequestParam(required = false) Integer limit) {
        List<Product> products = productService.getProductsByBrand(brandId, limit);
        return Result.success(products);
    }

    @GetMapping("/recommend")
    @Operation(summary = "获取推荐商品列表")
    public Result<List<Product>> getRecommendProducts(
            @Parameter(description = "限制数量") @RequestParam(required = false) Integer limit) {
        List<Product> products = productService.getRecommendProducts(limit);
        return Result.success(products);
    }

    @GetMapping("/hot")
    @Operation(summary = "获取热销商品列表")
    public Result<List<Product>> getHotProducts(
            @Parameter(description = "限制数量") @RequestParam(required = false) Integer limit) {
        List<Product> products = productService.getHotProducts(limit);
        return Result.success(products);
    }

    @GetMapping("/new")
    @Operation(summary = "获取新品商品列表")
    public Result<List<Product>> getNewProducts(
            @Parameter(description = "限制数量") @RequestParam(required = false) Integer limit) {
        List<Product> products = productService.getNewProducts(limit);
        return Result.success(products);
    }

    @PostMapping
    @Operation(summary = "创建商品")
    public Result<Boolean> createProduct(@Valid @RequestBody Product product) {
        boolean success = productService.createProduct(product);
        return Result.success(success);
    }

    @PutMapping
    @Operation(summary = "更新商品")
    public Result<Boolean> updateProduct(@Valid @RequestBody Product product) {
        boolean success = productService.updateProduct(product);
        return Result.success(success);
    }

    @DeleteMapping("/{productId}")
    @Operation(summary = "删除商品")
    public Result<Boolean> deleteProduct(
            @Parameter(description = "商品ID") @PathVariable Long productId) {
        boolean success = productService.deleteProduct(productId);
        return Result.success(success);
    }

    @DeleteMapping("/batch")
    @Operation(summary = "批量删除商品")
    public Result<Boolean> batchDeleteProducts(
            @Parameter(description = "商品ID列表") @RequestBody @NotEmpty List<Long> productIds) {
        boolean success = productService.batchDeleteProducts(productIds);
        return Result.success(success);
    }

    @PutMapping("/{productId}/status")
    @Operation(summary = "更新商品状态")
    public Result<Boolean> updateProductStatus(
            @Parameter(description = "商品ID") @PathVariable Long productId,
            @Parameter(description = "状态") @RequestParam @NotNull Integer status) {
        boolean success = productService.updateProductStatus(productId, status);
        return Result.success(success);
    }

    @PutMapping("/batch/status")
    @Operation(summary = "批量更新商品状态")
    public Result<Boolean> batchUpdateProductStatus(
            @Parameter(description = "商品ID列表") @RequestBody @NotEmpty List<Long> productIds,
            @Parameter(description = "状态") @RequestParam @NotNull Integer status) {
        boolean success = productService.batchUpdateProductStatus(productIds, status);
        return Result.success(success);
    }

    @PutMapping("/{productId}/shelf")
    @Operation(summary = "上架商品")
    public Result<Boolean> shelfProduct(
            @Parameter(description = "商品ID") @PathVariable Long productId) {
        boolean success = productService.shelfProduct(productId);
        return Result.success(success);
    }

    @PutMapping("/{productId}/unshelf")
    @Operation(summary = "下架商品")
    public Result<Boolean> unshelfProduct(
            @Parameter(description = "商品ID") @PathVariable Long productId) {
        boolean success = productService.unshelfProduct(productId);
        return Result.success(success);
    }

    @PutMapping("/batch/shelf")
    @Operation(summary = "批量上架商品")
    public Result<Boolean> batchShelfProducts(
            @Parameter(description = "商品ID列表") @RequestBody @NotEmpty List<Long> productIds) {
        boolean success = productService.batchShelfProducts(productIds);
        return Result.success(success);
    }

    @PutMapping("/batch/unshelf")
    @Operation(summary = "批量下架商品")
    public Result<Boolean> batchUnshelfProducts(
            @Parameter(description = "商品ID列表") @RequestBody @NotEmpty List<Long> productIds) {
        boolean success = productService.batchUnshelfProducts(productIds);
        return Result.success(success);
    }

    @PutMapping("/{productId}/sales")
    @Operation(summary = "更新商品销量")
    public Result<Boolean> updateSales(
            @Parameter(description = "商品ID") @PathVariable Long productId,
            @Parameter(description = "销量增量") @RequestParam @NotNull Integer salesIncrement) {
        boolean success = productService.updateSales(productId, salesIncrement);
        return Result.success(success);
    }

    @PutMapping("/{productId}/views")
    @Operation(summary = "更新商品浏览量")
    public Result<Boolean> updateViews(
            @Parameter(description = "商品ID") @PathVariable Long productId,
            @Parameter(description = "浏览量增量") @RequestParam @NotNull Integer viewsIncrement) {
        boolean success = productService.updateViews(productId, viewsIncrement);
        return Result.success(success);
    }

    @PutMapping("/{productId}/favorites")
    @Operation(summary = "更新商品收藏量")
    public Result<Boolean> updateFavorites(
            @Parameter(description = "商品ID") @PathVariable Long productId,
            @Parameter(description = "收藏量增量") @RequestParam @NotNull Integer favoritesIncrement) {
        boolean success = productService.updateFavorites(productId, favoritesIncrement);
        return Result.success(success);
    }

    @PutMapping("/{productId}/comments")
    @Operation(summary = "更新商品评论数")
    public Result<Boolean> updateComments(
            @Parameter(description = "商品ID") @PathVariable Long productId,
            @Parameter(description = "评论数增量") @RequestParam @NotNull Integer commentsIncrement) {
        boolean success = productService.updateComments(productId, commentsIncrement);
        return Result.success(success);
    }

    @PutMapping("/{productId}/rating")
    @Operation(summary = "更新商品评分")
    public Result<Boolean> updateRating(
            @Parameter(description = "商品ID") @PathVariable Long productId,
            @Parameter(description = "评分") @RequestParam @NotNull BigDecimal rating) {
        boolean success = productService.updateRating(productId, rating);
        return Result.success(success);
    }

    @GetMapping("/search")
    @Operation(summary = "搜索商品")
    public Result<IPage<Product>> searchProducts(
            @Parameter(description = "页码") @RequestParam(defaultValue = "1") Integer current,
            @Parameter(description = "每页大小") @RequestParam(defaultValue = "10") Integer size,
            @Parameter(description = "关键词") @RequestParam(required = false) String keyword,
            @Parameter(description = "分类ID") @RequestParam(required = false) Long categoryId,
            @Parameter(description = "品牌ID") @RequestParam(required = false) Long brandId,
            @Parameter(description = "最低价格") @RequestParam(required = false) BigDecimal minPrice,
            @Parameter(description = "最高价格") @RequestParam(required = false) BigDecimal maxPrice,
            @Parameter(description = "排序字段") @RequestParam(required = false) String sortField,
            @Parameter(description = "排序方向") @RequestParam(required = false) String sortOrder) {
        Page<Product> page = new Page<>(current, size);
        IPage<Product> result = productService.searchProducts(page, keyword, categoryId, brandId, 
                minPrice, maxPrice, sortField, sortOrder);
        return Result.success(result);
    }

    @GetMapping("/exists/code")
    @Operation(summary = "检查商品编码是否存在")
    public Result<Boolean> existsByProductCode(
            @Parameter(description = "商品编码") @RequestParam String productCode,
            @Parameter(description = "排除的商品ID") @RequestParam(required = false) Long excludeId) {
        boolean exists = productService.existsByProductCode(productCode, excludeId);
        return Result.success(exists);
    }

    @GetMapping("/exists/name")
    @Operation(summary = "检查商品名称是否存在")
    public Result<Boolean> existsByProductName(
            @Parameter(description = "商品名称") @RequestParam String productName,
            @Parameter(description = "排除的商品ID") @RequestParam(required = false) Long excludeId) {
        boolean exists = productService.existsByProductName(productName, excludeId);
        return Result.success(exists);
    }
}