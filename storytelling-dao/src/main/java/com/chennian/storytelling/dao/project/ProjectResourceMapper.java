package com.chennian.storytelling.dao.project;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.chennian.storytelling.bean.model.project.ProjectResource;
import org.apache.ibatis.annotations.MapKey;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.util.List;
import java.util.Map;

/**
 * 项目资源管理Mapper接口
 *
 * @author storytelling
 * @date 2024-01-01
 */
@Mapper
public interface ProjectResourceMapper extends BaseMapper<ProjectResource> {

    /**
     * 查询项目资源列表
     */
    IPage<ProjectResource> selectResourceList(Page<ProjectResource> page, @Param("resource") ProjectResource resource);

    /**
     * 根据项目ID查询资源列表
     */
    @Select("SELECT * FROM project_resource WHERE project_id = #{projectId} AND del_flag = '0' ORDER BY create_time DESC")
    List<ProjectResource> selectResourcesByProjectId(@Param("projectId") Long projectId);

    /**
     * 根据资源名称查询资源
     */
    @Select("SELECT * FROM project_resource WHERE resource_name = #{resourceName} AND del_flag = '0'")
    ProjectResource selectByResourceName(@Param("resourceName") String resourceName);

    /**
     * 根据资源类型查询资源列表
     */
    @Select("SELECT * FROM project_resource WHERE resource_type = #{resourceType} AND del_flag = '0'")
    IPage<ProjectResource> selectByResourceType(Page<ProjectResource> page, @Param("resourceType") Integer resourceType);

    /**
     * 根据资源状态查询资源列表
     */
    @Select("SELECT * FROM project_resource WHERE status = #{status} AND del_flag = '0'")
    IPage<ProjectResource> selectByStatus(Page<ProjectResource> page, @Param("status") Integer status);

    /**
     * 根据供应商查询资源列表
     */
    @Select("SELECT * FROM project_resource WHERE supplier = #{supplier} AND del_flag = '0'")
    IPage<ProjectResource> selectBySupplier(Page<ProjectResource> page, @Param("supplier") String supplier);

    /**
     * 查询可用资源
     */
    @Select("SELECT * FROM project_resource WHERE status = 1 AND available_quantity > 0 AND del_flag = '0'")
    IPage<ProjectResource> selectAvailableResources(Page<ProjectResource> page);

    /**
     * 查询资源不足的项目资源
     */
    @Select("SELECT * FROM project_resource WHERE available_quantity < minimum_quantity AND del_flag = '0'")
    IPage<ProjectResource> selectInsufficientResources(Page<ProjectResource> page);

    /**
     * 查询即将到期的资源
     */
    @Select("SELECT * FROM project_resource WHERE expiry_date BETWEEN NOW() AND DATE_ADD(NOW(), INTERVAL #{days} DAY) AND del_flag = '0'")
    IPage<ProjectResource> selectExpiringResources(Page<ProjectResource> page, @Param("days") Integer days);

    /**
     * 获取资源利用率分析
     */
    @MapKey("resourceId")
    Map<String, Object> selectResourceUtilizationAnalysis(@Param("startDate") String startDate, 
                                                          @Param("endDate") String endDate, 
                                                          @Param("resourceType") Integer resourceType);

    /**
     * 获取项目资源分析
     */
    @MapKey("resourceType")
    Map<String, Object> selectProjectResourceAnalysis(@Param("projectId") Long projectId);

    /**
     * 统计资源数量按类型
     */
    @Select("SELECT resource_type, COUNT(*) as count, SUM(total_quantity) as total_quantity, SUM(allocated_quantity) as allocated_quantity, SUM(available_quantity) as available_quantity FROM project_resource WHERE del_flag = '0' GROUP BY resource_type")
    Map<String, Object> selectResourceCountByType();

    /**
     * 统计资源数量按状态
     */
    @Select("SELECT status, COUNT(*) as count FROM project_resource WHERE del_flag = '0' GROUP BY status")
    Map<String, Object> selectResourceCountByStatus();

    /**
     * 统计资源数量按供应商
     */
    @Select("SELECT supplier, COUNT(*) as count, SUM(total_cost) as total_cost FROM project_resource WHERE del_flag = '0' GROUP BY supplier")
    Map<String, Object> selectResourceCountBySupplier();

    /**
     * 获取资源成本趋势
     */
    @MapKey("date")
    Map<String, Object> selectResourceCostTrend(@Param("startDate") String startDate, 
                                               @Param("endDate") String endDate, 
                                               @Param("period") String period);

    /**
     * 获取资源分配历史
     */
    @MapKey("resourceId")
    List<Map<String, Object>> selectResourceAllocationHistory(@Param("resourceId") Long resourceId);

    /**
     * 获取资源需求预测
     */
    @MapKey("resourceType")
    Map<String, Object> selectResourceDemandForecast(@Param("projectId") Long projectId, 
                                                     @Param("forecastPeriod") Integer forecastPeriod);

    /**
     * 获取资源冲突分析
     */
    @MapKey("resourceName")
    List<Map<String, Object>> selectResourceConflictAnalysis(@Param("startDate") String startDate, 
                                                             @Param("endDate") String endDate);

    /**
     * 获取资源效率分析
     */
    @MapKey("resourceId")
    Map<String, Object> selectResourceEfficiencyAnalysis(@Param("projectId") Long projectId);

    /**
     * 检查资源名称是否存在
     */
    @Select("SELECT COUNT(*) FROM project_resource WHERE resource_name = #{resourceName} AND del_flag = '0'")
    int checkResourceNameExists(@Param("resourceName") String resourceName);

    /**
     * 获取资源库存预警
     */
    @Select("SELECT * FROM project_resource WHERE available_quantity <= warning_quantity AND del_flag = '0'")
    List<ProjectResource> selectResourceStockWarning();

    /**
     * 获取资源采购建议
     */
    @MapKey("resourceType")
    Map<String, Object> selectResourceProcurementSuggestion(@Param("projectId") Long projectId);

    /**
     * 获取资源质量评估
     */
    @MapKey("resourceId")
    Map<String, Object> selectResourceQualityAssessment(@Param("resourceType") Integer resourceType);

    /**
     * 获取项目资源成本统计
     */
    @MapKey("resourceType")
    Map<String, Object> selectResourceCostsByProject(@Param("projectId") Long projectId);

    /**
     * 获取项目资源利用率统计
     */
    @MapKey("resourceId")
    Map<String, Object> selectResourceUtilizationByProject(@Param("projectId") Long projectId);

    /**
     * 获取所有资源利用率统计
     */
    @MapKey("resourceType")
    Map<String, Object> selectAllResourcesUtilization(@Param("startDate") String startDate, 
                                                      @Param("endDate") String endDate);
}