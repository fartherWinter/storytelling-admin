package com.chennian.storytelling.dao.hrm;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.chennian.storytelling.bean.model.hrm.HrmAttendance;
import org.apache.ibatis.annotations.MapKey;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.Date;
import java.util.List;

/**
 * 考勤记录数据访问层
 * 
 * @author storytelling
 * @date 2024-01-01
 */
@Mapper
public interface HrmAttendanceMapper extends BaseMapper<HrmAttendance> {

    /**
     * 根据员工ID查询考勤记录
     * 
     * @param employeeId 员工ID
     * @return 考勤记录列表
     */
    List<HrmAttendance> selectByEmployeeId(@Param("employeeId") Long employeeId);

    /**
     * 根据员工ID和日期查询考勤记录
     * 
     * @param employeeId 员工ID
     * @param attendanceDate 考勤日期
     * @return 考勤记录
     */
    HrmAttendance selectByEmployeeIdAndDate(@Param("employeeId") Long employeeId, @Param("attendanceDate") Date attendanceDate);

    /**
     * 根据部门ID查询考勤记录
     * 
     * @param departmentId 部门ID
     * @return 考勤记录列表
     */
    List<HrmAttendance> selectByDepartmentId(@Param("departmentId") Long departmentId);

    /**
     * 根据日期范围查询考勤记录
     * 
     * @param startDate 开始日期
     * @param endDate 结束日期
     * @return 考勤记录列表
     */
    List<HrmAttendance> selectByDateRange(@Param("startDate") Date startDate, @Param("endDate") Date endDate);

    /**
     * 根据员工ID和日期范围查询考勤记录
     * 
     * @param employeeId 员工ID
     * @param startDate 开始日期
     * @param endDate 结束日期
     * @return 考勤记录列表
     */
    List<HrmAttendance> selectByEmployeeIdAndDateRange(@Param("employeeId") Long employeeId, @Param("startDate") Date startDate, @Param("endDate") Date endDate);

    /**
     * 根据考勤状态查询考勤记录
     * 
     * @param status 考勤状态
     * @return 考勤记录列表
     */
    List<HrmAttendance> selectByStatus(@Param("status") Integer status);

    /**
     * 查询考勤统计信息
     * 
     * @param startDate 开始日期
     * @param endDate 结束日期
     * @return 统计信息
     */
    @MapKey("department_name")
    List<java.util.Map<String, Object>> selectAttendanceStatistics(@Param("startDate") Date startDate, @Param("endDate") Date endDate);

    /**
     * 查询员工月度考勤统计
     * 
     * @param employeeId 员工ID
     * @param year 年份
     * @param month 月份
     * @return 月度考勤统计
     */
    java.util.Map<String, Object> selectMonthlyAttendanceByEmployee(@Param("employeeId") Long employeeId, @Param("year") Integer year, @Param("month") Integer month);
}