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
import jakarta.validation.constraints.NotBlank;

/**
 * 工作流配置管理控制器扩展2
 * 提供流程分类、通知模板、报表配置、实例和任务管理功能
 * 
 * @author chennian
 */
@Api(tags = "工作流配置管理扩展")
@Slf4j
@RestController
@RequestMapping("/sys/workflow/config")
@RequiredArgsConstructor
public class WorkflowConfigExtendedController {

    private final WorkflowConfigService workflowConfigService;

    // ==================== 流程分类相关接口 ====================

    /**
     * 获取流程分类树
     * 对应前端: getCategoryTree
     */
    @ApiOperation("获取流程分类树")
    @GetMapping("/category/tree")
    public ServerResponseEntity<List<WorkflowConfigDTO.CategoryTreeDTO>> getCategoryTree() {
        try {
            List<WorkflowConfigDTO.CategoryTreeDTO> tree = workflowConfigService.getCategoryTree();
            return ServerResponseEntity.success(tree);
        } catch (Exception e) {
            log.error("获取流程分类树失败", e);
            return ServerResponseEntity.showFailMsg(e.getMessage());
        }

    }

    /**
     * 获取流程分类列表
     * 对应前端: getCategoryList
     */
    @ApiOperation("获取流程分类列表")
    @GetMapping("/category/list")
    public ServerResponseEntity<Map<String, Object>> getCategoryList(
            @ApiParam("页码") @RequestParam(defaultValue = "1") Integer page,
            @ApiParam("每页大小") @RequestParam(defaultValue = "10") Integer size,
            @ApiParam("分类名称") @RequestParam(required = false) String name,
            @ApiParam("父级ID") @RequestParam(required = false) String parentId,
            @ApiParam("状态") @RequestParam(required = false) String status) {

        
        try {
            Map<String, Object> params = new HashMap<>();
            params.put("page", page);
            params.put("size", size);
            params.put("name", name);
            params.put("parentId", parentId);
            params.put("status", status);
            
            Map<String, Object> result = workflowConfigService.getCategoryList(params);
            return ServerResponseEntity.success(result);
            
        } catch (Exception e) {
            log.error("获取流程分类列表失败", e);
            return ServerResponseEntity.showFailMsg(e.getMessage());
        }

    }

    /**
     * 保存流程分类
     * 对应前端: saveCategory
     */
    @ApiOperation("保存流程分类")
    @PostMapping("/category/save")
    public ServerResponseEntity<WorkflowConfigDTO.CategoryDTO> saveCategory(
            @ApiParam("分类数据") @Valid @RequestBody WorkflowConfigDTO.CategoryDTO category) {
        
        try {
            WorkflowConfigDTO.CategoryDTO savedCategory = workflowConfigService.saveCategory(category);
            
            log.info("保存流程分类: {}", category.getName());
            return ServerResponseEntity.success(savedCategory);
            
        } catch (Exception e) {
            log.error("保存流程分类失败", e);
            return ServerResponseEntity.showFailMsg(e.getMessage());
        }
    }

    /**
     * 删除流程分类
     * 对应前端: deleteCategory
     */
    @ApiOperation("删除流程分类")
    @DeleteMapping("/category/delete/{id}")
    public ServerResponseEntity<Void> deleteCategory(
            @ApiParam("分类ID") @PathVariable @NotBlank String id) {
        
        try {
            workflowConfigService.deleteCategory(id);
            
            log.info("删除流程分类: {}", id);
            return ServerResponseEntity.success();
            
        } catch (Exception e) {
            log.error("删除流程分类失败", e);
            return ServerResponseEntity.showFailMsg(e.getMessage());
        }
    }

