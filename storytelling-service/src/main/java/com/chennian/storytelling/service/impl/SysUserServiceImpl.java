package com.chennian.storytelling.service.impl;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.chennian.security.admin.util.SecurityUtils;
import com.chennian.storytelling.bean.bo.SysUserSearchBo;
import com.chennian.storytelling.bean.dto.AppProfileDto;
import com.chennian.storytelling.bean.model.SysUser;
import com.chennian.storytelling.bean.model.SysUserPost;
import com.chennian.storytelling.bean.model.SysUserRole;
import com.chennian.storytelling.bean.param.UserProfileParam;
import com.chennian.storytelling.common.constant.UserConstants;
import com.chennian.storytelling.common.exception.ServiceException;
import com.chennian.storytelling.common.exception.StorytellingBindException;
import com.chennian.storytelling.common.redis.RedisCache;
import com.chennian.storytelling.common.utils.SpringUtils;
import com.chennian.storytelling.common.utils.StringUtils;
import com.chennian.storytelling.dao.*;
import com.chennian.storytelling.service.SysUserService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

/**
* @author chen
* @description 针对表【sys_user(用户信息表)】的数据库操作Service实现
* @createDate 2025-05-06 19:11:49
*/
@Service
public class SysUserServiceImpl extends ServiceImpl<SysUserMapper, SysUser>
    implements SysUserService{

    private final SysUserMapper sysUserMapper;



    private final SysUserPostMapper sysUserPostMapper;

    private final SysUserRoleMapper sysUserRoleMapper;
    private final RedisCache redisCache;


    public SysUserServiceImpl (SysUserMapper sysUserMapper, SysUserPostMapper sysUserPostMapper, SysUserRoleMapper sysUserRoleMapper,
                               RedisCache redisCache) {
        this.sysUserMapper = sysUserMapper;

        this.sysUserPostMapper = sysUserPostMapper;
        this.sysUserRoleMapper = sysUserRoleMapper;
        this.redisCache = redisCache;
    }

    private static final String[] MOBILE_UNREAL = {"170", "162", "165", "171", "167"};

    @Override
    public List<SysUser> selectAllocatedList(SysUser user) {
        return List.of();
    }

    @Override
    public List<SysUser> selectUnallocatedList(SysUser user) {
        return List.of();
    }

    @Override
    public SysUser selectUserByUserName(String userName) {
        return null;
    }

    /**
     * 通过用户ID查询用户
     *
     * @param userId 用户ID
     * @return 用户对象信息
     */
    @Override
    public SysUser selectUserById(Long userId) {
        SysUser user = sysUserMapper.selectUserById(userId);
        return sysUserMapper.selectUserById(userId);
    }

    @Override
    public String selectUserRoleGroup(String userName) {
        return null;
    }

    @Override
    public String selectUserPostGroup(String userName) {
        return null;
    }

    /**
     * 校验用户名称是否唯一
     *
     * @param sysUser 用户信息
     * @return 结果
     */
    @Override
    public boolean checkUserNameUnique(SysUser sysUser) {
        Long userId = StringUtils.isNull(sysUser.getUserId()) ? -1L : sysUser.getUserId();
        SysUser info = sysUserPostMapper.checkUserNameUnique(sysUser.getUserName());
        if (StringUtils.isNotNull(info) && info.getUserId().longValue() != userId.longValue()) {
            return UserConstants.NOT_UNIQUE;
        }
        return UserConstants.UNIQUE;
    }

    /**
     * 校验手机号码是否唯一
     *
     * @param user 用户信息
     * @return
     */
    @Override
    public boolean checkPhoneUnique(SysUser user) {
        Long userId = StringUtils.isNull(user.getUserId()) ? -1L : user.getUserId();
        SysUser info = sysUserMapper.checkPhoneUnique(user.getPhoneNumber());
        if (StringUtils.isNotNull(info) && info.getUserId().longValue() != userId.longValue()) {
            return UserConstants.NOT_UNIQUE;
        }
        return UserConstants.UNIQUE;

    }

    /**
     * 校验email是否唯一
     *
     * @param user 用户信息
     * @return
     */
    @Override
    public boolean checkEmailUnique(SysUser user) {
        Long userId = StringUtils.isNull(user.getUserId()) ? -1L : user.getUserId();
        SysUser info = sysUserMapper.checkEmailUnique(user.getEmail());
        if (StringUtils.isNotNull(info) && info.getUserId().longValue() != userId.longValue()) {
            return UserConstants.NOT_UNIQUE;
        }
        return UserConstants.UNIQUE;
    }

    /**
     * 校验用户是否允许操作
     *
     * @param user 用户信息
     */
    @Override
    public void checkUserAllowed(SysUser user) {
        if (StringUtils.isNotNull(user.getUserId()) && user.isAdmin()) {
            throw new ServiceException("不允许操作超级管理员用户");
        }
    }

    /**
     * 校验用户是否有数据权限
     *
     * @param userId 用户id
     */
    @Override
    public void checkUserDataScope(Long userId) {
        if (!SysUser.isAdmin(1L)) {
            SysUser user = new SysUser();
            user.setUserId(userId);
            List<SysUser> users = SpringUtils.getAopProxy(this).selectUserList(user);
            if (StringUtils.isEmpty(users)) {
                throw new ServiceException("没有权限访问用户数据！");
            }
        }
    }

    /**
     * 新增保存用户信息
     *
     * @param user 用户信息
     * @return 结果
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public int insertUser(SysUser user) {
        //新增用户信息
        int rows = sysUserMapper.insertUser(user);
        //新增用户岗位关联
        insertUserPost(user);
        //新增用户与角色管理
        insertUserRole(user);
        return rows;
    }

    /**
     * 新增用户角色信息
     *
     * @param user 用户对象
     */
    private void insertUserRole(SysUser user) {
        this.insertUserRole(user.getUserId(), user.getRoleId());
    }

    /**
     * 新增用户角色信息
     *
     * @param userId  用户ID
     * @param roleIds 角色组
     */
    public void insertUserRole(Long userId, Long[] roleIds) {
        if (StringUtils.isNotEmpty(roleIds)) {
            // 新增用户与角色管理
            List<SysUserRole> list = new ArrayList<SysUserRole>(roleIds.length);
            for (Long roleId : roleIds) {
                SysUserRole ur = new SysUserRole();
                ur.setUserId(userId);
                ur.setRoleId(roleId);
                list.add(ur);
            }
            sysUserRoleMapper.batchUserRole(list);
        }
    }

    /**
     * 新增用户角色信息
     *
     * @param userId 用户ID
     * @param roleId 角色组
     */
    public void insertUserRole(Long userId, Long roleId) {
        SysUserRole userRole = new SysUserRole();
        userRole.setUserId(userId);
        userRole.setRoleId(roleId);
        sysUserRoleMapper.insert(userRole);
    }

    /**
     * 新增用户岗位信息
     *
     * @param user 用户对象
     */
    private void insertUserPost(SysUser user) {
        SysUserPost userPost=new SysUserPost();
        userPost.setUserId(user.getUserId());
        userPost.setPostId(user.getPostId());
        sysUserPostMapper.insert(userPost);
//        Long[] posts = user.getPostIds();
//        if (StringUtils.isNotEmpty(posts)) {
//            // 新增用户与岗位管理
//            List<SysUserPost> list = new ArrayList<SysUserPost>(posts.length);
//            for (Long postId : posts) {
//                SysUserPost up = new SysUserPost();
//                up.setUserId(user.getUserId());
//                up.setPostId(postId);
//                list.add(up);
//            }
//            sysUserPostMapper.batchUserPost(list);
//        }
    }

    @Override
    public boolean registerUser(SysUser sysUser) {
        return sysUserMapper.insertUser(sysUser) > 0;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public int updateUser(SysUser user) {
        Long userId = user.getUserId();
        sysUserRoleMapper.deleteUserRoleByUserId(userId);
        insertUserRole(user);
        sysUserPostMapper.deleteUserPostByUserId(userId);
        insertUserPost(user);
        return sysUserMapper.updateUser(user);
    }

    @Override
    public void insertUserAuth(Long userId, Long[] roleIds) {

    }

    /**
     * 修改用户状态
     *
     * @param user 用户信息
     * @return
     */
    @Override
    public int updateUserStatus(SysUser user) {
        return sysUserMapper.updateUser(user);
    }

    /**
     * 修改用户的基本信息
     *
     * @param user 用户信息
     * @return
     */
    @Override
    public int updateUserProfile(SysUser user) {
        return sysUserMapper.updateUser(user);
    }

    @Override
    public boolean updateUserAvatar(String userName, String avatar) {
        return false;
    }


    /**
     * 重置用户密码
     */
    @Override
    public int resetUserPwd(String userName, String password) {
        return sysUserMapper.resetUserPwd(userName, password);
    }

    @Override
    public int deleteUserById(Long userId) {
        return 0;
    }

    /**
     * 批量删除用户信息
     *
     * @param userIds 需要删除的用户ID
     * @return
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public int deleteUserByIds(Long[] userIds) {
        //删除用户与角色关联
        sysUserRoleMapper.deleteUserRole(userIds);
        //删除用户与岗位关联
        sysUserPostMapper.deleteUserPost(userIds);
        return sysUserMapper.deleteUserByIds(userIds);
    }

    @Override
    public String importUser(List<SysUser> userList, Boolean isUpdateSupport, String operName) {
        return null;
    }


    @Override
    public List<SysUser> selectUserList(SysUser user) {
        return null;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public IPage<SysUser> findByPage(IPage<SysUser> page, SysUserSearchBo sysUserSearchBo) {
        return sysUserMapper.findByPage(page, sysUserSearchBo);
    }

    /**
     * 获取用户信息
     */
    @Override
    public AppProfileDto getProfileInfo(int userId) {
        AppProfileDto appProfileDto = sysUserMapper.getProfileInfo(userId);
        return appProfileDto;
    }

    /**
     * 修改用户密码信息
     */
    @Override
    public Integer revisePwd(Integer userId, String oldPassword, String newPassword) {
        SysUser user = sysUserMapper.selectById(userId);
        if (StringUtils.isNull(user)) {
            throw new StorytellingBindException("用户状态失效，请重新登陆");
        }
        if (!user.getPassword().equals(oldPassword)) {
            throw new StorytellingBindException("旧密码错误");
        }
        user.setPassword(newPassword);
        return sysUserMapper.updateUser(user);
    }

    /**
     * 修改用户信息
     */
    @Override
    public Integer reviseUserProfile(UserProfileParam userProfileParam) {
        SysUser user = sysUserMapper.selectById(Long.valueOf(SecurityUtils.getUser()));
        user.setUserName(userProfileParam.getUserName());
        user.setPhoneNumber(userProfileParam.getPhoneNumber());
        user.setEmail(userProfileParam.getEmail());
        return sysUserMapper.updateUser(user);
    }
}




