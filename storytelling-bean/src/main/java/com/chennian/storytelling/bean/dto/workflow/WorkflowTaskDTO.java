package com.chennian.storytelling.bean.dto.workflow;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * 工作流任务DTO
 * 
 * @author chennian
 */
@Data
@Schema(description = "工作流任务DTO")
public class WorkflowTaskDTO implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 任务查询DTO
     */
    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    @Schema(description = "任务查询DTO")
    public static class TaskQueryDTO implements Serializable {
        
        private static final long serialVersionUID = 1L;
        
        @Schema(description = "页码")
        private Integer page;
        
        @Schema(description = "每页大小")
        private Integer size;
        
        @Schema(description = "任务状态")
        private String status;
        
        @Schema(description = "处理人")
        private String assignee;
        
        @Schema(description = "流程定义键")
        private String processDefinitionKey;
        
        @Schema(description = "业务键")
        private String businessKey;
        
        @Schema(description = "任务名称")
        private String taskName;
        
        @Schema(description = "开始时间")
        private LocalDateTime startTime;
        
        @Schema(description = "结束时间")
        private LocalDateTime endTime;
        
        @Schema(description = "流程实例ID")
        private String processInstanceId;
        
        @Schema(description = "候选用户")
        private String candidateUsers;
        
        @Schema(description = "候选组")
        private String candidateGroups;
        
        @Schema(description = "优先级")
        private Integer priority;
        
        @Schema(description = "租户ID")
        private String tenantId;
    }

    /**
     * 任务信息DTO
     */
    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    @Schema(description = "任务信息DTO")
    public static class TaskInfoDTO implements Serializable {
        
        private static final long serialVersionUID = 1L;
        
        @Schema(description = "任务ID")
        private String taskId;
        
        @Schema(description = "任务名称")
        private String taskName;
        
        @Schema(description = "流程实例ID")
        private String processInstanceId;
        
        @Schema(description = "流程定义ID")
        private String processDefinitionId;
        
        @Schema(description = "流程定义键")
        private String processDefinitionKey;
        
        @Schema(description = "流程定义名称")
        private String processDefinitionName;
        
        @Schema(description = "处理人")
        private String assignee;
        
        @Schema(description = "处理人名称")
        private String assigneeName;
        
        @Schema(description = "创建时间")
        private LocalDateTime createTime;
        
        @Schema(description = "到期时间")
        private LocalDateTime dueDate;
        
        @Schema(description = "优先级")
        private Integer priority;
        
        @Schema(description = "任务状态")
        private String status;
        
        @Schema(description = "业务键")
        private String businessKey;
        
        @Schema(description = "表单键")
        private String formKey;
        
        @Schema(description = "候选用户")
        private String candidateUsers;
        
        @Schema(description = "候选组")
        private String candidateGroups;
        
        @Schema(description = "租户ID")
        private String tenantId;
    }

    /**
     * 任务详情DTO
     */
    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    @Schema(description = "任务详情DTO")
    public static class TaskDetailDTO implements Serializable {
        
        private static final long serialVersionUID = 1L;
        
        @Schema(description = "任务ID")
        private String taskId;
        
        @Schema(description = "任务键")
        private String taskKey;
        
        @Schema(description = "任务名称")
        private String taskName;
        
        @Schema(description = "流程实例ID")
        private String processInstanceId;
        
        @Schema(description = "流程定义ID")
        private String processDefinitionId;
        
        @Schema(description = "流程定义键")
        private String processDefinitionKey;
        
        @Schema(description = "流程定义名称")
        private String processDefinitionName;
        
        @Schema(description = "处理人")
        private String assignee;
        
        @Schema(description = "处理人名称")
        private String assigneeName;
        
        @Schema(description = "创建时间")
        private LocalDateTime createTime;
        
        @Schema(description = "到期时间")
        private LocalDateTime dueDate;
        
        @Schema(description = "优先级")
        private Integer priority;
        
        @Schema(description = "任务描述")
        private String description;
        
        @Schema(description = "任务拥有者")
        private String owner;
        
        @Schema(description = "父任务ID")
        private String parentTaskId;
        
        @Schema(description = "表单键")
        private String formKey;
        
        @Schema(description = "流程变量")
        private Map<String, Object> processVariables;
        
        @Schema(description = "任务本地变量")
        private Map<String, Object> taskLocalVariables;
        
        @Schema(description = "任务状态")
        private String status;
        
        @Schema(description = "业务键")
        private String businessKey;
        
        @Schema(description = "候选用户")
        private String candidateUsers;
        
        @Schema(description = "候选组")
        private String candidateGroups;
        
        @Schema(description = "任务类型")
        private String taskType;
        
        @Schema(description = "完成时间")
        private LocalDateTime completedTime;
        
        @Schema(description = "处理时间(毫秒)")
        private Long processingTime;
        
        @Schema(description = "租户ID")
        private String tenantId;
        
        @Schema(description = "备注")
        private String remark;
        
        @Schema(description = "表单数据")
        private Map<String, Object> formData;
        
        @Schema(description = "流程历史")
        private List<Map<String, Object>> processHistory;
    }

    /**
     * 分页结果DTO
     */
    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    @Schema(description = "分页结果DTO")
    public static class PageResult<T> implements Serializable {
        
        private static final long serialVersionUID = 1L;
        
        @Schema(description = "数据列表")
        private List<T> records;
        
        @Schema(description = "总记录数")
        private Long total;
        
        @Schema(description = "当前页")
        private Long current;
        
        @Schema(description = "每页大小")
        private Long size;
        
        @Schema(description = "总页数")
        private Long pages;
        
        @Schema(description = "是否有下一页")
        private Boolean hasNext;
        
        @Schema(description = "是否有上一页")
        private Boolean hasPrevious;
        
        public PageResult(List<T> records, Long total, Long current, Long size) {
            this.records = records;
            this.total = total;
            this.current = current;
            this.size = size;
            this.pages = (total + size - 1) / size;
            this.hasNext = current < pages;
            this.hasPrevious = current > 1;
        }
    }
}