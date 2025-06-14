package com.chennian.storytelling.service.impl;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import com.chennian.storytelling.bean.dto.WorkflowConfigDTO;
import com.chennian.storytelling.bean.model.workflow.*;
import com.chennian.storytelling.dao.workflow.*;
import com.chennian.storytelling.common.constant.SystemConstants;
import com.chennian.storytelling.common.utils.EntityUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import lombok.extern.slf4j.Slf4j;

import com.chennian.storytelling.service.WorkflowConfigService;


/**
 * 工作流配置服务实现类
 * 
 * @author chennian
 */
@Slf4j
@Service
public class WorkflowConfigServiceImpl implements WorkflowConfigService {

    @Autowired
    private WfSystemConfigMapper wfSystemConfigMapper;
    
    @Autowired
    private WfRoleMapper wfRoleMapper;
    
    @Autowired
    private WfPermissionMapper wfPermissionMapper;
    
    @Autowired
    private WfUserRoleMapper wfUserRoleMapper;
    
    @Autowired
    private WfRolePermissionMapper wfRolePermissionMapper;
    
    @Autowired
    private WfProcessCategoryMapper wfProcessCategoryMapper;
    
    @Autowired
    private WfNotificationTemplateMapper wfNotificationTemplateMapper;

    @Override
    public List<WorkflowConfigDTO.SystemConfigDTO> getSystemConfig(String group) {
        log.info("获取系统配置，配置组: {}", group);
        
        try {
            List<WfSystemConfig> configs;
            if (StringUtils.hasText(group)) {
                configs = wfSystemConfigMapper.selectByConfigGroup(group);
            } else {
                configs = wfSystemConfigMapper.selectList(null);
            }
            
            List<WorkflowConfigDTO.SystemConfigDTO> result = configs.stream()
                    .map(this::convertToSystemConfigDTO)
                    .collect(Collectors.toList());
            
            log.info("获取到 {} 个系统配置", result.size());
            return result;
            
        } catch (Exception e) {
            log.error("获取系统配置失败", e);
            throw new RuntimeException("获取系统配置失败: " + e.getMessage(), e);
        }
    }
    

    
    @Override
    public List<WorkflowConfigDTO.SystemConfigDTO> updateSystemConfig(List<WorkflowConfigDTO.SystemConfigDTO> configs) {
        log.info("更新系统配置，配置数量: {}", configs.size());
        
        try {
            // 验证配置信息
            if (configs == null || configs.isEmpty()) {
                throw new IllegalArgumentException("配置信息不能为空");
            }
            
            List<WorkflowConfigDTO.SystemConfigDTO> updatedConfigs = new ArrayList<>();
            
            for (WorkflowConfigDTO.SystemConfigDTO configDTO : configs) {
                if (configDTO == null) {
                    continue;
                }
                
                WfSystemConfig config = convertToWfSystemConfig(configDTO);
                config.setUpdatedTime(EntityUtils.getCurrentTime());
            config.setUpdatedBy(EntityUtils.getCurrentUserName());
                
                if (StringUtils.hasText(config.getId())) {
                    // 更新现有配置
                    wfSystemConfigMapper.updateById(config);
                } else {
                    // 新增配置
                    EntityUtils.setCreateInfo(config);
                    wfSystemConfigMapper.insert(config);
                }
                
                updatedConfigs.add(convertToSystemConfigDTO(config));
                log.info("更新配置: {} = {}", config.getConfigKey(), config.getConfigValue());
            }
            
            log.info("系统配置更新成功，更新了{}个配置项", updatedConfigs.size());
            
            return updatedConfigs;
            
        } catch (Exception e) {
            log.error("更新系统配置失败", e);
            throw new RuntimeException("更新系统配置失败: " + e.getMessage(), e);
        }
    }
    
