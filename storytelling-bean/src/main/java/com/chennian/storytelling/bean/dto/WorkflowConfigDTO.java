package com.chennian.storytelling.bean.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;
import jakarta.validation.constraints.NotNull;
import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * 工作流配置DTO
 * 
 * @author chennian
 */
@Data
@ApiModel("工作流配置DTO")
public class WorkflowConfigDTO {

    /**
     * 权限配置DTO
     */
    @Data
    @ApiModel("权限配置DTO")
    public static class PermissionDTO {
        @ApiModelProperty("权限ID")
        private String id;
        
        @ApiModelProperty("权限名称")
        @NotBlank
        private String name;
        
        @ApiModelProperty("权限编码")
        private String code;
        
        @ApiModelProperty("权限描述")
        private String description;
        
        @ApiModelProperty("资源类型(MENU,BUTTON,API)")
        private String resourceType;
        
        @ApiModelProperty("资源URL")
        private String resourceUrl;
        
        @ApiModelProperty("父权限ID")
        private String parentId;
        
        @ApiModelProperty("排序")
        private Integer sortOrder;
        
        @ApiModelProperty("是否启用")
        private Boolean enabled = true;
        
        @ApiModelProperty("创建时间")
        private Date createdTime;
        
        @ApiModelProperty("更新时间")
        private Date updatedTime;
        
        @ApiModelProperty("创建人")
        private String createdBy;
        
        @ApiModelProperty("更新人")
        private String updatedBy;
        
        @ApiModelProperty("权限分类")
        private String category;
        
        @ApiModelProperty("操作列表")
        private List<ActionDTO> actions;
    }
    
    /**
     * 操作DTO
     */
    @Data
    @ApiModel("操作DTO")
    public static class ActionDTO {
        @ApiModelProperty("操作ID")
        @NotBlank
        private String id;
        
        @ApiModelProperty("操作名称")
        @NotBlank
        private String name;
        
        @ApiModelProperty("操作描述")
        private String description;
    }
    
    /**
     * 角色配置DTO
     */
    @Data
    @ApiModel("角色配置DTO")
    public static class RoleDTO {
        @ApiModelProperty("角色ID")
        private String id;
        
        @ApiModelProperty("角色名称")
        @NotBlank
        private String name;
        
        @ApiModelProperty("角色编码")
        private String code;
        
        @ApiModelProperty("角色描述")
        private String description;
        
        @ApiModelProperty("角色类型(SYSTEM,CUSTOM)")
        private String roleType;
        
        @ApiModelProperty("权限列表")
        @NotNull
        private List<String> permissions;
        
        @ApiModelProperty("用户数量")
        private Integer userCount;
        
        @ApiModelProperty("是否启用")
        private Boolean enabled = true;
        
        @ApiModelProperty("排序")
        private Integer sortOrder;
        
        @ApiModelProperty("创建时间")
        private Date createdTime;
        
        @ApiModelProperty("更新时间")
        private Date updatedTime;
        
        @ApiModelProperty("创建人")
        private String createdBy;
        
        @ApiModelProperty("更新人")
        private String updatedBy;
    }
    
    /**
     * 用户角色分配DTO
     */
    @Data
    @ApiModel("用户角色分配DTO")
    public static class UserRoleDTO {
        @ApiModelProperty("用户ID")
        @NotBlank
        private String userId;
        
        @ApiModelProperty("用户名")
        private String userName;
        
        @ApiModelProperty("角色列表")
        @NotNull
        private List<String> roles;
        
        @ApiModelProperty("部门ID")
        private String departmentId;
        
        @ApiModelProperty("部门名称")
        private String departmentName;
    }
    
    /**
     * 流程分类配置DTO
     */
    @Data
    @ApiModel("流程分类配置DTO")
    public static class ProcessCategoryDTO {
        @ApiModelProperty("分类ID")
        private String id;
        
        @ApiModelProperty("分类名称")
        @NotBlank
        private String name;
        
        @ApiModelProperty("分类编码")
        private String code;
        
        @ApiModelProperty("分类描述")
        private String description;
        
        @ApiModelProperty("父分类ID")
        private String parentId;
        
        @ApiModelProperty("排序")
        private Integer sortOrder;
        
        @ApiModelProperty("是否启用")
        private Boolean enabled = true;
        
        @ApiModelProperty("图标")
        private String icon;
        