    /**
     * 批量更新分类状态
     * 对应前端: batchUpdateCategoryStatus
     */
    @ApiOperation("批量更新分类状态")
    @PutMapping("/category/batch-status")
    public ServerResponseEntity<Void> batchUpdateCategoryStatus(
            @ApiParam("更新数据") @Valid @RequestBody WorkflowConfigDTO.BatchUpdateDTO data) {

        try {
            workflowConfigService.batchUpdateCategoryStatus(data);
            
            log.info("批量更新分类状态，共{}项", data.getIds().size());
            return ServerResponseEntity.success();
            
        } catch (Exception e) {
            log.error("批量更新分类状态失败", e);
            return ServerResponseEntity.showFailMsg(e.getMessage());
        }
    }

    /**
     * 获取分类统计信息
     * 对应前端: getCategoryStats
     */
    @ApiOperation("获取分类统计信息")
    @GetMapping("/category/stats")
    public ServerResponseEntity<Map<String, Object>> getCategoryStats() {
        try {
            Map<String, Object> stats = workflowConfigService.getCategoryStats();
            return ServerResponseEntity.success(stats);
            
        } catch (Exception e) {
            log.error("获取分类统计信息失败", e);
            return ServerResponseEntity.showFailMsg(e.getMessage());
        }
    }

    // ==================== 通知模板相关接口 ====================

    /**
     * 获取通知模板列表
     * 对应前端: getNotificationList
     */
    @ApiOperation("获取通知模板列表")
    @GetMapping("/notification/list")
    public ServerResponseEntity<Map<String, Object>> getNotificationList(
            @ApiParam("页码") @RequestParam(defaultValue = "1") Integer page,
            @ApiParam("每页大小") @RequestParam(defaultValue = "10") Integer size,
            @ApiParam("模板名称") @RequestParam(required = false) String name,
            @ApiParam("模板类型") @RequestParam(required = false) String type,
            @ApiParam("状态") @RequestParam(required = false) String status) {
        try {
            Map<String, Object> params = new HashMap<>();
            params.put("page", page);
            params.put("size", size);
            params.put("name", name);
            params.put("type", type);
            params.put("status", status);
            
            Map<String, Object> result = workflowConfigService.getNotificationList(params);
            return ServerResponseEntity.success(result);

            
        } catch (Exception e) {
            log.error("获取通知模板列表失败", e);
            return ServerResponseEntity.showFailMsg(e.getMessage());
        }
    }

    /**
     * 保存通知模板
     * 对应前端: saveNotification
     */
    @ApiOperation("保存通知模板")
    @PostMapping("/notification/save")
    public ServerResponseEntity<WorkflowConfigDTO.NotificationDTO> saveNotification(
            @ApiParam("模板数据") @Valid @RequestBody WorkflowConfigDTO.NotificationDTO notification) {
        try {
            WorkflowConfigDTO.NotificationDTO savedNotification = workflowConfigService.saveNotification(notification);
            log.info("保存通知模板: {}", notification.getName());
            return ServerResponseEntity.success(savedNotification);
        } catch (Exception e) {
            log.error("保存通知模板失败", e);
            return ServerResponseEntity.showFailMsg(e.getMessage());
        }
    }

    /**
     * 删除通知模板
     * 对应前端: deleteNotification
     */
    @ApiOperation("删除通知模板")
    @DeleteMapping("/notification/delete/{id}")
    public ServerResponseEntity<Void> deleteNotification(
            @ApiParam("模板ID") @PathVariable @NotBlank String id) {
        
        try {
            workflowConfigService.deleteNotification(id);
            
            log.info("删除通知模板: {}", id);
            return ServerResponseEntity.success();
            
        } catch (Exception e) {
            log.error("删除通知模板失败", e);
            return ServerResponseEntity.showFailMsg(e.getMessage());
        }
    }

    /**
     * 批量更新模板状态
     * 对应前端: batchUpdateNotificationStatus
     */
    @ApiOperation("批量更新模板状态")
    @PutMapping("/notification/batch-status")
    public ServerResponseEntity<Void> batchUpdateNotificationStatus(
            @ApiParam("更新数据") @Valid @RequestBody WorkflowConfigDTO.BatchUpdateDTO data) {
        
        try {
            workflowConfigService.batchUpdateNotificationStatus(data);
            
            log.info("批量更新模板状态，共{}项", data.getIds().size());
            return ServerResponseEntity.success();
            
        } catch (Exception e) {
            log.error("批量更新模板状态失败", e);
            return ServerResponseEntity.showFailMsg(e.getMessage());
        }
    }