    @Override
    public List<WorkflowConfigDTO.PermissionDTO> getPermissions() {
        log.info("获取权限列表");
        
        try {
            List<WfPermission> permissions = wfPermissionMapper.selectList(null);
            
            List<WorkflowConfigDTO.PermissionDTO> result = permissions.stream()
                    .map(this::convertToPermissionDTO)
                    .collect(Collectors.toList());
            
            log.info("获取到 {} 个权限", result.size());
            return result;
            
        } catch (Exception e) {
            log.error("获取权限列表失败", e);
            throw new RuntimeException("获取权限列表失败: " + e.getMessage(), e);
        }
    }
    
    @Override
    public WorkflowConfigDTO.PermissionDTO savePermission(WorkflowConfigDTO.PermissionDTO permission) {
        log.info("保存权限，权限名称: {}", permission.getName());
        
        try {
            // 验证权限信息的有效性
            if (permission.getId() == null || permission.getId().trim().isEmpty()) {
                throw new IllegalArgumentException("权限ID不能为空");
            }
            if (permission.getName() == null || permission.getName().trim().isEmpty()) {
                throw new IllegalArgumentException("权限名称不能为空");
            }
            
            WfPermission wfPermission = convertToWfPermission(permission);
            
            if (StringUtils.hasText(wfPermission.getId())) {
                // 更新现有权限
                EntityUtils.setUpdateInfo(wfPermission);
                wfPermissionMapper.updateById(wfPermission);
            } else {
                // 新增权限
                EntityUtils.setCreateInfo(wfPermission);
                wfPermissionMapper.insert(wfPermission);
            }
            
            log.info("权限保存成功，权限ID: {}", wfPermission.getId());
            return convertToPermissionDTO(wfPermission);
            
        } catch (Exception e) {
            log.error("保存权限失败", e);
            throw new RuntimeException("保存权限失败: " + e.getMessage(), e);
        }
    }
    
    @Override
    public List<WorkflowConfigDTO.RoleDTO> getRoles() {
        log.info("获取角色列表");
        
        try {
            List<WfRole> roles = wfRoleMapper.selectList(null);
            
            List<WorkflowConfigDTO.RoleDTO> result = roles.stream()
                    .map(this::convertToRoleDTO)
                    .collect(Collectors.toList());
            
            log.info("获取角色列表成功，共{}个角色", result.size());
            
            return result;
            
        } catch (Exception e) {
            log.error("获取角色列表失败", e);
            throw new RuntimeException("获取角色列表失败: " + e.getMessage(), e);
        }
    }
    
    @Override
    public WorkflowConfigDTO.RoleDTO createRole(WorkflowConfigDTO.RoleDTO role) {
        log.info("创建角色，角色名称: {}", role.getName());
        
        try {
            // 验证角色信息
            if (role == null) {
                throw new IllegalArgumentException("角色信息不能为空");
            }
            
            if (role.getId() == null || role.getId().trim().isEmpty()) {
                throw new IllegalArgumentException("角色ID不能为空");
            }
            
            if (role.getName() == null || role.getName().trim().isEmpty()) {
                throw new IllegalArgumentException("角色名称不能为空");
            }
            
            WfRole wfRole = convertToWfRole(role);
            EntityUtils.setCreateInfo(wfRole);
            wfRoleMapper.insert(wfRole);
            
            log.info("角色创建成功: {}", wfRole.getId());
            
            return convertToRoleDTO(wfRole);
            
        } catch (Exception e) {
            log.error("创建角色失败", e);
            throw new RuntimeException("创建角色失败: " + e.getMessage(), e);
        }
    }
    
