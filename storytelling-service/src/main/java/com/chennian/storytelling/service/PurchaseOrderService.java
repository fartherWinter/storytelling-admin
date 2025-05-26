package com.chennian.storytelling.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.IService;
import com.chennian.storytelling.bean.model.PurchaseOrder;
import com.chennian.storytelling.common.utils.PageParam;

/**
 * 采购订单服务接口
 * @author chennian
 * @date 2023/5/20
 */
public interface PurchaseOrderService extends IService<PurchaseOrder> {
    
    /**
     * 分页查询采购订单
     * 
     * @param page 分页参数
     * @param purchaseOrder 查询条件
     * @return 分页结果
     */
    IPage<PurchaseOrder> findByPage(PageParam<PurchaseOrder> page, PurchaseOrder purchaseOrder);
    
    /**
     * 根据ID查询采购订单
     * 
     * @param orderId 订单ID
     * @return 采购订单信息
     */
    PurchaseOrder selectOrderById(Long orderId);
    
    /**
     * 新增采购订单
     * 
     * @param purchaseOrder 采购订单信息
     * @return 结果
     */
    int insertOrder(PurchaseOrder purchaseOrder);
    
    /**
     * 修改采购订单
     * 
     * @param purchaseOrder 采购订单信息
     * @return 结果
     */
    int updateOrder(PurchaseOrder purchaseOrder);
    
    /**
     * 批量删除采购订单
     * 
     * @param orderIds 需要删除的订单ID数组
     * @return 结果
     */
    int deleteOrderByIds(Long[] orderIds);
    
    /**
     * 审核采购订单
     * 
     * @param orderId 订单ID
     * @return 结果
     */
    int approveOrder(Long orderId);

    Integer getPendingOrderCount();

    Object getSupplierPendingOrders(Long supplierId);

    Object getSupplierOrderHistory(Long supplierId);
}