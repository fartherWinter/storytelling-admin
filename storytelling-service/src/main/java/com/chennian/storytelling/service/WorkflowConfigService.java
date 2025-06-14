package com.chennian.storytelling.service;


import com.chennian.storytelling.bean.dto.WorkflowConfigDTO;

import java.util.List;

/**
 * 工作流配置服务接口
 * 
 * @author chennian
 */
public interface WorkflowConfigService {

    /**
     * 获取系统配置
     * 
     * @param group 配置组
     * @return 系统配置列表
     */
    List<WorkflowConfigDTO.SystemConfigDTO> getSystemConfig(String group);

    /**
     * 更新系统配置
     * 
     * @param configs 配置列表
     * @return 更新后的配置列表
     */
    List<WorkflowConfigDTO.SystemConfigDTO> updateSystemConfig(List<WorkflowConfigDTO.SystemConfigDTO> configs);
    
    /**
     * 获取权限列表
     * 
     * @return 权限列表
     */
    List<WorkflowConfigDTO.PermissionDTO> getPermissions();
    
    /**
     * 保存权限
     * 
     * @param permission 权限信息
     * @return 保存后的权限信息
     */
    WorkflowConfigDTO.PermissionDTO savePermission(WorkflowConfigDTO.PermissionDTO permission);
    
    /**
     * 获取角色列表
     * 
     * @return 角色列表
     */
    List<WorkflowConfigDTO.RoleDTO> getRoles();
    
    /**
     * 创建角色
     * 
     * @param role 角色信息
     * @return 创建后的角色信息
     */
    WorkflowConfigDTO.RoleDTO createRole(WorkflowConfigDTO.RoleDTO role);
    
    /**
     * 更新角色
     * 
     * @param role 角色信息
     * @return 更新后的角色信息
     */
    WorkflowConfigDTO.RoleDTO updateRole(WorkflowConfigDTO.RoleDTO role);
    
    /**
     * 删除角色
     * 
     * @param roleId 角色ID
     */
    void deleteRole(String roleId);
    
    /**
     * 获取用户角色分配
     * 
     * @param userId 用户ID* @return 用户角色列表
     */
    List<WorkflowConfigDTO.UserRoleDTO> getUserRoles(String userId);
    
    /**
     * 分配用户角色
     * 
     * @param userRole 用户角色信息
     * @return 分配后的用户角色信息
     */
    WorkflowConfigDTO.UserRoleDTO assignUserRoles(WorkflowConfigDTO.UserRoleDTO userRole);

    /**
     * 获取流程分类配置
     * 
     * @return 流程分类列表
     */
    List<WorkflowConfigDTO.ProcessCategoryDTO> getProcessCategories();
    
    /**
     * 保存流程分类
     * 
     * @param category 流程分类信息
     * @return 保存后的流程分类信息
     */
    WorkflowConfigDTO.ProcessCategoryDTO saveProcessCategory(WorkflowConfigDTO.ProcessCategoryDTO category);
    
    /**
     * 获取通知模板
     * 
     * @param type 模板类型
     * @return 通知模板列表
     */
    List<WorkflowConfigDTO.NotificationTemplateDTO> getNotificationTemplates(String type);
    
    /**
     * 保存通知模板
     * 
     * @param template 通知模板信息
     * @return 保存后的通知模板信息
     */
    WorkflowConfigDTO.NotificationTemplateDTO saveNotificationTemplate(WorkflowConfigDTO.NotificationTemplateDTO template);
}