package com.chennian.storytelling.bean.model.hrm;


import com.alibaba.excel.annotation.ExcelProperty;
import com.chennian.storytelling.common.domain.BaseEntity;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serial;
import java.math.BigDecimal;

/**
 * 职位信息对象 hrm_position
 * 
 * @author storytelling
 * @date 2024-01-01
 */
@EqualsAndHashCode(callSuper = true)
@Data
public class HrmPosition extends BaseEntity {
    @Serial
    private static final long serialVersionUID = 1L;

    /** 职位ID */
    private Long id;

    /** 职位编码 */
    @ExcelProperty(value = "职位编码")
    private String positionCode;

    /** 职位名称 */
    @ExcelProperty(value = "职位名称")
    private String positionName;

    /** 职位级别 */
    @ExcelProperty(value = "职位级别")
    private Integer level;

    /** 所属部门ID */
    @ExcelProperty(value = "所属部门ID")
    private Long departmentId;

    /** 所属部门名称 */
    @ExcelProperty(value = "所属部门")
    private String departmentName;

    /** 职位描述 */
    @ExcelProperty(value = "职位描述")
    private String description;

    /** 职责要求 */
    @ExcelProperty(value = "职责要求")
    private String requirements;

    /** 最低薪资 */
    @ExcelProperty(value = "最低薪资")
    private BigDecimal minSalary;

    /** 最高薪资 */
    @ExcelProperty(value = "最高薪资")
    private BigDecimal maxSalary;

    /** 排序 */
    @ExcelProperty(value = "排序")
    private Integer orderNum;

    /** 状态：1-正常，0-停用 */
    @ExcelProperty(value = "状态")
    private Integer status;

}