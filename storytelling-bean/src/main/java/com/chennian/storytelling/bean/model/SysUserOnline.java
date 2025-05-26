package com.chennian.storytelling.bean.model;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;

import java.io.Serializable;
import java.util.Date;
import lombok.Data;

/**
 * 在线用户记录
 * @author chen
 * @TableName sys_user_online
 */
@TableName(value ="sys_user_online")
@Data
public class SysUserOnline implements Serializable {
    /**
     * 用户会话id
     */
    @TableId(value = "sessionId")
    private String sessionId;

    /**
     * 登录账号
     */
    @TableField(value = "login_name")
    private String loginName;

    /**
     * 部门名称
     */
    @TableField(value = "dept_name")
    private String deptName;

    /**
     * 登录IP地址
     */
    @TableField(value = "ipaddr")
    private String ipaddr;

    /**
     * 登录地点
     */
    @TableField(value = "login_location")
    private String loginLocation;

    /**
     * 浏览器类型
     */
    @TableField(value = "browser")
    private String browser;

    /**
     * 操作系统
     */
    @TableField(value = "os")
    private String os;

    /**
     * 在线状态on_line在线off_line离线
     */
    @TableField(value = "status")
    private String status;

    /**
     * session创建时间
     */
    @TableField(value = "start_timestamp")
    private Date startTimestamp;

    /**
     * session最后访问时间
     */
    @TableField(value = "last_access_time")
    private Date lastAccessTime;

    /**
     * 超时时间，单位为分钟
     */
    @TableField(value = "expire_time")
    private Integer expireTime;


}