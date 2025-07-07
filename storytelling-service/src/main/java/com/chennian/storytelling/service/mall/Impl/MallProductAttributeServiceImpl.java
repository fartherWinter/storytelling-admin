package com.chennian.storytelling.service.mall.Impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.chennian.storytelling.bean.model.mall.MallProductAttribute;
import com.chennian.storytelling.common.utils.PageParam;
import com.chennian.storytelling.dao.MallProductAttributeMapper;
import com.chennian.storytelling.service.mall.MallProductAttributeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

/**
 * 商品属性Service实现类
 * 
 * @author chennian
 * @date 2025-01-27
 */
@Service
public class MallProductAttributeServiceImpl extends ServiceImpl<MallProductAttributeMapper, MallProductAttribute> implements MallProductAttributeService {
    
    @Autowired
    private MallProductAttributeMapper mallProductAttributeMapper;
    
    @Override
    public IPage<MallProductAttribute> findByPage(PageParam<MallProductAttribute> page, MallProductAttribute attribute) {
        Page<MallProductAttribute> pageInfo = new Page<>(page.getCurrent(), page.getSize());
        LambdaQueryWrapper<MallProductAttribute> queryWrapper = new LambdaQueryWrapper<>();
        
        if (attribute != null) {
            // 属性名称模糊查询
            if (StringUtils.hasText(attribute.getAttributeName())) {
                queryWrapper.like(MallProductAttribute::getAttributeName, attribute.getAttributeName());
            }
            // 属性编码精确查询
            if (StringUtils.hasText(attribute.getAttributeCode())) {
                queryWrapper.eq(MallProductAttribute::getAttributeCode, attribute.getAttributeCode());
            }
            // 属性类型查询
            if (attribute.getAttributeType() != null) {
                queryWrapper.eq(MallProductAttribute::getAttributeType, attribute.getAttributeType());
            }
            // 状态查询
            if (attribute.getStatus() != null) {
                queryWrapper.eq(MallProductAttribute::getStatus, attribute.getStatus());
            }
            // 是否必填查询
            if (attribute.getIsRequired() != null) {
                queryWrapper.eq(MallProductAttribute::getIsRequired, attribute.getIsRequired());
            }
            // 是否可搜索查询
            if (attribute.getIsSearchable() != null) {
                queryWrapper.eq(MallProductAttribute::getIsSearchable, attribute.getIsSearchable());
            }
            // 是否可筛选查询
            if (attribute.getIsFilterable() != null) {
                queryWrapper.eq(MallProductAttribute::getIsFilterable, attribute.getIsFilterable());
            }
        }
        
        // 按排序值降序，创建时间降序
        queryWrapper.orderByDesc(MallProductAttribute::getSort)
                   .orderByDesc(MallProductAttribute::getCreateTime);
        
        return this.page(pageInfo, queryWrapper);
    }
    
    @Override
    public List<MallProductAttribute> getEnabledAttributes() {
        return mallProductAttributeMapper.selectEnabledAttributes();
    }
    
    @Override
    public List<MallProductAttribute> getAttributesByType(Integer attributeType) {
        return mallProductAttributeMapper.selectByAttributeType(attributeType);
    }
    
    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean createAttribute(MallProductAttribute attribute) {
        // 检查属性名称是否重复
        if (mallProductAttributeMapper.checkAttributeNameExists(attribute.getAttributeName(), null) > 0) {
            throw new RuntimeException("属性名称已存在");
        }
        
        // 检查属性编码是否重复
        if (StringUtils.hasText(attribute.getAttributeCode()) && 
            mallProductAttributeMapper.checkAttributeCodeExists(attribute.getAttributeCode(), null) > 0) {
            throw new RuntimeException("属性编码已存在");
        }
        
        attribute.setCreateTime(LocalDateTime.now());
        attribute.setUpdateTime(LocalDateTime.now());
        
        return this.save(attribute);
    }
    
    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean updateAttribute(MallProductAttribute attribute) {
        // 检查属性名称是否重复
        if (mallProductAttributeMapper.checkAttributeNameExists(attribute.getAttributeName(), attribute.getId()) > 0) {
            throw new RuntimeException("属性名称已存在");
        }
        
        // 检查属性编码是否重复
        if (StringUtils.hasText(attribute.getAttributeCode()) && 
            mallProductAttributeMapper.checkAttributeCodeExists(attribute.getAttributeCode(), attribute.getId()) > 0) {
            throw new RuntimeException("属性编码已存在");
        }
        
        attribute.setUpdateTime(LocalDateTime.now());
        
        return this.updateById(attribute);
    }
    
    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean deleteAttribute(Long attributeId) {
        // 检查是否有商品使用了该属性，如果有则不允许删除
        int productCount = mallProductAttributeMapper.checkAttributeInUse(attributeId);
        if (productCount > 0) {
            throw new RuntimeException("该属性正在被 " + productCount + " 个商品使用，无法删除");
        }
        
        return this.removeById(attributeId);
    }
    
    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean batchDeleteAttributes(List<Long> attributeIds) {
        // 检查是否有商品使用了这些属性，如果有则不允许删除
        for (Long attributeId : attributeIds) {
            int productCount = mallProductAttributeMapper.checkAttributeInUse(attributeId);
            if (productCount > 0) {
                // 获取属性名称用于错误提示
                MallProductAttribute attribute = this.getById(attributeId);
                String attributeName = attribute != null ? attribute.getAttributeName() : "ID:" + attributeId;
                throw new RuntimeException("属性 '" + attributeName + "' 正在被 " + productCount + " 个商品使用，无法删除");
            }
        }
        
        return this.removeByIds(attributeIds);
    }
    
    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean updateAttributeStatus(Long attributeId, Integer status) {
        MallProductAttribute attribute = new MallProductAttribute();
        attribute.setId(attributeId);
        attribute.setStatus(status);
        attribute.setUpdateTime(LocalDateTime.now());
        
        return this.updateById(attribute);
    }
    
    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean batchUpdateAttributeStatus(List<Long> attributeIds, Integer status) {
        return mallProductAttributeMapper.batchUpdateStatus(attributeIds, status) > 0;
    }
    
    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean updateAttributeSort(Long attributeId, Integer sortOrder) {
        MallProductAttribute attribute = new MallProductAttribute();
        attribute.setId(attributeId);
        attribute.setSort(sortOrder);
        attribute.setUpdateTime(LocalDateTime.now());
        
        return this.updateById(attribute);
    }
    
    @Override
    public List<MallProductAttribute> searchAttributes(String keyword, Integer attributeType) {
        return mallProductAttributeMapper.searchAttributes(keyword, attributeType);
    }
    
    @Override
    public List<Map<String, Object>> getAttributeStats() {
        return mallProductAttributeMapper.getAttributeStats();
    }
    
    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean importAttributes(List<MallProductAttribute> attributes) {
        for (MallProductAttribute attribute : attributes) {
            // 检查属性名称是否重复
            if (mallProductAttributeMapper.checkAttributeNameExists(attribute.getAttributeName(), null) == 0) {
                attribute.setCreateTime(LocalDateTime.now());
                attribute.setUpdateTime(LocalDateTime.now());
                this.save(attribute);
            }
        }
        return true;
    }
    
    @Override
    public List<MallProductAttribute> exportAttributes(List<Long> attributeIds) {
        if (attributeIds == null || attributeIds.isEmpty()) {
            return this.list();
        }
        return this.listByIds(attributeIds);
    }
}