package com.chennian.storytelling.common.exception.user;

/**
 * 验证码失效异常类
 *
 * @author by chennian
 * @date 2023/3/22 13:42
 */
public class CaptchaExpireException extends UserException {
    private static final long serialVersionUID = 1L;

    public CaptchaExpireException() {
        super("user.jcaptcha.expire", null);
    }
}
