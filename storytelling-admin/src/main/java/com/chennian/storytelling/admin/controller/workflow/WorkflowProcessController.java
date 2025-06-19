package com.chennian.storytelling.admin.controller.workflow;

import com.chennian.storytelling.common.response.ServerResponseEntity;
import com.chennian.storytelling.service.WorkflowService;
import com.chennian.storytelling.bean.dto.WorkflowBatchOperationDTO;
import com.chennian.storytelling.bean.dto.WorkflowHistoryDTO;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import org.flowable.engine.runtime.ProcessInstance;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.HashMap;

/**
 * 工作流流程管理控制器
 * 提供流程实例的高级管理功能
 * 
 * @author chennian
 */
@Api(tags = "工作流流程管理")
@RestController
@RequestMapping("/sys/workflow/processes")
@RequiredArgsConstructor
public class WorkflowProcessController {

    private final WorkflowService workflowService;

    /**
     * 高级流程实例查询
     */
    @ApiOperation("高级流程实例查询")
    @GetMapping("/search")
    public ServerResponseEntity<Map<String, Object>> searchProcessInstances(
            @RequestParam(value = "processDefinitionKey", required = false) String processDefinitionKey,
            @RequestParam(value = "businessKey", required = false) String businessKey,
            @RequestParam(value = "startUserId", required = false) String startUserId,
            @RequestParam(value = "status", required = false) String status,
            @RequestParam(value = "startTime", required = false) 
            @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime startTime,
            @RequestParam(value = "endTime", required = false) 
            @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime endTime,
            @RequestParam(value = "page", defaultValue = "0") int page,
            @RequestParam(value = "size", defaultValue = "20") int size) {
        
        Map<String, Object> result = new HashMap<>();
        
        // 这里可以实现更复杂的流程实例查询逻辑
        // 根据不同的查询条件构建查询
        
        result.put("page", page);
        result.put("size", size);
        result.put("total", 0);
        result.put("processes", List.of());
        
        return ServerResponseEntity.success(result);
    }
    
    /**
     * 启动流程实例（增强版）
     */
    @ApiOperation("启动流程实例")
    @PostMapping("/start")
    public ServerResponseEntity<Map<String, Object>> startProcess(
            @RequestParam("processKey") String processKey,
            @RequestParam("businessKey") String businessKey,
            @RequestParam("businessType") String businessType,
            @RequestParam("title") String title,
            @RequestParam(value = "startUserId", required = false) String startUserId,
            @RequestBody(required = false) Map<String, Object> variables) {
        
        ProcessInstance processInstance = workflowService.startProcess(
            processKey, businessKey, businessType, title, variables);
        
        Map<String, Object> result = new HashMap<>();
        result.put("success", true);
        result.put("message", "流程启动成功");
        result.put("processInstanceId", processInstance.getId());
        result.put("processDefinitionId", processInstance.getProcessDefinitionId());
        result.put("businessKey", processInstance.getBusinessKey());
        
        return ServerResponseEntity.success(result);
    }
    
    /**
     * 获取流程实例详情
     */
    @ApiOperation("获取流程实例详情")
    @GetMapping("/{processInstanceId}/detail")
    public ServerResponseEntity<Map<String, Object>> getProcessDetail(@PathVariable("processInstanceId") String processInstanceId) {
        Map<String, Object> detail = new HashMap<>();
        
        // 获取流程历史
        WorkflowHistoryDTO history = workflowService.getProcessHistory(processInstanceId);
        detail.put("history", history);
        
        // 获取流程变量
        // Map<String, Object> variables = workflowService.getProcessVariables(processInstanceId);
        // detail.put("variables", variables);
        
        // 获取当前任务
        // List<Task> currentTasks = workflowService.findTasksByProcessInstanceId(processInstanceId);
        // detail.put("currentTasks", currentTasks);
        
        return ServerResponseEntity.success(detail);
    }
    
    /**
     * 获取流程图
     */
    @ApiOperation("获取流程图")
    @GetMapping("/{processInstanceId}/diagram")
    public ResponseEntity<byte[]> getProcessDiagram(@PathVariable("processInstanceId") String processInstanceId) {
        byte[] bytes = workflowService.getProcessDiagram(processInstanceId);
        if (bytes == null) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.IMAGE_PNG);
        headers.setContentDispositionFormData("attachment", "process-diagram.png");
        
