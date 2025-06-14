package com.chennian.storytelling.admin.controller;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.chennian.storytelling.bean.model.hrm.*;
import com.chennian.storytelling.common.annotation.EventTrack;
import com.chennian.storytelling.common.enums.BusinessType;
import com.chennian.storytelling.common.enums.HrmErrorCode;
import com.chennian.storytelling.bean.vo.HrmAttendanceStatisticsVO;
import com.chennian.storytelling.bean.vo.HrmSalaryStatisticsVO;
import com.chennian.storytelling.bean.vo.HrmEmployeeMonthlyAttendanceVO;
import com.chennian.storytelling.common.enums.ModelType;
import com.chennian.storytelling.common.enums.OperatorType;
import com.chennian.storytelling.common.response.ServerResponseEntity;
import com.chennian.storytelling.common.utils.PageParam;
import com.chennian.storytelling.service.HrmService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * 人力资源管理控制器
 * 
 * @author storytelling
 * @date 2024-01-01
 */
@Tag(name = "人力资源管理")
@RestController
@RequestMapping("/erp/hrm")
public class HrmController {

    @Autowired
    private HrmService hrmService;

    // ==================== 员工管理 ====================

    @Operation(summary = "分页查询员工列表")
    @PostMapping("/employee/list")
    @EventTrack(title = ModelType.SYSTEM, businessType = BusinessType.SEARCH, operatorType = OperatorType.MANAGE, description = "分页查询员工列表")
    public ServerResponseEntity<IPage<HrmEmployee>> getEmployeeList(
            @Parameter(description = "页码") @RequestParam(defaultValue = "1") Integer current,
            @Parameter(description = "每页大小") @RequestParam(defaultValue = "10") Integer size,
            @Parameter(description = "员工姓名") @RequestParam(required = false) String employeeName,
            @Parameter(description = "员工编号") @RequestParam(required = false) String employeeNo,
            @Parameter(description = "部门ID") @RequestParam(required = false) Long departmentId,
            @Parameter(description = "职位ID") @RequestParam(required = false) Long positionId,
            @Parameter(description = "状态") @RequestParam(required = false) Integer status) {
        
        PageParam<HrmEmployee> page = new PageParam<>();
        page.setCurrent(current);
        page.setSize(size);
        HrmEmployee employee = new HrmEmployee();
        employee.setName(employeeName);
        employee.setEmployeeNo(employeeNo);
        employee.setDepartmentId(departmentId);
        employee.setPositionId(positionId);
        employee.setStatus(status);
        
        IPage<HrmEmployee> result = hrmService.getEmployeeList(employee, page);
        return ServerResponseEntity.success(result);
    }

    @Operation(summary = "根据ID查询员工信息")
    @GetMapping("/employee/{id}")
    @EventTrack(title = ModelType.SYSTEM, businessType = BusinessType.SEARCH, operatorType = OperatorType.MANAGE, description = "根据ID查询员工信息")
    public ServerResponseEntity<HrmEmployee> getEmployeeById(@Parameter(description = "员工ID") @PathVariable Long id) {
        HrmEmployee employee = hrmService.getEmployeeById(id);
        return ServerResponseEntity.success(employee);
    }

    @Operation(summary = "保存员工信息")
    @PostMapping("/employee")
    @EventTrack(title = ModelType.SYSTEM, businessType = BusinessType.INSERT, operatorType = OperatorType.MANAGE, description = "保存员工信息")
    public ServerResponseEntity<String> saveEmployee(@Validated @RequestBody HrmEmployee employee) {
        boolean success = hrmService.saveEmployee(employee);
        return success ? ServerResponseEntity.success() : ServerResponseEntity.fail(HrmErrorCode.EMPLOYEE_SAVE_FAILED.getCode(), HrmErrorCode.EMPLOYEE_SAVE_FAILED.getMessage());
    }

