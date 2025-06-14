package com.chennian.storytelling.service.impl.hrm;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.chennian.storytelling.bean.model.hrm.*;
import com.chennian.storytelling.bean.vo.HrmAttendanceStatisticsVO;
import com.chennian.storytelling.bean.vo.HrmSalaryStatisticsVO;
import com.chennian.storytelling.bean.vo.HrmEmployeeMonthlyAttendanceVO;
import com.chennian.storytelling.common.exception.StorytellingBindException;
import com.chennian.storytelling.common.utils.PageParam;
import com.chennian.storytelling.dao.hrm.*;
import com.chennian.storytelling.service.HrmService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.*;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

/**
 * 人力资源管理服务实现类
 * 
 * @author storytelling
 * @date 2024-01-01
 */
@Slf4j
@Service
public class HrmServiceImpl implements HrmService {

    @Autowired
    private HrmEmployeeMapper employeeMapper;

    @Autowired
    private HrmDepartmentMapper departmentMapper;

    @Autowired
    private HrmPositionMapper positionMapper;

    @Autowired
    private HrmAttendanceMapper attendanceMapper;

    @Autowired
    private HrmSalaryMapper salaryMapper;

    // ==================== 员工管理 ====================

    @Override
    public IPage<HrmEmployee> getEmployeeList(HrmEmployee employee, PageParam<HrmEmployee> page) {
        LambdaQueryWrapper<HrmEmployee> wrapper = new LambdaQueryWrapper<>();
        
        if (StringUtils.hasText(employee.getName())) {
            wrapper.like(HrmEmployee::getName, employee.getName());
        }
        if (StringUtils.hasText(employee.getEmployeeNo())) {
            wrapper.like(HrmEmployee::getEmployeeNo, employee.getEmployeeNo());
        }
        if (employee.getDepartmentId() != null) {
            wrapper.eq(HrmEmployee::getDepartmentId, employee.getDepartmentId());
        }
        if (employee.getPositionId() != null) {
            wrapper.eq(HrmEmployee::getPositionId, employee.getPositionId());
        }
        if (employee.getStatus() != null) {
            wrapper.eq(HrmEmployee::getStatus, employee.getStatus());
        }
        
        wrapper.orderByDesc(HrmEmployee::getCreateTime);
        
        IPage<HrmEmployee> result = employeeMapper.selectPage(page, wrapper);
        
        // 填充部门和职位名称
        result.getRecords().forEach(emp -> {
            if (emp.getDepartmentId() != null) {
                HrmDepartment dept = departmentMapper.selectById(emp.getDepartmentId());
                if (dept != null) {
                    emp.setDepartmentName(dept.getDeptName());
                }
            }
            if (emp.getPositionId() != null) {
                HrmPosition position = positionMapper.selectById(emp.getPositionId());
                if (position != null) {
                    emp.setPositionName(position.getPositionName());
                }
            }
        });

        return result;
    }

    @Override
    public HrmEmployee getEmployeeById(Long id) {
        HrmEmployee employee = employeeMapper.selectById(id);
        if (employee != null) {
            // 填充部门和职位信息
            if (employee.getDepartmentId() != null) {
                HrmDepartment dept = departmentMapper.selectById(employee.getDepartmentId());
                if (dept != null) {
                    employee.setDepartmentName(dept.getDeptName());
                }
            }
            if (employee.getPositionId() != null) {
                HrmPosition position = positionMapper.selectById(employee.getPositionId());
                if (position != null) {
                    employee.setPositionName(position.getPositionName());
                }
            }
        }
        return employee;
    }

    @Override
    @Transactional
    public boolean saveEmployee(HrmEmployee employee) {
        // 检查员工编号是否重复
        if (StringUtils.hasText(employee.getEmployeeNo())) {
            HrmEmployee existing = employeeMapper.selectByEmployeeNo(employee.getEmployeeNo());
            if (existing != null && !existing.getId().equals(employee.getId())) {
                throw new RuntimeException("员工编号已存在");
            }
        }
        
        if (employee.getId() == null) {
            employee.setCreateTime(new Date());
            return employeeMapper.insert(employee) > 0;
        } else {
            employee.setUpdateTime(new Date());
            return employeeMapper.updateById(employee) > 0;
        }
    }

