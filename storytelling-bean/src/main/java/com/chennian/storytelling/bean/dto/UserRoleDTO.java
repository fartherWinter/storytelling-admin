package com.chennian.storytelling.bean.dto;

import lombok.Data;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * 用户角色关联DTO
 * 
 * @author chennian
 * @date 2024-01-20
 */
@Data
public class UserRoleDTO implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 用户ID
     */
    private Long userId;

    /**
     * 角色ID
     */
    private Long roleId;

    /**
     * 角色名称
     */
    private String roleName;

    /**
     * 角色权限字符
     */
    private String roleKey;

    /**
     * 部门ID
     */
    private Long departmentId;

    /**
     * 部门名称
     */
    private String departmentName;

    /**
     * 生效日期
     */
    private LocalDateTime effectiveDate;

    /**
     * 失效日期
     */
    private LocalDateTime expiryDate;

    /**
     * 是否启用
     */
    private Boolean enabled;

    /**
     * 角色类型(SYSTEM,WORKFLOW,CUSTOM)
     */
    private String roleType;

    /**
     * 是否启用工作流权限
     */
    private Boolean workflowEnabled;

    /**
     * 创建时间
     */
    private LocalDateTime createTime;

    /**
     * 更新时间
     */
    private LocalDateTime updateTime;

    /**
     * 备注
     */
    private String remark;

    /**
     * 是否选中（用于前端显示）
     */
    private Boolean checked = false;

    /**
     * 是否为临时角色
     */
    private Boolean temporary = false;

    /**
     * 角色状态描述
     */
    private String statusDesc;

    /**
     * 获取角色状态描述
     */
    public String getStatusDesc() {
        if (!Boolean.TRUE.equals(enabled)) {
            return "已禁用";
        }
        
        LocalDateTime now = LocalDateTime.now();
        
        if (effectiveDate != null && now.isBefore(effectiveDate)) {
            return "未生效";
        }
        
        if (expiryDate != null && now.isAfter(expiryDate)) {
            return "已过期";
        }
        
        return "正常";
    }

    /**
     * 判断角色是否有效
     */
    public boolean isValid() {
        if (!Boolean.TRUE.equals(enabled)) {
            return false;
        }
        
        LocalDateTime now = LocalDateTime.now();
        
        if (effectiveDate != null && now.isBefore(effectiveDate)) {
            return false;
        }
        
        if (expiryDate != null && now.isAfter(expiryDate)) {
            return false;
        }
        
        return true;
    }

    /**
     * 判断是否为系统角色
     */
    public boolean isSystemRole() {
        return "SYSTEM".equals(roleType);
    }

    /**
     * 判断是否为工作流角色
     */
    public boolean isWorkflowRole() {
        return "WORKFLOW".equals(roleType);
    }

    /**
     * 判断是否为自定义角色
     */
    public boolean isCustomRole() {
        return "CUSTOM".equals(roleType);
    }
}