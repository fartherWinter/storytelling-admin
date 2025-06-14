package com.chennian.storytelling.bean.model.hrm;

import com.alibaba.excel.annotation.ExcelProperty;
import com.chennian.storytelling.common.domain.BaseEntity;
import com.fasterxml.jackson.annotation.JsonFormat;

import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serial;
import java.math.BigDecimal;
import java.util.Date;

/**
 * 薪资记录对象 hrm_salary
 * 
 * @author storytelling
 * @date 2024-01-01
 */
@EqualsAndHashCode(callSuper = true)
@Data
public class HrmSalary extends BaseEntity {
    @Serial
    private static final long serialVersionUID = 1L;

    /** 薪资ID */
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

    /** 薪资年月 */
    @ExcelProperty(value = "薪资年月")
    private String salaryMonth;

    /** 基本工资 */
    @ExcelProperty(value = "基本工资")
    private BigDecimal baseSalary;

    /** 岗位工资 */
    @ExcelProperty(value = "岗位工资")
    private BigDecimal positionSalary;

    /** 绩效工资 */
    @ExcelProperty(value = "绩效工资")
    private BigDecimal performanceSalary;

    /** 加班工资 */
    @ExcelProperty(value = "加班工资")
    private BigDecimal overtimeSalary;

    /** 津贴补助 */
    @ExcelProperty(value = "津贴补助")
    private BigDecimal allowance;

    /** 奖金 */
    @ExcelProperty(value = "奖金")
    private BigDecimal bonus;

    /** 应发工资 */
    @ExcelProperty(value = "应发工资")
    private BigDecimal grossSalary;

    /** 社保个人部分 */
    @ExcelProperty(value = "社保个人部分")
    private BigDecimal socialInsurance;

    /** 公积金个人部分 */
    @ExcelProperty(value = "公积金个人部分")
    private BigDecimal housingFund;

    /** 个人所得税 */
    @ExcelProperty(value = "个人所得税")
    private BigDecimal incomeTax;

    /** 其他扣款 */
    @ExcelProperty(value = "其他扣款")
    private BigDecimal otherDeduction;

    /** 总扣款 */
    @ExcelProperty(value = "总扣款")
    private BigDecimal totalDeduction;

    /** 实发工资 */
    @ExcelProperty(value = "实发工资")
    private BigDecimal netSalary;

    /** 工作天数 */
    @ExcelProperty(value = "工作天数")
    private Integer workDays;

    /** 出勤天数 */
    @ExcelProperty(value = "出勤天数")
    private Integer attendanceDays;

    /** 加班小时数 */
    @ExcelProperty(value = "加班小时数")
    private Double overtimeHours;

    /** 发放状态：1-未发放，2-已发放 */
    @ExcelProperty(value = "发放状态")
    private Integer payStatus;

    /** 发放时间 */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @ExcelProperty(value = "发放时间")
    private Date payTime;


}