package com.chennian.storytelling.admin.controller;

import cn.dev33.satoken.annotation.SaCheckRole;
import cn.dev33.satoken.annotation.SaMode;
import cn.hutool.core.date.DateUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.chennian.security.admin.util.SecurityUtils;
import com.chennian.storytelling.bean.bo.SysRoleSearchBo;
import com.chennian.storytelling.bean.model.SysRole;
import com.chennian.storytelling.common.exception.StorytellingBindException;
import com.chennian.storytelling.common.response.ServerResponseEntity;
import com.chennian.storytelling.common.utils.PageParam;
import com.chennian.storytelling.service.SysPermissionService;
import com.chennian.storytelling.service.SysRoleService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

/**
 * @author by chennian
 * @date 2025/5/7.
 */
@RestController
@RequestMapping("/sys/role")
@SaCheckRole(value = {"admin", "super admin"}, mode = SaMode.OR)
@Tag(name = "角色管理")
public class SysRoleController {
    private SysRoleService sysRoleService;
    private SysPermissionService sysPermissionService;
    public SysRoleController(SysRoleService sysRoleService, SysPermissionService sysPermissionService) {
        this.sysRoleService = sysRoleService;
        this.sysPermissionService = sysPermissionService;
    }

    /**
     * 角色列表信息
     */
    @PostMapping("/page")
    @Operation(summary = "角色列表")
    public ServerResponseEntity<IPage<SysRole>> page(SysRoleSearchBo sysRoleSearchBo, PageParam<SysRole> page) {
        Integer user = SecurityUtils.getUser();
        IPage<SysRole> sysRoleIPage = sysRoleService.page(page, new LambdaQueryWrapper<SysRole>()
                .like(!sysRoleSearchBo.getRoleName().isEmpty(), SysRole::getRoleName, sysRoleSearchBo.getRoleName())
                .like(!sysRoleSearchBo.getRoleKey().isEmpty(), SysRole::getRoleKey, sysRoleSearchBo.getRoleKey())
                .eq(!sysRoleSearchBo.getStatus().isEmpty(), SysRole::getStatus, sysRoleSearchBo.getStatus())
                .eq(SysRole::getDelFlag, '0')
                .ge(!sysRoleSearchBo.getStartTime().isEmpty(), SysRole::getCreateTime, DateUtil.parse(sysRoleSearchBo.getStartTime()))
                .le(!sysRoleSearchBo.getEndTime().isEmpty(), SysRole::getCreateTime, DateUtil.parse(sysRoleSearchBo.getEndTime()))
                .orderByAsc(SysRole::getRoleSort)
        );
        return ServerResponseEntity.success(sysRoleIPage);
    }

    /**
     * 根据角色编号获取详细信息
     */
    @PostMapping("/getInfo/{sysRoleId}")
    @Operation(summary = "根据角色编号获取详细信息")
    public ServerResponseEntity<SysRole> info(@PathVariable("sysRoleId") Long roleId) {
        return ServerResponseEntity.success(sysRoleService.selectRoleById(roleId));
    }

    /**
     * 状态修改
     */
    @PostMapping("/changeStatus")
    @Operation(summary = "状态修改")
    public ServerResponseEntity<Integer> changeStatus(@RequestBody SysRole role) {
        return ServerResponseEntity.success(sysRoleService.updateRoleStatus(role));
    }

    /**
     * 修改保存角色
     */
    @PostMapping("/edit")
    @Operation(summary = "修改")
    public ServerResponseEntity<Integer> edit(@Validated @RequestBody SysRole sysRole) {
        if (!sysRoleService.checkRoleNameUnique(sysRole)) {
            return ServerResponseEntity.showFailMsg("修改角色'" + sysRole.getRoleName() + "'失败，角色名称已存在");
        } else if (!sysRoleService.checkRoleKeyUnique(sysRole)) {
            return ServerResponseEntity.showFailMsg("修改角色'" + sysRole.getRoleName() + "'失败，角色权限已存在");
        }
        int num = sysRoleService.updateRole(sysRole);
        return ServerResponseEntity.success(num);
    }

    /**
     * 新增角色
     */
    @PostMapping("/add")
    @Operation(summary = "新增")
    public ServerResponseEntity<Integer> add(@Validated @RequestBody SysRole sysRole) {
        if (!sysRoleService.checkRoleNameUnique(sysRole)) {
            throw new StorytellingBindException("新增角色'" + sysRole.getRoleName() + "'失败，角色名称已存在");
        } else if (!sysRoleService.checkRoleKeyUnique(sysRole)) {
            throw new StorytellingBindException("新增角色'" + sysRole.getRoleName() + "'失败，角色权限已存在");
        }
        return ServerResponseEntity.success(sysRoleService.insertRole(sysRole));
    }


    /**
     * 删除角色信息
     */
    @PostMapping("/remove/{roleIds}")
    @Operation(summary = "删除")
    public ServerResponseEntity<Integer> remove(@PathVariable("roleIds") Long[] roleIds) {
        return ServerResponseEntity.success(sysRoleService.deleteRoleByIds(roleIds));
    }
}
