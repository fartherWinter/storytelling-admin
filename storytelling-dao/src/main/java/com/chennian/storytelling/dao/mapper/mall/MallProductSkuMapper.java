package com.chennian.storytelling.dao.mapper.mall;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.chennian.storytelling.bean.model.mall.MallProductSku;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

/**
 * 商品SKU Mapper接口
 * 
 * @author chennian
 * @date 2025-01-27
 */
@Mapper
public interface MallProductSkuMapper extends BaseMapper<MallProductSku> {
    
    /**
     * 分页查询SKU列表
     * 
     * @param page 分页参数
     * @param sku 查询条件
     * @return SKU分页数据
     */
    IPage<MallProductSku> selectSkuPage(Page<MallProductSku> page, @Param("sku") MallProductSku sku);
    
    /**
     * 根据商品ID获取SKU列表
     * 
     * @param productId 商品ID
     * @return SKU列表
     */
    List<MallProductSku> selectSkusByProductId(@Param("productId") Long productId);
    
    /**
     * 根据商品ID获取启用的SKU列表
     * 
     * @param productId 商品ID
     * @return 启用的SKU列表
     */
    List<MallProductSku> selectEnabledSkusByProductId(@Param("productId") Long productId);
    
    /**
     * 根据SKU编码获取SKU
     * 
     * @param skuCode SKU编码
     * @return SKU信息
     */
    MallProductSku selectSkuByCode(@Param("skuCode") String skuCode);
    
    /**
     * 批量更新SKU状态
     * 
     * @param skuIds SKU ID列表
     * @param status 状态
     * @return 更新数量
     */
    int batchUpdateSkuStatus(@Param("skuIds") List<Long> skuIds, @Param("status") Integer status);
    
    /**
     * 更新SKU库存
     * 
     * @param skuId SKU ID
     * @param stock 库存数量
     * @return 更新数量
     */
    int updateSkuStock(@Param("skuId") Long skuId, @Param("stock") Integer stock);
    
    /**
     * 批量更新SKU库存
     * 
     * @param stockUpdates 库存更新数据
     * @return 更新数量
     */
    int batchUpdateSkuStock(@Param("stockUpdates") List<Map<String, Object>> stockUpdates);
    
    /**
     * 更新SKU价格
     * 
     * @param skuId SKU ID
     * @param salePrice 销售价格
     * @return 更新数量
     */
    int updateSkuPrice(@Param("skuId") Long skuId, @Param("salePrice") BigDecimal salePrice);
    
    /**
     * 批量更新SKU价格
     * 
     * @param priceUpdates 价格更新数据
     * @return 更新数量
     */
    int batchUpdateSkuPrice(@Param("priceUpdates") List<Map<String, Object>> priceUpdates);
    
    /**
     * 获取库存预警SKU列表
     * 
     * @return 库存预警SKU列表
     */
    List<MallProductSku> selectStockWarningSku();
    
    /**
     * 获取SKU销售统计
     * 
     * @param skuId SKU ID
     * @return 销售统计数据
     */
    Map<String, Object> selectSkuSalesStats(@Param("skuId") Long skuId);
    
    /**
     * 根据商品ID获取默认SKU
     * 
     * @param productId 商品ID
     * @return 默认SKU
     */
    MallProductSku selectDefaultSkuByProductId(@Param("productId") Long productId);
    
    /**
     * 清除商品的默认SKU标记
     * 
     * @param productId 商品ID
     * @return 更新数量
     */
    int clearDefaultSkuByProductId(@Param("productId") Long productId);
    
    /**
     * 设置默认SKU
     * 
     * @param skuId SKU ID
     * @return 更新数量
     */
    int setDefaultSku(@Param("skuId") Long skuId);
    
    /**
     * 根据商品ID删除SKU
     * 
     * @param productId 商品ID
     * @return 删除数量
     */
    int deleteSkusByProductId(@Param("productId") Long productId);
}