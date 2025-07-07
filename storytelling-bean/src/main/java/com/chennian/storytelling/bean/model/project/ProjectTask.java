package com.chennian.storytelling.bean.model.project;

import com.baomidou.mybatisplus.annotation.*;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.chennian.storytelling.bean.enums.DataStatusEnum;
import com.chennian.storytelling.bean.enums.PriorityEnum;
import com.chennian.storytelling.bean.enums.DelFlagEnum;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * 项目任务实体类
 *
 * @author storytelling
 * @date 2024-01-01
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@TableName("project_task")
public class ProjectTask implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 任务ID
     */
    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    /**
     * 项目ID
     */
    @TableField("project_id")
    private Long projectId;

    /**
     * 任务编号
     */
    @TableField("task_no")
    private String taskNo;

    /**
     * 任务名称
     */
    @TableField("task_name")
    private String taskName;

    /**
     * 任务描述
     */
    @TableField("description")
    private String description;

    /**
     * 任务类型（1-开发任务 2-测试任务 3-设计任务 4-文档任务 5-其他）
     */
    @TableField("task_type")
    private Integer taskType;

    /**
     * 任务状态（0-未开始 1-进行中 2-已暂停 3-已完成 4-已取消）
     */
    @TableField("status")
    private Integer status;
    
    /**
     * 获取任务状态枚举
     */
    public DataStatusEnum getStatusEnum() {
        return DataStatusEnum.getByCode(this.status);
    }
    
    /**
     * 设置任务状态枚举
     */
    public void setStatusEnum(DataStatusEnum statusEnum) {
        this.status = statusEnum != null ? statusEnum.getCode() : null;
    }

    /**
     * 优先级（1-低 2-中 3-高 4-紧急）
     */
    @TableField("priority")
    private Integer priority;
    
    /**
     * 获取优先级枚举
     */
    public PriorityEnum getPriorityEnum() {
        return PriorityEnum.getByCode(this.priority);
    }
    
    /**
     * 设置优先级枚举
     */
    public void setPriorityEnum(PriorityEnum priorityEnum) {
        this.priority = priorityEnum != null ? priorityEnum.getCode() : null;
    }

    /**
     * 父任务ID
     */
    @TableField("parent_id")
    private Long parentId;

    /**
     * 负责人ID
     */
    @TableField("assignee_id")
    private Long assigneeId;

    /**
     * 负责人姓名
     */
    @TableField("assignee_name")
    private String assigneeName;

    /**
     * 计划开始时间
     */
    @TableField("planned_start_time")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime plannedStartTime;

    /**
     * 计划结束时间
     */
    @TableField("planned_end_time")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime plannedEndTime;

    /**
     * 实际开始时间
     */
    @TableField("actual_start_time")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime actualStartTime;

    /**
     * 实际结束时间
     */
    @TableField("actual_end_time")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime actualEndTime;

    /**
     * 任务进度（0-100）
     */
    @TableField("progress")
    private Double progress;

    /**
     * 预计工时（小时）
     */
    @TableField("estimated_hours")
    private Double estimatedHours;

    /**
     * 实际工时（小时）
     */
    @TableField("actual_hours")
    private Double actualHours;

    /**
     * 任务成本
     */
    @TableField("cost")
    private BigDecimal cost;

    /**
     * 完成说明
     */
    @TableField("completion_notes")
    private String completionNotes;

    /**
     * 任务依赖
     */
    @TableField("dependencies")
    private String dependencies;

    /**
     * 任务标签
     */
    @TableField("tags")
    private String tags;

    /**
     * 任务备注
     */
    @TableField("remarks")
    private String remarks;

    /**
     * 创建者
     */
    @TableField(value = "create_by", fill = FieldFill.INSERT)
    private String createBy;

    /**
     * 创建时间
     */
    @TableField(value = "create_time", fill = FieldFill.INSERT)
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime createTime;

    /**
     * 更新者
     */
    @TableField(value = "update_by", fill = FieldFill.INSERT_UPDATE)
    private String updateBy;

    /**
     * 更新时间
     */
    @TableField(value = "update_time", fill = FieldFill.INSERT_UPDATE)
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime updateTime;

    /**
     * 删除标志（0代表存在 2代表删除）
     */
    @TableField("del_flag")
    @TableLogic
    private Integer delFlag;
    
    /**
     * 获取删除标志枚举
     */
    public DelFlagEnum getDelFlagEnum() {
        return DelFlagEnum.getByCode(this.delFlag);
    }
    
    /**
     * 设置删除标志枚举
     */
    public void setDelFlagEnum(DelFlagEnum delFlagEnum) {
        this.delFlag = delFlagEnum != null ? delFlagEnum.getCode() : null;
    }
}