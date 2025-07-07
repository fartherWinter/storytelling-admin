package com.chennian.storytelling.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.chennian.storytelling.bean.dto.WorkflowFormDTO;
import com.chennian.storytelling.bean.model.workflow.WfFormTemplate;
import com.chennian.storytelling.bean.model.workflow.WfFormData;
import com.chennian.storytelling.bean.model.workflow.WfFieldOption;
import com.chennian.storytelling.common.exception.StorytellingBindException;
import com.chennian.storytelling.dao.workflow.WfFormTemplateMapper;
import com.chennian.storytelling.dao.workflow.WfFormDataMapper;
import com.chennian.storytelling.dao.workflow.WfFieldOptionMapper;
import com.chennian.storytelling.service.WorkflowFormService;
import com.esotericsoftware.minlog.Log;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import java.time.LocalDateTime;
import java.util.*;

/**
 * 工作流表单服务实现类
 * 
 * @author chennian
 */
@Slf4j
@Service
public class WorkflowFormServiceImpl implements WorkflowFormService {

    @Autowired
    private WfFormTemplateMapper wfFormTemplateMapper;
    
    @Autowired
    private WfFormDataMapper wfFormDataMapper;
    
    @Autowired
    private WfFieldOptionMapper wfFieldOptionMapper;
    
    private final ObjectMapper objectMapper = new ObjectMapper();

    @Override
    public List<WorkflowFormDTO.FormTemplateDTO> getAllFormTemplates() {
        List<WfFormTemplate> templates = wfFormTemplateMapper.selectList(
            new QueryWrapper<WfFormTemplate>()
                .eq("del_flag", 0)
                .orderByDesc("create_time")
        );
        
        List<WorkflowFormDTO.FormTemplateDTO> result = new ArrayList<>();
        for (WfFormTemplate template : templates) {
            WorkflowFormDTO.FormTemplateDTO dto = convertToFormTemplateDTO(template);
            result.add(dto);
        }
        return result;
    }

    @Override
    public WorkflowFormDTO.FormTemplateDTO getFormTemplate(String formId) {
        WfFormTemplate template = wfFormTemplateMapper.selectById(formId);
        if (template == null || template.getDeleted() == 1) {
            return null;
        }
        return convertToFormTemplateDTO(template);
    }

    @Override
    @Transactional
    public Map<String, Object> createFormTemplate(Map<String, Object> formData) {
        Map<String, Object> result = new HashMap<>();
        try {
            WfFormTemplate template = new WfFormTemplate();
            template.setFormId(UUID.randomUUID().toString());
            template.setFormName((String) formData.get("formName"));
            template.setFormDescription((String) formData.get("formDescription"));
            template.setProcessKey((String) formData.get("processKey"));
            template.setVersion(1);
            template.setFormConfig((String) formData.get("formConfig"));
            template.setFormFields((String) formData.get("formFields"));
            template.setFormLayout((String) formData.get("formLayout"));
            template.setFormRules((String) formData.get("formRules"));
            template.setCategory((String) formData.get("category"));
            template.setEnabled(1);
            template.setSortOrder(0);
            template.setCreatedTime(LocalDateTime.now());
            template.setUpdatedTime(LocalDateTime.now());
            template.setDeleted(0);
            
            wfFormTemplateMapper.insert(template);
            
            result.put("success", true);
            result.put("message", "表单模板创建成功");
            result.put("formId", template.getFormId());
        } catch (Exception e) {
            result.put("success", false);
            result.put("message", "表单模板创建失败: " + e.getMessage());
        }
        return result;
    }

    @Override
    @Transactional
    public Map<String, Object> updateFormTemplate(String formId, Map<String, Object> formData) {
        Map<String, Object> result = new HashMap<>();
        try {
            WfFormTemplate template = wfFormTemplateMapper.selectById(formId);
            if (template == null || template.getDeleted() == 1) {
                result.put("success", false);
                result.put("message", "表单模板不存在");
                return result;
            }
            
            template.setFormName((String) formData.get("formName"));
            template.setFormDescription((String) formData.get("formDescription"));
            template.setProcessKey((String) formData.get("processKey"));
            template.setFormConfig((String) formData.get("formConfig"));
            template.setFormFields((String) formData.get("formFields"));
            template.setFormLayout((String) formData.get("formLayout"));
            template.setFormRules((String) formData.get("formRules"));
            template.setCategory((String) formData.get("category"));
            template.setUpdatedTime(LocalDateTime.now());
            
            wfFormTemplateMapper.updateById(template);
            
            result.put("success", true);
            result.put("message", "表单模板更新成功");
        } catch (Exception e) {
            result.put("success", false);
            result.put("message", "表单模板更新失败: " + e.getMessage());
        }
        return result;
    }

