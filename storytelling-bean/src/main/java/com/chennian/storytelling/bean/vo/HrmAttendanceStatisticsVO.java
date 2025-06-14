package com.chennian.storytelling.bean.vo;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

/**
 * HRM考勤统计数据VO对象
 * @author storytelling
 * @date 2024-01-01
 */
public class HrmAttendanceStatisticsVO implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 总出勤天数
     */
    private Integer totalAttendanceDays;

    /**
     * 正常出勤天数
     */
    private Integer normalAttendanceDays;

    /**
     * 迟到次数
     */
    private Integer lateCount;

    /**
     * 早退次数
     */
    private Integer earlyLeaveCount;

    /**
     * 缺勤天数
     */
    private Integer absentDays;

    /**
     * 加班小时数
     */
    private BigDecimal overtimeHours;

    /**
     * 出勤率
     */
    private BigDecimal attendanceRate;

    /**
     * 部门出勤统计
     */
    private List<Map<String, Object>> departmentAttendance;

    /**
     * 员工出勤排行
     */
    private List<Map<String, Object>> employeeAttendanceRanking;

    /**
     * 月度出勤趋势
     */
    private List<Map<String, Object>> monthlyAttendanceTrend;

    public Integer getTotalAttendanceDays() {
        return totalAttendanceDays;
    }

    public void setTotalAttendanceDays(Integer totalAttendanceDays) {
        this.totalAttendanceDays = totalAttendanceDays;
    }

    public Integer getNormalAttendanceDays() {
        return normalAttendanceDays;
    }

    public void setNormalAttendanceDays(Integer normalAttendanceDays) {
        this.normalAttendanceDays = normalAttendanceDays;
    }

    public Integer getLateCount() {
        return lateCount;
    }

    public void setLateCount(Integer lateCount) {
        this.lateCount = lateCount;
    }

    public Integer getEarlyLeaveCount() {
        return earlyLeaveCount;
    }

    public void setEarlyLeaveCount(Integer earlyLeaveCount) {
        this.earlyLeaveCount = earlyLeaveCount;
    }

    public Integer getAbsentDays() {
        return absentDays;
    }

    public void setAbsentDays(Integer absentDays) {
        this.absentDays = absentDays;
    }

    public BigDecimal getOvertimeHours() {
        return overtimeHours;
    }

    public void setOvertimeHours(BigDecimal overtimeHours) {
        this.overtimeHours = overtimeHours;
    }

    public BigDecimal getAttendanceRate() {
        return attendanceRate;
    }

    public void setAttendanceRate(BigDecimal attendanceRate) {
        this.attendanceRate = attendanceRate;
    }

    public List<Map<String, Object>> getDepartmentAttendance() {
        return departmentAttendance;
    }

    public void setDepartmentAttendance(List<Map<String, Object>> departmentAttendance) {
        this.departmentAttendance = departmentAttendance;
    }

    public List<Map<String, Object>> getEmployeeAttendanceRanking() {
        return employeeAttendanceRanking;
    }

    public void setEmployeeAttendanceRanking(List<Map<String, Object>> employeeAttendanceRanking) {
        this.employeeAttendanceRanking = employeeAttendanceRanking;
    }

    public List<Map<String, Object>> getMonthlyAttendanceTrend() {
        return monthlyAttendanceTrend;
    }

    public void setMonthlyAttendanceTrend(List<Map<String, Object>> monthlyAttendanceTrend) {
        this.monthlyAttendanceTrend = monthlyAttendanceTrend;
    }

    @Override
    public String toString() {
        return "HrmAttendanceStatisticsVO{" +
                "totalAttendanceDays=" + totalAttendanceDays +
                ", normalAttendanceDays=" + normalAttendanceDays +
                ", lateCount=" + lateCount +
                ", earlyLeaveCount=" + earlyLeaveCount +
                ", absentDays=" + absentDays +
                ", overtimeHours=" + overtimeHours +
                ", attendanceRate=" + attendanceRate +
                ", departmentAttendance=" + departmentAttendance +
                ", employeeAttendanceRanking=" + employeeAttendanceRanking +
                ", monthlyAttendanceTrend=" + monthlyAttendanceTrend +
                '}';
    }
}