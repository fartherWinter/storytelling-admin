package com.chennian.storytelling.bean.model;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;

/**
 * 供应商信息表
 * @author chen
 * @TableName erp_supplier
 */
@TableName(value ="erp_supplier")
@Data
public class Supplier implements Serializable {
    /**
     * 供应商ID
     */
    @TableId(value = "supplier_id", type = IdType.AUTO)
    private Long supplierId;

    /**
     * 供应商编码
     */
    @TableField(value = "supplier_code")
    private String supplierCode;

    /**
     * 供应商名称
     */
    @TableField(value = "supplier_name")
    private String supplierName;

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
     * 银行账户
     */
    @TableField(value = "bank_account")
    private String bankAccount;

    /**
     * 开户银行
     */
    @TableField(value = "bank_name")
    private String bankName;

    /**
     * 税号
     */
    @TableField(value = "tax_number")
    private String taxNumber;

    /**
     * 供应商类型（1原材料 2设备 3服务 4其他）
     */
    @TableField(value = "supplier_type")
    private String supplierType;

    /**
     * 状态（0正常 1禁用）
     */
    @TableField(value = "status")
    private String status;

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