    @Override
    @Transactional
    public Map<String, Object> deleteFormTemplate(String formId) {
        Map<String, Object> result = new HashMap<>();
        try {
            WfFormTemplate template = wfFormTemplateMapper.selectById(formId);
            if (template == null || template.getDeleted() == 1) {
                result.put("success", false);
                result.put("message", "表单模板不存在");
                return result;
            }
            
            template.setDeleted(1);
            template.setUpdatedTime(LocalDateTime.now());
            wfFormTemplateMapper.updateById(template);
            
            result.put("success", true);
            result.put("message", "表单模板删除成功");
        } catch (Exception e) {
            result.put("success", false);
            result.put("message", "表单模板删除失败: " + e.getMessage());
        }
        return result;
    }

    @Override
    @Transactional
    public Map<String, Object> copyFormTemplate(String formId, String newName) {
        Map<String, Object> result = new HashMap<>();
        try {
            WfFormTemplate sourceTemplate = wfFormTemplateMapper.selectById(formId);
            if (sourceTemplate == null || sourceTemplate.getDeleted() == 1) {
                result.put("success", false);
                result.put("message", "源表单模板不存在");
                return result;
            }
            
            WfFormTemplate newTemplate = new WfFormTemplate();
            newTemplate.setFormId(UUID.randomUUID().toString());
            newTemplate.setFormName(newName);
            newTemplate.setFormDescription(sourceTemplate.getFormDescription());
            newTemplate.setProcessKey(sourceTemplate.getProcessKey());
            newTemplate.setVersion(1);
            newTemplate.setFormConfig(sourceTemplate.getFormConfig());
            newTemplate.setFormFields(sourceTemplate.getFormFields());
            newTemplate.setFormLayout(sourceTemplate.getFormLayout());
            newTemplate.setFormRules(sourceTemplate.getFormRules());
            newTemplate.setCategory(sourceTemplate.getCategory());
            newTemplate.setEnabled(1);
            newTemplate.setSortOrder(0);
            newTemplate.setCreatedTime(LocalDateTime.now());
            newTemplate.setUpdatedTime(LocalDateTime.now());
            newTemplate.setDeleted(0);
            
            wfFormTemplateMapper.insert(newTemplate);
            
            result.put("success", true);
            result.put("message", "表单模板复制成功");
            result.put("formId", newTemplate.getFormId());
        } catch (Exception e) {
            result.put("success", false);
            result.put("message", "表单模板复制失败: " + e.getMessage());
        }
        return result;
    }

    @Override
    public Map<String, Object> getFormData(String formId, String instanceId) {
        Map<String, Object> result = new HashMap<>();
        try {
            // 根据表单ID和实例ID查询表单数据
            WfFormData formData = wfFormDataMapper.selectByFormIdAndInstanceId(formId, instanceId);
            
            Map<String, Object> data = new HashMap<>();
            if (formData != null && StringUtils.hasText(formData.getFormData())) {
                // 将JSON字符串转换为Map
                try {
                    data = objectMapper.readValue(formData.getFormData(), new TypeReference<Map<String, Object>>() {});
                } catch (Exception e) {
                    // 如果JSON解析失败，返回空数据
                    data = new HashMap<>();
                }
                
                // 添加元数据
                // 使用HashMap支持null值和扩展性
                Map<String, Object> metadata = new LinkedHashMap<>();
                metadata.put("id", formData.getId());
                metadata.put("formId", formData.getFormId());
                metadata.put("instanceId", formData.getInstanceId());
                metadata.put("taskId", formData.getTaskId());
                metadata.put("version", formData.getVersion());
                metadata.put("status", formData.getStatus());
                metadata.put("businessKey", formData.getBusinessKey());
                metadata.put("creatorId", formData.getCreatorId());
                metadata.put("creatorName", formData.getCreatorName());
                metadata.put("createTime", formData.getCreateTime());
                metadata.put("updateTime", formData.getUpdateTime());

                data.put("_metadata", metadata);
            }
            result.put("success", true);
            result.put("data", data);
        } catch (Exception e) {
            result.put("success", false);
            result.put("message", "获取表单数据失败: " + e.getMessage());
        }
        return result;
    }

