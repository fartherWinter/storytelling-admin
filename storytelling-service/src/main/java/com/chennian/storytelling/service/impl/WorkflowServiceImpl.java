package com.chennian.storytelling.service.impl;

import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import com.chennian.storytelling.bean.dto.*;
import org.flowable.bpmn.model.BpmnModel;
import org.flowable.engine.HistoryService;
import org.flowable.engine.ProcessEngine;
import org.flowable.engine.RepositoryService;
import org.flowable.engine.RuntimeService;
import org.flowable.engine.TaskService;
import org.flowable.engine.history.HistoricActivityInstance;
import org.flowable.engine.history.HistoricProcessInstance;
import org.flowable.engine.repository.Deployment;
import org.flowable.engine.repository.ProcessDefinition;
import org.flowable.engine.runtime.ProcessInstance;
import org.flowable.image.ProcessDiagramGenerator;
import org.flowable.task.api.Task;
import org.flowable.task.api.history.HistoricTaskInstance;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.chennian.storytelling.service.WorkflowService;

/**
 * 工作流服务实现类
 * 
 * @author chennian
 */
@Service
public class WorkflowServiceImpl implements WorkflowService {

    private final ProcessEngine processEngine;

    private final RepositoryService repositoryService;

    private final RuntimeService runtimeService;

    private final TaskService taskService;

    private final HistoryService historyService;

