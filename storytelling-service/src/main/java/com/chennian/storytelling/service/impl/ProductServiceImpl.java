package com.chennian.storytelling.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.chennian.storytelling.bean.bo.ProductSearchBo;
import com.chennian.storytelling.bean.model.Inventory;
import com.chennian.storytelling.bean.model.Product;
import com.chennian.storytelling.bean.model.SalesOrder;
import com.chennian.storytelling.bean.vo.HotProductVO;
import com.chennian.storytelling.bean.vo.MobileProductVO;
import com.chennian.storytelling.common.utils.PageParam;
import com.chennian.storytelling.dao.InventoryMapper;
import com.chennian.storytelling.dao.ProductMapper;
import com.chennian.storytelling.dao.SalesOrderMapper;
import com.chennian.storytelling.service.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;


import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 产品服务实现类
 * @author chennian
 */
@Service
public class ProductServiceImpl extends ServiceImpl<ProductMapper, Product> implements ProductService {

    @Autowired
    private ProductMapper productMapper;
    
    @Autowired
    private InventoryMapper inventoryMapper;
    
    @Autowired
    private SalesOrderMapper salesOrderMapper;

    /**
     * 分页查询产品列表
     */
    @Override
    public IPage<Product> findByPage(PageParam<Product> page, Product product) {
        LambdaQueryWrapper<Product> queryWrapper = new LambdaQueryWrapper<>();
        // 构建查询条件
        if (product != null) {
            if (StringUtils.hasText(product.getProductName())) {
                queryWrapper.like(Product::getProductName, product.getProductName());
            }
            if (StringUtils.hasText(product.getProductCode())) {
                queryWrapper.eq(Product::getProductCode, product.getProductCode());
            }
            if (product.getCategoryId() != null) {
                queryWrapper.eq(Product::getCategoryId, product.getCategoryId());
            }
            if (StringUtils.hasText(product.getStatus())) {
                queryWrapper.eq(Product::getStatus, product.getStatus());
            }
        }
        // 排序
        queryWrapper.orderByDesc(Product::getCreateTime);
        
        // 执行分页查询
        return productMapper.selectPage(new Page<>(page.getCurrent(), page.getSize()), queryWrapper);
    }

    /**
     * 根据ID查询产品
     */
    @Override
    public Product selectProductById(Long productId) {
        return productMapper.selectById(productId);
    }

    /**
     * 新增产品
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public int insertProduct(Product product) {
        // 设置默认状态为正常
        if (product.getStatus() == null) {
            product.setStatus("0");
        }
        return productMapper.insert(product);
    }

    /**
     * 修改产品
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public int updateProduct(Product product) {
        return productMapper.updateById(product);
    }

    /**
     * 批量删除产品
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public int deleteProductByIds(Long[] productIds) {
        int count = 0;
        for (Long productId : productIds) {
            count += productMapper.deleteById(productId);
        }
        return count;
    }
    
    /**
     * 更新产品状态
     */
    @Override
    public int updateProductStatus(Product product) {
        Product updateProduct = new Product();
        updateProduct.setProductId(product.getProductId());
        updateProduct.setStatus(product.getStatus());
        return productMapper.updateById(updateProduct);
    }
    
    /**
     * 获取产品库存信息
     */
    @Override
    public Map<String, Object> getProductInventory(Long productId) {
        Map<String, Object> result = new HashMap<>();
        
        // 获取产品基本信息
        Product product = productMapper.selectById(productId);
        if (product != null) {
            result.put("product", product);
            
            // 获取库存信息
            LambdaQueryWrapper<Inventory> queryWrapper = new LambdaQueryWrapper<>();
            queryWrapper.eq(Inventory::getProductId, productId);
            Inventory inventory = inventoryMapper.selectOne(queryWrapper);
            result.put("inventory", inventory);
            
            // 计算库存状态
            if (inventory != null) {
                int currentStock = inventory.getQuantity();
                if (currentStock <= product.getMinStock()) {
                    result.put("stockStatus", "低库存警告");
                } else if (currentStock >= product.getMaxStock()) {
                    result.put("stockStatus", "库存过高");
                } else {
                    result.put("stockStatus", "库存正常");
                }
            }
        }
        
        return result;
    }
    
    /**
     * 获取产品销售记录
     */
    @Override
    public List<Map<String, Object>> getProductSalesRecords(Long productId) {
        List<Map<String, Object>> result = new ArrayList<>();
        
        // 查询包含该产品的销售订单
        List<SalesOrder> salesOrders = salesOrderMapper.findSalesOrdersByProductId(productId);
        
        // 处理销售记录数据
        if (salesOrders != null && !salesOrders.isEmpty()) {
            for (SalesOrder order : salesOrders) {
                Map<String, Object> record = new HashMap<>();
                record.put("orderId", order.getOrderId());
                record.put("orderNo", order.getOrderNo());
                record.put("customerName", order.getCustomerName());
                record.put("orderDate", order.getCreateTime());
                record.put("totalAmount", order.getTotalAmount());
                record.put("status", order.getStatus());
                
                result.add(record);
            }
        }
        
        return result;
    }
    
