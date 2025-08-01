package com.chennian.storytelling.service;

import com.chennian.storytelling.bean.bo.SysRolePartitionBo;
import com.chennian.storytelling.bean.model.SysRole;
import com.baomidou.mybatisplus.extension.service.IService;
import com.chennian.storytelling.bean.model.SysUserRole;

import java.util.List;
import java.util.Set;

/**
 * 统一角色服务接口
 * @author chen
 * @createDate 2025-05-06 19:11:40
 */
public interface SysRoleService extends IService<SysRole> {
    /**
     * 根据条件分页查询角色数据
     *
     * @param role 角色信息
     * @return 角色数据集合信息
     */
    public List<SysRole> selectRoleList(SysRole role);

    /**
     * 根据用户ID查询角色列表
     *
     * @param userId 用户ID
     * @return 角色列表
     */
    public List<SysRole> selectRolesByUserId(Long userId);

    /**
     * 根据用户ID查询角色权限
     *
     * @param userId 用户ID
     * @return 权限列表
     */
    public Set<String> selectRolePermissionByUserId(Long userId);

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
     * 校验角色名称是否唯一
     *
     * @param role 角色信息
     * @return 结果
     */
    public boolean checkRoleNameUnique(SysRole role);

    /**
     * 校验角色权限是否唯一
     *
     * @param role 角色信息
     * @return 结果
     */
    public boolean checkRoleKeyUnique(SysRole role);

    /**
     * 校验角色是否允许操作
     *
     * @param role 角色信息
     */
    public void checkRoleAllowed(SysRole role);

    /**
     * 校验角色是否有数据权限
     *
     * @param roleId 角色id
     */
    public void checkRoleDataScope(Long roleId);

    /**
     * 通过角色ID查询角色使用数量
     *
     * @param roleId 角色ID
     * @return 结果
     */
    public int countUserRoleByRoleId(Long roleId);

    /**
     * 新增保存角色信息
     *
     * @param role 角色信息
     * @return 结果
     */
    public int insertRole(SysRole role);


    /**
     * 修改保存角色信息
     *
     * @param role 角色信息
     * @return 结果
     */
    public int updateRole(SysRole role);

    /**
     * 修改角色状态
     *
     * @param role 角色信息
     * @return 结果
     */
    public int updateRoleStatus(SysRole role);

    /**
     * 修改数据权限信息
     *
     * @param role 角色信息
     * @return 结果
     */
    public int authDataScope(SysRole role);

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
     * 取消授权用户角色
     *
     * @param userRole 用户和角色关联信息
     * @return 结果
     */
    public int deleteAuthUser(SysUserRole userRole);

    /**
     * 批量取消授权用户角色
     *
     * @param roleId  角色ID
     * @param userIds 需要取消授权的用户数据ID
     * @return 结果
     */
    public int deleteAuthUsers(Long roleId, Long[] userIds);

    /**
     * 批量选择授权用户角色
     *
     * @param roleId  角色ID
     * @param userIds 需要删除的用户数据ID
     * @return 结果
     */
    public int insertAuthUsers(Long roleId, Long[] userIds);
    
    // ==================== 工作流角色相关方法 ====================
    
    /**
     * 查询工作流角色列表
     * @return 工作流角色列表
     */
    public List<SysRole> selectWorkflowRoles();
    
    /**
     * 根据用户ID和部门ID查询角色列表
     * @param userId 用户ID
     * @param deptId 部门ID
     * @return 角色列表
     */
    public List<SysRole> selectRolesByUserIdAndDept(Long userId, Long deptId);
    
    /**
     * 为用户分配角色（支持部门、生效时间等）
     * @param userId 用户ID
     * @param roleId 角色ID
     * @param deptId 部门ID
     * @return 分配结果
     */
    public boolean assignRoleToUser(Long userId, Long roleId, Long deptId);
    
    /**
     * 根据角色类型查询角色列表
     * @param roleType 角色类型（SYSTEM、WORKFLOW、CUSTOM）
     * @return 角色列表
     */
    public List<SysRole> selectRolesByType(String roleType);
    
    /**
     * 检查用户在指定部门是否具有指定角色
     * @param userId 用户ID
     * @param roleId 角色ID
     * @param deptId 部门ID
     * @return 是否具有角色
     */
    public boolean hasRoleInDept(Long userId, Long roleId, Long deptId);

}
