package com.chennian.storytelling.common.bean;

import lombok.Data;

import java.io.Serializable;

/**
 * access_token实体
 *
 * @author by chennian
 * @date 2023/3/30 14:50
 */
@Data
public class WeChatAccessToken implements Serializable {
    private String access_token;
    private Long expires_in;
    private String openid;
}
