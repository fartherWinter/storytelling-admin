package com.chennian.storytelling.dao.workflow;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.chennian.storytelling.bean.model.workflow.WfRolePermission;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 角色权限关联Mapper接口
 * 
 * @author storytelling
 * @date 2024-01-01
 */
@Mapper
public interface WfRolePermissionMapper extends BaseMapper<WfRolePermission> {

    /**
     * 根据角色ID查询权限ID列表
     * 
     * @param roleId 角色ID
     * @return 权限ID列表
     */
    List<String> selectPermissionIdsByRoleId(@Param("roleId") String roleId);

    /**
     * 根据权限ID查询角色ID列表
     * 
     * @param permissionId 权限ID
     * @return 角色ID列表
     */
    List<String> selectRoleIdsByPermissionId(@Param("permissionId") String permissionId);

    /**
     * 根据角色ID删除角色权限关联
     * 
     * @param roleId 角色ID
     * @return 删除行数
     */
    int deleteByRoleId(@Param("roleId") String roleId);

    /**
     * 根据权限ID删除角色权限关联
     * 
     * @param permissionId 权限ID
     * @return 删除行数
     */
    int deleteByPermissionId(@Param("permissionId") String permissionId);

    /**
     * 根据角色ID和权限ID删除关联
     * 
     * @param roleId 角色ID
     * @param permissionId 权限ID
     * @return 删除行数
     */
    int deleteByRoleIdAndPermissionId(@Param("roleId") String roleId, 
                                     @Param("permissionId") String permissionId);

    /**
     * 批量插入角色权限关联
     * 
     * @param rolePermissions 角色权限关联列表
     * @return 插入行数
     */
    int batchInsertRolePermissions(@Param("rolePermissions") List<WfRolePermission> rolePermissions);

    /**
     * 根据角色ID批量删除权限关联
     * 
     * @param roleIds 角色ID列表
     * @return 删除行数
     */
    int batchDeleteByRoleIds(@Param("roleIds") List<String> roleIds);

    /**
     * 根据权限ID批量删除角色关联
     * 
     * @param permissionIds 权限ID列表
     * @return 删除行数
     */
    int batchDeleteByPermissionIds(@Param("permissionIds") List<String> permissionIds);

    /**
     * 检查角色权限关联是否存在
     * 
     * @param roleId 角色ID
     * @param permissionId 权限ID
     * @return 存在数量
     */
    int checkRolePermissionExists(@Param("roleId") String roleId, 
                                 @Param("permissionId") String permissionId);

    /**
     * 查询角色权限关联统计信息
     * 
     * @return 统计信息
     */
    List<java.util.Map<String, Object>> selectRolePermissionStatistics();

    /**
     * 根据角色ID列表查询权限ID列表
     * 
     * @param roleIds 角色ID列表
     * @return 权限ID列表
     */
    List<String> selectPermissionIdsByRoleIds(@Param("roleIds") List<String> roleIds);
}