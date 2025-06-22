package com.chennian.storytelling.service.impl;

import cn.hutool.core.date.DateTime;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;

import com.chennian.storytelling.bean.dto.RoleDTO;
import com.chennian.storytelling.bean.dto.UserRoleDTO;
import com.chennian.storytelling.bean.model.SysPermission;
import com.chennian.storytelling.bean.model.SysRole;
import com.chennian.storytelling.bean.model.SysRolePermission;
import com.chennian.storytelling.bean.model.SysUserRole;
import com.chennian.storytelling.common.enums.PermissionErrorCode;
import com.chennian.storytelling.common.response.ServerResponseEntity;
import com.chennian.storytelling.dao.SysPermissionMapper;
import com.chennian.storytelling.dao.SysRoleMapper;
import com.chennian.storytelling.dao.SysRolePermissionMapper;
import com.chennian.storytelling.dao.SysUserRoleMapper;
import com.chennian.storytelling.service.IUnifiedPermissionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

/**
 * 统一权限管理服务实现类
 * 融合原有系统权限和工作流权限管理
 * 
 * @author chennian
 * @date 2024-01-20
 */
@Service
public class UnifiedPermissionServiceImpl implements IUnifiedPermissionService {

    @Autowired
    private SysRoleMapper sysRoleMapper;
    
    @Autowired
    private SysPermissionMapper sysPermissionMapper;
    
    @Autowired
    private SysRolePermissionMapper sysRolePermissionMapper;
    
    @Autowired
    private SysUserRoleMapper sysUserRoleMapper;

    // =====================================================
    // 角色管理
    // =====================================================

    @Override
    public ServerResponseEntity<IPage<SysRole>> getRoleList(RoleDTO roleDTO) {
        Page<SysRole> page = new Page<>(roleDTO.getPageNum(), roleDTO.getPageSize());
        List<SysRole> roleList = sysRoleMapper.selectRoleList(roleDTO);
        int total = sysRoleMapper.countRoleList(roleDTO);
        
        page.setRecords(roleList);
        page.setTotal(total);
        return ServerResponseEntity.success(page);
    }

