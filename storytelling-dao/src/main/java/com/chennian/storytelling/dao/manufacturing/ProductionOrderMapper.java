package com.chennian.storytelling.dao.manufacturing;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.chennian.storytelling.bean.model.manufacturing.ProductionOrder;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.MapKey;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;

/**
 * 生产订单数据访问层
 * @author chennian
 */
@Mapper
public interface ProductionOrderMapper extends BaseMapper<ProductionOrder> {
    
//    /**
//     * 更新生产订单状态
//     *
//     * @param orderId 订单ID
//     * @param status 状态
//     * @return 结果
//     */
//    int updateStatus(@Param("orderId") Long orderId, @Param("status") String status);
    
    /**
     * 开工生产订单
     * 
     * @param orderId 订单ID
     * @return 结果
     */
    int startOrder(@Param("orderId") Long orderId);
    
    /**
     * 暂停生产订单
     * 
     * @param orderId 订单ID
     * @return 结果
     */
    int pauseOrder(@Param("orderId") Long orderId);
    
    /**
     * 根据生产计划ID查询生产订单
     * 
     * @param planId 生产计划ID
     * @return 生产订单列表
     */
    List<ProductionOrder> selectByPlanId(@Param("planId") Long planId);
    
    /**
     * 查询生产统计数据
     * 
     * @param startDate 开始日期
     * @param endDate 结束日期
     * @param dimension 统计维度
     * @return 生产统计数据
     */
    @MapKey("dimension")
    Map<String, Object> selectProductionStatistics(@Param("startDate") String startDate, 
                                                   @Param("endDate") String endDate, 
                                                   @Param("dimension") String dimension);
    
    /**
     * 查询生产效率数据
     * 
     * @param startDate 开始日期
     * @param endDate 结束日期
     * @return 生产效率数据
     */
    @MapKey("date")
    Map<String, Object> selectProductionEfficiencyData(@Param("startDate") String startDate, 
                                                       @Param("endDate") String endDate);
    
    /**
     * 查询生产趋势数据
     * 
     * @param period 时间周期
     * @param granularity 粒度
     * @return 生产趋势数据
     */
    @MapKey("period")
    Map<String, Object> selectProductionTrendData(@Param("period") String period, 
                                                  @Param("granularity") String granularity);
    
    /**
     * 根据产品ID查询生产订单
     * 
     * @param productId 产品ID
     * @return 生产订单列表
     */
    List<ProductionOrder> selectByProductId(@Param("productId") Long productId);
    
    /**
     * 查询生产订单统计信息
     * 
     * @param startDate 开始日期
     * @param endDate 结束日期
     * @return 统计信息
     */
    @MapKey("date")
    Map<String, Object> selectOrderStatistics(@Param("startDate") String startDate, @Param("endDate") String endDate);
    
//    /**
//     * 更新生产订单完成数量
//     *
//     * @param orderId 订单ID
//     * @param completedQuantity 完成数量
//     * @param qualifiedQuantity 合格数量
//     * @return 结果
//     */
//    int updateCompletedQuantity(@Param("orderId") Long orderId,
//                               @Param("completedQuantity") Integer completedQuantity,
//                               @Param("qualifiedQuantity") Integer qualifiedQuantity);
    
    /**
     * 查询生产报告数据
     * 
     * @param startDate 开始日期
     * @param endDate 结束日期
     * @param productId 产品ID
     * @return 生产报告数据
     */
    Map<String, Object> selectProductionReportData(@Param("startDate") String startDate, 
                                                   @Param("endDate") String endDate, 
                                                   @Param("productId") Long productId);
    
    /**
     * 查询生产趋势数据
     * 
     * @param startDate 开始日期
     * @param endDate 结束日期
     * @param productId 产品ID
     * @return 生产趋势数据
     */
    List<Map<String, Object>> selectProductionTrends(@Param("startDate") String startDate, 
                                                     @Param("endDate") String endDate, 
                                                     @Param("productId") Long productId);
    
    /**
     * 查询交期分析数据
     * 
     * @param startDate 开始日期
     * @param endDate 结束日期
     * @param productId 产品ID
     * @return 交期分析数据
     */
    Map<String, Object> selectLeadTimeAnalysis(@Param("startDate") String startDate, 
                                               @Param("endDate") String endDate, 
                                               @Param("productId") Long productId);
    
    /**
     * 查询客户满意度数据
     * 
     * @param startDate 开始日期
     * @param endDate 结束日期
     * @param customerId 客户ID
     * @return 客户满意度数据
     */
    Map<String, Object> selectCustomerSatisfaction(@Param("startDate") String startDate, 
                                                   @Param("endDate") String endDate, 
                                                   @Param("customerId") Long customerId);
    
    /**
     * 查询准时交货数据
     * 
     * @param startDate 开始日期
     * @param endDate 结束日期
     * @param customerId 客户ID
     * @return 准时交货数据
     */
    Map<String, Object> selectOnTimeDelivery(@Param("startDate") String startDate, 
                                             @Param("endDate") String endDate, 
                                             @Param("customerId") Long customerId);
    
    /**
     * 查询客户服务数据
     * 
     * @param startDate 开始日期
     * @param endDate 结束日期
     * @param customerId 客户ID
     * @return 客户服务数据
     */
    Map<String, Object> selectCustomerServiceData(@Param("startDate") String startDate, 
                                                  @Param("endDate") String endDate, 
                                                  @Param("customerId") Long customerId);
    
    /**
     * 查询市场数据
     * 
     * @param startDate 开始日期
     * @param endDate 结束日期
     * @param productId 产品ID
     * @return 市场数据
     */
    Map<String, Object> selectMarketData(@Param("startDate") String startDate, 
                                         @Param("endDate") String endDate, 
                                         @Param("productId") Long productId);
}