        @ApiModelProperty("颜色")
        private String color;
        
        @ApiModelProperty("创建时间")
        private Date createdTime;
        
        @ApiModelProperty("更新时间")
        private Date updatedTime;
        
        @ApiModelProperty("创建人")
        private String createdBy;
        
        @ApiModelProperty("更新人")
        private String updatedBy;
    }
    
    /**
     * 通知模板配置DTO
     */
    @Data
    @ApiModel("通知模板配置DTO")
    public static class NotificationTemplateDTO {
        @ApiModelProperty("模板ID")
        private String id;
        
        @ApiModelProperty("模板名称")
        @NotBlank
        private String name;
        
        @ApiModelProperty("模板编码")
        private String code;
        
        @ApiModelProperty("通知标题")
        private String title;
        
        @ApiModelProperty("模板内容")
        private String content;
        
        @ApiModelProperty("模板类型")
        @NotBlank
        private String type; // email, sms, system
        
        @ApiModelProperty("事件类型")
        @NotBlank
        private String eventType;
        
        @ApiModelProperty("可用变量(JSON格式)")
        private String variables;
        
        @ApiModelProperty("是否启用")
        private Boolean enabled = true;
        
        @ApiModelProperty("排序")
        private Integer sortOrder;
        
        @ApiModelProperty("创建时间")
        private Date createdTime;
        
        @ApiModelProperty("更新时间")
        private Date updatedTime;
        
        @ApiModelProperty("创建人")
        private String createdBy;
        
        @ApiModelProperty("更新人")
        private String updatedBy;
    }
    
    /**
     * 系统配置DTO
     */
    @Data
    @ApiModel("系统配置DTO")
    public static class SystemConfigDTO {
        @ApiModelProperty("配置ID")
        private String id;
        
        @ApiModelProperty("配置键")
        @NotBlank
        private String configKey;
        
        @ApiModelProperty("配置值")
        @NotBlank
        private String configValue;
        
        @ApiModelProperty("配置类型(STRING,NUMBER,BOOLEAN,JSON)")
        private String configType;
        
        @ApiModelProperty("配置描述")
        private String description;
        
        @ApiModelProperty("是否可编辑")
        private Boolean editable = true;
        
        @ApiModelProperty("配置分组")
        private String configGroup;
        
        @ApiModelProperty("排序")
        private Integer sortOrder;
        
        @ApiModelProperty("创建时间")
        private Date createdTime;
        
        @ApiModelProperty("更新时间")
        private Date updatedTime;
        
        @ApiModelProperty("创建人")
        private String createdBy;
        
        @ApiModelProperty("更新人")
        private String updatedBy;
    }
    
    /**
     * 报表配置DTO
     */
    @Data
    @ApiModel("报表配置DTO")
    public static class ReportConfigDTO {
        @ApiModelProperty("报表ID")
        private String id;
        
        @ApiModelProperty("报表名称")
        @NotBlank
        private String name;
        
        @ApiModelProperty("报表编码")
        private String code;
        
        @ApiModelProperty("报表描述")
        private String description;
        
        @ApiModelProperty("报表类型")
        @NotBlank
        private String type;
        
        @ApiModelProperty("数据源配置")
        private String dataSource;
        
        @ApiModelProperty("查询SQL")
        private String querySql;
        
        @ApiModelProperty("报表配置(JSON格式)")
        private String config;
        
        @ApiModelProperty("是否启用")
        private Boolean enabled = true;
        
        @ApiModelProperty("排序")
        private Integer sortOrder;
        
        @ApiModelProperty("创建时间")
        private Date createdTime;
        
        @ApiModelProperty("更新时间")
        private Date updatedTime;
        
        @ApiModelProperty("创建人")
        private String createdBy;
        
        @ApiModelProperty("更新人")
        private String updatedBy;
        
        @ApiModelProperty("最后执行时间")
        private Date lastExecuteTime;
        
        @ApiModelProperty("执行次数")
        private Integer executeCount;
    }
    
    /**
     * 权限树DTO
     */
    @Data
    @ApiModel("权限树DTO")
    public static class PermissionTreeDTO {
        @ApiModelProperty("权限ID")
        private String id;
        
        @ApiModelProperty("权限名称")
        private String name;
        
        @ApiModelProperty("权限编码")
        private String code;
        
        @ApiModelProperty("父权限ID")
        private String parentId;
        
