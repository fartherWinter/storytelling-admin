package com.chennian.storytelling.user.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.chennian.storytelling.user.entity.User;

import java.util.List;

/**
 * 用户服务接口
 * 
 * @author chennian
 * @since 2024-01-01
 */
public interface UserService extends IService<User> {

    /**
     * 用户注册
     * 
     * @param user 用户信息
     * @return 注册结果
     */
    boolean register(User user);

    /**
     * 用户登录
     * 
     * @param username 用户名
     * @param password 密码
     * @param loginIp 登录IP
     * @return 用户信息
     */
    User login(String username, String password, String loginIp);

    /**
     * 根据用户名查询用户
     * 
     * @param username 用户名
     * @return 用户信息
     */
    User getUserByUsername(String username);

    /**
     * 根据邮箱查询用户
     * 
     * @param email 邮箱
     * @return 用户信息
     */
    User getUserByEmail(String email);

    /**
     * 根据手机号查询用户
     * 
     * @param phone 手机号
     * @return 用户信息
     */
    User getUserByPhone(String phone);

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
    IPage<User> getUserPage(Page<User> page, String username, String nickname, 
                           String email, String phone, Integer status, Integer userType);

    /**
     * 创建用户
     * 
     * @param user 用户信息
     * @param roleIds 角色ID列表
     * @return 创建结果
     */
    boolean createUser(User user, List<Long> roleIds);

    /**
     * 更新用户信息
     * 
     * @param user 用户信息
     * @return 更新结果
     */
    boolean updateUser(User user);

    /**
     * 删除用户
     * 
     * @param userId 用户ID
     * @return 删除结果
     */
    boolean deleteUser(Long userId);

    /**
     * 批量删除用户
     * 
     * @param userIds 用户ID列表
     * @return 删除结果
     */
    boolean batchDeleteUsers(List<Long> userIds);

    /**
     * 启用/禁用用户
     * 
     * @param userId 用户ID
     * @param status 状态
     * @return 更新结果
     */
    boolean updateUserStatus(Long userId, Integer status);

    /**
     * 批量更新用户状态
     * 
     * @param userIds 用户ID列表
     * @param status 状态
     * @return 更新结果
     */
    boolean batchUpdateUserStatus(List<Long> userIds, Integer status);

    /**
     * 重置用户密码
     * 
     * @param userId 用户ID
     * @param newPassword 新密码
     * @return 重置结果
     */
    boolean resetPassword(Long userId, String newPassword);

    /**
     * 修改用户密码
     * 
     * @param userId 用户ID
     * @param oldPassword 旧密码
     * @param newPassword 新密码
     * @return 修改结果
     */
    boolean changePassword(Long userId, String oldPassword, String newPassword);

    /**
     * 分配用户角色
     * 
     * @param userId 用户ID
     * @param roleIds 角色ID列表
     * @return 分配结果
     */
    boolean assignUserRoles(Long userId, List<Long> roleIds);

    /**
     * 获取用户角色列表
     * 
     * @param userId 用户ID
     * @return 角色ID列表
     */
    List<Long> getUserRoleIds(Long userId);

    /**
     * 检查用户名是否存在
     * 
     * @param username 用户名
     * @param excludeUserId 排除的用户ID
     * @return 是否存在
     */
    boolean isUsernameExists(String username, Long excludeUserId);

    /**
     * 检查邮箱是否存在
     * 
     * @param email 邮箱
     * @param excludeUserId 排除的用户ID
     * @return 是否存在
     */
    boolean isEmailExists(String email, Long excludeUserId);

    /**
     * 检查手机号是否存在
     * 
     * @param phone 手机号
     * @param excludeUserId 排除的用户ID
     * @return 是否存在
     */
    boolean isPhoneExists(String phone, Long excludeUserId);

    /**
     * 更新用户最后登录信息
     * 
     * @param userId 用户ID
     * @param loginIp 登录IP
     * @return 更新结果
     */
    boolean updateLastLoginInfo(Long userId, String loginIp);
}