package com.chennian.storytelling.dao.workflow;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.chennian.storytelling.bean.model.workflow.WfUserRole;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 用户角色关联Mapper接口
 * 
 * @author storytelling
 * @date 2024-01-01
 */
@Mapper
public interface WfUserRoleMapper extends BaseMapper<WfUserRole> {

    /**
     * 根据用户ID查询角色ID列表
     * 
     * @param userId 用户ID
     * @return 角色ID列表
     */
    List<String> selectRoleIdsByUserId(@Param("userId") String userId);

    /**
     * 根据角色ID查询用户ID列表
     * 
     * @param roleId 角色ID
     * @return 用户ID列表
     */
    List<String> selectUserIdsByRoleId(@Param("roleId") String roleId);

    /**
     * 根据用户ID和部门ID查询角色ID列表
     * 
     * @param userId 用户ID
     * @param deptId 部门ID
     * @return 角色ID列表
     */
    List<String> selectRoleIdsByUserIdAndDeptId(@Param("userId") String userId, 
                                               @Param("deptId") String deptId);

    /**
     * 根据用户ID查询启用的角色ID列表
     * 
     * @param userId 用户ID
     * @return 角色ID列表
     */
    List<String> selectEnabledRoleIdsByUserId(@Param("userId") String userId);

    /**
     * 根据用户ID删除用户角色关联
     * 
     * @param userId 用户ID
     * @return 删除行数
     */
    int deleteByUserId(@Param("userId") String userId);

    /**
     * 根据角色ID删除用户角色关联
     * 
     * @param roleId 角色ID
     * @return 删除行数
     */
    int deleteByRoleId(@Param("roleId") String roleId);

    /**
     * 根据用户ID和角色ID删除关联
     * 
     * @param userId 用户ID
     * @param roleId 角色ID
     * @return 删除行数
     */
    int deleteByUserIdAndRoleId(@Param("userId") String userId, 
                               @Param("roleId") String roleId);

    /**
     * 根据部门ID删除用户角色关联
     * 
     * @param deptId 部门ID
     * @return 删除行数
     */
    int deleteByDeptId(@Param("deptId") String deptId);

    /**
     * 批量插入用户角色关联
     * 
     * @param userRoles 用户角色关联列表
     * @return 插入行数
     */
    int batchInsertUserRoles(@Param("userRoles") List<WfUserRole> userRoles);

    /**
     * 根据用户ID批量删除角色关联
     * 
     * @param userIds 用户ID列表
     * @return 删除行数
     */
    int batchDeleteByUserIds(@Param("userIds") List<String> userIds);

    /**
     * 根据角色ID批量删除用户关联
     * 
     * @param roleIds 角色ID列表
     * @return 删除行数
     */
    int batchDeleteByRoleIds(@Param("roleIds") List<String> roleIds);

    /**
     * 检查用户角色关联是否存在
     * 
     * @param userId 用户ID
     * @param roleId 角色ID
     * @param deptId 部门ID
     * @return 存在数量
     */
    int checkUserRoleExists(@Param("userId") String userId, 
                           @Param("roleId") String roleId, 
                           @Param("deptId") String deptId);

    /**
     * 更新用户角色状态
     * 
     * @param userId 用户ID
     * @param roleId 角色ID
     * @param enabled 启用状态
     * @param updatedBy 更新人
     * @return 更新行数
     */
    int updateUserRoleStatus(@Param("userId") String userId, 
                            @Param("roleId") String roleId, 
                            @Param("enabled") Integer enabled, 
                            @Param("updatedBy") String updatedBy);

    /**
     * 查询即将过期的用户角色关联
     * 
     * @param days 天数
     * @return 用户角色关联列表
     */
    List<WfUserRole> selectExpiringUserRoles(@Param("days") Integer days);

    /**
     * 查询已过期的用户角色关联
     * 
     * @return 用户角色关联列表
     */
    List<WfUserRole> selectExpiredUserRoles();

    /**
     * 查询用户角色关联统计信息
     * 
     * @return 统计信息
     */
    List<java.util.Map<String, Object>> selectUserRoleStatistics();

    /**
     * 根据用户ID联表查询用户角色详细信息
     * 
     * @param userId 用户ID
     * @return 用户角色详细信息列表
     */
    List<java.util.Map<String, Object>> selectUserRoleDetailsByUserId(@Param("userId") String userId);
}