package com.chennian.storytelling.bean.model;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.chennian.storytelling.bean.enums.GenderEnum;
import com.chennian.storytelling.bean.enums.EnableStatusEnum;
import com.chennian.storytelling.bean.enums.DelFlagEnum;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

import lombok.Data;

/**
 * 用户信息表
 * @author chen
 * @TableName sys_user
 */
@TableName(value ="sys_user")
@Data
public class SysUser implements Serializable {
    /**
     * 用户ID
     */
    @TableId(value = "user_id", type = IdType.AUTO)
    private Long userId;

    /**
     * 部门ID
     */
    @TableField(value = "dept_id")
    private Long deptId;

    /**
     * 登录账号
     */
    @TableField(value = "user_name")
    private String userName;

    /**
     * 用户昵称
     */
    @TableField(value = "nick_name")
    private String nickName;

    /**
     * 用户类型（00系统用户 01注册用户）
     */
    @TableField(value = "user_type")
    private String userType;

    /**
     * 用户邮箱
     */
    @TableField(value = "email")
    private String email;

    /**
     * 手机号码
     */
    @TableField(value = "phone_number")
    private String phoneNumber;

    /**
     * 用户性别
     */
    @TableField(value = "sex")
    private Integer sex;
    
    /**
     * 获取性别枚举
     */
    public GenderEnum getSexEnum() {
        return GenderEnum.getByCode(this.sex);
    }
    
    /**
     * 设置性别枚举
     */
    public void setSexEnum(GenderEnum genderEnum) {
        this.sex = genderEnum != null ? genderEnum.getCode() : null;
    }

    /**
     * 头像路径
     */
    @TableField(value = "avatar")
    private String avatar;

    /**
     * 密码
     */
    @TableField(value = "password")
    private String password;

    /**
     * 盐加密
     */
    @TableField(value = "salt")
    private String salt;

    /**
     * 账号状态
     */
    @TableField(value = "status")
    private Integer status;
    
    /**
     * 获取状态枚举
     */
    public EnableStatusEnum getStatusEnum() {
        return EnableStatusEnum.getByCode(this.status);
    }
    
    /**
     * 设置状态枚举
     */
    public void setStatusEnum(EnableStatusEnum statusEnum) {
        this.status = statusEnum != null ? statusEnum.getCode() : null;
    }

    /**
     * 删除标志
     */
    @TableField(value = "del_flag")
    private Integer delFlag;
    
    /**
     * 获取删除标志枚举
     */
    public DelFlagEnum getDelFlagEnum() {
        return DelFlagEnum.getByCode(this.delFlag);
    }
    
    /**
     * 设置删除标志枚举
     */
    public void setDelFlagEnum(DelFlagEnum delFlagEnum) {
        this.delFlag = delFlagEnum != null ? delFlagEnum.getCode() : null;
    }

    /**
     * 最后登录IP
     */
    @TableField(value = "login_ip")
    private String loginIp;

    /**
     * 最后登录时间
     */
    @TableField(value = "login_date")
    private Date loginDate;

    /**
     * 密码最后更新时间
     */
    @TableField(value = "pwd_update_date")
    private Date pwdUpdateDate;

    /**
     * 创建者
     */
    @TableField(value = "create_by")
    private String createBy;

    /**
     * 创建时间
     */
    @TableField(value = "create_time")
    private Date createTime;

    /**
     * 更新者
     */
    @TableField(value = "update_by")
    private String updateBy;

    /**
     * 更新时间
     */
    @TableField(value = "update_time")
    private Date updateTime;

    /**
     * 备注
     */
    @TableField(value = "remark")
    private String remark;
    @TableField(exist = false)
    private String deptName;
    /**
     * 部门对象
     */
    @TableField(exist = false)
    private SysDept dept;
    /**
     * 角色对象
     */
    @TableField(exist = false)
    private List<SysRole> roles;

    /**
     * 角色组
     */
    @TableField(exist = false)
    private Long[] roleIds;

    /**
     * 岗位组
     */
    @TableField(exist = false)
    private Long[] postIds;

    /**
     * 角色ID
     */
    @TableField(exist = false)
    private Long roleId;
    /**
     * 岗位ID
     */
    @TableField(exist = false)
    private Long postId;
    /**
     * 用户渠道标记
     */
    @TableField(exist = false)
    private String curAppKey;
    /**
     * 判断管理员方法
     *
     * @param userId
     * @return
     */
    public static boolean isAdmin(Long userId) {
        return userId != null && 1L == userId;
    }

    public boolean isAdmin() {
        return isAdmin(this.userId);
    }

}