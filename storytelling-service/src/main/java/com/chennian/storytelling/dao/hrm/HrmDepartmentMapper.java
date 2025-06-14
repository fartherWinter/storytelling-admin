package com.chennian.storytelling.dao.hrm;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.chennian.storytelling.bean.model.hrm.HrmDepartment;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 部门信息数据访问层
 * 
 * @author storytelling
 * @date 2024-01-01
 */
@Mapper
public interface HrmDepartmentMapper extends BaseMapper<HrmDepartment> {

    /**
     * 根据上级部门ID查询子部门列表
     * 
     * @param parentId 上级部门ID
     * @return 子部门列表
     */
    List<HrmDepartment> selectByParentId(@Param("parentId") Long parentId);

    /**
     * 根据部门编码查询部门信息
     * 
     * @param deptCode 部门编码
     * @return 部门信息
     */
    HrmDepartment selectByDeptCode(@Param("deptCode") String deptCode);

    /**
     * 查询所有根部门（顶级部门）
     * 
     * @return 根部门列表
     */
    List<HrmDepartment> selectRootDepartments();

    /**
     * 根据部门负责人ID查询部门列表
     * 
     * @param managerId 部门负责人ID
     * @return 部门列表
     */
    List<HrmDepartment> selectByManagerId(@Param("managerId") Long managerId);

    /**
     * 查询部门树形结构
     * 
     * @return 部门树形列表
     */
    List<HrmDepartment> selectDepartmentTree();

    /**
     * 根据状态查询部门列表
     * 
     * @param status 状态
     * @return 部门列表
     */
    List<HrmDepartment> selectByStatus(@Param("status") Integer status);
}