    /**
     * 获取模板类型列表
     * 对应前端: getTemplateTypes
     */
    @ApiOperation("获取模板类型列表")
    @GetMapping("/notification/template-types")
    public ServerResponseEntity<List<WorkflowConfigDTO.TemplateTypeDTO>> getTemplateTypes() {
        
        try {
            List<WorkflowConfigDTO.TemplateTypeDTO> types = workflowConfigService.getTemplateTypes();
            return ServerResponseEntity.success(types);
            
        } catch (Exception e) {
            log.error("获取模板类型列表失败", e);
            return ServerResponseEntity.showFailMsg(e.getMessage());
        }
    }

    /**
     * 获取事件类型列表
     * 对应前端: getEventTypes
     */
    @ApiOperation("获取事件类型列表")
    @GetMapping("/notification/event-types")
    public ServerResponseEntity<List<WorkflowConfigDTO.EventTypeDTO>> getEventTypes() {
        
        try {
            List<WorkflowConfigDTO.EventTypeDTO> types = workflowConfigService.getEventTypes();
            return ServerResponseEntity.success(types);
            
        } catch (Exception e) {
            log.error("获取事件类型列表失败", e);
            return ServerResponseEntity.showFailMsg(e.getMessage());
        }
    }

    /**
     * 预览通知模板
     * 对应前端: previewNotification
     */
    @ApiOperation("预览通知模板")
    @PostMapping("/notification/preview/{id}")
    public ServerResponseEntity<WorkflowConfigDTO.NotificationPreviewResultDTO> previewNotification(
            @ApiParam("模板ID") @PathVariable @NotBlank String id,
            @ApiParam("预览数据") @Valid @RequestBody WorkflowConfigDTO.NotificationPreviewDTO data) {
        
        try {
            WorkflowConfigDTO.NotificationPreviewResultDTO result = workflowConfigService.previewNotification(id, data);
            return ServerResponseEntity.success(result);
            
        } catch (Exception e) {
            log.error("预览通知模板失败", e);
            return ServerResponseEntity.showFailMsg(e.getMessage());
        }
    }

    /**
     * 获取通知模板统计信息
     * 对应前端: getNotificationStats
     */
    @ApiOperation("获取通知模板统计信息")
    @GetMapping("/notification/stats")
    public ServerResponseEntity<Map<String, Object>> getNotificationStats() {
        
        try {
            Map<String, Object> stats = workflowConfigService.getNotificationStats();
            return ServerResponseEntity.success(stats);
            
        } catch (Exception e) {
            log.error("获取通知模板统计信息失败", e);
            return ServerResponseEntity.showFailMsg(e.getMessage());
        }
    }

    // ==================== 报表配置相关接口 ====================

    /**
     * 获取报表配置列表
     * 对应前端: getReportConfigList
     */
    @ApiOperation("获取报表配置列表")
    @GetMapping("/report/list")
    public ServerResponseEntity<Map<String, Object>> getReportConfigList(
            @ApiParam("页码") @RequestParam(defaultValue = "1") Integer page,
            @ApiParam("每页大小") @RequestParam(defaultValue = "10") Integer size,
            @ApiParam("报表名称") @RequestParam(required = false) String name,
            @ApiParam("报表类型") @RequestParam(required = false) String type,
            @ApiParam("状态") @RequestParam(required = false) String status) {
        
        try {
            Map<String, Object> params = new HashMap<>();
            params.put("page", page);
            params.put("size", size);
            params.put("name", name);
            params.put("type", type);
            params.put("status", status);
            
            Map<String, Object> result = workflowConfigService.getReportConfigList(params);
            return ServerResponseEntity.success(result);
            
        } catch (Exception e) {
            log.error("获取报表配置列表失败", e);
            return ServerResponseEntity.showFailMsg(e.getMessage());
        }
    }

