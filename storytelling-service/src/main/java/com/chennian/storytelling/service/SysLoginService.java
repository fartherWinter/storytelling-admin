package com.chennian.storytelling.service;

import com.chennian.storytelling.common.bean.Token;

/**
 * @author by chennian
 * @date 2025/5/6.
 */
public interface SysLoginService {
    /**
     * 登录验证
     *
     * @param username 用户名
     * @param password 密码
     * @param code     验证码
     * @param uuid     唯一标识
     * @return 结果
     */
    Token login(String username, String password, String code, String uuid);

    /**
     * 校验验证码
     *
     * @param username 用户名
     * @param code     验证码
     * @param uuid     唯一标识
     */
    void validateCaptcha(String username, String code, String uuid);

    /**
     * 登录前置校验
     *
     * @param username 用户名
     * @param password 用户密码
     */
    void loginPreCheck(String username, String password);

    /**
     * 记录登录信息
     *
     * @param userId 用户ID
     */
    void recordLoginInfo(Long userId);
}