    /**
     * 获取移动端产品列表
     * @param params 查询参数，包含keyword、page、size
     * @return 移动端产品列表
     */
    @Override
    public List<MobileProductVO> getMobileProductList(Map<String, Object> params) {
        // 获取查询参数
        String keyword = (String) params.get("keyword");
        Integer pageNum = (Integer) params.get("page");
        Integer pageSize = (Integer) params.get("size");
        
        // 构建查询条件
        LambdaQueryWrapper<Product> queryWrapper = new LambdaQueryWrapper<>();
        if (StringUtils.hasText(keyword)) {
            queryWrapper.like(Product::getProductName, keyword)
                    .or().like(Product::getProductCode, keyword);
        }
        
        // 按创建时间降序排序
        queryWrapper.orderByDesc(Product::getCreateTime);
        
        // 执行分页查询
        Page<Product> page = new Page<>(pageNum, pageSize);
        Page<Product> productPage = page(page, queryWrapper);
        
        // 转换为移动端VO对象
        List<MobileProductVO> result = new ArrayList<>();
        for (Product product : productPage.getRecords()) {
            MobileProductVO vo = new MobileProductVO();
            BeanUtils.copyProperties(product, vo);
            
            // 获取库存信息
            LambdaQueryWrapper<Inventory> inventoryWrapper = new LambdaQueryWrapper<>();
            inventoryWrapper.eq(Inventory::getProductId, product.getProductId());
            Inventory inventory = inventoryMapper.selectOne(inventoryWrapper);
            if (inventory != null) {
                vo.setStock(inventory.getQuantity());
            } else {
                vo.setStock(0);
            }
            
            // 设置简短描述
            if (product.getRemark() != null && product.getRemark().length() > 50) {
                vo.setBriefDescription(product.getRemark().substring(0, 50) + "...");
            } else {
                vo.setBriefDescription(product.getRemark());
            }
            
            // 设置销售统计（简化版）
            Map<String, Object> salesStats = new HashMap<>();
            salesStats.put("totalSales", 0); // 这里应该从销售记录中统计，简化处理
            salesStats.put("lastMonthSales", 0);
            vo.setSalesStats(salesStats);
            
            result.add(vo);
        }
        
        return result;
    }
    
    /**
     * 获取移动端产品详情
     * @param productId 产品ID
     * @return 移动端产品详情
     */
    @Override
    public MobileProductVO getMobileProductDetail(Long productId) {
        // 获取产品基本信息
        Product product = getById(productId);
        if (product == null) {
            return null;
        }
        
        // 转换为移动端VO对象
        MobileProductVO vo = new MobileProductVO();
        BeanUtils.copyProperties(product, vo);
        
        // 获取库存信息
        LambdaQueryWrapper<Inventory> inventoryWrapper = new LambdaQueryWrapper<>();
        inventoryWrapper.eq(Inventory::getProductId, productId);
        Inventory inventory = inventoryMapper.selectOne(inventoryWrapper);
        if (inventory != null) {
            vo.setStock(inventory.getQuantity());
        } else {
            vo.setStock(0);
        }
        
        // 设置简短描述
        vo.setBriefDescription(product.getRemark());
        
        // 设置销售统计
        Map<String, Object> salesStats = new HashMap<>();
        List<Map<String, Object>> salesRecords = getProductSalesRecords(productId);
        salesStats.put("totalSales", salesRecords.size());
        salesStats.put("salesRecords", salesRecords);
        vo.setSalesStats(salesStats);
        
        return vo;
    }
    
    /**
     * 获取库存预警数量
     * @return 库存预警数量
     */
    @Override
    public Integer getInventoryAlertCount() {
        int count = 0;
        
        // 获取所有产品
        List<Product> products = list();
        for (Product product : products) {
            // 获取产品库存
            LambdaQueryWrapper<Inventory> queryWrapper = new LambdaQueryWrapper<>();
            queryWrapper.eq(Inventory::getProductId, product.getProductId());
            Inventory inventory = inventoryMapper.selectOne(queryWrapper);
            
            // 检查是否低于最小库存
            if (inventory != null && inventory.getQuantity() <= product.getMinStock()) {
                count++;
            }
        }
        
        return count;
    }
    
    /**
     * 获取热销产品数据
     * @param params 查询参数，可包含limit限制返回数量
     * @return 热销产品数据
     */
    @Override
    public List<HotProductVO> getHotProducts(Map<String, Object> params) {
        Integer limit = params != null && params.get("limit") != null ? (Integer) params.get("limit") : 10;
        // 查询销售订单统计产品销量
        List<Map<String, Object>> salesList = salesOrderMapper.getHotProducts(params);
        List<HotProductVO> result = new ArrayList<>();
        for (Map<String, Object> map : salesList) {
            HotProductVO vo = new HotProductVO();
            vo.setProductId(map.get("productId") != null ? Long.valueOf(map.get("productId").toString()) : null);
            vo.setProductName((String) map.get("productName"));
            vo.setProductCode((String) map.get("productCode"));
            vo.setTotalSales(map.get("totalSales") != null ? Integer.valueOf(map.get("totalSales").toString()) : 0);
            vo.setImageUrl((String) map.get("imageUrl"));
            // 查询库存
            if (vo.getProductId() != null) {
                LambdaQueryWrapper<Inventory> queryWrapper = new LambdaQueryWrapper<>();
                queryWrapper.eq(Inventory::getProductId, vo.getProductId());
                Inventory inventory = inventoryMapper.selectOne(queryWrapper);
                vo.setStock(inventory != null ? inventory.getQuantity() : 0);
            } else {
                vo.setStock(0);
            }
            result.add(vo);
            if (result.size() >= limit) {
                break;
            }
        }
        return result;
    }

    /**
     * 获取产品列表
     */
    @Override
    public IPage<Product> getProductList(ProductSearchBo productSearchBo){
        return productMapper.selectPage(productSearchBo.getParam(), new LambdaQueryWrapper<Product>()
                .eq(StringUtils.hasText(productSearchBo.getProductName()), Product::getProductName, productSearchBo.getProductName())
                .eq(StringUtils.hasText(productSearchBo.getProductCode()), Product::getProductCode, productSearchBo.getProductCode())
                .eq(StringUtils.hasText(productSearchBo.getSpecification()), Product::getSpecification, productSearchBo.getSpecification())
        );
    }

}