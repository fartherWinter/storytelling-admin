package com.chennian.storytelling.bean.vo;

import lombok.Data;

import java.io.Serializable;

/**
 * 供应链事件VO
 * 
 * @author chennian
 * @date 2023/12/15
 */
@Data
public class SupplyChainEventVO implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 事件ID
     */
    private Long eventId;

    /**
     * 事件类型
     */
    private String eventType;

    /**
     * 事件描述
     */
    private String description;

    /**
     * 事件时间
     */
    private Long eventTime;
    
    /**
     * 事件相关ID
     */
    private Long relatedId;
    
    /**
     * 事件状态
     */
    private String status;
    
    /**
     * 事件处理人
     */
    private String handler;
    
    /**
     * 事件处理时间
     */
    private Long handleTime;
    
    public SupplyChainEventVO() {
    }
    
    public SupplyChainEventVO(Long eventId, String eventType, String description, Long eventTime) {
        this.eventId = eventId;
        this.eventType = eventType;
        this.description = description;
        this.eventTime = eventTime;
    }
}