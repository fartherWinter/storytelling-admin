package com.chennian.storytelling.admin.config;

import com.chennian.storytelling.service.WorkflowService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.PermissionEvaluator;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.stereotype.Component;

import java.io.Serializable;
import java.util.Collection;

/**
 * 工作流权限评估器
 * 实现复杂的工作流权限判断逻辑
 * 
 * @author chennian
 */
@Component
@Slf4j
public class WorkflowPermissionEvaluator implements PermissionEvaluator {

    @Autowired
    private WorkflowService workflowService;

    /**
     * 评估用户对特定对象的权限
     * 
     * @param authentication 认证信息
     * @param targetDomainObject 目标对象
     * @param permission 权限
     * @return 是否有权限
     */
    @Override
    public boolean hasPermission(Authentication authentication, Object targetDomainObject, Object permission) {
        if (authentication == null || targetDomainObject == null || permission == null) {
            return false;
        }

        String userId = authentication.getName();
        String permissionStr = permission.toString();
        
        log.debug("评估权限: 用户={}, 对象={}, 权限={}", userId, targetDomainObject, permissionStr);

        try {
            // 管理员拥有所有权限
            if (hasAdminRole(authentication)) {
                return true;
            }

            // 根据目标对象类型进行权限判断
            if (targetDomainObject instanceof String) {
                String targetId = (String) targetDomainObject;
                return evaluatePermissionByType(authentication, targetId, permissionStr);
            }

            return false;
            
        } catch (Exception e) {
            log.error("权限评估异常: {}", e.getMessage(), e);
            return false;
        }
    }

    /**
     * 评估用户对特定ID对象的权限
     * 
     * @param authentication 认证信息
     * @param targetId 目标对象ID
     * @param targetType 目标对象类型
     * @param permission 权限
     * @return 是否有权限
     */
    @Override
    public boolean hasPermission(Authentication authentication, Serializable targetId, String targetType, Object permission) {
        if (authentication == null || targetId == null || targetType == null || permission == null) {
            return false;
        }

        String userId = authentication.getName();
        String permissionStr = permission.toString();
        
        log.debug("评估权限: 用户={}, 目标ID={}, 目标类型={}, 权限={}", 
                 userId, targetId, targetType, permissionStr);

        try {
            // 管理员拥有所有权限
            if (hasAdminRole(authentication)) {
                return true;
            }

            // 根据目标类型进行权限判断
            switch (targetType.toLowerCase()) {
                case "processinstance":
                    return evaluateProcessInstancePermission(authentication, targetId.toString(), permissionStr);
                case "task":
                    return evaluateTaskPermission(authentication, targetId.toString(), permissionStr);
                case "processdefinition":
                    return evaluateProcessDefinitionPermission(authentication, targetId.toString(), permissionStr);
                case "form":
                    return evaluateFormPermission(authentication, targetId.toString(), permissionStr);
                default:
                    return false;
            }
            
        } catch (Exception e) {
            log.error("权限评估异常: {}", e.getMessage(), e);
            return false;
        }
    }

    /**
     * 根据权限类型评估权限
     */
    private boolean evaluatePermissionByType(Authentication authentication, String targetId, String permission) {
        String userId = authentication.getName();
        
        switch (permission) {
            case "read":
                return hasReadPermission(authentication, targetId);
            case "write":
                return hasWritePermission(authentication, targetId);
            case "delete":
                return hasDeletePermission(authentication, targetId);
            case "manage":
                return hasManagePermission(authentication, targetId);
            default:
                return hasSpecificPermission(authentication, targetId, permission);
        }
    }

    /**
     * 评估流程实例权限
     */
    private boolean evaluateProcessInstancePermission(Authentication authentication, String processInstanceId, String permission) {
        String userId = authentication.getName();
        
        try {
            switch (permission) {
                case "read":
                case "view":
                    // 用户可以查看自己相关的流程实例
                    return workflowService.isUserRelatedToProcess(processInstanceId, userId);
                    
                case "terminate":
                case "suspend":
                case "activate":
                    // 只有流程发起人或管理员可以控制流程实例
                    return workflowService.isProcessStarter(processInstanceId, userId) ||
                           hasManagerRole(authentication);
                    
                case "manage":
                    // 管理权限需要管理员角色
                    return hasManagerRole(authentication);
                    
                default:
                    return false;
            }
        } catch (Exception e) {
            log.error("评估流程实例权限失败: {}", e.getMessage(), e);
            return false;
        }
    }

