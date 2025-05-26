package com.chennian.storytelling.bean.dto;

import java.time.LocalDate;
import java.time.LocalDateTime;

/**
 * 通用报告数据传输对象（日报/周报）
 * type: daily/weekly
 */
public class ReportDTO {
    private Long id;
    private Long userId;
    private String username;
    private String type; // "daily" 或 "weekly"
    private LocalDate reportDate; // 日报日期
    private LocalDate weekStartDate; // 周报开始日期
    private LocalDate weekEndDate;   // 周报结束日期
    private String content;
    private String summary;
    private String plan; // 明日计划/下周计划
    private LocalDateTime createTime;
    private LocalDateTime updateTime;
    private String processInstanceId; // 工作流流程实例ID
    private String approvalStatus; // 审批状态（如：待提交、审批中、已通过、已拒绝）

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public Long getUserId() { return userId; }
    public void setUserId(Long userId) { this.userId = userId; }
    public String getUsername() { return username; }
    public void setUsername(String username) { this.username = username; }
    public String getType() { return type; }
    public void setType(String type) { this.type = type; }
    public LocalDate getReportDate() { return reportDate; }
    public void setReportDate(LocalDate reportDate) { this.reportDate = reportDate; }
    public LocalDate getWeekStartDate() { return weekStartDate; }
    public void setWeekStartDate(LocalDate weekStartDate) { this.weekStartDate = weekStartDate; }
    public LocalDate getWeekEndDate() { return weekEndDate; }
    public void setWeekEndDate(LocalDate weekEndDate) { this.weekEndDate = weekEndDate; }
    public String getContent() { return content; }
    public void setContent(String content) { this.content = content; }
    public String getSummary() { return summary; }
    public void setSummary(String summary) { this.summary = summary; }
    public String getPlan() { return plan; }
    public void setPlan(String plan) { this.plan = plan; }
    public LocalDateTime getCreateTime() { return createTime; }
    public void setCreateTime(LocalDateTime createTime) { this.createTime = createTime; }
    public LocalDateTime getUpdateTime() { return updateTime; }
    public void setUpdateTime(LocalDateTime updateTime) { this.updateTime = updateTime; }
    public String getProcessInstanceId() { return processInstanceId; }
    public void setProcessInstanceId(String processInstanceId) { this.processInstanceId = processInstanceId; }
    public String getApprovalStatus() { return approvalStatus; }
    public void setApprovalStatus(String approvalStatus) { this.approvalStatus = approvalStatus; }

    @Override
    public String toString() {
        return "ReportDTO{" +
                "id=" + id +
                ", userId=" + userId +
                ", username='" + username + '\'' +
                ", type='" + type + '\'' +
                ", reportDate=" + reportDate +
                ", weekStartDate=" + weekStartDate +
                ", weekEndDate=" + weekEndDate +
                ", content='" + content + '\'' +
                ", summary='" + summary + '\'' +
                ", plan='" + plan + '\'' +
                ", processInstanceId='" + processInstanceId + '\'' +
                ", approvalStatus='" + approvalStatus + '\'' +
                ", createTime=" + createTime +
                ", updateTime=" + updateTime +
                '}';
    }
}