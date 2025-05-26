package com.chennian.storytelling.service;

import java.util.List;
import java.util.Map;

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
}