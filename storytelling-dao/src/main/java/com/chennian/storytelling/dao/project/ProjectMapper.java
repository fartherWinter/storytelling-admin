package com.chennian.storytelling.dao.project;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.chennian.storytelling.bean.model.project.Project;
import com.chennian.storytelling.bean.vo.ProjectProgressVO;
import com.chennian.storytelling.bean.vo.ProjectStatisticsVO;
import org.apache.ibatis.annotations.MapKey;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.util.List;
import java.util.Map;

/**
 * 项目管理Mapper接口
 *
 * @author storytelling
 * @date 2024-01-01
 */
@Mapper
public interface ProjectMapper extends BaseMapper<Project> {

    /**
     * 查询项目列表
     */
    IPage<Project> selectProjectList(Page<Project> page, @Param("project") Project project);

    /**
     * 根据项目编号查询项目
     */
    @Select("SELECT * FROM project WHERE project_no = #{projectNo} AND del_flag = '0'")
    Project selectByProjectNo(@Param("projectNo") String projectNo);

    /**
     * 根据项目经理查询项目列表
     */
    @Select("SELECT * FROM project WHERE manager_id = #{managerId} AND del_flag = '0'")
    IPage<Project> selectByManagerId(Page<Project> page, @Param("managerId") Long managerId);

    /**
     * 根据部门查询项目列表
     */
    @Select("SELECT * FROM project WHERE dept_id = #{deptId} AND del_flag = '0'")
    IPage<Project> selectByDeptId(Page<Project> page, @Param("deptId") Long deptId);

    /**
     * 根据项目状态查询项目列表
     */
    @Select("SELECT * FROM project WHERE status = #{status} AND del_flag = '0'")
    IPage<Project> selectByStatus(Page<Project> page, @Param("status") Integer status);

    /**
     * 根据项目类型查询项目列表
     */
    @Select("SELECT * FROM project WHERE project_type = #{projectType} AND del_flag = '0'")
    IPage<Project> selectByProjectType(Page<Project> page, @Param("projectType") Integer projectType);

    /**
     * 根据优先级查询项目列表
     */
    @Select("SELECT * FROM project WHERE priority = #{priority} AND del_flag = '0'")
    IPage<Project> selectByPriority(Page<Project> page, @Param("priority") Integer priority);

    /**
     * 查询超期项目
     */
    @Select("SELECT * FROM project WHERE planned_end_time < NOW() AND status IN (0, 1, 2) AND del_flag = '0'")
    IPage<Project> selectOverdueProjects(Page<Project> page);

    /**
     * 查询即将到期的项目
     */
    @Select("SELECT * FROM project WHERE planned_end_time BETWEEN NOW() AND DATE_ADD(NOW(), INTERVAL #{days} DAY) AND status IN (0, 1, 2) AND del_flag = '0'")
    IPage<Project> selectUpcomingDeadlineProjects(Page<Project> page, @Param("days") Integer days);

    /**
     * 获取项目概览统计
     */
    ProjectStatisticsVO selectProjectOverviewStatistics(@Param("startDate") String startDate,
                                                        @Param("endDate") String endDate,
                                                        @Param("status") String status);

    /**
     * 获取项目进度分析
     */
    ProjectProgressVO selectProjectProgressAnalysis(@Param("projectId") Long projectId,
                                                    @Param("startDate") String startDate,
                                                    @Param("endDate") String endDate);

    /**
     * 获取项目成本分析
     */
    @MapKey("costType")
    Map<String, Object> selectProjectCostAnalysis(@Param("projectId") Long projectId, 
                                                  @Param("startDate") String startDate, 
                                                  @Param("endDate") String endDate);

    /**
     * 获取项目绩效分析
     */
    @MapKey("metric")
    Map<String, Object> selectProjectPerformanceAnalysis(@Param("startDate") String startDate, 
                                                         @Param("endDate") String endDate);

    /**
     * 获取项目交付物分析
     */
    @MapKey("deliverableType")
    Map<String, Object> selectProjectDeliverableAnalysis(@Param("projectId") Long projectId);

    /**
     * 统计项目数量按状态
     */
    @Select("SELECT status, COUNT(*) as count FROM project WHERE del_flag = '0' GROUP BY status")
    Map<String, Object> selectProjectCountByStatus();

    /**
     * 统计项目数量按类型
     */
    @Select("SELECT project_type, COUNT(*) as count FROM project WHERE del_flag = '0' GROUP BY project_type")
    Map<String, Object> selectProjectCountByType();

    /**
     * 统计项目数量按优先级
     */
    @Select("SELECT priority, COUNT(*) as count FROM project WHERE del_flag = '0' GROUP BY priority")
    Map<String, Object> selectProjectCountByPriority();

    /**
     * 统计项目数量按部门
     */
    @Select("SELECT dept_name, COUNT(*) as count FROM project WHERE del_flag = '0' GROUP BY dept_id, dept_name")
    Map<String, Object> selectProjectCountByDept();

    /**
     * 获取项目进度趋势
     */
    @MapKey("period")
    Map<String, Object> selectProjectProgressTrend(@Param("startDate") String startDate, 
                                                   @Param("endDate") String endDate, 
                                                   @Param("period") String period);

    /**
     * 获取项目成本趋势
     */
    @MapKey("period")
    Map<String, Object> selectProjectCostTrend(@Param("startDate") String startDate, 
                                               @Param("endDate") String endDate, 
                                               @Param("period") String period);

    /**
     * 检查项目编号是否存在
     */
    @Select("SELECT COUNT(*) FROM project WHERE project_no = #{projectNo} AND del_flag = '0'")
    int checkProjectNoExists(@Param("projectNo") String projectNo);

    /**
     * 获取所有项目进度统计
     */
    @MapKey("projectId")
    Map<String, Object> selectAllProjectsProgress(@Param("startDate") String startDate, 
                                                  @Param("endDate") String endDate);

    /**
     * 获取所有项目成本统计
     */
    @MapKey("projectId")
    Map<String, Object> selectAllProjectsCost(@Param("startDate") String startDate, 
                                              @Param("endDate") String endDate);

    /**
     * 根据日期范围查询项目列表
     */
    @MapKey("projectId")
    List<Map<String, Object>> selectProjectsByDateRange(@Param("startDate") String startDate,
                                                        @Param("endDate") String endDate);

    /**
     * 获取项目资源分析
     */
    @MapKey("resourceType")
    Map<String, Object> selectProjectResourceAnalysis(@Param("projectId") Long projectId);
}