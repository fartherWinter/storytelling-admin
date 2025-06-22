package com.chennian.storytelling.bean.dto;

import lombok.Data;

import java.io.Serializable;
import java.util.Date;

/**
 * 今日完成实例DTO
 * 
 * @author storytelling
 * @date 2024-01-01
 */
@Data
public class TodayCompletedInstanceDTO implements Serializable {
    
    private static final long serialVersionUID = 1L;
    
    /** 流程实例ID */
    private String processInstanceId;
    
    /** 流程定义键 */
    private String processDefinitionKey;
    
    /** 流程定义名称 */
    private String processDefinitionName;
    
    /** 业务键 */
    private String businessKey;
    
    /** 发起人 */
    private String startUser;
    
    /** 开始时间 */
    private Date startTime;
    
    /** 结束时间 */
    private Date endTime;
    
    /** 持续时间(分钟) */
    private Long durationMinutes;
    
    /** 统计数量 */
    private Long count;
    /**
     * 时间
     */
    private Integer hour;
    /** 备注 */
    private String remark;
    


}