    @Operation(summary = "更新员工信息")
    @PutMapping("/employee")
    @EventTrack(title = ModelType.SYSTEM, businessType = BusinessType.UPDATE, operatorType = OperatorType.MANAGE, description = "更新员工信息")
    public ServerResponseEntity<String> updateEmployee(@Validated @RequestBody HrmEmployee employee) {
        boolean success = hrmService.saveEmployee(employee);
        return success ? ServerResponseEntity.success() : ServerResponseEntity.fail(HrmErrorCode.EMPLOYEE_UPDATE_FAILED.getCode(), HrmErrorCode.EMPLOYEE_UPDATE_FAILED.getMessage());
    }

    @Operation(summary = "删除员工信息")
    @DeleteMapping("/employee/delete")
    @EventTrack(title = ModelType.SYSTEM, businessType = BusinessType.DELETE, operatorType = OperatorType.MANAGE, description = "删除员工信息")
    public ServerResponseEntity<String> deleteEmployee(@Parameter(description = "员工ID数组") @RequestParam Long[] ids) {
        boolean success = hrmService.deleteEmployee(ids);
        return success ? ServerResponseEntity.success() : ServerResponseEntity.fail(HrmErrorCode.EMPLOYEE_DELETE_FAILED.getCode(), HrmErrorCode.EMPLOYEE_DELETE_FAILED.getMessage());
    }

    @Operation(summary = "根据部门查询员工列表")
    @GetMapping("/employee/department/{departmentId}")
    @EventTrack(title = ModelType.SYSTEM, businessType = BusinessType.SEARCH, operatorType = OperatorType.MANAGE, description = "根据部门查询员工列表")
    public ServerResponseEntity<List<HrmEmployee>> getEmployeesByDepartment(@Parameter(description = "部门ID") @PathVariable Long departmentId) {
        List<HrmEmployee> employees = hrmService.getEmployeesByDepartment(departmentId);
        return ServerResponseEntity.success(employees);
    }

    @Operation(summary = "根据职位查询员工列表")
    @GetMapping("/employee/position/{positionId}")
    @EventTrack(title = ModelType.SYSTEM, businessType = BusinessType.SEARCH, operatorType = OperatorType.MANAGE, description = "根据职位查询员工列表")
    public ServerResponseEntity<List<HrmEmployee>> getEmployeesByPosition(@Parameter(description = "职位ID") @PathVariable Long positionId) {
        List<HrmEmployee> employees = hrmService.getEmployeesByPosition(positionId);
        return ServerResponseEntity.success(employees);
    }

    @Operation(summary = "获取员工统计信息")
    @GetMapping("/employee/statistics")
    @EventTrack(title = ModelType.SYSTEM, businessType = BusinessType.SEARCH, operatorType = OperatorType.MANAGE, description = "获取员工统计信息")
    public ServerResponseEntity<Map<String, Object>> getEmployeeStatistics() {
        Map<String, Object> statistics = hrmService.getEmployeeStatistics();
        return ServerResponseEntity.success(statistics);
    }

    // ==================== 部门管理 ====================

    @Operation(summary = "查询部门列表")
    @GetMapping("/department/list")
    @EventTrack(title = ModelType.SYSTEM, businessType = BusinessType.SEARCH, operatorType = OperatorType.MANAGE, description = "查询部门列表")
    public ServerResponseEntity<List<HrmDepartment>> getDepartmentList(
            @Parameter(description = "部门名称") @RequestParam(required = false) String departmentName,
            @Parameter(description = "部门编码") @RequestParam(required = false) String departmentCode,
            @Parameter(description = "上级部门ID") @RequestParam(required = false) Long parentId,
            @Parameter(description = "状态") @RequestParam(required = false) Integer status) {
        
        HrmDepartment department = new HrmDepartment();
        department.setDeptName(departmentName);
        department.setDeptCode(departmentCode);
        department.setParentId(parentId);
        department.setStatus(status);
        
        List<HrmDepartment> departments = hrmService.getDepartmentList(department);
        return ServerResponseEntity.success(departments);
    }