    @Override
    @Transactional
    public boolean addEmployee(HrmEmployee employee) {
        // 检查员工编号是否重复
        if (StringUtils.hasText(employee.getEmployeeNo())) {
            HrmEmployee existing = employeeMapper.selectByEmployeeNo(employee.getEmployeeNo());
            if (existing != null) {
                throw new RuntimeException("员工编号已存在");
            }
        }
        
        employee.setCreateTime(new Date());
        return employeeMapper.insert(employee) > 0;
    }

    @Override
    @Transactional
    public boolean updateEmployee(HrmEmployee employee) {
        if (employee.getId() == null) {
            throw new RuntimeException("员工ID不能为空");
        }
        
        // 检查员工编号是否重复
        if (StringUtils.hasText(employee.getEmployeeNo())) {
            HrmEmployee existing = employeeMapper.selectByEmployeeNo(employee.getEmployeeNo());
            if (existing != null && !existing.getId().equals(employee.getId())) {
                throw new RuntimeException("员工编号已存在");
            }
        }
        
        employee.setUpdateTime(new Date());
        return employeeMapper.updateById(employee) > 0;
    }

    @Override
    @Transactional
    public boolean deleteEmployee(Long[] ids) {
        if (ids == null || ids.length == 0) {
            return false;
        }
        
        int deletedCount = 0;
        for (Long id : ids) {
            if (id != null) {
                deletedCount += employeeMapper.deleteById(id);
            }
        }
        
        return deletedCount > 0;
    }

    @Override
    public List<HrmEmployee> getEmployeesByDepartment(Long departmentId) {
        return employeeMapper.selectByDepartmentId(departmentId);
    }

    @Override
    public List<HrmEmployee> getEmployeesByPosition(Long positionId) {
        return employeeMapper.selectByPositionId(positionId);
    }

    @Override
    public Map<String, Object> getEmployeeStatistics() {
        Map<String, Object> result = new HashMap<>();
        
        // 总员工数
        Long totalCount = employeeMapper.selectCount(null);
        result.put("totalCount", totalCount);
        
        // 在职员工数
        Long activeCount = employeeMapper.selectCount(new LambdaQueryWrapper<HrmEmployee>().eq(HrmEmployee::getStatus, 1));
        result.put("activeCount", activeCount);
        
        // 离职员工数
        Long resignedCount = employeeMapper.selectCount(new LambdaQueryWrapper<HrmEmployee>().eq(HrmEmployee::getStatus, 2));
        result.put("resignedCount", resignedCount);
        
        // 按部门统计
        List<Map<String, Object>> deptStats = employeeMapper.selectEmployeeStatistics();
        result.put("departmentStatistics", deptStats);
        
        return result;
    }

    // ==================== 部门管理 ====================

    @Override
    public List<HrmDepartment> getDepartmentList(HrmDepartment department) {
        LambdaQueryWrapper<HrmDepartment> wrapper = new LambdaQueryWrapper<>();
        
        if (StringUtils.hasText(department.getDeptName())) {
            wrapper.like(HrmDepartment::getDeptName, department.getDeptName());
        }
        if (StringUtils.hasText(department.getDeptCode())) {
            wrapper.like(HrmDepartment::getDeptCode, department.getDeptCode());
        }
        if (department.getParentId() != null) {
            wrapper.eq(HrmDepartment::getParentId, department.getParentId());
        }
        if (department.getStatus() != null) {
            wrapper.eq(HrmDepartment::getStatus, department.getStatus());
        }
        
        wrapper.orderByAsc(HrmDepartment::getOrderNum);
        
        List<HrmDepartment> list = departmentMapper.selectList(wrapper);
        
        // 填充上级部门名称和负责人姓名
        list.forEach(dept -> {
            if (dept.getParentId() != null && dept.getParentId() != 0) {
                HrmDepartment parent = departmentMapper.selectById(dept.getParentId());
                if (parent != null) {
                    dept.setParentName(parent.getDeptName());
                }
            }
            if (dept.getManagerId() != null) {
                HrmEmployee manager = employeeMapper.selectById(dept.getManagerId());
                if (manager != null) {
                    dept.setManagerName(manager.getName());
                }
            }
        });
        
        return list;
    }

    @Override
    public HrmDepartment getDepartmentById(Long id) {
        HrmDepartment department = departmentMapper.selectById(id);
        if (department != null) {
            // 填充上级部门名称和负责人姓名
            if (department.getParentId() != null && department.getParentId() != 0) {
                HrmDepartment parent = departmentMapper.selectById(department.getParentId());
                if (parent != null) {
                    department.setParentName(parent.getDeptName());
                }
            }
            if (department.getManagerId() != null) {
                HrmEmployee manager = employeeMapper.selectById(department.getManagerId());
                if (manager != null) {
                    department.setManagerName(manager.getName());
                }
            }
        }
        return department;
    }