        @ApiModelProperty("子权限列表")
        private List<PermissionTreeDTO> children;
        
        @ApiModelProperty("是否启用")
        private Boolean enabled;
        
        @ApiModelProperty("资源类型")
        private String resourceType;
        
        @ApiModelProperty("排序")
        private Integer sortOrder;
    }
    
    /**
     * 角色权限关联DTO
     */
    @Data
    @ApiModel("角色权限关联DTO")
    public static class RolePermissionDTO {
        @ApiModelProperty("角色ID")
        @NotBlank
        private String roleId;
        
        @ApiModelProperty("权限ID列表")
        @NotNull
        private List<String> permissionIds;
        
        @ApiModelProperty("操作类型(ADD,REMOVE,UPDATE)")
        private String operation;
        
        @ApiModelProperty("备注")
        private String remark;
    }
    
    /**
     * 批量用户角色DTO
     */
    @Data
    @ApiModel("批量用户角色DTO")
    public static class BatchUserRoleDTO {
        @ApiModelProperty("用户角色关联列表")
        @NotNull
        private List<UserRoleDTO> userRoles;
        
        @ApiModelProperty("操作类型(ADD,REMOVE,UPDATE)")
        private String operation;
        
        @ApiModelProperty("备注")
        private String remark;
    }
    
    /**
     * 批量保存用户角色DTO
     */
    @Data
    @ApiModel("批量保存用户角色DTO")
    public static class BatchSaveUserRoleDTO {
        @ApiModelProperty("用户角色关联列表")
        @NotNull
        private List<UserRoleDTO> userRoles;
        
        @ApiModelProperty("是否覆盖已有关联")
        private Boolean override = false;
        
        @ApiModelProperty("备注")
        private String remark;
    }
    
    /**
     * 批量更新DTO
     */
    @Data
    @ApiModel("批量更新DTO")
    public static class BatchUpdateDTO {
        @ApiModelProperty("ID列表")
        @NotNull
        private List<String> ids;
        
        @ApiModelProperty("状态(0-禁用,1-启用)")
        private Integer status;
        
        @ApiModelProperty("操作类型")
        private String operation;
        
        @ApiModelProperty("更新字段")
        private Map<String, Object> updateFields;
        
        @ApiModelProperty("备注")
        private String remark;
    }
    
    /**
     * 批量删除DTO
     */
    @Data
    @ApiModel("批量删除DTO")
    public static class BatchDeleteDTO {
        @ApiModelProperty("ID列表")
        @NotNull
        private List<String> ids;
        
        @ApiModelProperty("删除类型(SOFT,HARD)")
        private String deleteType = "SOFT";
        
        @ApiModelProperty("备注")
        private String remark;
    }
    
    /**
     * 流程分类DTO
     */
    @Data
    @ApiModel("流程分类DTO")
    public static class CategoryDTO {
        @ApiModelProperty("分类ID")
        private String id;
        
        @ApiModelProperty("分类名称")
        @NotBlank
        private String name;
        
        @ApiModelProperty("分类编码")
        private String code;
        
        @ApiModelProperty("分类描述")
        private String description;
        
        @ApiModelProperty("父分类ID")
        private String parentId;
        
        @ApiModelProperty("排序")
        private Integer sortOrder;
        
        @ApiModelProperty("是否启用")
        private Boolean enabled = true;
        
        @ApiModelProperty("图标")
        private String icon;
        
        @ApiModelProperty("颜色")
        private String color;
        
        @ApiModelProperty("创建时间")
        private Date createdTime;
        
        @ApiModelProperty("更新时间")
        private Date updatedTime;
        
        @ApiModelProperty("创建人")
        private String createdBy;
        
        @ApiModelProperty("更新人")
        private String updatedBy;
    }
    
    /**
     * 流程分类树DTO
     */
    @Data
    @ApiModel("流程分类树DTO")
    public static class CategoryTreeDTO {
        @ApiModelProperty("分类ID")
        private String id;
        
        @ApiModelProperty("分类名称")
        private String name;
        
        @ApiModelProperty("分类编码")
        private String code;
        
        @ApiModelProperty("父分类ID")
        private String parentId;
        
        @ApiModelProperty("子分类列表")
        private List<CategoryTreeDTO> children;
        
        @ApiModelProperty("是否启用")
        private Boolean enabled;
        
        @ApiModelProperty("图标")
        private String icon;
        
