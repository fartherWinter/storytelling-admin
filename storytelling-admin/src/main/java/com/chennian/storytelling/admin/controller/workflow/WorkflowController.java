package com.chennian.storytelling.admin.controller.workflow;

import java.io.IOException;
import java.io.InputStream;
import java.util.List;
import java.util.Map;
import java.util.HashMap;

import com.chennian.storytelling.bean.dto.*;
import com.chennian.storytelling.common.enums.WorkflowResponseEnum;
import com.chennian.storytelling.common.response.ResponseEnum;
import com.chennian.storytelling.common.response.ServerResponseEntity;
import jakarta.validation.Valid;
import org.flowable.engine.RepositoryService;
import org.flowable.engine.RuntimeService;
import org.flowable.engine.repository.ProcessDefinition;
import org.flowable.engine.runtime.ProcessInstance;
import org.flowable.task.api.Task;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.chennian.storytelling.service.WorkflowService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import lombok.extern.slf4j.Slf4j;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.validation.annotation.Validated;
import jakarta.validation.constraints.NotBlank;
import java.time.LocalDateTime;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;

import static com.chennian.storytelling.admin.controller.workflow.WorkflowResponse.*;


/**
 * 工作流核心控制器
 * 提供任务处理和基础工作流操作接口
 * 注意：流程部署功能已迁移至 WorkflowDeploymentController
 * 注意：流程实例管理功能已迁移至 WorkflowInstanceController
 * 
 * @author chennian
 */
@Api(tags = "工作流核心管理")
@Slf4j
@Validated
@RestController
@RequestMapping(WorkflowApiPaths.BASE)
public class WorkflowController {

    private final WorkflowService workflowService;

    private final RepositoryService repositoryService;
    private final RuntimeService runtimeService;

    public WorkflowController(WorkflowService workflowService, RepositoryService repositoryService, RuntimeService runtimeService) {
        this.workflowService = workflowService;
        this.repositoryService = repositoryService;
        this.runtimeService = runtimeService;
    }
    
    // 流程部署功能已迁移至 WorkflowDeploymentController
    
    // 流程启动功能已迁移至 WorkflowInstanceController
    
    /**
     * 查询待办任务
     */
    @ApiOperation(value = "查询待办任务", notes = "获取当前用户或指定用户的待办任务列表")
    @GetMapping(WorkflowApiPaths.TaskPaths.TODO)
    @PreAuthorize("hasAuthority('workflow:task:read')")
    @Cacheable(value = "workflow:tasks", key = "'todo:' + (#assignee != null ? #assignee : authentication.name)", 
               condition = "#assignee == null or @workflowPermissionEvaluator.hasPermission(authentication, #assignee, 'USER', 'READ')")
    public ServerResponseEntity<Map<String, Object>> findTodoTasks(
            @ApiParam(value = "任务处理人", required = false) 
            @RequestParam(value = "assignee", required = false) String assignee,
            @ApiParam(value = "页码", defaultValue = "0") 
            @RequestParam(value = "page", defaultValue = "0") int page,
            @ApiParam(value = "每页大小", defaultValue = "20") 
            @RequestParam(value = "size", defaultValue = "20") int size) {
        try {
            Authentication auth = SecurityContextHolder.getContext().getAuthentication();
            String currentUser = auth.getName();
            
            // 如果没有指定assignee，使用当前用户
            String targetAssignee = assignee != null ? assignee : currentUser;
            
            // 权限检查：只能查询自己的任务，除非有管理权限
            if (!targetAssignee.equals(currentUser) && 
                !auth.getAuthorities().stream().anyMatch(a -> a.getAuthority().equals("workflow:task:manage"))) {
                log.warn("用户 {} 尝试查询其他用户 {} 的待办任务", currentUser, targetAssignee);
                return ServerResponseEntity.fail(WorkflowResponseEnum.PERMISSION_DENIED);
            }
            
            List<Task> tasks = workflowService.findTodoTasks(targetAssignee);
            
            Map<String, Object> result = new HashMap<>();
            result.put("tasks", tasks);
            result.put("total", tasks.size());
            result.put("assignee", targetAssignee);
            result.put("page", page);
            result.put("size", size);
            
            log.debug("查询待办任务成功: assignee={}, count={}", targetAssignee, tasks.size());
            return ServerResponseEntity.success(result);
        } catch (Exception e) {
            log.error("查询待办任务失败: assignee={}", assignee, e);
            return ServerResponseEntity.fail(WorkflowResponseEnum.TASK_NOT_FOUND);
        }
    }
    
