package com.chennian.storytelling.common.enums;

import lombok.Getter;

/**
 * 工作流响应消息枚举
 * 
 * @author chennian
 * @date 2025-01-27
 */
@Getter
public enum WorkflowResponseEnum {
    
    // 权限相关 (7001-7099)
    WORKFLOW_UNAUTHORIZED(7001, "无权限访问"),
    PERMISSION_DENIED(7002, "权限不足"),
    WORKFLOW_PERMISSION_DENIED(7003, "权限不足"),
    TASK_PERMISSION_DENIED(7004, "无权限操作此任务"),
    PROCESS_PERMISSION_DENIED(7005, "无权限访问该流程实例"),
    
    // 任务相关 (7100-7199)
    TASK_NOT_FOUND(7100, "任务不存在"),
    TASK_COMPLETE_FAILED(7101, "任务完成失败"),
    TASK_CLAIM_FAILED(7102, "任务认领失败"),
    TASK_DELEGATE_FAILED(7103, "任务委派失败"),
    TASK_TRANSFER_FAILED(7104, "任务转办失败"),
    TASK_APPROVE_FAILED(7105, "任务审批失败"),
    TASK_REJECT_FAILED(7106, "任务拒绝失败"),
    TASK_ALREADY_COMPLETED(7107, "任务已完成"),
    TASK_ALREADY_CLAIMED(7108, "任务已被认领"),
    
    // 流程实例相关 (7200-7299)
    PROCESS_INSTANCE_NOT_FOUND(7200, "流程实例不存在"),
    PROCESS_START_FAILED(7201, "流程启动失败"),
    PROCESS_TERMINATE_FAILED(7202, "流程终止失败"),
    PROCESS_SUSPEND_FAILED(7203, "流程挂起失败"),
    PROCESS_ACTIVATE_FAILED(7204, "流程激活失败"),
    PROCESS_ALREADY_SUSPENDED(7205, "流程已挂起"),
    PROCESS_ALREADY_ACTIVE(7206, "流程已激活"),
    PROCESS_ALREADY_TERMINATED(7207, "流程已终止"),
    
    // 流程定义相关 (7300-7399)
    PROCESS_DEFINITION_NOT_FOUND(7300, "流程定义不存在"),
    PROCESS_DEPLOY_FAILED(7301, "流程部署失败"),
    PROCESS_DELETE_FAILED(7302, "流程删除失败"),
    PROCESS_DEFINITION_SUSPEND_FAILED(7303, "流程定义挂起失败"),
    PROCESS_DEFINITION_ACTIVATE_FAILED(7304, "流程定义激活失败"),
    PROCESS_DEFINITION_ALREADY_SUSPENDED(7305, "流程定义已挂起"),
    PROCESS_DEFINITION_ALREADY_ACTIVE(7306, "流程定义已激活"),
    
    // 流程变量相关 (7400-7499)
    PROCESS_VARIABLE_ERROR(7400, "流程变量操作失败"),
    PROCESS_VARIABLE_GET_FAILED(7401, "获取流程变量失败"),
    PROCESS_VARIABLE_SET_FAILED(7402, "设置流程变量失败"),
    PROCESS_VARIABLE_NOT_FOUND(7403, "流程变量不存在"),
    
    // 流程历史相关 (7500-7599)
    PROCESS_HISTORY_ERROR(7500, "流程历史操作失败"),
    PROCESS_HISTORY_GET_FAILED(7501, "获取流程历史失败"),
    TASK_HISTORY_GET_FAILED(7502, "获取任务历史失败"),
    PROCESS_DETAIL_ERROR(7503, "获取流程详情失败"),
    
    // 流程图相关 (7600-7699)
    PROCESS_DIAGRAM_GET_FAILED(7600, "获取流程图失败"),
    PROCESS_DIAGRAM_NOT_FOUND(7601, "流程图不存在"),
    
    // 流程模型相关 (7700-7799)
    WORKFLOW_MODEL_NOT_FOUND(7700, "流程模型不存在"),
    WORKFLOW_MODEL_CREATE_FAILED(7701, "流程模型创建失败"),
    WORKFLOW_MODEL_UPDATE_FAILED(7702, "流程模型更新失败"),
    WORKFLOW_MODEL_DELETE_FAILED(7703, "流程模型删除失败"),
    WORKFLOW_MODEL_DEPLOY_FAILED(7704, "流程模型部署失败"),
    WORKFLOW_MODEL_CONVERT_FAILED(7705, "流程模型转换失败"),
    WORKFLOW_MODEL_SAVE_FAILED(7706, "流程模型保存失败"),
    WORKFLOW_MODEL_XML_GET_FAILED(7707, "获取流程模型XML失败"),
    WORKFLOW_MODEL_XML_SAVE_FAILED(7708, "保存流程模型XML失败"),
    
    // 批量操作相关 (7800-7899)
    BATCH_OPERATION_FAILED(7800, "批量操作失败"),
    BATCH_TASK_OPERATION_FAILED(7801, "批量任务操作失败"),
    BATCH_PROCESS_OPERATION_FAILED(7802, "批量流程操作失败"),
    
    // 通用工作流错误 (7900-7999)
    WORKFLOW_ERROR(7900, "工作流操作失败"),
    WORKFLOW_ENGINE_ERROR(7901, "工作流引擎错误"),
    WORKFLOW_CONFIGURATION_ERROR(7902, "工作流配置错误"),
    WORKFLOW_VALIDATION_ERROR(7903, "工作流验证失败"),
    WORKFLOW_RESOURCE_NOT_FOUND(7904, "工作流资源不存在"),
    WORKFLOW_STATUS_ERROR(7905, "工作流状态异常"),
    WORKFLOW_PARAMETER_INVALID(7906, "工作流参数无效");
    
    private final int code;
    private final String message;
    
    WorkflowResponseEnum(int code, String message) {
        this.code = code;
        this.message = message;
    }

}