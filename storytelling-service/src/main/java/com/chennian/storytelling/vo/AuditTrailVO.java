package com.chennian.storytelling.vo;

import lombok.Data;

/**
 * 审计跟踪VO
 * @author chen
 * @date 2023/6/15
 */
@Data
public class AuditTrailVO {
    
    /**
     * ID
     */
    private Integer id;
    
    /**
     * 模块
     */
    private String module;
    
    /**
     * 操作
     */
    private String operation;
    
    /**
     * 操作人
     */
    private String operator;
    
    /**
     * 操作时间
     */
    private String operateTime;
    
    /**
     * IP地址
     */
    private String ip;
}