    @Override
    @Transactional
    public Map<String, Object> saveFormData(String formId, String instanceId, Map<String, Object> formData) {
        Map<String, Object> result = new HashMap<>();
        try {
            // 提取元数据
            String taskId = (String) formData.get("taskId");
            String businessKey = (String) formData.get("businessKey");
            String creatorId = (String) formData.get("creatorId");
            String creatorName = (String) formData.get("creatorName");
            
            // 移除元数据，只保存实际表单数据
            Map<String, Object> actualFormData = new HashMap<>(formData);
            actualFormData.remove("taskId");
            actualFormData.remove("businessKey");
            actualFormData.remove("creatorId");
            actualFormData.remove("creatorName");
            actualFormData.remove("_metadata");
            
            // 将表单数据转换为JSON字符串
            String formDataJson = objectMapper.writeValueAsString(actualFormData);
            
            // 查询是否已存在数据
            WfFormData existingData = wfFormDataMapper.selectByFormIdAndInstanceId(formId, instanceId);
            
            if (existingData != null) {
                // 更新现有数据
                existingData.setFormData(formDataJson);
                existingData.setTaskId(taskId);
                existingData.setBusinessKey(businessKey);
                existingData.setVersion(existingData.getVersion() + 1);
                existingData.setUpdaterId(creatorId);
                existingData.setUpdaterName(creatorName);
                existingData.setUpdateTime(LocalDateTime.now());
                
                wfFormDataMapper.updateById(existingData);
                result.put("id", existingData.getId());
            } else {
                // 创建新数据
                WfFormData newFormData = new WfFormData();
                newFormData.setFormId(formId);
                newFormData.setInstanceId(instanceId);
                newFormData.setTaskId(taskId);
                newFormData.setFormData(formDataJson);
                newFormData.setVersion(1);
                newFormData.setStatus(0);
                newFormData.setBusinessKey(businessKey);
                newFormData.setCreatorId(creatorId);
                newFormData.setCreatorName(creatorName);
                newFormData.setCreateTime(LocalDateTime.now());
                newFormData.setUpdateTime(LocalDateTime.now());
                newFormData.setDeleted(0);
                
                wfFormDataMapper.insert(newFormData);
                result.put("id", newFormData.getId());
            }
            
            result.put("success", true);
            result.put("message", "表单数据保存成功");
        } catch (Exception e) {
            result.put("success", false);
            result.put("message", "保存表单数据失败: " + e.getMessage());
        }
        return result;
    }

