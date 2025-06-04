package com.chennian.storytelling.service.mall;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.IService;
import com.chennian.storytelling.bean.model.mall.MallProduct;
import com.chennian.storytelling.common.utils.PageParam;

import java.util.List;

/**
 * 商城商品Service接口
 * 
 * @author chennian
 * @date 2025-01-27
 */
public interface MallProductService extends IService<MallProduct> {
    
    /**
     * 分页查询商品列表
     * 
     * @param page 分页参数
     * @param mallProduct 查询条件
     * @return 商品分页数据
     */
    IPage<MallProduct> findByPage(PageParam<MallProduct> page, MallProduct mallProduct);
    
    /**
     * 根据ID查询商品
     * 
     * @param productId 商品ID
     * @return 商品信息
     */
    MallProduct selectProductById(Long productId);
    
    /**
     * 根据分类ID查询商品列表
     * 
     * @param categoryId 分类ID
     * @param page 分页参数
     * @return 商品列表
     */
    IPage<MallProduct> selectProductsByCategoryId(Long categoryId, PageParam<MallProduct> page);
    
    /**
     * 搜索商品
     * 
     * @param keyword 搜索关键词
     * @param page 分页参数
     * @return 商品列表
     */
    IPage<MallProduct> searchProducts(String keyword, PageParam<MallProduct> page);
    
    /**
     * 获取推荐商品
     * 
     * @param limit 限制数量
     * @return 推荐商品列表
     */
    List<MallProduct> getRecommendProducts(Integer limit);
    
    /**
     * 获取热销商品
     * 
     * @param limit 限制数量
     * @return 热销商品列表
     */
    List<MallProduct> getHotProducts(Integer limit);
    
    /**
     * 获取新品商品
     * 
     * @param limit 限制数量
     * @return 新品商品列表
     */
    List<MallProduct> getNewProducts(Integer limit);
    
    /**
     * 创建商品
     * 
     * @param mallProduct 商品信息
     * @return 结果
     */
    int createProduct(MallProduct mallProduct);
    
    /**
     * 更新商品
     * 
     * @param mallProduct 商品信息
     * @return 结果
     */
    int updateProduct(MallProduct mallProduct);
    
    /**
     * 删除商品
     * 
     * @param productId 商品ID
     * @return 结果
     */
    int deleteProduct(Long productId);
    
    /**
     * 上架商品
     * 
     * @param productId 商品ID
     * @return 结果
     */
    int onShelf(Long productId);
    
    /**
     * 下架商品
     * 
     * @param productId 商品ID
     * @return 结果
     */
    int offShelf(Long productId);
    
    /**
     * 更新商品库存
     * 
     * @param productId 商品ID
     * @param quantity 库存变化量（正数增加，负数减少）
     * @return 结果
     */
    int updateStock(Long productId, Integer quantity);
    
    /**
     * 增加商品浏览量
     * 
     * @param productId 商品ID
     * @return 结果
     */
    int increaseViews(Long productId);
    
    /**
     * 增加商品销量
     * 
     * @param productId 商品ID
     * @param quantity 销量增加数量
     * @return 结果
     */
    int increaseSales(Long productId, Integer quantity);
}