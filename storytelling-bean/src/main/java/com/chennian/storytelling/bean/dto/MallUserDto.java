package com.chennian.storytelling.bean.dto;

import lombok.Data;

/**
 * @author by chennian
 * @date 2025/5/25.
 */

@Data
public class MallUserDto {
    /**
     * id
     */
    private Integer id;
    /**
     * uid
     */
    private String uid;
    /**
     * openId
     */
    private String openid;
    /**
     * unionId
     */
    private String unionid;
    /**
     * 姓名
     */
    private String fullName;
    /**
     * 头像
     */
    private String headImg;
    /**
     * 昵称
     */
    private String nickName;
    /**
     * 积分
     */
    private Double cash;
}
