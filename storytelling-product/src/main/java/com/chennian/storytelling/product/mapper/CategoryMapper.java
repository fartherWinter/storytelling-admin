package com.chennian.storytelling.product.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.chennian.storytelling.product.entity.Category;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 分类Mapper接口
 * 
 * @author chennian
 * @since 2024-01-01
 */
@Mapper
public interface CategoryMapper extends BaseMapper<Category> {

    /**
     * 根据分类编码查询分类
     * 
     * @param categoryCode 分类编码
     * @return 分类信息
     */
    Category selectByCategoryCode(@Param("categoryCode") String categoryCode);

    /**
     * 根据父分类ID查询子分类列表
     * 
     * @param parentId 父分类ID
     * @param status 分类状态
     * @return 子分类列表
     */
    List<Category> selectByParentId(@Param("parentId") Long parentId,
                                  @Param("status") Integer status);

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
    IPage<Category> selectCategoryPage(Page<Category> page,
                                     @Param("categoryName") String categoryName,
                                     @Param("parentId") Long parentId,
                                     @Param("status") Integer status,
                                     @Param("level") Integer level);

    /**
     * 获取所有可用分类（树形结构）
     * 
     * @return 分类树列表
     */
    List<Category> selectCategoryTree();

    /**
     * 获取根分类列表
     * 
     * @param status 分类状态
     * @return 根分类列表
     */
    List<Category> selectRootCategories(@Param("status") Integer status);

    /**
     * 根据分类路径查询分类
     * 
     * @param path 分类路径
     * @return 分类信息
     */
    Category selectByPath(@Param("path") String path);

    /**
     * 获取分类的所有子分类ID
     * 
     * @param categoryId 分类ID
     * @return 子分类ID列表
     */
    List<Long> selectChildrenIds(@Param("categoryId") Long categoryId);

    /**
     * 获取分类的所有父分类
     * 
     * @param categoryId 分类ID
     * @return 父分类列表
     */
    List<Category> selectParentCategories(@Param("categoryId") Long categoryId);

    /**
     * 更新分类路径
     * 
     * @param categoryId 分类ID
     * @param path 新路径
     * @param updateBy 更新者
     * @return 更新结果
     */
    int updatePath(@Param("categoryId") Long categoryId,
                  @Param("path") String path,
                  @Param("updateBy") String updateBy);

    /**
     * 批量更新分类状态
     * 
     * @param categoryIds 分类ID列表
     * @param status 状态
     * @param updateBy 更新者
     * @return 更新结果
     */
    int batchUpdateStatus(@Param("categoryIds") List<Long> categoryIds,
                         @Param("status") Integer status,
                         @Param("updateBy") String updateBy);

    /**
     * 检查分类编码是否存在
     * 
     * @param categoryCode 分类编码
     * @param excludeId 排除的分类ID
     * @return 是否存在
     */
    boolean existsByCategoryCode(@Param("categoryCode") String categoryCode,
                                @Param("excludeId") Long excludeId);

    /**
     * 检查分类名称是否存在（同级别下）
     * 
     * @param categoryName 分类名称
     * @param parentId 父分类ID
     * @param excludeId 排除的分类ID
     * @return 是否存在
     */
    boolean existsByCategoryName(@Param("categoryName") String categoryName,
                                @Param("parentId") Long parentId,
                                @Param("excludeId") Long excludeId);

    /**
     * 检查分类是否有子分类
     * 
     * @param categoryId 分类ID
     * @return 是否有子分类
     */
    boolean hasChildren(@Param("categoryId") Long categoryId);

    /**
     * 检查分类是否有商品
     * 
     * @param categoryId 分类ID
     * @return 是否有商品
     */
    boolean hasProducts(@Param("categoryId") Long categoryId);

    /**
     * 获取分类下的商品数量
     * 
     * @param categoryId 分类ID
     * @return 商品数量
     */
    Integer getProductCount(@Param("categoryId") Long categoryId);

    /**
     * 获取最大排序值
     * 
     * @param parentId 父分类ID
     * @return 最大排序值
     */
    Integer getMaxSortOrder(@Param("parentId") Long parentId);
}