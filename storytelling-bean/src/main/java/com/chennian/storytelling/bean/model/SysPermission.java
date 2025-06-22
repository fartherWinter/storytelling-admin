package com.chennian.storytelling.bean.model;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.List;

/**
 * 权限信息表
 * 统一管理系统权限和工作流权限
 * 
 * @author chennian
 * @date 2024-01-20
 */
@Data
@TableName("sys_permission")
public class SysPermission implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 权限ID
     */
    @TableId(value = "permission_id", type = IdType.AUTO)
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
     * 排序
     */
    private Integer sortOrder;

    /**
     * 状态(0正常 1停用)
     */
    private String status;

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
     * 子权限列表（用于构建权限树）
     */
    @TableField(exist = false)
    private List<SysPermission> children;

    /**
     * 父权限名称
     */
    @TableField(exist = false)
    private String parentName;

    /**
     * 是否选中（用于角色权限分配）
     */
    @TableField(exist = false)
    private Boolean checked = false;

    // =====================================================
    // 权限类型常量
    // =====================================================

    /**
     * 菜单权限
     */
    public static final String TYPE_MENU = "MENU";

    /**
     * 按钮权限
     */
    public static final String TYPE_BUTTON = "BUTTON";

    /**
     * API权限
     */
    public static final String TYPE_API = "API";

    /**
     * 工作流权限
     */
    public static final String TYPE_WORKFLOW = "WORKFLOW";

    // =====================================================
    // 状态常量
    // =====================================================

    /**
     * 正常状态
     */
    public static final String STATUS_NORMAL = "0";

    /**
     * 停用状态
     */
    public static final String STATUS_DISABLE = "1";

    // =====================================================
    // 便捷方法
    // =====================================================

    /**
     * 是否为菜单权限
     */
    public boolean isMenuPermission() {
        return TYPE_MENU.equals(this.permissionType);
    }

    /**
     * 是否为按钮权限
     */
    public boolean isButtonPermission() {
        return TYPE_BUTTON.equals(this.permissionType);
    }

    /**
     * 是否为API权限
     */
    public boolean isApiPermission() {
        return TYPE_API.equals(this.permissionType);
    }

    /**
     * 是否为工作流权限
     */
    public boolean isWorkflowPermission() {
        return TYPE_WORKFLOW.equals(this.permissionType);
    }

    /**
     * 是否正常状态
     */
    public boolean isNormal() {
        return STATUS_NORMAL.equals(this.status);
    }

    /**
     * 是否停用状态
     */
    public boolean isDisable() {
        return STATUS_DISABLE.equals(this.status);
    }

    /**
     * 是否为根权限
     */
    public boolean isRootPermission() {
        return this.parentId == null || this.parentId == 0L;
    }

    /**
     * 获取权限层级（用于显示缩进）
     */
    @TableField(exist = false)
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
}