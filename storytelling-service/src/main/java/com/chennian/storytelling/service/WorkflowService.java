package com.chennian.storytelling.service;

import java.util.List;
import java.util.Map;

import com.chennian.storytelling.bean.dto.*;
import org.flowable.engine.runtime.ProcessInstance;
import org.flowable.task.api.Task;

/**
 * 工作流服务接口
 * 
 * @author chennian
 */
public interface WorkflowService {

    /**
     * 部署流程定义
     * 
     * @param name 流程名称
     * @param category 流程分类
     * @param resourceName 资源名称
     * @param deploymentFile 部署文件内容
     * @return 部署ID
     */
    String deployProcess(String name, String category, String resourceName, String deploymentFile);
    
    /**
     * 启动流程实例
     * 
     * @param processKey 流程定义Key
     * @param businessKey 业务键
     * @param businessType 业务类型
     * @param title 流程标题
     * @param variables 流程变量
     * @return 流程实例
     */
    ProcessInstance startProcess(String processKey, String businessKey, String businessType, String title, Map<String, Object> variables);
    
    /**
     * 查询待办任务
     * 
     * @param assignee 任务处理人
     * @return 任务列表
     */
    List<Task> findTodoTasks(String assignee);
    
    /**
     * 查询流程任务
     * 
     * @param processInstanceId 流程实例ID
     * @return 任务列表
     */
    List<Task> findTasksByProcessInstanceId(String processInstanceId);
    
    /**
     * 完成任务
     * 
     * @param taskId 任务ID
     * @param variables 流程变量
     * @param comment 审批意见
     */
    void completeTask(String taskId, Map<String, Object> variables, String comment);
    
    /**
     * 审批通过任务
     * 
     * @param taskId 任务ID
     * @param comment 审批意见
     */
    void approveTask(String taskId, String comment);
    
    /**
     * 拒绝任务
     * 
     * @param taskId 任务ID
     * @param comment 审批意见
     */
    void rejectTask(String taskId, String comment);
    
    /**
     * 获取流程实例
     * 
     * @param processInstanceId 流程实例ID
     * @return 流程实例
     */
    ProcessInstance getProcessInstance(String processInstanceId);
    
    /**
     * 根据业务键获取流程实例
     * 
     * @param businessKey 业务键
     * @return 流程实例
     */
    ProcessInstance getProcessInstanceByBusinessKey(String businessKey);
    
    /**
     * 获取流程变量
     * 
     * @param processInstanceId 流程实例ID
     * @param variableName 变量名称
     * @return 变量值
     */
    Object getProcessVariable(String processInstanceId, String variableName);
    
    /**
     * 设置流程变量
     * 
     * @param processInstanceId 流程实例ID
     * @param variableName 变量名称
     * @param value 变量值
     */
    void setProcessVariable(String processInstanceId, String variableName, Object value);
    
    /**
     * 获取流程图
     * 
     * @param processInstanceId 流程实例ID
     * @return 流程图字节数组
     */
    byte[] getProcessDiagram(String processInstanceId);
    
    /**
     * 终止流程实例
     * 
     * @param processInstanceId 流程实例ID
     * @param reason 终止原因
     */
    void terminateProcessInstance(String processInstanceId, String reason);
    
    /**
     * 获取工作流统计信息
     * 
     * @param startTime 开始时间
     * @param endTime 结束时间
     * @return 统计信息
     */
    WorkflowStatisticsDTO getWorkflowStatistics(java.time.LocalDateTime startTime, java.time.LocalDateTime endTime);
    
    /**
     * 批量操作任务
     * 
     * @param batchOperation 批量操作信息
     * @return 操作结果
     */
    WorkflowBatchOperationDTO.BatchOperationResult batchOperateTasks(WorkflowBatchOperationDTO batchOperation);
    
    /**
     * 批量操作流程实例
     * 
     * @param batchOperation 批量操作信息
     * @return 操作结果
     */
    WorkflowBatchOperationDTO.BatchOperationResult batchOperateProcessInstances(WorkflowBatchOperationDTO batchOperation);
    
    /**
     * 获取流程历史记录
     * 
     * @param processInstanceId 流程实例ID
     * @return 历史记录
     */
    WorkflowHistoryDTO getProcessHistory(String processInstanceId);
    
    /**
     * 查询用户的历史任务
     * 
     * @param assignee 任务处理人
     * @param startTime 开始时间（可选）
     * @param endTime 结束时间（可选）
     * @return 历史任务列表
     */
    List<TaskDTO> findHistoryTasks(String assignee, java.time.LocalDateTime startTime, java.time.LocalDateTime endTime);
    
    /**
     * 任务委派
     * 
     * @param taskId 任务ID
     * @param assignee 委派给的用户
     * @param comment 委派意见
     */
    void delegateTask(String taskId, String assignee, String comment);
    
    /**
     * 任务认领
     * 
     * @param taskId 任务ID
     * @param assignee 认领人
     */
    void claimTask(String taskId, String assignee);
    
    /**
     * 任务转办
     * 
     * @param taskId 任务ID
     * @param assignee 转办给的用户
     * @param comment 转办意见
     */
    void transferTask(String taskId, String assignee, String comment);
    
    /**
     * 获取流程定义列表
     * 
     * @param category 分类（可选）
     * @param key 流程键（可选）
     * @param name 流程名称（可选）
     * @return 流程定义列表
     */
    List<ProcessDefinitionDTO> listProcessDefinitions(String category, String key, String name);
    
    /**
     * 挂起流程定义
     * 
     * @param processDefinitionId 流程定义ID
     */
    void suspendProcessDefinition(String processDefinitionId);
    
    /**
     * 激活流程定义
     * 
     * @param processDefinitionId 流程定义ID
     */
    void activateProcessDefinition(String processDefinitionId);
    
    /**
     * 挂起流程实例
     * 
     * @param processInstanceId 流程实例ID
     */
    void suspendProcessInstance(String processInstanceId);
    
    /**
     * 激活流程实例
     * 
     * @param processInstanceId 流程实例ID
     */
    void activateProcessInstance(String processInstanceId);
    
    /**
     * 获取流程定义列表
     * 
     * @return 流程定义列表
     */
    List<ProcessDefinitionDTO> getProcessDefinitions();
    
    /**
     * 获取流程定义资源
     * 
     * @param definitionId 流程定义ID
     * @param resourceType 资源类型（xml或image）
     * @return 资源字节数组
     */
    byte[] getProcessDefinitionResource(String definitionId, String resourceType);
    
    /**
     * 部署流程定义（从文件）
     * 
     * @param fileName 文件名
     * @param fileBytes 文件字节数组
     * @param category 流程分类
     * @return 部署ID
     */
    String deployProcess(String fileName, byte[] fileBytes, String category);
    
    /**
     * 删除部署
     * 
     * @param deploymentId 部署ID
     * @param cascade 是否级联删除
     */
    void deleteDeployment(String deploymentId, boolean cascade);
    
    /**
     * 获取流程变量
     * 
     * @param processInstanceId 流程实例ID
     * @return 流程变量Map
     */
    java.util.Map<String, Object> getProcessVariables(String processInstanceId);
}