    /**
     * 查询流程任务
     */
    @ApiOperation(value = "查询流程任务", notes = "查询指定流程实例的所有任务")
    @GetMapping(WorkflowApiPaths.TaskPaths.BY_PROCESS_INSTANCE)
    public ServerResponseEntity<Map<String, Object>> findTasksByProcessInstanceId(
            @ApiParam(value = "流程实例ID", required = true) 
            @NotBlank @PathVariable("processInstanceId") String processInstanceId) {
        try {
            List<Task> tasks = workflowService.findTasksByProcessInstanceId(processInstanceId);
            
            Map<String, Object> result = new HashMap<>();
            result.put("tasks", tasks);
            result.put("total", tasks.size());
            result.put("processInstanceId", processInstanceId);
            
            log.debug("查询流程任务成功: processInstanceId={}, count={}", processInstanceId, tasks.size());
            return ServerResponseEntity.success(result);
        } catch (Exception e) {
            log.error("查询流程任务失败: processInstanceId={}", processInstanceId, e);
            return ServerResponseEntity.fail(WorkflowResponseEnum.TASK_NOT_FOUND);
        }
    }
    
    /**
     * 完成任务
     */
    @ApiOperation(value = "完成任务", notes = "完成指定的工作流任务")
    @PostMapping(WorkflowApiPaths.TaskPaths.COMPLETE)
    @PreAuthorize("hasAuthority('workflow:task:write') and @workflowPermissionEvaluator.hasPermission(authentication, #taskId, 'TASK', 'WRITE')")
    @CacheEvict(value = {"workflow:tasks", "workflow:instances"}, allEntries = true)
    public ServerResponseEntity<Map<String, Object>> completeTask(
            @ApiParam(value = "任务ID", required = true) 
            @NotBlank @PathVariable("taskId") String taskId,
            @ApiParam("审批意见") @RequestParam(value = "comment", required = false) String comment,
            @ApiParam("流程变量") @RequestBody(required = false) Map<String, Object> variables) {
        
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        String currentUser = auth.getName();
        
        try {
            // 验证任务ID
            if (!WorkflowApiPaths.Validator.isValidTaskId(taskId)) {
                return ServerResponseEntity.showFailMsg("无效的任务ID格式");
            }
            
            // 验证任务是否属于当前用户
            if (!workflowService.isTaskAssignedToUser(taskId, currentUser)) {
                log.warn("用户 {} 尝试完成不属于自己的任务: {}", currentUser, taskId);
                return ServerResponseEntity.fail(WorkflowResponseEnum.PERMISSION_DENIED);
            }
            
            workflowService.completeTask(taskId, variables, comment);
            
            Map<String, Object> result = new HashMap<>();
            result.put("taskId", taskId);
            result.put("comment", comment);
            result.put("completedBy", currentUser);
            
            log.info("任务完成成功: taskId={}, comment={}, user={}", taskId, comment, currentUser);
            return ServerResponseEntity.success(result);
        } catch (Exception e) {
            log.error("任务完成失败: taskId={}, user={}", taskId, currentUser, e);
            return ServerResponseEntity.fail(WorkflowResponseEnum.TASK_COMPLETE_FAILED);
        }
    }
    
    /**
     * 审批通过任务
     */
    @ApiOperation(value = "审批通过任务", notes = "审批通过指定的工作流任务")
    @PostMapping(WorkflowApiPaths.TaskPaths.APPROVE)
    @PreAuthorize("hasAuthority('workflow:task:approve') and @workflowPermissionEvaluator.hasPermission(authentication, #taskId, 'TASK', 'WRITE')")
    @CacheEvict(value = {"workflow:tasks", "workflow:instances"}, allEntries = true)
    public ServerResponseEntity<Map<String, Object>> approveTask(
            @ApiParam(value = "任务ID", required = true) 
            @NotBlank @PathVariable("taskId") String taskId,
            @ApiParam("审批意见") @RequestParam(value = "comment", required = false) String comment,
            @ApiParam("流程变量") @RequestBody(required = false) Map<String, Object> variables) {
        
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        String currentUser = auth.getName();
        
        try {
            // 验证任务是否属于当前用户
            if (!workflowService.isTaskAssignedToUser(taskId, currentUser)) {
                log.warn("用户 {} 尝试审批不属于自己的任务: {}", currentUser, taskId);
                return ServerResponseEntity.fail(WorkflowResponseEnum.PERMISSION_DENIED);
            }
            
            workflowService.approveTask(taskId, comment, variables);
            
            Map<String, Object> result = new HashMap<>();
            result.put("taskId", taskId);
            result.put("comment", comment);
            result.put("approvedBy", currentUser);
            
            log.info("任务审批通过: taskId={}, comment={}, user={}", taskId, comment, currentUser);
            return ServerResponseEntity.success(result);
        } catch (Exception e) {
            log.error("任务审批失败: taskId={}, user={}", taskId, currentUser, e);
            return ServerResponseEntity.fail(WorkflowResponseEnum.TASK_COMPLETE_FAILED);
        }
    }
    