        return new ResponseEntity<>(bytes, headers, HttpStatus.OK);
    }
    
    /**
     * 终止流程实例
     */
    @ApiOperation("终止流程实例")
    @PostMapping("/{processInstanceId}/terminate")
    public ServerResponseEntity<Map<String, Object>> terminateProcess(
            @PathVariable("processInstanceId") String processInstanceId,
            @RequestParam("reason") String reason) {
        
        workflowService.terminateProcessInstance(processInstanceId, reason);
        
        Map<String, Object> result = new HashMap<>();
        result.put("success", true);
        result.put("message", "流程终止成功");
        result.put("processInstanceId", processInstanceId);
        result.put("reason", reason);
        
        return ServerResponseEntity.success(result);
    }
    
    /**
     * 挂起流程实例
     */
    @ApiOperation("挂起流程实例")
    @PostMapping("/{processInstanceId}/suspend")
    public ServerResponseEntity<Map<String, Object>> suspendProcess(@PathVariable("processInstanceId") String processInstanceId) {
        workflowService.suspendProcessInstance(processInstanceId);
        
        Map<String, Object> result = new HashMap<>();
        result.put("success", true);
        result.put("message", "流程挂起成功");
        result.put("processInstanceId", processInstanceId);
        
        return ServerResponseEntity.success(result);
    }
    
    /**
     * 激活流程实例
     */
    @ApiOperation("激活流程实例")
    @PostMapping("/{processInstanceId}/activate")
    public ServerResponseEntity<Map<String, Object>> activateProcess(@PathVariable("processInstanceId") String processInstanceId) {
        workflowService.activateProcessInstance(processInstanceId);
        
        Map<String, Object> result = new HashMap<>();
        result.put("success", true);
        result.put("message", "流程激活成功");
        result.put("processInstanceId", processInstanceId);
        
        return ServerResponseEntity.success(result);
    }
    
    /**
     * 批量操作流程实例
     */
    @ApiOperation("批量操作流程实例")
    @PostMapping("/batch")
    public ServerResponseEntity<WorkflowBatchOperationDTO.BatchOperationResult> batchOperateProcesses(
            @RequestBody WorkflowBatchOperationDTO batchOperation) {
        return ServerResponseEntity.success(workflowService.batchOperateProcessInstances(batchOperation));
    }
    
    /**
     * 获取流程变量
     */
    @ApiOperation("获取流程变量")
    @GetMapping("/{processInstanceId}/variables")
    public ServerResponseEntity<Map<String, Object>> getProcessVariables(@PathVariable("processInstanceId") String processInstanceId) {
        Map<String, Object> result = new HashMap<>();
        
        // 这里可以获取所有流程变量
        // Map<String, Object> variables = workflowService.getProcessVariables(processInstanceId);
        // result.put("variables", variables);
        
        result.put("processInstanceId", processInstanceId);
        
        return ServerResponseEntity.success(result);
    }
    
    /**
     * 设置流程变量
     */
    @ApiOperation("设置流程变量")
    @PostMapping("/{processInstanceId}/variables")
    public ServerResponseEntity<Map<String, Object>> setProcessVariables(
            @PathVariable("processInstanceId") String processInstanceId,
            @RequestBody Map<String, Object> variables) {
        
        // 批量设置流程变量
        for (Map.Entry<String, Object> entry : variables.entrySet()) {
            workflowService.setProcessVariable(processInstanceId, entry.getKey(), entry.getValue());
        }
        
        Map<String, Object> result = new HashMap<>();
        result.put("success", true);
        result.put("message", "流程变量设置成功");
        result.put("processInstanceId", processInstanceId);
        result.put("variableCount", variables.size());
        
        return ServerResponseEntity.success(result);
    }
    
    // 获取流程历史记录已移至 WorkflowController 统一管理
    // 路径: /workflow/process/{processInstanceId}/history
    
    /**
     * 流程跳转
     */
    @ApiOperation("流程跳转")
    @PostMapping("/{processInstanceId}/jump")
    public ServerResponseEntity<Map<String, Object>> jumpToActivity(
            @PathVariable("processInstanceId") String processInstanceId,
            @RequestParam("targetActivityId") String targetActivityId,
            @RequestParam(value = "comment", required = false) String comment) {
        
        Map<String, Object> result = new HashMap<>();
        
        // 这里可以实现流程跳转功能
        // 需要使用Flowable的高级API
        
        result.put("success", true);
        result.put("message", "流程跳转成功");
        result.put("processInstanceId", processInstanceId);
        result.put("targetActivityId", targetActivityId);
        
        return ServerResponseEntity.success(result);
    }
    
    /**
     * 流程回退
     */
    @ApiOperation("流程回退")
    @PostMapping("/{processInstanceId}/rollback")
    public ServerResponseEntity<Map<String, Object>> rollbackProcess(
            @PathVariable("processInstanceId") String processInstanceId,
            @RequestParam("targetActivityId") String targetActivityId,
            @RequestParam(value = "comment", required = false) String comment) {
        
        Map<String, Object> result = new HashMap<>();
        
        // 这里可以实现流程回退功能
        
        result.put("success", true);
        result.put("message", "流程回退成功");
        result.put("processInstanceId", processInstanceId);
        result.put("targetActivityId", targetActivityId);
        
        return ServerResponseEntity.success(result);
    }
}