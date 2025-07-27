package com.chennian.storytelling.service.mall.Impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.chennian.storytelling.bean.model.mall.MallBrand;
import com.chennian.storytelling.bean.model.mall.MallProduct;
import com.chennian.storytelling.common.utils.PageParam;
import com.chennian.storytelling.dao.MallBrandMapper;
import com.chennian.storytelling.dao.MallProductMapper;
import com.chennian.storytelling.service.mall.MallBrandService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 商城品牌Service实现类
 * 
 * @author chennian
 * @date 2024-01-01
 */
@Service
public class MallBrandServiceImpl extends ServiceImpl<MallBrandMapper, MallBrand> implements MallBrandService {
    
    @Autowired
    private MallBrandMapper mallBrandMapper;
    
    @Autowired
    private MallProductMapper mallProductMapper;
    
    @Override
    public IPage<MallBrand> findByPage(PageParam<MallBrand> page, MallBrand mallBrand) {
        Page<MallBrand> pageInfo = new Page<>(page.getCurrent(), page.getSize());
        
        String brandName = null;
        Integer status = null;
        Integer isRecommend = null;
        
        if (mallBrand != null) {
            brandName = mallBrand.getBrandName();
            status = mallBrand.getStatus();
            isRecommend = mallBrand.getIsRecommend();
        }
        
        return mallBrandMapper.selectBrandPage(pageInfo, brandName, status, isRecommend);
    }
    
    @Override
    public MallBrand getBrandDetail(Long id) {
        return this.getById(id);
    }
    
    @Override
    public boolean enableBrand(Long id) {
        MallBrand brand = new MallBrand();
        brand.setId(id);
        brand.setStatus(1);
        brand.setUpdateTime(LocalDateTime.now());
        return this.updateById(brand);
    }
    
    @Override
    public boolean disableBrand(Long id) {
        MallBrand brand = new MallBrand();
        brand.setId(id);
        brand.setStatus(0);
        brand.setUpdateTime(LocalDateTime.now());
        return this.updateById(brand);
    }
    
    @Override
    public List<MallBrand> getRecommendBrands(Integer limit) {
        return mallBrandMapper.selectRecommendBrands(1, limit);
    }
    
    @Override
    public IPage<Object> getProductsByBrand(Long brandId, PageParam<Object> page) {
        Page<MallProduct> pageInfo = new Page<>(page.getCurrent(), page.getSize());
        LambdaQueryWrapper<MallProduct> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(MallProduct::getBrandId, brandId)
                   .eq(MallProduct::getStatus, 1)
                   .orderByDesc(MallProduct::getCreateTime);
        
        IPage<MallProduct> productPage = mallProductMapper.selectPage(pageInfo, queryWrapper);
        
        // 转换为Object类型的IPage
        Page<Object> resultPage = new Page<>(productPage.getCurrent(), productPage.getSize(), productPage.getTotal());
        resultPage.setRecords((List<Object>) (List<?>) productPage.getRecords());
        
        return resultPage;
    }
    
    @Override
    public IPage<MallProduct> getProductsByBrand(Long brandId, PageParam<MallProduct> page) {
        Page<MallProduct> pageInfo = new Page<>(page.getCurrent(), page.getSize());
        LambdaQueryWrapper<MallProduct> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(MallProduct::getBrandId, brandId)
                   .eq(MallProduct::getStatus, 1)
                   .orderByDesc(MallProduct::getCreateTime);
        
        return mallProductMapper.selectPage(pageInfo, queryWrapper);
    }
    
    @Override
    public boolean createBrand(MallBrand mallBrand) {
        mallBrand.setCreateTime(LocalDateTime.now());
        mallBrand.setUpdateTime(LocalDateTime.now());
        if (mallBrand.getStatus() == null) {
            mallBrand.setStatus(1);
        }
        if (mallBrand.getIsRecommend() == null) {
            mallBrand.setIsRecommend(0);
        }
        return this.save(mallBrand);
    }
    
