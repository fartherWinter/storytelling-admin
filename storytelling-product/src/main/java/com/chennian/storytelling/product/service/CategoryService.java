package com.chennian.storytelling.product.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.chennian.storytelling.product.entity.Category;

import java.util.List;

/**
 * 分类服务接口
 * 
 * @author chennian
 * @since 2024-01-01
 */
public interface CategoryService {

    /**
     * 根据分类ID查询分类
     * 
     * @param categoryId 分类ID
     * @return 分类信息
     */
    Category getById(Long categoryId);

    /**
     * 根据分类编码查询分类
     * 
     * @param categoryCode 分类编码
     * @return 分类信息
     */
    Category getByCategoryCode(String categoryCode);

    /**
     * 根据父分类ID查询子分类列表
     * 
     * @param parentId 父分类ID
     * @param status 分类状态
     * @return 子分类列表
     */
    List<Category> getByParentId(Long parentId, Integer status);

    /**
     * 分页查询分类列表
     * 
     * @param page 分页参数
     * @param categoryName 分类名称（模糊查询）
     * @param parentId 父分类ID
     * @param status 分类状态
     * @param level 分类层级
     * @return 分类分页列表
     */
    IPage<Category> getCategoryPage(Page<Category> page,
                                  String categoryName,
                                  Long parentId,
                                  Integer status,
                                  Integer level);

    /**
     * 获取所有可用分类（树形结构）
     * 
     * @return 分类树列表
     */
    List<Category> getCategoryTree();

    /**
     * 获取根分类列表
     * 
     * @param status 分类状态
     * @return 根分类列表
     */
    List<Category> getRootCategories(Integer status);

    /**
     * 根据分类路径查询分类
     * 
     * @param path 分类路径
     * @return 分类信息
     */
    Category getByPath(String path);

    /**
     * 获取分类的所有子分类ID
     * 
     * @param categoryId 分类ID
     * @return 子分类ID列表
     */
    List<Long> getChildrenIds(Long categoryId);

    /**
     * 获取分类的所有父分类
     * 
     * @param categoryId 分类ID
     * @return 父分类列表
     */
    List<Category> getParentCategories(Long categoryId);

    /**
     * 创建分类
     * 
     * @param category 分类信息
     * @return 创建结果
     */
    boolean createCategory(Category category);

    /**
     * 更新分类
     * 
     * @param category 分类信息
     * @return 更新结果
     */
    boolean updateCategory(Category category);

    /**
     * 删除分类
     * 
     * @param categoryId 分类ID
     * @return 删除结果
     */
    boolean deleteCategory(Long categoryId);

    /**
     * 批量删除分类
     * 
     * @param categoryIds 分类ID列表
     * @return 删除结果
     */
    boolean batchDeleteCategories(List<Long> categoryIds);

    /**
     * 启用分类
     * 
     * @param categoryId 分类ID
     * @return 启用结果
     */
    boolean enableCategory(Long categoryId);

    /**
     * 禁用分类
     * 
     * @param categoryId 分类ID
     * @return 禁用结果
     */
    boolean disableCategory(Long categoryId);

    /**
     * 批量更新分类状态
     * 
     * @param categoryIds 分类ID列表
     * @param status 状态
     * @return 更新结果
     */
    boolean batchUpdateStatus(List<Long> categoryIds, Integer status);

    /**
     * 移动分类
     * 
     * @param categoryId 分类ID
     * @param newParentId 新父分类ID
     * @return 移动结果
     */
    boolean moveCategory(Long categoryId, Long newParentId);

    /**
     * 更新分类排序
     * 
     * @param categoryId 分类ID
     * @param sortOrder 排序值
     * @return 更新结果
     */
    boolean updateSortOrder(Long categoryId, Integer sortOrder);

    /**
     * 检查分类编码是否存在
     * 
     * @param categoryCode 分类编码
     * @param excludeId 排除的分类ID
     * @return 是否存在
     */
    boolean existsByCategoryCode(String categoryCode, Long excludeId);

    /**
     * 检查分类名称是否存在（同级别下）
     * 
     * @param categoryName 分类名称
     * @param parentId 父分类ID
     * @param excludeId 排除的分类ID
     * @return 是否存在
     */
    boolean existsByCategoryName(String categoryName, Long parentId, Long excludeId);

    /**
     * 检查分类是否有子分类
     * 
     * @param categoryId 分类ID
     * @return 是否有子分类
     */
    boolean hasChildren(Long categoryId);

    /**
     * 检查分类是否有商品
     * 
     * @param categoryId 分类ID
     * @return 是否有商品
     */
    boolean hasProducts(Long categoryId);

    /**
     * 获取分类下的商品数量
     * 
     * @param categoryId 分类ID
     * @return 商品数量
     */
    Integer getProductCount(Long categoryId);

    /**
     * 构建分类树
     * 
     * @param categories 分类列表
     * @param parentId 父分类ID
     * @return 分类树
     */
    List<Category> buildCategoryTree(List<Category> categories, Long parentId);

    /**
     * 生成分类路径
     * 
     * @param categoryId 分类ID
     * @param parentId 父分类ID
     * @return 分类路径
     */
    String generatePath(Long categoryId, Long parentId);

    /**
     * 更新子分类路径
     * 
     * @param categoryId 分类ID
     * @param oldPath 旧路径
     * @param newPath 新路径
     * @return 更新结果
     */
    boolean updateChildrenPath(Long categoryId, String oldPath, String newPath);
}