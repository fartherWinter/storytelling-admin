package com.chennian.storytelling.dao.workflow;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.chennian.storytelling.bean.model.workflow.WfProcessCategory;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 流程分类Mapper接口
 * 
 * @author storytelling
 * @date 2024-01-01
 */
@Mapper
public interface WfProcessCategoryMapper extends BaseMapper<WfProcessCategory> {

    /**
     * 根据分类编码查询分类
     * 
     * @param categoryCode 分类编码
     * @return 流程分类
     */
    WfProcessCategory selectByCategoryCode(@Param("categoryCode") String categoryCode);

    /**
     * 根据父分类ID查询子分类列表
     * 
     * @param parentId 父分类ID
     * @return 分类列表
     */
    List<WfProcessCategory> selectByParentId(@Param("parentId") String parentId);

    /**
     * 根据启用状态查询分类列表
     * 
     * @param enabled 是否启用
     * @return 分类列表
     */
    List<WfProcessCategory> selectByEnabled(@Param("enabled") Integer enabled);

    /**
     * 查询所有根分类（父分类ID为空的分类）
     * 
     * @return 分类列表
     */
    List<WfProcessCategory> selectRootCategories();

    /**
     * 查询分类树形结构
     * 
     * @return 分类树
     */
    List<WfProcessCategory> selectCategoryTree();

    /**
     * 根据分类名称模糊查询
     * 
     * @param categoryName 分类名称
     * @return 分类列表
     */
    List<WfProcessCategory> selectByCategoryNameLike(@Param("categoryName") String categoryName);

    /**
     * 检查分类编码是否存在
     * 
     * @param categoryCode 分类编码
     * @param excludeId 排除的分类ID
     * @return 存在数量
     */
    int checkCategoryCodeExists(@Param("categoryCode") String categoryCode, 
                               @Param("excludeId") String excludeId);

    /**
     * 检查分类名称是否存在
     * 
     * @param categoryName 分类名称
     * @param excludeId 排除的分类ID
     * @return 存在数量
     */
    int checkCategoryNameExists(@Param("categoryName") String categoryName, 
                               @Param("excludeId") String excludeId);

    /**
     * 查询分类下是否有子分类
     * 
     * @param parentId 父分类ID
     * @return 子分类数量
     */
    int countChildCategories(@Param("parentId") String parentId);

    /**
     * 查询分类下的流程定义数量
     * 
     * @param categoryId 分类ID
     * @return 流程定义数量
     */
    int countProcessDefinitions(@Param("categoryId") String categoryId);

    /**
     * 批量更新分类状态
     * 
     * @param categoryIds 分类ID列表
     * @param enabled 启用状态
     * @param updatedBy 更新人
     * @return 更新行数
     */
    int batchUpdateCategoryStatus(@Param("categoryIds") List<String> categoryIds, 
                                 @Param("enabled") Integer enabled, 
                                 @Param("updatedBy") String updatedBy);

    /**
     * 查询分类统计信息
     * 
     * @return 统计信息
     */
    List<java.util.Map<String, Object>> selectCategoryStatistics();
}