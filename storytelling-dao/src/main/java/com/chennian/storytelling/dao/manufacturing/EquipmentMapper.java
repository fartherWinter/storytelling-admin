package com.chennian.storytelling.dao.manufacturing;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.chennian.storytelling.bean.model.manufacturing.Equipment;
import org.apache.ibatis.annotations.MapKey;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

/**
 * 设备Mapper接口
 * 
 * @author storytelling
 * @date 2024-01-01
 */
@Mapper
public interface EquipmentMapper extends BaseMapper<Equipment> {
    
    /**
     * 更新设备状态
     * 
     * @param equipmentId 设备ID
     * @param status 设备状态
     * @return 更新结果
     */
    int updateEquipmentStatus(@Param("equipmentId") Long equipmentId, @Param("status") String status);
    
    /**
     * 查询设备OEE分析数据
     * 
     * @param equipmentId 设备ID
     * @param startDate 开始日期
     * @param endDate 结束日期
     * @return OEE分析数据
     */
    @MapKey("equipmentId")
    Map<String, Object> selectOEEAnalysisData(@Param("equipmentId") Long equipmentId,
                                              @Param("startDate") String startDate,
                                              @Param("endDate") String endDate);
    
    /**
     * 查询实时监控数据
     * 
     * @return 实时监控数据
     */
    @MapKey("id")
    Map<String, Object> selectRealTimeMonitoringData();
    
    /**
     * 查询风险数据
     * 
     * @param startDate 开始日期
     * @param endDate 结束日期
     * @param departmentId 部门ID
     * @return 风险数据
     */
    @MapKey("departmentId")
    Map<String, Object> selectRiskData(@Param("startDate") String startDate, 
                                      @Param("endDate") String endDate, 
                                      @Param("departmentId") Long departmentId);
    
    /**
     * 根据生产线ID查询设备列表
     * 
     * @param lineId 生产线ID
     * @return 设备列表
     */
    List<Equipment> selectByLineId(@Param("lineId") Long lineId);
    
    /**
     * 根据车间ID查询设备列表
     * 
     * @param workshopId 车间ID
     * @return 设备列表
     */
    List<Equipment> selectByWorkshopId(@Param("workshopId") Long workshopId);
    
    /**
     * 根据设备状态查询设备列表
     * 
     * @param status 设备状态
     * @return 设备列表
     */
    List<Equipment> selectByStatus(@Param("status") Integer status);
    
    /**
     * 查询设备统计信息
     * 
     * @param startDate 开始日期
     * @param endDate 结束日期
     * @param equipmentId 设备ID（可选）
     * @return 统计信息
     */
    @MapKey("equipmentId")
    Map<String, Object> selectEquipmentStatistics(@Param("startDate") String startDate, 
                                                  @Param("endDate") String endDate, 
                                                  @Param("equipmentId") Long equipmentId);
    
    /**
     * 查询设备利用率统计
     * 
     * @param startDate 开始日期
     * @param endDate 结束日期
     * @param equipmentId 设备ID（可选）
     * @return 利用率统计
     */
    @MapKey("equipmentId")
    List<Map<String, Object>> selectEquipmentUtilizationStatistics(@Param("startDate") String startDate, 
                                                                   @Param("endDate") String endDate, 
                                                                   @Param("equipmentId") Long equipmentId);
    
    /**
     * 查询设备维护记录
     * 
     * @param equipmentId 设备ID
     * @param startDate 开始日期
     * @param endDate 结束日期
     * @return 维护记录
     */
    @MapKey("id")
    List<Map<String, Object>> selectMaintenanceRecords(@Param("equipmentId") Long equipmentId, 
                                                       @Param("startDate") String startDate, 
                                                       @Param("endDate") String endDate);
    
    /**
     * 新增设备维护记录
     * 
     * @param equipmentId 设备ID
     * @param maintenanceType 维护类型
     * @param description 维护描述
     * @param maintenanceDate 维护日期
     * @param maintainer 维护人员
     * @return 插入结果
     */
    int insertMaintenanceRecord(@Param("equipmentId") Long equipmentId, 
                               @Param("maintenanceType") String maintenanceType, 
                               @Param("description") String description, 
                               @Param("maintenanceDate") String maintenanceDate, 
                               @Param("maintainer") String maintainer);
    
    /**
     * 查询设备运行时间统计
     * 
     * @param equipmentId 设备ID
     * @param startDate 开始日期
     * @param endDate 结束日期
     * @return 运行时间统计
     */
    @MapKey("equipmentId")
    Map<String, Object> selectEquipmentRunningTimeStatistics(@Param("equipmentId") Long equipmentId, 
                                                             @Param("startDate") String startDate, 
                                                             @Param("endDate") String endDate);
    
    /**
     * 查询设备故障统计
     * 
     * @param equipmentId 设备ID
     * @param startDate 开始日期
     * @param endDate 结束日期
     * @return 故障统计
     */
    @MapKey("id")
    List<Map<String, Object>> selectEquipmentFailureStatistics(@Param("equipmentId") Long equipmentId, 
                                                               @Param("startDate") String startDate, 
                                                               @Param("endDate") String endDate);
    
    /**
     * 查询设备效率趋势
     * 
     * @param equipmentId 设备ID
     * @param startDate 开始日期
     * @param endDate 结束日期
     * @return 效率趋势
     */
    @MapKey("date")
    List<Map<String, Object>> selectEquipmentEfficiencyTrend(@Param("equipmentId") Long equipmentId, 
                                                             @Param("startDate") String startDate, 
                                                             @Param("endDate") String endDate);
    
