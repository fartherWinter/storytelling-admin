package com.chennian.storytelling.service.impl;

import java.util.List;
import java.util.ArrayList;

import com.chennian.storytelling.bean.dto.WorkflowFormDTO;
import org.springframework.stereotype.Service;
import lombok.extern.slf4j.Slf4j;

import com.chennian.storytelling.service.WorkflowFormService;
import com.chennian.storytelling.common.utils.EntityUtils;


/**
 * 工作流表单服务实现类
 * 
 * @author chennian
 */
@Slf4j
@Service
public class WorkflowFormServiceImpl implements WorkflowFormService {

    @Override
    public List<WorkflowFormDTO.FieldOptionDTO> getFieldOptions(String formId, String fieldId, String query) {
        log.info("获取字段选项，表单ID: {}, 字段ID: {}, 查询条件: {}", formId, fieldId, query);
        
        List<WorkflowFormDTO.FieldOptionDTO> result = new ArrayList<>();
        
        try {
            // 根据字段类型返回不同的选项
            if ("department".equals(fieldId)) {
                // 部门选项
                result.add(createOption("dept_001", "技术部"));
                result.add(createOption("dept_002", "市场部"));
                result.add(createOption("dept_003", "人事部"));
                result.add(createOption("dept_004", "财务部"));
            } else if ("priority".equals(fieldId)) {
                // 优先级选项
                result.add(createOption("high", "高"));
                result.add(createOption("medium", "中"));
                result.add(createOption("low", "低"));
            } else if ("status".equals(fieldId)) {
                // 状态选项
                result.add(createOption("pending", "待处理"));
                result.add(createOption("processing", "处理中"));
                result.add(createOption("completed", "已完成"));
                result.add(createOption("rejected", "已拒绝"));
            } else if ("category".equals(fieldId)) {
                // 分类选项
                result.add(createOption("leave", "请假申请"));
                result.add(createOption("expense", "费用报销"));
                result.add(createOption("purchase", "采购申请"));
                result.add(createOption("contract", "合同审批"));
            }
            
            // 根据查询条件过滤选项
            if (query != null && !query.trim().isEmpty()) {
                String queryLower = query.toLowerCase();
                result = result.stream()
                    .filter(option -> option.getLabel().toLowerCase().contains(queryLower) ||
                                    option.getValue().toLowerCase().contains(queryLower))
                    .collect(java.util.stream.Collectors.toList());
            }
            
        } catch (Exception e) {
            log.error("获取字段选项失败", e);
            throw new RuntimeException("获取字段选项失败", e);
        }
        
        return result;
    }
    
    private WorkflowFormDTO.FieldOptionDTO createOption(String value, String label) {
        WorkflowFormDTO.FieldOptionDTO option = new WorkflowFormDTO.FieldOptionDTO();
        option.setValue(value);
        option.setLabel(label);
        option.setDisabled(false);
        return option;
    }
    
    @Override
    public List<WorkflowFormDTO.FieldOptionDTO> saveFieldOptions(String formId, String fieldId, List<WorkflowFormDTO.FieldOptionDTO> options) {
        log.info("保存字段选项，表单ID: {}, 字段ID: {}, 选项数量: {}", formId, fieldId, options.size());
        
        try {
            // 验证表单ID和字段ID的有效性
            if (formId == null || formId.trim().isEmpty()) {
                throw new IllegalArgumentException("表单ID不能为空");
            }
            if (fieldId == null || fieldId.trim().isEmpty()) {
                throw new IllegalArgumentException("字段ID不能为空");
            }
            
            // 验证选项数据的有效性
            for (WorkflowFormDTO.FieldOptionDTO option : options) {
                if (option.getValue() == null || option.getValue().trim().isEmpty()) {
                    throw new IllegalArgumentException("选项值不能为空");
                }
                if (option.getLabel() == null || option.getLabel().trim().isEmpty()) {
                    throw new IllegalArgumentException("选项标签不能为空");
                }
            }
            
            // 这里应该保存到数据库，目前模拟保存成功
            log.info("字段选项保存成功，表单ID: {}, 字段ID: {}", formId, fieldId);
            
            // 返回保存后的选项列表（实际应该从数据库重新查询）
            return options;
            
        } catch (Exception e) {
            log.error("保存字段选项失败", e);
            throw new RuntimeException("保存字段选项失败: " + e.getMessage(), e);
        }
    }
    
    @Override
    public WorkflowFormDTO.FormDefinitionDTO getFormDefinition(String formKey) {
        log.info("获取表单定义，表单键: {}", formKey);
        
        try {
            if (formKey == null || formKey.trim().isEmpty()) {
                throw new IllegalArgumentException("表单键不能为空");
            }
            
            WorkflowFormDTO.FormDefinitionDTO result = new WorkflowFormDTO.FormDefinitionDTO();
            
            // 根据表单键返回不同的表单定义
            if ("leave_application".equals(formKey)) {
                result = createLeaveApplicationForm();
            } else if ("expense_reimbursement".equals(formKey)) {
                result = createExpenseReimbursementForm();
            } else if ("purchase_request".equals(formKey)) {
                result = createPurchaseRequestForm();
            } else {
                // 默认表单
                result = createDefaultForm(formKey);
            }
            
            return result;
            
        } catch (Exception e) {
            log.error("获取表单定义失败", e);
            throw new RuntimeException("获取表单定义失败: " + e.getMessage(), e);
        }
    }
    
