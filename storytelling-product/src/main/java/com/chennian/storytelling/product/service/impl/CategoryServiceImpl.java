package com.chennian.storytelling.product.service.impl;

import cn.dev33.satoken.stp.StpUtil;
import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.chennian.storytelling.common.exception.BusinessException;
import com.chennian.storytelling.product.entity.Category;
import com.chennian.storytelling.product.mapper.CategoryMapper;
import com.chennian.storytelling.product.service.CategoryService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * 分类服务实现类
 * 
 * @author chennian
 * @since 2024-01-01
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class CategoryServiceImpl implements CategoryService {

    private final CategoryMapper categoryMapper;

    @Override
    public Category getById(Long categoryId) {
        if (categoryId == null) {
            throw new BusinessException("分类ID不能为空");
        }
        return categoryMapper.selectById(categoryId);
    }

    @Override
    public Category getByCategoryCode(String categoryCode) {
        if (StrUtil.isBlank(categoryCode)) {
            throw new BusinessException("分类编码不能为空");
        }
        return categoryMapper.selectByCategoryCode(categoryCode);
    }

    @Override
    public List<Category> getByParentId(Long parentId, Integer status) {
        return categoryMapper.selectByParentId(parentId, status);
    }

    @Override
    public IPage<Category> getCategoryPage(Page<Category> page, String categoryName, 
                                         Long parentId, Integer status, Integer level) {
        return categoryMapper.selectCategoryPage(page, categoryName, parentId, status, level);
    }

    @Override
    public List<Category> getCategoryTree() {
        List<Category> allCategories = categoryMapper.selectCategoryTree();
        return buildCategoryTree(allCategories, 0L);
    }

    @Override
    public List<Category> getRootCategories(Integer status) {
        return categoryMapper.selectRootCategories(status);
    }

    @Override
    public Category getByPath(String path) {
        if (StrUtil.isBlank(path)) {
            throw new BusinessException("分类路径不能为空");
        }
        return categoryMapper.selectByPath(path);
    }

    @Override
    public List<Long> getChildrenIds(Long categoryId) {
        if (categoryId == null) {
            throw new BusinessException("分类ID不能为空");
        }
        return categoryMapper.selectChildrenIds(categoryId);
    }

    @Override
    public List<Category> getParentCategories(Long categoryId) {
        if (categoryId == null) {
            throw new BusinessException("分类ID不能为空");
        }
        return categoryMapper.selectParentCategories(categoryId);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean createCategory(Category category) {
        if (category == null) {
            throw new BusinessException("分类信息不能为空");
        }
        
        // 校验分类编码是否重复
        if (existsByCategoryCode(category.getCategoryCode(), null)) {
            throw new BusinessException("分类编码已存在");
        }
        
        // 校验分类名称是否重复（同级别下）
        if (existsByCategoryName(category.getCategoryName(), category.getParentId(), null)) {
            throw new BusinessException("同级别下分类名称已存在");
        }
        
        // 设置默认值
        if (category.getParentId() == null) {
            category.setParentId(0L);
        }
        if (category.getLevel() == null) {
            if (category.getParentId() == 0L) {
                category.setLevel(1);
            } else {
                Category parent = getById(category.getParentId());
                if (parent == null) {
                    throw new BusinessException("父分类不存在");
                }
                category.setLevel(parent.getLevel() + 1);
            }
        }
        if (category.getStatus() == null) {
            category.setStatus(1); // 默认启用
        }
        if (category.getIsShow() == null) {
            category.setIsShow(1); // 默认显示
        }
        if (category.getSortOrder() == null) {
            Integer maxSortOrder = categoryMapper.getMaxSortOrder(category.getParentId());
            category.setSortOrder(maxSortOrder == null ? 1 : maxSortOrder + 1);
        }
        
        // 插入分类
        int result = categoryMapper.insert(category);
        if (result > 0) {
            // 生成并更新分类路径
            String path = generatePath(category.getCategoryId(), category.getParentId());
            category.setPath(path);
            categoryMapper.updatePath(category.getCategoryId(), path, 
                                    StpUtil.isLogin() ? StpUtil.getLoginIdAsString() : "system");
        }
        
        return result > 0;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean updateCategory(Category category) {
        if (category == null || category.getCategoryId() == null) {
            throw new BusinessException("分类信息不能为空");
        }
        
        // 校验分类是否存在
        Category existCategory = getById(category.getCategoryId());
        if (existCategory == null) {
            throw new BusinessException("分类不存在");
        }
        
        // 校验分类编码是否重复
        if (StrUtil.isNotBlank(category.getCategoryCode()) && 
            existsByCategoryCode(category.getCategoryCode(), category.getCategoryId())) {
            throw new BusinessException("分类编码已存在");
        }
        
        // 校验分类名称是否重复（同级别下）
        Long parentId = category.getParentId() != null ? category.getParentId() : existCategory.getParentId();
        if (StrUtil.isNotBlank(category.getCategoryName()) && 
            existsByCategoryName(category.getCategoryName(), parentId, category.getCategoryId())) {
            throw new BusinessException("同级别下分类名称已存在");
        }
        
        // 如果修改了父分类，需要更新路径和层级
        if (category.getParentId() != null && !category.getParentId().equals(existCategory.getParentId())) {
            return moveCategory(category.getCategoryId(), category.getParentId());
        }
        
        return categoryMapper.updateById(category) > 0;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean deleteCategory(Long categoryId) {
        if (categoryId == null) {
            throw new BusinessException("分类ID不能为空");
        }
        
        Category category = getById(categoryId);
        if (category == null) {
            throw new BusinessException("分类不存在");
        }
        
        // 检查是否有子分类
        if (hasChildren(categoryId)) {
            throw new BusinessException("该分类下存在子分类，无法删除");
        }
        
        // 检查是否有商品
        if (hasProducts(categoryId)) {
            throw new BusinessException("该分类下存在商品，无法删除");
        }
        
        return categoryMapper.deleteById(categoryId) > 0;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean batchDeleteCategories(List<Long> categoryIds) {
        if (categoryIds == null || categoryIds.isEmpty()) {
            throw new BusinessException("分类ID列表不能为空");
        }
        
        // 检查每个分类是否可以删除
        for (Long categoryId : categoryIds) {
            if (hasChildren(categoryId)) {
                throw new BusinessException("分类ID: " + categoryId + " 下存在子分类，无法删除");
            }
            if (hasProducts(categoryId)) {
                throw new BusinessException("分类ID: " + categoryId + " 下存在商品，无法删除");
            }
        }
        
        return categoryMapper.deleteBatchIds(categoryIds) > 0;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean enableCategory(Long categoryId) {
        return updateStatus(categoryId, 1);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean disableCategory(Long categoryId) {
        return updateStatus(categoryId, 0);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean batchUpdateStatus(List<Long> categoryIds, Integer status) {
        if (categoryIds == null || categoryIds.isEmpty()) {
            throw new BusinessException("分类ID列表不能为空");
        }
        if (status == null) {
            throw new BusinessException("状态不能为空");
        }
        
        String updateBy = StpUtil.isLogin() ? StpUtil.getLoginIdAsString() : "system";
        return categoryMapper.batchUpdateStatus(categoryIds, status, updateBy) > 0;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean moveCategory(Long categoryId, Long newParentId) {
        if (categoryId == null) {
            throw new BusinessException("分类ID不能为空");
        }
        if (newParentId == null) {
            newParentId = 0L;
        }
        
        Category category = getById(categoryId);
        if (category == null) {
            throw new BusinessException("分类不存在");
        }
        
        // 不能移动到自己或自己的子分类下
        if (categoryId.equals(newParentId)) {
            throw new BusinessException("不能移动到自己下面");
        }
        
        List<Long> childrenIds = getChildrenIds(categoryId);
        if (childrenIds.contains(newParentId)) {
            throw new BusinessException("不能移动到自己的子分类下面");
        }
        
        // 计算新的层级
        Integer newLevel;
        if (newParentId == 0L) {
            newLevel = 1;
        } else {
            Category newParent = getById(newParentId);
            if (newParent == null) {
                throw new BusinessException("新父分类不存在");
            }
            newLevel = newParent.getLevel() + 1;
        }
        
        // 生成新路径
        String oldPath = category.getPath();
        String newPath = generatePath(categoryId, newParentId);
        
        // 更新分类信息
        Category updateCategory = new Category();
        updateCategory.setCategoryId(categoryId);
        updateCategory.setParentId(newParentId);
        updateCategory.setLevel(newLevel);
        updateCategory.setPath(newPath);
        
        boolean result = categoryMapper.updateById(updateCategory) > 0;
        
        // 更新子分类路径
        if (result && StrUtil.isNotBlank(oldPath)) {
            updateChildrenPath(categoryId, oldPath, newPath);
        }
        
        return result;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean updateSortOrder(Long categoryId, Integer sortOrder) {
        if (categoryId == null) {
            throw new BusinessException("分类ID不能为空");
        }
        if (sortOrder == null) {
            throw new BusinessException("排序值不能为空");
        }
        
        Category category = new Category();
        category.setCategoryId(categoryId);
        category.setSortOrder(sortOrder);
        
        return categoryMapper.updateById(category) > 0;
    }

    @Override
    public boolean existsByCategoryCode(String categoryCode, Long excludeId) {
        if (StrUtil.isBlank(categoryCode)) {
            return false;
        }
        return categoryMapper.existsByCategoryCode(categoryCode, excludeId);
    }

    @Override
    public boolean existsByCategoryName(String categoryName, Long parentId, Long excludeId) {
        if (StrUtil.isBlank(categoryName)) {
            return false;
        }
        return categoryMapper.existsByCategoryName(categoryName, parentId, excludeId);
    }

    @Override
    public boolean hasChildren(Long categoryId) {
        if (categoryId == null) {
            return false;
        }
        return categoryMapper.hasChildren(categoryId);
    }

    @Override
    public boolean hasProducts(Long categoryId) {
        if (categoryId == null) {
            return false;
        }
        return categoryMapper.hasProducts(categoryId);
    }

    @Override
    public Integer getProductCount(Long categoryId) {
        if (categoryId == null) {
            return 0;
        }
        return categoryMapper.getProductCount(categoryId);
    }

    @Override
    public List<Category> buildCategoryTree(List<Category> categories, Long parentId) {
        if (categories == null || categories.isEmpty()) {
            return new ArrayList<>();
        }
        
        return categories.stream()
                .filter(category -> parentId.equals(category.getParentId()))
                .peek(category -> {
                    List<Category> children = buildCategoryTree(categories, category.getCategoryId());
                    category.setChildren(children);
                })
                .collect(Collectors.toList());
    }

    @Override
    public String generatePath(Long categoryId, Long parentId) {
        if (categoryId == null) {
            return "";
        }
        
        if (parentId == null || parentId == 0L) {
            return categoryId.toString();
        }
        
        Category parent = getById(parentId);
        if (parent == null || StrUtil.isBlank(parent.getPath())) {
            return categoryId.toString();
        }
        
        return parent.getPath() + "," + categoryId;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean updateChildrenPath(Long categoryId, String oldPath, String newPath) {
        if (categoryId == null || StrUtil.isBlank(oldPath) || StrUtil.isBlank(newPath)) {
            return false;
        }
        
        List<Long> childrenIds = getChildrenIds(categoryId);
        if (childrenIds.isEmpty()) {
            return true;
        }
        
        String updateBy = StpUtil.isLogin() ? StpUtil.getLoginIdAsString() : "system";
        
        for (Long childId : childrenIds) {
            Category child = getById(childId);
            if (child != null && StrUtil.isNotBlank(child.getPath())) {
                String childNewPath = child.getPath().replace(oldPath, newPath);
                categoryMapper.updatePath(childId, childNewPath, updateBy);
            }
        }
        
        return true;
    }

    /**
     * 更新分类状态
     */
    private boolean updateStatus(Long categoryId, Integer status) {
        if (categoryId == null) {
            throw new BusinessException("分类ID不能为空");
        }
        if (status == null) {
            throw new BusinessException("状态不能为空");
        }
        
        Category category = new Category();
        category.setCategoryId(categoryId);
        category.setStatus(status);
        
        return categoryMapper.updateById(category) > 0;
    }
}