package com.chennian.storytelling.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.chennian.storytelling.bean.model.SysPermission;
import com.chennian.storytelling.bean.model.SysRole;
import com.chennian.storytelling.bean.model.SysUser;
import com.chennian.storytelling.dao.SysPermissionMapper;
import com.chennian.storytelling.service.SysMenuService;
import com.chennian.storytelling.service.SysPermissionService;
import com.chennian.storytelling.service.SysRoleService;
import com.chennian.storytelling.service.SysUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;

import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * @author by chennian
 * @date 2025/5/20.
 */
@Service
public class SysPermissionServiceImpl implements SysPermissionService {
    @Autowired
    private SysRoleService roleService;
    @Autowired
    private SysMenuService menuService;
    @Autowired
    private SysPermissionMapper sysPermissionMapper;
    @Autowired
    private SysUserService sysUserService;




    /**
     * 获取角色数据权限
     *
     * @param user
     * @return
     */
    @Override
    public Set<String> getRolePermission(SysUser user) {
        Set<String> roles = new HashSet<String>();
        // 管理员拥有所有权限
        if (user.isAdmin()) {
            roles.add("admin");
        } else {
            roles.addAll(roleService.selectRolePermissionByUserId(user.getUserId()));
        }
        return roles;
    }

    /**
     * 获取菜单数据权限
     *
     * @param user 用户信息
     * @return 菜单权限信息
     */
    @Override
    public Set<String> getMenuPermission(SysUser user) {
        Set<String> perms = new HashSet<String>();
        // 管理员拥有所有权限
        if (user.isAdmin()) {
            perms.add("*:*:*");
        } else {
            List<SysRole> roles = user.getRoles();
            if (!roles.isEmpty() && roles.size() > 1) {
                // 多角色设置permissions属性，以便数据权限匹配权限
                for (SysRole role : roles) {
                    Set<String> rolePerms = menuService.selectMenuPermsByRoleId(role.getRoleId());
                    role.setPermissions(rolePerms);
                    perms.addAll(rolePerms);
                }
            } else {
                perms.addAll(menuService.selectMenuPermsByUserId(user.getUserId()));
            }
        }
        return perms;
    }
    
    /**
     * 获取工作流权限
     * @param user 用户信息
     * @return 工作流权限集合
     */
    @Override
    public Set<String> getWorkflowPermission(SysUser user) {
        Set<String> workflowPerms = new HashSet<>();
        
        // 管理员拥有所有工作流权限
        if (user.isAdmin()) {
            workflowPerms.add("workflow:*:*");
            return workflowPerms;
        }
        
        // 获取用户的工作流角色
        List<SysRole> roles = roleService.selectRolesByUserId(user.getUserId());
        if (!CollectionUtils.isEmpty(roles)) {
            for (SysRole role : roles) {
                // 查询角色对应的工作流权限
                List<SysPermission> permissions = sysPermissionMapper.selectList(
                    new LambdaQueryWrapper<SysPermission>()
                        .eq(SysPermission::getPermissionType, SysPermission.TYPE_WORKFLOW)
                        .eq(SysPermission::getStatus, SysPermission.STATUS_NORMAL)
                        .inSql(SysPermission::getPermissionId, 
                            "SELECT permission_id FROM sys_role_permission WHERE role_id = " + role.getRoleId())
                );
                
                workflowPerms.addAll(permissions.stream()
                    .map(SysPermission::getPermissionCode)
                    .filter(StringUtils::hasText)
                    .collect(Collectors.toSet()));
            }
        }
        
        return workflowPerms;
    }
    
    /**
     * 检查用户是否具有指定工作流权限
     * @param userId 用户ID
     * @param permissionCode 权限代码
     * @return 是否具有权限
     */
    @Override
    public boolean hasWorkflowPermission(Long userId, String permissionCode) {
        if (userId == null || !StringUtils.hasText(permissionCode)) {
            return false;
        }
        
        SysUser user = sysUserService.getById(userId);
        if (user == null) {
            return false;
        }
        
        // 管理员拥有所有权限
        if (user.isAdmin()) {
            return true;
        }
        
        Set<String> workflowPerms = getWorkflowPermission(user);
        return workflowPerms.contains(permissionCode) || workflowPerms.contains("workflow:*:*");
    }
    
    /**
     * 根据权限类型获取权限列表
     * @param permissionType 权限类型（MENU、BUTTON、API、WORKFLOW）
     * @return 权限列表
     */
    @Override
    public List<SysPermission> getPermissionsByType(String permissionType) {
        if (!StringUtils.hasText(permissionType)) {
            return List.of();
        }
        
        return sysPermissionMapper.selectList(
            new LambdaQueryWrapper<SysPermission>()
                .eq(SysPermission::getPermissionType, permissionType)
                .eq(SysPermission::getStatus, SysPermission.STATUS_NORMAL)
                .orderByAsc(SysPermission::getSortOrder)
        );
    }
    
    /**
     * 获取用户所有权限（包括菜单、工作流等）
     * @param user 用户信息
     * @return 所有权限集合
     */
    @Override
    public Set<String> getAllPermissions(SysUser user) {
        Set<String> allPerms = new HashSet<>();
        
        // 添加角色权限
        allPerms.addAll(getRolePermission(user));
        
        // 添加菜单权限
        allPerms.addAll(getMenuPermission(user));
        
        // 添加工作流权限
        allPerms.addAll(getWorkflowPermission(user));
        
        return allPerms;
    }
}
