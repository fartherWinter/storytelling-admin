package com.chennian.storytelling.user.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.chennian.storytelling.user.entity.UserRole;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 用户角色关联Mapper接口
 * 
 * @author chennian
 * @since 2024-01-01
 */
@Mapper
public interface UserRoleMapper extends BaseMapper<UserRole> {

    /**
     * 根据用户ID删除用户角色关联
     * 
     * @param userId 用户ID
     * @return 删除结果
     */
    int deleteByUserId(@Param("userId") Long userId);

    /**
     * 根据角色ID删除用户角色关联
     * 
     * @param roleId 角色ID
     * @return 删除结果
     */
    int deleteByRoleId(@Param("roleId") Long roleId);

    /**
     * 根据用户ID查询角色ID列表
     * 
     * @param userId 用户ID
     * @return 角色ID列表
     */
    List<Long> selectRoleIdsByUserId(@Param("userId") Long userId);

    /**
     * 根据角色ID查询用户ID列表
     * 
     * @param roleId 角色ID
     * @return 用户ID列表
     */
    List<Long> selectUserIdsByRoleId(@Param("roleId") Long roleId);

    /**
     * 批量插入用户角色关联
     * 
     * @param userRoles 用户角色关联列表
     * @return 插入结果
     */
    int batchInsert(@Param("userRoles") List<UserRole> userRoles);

    /**
     * 检查用户角色关联是否存在
     * 
     * @param userId 用户ID
     * @param roleId 角色ID
     * @return 是否存在
     */
    boolean existsUserRole(@Param("userId") Long userId, @Param("roleId") Long roleId);

    /**
     * 批量删除用户角色关联
     * 
     * @param userIds 用户ID列表
     * @return 删除结果
     */
    int batchDeleteByUserIds(@Param("userIds") List<Long> userIds);

    /**
     * 批量删除角色用户关联
     * 
     * @param roleIds 角色ID列表
     * @return 删除结果
     */
    int batchDeleteByRoleIds(@Param("roleIds") List<Long> roleIds);
}