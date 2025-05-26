package com.chennian.storytelling.bean.model;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.io.Serializable;

/**
 * 角色和部门关联表
 * @author chen
 * @TableName sys_role_dept
 */
@TableName(value ="sys_role_dept")
@Data
public class SysRoleDept implements Serializable {
    /**
     * 角色ID
     */
    @TableId(value = "role_id")
    private Long roleId;

    /**
     * 部门ID
     */
    @TableField(value = "dept_id")
    private Long deptId;


}