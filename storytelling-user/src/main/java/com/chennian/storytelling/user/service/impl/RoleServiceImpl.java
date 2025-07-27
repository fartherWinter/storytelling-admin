package com.chennian.storytelling.user.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.chennian.storytelling.user.entity.Role;
import com.chennian.storytelling.user.entity.User;
import com.chennian.storytelling.user.mapper.RoleMapper;
import com.chennian.storytelling.user.mapper.UserMapper;
import com.chennian.storytelling.user.mapper.UserRoleMapper;
import com.chennian.storytelling.user.service.RoleService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * 角色服务实现类
 * 
 * @author chennian
 * @since 2024-01-01
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class RoleServiceImpl extends ServiceImpl<RoleMapper, Role> implements RoleService {

    private final RoleMapper roleMapper;
    private final UserMapper userMapper;
    private final UserRoleMapper userRoleMapper;

    @Override
    public Role getRoleByCode(String roleCode) {
        return roleMapper.selectByRoleCode(roleCode);
    }

    @Override
    public List<Role> getRolesByUserId(Long userId) {
        return roleMapper.selectRolesByUserId(userId);
    }

    @Override
    public IPage<Role> getRolePage(Page<Role> page, String roleName, String roleCode, Integer status) {
        return roleMapper.selectRolePage(page, roleName, roleCode, status);
    }

    @Override
    public List<Role> getAvailableRoles() {
        return roleMapper.selectAvailableRoles();
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean createRole(Role role) {
        // 检查角色编码是否存在
        if (isRoleCodeExists(role.getRoleCode(), null)) {
            throw new RuntimeException("角色编码已存在");
        }
        
        // 检查角色名称是否存在
        if (isRoleNameExists(role.getRoleName(), null)) {
            throw new RuntimeException("角色名称已存在");
        }
        
        // 设置默认值
        if (role.getStatus() == null) {
            role.setStatus(1); // 默认启用
        }
        if (role.getSortOrder() == null) {
            role.setSortOrder(0);
        }
        
        return save(role);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean updateRole(Role role) {
        Role existRole = getById(role.getRoleId());
        if (existRole == null) {
            throw new RuntimeException("角色不存在");
        }
        
        // 检查角色编码是否存在
        if (!existRole.getRoleCode().equals(role.getRoleCode()) && 
            isRoleCodeExists(role.getRoleCode(), role.getRoleId())) {
            throw new RuntimeException("角色编码已存在");
        }
        
        // 检查角色名称是否存在
        if (!existRole.getRoleName().equals(role.getRoleName()) && 
            isRoleNameExists(role.getRoleName(), role.getRoleId())) {
            throw new RuntimeException("角色名称已存在");
        }
        
        return updateById(role);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean deleteRole(Long roleId) {
        // 检查角色是否被使用
        if (isRoleInUse(roleId)) {
            throw new RuntimeException("角色正在使用中，无法删除");
        }
        
        // 删除角色用户关联
        userRoleMapper.deleteByRoleId(roleId);
        
        // 删除角色
        return removeById(roleId);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean batchDeleteRoles(List<Long> roleIds) {
        // 检查角色是否被使用
        for (Long roleId : roleIds) {
            if (isRoleInUse(roleId)) {
                Role role = getById(roleId);
                throw new RuntimeException("角色[" + role.getRoleName() + "]正在使用中，无法删除");
            }
        }
        
        // 删除角色用户关联
        userRoleMapper.batchDeleteByRoleIds(roleIds);
        
        // 删除角色
        return removeByIds(roleIds);
    }

    @Override
    public boolean updateRoleStatus(Long roleId, Integer status) {
        Role role = new Role();
        role.setRoleId(roleId);
        role.setStatus(status);
        return updateById(role);
    }

    @Override
    public boolean batchUpdateRoleStatus(List<Long> roleIds, Integer status) {
        return roleMapper.batchUpdateStatus(roleIds, status, "system") > 0;
    }

    @Override
    public List<User> getUsersByRoleId(Long roleId) {
        return userMapper.selectUsersByRoleId(roleId);
    }

    @Override
    public boolean isRoleCodeExists(String roleCode, Long excludeRoleId) {
        LambdaQueryWrapper<Role> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(Role::getRoleCode, roleCode);
        if (excludeRoleId != null) {
            wrapper.ne(Role::getRoleId, excludeRoleId);
        }
        return count(wrapper) > 0;
    }

    @Override
    public boolean isRoleNameExists(String roleName, Long excludeRoleId) {
        LambdaQueryWrapper<Role> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(Role::getRoleName, roleName);
        if (excludeRoleId != null) {
            wrapper.ne(Role::getRoleId, excludeRoleId);
        }
        return count(wrapper) > 0;
    }

    @Override
    public boolean isRoleInUse(Long roleId) {
        List<Long> userIds = userRoleMapper.selectUserIdsByRoleId(roleId);
        return !userIds.isEmpty();
    }
}