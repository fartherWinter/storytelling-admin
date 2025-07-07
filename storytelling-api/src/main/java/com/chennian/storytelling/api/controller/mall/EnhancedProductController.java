package com.chennian.storytelling.api.controller.mall;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.chennian.storytelling.bean.model.mall.*;
import com.chennian.storytelling.service.mall.*;
import com.chennian.storytelling.common.response.ServerResponseEntity;
import com.chennian.storytelling.common.utils.PageParam;
import com.chennian.storytelling.common.enums.MallResponseEnum;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 增强商品管理Controller
 * 提供完整的电商商品管理功能
 * 
 * @author chennian
 * @date 2025-01-27
 */
@Api(tags = "增强商品管理")
@RestController
@RequestMapping("/mall/enhanced-product")
public class EnhancedProductController {

    @Autowired
    private MallProductService mallProductService;
    
    @Autowired
    private MallBrandService mallBrandService;
    
    @Autowired
    private MallProductAttributeService mallProductAttributeService;
    
    @Autowired
    private MallProductAttributeValueService mallProductAttributeValueService;
    
    @Autowired
    private MallProductSkuService mallProductSkuService;
    
    @Autowired
    private MallProductReviewService mallProductReviewService;
    
    @Autowired
    private MallProductFavoriteService mallProductFavoriteService;

    // ==================== 商品基础管理 ====================
    
    /**
     * 获取商品详情（包含SKU、属性、评价统计）
     */
    @ApiOperation("获取商品完整详情")
    @GetMapping("/{productId}/detail")
    public ServerResponseEntity<Map<String, Object>> getProductDetail(
            @ApiParam("商品ID") @PathVariable Long productId) {
        Map<String, Object> result = new HashMap<>();
        
        // 获取商品基本信息
        MallProduct product = mallProductService.getById(productId);
        if (product == null) {
            return ServerResponseEntity.fail(MallResponseEnum.PRODUCT_NOT_FOUND);
        }
        result.put("product", product);
        
        // 添加SKU列表
        List<MallProductSku> skuList = mallProductSkuService.getSkusByProductId(productId);
        result.put("skuList", skuList);
        
        // 添加属性值
        List<MallProductAttributeValue> attributes = mallProductAttributeValueService.getAttributeValuesByProductId(productId);
        result.put("attributes", attributes);
        
        // 添加评价统计
        Map<String, Object> reviewStats = mallProductReviewService.getProductReviewStats(productId);
        result.put("reviewStats", reviewStats);
        
        return ServerResponseEntity.success(result);
    }
    
    /**
     * 多条件搜索商品
     */
    @ApiOperation("多条件搜索商品")
    @PostMapping("/search/advanced")
    public ServerResponseEntity<IPage<MallProduct>> advancedSearch(
            @RequestBody Map<String, Object> searchParams) {
        // 从searchParams中提取分页参数
        Integer current = (Integer) searchParams.getOrDefault("current", 1);
        Integer size = (Integer) searchParams.getOrDefault("size", 10);
        String keyword = (String) searchParams.get("keyword");
        
        PageParam<MallProduct> page = new PageParam<>();
        page.setCurrent(current.longValue());
        page.setSize(size.longValue());
        
        try {
            // 实现完整的多条件搜索逻辑
            IPage<MallProduct> result = mallProductService.advancedSearchProducts(searchParams, page);
            return ServerResponseEntity.success(result);
        } catch (Exception e) {
            return ServerResponseEntity.fail(MallResponseEnum.ENHANCED_PRODUCT_SEARCH_FAIL);
        }
    }
    
    /**
     * 获取商品推荐（基于用户行为）
     */
    @ApiOperation("获取个性化推荐商品")
    @GetMapping("/recommend/personalized")
    public ServerResponseEntity<List<MallProduct>> getPersonalizedRecommendations(
            @ApiParam("用户ID") @RequestParam Long userId,
            @ApiParam("推荐数量") @RequestParam(defaultValue = "10") Integer limit) {
        try {
            // 基于用户行为数据实现个性化推荐算法
            List<MallProduct> recommendations = mallProductService.getPersonalizedRecommendations(userId, limit);
            return ServerResponseEntity.success(recommendations);
        } catch (Exception e) {
            return ServerResponseEntity.fail(MallResponseEnum.ENHANCED_PRODUCT_RECOMMEND_FAIL);
        }
    }
    
