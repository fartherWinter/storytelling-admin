package com.chennian.storytelling.common.exception.user;

/**
 * 黑名单IP异常类
 *
 * @author by chennian
 * @date 2023/3/22 13:41
 */
public class BlackListException extends UserException {
    private static final long serialVersionUID = 1L;

    public BlackListException() {
        super("login.blocked", null);
    }
}
