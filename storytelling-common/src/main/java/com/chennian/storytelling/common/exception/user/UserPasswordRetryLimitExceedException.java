package com.chennian.storytelling.common.exception.user;

/**
 * 用户错误最大次数异常类
 *
 * @author by chennian
 * @date 2023/3/22 13:43
 */
public class UserPasswordRetryLimitExceedException extends UserException {
    private static final long serialVersionUID = 1L;

    public UserPasswordRetryLimitExceedException(int retryLimitCount, int lockTime) {
        super("user.password.retry.limit.exceed", new Object[]{retryLimitCount, lockTime});
    }
}
