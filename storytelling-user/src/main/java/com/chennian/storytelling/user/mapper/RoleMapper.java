package com.chennian.storytelling.user.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.chennian.storytelling.user.entity.Role;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 角色Mapper接口
 * 
 * @author chennian
 * @since 2024-01-01
 */
@Mapper
public interface RoleMapper extends BaseMapper<Role> {

    /**
     * 根据角色编码查询角色
     * 
     * @param roleCode 角色编码
     * @return 角色信息
     */
    Role selectByRoleCode(@Param("roleCode") String roleCode);

    /**
     * 根据用户ID查询角色列表
     * 
     * @param userId 用户ID
     * @return 角色列表
     */
    List<Role> selectRolesByUserId(@Param("userId") Long userId);

    /**
     * 分页查询角色列表
     * 
     * @param page 分页参数
     * @param roleName 角色名称（模糊查询）
     * @param roleCode 角色编码（模糊查询）
     * @param status 状态
     * @return 角色分页列表
     */
    IPage<Role> selectRolePage(Page<Role> page, 
                              @Param("roleName") String roleName,
                              @Param("roleCode") String roleCode,
                              @Param("status") Integer status);

    /**
     * 查询所有可用角色
     * 
     * @return 角色列表
     */
    List<Role> selectAvailableRoles();

    /**
     * 批量更新角色状态
     * 
     * @param roleIds 角色ID列表
     * @param status 状态
     * @param updateBy 更新者
     * @return 更新结果
     */
    int batchUpdateStatus(@Param("roleIds") List<Long> roleIds, 
                         @Param("status") Integer status, 
                         @Param("updateBy") String updateBy);
}