    public WorkflowServiceImpl(ProcessEngine processEngine, RepositoryService repositoryService, RuntimeService runtimeService, TaskService taskService, HistoryService historyService) {
        this.processEngine = processEngine;
        this.repositoryService = repositoryService;
        this.runtimeService = runtimeService;
        this.taskService = taskService;
        this.historyService = historyService;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public String deployProcess(String name, String category, String resourceName, String deploymentFile) {
        Deployment deployment = repositoryService.createDeployment()
                .name(name)
                .category(category)
                .addString(resourceName, deploymentFile)
                .deploy();
        
        // 获取流程定义
        ProcessDefinition processDefinition = repositoryService.createProcessDefinitionQuery()
                .deploymentId(deployment.getId())
                .singleResult();
        
        return processDefinition.getId();
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public ProcessInstance startProcess(String processKey, String businessKey, String businessType, String title,
            Map<String, Object> variables) {
        // 设置流程变量
        if (variables == null) {
            variables = new HashMap<>();
        }
        variables.put("businessType", businessType);
        variables.put("title", title);
        
        // 启动流程实例
        ProcessInstance processInstance = runtimeService.startProcessInstanceByKey(processKey, businessKey, variables);
        return processInstance;
    }

    @Override
    public List<Task> findTodoTasks(String assignee) {
        return taskService.createTaskQuery()
                .taskAssignee(assignee)
                .orderByTaskCreateTime()
                .desc()
                .list();
    }

    @Override
    public List<Task> findTasksByProcessInstanceId(String processInstanceId) {
        return taskService.createTaskQuery()
                .processInstanceId(processInstanceId)
                .orderByTaskCreateTime()
                .desc()
                .list();
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void completeTask(String taskId, Map<String, Object> variables, String comment) {
        // 添加审批意见
        if (comment != null && !comment.isEmpty()) {
            Task task = taskService.createTaskQuery().taskId(taskId).singleResult();
            taskService.addComment(taskId, task.getProcessInstanceId(), comment);
        }
        
        // 完成任务
        if (variables != null) {
            taskService.complete(taskId, variables);
        } else {
            taskService.complete(taskId);
        }
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void approveTask(String taskId, String comment) {
        Map<String, Object> variables = new HashMap<>();
        variables.put("approved", true);
        completeTask(taskId, variables, comment);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void rejectTask(String taskId, String comment) {
        Map<String, Object> variables = new HashMap<>();
        variables.put("approved", false);
        completeTask(taskId, variables, comment);
    }

    @Override
    public ProcessInstance getProcessInstance(String processInstanceId) {
        return runtimeService.createProcessInstanceQuery()
                .processInstanceId(processInstanceId)
                .singleResult();
    }

    @Override
    public ProcessInstance getProcessInstanceByBusinessKey(String businessKey) {
        return runtimeService.createProcessInstanceQuery()
                .processInstanceBusinessKey(businessKey)
                .singleResult();
    }

    @Override
    public Object getProcessVariable(String processInstanceId, String variableName) {
        return runtimeService.getVariable(processInstanceId, variableName);
    }

    @Override
    public void setProcessVariable(String processInstanceId, String variableName, Object value) {
        runtimeService.setVariable(processInstanceId, variableName, value);
    }

    @Override
    public byte[] getProcessDiagram(String processInstanceId) {
        ProcessInstance processInstance = getProcessInstance(processInstanceId);
        if (processInstance == null) {
            // 查询历史流程实例
            HistoricProcessInstance historicProcessInstance = historyService.createHistoricProcessInstanceQuery()
                    .processInstanceId(processInstanceId)
                    .singleResult();
            if (historicProcessInstance == null) {
                return null;
            }
        }
        
        // 获取流程定义
        BpmnModel bpmnModel = repositoryService.getBpmnModel(processInstance.getProcessDefinitionId());
        ProcessDiagramGenerator diagramGenerator = processEngine.getProcessEngineConfiguration().getProcessDiagramGenerator();
        // 获取历史服务实例
        HistoryService historyService = processEngine.getHistoryService();

        // 查询所有已执行的流程连线（sequenceFlow）
        List<String> highLightedFlows = historyService.createHistoricActivityInstanceQuery()
                .processInstanceId(processInstanceId)
                // 过滤类型为流程连线
                .activityType("sequenceFlow")
                .list()
                .stream()
                // 提取连线ID
                .map(HistoricActivityInstance::getActivityId)
                .toList();
//        // 提取所有流程连线ID（备用）
//        List<String> allFlowIds = bpmnModel.getMainProcess().getFlowElements().stream()
//                .filter(element -> element instanceof SequenceFlow)
//                .map(element -> (SequenceFlow) element)
//                .map(SequenceFlow::getId)
//                .toList();
//        // 结合历史数据过滤已执行的连线（需使用方法1的结果）
//        List<String> nowHighLightedFlows = allFlowIds.stream()
//                .filter(flowId -> highLightedFlows.contains(flowId)) // historyFlowIds 为方法1的结果
//                .collect(Collectors.toList());
        // 自动关闭 outputStream
        try (ByteArrayOutputStream outputStream = new ByteArrayOutputStream()) {
            // 获取流程活动节点和连线
            List<String> activeActivityIds = runtimeService.getActiveActivityIds(processInstanceId);

            // 生成流程图并自动关闭 InputStream
            try (InputStream imageStream = diagramGenerator.generateDiagram(
                    bpmnModel,
                    "png",
                    activeActivityIds,
                    highLightedFlows,
                    processEngine.getProcessEngineConfiguration().getActivityFontName(),
                    processEngine.getProcessEngineConfiguration().getLabelFontName(),
                    processEngine.getProcessEngineConfiguration().getAnnotationFontName(),
                    null,
                    1.0,
                    false)) {

                // 将图片流写入字节数组输出流
                imageStream.transferTo(outputStream);
                return outputStream.toByteArray();
            }
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }

    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void terminateProcessInstance(String processInstanceId, String reason) {
        runtimeService.deleteProcessInstance(processInstanceId, reason);
    }

    @Override
    public WorkflowStatisticsDTO getWorkflowStatistics(LocalDateTime startTime, LocalDateTime endTime) {
        WorkflowStatisticsDTO statistics = new WorkflowStatisticsDTO(startTime, endTime);
        
        Date start = startTime != null ? Date.from(startTime.atZone(ZoneId.systemDefault()).toInstant()) : null;
        Date end = endTime != null ? Date.from(endTime.atZone(ZoneId.systemDefault()).toInstant()) : null;
        
        // 统计流程实例
        long totalProcessInstances = historyService.createHistoricProcessInstanceQuery()
                .startedAfter(start)
                .startedBefore(end)
                .count();
        statistics.setTotalProcessInstances(totalProcessInstances);
        
        long runningProcessInstances = runtimeService.createProcessInstanceQuery().count();
        statistics.setRunningProcessInstances(runningProcessInstances);
        
        long completedProcessInstances = historyService.createHistoricProcessInstanceQuery()
                .finished()
                .startedAfter(start)
                .startedBefore(end)
                .count();
        statistics.setCompletedProcessInstances(completedProcessInstances);
        
        // 统计任务
        long totalTasks = historyService.createHistoricTaskInstanceQuery()
                .taskCreatedAfter(start)
                .taskCreatedBefore(end)
                .count();
        statistics.setTotalTasks(totalTasks);
        
        long pendingTasks = taskService.createTaskQuery().count();
        statistics.setPendingTasks(pendingTasks);
        
        long completedTasks = historyService.createHistoricTaskInstanceQuery()
                .finished()
                .taskCreatedAfter(start)
                .taskCreatedBefore(end)
                .count();
        statistics.setCompletedTasks(completedTasks);
        
        // 计算完成率
        if (totalProcessInstances > 0) {
            statistics.setCompletionRate((double) completedProcessInstances / totalProcessInstances * 100);
        }
        
        if (totalTasks > 0) {
            statistics.setTaskCompletionRate((double) completedTasks / totalTasks * 100);
        }
        
        // 按流程定义分组统计
        List<HistoricProcessInstance> processInstances = historyService.createHistoricProcessInstanceQuery()
                .startedAfter(start)
                .startedBefore(end)
                .list();
        
        Map<String, Long> processDefinitionStats = processInstances.stream()
                .collect(Collectors.groupingBy(
                    HistoricProcessInstance::getProcessDefinitionKey,
                    Collectors.counting()
                ));
        statistics.setProcessDefinitionStats(processDefinitionStats);
        
        return statistics;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public WorkflowBatchOperationDTO.BatchOperationResult batchOperateTasks(WorkflowBatchOperationDTO batchOperation) {
        List<String> taskIds = batchOperation.getTaskIds();
        WorkflowBatchOperationDTO.BatchOperationResult result = new WorkflowBatchOperationDTO.BatchOperationResult(taskIds.size());
        Map<String, String> failureDetails = new HashMap<>();
        
        for (String taskId : taskIds) {
            try {
                switch (batchOperation.getOperationType()) {
                    case APPROVE:
                        approveTask(taskId, batchOperation.getComment());
                        break;
                    case REJECT:
                        rejectTask(taskId, batchOperation.getComment());
                        break;
                    case COMPLETE:
                        completeTask(taskId, batchOperation.getVariables(), batchOperation.getComment());
                        break;
                    case ASSIGN:
                        taskService.setAssignee(taskId, batchOperation.getAssignee());
                        break;
                    case CLAIM:
                        claimTask(taskId, batchOperation.getOperator());
                        break;
                    default:
                        throw new IllegalArgumentException("不支持的操作类型: " + batchOperation.getOperationType());
                }
                result.incrementSuccess();
            } catch (Exception e) {
                result.incrementFailure();
                failureDetails.put(taskId, e.getMessage());
            }
        }
        
        result.setFailureDetails(failureDetails);
        return result;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public WorkflowBatchOperationDTO.BatchOperationResult batchOperateProcessInstances(WorkflowBatchOperationDTO batchOperation) {
        List<String> processInstanceIds = batchOperation.getProcessInstanceIds();
        WorkflowBatchOperationDTO.BatchOperationResult result = new WorkflowBatchOperationDTO.BatchOperationResult(processInstanceIds.size());
        Map<String, String> failureDetails = new HashMap<>();
        
        for (String processInstanceId : processInstanceIds) {
            try {
                switch (batchOperation.getOperationType()) {
                    case TERMINATE:
                        terminateProcessInstance(processInstanceId, batchOperation.getComment());
                        break;
                    default:
                        throw new IllegalArgumentException("不支持的流程实例操作类型: " + batchOperation.getOperationType());
                }
                result.incrementSuccess();
            } catch (Exception e) {
                result.incrementFailure();
                failureDetails.put(processInstanceId, e.getMessage());
            }
        }
        
        result.setFailureDetails(failureDetails);
        return result;
    }

    @Override
    public WorkflowHistoryDTO getProcessHistory(String processInstanceId) {
        HistoricProcessInstance historicProcessInstance = historyService.createHistoricProcessInstanceQuery()
                .processInstanceId(processInstanceId)
                .singleResult();
        
        if (historicProcessInstance == null) {
            return null;
        }
        
        WorkflowHistoryDTO history = new WorkflowHistoryDTO();
        history.setProcessInstanceId(processInstanceId);
        history.setProcessDefinitionId(historicProcessInstance.getProcessDefinitionId());
        history.setProcessDefinitionKey(historicProcessInstance.getProcessDefinitionKey());
        history.setProcessDefinitionName(historicProcessInstance.getProcessDefinitionName());
        history.setBusinessKey(historicProcessInstance.getBusinessKey());
        history.setStartTime(historicProcessInstance.getStartTime().toInstant().atZone(ZoneId.systemDefault()).toLocalDateTime());
        
        if (historicProcessInstance.getEndTime() != null) {
            history.setEndTime(historicProcessInstance.getEndTime().toInstant().atZone(ZoneId.systemDefault()).toLocalDateTime());
            history.setDuration(historicProcessInstance.getDurationInMillis());
        }
        
        history.setStartUserId(historicProcessInstance.getStartUserId());
        history.setDeleteReason(historicProcessInstance.getDeleteReason());
        
        // 获取历史任务
        List<HistoricTaskInstance> historicTasks = historyService.createHistoricTaskInstanceQuery()
                .processInstanceId(processInstanceId)
                .orderByHistoricTaskInstanceStartTime()
                .asc()
                .list();
        
        List<HistoricTaskDTO> taskInfos = new ArrayList<>();
        for (HistoricTaskInstance task : historicTasks) {
            HistoricTaskDTO taskInfo = new HistoricTaskDTO();
            taskInfo.setId(task.getId());
            taskInfo.setName(task.getName());
            taskInfo.setTaskDefinitionKey(task.getTaskDefinitionKey());
            taskInfo.setAssignee(task.getAssignee());
            taskInfo.setStartTime(task.getStartTime().toInstant().atZone(ZoneId.systemDefault()).toLocalDateTime());
            
            if (task.getEndTime() != null) {
                taskInfo.setEndTime(task.getEndTime().toInstant().atZone(ZoneId.systemDefault()).toLocalDateTime());
                taskInfo.setDuration(task.getDurationInMillis());
            }
            
            taskInfo.setDeleteReason(task.getDeleteReason());
            taskInfos.add(taskInfo);
        }
        history.setHistoricTasks(taskInfos);
        
        return history;
    }

    @Override
    public List<TaskDTO> findHistoryTasks(String assignee, LocalDateTime startTime, LocalDateTime endTime) {
        Date start = startTime != null ? Date.from(startTime.atZone(ZoneId.systemDefault()).toInstant()) : null;
        Date end = endTime != null ? Date.from(endTime.atZone(ZoneId.systemDefault()).toInstant()) : null;
        
        List<HistoricTaskInstance> historicTasks = historyService.createHistoricTaskInstanceQuery()
                .taskAssignee(assignee)
                .taskCreatedAfter(start)
                .taskCreatedBefore(end)
                .orderByHistoricTaskInstanceStartTime()
                .desc()
                .list();
        
        List<TaskDTO> taskDTOs = new ArrayList<>();
        for (HistoricTaskInstance task : historicTasks) {
            TaskDTO taskDTO = new TaskDTO();
            taskDTO.setTaskId(task.getId());
            taskDTO.setTaskName(task.getName());
            taskDTO.setTaskKey(task.getTaskDefinitionKey());
            taskDTO.setProcessInstanceId(task.getProcessInstanceId());
            taskDTO.setProcessDefinitionId(task.getProcessDefinitionId());
            taskDTO.setAssignee(task.getAssignee());
            taskDTO.setCreateTime(Date.from(task.getStartTime().toInstant()));
            
            taskDTOs.add(taskDTO);
        }
        
        return taskDTOs;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void delegateTask(String taskId, String assignee, String comment) {
        if (comment != null && !comment.isEmpty()) {
            Task task = taskService.createTaskQuery().taskId(taskId).singleResult();
            taskService.addComment(taskId, task.getProcessInstanceId(), comment);
        }
        taskService.delegateTask(taskId, assignee);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void claimTask(String taskId, String assignee) {
        taskService.claim(taskId, assignee);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void transferTask(String taskId, String assignee, String comment) {
        if (comment != null && !comment.isEmpty()) {
            Task task = taskService.createTaskQuery().taskId(taskId).singleResult();
            taskService.addComment(taskId, task.getProcessInstanceId(), comment);
        }
        taskService.setAssignee(taskId, assignee);
    }

    @Override
    public List<ProcessDefinitionDTO> listProcessDefinitions(String category, String key, String name) {
        org.flowable.engine.repository.ProcessDefinitionQuery query = repositoryService.createProcessDefinitionQuery()
                .latestVersion()
                .orderByProcessDefinitionName()
                .asc();
        
        if (category != null && !category.isEmpty()) {
            query.processDefinitionCategory(category);
        }
        
        if (key != null && !key.isEmpty()) {
            query.processDefinitionKey(key);
        }
        
        if (name != null && !name.isEmpty()) {
            query.processDefinitionNameLike("%" + name + "%");
        }
        
        List<ProcessDefinition> processDefinitions = query.list();
        List<ProcessDefinitionDTO> dtos = new ArrayList<>();
        
        for (ProcessDefinition pd : processDefinitions) {
            ProcessDefinitionDTO dto = new ProcessDefinitionDTO();
            dto.setId(pd.getId());
            dto.setKey(pd.getKey());
            dto.setName(pd.getName());
            dto.setCategory(pd.getCategory());
            dto.setVersion(pd.getVersion());
            dto.setResourceName(pd.getResourceName());
            dto.setDeploymentId(pd.getDeploymentId());
            dto.setDescription(pd.getDescription());
            dto.setSuspended(pd.isSuspended());
            dtos.add(dto);
        }
        
        return dtos;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void suspendProcessDefinition(String processDefinitionId) {
        repositoryService.suspendProcessDefinitionById(processDefinitionId);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void activateProcessDefinition(String processDefinitionId) {
        repositoryService.activateProcessDefinitionById(processDefinitionId);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void suspendProcessInstance(String processInstanceId) {
        runtimeService.suspendProcessInstanceById(processInstanceId);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void activateProcessInstance(String processInstanceId) {
        runtimeService.activateProcessInstanceById(processInstanceId);
    }

    @Override
    public List<ProcessDefinitionDTO> getProcessDefinitions() {
        List<ProcessDefinition> processDefinitions = repositoryService.createProcessDefinitionQuery()
                .latestVersion()
                .list();
        
        List<ProcessDefinitionDTO> dtos = new ArrayList<>();
        for (ProcessDefinition pd : processDefinitions) {
            ProcessDefinitionDTO dto = new ProcessDefinitionDTO();
            dto.setId(pd.getId());
            dto.setKey(pd.getKey());
            dto.setName(pd.getName());
            dto.setCategory(pd.getCategory());
            dto.setVersion(pd.getVersion());
            dto.setResourceName(pd.getResourceName());
            dto.setDeploymentId(pd.getDeploymentId());
            dto.setDescription(pd.getDescription());
            dto.setSuspended(pd.isSuspended());
            dtos.add(dto);
        }
        
        return dtos;
    }

    @Override
    public byte[] getProcessDefinitionResource(String processDefinitionId, String resourceName) {
        try {
            InputStream inputStream = repositoryService.getResourceAsStream(
                    repositoryService.createProcessDefinitionQuery()
                            .processDefinitionId(processDefinitionId)
                            .singleResult()
                            .getDeploymentId(),
                    resourceName);
            
            ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
            byte[] buffer = new byte[1024];
            int length;
            while ((length = inputStream.read(buffer)) != -1) {
                outputStream.write(buffer, 0, length);
            }
            
            return outputStream.toByteArray();
        } catch (Exception e) {
            throw new RuntimeException("获取流程定义资源失败", e);
        }
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public String deployProcess(String name, byte[] bpmnBytes, String category) {
        try {
            Deployment deployment = repositoryService.createDeployment()
                    .name(name)
                    .category(category)  // 设置流程分类，用于对流程进行分组管理
                    .addBytes(name + ".bpmn20.xml", bpmnBytes)
                    .deploy();
            
            return deployment.getId();
        } catch (Exception e) {
            throw new RuntimeException("部署流程失败", e);
        }
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void deleteDeployment(String deploymentId, boolean cascade) {
        try {
            if (cascade) {
                repositoryService.deleteDeployment(deploymentId, true);
            } else {
                repositoryService.deleteDeployment(deploymentId);
            }
        } catch (Exception e) {
            throw new RuntimeException("删除部署失败", e);
        }
    }

    @Override
    public Map<String, Object> getProcessVariables(String processInstanceId) {
        try {
            return runtimeService.getVariables(processInstanceId);
        } catch (Exception e) {
            throw new RuntimeException("获取流程变量失败", e);
        }
    }
    
    // ========== 权限检查方法实现 ==========
    
    @Override
    public boolean isTaskAssignedToUser(String taskId, String userId) {
        try {
            Task task = taskService.createTaskQuery().taskId(taskId).singleResult();
            return task != null && userId.equals(task.getAssignee());
        } catch (Exception e) {
            return false;
        }
    }
    
    @Override
    public boolean isUserRelatedToProcess(String processInstanceId, String userId) {
        try {
            // 检查是否为流程发起人
            if (isProcessStarter(processInstanceId, userId)) {
                return true;
            }
            
            // 检查是否有相关任务
            List<Task> tasks = taskService.createTaskQuery()
                    .processInstanceId(processInstanceId)
                    .taskAssignee(userId)
                    .list();
            if (!tasks.isEmpty()) {
                return true;
            }
            
            // 检查是否为候选人
            List<Task> candidateTasks = taskService.createTaskQuery()
                    .processInstanceId(processInstanceId)
                    .taskCandidateUser(userId)
                    .list();
            return !candidateTasks.isEmpty();
        } catch (Exception e) {
            return false;
        }
    }
    
    @Override
    public boolean isProcessStarter(String processInstanceId, String userId) {
        try {
            ProcessInstance processInstance = runtimeService.createProcessInstanceQuery()
                    .processInstanceId(processInstanceId)
                    .startedBy(userId)
                    .singleResult();
            return processInstance != null;
        } catch (Exception e) {
            return false;
        }
    }
    
    @Override
    public boolean isTaskAssigneeOrCandidate(String taskId, String userId) {
        try {
            // 检查是否为任务处理人
            if (isTaskAssignee(taskId, userId)) {
                return true;
            }
            
            // 检查是否为候选人
            Task task = taskService.createTaskQuery()
                    .taskId(taskId)
                    .taskCandidateUser(userId)
                    .singleResult();
            return task != null;
        } catch (Exception e) {
            return false;
        }
    }
    
    @Override
    public boolean canUserClaimTask(String taskId, String userId) {
        try {
            Task task = taskService.createTaskQuery().taskId(taskId).singleResult();
            if (task == null) {
                return false;
            }
            
            // 如果任务已分配给其他人，不能认领
            if (task.getAssignee() != null && !userId.equals(task.getAssignee())) {
                return false;
            }
            
            // 如果任务未分配，检查是否为候选人
            if (task.getAssignee() == null) {
                Task candidateTask = taskService.createTaskQuery()
                        .taskId(taskId)
                        .taskCandidateUser(userId)
                        .singleResult();
                return candidateTask != null;
            }
            
            return true;
        } catch (Exception e) {
            return false;
        }
    }
    
    @Override
    public boolean isTaskAssignee(String taskId, String userId) {
        try {
            Task task = taskService.createTaskQuery()
                    .taskId(taskId)
                    .taskAssignee(userId)
                    .singleResult();
            return task != null;
        } catch (Exception e) {
            return false;
        }
    }
    
    @Override
    public boolean isUserRelatedToTask(String taskId, String userId) {
        try {
            // 检查是否为任务处理人或候选人
            if (isTaskAssigneeOrCandidate(taskId, userId)) {
                return true;
            }
            
            // 检查是否为流程发起人
            Task task = taskService.createTaskQuery().taskId(taskId).singleResult();
            if (task != null) {
                return isProcessStarter(task.getProcessInstanceId(), userId);
            }
            
            return false;
        } catch (Exception e) {
            return false;
        }
    }
    
    @Override
    public boolean isUserRelatedToForm(String formId, String userId) {
        // 这里需要根据具体的表单权限逻辑实现
        // 暂时返回true，具体实现需要根据业务需求
        return true;
    }
    
    // ========== 其他缺失方法的实现 ==========
    
    @Override
    public Map<String, Object> searchProcessInstances(Map<String, Object> searchParams) {
        // 实现流程实例搜索逻辑
        Map<String, Object> result = new HashMap<>();
        // 这里需要根据具体的搜索需求实现
        result.put("total", 0);
        result.put("list", new ArrayList<>());
        return result;
    }
    
    @Override
    public Map<String, Object> getProcessInstanceBasicInfo(String processInstanceId) {
        try {
            ProcessInstance processInstance = runtimeService.createProcessInstanceQuery()
                    .processInstanceId(processInstanceId)
                    .singleResult();
            
            Map<String, Object> info = new HashMap<>();
            if (processInstance != null) {
                info.put("id", processInstance.getId());
                info.put("processDefinitionId", processInstance.getProcessDefinitionId());
                info.put("processDefinitionKey", processInstance.getProcessDefinitionKey());
                info.put("businessKey", processInstance.getBusinessKey());
                info.put("startUserId", processInstance.getStartUserId());
                info.put("startTime", processInstance.getStartTime());
            }
            return info;
        } catch (Exception e) {
            throw new RuntimeException("获取流程实例基本信息失败", e);
        }
    }
    
    @Override
    public String deployProcess(org.springframework.web.multipart.MultipartFile file, String name) {
        try {
            Deployment deployment = repositoryService.createDeployment()
                    .name(name)
                    .addInputStream(file.getOriginalFilename(), file.getInputStream())
                    .deploy();
            return deployment.getId();
        } catch (Exception e) {
            throw new RuntimeException("部署流程失败", e);
        }
    }
    
    @Override
    public Map<String, Object> getProcessDefinitions(int page, int size, String category) {
        org.flowable.engine.repository.ProcessDefinitionQuery query = repositoryService.createProcessDefinitionQuery()
                .latestVersion()
                .orderByProcessDefinitionName()
                .asc();
        
        if (category != null && !category.isEmpty()) {
            query.processDefinitionCategory(category);
        }
        
        long total = query.count();
        List<ProcessDefinition> processDefinitions = query
                .listPage((page - 1) * size, size);
        
        List<Map<String, Object>> list = new ArrayList<>();
        for (ProcessDefinition pd : processDefinitions) {
            Map<String, Object> item = new HashMap<>();
            item.put("id", pd.getId());
            item.put("key", pd.getKey());
            item.put("name", pd.getName());
            item.put("category", pd.getCategory());
            item.put("version", pd.getVersion());
            item.put("resourceName", pd.getResourceName());
            item.put("deploymentId", pd.getDeploymentId());
            item.put("description", pd.getDescription());
            item.put("suspended", pd.isSuspended());
            list.add(item);
        }
        
        Map<String, Object> result = new HashMap<>();
        result.put("total", total);
        result.put("list", list);
        return result;
    }
    
    @Override
    public Map<String, Object> getProcessDefinitionDetail(String processDefinitionId) {
        try {
            ProcessDefinition processDefinition = repositoryService.createProcessDefinitionQuery()
                    .processDefinitionId(processDefinitionId)
                    .singleResult();
            
            Map<String, Object> detail = new HashMap<>();
            if (processDefinition != null) {
                detail.put("id", processDefinition.getId());
                detail.put("key", processDefinition.getKey());
                detail.put("name", processDefinition.getName());
                detail.put("category", processDefinition.getCategory());
                detail.put("version", processDefinition.getVersion());
                detail.put("resourceName", processDefinition.getResourceName());
                detail.put("deploymentId", processDefinition.getDeploymentId());
                detail.put("description", processDefinition.getDescription());
                detail.put("suspended", processDefinition.isSuspended());
            }
            return detail;
        } catch (Exception e) {
            throw new RuntimeException("获取流程定义详情失败", e);
        }
    }
    
    @Override
    public String getProcessDefinitionXml(String processDefinitionId) {
        try {
            ProcessDefinition processDefinition = repositoryService.createProcessDefinitionQuery()
                    .processDefinitionId(processDefinitionId)
                    .singleResult();
            
            InputStream inputStream = repositoryService.getResourceAsStream(
                    processDefinition.getDeploymentId(),
                    processDefinition.getResourceName());
            
            return new String(inputStream.readAllBytes(), "UTF-8");
        } catch (Exception e) {
            throw new RuntimeException("获取流程定义XML失败", e);
        }
    }
    
    @Override
    public byte[] getProcessDefinitionImage(String processDefinitionId) {
        try {
            ProcessDefinition processDefinition = repositoryService.createProcessDefinitionQuery()
                    .processDefinitionId(processDefinitionId)
                    .singleResult();
            
            String diagramResourceName = processDefinition.getDiagramResourceName();
            if (diagramResourceName != null) {
                InputStream inputStream = repositoryService.getResourceAsStream(
                        processDefinition.getDeploymentId(),
                        diagramResourceName);
                return inputStream.readAllBytes();
            }
            return null;
        } catch (Exception e) {
            throw new RuntimeException("获取流程定义图片失败", e);
        }
    }
    
    @Override
    public Map<String, Object> getDeployments(int page, int size) {
        org.flowable.engine.repository.DeploymentQuery query = repositoryService.createDeploymentQuery()
                .orderByDeploymentTime()
                .desc();
        
        long total = query.count();
        List<Deployment> deployments = query.listPage((page - 1) * size, size);
        
        List<Map<String, Object>> list = new ArrayList<>();
        for (Deployment deployment : deployments) {
            Map<String, Object> item = new HashMap<>();
            item.put("id", deployment.getId());
            item.put("name", deployment.getName());
            item.put("category", deployment.getCategory());
            item.put("deploymentTime", deployment.getDeploymentTime());
            item.put("tenantId", deployment.getTenantId());
            list.add(item);
        }
        
        Map<String, Object> result = new HashMap<>();
        result.put("total", total);
        result.put("list", list);
        return result;
    }
    
    @Override
    public void approveTask(String taskId, String comment, Map<String, Object> variables) {
        try {
            if (comment != null && !comment.isEmpty()) {
                Task task = taskService.createTaskQuery().taskId(taskId).singleResult();
                taskService.addComment(taskId, task.getProcessInstanceId(), comment);
            }
            
            if (variables == null) {
                variables = new HashMap<>();
            }
            variables.put("approved", true);
            
            taskService.complete(taskId, variables);
        } catch (Exception e) {
            throw new RuntimeException("审批通过任务失败", e);
        }
    }
    
    @Override
    public void rejectTask(String taskId, String comment, Map<String, Object> variables) {
        try {
            if (comment != null && !comment.isEmpty()) {
                Task task = taskService.createTaskQuery().taskId(taskId).singleResult();
                taskService.addComment(taskId, task.getProcessInstanceId(), comment);
            }
            
            if (variables == null) {
                variables = new HashMap<>();
            }
            variables.put("approved", false);
            
            taskService.complete(taskId, variables);
        } catch (Exception e) {
            throw new RuntimeException("拒绝任务失败", e);
        }
    }
}