    /**
     * 获取相关商品推荐
     */
    @ApiOperation("获取相关商品推荐")
    @GetMapping("/{productId}/related")
    public ServerResponseEntity<List<MallProduct>> getRelatedProducts(
            @ApiParam("商品ID") @PathVariable Long productId,
            @ApiParam("推荐数量") @RequestParam(defaultValue = "10") Integer limit) {
        // 获取当前商品信息
        MallProduct currentProduct = mallProductService.getById(productId);
        if (currentProduct == null) {
            return ServerResponseEntity.fail(MallResponseEnum.PRODUCT_NOT_FOUND);
        }
        
        // 根据分类和品牌推荐相关商品
        List<MallProduct> relatedProducts = mallProductService.getRelatedProducts(productId, limit);
        return ServerResponseEntity.success(relatedProducts);
    }

    // ==================== 品牌管理 ====================
    
    /**
     * 获取品牌列表
     */
    @ApiOperation("获取品牌列表")
    @PostMapping("/brand/list")
    public ServerResponseEntity<IPage<MallBrand>> getBrandList(
            @RequestBody PageParam<MallBrand> page,
            @RequestBody MallBrand brand) {
        try {
            IPage<MallBrand> result = mallBrandService.findByPage(page, brand);
            return ServerResponseEntity.success(result);
        } catch (Exception e) {
            return ServerResponseEntity.fail(MallResponseEnum.ENHANCED_PRODUCT_BRAND_LIST_FAIL);
        }
    }
    
    /**
     * 创建品牌
     */
    @ApiOperation("创建品牌")
    @PostMapping("/brand")
    public ServerResponseEntity<String> createBrand(@RequestBody MallBrand brand) {
        try {
            boolean success = mallBrandService.save(brand);
            if (success) {
                return ServerResponseEntity.success("品牌创建成功");
            } else {
                return ServerResponseEntity.fail(MallResponseEnum.BRAND_CREATE_FAIL);
            }
        } catch (Exception e) {
            return ServerResponseEntity.fail(MallResponseEnum.BRAND_CREATE_FAIL);
        }
    }
    
    /**
     * 更新品牌
     */
    @ApiOperation("更新品牌")
    @PutMapping("/brand")
    public ServerResponseEntity<String> updateBrand(@RequestBody MallBrand brand) {
        try {
            boolean success = mallBrandService.updateById(brand);
            if (success) {
                return ServerResponseEntity.success("品牌更新成功");
            } else {
                return ServerResponseEntity.fail(MallResponseEnum.BRAND_UPDATE_FAIL);
            }
        } catch (Exception e) {
            return ServerResponseEntity.fail(MallResponseEnum.BRAND_UPDATE_FAIL);
        }
    }

    // ==================== SKU管理 ====================
    
    /**
     * 获取商品SKU列表
     */
    @ApiOperation("获取商品SKU列表")
    @GetMapping("/{productId}/sku")
    public ServerResponseEntity<List<MallProductSku>> getProductSkus(
            @ApiParam("商品ID") @PathVariable Long productId) {
        try {
            List<MallProductSku> skuList = mallProductSkuService.getSkusByProductId(productId);
            return ServerResponseEntity.success(skuList);
        } catch (Exception e) {
            return ServerResponseEntity.fail(MallResponseEnum.ENHANCED_PRODUCT_SKU_LIST_FAIL);
        }
    }
    
    /**
     * 创建商品SKU
     */
    @ApiOperation("创建商品SKU")
    @PostMapping("/sku")
    public ServerResponseEntity<String> createSku(@RequestBody MallProductSku sku) {
        try {
            boolean success = mallProductSkuService.save(sku);
            if (success) {
                return ServerResponseEntity.success("SKU创建成功");
            } else {
                return ServerResponseEntity.fail(MallResponseEnum.SKU_CREATE_FAIL);
            }
        } catch (Exception e) {
            return ServerResponseEntity.fail(MallResponseEnum.SKU_CREATE_FAIL);
        }
    }
    
