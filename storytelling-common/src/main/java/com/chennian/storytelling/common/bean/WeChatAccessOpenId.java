package com.chennian.storytelling.common.bean;

import lombok.Data;

import java.io.Serializable;

/**
 * @author by chennian
 * @date 2023/3/30 15:00
 */
@Data
public class WeChatAccessOpenId implements Serializable {
    private String session_key;
    private String unionid;
    private String errmsg;
    private String openid;
    private Integer errcode;
}
