package com.chennian.storytelling.admin.controller.workflow;

import com.chennian.storytelling.common.response.ServerResponseEntity;
import com.chennian.storytelling.common.response.ResponseEnum;
import com.chennian.storytelling.common.enums.WorkflowResponseEnum;
import com.chennian.storytelling.service.WorkflowService;
import com.chennian.storytelling.bean.dto.WorkflowBatchOperationDTO;
import com.chennian.storytelling.bean.dto.WorkflowHistoryDTO;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.flowable.engine.runtime.ProcessInstance;
import org.flowable.task.api.Task;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 工作流实例管理控制器
 * 专门处理流程实例的启动、查询、操作和监控
 * 
 * @author chennian
 */
@Api(tags = "工作流实例管理")
@RestController
@RequestMapping("/sys/workflow/instances")
@RequiredArgsConstructor
@Slf4j
public class WorkflowInstanceController {

    private final WorkflowService workflowService;

    /**
     * 启动流程实例
     * 需要基本用户权限
     */
    @ApiOperation("启动流程实例")
    @PostMapping("/start")
    @PreAuthorize("hasRole('USER') or hasAuthority('workflow:start')")
    @CacheEvict(value = "workflowStats", allEntries = true)
    public ServerResponseEntity<Map<String, Object>> startProcess(
            @RequestParam("processKey") String processKey,
            @RequestParam("businessKey") String businessKey,
            @RequestParam("businessType") String businessType,
            @RequestParam("title") String title,
            @RequestBody(required = false) Map<String, Object> variables) {
        
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        String startUserId = auth.getName();
        
        log.info("用户 {} 启动流程实例，流程键: {}, 业务键: {}", startUserId, processKey, businessKey);
        
        try {
            ProcessInstance processInstance = workflowService.startProcess(
                processKey, businessKey, businessType, title, variables);
            
            Map<String, Object> result = new HashMap<>();
            result.put("success", true);
            result.put("message", "流程启动成功");
            result.put("processInstanceId", processInstance.getId());
            result.put("processDefinitionId", processInstance.getProcessDefinitionId());
            result.put("businessKey", processInstance.getBusinessKey());
            result.put("startUserId", startUserId);
            
            log.info("流程实例启动成功，实例ID: {}", processInstance.getId());
            return ServerResponseEntity.success(result);
            
        } catch (Exception e) {
            log.error("启动流程实例失败: {}", e.getMessage(), e);
            return ServerResponseEntity.fail(WorkflowResponseEnum.PROCESS_START_FAILED);
        }
    }

    /**
     * 高级流程实例查询
     * 支持多条件查询和缓存
     */
    @ApiOperation("高级流程实例查询")
    @GetMapping("/search")
    @PreAuthorize("hasRole('USER') or hasAuthority('workflow:read')")
    @Cacheable(value = "processInstances", key = "#processDefinitionKey + '_' + #businessKey + '_' + #startUserId + '_' + #status + '_' + #page + '_' + #size")
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
        
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        String currentUserId = auth.getName();
        
        log.debug("用户 {} 查询流程实例，条件: processKey={}, businessKey={}, status={}", 
                 currentUserId, processDefinitionKey, businessKey, status);
        
        // 非管理员只能查看自己相关的流程实例
        if (!hasAdminRole(auth) && startUserId == null) {
            startUserId = currentUserId;
        }
        
        Map<String, Object> searchParams = new HashMap<>();
        searchParams.put("processDefinitionKey", processDefinitionKey);
        searchParams.put("businessKey", businessKey);
        searchParams.put("startUserId", startUserId);
        searchParams.put("status", status);
        searchParams.put("startTime", startTime);
        searchParams.put("endTime", endTime);
        searchParams.put("page", page);
        searchParams.put("size", size);
        
