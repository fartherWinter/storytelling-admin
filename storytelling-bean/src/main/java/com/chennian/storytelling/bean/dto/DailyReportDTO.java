package com.chennian.storytelling.bean.dto;

import java.time.LocalDate;
import java.time.LocalDateTime;

/**
 * 日报数据传输对象
 * 
 * @author TraeAI
 */
public class DailyReportDTO {

    private Long id;
    private Long userId; // 用户ID
    private String username; // 用户名，冗余字段，方便展示
    private LocalDate reportDate; // 报告日期
    private String content; // 日报内容
    private String summary; // 工作总结
    private String planForNextDay; // 明日计划
    private LocalDateTime createTime; // 创建时间
    private LocalDateTime updateTime; // 更新时间

    // Getters and Setters

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public LocalDate getReportDate() {
        return reportDate;
    }

    public void setReportDate(LocalDate reportDate) {
        this.reportDate = reportDate;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getSummary() {
        return summary;
    }

    public void setSummary(String summary) {
        this.summary = summary;
    }

    public String getPlanForNextDay() {
        return planForNextDay;
    }

    public void setPlanForNextDay(String planForNextDay) {
        this.planForNextDay = planForNextDay;
    }

    public LocalDateTime getCreateTime() {
        return createTime;
    }

    public void setCreateTime(LocalDateTime createTime) {
        this.createTime = createTime;
    }

    public LocalDateTime getUpdateTime() {
        return updateTime;
    }

    public void setUpdateTime(LocalDateTime updateTime) {
        this.updateTime = updateTime;
    }

    @Override
    public String toString() {
        return "DailyReportDTO{" +
                "id=" + id +
                ", userId=" + userId +
                ", username='" + username + '\'' +
                ", reportDate=" + reportDate +
                ", content='" + content + '\'' +
                ", summary='" + summary + '\'' +
                ", planForNextDay='" + planForNextDay + '\'' +
                ", createTime=" + createTime +
                ", updateTime=" + updateTime +
                '}';
    }
}