    @Operation(summary = "根据ID查询部门信息")
    @GetMapping("/department/{id}")
    @EventTrack(title = ModelType.SYSTEM, businessType = BusinessType.SEARCH, operatorType = OperatorType.MANAGE, description = "根据ID查询部门信息")
    public ServerResponseEntity<HrmDepartment> getDepartmentById(@Parameter(description = "部门ID") @PathVariable Long id) {
        HrmDepartment department = hrmService.getDepartmentById(id);
        return ServerResponseEntity.success(department);
    }

    @Operation(summary = "保存部门信息")
    @PostMapping("/department")
    @EventTrack(title = ModelType.SYSTEM, businessType = BusinessType.INSERT, operatorType = OperatorType.MANAGE, description = "保存部门信息")
    public ServerResponseEntity<String> saveDepartment(@Validated @RequestBody HrmDepartment department) {
        boolean success = hrmService.saveDepartment(department);
        return success ? ServerResponseEntity.success() : ServerResponseEntity.fail(HrmErrorCode.DEPARTMENT_SAVE_FAILED.getCode(), HrmErrorCode.DEPARTMENT_SAVE_FAILED.getMessage());
    }

    @Operation(summary = "更新部门信息")
    @PutMapping("/department")
    @EventTrack(title = ModelType.SYSTEM, businessType = BusinessType.UPDATE, operatorType = OperatorType.MANAGE, description = "更新部门信息")
    public ServerResponseEntity<String> updateDepartment(@Validated @RequestBody HrmDepartment department) {
        boolean success = hrmService.saveDepartment(department);
        return success ? ServerResponseEntity.success() : ServerResponseEntity.fail(HrmErrorCode.DEPARTMENT_UPDATE_FAILED.getCode(), HrmErrorCode.DEPARTMENT_UPDATE_FAILED.getMessage());
    }

    @Operation(summary = "删除部门信息")
    @DeleteMapping("/department/delete")
    @EventTrack(title = ModelType.SYSTEM, businessType = BusinessType.DELETE, operatorType = OperatorType.MANAGE, description = "删除部门信息")
    public ServerResponseEntity<String> deleteDepartment(@Parameter(description = "部门ID数组") @RequestParam Long[] ids) {
        boolean success = hrmService.deleteDepartment(ids);
        return success ? ServerResponseEntity.success() : ServerResponseEntity.fail(HrmErrorCode.DEPARTMENT_DELETE_FAILED.getCode(), HrmErrorCode.DEPARTMENT_DELETE_FAILED.getMessage());
    }

    @Operation(summary = "根据编码查询部门信息")
    @GetMapping("/department/code/{departmentCode}")
    @EventTrack(title = ModelType.SYSTEM, businessType = BusinessType.SEARCH, operatorType = OperatorType.MANAGE, description = "根据编码查询部门信息")
    public ServerResponseEntity<HrmDepartment> getDepartmentByCode(@Parameter(description = "部门编码") @PathVariable String departmentCode) {
        HrmDepartment department = hrmService.getDepartmentByCode(departmentCode);
        return ServerResponseEntity.success(department);
    }

    @Operation(summary = "获取部门树形结构")
    @GetMapping("/department/tree")
    @EventTrack(title = ModelType.SYSTEM, businessType = BusinessType.SEARCH, operatorType = OperatorType.MANAGE, description = "获取部门树形结构")
    public ServerResponseEntity<List<HrmDepartment>> getDepartmentTree() {
        List<HrmDepartment> tree = hrmService.getDepartmentTree();
        return ServerResponseEntity.success(tree);
    }

    // ==================== 职位管理 ====================