    @Override
    @Transactional
    public boolean saveDepartment(HrmDepartment department) {
        // 检查部门编码是否重复
        if (StringUtils.hasText(department.getDeptCode())) {
            HrmDepartment existing = departmentMapper.selectByDeptCode(department.getDeptCode());
            if (existing != null && !existing.getId().equals(department.getId())) {
                throw new RuntimeException("部门编码已存在");
            }
        }
        
        if (department.getId() == null) {
            department.setCreateTime(new Date());
            return departmentMapper.insert(department) > 0;
        } else {
            department.setUpdateTime(new Date());
            return departmentMapper.updateById(department) > 0;
        }
    }

    @Override
    @Transactional
    public boolean addDepartment(HrmDepartment department) {
        // 检查部门编码是否重复
        if (StringUtils.hasText(department.getDeptCode())) {
            HrmDepartment existing = departmentMapper.selectByDeptCode(department.getDeptCode());
            if (existing != null) {
                throw new RuntimeException("部门编码已存在");
            }
        }
        
        department.setCreateTime(new Date());
        return departmentMapper.insert(department) > 0;
    }

    @Override
    @Transactional
    public boolean updateDepartment(HrmDepartment department) {
        if (department.getId() == null) {
            throw new RuntimeException("部门ID不能为空");
        }
        
        // 检查部门编码是否重复
        if (StringUtils.hasText(department.getDeptCode())) {
            HrmDepartment existing = departmentMapper.selectByDeptCode(department.getDeptCode());
            if (existing != null && !existing.getId().equals(department.getId())) {
                throw new RuntimeException("部门编码已存在");
            }
        }
        
        department.setUpdateTime(new Date());
        return departmentMapper.updateById(department) > 0;
    }

    @Override
    @Transactional
    public boolean deleteDepartment(Long[] ids) {
        if (ids == null || ids.length == 0) {
            return false;
        }
        
        int deletedCount = 0;
        for (Long id : ids) {
            if (id != null) {
                // 检查是否有子部门
                List<HrmDepartment> children = departmentMapper.selectByParentId(id);
                if (!children.isEmpty()) {
                    throw new RuntimeException("部门ID:" + id + " 存在子部门，无法删除");
                }
                
                // 检查是否有员工
                List<HrmEmployee> employees = employeeMapper.selectByDepartmentId(id);
                if (!employees.isEmpty()) {
                    throw new RuntimeException("部门ID:" + id + " 下存在员工，无法删除");
                }
                
                deletedCount += departmentMapper.deleteById(id);
            }
        }
        
        return deletedCount > 0;
    }



    // ==================== 职位管理 ====================

    @Override
    public IPage<HrmPosition> getPositionList(Page<HrmPosition> page, HrmPosition position) {
        LambdaQueryWrapper<HrmPosition> wrapper = new LambdaQueryWrapper<>();
        
        if (StringUtils.hasText(position.getPositionName())) {
            wrapper.like(HrmPosition::getPositionName, position.getPositionName());
        }
        if (StringUtils.hasText(position.getPositionCode())) {
            wrapper.like(HrmPosition::getPositionCode, position.getPositionCode());
        }
        if (position.getDepartmentId() != null) {
            wrapper.eq(HrmPosition::getDepartmentId, position.getDepartmentId());
        }
        if (position.getLevel() != null) {
            wrapper.eq(HrmPosition::getLevel, position.getLevel());
        }
        if (position.getStatus() != null) {
            wrapper.eq(HrmPosition::getStatus, position.getStatus());
        }
        
        wrapper.orderByAsc(HrmPosition::getOrderNum);
        
        IPage<HrmPosition> result = positionMapper.selectPage(page, wrapper);
        
        // 填充部门名称
        result.getRecords().forEach(pos -> {
            if (pos.getDepartmentId() != null) {
                HrmDepartment dept = departmentMapper.selectById(pos.getDepartmentId());
                if (dept != null) {
                    pos.setDepartmentName(dept.getDeptName());
                }
            }
        });
        
        return result;
    }

