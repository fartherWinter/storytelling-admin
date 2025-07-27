package com.chennian.storytelling.user.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.chennian.storytelling.user.entity.Role;
import com.chennian.storytelling.user.entity.User;

import java.util.List;

/**
 * 角色服务接口
 * 
 * @author chennian
 * @since 2024-01-01
 */
public interface RoleService extends IService<Role> {

    /**
     * 根据角色编码查询角色
     * 
     * @param roleCode 角色编码
     * @return 角色信息
     */
    Role getRoleByCode(String roleCode);

    /**
     * 根据用户ID查询角色列表
     * 
     * @param userId 用户ID
     * @return 角色列表
     */
    List<Role> getRolesByUserId(Long userId);

    /**
     * 分页查询角色列表
     * 
     * @param page 分页参数
     * @param roleName 角色名称（模糊查询）
     * @param roleCode 角色编码（模糊查询）
     * @param status 状态
     * @return 角色分页列表
     */
    IPage<Role> getRolePage(Page<Role> page, String roleName, String roleCode, Integer status);

    /**
     * 查询所有可用角色
     * 
     * @return 角色列表
     */
    List<Role> getAvailableRoles();

    /**
     * 创建角色
     * 
     * @param role 角色信息
     * @return 创建结果
     */
    boolean createRole(Role role);

    /**
     * 更新角色信息
     * 
     * @param role 角色信息
     * @return 更新结果
     */
    boolean updateRole(Role role);

    /**
     * 删除角色
     * 
     * @param roleId 角色ID
     * @return 删除结果
     */
    boolean deleteRole(Long roleId);

    /**
     * 批量删除角色
     * 
     * @param roleIds 角色ID列表
     * @return 删除结果
     */
    boolean batchDeleteRoles(List<Long> roleIds);

    /**
     * 启用/禁用角色
     * 
     * @param roleId 角色ID
     * @param status 状态
     * @return 更新结果
     */
    boolean updateRoleStatus(Long roleId, Integer status);

    /**
     * 批量更新角色状态
     * 
     * @param roleIds 角色ID列表
     * @param status 状态
     * @return 更新结果
     */
    boolean batchUpdateRoleStatus(List<Long> roleIds, Integer status);

    /**
     * 获取角色下的用户列表
     * 
     * @param roleId 角色ID
     * @return 用户列表
     */
    List<User> getUsersByRoleId(Long roleId);

    /**
     * 检查角色编码是否存在
     * 
     * @param roleCode 角色编码
     * @param excludeRoleId 排除的角色ID
     * @return 是否存在
     */
    boolean isRoleCodeExists(String roleCode, Long excludeRoleId);

    /**
     * 检查角色名称是否存在
     * 
     * @param roleName 角色名称
     * @param excludeRoleId 排除的角色ID
     * @return 是否存在
     */
    boolean isRoleNameExists(String roleName, Long excludeRoleId);

    /**
     * 检查角色是否被使用
     * 
     * @param roleId 角色ID
     * @return 是否被使用
     */
    boolean isRoleInUse(Long roleId);
}