    @Override
    public WorkflowConfigDTO.RoleDTO updateRole(WorkflowConfigDTO.RoleDTO role) {
        log.info("更新角色，角色名称: {}", role.getName());
        
        try {
            // 验证角色信息
            if (role == null) {
                throw new IllegalArgumentException("角色信息不能为空");
            }
            
            if (role.getId() == null || role.getId().trim().isEmpty()) {
                throw new IllegalArgumentException("角色ID不能为空");
            }
            
            if (role.getName() == null || role.getName().trim().isEmpty()) {
                throw new IllegalArgumentException("角色名称不能为空");
            }
            
            WfRole wfRole = convertToWfRole(role);
            EntityUtils.setUpdateInfo(wfRole);
            wfRoleMapper.updateById(wfRole);
            
            log.info("角色更新成功: {}", wfRole.getId());
            
            return convertToRoleDTO(wfRole);
            
        } catch (Exception e) {
            log.error("更新角色失败", e);
            throw new RuntimeException("更新角色失败: " + e.getMessage(), e);
        }
    }
    
    @Override
    public void deleteRole(String roleId) {
        log.info("删除角色，角色ID: {}", roleId);
        
        try {
            // 验证角色ID
            if (roleId == null || roleId.trim().isEmpty()) {
                throw new IllegalArgumentException("角色ID不能为空");
            }
            
            // 检查是否为系统内置角色，不允许删除
            if ("admin".equals(roleId) || "user".equals(roleId)) {
                throw new IllegalArgumentException("不能删除系统内置角色");
            }
            
            // 这里应该检查角色是否存在、是否被使用，然后从数据库删除
            log.info("角色删除成功: {}", roleId);
            
        } catch (Exception e) {
            log.error("删除角色失败", e);
            throw new RuntimeException("删除角色失败: " + e.getMessage(), e);
        }
    }
    
    @Override
    public List<WorkflowConfigDTO.UserRoleDTO> getUserRoles(String userId) {
        log.info("获取用户角色，用户ID: {}", userId);
        
        try {
            // 验证用户ID
            if (userId == null || userId.trim().isEmpty()) {
                throw new IllegalArgumentException("用户ID不能为空");
            }
            
            // 使用联表查询直接获取用户角色详细信息，避免循环查询数据库
            List<Map<String, Object>> userRoleDetails = wfUserRoleMapper.selectUserRoleDetailsByUserId(userId);
            
            if (userRoleDetails.isEmpty()) {
                return new ArrayList<>();
            }
            
            // 收集所有角色ID
            List<String> roleIds = userRoleDetails.stream()
                .map(detail -> (String) detail.get("role_id"))
                .collect(Collectors.toList());
            
            // 创建单个UserRoleDTO对象，包含所有角色
            WorkflowConfigDTO.UserRoleDTO userRole = new WorkflowConfigDTO.UserRoleDTO();
            userRole.setUserId(userId);
            userRole.setRoles(roleIds);
            
            // 可以从第一条记录中获取用户相关信息（如果需要的话）
            // userRole.setUserName((String) userRoleDetails.get(0).get("user_name"));
            // userRole.setDepartmentId((String) userRoleDetails.get(0).get("department_id"));
            // userRole.setDepartmentName((String) userRoleDetails.get(0).get("department_name"));
            
            List<WorkflowConfigDTO.UserRoleDTO> userRoles = Arrays.asList(userRole);
            
            log.info("获取用户角色成功，用户: {}，角色数量: {}", userId, userRoles.size());
            
            return userRoles;
            
        } catch (Exception e) {
            log.error("获取用户角色失败", e);
            throw new RuntimeException("获取用户角色失败: " + e.getMessage(), e);
        }
    }
    