    /**
     * 拒绝任务
     */
    @ApiOperation(value = "拒绝任务", notes = "拒绝指定的工作流任务")
    @PostMapping(WorkflowApiPaths.TaskPaths.REJECT)
    @PreAuthorize("hasAuthority('workflow:task:reject') and @workflowPermissionEvaluator.hasPermission(authentication, #taskId, 'TASK', 'WRITE')")
    @CacheEvict(value = {"workflow:tasks", "workflow:instances"}, allEntries = true)
    public ServerResponseEntity<Map<String, Object>> rejectTask(
            @ApiParam(value = "任务ID", required = true) 
            @NotBlank @PathVariable("taskId") String taskId,
            @ApiParam("拒绝原因") @RequestParam(value = "comment", required = false) String comment,
            @ApiParam("流程变量") @RequestBody(required = false) Map<String, Object> variables) {
        
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        String currentUser = auth.getName();
        
        try {
            // 验证任务是否属于当前用户
            if (!workflowService.isTaskAssignedToUser(taskId, currentUser)) {
                log.warn("用户 {} 尝试拒绝不属于自己的任务: {}", currentUser, taskId);
                return ServerResponseEntity.fail(WorkflowResponseEnum.PERMISSION_DENIED);
            }
            
            workflowService.rejectTask(taskId, comment, variables);
            
            Map<String, Object> result = new HashMap<>();
            result.put("taskId", taskId);
            result.put("comment", comment);
            result.put("rejectedBy", currentUser);
            
            log.info("任务已拒绝: taskId={}, comment={}, user={}", taskId, comment, currentUser);
            return ServerResponseEntity.success(result);
        } catch (Exception e) {
            log.error("任务拒绝失败: taskId={}, user={}", taskId, currentUser, e);
            return ServerResponseEntity.fail(WorkflowResponseEnum.TASK_COMPLETE_FAILED);
        }
    }
    
    // 流程图获取功能已迁移至 WorkflowInstanceController
    
    // 流程实例终止功能已迁移至 WorkflowInstanceController
    
    // 流程变量相关功能已迁移至 WorkflowInstanceController
    
    // 流程定义相关功能已迁移至 WorkflowDeploymentController
    
    /**
     * 获取流程定义详情
     */
    @ApiOperation(value = "获取流程定义详情", notes = "获取指定流程定义的详细信息")
    @GetMapping(WorkflowApiPaths.DefinitionPaths.DETAIL)
    public ServerResponseEntity<Map<String, Object>> getProcessDefinition(
            @ApiParam(value = "流程定义ID", required = true) 
            @NotBlank @PathVariable("processDefinitionId") String processDefinitionId) {
        
        try {
            ProcessDefinition definition = repositoryService.createProcessDefinitionQuery()
                    .processDefinitionId(processDefinitionId)
                    .singleResult();
            
            if (definition == null) {
                return ServerResponseEntity.fail(WorkflowResponseEnum.PROCESS_DEFINITION_NOT_FOUND);
            }
            
            Map<String, Object> result = new HashMap<>();
            result.put("definition", definition);
            
            // 获取额外信息
            long instanceCount = runtimeService.createProcessInstanceQuery()
                    .processDefinitionId(processDefinitionId)
                    .count();
            result.put("instanceCount", instanceCount);
            
            log.debug("获取流程定义详情成功: processDefinitionId={}", processDefinitionId);
            return ServerResponseEntity.success(result);
        } catch (Exception e) {
            log.error("获取流程定义详情失败: processDefinitionId={}", processDefinitionId, e);
            return ServerResponseEntity.fail(WorkflowResponseEnum.PROCESS_DEFINITION_NOT_FOUND);
        }
    }
    
