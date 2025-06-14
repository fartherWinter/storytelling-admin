package com.chennian.storytelling.common.enums;

import lombok.Getter;

/**
 * HRM模块错误码枚举
 * 错误码范围：9000-9999
 * 
 * @author storytelling
 * @date 2024-01-01
 */
@Getter
public enum HrmErrorCode {
    
    // ==================== 员工管理错误码 9000-9099 ====================
    /**
     * 员工保存失败
     */
    EMPLOYEE_SAVE_FAILED(9001, "员工保存失败"),
    
    /**
     * 员工更新失败
     */
    EMPLOYEE_UPDATE_FAILED(9002, "员工更新失败"),
    
    /**
     * 员工删除失败
     */
    EMPLOYEE_DELETE_FAILED(9003, "员工删除失败"),
    
    // ==================== 部门管理错误码 9100-9199 ====================
    /**
     * 部门保存失败
     */
    DEPARTMENT_SAVE_FAILED(9101, "部门保存失败"),
    
    /**
     * 部门更新失败
     */
    DEPARTMENT_UPDATE_FAILED(9102, "部门更新失败"),
    
    /**
     * 部门删除失败
     */
    DEPARTMENT_DELETE_FAILED(9103, "部门删除失败"),
    
    // ==================== 职位管理错误码 9200-9299 ====================
    /**
     * 职位保存失败
     */
    POSITION_SAVE_FAILED(9201, "职位保存失败"),
    
    /**
     * 职位更新失败
     */
    POSITION_UPDATE_FAILED(9202, "职位更新失败"),
    
    /**
     * 职位删除失败
     */
    POSITION_DELETE_FAILED(9203, "职位删除失败"),
    
    // ==================== 考勤管理错误码 9300-9399 ====================
    /**
     * 考勤记录保存失败
     */
    ATTENDANCE_SAVE_FAILED(9301, "考勤记录保存失败"),
    
    /**
     * 考勤记录更新失败
     */
    ATTENDANCE_UPDATE_FAILED(9302, "考勤记录更新失败"),
    
    /**
     * 考勤记录删除失败
     */
    ATTENDANCE_DELETE_FAILED(9303, "考勤记录删除失败"),
    
    // ==================== 薪资管理错误码 9400-9499 ====================
    /**
     * 薪资记录保存失败
     */
    SALARY_SAVE_FAILED(9401, "薪资记录保存失败"),
    
    /**
     * 薪资记录更新失败
     */
    SALARY_UPDATE_FAILED(9402, "薪资记录更新失败"),
    
    /**
     * 薪资记录删除失败
     */
    SALARY_DELETE_FAILED(9403, "薪资记录删除失败");
    
    /**
     * 错误码
     */
    private final Integer code;
    
    /**
     * 错误信息
     */
    private final String message;
    
    HrmErrorCode(Integer code, String message) {
        this.code = code;
        this.message = message;
    }
}