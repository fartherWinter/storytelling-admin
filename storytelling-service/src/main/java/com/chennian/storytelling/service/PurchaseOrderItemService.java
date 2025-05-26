package com.chennian.storytelling.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.chennian.storytelling.bean.model.PurchaseOrderItem;

import java.util.List;

/**
 * 采购订单明细服务接口
 * @author chennian
 * @date 2023/5/20
 */
public interface PurchaseOrderItemService extends IService<PurchaseOrderItem> {
    
    /**
     * 根据采购订单ID查询明细列表
     * 
     * @param orderId 采购订单ID
     * @return 明细列表
     */
    List<PurchaseOrderItem> selectItemsByOrderId(Long orderId);
    
    /**
     * 新增采购订单明细
     * 
     * @param item 采购订单明细信息
     * @return 结果
     */
    int insertOrderItem(PurchaseOrderItem item);
    
    /**
     * 修改采购订单明细
     * 
     * @param item 采购订单明细信息
     * @return 结果
     */
    int updateOrderItem(PurchaseOrderItem item);
    
    /**
     * 批量删除采购订单明细
     * 
     * @param itemIds 需要删除的明细ID数组
     * @return 结果
     */
    int deleteOrderItemByIds(Long[] itemIds);
    
    /**
     * 批量新增采购订单明细
     * 
     * @param items 采购订单明细列表
     * @return 结果
     */
    int batchInsertOrderItems(List<PurchaseOrderItem> items);
    
    /**
     * 根据采购订单ID删除明细
     * 
     * @param orderId 采购订单ID
     * @return 结果
     */
    int deleteOrderItemByOrderId(Long orderId);
}