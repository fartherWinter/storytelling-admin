package com.chennian.storytelling.service.impl;

import cn.dev33.satoken.stp.SaTokenInfo;
import cn.dev33.satoken.stp.StpUtil;
import com.chennian.storytelling.bean.model.SysUser;
import com.chennian.storytelling.common.bean.Token;
import com.chennian.storytelling.common.constant.CacheConstants;
import com.chennian.storytelling.common.constant.Constants;
import com.chennian.storytelling.common.constant.UserConstants;
import com.chennian.storytelling.common.exception.StorytellingBindException;
import com.chennian.storytelling.common.exception.user.CaptchaException;
import com.chennian.storytelling.common.exception.user.CaptchaExpireException;
import com.chennian.storytelling.common.redis.RedisCache;
import com.chennian.storytelling.common.utils.*;
import com.chennian.storytelling.dao.SysUserMapper;
import com.chennian.storytelling.factory.AsyncFactory;
import com.chennian.storytelling.manager.AsyncManager;
import com.chennian.storytelling.service.SysLoginService;
import com.chennian.storytelling.service.SysUserService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

/**
 * @author by chennian
 * @date 2025/5/6.
 */
@Service
public class SysLoginServiceImpl implements SysLoginService {
    private SysUserService sysUserService;

    private RedisCache redisCache;

    private SysUserMapper sysUserMapper;

    public SysLoginServiceImpl(SysUserService sysUserService, RedisCache redisCache, SysUserMapper sysUserMapper) {
        this.sysUserService = sysUserService;
        this.redisCache = redisCache;
        this.sysUserMapper = sysUserMapper;
    }

    /**
     * 设置过期时间
     */
    @Value("${sa-token.timeout}")
    private Long timeout;
    /**
     * 登录验证逻辑
     */
    @Override
    public Token login(String username, String password, String code, String uuid) {
        //验证码校验
        validateCaptcha(username, code, uuid);
        SysUser sysUser = sysUserMapper.selectUserByName(username);
        if (StringUtils.isNull(sysUser)) {
            throw new StorytellingBindException("该账号不存在");
        }
        if (sysUser.getStatus().equals(UserConstants.NORMAL)) {
            throw new StorytellingBindException("当前账户无效");
        }
        if (!password.equals(sysUser.getPassword())) {
            throw new StorytellingBindException("密码错误");
        }
        //更新当前用户的登录状态
        sysUser.setLoginDate(DateUtils.currentTime());
        sysUserMapper.updateById(sysUser);
        StpUtil.login(sysUser.getUserId());
//        StpUtil.login(sysUser.getUserId(),
//                SaLoginConfig.setExtra("name", "hello"));
//        Object name = StpUtil.getExtra("name");
//        System.out.println(name);
        SaTokenInfo tokenInfo = StpUtil.getTokenInfo();
        long outTime = (long) ArithUtils.add(System.currentTimeMillis(), timeout * 1000);
        Token token = new Token();
        token.setKey(tokenInfo.getTokenValue());
        token.setTimeStamp(outTime);
        return token;
    }

    /**
     * 校验验证码
     *
     * @param username 用户名
     * @param code     验证码
     * @param uuid     唯一标识
     * @return 结果
     */
    @Override
    public void validateCaptcha(String username, String code, String uuid) {
        //todo 是否关闭验证码
        String verifyKey = CacheConstants.CAPTCHA_CODE_KEY + StringUtils.nvl(uuid, "");
        String captcha = redisCache.getCacheObject(verifyKey);
        redisCache.deleteObject(verifyKey);
        if (captcha == null) {
            throw new CaptchaExpireException();
        }
        if (!code.equalsIgnoreCase(captcha)) {
            throw new CaptchaException();
        }
    }

    /**
     * 登录前置校验
     *
     * @param username 用户名
     * @param password 用户密码
     */
    @Override
    public void loginPreCheck(String username, String password) {
        if (StringUtils.isEmpty(username) || StringUtils.isEmpty(password)) {
            AsyncManager.me().execute(AsyncFactory.recordLogininfor(username, Constants.LOGIN_FAIL, MessageUtils.message("not.null")));
            throw new StorytellingBindException("用户名或密码为空");
        }
        // 密码如果不在指定范围内 错误
        if (password.length() < UserConstants.PASSWORD_MIN_LENGTH || password.length() > UserConstants.PASSWORD_MAX_LENGTH) {
            AsyncManager.me().execute(AsyncFactory.recordLogininfor(username, Constants.LOGIN_FAIL, MessageUtils.message("user.password.not.match")));
            throw new StorytellingBindException("密码如果不在指定范围内");
        }
        // 用户名不在指定范围内 错误
        if (username.length() < UserConstants.USERNAME_MIN_LENGTH || username.length() > UserConstants.USERNAME_MAX_LENGTH) {
            AsyncManager.me().execute(AsyncFactory.recordLogininfor(username, Constants.LOGIN_FAIL, MessageUtils.message("user.password.not.match")));
            throw new StorytellingBindException("用户名不在指定范围内");
        }
    }

    /**
     * 记录登录信息
     *
     * @param userId 用户ID
     */
    @Override
    public void recordLoginInfo(Long userId) {
        SysUser sysUser = new SysUser();
        sysUser.setUserId(userId);
        sysUser.setLoginIp(IpUtils.getIpAddr());
        sysUser.setLoginDate(DateUtils.getCurDate());
        sysUserMapper.updateUser(sysUser);
    }

}
