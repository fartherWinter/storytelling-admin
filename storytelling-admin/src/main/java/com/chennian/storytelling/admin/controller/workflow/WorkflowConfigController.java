package com.chennian.storytelling.admin.controller.workflow;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;
import java.util.HashMap;
import java.util.ArrayList;
import com.chennian.storytelling.bean.dto.WorkflowConfigDTO;
import com.chennian.storytelling.service.WorkflowConfigService;
import jakarta.validation.constraints.NotBlank;

/**
 * 工作流配置管理控制器
 * 提供系统配置和权限管理功能
 * 
 * @author chennian
 */
@Api(tags = "工作流配置管理")
@Slf4j
@RestController
@RequestMapping("/workflow/config")
@RequiredArgsConstructor
public class WorkflowConfigController {

    private final WorkflowConfigService workflowConfigService;

    /**
     * 获取系统配置
     */
    @ApiOperation("获取系统配置")
    @GetMapping("/system-config")
    public Map<String, Object> getSystemConfig(
            @ApiParam("配置分组") @RequestParam(required = false) String group) {
        
        Map<String, Object> response = new HashMap<>();
        
        try {
            // 从数据库获取系统配置
            List<WorkflowConfigDTO.SystemConfigDTO> configs = workflowConfigService.getSystemConfig(group);
            
            response.put("success", true);
            response.put("data", configs);
            response.put("message", "获取系统配置成功");
            
        } catch (Exception e) {
            log.error("获取系统配置失败", e);
            response.put("success", false);
            response.put("message", "获取系统配置失败: " + e.getMessage());
        }
        
        return response;
    }
    
    /**
     * 更新系统配置
     */
    @ApiOperation("更新系统配置")
    @PostMapping("/system-config")
    public Map<String, Object> updateSystemConfig(
            @ApiParam("系统配置信息") @Valid @RequestBody List<WorkflowConfigDTO.SystemConfigDTO> configs) {
        
        Map<String, Object> response = new HashMap<>();
        
        try {
            // 更新系统配置到数据库
            List<WorkflowConfigDTO.SystemConfigDTO> updatedConfigs = workflowConfigService.updateSystemConfig(configs);
            
            log.info("更新系统配置，共{}项", configs.size());
            
            response.put("success", true);
            response.put("data", configs);
            response.put("message", "更新系统配置成功");
            
        } catch (Exception e) {
            log.error("更新系统配置失败", e);
            response.put("success", false);
            response.put("message", "更新系统配置失败: " + e.getMessage());
        }
        
        return response;
    }
    

    
    /**
     * 获取权限列表
     */
    @ApiOperation("获取权限列表")
    @GetMapping("/permissions")
    public Map<String, Object> getPermissions() {
        Map<String, Object> response = new HashMap<>();
        
        try {
            // 从数据库获取权限列表
            List<WorkflowConfigDTO.PermissionDTO> permissions = workflowConfigService.getPermissions();
            
            response.put("success", true);
            response.put("data", permissions);
            response.put("message", "获取权限列表成功");
            
        } catch (Exception e) {
            log.error("获取权限列表失败", e);
            response.put("success", false);
            response.put("message", "获取权限列表失败: " + e.getMessage());
        }
        
        return response;
    }
    
    /**
     * 创建或更新权限
     */
    @ApiOperation("创建或更新权限")
    @PostMapping("/permissions")
    public Map<String, Object> savePermission(
            @ApiParam("权限信息") @Valid @RequestBody WorkflowConfigDTO.PermissionDTO permission) {
        
        Map<String, Object> response = new HashMap<>();
        
        try {
            // 保存权限到数据库
            WorkflowConfigDTO.PermissionDTO savedPermission = workflowConfigService.savePermission(permission);
            
            log.info("保存权限: {}", permission.getName());
            
            response.put("success", true);
            response.put("data", permission);
            response.put("message", "保存权限成功");
            
        } catch (Exception e) {
            log.error("保存权限失败", e);
            response.put("success", false);
            response.put("message", "保存权限失败: " + e.getMessage());
        }
        
        return response;
    }
    
