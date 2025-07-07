package com.chennian.storytelling.service.mall.Impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.chennian.storytelling.bean.model.mall.MallCategory;
import com.chennian.storytelling.bean.model.mall.MallProduct;
import com.chennian.storytelling.common.utils.PageParam;
import com.chennian.storytelling.dao.MallCategoryMapper;
import com.chennian.storytelling.dao.MallProductMapper;
import com.chennian.storytelling.service.mall.MallCategoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

/**
 * 商城分类Service实现类
 * 
 * @author chennian
 * @date 2025-01-27
 */
@Service
public class MallCategoryServiceImpl extends ServiceImpl<MallCategoryMapper, MallCategory> implements MallCategoryService {
    
    @Autowired
    private MallProductMapper mallProductMapper;
    
    @Override
    public IPage<MallCategory> findByPage(PageParam<MallCategory> page, MallCategory mallCategory) {
        Page<MallCategory> pageInfo = new Page<>(page.getCurrent(), page.getSize());
        LambdaQueryWrapper<MallCategory> queryWrapper = new LambdaQueryWrapper<>();
        
        if (mallCategory != null) {
            // 分类名称模糊查询
            if (StringUtils.hasText(mallCategory.getCategoryName())) {
                queryWrapper.like(MallCategory::getCategoryName, mallCategory.getCategoryName());
            }
            // 分类编码精确查询
            if (StringUtils.hasText(mallCategory.getCategoryCode())) {
                queryWrapper.eq(MallCategory::getCategoryCode, mallCategory.getCategoryCode());
            }
            // 父级分类ID查询
            if (mallCategory.getParentId() != null) {
                queryWrapper.eq(MallCategory::getParentId, mallCategory.getParentId());
            }
            // 状态查询
            if (mallCategory.getStatus() != null) {
                queryWrapper.eq(MallCategory::getStatus, mallCategory.getStatus());
            }
            // 层级查询
            if (mallCategory.getLevel() != null) {
                queryWrapper.eq(MallCategory::getLevel, mallCategory.getLevel());
            }
        }
        
        queryWrapper.orderByAsc(MallCategory::getSort, MallCategory::getCreateTime);
        return this.page(pageInfo, queryWrapper);
    }
    
    @Override
    public MallCategory selectCategoryById(Long categoryId) {
        return this.getById(categoryId);
    }
    
    @Override
    public List<MallCategory> selectCategoriesByParentId(Long parentId) {
        LambdaQueryWrapper<MallCategory> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(MallCategory::getParentId, parentId)
                   .eq(MallCategory::getStatus, 1)
                   .orderByAsc(MallCategory::getSort, MallCategory::getCreateTime);
        return this.list(queryWrapper);
    }
    
    @Override
    public List<MallCategory> getTopCategories() {
        return selectCategoriesByParentId(0L);
    }
    
    @Override
    public List<MallCategory> getCategoryTree() {
        // 获取所有启用的分类
        LambdaQueryWrapper<MallCategory> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(MallCategory::getStatus, 1)
                   .orderByAsc(MallCategory::getSort, MallCategory::getCreateTime);
        List<MallCategory> allCategories = this.list(queryWrapper);
        
        // 构建树形结构
        return buildCategoryTree(allCategories, 0L);
    }
    
    @Override
    public List<MallCategory> getHomeCategories() {
        LambdaQueryWrapper<MallCategory> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(MallCategory::getStatus, 1)
                   .eq(MallCategory::getIsShow, 1)
                   .orderByAsc(MallCategory::getSort, MallCategory::getCreateTime);
        return this.list(queryWrapper);
    }
    
    @Override
    public int createCategory(MallCategory mallCategory) {
        // 设置层级
        if (mallCategory.getParentId() == null || mallCategory.getParentId() == 0) {
            mallCategory.setParentId(0L);
            mallCategory.setLevel(1);
        } else {
            MallCategory parentCategory = this.getById(mallCategory.getParentId());
            if (parentCategory != null) {
                mallCategory.setLevel(parentCategory.getLevel() + 1);
            } else {
                mallCategory.setLevel(1);
            }
        }
        
        mallCategory.setCreateTime(LocalDateTime.now());
        mallCategory.setUpdateTime(LocalDateTime.now());
        return this.save(mallCategory) ? 1 : 0;
    }
    
    @Override
    public int updateCategory(MallCategory mallCategory) {
        mallCategory.setUpdateTime(LocalDateTime.now());
        return this.updateById(mallCategory) ? 1 : 0;
    }
    
    @Override
    public int deleteCategory(Long categoryId) {
        // 检查是否有子分类
        if (hasChildren(categoryId)) {
            throw new RuntimeException("该分类下存在子分类，无法删除");
        }
        
        // 检查是否有商品
        if (hasProducts(categoryId)) {
            throw new RuntimeException("该分类下存在商品，无法删除");
        }
        
        return this.removeById(categoryId) ? 1 : 0;
    }
    
    @Override
    public int enableCategory(Long categoryId) {
        MallCategory category = new MallCategory();
        category.setId(categoryId);
        category.setStatus(1);
        category.setUpdateTime(LocalDateTime.now());
        return this.updateById(category) ? 1 : 0;
    }
    
    @Override
    public int disableCategory(Long categoryId) {
        MallCategory category = new MallCategory();
        category.setId(categoryId);
        category.setStatus(0);
        category.setUpdateTime(LocalDateTime.now());
        return this.updateById(category) ? 1 : 0;
    }
    
    @Override
    public boolean checkCategoryNameExists(String categoryName, Long parentId, Long excludeId) {
        LambdaQueryWrapper<MallCategory> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(MallCategory::getCategoryName, categoryName)
                   .eq(MallCategory::getParentId, parentId != null ? parentId : 0L);
        
        if (excludeId != null) {
            queryWrapper.ne(MallCategory::getId, excludeId);
        }
        
        return this.count(queryWrapper) > 0;
    }
    
    @Override
    public boolean hasChildren(Long categoryId) {
        LambdaQueryWrapper<MallCategory> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(MallCategory::getParentId, categoryId);
        return this.count(queryWrapper) > 0;
    }
    
    @Override
    public boolean hasProducts(Long categoryId) {
        LambdaQueryWrapper<MallProduct> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(MallProduct::getCategoryId, categoryId);
        return mallProductMapper.selectCount(queryWrapper) > 0;
    }
    
    /**
     * 构建分类树形结构
     * 
     * @param allCategories 所有分类
     * @param parentId 父级ID
     * @return 树形结构
     */
    private List<MallCategory> buildCategoryTree(List<MallCategory> allCategories, Long parentId) {
        List<MallCategory> result = new ArrayList<>();
        
        for (MallCategory category : allCategories) {
            if (category.getParentId().equals(parentId)) {
                // 递归查找子分类
                List<MallCategory> children = buildCategoryTree(allCategories, category.getId());
                // 这里可以添加children字段到MallCategory实体中，或者使用VO类
                result.add(category);
            }
        }
        
        return result;
    }
}