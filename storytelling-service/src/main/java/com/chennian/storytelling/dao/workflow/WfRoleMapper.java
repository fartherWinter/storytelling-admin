package com.chennian.storytelling.dao.workflow;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.chennian.storytelling.bean.model.workflow.WfRole;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 角色Mapper接口
 * 
 * @author storytelling
 * @date 2024-01-01
 */
@Mapper
public interface WfRoleMapper extends BaseMapper<WfRole> {

    /**
     * 根据角色编码查询角色
     * 
     * @param roleCode 角色编码
     * @return 角色信息
     */
    WfRole selectByRoleCode(@Param("roleCode") String roleCode);

    /**
     * 根据角色类型查询角色列表
     * 
     * @param roleType 角色类型
     * @return 角色列表
     */
    List<WfRole> selectByRoleType(@Param("roleType") String roleType);

    /**
     * 根据启用状态查询角色列表
     * 
     * @param enabled 是否启用
     * @return 角色列表
     */
    List<WfRole> selectByEnabled(@Param("enabled") Integer enabled);

    /**
     * 根据角色名称模糊查询
     * 
     * @param roleName 角色名称
     * @return 角色列表
     */
    List<WfRole> selectByRoleNameLike(@Param("roleName") String roleName);

    /**
     * 根据用户ID查询用户角色列表
     * 
     * @param userId 用户ID
     * @return 角色列表
     */
    List<WfRole> selectRolesByUserId(@Param("userId") String userId);

    /**
     * 根据用户ID和部门ID查询用户在指定部门的角色列表
     * 
     * @param userId 用户ID
     * @param deptId 部门ID
     * @return 角色列表
     */
    List<WfRole> selectRolesByUserIdAndDeptId(@Param("userId") String userId, 
                                             @Param("deptId") String deptId);

    /**
     * 根据权限ID查询拥有该权限的角色列表
     * 
     * @param permissionId 权限ID
     * @return 角色列表
     */
    List<WfRole> selectRolesByPermissionId(@Param("permissionId") String permissionId);

    /**
     * 查询角色统计信息
     * 
     * @return 统计信息
     */
    List<java.util.Map<String, Object>> selectRoleStatistics();

    /**
     * 批量更新角色状态
     * 
     * @param roleIds 角色ID列表
     * @param enabled 启用状态
     * @param updatedBy 更新人
     * @return 更新行数
     */
    int batchUpdateRoleStatus(@Param("roleIds") List<String> roleIds, 
                             @Param("enabled") Integer enabled, 
                             @Param("updatedBy") String updatedBy);

    /**
     * 检查角色编码是否存在
     * 
     * @param roleCode 角色编码
     * @param excludeId 排除的角色ID
     * @return 存在数量
     */
    int checkRoleCodeExists(@Param("roleCode") String roleCode, 
                           @Param("excludeId") String excludeId);

    /**
     * 检查角色名称是否存在
     * 
     * @param roleName 角色名称
     * @param excludeId 排除的角色ID
     * @return 存在数量
     */
    int checkRoleNameExists(@Param("roleName") String roleName, 
                           @Param("excludeId") String excludeId);
}