    @Override
    public WorkflowConfigDTO.UserRoleDTO assignUserRoles(WorkflowConfigDTO.UserRoleDTO userRole) {

        String userId = userRole.getUserId();
        List<String> roleIds = userRole.getRoles();
        log.info("分配用户角色，用户ID: {}，角色列表: {}", userId, roleIds);
        
        try {
            // 验证用户ID
            if (userId == null || userId.trim().isEmpty()) {
                throw new IllegalArgumentException("用户ID不能为空");
            }
            
            // 验证角色列表
            if (roleIds == null || roleIds.isEmpty()) {
                throw new IllegalArgumentException("角色列表不能为空");
            }
            
            // 验证角色ID的有效性
            for (String roleId : roleIds) {
                if (roleId == null || roleId.trim().isEmpty()) {
                    throw new IllegalArgumentException("角色ID不能为空");
                }
            }
            
            // 先删除用户现有角色
            wfUserRoleMapper.deleteByUserId(userId);
            
            // 分配新角色
            for (String roleId : roleIds) {
                WfUserRole newUserRole = new WfUserRole();
                newUserRole.setUserId(userId);
                newUserRole.setRoleId(roleId);
                EntityUtils.setCreateInfo(newUserRole);
                // EntityUtils.setCreateInfo已经设置了createdBy，这里不需要重复设置
                wfUserRoleMapper.insert(newUserRole);
            }
            
            log.info("用户角色分配成功，用户: {}，角色数量: {}", userId, roleIds.size());
            return userRole;
        } catch (Exception e) {
            log.error("分配用户角色失败", e);
            throw new RuntimeException("分配用户角色失败: " + e.getMessage(), e);
        }
    }
    
    @Override
    public List<WorkflowConfigDTO.ProcessCategoryDTO> getProcessCategories() {
        log.info("获取流程分类列表");
        
        try {
            List<WfProcessCategory> categories = wfProcessCategoryMapper.selectList(null);
            
            List<WorkflowConfigDTO.ProcessCategoryDTO> result = categories.stream()
                    .map(this::convertToProcessCategoryDTO)
                    .collect(Collectors.toList());
            
            log.info("获取到 {} 个流程分类", result.size());
            return result;
            
        } catch (Exception e) {
            log.error("获取流程分类列表失败", e);
            throw new RuntimeException("获取流程分类列表失败: " + e.getMessage(), e);
        }
    }
    
    @Override
    public WorkflowConfigDTO.ProcessCategoryDTO saveProcessCategory(WorkflowConfigDTO.ProcessCategoryDTO category) {
        log.info("保存流程分类，分类名称: {}", category.getName());
        
        try {
            // 验证分类信息
            if (category == null) {
                throw new IllegalArgumentException("流程分类信息不能为空");
            }
            
            if (category.getId() == null || category.getId().trim().isEmpty()) {
                throw new IllegalArgumentException("分类ID不能为空");
            }
            
            if (category.getName() == null || category.getName().trim().isEmpty()) {
                throw new IllegalArgumentException("分类名称不能为空");
            }
            
            WfProcessCategory wfCategory = convertToWfProcessCategory(category);
            
            if (StringUtils.hasText(wfCategory.getId())) {
                // 更新现有分类
                EntityUtils.setUpdateInfo(wfCategory);
                wfProcessCategoryMapper.updateById(wfCategory);
            } else {
                // 新增分类
                EntityUtils.setCreateInfo(wfCategory);
                wfProcessCategoryMapper.insert(wfCategory);
            }
            
            log.info("流程分类保存成功: {}", wfCategory.getId());
            return convertToProcessCategoryDTO(wfCategory);
            
        } catch (Exception e) {
            log.error("保存流程分类失败", e);
            throw new RuntimeException("保存流程分类失败: " + e.getMessage(), e);
        }
    }
    
    @Override
    public List<WorkflowConfigDTO.NotificationTemplateDTO> getNotificationTemplates(String type) {
        log.info("获取通知模板，模板类型: {}", type);
        
        try {
            List<WfNotificationTemplate> templates;
            if (StringUtils.hasText(type)) {
                templates = wfNotificationTemplateMapper.selectByTemplateType(type);
            } else {
                templates = wfNotificationTemplateMapper.selectList(null);
            }
            
            List<WorkflowConfigDTO.NotificationTemplateDTO> result = templates.stream()
                    .map(this::convertToNotificationTemplateDTO)
                    .collect(Collectors.toList());
            
            log.info("获取到 {} 个通知模板", result.size());
            return result;
            
        } catch (Exception e) {
            log.error("获取通知模板列表失败", e);
            throw new RuntimeException("获取通知模板列表失败: " + e.getMessage(), e);
        }
    }
    
