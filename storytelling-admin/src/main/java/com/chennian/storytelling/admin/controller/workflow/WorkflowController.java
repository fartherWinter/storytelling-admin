package com.chennian.storytelling.admin.controller.workflow;

import java.io.IOException;
import java.io.InputStream;
import java.util.List;
import java.util.Map;

import org.flowable.engine.RepositoryService;
import org.flowable.engine.repository.ProcessDefinition;
import org.flowable.engine.runtime.ProcessInstance;
import org.flowable.task.api.Task;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.chennian.storytelling.workflow.domain.ProcessDefinitionDTO;
import com.chennian.storytelling.service.WorkflowService;


/**
 * 工作流控制器
 * 
 * @author chennian
 */
@RestController
@RequestMapping("/workflow")
public class WorkflowController {

    private final WorkflowService workflowService;

    private final RepositoryService repositoryService;

    public WorkflowController(WorkflowService workflowService, RepositoryService repositoryService) {
        this.workflowService = workflowService;
        this.repositoryService = repositoryService;
    }
    
    /**
     * 部署流程定义
     */
    @PostMapping("/deploy")
    public String deployProcess(@RequestBody ProcessDefinitionDTO processDefinition) {
        return workflowService.deployProcess(
                processDefinition.getName(),
                processDefinition.getCategory(),
                processDefinition.getResourceName(),
                processDefinition.getDeploymentFile());
    }
    
    /**
     * 启动流程实例
     */
    @PostMapping("/start")
    public String startProcess(@RequestParam("processKey") String processKey,
                             @RequestParam("businessKey") String businessKey,
                             @RequestParam("businessType") String businessType,
                             @RequestParam("title") String title,
                             @RequestBody(required = false) Map<String, Object> variables) {
        ProcessInstance processInstance = workflowService.startProcess(processKey, businessKey, businessType, title, variables);
        return processInstance.getId();
    }
    
    /**
     * 查询待办任务
     */
    @GetMapping("/tasks")
    public List<Task> findTodoTasks(@RequestParam("assignee") String assignee) {
        return workflowService.findTodoTasks(assignee);
    }
    
    /**
     * 查询流程任务
     */
    @GetMapping("/process/{processInstanceId}/tasks")
    public List<Task> findTasksByProcessInstanceId(@PathVariable("processInstanceId") String processInstanceId) {
        return workflowService.findTasksByProcessInstanceId(processInstanceId);
    }
    
    /**
     * 完成任务
     */
    @PostMapping("/tasks/{taskId}/complete")
    public void completeTask(@PathVariable("taskId") String taskId,
                           @RequestParam(value = "comment", required = false) String comment,
                           @RequestBody(required = false) Map<String, Object> variables) {
        workflowService.completeTask(taskId, variables, comment);
    }
    
    /**
     * 审批通过任务
     */
    @PostMapping("/tasks/{taskId}/approve")
    public void approveTask(@PathVariable("taskId") String taskId,
                          @RequestParam(value = "comment", required = false) String comment) {
        workflowService.approveTask(taskId, comment);
    }
    
    /**
     * 拒绝任务
     */
    @PostMapping("/tasks/{taskId}/reject")
    public void rejectTask(@PathVariable("taskId") String taskId,
                         @RequestParam(value = "comment", required = false) String comment) {
        workflowService.rejectTask(taskId, comment);
    }
    
    /**
     * 获取流程图
     */
    @GetMapping("/process/{processInstanceId}/diagram")
    public ResponseEntity<byte[]> getProcessDiagram(@PathVariable("processInstanceId") String processInstanceId) {
        byte[] bytes = workflowService.getProcessDiagram(processInstanceId);
        if (bytes == null) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.IMAGE_PNG);
        
        return new ResponseEntity<>(bytes, headers, HttpStatus.OK);
    }
    
    /**
     * 终止流程实例
     */
    @DeleteMapping("/process/{processInstanceId}")
    public void terminateProcessInstance(@PathVariable("processInstanceId") String processInstanceId,
                                       @RequestParam("reason") String reason) {
        workflowService.terminateProcessInstance(processInstanceId, reason);
    }
    
    /**
     * 获取流程变量
     */
    @GetMapping("/process/{processInstanceId}/variables/{variableName}")
    public Object getProcessVariable(@PathVariable("processInstanceId") String processInstanceId,
                                   @PathVariable("variableName") String variableName) {
        return workflowService.getProcessVariable(processInstanceId, variableName);
    }
    
    /**
     * 设置流程变量
     */
    @PostMapping("/process/{processInstanceId}/variables/{variableName}")
    public void setProcessVariable(@PathVariable("processInstanceId") String processInstanceId,
                                 @PathVariable("variableName") String variableName,
                                 @RequestBody Object value) {
        workflowService.setProcessVariable(processInstanceId, variableName, value);
    }
    
    /**
     * 获取流程定义列表
     */
    @GetMapping("/process-definitions")
    public List<ProcessDefinition> getProcessDefinitions(
            @RequestParam(value = "key", required = false) String key,
            @RequestParam(value = "category", required = false) String category,
            @RequestParam(value = "latest", required = false, defaultValue = "true") boolean latest) {
        
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
        
        return query.list();
    }
    
    /**
     * 获取流程定义详情
     */
    @GetMapping("/process-definitions/{processDefinitionId}")
    public ProcessDefinition getProcessDefinition(@PathVariable("processDefinitionId") String processDefinitionId) {
        return repositoryService.createProcessDefinitionQuery()
                .processDefinitionId(processDefinitionId)
                .singleResult();
    }
    
    /**
     * 获取流程定义资源
     */
    @GetMapping("/process-definitions/{processDefinitionId}/resource")
    public ResponseEntity<byte[]> getProcessDefinitionResource(
            @PathVariable("processDefinitionId") String processDefinitionId,
            @RequestParam(value = "resourceType", defaultValue = "xml") String resourceType) {
        
        // 获取流程定义
        ProcessDefinition processDefinition = repositoryService.createProcessDefinitionQuery()
                .processDefinitionId(processDefinitionId)
                .singleResult();
        
        if (processDefinition == null) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        
        // 根据资源类型获取不同的资源
        byte[] resourceBytes = null;
        String resourceName;
        MediaType mediaType;
        
        try {
            if ("diagram".equals(resourceType)) {
                // 获取流程图
                // 检查是否存在图资源
                if (processDefinition.getDiagramResourceName() == null) {
                    return new ResponseEntity<>(HttpStatus.NOT_FOUND);
                }
                
                resourceBytes = repositoryService.getResourceAsStream(
                        processDefinition.getDeploymentId(), 
                        processDefinition.getDiagramResourceName())
                        .readAllBytes();
                        
                resourceName = processDefinition.getDiagramResourceName();
                mediaType = MediaType.IMAGE_PNG;
            } else {
                // 获取XML资源
                if (processDefinition.getResourceName() == null) {
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
        } catch (IOException e) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
        
        if (resourceBytes == null || resourceBytes.length == 0) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(mediaType);
        headers.setContentDispositionFormData("attachment", resourceName);
        
        return new ResponseEntity<>(resourceBytes, headers, HttpStatus.OK);
    }
}