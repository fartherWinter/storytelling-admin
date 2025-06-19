package com.chennian.storytelling.service;

import com.chennian.storytelling.bean.dto.WorkflowFormDTO;
import com.chennian.storytelling.bean.model.workflow.WfFormTemplate;

import java.util.List;
import java.util.Map;


/**
 * 工作流表单服务接口
 * 
 * @author chennian
 */
public interface WorkflowFormService {

    /**
     * 获取所有表单模板
     * 
     * @return 表单模板列表
     */
    List<WorkflowFormDTO.FormTemplateDTO> getAllFormTemplates();

    /**
     * 根据表单ID获取表单模板
     * 
     * @param formId 表单ID
     * @return 表单模板
     */
    WorkflowFormDTO.FormTemplateDTO getFormTemplate(String formId);

    /**
     * 创建表单模板
     * 
     * @param formData 表单数据
     * @return 创建结果
     */
    Map<String, Object> createFormTemplate(Map<String, Object> formData);

    /**
     * 更新表单模板
     * 
     * @param formId 表单ID
     * @param formData 表单数据
     * @return 更新结果
     */
    Map<String, Object> updateFormTemplate(String formId, Map<String, Object> formData);

    /**
     * 删除表单模板
     * 
     * @param formId 表单ID
     * @return 删除结果
     */
    Map<String, Object> deleteFormTemplate(String formId);

    /**
     * 复制表单模板
     * 
     * @param formId 源表单ID
     * @param newName 新表单名称
     * @return 复制结果
     */
    Map<String, Object> copyFormTemplate(String formId, String newName);

    /**
     * 获取表单数据
     * 
     * @param formId 表单ID
     * @param instanceId 实例ID
     * @return 表单数据
     */
    Map<String, Object> getFormData(String formId, String instanceId);

    /**
     * 保存表单数据
     * 
     * @param formId 表单ID
     * @param instanceId 实例ID
     * @param formData 表单数据
     * @return 保存结果
     */
    Map<String, Object> saveFormData(String formId, String instanceId, Map<String, Object> formData);

    /**
     * 验证表单数据
     * 
     * @param formId 表单ID
     * @param formData 表单数据
     * @return 验证结果
     */
    Map<String, Object> validateFormData(String formId, Map<String, Object> formData);

    /**
     * 获取字段选项
     * 
     * @param formId 表单ID
     * @param fieldId 字段ID
     * @param query 查询条件
     * @return 字段选项列表
     */
    List<WorkflowFormDTO.FieldOptionDTO> getFieldOptions(String formId, String fieldId, String query);
    
    /**
     * 保存字段选项
     * 
     * @param formId 表单ID
     * @param fieldId 字段ID
     * @param options 字段选项列表
     * @return 保存后的字段选项列表
     */
    List<WorkflowFormDTO.FieldOptionDTO> saveFieldOptions(String formId, String fieldId, List<WorkflowFormDTO.FieldOptionDTO> options);
    
    /**
     * 获取表单定义
     * 
     * @param formKey 表单键
     * @return 表单定义
     */
    WorkflowFormDTO.FormDefinitionDTO getFormDefinition(String formKey);
    
    /**
     * 保存表单定义
     * 
     * @param formDefinition 表单定义
     * @return 保存后的表单定义
     */
    WorkflowFormDTO.FormDefinitionDTO saveFormDefinition(WorkflowFormDTO.FormDefinitionDTO formDefinition);

    /**
     * 导出表单模板
     * 
     * @param formId 表单ID
     * @return 导出结果
     */
    Map<String, Object> exportFormTemplate(String formId);

    /**
     * 导入表单模板
     * 
     * @param formTemplate 表单模板数据
     * @return 导入结果
     */
    Map<String, Object> importFormTemplate(Map<String, Object> formTemplate);
}