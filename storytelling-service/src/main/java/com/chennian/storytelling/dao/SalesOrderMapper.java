package com.chennian.storytelling.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.chennian.storytelling.bean.model.SalesOrder;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

/**
 * 销售订单数据访问层
 * @author chennian
 */
@Mapper
public interface SalesOrderMapper extends BaseMapper<SalesOrder> {
    
    /**
     * 根据产品ID查询销售订单
     * 
     * @param productId 产品ID
     * @return 销售订单列表
     */
    List<SalesOrder> findSalesOrdersByProductId(@Param("productId") Long productId);
    
    /**
     * 根据客户ID查询销售订单
     * 
     * @param customerId 客户ID
     * @return 销售订单列表
     */
    List<SalesOrder> findSalesOrdersByCustomerId(@Param("customerId") Long customerId);
    
    /**
     * 更新销售订单状态
     * 
     * @param orderId 订单ID
     * @param status 状态
     * @return 结果
     */
    int updateOrderStatus(@Param("orderId") Long orderId, @Param("status") String status);
    
    /**
     * 获取销售金额统计
     * @param dateRange 日期范围
     * @return 销售金额统计数据
     */
    List<Map<String, Object>> getSalesAmount(@Param("dateRange") String dateRange);

    /**
     * 获取热销产品统计
     * @param params 查询参数，包含limit、dateRange等
     * @return 热销产品统计数据
     */
    List<Map<String, Object>> getHotProducts(@Param("params") Map<String, Object> params);
    
    /**
     * 获取指定产品类别的销售统计数据
     * @param productCategory 产品类别
     * @param dateRange 日期范围
     * @return 产品类别销售统计数据
     */
    List<Map<String, Object>> getCategoryStats(@Param("productCategory") String productCategory, @Param("dateRange") String dateRange);
}