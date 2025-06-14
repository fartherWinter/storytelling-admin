package com.chennian.storytelling.bean.dto;

import lombok.Data;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * 历史任务数据传输对象
 * 
 * @author chennian
 */
@Data
public class HistoricTaskDTO implements Serializable {

    private static final long serialVersionUID = 1L;

    /** 任务ID */
    private String id;
    
    /** 任务名称 */
    private String name;
    
    /** 任务定义KEY */
    private String taskDefinitionKey;
    
    /** 任务描述 */
    private String description;
    
    /** 流程实例ID */
    private String processInstanceId;
    
    /** 流程定义ID */
    private String processDefinitionId;
    
    /** 执行ID */
    private String executionId;
    
    /** 任务分配人 */
    private String assignee;
    
    /** 任务分配人姓名 */
    private String assigneeName;
    
    /** 任务所有者 */
    private String owner;
    
    /** 任务候选组 */
    private String candidateGroups;
    
    /** 任务候选用户 */
    private String candidateUsers;
    
    /** 任务优先级 */
    private Integer priority;
    
    /** 任务表单KEY */
    private String formKey;
    
    /** 任务创建时间 */
    private LocalDateTime createTime;
    
    /** 任务开始时间 */
    private LocalDateTime startTime;
    
    /** 任务结束时间 */
    private LocalDateTime endTime;
    
    /** 任务持续时间（毫秒） */
    private Long duration;
    
    /** 任务删除原因 */
    private String deleteReason;
    
    /** 任务状态 */
    private String status;
    
    /** 任务结果 */
    private String result;
    
    /** 任务意见 */
    private String comment;
    
    /** 父任务ID */
    private String parentTaskId;
    
    /** 任务到期时间 */
    private LocalDateTime dueDate;
    
    /** 任务跟进时间 */
    private LocalDateTime followUpDate;
    
    /** 任务类别 */
    private String category;
    
    /** 租户ID */
    private String tenantId;
    
    /** 任务声明时间 */
    private LocalDateTime claimTime;

}