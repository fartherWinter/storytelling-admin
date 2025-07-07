package com.chennian.storytelling.bean.model;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.chennian.storytelling.bean.enums.NotificationTypeEnum;
import com.chennian.storytelling.bean.enums.EnableStatusEnum;

import java.io.Serializable;
import java.util.Arrays;
import java.util.Date;
import lombok.Data;

/**
 * 通知公告表
 * @author chen
 * @TableName sys_notice
 */
@TableName(value ="sys_notice")
@Data
public class SysNotice implements Serializable {
    /**
     * 公告ID
     */
    @TableId(value = "notice_id", type = IdType.AUTO)
    private Integer noticeId;

    /**
     * 公告标题
     */
    @TableField(value = "notice_title")
    private String noticeTitle;

    /**
     * 公告类型
     */
    @TableField(value = "notice_type")
    private Integer noticeType;
    
    /**
     * 获取公告类型枚举
     */
    public NotificationTypeEnum getNoticeTypeEnum() {
        return NotificationTypeEnum.getByCode(this.noticeType);
    }
    
    /**
     * 设置公告类型枚举
     */
    public void setNoticeTypeEnum(NotificationTypeEnum typeEnum) {
        this.noticeType = typeEnum != null ? typeEnum.getCode() : null;
    }

    /**
     * 公告状态
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

    /**
     * 公告内容
     */
    @TableField(value = "notice_content")
    private byte[] notice_content;


}