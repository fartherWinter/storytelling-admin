package com.chennian.storytelling.bean.model.hrm;

import com.alibaba.excel.annotation.ExcelProperty;
import com.chennian.storytelling.common.domain.BaseEntity;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

import java.io.Serial;
import java.util.List;

/**
 * 部门信息对象 hrm_department
 * 
 * @author storytelling
 * @date 2024-01-01
 */
@EqualsAndHashCode(callSuper = true)
@Data
public class HrmDepartment extends BaseEntity {
    @Serial
    private static final long serialVersionUID = 1L;

    /** 部门ID */
    private Long id;

    /** 部门编码 */
    @ExcelProperty(value = "部门编码")
    private String deptCode;

    /** 部门名称 */
    @ExcelProperty(value = "部门名称")
    private String deptName;

    /** 上级部门ID */
    @ExcelProperty(value = "上级部门ID")
    private Long parentId;

    /** 上级部门名称 */
    @ExcelProperty(value = "上级部门名称")
    private String parentName;

    /** 部门负责人ID */
    @ExcelProperty(value = "部门负责人ID")
    private Long managerId;

    /** 部门负责人姓名 */
    @ExcelProperty(value = "部门负责人")
    private String managerName;

    /** 联系电话 */
    @ExcelProperty(value = "联系电话")
    private String phone;

    /** 邮箱 */
    @ExcelProperty(value = "邮箱")
    private String email;

    /** 部门地址 */
    @ExcelProperty(value = "部门地址")
    private String address;

    /** 排序 */
    @ExcelProperty(value = "排序")
    private Integer orderNum;

    /** 状态：1-正常，0-停用 */
    @ExcelProperty(value = "状态")
    private Integer status;

    /** 部门层级 */
    @ExcelProperty(value = "部门层级")
    private Integer level;

    /** 部门路径 */
    @ExcelProperty(value = "部门路径")
    private String path;

    private List<HrmDepartment> children;
}