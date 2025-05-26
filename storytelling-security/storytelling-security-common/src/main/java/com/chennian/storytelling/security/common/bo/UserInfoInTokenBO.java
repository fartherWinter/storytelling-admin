package com.chennian.storytelling.security.common.bo;

import lombok.Data;

import java.util.Set;

/**
 * @author by chennian
 * @date 2023/3/19 19:57
 */
@Data
public class UserInfoInTokenBO {
    /**
     * 用户在自己系统的用户id
     */
    private String userId;

    /**
     * 租户id (商家id)
     */
    private Long shopId;

    /**
     * 昵称
     */
    private String nickName;

    /**
     * 系统类型
     */
    private Integer sysType;

    /**
     * 是否是管理员
     */
    private Integer isAdmin;

    private String bizUserId;

    /**
     * 权限列表
     */
    private Set<String> perms;

    /**
     * 状态 1 正常 0 无效
     */
    private Boolean enabled;

    /**
     * 其他Id
     */
    private Long otherId;
}
