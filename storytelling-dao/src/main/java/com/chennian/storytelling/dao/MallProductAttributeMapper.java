package com.chennian.storytelling.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.chennian.storytelling.bean.model.mall.MallProductAttribute;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

/**
 * 商品属性Mapper接口
 * 
 * @author chennian
 * @date 2025-01-27
 */
@Mapper
public interface MallProductAttributeMapper extends BaseMapper<MallProductAttribute> {
    
    /**
     * 根据属性类型查询属性列表
     * 
     * @param attributeType 属性类型
     * @return 属性列表
     */
    List<MallProductAttribute> selectByAttributeType(@Param("attributeType") Integer attributeType);
    
    /**
     * 查询所有启用的属性
     * 
     * @return 启用的属性列表
     */
    List<MallProductAttribute> selectEnabledAttributes();
    
    /**
     * 根据关键词搜索属性
     * 
     * @param keyword 搜索关键词
     * @param attributeType 属性类型（可选）
     * @return 属性列表
     */
    List<MallProductAttribute> searchAttributes(@Param("keyword") String keyword, @Param("attributeType") Integer attributeType);
    
    /**
     * 批量更新属性状态
     * 
     * @param attributeIds 属性ID列表
     * @param status 状态
     * @return 更新数量
     */
    int batchUpdateStatus(@Param("attributeIds") List<Long> attributeIds, @Param("status") Integer status);
    
    /**
     * 获取属性使用统计
     * 
     * @return 统计结果
     */
    List<Map<String, Object>> getAttributeStats();
    
    /**
     * 检查属性名称是否存在
     * 
     * @param attributeName 属性名称
     * @param excludeId 排除的ID（用于更新时检查）
     * @return 存在数量
     */
    int checkAttributeNameExists(@Param("attributeName") String attributeName, @Param("excludeId") Long excludeId);
    
    /**
     * 检查属性编码是否存在
     * 
     * @param attributeCode 属性编码
     * @param excludeId 排除的ID（用于更新时检查）
     * @return 存在数量
     */
    int checkAttributeCodeExists(@Param("attributeCode") String attributeCode, @Param("excludeId") Long excludeId);
}