    /**
     * 保存报表配置
     * 对应前端: saveReportConfig
     */
    @ApiOperation("保存报表配置")
    @PostMapping("/report/save")
    public ServerResponseEntity<WorkflowConfigDTO.ReportConfigDTO> saveReportConfig(
            @ApiParam("报表数据") @Valid @RequestBody WorkflowConfigDTO.ReportConfigDTO reportConfig) {
        
        try {
            WorkflowConfigDTO.ReportConfigDTO savedConfig = workflowConfigService.saveReportConfig(reportConfig);
            
            log.info("保存报表配置: {}", reportConfig.getName());
            return ServerResponseEntity.success(savedConfig);
            
        } catch (Exception e) {
            log.error("保存报表配置失败", e);
            return ServerResponseEntity.showFailMsg(e.getMessage());
        }
    }

    /**
     * 删除报表配置
     * 对应前端: deleteReportConfig
     */
    @ApiOperation("删除报表配置")
    @DeleteMapping("/report/delete/{id}")
    public ServerResponseEntity<Void> deleteReportConfig(
            @ApiParam("报表ID") @PathVariable @NotBlank String id) {
        
        try {
            workflowConfigService.deleteReportConfig(id);
            
            log.info("删除报表配置: {}", id);
            return ServerResponseEntity.success();
            
        } catch (Exception e) {
            log.error("删除报表配置失败", e);
            return ServerResponseEntity.showFailMsg(e.getMessage());
        }
    }

    /**
     * 批量更新报表状态
     * 对应前端: batchUpdateReportStatus
     */
    @ApiOperation("批量更新报表状态")
    @PutMapping("/report/batch-status")
    public ServerResponseEntity<Void> batchUpdateReportStatus(
            @ApiParam("更新数据") @Valid @RequestBody WorkflowConfigDTO.BatchUpdateDTO data) {

        try {
            workflowConfigService.batchUpdateReportStatus(data);
            
            log.info("批量更新报表状态，共{}项", data.getIds().size());
            return ServerResponseEntity.success();
            
        } catch (Exception e) {
            log.error("批量更新报表状态失败", e);
            return ServerResponseEntity.showFailMsg(e.getMessage());
        }
    }

    /**
     * 获取报表类型列表
     * 对应前端: getReportTypes
     */
    @ApiOperation("获取报表类型列表")
    @GetMapping("/report/types")
    public ServerResponseEntity<List<WorkflowConfigDTO.ReportTypeDTO>> getReportTypes() {
        
        try {
            List<WorkflowConfigDTO.ReportTypeDTO> types = workflowConfigService.getReportTypes();
            return ServerResponseEntity.success(types);
            
        } catch (Exception e) {
            log.error("获取报表类型列表失败", e);
            return ServerResponseEntity.showFailMsg(e.getMessage());
        }
    }

    /**
     * 获取报表统计信息
     * 对应前端: getReportStats
     */
    @ApiOperation("获取报表统计信息")
    @GetMapping("/report/stats")
    public ServerResponseEntity<Map<String, Object>> getReportStats() {
        
        try {
            Map<String, Object> stats = workflowConfigService.getReportStats();
            return ServerResponseEntity.success(stats);
            
        } catch (Exception e) {
            log.error("获取报表统计信息失败", e);
            return ServerResponseEntity.showFailMsg(e.getMessage());
        }
    }

    /**
     * 更新报表使用次数
     * 对应前端: updateReportUsageCount
     */
    @ApiOperation("更新报表使用次数")
    @PutMapping("/report/usage/{id}")
    public ServerResponseEntity<Void> updateReportUsageCount(
            @ApiParam("报表ID") @PathVariable @NotBlank String id) {
        
        try {
            workflowConfigService.updateReportUsageCount(id);
            
            log.info("更新报表使用次数: {}", id);
            return ServerResponseEntity.success();
            
        } catch (Exception e) {
            log.error("更新报表使用次数失败", e);
            return ServerResponseEntity.showFailMsg(e.getMessage());
        }
    }

