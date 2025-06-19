package com.chennian.storytelling.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.chennian.security.admin.util.SecurityUtils;
import com.chennian.storytelling.bean.dto.TaskStatisticsDTO;
import com.chennian.storytelling.bean.dto.workflow.WorkflowTaskDTO;
import com.chennian.storytelling.bean.model.workflow.WfTask;
import com.chennian.storytelling.common.utils.PageParam;
import com.chennian.storytelling.common.utils.ServletUtils;
import com.chennian.storytelling.common.utils.StringUtils;
import com.chennian.storytelling.dao.workflow.WfTaskMapper;
import com.chennian.storytelling.service.WorkflowTaskService;
import lombok.extern.slf4j.Slf4j;
import org.flowable.engine.TaskService;
import org.flowable.task.api.Task;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.*;
import java.util.stream.Collectors;

/**
 * 工作流任务服务实现类
 * 
 * @author chennian
 */
@Slf4j
@Service
public class WorkflowTaskServiceImpl implements WorkflowTaskService {

    @Autowired
    private WfTaskMapper wfTaskMapper;

    @Autowired
    private TaskService taskService;

    @Override
    public IPage<WorkflowTaskDTO.TaskInfoDTO> getTaskList(WorkflowTaskDTO.TaskQueryDTO queryDTO) {
        log.info("获取任务列表，查询条件: {}", queryDTO);
        
        try {
            // 构建分页对象
            PageParam<WfTask> page = new PageParam<>();
            page.setCurrent(queryDTO.getPage());
            page.setSize(queryDTO.getSize());
            // 查询任务列表
            IPage<WfTask> taskPage = wfTaskMapper.selectPage(page, new LambdaQueryWrapper<WfTask>()
                    .eq(StringUtils.isNotEmpty(queryDTO.getTaskName()), WfTask::getTaskName, queryDTO.getTaskName())
                    .eq(StringUtils.isNotEmpty(queryDTO.getAssignee()), WfTask::getAssignee, queryDTO.getAssignee())
                    .ge(StringUtils.isNotNull(queryDTO.getStartTime()), WfTask::getCreateTime, queryDTO.getStartTime())
                    .le(StringUtils.isNotNull(queryDTO.getEndTime()), WfTask::getCreateTime, queryDTO.getEndTime()));
            
            // 转换为DTO
            List<WorkflowTaskDTO.TaskInfoDTO> taskInfoList = taskPage.getRecords().stream()
                    .map(this::convertToTaskInfoDTO)
                    .collect(Collectors.toList());
            
            // 构建返回结果
            Page<WorkflowTaskDTO.TaskInfoDTO> resultPage = new Page<>(queryDTO.getPage(), queryDTO.getSize());
            resultPage.setRecords(taskInfoList);
            resultPage.setTotal(taskPage.getTotal());
            
            return resultPage;
            
        } catch (Exception e) {
            log.error("获取任务列表失败", e);
            throw new RuntimeException("获取任务列表失败: " + e.getMessage());
        }
    }

