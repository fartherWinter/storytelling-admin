package com.chennian.storytelling.workflow.utils;

import org.flowable.engine.runtime.ProcessInstance;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.chennian.storytelling.bean.model.SysUser;
import com.chennian.storytelling.service.WorkflowService;

import java.util.HashMap;
import java.util.Map;

/**
 * 管理员工作流工具类
 * 
 * @author chennian
 */
@Component
public class AdminWorkflowUtils {

    private static final Logger logger = LoggerFactory.getLogger(AdminWorkflowUtils.class);
    
    @Autowired
    private WorkflowService workflowService;
    
    /**
     * 启动管理员创建审批流程
     * 
     * @param sysUser 管理员用户信息
     * @param initiator 流程发起人
     * @return 流程实例ID
     */
    public String startAdminCreationProcess(SysUser sysUser, String initiator) {
        // 设置流程变量
        Map<String, Object> variables = new HashMap<>();
        variables.put("initiator", initiator);
        variables.put("userName", sysUser.getUserName());
        variables.put("password", sysUser.getPassword());
        variables.put("nickName", sysUser.getNickName());
        variables.put("deptId", sysUser.getDeptId());
        variables.put("email", sysUser.getEmail());
        variables.put("phoneNumber", sysUser.getPhoneNumber());
        variables.put("sex", sysUser.getSex());
        variables.put("permissionLevel", sysUser.getRoleIds() != null && sysUser.getRoleIds().length > 0 ? sysUser.getRoleIds().length : 1);
        variables.put("roleIds", sysUser.getRoleIds());
        variables.put("postIds", sysUser.getPostIds());
        variables.put("remark", sysUser.getRemark());
        
        // 启动流程实例
        ProcessInstance processInstance = workflowService.startProcess(
                "adminCreationApproval", // 流程定义Key
                "ADMIN_" + sysUser.getUserName(), // 业务键
                "ADMIN_CREATION", // 业务类型
                "管理员[" + sysUser.getUserName() + "]创建审批流程", // 流程标题
                variables);
        
        logger.info("启动管理员创建流程，流程实例ID：{}", processInstance.getId());
        
        return processInstance.getId();
    }
    
    /**
     * 获取管理员创建流程状态
     * 
     * @param userName 用户名
     * @return 流程状态
     */
    public String getAdminCreationProcessStatus(String userName) {
        ProcessInstance processInstance = workflowService.getProcessInstanceByBusinessKey("ADMIN_" + userName);
        if (processInstance == null) {
            return "未找到流程实例";
        }
        
        // 查询流程状态
        Object processStatus = workflowService.getProcessVariable(processInstance.getId(), "processStatus");
        return processStatus != null ? processStatus.toString() : "进行中";
    }
    
    /**
     * 终止管理员创建流程
     * 
     * @param userName 用户名
     * @param reason 终止原因
     * @return 是否成功
     */
    public boolean terminateAdminCreationProcess(String userName, String reason) {
        ProcessInstance processInstance = workflowService.getProcessInstanceByBusinessKey("ADMIN_" + userName);
        if (processInstance == null) {
            return false;
        }
        
        workflowService.terminateProcessInstance(processInstance.getId(), reason);
        return true;
    }
}