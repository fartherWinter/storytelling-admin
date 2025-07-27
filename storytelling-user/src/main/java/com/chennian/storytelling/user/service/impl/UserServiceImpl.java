package com.chennian.storytelling.user.service.impl;

import cn.hutool.core.util.StrUtil;
import cn.hutool.crypto.digest.BCrypt;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.chennian.storytelling.user.entity.User;
import com.chennian.storytelling.user.entity.UserRole;
import com.chennian.storytelling.user.mapper.UserMapper;
import com.chennian.storytelling.user.mapper.UserRoleMapper;
import com.chennian.storytelling.user.service.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

/**
 * 用户服务实现类
 * 
 * @author chennian
 * @since 2024-01-01
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class UserServiceImpl extends ServiceImpl<UserMapper, User> implements UserService {

    private final UserMapper userMapper;
    private final UserRoleMapper userRoleMapper;

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean register(User user) {
        // 检查用户名是否存在
        if (isUsernameExists(user.getUsername(), null)) {
            throw new RuntimeException("用户名已存在");
        }
        
        // 检查邮箱是否存在
        if (StrUtil.isNotBlank(user.getEmail()) && isEmailExists(user.getEmail(), null)) {
            throw new RuntimeException("邮箱已存在");
        }
        
        // 检查手机号是否存在
        if (StrUtil.isNotBlank(user.getPhone()) && isPhoneExists(user.getPhone(), null)) {
            throw new RuntimeException("手机号已存在");
        }
        
        // 加密密码
        user.setPassword(BCrypt.hashpw(user.getPassword()));
        
        // 设置默认值
        if (user.getStatus() == null) {
            user.setStatus(1); // 默认启用
        }
        if (user.getUserType() == null) {
            user.setUserType(0); // 默认普通用户
        }
        if (user.getLoginCount() == null) {
            user.setLoginCount(0);
        }
        
        return save(user);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public User login(String username, String password, String loginIp) {
        User user = getUserByUsername(username);
        if (user == null) {
            throw new RuntimeException("用户不存在");
        }
        
        if (user.getStatus() == 0) {
            throw new RuntimeException("用户已被禁用");
        }
        
        if (!BCrypt.checkpw(password, user.getPassword())) {
            throw new RuntimeException("密码错误");
        }
        
        // 更新登录信息
        updateLastLoginInfo(user.getUserId(), loginIp);
        
        // 清除密码信息
        user.setPassword(null);
        
        return user;
    }

    @Override
    public User getUserByUsername(String username) {
        return userMapper.selectByUsername(username);
    }

    @Override
    public User getUserByEmail(String email) {
        return userMapper.selectByEmail(email);
    }

    @Override
    public User getUserByPhone(String phone) {
        return userMapper.selectByPhone(phone);
    }

    @Override
    public IPage<User> getUserPage(Page<User> page, String username, String nickname, 
                                  String email, String phone, Integer status, Integer userType) {
        return userMapper.selectUserPage(page, username, nickname, email, phone, status, userType);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean createUser(User user, List<Long> roleIds) {
        // 检查用户名是否存在
        if (isUsernameExists(user.getUsername(), null)) {
            throw new RuntimeException("用户名已存在");
        }
        
        // 检查邮箱是否存在
        if (StrUtil.isNotBlank(user.getEmail()) && isEmailExists(user.getEmail(), null)) {
            throw new RuntimeException("邮箱已存在");
        }
        
        // 检查手机号是否存在
        if (StrUtil.isNotBlank(user.getPhone()) && isPhoneExists(user.getPhone(), null)) {
            throw new RuntimeException("手机号已存在");
        }
        
        // 加密密码
        user.setPassword(BCrypt.hashpw(user.getPassword()));
        
        // 设置默认值
        if (user.getStatus() == null) {
            user.setStatus(1);
        }
        if (user.getUserType() == null) {
            user.setUserType(0);
        }
        if (user.getLoginCount() == null) {
            user.setLoginCount(0);
        }
        
        // 保存用户
        boolean result = save(user);
        
        // 分配角色
        if (result && !CollectionUtils.isEmpty(roleIds)) {
            assignUserRoles(user.getUserId(), roleIds);
        }
        
        return result;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean updateUser(User user) {
        User existUser = getById(user.getUserId());
        if (existUser == null) {
            throw new RuntimeException("用户不存在");
        }
        
        // 检查用户名是否存在
        if (!existUser.getUsername().equals(user.getUsername()) && 
            isUsernameExists(user.getUsername(), user.getUserId())) {
            throw new RuntimeException("用户名已存在");
        }
        
        // 检查邮箱是否存在
        if (StrUtil.isNotBlank(user.getEmail()) && 
            !user.getEmail().equals(existUser.getEmail()) && 
            isEmailExists(user.getEmail(), user.getUserId())) {
            throw new RuntimeException("邮箱已存在");
        }
        
        // 检查手机号是否存在
        if (StrUtil.isNotBlank(user.getPhone()) && 
            !user.getPhone().equals(existUser.getPhone()) && 
            isPhoneExists(user.getPhone(), user.getUserId())) {
            throw new RuntimeException("手机号已存在");
        }
        
        // 不允许修改密码
        user.setPassword(null);
        
        return updateById(user);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean deleteUser(Long userId) {
        // 删除用户角色关联
        userRoleMapper.deleteByUserId(userId);
        
        // 删除用户
        return removeById(userId);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean batchDeleteUsers(List<Long> userIds) {
        // 删除用户角色关联
        userRoleMapper.batchDeleteByUserIds(userIds);
        
        // 删除用户
        return removeByIds(userIds);
    }

    @Override
    public boolean updateUserStatus(Long userId, Integer status) {
        User user = new User();
        user.setUserId(userId);
        user.setStatus(status);
        return updateById(user);
    }

    @Override
    public boolean batchUpdateUserStatus(List<Long> userIds, Integer status) {
        return userMapper.batchUpdateStatus(userIds, status, "system") > 0;
    }

    @Override
    public boolean resetPassword(Long userId, String newPassword) {
        User user = new User();
        user.setUserId(userId);
        user.setPassword(BCrypt.hashpw(newPassword));
        return updateById(user);
    }

    @Override
    public boolean changePassword(Long userId, String oldPassword, String newPassword) {
        User user = getById(userId);
        if (user == null) {
            throw new RuntimeException("用户不存在");
        }
        
        if (!BCrypt.checkpw(oldPassword, user.getPassword())) {
            throw new RuntimeException("原密码错误");
        }
        
        user.setPassword(BCrypt.hashpw(newPassword));
        return updateById(user);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean assignUserRoles(Long userId, List<Long> roleIds) {
        // 删除原有角色关联
        userRoleMapper.deleteByUserId(userId);
        
        // 添加新的角色关联
        if (!CollectionUtils.isEmpty(roleIds)) {
            List<UserRole> userRoles = roleIds.stream()
                .map(roleId -> {
                    UserRole userRole = new UserRole();
                    userRole.setUserId(userId);
                    userRole.setRoleId(roleId);
                    userRole.setCreateTime(LocalDateTime.now());
                    userRole.setCreateBy("system");
                    return userRole;
                })
                .collect(Collectors.toList());
            
            return userRoleMapper.batchInsert(userRoles) > 0;
        }
        
        return true;
    }

    @Override
    public List<Long> getUserRoleIds(Long userId) {
        return userRoleMapper.selectRoleIdsByUserId(userId);
    }

    @Override
    public boolean isUsernameExists(String username, Long excludeUserId) {
        LambdaQueryWrapper<User> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(User::getUsername, username);
        if (excludeUserId != null) {
            wrapper.ne(User::getUserId, excludeUserId);
        }
        return count(wrapper) > 0;
    }

    @Override
    public boolean isEmailExists(String email, Long excludeUserId) {
        LambdaQueryWrapper<User> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(User::getEmail, email);
        if (excludeUserId != null) {
            wrapper.ne(User::getUserId, excludeUserId);
        }
        return count(wrapper) > 0;
    }

    @Override
    public boolean isPhoneExists(String phone, Long excludeUserId) {
        LambdaQueryWrapper<User> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(User::getPhone, phone);
        if (excludeUserId != null) {
            wrapper.ne(User::getUserId, excludeUserId);
        }
        return count(wrapper) > 0;
    }

    @Override
    public boolean updateLastLoginInfo(Long userId, String loginIp) {
        return userMapper.updateLastLoginInfo(userId, loginIp) > 0;
    }
}