    /**
     * 批量更新SKU库存
     */
    @ApiOperation("批量更新SKU库存")
    @PutMapping("/sku/stock/batch")
    public ServerResponseEntity<String> batchUpdateSkuStock(
            @RequestBody List<Map<String, Object>> skuStockList) {
        try {
            for (Map<String, Object> stockUpdate : skuStockList) {
                Long skuId = Long.valueOf(stockUpdate.get("skuId").toString());
                Integer stock = Integer.valueOf(stockUpdate.get("stock").toString());
                mallProductSkuService.updateStock(skuId, stock);
            }
            return ServerResponseEntity.success("库存更新成功");
        } catch (Exception e) {
            return ServerResponseEntity.fail(MallResponseEnum.SKU_STOCK_UPDATE_FAIL);
        }
    }
    
    /**
     * 获取SKU价格
     */
    @ApiOperation("获取SKU价格")
    @GetMapping("/sku/{skuId}/price")
    public ServerResponseEntity<BigDecimal> getSkuPrice(
            @ApiParam("SKU ID") @PathVariable Long skuId) {
        try {
            MallProductSku sku = mallProductSkuService.getById(skuId);
            if (sku == null) {
                return ServerResponseEntity.fail(MallResponseEnum.SKU_NOT_FOUND);
            }
            return ServerResponseEntity.success(sku.getSalePrice());
        } catch (Exception e) {
            return ServerResponseEntity.fail(MallResponseEnum.ENHANCED_PRODUCT_SKU_PRICE_FAIL);
        }
    }

    // ==================== 属性管理 ====================
    
    /**
     * 获取商品属性列表
     */
    @ApiOperation("获取商品属性列表")
    @PostMapping("/attribute/list")
    public ServerResponseEntity<IPage<MallProductAttribute>> getAttributeList(
            @RequestBody PageParam<MallProductAttribute> page,
            @RequestBody MallProductAttribute attribute) {
        IPage<MallProductAttribute> result = mallProductAttributeService.findByPage(page, attribute);
        return ServerResponseEntity.success(result);
    }
    
    /**
     * 创建商品属性
     */
    @ApiOperation("创建商品属性")
    @PostMapping("/attribute")
    public ServerResponseEntity<String> createAttribute(@RequestBody MallProductAttribute attribute) {
        try {
            boolean success = mallProductAttributeService.createAttribute(attribute);
            if (success) {
                return ServerResponseEntity.success("属性创建成功");
            } else {
                return ServerResponseEntity.fail(MallResponseEnum.ATTRIBUTE_CREATE_FAIL);
            }
        } catch (Exception e) {
            return ServerResponseEntity.fail(MallResponseEnum.ATTRIBUTE_CREATE_FAIL);
        }
    }
    
    /**
     * 获取商品属性值
     */
    @ApiOperation("获取商品属性值")
    @GetMapping("/{productId}/attribute-values")
    public ServerResponseEntity<List<MallProductAttributeValue>> getProductAttributeValues(
            @ApiParam("商品ID") @PathVariable Long productId) {
        List<MallProductAttributeValue> attributeValues = mallProductAttributeValueService.getAttributeValuesByProductId(productId);
        return ServerResponseEntity.success(attributeValues);
    }
    
    /**
     * 设置商品属性值
     */
    @ApiOperation("设置商品属性值")
    @PostMapping("/{productId}/attribute-values")
    public ServerResponseEntity<String> setProductAttributeValues(
            @ApiParam("商品ID") @PathVariable Long productId,
            @RequestBody List<MallProductAttributeValue> attributeValues) {
        try {
            boolean success = mallProductAttributeValueService.setProductAttributeValues(productId, attributeValues);
            if (success) {
                return ServerResponseEntity.success("属性值设置成功");
            } else {
                return ServerResponseEntity.fail(MallResponseEnum.ATTRIBUTE_VALUE_SET_FAIL);
            }
        } catch (Exception e) {
            return ServerResponseEntity.fail(MallResponseEnum.ATTRIBUTE_VALUE_SET_FAIL);
        }
    }

