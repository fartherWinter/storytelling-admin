package com.chennian.storytelling.dao.project;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.chennian.storytelling.bean.model.project.ProjectMilestone;
import org.apache.ibatis.annotations.MapKey;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.util.List;
import java.util.Map;

/**
 * 项目里程碑管理Mapper接口
 *
 * @author storytelling
 * @date 2024-01-01
 */
@Mapper
public interface ProjectMilestoneMapper extends BaseMapper<ProjectMilestone> {

    /**
     * 查询项目里程碑列表
     */
    IPage<ProjectMilestone> selectMilestoneList(Page<ProjectMilestone> page, @Param("milestone") ProjectMilestone milestone);

    /**
     * 根据项目ID查询里程碑列表
     */
    @Select("SELECT * FROM project_milestone WHERE project_id = #{projectId} AND del_flag = '0' ORDER BY planned_date ASC")
    List<ProjectMilestone> selectMilestonesByProjectId(@Param("projectId") Long projectId);

    /**
     * 根据里程碑名称查询里程碑
     */
    @Select("SELECT * FROM project_milestone WHERE milestone_name = #{milestoneName} AND project_id = #{projectId} AND del_flag = '0'")
    ProjectMilestone selectByMilestoneName(@Param("projectId") Long projectId, @Param("milestoneName") String milestoneName);

    /**
     * 根据里程碑状态查询里程碑列表
     */
    @Select("SELECT * FROM project_milestone WHERE status = #{status} AND del_flag = '0'")
    IPage<ProjectMilestone> selectByStatus(Page<ProjectMilestone> page, @Param("status") Integer status);

    /**
     * 根据里程碑类型查询里程碑列表
     */
    @Select("SELECT * FROM project_milestone WHERE milestone_type = #{milestoneType} AND del_flag = '0'")
    IPage<ProjectMilestone> selectByMilestoneType(Page<ProjectMilestone> page, @Param("milestoneType") Integer milestoneType);

    /**
     * 查询超期里程碑
     */
    @Select("SELECT * FROM project_milestone WHERE planned_date < NOW() AND status = 0 AND del_flag = '0'")
    IPage<ProjectMilestone> selectOverdueMilestones(Page<ProjectMilestone> page);

    /**
     * 查询即将到期的里程碑
     */
    @Select("SELECT * FROM project_milestone WHERE planned_date BETWEEN NOW() AND DATE_ADD(NOW(), INTERVAL #{days} DAY) AND status = 0 AND del_flag = '0'")
    IPage<ProjectMilestone> selectUpcomingDeadlineMilestones(Page<ProjectMilestone> page, @Param("days") Integer days);

    /**
     * 查询已达成的里程碑
     */
    @Select("SELECT * FROM project_milestone WHERE status = 1 AND project_id = #{projectId} AND del_flag = '0' ORDER BY actual_date DESC")
    List<ProjectMilestone> selectAchievedMilestones(@Param("projectId") Long projectId);

    /**
     * 查询未达成的里程碑
     */
    @Select("SELECT * FROM project_milestone WHERE status = 0 AND project_id = #{projectId} AND del_flag = '0' ORDER BY planned_date ASC")
    List<ProjectMilestone> selectPendingMilestones(@Param("projectId") Long projectId);

    /**
     * 统计里程碑数量按状态
     */
    @Select("SELECT status, COUNT(*) as count FROM project_milestone WHERE project_id = #{projectId} AND del_flag = '0' GROUP BY status")
    @MapKey("status")
    Map<String, Object> selectMilestoneCountByStatus(@Param("projectId") Long projectId);

    /**
     * 统计里程碑数量按类型
     */
    @Select("SELECT milestone_type, COUNT(*) as count FROM project_milestone WHERE project_id = #{projectId} AND del_flag = '0' GROUP BY milestone_type")
    @MapKey("milestoneType")
    Map<String, Object> selectMilestoneCountByType(@Param("projectId") Long projectId);

    /**
     * 获取里程碑达成率趋势
     */
    @MapKey("date")
    Map<String, Object> selectMilestoneAchievementTrend(@Param("projectId") Long projectId, 
                                                       @Param("startDate") String startDate, 
                                                       @Param("endDate") String endDate, 
                                                       @Param("period") String period);

    /**
     * 获取里程碑延期分析
     */
    @MapKey("milestoneId")
    Map<String, Object> selectMilestoneDelayAnalysis(@Param("projectId") Long projectId);

    /**
     * 获取关键里程碑
     */
    @Select("SELECT * FROM project_milestone WHERE is_critical = 1 AND project_id = #{projectId} AND del_flag = '0' ORDER BY planned_date ASC")
    List<ProjectMilestone> selectCriticalMilestones(@Param("projectId") Long projectId);

    /**
     * 获取里程碑时间线
     */
    List<Map<String, Object>> selectMilestoneTimeline(@Param("projectId") Long projectId);

    /**
     * 获取里程碑依赖关系
     */
    @Select("SELECT m1.id as milestone_id, m1.milestone_name, m2.id as dependency_id, m2.milestone_name as dependency_name FROM project_milestone m1 LEFT JOIN project_milestone m2 ON m1.dependency_milestone_id = m2.id WHERE m1.project_id = #{projectId} AND m1.del_flag = '0'")
    List<Map<String, Object>> selectMilestoneDependencies(@Param("projectId") Long projectId);

    /**
     * 检查里程碑名称是否存在
     */
    @Select("SELECT COUNT(*) FROM project_milestone WHERE milestone_name = #{milestoneName} AND project_id = #{projectId} AND del_flag = '0'")
    int checkMilestoneNameExists(@Param("projectId") Long projectId, @Param("milestoneName") String milestoneName);

    /**
     * 获取里程碑完成度统计
     */
    @MapKey("status")
    Map<String, Object> selectMilestoneCompletionStatistics(@Param("projectId") Long projectId);

    /**
     * 获取里程碑风险评估
     */
    @MapKey("milestoneId")
    Map<String, Object> selectMilestoneRiskAssessment(@Param("projectId") Long projectId);

    /**
     * 获取项目里程碑进度统计
     */
    @MapKey("milestoneId")
    Map<String, Object> selectMilestoneProgressByProject(@Param("projectId") Long projectId);
}