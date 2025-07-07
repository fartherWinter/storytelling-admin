package com.chennian.storytelling.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.chennian.storytelling.bean.dto.UserRoleDTO;
import com.chennian.storytelling.bean.model.SysPermission;
import com.chennian.storytelling.bean.model.SysRole;
import com.chennian.storytelling.bean.dto.RoleDTO;
import com.chennian.storytelling.common.response.ServerResponseEntity;

import java.util.List;

/**
 * 统一权限管理服务接口
 * 融合原有系统权限和工作流权限管理
 * 
 * @author chennian
 * @date 2024-01-20
 */
public interface IUnifiedPermissionService {

    // =====================================================
    // 角色管理
    // =====================================================

    /**
     * 分页查询角色列表
     * 
     * @param roleDTO 查询条件
     * @return 角色列表
     */
    ServerResponseEntity<IPage<SysRole>> getRoleList(RoleDTO roleDTO);

    /**
     * 根据角色ID查询角色信息
     * 
     * @param roleId 角色ID
     * @return 角色信息
     */
    ServerResponseEntity<SysRole> getRoleById(Long roleId);

    /**
     * 新增角色
     * 
     * @param role 角色信息
     * @return 操作结果
     */
    ServerResponseEntity<Void> saveRole(SysRole role);

    /**
     * 修改角色
     * 
     * @param role 角色信息
     * @return 操作结果
     */
    ServerResponseEntity<Void> updateRole(SysRole role);

    /**
     * 删除角色
     * 
     * @param roleId 角色ID
     * @return 操作结果
     */
    ServerResponseEntity<Void> deleteRole(Long roleId);

    // =====================================================
    // 权限管理
    // =====================================================

    /**
     * 获取权限树
     * 
     * @param permissionType 权限类型(MENU,BUTTON,API,WORKFLOW)
     * @return 权限树
     */
    ServerResponseEntity<List<SysPermission>> getPermissionTree(String permissionType);

    /**
     * 根据角色ID获取权限列表
     * 
     * @param roleId 角色ID
     * @return 权限列表
     */
    ServerResponseEntity<List<SysPermission>> getPermissionsByRoleId(Long roleId);

    /**
     * 新增权限
     * 
     * @param permission 权限信息
     * @return 操作结果
     */
    ServerResponseEntity<Void> savePermission(SysPermission permission);

    /**
     * 修改权限
     * 
     * @param permission 权限信息
     * @return 操作结果
     */
    ServerResponseEntity<Void> updatePermission(SysPermission permission);

    /**
     * 删除权限
     * 
     * @param permissionId 权限ID
     * @return 操作结果
     */
    ServerResponseEntity<Void> deletePermission(Long permissionId);

    // =====================================================
    // 角色权限关联管理
    // =====================================================

    /**
     * 为角色分配权限
     * 
     * @param roleId 角色ID
     * @param permissionIds 权限ID列表
     * @return 操作结果
     */
    ServerResponseEntity<Void> assignPermissionsToRole(Long roleId, List<Long> permissionIds);

    // =====================================================
    // 用户角色管理
    // =====================================================

    /**
     * 根据用户ID获取角色列表
     * 
     * @param userId 用户ID
     * @return 角色列表
     */
    ServerResponseEntity<List<SysRole>> getRolesByUserId(Long userId);

    /**
     * 为用户分配角色
     * 
     * @param userId 用户ID
     * @param userRoles 用户角色关联信息列表
     * @return 操作结果
     */
    ServerResponseEntity<Void> assignRolesToUser(Long userId, List<UserRoleDTO> userRoles);

    /**
     * 获取用户权限列表
     * 
     * @param userId 用户ID
     * @return 权限编码列表
     */
    ServerResponseEntity<List<String>> getUserPermissions(Long userId);

    /**
     * 检查用户是否有指定权限
     * 
     * @param userId 用户ID
     * @param permissionCode 权限编码
     * @return 是否有权限
     */
    ServerResponseEntity<Boolean> hasPermission(Long userId, String permissionCode);

    // =====================================================
    // 工作流权限管理
    // =====================================================

    /**
     * 获取工作流角色列表
     * 
     * @return 工作流角色列表
     */
    ServerResponseEntity<List<SysRole>> getWorkflowRoles();

    /**
     * 获取工作流权限列表
     * 
     * @return 工作流权限列表
     */
    ServerResponseEntity<List<SysPermission>> getWorkflowPermissions();

    /**
     * 检查用户是否有工作流操作权限
     * 
     * @param userId 用户ID
     * @param processKey 流程标识
     * @param operation 操作类型(start,approve,reject,transfer等)
     * @return 是否有权限
     */
    ServerResponseEntity<Boolean> hasWorkflowPermission(Long userId, String processKey, String operation);
}