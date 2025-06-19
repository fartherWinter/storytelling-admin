package com.chennian.storytelling.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.chennian.storytelling.bean.dto.TaskStatisticsDTO;
import com.chennian.storytelling.bean.dto.workflow.WorkflowTaskDTO;

import java.util.List;

/**
 * 工作流任务服务接口
 * 
 * @author chennian
 */
public interface WorkflowTaskService {

    /**
     * 获取任务列表
     * 
     * @param queryDTO 查询条件
     * @return 任务列表分页结果
     */
    IPage<WorkflowTaskDTO.TaskInfoDTO> getTaskList(WorkflowTaskDTO.TaskQueryDTO queryDTO);

    /**
     * 获取任务详情
     * 
     * @param taskId 任务ID
     * @return 任务详情
     */
    WorkflowTaskDTO.TaskDetailDTO getTaskDetail(String taskId);

    /**
     * 设置任务优先级
     * 
     * @param taskId 任务ID
     * @param priority 优先级
     */
    void setPriority(String taskId, int priority);

    /**
     * 认领任务
     * 
     * @param taskId 任务ID
     * @param assignee 处理人
     */
    void claimTask(String taskId, String assignee);

    /**
     * 完成任务
     * 
     * @param taskId 任务ID
     * @param variables 流程变量
     * @param comment 备注
     */
    void completeTask(String taskId, java.util.Map<String, Object> variables, String comment);

    /**
     * 转办任务
     * 
     * @param taskId 任务ID
     * @param assignee 新处理人
     * @param comment 转办原因
     */
    void transferTask(String taskId, String assignee, String comment);

    /**
     * 委派任务
     * 
     * @param taskId 任务ID
     * @param assignee 委派人
     * @param comment 委派原因
     */
    void delegateTask(String taskId, String assignee, String comment);

    /**
     * 退回任务
     * 
     * @param taskId 任务ID
     * @param targetActivityId 目标节点ID
     * @param comment 退回原因
     */
    void rejectTask(String taskId, String targetActivityId, String comment);

    /**
     * 获取任务统计信息
     * 
     * @param assignee 处理人
     * @return 统计信息
     */
    List<TaskStatisticsDTO> getTaskStatistics(String assignee);

    /**
     * 批量处理任务
     * 
     * @param taskIds 任务ID列表
     * @param action 操作类型
     * @param params 操作参数
     * @return 处理结果
     */
    java.util.Map<String, Object> batchProcessTasks(java.util.List<String> taskIds, String action, java.util.Map<String, Object> params);
}