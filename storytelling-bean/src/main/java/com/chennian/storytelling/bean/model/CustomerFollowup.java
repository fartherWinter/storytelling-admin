package com.chennian.storytelling.bean.model;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;

/**
 * 客户跟进记录表
 * @author chen
 * @TableName erp_customer_followup
 */
@TableName(value ="erp_customer_followup")
@Data
public class CustomerFollowup implements Serializable {
    /**
     * 跟进记录ID
     */
    @TableId(value = "followup_id", type = IdType.AUTO)
    private Long followupId;

    /**
     * 客户ID
     */
    @TableField(value = "customer_id")
    private Long customerId;

    /**
     * 跟进方式（1电话 2邮件 3拜访 4其他）
     */
    @TableField(value = "followup_type")
    private String followupType;

    /**
     * 跟进内容
     */
    @TableField(value = "followup_content")
    private String followupContent;

    /**
     * 跟进结果
     */
    @TableField(value = "followup_result")
    private String followupResult;

    /**
     * 下次跟进时间
     */
    @TableField(value = "next_followup_time")
    private Date nextFollowupTime;

    /**
     * 跟进人员
     */
    @TableField(value = "followup_person")
    private String followupPerson;

    /**
     * 跟进时间
     */
    @TableField(value = "followup_time")
    private Date followupTime;

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