    @Override
    public Map<String, Object> validateFormData(String formId, Map<String, Object> formData) {
        Map<String, Object> result = new HashMap<>();
        List<Map<String, Object>> errors = new ArrayList<>();
        
        try {
            // 获取表单定义
            WfFormTemplate formTemplate = wfFormTemplateMapper.selectById(formId);
            if (formTemplate == null || formTemplate.getDeleted() == 1) {
                result.put("valid", false);
                result.put("message", "表单定义不存在");
                return result;
            }
            
            // 解析表单定义
            Map<String, Object> formDefinition;
            try {
                formDefinition = objectMapper.readValue(formTemplate.getFormFields(), new TypeReference<Map<String, Object>>() {});
            } catch (Exception e) {
                result.put("valid", false);
                result.put("message", "表单定义格式错误");
                return result;
            }
            
            // 获取字段定义
            List<Map<String, Object>> fields = (List<Map<String, Object>>) formDefinition.get("fields");
            if (fields != null) {
                for (Map<String, Object> field : fields) {
                    String fieldId = (String) field.get("id");
                    String fieldName = (String) field.get("name");
                    String fieldType = (String) field.get("type");
                    Boolean required = (Boolean) field.get("required");
                    Map<String, Object> validation = (Map<String, Object>) field.get("validation");
                    
                    Object fieldValue = formData.get(fieldId);
                    
                    // 必填验证
                    if (Boolean.TRUE.equals(required) && (fieldValue == null || 
                        (fieldValue instanceof String && ((String) fieldValue).trim().isEmpty()))) {
                        errors.add(Map.of(
                            "field", fieldId,
                            "fieldName", fieldName != null ? fieldName : fieldId,
                            "message", "该字段为必填项",
                            "type", "required"
                        ));
                        continue;
                    }
                    
                    // 如果字段有值，进行类型和格式验证
                    if (fieldValue != null && validation != null) {
                        // 字符串长度验证
                        if ("text".equals(fieldType) || "textarea".equals(fieldType)) {
                            String strValue = fieldValue.toString();
                            Integer minLength = (Integer) validation.get("minLength");
                            Integer maxLength = (Integer) validation.get("maxLength");
                            
                            if (minLength != null && strValue.length() < minLength) {
                                errors.add(Map.of(
                                    "field", fieldId,
                                    "fieldName", fieldName != null ? fieldName : fieldId,
                                    "message", "字符长度不能少于" + minLength + "个字符",
                                    "type", "minLength"
                                ));
                            }
                            
                            if (maxLength != null && strValue.length() > maxLength) {
                                errors.add(Map.of(
                                    "field", fieldId,
                                    "fieldName", fieldName != null ? fieldName : fieldId,
                                    "message", "字符长度不能超过" + maxLength + "个字符",
                                    "type", "maxLength"
                                ));
                            }
                        }
                        
                        // 数字范围验证
                        if ("number".equals(fieldType)) {
                            try {
                                Double numValue = Double.parseDouble(fieldValue.toString());
                                Double min = validation.get("min") != null ? Double.parseDouble(validation.get("min").toString()) : null;
                                Double max = validation.get("max") != null ? Double.parseDouble(validation.get("max").toString()) : null;
                                
                                if (min != null && numValue < min) {
                                    errors.add(Map.of(
                                        "field", fieldId,
                                        "fieldName", fieldName != null ? fieldName : fieldId,
                                        "message", "数值不能小于" + min,
                                        "type", "min"
                                    ));
                                }
                                
                                if (max != null && numValue > max) {
                                    errors.add(Map.of(
                                        "field", fieldId,
                                        "fieldName", fieldName != null ? fieldName : fieldId,
                                        "message", "数值不能大于" + max,
                                        "type", "max"
                                    ));
                                }
                            } catch (NumberFormatException e) {
                                errors.add(Map.of(
                                    "field", fieldId,
                                    "fieldName", fieldName != null ? fieldName : fieldId,
                                    "message", "请输入有效的数字",
                                    "type", "format"
                                ));
                            }
                        }
                        
                        // 正则表达式验证
                        String pattern = (String) validation.get("pattern");
                        if (pattern != null && fieldValue instanceof String) {
                            if (!((String) fieldValue).matches(pattern)) {
                                String patternMessage = (String) validation.get("patternMessage");
                                errors.add(Map.of(
                                    "field", fieldId,
                                    "fieldName", fieldName != null ? fieldName : fieldId,
                                    "message", patternMessage != null ? patternMessage : "格式不正确",
                                    "type", "pattern"
                                ));
                            }
                        }
                    }
                }
            }
            
            result.put("valid", errors.isEmpty());
            result.put("errors", errors);
            
        } catch (Exception e) {
            result.put("valid", false);
            result.put("message", "表单数据验证失败: " + e.getMessage());
        }
        return result;
    }

