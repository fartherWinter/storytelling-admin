package com.chennian.storytelling.api.controller.mall;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.chennian.storytelling.bean.model.mall.MallCategory;
import com.chennian.storytelling.common.enums.MallResponseEnum;
import com.chennian.storytelling.common.response.ResponseEnum;
import com.chennian.storytelling.common.response.ServerResponseEntity;
import com.chennian.storytelling.common.utils.PageParam;
import com.chennian.storytelling.service.mall.MallCategoryService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 商城分类Controller
 * 
 * @author chennian
 * @date 2025-01-27
 */
@Api(tags = "商城分类管理")
@RestController
@RequestMapping("/mall/category")
public class MallCategoryController {
    
    @Autowired
    private MallCategoryService mallCategoryService;
    
    /**
     * 分页查询分类列表
     */
    @ApiOperation("分页查询分类列表")
    @PostMapping("/page")
    public ServerResponseEntity<IPage<MallCategory>> getCategoryPage(
            @RequestBody PageParam<MallCategory> page,
            @ApiParam("查询条件") MallCategory mallCategory) {
        IPage<MallCategory> result = mallCategoryService.findByPage(page, mallCategory);
        return ServerResponseEntity.success(result);
    }
    
    /**
     * 根据ID查询分类详情
     */
    @ApiOperation("根据ID查询分类详情")
    @GetMapping("/{categoryId}")
    public ServerResponseEntity<MallCategory> getCategoryById(
            @ApiParam("分类ID") @PathVariable Long categoryId) {
        MallCategory category = mallCategoryService.selectCategoryById(categoryId);
        return ServerResponseEntity.success(category);
    }
    
    /**
     * 根据父级ID查询子分类列表
     */
    @ApiOperation("根据父级ID查询子分类列表")
    @GetMapping("/children/{parentId}")
    public ServerResponseEntity<List<MallCategory>> getCategoriesByParentId(
            @ApiParam("父级分类ID") @PathVariable Long parentId) {
        List<MallCategory> result = mallCategoryService.selectCategoriesByParentId(parentId);
        return ServerResponseEntity.success(result);
    }
    
    /**
     * 获取所有顶级分类
     */
    @ApiOperation("获取所有顶级分类")
    @GetMapping("/top")
    public ServerResponseEntity<List<MallCategory>> getTopCategories() {
        List<MallCategory> result = mallCategoryService.getTopCategories();
        return ServerResponseEntity.success(result);
    }
    
    /**
     * 获取分类树形结构
     */
    @ApiOperation("获取分类树形结构")
    @GetMapping("/tree")
    public ServerResponseEntity<List<MallCategory>> getCategoryTree() {
        List<MallCategory> result = mallCategoryService.getCategoryTree();
        return ServerResponseEntity.success(result);
    }
    
    /**
     * 获取首页显示的分类
     */
    @ApiOperation("获取首页显示的分类")
    @GetMapping("/home")
    public ServerResponseEntity<List<MallCategory>> getHomeCategories() {
        List<MallCategory> result = mallCategoryService.getHomeCategories();
        return ServerResponseEntity.success(result);
    }
    
    /**
     * 创建分类
     */
    @ApiOperation("创建分类")
    @PostMapping
    public ServerResponseEntity<String> createCategory(@RequestBody MallCategory mallCategory) {
        // 检查分类名称是否已存在
        if (mallCategoryService.checkCategoryNameExists(
                mallCategory.getCategoryName(), 
                mallCategory.getParentId(), 
                null)) {
            return ServerResponseEntity.fail(MallResponseEnum.CATEGORY_NAME_EXISTS.getCode(), MallResponseEnum.CATEGORY_NAME_EXISTS.getMessage());
        }
        
        int result = mallCategoryService.createCategory(mallCategory);
        if (result > 0) {
            return ServerResponseEntity.success(MallResponseEnum.CATEGORY_CREATE_SUCCESS.getMessage());
        }
        return ServerResponseEntity.fail(MallResponseEnum.CATEGORY_CREATE_FAIL.getCode(), MallResponseEnum.CATEGORY_CREATE_FAIL.getMessage());
    }
    
