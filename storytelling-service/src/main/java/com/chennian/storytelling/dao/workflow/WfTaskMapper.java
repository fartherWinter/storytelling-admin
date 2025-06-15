package com.chennian.storytelling.dao.workflow;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.chennian.storytelling.bean.model.workflow.WfTask;
import com.chennian.storytelling.bean.dto.TaskStatisticsDTO;
import com.chennian.storytelling.bean.dto.PendingTaskDTO;
import com.chennian.storytelling.bean.dto.RecentTaskDTO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.Date;
import java.util.List;

/**
 * 工作流任务Mapper接口
 * 
 * @author storytelling
 * @date 2024-01-01
 */
@Mapper
public interface WfTaskMapper extends BaseMapper<WfTask> {

    /**
     * 根据流程实例ID查询任务列表
     * 
     * @param processInstanceId 流程实例ID
     * @return 任务列表
     */
    List<WfTask> selectByProcessInstanceId(@Param("processInstanceId") String processInstanceId);

    /**
     * 根据流程定义ID查询任务列表
     * 
     * @param processDefinitionId 流程定义ID
     * @return 任务列表
     */
    List<WfTask> selectByProcessDefinitionId(@Param("processDefinitionId") String processDefinitionId);

    /**
     * 根据流程定义键查询任务列表
     * 
     * @param processDefinitionKey 流程定义键
     * @return 任务列表
     */
    List<WfTask> selectByProcessDefinitionKey(@Param("processDefinitionKey") String processDefinitionKey);

    /**
     * 根据任务状态查询任务列表
     * 
     * @param taskStatus 任务状态
     * @return 任务列表
     */
    List<WfTask> selectByTaskStatus(@Param("taskStatus") String taskStatus);

    /**
     * 根据分配人ID查询任务列表
     * 
     * @param assignee 分配人ID
     * @return 任务列表
     */
    List<WfTask> selectByAssignee(@Param("assignee") String assignee);

    /**
     * 根据候选人查询任务列表
     * 
     * @param candidateUser 候选人ID
     * @return 任务列表
     */
    List<WfTask> selectByCandidateUser(@Param("candidateUser") String candidateUser);

    /**
     * 根据候选组查询任务列表
     * 
     * @param candidateGroup 候选组
     * @return 任务列表
     */
    List<WfTask> selectByCandidateGroup(@Param("candidateGroup") String candidateGroup);

    /**
     * 根据委托人ID查询任务列表
     * 
     * @param owner 委托人ID
     * @return 任务列表
     */
    List<WfTask> selectByOwner(@Param("owner") String owner);

    /**
     * 根据任务类型查询任务列表
     * 
     * @param taskType 任务类型
     * @return 任务列表
     */
    List<WfTask> selectByTaskType(@Param("taskType") String taskType);

    /**
     * 根据优先级查询任务列表
     * 
     * @param priority 优先级
     * @return 任务列表
     */
    List<WfTask> selectByPriority(@Param("priority") Integer priority);

    /**
     * 根据租户ID查询任务列表
     * 
     * @param tenantId 租户ID
     * @return 任务列表
     */
    List<WfTask> selectByTenantId(@Param("tenantId") String tenantId);

    /**
     * 根据业务键查询任务列表
     * 
     * @param businessKey 业务键
     * @return 任务列表
     */
    List<WfTask> selectByBusinessKey(@Param("businessKey") String businessKey);

    /**
     * 查询待办任务列表
     * 
     * @param assignee 分配人ID
     * @return 任务列表
     */
    List<WfTask> selectTodoTasks(@Param("assignee") String assignee);

    /**
     * 查询已办任务列表
     * 
     * @param assignee 分配人ID
     * @return 任务列表
     */
    List<WfTask> selectDoneTasks(@Param("assignee") String assignee);

    /**
     * 查询候选任务列表
     * 
     * @param candidateUser 候选人ID
     * @param candidateGroups 候选组列表
     * @return 任务列表
     */
    List<WfTask> selectCandidateTasks(@Param("candidateUser") String candidateUser, 
                                     @Param("candidateGroups") List<String> candidateGroups);

    /**
     * 查询超时任务列表
     * 
     * @return 任务列表
     */
    List<WfTask> selectOverdueTasks();

    /**
     * 查询即将超时的任务列表
     * 
     * @param hours 小时数
     * @return 任务列表
     */
    List<WfTask> selectSoonOverdueTasks(@Param("hours") Integer hours);

    /**
     * 根据时间范围查询任务列表
     * 
     * @param startTime 开始时间
     * @param endTime 结束时间
     * @return 任务列表
     */
    List<WfTask> selectByTimeRange(@Param("startTime") Date startTime, 
                                  @Param("endTime") Date endTime);

    /**
     * 根据任务名称模糊查询
     * 
     * @param taskName 任务名称
     * @return 任务列表
     */
    List<WfTask> selectByTaskNameLike(@Param("taskName") String taskName);

    /**
     * 查询任务统计信息
     * 
     * @return 统计信息
     */
    List<TaskStatisticsDTO> selectTaskStatistics();

    /**
     * 根据状态统计任务数量
     * 
     * @return 统计信息
     */
    List<TaskStatisticsDTO> selectTaskCountByStatus();

    /**
     * 根据分配人统计任务数量
     * 
     * @return 统计信息
     */
    List<TaskStatisticsDTO> selectTaskCountByAssignee();

    /**
     * 根据任务类型统计任务数量
     * 
     * @return 统计信息
     */
    List<TaskStatisticsDTO> selectTaskCountByType();

    /**
     * 根据流程定义统计任务数量
     * 
     * @return 统计信息
     */
    List<TaskStatisticsDTO> selectTaskCountByProcessDefinition();

    /**
     * 查询平均处理时长
     * 
     * @return 平均时长（毫秒）
     */
    Long selectAverageDuration();

    /**
     * 根据任务类型查询平均处理时长
     * 
     * @param taskType 任务类型
     * @return 平均时长（毫秒）
     */
    Long selectAverageDurationByTaskType(@Param("taskType") String taskType);

    /**
     * 根据分配人查询平均处理时长
     * 
     * @param assignee 分配人ID
     * @return 平均时长（毫秒）
     */
    Long selectAverageDurationByAssignee(@Param("assignee") String assignee);

    /**
     * 批量更新任务状态
     * 
     * @param taskIds 任务ID列表
     * @param taskStatus 任务状态
     * @param updatedBy 更新人
     * @return 更新行数
     */
    int batchUpdateTaskStatus(@Param("taskIds") List<String> taskIds, 
                             @Param("taskStatus") String taskStatus, 
                             @Param("updatedBy") String updatedBy);

    /**
     * 批量分配任务
     * 
     * @param taskIds 任务ID列表
     * @param assignee 分配人ID
     * @param updatedBy 更新人
     * @return 更新行数
     */
    int batchAssignTasks(@Param("taskIds") List<String> taskIds, 
                        @Param("assignee") String assignee, 
                        @Param("updatedBy") String updatedBy);

    /**
     * 查询待处理任务统计
     * 
     * @return 待处理任务统计信息
     */
    List<PendingTaskDTO> selectPendingTasks();

    /**
     * 查询最近任务列表
     * 
     * @param limit 限制数量
     * @return 最近任务列表
     */
    List<RecentTaskDTO> selectRecentTasks(@Param("limit") Integer limit);
}