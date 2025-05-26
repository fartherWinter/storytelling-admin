package com.chennian.storytelling.service;

import com.chennian.storytelling.bean.model.SysUser;

import java.util.Set;

/**
 * @author by chennian
 * @date 2025/5/6.
 */
public interface SysPermissionService {
    /**
     * 获取角色数据权限
     */
    public Set<String> getRolePermission(SysUser user);
    /**
     * 获取菜单数据权限
     */
    public Set<String> getMenuPermission(SysUser user);
}