    /**
     * 查询设备维护历史
     * 
     * @param startDate 开始日期
     * @param endDate 结束日期
     * @param equipmentId 设备ID（可选）
     * @return 维护历史数据
     */
    @MapKey("id")
    List<Map<String, Object>> selectMaintenanceHistory(@Param("startDate") String startDate, 
                                                       @Param("endDate") String endDate, 
                                                       @Param("equipmentId") Long equipmentId);
    
    /**
     * 查询设备利用率统计数据
     * 
     * @param startDate 开始日期
     * @param endDate 结束日期
     * @param equipmentId 设备ID
     * @return 利用率统计数据
     */
    @MapKey("equipmentId")
    Map<String, Object> selectUtilizationStatistics(@Param("startDate") String startDate, 
                                                     @Param("endDate") String endDate, 
                                                     @Param("equipmentId") Long equipmentId);
    
    /**
     * 查询设备运行时间统计
     * 
     * @param startDate 开始日期
     * @param endDate 结束日期
     * @param equipmentId 设备ID
     * @return 运行时间统计数据
     */
    @MapKey("equipmentId")
    Map<String, Object> selectRunTimeStatistics(@Param("startDate") String startDate, 
                                                @Param("endDate") String endDate, 
                                                @Param("equipmentId") Long equipmentId);
    
    /**
     * 查询设备详细信息
     * 
     * @param startDate 开始日期
     * @param endDate 结束日期
     * @param equipmentId 设备ID
     * @return 设备详细信息
     */
    @MapKey("equipmentId")
    Map<String, Object> selectEquipmentDetails(@Param("startDate") String startDate, 
                                               @Param("endDate") String endDate, 
                                               @Param("equipmentId") Long equipmentId);
    
    /**
     * 查询设备状态分布
     * 
     * @param startDate 开始日期
     * @param endDate 结束日期
     * @return 设备状态分布数据
     */
    @MapKey("status")
    Map<String, Object> selectStatusDistribution(@Param("startDate") String startDate, 
                                                 @Param("endDate") String endDate);
    
    /**
     * 查询设备报告数据
     * 
     * @param startDate 开始日期
     * @param endDate 结束日期
     * @return 设备报告数据
     */
    @MapKey("reportType")
    Map<String, Object> selectEquipmentReportData(@Param("startDate") String startDate, 
                                                  @Param("endDate") String endDate);
    
    /**
     * 查询设备能耗数据
     * 
     * @param startDate 开始日期
     * @param endDate 结束日期
     * @param equipmentId 设备ID
     * @return 能耗数据
     */
    @MapKey("equipmentId")
    Map<String, Object> selectEnergyConsumption(@Param("startDate") String startDate, 
                                               @Param("endDate") String endDate, 
                                               @Param("equipmentId") Long equipmentId);
    
    /**
     * 查询设备维护成本统计
     * 
     * @param startDate 开始日期
     * @param endDate 结束日期
     * @param equipmentId 设备ID
     * @return 维护成本统计数据
     */
    @MapKey("equipmentId")
    Map<String, Object> selectMaintenanceCostStatistics(@Param("startDate") String startDate, 
                                                        @Param("endDate") String endDate, 
                                                        @Param("equipmentId") Long equipmentId);
    
    /**
     * 添加设备维护记录
     * 
     * @param equipmentId 设备ID
     * @param maintenanceType 维护类型
     * @param description 描述
     * @param maintenanceDate 维护日期
     * @param maintainer 维护人员
     * @return 插入结果
     */
    int addMaintenanceRecord(@Param("equipmentId") Long equipmentId, 
                            @Param("maintenanceType") String maintenanceType, 
                            @Param("description") String description, 
                            @Param("maintenanceDate") String maintenanceDate, 
                            @Param("maintainer") String maintainer);
    
    /**
     * 查询维护计划数据
     * 
     * @param startDate 开始日期
     * @param endDate 结束日期
     * @param equipmentId 设备ID
     * @return 维护计划数据
     */
    @MapKey("id")
    List<Map<String, Object>> selectMaintenanceScheduleData(@Param("startDate") String startDate, 
                                                            @Param("endDate") String endDate, 
                                                            @Param("equipmentId") Long equipmentId);
    
    /**
     * 查询能效数据
     * 
     * @param startDate 开始日期
     * @param endDate 结束日期
     * @param equipmentId 设备ID
     * @return 能效数据
     */
    @MapKey("equipmentId")
    Map<String, Object> selectEnergyEfficiencyData(@Param("startDate") String startDate, 
                                                   @Param("endDate") String endDate, 
                                                   @Param("equipmentId") Long equipmentId);
    
    /**
     * 查询安全数据
     * 
     * @param startDate 开始日期
     * @param endDate 结束日期
     * @param departmentId 部门ID
     * @return 安全数据
     */
    @MapKey("departmentId")
    Map<String, Object> selectSafetyData(@Param("startDate") String startDate, 
                                         @Param("endDate") String endDate, 
                                         @Param("departmentId") Long departmentId);
    
    /**
     * 查询环境数据
     * 
     * @param startDate 开始日期
     * @param endDate 结束日期
     * @param factoryId 工厂ID
     * @return 环境数据
     */
    @MapKey("factoryId")
    Map<String, Object> selectEnvironmentalData(@Param("startDate") String startDate, 
                                                @Param("endDate") String endDate, 
                                                @Param("factoryId") Long factoryId);
    
    /**
     * 查询技术数据
     * 
     * @param startDate 开始日期
     * @param endDate 结束日期
     * @param equipmentId 设备ID
     * @return 技术数据
     */
    @MapKey("equipmentId")
    Map<String, Object> selectTechnologyData(@Param("startDate") String startDate, 
                                             @Param("endDate") String endDate, 
                                             @Param("equipmentId") Long equipmentId);
    

}