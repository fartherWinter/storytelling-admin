package com.chennian.storytelling.bean.dto;

import lombok.Data;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * 历史活动数据传输对象
 * 
 * @author chennian
 */
@Data
public class HistoricActivityDTO implements Serializable {

    private static final long serialVersionUID = 1L;

    /** 活动ID */
    private String id;
    
    /** 活动名称 */
    private String activityName;
    
    /** 活动类型 */
    private String activityType;
    
    /** 活动ID */
    private String activityId;
    
    /** 流程实例ID */
    private String processInstanceId;
    
    /** 流程定义ID */
    private String processDefinitionId;
    
    /** 执行ID */
    private String executionId;
    
    /** 任务ID */
    private String taskId;
    
    /** 调用的流程实例ID */
    private String calledProcessInstanceId;
    
    /** 活动分配人 */
    private String assignee;
    
    /** 活动分配人姓名 */
    private String assigneeName;
    
    /** 活动开始时间 */
    private LocalDateTime startTime;
    
    /** 活动结束时间 */
    private LocalDateTime endTime;
    
    /** 活动持续时间（毫秒） */
    private Long duration;
    
    /** 活动删除原因 */
    private String deleteReason;
    
    /** 活动状态 */
    private String status;
    
    /** 活动结果 */
    private String result;
    
    /** 活动意见 */
    private String comment;
    
    /** 租户ID */
    private String tenantId;
    
    /** 活动实例状态 */
    private String activityInstanceState;
    
    /** 序列计数器 */
    private Long sequenceCounter;
    
    /** 活动实例ID */
    private String activityInstanceId;
    
    /** 行为ID */
    private String behaviorClass;
    
    /** 是否多实例 */
    private Boolean multiInstance;
    
    /** 多实例根活动实例ID */
    private String rootProcessInstanceId;
    
    /** 是否范围 */
    private Boolean scope;
    
    /** 子流程实例ID */
    private String subProcessInstanceId;
    
    /** 子案例实例ID */
    private String subCaseInstanceId;

}