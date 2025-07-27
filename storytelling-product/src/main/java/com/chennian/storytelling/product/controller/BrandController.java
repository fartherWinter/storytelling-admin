package com.chennian.storytelling.product.controller;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.chennian.storytelling.common.result.Result;
import com.chennian.storytelling.product.entity.Brand;
import com.chennian.storytelling.product.service.BrandService;
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
import java.util.List;

/**
 * 品牌控制器
 * 
 * @author chennian
 * @since 2024-01-01
 */
@Slf4j
@RestController
@RequestMapping("/api/brand")
@RequiredArgsConstructor
@Validated
@Tag(name = "品牌管理", description = "品牌相关接口")
public class BrandController {

    private final BrandService brandService;

    @GetMapping("/{brandId}")
    @Operation(summary = "根据ID获取品牌信息")
    public Result<Brand> getBrand(
            @Parameter(description = "品牌ID") @PathVariable Long brandId) {
        Brand brand = brandService.getById(brandId);
        return Result.success(brand);
    }

    @GetMapping("/code/{brandCode}")
    @Operation(summary = "根据编码获取品牌信息")
    public Result<Brand> getBrandByCode(
            @Parameter(description = "品牌编码") @PathVariable String brandCode) {
        Brand brand = brandService.getByBrandCode(brandCode);
        return Result.success(brand);
    }

    @GetMapping("/page")
    @Operation(summary = "分页查询品牌列表")
    public Result<IPage<Brand>> getBrandPage(
            @Parameter(description = "页码") @RequestParam(defaultValue = "1") Integer current,
            @Parameter(description = "每页大小") @RequestParam(defaultValue = "10") Integer size,
            @Parameter(description = "品牌名称") @RequestParam(required = false) String brandName,
            @Parameter(description = "状态") @RequestParam(required = false) Integer status,
            @Parameter(description = "是否推荐") @RequestParam(required = false) Integer isRecommend) {
        Page<Brand> page = new Page<>(current, size);
        IPage<Brand> result = brandService.getBrandPage(page, brandName, status, isRecommend);
        return Result.success(result);
    }

    @GetMapping("/all")
    @Operation(summary = "获取所有可用品牌")
    public Result<List<Brand>> getAllAvailable() {
        List<Brand> brands = brandService.getAllAvailable();
        return Result.success(brands);
    }

    @GetMapping("/recommend")
    @Operation(summary = "获取推荐品牌列表")
    public Result<List<Brand>> getRecommendBrands(
            @Parameter(description = "限制数量") @RequestParam(required = false) Integer limit) {
        List<Brand> brands = brandService.getRecommendBrands(limit);
        return Result.success(brands);
    }

    @PostMapping
    @Operation(summary = "创建品牌")
    public Result<Boolean> createBrand(@Valid @RequestBody Brand brand) {
        boolean success = brandService.createBrand(brand);
        return Result.success(success);
    }

    @PutMapping
    @Operation(summary = "更新品牌")
    public Result<Boolean> updateBrand(@Valid @RequestBody Brand brand) {
        boolean success = brandService.updateBrand(brand);
        return Result.success(success);
    }

    @DeleteMapping("/{brandId}")
    @Operation(summary = "删除品牌")
    public Result<Boolean> deleteBrand(
            @Parameter(description = "品牌ID") @PathVariable Long brandId) {
        boolean success = brandService.deleteBrand(brandId);
        return Result.success(success);
    }

    @DeleteMapping("/batch")
    @Operation(summary = "批量删除品牌")
    public Result<Boolean> batchDeleteBrands(
            @Parameter(description = "品牌ID列表") @RequestBody @NotEmpty List<Long> brandIds) {
        boolean success = brandService.batchDeleteBrands(brandIds);
        return Result.success(success);
    }

    @PutMapping("/{brandId}/enable")
    @Operation(summary = "启用品牌")
    public Result<Boolean> enableBrand(
            @Parameter(description = "品牌ID") @PathVariable Long brandId) {
        boolean success = brandService.enableBrand(brandId);
        return Result.success(success);
    }

    @PutMapping("/{brandId}/disable")
    @Operation(summary = "禁用品牌")
    public Result<Boolean> disableBrand(
            @Parameter(description = "品牌ID") @PathVariable Long brandId) {
        boolean success = brandService.disableBrand(brandId);
        return Result.success(success);
    }

    @PutMapping("/batch/status")
    @Operation(summary = "批量更新品牌状态")
    public Result<Boolean> batchUpdateBrandStatus(
            @Parameter(description = "品牌ID列表") @RequestBody @NotEmpty List<Long> brandIds,
            @Parameter(description = "状态") @RequestParam @NotNull Integer status) {
        boolean success = brandService.batchUpdateStatus(brandIds, status);
        return Result.success(success);
    }

    @PutMapping("/{brandId}/sort")
    @Operation(summary = "更新品牌排序")
    public Result<Boolean> updateSortOrder(
            @Parameter(description = "品牌ID") @PathVariable Long brandId,
            @Parameter(description = "排序值") @RequestParam @NotNull Integer sortOrder) {
        boolean success = brandService.updateSortOrder(brandId, sortOrder);
        return Result.success(success);
    }

    @GetMapping("/exists/code")
    @Operation(summary = "检查品牌编码是否存在")
    public Result<Boolean> existsByBrandCode(
            @Parameter(description = "品牌编码") @RequestParam String brandCode,
            @Parameter(description = "排除的品牌ID") @RequestParam(required = false) Long excludeId) {
        boolean exists = brandService.existsByBrandCode(brandCode, excludeId);
        return Result.success(exists);
    }

    @GetMapping("/exists/name")
    @Operation(summary = "检查品牌名称是否存在")
    public Result<Boolean> existsByBrandName(
            @Parameter(description = "品牌名称") @RequestParam String brandName,
            @Parameter(description = "排除的品牌ID") @RequestParam(required = false) Long excludeId) {
        boolean exists = brandService.existsByBrandName(brandName, excludeId);
        return Result.success(exists);
    }

    @GetMapping("/{brandId}/has-products")
    @Operation(summary = "检查品牌是否有商品")
    public Result<Boolean> hasProducts(
            @Parameter(description = "品牌ID") @PathVariable Long brandId) {
        boolean hasProducts = brandService.hasProducts(brandId);
        return Result.success(hasProducts);
    }

    @GetMapping("/{brandId}/product-count")
    @Operation(summary = "获取品牌下的商品数量")
    public Result<Integer> getProductCount(
            @Parameter(description = "品牌ID") @PathVariable Long brandId) {
        Integer count = brandService.getProductCount(brandId);
        return Result.success(count);
    }
}