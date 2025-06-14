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
}