        @ApiModelProperty("颜色")
        private String color;
        
        @ApiModelProperty("排序")
        private Integer sortOrder;
    }
    
    /**
     * 通知模板DTO
     */
    @Data
    @ApiModel("通知模板DTO")
    public static class NotificationDTO {
        @ApiModelProperty("模板ID")
        private String id;
        
        @ApiModelProperty("模板名称")
        @NotBlank
        private String name;
        
        @ApiModelProperty("模板编码")
        private String code;
        
        @ApiModelProperty("通知标题")
        private String title;
        
        @ApiModelProperty("模板内容")
        private String content;
        
        @ApiModelProperty("模板类型")
        @NotBlank
        private String type; // email, sms, system
        
        @ApiModelProperty("事件类型")
        @NotBlank
        private String eventType;
        
        @ApiModelProperty("可用变量(JSON格式)")
        private String variables;
        
        @ApiModelProperty("是否启用")
        private Boolean enabled = true;
        
        @ApiModelProperty("排序")
        private Integer sortOrder;
        
        @ApiModelProperty("创建时间")
        private Date createdTime;
        
        @ApiModelProperty("更新时间")
        private Date updatedTime;
        
        @ApiModelProperty("创建人")
        private String createdBy;
        
        @ApiModelProperty("更新人")
        private String updatedBy;
    }
    
    /**
     * 模板类型DTO
     */
    @Data
    @ApiModel("模板类型DTO")
    public static class TemplateTypeDTO {
        @ApiModelProperty("类型编码")
        private String code;
        
        @ApiModelProperty("类型名称")
        private String name;
        
        @ApiModelProperty("类型描述")
        private String description;
        
        @ApiModelProperty("是否启用")
        private Boolean enabled;
    }
    
    /**
     * 事件类型DTO
     */
    @Data
    @ApiModel("事件类型DTO")
    public static class EventTypeDTO {
        @ApiModelProperty("事件编码")
        private String code;
        
        @ApiModelProperty("事件名称")
        private String name;
        
        @ApiModelProperty("事件描述")
        private String description;
        
        @ApiModelProperty("可用变量")
        private List<String> variables;
        
        @ApiModelProperty("是否启用")
        private Boolean enabled;
    }
    
    /**
     * 通知模板预览DTO
     */
    @Data
    @ApiModel("通知模板预览DTO")
    public static class NotificationPreviewDTO {
        @ApiModelProperty("预览数据(JSON格式)")
        private Map<String, Object> data;
        
        @ApiModelProperty("预览类型(TITLE,CONTENT,ALL)")
        private String previewType = "ALL";
    }
    
    /**
     * 通知模板预览结果DTO
     */
    @Data
    @ApiModel("通知模板预览结果DTO")
    public static class NotificationPreviewResultDTO {
        @ApiModelProperty("预览标题")
        private String title;
        
        @ApiModelProperty("预览内容")
        private String content;
        
        @ApiModelProperty("预览时间")
        private Date previewTime;
        
        @ApiModelProperty("使用的变量")
        private Map<String, Object> usedVariables;
    }
    
    /**
     * 报表类型DTO
     */
    @Data
    @ApiModel("报表类型DTO")
    public static class ReportTypeDTO {
        @ApiModelProperty("类型编码")
        private String code;
        
        @ApiModelProperty("类型名称")
        private String name;
        
        @ApiModelProperty("类型描述")
        private String description;
        
        @ApiModelProperty("支持的图表类型")
        private List<String> chartTypes;
        
        @ApiModelProperty("是否启用")
        private Boolean enabled;
    }
    
    /**
     * 批量分配任务DTO
     */
    @Data
    @ApiModel("批量分配任务DTO")
    public static class BatchAssignTaskDTO {
        @ApiModelProperty("任务ID列表")
        @NotNull
        private List<String> taskIds;
        
        @ApiModelProperty("分配给用户ID")
        @NotBlank
        private String assigneeId;
        
        @ApiModelProperty("分配给用户名")
        private String assigneeName;
        
        @ApiModelProperty("分配原因")
        private String reason;
        
        @ApiModelProperty("是否发送通知")
        private Boolean sendNotification = true;
    }
    
    /**
     * 工作流实例DTO
     */
    @Data
    @ApiModel("工作流实例DTO")
    public static class WorkflowInstanceDTO {
        @ApiModelProperty("实例ID")
        private String id;
        
