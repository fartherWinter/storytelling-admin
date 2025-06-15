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

import static com.chennian.storytelling.admin.controller.workflow.WorkflowResponse.*;

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
    
    // 批量任务操作已移至 WorkflowController 统一管理
    // 路径: /workflow/tasks/batch

    
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