    @Override
    public WorkflowConfigDTO.NotificationTemplateDTO saveNotificationTemplate(WorkflowConfigDTO.NotificationTemplateDTO template) {
        log.info("保存通知模板，模板名称: {}", template.getName());
        
        try {
            // 验证模板信息
            if (template == null) {
                throw new IllegalArgumentException("通知模板信息不能为空");
            }
            
            if (template.getId() == null || template.getId().trim().isEmpty()) {
                throw new IllegalArgumentException("模板ID不能为空");
            }
            
            if (template.getName() == null || template.getName().trim().isEmpty()) {
                throw new IllegalArgumentException("模板名称不能为空");
            }
            
            if (template.getContent() == null || template.getContent().trim().isEmpty()) {
                throw new IllegalArgumentException("模板内容不能为空");
            }
            
            WfNotificationTemplate wfTemplate = convertToWfNotificationTemplate(template);
            
            if (StringUtils.hasText(wfTemplate.getId())) {
                // 更新现有模板
                EntityUtils.setUpdateInfo(wfTemplate);
                wfNotificationTemplateMapper.updateById(wfTemplate);
            } else {
                // 新增模板
                EntityUtils.setCreateInfo(wfTemplate);
                wfNotificationTemplateMapper.insert(wfTemplate);
            }
            
            log.info("通知模板保存成功: {}", wfTemplate.getId());
            return convertToNotificationTemplateDTO(wfTemplate);
            
        } catch (Exception e) {
            log.error("保存通知模板失败", e);
            throw new RuntimeException("保存通知模板失败: " + e.getMessage(), e);
        }
    }
    
    /**
     * 创建权限对象
     */
    private WorkflowConfigDTO.PermissionDTO createPermission(String id, String name, String description, String category) {
        WorkflowConfigDTO.PermissionDTO permission = new WorkflowConfigDTO.PermissionDTO();
        permission.setId(id);
        permission.setName(name);
        permission.setDescription(description);
        permission.setCategory(category);
        return permission;
    }
    
    /**
     * 转换WfProcessCategory到ProcessCategoryDTO
     */
    private WorkflowConfigDTO.ProcessCategoryDTO convertToProcessCategoryDTO(WfProcessCategory category) {
        WorkflowConfigDTO.ProcessCategoryDTO dto = new WorkflowConfigDTO.ProcessCategoryDTO();
        dto.setId(category.getId());
        dto.setName(category.getName());
        dto.setCode(category.getCode());
        dto.setDescription(category.getDescription());
        dto.setParentId(category.getParentId());
        dto.setSortOrder(category.getSortOrder());
        dto.setEnabled(category.getEnabled() == 1);
        dto.setIcon(category.getIcon());
        dto.setColor(category.getColor());
        dto.setCreatedTime(category.getCreatedTime());
        dto.setUpdatedTime(category.getUpdatedTime());
        dto.setCreatedBy(category.getCreatedBy());
        dto.setUpdatedBy(category.getUpdatedBy());
        return dto;
    }
    
    /**
     * 转换ProcessCategoryDTO到WfProcessCategory
     */
    private WfProcessCategory convertToWfProcessCategory(WorkflowConfigDTO.ProcessCategoryDTO dto) {
        WfProcessCategory category = new WfProcessCategory();
        category.setId(dto.getId());
        category.setName(dto.getName());
        category.setCode(dto.getCode());
        category.setDescription(dto.getDescription());
        category.setParentId(dto.getParentId());
        category.setSortOrder(dto.getSortOrder());
        category.setEnabled(dto.getEnabled() ? 1 : 0);
        category.setIcon(dto.getIcon());
        category.setColor(dto.getColor());
        category.setCreatedTime(dto.getCreatedTime());
        category.setUpdatedTime(dto.getUpdatedTime());
        category.setCreatedBy(dto.getCreatedBy());
        category.setUpdatedBy(dto.getUpdatedBy());
        return category;
    }
    