    @Override
    public ServerResponseEntity<SysRole> getRoleById(Long roleId) {
        SysRole role = sysRoleMapper.selectRoleById(roleId);
        if (role == null) {
            return ServerResponseEntity.fail(PermissionErrorCode.ROLE_NOT_FOUND.getCode(), PermissionErrorCode.ROLE_NOT_FOUND.getMessage());
        }
        return ServerResponseEntity.success(role);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public ServerResponseEntity<Void> saveRole(SysRole role) {
        // 校验角色名称唯一性
        if (sysRoleMapper.checkRoleNameUnique(role.getRoleName(), null) > 0) {
            return ServerResponseEntity.fail(PermissionErrorCode.ROLE_NAME_EXISTS.getCode(), PermissionErrorCode.ROLE_NAME_EXISTS.getMessage());
        }
        
        // 校验角色权限字符唯一性
        if (sysRoleMapper.checkRoleKeyUnique(role.getRoleKey(), null) > 0) {
            return ServerResponseEntity.fail(PermissionErrorCode.ROLE_KEY_EXISTS.getCode(), PermissionErrorCode.ROLE_KEY_EXISTS.getMessage());
        }
        
        role.setCreateTime(DateTime.now());
        int result = sysRoleMapper.insertRole(role);
        
        if (result > 0) {
            return ServerResponseEntity.success();
        }
        return ServerResponseEntity.fail(PermissionErrorCode.ROLE_SAVE_FAILED.getCode(), PermissionErrorCode.ROLE_SAVE_FAILED.getMessage());
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public ServerResponseEntity<Void> updateRole(SysRole role) {
        // 校验角色名称唯一性
        if (sysRoleMapper.checkRoleNameUnique(role.getRoleName(), role.getRoleId()) > 0) {
            return ServerResponseEntity.fail(PermissionErrorCode.ROLE_NAME_EXISTS.getCode(), PermissionErrorCode.ROLE_NAME_EXISTS.getMessage());
        }
        
        // 校验角色权限字符唯一性
        if (sysRoleMapper.checkRoleKeyUnique(role.getRoleKey(), role.getRoleId()) > 0) {
            return ServerResponseEntity.fail(PermissionErrorCode.ROLE_KEY_EXISTS.getCode(), PermissionErrorCode.ROLE_KEY_EXISTS.getMessage());
        }
        
        role.setUpdateTime(DateTime.now());
        int result = sysRoleMapper.updateRole(role);
        
        if (result > 0) {
            return ServerResponseEntity.success();
        }
        return ServerResponseEntity.fail(PermissionErrorCode.ROLE_UPDATE_FAILED.getCode(), PermissionErrorCode.ROLE_UPDATE_FAILED.getMessage());
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public ServerResponseEntity<Void> deleteRole(Long roleId) {
        // 检查角色是否被用户使用
        int userCount = sysUserRoleMapper.countUserRoleByRoleId(roleId);
        if (userCount > 0) {
            return ServerResponseEntity.fail(PermissionErrorCode.ROLE_IN_USE.getCode(), PermissionErrorCode.ROLE_IN_USE.getMessage());
        }
        
        // 删除角色权限关联
        sysRolePermissionMapper.deleteRolePermissionByRoleId(roleId);
        
        // 删除角色
        int result = sysRoleMapper.deleteRoleById(roleId);
        
        if (result > 0) {
            return ServerResponseEntity.success();
        }
        return ServerResponseEntity.fail(PermissionErrorCode.ROLE_DELETE_FAILED.getCode(), PermissionErrorCode.ROLE_DELETE_FAILED.getMessage());
    }

    // =====================================================
    // 权限管理
    // =====================================================

    @Override
    public ServerResponseEntity<List<SysPermission>> getPermissionTree(String permissionType) {
        List<SysPermission> permissions = sysPermissionMapper.selectPermissionList(permissionType);
        List<SysPermission> tree = buildPermissionTree(permissions, 0L);
        return ServerResponseEntity.success(tree);
    }

    @Override
    public ServerResponseEntity<List<SysPermission>> getPermissionsByRoleId(Long roleId) {
        List<SysPermission> permissions = sysPermissionMapper.selectPermissionsByRoleId(roleId);
        return ServerResponseEntity.success(permissions);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public ServerResponseEntity<Void> savePermission(SysPermission permission) {
        // 校验权限编码唯一性
        if (sysPermissionMapper.checkPermissionCodeUnique(permission.getPermissionCode(), null) > 0) {
            return ServerResponseEntity.fail(PermissionErrorCode.PERMISSION_CODE_EXISTS.getCode(), PermissionErrorCode.PERMISSION_CODE_EXISTS.getMessage());
        }
        
        permission.setCreateTime(LocalDateTime.now());
        int result = sysPermissionMapper.insertPermission(permission);
        
        if (result > 0) {
            return ServerResponseEntity.success();
        }
        return ServerResponseEntity.fail(PermissionErrorCode.PERMISSION_SAVE_FAILED.getCode(), PermissionErrorCode.PERMISSION_SAVE_FAILED.getMessage());
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public ServerResponseEntity<Void> updatePermission(SysPermission permission) {
        // 校验权限编码唯一性
        if (sysPermissionMapper.checkPermissionCodeUnique(permission.getPermissionCode(), permission.getPermissionId()) > 0) {
            return ServerResponseEntity.fail(PermissionErrorCode.PERMISSION_CODE_EXISTS.getCode(), PermissionErrorCode.PERMISSION_CODE_EXISTS.getMessage());
        }
        
        permission.setUpdateTime(LocalDateTime.now());
        int result = sysPermissionMapper.updatePermission(permission);
        
        if (result > 0) {
            return ServerResponseEntity.success();
        }
        return ServerResponseEntity.fail(PermissionErrorCode.PERMISSION_UPDATE_FAILED.getCode(), PermissionErrorCode.PERMISSION_UPDATE_FAILED.getMessage());
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public ServerResponseEntity<Void> deletePermission(Long permissionId) {
        // 检查是否有子权限
        int childCount = sysPermissionMapper.countChildPermissions(permissionId);
        if (childCount > 0) {
            return ServerResponseEntity.fail(PermissionErrorCode.PERMISSION_HAS_CHILDREN.getCode(), PermissionErrorCode.PERMISSION_HAS_CHILDREN.getMessage());
        }
        
        // 检查权限是否被角色使用
        int roleCount = sysRolePermissionMapper.countRolePermissionByPermissionId(permissionId);
        if (roleCount > 0) {
            return ServerResponseEntity.fail(PermissionErrorCode.PERMISSION_IN_USE.getCode(), PermissionErrorCode.PERMISSION_IN_USE.getMessage());
        }
        
        int result = sysPermissionMapper.deletePermissionById(permissionId);
        
        if (result > 0) {
            return ServerResponseEntity.success();
        }
        return ServerResponseEntity.fail(PermissionErrorCode.PERMISSION_DELETE_FAILED.getCode(), PermissionErrorCode.PERMISSION_DELETE_FAILED.getMessage());
    }

    // =====================================================
    // 角色权限关联管理
    // =====================================================

    @Override
    @Transactional(rollbackFor = Exception.class)
    public ServerResponseEntity<Void> assignPermissionsToRole(Long roleId, List<Long> permissionIds) {
        // 先删除原有权限
        sysRolePermissionMapper.deleteRolePermissionByRoleId(roleId);
        
        // 批量插入新权限
        if (!CollectionUtils.isEmpty(permissionIds)) {
            List<SysRolePermission> rolePermissions = permissionIds.stream()
                .map(permissionId -> {
                    SysRolePermission rp = new SysRolePermission();
                    rp.setRoleId(roleId);
                    rp.setPermissionId(permissionId);
                    rp.setCreateTime(LocalDateTime.now());
                    return rp;
                })
                .collect(Collectors.toList());
            
            int result = sysRolePermissionMapper.batchInsertRolePermission(rolePermissions);
            if (result != rolePermissions.size()) {
                return ServerResponseEntity.fail(PermissionErrorCode.ASSIGN_PERMISSION_FAILED.getCode(), PermissionErrorCode.ASSIGN_PERMISSION_FAILED.getMessage());
            }
        }
        
        return ServerResponseEntity.success();
    }

    // =====================================================
    // 用户角色管理
    // =====================================================

    @Override
    public ServerResponseEntity<List<SysRole>> getRolesByUserId(Long userId) {
        List<SysRole> roles = sysRoleMapper.selectRolesByUserId(userId);
        return ServerResponseEntity.success(roles);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public ServerResponseEntity<Void> assignRolesToUser(Long userId, List<UserRoleDTO> userRoles) {
        // 先删除用户原有角色
        sysUserRoleMapper.deleteUserRoleByUserId(userId);
        
        // 批量插入新角色
        if (!CollectionUtils.isEmpty(userRoles)) {
            List<SysUserRole> userRoleList = userRoles.stream()
                .map(dto -> {
                    SysUserRole ur = new SysUserRole();
                    ur.setUserId(userId);
                    ur.setRoleId(dto.getRoleId());
                    ur.setDepartmentId(dto.getDepartmentId());
                    ur.setEffectiveDate(dto.getEffectiveDate());
                    ur.setExpiryDate(dto.getExpiryDate());
                    ur.setEnabled(dto.getEnabled()  ? 1 : 0);
                    ur.setCreatedTime(LocalDateTime.now());
                    return ur;
                })
                .collect(Collectors.toList());
            
            int result = sysUserRoleMapper.batchInsertUserRole(userRoleList);
            if (result != userRoleList.size()) {
                return ServerResponseEntity.fail(PermissionErrorCode.ASSIGN_ROLE_FAILED.getCode(), PermissionErrorCode.ASSIGN_ROLE_FAILED.getMessage());
            }
        }
        
        return ServerResponseEntity.success();
    }

    @Override
    public ServerResponseEntity<List<String>> getUserPermissions(Long userId) {
        List<String> permissions = sysPermissionMapper.selectPermissionCodesByUserId(userId);
        return ServerResponseEntity.success(permissions);
    }

    @Override
    public ServerResponseEntity<Boolean> hasPermission(Long userId, String permissionCode) {
        int count = sysPermissionMapper.checkUserPermission(userId, permissionCode);
        return ServerResponseEntity.success(count > 0);
    }

    // =====================================================
    // 工作流权限管理
    // =====================================================

    @Override
    public ServerResponseEntity<List<SysRole>> getWorkflowRoles() {
        List<SysRole> roles = sysRoleMapper.selectWorkflowRoles();
        return ServerResponseEntity.success(roles);
    }

    @Override
    public ServerResponseEntity<List<SysPermission>> getWorkflowPermissions() {
        List<SysPermission> permissions = sysPermissionMapper.selectWorkflowPermissions();
        return ServerResponseEntity.success(permissions);
    }

    @Override
    public ServerResponseEntity<Boolean> hasWorkflowPermission(Long userId, String processKey, String operation) {
        String permissionCode = "workflow:" + processKey + ":" + operation;
        return hasPermission(userId, permissionCode);
    }

    // =====================================================
    // 私有方法
    // =====================================================

    /**
     * 构建权限树
     */
    private List<SysPermission> buildPermissionTree(List<SysPermission> permissions, Long parentId) {
        List<SysPermission> tree = new ArrayList<>();
        
        for (SysPermission permission : permissions) {
            if (Objects.equals(permission.getParentId(), parentId)) {
                List<SysPermission> children = buildPermissionTree(permissions, permission.getPermissionId());
                permission.setChildren(children);
                tree.add(permission);
            }
        }
        
        return tree;
    }
}