    @Override
    public HrmPosition getPositionById(Long id) {
        HrmPosition position = positionMapper.selectById(id);
        if (position != null && position.getDepartmentId() != null) {
            HrmDepartment dept = departmentMapper.selectById(position.getDepartmentId());
            if (dept != null) {
                position.setDepartmentName(dept.getDeptName());
            }
        }
        return position;
    }

    @Override
    @Transactional
    public boolean savePosition(HrmPosition position) {
        // 检查职位编码是否重复
        if (StringUtils.hasText(position.getPositionCode())) {
            HrmPosition existing = positionMapper.selectByPositionCode(position.getPositionCode());
            if (existing != null && !existing.getId().equals(position.getId())) {
                throw new RuntimeException("职位编码已存在");
            }
        }
        
        if (position.getId() == null) {
            position.setCreateTime(new Date());
            return positionMapper.insert(position) > 0;
        } else {
            position.setUpdateTime(new Date());
            return positionMapper.updateById(position) > 0;
        }
    }

    @Override
    @Transactional
    public boolean addPosition(HrmPosition position) {
        // 检查职位编码是否重复
        if (StringUtils.hasText(position.getPositionCode())) {
            HrmPosition existing = positionMapper.selectByPositionCode(position.getPositionCode());
            if (existing != null) {
                throw new RuntimeException("职位编码已存在");
            }
        }
        
        position.setCreateTime(new Date());
        return positionMapper.insert(position) > 0;
    }

    @Override
    @Transactional
    public boolean updatePosition(HrmPosition position) {
        if (position.getId() == null) {
            throw new RuntimeException("职位ID不能为空");
        }
        
        // 检查职位编码是否重复
        if (StringUtils.hasText(position.getPositionCode())) {
            HrmPosition existing = positionMapper.selectByPositionCode(position.getPositionCode());
            if (existing != null && !existing.getId().equals(position.getId())) {
                throw new RuntimeException("职位编码已存在");
            }
        }
        
        position.setUpdateTime(new Date());
        return positionMapper.updateById(position) > 0;
    }

    @Override
    @Transactional
    public boolean deletePosition(Long[] ids) {
        if (ids == null || ids.length == 0) {
            return false;
        }
        
        int deletedCount = 0;
        for (Long id : ids) {
            if (id != null) {
                // 检查是否有员工使用该职位
                List<HrmEmployee> employees = employeeMapper.selectByPositionId(id);
                if (!employees.isEmpty()) {
                    throw new RuntimeException("职位ID:" + id + " 下存在员工，无法删除");
                }
                
                deletedCount += positionMapper.deleteById(id);
            }
        }
        
        return deletedCount > 0;
    }

    @Override
    public List<HrmPosition> getPositionsByDepartment(Long departmentId) {
        return positionMapper.selectByDepartmentId(departmentId);
    }

    // ==================== 考勤管理 ====================

    @Override
    public IPage<HrmAttendance> getAttendanceList(HrmAttendance attendance, PageParam<HrmAttendance> page) {
        LambdaQueryWrapper<HrmAttendance> wrapper = new LambdaQueryWrapper<>();
        
        if (attendance.getEmployeeId() != null) {
            wrapper.eq(HrmAttendance::getEmployeeId, attendance.getEmployeeId());
        }
        if (StringUtils.hasText(attendance.getEmployeeName())) {
            wrapper.like(HrmAttendance::getEmployeeName, attendance.getEmployeeName());
        }
        if (attendance.getDepartmentId() != null) {
            wrapper.eq(HrmAttendance::getDepartmentId, attendance.getDepartmentId());
        }
        if (attendance.getAttendanceDate() != null) {
            wrapper.eq(HrmAttendance::getAttendanceDate, attendance.getAttendanceDate());
        }
        if (attendance.getStatus() != null) {
            wrapper.eq(HrmAttendance::getStatus, attendance.getStatus());
        }
        
        wrapper.orderByDesc(HrmAttendance::getAttendanceDate);

        return attendanceMapper.selectPage(page, wrapper);
    }

    @Override
    public HrmAttendance getAttendanceById(Long id) {
        return attendanceMapper.selectById(id);
    }

    @Override
    @Transactional
    public boolean saveAttendance(HrmAttendance attendance) {
        if (attendance.getId() == null) {
            attendance.setCreateTime(new Date());
            return attendanceMapper.insert(attendance) > 0;
        } else {
            attendance.setUpdateTime(new Date());
            return attendanceMapper.updateById(attendance) > 0;
        }
    }

