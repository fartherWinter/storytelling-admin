package com.chennian.storytelling.service.mall.Impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.chennian.storytelling.bean.model.mall.MallProduct;
import com.chennian.storytelling.common.utils.PageParam;
import com.chennian.storytelling.dao.MallProductMapper;
import com.chennian.storytelling.service.mall.MallProductService;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.time.LocalDateTime;
import java.util.List;

/**
 * 商城商品Service实现类
 * 
 * @author chennian
 * @date 2025-01-27
 */
@Service
public class MallProductServiceImpl extends ServiceImpl<MallProductMapper, MallProduct> implements MallProductService {
    
    @Override
    public IPage<MallProduct> findByPage(PageParam<MallProduct> page, MallProduct mallProduct) {
        Page<MallProduct> pageInfo = new Page<>(page.getCurrent(), page.getSize());
        LambdaQueryWrapper<MallProduct> queryWrapper = new LambdaQueryWrapper<>();
        
        if (mallProduct != null) {
            // 商品名称模糊查询
            if (StringUtils.hasText(mallProduct.getProductName())) {
                queryWrapper.like(MallProduct::getProductName, mallProduct.getProductName());
            }
            // 商品编码精确查询
            if (StringUtils.hasText(mallProduct.getProductCode())) {
                queryWrapper.eq(MallProduct::getProductCode, mallProduct.getProductCode());
            }
            // 分类ID查询
            if (mallProduct.getCategoryId() != null) {
                queryWrapper.eq(MallProduct::getCategoryId, mallProduct.getCategoryId());
            }
            // 品牌ID查询
            if (mallProduct.getBrandId() != null) {
                queryWrapper.eq(MallProduct::getBrandId, mallProduct.getBrandId());
            }
            // 状态查询
            if (mallProduct.getStatus() != null) {
                queryWrapper.eq(MallProduct::getStatus, mallProduct.getStatus());
            }
        }
        
        queryWrapper.orderByDesc(MallProduct::getSort, MallProduct::getCreateTime);
        return this.page(pageInfo, queryWrapper);
    }
    
    @Override
    public MallProduct selectProductById(Long productId) {
        return this.getById(productId);
    }
    
    @Override
    public IPage<MallProduct> selectProductsByCategoryId(Long categoryId, PageParam<MallProduct> page) {
        Page<MallProduct> pageInfo = new Page<>(page.getCurrent(), page.getSize());
        LambdaQueryWrapper<MallProduct> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(MallProduct::getCategoryId, categoryId)
                   .eq(MallProduct::getStatus, 1) // 只查询上架商品
                   .orderByDesc(MallProduct::getSort, MallProduct::getCreateTime);
        return this.page(pageInfo, queryWrapper);
    }
    
    @Override
    public IPage<MallProduct> searchProducts(String keyword, PageParam<MallProduct> page) {
        Page<MallProduct> pageInfo = new Page<>(page.getCurrent(), page.getSize());
        LambdaQueryWrapper<MallProduct> queryWrapper = new LambdaQueryWrapper<>();
        
        if (StringUtils.hasText(keyword)) {
            queryWrapper.and(wrapper -> wrapper
                .like(MallProduct::getProductName, keyword)
                .or()
                .like(MallProduct::getKeywords, keyword)
                .or()
                .like(MallProduct::getDescription, keyword)
            );
        }
        
        queryWrapper.eq(MallProduct::getStatus, 1) // 只搜索上架商品
                   .orderByDesc(MallProduct::getSort, MallProduct::getSales, MallProduct::getCreateTime);
        
        return this.page(pageInfo, queryWrapper);
    }
    
    @Override
    public List<MallProduct> getRecommendProducts(Integer limit) {
        LambdaQueryWrapper<MallProduct> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(MallProduct::getStatus, 1)
                   .eq(MallProduct::getIsRecommend, 1)
                   .orderByDesc(MallProduct::getSort, MallProduct::getSales)
                   .last("LIMIT " + (limit != null ? limit : 10));
        return this.list(queryWrapper);
    }
    
    @Override
    public List<MallProduct> getHotProducts(Integer limit) {
        LambdaQueryWrapper<MallProduct> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(MallProduct::getStatus, 1)
                   .eq(MallProduct::getIsHot, 1)
                   .orderByDesc(MallProduct::getSales, MallProduct::getSort)
                   .last("LIMIT " + (limit != null ? limit : 10));
        return this.list(queryWrapper);
    }
    
    @Override
    public List<MallProduct> getNewProducts(Integer limit) {
        LambdaQueryWrapper<MallProduct> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(MallProduct::getStatus, 1)
                   .eq(MallProduct::getIsNew, 1)
                   .orderByDesc(MallProduct::getCreateTime, MallProduct::getSort)
                   .last("LIMIT " + (limit != null ? limit : 10));
        return this.list(queryWrapper);
    }
    
    @Override
    public int createProduct(MallProduct mallProduct) {
        mallProduct.setCreateTime(LocalDateTime.now());
        mallProduct.setUpdateTime(LocalDateTime.now());
        return this.save(mallProduct) ? 1 : 0;
    }
    
    @Override
    public int updateProduct(MallProduct mallProduct) {
        mallProduct.setUpdateTime(LocalDateTime.now());
        return this.updateById(mallProduct) ? 1 : 0;
    }
    
    @Override
    public int deleteProduct(Long productId) {
        return this.removeById(productId) ? 1 : 0;
    }
    
    @Override
    public int onShelf(Long productId) {
        MallProduct product = new MallProduct();
        product.setId(productId);
        product.setStatus(1); // 上架
        product.setUpdateTime(LocalDateTime.now());
        return this.updateById(product) ? 1 : 0;
    }
    
    @Override
    public int offShelf(Long productId) {
        MallProduct product = new MallProduct();
        product.setId(productId);
        product.setStatus(0); // 下架
        product.setUpdateTime(LocalDateTime.now());
        return this.updateById(product) ? 1 : 0;
    }
    
    @Override
    public int updateStock(Long productId, Integer quantity) {
        MallProduct product = this.getById(productId);
        if (product != null) {
            int newStock = product.getStock() + quantity;
            if (newStock < 0) {
                newStock = 0;
            }
            
            MallProduct updateProduct = new MallProduct();
            updateProduct.setId(productId);
            updateProduct.setStock(newStock);
            updateProduct.setUpdateTime(LocalDateTime.now());
            
            // 如果库存为0，设置为售罄状态
            if (newStock == 0) {
                updateProduct.setStatus(2);
            } else if (product.getStatus() == 2 && newStock > 0) {
                // 如果之前是售罄状态，现在有库存了，设置为上架状态
                updateProduct.setStatus(1);
            }
            
            return this.updateById(updateProduct) ? 1 : 0;
        }
        return 0;
    }
    
    @Override
    public int increaseViews(Long productId) {
        MallProduct product = this.getById(productId);
        if (product != null) {
            MallProduct updateProduct = new MallProduct();
            updateProduct.setId(productId);
            updateProduct.setViews(product.getViews() + 1);
            updateProduct.setUpdateTime(LocalDateTime.now());
            return this.updateById(updateProduct) ? 1 : 0;
        }
        return 0;
    }
    
    @Override
    public int increaseSales(Long productId, Integer quantity) {
        MallProduct product = this.getById(productId);
        if (product != null) {
            MallProduct updateProduct = new MallProduct();
            updateProduct.setId(productId);
            updateProduct.setSales(product.getSales() + quantity);
            updateProduct.setUpdateTime(LocalDateTime.now());
            return this.updateById(updateProduct) ? 1 : 0;
        }
        return 0;
    }
}