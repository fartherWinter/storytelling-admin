package com.chennian.storytelling.bean.model;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.chennian.storytelling.bean.enums.EnableStatusEnum;
import com.chennian.storytelling.bean.enums.LevelEnum;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;

/**
 * 客户信息表
 * @author chen
 * @TableName erp_customer
 */
@TableName(value ="erp_customer")
@Data
public class Customer implements Serializable {
    /**
     * 客户ID
     */
    @TableId(value = "customer_id", type = IdType.AUTO)
    private Long customerId;

    /**
     * 客户编码
     */
    @TableField(value = "customer_code")
    private String customerCode;

    /**
     * 客户名称
     */
    @TableField(value = "customer_name")
    private String customerName;

    /**
     * 联系人
     */
    @TableField(value = "contact_person")
    private String contactPerson;

    /**
     * 联系电话
     */
    @TableField(value = "contact_phone")
    private String contactPhone;

    /**
     * 电子邮箱
     */
    @TableField(value = "email")
    private String email;

    /**
     * 地址
     */
    @TableField(value = "address")
    private String address;

    /**
     * 客户类型（1个人 2企业 3政府 4其他）
     */
    @TableField(value = "customer_type")
    private String customerType;

    /**
     * 等级
     */
    @TableField(value = "level")
    private Integer level;
    
    /**
     * 获取等级枚举
     */
    public LevelEnum getLevelEnum() {
        return LevelEnum.getByCode(this.level);
    }
    
    /**
     * 设置等级枚举
     */
    public void setLevelEnum(LevelEnum levelEnum) {
        this.level = levelEnum != null ? levelEnum.getCode() : null;
    }

    /**
     * 信用额度
     */
    @TableField(value = "credit_limit")
    private Long creditLimit;

    /**
     * 客户等级（1普通 2银牌 3金牌 4钻石）
     */
    @TableField(value = "customer_level")
    private String customerLevel;

    /**
     * 状态
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
        this.status = statusEnum != null ? Integer.valueOf(statusEnum.getCode()) : null;
    }

    /**
     * 备注
     */
    @TableField(value = "remark")
    private String remark;

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

    @TableField(exist = false)
    private static final long serialVersionUID = 1L;

}

