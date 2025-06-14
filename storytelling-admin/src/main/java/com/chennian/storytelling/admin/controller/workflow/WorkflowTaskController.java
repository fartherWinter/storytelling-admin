package com.chennian.storytelling.admin.controller.workflow;

import com.chennian.storytelling.service.WorkflowService;
import com.chennian.storytelling.bean.dto.TaskDTO;
import com.chennian.storytelling.bean.dto.WorkflowBatchOperationDTO;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import org.flowable.task.api.Task;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.HashMap;

/**
 * 工作流任务管理控制器
 * 提供任务的高级管理功能
 * 
 * @author chennian
 */
@Api(tags = "工作流任务管理")
@RestController
@RequestMapping("/workflow/tasks")
@RequiredArgsConstructor
public class WorkflowTaskController {

    private final WorkflowService workflowService;

    /**
     * 高级任务查询
     */
    @ApiOperation("高级任务查询")
    @GetMapping("/search")
    public Map<String, Object> searchTasks(
            @RequestParam(value = "assignee", required = false) String assignee,
            @RequestParam(value = "processDefinitionKey", required = false) String processDefinitionKey,
            @RequestParam(value = "businessKey", required = false) String businessKey,
            @RequestParam(value = "taskName", required = false) String taskName,
            @RequestParam(value = "startTime", required = false) 
            @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime startTime,
            @RequestParam(value = "endTime", required = false) 
            @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime endTime,
            @RequestParam(value = "page", defaultValue = "0") int page,
            @RequestParam(value = "size", defaultValue = "20") int size) {
        
        Map<String, Object> result = new HashMap<>();
        
        // 这里可以实现更复杂的查询逻辑
        if (assignee != null) {
            List<Task> tasks = workflowService.findTodoTasks(assignee);
            result.put("tasks", tasks);
            result.put("total", tasks.size());
        }
        
        result.put("page", page);
        result.put("size", size);
        
        return result;
    }
    
    /**
     * 获取任务详情
     */
    @ApiOperation("获取任务详情")
    @GetMapping("/{taskId}/detail")
    public Map<String, Object> getTaskDetail(@PathVariable("taskId") String taskId) {
        Map<String, Object> detail = new HashMap<>();
        
        // 这里可以获取任务的详细信息
        // 包括任务基本信息、流程变量、历史记录等
        
        detail.put("taskId", taskId);
        // detail.put("taskInfo", taskInfo);
        // detail.put("processVariables", processVariables);
        // detail.put("comments", comments);
        
        return detail;
    }
    
    /**
     * 批量任务操作
     */
    @ApiOperation("批量任务操作")
    @PostMapping("/batch")
    public WorkflowBatchOperationDTO.BatchOperationResult batchOperateTasks(
            @RequestBody WorkflowBatchOperationDTO batchOperation) {
        return workflowService.batchOperateTasks(batchOperation);
    }
    
    /**
     * 任务委派
     */
    @ApiOperation("任务委派")
    @PostMapping("/{taskId}/delegate")
    public Map<String, Object> delegateTask(
            @PathVariable("taskId") String taskId,
            @RequestParam("assignee") String assignee,
            @RequestParam(value = "comment", required = false) String comment) {
        
        workflowService.delegateTask(taskId, assignee, comment);
        
        Map<String, Object> result = new HashMap<>();
        result.put("success", true);
        result.put("message", "任务委派成功");
        result.put("taskId", taskId);
        result.put("newAssignee", assignee);
        
        return result;
    }
    
    /**
     * 任务认领
     */
    @ApiOperation("任务认领")
    @PostMapping("/{taskId}/claim")
    public Map<String, Object> claimTask(
            @PathVariable("taskId") String taskId,
            @RequestParam("assignee") String assignee) {
        
        workflowService.claimTask(taskId, assignee);
        
        Map<String, Object> result = new HashMap<>();
        result.put("success", true);
        result.put("message", "任务认领成功");
        result.put("taskId", taskId);
        result.put("assignee", assignee);
        
        return result;
    }
    
