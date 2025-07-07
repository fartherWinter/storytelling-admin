package com.chennian.storytelling.dao.project;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.chennian.storytelling.bean.model.project.ProjectTask;
import org.apache.ibatis.annotations.MapKey;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.util.List;
import java.util.Map;

/**
 * 项目任务管理Mapper接口
 *
 * @author storytelling
 * @date 2024-01-01
 */
@Mapper
public interface ProjectTaskMapper extends BaseMapper<ProjectTask> {

    /**
     * 查询项目任务列表
     */
    IPage<ProjectTask> selectTaskList(Page<ProjectTask> page, @Param("task") ProjectTask task);

    /**
     * 根据项目ID查询任务列表
     */
    @Select("SELECT * FROM project_task WHERE project_id = #{projectId} AND del_flag = '0' ORDER BY create_time DESC")
    List<ProjectTask> selectTasksByProjectId(@Param("projectId") Long projectId);

    /**
     * 根据任务编号查询任务
     */
    @Select("SELECT * FROM project_task WHERE task_no = #{taskNo} AND del_flag = '0'")
    ProjectTask selectByTaskNo(@Param("taskNo") String taskNo);

    /**
     * 根据负责人查询任务列表
     */
    @Select("SELECT * FROM project_task WHERE assignee_id = #{assigneeId} AND del_flag = '0'")
    IPage<ProjectTask> selectByAssigneeId(Page<ProjectTask> page, @Param("assigneeId") Long assigneeId);

    /**
     * 根据任务状态查询任务列表
     */
    @Select("SELECT * FROM project_task WHERE status = #{status} AND del_flag = '0'")
    IPage<ProjectTask> selectByStatus(Page<ProjectTask> page, @Param("status") Integer status);

    /**
     * 根据任务类型查询任务列表
     */
    @Select("SELECT * FROM project_task WHERE task_type = #{taskType} AND del_flag = '0'")
    IPage<ProjectTask> selectByTaskType(Page<ProjectTask> page, @Param("taskType") Integer taskType);

    /**
     * 根据优先级查询任务列表
     */
    @Select("SELECT * FROM project_task WHERE priority = #{priority} AND del_flag = '0'")
    IPage<ProjectTask> selectByPriority(Page<ProjectTask> page, @Param("priority") Integer priority);

    /**
     * 根据父任务ID查询子任务列表
     */
    @Select("SELECT * FROM project_task WHERE parent_id = #{parentId} AND del_flag = '0' ORDER BY create_time ASC")
    List<ProjectTask> selectSubTasksByParentId(@Param("parentId") Long parentId);

    /**
     * 查询超期任务
     */
    @Select("SELECT * FROM project_task WHERE planned_end_time < NOW() AND status IN (0, 1, 2) AND del_flag = '0'")
    IPage<ProjectTask> selectOverdueTasks(Page<ProjectTask> page);

    /**
     * 查询即将到期的任务
     */
    @Select("SELECT * FROM project_task WHERE planned_end_time BETWEEN NOW() AND DATE_ADD(NOW(), INTERVAL #{days} DAY) AND status IN (0, 1, 2) AND del_flag = '0'")
    IPage<ProjectTask> selectUpcomingDeadlineTasks(Page<ProjectTask> page, @Param("days") Integer days);

    /**
     * 获取任务甘特图数据
     */
    @MapKey("taskId")
    List<Map<String, Object>> selectTaskGanttData(@Param("projectId") Long projectId);

    /**
     * 获取项目团队绩效分析
     */
    @MapKey("memberId")
    Map<String, Object> selectTeamPerformanceAnalysis(@Param("projectId") Long projectId);

    /**
     * 统计任务数量按状态
     */
    @Select("SELECT status, COUNT(*) as count FROM project_task WHERE project_id = #{projectId} AND del_flag = '0' GROUP BY status")
    @MapKey("status")
    Map<String, Object> selectTaskCountByStatus(@Param("projectId") Long projectId);

    /**
     * 统计任务数量按类型
     */
    @Select("SELECT task_type, COUNT(*) as count FROM project_task WHERE project_id = #{projectId} AND del_flag = '0' GROUP BY task_type")
    @MapKey("taskType")
    Map<String, Object> selectTaskCountByType(@Param("projectId") Long projectId);

    /**
     * 统计任务数量按优先级
     */
    @Select("SELECT priority, COUNT(*) as count FROM project_task WHERE project_id = #{projectId} AND del_flag = '0' GROUP BY priority")
    @MapKey("priority")
    Map<String, Object> selectTaskCountByPriority(@Param("projectId") Long projectId);

    /**
     * 统计任务数量按负责人
     */
    @Select("SELECT assignee_name, COUNT(*) as count FROM project_task WHERE project_id = #{projectId} AND del_flag = '0' GROUP BY assignee_id, assignee_name")
    @MapKey("assigneeId")
    Map<String, Object> selectTaskCountByAssignee(@Param("projectId") Long projectId);

    /**
     * 获取任务完成率趋势
     */
    @MapKey("date")
    Map<String, Object> selectTaskCompletionTrend(@Param("projectId") Long projectId, 
                                                  @Param("startDate") String startDate, 
                                                  @Param("endDate") String endDate, 
                                                  @Param("period") String period);

    /**
     * 获取任务工时统计
     */
    @MapKey("taskId")
    Map<String, Object> selectWorkHourStatistics(@Param("projectId") Long projectId,
                                                      @Param("startDate") String startDate, 
                                                      @Param("endDate") String endDate);

    /**
     * 获取任务依赖关系
     */
    @Select("SELECT t1.id as task_id, t1.task_name, t2.id as dependency_id, t2.task_name as dependency_name FROM project_task t1 LEFT JOIN project_task t2 ON t1.parent_id = t2.id WHERE t1.project_id = #{projectId} AND t1.del_flag = '0'")
    List<Map<String, Object>> selectTaskDependencies(@Param("projectId") Long projectId);

    /**
     * 获取关键路径任务
     */
    @MapKey("taskId")
    List<Map<String, Object>> selectCriticalPathTasks(@Param("projectId") Long projectId);

    /**
     * 检查任务编号是否存在
     */
    @Select("SELECT COUNT(*) FROM project_task WHERE task_no = #{taskNo} AND del_flag = '0'")
    int checkTaskNoExists(@Param("taskNo") String taskNo);

    /**
     * 获取任务层级结构
     */
    @MapKey("taskId")
    List<Map<String, Object>> selectTaskHierarchy(@Param("projectId") Long projectId);

    /**
     * 获取任务资源分配情况
     */
    @MapKey("taskId")
    Map<String, Object> selectTaskResourceAllocation(@Param("projectId") Long projectId);

    /**
     * 获取项目任务进度统计
     */
    @MapKey("taskId")
    Map<String, Object> selectTaskProgressByProject(@Param("projectId") Long projectId);
}