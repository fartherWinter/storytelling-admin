package com.chennian.storytelling.product.controller;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.chennian.storytelling.common.result.Result;
import com.chennian.storytelling.product.entity.Category;
import com.chennian.storytelling.product.service.CategoryService;
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
 * 分类控制器
 * 
 * @author chennian
 * @since 2024-01-01
 */
@Slf4j
@RestController
@RequestMapping("/api/category")
@RequiredArgsConstructor
@Validated
@Tag(name = "分类管理", description = "分类相关接口")
public class CategoryController {

    private final CategoryService categoryService;

    @GetMapping("/{categoryId}")
    @Operation(summary = "根据ID获取分类信息")
    public Result<Category> getCategory(
            @Parameter(description = "分类ID") @PathVariable Long categoryId) {
        Category category = categoryService.getById(categoryId);
        return Result.success(category);
    }

    @GetMapping("/code/{categoryCode}")
    @Operation(summary = "根据编码获取分类信息")
    public Result<Category> getCategoryByCode(
            @Parameter(description = "分类编码") @PathVariable String categoryCode) {
        Category category = categoryService.getByCategoryCode(categoryCode);
        return Result.success(category);
    }

    @GetMapping("/parent/{parentId}")
    @Operation(summary = "根据父分类ID获取子分类列表")
    public Result<List<Category>> getCategoriesByParent(
            @Parameter(description = "父分类ID") @PathVariable Long parentId) {
        List<Category> categories = categoryService.getCategoriesByParentId(parentId);
        return Result.success(categories);
    }

    @GetMapping("/path/{categoryPath}")
    @Operation(summary = "根据路径获取分类信息")
    public Result<Category> getCategoryByPath(
            @Parameter(description = "分类路径") @PathVariable String categoryPath) {
        Category category = categoryService.getByCategoryPath(categoryPath);
        return Result.success(category);
    }

    @GetMapping("/page")
    @Operation(summary = "分页查询分类列表")
    public Result<IPage<Category>> getCategoryPage(
            @Parameter(description = "页码") @RequestParam(defaultValue = "1") Integer current,
            @Parameter(description = "每页大小") @RequestParam(defaultValue = "10") Integer size,
            @Parameter(description = "分类名称") @RequestParam(required = false) String categoryName,
            @Parameter(description = "父分类ID") @RequestParam(required = false) Long parentId,
            @Parameter(description = "状态") @RequestParam(required = false) Integer status,
            @Parameter(description = "是否显示") @RequestParam(required = false) Integer isShow) {
        Page<Category> page = new Page<>(current, size);
        IPage<Category> result = categoryService.getCategoryPage(page, categoryName, parentId, status, isShow);
        return Result.success(result);
    }

    @GetMapping("/tree")
    @Operation(summary = "获取分类树")
    public Result<List<Category>> getCategoryTree() {
        List<Category> tree = categoryService.getCategoryTree();
        return Result.success(tree);
    }

    @GetMapping("/root")
    @Operation(summary = "获取根分类列表")
    public Result<List<Category>> getRootCategories() {
        List<Category> categories = categoryService.getRootCategories();
        return Result.success(categories);
    }

    @GetMapping("/{categoryId}/children/ids")
    @Operation(summary = "获取分类的所有子分类ID")
    public Result<List<Long>> getChildCategoryIds(
            @Parameter(description = "分类ID") @PathVariable Long categoryId) {
        List<Long> childIds = categoryService.getChildCategoryIds(categoryId);
        return Result.success(childIds);
    }

    @GetMapping("/{categoryId}/parents")
    @Operation(summary = "获取分类的所有父分类")
    public Result<List<Category>> getParentCategories(
            @Parameter(description = "分类ID") @PathVariable Long categoryId) {
        List<Category> parents = categoryService.getParentCategories(categoryId);
        return Result.success(parents);
    }

    @PostMapping
    @Operation(summary = "创建分类")
    public Result<Boolean> createCategory(@Valid @RequestBody Category category) {
        boolean success = categoryService.createCategory(category);
        return Result.success(success);
    }

    @PutMapping
    @Operation(summary = "更新分类")
    public Result<Boolean> updateCategory(@Valid @RequestBody Category category) {
        boolean success = categoryService.updateCategory(category);
        return Result.success(success);
    }

