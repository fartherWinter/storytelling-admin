package com.chennian.storytelling.dao.manufacturing;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.chennian.storytelling.bean.model.manufacturing.WorkOrder;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.MapKey;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;

/**
 * 工单数据访问层
 * @author chennian
 */
@Mapper
public interface WorkOrderMapper extends BaseMapper<WorkOrder> {
    
    /**
     * 更新工单状态
     * 
     * @param workOrderId 工单ID
     * @param status 状态
     * @return 结果
     */
    int updateOrderStatus(@Param("workOrderId") Long workOrderId, @Param("status") String status);
    
    /**
     * 派工
     * 
     * @param workOrderId 工单ID
     * @param operatorId 操作员ID
     * @return 结果
     */
    int assignOperator(@Param("workOrderId") Long workOrderId, @Param("operatorId") Long operatorId);
    
    /**
     * 开工
     * 
     * @param workOrderId 工单ID
     * @return 结果
     */
    int startWork(@Param("workOrderId") Long workOrderId);
    
    /**
     * 完工
     * 
     * @param workOrderId 工单ID
     * @return 结果
     */
    int completeWork(@Param("workOrderId") Long workOrderId);
    
    /**
     * 报工
     * 
     * @param workOrderId 工单ID
     * @param quantity 数量
     * @param remark 备注
     * @return 结果
     */
    int reportWork(@Param("workOrderId") Long workOrderId, 
                   @Param("quantity") Integer quantity, 
                   @Param("remark") String remark);
    
    /**
     * 根据生产订单ID查询工单列表
     * 
     * @param productionOrderId 生产订单ID
     * @return 工单列表
     */
    List<WorkOrder> selectByProductionOrderId(@Param("productionOrderId") Long productionOrderId);
    
    /**
     * 根据状态查询工单
     * 
     * @param status 状态
     * @return 工单列表
     */
    List<WorkOrder> selectByStatus(@Param("status") String status);
    
    /**
     * 根据生产线ID查询工单
     * 
     * @param lineId 生产线ID
     * @return 工单列表
     */
    List<WorkOrder> selectByLineId(@Param("lineId") Long lineId);
    
    /**
     * 根据产品ID查询工单
     * 
     * @param productId 产品ID
     * @return 工单列表
     */
    List<WorkOrder> selectByProductId(@Param("productId") Long productId);
    
    /**
     * 查询质量分析数据
     * 
     * @param startDate 开始日期
     * @param endDate 结束日期
     * @param workOrderId 工单ID
     * @return 质量分析数据
     */
    @MapKey("workOrderId")
    Map<String, Object> selectQualityAnalysisData(@Param("startDate") String startDate, 
                                                  @Param("endDate") String endDate, 
                                                  @Param("workOrderId") Long workOrderId);
    
    /**
     * 查询异常分析数据
     * 
     * @param startDate 开始日期
     * @param endDate 结束日期
     * @param workOrderId 工单ID
     * @return 异常分析数据
     */
    @MapKey("workOrderId")
    Map<String, Object> selectAnomalyAnalysisData(@Param("startDate") String startDate, 
                                                  @Param("endDate") String endDate, 
                                                  @Param("workOrderId") Long workOrderId);
    
    /**
     * 查询绩效指标数据
     * 
     * @param startDate 开始日期
     * @param endDate 结束日期
     * @param indicatorType 指标类型
     * @return 绩效指标数据
     */
    @MapKey("indicatorType")
    Map<String, Object> selectPerformanceIndicatorData(@Param("startDate") String startDate, 
                                                       @Param("endDate") String endDate, 
                                                       @Param("indicatorType") String indicatorType);
    
    /**
     * 根据操作员ID查询工单
     * 
     * @param operatorId 操作员ID
     * @return 工单列表
     */
    List<WorkOrder> selectByOperatorId(@Param("operatorId") Long operatorId);
    
    /**
     * 查询工单统计信息
     * 
     * @param startDate 开始日期
     * @param endDate 结束日期
     * @return 统计信息
     */
    @MapKey("statisticType")
    Map<String, Object> selectOrderStatistics(@Param("startDate") String startDate, @Param("endDate") String endDate);
    
    /**
     * 查询生产数据
     * 
     * @param startDate 开始日期
     * @param endDate 结束日期
     * @param lineId 生产线ID
     * @return 生产数据
     */
    @MapKey("lineId")
    Map<String, Object> selectProductionData(@Param("startDate") String startDate, 
                                            @Param("endDate") String endDate, 
                                            @Param("lineId") Long lineId);
    
    /**
     * 查询质量数据
     * 
     * @param startDate 开始日期
     * @param endDate 结束日期
     * @param lineId 生产线ID
     * @param operatorId 操作员ID
     * @return 质量数据
     */
    @MapKey("workOrderId")
    Map<String, Object> selectQualityData(@Param("startDate") String startDate, 
                                         @Param("endDate") String endDate, 
                                         @Param("lineId") Long lineId, 
                                         @Param("operatorId") Long operatorId);
    
    /**
     * 根据生产订单生成工单
     * 
     * @param productionOrderId 生产订单ID
     * @return 结果
     */
    int generateFromProductionOrder(@Param("productionOrderId") Long productionOrderId);
    
    /**
     * 派工 - ServiceImpl调用的方法名
     * 
     * @param id 工单ID
     * @param operatorId 操作员ID
     * @return 结果
     */
    int assignWorkOrder(@Param("id") Long id, @Param("operatorId") Long operatorId);
    
    /**
     * 报工 - ServiceImpl调用的方法名
     * 
     * @param id 工单ID
     * @param quantity 数量
     * @param remark 备注
     * @return 结果
     */
    int reportWorkOrder(@Param("id") Long id, @Param("quantity") Integer quantity, @Param("remark") String remark);
    
