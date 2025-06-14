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
 * 员工信息对象 hrm_employee
 * 
 * @author storytelling
 * @date 2024-01-01
 */
@EqualsAndHashCode(callSuper = true)
@Data
public class HrmEmployee extends BaseEntity {
    @Serial
    private static final long serialVersionUID = 1L;

    /** 员工ID */
    private Long id;

    /** 员工编号 */
    @ExcelProperty(value = "员工编号")
    private String employeeNo;

    /** 姓名 */
    @ExcelProperty(value = "姓名")
    private String name;

    /** 性别：1-男，2-女 */
    @ExcelProperty(value = "性别")
    private Integer gender;

    /** 出生日期 */
    @JsonFormat(pattern = "yyyy-MM-dd")
    @ExcelProperty(value = "出生日期")
    private Date birthDate;

    /** 身份证号 */
    @ExcelProperty(value = "身份证号")
    private String idCard;

    /** 手机号码 */
    @ExcelProperty(value = "手机号码")
    private String phone;

    /** 邮箱 */
    @ExcelProperty(value = "邮箱")
    private String email;

    /** 地址 */
    @ExcelProperty(value = "地址")
    private String address;

    /** 部门ID */
    @ExcelProperty(value = "部门ID")
    private Long departmentId;

    /** 部门名称 */
    @ExcelProperty(value = "部门名称")
    private String departmentName;

    /** 职位ID */
    @ExcelProperty(value = "职位ID")
    private Long positionId;

    /** 职位名称 */
    @ExcelProperty(value = "职位名称")
    private String positionName;

    /** 入职日期 */
    @JsonFormat(pattern = "yyyy-MM-dd")
    @ExcelProperty(value = "入职日期")
    private Date hireDate;

    @ExcelProperty(value = "离职原因")
    private String resignReason;

    @JsonFormat(pattern = "yyyy-MM-dd")
    @ExcelProperty(value = "离职原因")
    private Date resignDate;

    /** 用工类型：1-正式，2-试用，3-实习，4-兼职 */
    @ExcelProperty(value = "用工类型")
    private Integer employmentType;

    /** 状态：1-在职，2-离职，3-停薪留职 */
    @ExcelProperty(value = "状态")
    private Integer status;

    /** 基本工资 */
    @ExcelProperty(value = "基本工资")
    private BigDecimal salary;

    /** 紧急联系人 */
    @ExcelProperty(value = "紧急联系人")
    private String emergencyContact;

    /** 紧急联系电话 */
    @ExcelProperty(value = "紧急联系电话")
    private String emergencyPhone;


}