    @Operation(summary = "查询职位列表")
    @GetMapping("/position/list")
    @EventTrack(title = ModelType.SYSTEM, businessType = BusinessType.SEARCH, operatorType = OperatorType.MANAGE, description = "查询职位列表")
    public ServerResponseEntity<List<HrmPosition>> getPositionList(
            @Parameter(description = "职位名称") @RequestParam(required = false) String positionName,
            @Parameter(description = "职位编码") @RequestParam(required = false) String positionCode,
            @Parameter(description = "部门ID") @RequestParam(required = false) Long departmentId,
            @Parameter(description = "状态") @RequestParam(required = false) Integer status) {

        HrmPosition position = new HrmPosition();
        position.setPositionName(positionName);
        position.setPositionCode(positionCode);
        position.setDepartmentId(departmentId);
        position.setStatus(status);
        
        List<HrmPosition> positions = hrmService.getPositionList(position);
        return ServerResponseEntity.success(positions);
    }

    @Operation(summary = "根据ID查询职位信息")
    @GetMapping("/position/{id}")
    @EventTrack(title = ModelType.SYSTEM, businessType = BusinessType.SEARCH, operatorType = OperatorType.MANAGE, description = "根据ID查询职位信息")
    public ServerResponseEntity<HrmPosition> getPositionById(@Parameter(description = "职位ID") @PathVariable Long id) {
        HrmPosition position = hrmService.getPositionById(id);
        return ServerResponseEntity.success(position);
    }

    @Operation(summary = "保存职位信息")
    @PostMapping("/position")
    @EventTrack(title = ModelType.SYSTEM, businessType = BusinessType.INSERT, operatorType = OperatorType.MANAGE, description = "保存职位信息")
    public ServerResponseEntity<String> savePosition(@Validated @RequestBody HrmPosition position) {
        boolean success = hrmService.savePosition(position);
        return success ? ServerResponseEntity.success() : ServerResponseEntity.fail(HrmErrorCode.POSITION_SAVE_FAILED.getCode(), HrmErrorCode.POSITION_SAVE_FAILED.getMessage());
    }

    @Operation(summary = "更新职位信息")
    @PutMapping("/position")
    @EventTrack(title = ModelType.SYSTEM, businessType = BusinessType.UPDATE, operatorType = OperatorType.MANAGE, description = "更新职位信息")
    public ServerResponseEntity<String> updatePosition(@Validated @RequestBody HrmPosition position) {
        boolean success = hrmService.savePosition(position);
        return success ? ServerResponseEntity.success() : ServerResponseEntity.fail(HrmErrorCode.POSITION_UPDATE_FAILED.getCode(), HrmErrorCode.POSITION_UPDATE_FAILED.getMessage());
    }

    @Operation(summary = "删除职位信息")
    @DeleteMapping("/position/delete")
    @EventTrack(title = ModelType.SYSTEM, businessType = BusinessType.DELETE, operatorType = OperatorType.MANAGE, description = "删除职位信息")
    public ServerResponseEntity<String> deletePosition(@Parameter(description = "职位ID数组") @RequestParam Long[] ids) {
        boolean success = hrmService.deletePosition(ids);
        return success ? ServerResponseEntity.success() : ServerResponseEntity.fail(HrmErrorCode.POSITION_DELETE_FAILED.getCode(), HrmErrorCode.POSITION_DELETE_FAILED.getMessage());
    }

    @Operation(summary = "根据部门查询职位列表")
    @GetMapping("/position/department/{departmentId}")
    @EventTrack(title = ModelType.SYSTEM, businessType = BusinessType.SEARCH, operatorType = OperatorType.MANAGE, description = "根据部门查询职位列表")
    public ServerResponseEntity<List<HrmPosition>> getPositionsByDepartment(@Parameter(description = "部门ID") @PathVariable Long departmentId) {
        List<HrmPosition> positions = hrmService.getPositionsByDepartment(departmentId);
        return ServerResponseEntity.success(positions);
    }

