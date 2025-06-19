package com.chennian.storytelling.admin.controller.workflow;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.chennian.storytelling.bean.dto.workflow.WorkflowTaskDTO;
import com.chennian.storytelling.common.response.ServerResponseEntity;
import com.chennian.storytelling.service.WorkflowService;
import com.chennian.storytelling.service.WorkflowTaskService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
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
@Slf4j
@RequestMapping("/sys/workflow/tasks")
@RequiredArgsConstructor
public class WorkflowTaskController {

    private final WorkflowService workflowService;

    private final WorkflowTaskService workflowTaskService;

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
     * 获取任务列表
     */
    @ApiOperation(value = "获取任务列表", notes = "分页查询任务列表")
    @GetMapping("/list")
    public ServerResponseEntity<Map<String, Object>> getTaskList(
            @ApiParam("页码") @RequestParam(value = "page", defaultValue = "1") Integer page,
            @ApiParam("每页大小") @RequestParam(value = "size", defaultValue = "20") Integer size,
            @ApiParam("任务状态") @RequestParam(value = "status", required = false) String status,
            @ApiParam("处理人") @RequestParam(value = "assignee", required = false) String assignee) {
        
        try {
            // 构建查询条件
            WorkflowTaskDTO.TaskQueryDTO queryDTO = new WorkflowTaskDTO.TaskQueryDTO();
            queryDTO.setPage(page);
            queryDTO.setSize(size);
            queryDTO.setStatus(status);
            queryDTO.setAssignee(assignee);
            
            // 查询任务列表
            IPage<WorkflowTaskDTO.TaskInfoDTO> result = workflowTaskService.getTaskList(queryDTO);
            
            Map<String, Object> response = new HashMap<>();
            response.put("data", result.getRecords());
            response.put("total", result.getTotal());
            response.put("page", page);
            response.put("size", size);
            
            return ServerResponseEntity.success(response);
            
        } catch (Exception e) {
            log.error("获取任务列表失败", e);
            return ServerResponseEntity.showFailMsg("获取任务列表失败: " + e.getMessage());
        }
    }
    
    /**
     * 获取任务详情
     */
    @ApiOperation(value = "获取任务详情", notes = "根据任务ID获取任务详细信息")
    @GetMapping("/{taskId}")
    public ServerResponseEntity<WorkflowTaskDTO.TaskDetailDTO> getTaskDetail(
            @ApiParam("任务ID") @PathVariable String taskId) {
        
        try {
            // 获取任务详情
            WorkflowTaskDTO.TaskDetailDTO taskDetail = workflowTaskService.getTaskDetail(taskId);
            
            if (taskDetail == null) {
                return ServerResponseEntity.showFailMsg("任务不存在");
            }
            
            return ServerResponseEntity.success(taskDetail);
            
        } catch (Exception e) {
            log.error("获取任务详情失败: taskId={}", taskId, e);
            return ServerResponseEntity.showFailMsg("获取任务详情失败: " + e.getMessage());
        }
    }

    
    /**
     * 任务提醒设置
     */
    @ApiOperation("任务提醒设置")
    @PostMapping("/{taskId}/reminder")
    public ServerResponseEntity<Map<String, Object>> setTaskReminder(
            @PathVariable("taskId") String taskId,
            @RequestParam("reminderTime") 
            @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime reminderTime,
            @RequestParam(value = "message", required = false) String message) {
        
        // 这里可以实现任务提醒功能
        // 比如设置定时任务、发送通知等
        
        Map<String, Object> result = new HashMap<>();
        result.put("taskId", taskId);
        result.put("reminderTime", reminderTime);
        result.put("message", message);
        
        return ServerResponseEntity.success(result);
    }
    
    /**
     * 任务优先级设置
     */
    @ApiOperation("任务优先级设置")
    @PostMapping("/{taskId}/priority")
    public ServerResponseEntity<Map<String, Object>> setTaskPriority(
            @PathVariable("taskId") String taskId,
            @RequestParam("priority") int priority) {
        
        // 这里可以实现任务优先级设置
        workflowTaskService.setPriority(taskId, priority);
        
        Map<String, Object> result = new HashMap<>();
        result.put("taskId", taskId);
        result.put("priority", priority);
        
        return ServerResponseEntity.success(result);
    }
}