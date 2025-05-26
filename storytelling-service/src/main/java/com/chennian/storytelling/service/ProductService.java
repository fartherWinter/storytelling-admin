package com.chennian.storytelling.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.IService;
import com.chennian.storytelling.bean.bo.ProductSearchBo;
import com.chennian.storytelling.bean.model.Product;
import com.chennian.storytelling.bean.vo.HotProductVO;
import com.chennian.storytelling.bean.vo.MobileProductVO;
import com.chennian.storytelling.common.utils.PageParam;

import java.util.List;
import java.util.Map;

/**
 * 产品服务接口
 * @author chennian
 */
public interface ProductService extends IService<Product> {

    /**
     * 分页查询产品列表
     * @param page 分页参数
     * @param product 查询条件
     * @return 产品分页数据
     */
    IPage<Product> findByPage(PageParam<Product> page, Product product);

    /**
     * 根据ID查询产品
     * @param productId 产品ID
     * @return 产品信息
     */
    Product selectProductById(Long productId);

    /**
     * 新增产品
     * @param product 产品信息
     * @return 结果
     */
    int insertProduct(Product product);

    /**
     * 修改产品
     * @param product 产品信息
     * @return 结果
     */
    int updateProduct(Product product);

    /**
     * 批量删除产品
     * @param productIds 需要删除的产品ID数组
     * @return 结果
     */
    int deleteProductByIds(Long[] productIds);
    
    /**
     * 更新产品状态
     * @param product 产品信息
     * @return 结果
     */
    int updateProductStatus(Product product);
    
    /**
     * 获取产品库存信息
     * @param productId 产品ID
     * @return 库存信息
     */
    Map<String, Object> getProductInventory(Long productId);
    
    /**
     * 获取产品销售记录
     * @param productId 产品ID
     * @return 销售记录
     */
    List<Map<String, Object>> getProductSalesRecords(Long productId);
    
    /**
     * 获取移动端产品列表
     * @param params 查询参数，包含keyword、page、size
     * @return 移动端产品列表
     */
    List<MobileProductVO> getMobileProductList(Map<String, Object> params);
    
    /**
     * 获取移动端产品详情
     * @param productId 产品ID
     * @return 移动端产品详情
     */
    MobileProductVO getMobileProductDetail(Long productId);
    
    /**
     * 获取库存预警数量
     * @return 库存预警数量
     */
    Integer getInventoryAlertCount();
    
    /**
     * 获取热销产品数据
     * @param params 查询参数，可包含limit限制返回数量
     * @return 热销产品数据
     */
    List<HotProductVO> getHotProducts(Map<String, Object> params);

    /**
     * 获取产品列表
     * @param productSearchBo
     * @return
     */
    IPage<Product> getProductList(ProductSearchBo productSearchBo);
}