package com.chennian.storytelling.product.service.impl;

import cn.dev33.satoken.stp.StpUtil;
import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.chennian.storytelling.common.exception.BusinessException;
import com.chennian.storytelling.product.entity.Product;
import com.chennian.storytelling.product.mapper.ProductMapper;
import com.chennian.storytelling.product.service.ProductService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

/**
 * 商品服务实现类
 * 
 * @author chennian
 * @since 2024-01-01
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class ProductServiceImpl implements ProductService {

    private final ProductMapper productMapper;

    @Override
    public Product getById(Long productId) {
        if (productId == null) {
            throw new BusinessException("商品ID不能为空");
        }
        return productMapper.selectById(productId);
    }

    @Override
    public Product getByProductCode(String productCode) {
        if (StrUtil.isBlank(productCode)) {
            throw new BusinessException("商品编码不能为空");
        }
        return productMapper.selectByProductCode(productCode);
    }

    @Override
    public IPage<Product> getProductPage(Page<Product> page, String productName, Long categoryId, 
                                       Long brandId, Integer status, BigDecimal minPrice, 
                                       BigDecimal maxPrice, Integer isRecommend, 
                                       Integer isHot, Integer isNew) {
        return productMapper.selectProductPage(page, productName, categoryId, brandId, 
                                             status, minPrice, maxPrice, isRecommend, isHot, isNew);
    }

    @Override
    public List<Product> getByCategoryId(Long categoryId, Integer status) {
        if (categoryId == null) {
            throw new BusinessException("分类ID不能为空");
        }
        return productMapper.selectByCategoryId(categoryId, status);
    }

    @Override
    public List<Product> getByBrandId(Long brandId, Integer status) {
        if (brandId == null) {
            throw new BusinessException("品牌ID不能为空");
        }
        return productMapper.selectByBrandId(brandId, status);
    }

    @Override
    public List<Product> getRecommendProducts(Integer limit) {
        if (limit == null || limit <= 0) {
            limit = 10;
        }
        return productMapper.selectRecommendProducts(limit);
    }

    @Override
    public List<Product> getHotProducts(Integer limit) {
        if (limit == null || limit <= 0) {
            limit = 10;
        }
        return productMapper.selectHotProducts(limit);
    }

    @Override
    public List<Product> getNewProducts(Integer limit) {
        if (limit == null || limit <= 0) {
            limit = 10;
        }
        return productMapper.selectNewProducts(limit);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean createProduct(Product product) {
        if (product == null) {
            throw new BusinessException("商品信息不能为空");
        }
        
        // 校验商品编码是否重复
        if (existsByProductCode(product.getProductCode(), null)) {
            throw new BusinessException("商品编码已存在");
        }
        
        // 校验商品名称是否重复
        if (existsByProductName(product.getProductName(), null)) {
            throw new BusinessException("商品名称已存在");
        }
        
        // 设置默认值
        if (product.getStatus() == null) {
            product.setStatus(0); // 默认下架
        }
        if (product.getIsRecommend() == null) {
            product.setIsRecommend(0);
        }
        if (product.getIsHot() == null) {
            product.setIsHot(0);
        }
        if (product.getIsNew() == null) {
            product.setIsNew(0);
        }
        if (product.getSalesCount() == null) {
            product.setSalesCount(0);
        }
        if (product.getViewCount() == null) {
            product.setViewCount(0);
        }
        if (product.getFavoriteCount() == null) {
            product.setFavoriteCount(0);
        }
        if (product.getCommentCount() == null) {
            product.setCommentCount(0);
        }
        if (product.getRating() == null) {
            product.setRating(BigDecimal.ZERO);
        }
        if (product.getSortOrder() == null) {
            product.setSortOrder(0);
        }
        
        return productMapper.insert(product) > 0;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean updateProduct(Product product) {
        if (product == null || product.getProductId() == null) {
            throw new BusinessException("商品信息不能为空");
        }
        
        // 校验商品是否存在
        Product existProduct = getById(product.getProductId());
        if (existProduct == null) {
            throw new BusinessException("商品不存在");
        }
        
        // 校验商品编码是否重复
        if (StrUtil.isNotBlank(product.getProductCode()) && 
            existsByProductCode(product.getProductCode(), product.getProductId())) {
            throw new BusinessException("商品编码已存在");
        }
        
        // 校验商品名称是否重复
        if (StrUtil.isNotBlank(product.getProductName()) && 
            existsByProductName(product.getProductName(), product.getProductId())) {
            throw new BusinessException("商品名称已存在");
        }
        
        return productMapper.updateById(product) > 0;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean deleteProduct(Long productId) {
        if (productId == null) {
            throw new BusinessException("商品ID不能为空");
        }
        
        Product product = getById(productId);
        if (product == null) {
            throw new BusinessException("商品不存在");
        }
        
        return productMapper.deleteById(productId) > 0;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean batchDeleteProducts(List<Long> productIds) {
        if (productIds == null || productIds.isEmpty()) {
            throw new BusinessException("商品ID列表不能为空");
        }
        
        return productMapper.deleteBatchIds(productIds) > 0;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean updateStatus(Long productId, Integer status) {
        if (productId == null) {
            throw new BusinessException("商品ID不能为空");
        }
        if (status == null) {
            throw new BusinessException("状态不能为空");
        }
        
        Product product = new Product();
        product.setProductId(productId);
        product.setStatus(status);
        
        return productMapper.updateById(product) > 0;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean batchUpdateStatus(List<Long> productIds, Integer status) {
        if (productIds == null || productIds.isEmpty()) {
            throw new BusinessException("商品ID列表不能为空");
        }
        if (status == null) {
            throw new BusinessException("状态不能为空");
        }
        
        String updateBy = StpUtil.isLogin() ? StpUtil.getLoginIdAsString() : "system";
        return productMapper.batchUpdateStatus(productIds, status, updateBy) > 0;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean publishProduct(Long productId) {
        if (productId == null) {
            throw new BusinessException("商品ID不能为空");
        }
        
        Product product = new Product();
        product.setProductId(productId);
        product.setStatus(1); // 上架
        product.setPublishTime(LocalDateTime.now());
        
        return productMapper.updateById(product) > 0;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean unpublishProduct(Long productId) {
        if (productId == null) {
            throw new BusinessException("商品ID不能为空");
        }
        
        Product product = new Product();
        product.setProductId(productId);
        product.setStatus(0); // 下架
        product.setUnpublishTime(LocalDateTime.now());
        
        return productMapper.updateById(product) > 0;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean batchPublishProducts(List<Long> productIds) {
        if (productIds == null || productIds.isEmpty()) {
            throw new BusinessException("商品ID列表不能为空");
        }
        
        String updateBy = StpUtil.isLogin() ? StpUtil.getLoginIdAsString() : "system";
        return productMapper.batchUpdateStatus(productIds, 1, updateBy) > 0;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean batchUnpublishProducts(List<Long> productIds) {
        if (productIds == null || productIds.isEmpty()) {
            throw new BusinessException("商品ID列表不能为空");
        }
        
        String updateBy = StpUtil.isLogin() ? StpUtil.getLoginIdAsString() : "system";
        return productMapper.batchUpdateStatus(productIds, 0, updateBy) > 0;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean updateSalesCount(Long productId, Integer quantity) {
        if (productId == null) {
            throw new BusinessException("商品ID不能为空");
        }
        if (quantity == null || quantity <= 0) {
            throw new BusinessException("销量增量必须大于0");
        }
        
        return productMapper.updateSalesCount(productId, quantity) > 0;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean updateViewCount(Long productId) {
        if (productId == null) {
            throw new BusinessException("商品ID不能为空");
        }
        
        return productMapper.updateViewCount(productId) > 0;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean updateFavoriteCount(Long productId, Integer increment) {
        if (productId == null) {
            throw new BusinessException("商品ID不能为空");
        }
        if (increment == null || increment == 0) {
            throw new BusinessException("收藏量增量不能为0");
        }
        
        return productMapper.updateFavoriteCount(productId, increment) > 0;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean updateCommentCount(Long productId, Integer increment) {
        if (productId == null) {
            throw new BusinessException("商品ID不能为空");
        }
        if (increment == null || increment == 0) {
            throw new BusinessException("评论数增量不能为0");
        }
        
        return productMapper.updateCommentCount(productId, increment) > 0;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean updateRating(Long productId, BigDecimal rating) {
        if (productId == null) {
            throw new BusinessException("商品ID不能为空");
        }
        if (rating == null) {
            throw new BusinessException("评分不能为空");
        }
        
        return productMapper.updateRating(productId, rating) > 0;
    }

    @Override
    public IPage<Product> searchProducts(Page<Product> page, String keyword, Long categoryId, 
                                       Long brandId, BigDecimal minPrice, BigDecimal maxPrice, 
                                       String sortField, String sortOrder) {
        return productMapper.searchProducts(page, keyword, categoryId, brandId, 
                                          minPrice, maxPrice, sortField, sortOrder);
    }

    @Override
    public boolean existsByProductCode(String productCode, Long excludeId) {
        if (StrUtil.isBlank(productCode)) {
            return false;
        }
        return productMapper.existsByProductCode(productCode, excludeId);
    }

    @Override
    public boolean existsByProductName(String productName, Long excludeId) {
        if (StrUtil.isBlank(productName)) {
            return false;
        }
        return productMapper.existsByProductName(productName, excludeId);
    }
}