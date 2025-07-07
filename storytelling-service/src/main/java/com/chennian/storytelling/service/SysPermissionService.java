package com.chennian.storytelling.service;

import com.chennian.storytelling.bean.model.SysPermission;
import com.chennian.storytelling.bean.model.SysUser;

import java.util.List;
import java.util.Set;

/**
 * 统一权限服务接口
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
    
    /**
     * 获取工作流权限
     * @param user 用户信息
     * @return 工作流权限集合
     */
    public Set<String> getWorkflowPermission(SysUser user);
    
    /**
     * 检查用户是否具有指定工作流权限
     * @param userId 用户ID
     * @param permissionCode 权限代码
     * @return 是否具有权限
     */
    public boolean hasWorkflowPermission(Long userId, String permissionCode);
    
    /**
     * 根据权限类型获取权限列表
     * @param permissionType 权限类型（MENU、BUTTON、API、WORKFLOW）
     * @return 权限列表
     */
    public List<SysPermission> getPermissionsByType(String permissionType);
    
    /**
     * 获取用户所有权限（包括菜单、工作流等）
     * @param user 用户信息
     * @return 所有权限集合
     */
    public Set<String> getAllPermissions(SysUser user);
}
