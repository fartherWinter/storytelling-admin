package com.chennian.storytelling.bean.model.hrm;

import com.alibaba.excel.annotation.ExcelProperty;
import com.chennian.storytelling.common.domain.BaseEntity;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serial;
import java.util.Date;

/**
 * 考勤记录对象 hrm_attendance
 * 
 * @author storytelling
 * @date 2024-01-01
 */
@EqualsAndHashCode(callSuper = true)
@Data
public class HrmAttendance extends BaseEntity {
    @Serial
    private static final long serialVersionUID = 1L;

    /** 考勤ID */
    private Long id;

    /** 员工ID */
    @ExcelProperty(value = "员工ID")
    private Long employeeId;

    /** 员工姓名 */
    @ExcelProperty(value = "员工姓名")
    private String employeeName;

    /** 员工编号 */
    @ExcelProperty(value = "员工编号")
    private String employeeNo;

    /** 部门ID */
    @ExcelProperty(value = "部门ID")
    private Long departmentId;

    /** 部门名称 */
    @ExcelProperty(value = "部门名称")
    private String departmentName;

    /** 考勤日期 */
    @JsonFormat(pattern = "yyyy-MM-dd")
    @ExcelProperty(value = "考勤日期")
    private Date attendanceDate;

    /** 上班时间 */
    @JsonFormat(pattern = "HH:mm:ss")
    @ExcelProperty(value = "上班时间")
    private Date checkInTime;

    /** 下班时间 */
    @JsonFormat(pattern = "HH:mm:ss")
    @ExcelProperty(value = "下班时间")
    private Date checkOutTime;

    /** 工作时长（小时） */
    @ExcelProperty(value = "工作时长")
    private Double workHours;

    /** 加班时长（小时） */
    @ExcelProperty(value = "加班时长")
    private Double overtimeHours;

    /** 迟到时长（分钟） */
    @ExcelProperty(value = "迟到时长")
    private Integer lateMinutes;

    /** 早退时长（分钟） */
    @ExcelProperty(value = "早退时长")
    private Integer earlyMinutes;

    /** 考勤状态：1-正常，2-迟到，3-早退，4-旷工，5-请假 */
    @ExcelProperty(value = "考勤状态")
    private Integer status;

    /** 请假类型：1-事假，2-病假，3-年假，4-调休，5-其他 */
    @ExcelProperty(value = "请假类型")
    private Integer leaveType;

    /** 异常说明 */
    @ExcelProperty(value = "异常说明")
    private String abnormalReason;


}