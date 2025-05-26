package com.chennian.storytelling.common.exception.user;

/**
 * 用户不存在异常类
 *
 * @author by chennian
 * @date 2023/3/22 13:42
 */
public class UserNotExistsException extends UserException {
    private static final long serialVersionUID = 1L;

    public UserNotExistsException() {
        super("user.not.exists", null);
    }
}