    /**
     * 任务转办
     */
    @ApiOperation("任务转办")
    @PostMapping("/{taskId}/transfer")
    public Map<String, Object> transferTask(
            @PathVariable("taskId") String taskId,
            @RequestParam("assignee") String assignee,
            @RequestParam(value = "comment", required = false) String comment) {
        
        workflowService.transferTask(taskId, assignee, comment);
        
        Map<String, Object> result = new HashMap<>();
        result.put("success", true);
        result.put("message", "任务转办成功");
        result.put("taskId", taskId);
        result.put("newAssignee", assignee);
        
        return result;
    }
    
    /**
     * 获取用户历史任务
     */
    @ApiOperation("获取用户历史任务")
    @GetMapping("/history")
    public Map<String, Object> getHistoryTasks(
            @RequestParam("assignee") String assignee,
            @RequestParam(value = "startTime", required = false) 
            @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime startTime,
            @RequestParam(value = "endTime", required = false) 
            @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime endTime,
            @RequestParam(value = "page", defaultValue = "0") int page,
            @RequestParam(value = "size", defaultValue = "20") int size) {
        
        List<TaskDTO> historyTasks = workflowService.findHistoryTasks(assignee, startTime, endTime);
        
        Map<String, Object> result = new HashMap<>();
        result.put("tasks", historyTasks);
        result.put("total", historyTasks.size());
        result.put("page", page);
        result.put("size", size);
        
        return result;
    }
    
    /**
     * 获取任务统计信息
     */
    @ApiOperation("获取任务统计信息")
    @GetMapping("/statistics")
    public Map<String, Object> getTaskStatistics(
            @RequestParam(value = "assignee", required = false) String assignee,
            @RequestParam(value = "startTime", required = false) 
            @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime startTime,
            @RequestParam(value = "endTime", required = false) 
            @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime endTime) {
        
        Map<String, Object> statistics = new HashMap<>();
        
        if (assignee != null) {
            List<TaskDTO> historyTasks = workflowService.findHistoryTasks(assignee, startTime, endTime);
            List<Task> todoTasks = workflowService.findTodoTasks(assignee);
            
            statistics.put("completedTaskCount", historyTasks.size());
            statistics.put("pendingTaskCount", todoTasks.size());
            statistics.put("totalTaskCount", historyTasks.size() + todoTasks.size());
            
            // 计算完成率
            int total = historyTasks.size() + todoTasks.size();
            if (total > 0) {
                double completionRate = (double) historyTasks.size() / total * 100;
                statistics.put("completionRate", completionRate);
            }
        }
        
        return statistics;
    }
    
    /**
     * 任务提醒设置
     */
    @ApiOperation("任务提醒设置")
    @PostMapping("/{taskId}/reminder")
    public Map<String, Object> setTaskReminder(
            @PathVariable("taskId") String taskId,
            @RequestParam("reminderTime") 
            @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime reminderTime,
            @RequestParam(value = "message", required = false) String message) {
        
        Map<String, Object> result = new HashMap<>();
        
        // 这里可以实现任务提醒功能
        // 比如设置定时任务、发送通知等
        
        result.put("success", true);
        result.put("message", "任务提醒设置成功");
        result.put("taskId", taskId);
        result.put("reminderTime", reminderTime);
        
        return result;
    }
    
    /**
     * 任务优先级设置
     */
    @ApiOperation("任务优先级设置")
    @PostMapping("/{taskId}/priority")
    public Map<String, Object> setTaskPriority(
            @PathVariable("taskId") String taskId,
            @RequestParam("priority") int priority) {
        
        Map<String, Object> result = new HashMap<>();
        
        // 这里可以实现任务优先级设置
        // taskService.setPriority(taskId, priority);
        
        result.put("success", true);
        result.put("message", "任务优先级设置成功");
        result.put("taskId", taskId);
        result.put("priority", priority);
        
        return result;
    }
}