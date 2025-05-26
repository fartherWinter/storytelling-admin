package com.chennian.storytelling.security.common.bo;

import lombok.Data;

/**
 * @author by chennian
 * @date 2023/3/19 19:56
 */
@Data
public class TokenInfoBO {
    /**
     * 保存在token信息里面的用户信息
     */
    private UserInfoInTokenBO userInfoInToken;

    private String accessToken;

    private String refreshToken;

    /**
     * 在多少秒后过期
     */
    private Integer expiresIn;
}
