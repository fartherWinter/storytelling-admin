package com.chennian.storytelling.workflow.delegate;

import org.flowable.engine.delegate.DelegateExecution;
import org.flowable.engine.delegate.JavaDelegate;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.chennian.storytelling.bean.model.SysUser;
import com.chennian.storytelling.service.SysUserService;

/**
 * 管理员创建流程委托类
 * 
 * @author chennian
 */
@Component
public class AdminCreationDelegate implements JavaDelegate {

    private static final Logger logger = LoggerFactory.getLogger(AdminCreationDelegate.class);
    
    @Autowired
    private SysUserService sysUserService;
    
    @Override
    public void execute(DelegateExecution execution) {
        logger.info("执行管理员创建操作");
        
        // 获取流程变量
        String userName = (String) execution.getVariable("userName");
        String password = (String) execution.getVariable("password");
        String nickName = (String) execution.getVariable("nickName");
        Long deptId = (Long) execution.getVariable("deptId");
        String email = (String) execution.getVariable("email");
        String phoneNumber = (String) execution.getVariable("phoneNumber");
        String sex = (String) execution.getVariable("sex");
        Integer permissionLevel = (Integer) execution.getVariable("permissionLevel");
        Long[] roleIds = (Long[]) execution.getVariable("roleIds");
        Long[] postIds = (Long[]) execution.getVariable("postIds");
        String remark = (String) execution.getVariable("remark");
        
        try {
            // 创建管理员用户对象
            SysUser sysUser = new SysUser();
            sysUser.setUserName(userName);
            sysUser.setNickName(nickName);
            sysUser.setDeptId(deptId);
            sysUser.setEmail(email);
            sysUser.setPhoneNumber(phoneNumber);
            sysUser.setSex(sex);
            sysUser.setPassword(password); // 实际应用中应该加密处理
            sysUser.setStatus("0"); // 正常状态
            sysUser.setRemark(remark);
            
            // 调用用户服务创建管理员
            int result = sysUserService.insertUser(sysUser);
            
            // 设置流程变量，标记管理员创建结果
            execution.setVariable("adminCreationResult", result > 0);
            execution.setVariable("adminId", sysUser.getUserId());
            execution.setVariable("processStatus", "completed");
            
            logger.info("管理员创建成功，用户ID：{}", sysUser.getUserId());
        } catch (Exception e) {
            logger.error("管理员创建失败", e);
            execution.setVariable("adminCreationResult", false);
            execution.setVariable("errorMessage", e.getMessage());
            execution.setVariable("processStatus", "failed");
        }
    }
}