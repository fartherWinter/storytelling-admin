package com.chennian.storytelling.bean.model;

import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * 角色权限关联表
 * 
 * @author chennian
 * @date 2024-01-20
 */
@Data
@EqualsAndHashCode(callSuper = false)
@TableName("sys_role_permission")
public class SysRolePermission implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 角色ID
     */
    private Long roleId;

    /**
     * 权限ID
     */
    private Long permissionId;

    /**
     * 创建时间
     */
    private LocalDateTime createTime;

    /**
     * 构造函数
     */
    public SysRolePermission() {
    }

    /**
     * 构造函数
     * 
     * @param roleId 角色ID
     * @param permissionId 权限ID
     */
    public SysRolePermission(Long roleId, Long permissionId) {
        this.roleId = roleId;
        this.permissionId = permissionId;
        this.createTime = LocalDateTime.now();
    }
}