    @DeleteMapping("/{categoryId}")
    @Operation(summary = "删除分类")
    public Result<Boolean> deleteCategory(
            @Parameter(description = "分类ID") @PathVariable Long categoryId) {
        boolean success = categoryService.deleteCategory(categoryId);
        return Result.success(success);
    }

    @DeleteMapping("/batch")
    @Operation(summary = "批量删除分类")
    public Result<Boolean> batchDeleteCategories(
            @Parameter(description = "分类ID列表") @RequestBody @NotEmpty List<Long> categoryIds) {
        boolean success = categoryService.batchDeleteCategories(categoryIds);
        return Result.success(success);
    }

    @PutMapping("/{categoryId}/enable")
    @Operation(summary = "启用分类")
    public Result<Boolean> enableCategory(
            @Parameter(description = "分类ID") @PathVariable Long categoryId) {
        boolean success = categoryService.enableCategory(categoryId);
        return Result.success(success);
    }

    @PutMapping("/{categoryId}/disable")
    @Operation(summary = "禁用分类")
    public Result<Boolean> disableCategory(
            @Parameter(description = "分类ID") @PathVariable Long categoryId) {
        boolean success = categoryService.disableCategory(categoryId);
        return Result.success(success);
    }

    @PutMapping("/batch/status")
    @Operation(summary = "批量更新分类状态")
    public Result<Boolean> batchUpdateCategoryStatus(
            @Parameter(description = "分类ID列表") @RequestBody @NotEmpty List<Long> categoryIds,
            @Parameter(description = "状态") @RequestParam @NotNull Integer status) {
        boolean success = categoryService.batchUpdateStatus(categoryIds, status);
        return Result.success(success);
    }

    @PutMapping("/{categoryId}/move")
    @Operation(summary = "移动分类")
    public Result<Boolean> moveCategory(
            @Parameter(description = "分类ID") @PathVariable Long categoryId,
            @Parameter(description = "新父分类ID") @RequestParam Long newParentId) {
        boolean success = categoryService.moveCategory(categoryId, newParentId);
        return Result.success(success);
    }

    @PutMapping("/{categoryId}/sort")
    @Operation(summary = "更新分类排序")
    public Result<Boolean> updateSortOrder(
            @Parameter(description = "分类ID") @PathVariable Long categoryId,
            @Parameter(description = "排序值") @RequestParam @NotNull Integer sortOrder) {
        boolean success = categoryService.updateSortOrder(categoryId, sortOrder);
        return Result.success(success);
    }

    @GetMapping("/exists/code")
    @Operation(summary = "检查分类编码是否存在")
    public Result<Boolean> existsByCategoryCode(
            @Parameter(description = "分类编码") @RequestParam String categoryCode,
            @Parameter(description = "排除的分类ID") @RequestParam(required = false) Long excludeId) {
        boolean exists = categoryService.existsByCategoryCode(categoryCode, excludeId);
        return Result.success(exists);
    }

    @GetMapping("/exists/name")
    @Operation(summary = "检查分类名称是否存在")
    public Result<Boolean> existsByCategoryName(
            @Parameter(description = "分类名称") @RequestParam String categoryName,
            @Parameter(description = "父分类ID") @RequestParam(required = false) Long parentId,
            @Parameter(description = "排除的分类ID") @RequestParam(required = false) Long excludeId) {
        boolean exists = categoryService.existsByCategoryName(categoryName, parentId, excludeId);
        return Result.success(exists);
    }

    @GetMapping("/{categoryId}/has-children")
    @Operation(summary = "检查分类是否有子分类")
    public Result<Boolean> hasChildren(
            @Parameter(description = "分类ID") @PathVariable Long categoryId) {
        boolean hasChildren = categoryService.hasChildren(categoryId);
        return Result.success(hasChildren);
    }

    @GetMapping("/{categoryId}/has-products")
    @Operation(summary = "检查分类是否有商品")
    public Result<Boolean> hasProducts(
            @Parameter(description = "分类ID") @PathVariable Long categoryId) {
        boolean hasProducts = categoryService.hasProducts(categoryId);
        return Result.success(hasProducts);
    }

    @GetMapping("/{categoryId}/product-count")
    @Operation(summary = "获取分类下的商品数量")
    public Result<Integer> getProductCount(
            @Parameter(description = "分类ID") @PathVariable Long categoryId) {
        Integer count = categoryService.getProductCount(categoryId);
        return Result.success(count);
    }
}