    @Override
    @Transactional
    public boolean addAttendance(HrmAttendance attendance) {
        attendance.setCreateTime(new Date());
        return attendanceMapper.insert(attendance) > 0;
    }

    @Override
    @Transactional
    public boolean updateAttendance(HrmAttendance attendance) {
        if (attendance.getId() == null) {
            throw new RuntimeException("考勤记录ID不能为空");
        }
        
        attendance.setUpdateTime(new Date());
        return attendanceMapper.updateById(attendance) > 0;
    }

    @Override
    @Transactional
    public boolean deleteAttendance(Long[] ids) {
        if (ids == null || ids.length == 0) {
            return false;
        }
        
        int deletedCount = 0;
        for (Long id : ids) {
            if (id != null) {
                deletedCount += attendanceMapper.deleteById(id);
            }
        }
        
        return deletedCount > 0;
    }

    @Override
    public List<HrmAttendance> getAttendanceByEmployee(Long employeeId, Date startDate, Date endDate) {
        return attendanceMapper.selectByEmployeeIdAndDateRange(employeeId, startDate, endDate);
    }

    @Override
    public Map<String, Object> getAttendanceStatistics(Date startDate, Date endDate) {
        List<Map<String, Object>> stats = attendanceMapper.selectAttendanceStatistics(startDate, endDate);
        Map<String, Object> result = new HashMap<>();
        result.put("statistics", stats);
        return result;
    }

    @Override
    public List<HrmAttendance> getAttendanceByDepartment(Long departmentId) {
        LambdaQueryWrapper<HrmAttendance> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(HrmAttendance::getDepartmentId, departmentId);
        return attendanceMapper.selectList(wrapper);
    }

    @Override
    public List<HrmSalary> getEmployeeYearlySalary(Long employeeId, String year) {
        List<HrmSalary> salaries = salaryMapper.selectList(
            new LambdaQueryWrapper<HrmSalary>()
                .eq(HrmSalary::getEmployeeId, employeeId)
                .like(HrmSalary::getSalaryMonth, year + "%")
        );
        return salaries;
    }

    @Override
    public Map<String, Object> getDepartmentSalaryStatistics(Long departmentId, String salaryYearMonth) {
        Map<String, Object> statistics = new HashMap<>();
        LambdaQueryWrapper<HrmSalary> wrapper = new LambdaQueryWrapper<HrmSalary>()
            .eq(HrmSalary::getDepartmentId, departmentId);
        if (StringUtils.hasText(salaryYearMonth)) {
            wrapper.eq(HrmSalary::getSalaryMonth, salaryYearMonth);
        }
        List<HrmSalary> salaries = salaryMapper.selectList(wrapper);

        BigDecimal totalSalary = salaries.stream().map(HrmSalary::getGrossSalary).reduce(BigDecimal.ZERO, BigDecimal::add);
        long employeeCount = salaries.stream().map(HrmSalary::getEmployeeId).distinct().count();
        BigDecimal averageSalary = employeeCount > 0 ? totalSalary.divide(BigDecimal.valueOf(employeeCount), 2, RoundingMode.HALF_DOWN) : BigDecimal.ZERO;

        statistics.put("departmentId", departmentId);
        statistics.put("salaryYearMonth", salaryYearMonth);
        statistics.put("totalSalary", totalSalary);
        statistics.put("employeeCount", employeeCount);
        statistics.put("averageSalary", averageSalary);
        statistics.put("salaries", salaries);
        return statistics;
    }

    @Override
    public List<HrmSalary> getSalaryByDepartment(Long departmentId) {
        LambdaQueryWrapper<HrmSalary> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(HrmSalary::getDepartmentId, departmentId);
        return salaryMapper.selectList(wrapper);
    }

    // ==================== 薪资管理 ====================

    @Override
    public IPage<HrmSalary> getSalaryList(HrmSalary salary, PageParam<HrmSalary> page) {
        LambdaQueryWrapper<HrmSalary> wrapper = new LambdaQueryWrapper<>();
        
        if (salary.getEmployeeId() != null) {
            wrapper.eq(HrmSalary::getEmployeeId, salary.getEmployeeId());
        }
        if (StringUtils.hasText(salary.getEmployeeName())) {
            wrapper.like(HrmSalary::getEmployeeName, salary.getEmployeeName());
        }
        if (salary.getDepartmentId() != null) {
            wrapper.eq(HrmSalary::getDepartmentId, salary.getDepartmentId());
        }
        if (StringUtils.hasText(salary.getSalaryMonth())) {
            wrapper.eq(HrmSalary::getSalaryMonth, salary.getSalaryMonth());
        }
        if (salary.getPayStatus() != null) {
            wrapper.eq(HrmSalary::getPayStatus, salary.getPayStatus());
        }
        wrapper.orderByDesc(HrmSalary::getSalaryMonth);
        return salaryMapper.selectPage(page, wrapper);
    }

