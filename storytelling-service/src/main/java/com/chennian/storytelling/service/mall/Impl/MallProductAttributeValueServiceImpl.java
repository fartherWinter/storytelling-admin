package com.chennian.storytelling.service.mall.Impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.chennian.storytelling.bean.model.mall.MallProductAttributeValue;
import com.chennian.storytelling.common.utils.PageParam;
import com.chennian.storytelling.dao.MallProductAttributeValueMapper;
import com.chennian.storytelling.service.mall.MallProductAttributeValueService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * 商品属性值Service实现类
 * 
 * @author chennian
 * @date 2025-01-27
 */
@Service
public class MallProductAttributeValueServiceImpl extends ServiceImpl<MallProductAttributeValueMapper, MallProductAttributeValue> implements MallProductAttributeValueService {
    
    @Autowired
    private MallProductAttributeValueMapper mallProductAttributeValueMapper;
    
    @Override
    public IPage<MallProductAttributeValue> findByPage(PageParam<MallProductAttributeValue> page, MallProductAttributeValue attributeValue) {
        Page<MallProductAttributeValue> pageInfo = new Page<>(page.getCurrent(), page.getSize());
        LambdaQueryWrapper<MallProductAttributeValue> queryWrapper = new LambdaQueryWrapper<>();
        
        if (attributeValue != null) {
            // 商品ID查询
            if (attributeValue.getProductId() != null) {
                queryWrapper.eq(MallProductAttributeValue::getProductId, attributeValue.getProductId());
            }
            // 属性ID查询
            if (attributeValue.getAttributeId() != null) {
                queryWrapper.eq(MallProductAttributeValue::getAttributeId, attributeValue.getAttributeId());
            }
            // 属性名称模糊查询
            if (StringUtils.hasText(attributeValue.getAttributeName())) {
                queryWrapper.like(MallProductAttributeValue::getAttributeName, attributeValue.getAttributeName());
            }
            // 属性值模糊查询
            if (StringUtils.hasText(attributeValue.getAttributeValue())) {
                queryWrapper.like(MallProductAttributeValue::getAttributeValue, attributeValue.getAttributeValue());
            }
        }
        
        // 按排序值升序，创建时间降序
        queryWrapper.orderByAsc(MallProductAttributeValue::getSort)
                   .orderByDesc(MallProductAttributeValue::getCreateTime);
        
        return this.page(pageInfo, queryWrapper);
    }
    
    @Override
    public List<MallProductAttributeValue> getAttributeValuesByProductId(Long productId) {
        return mallProductAttributeValueMapper.selectByProductId(productId);
    }
    
    @Override
    public List<MallProductAttributeValue> getAttributeValuesByAttributeId(Long attributeId) {
        LambdaQueryWrapper<MallProductAttributeValue> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(MallProductAttributeValue::getAttributeId, attributeId)
                   .orderByAsc(MallProductAttributeValue::getSort);
        return this.list(queryWrapper);
    }
    
    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean setProductAttributeValues(Long productId, List<MallProductAttributeValue> attributeValues) {
        // 先删除商品的所有属性值
        mallProductAttributeValueMapper.deleteByProductId(productId);
        
        // 批量插入新的属性值
        if (attributeValues != null && !attributeValues.isEmpty()) {
            for (MallProductAttributeValue attributeValue : attributeValues) {
                attributeValue.setProductId(productId);
                attributeValue.setCreateTime(LocalDateTime.now());
                attributeValue.setUpdateTime(LocalDateTime.now());
            }
            return mallProductAttributeValueMapper.batchInsert(attributeValues) > 0;
        }
        
        return true;
    }
    
    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean batchSetProductAttributeValues(Map<Long, List<MallProductAttributeValue>> productAttributeMap) {
        for (Map.Entry<Long, List<MallProductAttributeValue>> entry : productAttributeMap.entrySet()) {
            Long productId = entry.getKey();
            List<MallProductAttributeValue> attributeValues = entry.getValue();
            
            if (!setProductAttributeValues(productId, attributeValues)) {
                return false;
            }
        }
        return true;
    }
    
    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean deleteAttributeValue(Long valueId) {
        return this.removeById(valueId);
    }
    
    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean deleteProductAttributeValues(Long productId) {
        return mallProductAttributeValueMapper.deleteByProductId(productId) >= 0;
    }
    
    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean copyAttributeConfig(Long sourceProductId, List<Long> targetProductIds) {
        // 获取源商品的属性配置
        List<MallProductAttributeValue> sourceAttributeValues = getAttributeValuesByProductId(sourceProductId);
        
        if (sourceAttributeValues == null || sourceAttributeValues.isEmpty()) {
            return true;
        }
        
        // 为每个目标商品复制属性配置
        for (Long targetProductId : targetProductIds) {
            List<MallProductAttributeValue> targetAttributeValues = new ArrayList<>();
            
            for (MallProductAttributeValue sourceValue : sourceAttributeValues) {
                MallProductAttributeValue targetValue = new MallProductAttributeValue();
                targetValue.setProductId(targetProductId);
                targetValue.setAttributeId(sourceValue.getAttributeId());
                targetValue.setAttributeName(sourceValue.getAttributeName());
                targetValue.setAttributeValue(sourceValue.getAttributeValue());
                targetValue.setAttributeValueName(sourceValue.getAttributeValueName());
                targetValue.setAttributeValueImage(sourceValue.getAttributeValueImage());
                targetValue.setSort(sourceValue.getSort());
                targetValue.setCreateTime(LocalDateTime.now());
                targetValue.setUpdateTime(LocalDateTime.now());
                
                targetAttributeValues.add(targetValue);
            }
            
            if (!setProductAttributeValues(targetProductId, targetAttributeValues)) {
                return false;
            }
        }
        
        return true;
    }
    
    @Override
    public MallProductAttributeValue getAttributeValueByProductIdAndAttributeId(Long productId, Long attributeId) {
        return mallProductAttributeValueMapper.selectByProductIdAndAttributeId(productId, attributeId);
    }
    
    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean batchDeleteAttributeValues(List<Long> valueIds) {
        return mallProductAttributeValueMapper.batchDeleteByIds(valueIds) > 0;
    }
}