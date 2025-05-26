package com.chennian.storytelling.service.impl;

import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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
}