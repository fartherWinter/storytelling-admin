package com.chennian.storytelling.bean.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;
import jakarta.validation.constraints.NotNull;
import java.util.List;
import java.util.Map;

/**
 * 工作流表单DTO
 * 
 * @author chennian
 */
@Data
@ApiModel("工作流表单DTO")
public class WorkflowFormDTO {

    /**
     * 表单定义DTO
     */
    @Data
    @ApiModel("表单定义DTO")
    public static class FormDefinitionDTO {
        @ApiModelProperty("表单ID")
        private String id;
        
        @ApiModelProperty("表单名称")
        @NotBlank
        private String name;
        
        @ApiModelProperty("表单描述")
        private String description;
        
        @ApiModelProperty("关联流程键")
        @NotBlank
        private String processKey;
        
        @ApiModelProperty("版本号")
        private Integer version;
        
        @ApiModelProperty("状态")
        private String status; // active, inactive, draft
        
        @ApiModelProperty("表单字段")
        @NotNull
        private List<FormFieldDTO> fields;
        
        @ApiModelProperty("表单布局")
        private FormLayoutDTO layout;
        
        @ApiModelProperty("表单规则")
        private List<FormRuleDTO> rules;
        
        @ApiModelProperty("表单样式")
        private Map<String, Object> styles;

        @ApiModelProperty("表单键")
        private String formKey;
    }
    
    /**
     * 表单字段DTO
     */
    @Data
    @ApiModel("表单字段DTO")
    public static class FormFieldDTO {
        @ApiModelProperty("字段ID")
        @NotBlank
        private String id;
        
        @ApiModelProperty("字段名称")
        @NotBlank
        private String name;
        
        @ApiModelProperty("字段类型")
        @NotBlank
        private String type; // text, textarea, select, radio, checkbox, date, file, etc.
        
        @ApiModelProperty("是否必填")
        private Boolean required = false;
        
        @ApiModelProperty("默认值")
        private Object defaultValue;
        
        @ApiModelProperty("占位符")
        private String placeholder;
        
        @ApiModelProperty("字段选项")
        private List<FieldOptionDTO> options;
        
        @ApiModelProperty("验证规则")
        private Map<String, Object> validation;
        
        @ApiModelProperty("字段属性")
        private Map<String, Object> attributes;
        
        @ApiModelProperty("字段样式")
        private Map<String, Object> styles;
        
        @ApiModelProperty("是否只读")
        private Boolean readonly = false;
        
        @ApiModelProperty("是否隐藏")
        private Boolean hidden = false;
    }
    
    /**
     * 字段选项DTO
     */
    @Data
    @ApiModel("字段选项DTO")
    public static class FieldOptionDTO {
        @ApiModelProperty("选项值")
        @NotBlank
        private String value;
        
        @ApiModelProperty("选项标签")
        @NotBlank
        private String label;
        
        @ApiModelProperty("是否禁用")
        private Boolean disabled = false;
        
        @ApiModelProperty("选项描述")
        private String description;
    }
    
    /**
     * 表单布局DTO
     */
    @Data
    @ApiModel("表单布局DTO")
    public static class FormLayoutDTO {
        @ApiModelProperty("列数")
        private Integer columns = 1;
        
        @ApiModelProperty("标签位置")
        private String labelPosition = "top"; // top, left, right
        
        @ApiModelProperty("标签宽度")
        private String labelWidth;
        
        @ApiModelProperty("字段间距")
        private String spacing;
        
        @ApiModelProperty("分组配置")
        private List<FormGroupDTO> groups;
    }
    
    /**
     * 表单分组DTO
     */
    @Data
    @ApiModel("表单分组DTO")
    public static class FormGroupDTO {
        @ApiModelProperty("分组ID")
        @NotBlank
        private String id;
        
        @ApiModelProperty("分组标题")
        @NotBlank
        private String title;
        
        @ApiModelProperty("字段列表")
        @NotNull
        private List<String> fields;
        
        @ApiModelProperty("是否可折叠")
        private Boolean collapsible = false;
        
        @ApiModelProperty("默认是否展开")
        private Boolean expanded = true;
    }
    
    /**
     * 表单规则DTO
     */
    @Data
    @ApiModel("表单规则DTO")
    public static class FormRuleDTO {
        @ApiModelProperty("规则ID")
        @NotBlank
        private String id;
        
        @ApiModelProperty("规则类型")
        @NotBlank
        private String type; // visibility, required, validation, calculation
        
        @ApiModelProperty("触发条件")
        @NotBlank
        private String condition;
        
        @ApiModelProperty("目标字段")
        @NotNull
        private List<String> targetFields;
        
        @ApiModelProperty("执行动作")
        @NotBlank
        private String action;
        
        @ApiModelProperty("动作参数")
        private Map<String, Object> parameters;
    }
    
    /**
     * 表单数据DTO
     */
    @Data
    @ApiModel("表单数据DTO")
    public static class FormDataDTO {
        @ApiModelProperty("表单ID")
        @NotBlank
        private String formId;
        
        @ApiModelProperty("任务ID")
        private String taskId;
        
        @ApiModelProperty("流程实例ID")
        private String processInstanceId;
        
        @ApiModelProperty("表单数据")
        @NotNull
        private Map<String, Object> data;
        
        @ApiModelProperty("提交类型")
        private String submitType = "save"; // save, submit, draft
        
        @ApiModelProperty("备注")
        private String comment;
    }
    
    /**
     * 表单模板DTO
     */
    @Data
    @ApiModel("表单模板DTO")
    public static class FormTemplateDTO {
        @ApiModelProperty("模板ID")
        private String id;
        
        @ApiModelProperty("模板名称")
        @NotBlank
        private String name;
        
        @ApiModelProperty("模板描述")
        private String description;
        
        @ApiModelProperty("模板分类")
        private String category;
        
        @ApiModelProperty("模板内容")
        @NotNull
        private FormDefinitionDTO template;
        
        @ApiModelProperty("是否公开")
        private Boolean isPublic = false;
        
        @ApiModelProperty("创建者")
        private String creator;
        
        @ApiModelProperty("使用次数")
        private Integer usageCount = 0;
    }
}