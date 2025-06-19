package com.chennian.storytelling.admin.controller.workflow;

import com.chennian.storytelling.common.response.ServerResponseEntity;
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
import com.chennian.storytelling.bean.dto.WorkflowConfigDTO;
import com.chennian.storytelling.service.WorkflowConfigService;

/**
 * 工作流配置管理控制器
 * 提供系统配置和权限管理功能
 * 
 * @author chennian
 */
@Api(tags = "工作流配置管理")
@Slf4j
@RestController
@RequestMapping("/sys/workflow/config")
@RequiredArgsConstructor
public class WorkflowConfigController {

    private final WorkflowConfigService workflowConfigService;

    /**
     * 获取系统配置
     */
    @ApiOperation(value = "获取系统配置", notes = "获取工作流系统配置信息")
    @GetMapping("/system")
    public ServerResponseEntity<List<WorkflowConfigDTO.SystemConfigDTO>> getSystemConfig(@RequestBody String group) {
        try {
            List<WorkflowConfigDTO.SystemConfigDTO> config = workflowConfigService.getSystemConfig(group);
            return ServerResponseEntity.success(config);
        } catch (Exception e) {
            log.error("获取系统配置失败", e);
            return ServerResponseEntity.showFailMsg("获取系统配置失败: " + e.getMessage());
        }
    }
    
    /**
     * 更新系统配置
     */
    @ApiOperation(value = "更新系统配置", notes = "更新工作流系统配置")
    @PutMapping("/system")
    public ServerResponseEntity<Void> updateSystemConfig(@RequestBody List<WorkflowConfigDTO.SystemConfigDTO> configs) {
        try {
            workflowConfigService.updateSystemConfig(configs);
            return ServerResponseEntity.success();
        } catch (Exception e) {
            log.error("更新系统配置失败", e);
            return ServerResponseEntity.showFailMsg("更新系统配置失败: " + e.getMessage());
        }
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
    @ApiOperation(value = "获取通知模板", notes = "获取工作流通知模板")
    @GetMapping("/notification-templates")
    public ServerResponseEntity<List<WorkflowConfigDTO.NotificationTemplateDTO>> getNotificationTemplates(@RequestBody String type) {
        try {
            List<WorkflowConfigDTO.NotificationTemplateDTO> notificationTemplates = workflowConfigService.getNotificationTemplates(type);
            return ServerResponseEntity.success(notificationTemplates);
        } catch (Exception e) {
            log.error("获取通知模板失败", e);
            return ServerResponseEntity.showFailMsg("获取通知模板失败: " + e.getMessage());
        }
    }
    
    /**
     * 创建或更新通知模板
     */
    @ApiOperation("创建或更新通知模板")
    @PostMapping("/notification-templates")
    public ServerResponseEntity<WorkflowConfigDTO.NotificationTemplateDTO> saveNotificationTemplate(
            @ApiParam("通知模板信息") @Valid @RequestBody WorkflowConfigDTO.NotificationTemplateDTO template) {
        
        try {
            // 保存通知模板到数据库
            WorkflowConfigDTO.NotificationTemplateDTO savedTemplate = workflowConfigService.saveNotificationTemplate(template);
            
            log.info("保存通知模板: {}", template.getName());
            
            return ServerResponseEntity.success(savedTemplate);
            
        } catch (Exception e) {
            log.error("保存通知模板失败", e);
            return ServerResponseEntity.showFailMsg("保存通知模板失败: " + e.getMessage());
        }
    }
    

}