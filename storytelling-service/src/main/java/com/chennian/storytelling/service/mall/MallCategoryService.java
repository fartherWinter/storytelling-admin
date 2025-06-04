package com.chennian.storytelling.service.mall;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.IService;
import com.chennian.storytelling.bean.model.mall.MallCategory;
import com.chennian.storytelling.common.utils.PageParam;

import java.util.List;

/**
 * 商城分类Service接口
 * 
 * @author chennian
 * @date 2025-01-27
 */
public interface MallCategoryService extends IService<MallCategory> {
    
    /**
     * 分页查询分类列表
     * 
     * @param page 分页参数
     * @param mallCategory 查询条件
     * @return 分类分页数据
     */
    IPage<MallCategory> findByPage(PageParam<MallCategory> page, MallCategory mallCategory);
    
    /**
     * 根据ID查询分类
     * 
     * @param categoryId 分类ID
     * @return 分类信息
     */
    MallCategory selectCategoryById(Long categoryId);
    
    /**
     * 根据父级ID查询子分类列表
     * 
     * @param parentId 父级分类ID
     * @return 子分类列表
     */
    List<MallCategory> selectCategoriesByParentId(Long parentId);
    
    /**
     * 获取所有顶级分类
     * 
     * @return 顶级分类列表
     */
    List<MallCategory> getTopCategories();
    
    /**
     * 获取分类树形结构
     * 
     * @return 分类树
     */
    List<MallCategory> getCategoryTree();
    
    /**
     * 获取首页显示的分类
     * 
     * @return 首页分类列表
     */
    List<MallCategory> getHomeCategories();
    
    /**
     * 创建分类
     * 
     * @param mallCategory 分类信息
     * @return 结果
     */
    int createCategory(MallCategory mallCategory);
    
    /**
     * 更新分类
     * 
     * @param mallCategory 分类信息
     * @return 结果
     */
    int updateCategory(MallCategory mallCategory);
    
    /**
     * 删除分类
     * 
     * @param categoryId 分类ID
     * @return 结果
     */
    int deleteCategory(Long categoryId);
    
    /**
     * 启用分类
     * 
     * @param categoryId 分类ID
     * @return 结果
     */
    int enableCategory(Long categoryId);
    
    /**
     * 禁用分类
     * 
     * @param categoryId 分类ID
     * @return 结果
     */
    int disableCategory(Long categoryId);
    
    /**
     * 检查分类名称是否存在
     * 
     * @param categoryName 分类名称
     * @param parentId 父级分类ID
     * @param excludeId 排除的分类ID（用于更新时排除自己）
     * @return 是否存在
     */
    boolean checkCategoryNameExists(String categoryName, Long parentId, Long excludeId);
    
    /**
     * 检查分类是否有子分类
     * 
     * @param categoryId 分类ID
     * @return 是否有子分类
     */
    boolean hasChildren(Long categoryId);
    
    /**
     * 检查分类下是否有商品
     * 
     * @param categoryId 分类ID
     * @return 是否有商品
     */
    boolean hasProducts(Long categoryId);
}