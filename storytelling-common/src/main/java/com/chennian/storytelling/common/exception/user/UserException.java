package com.chennian.storytelling.common.exception.user;

import com.chennian.storytelling.common.exception.base.BaseException;

/**
 * 用户信息异常类
 *
 * @author by chennian
 * @date 2023/3/22 13:40
 */
public class UserException extends BaseException {
    private static final long serialVersionUID = 1L;

    public UserException(String code, Object[] args) {
        super("user", code, args, null);
    }
}
