package com.chennian.storytelling.common.exception.user;

/**
 * 验证码错误异常类
 *
 * @author by chennian
 * @date 2023/3/22 13:42
 */
public class CaptchaException extends UserException {
    private static final long serialVersionUID = 1L;

    public CaptchaException() {
        super("user.jcaptcha.error", null);
    }
}