    /**
     * 获取角色列表
     */
    @ApiOperation("获取角色列表")
    @GetMapping("/roles")
    public Map<String, Object> getRoles() {
        Map<String, Object> response = new HashMap<>();
        
        try {
            // 从数据库获取角色列表
            List<WorkflowConfigDTO.RoleDTO> roles = workflowConfigService.getRoles();
            
            response.put("success", true);
            response.put("data", roles);
            response.put("message", "获取角色列表成功");
            
        } catch (Exception e) {
            log.error("获取角色列表失败", e);
            response.put("success", false);
            response.put("message", "获取角色列表失败: " + e.getMessage());
        }
        
        return response;
    }
    
    /**
     * 创建角色
     */
    @ApiOperation("创建角色")
    @PostMapping("/roles")
    public Map<String, Object> createRole(
            @ApiParam("角色信息") @Valid @RequestBody WorkflowConfigDTO.RoleDTO role) {
        
        Map<String, Object> response = new HashMap<>();
        
        try {
            // 保存角色到数据库
            WorkflowConfigDTO.RoleDTO savedRole = workflowConfigService.createRole(role);
            
            log.info("创建角色: {}", role.getName());
            
            response.put("success", true);
            response.put("data", role);
            response.put("message", "创建角色成功");
            
        } catch (Exception e) {
            log.error("创建角色失败", e);
            response.put("success", false);
            response.put("message", "创建角色失败: " + e.getMessage());
        }
        
        return response;
    }
    
    /**
     * 更新角色
     */
    @ApiOperation("更新角色")
    @PutMapping("/roles/{roleId}")
    public Map<String, Object> updateRole(
            @ApiParam("角色ID") @PathVariable @NotBlank String roleId,
            @ApiParam("角色信息") @Valid @RequestBody WorkflowConfigDTO.RoleDTO role) {
        
        Map<String, Object> response = new HashMap<>();
        
        try {
            role.setId(roleId);
            // 更新角色到数据库
            WorkflowConfigDTO.RoleDTO updatedRole = workflowConfigService.updateRole(role);
            
            log.info("更新角色: {} - {}", roleId, role.getName());
            
            response.put("success", true);
            response.put("data", role);
            response.put("message", "更新角色成功");
            
        } catch (Exception e) {
            log.error("更新角色失败", e);
            response.put("success", false);
            response.put("message", "更新角色失败: " + e.getMessage());
        }
        
        return response;
    }
    
    /**
     * 删除角色
     */
    @ApiOperation("删除角色")
    @DeleteMapping("/roles/{roleId}")
    public Map<String, Object> deleteRole(
            @ApiParam("角色ID") @PathVariable @NotBlank String roleId) {
        
        Map<String, Object> response = new HashMap<>();
        
        try {
            // 从数据库删除角色
            workflowConfigService.deleteRole(roleId);
            
            log.info("删除角色: {}", roleId);
            
            response.put("success", true);
            response.put("message", "删除角色成功");
            
        } catch (Exception e) {
            log.error("删除角色失败", e);
            response.put("success", false);
            response.put("message", "删除角色失败: " + e.getMessage());
        }
        
        return response;
    }
    
    /**
     * 获取用户角色分配
     */
    @ApiOperation("获取用户角色分配")
    @GetMapping("/user-roles")
    public Map<String, Object> getUserRoles(
            @ApiParam("用户ID") @RequestParam(required = false) String userId) {
        
        Map<String, Object> response = new HashMap<>();
        
        try {
            // 从数据库获取用户角色分配
            List<WorkflowConfigDTO.UserRoleDTO> userRoles = workflowConfigService.getUserRoles(userId);
            
            response.put("success", true);
            response.put("data", userRoles);
            response.put("message", "获取用户角色分配成功");
            
        } catch (Exception e) {
            log.error("获取用户角色分配失败", e);
            response.put("success", false);
            response.put("message", "获取用户角色分配失败: " + e.getMessage());
        }
        
        return response;
    }
    