        Map<String, Object> result = workflowService.searchProcessInstances(searchParams);
        return ServerResponseEntity.success(result);
    }

    /**
     * 获取流程实例详情
     * 支持缓存
     */
    @ApiOperation("获取流程实例详情")
    @GetMapping("/{processInstanceId}/detail")
    @PreAuthorize("hasRole('USER') or hasAuthority('workflow:read')")
    @Cacheable(value = "processInstanceDetail", key = "#processInstanceId")
    public ServerResponseEntity<Map<String, Object>> getProcessDetail(
            @PathVariable("processInstanceId") String processInstanceId) {
        
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        String currentUserId = auth.getName();
        
        log.debug("用户 {} 获取流程实例详情，实例ID: {}", currentUserId, processInstanceId);
        
        try {
            // 权限检查：非管理员只能查看自己相关的流程实例
            if (!hasAdminRole(auth) && !workflowService.isUserRelatedToProcess(processInstanceId, currentUserId)) {
                return ServerResponseEntity.fail(WorkflowResponseEnum.PERMISSION_DENIED);
            }
            
            Map<String, Object> detail = new HashMap<>();
            
            // 获取流程历史
            WorkflowHistoryDTO history = workflowService.getProcessHistory(processInstanceId);
            detail.put("history", history);
            
            // 获取流程变量
            Map<String, Object> variables = workflowService.getProcessVariables(processInstanceId);
            detail.put("variables", variables);
            
            // 获取当前任务
            List<Task> currentTasks = workflowService.findTasksByProcessInstanceId(processInstanceId);
            detail.put("currentTasks", currentTasks);
            
            // 获取基本信息
            Map<String, Object> basicInfo = workflowService.getProcessInstanceBasicInfo(processInstanceId);
            detail.put("basicInfo", basicInfo);
            
            return ServerResponseEntity.success(detail);
            
        } catch (Exception e) {
            log.error("获取流程实例详情失败: {}", e.getMessage(), e);
            return ServerResponseEntity.fail(WorkflowResponseEnum.PROCESS_DETAIL_ERROR);
        }
    }

    /**
     * 获取流程图
     */
    @ApiOperation("获取流程图")
    @GetMapping("/{processInstanceId}/diagram")
    @PreAuthorize("hasRole('USER') or hasAuthority('workflow:read')")
    public ResponseEntity<byte[]> getProcessDiagram(
            @PathVariable("processInstanceId") String processInstanceId) {
        
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        String currentUserId = auth.getName();
        
        log.debug("用户 {} 获取流程图，实例ID: {}", currentUserId, processInstanceId);
        
        try {
            // 权限检查
            if (!hasAdminRole(auth) && !workflowService.isUserRelatedToProcess(processInstanceId, currentUserId)) {
                return new ResponseEntity<>(HttpStatus.FORBIDDEN);
            }
            
            byte[] bytes = workflowService.getProcessDiagram(processInstanceId);
            if (bytes == null) {
                return new ResponseEntity<>(HttpStatus.NOT_FOUND);
            }
            
            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.IMAGE_PNG);
            headers.setContentDispositionFormData("attachment", "process-diagram.png");
            
            return new ResponseEntity<>(bytes, headers, HttpStatus.OK);
            
        } catch (Exception e) {
            log.error("获取流程图失败: {}", e.getMessage(), e);
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    /**
     * 终止流程实例
     * 需要管理员权限或流程发起人权限
     */
    @ApiOperation("终止流程实例")
    @PostMapping("/{processInstanceId}/terminate")
    @PreAuthorize("hasRole('ADMIN') or hasAuthority('workflow:terminate')")
    @CacheEvict(value = {"processInstances", "processInstanceDetail", "workflowStats"}, allEntries = true)
    public ServerResponseEntity<Map<String, Object>> terminateProcess(
            @PathVariable("processInstanceId") String processInstanceId,
            @RequestParam("reason") String reason) {
        
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        String currentUserId = auth.getName();
        
        log.info("用户 {} 终止流程实例，实例ID: {}, 原因: {}", currentUserId, processInstanceId, reason);
        
        try {
            // 权限检查：管理员或流程发起人可以终止
            if (!hasAdminRole(auth) && !workflowService.isProcessStarter(processInstanceId, currentUserId)) {
                return ServerResponseEntity.fail(WorkflowResponseEnum.PERMISSION_DENIED);
            }
            
            workflowService.terminateProcessInstance(processInstanceId, reason);
            
            Map<String, Object> result = new HashMap<>();
            result.put("success", true);
            result.put("message", "流程终止成功");
            result.put("processInstanceId", processInstanceId);
            result.put("reason", reason);
            result.put("terminatedBy", currentUserId);
            
            log.info("流程实例终止成功，实例ID: {}", processInstanceId);
            return ServerResponseEntity.success(result);
            
        } catch (Exception e) {
            log.error("终止流程实例失败: {}", e.getMessage(), e);
            return ServerResponseEntity.fail(WorkflowResponseEnum.PROCESS_TERMINATE_FAILED);
        }
    }

    /**
     * 挂起流程实例
     */
    @ApiOperation("挂起流程实例")
    @PostMapping("/{processInstanceId}/suspend")
    @PreAuthorize("hasRole('ADMIN') or hasAuthority('workflow:manage')")
    @CacheEvict(value = {"processInstances", "processInstanceDetail"}, allEntries = true)
    public ServerResponseEntity<Map<String, Object>> suspendProcess(
            @PathVariable("processInstanceId") String processInstanceId) {
        
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        String currentUserId = auth.getName();
        
        log.info("用户 {} 挂起流程实例，实例ID: {}", currentUserId, processInstanceId);
        
        try {
            workflowService.suspendProcessInstance(processInstanceId);
            
            Map<String, Object> result = new HashMap<>();
            result.put("success", true);
            result.put("message", "流程挂起成功");
            result.put("processInstanceId", processInstanceId);
            result.put("suspendedBy", currentUserId);
            
            return ServerResponseEntity.success(result);
            
        } catch (Exception e) {
            log.error("挂起流程实例失败: {}", e.getMessage(), e);
            return ServerResponseEntity.fail(WorkflowResponseEnum.PROCESS_SUSPEND_FAILED);
        }
    }

    /**
     * 激活流程实例
     */
    @ApiOperation("激活流程实例")
    @PostMapping("/{processInstanceId}/activate")
    @PreAuthorize("hasRole('ADMIN') or hasAuthority('workflow:manage')")
    @CacheEvict(value = {"processInstances", "processInstanceDetail"}, allEntries = true)
    public ServerResponseEntity<Map<String, Object>> activateProcess(
            @PathVariable("processInstanceId") String processInstanceId) {
        
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        String currentUserId = auth.getName();
        
        log.info("用户 {} 激活流程实例，实例ID: {}", currentUserId, processInstanceId);
        
        try {
            workflowService.activateProcessInstance(processInstanceId);
            
            Map<String, Object> result = new HashMap<>();
            result.put("success", true);
            result.put("message", "流程激活成功");
            result.put("processInstanceId", processInstanceId);
            result.put("activatedBy", currentUserId);
            
            return ServerResponseEntity.success(result);
            
        } catch (Exception e) {
            log.error("激活流程实例失败: {}", e.getMessage(), e);
            return ServerResponseEntity.fail(WorkflowResponseEnum.PROCESS_ACTIVATE_FAILED);
        }
    }

    /**
     * 批量操作流程实例
     */
    @ApiOperation("批量操作流程实例")
    @PostMapping("/batch")
    @PreAuthorize("hasRole('ADMIN') or hasAuthority('workflow:batch')")
    @CacheEvict(value = {"processInstances", "processInstanceDetail", "workflowStats"}, allEntries = true)
    public ServerResponseEntity<WorkflowBatchOperationDTO.BatchOperationResult> batchOperateProcesses(
            @RequestBody WorkflowBatchOperationDTO batchOperation) {
        
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        String currentUserId = auth.getName();
        
        log.info("用户 {} 执行批量操作，操作类型: {}, 实例数量: {}", 
                currentUserId, batchOperation.getOperationType(), batchOperation.getProcessInstanceIds().size());
        
        try {
            WorkflowBatchOperationDTO.BatchOperationResult result = 
                workflowService.batchOperateProcessInstances(batchOperation);
            
            log.info("批量操作完成，成功: {}, 失败: {}", result.getSuccessCount(), result.getFailureCount());
            return ServerResponseEntity.success(result);
            
        } catch (Exception e) {
            log.error("批量操作失败: {}", e.getMessage(), e);
            return ServerResponseEntity.fail(WorkflowResponseEnum.BATCH_OPERATION_FAILED);
        }
    }

    /**
     * 获取流程变量
     */
    @ApiOperation("获取流程变量")
    @GetMapping("/{processInstanceId}/variables")
    @PreAuthorize("hasRole('USER') or hasAuthority('workflow:read')")
    public ServerResponseEntity<Map<String, Object>> getProcessVariables(
            @PathVariable("processInstanceId") String processInstanceId) {
        
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        String currentUserId = auth.getName();
        
        log.debug("用户 {} 获取流程变量，实例ID: {}", currentUserId, processInstanceId);
        
        try {
            // 权限检查
            if (!hasAdminRole(auth) && !workflowService.isUserRelatedToProcess(processInstanceId, currentUserId)) {
                return ServerResponseEntity.fail(WorkflowResponseEnum.PROCESS_PERMISSION_DENIED);
            }
            
            Map<String, Object> variables = workflowService.getProcessVariables(processInstanceId);
            
            Map<String, Object> result = new HashMap<>();
            result.put("processInstanceId", processInstanceId);
            result.put("variables", variables);
            
            return ServerResponseEntity.success(result);
            
        } catch (Exception e) {
            log.error("获取流程变量失败: {}", e.getMessage(), e);
            return ServerResponseEntity.fail(WorkflowResponseEnum.PROCESS_VARIABLE_ERROR);
        }
    }

    /**
     * 设置流程变量
     */
    @ApiOperation("设置流程变量")
    @PostMapping("/{processInstanceId}/variables")
    @PreAuthorize("hasRole('ADMIN') or hasAuthority('workflow:manage')")
    @CacheEvict(value = "processInstanceDetail", key = "#processInstanceId")
    public ServerResponseEntity<Map<String, Object>> setProcessVariables(
            @PathVariable("processInstanceId") String processInstanceId,
            @RequestBody Map<String, Object> variables) {
        
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        String currentUserId = auth.getName();
        
        log.info("用户 {} 设置流程变量，实例ID: {}, 变量数量: {}", 
                currentUserId, processInstanceId, variables.size());
        
        try {
            // 批量设置流程变量
            for (Map.Entry<String, Object> entry : variables.entrySet()) {
                workflowService.setProcessVariable(processInstanceId, entry.getKey(), entry.getValue());
            }
            
            Map<String, Object> result = new HashMap<>();
            result.put("success", true);
            result.put("message", "流程变量设置成功");
            result.put("processInstanceId", processInstanceId);
            result.put("variableCount", variables.size());
            result.put("updatedBy", currentUserId);
            
            return ServerResponseEntity.success(result);
            
        } catch (Exception e) {
            log.error("设置流程变量失败: {}", e.getMessage(), e);
            return ServerResponseEntity.fail(WorkflowResponseEnum.PROCESS_VARIABLE_ERROR);
        }
    }

    /**
     * 检查用户是否具有管理员角色
     */
    private boolean hasAdminRole(Authentication auth) {
        return auth.getAuthorities().stream()
                .anyMatch(authority -> 
                    authority.getAuthority().equals("ROLE_ADMIN") || 
                    authority.getAuthority().equals("workflow:admin"));
    }
}