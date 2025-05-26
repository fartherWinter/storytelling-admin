package com.chennian.storytelling.bean.vo;

import lombok.Data;

import java.io.Serializable;
import java.util.Date;

/**
 * 客户跟进记录VO
 * @author chennian
 */
@Data
public class CustomerFollowupVO implements Serializable {
    
    private static final long serialVersionUID = 1L;
    
    /**
     * 跟进记录ID
     */
    private Long followupId;
    
    /**
     * 客户ID
     */
    private Long customerId;
    
    /**
     * 客户名称
     */
    private String customerName;
    
    /**
     * 跟进方式（1电话 2邮件 3拜访 4其他）
     */
    private String followupType;
    
    /**
     * 跟进内容
     */
    private String followupContent;
    
    /**
     * 跟进结果
     */
    private String followupResult;
    
    /**
     * 下次跟进时间
     */
    private Date nextFollowupTime;
    
    /**
     * 跟进人员
     */
    private String followupPerson;
    
    /**
     * 跟进时间
     */
    private Date followupTime;
    
    /**
     * 备注
     */
    private String remark;
    
    /**
     * 创建者
     */
    private String createBy;
    
    /**
     * 创建时间
     */
    private Date createTime;
}