    @Override
    public List<WorkflowFormDTO.FieldOptionDTO> getFieldOptions(String formId, String fieldId, String query) {
        List<WorkflowFormDTO.FieldOptionDTO> options = new ArrayList<>();
        try {
            // 从数据库查询字段选项
            List<WfFieldOption> fieldOptions = wfFieldOptionMapper.selectByFormIdAndFieldId(formId, fieldId);
            
            // 转换为DTO
            for (WfFieldOption option : fieldOptions) {
                WorkflowFormDTO.FieldOptionDTO dto = new WorkflowFormDTO.FieldOptionDTO();
                dto.setValue(option.getOptionValue());
                dto.setLabel(option.getOptionLabel());
                dto.setDescription(option.getOptionDescription());
                dto.setDisabled(option.getDisabled() == 0);
                
                // 如果有查询条件，进行过滤
                if (StringUtils.hasText(query)) {
                    if (option.getOptionLabel().toLowerCase().contains(query.toLowerCase()) ||
                        option.getOptionValue().toLowerCase().contains(query.toLowerCase())) {
                        options.add(dto);
                    }
                } else {
                    options.add(dto);
                }
            }
            
            // 按排序字段排序
            options.sort((o1, o2) -> {
                // 这里需要根据实际的排序字段来实现，暂时按label排序
                return o1.getLabel().compareTo(o2.getLabel());
            });
            
        } catch (Exception e) {
            // 记录错误日志
            Log.error("获取字段选项失败: " + e.getMessage());
            throw new StorytellingBindException("获取字段选项失败");
        }
        return options;
    }
    @Override
    @Transactional
    public List<WorkflowFormDTO.FieldOptionDTO> saveFieldOptions(String formId, String fieldId, List<WorkflowFormDTO.FieldOptionDTO> options) {
        Map<String, Object> result = new HashMap<>();
        try {
            // 先删除该字段的所有现有选项
            wfFieldOptionMapper.deleteByFormIdAndFieldId(formId, fieldId);
            
            // 批量插入新的选项
            if (options != null && !options.isEmpty()) {
                List<WfFieldOption> fieldOptions = new ArrayList<>();
                
                for (int i = 0; i < options.size(); i++) {
                    WorkflowFormDTO.FieldOptionDTO dto = options.get(i);
                    
                    WfFieldOption option = new WfFieldOption();
                    option.setFormId(formId);
                    option.setFieldId(fieldId);
                    option.setOptionValue(dto.getValue());
                    option.setOptionLabel(dto.getLabel());
                    option.setOptionDescription(dto.getDescription());
                    // 按顺序设置排序
                    option.setSortOrder(i + 1);
                    option.setDisabled(dto.getDisabled() != null && dto.getDisabled() ? 0 : 1);
                    option.setIsDefault(0);
                    option.setOptionGroup(null);
                    option.setExtraProps("{}");
                    
                    option.setCreateTime(LocalDateTime.now());
                    option.setUpdateTime(LocalDateTime.now());
                    option.setDeleted(0);
                    
                    fieldOptions.add(option);
                }
                
                // 批量插入
                wfFieldOptionMapper.batchInsert(fieldOptions);
            }
        } catch (Exception e) {
            throw new StorytellingBindException("保存字段选项失败");
        }
        return options;
    }
    @Override
    public WorkflowFormDTO.FormDefinitionDTO getFormDefinition(String formKey) {
        try {
            // 从数据库获取表单模板
            WfFormTemplate formTemplate = wfFormTemplateMapper.selectById(formKey);
            
            if (formTemplate == null || formTemplate.getDeleted() == 1) {
                return null;
            }
            
            // 构建FormDefinitionDTO
            WorkflowFormDTO.FormDefinitionDTO formDefinition = new WorkflowFormDTO.FormDefinitionDTO();
            formDefinition.setId(formTemplate.getFormId());
            formDefinition.setName(formTemplate.getFormName());
            formDefinition.setDescription(formTemplate.getFormDescription());
            formDefinition.setProcessKey(formTemplate.getProcessKey());
            formDefinition.setVersion(formTemplate.getVersion());
            formDefinition.setFormConfig(formTemplate.getFormConfig());
            formDefinition.setFormFields(formTemplate.getFormFields());
            formDefinition.setFormLayout(formTemplate.getFormLayout());
            formDefinition.setFormRules(formTemplate.getFormRules());
            formDefinition.setEnabled(formTemplate.getEnabled() != null && formTemplate.getEnabled() == 1);
            formDefinition.setSortOrder(formTemplate.getSortOrder());
            formDefinition.setRemark(formTemplate.getRemark());
            formDefinition.setCreatedTime(formTemplate.getCreatedTime());
            formDefinition.setUpdatedTime(formTemplate.getUpdatedTime());
            
            return formDefinition;
            
        } catch (Exception e) {
            // 记录错误日志
            System.err.println("获取表单定义失败: " + e.getMessage());
            return null;
        }
    }

