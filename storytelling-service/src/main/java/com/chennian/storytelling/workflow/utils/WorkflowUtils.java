package com.chennian.storytelling.workflow.utils;

import java.util.HashMap;
import java.util.Map;

import org.flowable.engine.runtime.ProcessInstance;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.chennian.storytelling.service.WorkflowService;

/**
 * 工作流工具类
 * 
 * @author chennian
 */
@Component
public class WorkflowUtils {

    @Autowired
    private WorkflowService workflowService;
    
    /**
     * 启动销售订单审批流程
     * 
     * @param orderId 订单ID
     * @param orderCode 订单编号
     * @param orderAmount 订单金额
     * @param createBy 创建人
     * @return 流程实例ID
     */
    public String startSalesOrderApprovalProcess(Long orderId, String orderCode, Double orderAmount, String createBy) {
        // 设置流程变量
        Map<String, Object> variables = new HashMap<>();
        variables.put("orderId", orderId);
        variables.put("orderCode", orderCode);
        variables.put("orderAmount", orderAmount);
        variables.put("createBy", createBy);
        
        // 启动流程实例
        ProcessInstance processInstance = workflowService.startProcess(
                "salesOrderApproval", // 流程定义Key
                "ORDER_" + orderId, // 业务键
                "SALES_ORDER", // 业务类型
                "销售订单[" + orderCode + "]审批流程", // 流程标题
                variables);
        
        return processInstance.getId();
    }
    
    /**
     * 获取销售订单审批状态
     * 
     * @param orderId 订单ID
     * @return 审批状态（null:未提交审批, running:审批中, completed:审批完成, terminated:审批终止）
     */
    public String getSalesOrderApprovalStatus(Long orderId) {
        ProcessInstance processInstance = workflowService.getProcessInstanceByBusinessKey("ORDER_" + orderId);
        if (processInstance == null) {
            // 查询历史流程实例
            Object processStatus = workflowService.getProcessVariable("ORDER_" + orderId, "processStatus");
            return processStatus != null ? processStatus.toString() : null;
        }
        return "running";
    }
    
    /**
     * 终止销售订单审批流程
     * 
     * @param orderId 订单ID
     * @param reason 终止原因
     */
    public void terminateSalesOrderApprovalProcess(Long orderId, String reason) {
        ProcessInstance processInstance = workflowService.getProcessInstanceByBusinessKey("ORDER_" + orderId);
        if (processInstance != null) {
            workflowService.terminateProcessInstance(processInstance.getId(), reason);
        }
    }
}