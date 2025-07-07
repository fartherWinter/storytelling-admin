package com.chennian.storytelling.bean.model.mall;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

import java.io.Serializable;
import java.util.Date;

@Data
@EqualsAndHashCode(callSuper = false)
@ToString
@TableName("mall_user")
public class MallUser implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 用户ID, 分片键
     */
    @TableId(value = "id", type = IdType.ASSIGN_ID) // 使用MyBatis-Plus的ASSIGN_ID配合ShardingSphere的分布式ID
    private Long id;

    /**
     * 用户名
     */
    private String username;

    /**
     * 密码
     */
    private String password;

    /**
     * 邮箱
     */
    private String email;

    /**
     * 手机号
     */
    private String mobile;

    /**
     * 微信唯一账户ID
     */
    private String openid;
    /**
     *
     */
    private String unionid;
    /**
     * 用户来源（H5/线下/小程序/APP等）
     */
    private String source;

    /**
     * 昵称
     */
    private String nickname;

    /**
     * 头像URL
     */
    private String avatarUrl;

    /**
     * 状态: 0-禁用, 1-启用
     */
    private Integer status;

    /**
     * 创建时间
     */
    private Date createTime;

    /**
     * 更新时间
     */
    private Date updateTime;

    /**
     * 最后登录时间
     */
    private Date lastLoginTime;
}