    /**
     * 分配用户角色
     */
    @ApiOperation("分配用户角色")
    @PostMapping("/user-roles")
    public Map<String, Object> assignUserRoles(
            @ApiParam("用户角色分配信息") @Valid @RequestBody WorkflowConfigDTO.UserRoleDTO userRole) {
        
        Map<String, Object> response = new HashMap<>();
        
        try {
            // 保存用户角色分配到数据库
            WorkflowConfigDTO.UserRoleDTO savedUserRole = workflowConfigService.assignUserRoles(userRole);
            
            log.info("分配用户角色: {} - {}", userRole.getUserName(), userRole.getRoles());
            
            response.put("success", true);
            response.put("data", userRole);
            response.put("message", "分配用户角色成功");
            
        } catch (Exception e) {
            log.error("分配用户角色失败", e);
            response.put("success", false);
            response.put("message", "分配用户角色失败: " + e.getMessage());
        }
        
        return response;
    }
    
    /**
     * 获取流程分类配置
     */
    @ApiOperation("获取流程分类配置")
    @GetMapping("/process-categories")
    public Map<String, Object> getProcessCategories() {
        Map<String, Object> response = new HashMap<>();
        
        try {
            // 从数据库获取流程分类配置
            List<WorkflowConfigDTO.ProcessCategoryDTO> categories = workflowConfigService.getProcessCategories();
            
            response.put("success", true);
            response.put("data", categories);
            response.put("message", "获取流程分类配置成功");
            
        } catch (Exception e) {
            log.error("获取流程分类配置失败", e);
            response.put("success", false);
            response.put("message", "获取流程分类配置失败: " + e.getMessage());
        }
        
        return response;
    }
    
    /**
     * 创建或更新流程分类
     */
    @ApiOperation("创建或更新流程分类")
    @PostMapping("/process-categories")
    public Map<String, Object> saveProcessCategory(
            @ApiParam("流程分类信息") @Valid @RequestBody WorkflowConfigDTO.ProcessCategoryDTO category) {
        
        Map<String, Object> response = new HashMap<>();
        
        try {
            // 保存流程分类到数据库
            WorkflowConfigDTO.ProcessCategoryDTO savedCategory = workflowConfigService.saveProcessCategory(category);
            
            log.info("保存流程分类: {}", category.getName());
            
            response.put("success", true);
            response.put("data", category);
            response.put("message", "保存流程分类成功");
            
        } catch (Exception e) {
            log.error("保存流程分类失败", e);
            response.put("success", false);
            response.put("message", "保存流程分类失败: " + e.getMessage());
        }
        
        return response;
    }
    
    /**
     * 获取通知模板
     */
    @ApiOperation("获取通知模板")
    @GetMapping("/notification-templates")
    public Map<String, Object> getNotificationTemplates(
            @ApiParam("模板类型") @RequestParam(required = false) String type) {
        
        Map<String, Object> response = new HashMap<>();
        
        try {
            // 从数据库获取通知模板
            List<WorkflowConfigDTO.NotificationTemplateDTO> templates = workflowConfigService.getNotificationTemplates(type);
            
            response.put("success", true);
            response.put("data", templates);
            response.put("message", "获取通知模板成功");
            
        } catch (Exception e) {
            log.error("获取通知模板失败", e);
            response.put("success", false);
            response.put("message", "获取通知模板失败: " + e.getMessage());
        }
        
        return response;
    }
    
    /**
     * 创建或更新通知模板
     */
    @ApiOperation("创建或更新通知模板")
    @PostMapping("/notification-templates")
    public Map<String, Object> saveNotificationTemplate(
            @ApiParam("通知模板信息") @Valid @RequestBody WorkflowConfigDTO.NotificationTemplateDTO template) {
        
        Map<String, Object> response = new HashMap<>();
        
        try {
            // 保存通知模板到数据库
            WorkflowConfigDTO.NotificationTemplateDTO savedTemplate = workflowConfigService.saveNotificationTemplate(template);
            
            log.info("保存通知模板: {}", template.getName());
            
            response.put("success", true);
            response.put("data", template);
            response.put("message", "保存通知模板成功");
            
        } catch (Exception e) {
            log.error("保存通知模板失败", e);
            response.put("success", false);
            response.put("message", "保存通知模板失败: " + e.getMessage());
        }
        
        return response;
    }
    

}