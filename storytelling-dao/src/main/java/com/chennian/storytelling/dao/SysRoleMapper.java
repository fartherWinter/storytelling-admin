package com.chennian.storytelling.dao;

import com.chennian.storytelling.bean.dto.RoleDTO;
import com.chennian.storytelling.bean.model.SysRole;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
* @author chen
*
* @createDate 2025-05-06 19:11:40
*
*/
@Mapper
public interface SysRoleMapper extends BaseMapper<SysRole> {
    /**
     * 根据条件分页查询角色数据
     *
     * @param role 角色信息
     * @return 角色数据集合信息
     */
    public List<SysRole> selectRoleList(SysRole role);

    /**
     * 根据用户ID查询角色
     *
     * @param userId 用户ID
     * @return 角色列表
     */
    public List<SysRole> selectRolePermissionByUserId(Long userId);

    /**
     * 查询所有角色
     *
     * @return 角色列表
     */
    public List<SysRole> selectRoleAll();

    /**
     * 根据用户ID获取角色选择框列表
     *
     * @param userId 用户ID
     * @return 选中角色ID列表
     */
    public List<Long> selectRoleListByUserId(Long userId);

    /**
     * 通过角色ID查询角色
     *
     * @param roleId 角色ID
     * @return 角色对象信息
     */
    public SysRole selectRoleById(Long roleId);

    /**
     * 根据用户ID查询角色
     *
     * @param userName 用户名
     * @return 角色列表
     */
    public List<SysRole> selectRolesByUserName(String userName);

    /**
     * 校验角色名称是否唯一
     *
     * @param roleName 角色名称
     * @return 角色信息
     */
    public SysRole checkRoleNameUnique(String roleName);

    /**
     * 校验角色权限是否唯一
     *
     * @param roleKey 角色权限
     * @return 角色信息
     */
    public SysRole checkRoleKeyUnique(String roleKey);

    /**
     * 校验角色名称是否唯一（排除指定角色ID）
     *
     * @param roleName 角色名称
     * @param roleId 排除的角色ID
     * @return 重复数量
     */
    public int checkRoleNameUnique(@Param("roleName") String roleName, @Param("roleId") Long roleId);

    /**
     * 校验角色权限字符是否唯一（排除指定角色ID）
     *
     * @param roleKey 角色权限字符
     * @param roleId 排除的角色ID
     * @return 重复数量
     */
    public int checkRoleKeyUnique(@Param("roleKey") String roleKey, @Param("roleId") Long roleId);

    /**
     * 修改角色信息
     *
     * @param role 角色信息
     * @return 结果
     */
    public int updateRole(SysRole role);

    /**
     * 新增角色信息
     *
     * @param role 角色信息
     * @return 结果
     */
    public int insertRole(SysRole role);

    /**
     * 通过角色ID删除角色
     *
     * @param roleId 角色ID
     * @return 结果
     */
    public int deleteRoleById(Long roleId);

    /**
     * 批量删除角色信息
     *
     * @param roleIds 需要删除的角色ID
     * @return 结果
     */
    public int deleteRoleByIds(Long[] roleIds);

    /**
     * 根据条件分页查询角色数据（使用RoleDTO）
     *
     * @param roleDTO 角色查询条件
     * @return 角色数据集合信息
     */
    public List<SysRole> selectRoleList(RoleDTO roleDTO);

    /**
     * 根据条件统计角色数量
     *
     * @param roleDTO 角色查询条件
     * @return 角色数量
     */
    public int countRoleList(RoleDTO roleDTO);

    /**
     * 根据用户ID查询角色列表
     *
     * @param userId 用户ID
     * @return 角色列表
     */
    public List<SysRole> selectRolesByUserId(Long userId);

    /**
     * 查询工作流角色列表
     *
     * @return 工作流角色列表
     */
    public List<SysRole> selectWorkflowRoles();

    /**
     * 根据用户ID和部门ID查询角色列表
     *
     * @param userId 用户ID
     * @param deptId 部门ID
     * @return 角色列表
     */
    public List<SysRole> selectRolesByUserIdAndDept(@Param("userId") Long userId, @Param("deptId") Long deptId);

    /**
     * 根据角色类型查询角色列表
     *
     * @param roleType 角色类型
     * @return 角色列表
     */
    public List<SysRole> selectRolesByType(@Param("roleType") String roleType);
}




