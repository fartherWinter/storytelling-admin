package com.chennian.storytelling.user.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.chennian.storytelling.user.entity.User;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 用户Mapper接口
 * 
 * @author chennian
 * @since 2024-01-01
 */
@Mapper
public interface UserMapper extends BaseMapper<User> {

    /**
     * 根据用户名查询用户
     * 
     * @param username 用户名
     * @return 用户信息
     */
    User selectByUsername(@Param("username") String username);

    /**
     * 根据邮箱查询用户
     * 
     * @param email 邮箱
     * @return 用户信息
     */
    User selectByEmail(@Param("email") String email);

    /**
     * 根据手机号查询用户
     * 
     * @param phone 手机号
     * @return 用户信息
     */
    User selectByPhone(@Param("phone") String phone);

    /**
     * 分页查询用户列表
     * 
     * @param page 分页参数
     * @param username 用户名（模糊查询）
     * @param nickname 昵称（模糊查询）
     * @param email 邮箱（模糊查询）
     * @param phone 手机号（模糊查询）
     * @param status 状态
     * @param userType 用户类型
     * @return 用户分页列表
     */
    IPage<User> selectUserPage(Page<User> page, 
                              @Param("username") String username,
                              @Param("nickname") String nickname,
                              @Param("email") String email,
                              @Param("phone") String phone,
                              @Param("status") Integer status,
                              @Param("userType") Integer userType);

    /**
     * 根据角色ID查询用户列表
     * 
     * @param roleId 角色ID
     * @return 用户列表
     */
    List<User> selectUsersByRoleId(@Param("roleId") Long roleId);

    /**
     * 更新用户最后登录信息
     * 
     * @param userId 用户ID
     * @param loginIp 登录IP
     * @return 更新结果
     */
    int updateLastLoginInfo(@Param("userId") Long userId, @Param("loginIp") String loginIp);

    /**
     * 批量更新用户状态
     * 
     * @param userIds 用户ID列表
     * @param status 状态
     * @param updateBy 更新者
     * @return 更新结果
     */
    int batchUpdateStatus(@Param("userIds") List<Long> userIds, 
                         @Param("status") Integer status, 
                         @Param("updateBy") String updateBy);
}