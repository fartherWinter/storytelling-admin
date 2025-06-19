package com.chennian.storytelling.service;


import com.chennian.storytelling.bean.dto.WorkflowConfigDTO;

import java.util.List;
import java.util.Map;

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
    
    // ==================== 扩展接口方法 ====================
    
    /**
     * 获取系统配置列表
     * 
     * @param params 查询参数
     * @return 系统配置列表
     */
    Map<String, Object> getSystemConfigList(Map<String, Object> params);
    
    /**
     * 保存系统配置
     * 
     * @param config 系统配置
     * @return 保存后的系统配置
     */
    WorkflowConfigDTO.SystemConfigDTO saveSystemConfig(WorkflowConfigDTO.SystemConfigDTO config);
    
    /**
     * 删除系统配置
     * 
     * @param configId 配置ID
     */
    void deleteSystemConfig(String configId);
    
    /**
     * 批量更新系统配置状态
     * 
     * @param data 批量更新数据
     */
    void batchUpdateSystemConfigStatus(WorkflowConfigDTO.BatchUpdateDTO data);
    
    /**
     * 获取系统配置统计信息
     * 
     * @return 统计信息
     */
    Map<String, Object> getSystemConfigStats();
    

    /**
     * 获取流程分类树
     * 
     * @return 流程分类树
     */
    List<WorkflowConfigDTO.CategoryTreeDTO> getCategoryTree();
    
    /**
     * 获取流程分类列表
     * 
     * @param params 查询参数
     * @return 流程分类列表
     */
    Map<String, Object> getCategoryList(Map<String, Object> params);
    
    /**
     * 保存流程分类
     * 
     * @param category 流程分类信息
     * @return 保存后的流程分类信息
     */
    WorkflowConfigDTO.CategoryDTO saveCategory(WorkflowConfigDTO.CategoryDTO category);
    
    /**
     * 删除流程分类
     * 
     * @param categoryId 分类ID
     */
    void deleteCategory(String categoryId);
    
    /**
     * 批量更新分类状态
     * 
     * @param data 批量更新数据
     */
    void batchUpdateCategoryStatus(WorkflowConfigDTO.BatchUpdateDTO data);
    
    /**
     * 获取分类统计信息
     * 
     * @return 统计信息
     */
    Map<String, Object> getCategoryStats();
    
    /**
     * 获取通知模板列表
     * 
     * @param params 查询参数
     * @return 通知模板列表
     */
    Map<String, Object> getNotificationList(Map<String, Object> params);
    
    /**
     * 保存通知模板
     * 
     * @param notification 通知模板信息
     * @return 保存后的通知模板信息
     */
    WorkflowConfigDTO.NotificationDTO saveNotification(WorkflowConfigDTO.NotificationDTO notification);
    
    /**
     * 删除通知模板
     * 
     * @param notificationId 模板ID
     */
    void deleteNotification(String notificationId);
    
    /**
     * 批量更新模板状态
     * 
     * @param data 批量更新数据
     */
    void batchUpdateNotificationStatus(WorkflowConfigDTO.BatchUpdateDTO data);
    
    /**
     * 获取模板类型列表
     * 
     * @return 模板类型列表
     */
    List<WorkflowConfigDTO.TemplateTypeDTO> getTemplateTypes();
    
    /**
     * 获取事件类型列表
     * 
     * @return 事件类型列表
     */
    List<WorkflowConfigDTO.EventTypeDTO> getEventTypes();
    
    /**
     * 预览通知模板
     * 
     * @param templateId 模板ID
     * @param data 预览数据
     * @return 预览结果
     */
    WorkflowConfigDTO.NotificationPreviewResultDTO previewNotification(String templateId, WorkflowConfigDTO.NotificationPreviewDTO data);
    
    /**
     * 获取通知模板统计信息
     * 
     * @return 统计信息
     */
    Map<String, Object> getNotificationStats();
    
    /**
     * 获取报表配置列表
     * 
     * @param params 查询参数
     * @return 报表配置列表
     */
    Map<String, Object> getReportConfigList(Map<String, Object> params);
    
    /**
     * 保存报表配置
     * 
     * @param reportConfig 报表配置信息
     * @return 保存后的报表配置信息
     */
    WorkflowConfigDTO.ReportConfigDTO saveReportConfig(WorkflowConfigDTO.ReportConfigDTO reportConfig);
    
    /**
     * 删除报表配置
     * 
     * @param reportId 报表ID
     */
    void deleteReportConfig(String reportId);
    
    /**
     * 批量更新报表状态
     * 
     * @param data 批量更新数据
     */
    void batchUpdateReportStatus(WorkflowConfigDTO.BatchUpdateDTO data);
    
    /**
     * 获取报表类型列表
     * 
     * @return 报表类型列表
     */
    List<WorkflowConfigDTO.ReportTypeDTO> getReportTypes();
    
    /**
     * 获取报表统计信息
     * 
     * @return 统计信息
     */
    Map<String, Object> getReportStats();
    
    /**
     * 更新报表使用次数
     * 
     * @param reportId 报表ID
     */
    void updateReportUsageCount(String reportId);
    
    /**
     * 获取工作流实例列表
     * 
     * @param params 查询参数
     * @return 工作流实例列表
     */
    Map<String, Object> getInstanceList(Map<String, Object> params);
    
    /**
     * 获取实例统计信息
     * 
     * @param params 查询参数
     * @return 统计信息
     */
    Map<String, Object> getInstanceStats(Map<String, Object> params);
    
    /**
     * 批量更新实例状态
     * 
     * @param data 批量更新数据
     */
    void batchUpdateInstanceStatus(WorkflowConfigDTO.BatchUpdateDTO data);
    
    /**
     * 获取工作流任务列表
     * 
     * @param params 查询参数
     * @return 工作流任务列表
     */
    Map<String, Object> getTaskList(Map<String, Object> params);
    
    /**
     * 获取任务统计信息
     * 
     * @param params 查询参数
     * @return 统计信息
     */
    Map<String, Object> getTaskStats(Map<String, Object> params);
    
    /**
     * 批量更新任务状态
     * 
     * @param data 批量更新数据
     */
    void batchUpdateTaskStatus(WorkflowConfigDTO.BatchUpdateDTO data);
    
    /**
     * 批量分配任务
     * 
     * @param data 分配数据
     */
    void batchAssignTask(WorkflowConfigDTO.BatchAssignTaskDTO data);
    
    /**
     * 获取流程定义列表
     * 
     * @param params 查询参数
     * @return 流程定义列表
     */
    Map<String, Object> getProcessDefinitionList(Map<String, Object> params);
}