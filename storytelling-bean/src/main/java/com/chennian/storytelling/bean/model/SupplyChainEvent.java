package com.chennian.storytelling.bean.model;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.io.Serializable;

/**
 * 供应链事件实体
 * 
 * @author chennian
 * @date 2023/12/15
 */
@Data
@TableName("supply_chain_event")
public class SupplyChainEvent implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 事件ID
     */
    @TableId(value = "event_id", type = IdType.AUTO)
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
}