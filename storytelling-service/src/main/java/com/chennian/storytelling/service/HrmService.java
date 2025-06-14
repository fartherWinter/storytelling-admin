package com.chennian.storytelling.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.chennian.storytelling.bean.model.hrm.*;
import com.chennian.storytelling.common.utils.PageParam;
import com.chennian.storytelling.bean.vo.HrmAttendanceStatisticsVO;
import com.chennian.storytelling.bean.vo.HrmSalaryStatisticsVO;
import com.chennian.storytelling.bean.vo.HrmEmployeeMonthlyAttendanceVO;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * 人力资源管理服务接口
 * 
 * @author storytelling
 * @date 2024-01-01
 */
public interface HrmService {

    // ==================== 员工管理 ====================
    
    /**
     * 查询员工列表
     */
    IPage<HrmEmployee> getEmployeeList(HrmEmployee employee, PageParam<HrmEmployee> pageParam);
    
    /**
     * 根据ID查询员工信息
     */
    HrmEmployee getEmployeeById(Long id);

    @Transactional
    boolean saveEmployee(HrmEmployee employee);

    /**
     * 新增员工
     */
    boolean addEmployee(HrmEmployee employee);
    
    /**
     * 修改员工信息
     */
    boolean updateEmployee(HrmEmployee employee);
    
    /**
     * 删除员工
     */
    boolean deleteEmployee(Long[] ids);

    List<HrmSalary> getSalaryByEmployee(Long employeeId);

    /**
     * 员工离职处理
     */
    Boolean resignEmployee(Long employeeId, String resignReason);
    
    /**
     * 员工转岗
     */
    Boolean transferEmployee(Long employeeId, Long newDeptId, Long newPositionId);
    
    // ==================== 部门管理 ====================

    List<HrmEmployee> getEmployeesByDepartment(Long departmentId);

    List<HrmEmployee> getEmployeesByPosition(Long positionId);

    Map<String, Object> getEmployeeStatistics();

    /**
     * 查询部门列表
     */
    List<HrmDepartment> getDepartmentList(HrmDepartment department);
    
    /**
     * 根据ID查询部门信息
     */
    HrmDepartment getDepartmentById(Long id);

    @Transactional
    boolean saveDepartment(HrmDepartment department);

    /**
     * 新增部门
     */
    boolean addDepartment(HrmDepartment department);
    
    /**
     * 修改部门信息
     */
    boolean updateDepartment(HrmDepartment department);
    
    /**
     * 删除部门
     */
    boolean deleteDepartment(Long[] ids);
    
    /**
     * 获取部门树结构
     */
    List<HrmDepartment> getDepartmentTree();
    
    // ==================== 职位管理 ====================
    
    /**
     * 查询职位列表
     */
    List<HrmPosition> getPositionList(HrmPosition position);

    IPage<HrmPosition> getPositionList(Page<HrmPosition> page, HrmPosition position);

    /**
     * 根据ID查询职位信息
     */
    HrmPosition getPositionById(Long id);

    @Transactional
    boolean savePosition(HrmPosition position);

    /**
     * 新增职位
     */
    boolean addPosition(HrmPosition position);
    
    /**
     * 修改职位信息
     */
    boolean updatePosition(HrmPosition position);
    
    /**
     * 删除职位
     */
    boolean deletePosition(Long[] ids);
    
    /**
     * 根据部门ID查询职位列表
     */
    List<HrmPosition> getPositionsByDeptId(Long deptId);
    
    List<HrmPosition> getPositionsByDepartment(Long departmentId);
    
    // ==================== 考勤管理 ====================

    

    /**
     * 查询考勤记录列表
     */
    IPage<HrmAttendance> getAttendanceList(HrmAttendance attendance, PageParam<HrmAttendance> page);
    
    /**
     * 根据ID查询考勤记录
     */
    HrmAttendance getAttendanceById(Long id);
    
    /**
     * 员工签到
     */
    Boolean checkIn(Long employeeId);
    
    /**
     * 员工签退
     */
    Boolean checkOut(Long employeeId);

    @Transactional
    boolean saveAttendance(HrmAttendance attendance);

    /**
     * 新增考勤记录
     */
    boolean addAttendance(HrmAttendance attendance);
    
    /**
     * 修改考勤记录
     */
    boolean updateAttendance(HrmAttendance attendance);
    
    /**
     * 删除考勤记录
     */
    boolean deleteAttendance(Long[] ids);
    
    /**
     * 考勤统计
     */
    HrmAttendanceStatisticsVO getAttendanceStatistics(String startDate, String endDate, Long deptId);
    
    // ==================== 薪资管理 ====================

    List<HrmAttendance> getAttendanceByEmployee(Long employeeId, Date startDate, Date endDate);

    Map<String, Object> getAttendanceStatistics(Date startDate, Date endDate);

    /**
     * 查询薪资记录列表
     */
    IPage<HrmSalary> getSalaryList(HrmSalary salary, PageParam<HrmSalary> page);
    
    /**
     * 根据ID查询薪资记录
     */
    HrmSalary getSalaryById(Long id);

    @Transactional
    boolean saveSalary(HrmSalary salary);

    /**
     * 新增薪资记录
     */
    boolean addSalary(HrmSalary salary);
    
    /**
     * 修改薪资记录
     */
    boolean updateSalary(HrmSalary salary);
    
    /**
     * 删除薪资记录
     */
    boolean deleteSalary(Long[] ids);
    
    /**
     * 批量生成薪资
     */
    Integer generateSalaryBatch(String salaryMonth, Long[] employeeIds);
    
    /**
     * 薪资发放
     */
    Integer paySalary(Long[] salaryIds);
    
    /**
     * 薪资统计
     */
    HrmSalaryStatisticsVO getSalaryStatistics(String startMonth, String endMonth, Long deptId);

    HrmDepartment getDepartmentByCode(String departmentCode);

    HrmPosition getPositionByCode(String positionCode);

    Map<String, Object> getPositionStatistics();

    List<HrmAttendance> getAttendanceByDepartment(Long departmentId);

    /**
     * 获取员工月度考勤统计
     */
    HrmEmployeeMonthlyAttendanceVO getEmployeeMonthlyAttendance(Long employeeId, String yearMonth);

    List<HrmSalary> getEmployeeYearlySalary(Long employeeId, String year);

    Map<String, Object> getDepartmentSalaryStatistics(Long departmentId, String salaryYearMonth);

    List<HrmSalary> getSalaryByDepartment(Long departmentId);
}