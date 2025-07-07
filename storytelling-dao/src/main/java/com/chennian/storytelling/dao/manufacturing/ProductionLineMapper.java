package com.chennian.storytelling.dao.manufacturing;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.chennian.storytelling.bean.model.manufacturing.ProductionLine;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.MapKey;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;

/**
 * 生产线Mapper接口
 * 
 * @author storytelling
 * @date 2024-01-01
 */
@Mapper
public interface ProductionLineMapper extends BaseMapper<ProductionLine> {
    
    /**
     * 更新生产线状态
     * 
     * @param lineId 生产线ID
     * @param status 生产线状态
     * @return 更新结果
     */
    int updateLineStatus(@Param("lineId") Long lineId, @Param("status") String status);
    
    /**
     * 根据车间ID查询生产线列表
     * 
     * @param workshopId 车间ID
     * @return 生产线列表
     */
    List<ProductionLine> selectByWorkshopId(@Param("workshopId") Long workshopId);
    
    /**
     * 查询产能利用率数据
     * 
     * @param startDate 开始日期
     * @param endDate 结束日期
     * @param lineId 生产线ID
     * @return 产能利用率数据
     */
    @MapKey("lineId")
    Map<String, Object> selectCapacityUtilizationData(@Param("startDate") String startDate, 
                                                      @Param("endDate") String endDate, 
                                                      @Param("lineId") Long lineId);
    
    /**
     * 根据生产线状态查询生产线列表
     * 
     * @param status 生产线状态
     * @return 生产线列表
     */
    List<ProductionLine> selectByStatus(@Param("status") Integer status);
    
    /**
     * 查询生产线统计信息
     * 
     * @param startDate 开始日期
     * @param endDate 结束日期
     * @param lineId 生产线ID
     * @return 生产线统计信息
     */
    @MapKey("lineId")
    Map<String, Object> selectProductionLineStatistics(@Param("startDate") String startDate, 
                                                       @Param("endDate") String endDate, 
                                                       @Param("lineId") Long lineId);
    
    /**
     * 查询生产线产能分析
     * 
     * @param startDate 开始日期
     * @param endDate 结束日期
     * @param lineId 生产线ID
     * @return 产能分析数据
     */
    @MapKey("lineId")
    Map<String, Object> selectProductionLineCapacityAnalysis(@Param("startDate") String startDate, 
                                                             @Param("endDate") String endDate, 
                                                             @Param("lineId") Long lineId);
    
    /**
     * 查询生产线效率统计
     * 
     * @param startDate 开始日期
     * @param endDate 结束日期
     * @param lineId 生产线ID
     * @return 效率统计数据
     */
    @MapKey("id")
    List<Map<String, Object>> selectProductionLineEfficiencyStatistics(@Param("startDate") String startDate, 
                                                                       @Param("endDate") String endDate, 
                                                                       @Param("lineId") Long lineId);
    
    /**
     * 查询生产线运行时间统计
     * 
     * @param startDate 开始日期
     * @param endDate 结束日期
     * @param lineId 生产线ID
     * @return 运行时间统计数据
     */
    @MapKey("lineId")
    Map<String, Object> selectProductionLineRunningTimeStatistics(@Param("startDate") String startDate, 
                                                                  @Param("endDate") String endDate, 
                                                                  @Param("lineId") Long lineId);
    
    /**
     * 查询生产线产量统计
     * 
     * @param lineId 生产线ID
     * @param startDate 开始日期
     * @param endDate 结束日期
     * @return 产量统计
     */
    Map<String, Object> selectProductionLineOutputStatistics(@Param("lineId") Long lineId, 
                                                             @Param("startDate") String startDate, 
                                                             @Param("endDate") String endDate);
    
    /**
     * 查询生产线质量统计
     * 
     * @param lineId 生产线ID
     * @param startDate 开始日期
     * @param endDate 结束日期
     * @return 质量统计
     */
    Map<String, Object> selectProductionLineQualityStatistics(@Param("lineId") Long lineId, 
                                                              @Param("startDate") String startDate, 
                                                              @Param("endDate") String endDate);
    
    /**
     * 查询生产线效率趋势
     * 
     * @param lineId 生产线ID
     * @param startDate 开始日期
     * @param endDate 结束日期
     * @return 效率趋势
     */
    List<Map<String, Object>> selectProductionLineEfficiencyTrend(@Param("lineId") Long lineId, 
                                                                 @Param("startDate") String startDate, 
                                                                 @Param("endDate") String endDate);
    
    /**
     * 查询生产线瓶颈分析
     * 
     * @param lineId 生产线ID
     * @param startDate 开始日期
     * @param endDate 结束日期
     * @return 瓶颈分析
     */
    List<Map<String, Object>> selectProductionLineBottleneckAnalysis(@Param("lineId") Long lineId, 
                                                                     @Param("startDate") String startDate, 
                                                                     @Param("endDate") String endDate);
    
    /**
     * 查询效率统计数据
     * 
     * @param startDate 开始日期
     * @param endDate 结束日期
     * @param lineId 生产线ID
     * @return 效率统计数据
     */
    Map<String, Object> selectEfficiencyStatistics(@Param("startDate") String startDate, 
                                                    @Param("endDate") String endDate, 
                                                    @Param("lineId") Long lineId);
    
    /**
     * 查询效率趋势数据
     * 
     * @param startDate 开始日期
     * @param endDate 结束日期
     * @param lineId 生产线ID
     * @return 效率趋势数据
     */
    List<Map<String, Object>> selectEfficiencyTrends(@Param("startDate") String startDate, 
                                                      @Param("endDate") String endDate, 
                                                      @Param("lineId") Long lineId);
    
    /**
     * 查询生产线对比数据
     * 
     * @param startDate 开始日期
     * @param endDate 结束日期
     * @return 生产线对比数据
     */
    List<Map<String, Object>> selectLineComparisons(@Param("startDate") String startDate, 
                                                     @Param("endDate") String endDate);
    
    /**
     * 查询产能分析数据
     * 
     * @param id 生产线ID
     * @param startDate 开始日期
     * @param endDate 结束日期
     * @return 产能分析数据
     */
    Map<String, Object> selectCapacityAnalysis(@Param("id") Long id, 
                                               @Param("startDate") String startDate, 
                                               @Param("endDate") String endDate);
    
    /**
     * 查询瓶颈分析数据
     * 
     * @param startDate 开始日期
     * @param endDate 结束日期
     * @param lineId 生产线ID
     * @return 瓶颈分析数据
     */
    List<Map<String, Object>> selectBottleneckAnalysis(@Param("startDate") String startDate, 
                                                       @Param("endDate") String endDate, 
                                                       @Param("lineId") Long lineId);
}