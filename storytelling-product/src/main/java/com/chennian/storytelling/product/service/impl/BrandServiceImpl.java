package com.chennian.storytelling.product.service.impl;

import cn.dev33.satoken.stp.StpUtil;
import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.chennian.storytelling.common.exception.BusinessException;
import com.chennian.storytelling.product.entity.Brand;
import com.chennian.storytelling.product.mapper.BrandMapper;
import com.chennian.storytelling.product.service.BrandService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * 品牌服务实现类
 * 
 * @author chennian
 * @since 2024-01-01
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class BrandServiceImpl implements BrandService {

    private final BrandMapper brandMapper;

    @Override
    public Brand getById(Long brandId) {
        if (brandId == null) {
            throw new BusinessException("品牌ID不能为空");
        }
        return brandMapper.selectById(brandId);
    }

    @Override
    public Brand getByBrandCode(String brandCode) {
        if (StrUtil.isBlank(brandCode)) {
            throw new BusinessException("品牌编码不能为空");
        }
        return brandMapper.selectByBrandCode(brandCode);
    }

    @Override
    public IPage<Brand> getBrandPage(Page<Brand> page, String brandName, Integer status, Integer isRecommend) {
        return brandMapper.selectBrandPage(page, brandName, status, isRecommend);
    }

    @Override
    public List<Brand> getAllAvailable() {
        return brandMapper.selectAllAvailable();
    }

    @Override
    public List<Brand> getRecommendBrands(Integer limit) {
        if (limit == null || limit <= 0) {
            limit = 10;
        }
        return brandMapper.selectRecommendBrands(limit);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean createBrand(Brand brand) {
        if (brand == null) {
            throw new BusinessException("品牌信息不能为空");
        }
        
        // 校验品牌编码是否重复
        if (existsByBrandCode(brand.getBrandCode(), null)) {
            throw new BusinessException("品牌编码已存在");
        }
        
        // 校验品牌名称是否重复
        if (existsByBrandName(brand.getBrandName(), null)) {
            throw new BusinessException("品牌名称已存在");
        }
        
        // 设置默认值
        if (brand.getStatus() == null) {
            brand.setStatus(1); // 默认启用
        }
        if (brand.getIsRecommend() == null) {
            brand.setIsRecommend(0); // 默认不推荐
        }
        if (brand.getSortOrder() == null) {
            Integer maxSortOrder = brandMapper.getMaxSortOrder();
            brand.setSortOrder(maxSortOrder == null ? 1 : maxSortOrder + 1);
        }
        
        return brandMapper.insert(brand) > 0;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean updateBrand(Brand brand) {
        if (brand == null || brand.getBrandId() == null) {
            throw new BusinessException("品牌信息不能为空");
        }
        
        // 校验品牌是否存在
        Brand existBrand = getById(brand.getBrandId());
        if (existBrand == null) {
            throw new BusinessException("品牌不存在");
        }
        
        // 校验品牌编码是否重复
        if (StrUtil.isNotBlank(brand.getBrandCode()) && 
            existsByBrandCode(brand.getBrandCode(), brand.getBrandId())) {
            throw new BusinessException("品牌编码已存在");
        }
        
        // 校验品牌名称是否重复
        if (StrUtil.isNotBlank(brand.getBrandName()) && 
            existsByBrandName(brand.getBrandName(), brand.getBrandId())) {
            throw new BusinessException("品牌名称已存在");
        }
        
        return brandMapper.updateById(brand) > 0;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean deleteBrand(Long brandId) {
        if (brandId == null) {
            throw new BusinessException("品牌ID不能为空");
        }
        
        Brand brand = getById(brandId);
        if (brand == null) {
            throw new BusinessException("品牌不存在");
        }
        
        // 检查是否有商品使用该品牌
        if (hasProducts(brandId)) {
            throw new BusinessException("该品牌下存在商品，无法删除");
        }
        
        return brandMapper.deleteById(brandId) > 0;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean batchDeleteBrands(List<Long> brandIds) {
        if (brandIds == null || brandIds.isEmpty()) {
            throw new BusinessException("品牌ID列表不能为空");
        }
        
        // 检查每个品牌是否可以删除
        for (Long brandId : brandIds) {
            if (hasProducts(brandId)) {
                throw new BusinessException("品牌ID: " + brandId + " 下存在商品，无法删除");
            }
        }
        
        return brandMapper.deleteBatchIds(brandIds) > 0;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean enableBrand(Long brandId) {
        return updateStatus(brandId, 1);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean disableBrand(Long brandId) {
        return updateStatus(brandId, 0);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean batchUpdateStatus(List<Long> brandIds, Integer status) {
        if (brandIds == null || brandIds.isEmpty()) {
            throw new BusinessException("品牌ID列表不能为空");
        }
        if (status == null) {
            throw new BusinessException("状态不能为空");
        }
        
        String updateBy = StpUtil.isLogin() ? StpUtil.getLoginIdAsString() : "system";
        return brandMapper.batchUpdateStatus(brandIds, status, updateBy) > 0;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean updateSortOrder(Long brandId, Integer sortOrder) {
        if (brandId == null) {
            throw new BusinessException("品牌ID不能为空");
        }
        if (sortOrder == null) {
            throw new BusinessException("排序值不能为空");
        }
        
        Brand brand = new Brand();
        brand.setBrandId(brandId);
        brand.setSortOrder(sortOrder);
        
        return brandMapper.updateById(brand) > 0;
    }

    @Override
    public boolean existsByBrandCode(String brandCode, Long excludeId) {
        if (StrUtil.isBlank(brandCode)) {
            return false;
        }
        return brandMapper.existsByBrandCode(brandCode, excludeId);
    }

    @Override
    public boolean existsByBrandName(String brandName, Long excludeId) {
        if (StrUtil.isBlank(brandName)) {
            return false;
        }
        return brandMapper.existsByBrandName(brandName, excludeId);
    }

    @Override
    public boolean hasProducts(Long brandId) {
        if (brandId == null) {
            return false;
        }
        return brandMapper.hasProducts(brandId);
    }

    @Override
    public Integer getProductCount(Long brandId) {
        if (brandId == null) {
            return 0;
        }
        return brandMapper.getProductCount(brandId);
    }

    /**
     * 更新品牌状态
     */
    private boolean updateStatus(Long brandId, Integer status) {
        if (brandId == null) {
            throw new BusinessException("品牌ID不能为空");
        }
        if (status == null) {
            throw new BusinessException("状态不能为空");
        }
        
        Brand brand = new Brand();
        brand.setBrandId(brandId);
        brand.setStatus(status);
        
        return brandMapper.updateById(brand) > 0;
    }
}