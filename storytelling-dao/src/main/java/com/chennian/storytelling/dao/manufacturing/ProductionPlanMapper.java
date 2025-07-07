package com.chennian.storytelling.dao.manufacturing;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.chennian.storytelling.bean.model.manufacturing.ProductionPlan;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.MapKey;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;

/**
 * 生产计划数据访问层
 * @author chennian
 */
@Mapper
public interface ProductionPlanMapper extends BaseMapper<ProductionPlan> {
    
    /**
     * 更新生产计划状态
     * 
     * @param planId 计划ID
     * @param status 状态
     * @return 结果
     */
    int updateStatus(@Param("planId") Long planId, @Param("status") String status);
    
    /**
     * 审核生产计划
     * 
     * @param planId 计划ID
     * @param approver 审核
     * @return 结果
     */
    int approvePlan(@Param("planId") Long planId, @Param("approver") String approver);
    
    /**
     * 查询预测分析数据
     * 
     * @param analysisType 分析类型
     * @param parameters 参数
     * @return 预测分析数据
     */
    @MapKey("analysisType")
    Map<String, Object> selectPredictiveAnalysisData(@Param("analysisType") String analysisType, 
                                                     @Param("parameters") Map<String, Object> parameters);
    
    /**
     * 执行生产计划
     * 
     * @param planId 计划ID
     * @return 结果
     */
    int executePlan(@Param("planId") Long planId);
    
    /**
     * 完成生产计划
     * 
     * @param planId 计划ID
     * @return 结果
     */
    int completePlan(@Param("planId") Long planId);
    
    /**
     * 根据产品ID查询生产计划
     * 
     * @param productId 产品ID
     * @return 生产计划列表
     */
    List<ProductionPlan> selectByProductId(@Param("productId") Long productId);
    
    /**
     * 查询生产计划统计信息
     * 
     * @param startDate 开始日期
     * @param endDate 结束日期
     * @return 统计信息
     */
    @MapKey("status")
    Map<String, Object> selectPlanStatistics(@Param("startDate") String startDate, @Param("endDate") String endDate);
    
    /**
     * 查询生产计划优化数据
     * 
     * @param startDate 开始日期
     * @param endDate 结束日期
     * @param lineId 生产线ID
     * @return 优化数据
     */
    @MapKey("id")
    List<Map<String, Object>> selectOptimizationData(@Param("startDate") String startDate, 
                                                     @Param("endDate") String endDate, 
                                                     @Param("lineId") Long lineId);
    
    /**
     * 查询决策数据
     * 
     * @param startDate 开始日期
     * @param endDate 结束日期
     * @param departmentId 部门ID
     * @return 决策数据
     */
    @MapKey("departmentId")
    Map<String, Object> selectDecisionData(@Param("startDate") String startDate, 
                                           @Param("endDate") String endDate, 
                                           @Param("departmentId") Long departmentId);
}