    // ==================== 工作流实例相关接口 ====================

    /**
     * 获取工作流实例列表
     * 对应前端: getInstanceList
     */
    @ApiOperation("获取工作流实例列表")
    @GetMapping("/instance/list")
    public ServerResponseEntity<Map<String, Object>> getInstanceList(
            @ApiParam("页码") @RequestParam(defaultValue = "1") Integer page,
            @ApiParam("每页大小") @RequestParam(defaultValue = "10") Integer size,
            @ApiParam("流程定义键") @RequestParam(required = false) String processDefinitionKey,
            @ApiParam("业务键") @RequestParam(required = false) String businessKey,
            @ApiParam("状态") @RequestParam(required = false) String status,
            @ApiParam("发起人") @RequestParam(required = false) String startUserId) {
        
        try {
            Map<String, Object> params = new HashMap<>();
            params.put("page", page);
            params.put("size", size);
            params.put("processDefinitionKey", processDefinitionKey);
            params.put("businessKey", businessKey);
            params.put("status", status);
            params.put("startUserId", startUserId);
            
            Map<String, Object> result = workflowConfigService.getInstanceList(params);
            return ServerResponseEntity.success(result);
            
        } catch (Exception e) {
            log.error("获取工作流实例列表失败", e);
            return ServerResponseEntity.showFailMsg(e.getMessage());
        }
    }

    /**
     * 获取实例统计信息
     * 对应前端: getInstanceStats
     */
    @ApiOperation("获取实例统计信息")
    @GetMapping("/instance/stats")
    public ServerResponseEntity<Map<String, Object>> getInstanceStats(
            @ApiParam("开始时间") @RequestParam(required = false) String startTime,
            @ApiParam("结束时间") @RequestParam(required = false) String endTime,
            @ApiParam("流程定义键") @RequestParam(required = false) String processDefinitionKey) {
        
        try {
            Map<String, Object> params = new HashMap<>();
            params.put("startTime", startTime);
            params.put("endTime", endTime);
            params.put("processDefinitionKey", processDefinitionKey);
            
            Map<String, Object> stats = workflowConfigService.getInstanceStats(params);
            return ServerResponseEntity.success(stats);
            
        } catch (Exception e) {
            log.error("获取实例统计信息失败", e);
            return ServerResponseEntity.showFailMsg(e.getMessage());
        }
    }

    /**
     * 批量更新实例状态
     * 对应前端: batchUpdateInstanceStatus
     */
    @ApiOperation("批量更新实例状态")
    @PutMapping("/instance/batch-status")
    public ServerResponseEntity<Void> batchUpdateInstanceStatus(
            @ApiParam("更新数据") @Valid @RequestBody WorkflowConfigDTO.BatchUpdateDTO data) {
        
        try {
            workflowConfigService.batchUpdateInstanceStatus(data);
            
            log.info("批量更新实例状态，共{}项", data.getIds().size());
            return ServerResponseEntity.success();
            
        } catch (Exception e) {
            log.error("批量更新实例状态失败", e);
            return ServerResponseEntity.showFailMsg(e.getMessage());
        }
    }

    // ==================== 工作流任务相关接口 ====================

    /**
     * 获取工作流任务列表
     * 对应前端: getTaskList
     */
    @ApiOperation("获取工作流任务列表")
    @GetMapping("/task/list")
    public ServerResponseEntity<Map<String, Object>> getTaskList(
            @ApiParam("页码") @RequestParam(defaultValue = "1") Integer page,
            @ApiParam("每页大小") @RequestParam(defaultValue = "10") Integer size,
            @ApiParam("任务名称") @RequestParam(required = false) String name,
            @ApiParam("分配人") @RequestParam(required = false) String assignee,
            @ApiParam("候选组") @RequestParam(required = false) String candidateGroup,
            @ApiParam("状态") @RequestParam(required = false) String status) {
        
        try {
            Map<String, Object> params = new HashMap<>();
            params.put("page", page);
            params.put("size", size);
            params.put("name", name);
            params.put("assignee", assignee);
            params.put("candidateGroup", candidateGroup);
            params.put("status", status);
            
            Map<String, Object> result = workflowConfigService.getTaskList(params);
            return ServerResponseEntity.success(result);
            
        } catch (Exception e) {
            log.error("获取工作流任务列表失败", e);
            return ServerResponseEntity.showFailMsg(e.getMessage());
        }
    }