    /**
     * 根据生产订单生成工单 - ServiceImpl调用的方法名
     * 
     * @param productionOrderId 生产订单ID
     * @return 结果
     */
    int generateWorkOrderFromProductionOrder(@Param("productionOrderId") Long productionOrderId);
    
    /**
     * 查询质量统计数据
     * 
     * @param startDate 开始日期
     * @param endDate 结束日期
     * @param productId 产品ID
     * @return 质量统计数据
     */
    @MapKey("statisticType")
    Map<String, Object> selectQualityStatistics(@Param("startDate") String startDate, 
                                               @Param("endDate") String endDate, 
                                               @Param("productId") Long productId);
    
    /**
     * 查询废料统计数据
     * 
     * @param startDate 开始日期
     * @param endDate 结束日期
     * @param productId 产品ID
     * @return 废料统计数据
     */
    @MapKey("productName")
    Map<String, Object> selectWasteStatistics(@Param("startDate") String startDate, 
                                             @Param("endDate") String endDate, 
                                             @Param("productId") Long productId);
    
    /**
     * 查询退货率分析数据
     * 
     * @param startDate 开始日期
     * @param endDate 结束日期
     * @param productId 产品ID
     * @return 退货率分析数据
     */
    @MapKey("productName")
    Map<String, Object> selectReturnRateAnalysis(@Param("startDate") String startDate, 
                                                @Param("endDate") String endDate, 
                                                @Param("productId") Long productId);
    
    /**
     * 查询资源配置数据
     * 
     * @param startDate 开始日期
     * @param endDate 结束日期
     * @param lineId 生产线ID
     * @param operatorId 操作员ID
     * @return 资源配置数据
     */
    @MapKey("workOrderId")
    Map<String, Object> selectResourceAllocationData(@Param("startDate") String startDate, 
                                                    @Param("endDate") String endDate, 
                                                    @Param("lineId") Long lineId, 
                                                    @Param("operatorId") Long operatorId);
    
    /**
     * 启动工单
     * 
     * @param id 工单ID
     * @return 结果
     */
    int startWorkOrder(@Param("id") Long id);
    
    /**
     * 完成工单
     * 
     * @param id 工单ID
     * @return 结果
     */
    int completeWorkOrder(@Param("id") Long id);
    
    /**
     * 查询质量报告数据
     * 
     * @param startDate 开始日期
     * @param endDate 结束日期
     * @param productId 产品ID
     * @return 质量报告数据
     */
    @MapKey("reportType")
    Map<String, Object> selectQualityReportData(@Param("startDate") String startDate, 
                                                @Param("endDate") String endDate, 
                                                @Param("productId") Long productId);
    
    /**
     * 查询人员效率数据
     * 
     * @param startDate 开始日期
     * @param endDate 结束日期
     * @param departmentId 部门ID
     * @return 人员效率数据
     */
    List<Map<String, Object>> selectPersonnelEfficiencyData(@Param("startDate") String startDate, 
                                                            @Param("endDate") String endDate, 
                                                            @Param("departmentId") Long departmentId);
    
    /**
     * 查询培训数据
     * 
     * @param startDate 开始日期
     * @param endDate 结束日期
     * @param departmentId 部门ID
     * @return 培训数据
     */
    List<Map<String, Object>> selectTrainingData(@Param("startDate") String startDate, 
                                                 @Param("endDate") String endDate, 
                                                 @Param("departmentId") Long departmentId);
    
    /**
     * 查询效率数据
     * 
     * @param startDate 开始日期
     * @param endDate 结束日期
     * @param operatorId 操作员ID
     * @param lineId 生产线ID
     * @return 效率数据
     */
    @MapKey("operatorId")
    Map<String, Object> selectEfficiencyData(@Param("startDate") String startDate, 
                                            @Param("endDate") String endDate, 
                                            @Param("operatorId") Long operatorId, 
                                            @Param("lineId") Long lineId);
    
    /**
     * 查询趋势数据
     * 
     * @param period 时间段
     * @param granularity 粒度
     * @return 趋势数据
     */
    @MapKey("period")
    Map<String, Object> selectTrendData(@Param("period") String period, 
                                       @Param("granularity") String granularity);
    
    /**
     * 查询决策数据
     * 
     * @param startDate 开始日期
     * @param endDate 结束日期
     * @param productionLineId 生产线ID
     * @return 决策数据
     */
    @MapKey("workDate")
    Map<String, Object> selectDecisionData(@Param("startDate") String startDate, 
                                          @Param("endDate") String endDate, 
                                          @Param("productionLineId") Long productionLineId);
    
    /**
     * 查询风险管理数据
     * 
     * @param startDate 开始日期
     * @param endDate 结束日期
     * @return 风险管理数据
     */
    @MapKey("riskType")
    Map<String, Object> selectRiskManagementData(@Param("startDate") String startDate, 
                                                 @Param("endDate") String endDate);
    
    /**
     * 查询工作流数据
     * 
     * @param startDate 开始日期
     * @param endDate 结束日期
     * @param processId 流程ID
     * @return 工作流数据
     */
    List<Map<String, Object>> selectWorkflowData(@Param("startDate") String startDate, 
                                                 @Param("endDate") String endDate, 
                                                 @Param("processId") Long processId);
    
    /**
     * 查询沟通数据
     * 
     * @param startDate 开始日期
     * @param endDate 结束日期
     * @param operatorId 操作员ID
     * @return 沟通数据
     */
    @MapKey("workOrderId")
    Map<String, Object> selectCommunicationData(@Param("startDate") String startDate, 
                                                @Param("endDate") String endDate, 
                                                @Param("operatorId") Long operatorId);
}