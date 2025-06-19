package com.chennian.storytelling.dao.workflow;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.chennian.storytelling.bean.dto.workflow.WorkflowTaskDTO;
import com.chennian.storytelling.bean.model.workflow.WfTask;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.Map;

/**
 * 工作流任务Mapper接口
 * 
 * @author chennian
 */
@Mapper
public interface WorkflowTaskMapper extends BaseMapper<WfTask> {

    /**
     * 分页查询任务列表
     * 
     * @param page 分页参数
     * @param queryDTO 查询条件
     * @return 任务列表
     */
    IPage<WfTask> selectTaskList(Page<WfTask> page, @Param("query") WorkflowTaskDTO.TaskQueryDTO queryDTO);

    /**
     * 查询任务详情
     * 
     * @param taskId 任务ID
     * @return 任务详情
     */
    WfTask selectTaskDetail(@Param("taskId") String taskId);

    /**
     * 更新任务优先级
     * 
     * @param taskId 任务ID
     * @param priority 优先级
     * @return 更新行数
     */
    int updateTaskPriority(@Param("taskId") String taskId, @Param("priority") int priority);

    /**
     * 更新任务处理人
     * 
     * @param taskId 任务ID
     * @param assignee 处理人
     * @return 更新行数
     */
    int updateTaskAssignee(@Param("taskId") String taskId, @Param("assignee") String assignee);

    /**
     * 更新任务状态
     * 
     * @param taskId 任务ID
     * @param status 任务状态
     * @return 更新行数
     */
    int updateTaskStatus(@Param("taskId") String taskId, @Param("status") String status);

    /**
     * 查询任务统计信息
     * 
     * @param assignee 处理人
     * @return 统计信息
     */
    Map<String, Object> selectTaskStatistics(@Param("assignee") String assignee);

    /**
     * 根据流程实例ID查询任务列表
     * 
     * @param processInstanceId 流程实例ID
     * @return 任务列表
     */
    java.util.List<WfTask> selectTasksByProcessInstanceId(@Param("processInstanceId") String processInstanceId);

    /**
     * 根据流程定义键查询任务列表
     * 
     * @param processDefinitionKey 流程定义键
     * @return 任务列表
     */
    java.util.List<WfTask> selectTasksByProcessDefinitionKey(@Param("processDefinitionKey") String processDefinitionKey);

    /**
     * 根据处理人查询待办任务数量
     * 
     * @param assignee 处理人
     * @return 待办任务数量
     */
    int countPendingTasksByAssignee(@Param("assignee") String assignee);

    /**
     * 根据候选人查询候选任务数量
     * 
     * @param candidateUser 候选人
     * @return 候选任务数量
     */
    int countCandidateTasksByUser(@Param("candidateUser") String candidateUser);

    /**
     * 根据候选组查询候选任务数量
     * 
     * @param candidateGroup 候选组
     * @return 候选任务数量
     */
    int countCandidateTasksByGroup(@Param("candidateGroup") String candidateGroup);

    /**
     * 查询超时任务列表
     * 
     * @return 超时任务列表
     */
    java.util.List<WfTask> selectOverdueTasks();

    /**
     * 查询即将到期任务列表
     * 
     * @param hours 小时数
     * @return 即将到期任务列表
     */
    java.util.List<WfTask> selectTasksDueSoon(@Param("hours") int hours);

    /**
     * 批量更新任务状态
     * 
     * @param taskIds 任务ID列表
     * @param status 任务状态
     * @return 更新行数
     */
    int batchUpdateTaskStatus(@Param("taskIds") java.util.List<String> taskIds, @Param("status") String status);

    /**
     * 批量更新任务处理人
     * 
     * @param taskIds 任务ID列表
     * @param assignee 处理人
     * @return 更新行数
     */
    int batchUpdateTaskAssignee(@Param("taskIds") java.util.List<String> taskIds, @Param("assignee") String assignee);
}