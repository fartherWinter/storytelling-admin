package com.chennian.storytelling.bean.dto;

import lombok.Data;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.List;

/**
 * 权限DTO
 * 
 * @author chennian
 * @date 2024-01-20
 */
@Data
public class PermissionDTO implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 权限ID
     */
    private Long permissionId;

    /**
     * 权限名称
     */
    private String permissionName;

    /**
     * 权限编码
     */
    private String permissionCode;

    /**
     * 权限类型(MENU,BUTTON,API,WORKFLOW)
     */
    private String permissionType;

    /**
     * 资源URL
     */
    private String resourceUrl;

    /**
     * 父权限ID
     */
    private Long parentId;

    /**
     * 父权限名称
     */
    private String parentName;

    /**
     * 排序
     */
    private Integer sortOrder;

    /**
     * 状态(0正常 1停用)
     */
    private String status;

    /**
     * 状态描述
     */
    private String statusDesc;

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
     * 子权限列表
     */
    private List<PermissionDTO> children;

    /**
     * 是否选中（用于角色权限分配）
     */
    private Boolean checked = false;

    /**
     * 权限层级
     */
    private Integer level;

    /**
     * 权限图标
     */
    private String icon;

    /**
     * 是否外链
     */
    private Boolean isFrame;

    /**
     * 是否缓存
     */
    private Boolean isCache;

    /**
     * 显示状态
     */
    private Boolean visible;

    /**
     * 组件路径
     */
    private String component;

    /**
     * 路由参数
     */
    private String query;

    // =====================================================
    // 查询条件字段
    // =====================================================

    /**
     * 权限名称（模糊查询）
     */
    private String permissionNameLike;

    /**
     * 权限编码（模糊查询）
     */
    private String permissionCodeLike;

    /**
     * 权限类型列表（多选查询）
     */
    private List<String> permissionTypes;

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
     * 是否包含子权限
     */
    private Boolean includeChildren;

    /**
     * 角色ID（查询角色权限时使用）
     */
    private Long roleId;

    /**
     * 用户ID（查询用户权限时使用）
     */
    private Long userId;

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
     * 是否为菜单权限
     */
    public boolean isMenuPermission() {
        return "MENU".equals(this.permissionType);
    }

    /**
     * 是否为按钮权限
     */
    public boolean isButtonPermission() {
        return "BUTTON".equals(this.permissionType);
    }

    /**
     * 是否为API权限
     */
    public boolean isApiPermission() {
        return "API".equals(this.permissionType);
    }

    /**
     * 是否为工作流权限
     */
    public boolean isWorkflowPermission() {
        return "WORKFLOW".equals(this.permissionType);
    }

    /**
     * 是否正常状态
     */
    public boolean isNormal() {
        return "0".equals(this.status);
    }

    /**
     * 是否为根权限
     */
    public boolean isRootPermission() {
        return this.parentId == null || this.parentId == 0L;
    }
}