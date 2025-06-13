package com.chennian.storytelling.admin.controller;

import com.chennian.storytelling.common.annotation.EventTrack;
import com.chennian.storytelling.common.enums.BusinessType;
import com.chennian.storytelling.common.enums.ModelType;
import com.chennian.storytelling.common.enums.OperatorType;
import com.chennian.security.admin.util.SecurityUtils;
import com.chennian.storytelling.bean.model.SysUser;
import com.chennian.storytelling.common.response.ServerResponseEntity;
import com.chennian.storytelling.bean.dto.ProcessDefinitionDTO;
import com.chennian.storytelling.service.WorkflowService;
import com.chennian.storytelling.workflow.utils.AdminWorkflowUtils;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.flowable.task.api.Task;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 管理员创建流程控制器
 * 
 * @author chennian
 */
@RestController
@RequestMapping("/admin/workflow")
@Tag(name = "管理员创建流程")
public class AdminWorkflowController {

    private final WorkflowService workflowService;

    private final AdminWorkflowUtils adminWorkflowUtils;

    public AdminWorkflowController(WorkflowService workflowService, AdminWorkflowUtils adminWorkflowUtils) {
        this.workflowService = workflowService;
        this.adminWorkflowUtils = adminWorkflowUtils;
    }
    
    /**
     * 部署管理员创建流程
     */
    @PostMapping("/deploy/admin")
    @Operation(summary = "部署管理员创建流程")
    @EventTrack(title = ModelType.SYSTEM, businessType = BusinessType.INSERT, operatorType = OperatorType.MANAGE, description = "部署管理员创建流程")
    public ServerResponseEntity<String> deployAdminProcess(@RequestBody ProcessDefinitionDTO processDefinition) {
        String processDefinitionId = workflowService.deployProcess(
                processDefinition.getName(),
                processDefinition.getCategory(),
                processDefinition.getResourceName(),
                processDefinition.getDeploymentFile());
        return ServerResponseEntity.success(processDefinitionId);
    }
    
    /**
     * 启动管理员创建流程
     */
    @PostMapping("/start/admin")
    @Operation(summary = "启动管理员创建流程")
    @EventTrack(title = ModelType.SYSTEM, businessType = BusinessType.INSERT, operatorType = OperatorType.MANAGE, description = "启动管理员创建流程")
    public ServerResponseEntity<String> startAdminProcess(@RequestBody SysUser sysUser) {
        // 获取当前登录用户
        String currentUserName = SecurityUtils.getUserName();
        
        // 启动管理员创建流程
        String processInstanceId = adminWorkflowUtils.startAdminCreationProcess(sysUser, currentUserName);
        
        return ServerResponseEntity.success(processInstanceId);
    }
    
    /**
     * 查询我的待办任务
     */
    @PostMapping("/tasks/todo")
    @Operation(summary = "我的待办任务")
    @EventTrack(title = ModelType.SYSTEM, businessType = BusinessType.SEARCH, operatorType = OperatorType.MANAGE, description = "我的待办任务")
    public ServerResponseEntity<List<Task>> myTodoTasks() {
        String username = SecurityUtils.getUserName();
        List<Task> tasks = workflowService.findTodoTasks(username);
        return ServerResponseEntity.success(tasks);
    }
    
    /**
     * 审批通过任务
     */
    @PostMapping("/task/approve/{taskId}")
    @Operation(summary = "审批通过")
    @EventTrack(title = ModelType.SYSTEM, businessType = BusinessType.UPDATE, operatorType = OperatorType.MANAGE, description = "审批通过")
    public ServerResponseEntity<Void> approveTask(@PathVariable("taskId") String taskId, @RequestParam("comment") String comment) {
        workflowService.approveTask(taskId, comment);
        return ServerResponseEntity.success();
    }
    
    /**
     * 拒绝任务
     */
    @PostMapping("/task/reject/{taskId}")
    @Operation(summary = "拒绝任务")
    @EventTrack(title = ModelType.SYSTEM, businessType = BusinessType.UPDATE, operatorType = OperatorType.MANAGE, description = "拒绝任务")
    public ServerResponseEntity<Void> rejectTask(@PathVariable("taskId") String taskId, @RequestParam("comment") String comment) {
        workflowService.rejectTask(taskId, comment);
        return ServerResponseEntity.success();
    }
    
    /**
     * 获取流程图
     */
    @GetMapping("/diagram/{processInstanceId}")
    @Operation(summary = "获取流程图")
    @EventTrack(title = ModelType.SYSTEM, businessType = BusinessType.SEARCH, operatorType = OperatorType.MANAGE, description = "获取流程图")
    public ServerResponseEntity<byte[]> getProcessDiagram(@PathVariable("processInstanceId") String processInstanceId) {
        byte[] bytes = workflowService.getProcessDiagram(processInstanceId);
        return ServerResponseEntity.success(bytes);
    }
    
    /**
     * 获取流程状态
     */
    @GetMapping("/status/{processInstanceId}")
    @Operation(summary = "获取流程状态")
    @EventTrack(title = ModelType.SYSTEM, businessType = BusinessType.SEARCH, operatorType = OperatorType.MANAGE, description = "获取流程状态")
    public ServerResponseEntity<Object> getProcessStatus(@PathVariable("processInstanceId") String processInstanceId) {
        Object status = workflowService.getProcessVariable(processInstanceId, "processStatus");
        return ServerResponseEntity.success(status);
    }
    
    /**
     * 终止流程
     */
    @PostMapping("/terminate/{processInstanceId}")
    @Operation(summary = "终止流程")
    @EventTrack(title = ModelType.SYSTEM, businessType = BusinessType.UPDATE, operatorType = OperatorType.MANAGE, description = "终止流程")
    public ServerResponseEntity<Void> terminateProcess(
            @PathVariable("processInstanceId") String processInstanceId,
            @RequestParam("reason") String reason) {
        workflowService.terminateProcessInstance(processInstanceId, reason);
        return ServerResponseEntity.success();
    }
}