    @Override
    @Transactional
    public WorkflowFormDTO.FormDefinitionDTO saveFormDefinition(WorkflowFormDTO.FormDefinitionDTO definition) {
        Map<String, Object> result = new HashMap<>();
        try {
            // 查询是否已存在表单定义
            WfFormTemplate existingTemplate = wfFormTemplateMapper.selectById(definition.getId());
            String formId = definition.getId();
            // 提取定义中的各个部分
            String formName = definition.getName();
            String description = definition.getDescription();
            // FormDefinitionDTO没有category字段，使用默认值或从其他地方获取
            // definition没有category字段
            String category = "DEFAULT";
            // 使用DTO中的status字段
            String status = definition.getStatus();
            Integer version = definition.getVersion();
            
            // FormDefinitionDTO没有_metadata字段，暂时设置为null
            String creatorId = null;
            String creatorName = null;
            
            // 构建字段定义JSON
            Map<String, Object> fieldsData = new HashMap<>();
            fieldsData.put("fields", definition.getFields());
            fieldsData.put("layout", definition.getLayout());
            fieldsData.put("rules", definition.getRules());
            String formFields = objectMapper.writeValueAsString(fieldsData);
            
            // 构建样式JSON
            String formStyle = "{}";
            if (definition.getStyles() != null) {
                formStyle = objectMapper.writeValueAsString(definition.getStyles());
            }
            
            // 构建配置JSON - 使用formConfig字段
            String formConfig = definition.getFormConfig() != null ? definition.getFormConfig() : "{}";
            
            if (existingTemplate != null && existingTemplate.getDeleted() != 1) {
                // 更新现有定义
                existingTemplate.setFormName(formName);
                existingTemplate.setFormDescription(description);
                existingTemplate.setCategory(category);
                existingTemplate.setVersion(version != null ? version : existingTemplate.getVersion() + 1);
                existingTemplate.setFormFields(formFields);
                existingTemplate.setFormLayout(definition.getFormLayout());
                existingTemplate.setFormRules(definition.getFormRules());
                existingTemplate.setFormConfig(formConfig);
                existingTemplate.setUpdatedTime(LocalDateTime.now());
                
                wfFormTemplateMapper.updateById(existingTemplate);
                result.put("id", existingTemplate.getFormId());
                result.put("formId", existingTemplate.getFormId());
                
            } else {
                // 创建新定义
                WfFormTemplate newTemplate = new WfFormTemplate();
                newTemplate.setFormId(formId);
                newTemplate.setFormName(formName != null ? formName : "未命名表单");
                newTemplate.setFormDescription(description);
                newTemplate.setCategory(category != null ? category : "DEFAULT");
                newTemplate.setProcessKey(definition.getProcessKey());
                newTemplate.setVersion(version != null ? version : 1);
                newTemplate.setFormFields(formFields);
                newTemplate.setFormLayout(definition.getFormLayout());
                newTemplate.setFormRules(definition.getFormRules());
                newTemplate.setFormConfig(formConfig);
                newTemplate.setEnabled(1);
                newTemplate.setSortOrder(0);
                newTemplate.setCreatedTime(LocalDateTime.now());
                newTemplate.setUpdatedTime(LocalDateTime.now());
                newTemplate.setDeleted(0);
                
                wfFormTemplateMapper.insert(newTemplate);
            }
        } catch (Exception e) {
            throw new StorytellingBindException("保存表单定义失败");
        }
        return definition;
    }

