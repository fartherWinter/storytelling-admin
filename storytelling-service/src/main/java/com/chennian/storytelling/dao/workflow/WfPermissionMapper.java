package com.chennian.storytelling.dao.workflow;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.chennian.storytelling.bean.model.workflow.WfPermission;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 权限Mapper接口
 * 
 * @author storytelling
 * @date 2024-01-01
 */
@Mapper
public interface WfPermissionMapper extends BaseMapper<WfPermission> {

    /**
     * 根据权限编码查询权限
     * 
     * @param permissionCode 权限编码
     * @return 权限信息
     */
    WfPermission selectByPermissionCode(@Param("permissionCode") String permissionCode);

    /**
     * 根据父权限ID查询子权限列表
     * 
     * @param parentId 父权限ID
     * @return 权限列表
     */
    List<WfPermission> selectByParentId(@Param("parentId") String parentId);

    /**
     * 根据资源类型查询权限列表
     * 
     * @param resourceType 资源类型
     * @return 权限列表
     */
    List<WfPermission> selectByResourceType(@Param("resourceType") String resourceType);

    /**
     * 根据启用状态查询权限列表
     * 
     * @param enabled 是否启用
     * @return 权限列表
     */
    List<WfPermission> selectByEnabled(@Param("enabled") Integer enabled);

    /**
     * 查询所有根权限（父权限ID为空的权限）
     * 
     * @return 权限列表
     */
    List<WfPermission> selectRootPermissions();

    /**
     * 查询权限树形结构
     * 
     * @return 权限树
     */
    List<WfPermission> selectPermissionTree();

    /**
     * 根据用户ID查询用户权限列表
     * 
     * @param userId 用户ID
     * @return 权限列表
     */
    List<WfPermission> selectPermissionsByUserId(@Param("userId") String userId);

    /**
     * 根据角色ID查询角色权限列表
     * 
     * @param roleId 角色ID
     * @return 权限列表
     */
    List<WfPermission> selectPermissionsByRoleId(@Param("roleId") String roleId);

    /**
     * 根据权限名称模糊查询
     * 
     * @param permissionName 权限名称
     * @return 权限列表
     */
    List<WfPermission> selectByPermissionNameLike(@Param("permissionName") String permissionName);

    /**
     * 查询权限统计信息
     * 
     * @return 统计信息
     */
    List<java.util.Map<String, Object>> selectPermissionStatistics();

    /**
     * 批量更新权限状态
     * 
     * @param permissionIds 权限ID列表
     * @param enabled 启用状态
     * @param updatedBy 更新人
     * @return 更新行数
     */
    int batchUpdatePermissionStatus(@Param("permissionIds") List<String> permissionIds, 
                                   @Param("enabled") Integer enabled, 
                                   @Param("updatedBy") String updatedBy);
}