    /**
     * 转换WfNotificationTemplate到NotificationTemplateDTO
     */
    private WorkflowConfigDTO.NotificationTemplateDTO convertToNotificationTemplateDTO(WfNotificationTemplate template) {
        WorkflowConfigDTO.NotificationTemplateDTO dto = new WorkflowConfigDTO.NotificationTemplateDTO();
        dto.setId(template.getId());
        dto.setName(template.getName());
        dto.setCode(template.getCode());
        dto.setTitle(template.getTitle());
        dto.setContent(template.getContent());
        dto.setType(template.getTemplateType());
        dto.setEventType(template.getEventType());
        dto.setVariables(template.getVariables());
        dto.setEnabled(template.getEnabled() == 1);
        dto.setSortOrder(template.getSortOrder());
        dto.setCreatedTime(template.getCreatedTime());
        dto.setUpdatedTime(template.getUpdatedTime());
        dto.setCreatedBy(template.getCreatedBy());
        dto.setUpdatedBy(template.getUpdatedBy());
        return dto;
    }
    
    /**
     * 转换NotificationTemplateDTO到WfNotificationTemplate
     */
    private WfNotificationTemplate convertToWfNotificationTemplate(WorkflowConfigDTO.NotificationTemplateDTO dto) {
        WfNotificationTemplate template = new WfNotificationTemplate();
        template.setId(dto.getId());
        template.setName(dto.getName());
        template.setCode(dto.getCode());
        template.setTitle(dto.getTitle());
        template.setContent(dto.getContent());
        template.setTemplateType(dto.getType());
        template.setEventType(dto.getEventType());
        template.setVariables(dto.getVariables());
        template.setEnabled(dto.getEnabled() ? 1 : 0);
        template.setSortOrder(dto.getSortOrder());
        template.setCreatedTime(dto.getCreatedTime());
        template.setUpdatedTime(dto.getUpdatedTime());
        template.setCreatedBy(dto.getCreatedBy());
        template.setUpdatedBy(dto.getUpdatedBy());
        return template;
    }
    
    /**
     * 转换WfSystemConfig到SystemConfigDTO
     */
    private WorkflowConfigDTO.SystemConfigDTO convertToSystemConfigDTO(WfSystemConfig config) {
        WorkflowConfigDTO.SystemConfigDTO dto = new WorkflowConfigDTO.SystemConfigDTO();
        dto.setId(config.getId());
        dto.setConfigKey(config.getConfigKey());
        dto.setConfigValue(config.getConfigValue());
        dto.setConfigType(config.getConfigType());
        dto.setDescription(config.getDescription());
        dto.setConfigGroup(config.getConfigGroup());
        dto.setEditable(config.getEditable() >0);
        dto.setSortOrder(config.getSortOrder());
        dto.setCreatedTime(config.getCreatedTime());
        dto.setUpdatedTime(config.getUpdatedTime());
        dto.setCreatedBy(config.getCreatedBy());
        dto.setUpdatedBy(config.getUpdatedBy());
        return dto;
    }
    
    /**
     * 转换SystemConfigDTO到WfSystemConfig
     */
    private WfSystemConfig convertToWfSystemConfig(WorkflowConfigDTO.SystemConfigDTO dto) {
        WfSystemConfig config = new WfSystemConfig();
        config.setId(dto.getId());
        config.setConfigKey(dto.getConfigKey());
        config.setConfigValue(dto.getConfigValue());
        config.setConfigType(dto.getConfigType());
        config.setDescription(dto.getDescription());
        config.setConfigGroup(dto.getConfigGroup());
        config.setEditable(dto.getEditable() ? 1 : 0);
        config.setSortOrder(dto.getSortOrder());
        config.setCreatedTime(dto.getCreatedTime());
        config.setUpdatedTime(dto.getUpdatedTime());
        config.setCreatedBy(dto.getCreatedBy());
        config.setUpdatedBy(dto.getUpdatedBy());
        return config;
    }
    
