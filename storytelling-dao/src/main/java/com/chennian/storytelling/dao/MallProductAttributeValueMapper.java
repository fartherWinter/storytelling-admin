package com.chennian.storytelling.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.chennian.storytelling.bean.model.mall.MallProductAttributeValue;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 商品属性值Mapper接口
 * 
 * @author chennian
 * @date 2025-01-27
 */
@Mapper
public interface MallProductAttributeValueMapper extends BaseMapper<MallProductAttributeValue> {
    
    /**
     * 根据商品ID查询属性值列表
     * 
     * @param productId 商品ID
     * @return 属性值列表
     */
    List<MallProductAttributeValue> selectByProductId(@Param("productId") Long productId);
    
    /**
     * 根据属性ID查询属性值列表
     * 
     * @param attributeId 属性ID
     * @return 属性值列表
     */
    List<MallProductAttributeValue> selectByAttributeId(@Param("attributeId") Long attributeId);
    
    /**
     * 根据商品ID删除属性值
     * 
     * @param productId 商品ID
     * @return 删除数量
     */
    int deleteByProductId(@Param("productId") Long productId);
    
    /**
     * 根据属性ID删除属性值
     * 
     * @param attributeId 属性ID
     * @return 删除数量
     */
    int deleteByAttributeId(@Param("attributeId") Long attributeId);
    
    /**
     * 批量插入属性值
     * 
     * @param attributeValues 属性值列表
     * @return 插入数量
     */
    int batchInsert(@Param("attributeValues") List<MallProductAttributeValue> attributeValues);
    
    /**
     * 根据商品ID和属性ID查询属性值
     * 
     * @param productId 商品ID
     * @param attributeId 属性ID
     * @return 属性值
     */
    MallProductAttributeValue selectByProductIdAndAttributeId(@Param("productId") Long productId, @Param("attributeId") Long attributeId);
    
    /**
     * 批量删除属性值
     * 
     * @param valueIds 属性值ID列表
     * @return 删除数量
     */
    int batchDeleteByIds(@Param("valueIds") List<Long> valueIds);
}