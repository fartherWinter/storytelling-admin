package com.chennian.storytelling.common.param;

import lombok.Data;

/**
 * @author by chennian
 * @date 2023/5/5 9:07
 */
@Data
public class WxPhoneNumberParam {
    /**
     * 接口调用凭证
     */
    private String access_token;
    /**
     * 手机号获取凭证
     */
    private String code;
}
