package com.chennian.storytelling.dao;

import com.chennian.storytelling.bean.model.SysRolePermission;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

/**
 * 角色权限关联 数据层
 * 
 * @author chennian
 * @date 2024-01-20
 */
@Mapper
public interface SysRolePermissionMapper {

    /**
     * 根据角色ID查询权限ID列表
     * 
     * @param roleId 角色ID
     * @return 权限ID列表
     */
    List<Long> selectPermissionIdsByRoleId(@Param("roleId") Long roleId);

    /**
     * 根据权限ID查询角色ID列表
     * 
     * @param permissionId 权限ID
     * @return 角色ID列表
     */
    List<Long> selectRoleIdsByPermissionId(@Param("permissionId") Long permissionId);

    /**
     * 新增角色权限关联
     * 
     * @param rolePermission 角色权限关联信息
     * @return 结果
     */
    int insertRolePermission(SysRolePermission rolePermission);

    /**
     * 批量新增角色权限关联
     * 
     * @param rolePermissions 角色权限关联列表
     * @return 结果
     */
    int batchInsertRolePermission(@Param("rolePermissions") List<SysRolePermission> rolePermissions);

    /**
     * 删除角色权限关联
     * 
     * @param roleId 角色ID
     * @param permissionId 权限ID
     * @return 结果
     */
    int deleteRolePermission(@Param("roleId") Long roleId, @Param("permissionId") Long permissionId);

    /**
     * 根据角色ID删除角色权限关联
     * 
     * @param roleId 角色ID
     * @return 结果
     */
    int deleteRolePermissionByRoleId(@Param("roleId") Long roleId);

    /**
     * 根据权限ID删除角色权限关联
     * 
     * @param permissionId 权限ID
     * @return 结果
     */
    int deleteRolePermissionByPermissionId(@Param("permissionId") Long permissionId);

    /**
     * 批量删除角色权限关联（根据角色ID）
     * 
     * @param roleIds 角色ID列表
     * @return 结果
     */
    int deleteRolePermissionByRoleIds(@Param("roleIds") Long[] roleIds);

    /**
     * 批量删除角色权限关联（根据权限ID）
     * 
     * @param permissionIds 权限ID列表
     * @return 结果
     */
    int deleteRolePermissionByPermissionIds(@Param("permissionIds") Long[] permissionIds);

    /**
     * 查询角色权限关联是否存在
     * 
     * @param roleId 角色ID
     * @param permissionId 权限ID
     * @return 结果
     */
    int checkRolePermissionExists(@Param("roleId") Long roleId, @Param("permissionId") Long permissionId);

    /**
     * 统计角色权限关联数量（根据角色ID）
     * 
     * @param roleId 角色ID
     * @return 关联数量
     */
    int countRolePermissionByRoleId(@Param("roleId") Long roleId);

    /**
     * 统计角色权限关联数量（根据权限ID）
     * 
     * @param permissionId 权限ID
     * @return 关联数量
     */
    int countRolePermissionByPermissionId(@Param("permissionId") Long permissionId);

    /**
     * 查询所有角色权限关联
     * 
     * @return 角色权限关联列表
     */
    List<SysRolePermission> selectAllRolePermissions();

    /**
     * 根据角色ID列表查询权限ID列表
     * 
     * @param roleIds 角色ID列表
     * @return 权限ID列表
     */
    List<Long> selectPermissionIdsByRoleIds(@Param("roleIds") List<Long> roleIds);

    /**
     * 根据权限ID列表查询角色ID列表
     * 
     * @param permissionIds 权限ID列表
     * @return 角色ID列表
     */
    List<Long> selectRoleIdsByPermissionIds(@Param("permissionIds") List<Long> permissionIds);

    /**
     * 查询角色权限关联详情
     * 
     * @param roleId 角色ID
     * @param permissionId 权限ID
     * @return 角色权限关联信息
     */
    SysRolePermission selectRolePermission(@Param("roleId") Long roleId, @Param("permissionId") Long permissionId);

    /**
     * 根据角色ID查询角色权限关联列表
     * 
     * @param roleId 角色ID
     * @return 角色权限关联列表
     */
    List<SysRolePermission> selectRolePermissionsByRoleId(@Param("roleId") Long roleId);

    /**
     * 根据权限ID查询角色权限关联列表
     * 
     * @param permissionId 权限ID
     * @return 角色权限关联列表
     */
    List<SysRolePermission> selectRolePermissionsByPermissionId(@Param("permissionId") Long permissionId);

    /**
     * 批量更新角色权限关联
     * 
     * @param roleId 角色ID
     * @param permissionIds 权限ID列表
     * @return 结果
     */
    int batchUpdateRolePermissions(@Param("roleId") Long roleId, @Param("permissionIds") List<Long> permissionIds);

    /**
     * 查询角色权限统计信息
     * 
     * @return 统计信息
     */
    List<Map<String, Object>> selectRolePermissionStatistics();

    /**
     * 根据角色类型查询权限ID列表
     * 
     * @param roleType 角色类型
     * @return 权限ID列表
     */
    List<Long> selectPermissionIdsByRoleType(@Param("roleType") String roleType);

    /**
     * 根据权限类型查询角色ID列表
     * 
     * @param permissionType 权限类型
     * @return 角色ID列表
     */
    List<Long> selectRoleIdsByPermissionType(@Param("permissionType") String permissionType);

    /**
     * 查询工作流角色权限关联
     * 
     * @return 角色权限关联列表
     */
    List<SysRolePermission> selectWorkflowRolePermissions();

    /**
     * 查询系统角色权限关联
     * 
     * @return 角色权限关联列表
     */
    List<SysRolePermission> selectSystemRolePermissions();

    /**
     * 复制角色权限
     * 
     * @param sourceRoleId 源角色ID
     * @param targetRoleId 目标角色ID
     * @return 结果
     */
    int copyRolePermissions(@Param("sourceRoleId") Long sourceRoleId, @Param("targetRoleId") Long targetRoleId);

    /**
     * 同步角色权限（删除无效关联）
     * 
     * @return 结果
     */
    int syncRolePermissions();

    /**
     * 查询孤立的角色权限关联（角色或权限已被删除）
     * 
     * @return 孤立的关联列表
     */
    List<SysRolePermission> selectOrphanRolePermissions();

    /**
     * 清理孤立的角色权限关联
     * 
     * @return 结果
     */
    int cleanOrphanRolePermissions();
}