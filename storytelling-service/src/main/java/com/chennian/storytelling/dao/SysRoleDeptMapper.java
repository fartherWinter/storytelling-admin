package com.chennian.storytelling.dao;

import com.chennian.storytelling.bean.model.SysRoleDept;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

/**
* @author chen
*
* @createDate 2025-05-06 19:12:52
*
*/
@Mapper
public interface SysRoleDeptMapper {
    /**
     * 通过角色ID删除角色和部门关联
     *
     * @param roleId 角色ID
     * @return 结果
     */
    public int deleteRoleDeptByRoleId(Long roleId);

    /**
     * 批量删除角色部门关联信息
     *
     * @param ids 需要删除的数据ID
     * @return 结果
     */
    public int deleteRoleDept(Long[] ids);

    /**
     * 查询部门使用数量
     *
     * @param deptId 部门ID
     * @return 结果
     */
    public int selectCountRoleDeptByDeptId(Long deptId);

    /**
     * 批量新增角色部门信息
     * @return 结果
     */
    public int batchRoleDept(List<SysRoleDept> roleDeptList);
}




