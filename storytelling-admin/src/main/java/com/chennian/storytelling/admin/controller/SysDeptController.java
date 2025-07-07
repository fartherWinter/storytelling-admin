package com.chennian.storytelling.admin.controller;

import com.chennian.security.admin.util.SecurityUtils;
import com.chennian.storytelling.bean.model.SysDept;
import com.chennian.storytelling.common.constant.UserConstants;
import com.chennian.storytelling.common.response.ServerResponseEntity;
import com.chennian.storytelling.common.utils.StringUtils;
import com.chennian.storytelling.service.SysDeptService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.apache.commons.lang3.ArrayUtils;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * @author by chennian
 * @date 2025/5/8.
 */
@RestController
@RequestMapping("/sys/dept")
@Tag(name = "部门管理首页")
public class SysDeptController {
    private final SysDeptService sysDeptService;
    public SysDeptController(SysDeptService sysDeptService) {
        this.sysDeptService = sysDeptService;
    }


    /**
     * 列表数据查询
     */
    @PostMapping("/list")
    @Operation(summary = "列表")
    public ServerResponseEntity<List<SysDept>> list(@RequestBody SysDept dept) {
        Integer user = SecurityUtils.getUser();
        List<SysDept> deptList = sysDeptService.selectDeptList(dept);
        return ServerResponseEntity.success(deptList);
    }

    /**
     * 查询部门列表（排除节点）
     */
    @PostMapping("/list/exclude/{deptId}")
    @Operation(summary = "查询部门列表-排除节点")
    public ServerResponseEntity<List<SysDept>> excludeChild(@PathVariable(value = "deptId", required = false) Long deptId) {
        List<SysDept> depts = sysDeptService.selectDeptList(new SysDept());
        depts.removeIf(d -> d.getDeptId().intValue() == deptId || ArrayUtils.contains(StringUtils.split(d.getAncestors(), ","), deptId + ""));
        return ServerResponseEntity.success(depts);
    }

    /**
     * 根据部门编号获取详细信息
     *
     * @param sysDepId
     * @return
     */
    @PostMapping("/info/{sysDepId}")
    @Operation(summary = "根据部门编号获取详细信息")
    public ServerResponseEntity<SysDept> info(@PathVariable("sysDepId") String sysDepId) {
        // 校验部门是否有数据权限
        Long currentUserId = Long.valueOf(SecurityUtils.getUser());
        if (!sysDeptService.checkDeptDataScope(currentUserId, Long.valueOf(sysDepId))) {
            return ServerResponseEntity.showFailMsg("没有权限访问该部门信息");
        }
        return ServerResponseEntity.success(sysDeptService.getById(sysDepId));
    }

    /**
     * 修改部门信息
     *
     * @param sysDept
     * @return
     */
    @PostMapping("/update")
    @Operation(summary = "修改")
    public ServerResponseEntity<Integer> update(@RequestBody SysDept sysDept) {
        Long deptId = sysDept.getDeptId();
        // 部门校验
        Long currentUserId = Long.valueOf(SecurityUtils.getUser());
        if (!sysDeptService.checkDeptDataScope(currentUserId, deptId)) {
            return ServerResponseEntity.showFailMsg("没有权限修改该部门信息");
        }
        if (!sysDeptService.checkDeptNameUnique(sysDept)) {
            return ServerResponseEntity.showFailMsg("修改部门'" + sysDept.getDeptName() + "'失败，部门名称已存在");
        } else if (sysDept.getParentId().equals(deptId)) {
            return ServerResponseEntity.showFailMsg("修改部门'" + sysDept.getDeptName() + "'失败，上级部门不能是自己");
        } else if (StringUtils.equals(UserConstants.DEPT_DISABLE, sysDept.getStatus()) && sysDeptService.selectNormalChildrenDeptById(deptId) > 0) {
            return ServerResponseEntity.showFailMsg("该部门包含未停用的子部门！");
        }
        // 增加操作人员选项
        sysDept.setUpdateBy(SecurityUtils.getUsername());
        return ServerResponseEntity.success(sysDeptService.updateDept(sysDept));
    }

    /**
     * 新增部门信息
     */
    @PostMapping("/add")
    @Operation(summary = "新增部门信息")
    public ServerResponseEntity<Integer> add(@Validated @RequestBody SysDept sysDept) {
        if (!sysDeptService.checkDeptNameUnique(sysDept)) {
            return ServerResponseEntity.showFailMsg("新增部门'" + sysDept.getDeptName() + "'失败，部门名称已存在");
        }
        // 增加操作人员选项
        sysDept.setCreateBy(SecurityUtils.getUsername());
        return ServerResponseEntity.success(sysDeptService.insertDept(sysDept));
    }

    /**
     * 删除部门信息
     */
    @PostMapping("/delete/{deptId}")
    @Operation(summary = "删除")
    public ServerResponseEntity<Integer> remove(@PathVariable("deptId") Long deptId) {
        if (sysDeptService.hasChildByDeptId(deptId)) {
            return ServerResponseEntity.showFailMsg("存在下级部门,不允许删除");
        }
        if (sysDeptService.checkDeptExistUser(deptId)) {
            return ServerResponseEntity.showFailMsg("部门存在用户,不允许删除");
        }
        // 检查数据权限
        Long currentUserId = Long.valueOf(SecurityUtils.getUser());
        if (!sysDeptService.checkDeptDataScope(currentUserId, deptId)) {
            return ServerResponseEntity.showFailMsg("没有权限删除该部门");
        }
        return ServerResponseEntity.success(sysDeptService.deleteDeptById(deptId));
    }

    /**
     * 状态修改
     */
    @PostMapping("/changeStatus")
    @Operation(summary = "状态修改")
    public ServerResponseEntity<Integer> quickEnable(@RequestBody SysDept dept) {
        return ServerResponseEntity.success(sysDeptService.changeStatus(dept));
    }

}
