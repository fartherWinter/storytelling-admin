package com.chennian.storytelling.bean.dto;

import lombok.Data;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.List;

/**
 * 工作流历史记录数据传输对象
 * 
 * @author chennian
 */
@Data
public class WorkflowHistoryDTO implements Serializable {

    private static final long serialVersionUID = 1L;

    /** 流程实例ID */
    private String processInstanceId;
    
    /** 流程定义ID */
    private String processDefinitionId;
    
    /** 流程定义名称 */
    private String processDefinitionName;
    
    /** 流程定义KEY */
    private String processDefinitionKey;
    
    /** 业务键 */
    private String businessKey;
    
    /** 业务类型 */
    private String businessType;
    
    /** 流程标题 */
    private String title;
    
    /** 流程状态 */
    private String status;
    
    /** 开始时间 */
    private LocalDateTime startTime;
    
    /** 结束时间 */
    private LocalDateTime endTime;
    
    /** 持续时间（毫秒） */
    private Long duration;
    
    /** 发起人 */
    private String startUserId;
    
    /** 发起人姓名 */
    private String startUserName;
    
    /** 结束原因 */
    private String deleteReason;
    
    /** 历史任务列表 */
    private List<HistoricTaskDTO> historicTasks;
    
    /** 历史活动列表 */
    private List<HistoricActivityDTO> historicActivities;


}