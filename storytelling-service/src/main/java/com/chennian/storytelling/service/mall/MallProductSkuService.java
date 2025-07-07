package com.chennian.storytelling.service.mall;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.IService;
import com.chennian.storytelling.bean.model.mall.MallProductSku;
import com.chennian.storytelling.common.utils.PageParam;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

/**
 * 商品SKU Service接口
 * 
 * @author chennian
 * @date 2025-01-27
 */
public interface MallProductSkuService extends IService<MallProductSku> {
    
    /**
     * 分页查询SKU列表
     * 
     * @param page 分页参数
     * @param sku 查询条件
     * @return SKU分页数据
     */
    IPage<MallProductSku> findByPage(PageParam<MallProductSku> page, MallProductSku sku);
    
    /**
     * 分页查询SKU列表
     * 
     * @param page 分页参数
     * @param sku 查询条件
     * @return SKU分页数据
     */
    IPage<MallProductSku> getSkuPage(PageParam<MallProductSku> page, MallProductSku sku);
    
    /**
     * 根据商品ID获取SKU列表
     * 
     * @param productId 商品ID
     * @return SKU列表
     */
    List<MallProductSku> getSkusByProductId(Long productId);
    
    /**
     * 根据商品ID获取启用的SKU列表
     * 
     * @param productId 商品ID
     * @return 启用的SKU列表
     */
    List<MallProductSku> getEnabledSkusByProductId(Long productId);
    
    /**
     * 根据SKU编码获取SKU
     * 
     * @param skuCode SKU编码
     * @return SKU信息
     */
    MallProductSku getSkuByCode(String skuCode);
    
    /**
     * 根据SKU编码获取SKU
     * 
     * @param skuCode SKU编码
     * @return SKU信息
     */
    MallProductSku getBySkuCode(String skuCode);
    
    /**
     * 创建SKU
     * 
     * @param sku SKU信息
     * @return 是否成功
     */
    boolean createSku(MallProductSku sku);
    
    /**
     * 更新SKU
     * 
     * @param sku SKU信息
     * @return 是否成功
     */
    boolean updateSku(MallProductSku sku);
    
    /**
     * 删除SKU
     * 
     * @param skuId SKU ID
     * @return 是否成功
     */
    boolean deleteSku(Long skuId);
    
    /**
     * 批量删除SKU
     * 
     * @param skuIds SKU ID列表
     * @return 是否成功
     */
    boolean batchDeleteSkus(List<Long> skuIds);
    
    /**
     * 更新SKU状态
     * 
     * @param skuId SKU ID
     * @param status 状态
     * @return 是否成功
     */
    boolean updateSkuStatus(Long skuId, Integer status);
    
    /**
     * 更新SKU状态
     * 
     * @param skuId SKU ID
     * @param status 状态
     * @param username 操作用户
     * @return 是否成功
     */
    boolean updateSkuStatus(Long skuId, Integer status, String username);
    
    /**
     * 批量更新SKU状态
     * 
     * @param skuIds SKU ID列表
     * @param status 状态
     * @return 是否成功
     */
    boolean batchUpdateSkuStatus(List<Long> skuIds, Integer status);
    
    /**
     * 批量更新SKU状态
     * 
     * @param skuIds SKU ID列表
     * @param status 状态
     * @param username 操作用户
     * @return 是否成功
     */
    boolean batchUpdateSkuStatus(List<Long> skuIds, Integer status, String username);
    
    /**
     * 更新SKU库存
     * 
     * @param skuId SKU ID
     * @param stock 库存数量
     * @return 是否成功
     */
    boolean updateSkuStock(Long skuId, Integer stock);
    
    /**
     * 更新SKU库存
     * 
     * @param skuId SKU ID
     * @param stock 库存数量
     * @return 是否成功
     */
    boolean updateStock(Long skuId, Integer stock);
    
    /**
     * 更新SKU库存
     * 
     * @param skuId SKU ID
     * @param stockQuantity 库存数量
     * @param operationType 操作类型
     * @param remark 操作备注
     * @param username 操作用户
     * @return 是否成功
     */
    boolean updateSkuStock(Long skuId, Integer stockQuantity, String operationType, String remark, String username);
    
    /**
     * 批量更新SKU库存
     * 
     * @param stockUpdates 库存更新数据，key为SKU ID，value为库存数量
     * @return 是否成功
     */
    boolean batchUpdateSkuStock(Map<Long, Integer> stockUpdates);
    
    /**
     * 批量更新SKU库存
     * 
     * @param stockUpdates 库存更新数据列表
     * @param username 操作用户
     * @return 是否成功
     */
    boolean batchUpdateSkuStock(List<Map<String, Object>> stockUpdates, String username);
    