    @Operation(summary = "根据编码查询职位信息")
    @GetMapping("/position/code/{positionCode}")
    @EventTrack(title = ModelType.SYSTEM, businessType = BusinessType.SEARCH, operatorType = OperatorType.MANAGE, description = "根据编码查询职位信息")
    public ServerResponseEntity<HrmPosition> getPositionByCode(@Parameter(description = "职位编码") @PathVariable String positionCode) {
        HrmPosition position = hrmService.getPositionByCode(positionCode);
        return ServerResponseEntity.success(position);
    }

    @Operation(summary = "获取职位统计信息")
    @GetMapping("/position/statistics")
    @EventTrack(title = ModelType.SYSTEM, businessType = BusinessType.SEARCH, operatorType = OperatorType.MANAGE, description = "获取职位统计信息")
    public ServerResponseEntity<Map<String, Object>> getPositionStatistics() {
        Map<String, Object> statistics = hrmService.getPositionStatistics();
        return ServerResponseEntity.success(statistics);
    }

    // ==================== 考勤管理 ====================

    @Operation(summary = "分页查询考勤记录列表")
    @PostMapping("/attendance/list")
    @EventTrack(title = ModelType.SYSTEM, businessType = BusinessType.SEARCH, operatorType = OperatorType.MANAGE, description = "分页查询考勤记录列表")
    public ServerResponseEntity<IPage<HrmAttendance>> getAttendanceList(
            @Parameter(description = "页码") @RequestParam(defaultValue = "1") Integer current,
            @Parameter(description = "每页大小") @RequestParam(defaultValue = "10") Integer size,
            @Parameter(description = "员工ID") @RequestParam(required = false) Long employeeId,
            @Parameter(description = "员工姓名") @RequestParam(required = false) String employeeName,
            @Parameter(description = "部门ID") @RequestParam(required = false) Long departmentId,
            @Parameter(description = "考勤日期") @RequestParam(required = false) @DateTimeFormat(pattern = "yyyy-MM-dd") Date attendanceDate,
            @Parameter(description = "状态") @RequestParam(required = false) Integer status) {
        
        PageParam<HrmAttendance> page = new PageParam<>();
        page.setCurrent(current);
        page.setSize(size);
        HrmAttendance attendance = new HrmAttendance();
        attendance.setEmployeeId(employeeId);
        attendance.setEmployeeName(employeeName);
        attendance.setDepartmentId(departmentId);
        attendance.setAttendanceDate(attendanceDate);
        attendance.setStatus(status);
        
        IPage<HrmAttendance> result = hrmService.getAttendanceList(attendance, page);
        return ServerResponseEntity.success(result);
    }

    @Operation(summary = "根据ID查询考勤记录")
    @GetMapping("/attendance/{id}")
    @EventTrack(title = ModelType.SYSTEM, businessType = BusinessType.SEARCH, operatorType = OperatorType.MANAGE, description = "根据ID查询考勤记录")
    public ServerResponseEntity<HrmAttendance> getAttendanceById(@Parameter(description = "考勤记录ID") @PathVariable Long id) {
        HrmAttendance attendance = hrmService.getAttendanceById(id);
        return ServerResponseEntity.success(attendance);
    }

    @Operation(summary = "保存考勤记录")
    @PostMapping("/attendance")
    @EventTrack(title = ModelType.SYSTEM, businessType = BusinessType.INSERT, operatorType = OperatorType.MANAGE, description = "保存考勤记录")
    public ServerResponseEntity<String> saveAttendance(@Validated @RequestBody HrmAttendance attendance) {
        boolean success = hrmService.saveAttendance(attendance);
        return success ? ServerResponseEntity.success() : ServerResponseEntity.fail(HrmErrorCode.ATTENDANCE_SAVE_FAILED.getCode(), HrmErrorCode.ATTENDANCE_SAVE_FAILED.getMessage());
    }

