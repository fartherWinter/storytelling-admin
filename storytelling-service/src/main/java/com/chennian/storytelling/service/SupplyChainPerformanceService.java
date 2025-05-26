package com.chennian.storytelling.service;

import java.util.Map;

/**
 * 供应链绩效服务接口
 * 
 * @author chennian
 * @date 2023/12/15
 */
public interface SupplyChainPerformanceService {
    
    /**
     * 获取订单履行率
     * 
     * @return 订单履行率（百分比格式）
     */
    String getOrderFulfillmentRate();
    
    /**
     * 获取平均交付时间
     * 
     * @return 平均交付时间（天数格式）
     */
    String getAverageDeliveryTime();
    
    /**
     * 获取供应商准时率
     * 
     * @return 供应商准时率（百分比格式）
     */
    String getSupplierOnTimeRate();
    
    /**
     * 获取库存周转率
     * 
     * @return 库存周转率（次/年格式）
     */
    String getInventoryTurnoverRate();
    
    /**
     * 获取供应链成本
     * 
     * @return 供应链成本（占总收入百分比格式）
     */
    String getSupplyChainCost();
    
    /**
     * 获取所有供应链绩效指标
     * 
     * @return 包含所有绩效指标的Map
     */
    Map<String, Object> getAllPerformanceMetrics();
}