    /**
     * 更新SKU价格
     * 
     * @param skuId SKU ID
     * @param salePrice 销售价格
     * @return 是否成功
     */
    boolean updateSkuPrice(Long skuId, BigDecimal salePrice);
    
    /**
     * 更新SKU价格
     * 
     * @param skuId SKU ID
     * @param marketPrice 市场价格
     * @param salePrice 销售价格
     * @param costPrice 成本价格
     * @param username 操作用户
     * @return 是否成功
     */
    boolean updateSkuPrice(Long skuId, BigDecimal marketPrice, BigDecimal salePrice, BigDecimal costPrice, String username);
    
    /**
     * 批量更新SKU价格
     * 
     * @param priceUpdates 价格更新数据，key为SKU ID，value为销售价格
     * @return 是否成功
     */
    boolean batchUpdateSkuPrice(Map<Long, BigDecimal> priceUpdates);
    
    /**
     * 批量更新SKU价格
     * 
     * @param priceUpdates 价格更新数据列表
     * @param username 操作用户
     * @return 是否成功
     */
    boolean batchUpdateSkuPrice(List<Map<String, Object>> priceUpdates, String username);
    
    /**
     * 获取库存预警SKU列表
     * 
     * @return 库存预警SKU列表
     */
    List<MallProductSku> getStockWarningSku();
    
    /**
     * 获取库存预警SKU列表
     * 
     * @param page 分页参数
     * @return 库存预警SKU分页数据
     */
    IPage<MallProductSku> getStockAlertSkus(PageParam<MallProductSku> page);
    
    /**
     * 获取SKU销售统计
     * 
     * @param skuId SKU ID
     * @return 销售统计数据
     */
    Map<String, Object> getSkuSalesStats(Long skuId);
    
    /**
     * 获取SKU销售统计
     * 
     * @param productId 商品ID
     * @param startDate 开始日期
     * @param endDate 结束日期
     * @return 销售统计数据
     */
    Map<String, Object> getSkuSalesStats(Long productId, String startDate, String endDate);
    
    /**
     * 根据商品ID获取默认SKU
     * 
     * @param productId 商品ID
     * @return 默认SKU
     */
    MallProductSku getDefaultSkuByProductId(Long productId);
    
    /**
     * 设置默认SKU
     * 
     * @param productId 商品ID
     * @param skuId SKU ID
     * @return 是否成功
     */
    boolean setDefaultSku(Long productId, Long skuId);
    
    /**
     * 设置默认SKU
     * 
     * @param skuId SKU ID
     * @param username 操作用户
     * @return 是否成功
     */
    boolean setDefaultSku(Long skuId, String username);
    
    /**
     * 复制SKU配置
     * 
     * @param sourceProductId 源商品ID
     * @param targetProductId 目标商品ID
     * @return 是否成功
     */
    boolean copySkuConfig(Long sourceProductId, Long targetProductId);
    
    /**
     * 复制SKU配置
     * 
     * @param sourceProductId 源商品ID
     * @param targetProductId 目标商品ID
     * @param username 操作用户
     * @return 是否成功
     */
    boolean copySkuConfig(Long sourceProductId, Long targetProductId, String username);
    
    /**
     * 获取SKU库存统计
     * 
     * @param productId 商品ID
     * @return 库存统计数据
     */
    Map<String, Object> getSkuStockStats(Long productId);
    
    /**
     * 生成SKU编码
     * 
     * @param productId 商品ID
     * @param specAttributes 规格属性
     * @return SKU编码
     */
    String generateSkuCode(Long productId, String specAttributes);
    
    /**
     * 检查SKU编码是否存在
     * 
     * @param skuCode SKU编码
     * @param excludeSkuId 排除的SKU ID
     * @return 是否存在
     */
    boolean checkSkuCodeExists(String skuCode, Long excludeSkuId);
    
    /**
     * 获取SKU价格区间
     * 
     * @param productId 商品ID
     * @return 价格区间数据
     */
    Map<String, BigDecimal> getSkuPriceRange(Long productId);
    
    /**
     * 导入SKU数据
     * 
     * @param skus SKU列表
     * @param username 操作用户
     * @return 是否成功
     */
    boolean importSkus(List<MallProductSku> skus, String username);
    
    /**
     * 导出SKU数据
     * 
     * @param productId 商品ID
     * @param skuIds SKU ID列表
     * @return 导出文件URL
     */
    String exportSkus(Long productId, List<Long> skuIds);
}