    /**
     * 获取任务统计信息
     * 对应前端: getTaskStats
     */
    @ApiOperation("获取任务统计信息")
    @GetMapping("/task/stats")
    public ServerResponseEntity<Map<String, Object>> getTaskStats(
            @ApiParam("开始时间") @RequestParam(required = false) String startTime,
            @ApiParam("结束时间") @RequestParam(required = false) String endTime,
            @ApiParam("分配人") @RequestParam(required = false) String assignee) {
        
        try {
            Map<String, Object> params = new HashMap<>();
            params.put("startTime", startTime);
            params.put("endTime", endTime);
            params.put("assignee", assignee);
            
            Map<String, Object> stats = workflowConfigService.getTaskStats(params);
            return ServerResponseEntity.success(stats);
            
        } catch (Exception e) {
            log.error("获取任务统计信息失败", e);
            return ServerResponseEntity.showFailMsg(e.getMessage());
        }
    }

    /**
     * 批量更新任务状态
     * 对应前端: batchUpdateTaskStatus
     */
    @ApiOperation("批量更新任务状态")
    @PutMapping("/task/batch-status")
    public ServerResponseEntity<Void> batchUpdateTaskStatus(
            @ApiParam("更新数据") @Valid @RequestBody WorkflowConfigDTO.BatchUpdateDTO data) {
        
        try {
            workflowConfigService.batchUpdateTaskStatus(data);
            
            log.info("批量更新任务状态，共{}项", data.getIds().size());
            return ServerResponseEntity.success();
            
        } catch (Exception e) {
            log.error("批量更新任务状态失败", e);
            return ServerResponseEntity.showFailMsg(e.getMessage());
        }
    }

    /**
     * 批量分配任务
     * 对应前端: batchAssignTask
     */
    @ApiOperation("批量分配任务")
    @PostMapping("/task/batch-assign")
    public ServerResponseEntity<Void> batchAssignTask(
            @ApiParam("分配数据") @Valid @RequestBody WorkflowConfigDTO.BatchAssignTaskDTO data) {
        
        try {
            workflowConfigService.batchAssignTask(data);
            
            log.info("批量分配任务，共{}项", data.getTaskIds().size());
            return ServerResponseEntity.success();
            
        } catch (Exception e) {
            log.error("批量分配任务失败", e);
            return ServerResponseEntity.showFailMsg(e.getMessage());
        }
    }

    /**
     * 获取流程定义列表
     * 对应前端: getProcessDefinitionList
     */
    @ApiOperation("获取流程定义列表")
    @GetMapping("/process/definition/list")
    public ServerResponseEntity<Map<String, Object>> getProcessDefinitionList(
            @ApiParam("页码") @RequestParam(defaultValue = "1") Integer page,
            @ApiParam("每页大小") @RequestParam(defaultValue = "10") Integer size,
            @ApiParam("流程名称") @RequestParam(required = false) String name,
            @ApiParam("流程键") @RequestParam(required = false) String key,
            @ApiParam("分类") @RequestParam(required = false) String category) {
        
        try {
            Map<String, Object> params = new HashMap<>();
            params.put("page", page);
            params.put("size", size);
            params.put("name", name);
            params.put("key", key);
            params.put("category", category);
            
            Map<String, Object> result = workflowConfigService.getProcessDefinitionList(params);
            return ServerResponseEntity.success(result);
            
        } catch (Exception e) {
            log.error("获取流程定义列表失败", e);
            return ServerResponseEntity.showFailMsg(e.getMessage());
        }
    }

}