    @Override
    public HrmSalary getSalaryById(Long id) {
        return salaryMapper.selectById(id);
    }

    @Override
    @Transactional
    public boolean saveSalary(HrmSalary salary) {
        if (salary.getId() == null) {
            salary.setCreateTime(new Date());
            return salaryMapper.insert(salary) > 0;
        } else {
            salary.setUpdateTime(new Date());
            return salaryMapper.updateById(salary) > 0;
        }
    }

    @Override
    @Transactional
    public boolean addSalary(HrmSalary salary) {
        salary.setCreateTime(new Date());
        return salaryMapper.insert(salary) > 0;
    }

    @Override
    @Transactional
    public boolean updateSalary(HrmSalary salary) {
        if (salary.getId() == null) {
            throw new RuntimeException("薪资记录ID不能为空");
        }
        
        salary.setUpdateTime(new Date());
        return salaryMapper.updateById(salary) > 0;
    }

    @Override
    @Transactional
    public boolean deleteSalary(Long[] ids) {
        if (ids == null || ids.length == 0) {
            return false;
        }
        
        int deletedCount = 0;
        for (Long id : ids) {
            if (id != null) {
                deletedCount += salaryMapper.deleteById(id);
            }
        }
        
        return deletedCount > 0;
    }

    @Override
    public List<HrmSalary> getSalaryByEmployee(Long employeeId) {
        return salaryMapper.selectByEmployeeId(employeeId);
    }

    @Override
    public Boolean resignEmployee(Long employeeId, String resignReason) {
        try {
            HrmEmployee employee = employeeMapper.selectById(employeeId);
            if (employee == null) {
                throw new StorytellingBindException("员工不存在");
            }
            // 设置为离职状态
            employee.setStatus(2); 
            employee.setResignReason(resignReason);
            employee.setResignDate(new Date());
            
            int result = employeeMapper.updateById(employee);
            return result > 0 ;
        } catch (Exception e) {
            throw new StorytellingBindException("员工离职处理异常: " + e.getMessage());
        }
    }

    @Override
    public Boolean transferEmployee(Long employeeId, Long newDeptId, Long newPositionId) {
        try {
            HrmEmployee employee = employeeMapper.selectById(employeeId);
            if (employee == null) {
                throw new StorytellingBindException("员工不存在");
            }
            
            employee.setDepartmentId(newDeptId);
            employee.setPositionId(newPositionId);
            
            int result = employeeMapper.updateById(employee);
            return result > 0 ;
        } catch (Exception e) {
            throw new StorytellingBindException("员工转岗异常: " + e.getMessage());
        }
    }

    @Override
    public Boolean checkIn(Long employeeId) {
        try {
            HrmEmployee employee = employeeMapper.selectById(employeeId);
            if (employee == null) {
                throw new StorytellingBindException("员工不存在");
            }
            
            // 检查今天是否已经签到
            Date today = new Date();
            LambdaQueryWrapper<HrmAttendance> wrapper = new LambdaQueryWrapper<>();
            wrapper.eq(HrmAttendance::getEmployeeId, employeeId)
                   .eq(HrmAttendance::getAttendanceDate, today);
            
            HrmAttendance existingAttendance = attendanceMapper.selectOne(wrapper);
            if (existingAttendance != null && existingAttendance.getCheckInTime() != null) {
                throw new StorytellingBindException("今日已签到");
            }
            
            HrmAttendance attendance = new HrmAttendance();
            attendance.setEmployeeId(employeeId);
            attendance.setEmployeeName(employee.getName());
            attendance.setDepartmentId(employee.getDepartmentId());
            attendance.setAttendanceDate(today);
            attendance.setCheckInTime(new Date());
            // 正常
            attendance.setStatus(1); 
            
            int result = attendanceMapper.insert(attendance);
            return result > 0;
        } catch (Exception e) {
            throw new StorytellingBindException("签到异常: " + e.getMessage());
        }
    }