    @Override
    public Map<String, Object> exportFormTemplate(String formId) {
        Map<String, Object> result = new HashMap<>();
        try {
            WfFormTemplate template = wfFormTemplateMapper.selectById(formId);
            if (template == null || template.getDeleted() == 1) {
                result.put("success", false);
                result.put("message", "表单模板不存在");
                return result;
            }
            
            Map<String, Object> exportData = new HashMap<>();
            exportData.put("formName", template.getFormName());
            exportData.put("formDescription", template.getFormDescription());
            exportData.put("formConfig", template.getFormConfig());
            exportData.put("formFields", template.getFormFields());
            exportData.put("formLayout", template.getFormLayout());
            exportData.put("formRules", template.getFormRules());
            exportData.put("category", template.getCategory());
            
            result.put("success", true);
            result.put("data", exportData);
        } catch (Exception e) {
            result.put("success", false);
            result.put("message", "导出失败: " + e.getMessage());
        }
        return result;
    }

    @Override
    @Transactional
    public Map<String, Object> importFormTemplate(Map<String, Object> formTemplate) {
        Map<String, Object> result = new HashMap<>();
        try {
            WfFormTemplate template = new WfFormTemplate();
            template.setFormId(UUID.randomUUID().toString());
            template.setFormName((String) formTemplate.get("formName"));
            template.setFormDescription((String) formTemplate.get("formDescription"));
            template.setVersion(1);
            template.setFormConfig((String) formTemplate.get("formConfig"));
            template.setFormFields((String) formTemplate.get("formFields"));
            template.setFormLayout((String) formTemplate.get("formLayout"));
            template.setFormRules((String) formTemplate.get("formRules"));
            template.setCategory((String) formTemplate.get("category"));
            template.setEnabled(1);
            template.setSortOrder(0);
            template.setCreatedTime(LocalDateTime.now());
            template.setUpdatedTime(LocalDateTime.now());
            template.setDeleted(0);
            
            wfFormTemplateMapper.insert(template);
            
            result.put("success", true);
            result.put("message", "导入成功");
            result.put("formId", template.getFormId());
        } catch (Exception e) {
            result.put("success", false);
            result.put("message", "导入失败: " + e.getMessage());
        }
        return result;
    }

    private WorkflowFormDTO.FormTemplateDTO convertToFormTemplateDTO(WfFormTemplate template) {
        WorkflowFormDTO.FormTemplateDTO dto = new WorkflowFormDTO.FormTemplateDTO();
        dto.setId(template.getFormId());
        dto.setName(template.getFormName());
        dto.setDescription(template.getFormDescription());
        dto.setCategory(template.getCategory());
        dto.setProcessKey(template.getProcessKey());
        dto.setVersion(template.getVersion());
        dto.setFormConfig(template.getFormConfig());
        dto.setFormFields(template.getFormFields());
        dto.setFormLayout(template.getFormLayout());
        dto.setFormRules(template.getFormRules());
        dto.setEnabled(template.getEnabled());
        dto.setSortOrder(template.getSortOrder());
        dto.setRemark(template.getRemark());
        dto.setCreatedTime(template.getCreatedTime());
        dto.setUpdatedTime(template.getUpdatedTime());
        
        // 设置是否公开（将数据库的Integer转换为Boolean）
        dto.setIsPublic(template.getIsPublic() != null && template.getIsPublic() == 1);
        
        // 设置创建者
        dto.setCreator(template.getCreatorName());
        
        // 使用次数默认为0，实际使用时可能需要从其他地方获取
        dto.setUsageCount(0);
        
        // 构建FormDefinitionDTO作为template字段
        WorkflowFormDTO.FormDefinitionDTO formDefinition = new WorkflowFormDTO.FormDefinitionDTO();
        formDefinition.setId(template.getFormId());
        formDefinition.setName(template.getFormName());
        formDefinition.setDescription(template.getFormDescription());
        formDefinition.setProcessKey(template.getProcessKey());
        formDefinition.setVersion(template.getVersion());
        formDefinition.setFormConfig(template.getFormConfig());
        formDefinition.setFormFields(template.getFormFields());
        formDefinition.setFormLayout(template.getFormLayout());
        formDefinition.setFormRules(template.getFormRules());
        formDefinition.setEnabled(template.getEnabled() != null && template.getEnabled() == 1);
        formDefinition.setSortOrder(template.getSortOrder());
        formDefinition.setRemark(template.getRemark());
        formDefinition.setCreatedTime(template.getCreatedTime());
        formDefinition.setUpdatedTime(template.getUpdatedTime());
        
        dto.setTemplate(formDefinition);
        
        return dto;
    }

}