package com.chennian.storytelling.dao.hrm;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.chennian.storytelling.bean.model.hrm.HrmEmployee;
import org.apache.ibatis.annotations.MapKey;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 员工信息数据访问层
 * 
 * @author storytelling
 * @date 2024-01-01
 */
@Mapper
public interface HrmEmployeeMapper extends BaseMapper<HrmEmployee> {

    /**
     * 根据部门ID查询员工列表
     * 
     * @param departmentId 部门ID
     * @return 员工列表
     */
    List<HrmEmployee> selectByDepartmentId(@Param("departmentId") Long departmentId);

    /**
     * 根据职位ID查询员工列表
     * 
     * @param positionId 职位ID
     * @return 员工列表
     */
    List<HrmEmployee> selectByPositionId(@Param("positionId") Long positionId);

    /**
     * 根据员工状态查询员工列表
     * 
     * @param status 员工状态
     * @return 员工列表
     */
    List<HrmEmployee> selectByStatus(@Param("status") Integer status);

    /**
     * 根据员工编号查询员工信息
     * 
     * @param employeeNo 员工编号
     * @return 员工信息
     */
    HrmEmployee selectByEmployeeNo(@Param("employeeNo") String employeeNo);

    /**
     * 查询员工统计信息
     * 
     * @return 统计信息
     */
    @MapKey("department_name")
    List<java.util.Map<String, Object>> selectEmployeeStatistics();
}