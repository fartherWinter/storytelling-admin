package com.chennian.storytelling.bean.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

/**
 * 员工月度考勤统计VO
 * 
 * @author storytelling
 * @date 2024-01-01
 */
@Data
@Schema(description = "员工月度考勤统计数据")
public class HrmEmployeeMonthlyAttendanceVO implements Serializable {

    private static final long serialVersionUID = 1L;

    @Schema(description = "员工ID")
    private Long employeeId;

    @Schema(description = "员工姓名")
    private String employeeName;

    @Schema(description = "部门名称")
    private String departmentName;

    @Schema(description = "职位名称")
    private String positionName;

    @Schema(description = "统计年月")
    private String yearMonth;

    @Schema(description = "应出勤天数")
    private Integer shouldAttendDays;

    @Schema(description = "实际出勤天数")
    private Integer actualAttendDays;

    @Schema(description = "正常出勤天数")
    private Integer normalAttendDays;

    @Schema(description = "迟到次数")
    private Integer lateCount;

    @Schema(description = "早退次数")
    private Integer earlyLeaveCount;

    @Schema(description = "缺勤天数")
    private Integer absentDays;

    @Schema(description = "请假天数")
    private Integer leaveDays;

    @Schema(description = "加班小时数")
    private BigDecimal overtimeHours;

    @Schema(description = "出勤率")
    private BigDecimal attendanceRate;

    @Schema(description = "迟到总时长(分钟)")
    private Integer totalLateMinutes;

    @Schema(description = "早退总时长(分钟)")
    private Integer totalEarlyLeaveMinutes;

    @Schema(description = "平均每日工作时长(小时)")
    private BigDecimal avgDailyWorkHours;

    @Schema(description = "每日考勤详情")
    private List<DailyAttendanceDetail> dailyDetails;

    @Schema(description = "考勤异常统计")
    private Map<String, Integer> abnormalStatistics;

    @Schema(description = "月度考勤趋势")
    private List<AttendanceTrend> monthlyTrend;

    /**
     * 每日考勤详情
     */
    @Data
    @Schema(description = "每日考勤详情")
    public static class DailyAttendanceDetail implements Serializable {
        
        @Schema(description = "日期")
        private String date;
        
        @Schema(description = "上班打卡时间")
        private String checkInTime;
        
        @Schema(description = "下班打卡时间")
        private String checkOutTime;
        
        @Schema(description = "工作时长(小时)")
        private BigDecimal workHours;
        
        @Schema(description = "考勤状态: 正常、迟到、早退、缺勤、请假")
        private String status;
        
        @Schema(description = "迟到时长(分钟)")
        private Integer lateMinutes;
        
        @Schema(description = "早退时长(分钟)")
        private Integer earlyLeaveMinutes;
        
        @Schema(description = "加班时长(小时)")
        private BigDecimal overtimeHours;
        
        @Schema(description = "备注")
        private String remark;
    }

    /**
     * 考勤趋势
     */
    @Data
    @Schema(description = "考勤趋势")
    public static class AttendanceTrend implements Serializable {
        
        @Schema(description = "日期")
        private String date;
        
        @Schema(description = "出勤状态: 1-正常 0-异常")
        private Integer attendanceStatus;
        
        @Schema(description = "工作时长")
        private BigDecimal workHours;
    }
}