    @Override
    public Boolean checkOut(Long employeeId) {
        try {
            // 查找今天的考勤记录
            Date today = new Date();
            LambdaQueryWrapper<HrmAttendance> wrapper = new LambdaQueryWrapper<>();
            wrapper.eq(HrmAttendance::getEmployeeId, employeeId)
                   .eq(HrmAttendance::getAttendanceDate, today);
            
            HrmAttendance attendance = attendanceMapper.selectOne(wrapper);
            if (attendance == null) {
                throw new StorytellingBindException("请先签到");
            }
            
            if (attendance.getCheckOutTime() != null) {
                throw new StorytellingBindException("今日已签退");
            }
            
            attendance.setCheckOutTime(new Date());
            
            int result = attendanceMapper.updateById(attendance);
            return result > 0;
        } catch (Exception e) {
            throw new StorytellingBindException("签退异常: " + e.getMessage());
        }
    }

    @Override
    public HrmAttendanceStatisticsVO getAttendanceStatistics(String startDate, String endDate, Long deptId) {
        try {
            HrmAttendanceStatisticsVO statistics = new HrmAttendanceStatisticsVO();
            // 这里可以添加具体的统计逻辑
            statistics.setTotalAttendanceDays(0);
            statistics.setNormalAttendanceDays(0);
            statistics.setAbsentDays(0);
            statistics.setLateCount(0);
            statistics.setEarlyLeaveCount(0);
            statistics.setOvertimeHours(java.math.BigDecimal.ZERO);
            statistics.setAttendanceRate(java.math.BigDecimal.ZERO);
            
            return statistics;
        } catch (Exception e) {
            throw new StorytellingBindException("获取考勤统计异常: " + e.getMessage());
        }
    }

    @Override
    public Integer generateSalaryBatch(String salaryMonth, Long[] employeeIds) {
        try {
            int generatedCount = 0;
            for (Long employeeId : employeeIds) {
                HrmEmployee employee = employeeMapper.selectById(employeeId);
                if (employee != null) {
                    HrmSalary salary = new HrmSalary();
                    salary.setEmployeeId(employeeId);
                    salary.setEmployeeName(employee.getName());
                    salary.setDepartmentId(employee.getDepartmentId());
                    salary.setSalaryMonth(salaryMonth);
                    salary.setBaseSalary(employee.getSalary());
                    salary.setPayStatus(0); // 未发放
                    
                    salaryMapper.insert(salary);
                    generatedCount++;
                }
            }
            log.info("批量生成薪资成功，共生成{}条记录", generatedCount);
            return generatedCount;
        } catch (Exception e) {
            throw new StorytellingBindException("批量生成薪资异常: " + e.getMessage());
        }
    }

    @Override
    public Integer paySalary(Long[] salaryIds) {
        try {
            int paidCount = 0;
            for (Long salaryId : salaryIds) {
                HrmSalary salary = salaryMapper.selectById(salaryId);
                if (salary != null && salary.getPayStatus() == 0) {
                    // 已发放
                    salary.setPayStatus(1);
                    salary.setPayTime(new Date());
                    
                    salaryMapper.updateById(salary);
                    paidCount++;
                }
            }
            log.info("薪资发放成功，共发放{}条记录", paidCount);
            return paidCount;
        } catch (Exception e) {
            throw new StorytellingBindException("薪资发放异常: " + e.getMessage());
        }
    }


    @Override
    public HrmSalaryStatisticsVO getSalaryStatistics(String startMonth, String endMonth, Long deptId) {
        try {
            HrmSalaryStatisticsVO statistics = new HrmSalaryStatisticsVO();
            // 这里可以添加具体的统计逻辑
            statistics.setTotalSalaryRecords(0L);
            statistics.setTotalSalaryAmount(java.math.BigDecimal.ZERO);
            statistics.setAverageSalary(java.math.BigDecimal.ZERO);
            statistics.setMaxSalary(java.math.BigDecimal.ZERO);
            statistics.setMinSalary(java.math.BigDecimal.ZERO);
            statistics.setTotalBaseSalary(java.math.BigDecimal.ZERO);
            statistics.setTotalPerformanceSalary(java.math.BigDecimal.ZERO);
            statistics.setTotalAllowance(java.math.BigDecimal.ZERO);
            statistics.setTotalOvertimePay(java.math.BigDecimal.ZERO);
            statistics.setTotalDeduction(java.math.BigDecimal.ZERO);
            
            return statistics;
        } catch (Exception e) {
            throw new StorytellingBindException("获取薪资统计异常: " + e.getMessage());
        }
    }