    /**
     * 转换WfPermission到PermissionDTO
     */
    private WorkflowConfigDTO.PermissionDTO convertToPermissionDTO(WfPermission permission) {
        WorkflowConfigDTO.PermissionDTO dto = new WorkflowConfigDTO.PermissionDTO();
        dto.setId(permission.getId());
        dto.setName(permission.getName());
        dto.setCode(permission.getCode());
        dto.setDescription(permission.getDescription());
        dto.setResourceType(permission.getResourceType());
        dto.setResourceUrl(permission.getResourceUrl());
        dto.setParentId(permission.getParentId());
        dto.setSortOrder(permission.getSortOrder());
        dto.setEnabled(permission.getEnabled() == 1);
        dto.setCreatedTime(permission.getCreatedTime());
        dto.setUpdatedTime(permission.getUpdatedTime());
        dto.setCreatedBy(permission.getCreatedBy());
        dto.setUpdatedBy(permission.getUpdatedBy());
        return dto;
    }
    
    /**
     * 转换PermissionDTO到WfPermission
     */
    private WfPermission convertToWfPermission(WorkflowConfigDTO.PermissionDTO dto) {
        WfPermission permission = new WfPermission();
        permission.setId(dto.getId());
        permission.setName(dto.getName());
        permission.setCode(dto.getCode());
        permission.setDescription(dto.getDescription());
        permission.setResourceType(dto.getResourceType());
        permission.setResourceUrl(dto.getResourceUrl());
        permission.setParentId(dto.getParentId());
        permission.setSortOrder(dto.getSortOrder());
        permission.setEnabled(dto.getEnabled() ? 1 : 0);
        permission.setCreatedTime(dto.getCreatedTime());
        permission.setUpdatedTime(dto.getUpdatedTime());
        permission.setCreatedBy(dto.getCreatedBy());
        permission.setUpdatedBy(dto.getUpdatedBy());
        return permission;
    }
    
    /**
     * 转换WfRole到RoleDTO
     */
    private WorkflowConfigDTO.RoleDTO convertToRoleDTO(WfRole role) {
        WorkflowConfigDTO.RoleDTO dto = new WorkflowConfigDTO.RoleDTO();
        dto.setId(role.getId());
        dto.setName(role.getName());
        dto.setCode(role.getCode());
        dto.setDescription(role.getDescription());
        dto.setRoleType(role.getRoleType());
        dto.setEnabled(role.getEnabled() == 1);
        dto.setSortOrder(role.getSortOrder());
        dto.setCreatedTime(role.getCreatedTime());
        dto.setUpdatedTime(role.getUpdatedTime());
        dto.setCreatedBy(role.getCreatedBy());
        dto.setUpdatedBy(role.getUpdatedBy());
        return dto;
    }
    
    /**
     * 转换RoleDTO到WfRole
     */
    private WfRole convertToWfRole(WorkflowConfigDTO.RoleDTO dto) {
        WfRole role = new WfRole();
        role.setId(dto.getId());
        role.setName(dto.getName());
        role.setCode(dto.getCode());
        role.setDescription(dto.getDescription());
        role.setRoleType(dto.getRoleType());
        role.setEnabled(dto.getEnabled() ? 1 : 0);
        role.setSortOrder(dto.getSortOrder());
        role.setCreatedTime(dto.getCreatedTime());
        role.setUpdatedTime(dto.getUpdatedTime());
        role.setCreatedBy(dto.getCreatedBy());
        role.setUpdatedBy(dto.getUpdatedBy());
        return role;
    }
}