    @Operation(summary = "更新考勤记录")
    @PutMapping("/attendance")
    @EventTrack(title = ModelType.SYSTEM, businessType = BusinessType.UPDATE, operatorType = OperatorType.MANAGE, description = "更新考勤记录")
    public ServerResponseEntity<String> updateAttendance(@Validated @RequestBody HrmAttendance attendance) {
        boolean success = hrmService.saveAttendance(attendance);
        return success ? ServerResponseEntity.success() : ServerResponseEntity.fail(HrmErrorCode.ATTENDANCE_UPDATE_FAILED.getCode(), HrmErrorCode.ATTENDANCE_UPDATE_FAILED.getMessage());
    }

    @Operation(summary = "删除考勤记录")
    @DeleteMapping("/attendance/delete")
    @EventTrack(title = ModelType.SYSTEM, businessType = BusinessType.DELETE, operatorType = OperatorType.MANAGE, description = "删除考勤记录")
    public ServerResponseEntity<String> deleteAttendance(@Parameter(description = "考勤记录ID数组") @RequestParam Long[] ids) {
        boolean success = hrmService.deleteAttendance(ids);
        return success ? ServerResponseEntity.success() : ServerResponseEntity.fail(HrmErrorCode.ATTENDANCE_DELETE_FAILED.getCode(), HrmErrorCode.ATTENDANCE_DELETE_FAILED.getMessage());
    }

    @Operation(summary = "根据员工查询考勤记录")
    @GetMapping("/attendance/employee/{employeeId}")
    @EventTrack(title = ModelType.SYSTEM, businessType = BusinessType.SEARCH, operatorType = OperatorType.MANAGE, description = "根据员工查询考勤记录")
    public ServerResponseEntity<List<HrmAttendance>> getAttendanceByEmployee(
            @Parameter(description = "员工ID") @PathVariable Long employeeId,
            @Parameter(description = "开始日期") @RequestParam Date startDate,
            @Parameter(description = "结束日期") @RequestParam Date endDate) {
        List<HrmAttendance> attendances = hrmService.getAttendanceByEmployee(employeeId, startDate, endDate);
        return ServerResponseEntity.success(attendances);
    }

    @Operation(summary = "根据部门查询考勤记录")
    @GetMapping("/attendance/department/{departmentId}")
    @EventTrack(title = ModelType.SYSTEM, businessType = BusinessType.SEARCH, operatorType = OperatorType.MANAGE, description = "根据部门查询考勤记录")
    public ServerResponseEntity<List<HrmAttendance>> getAttendanceByDepartment(@Parameter(description = "部门ID") @PathVariable Long departmentId) {
        List<HrmAttendance> attendances = hrmService.getAttendanceByDepartment(departmentId);
        return ServerResponseEntity.success(attendances);
    }

    @Operation(summary = "获取考勤统计信息")
    @GetMapping("/attendance/statistics")
    @EventTrack(title = ModelType.SYSTEM, businessType = BusinessType.SEARCH, operatorType = OperatorType.MANAGE, description = "获取考勤统计信息")
    public ServerResponseEntity<HrmAttendanceStatisticsVO> getAttendanceStatistics(
            @Parameter(description = "开始日期") @RequestParam String startDate,
            @Parameter(description = "结束日期") @RequestParam String endDate,
            @Parameter(description = "部门ID") @RequestParam(required = false) Long departmentId) {
        HrmAttendanceStatisticsVO result = hrmService.getAttendanceStatistics(startDate, endDate, departmentId);
        return ServerResponseEntity.success(result);
    }

    @Operation(summary = "获取员工月度考勤统计")
    @GetMapping("/attendance/employee-monthly/{employeeId}")
    @EventTrack(title = ModelType.SYSTEM, businessType = BusinessType.SEARCH, operatorType = OperatorType.MANAGE, description = "获取员工月度考勤统计")
    public ServerResponseEntity<HrmEmployeeMonthlyAttendanceVO> getEmployeeMonthlyAttendance(
            @Parameter(description = "员工ID") @PathVariable Long employeeId,
            @Parameter(description = "年月") @RequestParam String yearMonth) {
        HrmEmployeeMonthlyAttendanceVO statistics = hrmService.getEmployeeMonthlyAttendance(employeeId, yearMonth);
        return ServerResponseEntity.success(statistics);
    }

