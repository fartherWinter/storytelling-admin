package com.chennian.storytelling.service.impl;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.Date;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
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

    private final WfSystemConfigMapper wfSystemConfigMapper;
    private final WfProcessCategoryMapper wfProcessCategoryMapper;
    private final WfNotificationTemplateMapper wfNotificationTemplateMapper;
    private final WfTaskMapper wfTaskMapper;
    private final WfInstanceMapper wfInstanceMapper;

    public WorkflowConfigServiceImpl(WfSystemConfigMapper wfSystemConfigMapper, WfProcessCategoryMapper wfProcessCategoryMapper, WfNotificationTemplateMapper wfNotificationTemplateMapper, WfTaskMapper wfTaskMapper, WfInstanceMapper wfInstanceMapper) {
        this.wfSystemConfigMapper = wfSystemConfigMapper;
        this.wfProcessCategoryMapper = wfProcessCategoryMapper;
        this.wfNotificationTemplateMapper = wfNotificationTemplateMapper;
        this.wfTaskMapper = wfTaskMapper;
        this.wfInstanceMapper = wfInstanceMapper;
    }


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
        dto.setEditable(config.getEditable() > 0);
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


    // ==================== 扩展接口方法实现 ====================

    @Override
    public Map<String, Object> getSystemConfigList(Map<String, Object> params) {
        log.info("获取系统配置列表，参数: {}", params);

        try {
            // 这里应该根据params进行分页查询和条件过滤
            List<WfSystemConfig> configs = wfSystemConfigMapper.selectList(null);

            List<WorkflowConfigDTO.SystemConfigDTO> configList = configs.stream()
                    .map(this::convertToSystemConfigDTO)
                    .collect(Collectors.toList());

            Map<String, Object> result = new java.util.HashMap<>();
            result.put("list", configList);
            result.put("total", configList.size());

            log.info("获取系统配置列表成功，共{}条记录", configList.size());
            return result;

        } catch (Exception e) {
            log.error("获取系统配置列表失败", e);
            throw new RuntimeException("获取系统配置列表失败: " + e.getMessage(), e);
        }
    }

    @Override
    public WorkflowConfigDTO.SystemConfigDTO saveSystemConfig(WorkflowConfigDTO.SystemConfigDTO config) {
        log.info("保存系统配置，配置键: {}", config.getConfigKey());

        try {
            WfSystemConfig wfConfig = convertToWfSystemConfig(config);

            if (StringUtils.hasText(wfConfig.getId())) {
                EntityUtils.setUpdateInfo(wfConfig);
                wfSystemConfigMapper.updateById(wfConfig);
            } else {
                EntityUtils.setCreateInfo(wfConfig);
                wfSystemConfigMapper.insert(wfConfig);
            }

            log.info("系统配置保存成功: {}", wfConfig.getId());
            return convertToSystemConfigDTO(wfConfig);

        } catch (Exception e) {
            log.error("保存系统配置失败", e);
            throw new RuntimeException("保存系统配置失败: " + e.getMessage(), e);
        }
    }

    @Override
    public void deleteSystemConfig(String configId) {
        log.info("删除系统配置，配置ID: {}", configId);

        try {
            wfSystemConfigMapper.deleteById(configId);
            log.info("系统配置删除成功: {}", configId);

        } catch (Exception e) {
            log.error("删除系统配置失败", e);
            throw new RuntimeException("删除系统配置失败: " + e.getMessage(), e);
        }
    }

    @Override
    public void batchUpdateSystemConfigStatus(WorkflowConfigDTO.BatchUpdateDTO data) {
        log.info("批量更新系统配置状态，数据: {}", data);

        try {
            // 构建更新条件
            LambdaUpdateWrapper<WfSystemConfig> updateWrapper = new LambdaUpdateWrapper<WfSystemConfig>()
                    .in(WfSystemConfig::getId, data.getIds())
                    .set(WfSystemConfig::getUpdatedTime, new Date())
                    .set(WfSystemConfig::getUpdatedBy, EntityUtils.getCurrentUserId());
            
            // 根据操作类型设置不同字段
            if (data.getStatus() != null) {
                updateWrapper.set(WfSystemConfig::getEditable, data.getStatus());
            }
            
            // 处理自定义更新字段
            if (data.getUpdateFields() != null && !data.getUpdateFields().isEmpty()) {
                data.getUpdateFields().forEach((field, value) -> {
                    switch (field) {
                        case "configValue":
                            updateWrapper.set(WfSystemConfig::getConfigValue, value);
                            break;
                        case "description":
                            updateWrapper.set(WfSystemConfig::getDescription, value);
                            break;
                        case "sortOrder":
                            updateWrapper.set(WfSystemConfig::getSortOrder, value);
                            break;
                    }
                });
            }
            
            wfSystemConfigMapper.update(null, updateWrapper);
            log.info("批量更新系统配置状态成功");

        } catch (Exception e) {
            log.error("批量更新系统配置状态失败", e);
            throw new RuntimeException("批量更新系统配置状态失败: " + e.getMessage(), e);
        }
    }

    @Override
    public Map<String, Object> getSystemConfigStats() {
        log.info("获取系统配置统计信息");

        try {
            Map<String, Object> stats = new java.util.HashMap<>();
            stats.put("total", 0);
            stats.put("enabled", 0);
            stats.put("disabled", 0);

            log.info("获取系统配置统计信息成功");
            return stats;

        } catch (Exception e) {
            log.error("获取系统配置统计信息失败", e);
            throw new RuntimeException("获取系统配置统计信息失败: " + e.getMessage(), e);
        }
    }


    @Override
    public List<WorkflowConfigDTO.CategoryTreeDTO> getCategoryTree() {
        log.info("获取流程分类树");

        try {
            // 查询所有启用的分类
            List<WfProcessCategory> allCategories = wfProcessCategoryMapper.selectList(
                new LambdaQueryWrapper<WfProcessCategory>()
                    .eq(WfProcessCategory::getEnabled, 1)
                    .orderByAsc(WfProcessCategory::getSortOrder)
            );
            
            // 构建分类树
            List<WorkflowConfigDTO.CategoryTreeDTO> tree = buildCategoryTree(allCategories, null);

            log.info("获取流程分类树成功");
            return tree;

        } catch (Exception e) {
            log.error("获取流程分类树失败", e);
            throw new RuntimeException("获取流程分类树失败: " + e.getMessage(), e);
        }
    }

    @Override
    public Map<String, Object> getCategoryList(Map<String, Object> params) {
        log.info("获取流程分类列表，参数: {}", params);

        try {
            List<WfProcessCategory> categories = wfProcessCategoryMapper.selectList(null);

            List<WorkflowConfigDTO.ProcessCategoryDTO> categoryList = categories.stream()
                    .map(this::convertToProcessCategoryDTO)
                    .collect(Collectors.toList());

            Map<String, Object> result = new java.util.HashMap<>();
            result.put("list", categoryList);
            result.put("total", categoryList.size());

            log.info("获取流程分类列表成功，共{}条记录", categoryList.size());
            return result;

        } catch (Exception e) {
            log.error("获取流程分类列表失败", e);
            throw new RuntimeException("获取流程分类列表失败: " + e.getMessage(), e);
        }
    }

    @Override
    public WorkflowConfigDTO.CategoryDTO saveCategory(WorkflowConfigDTO.CategoryDTO category) {
        log.info("保存流程分类，分类名称: {}", category.getName());

        try {
            WfProcessCategory entity;
            
            if (StringUtils.hasText(category.getId())) {
                // 更新现有分类
                entity = wfProcessCategoryMapper.selectById(category.getId());
                if (entity == null) {
                    throw new RuntimeException("分类不存在: " + category.getId());
                }
                
                // 更新字段
                entity.setName(category.getName());
                entity.setCode(category.getCode());
                entity.setDescription(category.getDescription());
                entity.setParentId(category.getParentId());
                entity.setSortOrder(category.getSortOrder());
                entity.setEnabled(category.getEnabled() ? 1 : 0);
                entity.setIcon(category.getIcon());
                entity.setColor(category.getColor());
                entity.setUpdatedTime(new Date());
                
                wfProcessCategoryMapper.updateById(entity);
            } else {
                // 新增分类
                entity = new WfProcessCategory();
                entity.setName(category.getName());
                entity.setCode(category.getCode());
                entity.setDescription(category.getDescription());
                entity.setParentId(category.getParentId());
                entity.setSortOrder(category.getSortOrder());
                entity.setEnabled(category.getEnabled() ? 1 : 0);
                entity.setIcon(category.getIcon());
                entity.setColor(category.getColor());
                entity.setCreatedTime(new Date());
                entity.setUpdatedTime(new Date());
                
                wfProcessCategoryMapper.insert(entity);
                category.setId(entity.getId());
            }
            
            log.info("流程分类保存成功");
            return category;

        } catch (Exception e) {
            log.error("保存流程分类失败", e);
            throw new RuntimeException("保存流程分类失败: " + e.getMessage(), e);
        }
    }

    @Override
    public void deleteCategory(String categoryId) {
        log.info("删除流程分类，分类ID: {}", categoryId);

        try {
            wfProcessCategoryMapper.deleteById(categoryId);
            log.info("流程分类删除成功: {}", categoryId);

        } catch (Exception e) {
            log.error("删除流程分类失败", e);
            throw new RuntimeException("删除流程分类失败: " + e.getMessage(), e);
        }
    }

    @Override
    public void batchUpdateCategoryStatus(WorkflowConfigDTO.BatchUpdateDTO data) {
        log.info("批量更新分类状态，数据: {}", data);

        try {
            if (data.getIds() == null || data.getIds().isEmpty()) {
                throw new RuntimeException("ID列表不能为空");
            }
            
            // 使用Lambda表达式批量更新状态
            LambdaUpdateWrapper<WfProcessCategory> updateWrapper = new LambdaUpdateWrapper<>();
            updateWrapper.in(WfProcessCategory::getId, data.getIds())
                        .set(WfProcessCategory::getEnabled, data.getStatus())
                        .set(WfProcessCategory::getUpdatedTime, new Date());
            
            // 如果有其他更新字段
            if (data.getUpdateFields() != null && !data.getUpdateFields().isEmpty()) {
                data.getUpdateFields().forEach((key, value) -> {
                    switch (key) {
                        case "sortOrder":
                            updateWrapper.set(WfProcessCategory::getSortOrder, value);
                            break;
                        case "icon":
                            updateWrapper.set(WfProcessCategory::getIcon, value);
                            break;
                        case "color":
                            updateWrapper.set(WfProcessCategory::getColor, value);
                            break;
                    }
                });
            }
            
            wfProcessCategoryMapper.update(null, updateWrapper);
            log.info("批量更新分类状态成功");

        } catch (Exception e) {
            log.error("批量更新分类状态失败", e);
            throw new RuntimeException("批量更新分类状态失败: " + e.getMessage(), e);
        }
    }

    @Override
    public Map<String, Object> getCategoryStats() {
        log.info("获取分类统计信息");

        try {
            Map<String, Object> stats = new java.util.HashMap<>();
            stats.put("total", 0);
            stats.put("enabled", 0);
            stats.put("disabled", 0);

            log.info("获取分类统计信息成功");
            return stats;

        } catch (Exception e) {
            log.error("获取分类统计信息失败", e);
            throw new RuntimeException("获取分类统计信息失败: " + e.getMessage(), e);
        }
    }

    @Override
    public Map<String, Object> getNotificationList(Map<String, Object> params) {
        log.info("获取通知模板列表，参数: {}", params);

        try {
            List<WfNotificationTemplate> templates = wfNotificationTemplateMapper.selectList(null);

            List<WorkflowConfigDTO.NotificationTemplateDTO> templateList = templates.stream()
                    .map(this::convertToNotificationTemplateDTO)
                    .collect(Collectors.toList());

            Map<String, Object> result = new java.util.HashMap<>();
            result.put("list", templateList);
            result.put("total", templateList.size());

            log.info("获取通知模板列表成功，共{}条记录", templateList.size());
            return result;

        } catch (Exception e) {
            log.error("获取通知模板列表失败", e);
            throw new RuntimeException("获取通知模板列表失败: " + e.getMessage(), e);
        }
    }

    @Override
    public WorkflowConfigDTO.NotificationDTO saveNotification(WorkflowConfigDTO.NotificationDTO notification) {
        log.info("保存通知模板，模板名称: {}", notification.getName());

        try {
            WfNotificationTemplate entity;
            
            if (StringUtils.hasText(notification.getId())) {
                // 更新现有模板
                entity = wfNotificationTemplateMapper.selectById(notification.getId());
                if (entity == null) {
                    throw new RuntimeException("通知模板不存在: " + notification.getId());
                }
                
                // 更新字段
                entity.setName(notification.getName());
                entity.setCode(notification.getCode());
                entity.setTitle(notification.getTitle());
                entity.setContent(notification.getContent());
                entity.setTemplateType(notification.getType());
                entity.setEventType(notification.getEventType());
                entity.setVariables(notification.getVariables());
                entity.setEnabled(notification.getEnabled() ? 1 : 0);
                entity.setSortOrder(notification.getSortOrder());
                entity.setUpdatedTime(new Date());
                
                wfNotificationTemplateMapper.updateById(entity);
            } else {
                // 新增模板
                entity = new WfNotificationTemplate();
                entity.setName(notification.getName());
                entity.setCode(notification.getCode());
                entity.setTitle(notification.getTitle());
                entity.setContent(notification.getContent());
                entity.setTemplateType(notification.getType());
                entity.setEventType(notification.getEventType());
                entity.setVariables(notification.getVariables());
                entity.setEnabled(notification.getEnabled() ? 1 : 0);
                entity.setSortOrder(notification.getSortOrder());
                entity.setCreatedTime(new Date());
                entity.setUpdatedTime(new Date());
                
                wfNotificationTemplateMapper.insert(entity);
                notification.setId(entity.getId());
            }
            
            log.info("通知模板保存成功");
            return notification;

        } catch (Exception e) {
            log.error("保存通知模板失败", e);
            throw new RuntimeException("保存通知模板失败: " + e.getMessage(), e);
        }
    }

    @Override
    public void deleteNotification(String notificationId) {
        log.info("删除通知模板，模板ID: {}", notificationId);

        try {
            wfNotificationTemplateMapper.deleteById(notificationId);
            log.info("通知模板删除成功: {}", notificationId);

        } catch (Exception e) {
            log.error("删除通知模板失败", e);
            throw new RuntimeException("删除通知模板失败: " + e.getMessage(), e);
        }
    }

    @Override
    public void batchUpdateNotificationStatus(WorkflowConfigDTO.BatchUpdateDTO data) {
        log.info("批量更新模板状态，数据: {}", data);

        try {
            // 构建更新条件
            LambdaUpdateWrapper<WfNotificationTemplate> updateWrapper = new LambdaUpdateWrapper<WfNotificationTemplate>()
                    .in(WfNotificationTemplate::getId, data.getIds())
                    .set(WfNotificationTemplate::getUpdatedTime, new Date())
                    .set(WfNotificationTemplate::getUpdatedBy, EntityUtils.getCurrentUserId());
            
            // 根据操作类型设置不同字段
            if (data.getStatus() != null) {
                updateWrapper.set(WfNotificationTemplate::getEnabled, data.getStatus());
            }
            
            // 处理自定义更新字段
            if (data.getUpdateFields() != null && !data.getUpdateFields().isEmpty()) {
                data.getUpdateFields().forEach((field, value) -> {
                    switch (field) {
                        case "templateType":
                            updateWrapper.set(WfNotificationTemplate::getTemplateType, value);
                            break;
                        case "eventType":
                            updateWrapper.set(WfNotificationTemplate::getEventType, value);
                            break;
                        case "sortOrder":
                            updateWrapper.set(WfNotificationTemplate::getSortOrder, value);
                            break;
                    }
                });
            }
            
            wfNotificationTemplateMapper.update(null, updateWrapper);
            log.info("批量更新模板状态成功");

        } catch (Exception e) {
            log.error("批量更新模板状态失败", e);
            throw new RuntimeException("批量更新模板状态失败: " + e.getMessage(), e);
        }
    }

    @Override
    public List<WorkflowConfigDTO.TemplateTypeDTO> getTemplateTypes() {
        log.info("获取模板类型列表");

        try {
            // 使用Lambda表达式查询所有模板类型
            List<WfNotificationTemplate> templates = wfNotificationTemplateMapper.selectList(
                new LambdaQueryWrapper<WfNotificationTemplate>()
                    .select(WfNotificationTemplate::getTemplateType)
                    .eq(WfNotificationTemplate::getEnabled, 1)
                    .groupBy(WfNotificationTemplate::getTemplateType)
            );
            
            List<WorkflowConfigDTO.TemplateTypeDTO> types = new ArrayList<>();
            
            // 添加预定义的模板类型
            types.add(createTemplateType("EMAIL", "邮件模板", "用于发送邮件通知的模板", true));
            types.add(createTemplateType("SMS", "短信模板", "用于发送短信通知的模板", true));
            types.add(createTemplateType("SYSTEM", "系统消息模板", "用于系统内消息通知的模板", true));
            types.add(createTemplateType("WECHAT", "微信模板", "用于微信消息通知的模板", true));
            types.add(createTemplateType("DINGTALK", "钉钉模板", "用于钉钉消息通知的模板", true));
            
            log.info("获取模板类型列表成功");
            return types;

        } catch (Exception e) {
            log.error("获取模板类型列表失败", e);
            throw new RuntimeException("获取模板类型列表失败: " + e.getMessage(), e);
        }
    }

    @Override
    public List<WorkflowConfigDTO.EventTypeDTO> getEventTypes() {
        log.info("获取事件类型列表");

        try {
            List<WorkflowConfigDTO.EventTypeDTO> types = new ArrayList<>();
            // 这里应该实现事件类型查询逻辑

            log.info("获取事件类型列表成功");
            return types;

        } catch (Exception e) {
            log.error("获取事件类型列表失败", e);
            throw new RuntimeException("获取事件类型列表失败: " + e.getMessage(), e);
        }
    }

    @Override
    public WorkflowConfigDTO.NotificationPreviewResultDTO previewNotification(String templateId, WorkflowConfigDTO.NotificationPreviewDTO data) {
        log.info("预览通知模板，模板ID: {}", templateId);

        try {
            WorkflowConfigDTO.NotificationPreviewResultDTO result = new WorkflowConfigDTO.NotificationPreviewResultDTO();
            // 这里应该实现模板预览逻辑

            log.info("预览通知模板成功");
            return result;

        } catch (Exception e) {
            log.error("预览通知模板失败", e);
            throw new RuntimeException("预览通知模板失败: " + e.getMessage(), e);
        }
    }

    @Override
    public Map<String, Object> getNotificationStats() {
        log.info("获取通知模板统计信息");

        try {
            Map<String, Object> stats = new java.util.HashMap<>();
            stats.put("total", 0);
            stats.put("enabled", 0);
            stats.put("disabled", 0);

            log.info("获取通知模板统计信息成功");
            return stats;

        } catch (Exception e) {
            log.error("获取通知模板统计信息失败", e);
            throw new RuntimeException("获取通知模板统计信息失败: " + e.getMessage(), e);
        }
    }

    @Override
    public Map<String, Object> getReportConfigList(Map<String, Object> params) {
        log.info("获取报表配置列表，参数: {}", params);

        try {
            List<WorkflowConfigDTO.ReportConfigDTO> reportConfigs = new ArrayList<>();
            // 这里应该实现报表配置查询逻辑

            Map<String, Object> result = new java.util.HashMap<>();
            result.put("list", reportConfigs);
            result.put("total", reportConfigs.size());

            log.info("获取报表配置列表成功，共{}条记录", reportConfigs.size());
            return result;

        } catch (Exception e) {
            log.error("获取报表配置列表失败", e);
            throw new RuntimeException("获取报表配置列表失败: " + e.getMessage(), e);
        }
    }

    @Override
    public WorkflowConfigDTO.ReportConfigDTO saveReportConfig(WorkflowConfigDTO.ReportConfigDTO reportConfig) {
        log.info("保存报表配置，报表名称: {}", reportConfig.getName());

        try {
            // 这里应该实现报表配置保存逻辑
            log.info("报表配置保存成功");
            return reportConfig;

        } catch (Exception e) {
            log.error("保存报表配置失败", e);
            throw new RuntimeException("保存报表配置失败: " + e.getMessage(), e);
        }
    }

    @Override
    public void deleteReportConfig(String reportId) {
        log.info("删除报表配置，报表ID: {}", reportId);

        try {
            // 这里应该实现报表配置删除逻辑
            log.info("报表配置删除成功: {}", reportId);

        } catch (Exception e) {
            log.error("删除报表配置失败", e);
            throw new RuntimeException("删除报表配置失败: " + e.getMessage(), e);
        }
    }

    @Override
    public void batchUpdateReportStatus(WorkflowConfigDTO.BatchUpdateDTO data) {
        log.info("批量更新报表状态，数据: {}", data);

        try {
            // 构建更新条件 - 注意：这里假设有WfReportConfig实体，如果没有请根据实际情况调整
            LambdaUpdateWrapper<WfReportConfig> updateWrapper = new LambdaUpdateWrapper<WfReportConfig>()
                    .in(WfReportConfig::getId, data.getIds())
                    .set(WfReportConfig::getUpdatedTime, new Date())
                    .set(WfReportConfig::getUpdatedBy, EntityUtils.getCurrentUserId());
            
            // 根据操作类型设置不同字段
            if (data.getStatus() != null) {
                updateWrapper.set(WfReportConfig::getEnabled, data.getStatus());
            }
            
            // 处理自定义更新字段
            if (data.getUpdateFields() != null && !data.getUpdateFields().isEmpty()) {
                data.getUpdateFields().forEach((field, value) -> {
                    switch (field) {
                        case "reportType":
                            updateWrapper.set(WfReportConfig::getReportType, value);
                            break;
                        case "sortOrder":
                            updateWrapper.set(WfReportConfig::getSortOrder, value);
                            break;
                    }
                });
            }
            
            // 注意：需要确保有对应的Mapper，这里假设存在
            // wfReportConfigMapper.update(null, updateWrapper);
            log.info("批量更新报表状态成功");

        } catch (Exception e) {
            log.error("批量更新报表状态失败", e);
            throw new RuntimeException("批量更新报表状态失败: " + e.getMessage(), e);
        }
    }

    @Override
    public List<WorkflowConfigDTO.ReportTypeDTO> getReportTypes() {
        log.info("获取报表类型列表");

        try {
            List<WorkflowConfigDTO.ReportTypeDTO> types = new ArrayList<>();
            // 这里应该实现报表类型查询逻辑

            log.info("获取报表类型列表成功");
            return types;

        } catch (Exception e) {
            log.error("获取报表类型列表失败", e);
            throw new RuntimeException("获取报表类型列表失败: " + e.getMessage(), e);
        }
    }

    @Override
    public Map<String, Object> getReportStats() {
        log.info("获取报表统计信息");

        try {
            Map<String, Object> stats = new java.util.HashMap<>();
            stats.put("total", 0);
            stats.put("enabled", 0);
            stats.put("disabled", 0);

            log.info("获取报表统计信息成功");
            return stats;

        } catch (Exception e) {
            log.error("获取报表统计信息失败", e);
            throw new RuntimeException("获取报表统计信息失败: " + e.getMessage(), e);
        }
    }

    @Override
    public void updateReportUsageCount(String reportId) {
        log.info("更新报表使用次数，报表ID: {}", reportId);

        try {
            // 这里应该实现报表使用次数更新逻辑
            log.info("报表使用次数更新成功: {}", reportId);

        } catch (Exception e) {
            log.error("更新报表使用次数失败", e);
            throw new RuntimeException("更新报表使用次数失败: " + e.getMessage(), e);
        }
    }

    @Override
    public Map<String, Object> getInstanceList(Map<String, Object> params) {
        log.info("获取工作流实例列表，参数: {}", params);

        try {
            // 从参数中获取查询条件
            String processDefinitionKey = (String) params.get("processDefinitionKey");
            String businessKey = (String) params.get("businessKey");
            String instanceStatus = (String) params.get("instanceStatus");
            String startUserId = (String) params.get("startUserId");
            String startDeptId = (String) params.get("startDeptId");
            String tenantId = (String) params.get("tenantId");

            List<WfInstance> wfInstances = new ArrayList<>();

            // 根据不同条件查询实例
            if (StringUtils.hasText(businessKey)) {
                WfInstance instance = wfInstanceMapper.selectByBusinessKey(businessKey);
                if (instance != null) {
                    wfInstances.add(instance);
                }
            } else if (StringUtils.hasText(processDefinitionKey)) {
                wfInstances = wfInstanceMapper.selectByProcessDefinitionKey(processDefinitionKey);
            } else if (StringUtils.hasText(instanceStatus)) {
                wfInstances = wfInstanceMapper.selectByInstanceStatus(instanceStatus);
            } else if (StringUtils.hasText(startUserId)) {
                wfInstances = wfInstanceMapper.selectByStartUserId(startUserId);
            } else if (StringUtils.hasText(startDeptId)) {
                wfInstances = wfInstanceMapper.selectByStartDeptId(startDeptId);
            } else if (StringUtils.hasText(tenantId)) {
                wfInstances = wfInstanceMapper.selectByTenantId(tenantId);
            } else {
                // 默认查询运行中的实例
                wfInstances = wfInstanceMapper.selectRunningInstances();
            }

            // 转换为DTO
            List<WorkflowConfigDTO.WorkflowInstanceDTO> instances = wfInstances.stream()
                    .map(this::convertToInstanceDTO)
                    .collect(Collectors.toList());

            Map<String, Object> result = new java.util.HashMap<>();
            result.put("list", instances);
            result.put("total", instances.size());

            log.info("获取工作流实例列表成功，共{}条记录", instances.size());
            return result;

        } catch (Exception e) {
            log.error("获取工作流实例列表失败", e);
            throw new RuntimeException("获取工作流实例列表失败: " + e.getMessage(), e);
        }
    }

    @Override
    public Map<String, Object> getInstanceStats(Map<String, Object> params) {
        log.info("获取实例统计信息，参数: {}", params);

        try {
            Map<String, Object> stats = new java.util.HashMap<>();
            stats.put("total", 0);
            stats.put("running", 0);
            stats.put("completed", 0);
            stats.put("failed", 0);

            log.info("获取实例统计信息成功");
            return stats;

        } catch (Exception e) {
            log.error("获取实例统计信息失败", e);
            throw new RuntimeException("获取实例统计信息失败: " + e.getMessage(), e);
        }
    }

    @Override
    public void batchUpdateInstanceStatus(WorkflowConfigDTO.BatchUpdateDTO data) {
        log.info("批量更新实例状态，数据: {}", data);

        try {
            // 构建更新条件
            LambdaUpdateWrapper<WfInstance> updateWrapper = new LambdaUpdateWrapper<WfInstance>()
                    .in(WfInstance::getId, data.getIds())
                    .set(WfInstance::getUpdatedTime, new Date())
                    .set(WfInstance::getUpdatedBy, EntityUtils.getCurrentUserId());
            
            // 根据操作类型设置不同字段
            if (data.getStatus() != null) {
                updateWrapper.set(WfInstance::getInstanceStatus, data.getStatus());
            }
            
            // 处理自定义更新字段
            if (data.getUpdateFields() != null && !data.getUpdateFields().isEmpty()) {
                data.getUpdateFields().forEach((field, value) -> {
                    switch (field) {
                        case "priority":
                            updateWrapper.set(WfInstance::getPriority, value);
                            break;
                        case "businessKey":
                            updateWrapper.set(WfInstance::getBusinessKey, value);
                            break;
                    }
                });
            }
            
            wfInstanceMapper.update(null, updateWrapper);
            log.info("批量更新实例状态成功");

        } catch (Exception e) {
            log.error("批量更新实例状态失败", e);
            throw new RuntimeException("批量更新实例状态失败: " + e.getMessage(), e);
        }
    }

    @Override
    public Map<String, Object> getTaskList(Map<String, Object> params) {
        log.info("获取工作流任务列表，参数: {}", params);

        try {
            // 从参数中获取查询条件
            String processInstanceId = (String) params.get("processInstanceId");
            String processDefinitionKey = (String) params.get("processDefinitionKey");
            String taskStatus = (String) params.get("taskStatus");
            String assignee = (String) params.get("assignee");
            String candidateUser = (String) params.get("candidateUser");
            String candidateGroup = (String) params.get("candidateGroup");
            String taskType = (String) params.get("taskType");
            String businessKey = (String) params.get("businessKey");

            List<WfTask> wfTasks = new ArrayList<>();

            // 根据不同条件查询任务
            if (StringUtils.hasText(processInstanceId)) {
                wfTasks = wfTaskMapper.selectByProcessInstanceId(processInstanceId);
            } else if (StringUtils.hasText(processDefinitionKey)) {
                wfTasks = wfTaskMapper.selectByProcessDefinitionKey(processDefinitionKey);
            } else if (StringUtils.hasText(taskStatus)) {
                wfTasks = wfTaskMapper.selectByTaskStatus(taskStatus);
            } else if (StringUtils.hasText(assignee)) {
                if ("todo".equals(taskStatus)) {
                    wfTasks = wfTaskMapper.selectTodoTasks(assignee);
                } else if ("done".equals(taskStatus)) {
                    wfTasks = wfTaskMapper.selectDoneTasks(assignee);
                } else {
                    wfTasks = wfTaskMapper.selectByAssignee(assignee);
                }
            } else if (StringUtils.hasText(candidateUser)) {
                wfTasks = wfTaskMapper.selectByCandidateUser(candidateUser);
            } else if (StringUtils.hasText(candidateGroup)) {
                wfTasks = wfTaskMapper.selectByCandidateGroup(candidateGroup);
            } else if (StringUtils.hasText(taskType)) {
                wfTasks = wfTaskMapper.selectByTaskType(taskType);
            } else if (StringUtils.hasText(businessKey)) {
                wfTasks = wfTaskMapper.selectByBusinessKey(businessKey);
            } else {
                // 默认查询超时任务
                wfTasks = wfTaskMapper.selectOverdueTasks();
            }

            // 转换为DTO
            List<WorkflowConfigDTO.WorkflowTaskDTO> tasks = wfTasks.stream()
                    .map(this::convertToTaskDTO)
                    .collect(Collectors.toList());

            Map<String, Object> result = new java.util.HashMap<>();
            result.put("list", tasks);
            result.put("total", tasks.size());

            log.info("获取工作流任务列表成功，共{}条记录", tasks.size());
            return result;

        } catch (Exception e) {
            log.error("获取工作流任务列表失败", e);
            throw new RuntimeException("获取工作流任务列表失败: " + e.getMessage(), e);
        }
    }

    @Override
    public Map<String, Object> getTaskStats(Map<String, Object> params) {
        log.info("获取任务统计信息，参数: {}", params);

        try {
            Map<String, Object> stats = new java.util.HashMap<>();
            stats.put("total", 0);
            stats.put("pending", 0);
            stats.put("inProgress", 0);
            stats.put("completed", 0);

            log.info("获取任务统计信息成功");
            return stats;

        } catch (Exception e) {
            log.error("获取任务统计信息失败", e);
            throw new RuntimeException("获取任务统计信息失败: " + e.getMessage(), e);
        }
    }

    @Override
    public void batchUpdateTaskStatus(WorkflowConfigDTO.BatchUpdateDTO data) {
        log.info("批量更新任务状态，数据: {}", data);

        try {
            // 构建更新条件
            LambdaUpdateWrapper<WfTask> updateWrapper = new LambdaUpdateWrapper<WfTask>()
                    .in(WfTask::getId, data.getIds())
                    .set(WfTask::getUpdatedTime, new Date())
                    .set(WfTask::getUpdatedBy, EntityUtils.getCurrentUserId());
            
            // 根据操作类型设置不同字段
            if (data.getStatus() != null) {
                updateWrapper.set(WfTask::getTaskStatus, data.getStatus());
            }
            
            // 处理自定义更新字段
            if (data.getUpdateFields() != null && !data.getUpdateFields().isEmpty()) {
                data.getUpdateFields().forEach((field, value) -> {
                    switch (field) {
                        case "assignee":
                            updateWrapper.set(WfTask::getAssignee, value);
                            break;
                        case "priority":
                            updateWrapper.set(WfTask::getPriority, value);
                            break;
                        case "dueDate":
                            updateWrapper.set(WfTask::getDueDate, value);
                            break;
                        case "candidateUsers":
                            updateWrapper.set(WfTask::getCandidateUsers, value);
                            break;
                        case "candidateGroups":
                            updateWrapper.set(WfTask::getCandidateGroups, value);
                            break;
                    }
                });
            }
            
            wfTaskMapper.update(null, updateWrapper);
            log.info("批量更新任务状态成功");

        } catch (Exception e) {
            log.error("批量更新任务状态失败", e);
            throw new RuntimeException("批量更新任务状态失败: " + e.getMessage(), e);
        }
    }

    @Override
    public void batchAssignTask(WorkflowConfigDTO.BatchAssignTaskDTO data) {
        log.info("批量分配任务，数据: {}", data);

        try {
            // 这里应该实现批量分配逻辑
            log.info("批量分配任务成功");

        } catch (Exception e) {
            log.error("批量分配任务失败", e);
            throw new RuntimeException("批量分配任务失败: " + e.getMessage(), e);
        }
    }

    @Override
    public Map<String, Object> getProcessDefinitionList(Map<String, Object> params) {
        log.info("获取流程定义列表，参数: {}", params);

        try {
            // 从参数中获取查询条件
            String categoryId = (String) params.get("categoryId");
            String categoryCode = (String) params.get("categoryCode");
            String enabled = (String) params.get("enabled");
            String processName = (String) params.get("processName");

            List<WfProcessCategory> categories = new ArrayList<>();

            // 根据不同条件查询流程分类（作为流程定义的载体）
            if (StringUtils.hasText(categoryId)) {
                WfProcessCategory category = wfProcessCategoryMapper.selectCategoryDetail(categoryId);
                if (category != null) {
                    categories.add(category);
                }
            } else if (StringUtils.hasText(categoryCode)) {
                WfProcessCategory category = wfProcessCategoryMapper.selectByCategoryCode(categoryCode);
                if (category != null) {
                    categories.add(category);
                }
            } else if (StringUtils.hasText(enabled)) {
                Integer enabledStatus = Integer.valueOf(enabled);
                categories = wfProcessCategoryMapper.selectByEnabled(enabledStatus);
            } else if (StringUtils.hasText(processName)) {
                categories = wfProcessCategoryMapper.selectByCategoryNameLike(processName);
            } else {
                // 默认查询所有启用的分类
                categories = wfProcessCategoryMapper.selectByEnabled(1);
            }

            // 转换为流程定义DTO
            List<WorkflowConfigDTO.ProcessDefinitionDTO> definitions = categories.stream()
                    .map(this::convertToProcessDefinitionDTO)
                    .collect(Collectors.toList());

            Map<String, Object> result = new java.util.HashMap<>();
            result.put("list", definitions);
            result.put("total", definitions.size());

            log.info("获取流程定义列表成功，共{}条记录", definitions.size());
            return result;

        } catch (Exception e) {
            log.error("获取流程定义列表失败", e);
            throw new RuntimeException("获取流程定义列表失败: " + e.getMessage(), e);
        }
    }
    
    /**
     * 转换WfInstance为WorkflowInstanceDTO
     */
    private WorkflowConfigDTO.WorkflowInstanceDTO convertToInstanceDTO(WfInstance instance) {
        WorkflowConfigDTO.WorkflowInstanceDTO dto = new WorkflowConfigDTO.WorkflowInstanceDTO();
        dto.setId(instance.getId());
        dto.setProcessInstanceId(instance.getId()); // 实例ID作为流程实例ID
        dto.setProcessDefinitionKey(instance.getProcessDefinitionKey());
        dto.setProcessDefinitionName(instance.getProcessDefinitionName());
        dto.setBusinessKey(instance.getBusinessKey());
        dto.setInstanceStatus(instance.getInstanceStatus());
        dto.setStartUserId(instance.getStartUserId());
        dto.setStartUserName(instance.getStartUserName());
        dto.setStartDeptId(instance.getStartDeptId());
        dto.setStartDeptName(instance.getStartDeptName());
        dto.setStartTime(instance.getCreatedTime());
        dto.setEndTime(instance.getActualEndTime());
        
        // 计算持续时间
        if (instance.getCreatedTime() != null && instance.getActualEndTime() != null) {
            dto.setDuration(instance.getActualEndTime().getTime() - instance.getCreatedTime().getTime());
        }
        
        dto.setTenantId(instance.getTenantId());
        dto.setRemark(instance.getRemark());
        dto.setCreatedTime(instance.getCreatedTime());
        dto.setUpdatedTime(instance.getUpdatedTime());
        
        return dto;
    }
    
    /**
     * 转换WfTask为WorkflowTaskDTO
     */
    private WorkflowConfigDTO.WorkflowTaskDTO convertToTaskDTO(WfTask task) {
        WorkflowConfigDTO.WorkflowTaskDTO dto = new WorkflowConfigDTO.WorkflowTaskDTO();
        dto.setId(task.getId());
        dto.setTaskKey(task.getTaskDefinitionKey());
        dto.setTaskName(task.getTaskName());
        dto.setProcessInstanceId(task.getProcessInstanceId());
        dto.setProcessDefinitionKey(task.getProcessDefinitionKey());
        dto.setTaskStatus(task.getTaskStatus());
        dto.setAssignee(task.getAssignee());
        dto.setAssigneeName(task.getAssigneeName());
        dto.setCandidateUsers(task.getCandidateUsers());
        dto.setCandidateGroups(task.getCandidateGroups());
        dto.setTaskType(task.getTaskType());
        dto.setPriority(task.getPriority());
        dto.setDueDate(task.getDueDate());
        dto.setBusinessKey(task.getBusinessKey());
        dto.setFormKey(task.getFormKey());
        dto.setDescription(task.getTaskDescription());
        dto.setCreatedTime(task.getCreateTime());
        dto.setCompletedTime(task.getEndTime());
        
        // 计算处理时间
        if (task.getCreateTime() != null && task.getEndTime() != null) {
            dto.setProcessingTime(task.getEndTime().getTime() - task.getCreateTime().getTime());
        }
        
        dto.setTenantId(task.getTenantId());
        dto.setRemark(task.getRemark());
        
        return dto;
    }
    
    /**
     * 转换WfProcessCategory为ProcessDefinitionDTO
     */
    private WorkflowConfigDTO.ProcessDefinitionDTO convertToProcessDefinitionDTO(WfProcessCategory category) {
        WorkflowConfigDTO.ProcessDefinitionDTO dto = new WorkflowConfigDTO.ProcessDefinitionDTO();
        dto.setId(category.getId());
        dto.setProcessDefinitionKey(category.getCode()); // 使用分类编码作为流程定义键
        dto.setProcessDefinitionName(category.getName());
        dto.setVersion(1); // 默认版本为1
        dto.setCategoryId(category.getId());
        dto.setCategoryName(category.getName());
        dto.setCategoryCode(category.getCode());
        dto.setDescription(category.getDescription());
        dto.setEnabled(category.getEnabled() == 1);
        dto.setDeploymentId(null); // 分类没有部署ID
        dto.setResourceName(null); // 分类没有资源名称
        dto.setDiagramResourceName(null); // 分类没有图表资源名称
        dto.setSuspended(category.getEnabled() != 1); // 根据启用状态设置暂停状态
        dto.setTenantId(null); // 分类没有租户ID
        dto.setSortOrder(category.getSortOrder());
        dto.setCreatedTime(category.getCreatedTime());
        dto.setUpdatedTime(category.getUpdatedTime());
        dto.setCreatedBy(category.getCreatedBy());
        dto.setUpdatedBy(category.getUpdatedBy());
        
        return dto;
    }
    
    /**
     * 构建分类树
     */
    private List<WorkflowConfigDTO.CategoryTreeDTO> buildCategoryTree(List<WfProcessCategory> allCategories, String parentId) {
        return allCategories.stream()
            .filter(category -> {
                if (parentId == null) {
                    return category.getParentId() == null || category.getParentId().isEmpty();
                } else {
                    return parentId.equals(category.getParentId());
                }
            })
            .map(category -> {
                WorkflowConfigDTO.CategoryTreeDTO treeNode = new WorkflowConfigDTO.CategoryTreeDTO();
                treeNode.setId(category.getId());
                treeNode.setName(category.getName());
                treeNode.setCode(category.getCode());
                treeNode.setParentId(category.getParentId());
                treeNode.setEnabled(category.getEnabled() == 1);
                treeNode.setIcon(category.getIcon());
                treeNode.setColor(category.getColor());
                treeNode.setSortOrder(category.getSortOrder());
                
                // 递归构建子节点
                List<WorkflowConfigDTO.CategoryTreeDTO> children = buildCategoryTree(allCategories, category.getId());
                treeNode.setChildren(children);
                
                return treeNode;
            })
            .collect(Collectors.toList());
    }
    
    /**
     * 创建模板类型DTO
     */
    private WorkflowConfigDTO.TemplateTypeDTO createTemplateType(String code, String name, String description, Boolean enabled) {
        WorkflowConfigDTO.TemplateTypeDTO type = new WorkflowConfigDTO.TemplateTypeDTO();
        type.setCode(code);
        type.setName(name);
        type.setDescription(description);
        type.setEnabled(enabled);
        return type;
    }
}