    // ==================== 评价管理 ====================
    
    /**
     * 获取商品评价列表
     */
    @ApiOperation("获取商品评价列表")
    @PostMapping("/{productId}/reviews")
    public ServerResponseEntity<IPage<MallProductReview>> getProductReviews(
            @ApiParam("商品ID") @PathVariable Long productId,
            @RequestBody PageParam<MallProductReview> page,
            @ApiParam("评价类型") @RequestParam(required = false) Integer reviewType) {
        try {
            IPage<MallProductReview> result = mallProductReviewService.getReviewsByProductId(productId, page, reviewType, null);
            return ServerResponseEntity.success(result);
        } catch (Exception e) {
            return ServerResponseEntity.fail(MallResponseEnum.REVIEW_LIST_FAIL);
        }
    }
    
    /**
     * 获取商品评价统计
     */
    @ApiOperation("获取商品评价统计")
    @GetMapping("/{productId}/review-stats")
    public ServerResponseEntity<Map<String, Object>> getProductReviewStats(
            @ApiParam("商品ID") @PathVariable Long productId) {
        try {
            Map<String, Object> reviewStats = mallProductReviewService.getProductReviewStats(productId);
            return ServerResponseEntity.success(reviewStats);
        } catch (Exception e) {
            return ServerResponseEntity.fail(MallResponseEnum.ENHANCED_PRODUCT_REVIEW_STATS_FAIL);
        }
    }
    
    /**
     * 商家回复评价
     */
    @ApiOperation("商家回复评价")
    @PostMapping("/review/{reviewId}/reply")
    public ServerResponseEntity<String> replyReview(
            @ApiParam("评价ID") @PathVariable Long reviewId,
            @ApiParam("回复内容") @RequestParam String replyContent) {
        try {
            boolean success = mallProductReviewService.replyReview(reviewId, replyContent);
            if (success) {
                return ServerResponseEntity.success("回复成功");
            } else {
                return ServerResponseEntity.fail(MallResponseEnum.REVIEW_REPLY_FAIL);
            }
        } catch (Exception e) {
            return ServerResponseEntity.fail(MallResponseEnum.REVIEW_REPLY_FAIL);
        }
    }

    // ==================== 收藏管理 ====================
    
    /**
     * 添加商品收藏
     */
    @ApiOperation("添加商品收藏")
    @PostMapping("/favorite")
    public ServerResponseEntity<String> addFavorite(@RequestBody MallProductFavorite favorite) {
        try {
            boolean success = mallProductFavoriteService.addFavorite(
                    favorite.getUserId(), 
                    favorite.getProductId(), 
                    favorite.getGroupId()
            );
            if (success) {
                return ServerResponseEntity.success("收藏成功");
            } else {
                return ServerResponseEntity.fail(MallResponseEnum.FAVORITE_ADD_FAIL);
            }
        } catch (Exception e) {
            return ServerResponseEntity.fail(MallResponseEnum.FAVORITE_ADD_FAIL);
        }
    }
    
    /**
     * 取消商品收藏
     */
    @ApiOperation("取消商品收藏")
    @DeleteMapping("/favorite/{userId}/{productId}")
    public ServerResponseEntity<String> removeFavorite(
            @ApiParam("用户ID") @PathVariable Long userId,
            @ApiParam("商品ID") @PathVariable Long productId) {
        try {
            boolean success = mallProductFavoriteService.removeFavorite(userId, productId);
            if (success) {
                return ServerResponseEntity.success("取消收藏成功");
            } else {
                return ServerResponseEntity.fail(MallResponseEnum.FAVORITE_REMOVE_FAIL);
            }
        } catch (Exception e) {
            return ServerResponseEntity.fail(MallResponseEnum.FAVORITE_REMOVE_FAIL);
        }
    }
    