    @Override
    public boolean updateBrand(MallBrand mallBrand) {
        mallBrand.setUpdateTime(LocalDateTime.now());
        return this.updateById(mallBrand);
    }
    
    @Override
    public boolean deleteBrand(Long id) {
        // 检查是否有关联的商品
        LambdaQueryWrapper<MallProduct> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(MallProduct::getBrandId, id);
        long productCount = mallProductMapper.selectCount(queryWrapper);
        
        if (productCount > 0) {
            throw new RuntimeException("该品牌下还有商品，无法删除");
        }
        
        return this.removeById(id);
    }
    
    @Override
    public Object getBrandStatistics() {
        Map<String, Object> statistics = new HashMap<>();
        
        // 总品牌数
        long totalCount = this.count();
        statistics.put("totalCount", totalCount);
        
        // 启用品牌数
        LambdaQueryWrapper<MallBrand> enabledWrapper = new LambdaQueryWrapper<>();
        enabledWrapper.eq(MallBrand::getStatus, 1);
        long enabledCount = this.count(enabledWrapper);
        statistics.put("enabledCount", enabledCount);
        
        // 禁用品牌数
        statistics.put("disabledCount", totalCount - enabledCount);
        
        // 推荐品牌数
        LambdaQueryWrapper<MallBrand> recommendWrapper = new LambdaQueryWrapper<>();
        recommendWrapper.eq(MallBrand::getIsRecommend, 1);
        long recommendCount = this.count(recommendWrapper);
        statistics.put("recommendCount", recommendCount);
        
        return statistics;
    }
    
    @Override
    public IPage<MallBrand> searchBrands(String keyword, PageParam<MallBrand> page) {
        Page<MallBrand> pageInfo = new Page<>(page.getCurrent(), page.getSize());
        LambdaQueryWrapper<MallBrand> queryWrapper = new LambdaQueryWrapper<>();
        
        if (StringUtils.hasText(keyword)) {
            queryWrapper.and(wrapper -> wrapper
                .like(MallBrand::getBrandName, keyword)
                .or()
                .like(MallBrand::getBrandCode, keyword)
                .or()
                .like(MallBrand::getDescription, keyword)
            );
        }
        
        queryWrapper.orderByDesc(MallBrand::getCreateTime);
        
        return this.page(pageInfo, queryWrapper);
    }
    
    @Override
    public boolean batchUpdateStatus(List<Long> ids, Integer status) {
        if (ids == null || ids.isEmpty()) {
            return false;
        }
        
        int updateCount = mallBrandMapper.batchUpdateStatus(ids, status);
        return updateCount > 0;
    }
    
    @Override
    public List<MallBrand> getEnabledBrands() {
        return mallBrandMapper.selectByStatus(1);
    }
    
    @Override
    public boolean setRecommendBrand(Long brandId, Integer isRecommend) {
        MallBrand brand = new MallBrand();
        brand.setId(brandId);
        brand.setIsRecommend(isRecommend);
        brand.setUpdateTime(LocalDateTime.now());
        return this.updateById(brand);
    }
    
    @Override
    public Map<String, Object> getBrandStatistics(Long brandId) {
        Map<String, Object> statistics = new HashMap<>();
        
        // 获取品牌信息
        MallBrand brand = this.getById(brandId);
        if (brand == null) {
            return statistics;
        }
        
        statistics.put("brand", brand);
        
        // 获取该品牌下的商品数量
        LambdaQueryWrapper<MallProduct> productWrapper = new LambdaQueryWrapper<>();
        productWrapper.eq(MallProduct::getBrandId, brandId);
        long productCount = mallProductMapper.selectCount(productWrapper);
        statistics.put("productCount", productCount);
        
        // 获取启用的商品数量
        LambdaQueryWrapper<MallProduct> enabledProductWrapper = new LambdaQueryWrapper<>();
        enabledProductWrapper.eq(MallProduct::getBrandId, brandId)
                           .eq(MallProduct::getStatus, 1);
        long enabledProductCount = mallProductMapper.selectCount(enabledProductWrapper);
        statistics.put("enabledProductCount", enabledProductCount);
        
        return statistics;
    }
}