    @Override
    public HrmDepartment getDepartmentByCode(String departmentCode) {
        return departmentMapper.selectByDeptCode(departmentCode);
    }

    @Override
    public HrmPosition getPositionByCode(String positionCode) {
        return positionMapper.selectByPositionCode(positionCode);
    }

    @Override
    public Map<String, Object> getPositionStatistics() {
        return Map.of();
    }

    @Override
    public List<HrmPosition> getPositionsByDeptId(Long deptId) {
        LambdaQueryWrapper<HrmPosition> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(HrmPosition::getDepartmentId, deptId);
        return positionMapper.selectList(wrapper);
    }

    @Override
    public List<HrmDepartment> getDepartmentTree() {
        List<HrmDepartment> allDepartments = departmentMapper.selectList(null);
        return buildDepartmentTree(allDepartments, 0L);
    }

    @Override
    public List<HrmPosition> getPositionList(HrmPosition position) {
        return List.of();
    }

    @Override
    public HrmEmployeeMonthlyAttendanceVO getEmployeeMonthlyAttendance(Long employeeId, String yearMonth) {
        try {
            // 获取员工信息
            HrmEmployee employee = employeeMapper.selectById(employeeId);
            if (employee == null) {
                throw new StorytellingBindException("员工不存在");
            }

            // 获取部门和职位信息
            HrmDepartment department = departmentMapper.selectById(employee.getDepartmentId());
            HrmPosition position = positionMapper.selectById(employee.getPositionId());

            // 创建返回对象
            HrmEmployeeMonthlyAttendanceVO vo = new HrmEmployeeMonthlyAttendanceVO();
            vo.setEmployeeId(employeeId);
            vo.setEmployeeName(employee.getName());
            vo.setDepartmentName(department != null ? department.getDeptName() : "");
            vo.setPositionName(position != null ? position.getPositionName() : "");
            vo.setYearMonth(yearMonth);

            // 设置基础统计数据（这里可以根据实际业务逻辑查询数据库获取真实数据）
            vo.setShouldAttendDays(22); // 应出勤天数
            vo.setActualAttendDays(20);  // 实际出勤天数
            vo.setNormalAttendDays(18);  // 正常出勤天数
            vo.setLateCount(2);          // 迟到次数
            vo.setEarlyLeaveCount(1);    // 早退次数
            vo.setAbsentDays(2);         // 缺勤天数
            vo.setLeaveDays(0);          // 请假天数
            vo.setOvertimeHours(java.math.BigDecimal.valueOf(8.5)); // 加班小时数
            vo.setAttendanceRate(java.math.BigDecimal.valueOf(90.91)); // 出勤率
            vo.setTotalLateMinutes(30);  // 迟到总时长
            vo.setTotalEarlyLeaveMinutes(15); // 早退总时长
            vo.setAvgDailyWorkHours(java.math.BigDecimal.valueOf(8.2)); // 平均每日工作时长

            // 设置考勤异常统计
            Map<String, Integer> abnormalStats = new HashMap<>();
            abnormalStats.put("迟到", 2);
            abnormalStats.put("早退", 1);
            abnormalStats.put("缺勤", 2);
            vo.setAbnormalStatistics(abnormalStats);

            // 设置每日考勤详情（示例数据）
            List<HrmEmployeeMonthlyAttendanceVO.DailyAttendanceDetail> dailyDetails = new ArrayList<>();
            // 这里可以根据实际需求查询数据库获取每日考勤详情
            vo.setDailyDetails(dailyDetails);

            // 设置月度考勤趋势（示例数据）
            List<HrmEmployeeMonthlyAttendanceVO.AttendanceTrend> monthlyTrend = new ArrayList<>();
            // 这里可以根据实际需求查询数据库获取月度趋势数据
            vo.setMonthlyTrend(monthlyTrend);

            return vo;
        } catch (Exception e) {
            throw new StorytellingBindException("获取员工月度考勤统计异常: " + e.getMessage());
        }
    }

    private List<HrmDepartment> buildDepartmentTree(List<HrmDepartment> allDepartments, Long parentId) {
        List<HrmDepartment> tree = new ArrayList<>();
        for (HrmDepartment dept : allDepartments) {
            if (Objects.equals(dept.getParentId(), parentId)) {
                dept.setChildren(buildDepartmentTree(allDepartments, dept.getId()));
                tree.add(dept);
            }
        }
        return tree;
    }

}