        @ApiModelProperty("流程实例ID")
        private String processInstanceId;
        
        @ApiModelProperty("流程定义键")
        private String processDefinitionKey;
        
        @ApiModelProperty("流程定义名称")
        private String processDefinitionName;
        
        @ApiModelProperty("业务键")
        private String businessKey;
        
        @ApiModelProperty("实例状态")
        private String instanceStatus;
        
        @ApiModelProperty("开始用户ID")
        private String startUserId;
        
        @ApiModelProperty("开始用户名")
        private String startUserName;
        
        @ApiModelProperty("开始部门ID")
        private String startDeptId;
        
        @ApiModelProperty("开始部门名称")
        private String startDeptName;
        
        @ApiModelProperty("开始时间")
        private Date startTime;
        
        @ApiModelProperty("结束时间")
        private Date endTime;
        
        @ApiModelProperty("持续时间(毫秒)")
        private Long duration;
        
        @ApiModelProperty("租户ID")
        private String tenantId;
        
        @ApiModelProperty("备注")
        private String remark;
        
        @ApiModelProperty("创建时间")
        private Date createdTime;
        
        @ApiModelProperty("更新时间")
        private Date updatedTime;
    }
    
    /**
     * 工作流任务DTO
     */
    @Data
    @ApiModel("工作流任务DTO")
    public static class WorkflowTaskDTO {
        @ApiModelProperty("任务ID")
        private String id;
        
        @ApiModelProperty("任务键")
        private String taskKey;
        
        @ApiModelProperty("任务名称")
        private String taskName;
        
        @ApiModelProperty("流程实例ID")
        private String processInstanceId;
        
        @ApiModelProperty("流程定义键")
        private String processDefinitionKey;
        
        @ApiModelProperty("任务状态")
        private String taskStatus;
        
        @ApiModelProperty("分配人")
        private String assignee;
        
        @ApiModelProperty("分配人名称")
        private String assigneeName;
        
        @ApiModelProperty("候选用户")
        private String candidateUsers;
        
        @ApiModelProperty("候选组")
        private String candidateGroups;
        
        @ApiModelProperty("任务类型")
        private String taskType;
        
        @ApiModelProperty("优先级")
        private Integer priority;
        
        @ApiModelProperty("到期时间")
        private Date dueDate;
        
        @ApiModelProperty("业务键")
        private String businessKey;
        
        @ApiModelProperty("表单键")
        private String formKey;
        
        @ApiModelProperty("任务描述")
        private String description;
        
        @ApiModelProperty("创建时间")
        private Date createdTime;
        
        @ApiModelProperty("完成时间")
        private Date completedTime;
        
        @ApiModelProperty("处理时间(毫秒)")
        private Long processingTime;
        
        @ApiModelProperty("租户ID")
        private String tenantId;
        
        @ApiModelProperty("备注")
        private String remark;
    }
    
    /**
     * 流程定义DTO
     */
    @Data
    @ApiModel("流程定义DTO")
    public static class ProcessDefinitionDTO {
        @ApiModelProperty("定义ID")
        private String id;
        
        @ApiModelProperty("流程定义键")
        private String processDefinitionKey;
        
        @ApiModelProperty("流程定义名称")
        private String processDefinitionName;
        
        @ApiModelProperty("版本")
        private Integer version;
        
        @ApiModelProperty("分类ID")
        private String categoryId;
        
        @ApiModelProperty("分类名称")
        private String categoryName;
        
        @ApiModelProperty("分类编码")
        private String categoryCode;
        
        @ApiModelProperty("描述")
        private String description;
        
        @ApiModelProperty("是否启用")
        private Boolean enabled;
        
        @ApiModelProperty("部署ID")
        private String deploymentId;
        
        @ApiModelProperty("资源名称")
        private String resourceName;
        
        @ApiModelProperty("图表资源名称")
        private String diagramResourceName;
        
        @ApiModelProperty("是否暂停")
        private Boolean suspended;
        
        @ApiModelProperty("租户ID")
        private String tenantId;
        
        @ApiModelProperty("排序")
        private Integer sortOrder;
        
        @ApiModelProperty("创建时间")
        private Date createdTime;
        
        @ApiModelProperty("更新时间")
        private Date updatedTime;
        
        @ApiModelProperty("创建人")
        private String createdBy;
        
        @ApiModelProperty("更新人")
        private String updatedBy;
    }
}