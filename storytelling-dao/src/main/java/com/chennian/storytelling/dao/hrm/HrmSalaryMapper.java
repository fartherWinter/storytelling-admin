package com.chennian.storytelling.dao.hrm;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.chennian.storytelling.bean.model.hrm.HrmSalary;
import org.apache.ibatis.annotations.MapKey;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 薪资记录数据访问层
 * 
 * @author storytelling
 * @date 2024-01-01
 */
@Mapper
public interface HrmSalaryMapper extends BaseMapper<HrmSalary> {

    /**
     * 根据员工ID查询薪资记录
     * 
     * @param employeeId 员工ID
     * @return 薪资记录列表
     */
    List<HrmSalary> selectByEmployeeId(@Param("employeeId") Long employeeId);

    /**
     * 根据员工ID和薪资年月查询薪资记录
     * 
     * @param employeeId 员工ID
     * @param salaryMonth 薪资年月
     * @return 薪资记录
     */
    HrmSalary selectByEmployeeIdAndMonth(@Param("employeeId") Long employeeId, @Param("salaryMonth") String salaryMonth);

    /**
     * 根据部门ID查询薪资记录
     * 
     * @param departmentId 部门ID
     * @return 薪资记录列表
     */
    List<HrmSalary> selectByDepartmentId(@Param("departmentId") Long departmentId);

    /**
     * 根据薪资年月查询薪资记录
     * 
     * @param salaryMonth 薪资年月
     * @return 薪资记录列表
     */
    List<HrmSalary> selectBySalaryMonth(@Param("salaryMonth") String salaryMonth);

    /**
     * 根据发放状态查询薪资记录
     * 
     * @param payStatus 发放状态
     * @return 薪资记录列表
     */
    List<HrmSalary> selectByPayStatus(@Param("payStatus") Integer payStatus);

    /**
     * 查询薪资统计信息
     * 
     * @param salaryMonth 薪资年月
     * @return 统计信息
     */
    @MapKey("department_name")
    List<java.util.Map<String, Object>> selectSalaryStatistics(@Param("salaryMonth") String salaryMonth);

    /**
     * 查询员工年度薪资统计
     * 
     * @param employeeId 员工ID
     * @param year 年份
     * @return 年度薪资统计
     */
    @MapKey("salary_month")
    List<java.util.Map<String, Object>> selectYearlySalaryByEmployee(@Param("employeeId") Long employeeId, @Param("year") String year);

    /**
     * 查询部门薪资统计
     * 
     * @param departmentId 部门ID
     * @param salaryMonth 薪资年月
     * @return 部门薪资统计
     */
    java.util.Map<String, Object> selectDepartmentSalaryStatistics(@Param("departmentId") Long departmentId, @Param("salaryMonth") String salaryMonth);
}