package com.chennian.storytelling.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.chennian.storytelling.bean.model.manufacturing.ProductionOrder;
import com.chennian.storytelling.common.utils.PageParam;
import org.springframework.transaction.annotation.Transactional;

/**
 * 生产订单管理服务接口
 * 
 * @author storytelling
 * @date 2024-01-01
 */
public interface ProductionOrderService {

    /**
     * 查询生产订单列表
     */
    IPage<ProductionOrder> getProductionOrderList(PageParam<ProductionOrder> page, ProductionOrder order);
    
    /**
     * 根据ID查询生产订单信息
     */
    ProductionOrder getProductionOrderById(Long id);
    
    /**
     * 新增生产订单
     */
    void addProductionOrder(ProductionOrder order);
    
    /**
     * 修改生产订单信息
     */
    void updateProductionOrder(ProductionOrder order);
    
    /**
     * 删除生产订单
     */
    void deleteProductionOrder(Long[] ids);

    /**
     * 删除生产订单（单个）
     */
    @Transactional
    void deleteProductionOrder(Long id);

    /**
     * 生产订单开工
     */
    void startProductionOrder(Long orderId);
    
    /**
     * 生产订单暂停
     */
    void pauseProductionOrder(Long orderId, String pauseReason);
    
    /**
     * 生产订单完工
     */
    void completeProductionOrder(Long orderId, Integer actualQuantity, Integer qualifiedQuantity, String remark);
    
    /**
     * 生产订单暂停（单个参数）
     */
    @Transactional
    void pauseProductionOrder(Long id);
}