    /**
     * 获取流程定义资源
     */
    @ApiOperation(value = "获取流程定义资源", notes = "获取流程定义的XML或流程图资源")
    @GetMapping(WorkflowApiPaths.DefinitionPaths.RESOURCE)
    public ResponseEntity<byte[]> getProcessDefinitionResource(
            @ApiParam(value = "流程定义ID", required = true) 
            @NotBlank @PathVariable("processDefinitionId") String processDefinitionId,
            @ApiParam(value = "资源类型", allowableValues = "xml,diagram") 
            @RequestParam(value = "resourceType", defaultValue = "xml") String resourceType) {
        
        try {
            // 获取流程定义
            ProcessDefinition processDefinition = repositoryService.createProcessDefinitionQuery()
                    .processDefinitionId(processDefinitionId)
                    .singleResult();
            
            if (processDefinition == null) {
                log.warn("流程定义不存在: processDefinitionId={}", processDefinitionId);
                return new ResponseEntity<>(HttpStatus.NOT_FOUND);
            }
            
            // 根据资源类型获取不同的资源
            byte[] resourceBytes = null;
            String resourceName;
            MediaType mediaType;
            
            if ("diagram".equals(resourceType)) {
                // 获取流程图
                if (processDefinition.getDiagramResourceName() == null) {
                    log.warn("流程图资源不存在: processDefinitionId={}", processDefinitionId);
                    return new ResponseEntity<>(HttpStatus.NOT_FOUND);
                }
                
                try (InputStream resourceStream = repositoryService.getResourceAsStream(
                        processDefinition.getDeploymentId(), 
                        processDefinition.getDiagramResourceName())) {
                    
                    if (resourceStream == null) {
                        return new ResponseEntity<>(HttpStatus.NOT_FOUND);
                    }
                    resourceBytes = resourceStream.readAllBytes();
                }
                        
                resourceName = processDefinition.getDiagramResourceName();
                mediaType = MediaType.IMAGE_PNG;
            } else {
                // 获取XML资源
                if (processDefinition.getResourceName() == null) {
                    log.warn("XML资源不存在: processDefinitionId={}", processDefinitionId);
                    return new ResponseEntity<>(HttpStatus.NOT_FOUND);
                }
                
                try (InputStream resourceStream = repositoryService.getResourceAsStream(
                        processDefinition.getDeploymentId(), 
                        processDefinition.getResourceName())) {
                    
                    if (resourceStream == null) {
                        return new ResponseEntity<>(HttpStatus.NOT_FOUND);
                    }
                    
                    resourceBytes = resourceStream.readAllBytes();
                }
                
                resourceName = processDefinition.getResourceName();
                mediaType = MediaType.APPLICATION_XML;
            }
            
            if (resourceBytes == null || resourceBytes.length == 0) {
                log.warn("资源内容为空: processDefinitionId={}, resourceType={}", processDefinitionId, resourceType);
                return new ResponseEntity<>(HttpStatus.NOT_FOUND);
            }
            
            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(mediaType);
            headers.setContentDispositionFormData("attachment", resourceName);
            
            log.debug("获取流程定义资源成功: processDefinitionId={}, resourceType={}, size={}", 
                    processDefinitionId, resourceType, resourceBytes.length);
            
            return new ResponseEntity<>(resourceBytes, headers, HttpStatus.OK);
            
        } catch (IOException e) {
            log.error("获取流程定义资源失败: processDefinitionId={}, resourceType={}", processDefinitionId, resourceType, e);
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        } catch (Exception e) {
            log.error("获取流程定义资源异常: processDefinitionId={}, resourceType={}", processDefinitionId, resourceType, e);
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
    
    /**
     * 获取工作流统计信息
     */
    @ApiOperation(value = "获取工作流统计信息", notes = "获取指定时间范围内的工作流统计数据")
    @GetMapping(WorkflowApiPaths.MonitorPaths.STATISTICS)
    @PreAuthorize("hasAuthority('workflow:statistics:read')")
    @Cacheable(value = "workflow:statistics", key = "'global:' + (#startTime != null ? #startTime.toString() : 'null') + ':' + (#endTime != null ? #endTime.toString() : 'null')", unless = "#result == null")
    public ServerResponseEntity<Map<String, Object>> getWorkflowStatistics(
            @ApiParam("开始时间") @RequestParam(value = "startTime", required = false) 
            @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime startTime,
            @ApiParam("结束时间") @RequestParam(value = "endTime", required = false) 
            @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime endTime) {
        
        try {
            WorkflowStatisticsDTO statistics = workflowService.getWorkflowStatistics(startTime, endTime);
            
            Map<String, Object> result = new HashMap<>();
            result.put("statistics", statistics);
            result.put("startTime", startTime);
            result.put("endTime", endTime);
            
            log.debug("获取工作流统计信息成功: startTime={}, endTime={}", startTime, endTime);
            return ServerResponseEntity.success(result);
        } catch (Exception e) {
            log.error("获取工作流统计信息失败: startTime={}, endTime={}", startTime, endTime, e);
            return ServerResponseEntity.fail(WorkflowResponseEnum.WORKFLOW_ERROR);
        }
    }
    
    /**
     * 批量操作任务
     */
    @ApiOperation(value = "批量操作任务", notes = "批量完成、审批或拒绝任务")
    @PostMapping(WorkflowApiPaths.TaskPaths.BATCH)
    @PreAuthorize("hasAuthority('workflow:task:batch')")
    @CacheEvict(value = {"workflow:tasks", "workflow:instances"}, allEntries = true)
    public ServerResponseEntity<Map<String, Object>> batchOperateTasks(
            @ApiParam(value = "批量操作参数", required = true) 
            @Valid @RequestBody WorkflowBatchOperationDTO batchOperation) {
        
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        String currentUser = auth.getName();
        
        try {
            // 验证批量操作的权限
            for (String taskId : batchOperation.getTaskIds()) {
                if (!workflowService.isTaskAssignedToUser(taskId, currentUser)) {
                    log.warn("用户 {} 尝试批量操作不属于自己的任务: {}", currentUser, taskId);
                    return ServerResponseEntity.fail(WorkflowResponseEnum.PERMISSION_DENIED);
                }
            }
            
            WorkflowBatchOperationDTO.BatchOperationResult operationResult =
                    workflowService.batchOperateTasks(batchOperation);
            
            int totalCount = batchOperation.getTaskIds().size();
            int successCount = operationResult.getSuccessCount();
            int failureCount = operationResult.getFailureCount();
            
            Map<String, Object> result = new HashMap<>();
            result.put("operation", batchOperation.getOperationType().name());
            result.put("totalCount", totalCount);
            result.put("successCount", successCount);
            result.put("failureCount", failureCount);
            result.put("result", operationResult);
            result.put("operatedBy", currentUser);
            
            log.info("批量操作任务完成: operation={}, total={}, success={}, failure={}, user={}", 
                    batchOperation.getOperationType().name(), totalCount, successCount, failureCount, currentUser);
            
            return ServerResponseEntity.success(result);
        } catch (Exception e) {
            log.error("批量操作任务失败: operation={}, user={}", batchOperation.getOperationType().name(), currentUser, e);
            return ServerResponseEntity.fail(WorkflowResponseEnum.TASK_COMPLETE_FAILED);
        }
    }
    
    // 批量操作流程实例功能已迁移至 WorkflowInstanceController
    
    /**
     * 获取流程历史记录
     */
    @ApiOperation(value = "获取流程历史记录", notes = "获取指定流程实例的历史记录")
    @GetMapping(WorkflowApiPaths.ProcessPaths.HISTORY)
    @PreAuthorize("hasAuthority('workflow:history:read') and @workflowPermissionEvaluator.hasPermission(authentication, #processInstanceId, 'PROCESS_INSTANCE', 'READ')")
    @Cacheable(value = "workflow:history", key = "#processInstanceId", unless = "#result == null")
    public ServerResponseEntity<Map<String, Object>> getProcessHistory(
            @ApiParam(value = "流程实例ID", required = true) 
            @NotBlank @PathVariable("processInstanceId") String processInstanceId) {
        
        try {
            WorkflowHistoryDTO history = workflowService.getProcessHistory(processInstanceId);
            
            Map<String, Object> result = new HashMap<>();
            result.put("history", history);
            result.put("processInstanceId", processInstanceId);
            
            log.debug("获取流程历史记录成功: processInstanceId={}, count={}", processInstanceId, history != null ? 1 : 0);
            return ServerResponseEntity.success(result);
        } catch (Exception e) {
            log.error("获取流程历史记录失败: processInstanceId={}", processInstanceId, e);
            return ServerResponseEntity.fail(WorkflowResponseEnum.PROCESS_HISTORY_ERROR);
        }
    }
    
    /**
     * 查询用户历史任务
     */
    @ApiOperation(value = "查询用户历史任务", notes = "查询指定用户在指定时间范围内的历史任务")
    @GetMapping(WorkflowApiPaths.TaskPaths.HISTORY)
    @PreAuthorize("hasAuthority('workflow:task:read')")
    @Cacheable(value = "workflow:history:tasks", key = "'user:' + #assignee + ':' + (#startTime != null ? #startTime.toString() : 'null') + ':' + (#endTime != null ? #endTime.toString() : 'null')", unless = "#result == null")
    public ServerResponseEntity<Map<String, Object>> findHistoryTasks(
            @ApiParam(value = "任务处理人", required = true) 
            @NotBlank @RequestParam("assignee") String assignee,
            @ApiParam("开始时间") @RequestParam(value = "startTime", required = false) 
            @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime startTime,
            @ApiParam("结束时间") @RequestParam(value = "endTime", required = false) 
            @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime endTime,
            @ApiParam("页码") @RequestParam(value = "page", required = false, defaultValue = "1") int page,
            @ApiParam("每页大小") @RequestParam(value = "size", required = false, defaultValue = "20") int size) {
        
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        String currentUser = auth.getName();
        
        // 权限检查：只能查询自己的历史任务，除非有管理权限
        if (!assignee.equals(currentUser) && 
            !auth.getAuthorities().stream().anyMatch(a -> a.getAuthority().equals("workflow:task:manage"))) {
            log.warn("用户 {} 尝试查询其他用户 {} 的历史任务", currentUser, assignee);
            return ServerResponseEntity.fail(WorkflowResponseEnum.PERMISSION_DENIED);
        }
        
        try {
            List<TaskDTO> tasks = workflowService.findHistoryTasks(assignee, startTime, endTime);
            
            // 手动分页
            int total = tasks.size();
            int fromIndex = (page - 1) * size;
            int toIndex = Math.min(fromIndex + size, total);
            List<TaskDTO> pagedTasks = tasks.subList(fromIndex, toIndex);
            
            Map<String, Object> result = new HashMap<>();
            result.put("tasks", pagedTasks);
            result.put("total", total);
            result.put("page", page);
            result.put("size", size);
            result.put("totalPages", (total + size - 1) / size);
            result.put("assignee", assignee);
            
            log.debug("查询用户历史任务成功: assignee={}, count={}", assignee, tasks.size());
            return ServerResponseEntity.success(result);
        } catch (Exception e) {
            log.error("查询用户历史任务失败: assignee={}", assignee, e);
            return ServerResponseEntity.fail(WorkflowResponseEnum.TASK_NOT_FOUND);
        }
    }
    
    /**
     * 任务委派
     */
    @ApiOperation(value = "任务委派", notes = "将任务委派给其他用户")
    @PostMapping(WorkflowApiPaths.TaskPaths.DELEGATE)
    @PreAuthorize("hasAuthority('workflow:task:delegate') and @workflowPermissionEvaluator.hasPermission(authentication, #taskId, 'TASK', 'WRITE')")
    @CacheEvict(value = {"workflow:tasks"}, allEntries = true)
    public ServerResponseEntity<Map<String, Object>> delegateTask(
            @ApiParam(value = "任务ID", required = true) 
            @NotBlank @PathVariable("taskId") String taskId,
            @ApiParam(value = "被委派人", required = true) 
            @NotBlank @RequestParam("assignee") String assignee,
            @ApiParam("委派说明") @RequestParam(value = "comment", required = false) String comment) {
        
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        String currentUser = auth.getName();
        
        try {
            // 验证任务ID
            if (!WorkflowApiPaths.Validator.isValidTaskId(taskId)) {
                return ServerResponseEntity.fail(WorkflowResponseEnum.TASK_NOT_FOUND);
            }
            
            // 验证任务是否属于当前用户
            if (!workflowService.isTaskAssignedToUser(taskId, currentUser)) {
                log.warn("用户 {} 尝试委派不属于自己的任务: {}", currentUser, taskId);
                return ServerResponseEntity.fail(WorkflowResponseEnum.PERMISSION_DENIED);
            }
            
            workflowService.delegateTask(taskId, assignee, comment);
            
            Map<String, Object> result = new HashMap<>();
            result.put("message", "任务委派成功");
            result.put("taskId", taskId);
            result.put("assignee", assignee);
            result.put("comment", comment);
            result.put("delegatedBy", currentUser);
            
            log.info("任务委派成功: taskId={}, from={}, to={}", taskId, currentUser, assignee);
            return ServerResponseEntity.success(result);
        } catch (Exception e) {
            log.error("任务委派失败: taskId={}, from={}, to={}", taskId, currentUser, assignee, e);
            return ServerResponseEntity.fail(WorkflowResponseEnum.TASK_DELEGATE_FAILED);
        }
    }
    
    /**
     * 任务认领
     */
    @ApiOperation(value = "任务认领", notes = "认领候选任务")
    @PostMapping(WorkflowApiPaths.TaskPaths.CLAIM)
    @PreAuthorize("hasAuthority('workflow:task:claim')")
    @CacheEvict(value = {"workflow:tasks"}, allEntries = true)
    public ServerResponseEntity<Map<String, Object>> claimTask(
            @ApiParam(value = "任务ID", required = true) 
            @NotBlank @PathVariable("taskId") String taskId) {
        
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        String currentUser = auth.getName();
        
        try {
            // 验证任务ID
            if (!WorkflowApiPaths.Validator.isValidTaskId(taskId)) {
                return ServerResponseEntity.fail(WorkflowResponseEnum.TASK_NOT_FOUND);
            }
            
            // 验证任务是否可以被当前用户认领
            if (!workflowService.canUserClaimTask(taskId, currentUser)) {
                log.warn("用户 {} 尝试认领无权限的任务: {}", currentUser, taskId);
                return ServerResponseEntity.fail(WorkflowResponseEnum.PERMISSION_DENIED);
            }
            
            workflowService.claimTask(taskId, currentUser);
            
            Map<String, Object> result = new HashMap<>();
            result.put("message", "任务认领成功");
            result.put("taskId", taskId);
            result.put("claimedBy", currentUser);
            
            log.info("任务认领成功: taskId={}, user={}", taskId, currentUser);
            return ServerResponseEntity.success(result);
        } catch (Exception e) {
            log.error("任务认领失败: taskId={}, user={}", taskId, currentUser, e);
            return ServerResponseEntity.fail(WorkflowResponseEnum.TASK_CLAIM_FAILED);
        }
    }
    
    /**
     * 任务转办
     */
    @ApiOperation(value = "任务转办", notes = "将任务转办给其他用户")
    @PostMapping(WorkflowApiPaths.TaskPaths.TRANSFER)
    @PreAuthorize("hasAuthority('workflow:task:transfer') and @workflowPermissionEvaluator.hasPermission(authentication, #taskId, 'TASK', 'WRITE')")
    @CacheEvict(value = {"workflow:tasks"}, allEntries = true)
    public ServerResponseEntity<Map<String, Object>> transferTask(
            @ApiParam(value = "任务ID", required = true) 
            @NotBlank @PathVariable("taskId") String taskId,
            @ApiParam(value = "转办给", required = true) 
            @NotBlank @RequestParam("assignee") String assignee,
            @ApiParam("转办说明") @RequestParam(value = "comment", required = false) String comment) {
        
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        String currentUser = auth.getName();
        
        try {
            // 验证任务ID
            if (!WorkflowApiPaths.Validator.isValidTaskId(taskId)) {
                return ServerResponseEntity.fail(WorkflowResponseEnum.TASK_NOT_FOUND);
            }
            
            // 验证任务是否属于当前用户
            if (!workflowService.isTaskAssignedToUser(taskId, currentUser)) {
                log.warn("用户 {} 尝试转办不属于自己的任务: {}", currentUser, taskId);
                return ServerResponseEntity.fail(WorkflowResponseEnum.PERMISSION_DENIED);
            }
            
            workflowService.transferTask(taskId, assignee, comment);
            
            Map<String, Object> result = new HashMap<>();
            result.put("message", "任务转办成功");
            result.put("taskId", taskId);
            result.put("assignee", assignee);
            result.put("comment", comment);
            result.put("transferredBy", currentUser);
            
            log.info("任务转办成功: taskId={}, from={}, to={}", taskId, currentUser, assignee);
            return ServerResponseEntity.success(result);
        } catch (Exception e) {
            log.error("任务转办失败: taskId={}, from={}, to={}", taskId, currentUser, assignee, e);
            return ServerResponseEntity.fail(WorkflowResponseEnum.TASK_TRANSFER_FAILED);
        }
    }
    
    /**
     * 获取流程定义列表（增强版）
     */
    @ApiOperation(value = "获取流程定义列表", notes = "根据分类、键值、名称等条件查询流程定义")
    @GetMapping(WorkflowApiPaths.DefinitionPaths.ENHANCED)
    public ServerResponseEntity<Map<String, Object>> listProcessDefinitions(
            @ApiParam("流程分类") @RequestParam(value = "category", required = false) String category,
            @ApiParam("流程键值") @RequestParam(value = "key", required = false) String key,
            @ApiParam("流程名称") @RequestParam(value = "name", required = false) String name,
            @ApiParam("页码") @RequestParam(value = "page", required = false, defaultValue = "1") int page,
            @ApiParam("每页大小") @RequestParam(value = "size", required = false, defaultValue = "20") int size) {
        
        try {
            List<ProcessDefinitionDTO> definitions = workflowService.listProcessDefinitions(category, key, name);
            
            // 手动分页
            int total = definitions.size();
            int fromIndex = (page - 1) * size;
            int toIndex = Math.min(fromIndex + size, total);
            List<ProcessDefinitionDTO> pagedDefinitions = definitions.subList(fromIndex, toIndex);
            
            Map<String, Object> result = new HashMap<>();
            result.put("definitions", pagedDefinitions);
            result.put("total", total);
            result.put("page", page);
            result.put("size", size);
            result.put("totalPages", (total + size - 1) / size);
            result.put("category", category);
            result.put("key", key);
            result.put("name", name);
            
            log.debug("获取流程定义列表成功: count={}, category={}, key={}, name={}", 
                    definitions.size(), category, key, name);
            return ServerResponseEntity.success(result);
        } catch (Exception e) {
            log.error("获取流程定义列表失败: category={}, key={}, name={}", category, key, name, e);
            return ServerResponseEntity.fail(WorkflowResponseEnum.PROCESS_DEFINITION_NOT_FOUND);
        }
    }
    
    /**
     * 挂起流程定义
     */
    @ApiOperation(value = "挂起流程定义", notes = "挂起指定的流程定义，挂起后无法启动新的流程实例")
    @PostMapping(WorkflowApiPaths.DefinitionPaths.SUSPEND)
    public ServerResponseEntity<Map<String, Object>> suspendProcessDefinition(
            @ApiParam(value = "流程定义ID", required = true) 
            @NotBlank @PathVariable("processDefinitionId") String processDefinitionId) {
        
        try {
            workflowService.suspendProcessDefinition(processDefinitionId);
            
            Map<String, Object> result = new HashMap<>();
            result.put("message", "流程定义挂起成功");
            result.put("processDefinitionId", processDefinitionId);
            
            log.info("流程定义挂起成功: processDefinitionId={}", processDefinitionId);
            return ServerResponseEntity.success(result);
        } catch (Exception e) {
            log.error("流程定义挂起失败: processDefinitionId={}", processDefinitionId, e);
            return ServerResponseEntity.fail(WorkflowResponseEnum.WORKFLOW_ERROR);
        }
    }
    
    /**
     * 激活流程定义
     */
    @ApiOperation(value = "激活流程定义", notes = "激活指定的流程定义，激活后可以启动新的流程实例")
    @PostMapping(WorkflowApiPaths.DefinitionPaths.ACTIVATE)
    public ServerResponseEntity<Map<String, Object>> activateProcessDefinition(
            @ApiParam(value = "流程定义ID", required = true) 
            @NotBlank @PathVariable("processDefinitionId") String processDefinitionId) {
        
        try {
            workflowService.activateProcessDefinition(processDefinitionId);
            
            Map<String, Object> result = new HashMap<>();
            result.put("message", "流程定义激活成功");
            result.put("processDefinitionId", processDefinitionId);
            
            log.info("流程定义激活成功: processDefinitionId={}", processDefinitionId);
            return ServerResponseEntity.success(result);
        } catch (Exception e) {
            log.error("流程定义激活失败: processDefinitionId={}", processDefinitionId, e);
            return ServerResponseEntity.fail(WorkflowResponseEnum.WORKFLOW_ERROR);
        }
    }
    
    /**
     * 挂起流程实例
     */
    @ApiOperation(value = "挂起流程实例", notes = "挂起指定的流程实例，挂起后无法继续执行任务")
    @PostMapping(WorkflowApiPaths.ProcessPaths.SUSPEND)
    public ServerResponseEntity<Map<String, Object>> suspendProcessInstance(
            @ApiParam(value = "流程实例ID", required = true) 
            @NotBlank @PathVariable("processInstanceId") String processInstanceId) {
        
        try {
            workflowService.suspendProcessInstance(processInstanceId);
            
            Map<String, Object> result = new HashMap<>();
            result.put("message", "流程实例挂起成功");
            result.put("processInstanceId", processInstanceId);
            
            log.info("流程实例挂起成功: processInstanceId={}", processInstanceId);
            return ServerResponseEntity.success(result);
        } catch (Exception e) {
            log.error("流程实例挂起失败: processInstanceId={}", processInstanceId, e);
            return ServerResponseEntity.fail(WorkflowResponseEnum.WORKFLOW_ERROR);
        }
    }
    
    /**
     * 激活流程实例
     */
    @ApiOperation(value = "激活流程实例", notes = "激活指定的流程实例，激活后可以继续执行任务")
    @PostMapping(WorkflowApiPaths.ProcessPaths.ACTIVATE)
    public ServerResponseEntity<Map<String, Object>> activateProcessInstance(
            @ApiParam(value = "流程实例ID", required = true) 
            @NotBlank @PathVariable("processInstanceId") String processInstanceId) {
        
        try {
            workflowService.activateProcessInstance(processInstanceId);
            
            Map<String, Object> result = new HashMap<>();
            result.put("message", "流程实例激活成功");
            result.put("processInstanceId", processInstanceId);
            
            log.info("流程实例激活成功: processInstanceId={}", processInstanceId);
            return ServerResponseEntity.success(result);
        } catch (Exception e) {
            log.error("流程实例激活失败: processInstanceId={}", processInstanceId, e);
            return ServerResponseEntity.fail(WorkflowResponseEnum.WORKFLOW_ERROR);
        }
    }
}