    @Override
    public WorkflowTaskDTO.TaskDetailDTO getTaskDetail(String taskId) {
        log.info("获取任务详情，任务ID: {}", taskId);
        
        try {
            // 查询任务详情
            WfTask task = wfTaskMapper.selectTaskDetail(taskId);
            if (task == null) {
                return null;
            }
            
            // 转换为DTO
            return convertToTaskDetailDTO(task);
            
        } catch (Exception e) {
            log.error("获取任务详情失败，任务ID: {}", taskId, e);
            throw new RuntimeException("获取任务详情失败: " + e.getMessage());
        }
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void setPriority(String taskId, int priority) {
        log.info("设置任务优先级，任务ID: {}, 优先级: {}", taskId, priority);

        try {
            // 更新Flowable任务优先级
            taskService.setPriority(taskId, priority);
            
            // 更新数据库任务优先级
            wfTaskMapper.updateTaskPriority(taskId, priority, SecurityUtils.getUserName());
            
        } catch (Exception e) {
            log.error("设置任务优先级失败，任务ID: {}, 优先级: {}", taskId, priority, e);
            throw new RuntimeException("设置任务优先级失败: " + e.getMessage());
        }
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void claimTask(String taskId, String assignee) {
        log.info("认领任务，任务ID: {}, 处理人: {}", taskId, assignee);
        
        try {
            // 认领Flowable任务
            taskService.claim(taskId, assignee);
            
            // 更新数据库任务状态
            wfTaskMapper.update(new LambdaUpdateWrapper<WfTask>().eq(WfTask::getId,taskId).set(WfTask::getAssignee, assignee));
            
        } catch (Exception e) {
            log.error("认领任务失败，任务ID: {}, 处理人: {}", taskId, assignee, e);
            throw new RuntimeException("认领任务失败: " + e.getMessage());
        }
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void completeTask(String taskId, Map<String, Object> variables, String comment) {
        log.info("完成任务，任务ID: {}, 变量: {}, 备注: {}", taskId, variables, comment);
        
        try {
            // 添加备注到变量中
            if (comment != null && !comment.isEmpty()) {
                if (variables == null) {
                    variables = new HashMap<>();
                }
                variables.put("comment", comment);
            }
            
            // 完成Flowable任务
            taskService.complete(taskId, variables);
            
            // 更新数据库任务状态
            wfTaskMapper.update(new LambdaUpdateWrapper<WfTask>().eq(WfTask::getId,taskId).set(WfTask::getTaskStatus, "COMPLETED"));
            
        } catch (Exception e) {
            log.error("完成任务失败，任务ID: {}", taskId, e);
            throw new RuntimeException("完成任务失败: " + e.getMessage());
        }
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void transferTask(String taskId, String assignee, String comment) {
        log.info("转办任务，任务ID: {}, 新处理人: {}, 原因: {}", taskId, assignee, comment);
        
        try {
            // 添加备注
            if (comment != null && !comment.isEmpty()) {
                taskService.addComment(taskId, null, comment);
            }
            
            // 转办任务
            taskService.setAssignee(taskId, assignee);
            
            // 更新数据库任务信息
            wfTaskMapper.update(new LambdaUpdateWrapper<WfTask>().eq(WfTask::getId,taskId).set(WfTask::getAssignee, assignee));
            
        } catch (Exception e) {
            log.error("转办任务失败，任务ID: {}, 新处理人: {}", taskId, assignee, e);
            throw new RuntimeException("转办任务失败: " + e.getMessage());
        }
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void delegateTask(String taskId, String assignee, String comment) {
        log.info("委派任务，任务ID: {}, 委派人: {}, 原因: {}", taskId, assignee, comment);
        
        try {
            // 添加备注
            if (comment != null && !comment.isEmpty()) {
                taskService.addComment(taskId, null, comment);
            }
            
            // 委派任务
            taskService.delegateTask(taskId, assignee);
            
            // 更新数据库任务状态
            wfTaskMapper.update(new LambdaUpdateWrapper<WfTask>().eq(WfTask::getId,taskId).set(WfTask::getTaskStatus, "DELEGATED"));
            
        } catch (Exception e) {
            log.error("委派任务失败，任务ID: {}, 委派人: {}", taskId, assignee, e);
            throw new RuntimeException("委派任务失败: " + e.getMessage());
        }
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void rejectTask(String taskId, String targetActivityId, String comment) {
        log.info("退回任务，任务ID: {}, 目标节点: {}, 原因: {}", taskId, targetActivityId, comment);
        
        try {
            // 添加备注
            if (comment != null && !comment.isEmpty()) {
                taskService.addComment(taskId, null, comment);
            }
            
            // 获取任务信息
            Task task = taskService.createTaskQuery().taskId(taskId).singleResult();
            if (task == null) {
                throw new RuntimeException("任务不存在");
            }
            
            // 这里需要根据具体的流程引擎实现退回逻辑
            // Flowable的退回需要使用RuntimeService进行流程跳转
            Map<String, Object> variables = new HashMap<>();
            variables.put("reject", true);
            variables.put("rejectReason", comment);
            variables.put("targetActivityId", targetActivityId);
            
            taskService.complete(taskId, variables);
            
            // 更新数据库任务状态
            wfTaskMapper.update(new LambdaUpdateWrapper<WfTask>().eq(WfTask::getId,taskId).set(WfTask::getTaskStatus, "REJECTED"));
            
        } catch (Exception e) {
            log.error("退回任务失败，任务ID: {}, 目标节点: {}", taskId, targetActivityId, e);
            throw new RuntimeException("退回任务失败: " + e.getMessage());
        }
    }

    @Override
    public List<TaskStatisticsDTO> getTaskStatistics(String assignee) {
        log.info("获取任务统计信息，处理人: {}", assignee);
        
        try {
            List<TaskStatisticsDTO> taskStatisticsDTOS = wfTaskMapper.selectTaskStatistics();
            return taskStatisticsDTOS != null ? taskStatisticsDTOS : new ArrayList<>();
            
        } catch (Exception e) {
            log.error("获取任务统计信息失败，处理人: {}", assignee, e);
            throw new RuntimeException("获取任务统计信息失败: " + e.getMessage());
        }
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Map<String, Object> batchProcessTasks(List<String> taskIds, String action, Map<String, Object> params) {
        log.info("批量处理任务，任务ID列表: {}, 操作: {}, 参数: {}", taskIds, action, params);
        
        Map<String, Object> result = new HashMap<>();
        int successCount = 0;
        int failureCount = 0;
        List<String> failureTaskIds = new ArrayList<>();
        
        try {
            for (String taskId : taskIds) {
                try {
                    switch (action.toLowerCase()) {
                        case "complete":
                            completeTask(taskId, params, (String) params.get("comment"));
                            break;
                        case "claim":
                            claimTask(taskId, (String) params.get("assignee"));
                            break;
                        case "transfer":
                            transferTask(taskId, (String) params.get("assignee"), (String) params.get("comment"));
                            break;
                        case "delegate":
                            delegateTask(taskId, (String) params.get("assignee"), (String) params.get("comment"));
                            break;
                        default:
                            throw new RuntimeException("不支持的操作类型: " + action);
                    }
                    successCount++;
                } catch (Exception e) {
                    log.error("批量处理任务失败，任务ID: {}, 操作: {}", taskId, action, e);
                    failureCount++;
                    failureTaskIds.add(taskId);
                }
            }
            
            result.put("totalCount", taskIds.size());
            result.put("successCount", successCount);
            result.put("failureCount", failureCount);
            result.put("failureTaskIds", failureTaskIds);
            result.put("success", failureCount == 0);
            
            return result;
            
        } catch (Exception e) {
            log.error("批量处理任务失败", e);
            throw new RuntimeException("批量处理任务失败: " + e.getMessage());
        }
    }

    /**
     * 转换为任务信息DTO
     */
    private WorkflowTaskDTO.TaskInfoDTO convertToTaskInfoDTO(WfTask task) {
        WorkflowTaskDTO.TaskInfoDTO dto = new WorkflowTaskDTO.TaskInfoDTO();
        dto.setTaskId(task.getId());
        dto.setTaskName(task.getTaskName());
        dto.setProcessInstanceId(task.getProcessInstanceId());
        dto.setProcessDefinitionId(task.getProcessDefinitionId());
        dto.setProcessDefinitionKey(task.getProcessDefinitionKey());
        dto.setProcessDefinitionName(task.getProcessDefinitionName());
        dto.setAssignee(task.getAssignee());
        dto.setAssigneeName(task.getAssigneeName());
        dto.setCreateTime(convertToLocalDateTime(task.getCreateTime()));
        dto.setDueDate(convertToLocalDateTime(task.getDueDate()));
        dto.setPriority(task.getPriority());
        dto.setStatus(task.getTaskStatus());
        dto.setBusinessKey(task.getBusinessKey());
        dto.setFormKey(task.getFormKey());
        dto.setCandidateUsers(task.getCandidateUsers());
        dto.setCandidateGroups(task.getCandidateGroups());
        dto.setTenantId(task.getTenantId());
        return dto;
    }

    /**
     * 转换为任务详情DTO
     */
    private WorkflowTaskDTO.TaskDetailDTO convertToTaskDetailDTO(WfTask task) {
        WorkflowTaskDTO.TaskDetailDTO dto = new WorkflowTaskDTO.TaskDetailDTO();
        dto.setTaskId(task.getId());
        dto.setTaskKey(task.getTaskDefinitionKey());
        dto.setTaskName(task.getTaskName());
        dto.setProcessInstanceId(task.getProcessInstanceId());
        dto.setProcessDefinitionId(task.getProcessDefinitionId());
        dto.setProcessDefinitionKey(task.getProcessDefinitionKey());
        dto.setProcessDefinitionName(task.getProcessDefinitionName());
        dto.setAssignee(task.getAssignee());
        dto.setAssigneeName(task.getAssigneeName());
        dto.setCreateTime(convertToLocalDateTime(task.getCreateTime()));
        dto.setDueDate(convertToLocalDateTime(task.getDueDate()));
        dto.setPriority(task.getPriority());
        dto.setDescription(task.getTaskDescription());
        dto.setOwner(task.getOwner());
        dto.setParentTaskId(task.getParentTaskId());
        dto.setFormKey(task.getFormKey());
        dto.setStatus(task.getTaskStatus());
        dto.setBusinessKey(task.getBusinessKey());
        dto.setCandidateUsers(task.getCandidateUsers());
        dto.setCandidateGroups(task.getCandidateGroups());
        dto.setTaskType(task.getTaskType());
        dto.setCompletedTime(convertToLocalDateTime(task.getEndTime()));
        dto.setProcessingTime(task.getDuration());
        dto.setTenantId(task.getTenantId());
        dto.setRemark(task.getRemark());
        
        // 设置流程变量和任务变量（这里可以根据需要从其他地方获取）
        dto.setProcessVariables(new HashMap<>());
        dto.setTaskLocalVariables(new HashMap<>());
        dto.setFormData(new HashMap<>());
        dto.setProcessHistory(new ArrayList<>());
        
        return dto;
    }

    /**
     * Date转LocalDateTime
     */
    private LocalDateTime convertToLocalDateTime(Date date) {
        if (date == null) {
            return null;
        }
        return date.toInstant().atZone(ZoneId.systemDefault()).toLocalDateTime();
    }
}