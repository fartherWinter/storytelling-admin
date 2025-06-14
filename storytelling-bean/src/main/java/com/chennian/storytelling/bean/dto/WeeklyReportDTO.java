package com.chennian.storytelling.bean.dto;

import java.time.LocalDate;
import java.time.LocalDateTime;

/**
 * 周报数据传输对象
 * 
 * @author TraeAI
 */
public class WeeklyReportDTO {

    private Long id;
    private Long userId; // 用户ID
    private String username; // 用户名，冗余字段，方便展示
    private LocalDate weekStartDate; // 周开始日期
    private LocalDate weekEndDate; // 周结束日期
    private String content; // 周报内容
    private String summary; // 工作总结
    private String planForNextWeek; //下周计划
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

    public LocalDate getWeekStartDate() {
        return weekStartDate;
    }

    public void setWeekStartDate(LocalDate weekStartDate) {
        this.weekStartDate = weekStartDate;
    }

    public LocalDate getWeekEndDate() {
        return weekEndDate;
    }

    public void setWeekEndDate(LocalDate weekEndDate) {
        this.weekEndDate = weekEndDate;
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

    public String getPlanForNextWeek() {
        return planForNextWeek;
    }

    public void setPlanForNextWeek(String planForNextWeek) {
        this.planForNextWeek = planForNextWeek;
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
        return "WeeklyReportDTO{" +
                "id=" + id +
                ", userId=" + userId +
                ", username='" + username + '\'' +
                ", weekStartDate=" + weekStartDate +
                ", weekEndDate=" + weekEndDate +
                ", content='" + content + '\'' +
                ", summary='" + summary + '\'' +
                ", planForNextWeek='" + planForNextWeek + '\'' +
                ", createTime=" + createTime +
                ", updateTime=" + updateTime +
                '}';
    }
}