    /**
     * 评估任务权限
     */
    private boolean evaluateTaskPermission(Authentication authentication, String taskId, String permission) {
        String userId = authentication.getName();
        
        try {
            switch (permission) {
                case "complete":
                case "claim":
                    // 用户可以处理分配给自己的任务或候选任务
                    return workflowService.isTaskAssigneeOrCandidate(taskId, userId);
                    
                case "delegate":
                case "assign":
                    // 任务委派和分配需要任务负责人或管理员权限
                    return workflowService.isTaskAssignee(taskId, userId) ||
                           hasManagerRole(authentication);
                    
                case "read":
                case "view":
                    // 用户可以查看相关的任务
                    return workflowService.isUserRelatedToTask(taskId, userId);
                    
                default:
                    return false;
            }
        } catch (Exception e) {
            log.error("评估任务权限失败: {}", e.getMessage(), e);
            return false;
        }
    }

    /**
     * 评估流程定义权限
     */
    private boolean evaluateProcessDefinitionPermission(Authentication authentication, String processDefinitionId, String permission) {
        switch (permission) {
            case "read":
            case "view":
                // 所有用户都可以查看流程定义
                return hasUserRole(authentication);
                
            case "deploy":
            case "delete":
            case "suspend":
            case "activate":
                // 流程定义管理需要管理员权限
                return hasManagerRole(authentication);
                
            case "start":
                // 用户可以启动流程
                return hasUserRole(authentication);
                
            default:
                return false;
        }
    }

    /**
     * 评估表单权限
     */
    private boolean evaluateFormPermission(Authentication authentication, String formId, String permission) {
        String userId = authentication.getName();
        
        try {
            switch (permission) {
                case "read":
                case "view":
                    // 用户可以查看相关的表单
                    return workflowService.isUserRelatedToForm(formId, userId);
                    
                case "design":
                case "manage":
                    // 表单设计和管理需要设计师或管理员权限
                    return hasDesignerRole(authentication) || hasManagerRole(authentication);
                    
                case "submit":
                    // 用户可以提交表单
                    return hasUserRole(authentication);
                    
                default:
                    return false;
            }
        } catch (Exception e) {
            log.error("评估表单权限失败: {}", e.getMessage(), e);
            return false;
        }
    }

    /**
     * 检查读权限
     */
    private boolean hasReadPermission(Authentication authentication, String targetId) {
        return hasUserRole(authentication);
    }

    /**
     * 检查写权限
     */
    private boolean hasWritePermission(Authentication authentication, String targetId) {
        return hasUserRole(authentication);
    }

    /**
     * 检查删除权限
     */
    private boolean hasDeletePermission(Authentication authentication, String targetId) {
        return hasManagerRole(authentication);
    }

    /**
     * 检查管理权限
     */
    private boolean hasManagePermission(Authentication authentication, String targetId) {
        return hasManagerRole(authentication);
    }

    /**
     * 检查特定权限
     */
    private boolean hasSpecificPermission(Authentication authentication, String targetId, String permission) {
        // 可以根据具体需求实现特定权限的判断逻辑
        return hasAuthority(authentication, "workflow:" + permission);
    }

    /**
     * 检查是否有管理员角色
     */
    private boolean hasAdminRole(Authentication authentication) {
        return hasRole(authentication, "ADMIN") || 
               hasRole(authentication, "WORKFLOW_ADMIN") ||
               hasAuthority(authentication, "workflow:admin");
    }

    /**
     * 检查是否有管理者角色
     */
    private boolean hasManagerRole(Authentication authentication) {
        return hasAdminRole(authentication) ||
               hasRole(authentication, "WORKFLOW_MANAGER") ||
               hasAuthority(authentication, "workflow:manage");
    }

    /**
     * 检查是否有用户角色
     */
    private boolean hasUserRole(Authentication authentication) {
        return hasRole(authentication, "USER") ||
               hasRole(authentication, "WORKFLOW_USER") ||
               hasAuthority(authentication, "workflow:read");
    }

    /**
     * 检查是否有设计师角色
     */
    private boolean hasDesignerRole(Authentication authentication) {
        return hasRole(authentication, "WORKFLOW_DESIGNER") ||
               hasAuthority(authentication, "workflow:design");
    }

    /**
     * 检查是否有特定角色
     */
    private boolean hasRole(Authentication authentication, String role) {
        Collection<? extends GrantedAuthority> authorities = authentication.getAuthorities();
        return authorities.stream()
                .anyMatch(authority -> 
                    authority.getAuthority().equals("ROLE_" + role) ||
                    authority.getAuthority().equals(role));
    }

    /**
     * 检查是否有特定权限
     */
    private boolean hasAuthority(Authentication authentication, String authority) {
        Collection<? extends GrantedAuthority> authorities = authentication.getAuthorities();
        return authorities.stream()
                .anyMatch(auth -> auth.getAuthority().equals(authority));
    }
}