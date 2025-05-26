package com.chennian.storytelling.dao;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.Map;

/**
 * 成本会计数据访问层
 * @author chen
 * @date 2023/6/15
 */
@Mapper
public interface CostAccountingMapper {
    
    /**
     * 获取成本中心基本信息
     * @param period 期间
     * @param costCenterId 成本中心ID
     * @return 成本中心基本信息
     */
    Map<String, Object> getCostCenterBasicInfo(@Param("period") String period, @Param("costCenterId") Long costCenterId);
    
    /**
     * 获取成本中心成本汇总
     * @param period 期间
     * @param costCenterId 成本中心ID
     * @return 成本中心成本汇总
     */
    Map<String, Object> getCostCenterSummary(@Param("period") String period, @Param("costCenterId") Long costCenterId);
    
    /**
     * 获取成本中心成本明细
     * @param period 期间
     * @param costCenterId 成本中心ID
     * @return 成本中心成本明细
     */
    Map<String, Object> getCostCenterDetails(@Param("period") String period, @Param("costCenterId") Long costCenterId);
    
    /**
     * 获取成本中心成本分析
     * @param period 期间
     * @param costCenterId 成本中心ID
     * @return 成本中心成本分析
     */
    Map<String, Object> getCostCenterAnalysis(@Param("period") String period, @Param("costCenterId") Long costCenterId);
    
    /**
     * 获取产品基本信息
     * @param period 期间
     * @param productId 产品ID
     * @return 产品基本信息
     */
    Map<String, Object> getProductBasicInfo(@Param("period") String period, @Param("productId") Long productId);
    
    /**
     * 获取产品成本汇总
     * @param period 期间
     * @param productId 产品ID
     * @return 产品成本汇总
     */
    Map<String, Object> getProductCostSummary(@Param("period") String period, @Param("productId") Long productId);
    
    /**
     * 获取产品成本构成
     * @param period 期间
     * @param productId 产品ID
     * @return 产品成本构成
     */
    Map<String, Object> getProductCostComposition(@Param("period") String period, @Param("productId") Long productId);
    
    /**
     * 获取产品成本趋势
     * @param period 期间
     * @param productId 产品ID
     * @return 产品成本趋势
     */
    Map<String, Object> getProductCostTrend(@Param("period") String period, @Param("productId") Long productId);
    
    /**
     * 获取产品成本优化建议
     * @param period 期间
     * @param productId 产品ID
     * @return 产品成本优化建议
     */
    Map<String, Object> getProductCostOptimization(@Param("period") String period, @Param("productId") Long productId);
    
    /**
     * 获取活动成本池
     * @param period 期间
     * @return 活动成本池
     */
    Map<String, Double> getActivityCostPool(@Param("period") String period);
    
    /**
     * 获取成本动因
     * @param period 期间
     * @return 成本动因
     */
    Map<String, Integer> getCostDrivers(@Param("period") String period);
    
    /**
     * 获取单位成本动因率
     * @param period 期间
     * @return 单位成本动因率
     */
    Map<String, Double> getUnitCostDriverRate(@Param("period") String period);
    
    /**
     * 获取产品ABC成本分析
     * @param period 期间
     * @return 产品ABC成本分析
     */
    Map<String, Map<String, Object>> getProductABC(@Param("period") String period);
    
    /**
     * 获取活动价值分析
     * @param period 期间
     * @return 活动价值分析
     */
    Map<String, Object> getActivityValueAnalysis(@Param("period") String period);
}