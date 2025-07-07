package com.chennian.storytelling.service.mall;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.IService;
import com.chennian.storytelling.bean.model.mall.MallProductAttributeValue;
import com.chennian.storytelling.common.utils.PageParam;

import java.util.List;
import java.util.Map;

/**
 * 商品属性值Service接口
 * 
 * @author chennian
 * @date 2025-01-27
 */
public interface MallProductAttributeValueService extends IService<MallProductAttributeValue> {
    
    /**
     * 分页查询属性值列表
     * 
     * @param page 分页参数
     * @param attributeValue 查询条件
     * @return 属性值分页数据
     */
    IPage<MallProductAttributeValue> findByPage(PageParam<MallProductAttributeValue> page, MallProductAttributeValue attributeValue);
    
    /**
     * 根据商品ID获取属性值
     * 
     * @param productId 商品ID
     * @return 属性值列表
     */
    List<MallProductAttributeValue> getAttributeValuesByProductId(Long productId);
    
    /**
     * 根据属性ID获取属性值
     * 
     * @param attributeId 属性ID
     * @return 属性值列表
     */
    List<MallProductAttributeValue> getAttributeValuesByAttributeId(Long attributeId);
    
    /**
     * 设置商品属性值
     * 
     * @param productId 商品ID
     * @param attributeValues 属性值列表
     * @return 是否成功
     */
    boolean setProductAttributeValues(Long productId, List<MallProductAttributeValue> attributeValues);
    
    /**
     * 批量设置商品属性值
     * 
     * @param productAttributeMap 商品属性值映射
     * @return 是否成功
     */
    boolean batchSetProductAttributeValues(Map<Long, List<MallProductAttributeValue>> productAttributeMap);
    
    /**
     * 删除商品属性值
     * 
     * @param valueId 属性值ID
     * @return 是否成功
     */
    boolean deleteAttributeValue(Long valueId);
    
    /**
     * 删除商品的所有属性值
     * 
     * @param productId 商品ID
     * @return 是否成功
     */
    boolean deleteProductAttributeValues(Long productId);
    
    /**
     * 复制属性配置
     * 
     * @param sourceProductId 源商品ID
     * @param targetProductIds 目标商品ID列表
     * @return 是否成功
     */
    boolean copyAttributeConfig(Long sourceProductId, List<Long> targetProductIds);
    
    /**
     * 根据商品ID和属性ID获取属性值
     * 
     * @param productId 商品ID
     * @param attributeId 属性ID
     * @return 属性值
     */
    MallProductAttributeValue getAttributeValueByProductIdAndAttributeId(Long productId, Long attributeId);
    
    /**
     * 批量删除属性值
     * 
     * @param valueIds 属性值ID列表
     * @return 是否成功
     */
    boolean batchDeleteAttributeValues(List<Long> valueIds);
}