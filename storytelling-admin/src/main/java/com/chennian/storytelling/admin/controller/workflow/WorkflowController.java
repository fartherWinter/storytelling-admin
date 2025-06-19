package com.chennian.storytelling.admin.controller.workflow;

import java.io.IOException;
import java.io.InputStream;
import java.util.List;
import java.util.Map;
import java.util.HashMap;

import com.chennian.storytelling.bean.dto.*;
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

import static com.chennian.storytelling.admin.controller.workflow.WorkflowResponse.*;


/**
 * 工作流控制器 - 基础工作流操作
 * 提供流程部署、启动、任务处理等基础功能
 * 
 * @author chennian
 */
@Api(tags = "工作流基础操作")
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
    
    /**
     * 部署流程定义
     */
    @ApiOperation(value = "部署流程定义", notes = "上传并部署新的流程定义")
    @PostMapping(WorkflowApiPaths.CorePaths.DEPLOY)
    public ServerResponseEntity<Map<String, Object>> deployProcess(
            @ApiParam(value = "流程定义信息", required = true) 
            @Valid @RequestBody ProcessDefinitionDTO processDefinition) {
        try {
            String deploymentId = workflowService.deployProcess(
                    processDefinition.getName(),
                    processDefinition.getCategory(),
                    processDefinition.getResourceName(),
                    processDefinition.getDeploymentFile());
            
            Map<String, Object> result = new HashMap<>();
            result.put("deploymentId", deploymentId);
            result.put("processName", processDefinition.getName());
            
            log.info("流程部署成功: {}, deploymentId: {}", processDefinition.getName(), deploymentId);
            return ServerResponseEntity.success(result);
        } catch (Exception e) {
            log.error("流程部署失败: {}", processDefinition.getName(), e);
            return ServerResponseEntity.showFailMsg("流程部署失败: " + e.getMessage());
        }
    }
    
    /**
     * 启动流程实例
     */
    @ApiOperation(value = "启动流程实例", notes = "根据流程定义启动新的流程实例")
    @PostMapping(WorkflowApiPaths.CorePaths.START)
    public ServerResponseEntity<Map<String, Object>> startProcess(
            @ApiParam(value = "流程定义Key", required = true) 
            @NotBlank @RequestParam("processKey") String processKey,
            @ApiParam(value = "业务Key", required = true) 
            @NotBlank @RequestParam("businessKey") String businessKey,
            @ApiParam(value = "业务类型", required = true) 
            @NotBlank @RequestParam("businessType") String businessType,
            @ApiParam(value = "流程标题", required = true) 
            @NotBlank @RequestParam("title") String title,
            @ApiParam(value = "流程变量") 
            @RequestBody(required = false) Map<String, Object> variables) {
        try {
            ProcessInstance processInstance = workflowService.startProcess(
                processKey, businessKey, businessType, title, variables);
            
            Map<String, Object> result = new HashMap<>();
            result.put("processInstanceId", processInstance.getId());
            result.put("processDefinitionId", processInstance.getProcessDefinitionId());
            result.put("businessKey", processInstance.getBusinessKey());
            result.put("processKey", processKey);
            result.put("title", title);
            
            log.info("流程启动成功: processKey={}, businessKey={}, instanceId={}", 
                processKey, businessKey, processInstance.getId());
            return ServerResponseEntity.success(result);
        } catch (Exception e) {
            log.error("流程启动失败: processKey={}, businessKey={}", processKey, businessKey, e);
            return ServerResponseEntity.showFailMsg("流程启动失败: " + e.getMessage());
        }
    }
    
    /**
     * 查询待办任务
     */
    @ApiOperation(value = "查询待办任务", notes = "查询指定用户的待办任务列表")
    @GetMapping(WorkflowApiPaths.TaskPaths.TODO)
    public ServerResponseEntity<Map<String, Object>> findTodoTasks(
            @ApiParam(value = "任务处理人", required = true) 
            @NotBlank @RequestParam("assignee") String assignee,
            @ApiParam(value = "页码", defaultValue = "0") 
            @RequestParam(value = "page", defaultValue = "0") int page,
            @ApiParam(value = "每页大小", defaultValue = "20") 
            @RequestParam(value = "size", defaultValue = "20") int size) {
        try {
            List<Task> tasks = workflowService.findTodoTasks(assignee);
            
            Map<String, Object> result = new HashMap<>();
            result.put("tasks", tasks);
            result.put("total", tasks.size());
            result.put("assignee", assignee);
            result.put("page", page);
            result.put("size", size);
            
            log.debug("查询待办任务成功: assignee={}, count={}", assignee, tasks.size());
            return ServerResponseEntity.success(result);
        } catch (Exception e) {
            log.error("查询待办任务失败: assignee={}", assignee, e);
            return ServerResponseEntity.showFailMsg("查询待办任务失败: " + e.getMessage());
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
            return ServerResponseEntity.showFailMsg("查询流程任务失败: " + e.getMessage());
        }
    }
    
    /**
     * 完成任务
     */
    @ApiOperation(value = "完成任务", notes = "完成指定的工作流任务")
    @PostMapping(WorkflowApiPaths.TaskPaths.COMPLETE)
    public ServerResponseEntity<Map<String, Object>> completeTask(
            @ApiParam(value = "任务ID", required = true) 
            @NotBlank @PathVariable("taskId") String taskId,
            @ApiParam("审批意见") @RequestParam(value = "comment", required = false) String comment,
            @ApiParam("流程变量") @RequestBody(required = false) Map<String, Object> variables) {
        
        try {
            // 验证任务ID
            if (!WorkflowApiPaths.Validator.isValidTaskId(taskId)) {
                return ServerResponseEntity.showFailMsg("无效的任务ID格式");
            }
            
            workflowService.completeTask(taskId, variables, comment);
            
            Map<String, Object> result = new HashMap<>();
            result.put("taskId", taskId);
            result.put("comment", comment);
            
            log.info("任务完成成功: taskId={}, comment={}", taskId, comment);
            return ServerResponseEntity.success(result);
        } catch (Exception e) {
            log.error("任务完成失败: taskId={}", taskId, e);
            return ServerResponseEntity.showFailMsg("任务完成失败: " + e.getMessage());
        }
    }
    
    /**
     * 审批通过任务
     */
    @ApiOperation(value = "审批通过任务", notes = "审批通过指定的工作流任务")
    @PostMapping(WorkflowApiPaths.TaskPaths.APPROVE)
    public ServerResponseEntity<Map<String, Object>> approveTask(
            @ApiParam(value = "任务ID", required = true) 
            @NotBlank @PathVariable("taskId") String taskId,
            @ApiParam("审批意见") @RequestParam(value = "comment", required = false) String comment) {
        
        try {
            workflowService.approveTask(taskId, comment);
            
            Map<String, Object> result = new HashMap<>();
            result.put("taskId", taskId);
            result.put("comment", comment);
            
            log.info("任务审批通过: taskId={}, comment={}", taskId, comment);
            return ServerResponseEntity.success(result);
        } catch (Exception e) {
            log.error("任务审批失败: taskId={}", taskId, e);
            return ServerResponseEntity.showFailMsg("任务审批失败: " + e.getMessage());
        }
    }
    
    /**
     * 拒绝任务
     */
    @ApiOperation(value = "拒绝任务", notes = "拒绝指定的工作流任务")
    @PostMapping(WorkflowApiPaths.TaskPaths.REJECT)
    public Map<String, Object> rejectTask(
            @ApiParam(value = "任务ID", required = true) 
            @NotBlank @PathVariable("taskId") String taskId,
            @ApiParam("拒绝原因") @RequestParam(value = "comment", required = false) String comment) {
        
        Map<String, Object> result = new HashMap<>();
        try {
            workflowService.rejectTask(taskId, comment);
            
            result.put("success", true);
            result.put("message", "任务已拒绝");
            result.put("taskId", taskId);
            result.put("comment", comment);
            
            log.info("任务已拒绝: taskId={}, comment={}", taskId, comment);
        } catch (Exception e) {
            log.error("任务拒绝失败: taskId={}", taskId, e);
            result.put("success", false);
            result.put("message", "任务拒绝失败: " + e.getMessage());
        }
        return result;
    }
    
    /**
     * 获取流程图
     */
    @ApiOperation(value = "获取流程图", notes = "获取指定流程实例的流程图")
    @GetMapping(WorkflowApiPaths.ProcessPaths.DIAGRAM)
    public ResponseEntity<byte[]> getProcessDiagram(
            @ApiParam(value = "流程实例ID", required = true) 
            @NotBlank @PathVariable("processInstanceId") String processInstanceId) {
        
        try {
            byte[] bytes = workflowService.getProcessDiagram(processInstanceId);
            if (bytes == null) {
                log.warn("流程图不存在: processInstanceId={}", processInstanceId);
                return new ResponseEntity<>(HttpStatus.NOT_FOUND);
            }
            
            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.IMAGE_PNG);
            headers.setContentDispositionFormData("attachment", "process_diagram.png");
            
            log.debug("获取流程图成功: processInstanceId={}, size={}", processInstanceId, bytes.length);
            return new ResponseEntity<>(bytes, headers, HttpStatus.OK);
            
        } catch (Exception e) {
            log.error("获取流程图失败: processInstanceId={}", processInstanceId, e);
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
    
    /**
     * 终止流程实例
     */
    @ApiOperation(value = "终止流程实例", notes = "终止指定的流程实例")
    @DeleteMapping(WorkflowApiPaths.ProcessPaths.TERMINATE)
    public Map<String, Object> terminateProcessInstance(
            @ApiParam(value = "流程实例ID", required = true) 
            @NotBlank @PathVariable("processInstanceId") String processInstanceId,
            @ApiParam(value = "终止原因", required = true) 
            @NotBlank @RequestParam("reason") String reason) {
        
        Map<String, Object> result = new HashMap<>();
        try {
            workflowService.terminateProcessInstance(processInstanceId, reason);
            
            result.put("success", true);
            result.put("message", "流程实例终止成功");
            result.put("processInstanceId", processInstanceId);
            result.put("reason", reason);
            
            log.info("流程实例终止成功: processInstanceId={}, reason={}", processInstanceId, reason);
        } catch (Exception e) {
            log.error("流程实例终止失败: processInstanceId={}", processInstanceId, e);
            result.put("success", false);
            result.put("message", "流程实例终止失败: " + e.getMessage());
        }
        return result;
    }
    
    /**
     * 获取流程变量
     */
    @ApiOperation(value = "获取流程变量", notes = "获取指定流程实例的变量值")
    @GetMapping(WorkflowApiPaths.ProcessPaths.VARIABLE_GET)
    public Map<String, Object> getProcessVariable(
            @ApiParam(value = "流程实例ID", required = true) 
            @NotBlank @PathVariable("processInstanceId") String processInstanceId,
            @ApiParam(value = "变量名", required = true) 
            @NotBlank @PathVariable("variableName") String variableName) {
        
        Map<String, Object> result = new HashMap<>();
        try {
            Object value = workflowService.getProcessVariable(processInstanceId, variableName);
            
            result.put("success", true);
            result.put("processInstanceId", processInstanceId);
            result.put("variableName", variableName);
            result.put("value", value);
            
            log.debug("获取流程变量成功: processInstanceId={}, variableName={}", processInstanceId, variableName);
        } catch (Exception e) {
            log.error("获取流程变量失败: processInstanceId={}, variableName={}", processInstanceId, variableName, e);
            result.put("success", false);
            result.put("message", "获取流程变量失败: " + e.getMessage());
        }
        return result;
    }
    
    /**
     * 设置流程变量
     */
    @ApiOperation(value = "设置流程变量", notes = "设置指定流程实例的变量值")
    @PostMapping(WorkflowApiPaths.ProcessPaths.VARIABLE_SET)
    public Map<String, Object> setProcessVariable(
            @ApiParam(value = "流程实例ID", required = true) 
            @NotBlank @PathVariable("processInstanceId") String processInstanceId,
            @ApiParam(value = "变量名", required = true) 
            @NotBlank @PathVariable("variableName") String variableName,
            @ApiParam(value = "变量值", required = true) @RequestBody Object value) {
        
        Map<String, Object> result = new HashMap<>();
        try {
            workflowService.setProcessVariable(processInstanceId, variableName, value);
            
            result.put("success", true);
            result.put("message", "流程变量设置成功");
            result.put("processInstanceId", processInstanceId);
            result.put("variableName", variableName);
            result.put("value", value);
            
            log.info("流程变量设置成功: processInstanceId={}, variableName={}", processInstanceId, variableName);
        } catch (Exception e) {
            log.error("流程变量设置失败: processInstanceId={}, variableName={}", processInstanceId, variableName, e);
            result.put("success", false);
            result.put("message", "流程变量设置失败: " + e.getMessage());
        }
        return result;
    }
    
    /**
     * 获取流程定义列表
     */
    @ApiOperation(value = "获取流程定义列表", notes = "获取系统中的流程定义列表")
    @GetMapping(WorkflowApiPaths.DefinitionPaths.LIST)
    public Map<String, Object> getProcessDefinitions(
            @ApiParam("流程定义Key") @RequestParam(value = "key", required = false) String key,
            @ApiParam("流程分类") @RequestParam(value = "category", required = false) String category,
            @ApiParam("是否只查询最新版本") @RequestParam(value = "latest", required = false, defaultValue = "true") boolean latest,
            @ApiParam("页码") @RequestParam(value = "page", required = false, defaultValue = "1") int page,
            @ApiParam("每页大小") @RequestParam(value = "size", required = false, defaultValue = "20") int size) {
        
        Map<String, Object> result = new HashMap<>();
        try {
            // 构建查询条件
            org.flowable.engine.repository.ProcessDefinitionQuery query = repositoryService.createProcessDefinitionQuery();
            
            if (key != null && !key.isEmpty()) {
                query.processDefinitionKeyLike("%" + key + "%");
            }
            
            if (category != null && !category.isEmpty()) {
                query.processDefinitionCategoryLike("%" + category + "%");
            }
            
            // 是否只查询最新版本
            if (latest) {
                query.latestVersion();
            }
            
            // 按版本降序排列
            query.orderByProcessDefinitionVersion().desc();
            
            // 分页查询
            long total = query.count();
            List<ProcessDefinition> definitions = query
                    .listPage((page - 1) * size, size);
            
            result.put("success", true);
            result.put("definitions", definitions);
            result.put("total", total);
            result.put("page", page);
            result.put("size", size);
            result.put("totalPages", (total + size - 1) / size);
            
            log.debug("获取流程定义列表成功: count={}, page={}, size={}", definitions.size(), page, size);
        } catch (Exception e) {
            log.error("获取流程定义列表失败", e);
            result.put("success", false);
            result.put("message", "获取流程定义列表失败: " + e.getMessage());
        }
        return result;
    }
    
    /**
     * 获取流程定义详情
     */
    @ApiOperation(value = "获取流程定义详情", notes = "获取指定流程定义的详细信息")
    @GetMapping(WorkflowApiPaths.DefinitionPaths.DETAIL)
    public Map<String, Object> getProcessDefinition(
            @ApiParam(value = "流程定义ID", required = true) 
            @NotBlank @PathVariable("processDefinitionId") String processDefinitionId) {
        
        Map<String, Object> result = new HashMap<>();
        try {
            ProcessDefinition definition = repositoryService.createProcessDefinitionQuery()
                    .processDefinitionId(processDefinitionId)
                    .singleResult();
            
            if (definition == null) {
                result.put("success", false);
                result.put("message", "流程定义不存在");
            } else {
                result.put("success", true);
                result.put("definition", definition);
                
                // 获取额外信息
                long instanceCount = runtimeService.createProcessInstanceQuery()
                        .processDefinitionId(processDefinitionId)
                        .count();
                result.put("instanceCount", instanceCount);
                
                log.debug("获取流程定义详情成功: processDefinitionId={}", processDefinitionId);
            }
        } catch (Exception e) {
            log.error("获取流程定义详情失败: processDefinitionId={}", processDefinitionId, e);
            result.put("success", false);
            result.put("message", "获取流程定义详情失败: " + e.getMessage());
        }
        return result;
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
    public Map<String, Object> getWorkflowStatistics(
            @ApiParam("开始时间") @RequestParam(value = "startTime", required = false) 
            @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime startTime,
            @ApiParam("结束时间") @RequestParam(value = "endTime", required = false) 
            @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime endTime) {
        
        Map<String, Object> result = new HashMap<>();
        try {
            WorkflowStatisticsDTO statistics = workflowService.getWorkflowStatistics(startTime, endTime);
            
            result.put("success", true);
            result.put("statistics", statistics);
            result.put("startTime", startTime);
            result.put("endTime", endTime);
            
            log.debug("获取工作流统计信息成功: startTime={}, endTime={}", startTime, endTime);
        } catch (Exception e) {
            log.error("获取工作流统计信息失败: startTime={}, endTime={}", startTime, endTime, e);
            result.put("success", false);
            result.put("message", "获取工作流统计信息失败: " + e.getMessage());
        }
        return result;
    }
    
    /**
     * 批量操作任务
     */
    @ApiOperation(value = "批量操作任务", notes = "批量完成、审批或拒绝任务")
    @PostMapping(WorkflowApiPaths.TaskPaths.BATCH)
    public Map<String, Object> batchOperateTasks(
            @ApiParam(value = "批量操作参数", required = true) 
            @Valid @RequestBody WorkflowBatchOperationDTO batchOperation) {
        
        try {
            WorkflowBatchOperationDTO.BatchOperationResult operationResult =
                    workflowService.batchOperateTasks(batchOperation);
            
            int totalCount = batchOperation.getTaskIds().size();
            int successCount = operationResult.getSuccessCount();
            int failureCount = operationResult.getFailureCount();
            
            log.info("批量操作任务完成: operation={}, total={}, success={}, failure={}", 
                    batchOperation.getOperationType().name(), totalCount, successCount, failureCount);
            
            return batch(batchOperation.getOperationType().name(), totalCount, successCount, failureCount);
        } catch (Exception e) {
            log.error("批量操作任务失败: operation={}", batchOperation.getOperationType().name(), e);
            return error("批量操作任务失败: " + e.getMessage(), e);
        }
    }
    
    /**
     * 批量操作流程实例
     */
    @ApiOperation(value = "批量操作流程实例", notes = "批量终止、挂起或激活流程实例")
    @PostMapping(WorkflowApiPaths.ProcessPaths.BATCH)
    public Map<String, Object> batchOperateProcessInstances(
            @ApiParam(value = "批量操作参数", required = true) 
            @Valid @RequestBody WorkflowBatchOperationDTO batchOperation) {
        
        Map<String, Object> result = new HashMap<>();
        try {
            WorkflowBatchOperationDTO.BatchOperationResult operationResult = 
                    workflowService.batchOperateProcessInstances(batchOperation);
            
            result.put("success", true);
            result.put("result", operationResult);
            result.put("operation", batchOperation.getOperationType().name());
            result.put("instanceCount", batchOperation.getProcessInstanceIds().size());
            
            log.info("批量操作流程实例成功: operation={}, instanceCount={}", 
                    batchOperation.getOperationType().name(), batchOperation.getProcessInstanceIds().size());
        } catch (Exception e) {
            log.error("批量操作流程实例失败: operation={}", batchOperation.getOperationType().name(), e);
            result.put("success", false);
            result.put("message", "批量操作流程实例失败: " + e.getMessage());
        }
        return result;
    }
    
    /**
     * 获取流程历史记录
     */
    @ApiOperation(value = "获取流程历史记录", notes = "获取指定流程实例的完整历史记录")
    @GetMapping(WorkflowApiPaths.ProcessPaths.HISTORY)
    public Map<String, Object> getProcessHistory(
            @ApiParam(value = "流程实例ID", required = true) 
            @NotBlank @PathVariable("processInstanceId") String processInstanceId) {
        
        Map<String, Object> result = new HashMap<>();
        try {
            WorkflowHistoryDTO history = workflowService.getProcessHistory(processInstanceId);
            
            result.put("success", true);
            result.put("history", history);
            result.put("processInstanceId", processInstanceId);
            
            log.debug("获取流程历史记录成功: processInstanceId={}", processInstanceId);
        } catch (Exception e) {
            log.error("获取流程历史记录失败: processInstanceId={}", processInstanceId, e);
            result.put("success", false);
            result.put("message", "获取流程历史记录失败: " + e.getMessage());
        }
        return result;
    }
    
    /**
     * 查询用户历史任务
     */
    @ApiOperation(value = "查询用户历史任务", notes = "查询指定用户在指定时间范围内的历史任务")
    @GetMapping(WorkflowApiPaths.TaskPaths.HISTORY)
    public Map<String, Object> findHistoryTasks(
            @ApiParam(value = "任务处理人", required = true) 
            @NotBlank @RequestParam("assignee") String assignee,
            @ApiParam("开始时间") @RequestParam(value = "startTime", required = false) 
            @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime startTime,
            @ApiParam("结束时间") @RequestParam(value = "endTime", required = false) 
            @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime endTime,
            @ApiParam("页码") @RequestParam(value = "page", required = false, defaultValue = "1") int page,
            @ApiParam("每页大小") @RequestParam(value = "size", required = false, defaultValue = "20") int size) {
        
        Map<String, Object> result = new HashMap<>();
        try {
            List<TaskDTO> tasks = workflowService.findHistoryTasks(assignee, startTime, endTime);
            
            // 手动分页
            int total = tasks.size();
            int fromIndex = (page - 1) * size;
            int toIndex = Math.min(fromIndex + size, total);
            List<TaskDTO> pagedTasks = tasks.subList(fromIndex, toIndex);
            
            result.put("success", true);
            result.put("tasks", pagedTasks);
            result.put("total", total);
            result.put("page", page);
            result.put("size", size);
            result.put("totalPages", (total + size - 1) / size);
            result.put("assignee", assignee);
            
            log.debug("查询用户历史任务成功: assignee={}, count={}", assignee, tasks.size());
        } catch (Exception e) {
            log.error("查询用户历史任务失败: assignee={}", assignee, e);
            result.put("success", false);
            result.put("message", "查询用户历史任务失败: " + e.getMessage());
        }
        return result;
    }
    
    /**
     * 任务委派
     */
    @ApiOperation(value = "任务委派", notes = "将任务委派给其他用户处理")
    @PostMapping(WorkflowApiPaths.TaskPaths.DELEGATE)
    public Map<String, Object> delegateTask(
            @ApiParam(value = "任务ID", required = true) 
            @NotBlank @PathVariable("taskId") String taskId,
            @ApiParam(value = "被委派人", required = true) 
            @NotBlank @RequestParam("assignee") String assignee,
            @ApiParam("委派说明") @RequestParam(value = "comment", required = false) String comment) {
        
        Map<String, Object> result = new HashMap<>();
        try {
            // 验证任务ID
            if (!WorkflowApiPaths.Validator.isValidTaskId(taskId)) {
                result.put("success", false);
                result.put("message", "无效的任务ID格式");
                return result;
            }
            
            workflowService.delegateTask(taskId, assignee, comment);
            
            result.put("success", true);
            result.put("message", "任务委派成功");
            result.put("taskId", taskId);
            result.put("assignee", assignee);
            result.put("comment", comment);
            
            log.info("任务委派成功: taskId={}, assignee={}", taskId, assignee);
        } catch (Exception e) {
            log.error("任务委派失败: taskId={}, assignee={}", taskId, assignee, e);
            result.put("success", false);
            result.put("message", "任务委派失败: " + e.getMessage());
        }
        return result;
    }
    
    /**
     * 任务认领
     */
    @ApiOperation(value = "任务认领", notes = "认领候选任务")
    @PostMapping(WorkflowApiPaths.TaskPaths.CLAIM)
    public Map<String, Object> claimTask(
            @ApiParam(value = "任务ID", required = true) 
            @NotBlank @PathVariable("taskId") String taskId,
            @ApiParam(value = "认领人", required = true) 
            @NotBlank @RequestParam("assignee") String assignee) {
        
        Map<String, Object> result = new HashMap<>();
        try {
            // 验证任务ID
            if (!WorkflowApiPaths.Validator.isValidTaskId(taskId)) {
                result.put("success", false);
                result.put("message", "无效的任务ID格式");
                return result;
            }
            
            workflowService.claimTask(taskId, assignee);
            
            result.put("success", true);
            result.put("message", "任务认领成功");
            result.put("taskId", taskId);
            result.put("assignee", assignee);
            
            log.info("任务认领成功: taskId={}, assignee={}", taskId, assignee);
        } catch (Exception e) {
            log.error("任务认领失败: taskId={}, assignee={}", taskId, assignee, e);
            result.put("success", false);
            result.put("message", "任务认领失败: " + e.getMessage());
        }
        return result;
    }
    
    /**
     * 任务转办
     */
    @ApiOperation(value = "任务转办", notes = "将任务转办给其他用户")
    @PostMapping(WorkflowApiPaths.TaskPaths.TRANSFER)
    public Map<String, Object> transferTask(
            @ApiParam(value = "任务ID", required = true) 
            @NotBlank @PathVariable("taskId") String taskId,
            @ApiParam(value = "转办给", required = true) 
            @NotBlank @RequestParam("assignee") String assignee,
            @ApiParam("转办说明") @RequestParam(value = "comment", required = false) String comment) {
        
        Map<String, Object> result = new HashMap<>();
        try {
            // 验证任务ID
            if (!WorkflowApiPaths.Validator.isValidTaskId(taskId)) {
                result.put("success", false);
                result.put("message", "无效的任务ID格式");
                return result;
            }
            
            workflowService.transferTask(taskId, assignee, comment);
            
            result.put("success", true);
            result.put("message", "任务转办成功");
            result.put("taskId", taskId);
            result.put("assignee", assignee);
            result.put("comment", comment);
            
            log.info("任务转办成功: taskId={}, assignee={}", taskId, assignee);
        } catch (Exception e) {
            log.error("任务转办失败: taskId={}, assignee={}", taskId, assignee, e);
            result.put("success", false);
            result.put("message", "任务转办失败: " + e.getMessage());
        }
        return result;
    }
    
    /**
     * 获取流程定义列表（增强版）
     */
    @ApiOperation(value = "获取流程定义列表", notes = "根据分类、键值、名称等条件查询流程定义")
    @GetMapping(WorkflowApiPaths.DefinitionPaths.ENHANCED)
    public Map<String, Object> listProcessDefinitions(
            @ApiParam("流程分类") @RequestParam(value = "category", required = false) String category,
            @ApiParam("流程键值") @RequestParam(value = "key", required = false) String key,
            @ApiParam("流程名称") @RequestParam(value = "name", required = false) String name,
            @ApiParam("页码") @RequestParam(value = "page", required = false, defaultValue = "1") int page,
            @ApiParam("每页大小") @RequestParam(value = "size", required = false, defaultValue = "20") int size) {
        
        Map<String, Object> result = new HashMap<>();
        try {
            List<ProcessDefinitionDTO> definitions = workflowService.listProcessDefinitions(category, key, name);
            
            // 手动分页
            int total = definitions.size();
            int fromIndex = (page - 1) * size;
            int toIndex = Math.min(fromIndex + size, total);
            List<ProcessDefinitionDTO> pagedDefinitions = definitions.subList(fromIndex, toIndex);
            
            result.put("success", true);
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
        } catch (Exception e) {
            log.error("获取流程定义列表失败: category={}, key={}, name={}", category, key, name, e);
            result.put("success", false);
            result.put("message", "获取流程定义列表失败: " + e.getMessage());
        }
        return result;
    }
    
    /**
     * 挂起流程定义
     */
    @ApiOperation(value = "挂起流程定义", notes = "挂起指定的流程定义，挂起后无法启动新的流程实例")
    @PostMapping(WorkflowApiPaths.DefinitionPaths.SUSPEND)
    public Map<String, Object> suspendProcessDefinition(
            @ApiParam(value = "流程定义ID", required = true) 
            @NotBlank @PathVariable("processDefinitionId") String processDefinitionId) {
        
        Map<String, Object> result = new HashMap<>();
        try {
            workflowService.suspendProcessDefinition(processDefinitionId);
            
            result.put("success", true);
            result.put("message", "流程定义挂起成功");
            result.put("processDefinitionId", processDefinitionId);
            
            log.info("流程定义挂起成功: processDefinitionId={}", processDefinitionId);
        } catch (Exception e) {
            log.error("流程定义挂起失败: processDefinitionId={}", processDefinitionId, e);
            result.put("success", false);
            result.put("message", "流程定义挂起失败: " + e.getMessage());
        }
        return result;
    }
    
    /**
     * 激活流程定义
     */
    @ApiOperation(value = "激活流程定义", notes = "激活指定的流程定义，激活后可以启动新的流程实例")
    @PostMapping(WorkflowApiPaths.DefinitionPaths.ACTIVATE)
    public Map<String, Object> activateProcessDefinition(
            @ApiParam(value = "流程定义ID", required = true) 
            @NotBlank @PathVariable("processDefinitionId") String processDefinitionId) {
        
        Map<String, Object> result = new HashMap<>();
        try {
            workflowService.activateProcessDefinition(processDefinitionId);
            
            result.put("success", true);
            result.put("message", "流程定义激活成功");
            result.put("processDefinitionId", processDefinitionId);
            
            log.info("流程定义激活成功: processDefinitionId={}", processDefinitionId);
        } catch (Exception e) {
            log.error("流程定义激活失败: processDefinitionId={}", processDefinitionId, e);
            result.put("success", false);
            result.put("message", "流程定义激活失败: " + e.getMessage());
        }
        return result;
    }
    
    /**
     * 挂起流程实例
     */
    @ApiOperation(value = "挂起流程实例", notes = "挂起指定的流程实例，挂起后无法继续执行任务")
    @PostMapping(WorkflowApiPaths.ProcessPaths.SUSPEND)
    public Map<String, Object> suspendProcessInstance(
            @ApiParam(value = "流程实例ID", required = true) 
            @NotBlank @PathVariable("processInstanceId") String processInstanceId) {
        
        Map<String, Object> result = new HashMap<>();
        try {
            workflowService.suspendProcessInstance(processInstanceId);
            
            result.put("success", true);
            result.put("message", "流程实例挂起成功");
            result.put("processInstanceId", processInstanceId);
            
            log.info("流程实例挂起成功: processInstanceId={}", processInstanceId);
        } catch (Exception e) {
            log.error("流程实例挂起失败: processInstanceId={}", processInstanceId, e);
            result.put("success", false);
            result.put("message", "流程实例挂起失败: " + e.getMessage());
        }
        return result;
    }
    
    /**
     * 激活流程实例
     */
    @ApiOperation(value = "激活流程实例", notes = "激活指定的流程实例，激活后可以继续执行任务")
    @PostMapping(WorkflowApiPaths.ProcessPaths.ACTIVATE)
    public Map<String, Object> activateProcessInstance(
            @ApiParam(value = "流程实例ID", required = true) 
            @NotBlank @PathVariable("processInstanceId") String processInstanceId) {
        
        Map<String, Object> result = new HashMap<>();
        try {
            workflowService.activateProcessInstance(processInstanceId);
            
            result.put("success", true);
            result.put("message", "流程实例激活成功");
            result.put("processInstanceId", processInstanceId);
            
            log.info("流程实例激活成功: processInstanceId={}", processInstanceId);
        } catch (Exception e) {
            log.error("流程实例激活失败: processInstanceId={}", processInstanceId, e);
            result.put("success", false);
            result.put("message", "流程实例激活失败: " + e.getMessage());
        }
        return result;
    }
}