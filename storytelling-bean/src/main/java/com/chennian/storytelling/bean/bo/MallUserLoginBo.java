package com.chennian.storytelling.bean.bo;

import lombok.Data;

/**
 * @author by chennian
 * @date 2025/5/25.
 */
@Data
public class MallUserLoginBo {
    /**
     * jsCode
     */
    private String jsCode;
    /**
     * data
     */
    private String encryptedData;
    /**
     * sessionKey
     */
    private String sessionKey;
    /**
     * iv
     */
    private String iv;
    /**
     * openid
     */
    private String openid;
    /**
     * mobile
     */
    private String mobile;
    /**
     * unionid
     */
    private String unionid;
    /**
     * uuid
     */
    private String uuid;
    /**
     * code
     */
    private String code;
    /**
     * wrCode
     */
    private String wrCode;
}