    @Override
    public WorkflowFormDTO.FormDefinitionDTO saveFormDefinition(WorkflowFormDTO.FormDefinitionDTO formDefinition) {
        log.info("保存表单定义，表单键: {}", formDefinition.getFormKey());
        
        try {
            // 验证表单定义的完整性
            if (formDefinition == null) {
                throw new IllegalArgumentException("表单定义不能为空");
            }
            if (formDefinition.getFormKey() == null || formDefinition.getFormKey().trim().isEmpty()) {
                throw new IllegalArgumentException("表单键不能为空");
            }
            if (formDefinition.getName() == null || formDefinition.getName().trim().isEmpty()) {
                throw new IllegalArgumentException("表单名称不能为空");
            }
            
            // 验证表单字段
            if (formDefinition.getFields() != null) {
                for (WorkflowFormDTO.FormFieldDTO field : formDefinition.getFields()) {
                    if (field.getId() == null || field.getId().trim().isEmpty()) {
                        throw new IllegalArgumentException("字段ID不能为空");
                    }
                    if (field.getName() == null || field.getName().trim().isEmpty()) {
                        throw new IllegalArgumentException("字段名称不能为空");
                    }
                }
            }
            
            // 这里应该保存到数据库，目前模拟保存成功
            log.info("表单定义保存成功，表单键: {}", formDefinition.getFormKey());
            
            // 返回保存后的表单定义（实际应该从数据库重新查询）
            return formDefinition;
            
        } catch (Exception e) {
            log.error("保存表单定义失败", e);
            throw new RuntimeException("保存表单定义失败: " + e.getMessage(), e);
        }
    }
    
    /**
     * 创建请假申请表单
     */
    private WorkflowFormDTO.FormDefinitionDTO createLeaveApplicationForm() {
        WorkflowFormDTO.FormDefinitionDTO form = new WorkflowFormDTO.FormDefinitionDTO();
        form.setFormKey("leave_application");
        form.setName("请假申请");
        form.setDescription("员工请假申请表单");
        
        List<WorkflowFormDTO.FormFieldDTO> fields = new ArrayList<>();
        
        // 使用通用方法创建字段
        fields.add(createFormField("leaveType", "请假类型", "select", true));
        fields.add(createFormField("startDate", "开始时间", "date", true));
        fields.add(createFormField("endDate", "结束时间", "date", true));
        fields.add(createFormField("reason", "请假原因", "textarea", true));
        
        form.setFields(fields);
        return form;
    }
    
    /**
     * 创建费用报销表单
     */
    private WorkflowFormDTO.FormDefinitionDTO createExpenseReimbursementForm() {
        WorkflowFormDTO.FormDefinitionDTO form = new WorkflowFormDTO.FormDefinitionDTO();
        form.setFormKey("expense_reimbursement");
        form.setName("费用报销");
        form.setDescription("员工费用报销申请表单");
        
        List<WorkflowFormDTO.FormFieldDTO> fields = new ArrayList<>();
        
        // 使用通用方法创建字段
        fields.add(createFormField("expenseType", "报销类型", "select", true));
        fields.add(createFormField("amount", "报销金额", "text", true));
        fields.add(createFormField("description", "费用说明", "textarea", true));
        
        form.setFields(fields);
        return form;
    }
    
    /**
     * 创建采购申请表单
     */
    private WorkflowFormDTO.FormDefinitionDTO createPurchaseRequestForm() {
        WorkflowFormDTO.FormDefinitionDTO form = new WorkflowFormDTO.FormDefinitionDTO();
        form.setFormKey("purchase_request");
        form.setName("采购申请");
        form.setDescription("物品采购申请表单");
        
        List<WorkflowFormDTO.FormFieldDTO> fields = new ArrayList<>();
        
        // 使用通用方法创建字段
        fields.add(createFormField("itemName", "物品名称", "text", true));
        fields.add(createFormField("quantity", "数量", "text", true));
        fields.add(createFormField("budget", "预算金额", "text", true));
        fields.add(createFormField("reason", "采购理由", "textarea", true));

        form.setFields(fields);
        return form;
    }
    
    /**
     * 创建默认表单
     */
    private WorkflowFormDTO.FormDefinitionDTO createDefaultForm(String formKey) {
        WorkflowFormDTO.FormDefinitionDTO form = new WorkflowFormDTO.FormDefinitionDTO();
        form.setFormKey(formKey);
        form.setName("默认表单");
        form.setDescription("默认表单定义");
        
        List<WorkflowFormDTO.FormFieldDTO> fields = new ArrayList<>();
        
        // 使用通用方法创建字段
        fields.add(createFormField("title", "标题", "text", true));
        fields.add(createFormField("content", "内容", "textarea", true));
        
        form.setFields(fields);
        return form;
    }
    
    /**
      * 创建表单字段的通用方法
      * @param fieldId 字段ID
      * @param fieldName 字段名称
      * @param fieldType 字段类型
      * @param required 是否必填
      * @return 表单字段DTO
      */
     private WorkflowFormDTO.FormFieldDTO createFormField(String fieldId, String fieldName, String fieldType, boolean required) {
         WorkflowFormDTO.FormFieldDTO field = new WorkflowFormDTO.FormFieldDTO();
         field.setId(fieldId);
         field.setName(fieldName);
         field.setType(fieldType);
         field.setRequired(required);
         return field;
     }
}