package com.chennian.storytelling.service.mall;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.IService;
import com.chennian.storytelling.bean.model.mall.MallProductAttribute;
import com.chennian.storytelling.common.utils.PageParam;

import java.util.List;
import java.util.Map;

/**
 * 商品属性Service接口
 * 
 * @author chennian
 * @date 2025-01-27
 */
public interface MallProductAttributeService extends IService<MallProductAttribute> {
    
    /**
     * 分页查询属性列表
     * 
     * @param page 分页参数
     * @param attribute 查询条件
     * @return 属性分页数据
     */
    IPage<MallProductAttribute> findByPage(PageParam<MallProductAttribute> page, MallProductAttribute attribute);
    
    /**
     * 获取所有启用的属性
     * 
     * @return 启用的属性列表
     */
    List<MallProductAttribute> getEnabledAttributes();
    
    /**
     * 根据属性类型获取属性列表
     * 
     * @param attributeType 属性类型
     * @return 属性列表
     */
    List<MallProductAttribute> getAttributesByType(Integer attributeType);
    
    /**
     * 创建商品属性
     * 
     * @param attribute 属性信息
     * @return 是否成功
     */
    boolean createAttribute(MallProductAttribute attribute);
    
    /**
     * 更新商品属性
     * 
     * @param attribute 属性信息
     * @return 是否成功
     */
    boolean updateAttribute(MallProductAttribute attribute);
    
    /**
     * 删除商品属性
     * 
     * @param attributeId 属性ID
     * @return 是否成功
     */
    boolean deleteAttribute(Long attributeId);
    
    /**
     * 批量删除属性
     * 
     * @param attributeIds 属性ID列表
     * @return 是否成功
     */
    boolean batchDeleteAttributes(List<Long> attributeIds);
    
    /**
     * 更新属性状态
     * 
     * @param attributeId 属性ID
     * @param status 状态
     * @return 是否成功
     */
    boolean updateAttributeStatus(Long attributeId, Integer status);
    
    /**
     * 批量更新属性状态
     * 
     * @param attributeIds 属性ID列表
     * @param status 状态
     * @return 是否成功
     */
    boolean batchUpdateAttributeStatus(List<Long> attributeIds, Integer status);
    
    /**
     * 更新属性排序
     * 
     * @param attributeId 属性ID
     * @param sortOrder 排序值
     * @return 是否成功
     */
    boolean updateAttributeSort(Long attributeId, Integer sortOrder);
    
    /**
     * 搜索属性
     * 
     * @param keyword 搜索关键词
     * @param attributeType 属性类型
     * @return 属性列表
     */
    List<MallProductAttribute> searchAttributes(String keyword, Integer attributeType);
    
    /**
     * 获取属性使用统计
     * 
     * @return 统计结果
     */
    List<Map<String, Object>> getAttributeStats();
    
    /**
     * 获取属性使用统计
     * 
     * @return 统计结果
     */
    List<Map<String, Object>> getAttributeUsageStats();
    
    /**
     * 导入属性配置
     * 
     * @param attributes 属性列表
     * @return 是否成功
     */
    boolean importAttributes(List<MallProductAttribute> attributes);
    
    /**
     * 导出属性配置
     * 
     * @param attributeIds 属性ID列表
     * @return 属性列表
     */
    List<MallProductAttribute> exportAttributes(List<Long> attributeIds);
}