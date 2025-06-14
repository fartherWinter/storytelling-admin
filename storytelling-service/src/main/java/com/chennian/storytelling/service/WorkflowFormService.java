package com.chennian.storytelling.service;

import com.chennian.storytelling.bean.dto.WorkflowFormDTO;

import java.util.List;


/**
 * 工作流表单服务接口
 * 
 * @author chennian
 */
public interface WorkflowFormService {

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
}