    /**
     * 获取用户收藏列表
     */
    @ApiOperation("获取用户收藏列表")
    @PostMapping("/favorite/{userId}/list")
    public ServerResponseEntity<IPage<MallProductFavorite>> getUserFavorites(
            @ApiParam("用户ID") @PathVariable Long userId,
            @RequestBody PageParam<MallProductFavorite> page) {
        try {
            IPage<MallProductFavorite> result = mallProductFavoriteService.getUserFavorites(
                    userId, 
                    null, 
                    page.getCurrent(), 
                    page.getSize()
            );
            return ServerResponseEntity.success(result);
        } catch (Exception e) {
            return ServerResponseEntity.fail(MallResponseEnum.ENHANCED_PRODUCT_FAVORITE_LIST_FAIL);
        }
    }

    // ==================== 库存管理 ====================
    
    /**
     * 获取库存预警商品
     */
    @ApiOperation("获取库存预警商品")
    @PostMapping("/stock/warning")
    public ServerResponseEntity<IPage<MallProduct>> getStockWarningProducts(
            @RequestBody PageParam<MallProduct> page) {
        // 查询库存低于预警值的商品
        IPage<MallProduct> productPage = new Page<>(page.getCurrent(), page.getSize());
        LambdaQueryWrapper<MallProduct> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.le(MallProduct::getStock, 10) // 库存预警阈值为10
                   .eq(MallProduct::getStatus, 1)
                   .orderByAsc(MallProduct::getStock);
        
        IPage<MallProduct> result = mallProductService.page(productPage, queryWrapper);
        return ServerResponseEntity.success(result);
    }
    
    /**
     * 批量调整商品价格
     */
    @ApiOperation("批量调整商品价格")
    @PutMapping("/price/batch")
    public ServerResponseEntity<String> batchUpdatePrice(
            @RequestBody List<Map<String, Object>> priceUpdateList) {
        try {
            for (Map<String, Object> priceUpdate : priceUpdateList) {
                Long productId = Long.valueOf(priceUpdate.get("productId").toString());
                BigDecimal price = new BigDecimal(priceUpdate.get("price").toString());
                
                MallProduct product = mallProductService.getById(productId);
                if (product != null) {
                    product.setSalePrice(price);
                    mallProductService.updateById(product);
                }
            }
            return ServerResponseEntity.success("价格调整成功");
        } catch (Exception e) {
            return ServerResponseEntity.fail(MallResponseEnum.ENHANCED_PRODUCT_PRICE_ADJUST_FAIL);
        }
    }
    
    /**
     * 获取商品销售统计
     */
    @ApiOperation("获取商品销售统计")
    @GetMapping("/{productId}/sales-stats")
    public ServerResponseEntity<Map<String, Object>> getProductSalesStats(
            @ApiParam("商品ID") @PathVariable Long productId,
            @ApiParam("统计开始日期") @RequestParam String startDate,
            @ApiParam("统计结束日期") @RequestParam String endDate) {
        try {
            MallProduct product = mallProductService.getById(productId);
            if (product == null) {
                return ServerResponseEntity.fail(MallResponseEnum.PRODUCT_NOT_FOUND);
            }
            
            Map<String, Object> salesStats = new HashMap<>();
            salesStats.put("productId", productId);
            salesStats.put("productName", product.getProductName());
            salesStats.put("currentStock", product.getStock());
            
            // 从订单表统计实际销量和收入
            Map<String, Object> orderStats = mallProductService.getProductOrderStats(productId, startDate, endDate);
            salesStats.put("totalSales", orderStats.getOrDefault("totalSales", 0));
            salesStats.put("totalRevenue", orderStats.getOrDefault("totalRevenue", BigDecimal.ZERO));
            
            // 从评价表统计平均评分
            Map<String, Object> reviewStats = mallProductReviewService.getProductReviewStats(productId);
            salesStats.put("averageRating", reviewStats.getOrDefault("averageRating", 5.0));
            
            salesStats.put("startDate", startDate);
            salesStats.put("endDate", endDate);
            
            return ServerResponseEntity.success(salesStats);
        } catch (Exception e) {
            return ServerResponseEntity.fail(MallResponseEnum.ENHANCED_PRODUCT_SALES_STATS_FAIL);
        }
    }
}