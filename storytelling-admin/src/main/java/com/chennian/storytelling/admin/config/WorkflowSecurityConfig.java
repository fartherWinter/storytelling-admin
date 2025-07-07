package com.chennian.storytelling.admin.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.access.expression.method.DefaultMethodSecurityExpressionHandler;
import org.springframework.security.access.expression.method.MethodSecurityExpressionHandler;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.method.configuration.GlobalMethodSecurityConfiguration;

/**
 * 工作流安全配置
 * 统一管理工作流模块的权限控制
 * 
 * @author chennian
 */
@Configuration
@EnableGlobalMethodSecurity(
    prePostEnabled = true,  // 启用@PreAuthorize和@PostAuthorize
    securedEnabled = true,  // 启用@Secured
    jsr250Enabled = true    // 启用@RolesAllowed
)
public class WorkflowSecurityConfig extends GlobalMethodSecurityConfiguration {

    /**
     * 自定义方法安全表达式处理器
     */
    @Override
    protected MethodSecurityExpressionHandler createExpressionHandler() {
        DefaultMethodSecurityExpressionHandler expressionHandler = 
            new DefaultMethodSecurityExpressionHandler();
        
        // 可以在这里添加自定义的权限评估器
        // expressionHandler.setPermissionEvaluator(new WorkflowPermissionEvaluator());
        
        return expressionHandler;
    }

    /**
     * 工作流权限评估器
     * 用于复杂的权限判断逻辑
     */
    @Bean
    public WorkflowPermissionEvaluator workflowPermissionEvaluator() {
        return new WorkflowPermissionEvaluator();
    }

    /**
     * 工作流权限常量定义
     */
    public static class WorkflowPermissions {
        // 基础权限
        public static final String READ = "workflow:read";
        public static final String WRITE = "workflow:write";
        public static final String DELETE = "workflow:delete";
        public static final String ADMIN = "workflow:admin";
        
        // 流程定义权限
        public static final String DEPLOY = "workflow:deploy";
        public static final String DEFINITION_MANAGE = "workflow:definition:manage";
        public static final String DEFINITION_VIEW = "workflow:definition:view";
        
        // 流程实例权限
        public static final String START = "workflow:start";
        public static final String TERMINATE = "workflow:terminate";
        public static final String SUSPEND = "workflow:suspend";
        public static final String INSTANCE_MANAGE = "workflow:instance:manage";
        
        // 任务权限
        public static final String TASK_COMPLETE = "workflow:task:complete";
        public static final String TASK_ASSIGN = "workflow:task:assign";
        public static final String TASK_DELEGATE = "workflow:task:delegate";
        public static final String TASK_CLAIM = "workflow:task:claim";
        
        // 表单权限
        public static final String FORM_DESIGN = "workflow:form:design";
        public static final String FORM_MANAGE = "workflow:form:manage";
        public static final String FORM_VIEW = "workflow:form:view";
        
        // 批量操作权限
        public static final String BATCH = "workflow:batch";
        public static final String BATCH_APPROVE = "workflow:batch:approve";
        public static final String BATCH_REJECT = "workflow:batch:reject";
        
        // 监控权限
        public static final String MONITOR = "workflow:monitor";
        public static final String STATS = "workflow:stats";
        public static final String REPORT = "workflow:report";
        
        // 配置权限
        public static final String CONFIG = "workflow:config";
        public static final String CONFIG_MANAGE = "workflow:config:manage";
    }

    /**
     * 工作流角色常量定义
     */
    public static class WorkflowRoles {
        public static final String ADMIN = "ROLE_WORKFLOW_ADMIN";
        public static final String MANAGER = "ROLE_WORKFLOW_MANAGER";
        public static final String USER = "ROLE_WORKFLOW_USER";
        public static final String VIEWER = "ROLE_WORKFLOW_VIEWER";
        public static final String DESIGNER = "ROLE_WORKFLOW_DESIGNER";
    }

    /**
     * 权限表达式常量
     * 用于@PreAuthorize注解中的复杂权限判断
     */
    public static class PermissionExpressions {
        // 管理员权限
        public static final String IS_ADMIN = "hasRole('ADMIN') or hasRole('WORKFLOW_ADMIN') or hasAuthority('workflow:admin')";
        
        // 管理者权限
        public static final String IS_MANAGER = "hasRole('WORKFLOW_MANAGER') or hasAuthority('workflow:manage')";
        
        // 用户权限
        public static final String IS_USER = "hasRole('USER') or hasRole('WORKFLOW_USER') or hasAuthority('workflow:read')";
        
        // 设计者权限
        public static final String IS_DESIGNER = "hasRole('WORKFLOW_DESIGNER') or hasAuthority('workflow:design')";
        
        // 流程启动权限
        public static final String CAN_START_PROCESS = "hasRole('USER') or hasAuthority('workflow:start')";
        
        // 流程管理权限
        public static final String CAN_MANAGE_PROCESS = "hasRole('ADMIN') or hasRole('WORKFLOW_MANAGER') or hasAuthority('workflow:manage')";
        
        // 任务处理权限
        public static final String CAN_HANDLE_TASK = "hasRole('USER') or hasAuthority('workflow:task:complete')";
        
        // 批量操作权限
        public static final String CAN_BATCH_OPERATE = "hasRole('ADMIN') or hasRole('WORKFLOW_MANAGER') or hasAuthority('workflow:batch')";
        
        // 监控权限
        public static final String CAN_MONITOR = "hasRole('ADMIN') or hasRole('WORKFLOW_MANAGER') or hasAuthority('workflow:monitor')";
        
        // 配置权限
        public static final String CAN_CONFIG = "hasRole('ADMIN') or hasAuthority('workflow:config')";
    }
}