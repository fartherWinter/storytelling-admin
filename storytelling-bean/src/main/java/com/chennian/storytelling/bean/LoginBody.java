package com.chennian.storytelling.bean;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

/**
 * @author by chennian
 * @date 2025/5/6.
 */
@Data
@Schema(description = "登录对象")
public class LoginBody {
    /**
     * 用户名
     */
    @Schema(description = "用户名")
    private String userName;

    /**
     * 用户密码
     */
    @Schema(description = "用户密码")
    private String passWord;

    /**
     * 验证码
     */
    @Schema(description = "验证码")
    private String code;

    /**
     * 唯一标识
     */
    @Schema(description = "唯一标识")
    private String uuid;
}