    // ==================== 薪资管理 ====================

    @Operation(summary = "分页查询薪资记录列表")
    @PostMapping("/salary/list")
    @EventTrack(title = ModelType.SYSTEM, businessType = BusinessType.SEARCH, operatorType = OperatorType.MANAGE, description = "分页查询薪资记录列表")
    public ServerResponseEntity<IPage<HrmSalary>> getSalaryList(
            @Parameter(description = "页码") @RequestParam(defaultValue = "1") Integer current,
            @Parameter(description = "每页大小") @RequestParam(defaultValue = "10") Integer size,
            @Parameter(description = "员工ID") @RequestParam(required = false) Long employeeId,
            @Parameter(description = "员工姓名") @RequestParam(required = false) String employeeName,
            @Parameter(description = "部门ID") @RequestParam(required = false) Long departmentId,
            @Parameter(description = "薪资年月") @RequestParam(required = false) String salaryYearMonth,
            @Parameter(description = "状态") @RequestParam(required = false) Integer status) {
        
        PageParam<HrmSalary> page = new PageParam<>();
        page.setCurrent(current);
        page.setSize(size);
        HrmSalary salary = new HrmSalary();
        salary.setEmployeeId(employeeId);
        salary.setEmployeeName(employeeName);
        salary.setDepartmentId(departmentId);
        salary.setSalaryMonth(salaryYearMonth);
        salary.setPayStatus(status);
        
        IPage<HrmSalary> result = hrmService.getSalaryList(salary, page);
        return ServerResponseEntity.success(result);
    }

    @Operation(summary = "根据ID查询薪资记录")
    @GetMapping("/salary/{id}")
    @EventTrack(title = ModelType.SYSTEM, businessType = BusinessType.SEARCH, operatorType = OperatorType.MANAGE, description = "根据ID查询薪资记录")
    public ServerResponseEntity<HrmSalary> getSalaryById(@Parameter(description = "薪资记录ID") @PathVariable Long id) {
        HrmSalary salary = hrmService.getSalaryById(id);
        return ServerResponseEntity.success(salary);
    }

    @Operation(summary = "保存薪资记录")
    @PostMapping("/salary")
    @EventTrack(title = ModelType.SYSTEM, businessType = BusinessType.INSERT, operatorType = OperatorType.MANAGE, description = "保存薪资记录")
    public ServerResponseEntity<String> saveSalary(@Validated @RequestBody HrmSalary salary) {
        boolean success = hrmService.saveSalary(salary);
        return success ? ServerResponseEntity.success() : ServerResponseEntity.fail(HrmErrorCode.SALARY_SAVE_FAILED.getCode(), HrmErrorCode.SALARY_SAVE_FAILED.getMessage());
    }

    @Operation(summary = "更新薪资记录")
    @PutMapping("/salary")
    @EventTrack(title = ModelType.SYSTEM, businessType = BusinessType.UPDATE, operatorType = OperatorType.MANAGE, description = "更新薪资记录")
    public ServerResponseEntity<String> updateSalary(@Validated @RequestBody HrmSalary salary) {
        boolean success = hrmService.saveSalary(salary);
        return success ? ServerResponseEntity.success() : ServerResponseEntity.fail(HrmErrorCode.SALARY_UPDATE_FAILED.getCode(), HrmErrorCode.SALARY_UPDATE_FAILED.getMessage());
    }

