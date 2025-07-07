package com.chennian.storytelling.bean.dto;

import lombok.Data;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.List;

/**
 * 角色DTO
 * 
 * @author chennian
 * @date 2024-01-20
 */
@Data
public class RoleDTO implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 角色ID
     */
    private Long roleId;

    /**
     * 角色名称
     */
    private String roleName;

    /**
     * 角色权限字符串
     */
    private String roleKey;

    /**
     * 显示顺序
     */
    private Integer roleSort;

    /**
     * 数据范围（1：全部数据权限 2：自定数据权限 3：本部门数据权限 4：本部门及以下数据权限）
     */
    private String dataScope;

    /**
     * 菜单树选择项是否关联显示
     */
    private Boolean menuCheckStrictly;

    /**
     * 部门树选择项是否关联显示
     */
    private Boolean deptCheckStrictly;

    /**
     * 角色状态（0正常 1停用）
     */
    private String status;

    /**
     * 状态描述
     */
    private String statusDesc;

    /**
     * 删除标志（0代表存在 2代表删除）
     */
    private String delFlag;

    /**
     * 创建者
     */
    private String createBy;

    /**
     * 创建时间
     */
    private LocalDateTime createTime;

    /**
     * 更新者
     */
    private String updateBy;

    /**
     * 更新时间
     */
    private LocalDateTime updateTime;

    /**
     * 备注
     */
    private String remark;

    /**
     * 角色类型(SYSTEM,WORKFLOW,CUSTOM)
     */
    private String roleType;

    /**
     * 是否启用工作流权限
     */
    private Boolean workflowEnabled;

    /**
     * 排序
     */
    private Integer sortOrder;

    /**
     * 权限ID列表（用于角色权限分配）
     */
    private List<Long> permissionIds;

    /**
     * 部门ID列表（用于数据权限）
     */
    private List<Long> deptIds;

    /**
     * 是否选中（用于用户角色分配）
     */
    private Boolean checked = false;

    /**
     * 用户数量
     */
    private Integer userCount;

    /**
     * 权限数量
     */
    private Integer permissionCount;

    // =====================================================
    // 查询条件字段
    // =====================================================

    /**
     * 角色名称（模糊查询）
     */
    private String roleNameLike;

    /**
     * 角色权限字符（模糊查询）
     */
    private String roleKeyLike;

    /**
     * 角色类型列表（多选查询）
     */
    private List<String> roleTypes;

    /**
     * 状态列表（多选查询）
     */
    private List<String> statusList;

    /**
     * 创建时间开始
     */
    private LocalDateTime createTimeStart;

    /**
     * 创建时间结束
     */
    private LocalDateTime createTimeEnd;

    /**
     * 是否启用工作流权限（查询条件）
     */
    private Boolean workflowEnabledQuery;

    /**
     * 用户ID（查询用户角色时使用）
     */
    private Long userId;

    /**
     * 部门ID（查询部门角色时使用）
     */
    private Long deptId;

    // =====================================================
    // 分页参数
    // =====================================================

    /**
     * 页码
     */
    private Integer pageNum = 1;

    /**
     * 页大小
     */
    private Integer pageSize = 10;

    /**
     * 排序字段
     */
    private String orderBy;

    /**
     * 排序方向
     */
    private String orderDirection = "ASC";

    // =====================================================
    // 便捷方法
    // =====================================================

    /**
     * 获取状态描述
     */
    public String getStatusDesc() {
        if ("0".equals(status)) {
            return "正常";
        } else if ("1".equals(status)) {
            return "停用";
        }
        return "未知";
    }

    /**
     * 是否正常状态
     */
    public boolean isNormal() {
        return "0".equals(this.status);
    }

    /**
     * 是否停用状态
     */
    public boolean isDisable() {
        return "1".equals(this.status);
    }

    /**
     * 是否为系统角色
     */
    public boolean isSystemRole() {
        return "SYSTEM".equals(this.roleType);
    }

    /**
     * 是否为工作流角色
     */
    public boolean isWorkflowRole() {
        return "WORKFLOW".equals(this.roleType);
    }

    /**
     * 是否为自定义角色
     */
    public boolean isCustomRole() {
        return "CUSTOM".equals(this.roleType);
    }

    /**
     * 是否启用工作流
     */
    public boolean isWorkflowEnabled() {
        return Boolean.TRUE.equals(this.workflowEnabled);
    }

    /**
     * 获取角色类型描述
     */
    public String getRoleTypeDesc() {
        if ("SYSTEM".equals(roleType)) {
            return "系统角色";
        } else if ("WORKFLOW".equals(roleType)) {
            return "工作流角色";
        } else if ("CUSTOM".equals(roleType)) {
            return "自定义角色";
        }
        return "未知类型";
    }

    /**
     * 获取数据范围描述
     */
    public String getDataScopeDesc() {
        switch (dataScope) {
            case "1":
                return "全部数据权限";
            case "2":
                return "自定数据权限";
            case "3":
                return "本部门数据权限";
            case "4":
                return "本部门及以下数据权限";
            case "5":
                return "仅本人数据权限";
            default:
                return "未知权限";
        }
    }
}