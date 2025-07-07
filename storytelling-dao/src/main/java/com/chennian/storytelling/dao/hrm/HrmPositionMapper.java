package com.chennian.storytelling.dao.hrm;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.chennian.storytelling.bean.model.hrm.HrmPosition;
import org.apache.ibatis.annotations.MapKey;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 职位信息数据访问层
 * 
 * @author storytelling
 * @date 2024-01-01
 */
@Mapper
public interface HrmPositionMapper extends BaseMapper<HrmPosition> {

    /**
     * 根据部门ID查询职位列表
     * 
     * @param departmentId 部门ID
     * @return 职位列表
     */
    List<HrmPosition> selectByDepartmentId(@Param("departmentId") Long departmentId);

    /**
     * 根据职位编码查询职位信息
     * 
     * @param positionCode 职位编码
     * @return 职位信息
     */
    HrmPosition selectByPositionCode(@Param("positionCode") String positionCode);

    /**
     * 根据职位级别查询职位列表
     * 
     * @param level 职位级别
     * @return 职位列表
     */
    List<HrmPosition> selectByLevel(@Param("level") Integer level);

    /**
     * 根据状态查询职位列表
     * 
     * @param status 状态
     * @return 职位列表
     */
    List<HrmPosition> selectByStatus(@Param("status") Integer status);

    /**
     * 查询职位统计信息
     * 
     * @return 统计信息
     */
    @MapKey("department_name")
    List<java.util.Map<String, Object>> selectPositionStatistics();
}