    @Operation(summary = "删除薪资记录")
    @DeleteMapping("/salary/delete")
    @EventTrack(title = ModelType.SYSTEM, businessType = BusinessType.DELETE, operatorType = OperatorType.MANAGE, description = "删除薪资记录")
    public ServerResponseEntity<String> deleteSalary(@Parameter(description = "薪资记录ID数组") @RequestParam Long[] ids) {
        boolean success = hrmService.deleteSalary(ids);
        return success ? ServerResponseEntity.success() : ServerResponseEntity.fail(HrmErrorCode.SALARY_DELETE_FAILED.getCode(), HrmErrorCode.SALARY_DELETE_FAILED.getMessage());
    }

    @Operation(summary = "根据员工查询薪资记录")
    @GetMapping("/salary/employee/{employeeId}")
    @EventTrack(title = ModelType.SYSTEM, businessType = BusinessType.SEARCH, operatorType = OperatorType.MANAGE, description = "根据员工查询薪资记录")
    public ServerResponseEntity<List<HrmSalary>> getSalaryByEmployee(@Parameter(description = "员工ID") @PathVariable Long employeeId) {
        List<HrmSalary> salaries = hrmService.getSalaryByEmployee(employeeId);
        return ServerResponseEntity.success(salaries);
    }

    @Operation(summary = "根据部门查询薪资记录")
    @GetMapping("/salary/department/{departmentId}")
    @EventTrack(title = ModelType.SYSTEM, businessType = BusinessType.SEARCH, operatorType = OperatorType.MANAGE, description = "根据部门查询薪资记录")
    public ServerResponseEntity<List<HrmSalary>> getSalaryByDepartment(@Parameter(description = "部门ID") @PathVariable Long departmentId) {
        List<HrmSalary> salaries = hrmService.getSalaryByDepartment(departmentId);
        return ServerResponseEntity.success(salaries);
    }

    @Operation(summary = "获取薪资统计信息")
    @GetMapping("/salary/statistics")
    @EventTrack(title = ModelType.SYSTEM, businessType = BusinessType.SEARCH, operatorType = OperatorType.MANAGE, description = "获取薪资统计信息")
    public ServerResponseEntity<HrmSalaryStatisticsVO> getSalaryStatistics(
            @Parameter(description = "开始月份") @RequestParam String startMonth,
            @Parameter(description = "结束月份") @RequestParam String endMonth,
            @Parameter(description = "部门ID") @RequestParam(required = false) Long departmentId) {
        HrmSalaryStatisticsVO result = hrmService.getSalaryStatistics(startMonth, endMonth, departmentId);
        return ServerResponseEntity.success(result);
    }

    @Operation(summary = "获取员工年度薪资统计")
    @GetMapping("/salary/employee-yearly/{employeeId}")
    @EventTrack(title = ModelType.SYSTEM, businessType = BusinessType.SEARCH, operatorType = OperatorType.MANAGE, description = "获取员工年度薪资统计")
    public ServerResponseEntity<List<HrmSalary>> getEmployeeYearlySalary(
            @Parameter(description = "员工ID") @PathVariable Long employeeId,
            @Parameter(description = "年份") @RequestParam String year) {
        List<HrmSalary> salaries = hrmService.getEmployeeYearlySalary(employeeId, year);
        return ServerResponseEntity.success(salaries);
    }

    @Operation(summary = "获取部门薪资统计")
    @GetMapping("/salary/department-statistics/{departmentId}")
    @EventTrack(title = ModelType.SYSTEM, businessType = BusinessType.SEARCH, operatorType = OperatorType.MANAGE, description = "获取部门薪资统计")
    public ServerResponseEntity<Map<String, Object>> getDepartmentSalaryStatistics(
            @Parameter(description = "部门ID") @PathVariable Long departmentId,
            @Parameter(description = "薪资年月") @RequestParam String salaryYearMonth) {
        Map<String, Object> statistics = hrmService.getDepartmentSalaryStatistics(departmentId, salaryYearMonth);
        return ServerResponseEntity.success(statistics);
    }
}