    /**
     * 更新分类
     */
    @ApiOperation("更新分类")
    @PutMapping
    public ServerResponseEntity<String> updateCategory(@RequestBody MallCategory mallCategory) {
        // 检查分类名称是否已存在（排除自己）
        if (mallCategoryService.checkCategoryNameExists(
                mallCategory.getCategoryName(), 
                mallCategory.getParentId(), 
                mallCategory.getId())) {
            return ServerResponseEntity.fail(MallResponseEnum.CATEGORY_NAME_EXISTS.getCode(), MallResponseEnum.CATEGORY_NAME_EXISTS.getMessage());
        }
        
        int result = mallCategoryService.updateCategory(mallCategory);
        if (result > 0) {
            return ServerResponseEntity.success(MallResponseEnum.CATEGORY_UPDATE_SUCCESS.getMessage());
        }
        return ServerResponseEntity.fail(MallResponseEnum.CATEGORY_UPDATE_FAIL.getCode(), MallResponseEnum.CATEGORY_UPDATE_FAIL.getMessage());
    }
    
    /**
     * 删除分类
     */
    @ApiOperation("删除分类")
    @DeleteMapping("/{categoryId}")
    public ServerResponseEntity<String> deleteCategory(
            @ApiParam("分类ID") @PathVariable Long categoryId) {
        try {
            int result = mallCategoryService.deleteCategory(categoryId);
            if (result > 0) {
                return ServerResponseEntity.success(MallResponseEnum.CATEGORY_DELETE_SUCCESS.getMessage());
            }
            return ServerResponseEntity.fail(MallResponseEnum.CATEGORY_DELETE_FAIL.getCode(), MallResponseEnum.CATEGORY_DELETE_FAIL.getMessage());
        } catch (RuntimeException e) {
            return ServerResponseEntity.fail(ResponseEnum.valueOf(e.getMessage()));
        }
    }
    
    /**
     * 启用分类
     */
    @ApiOperation("启用分类")
    @PutMapping("/{categoryId}/enable")
    public ServerResponseEntity<String> enableCategory(
            @ApiParam("分类ID") @PathVariable Long categoryId) {
        int result = mallCategoryService.enableCategory(categoryId);
        if (result > 0) {
            return ServerResponseEntity.success(MallResponseEnum.CATEGORY_ENABLE_SUCCESS.getMessage());
        }
        return ServerResponseEntity.fail(MallResponseEnum.CATEGORY_ENABLE_FAIL.getCode(), MallResponseEnum.CATEGORY_ENABLE_FAIL.getMessage());
    }
    
    /**
     * 禁用分类
     */
    @ApiOperation("禁用分类")
    @PutMapping("/{categoryId}/disable")
    public ServerResponseEntity<String> disableCategory(
            @ApiParam("分类ID") @PathVariable Long categoryId) {
        int result = mallCategoryService.disableCategory(categoryId);
        if (result > 0) {
            return ServerResponseEntity.success(MallResponseEnum.CATEGORY_DISABLE_SUCCESS.getMessage());
        }
        return ServerResponseEntity.fail(MallResponseEnum.CATEGORY_DISABLE_FAIL.getCode(), MallResponseEnum.CATEGORY_DISABLE_FAIL.getMessage());
    }
    
    /**
     * 检查分类名称是否存在
     */
    @ApiOperation("检查分类名称是否存在")
    @GetMapping("/check-name")
    public ServerResponseEntity<Boolean> checkCategoryNameExists(
            @ApiParam("分类名称") @RequestParam String categoryName,
            @ApiParam("父级分类ID") @RequestParam(required = false) Long parentId,
            @ApiParam("排除的分类ID") @RequestParam(required = false) Long excludeId) {
        boolean exists = mallCategoryService.checkCategoryNameExists(categoryName, parentId, excludeId);
        return ServerResponseEntity.success(exists);
    }
    
    /**
     * 检查分类是否有子分类
     */
    @ApiOperation("检查分类是否有子分类")
    @GetMapping("/{categoryId}/has-children")
    public ServerResponseEntity<Boolean> hasChildren(
            @ApiParam("分类ID") @PathVariable Long categoryId) {
        boolean hasChildren = mallCategoryService.hasChildren(categoryId);
        return ServerResponseEntity.success(hasChildren);
    }
    
    /**
     * 检查分类下是否有商品
     */
    @ApiOperation("检查分类下是否有商品")
    @GetMapping("/{categoryId}/has-products")
    public ServerResponseEntity<Boolean> hasProducts(
            @ApiParam("分类ID") @PathVariable Long categoryId) {
        boolean hasProducts = mallCategoryService.hasProducts(categoryId);
        return ServerResponseEntity.success(hasProducts);
    }
}