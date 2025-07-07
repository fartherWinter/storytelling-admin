package com.chennian.storytelling.admin.controller;

import com.chennian.storytelling.common.annotation.EventTrack;
import com.chennian.storytelling.common.enums.BusinessType;
import com.chennian.storytelling.common.enums.ModelType;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.chennian.security.admin.util.SecurityUtils;
import com.chennian.storytelling.bean.TreeSelect;
import com.chennian.storytelling.bean.bo.SysUserSearchBo;
import com.chennian.storytelling.bean.model.SysDept;
import com.chennian.storytelling.bean.model.SysPost;
import com.chennian.storytelling.bean.model.SysRole;
import com.chennian.storytelling.bean.model.SysUser;
import com.chennian.storytelling.common.enums.ResultCode;
import com.chennian.storytelling.common.exception.StorytellingBindException;
import com.chennian.storytelling.common.response.ServerResponseEntity;
import com.chennian.storytelling.common.utils.PageParam;
import com.chennian.storytelling.service.SysDeptService;
import com.chennian.storytelling.service.SysPostService;
import com.chennian.storytelling.service.SysRoleService;
import com.chennian.storytelling.service.SysUserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletResponse;
import org.apache.commons.lang.ArrayUtils;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Random;

/**
 * @author by chennian
 * @date 2025/5/7.
 */
@RestController
@RequestMapping("/sys/sysUser")
@Tag(name = "用户管理")
public class SysUserController {

    private final SysUserService sysUserService;
    private final SysRoleService sysRoleService;
    private final SysDeptService sysDeptService;
    private final SysPostService sysPostService;
    public SysUserController(SysUserService sysUserService, SysRoleService sysRoleService, SysDeptService sysDeptService, SysPostService sysPostService) {
        this.sysUserService = sysUserService;
        this.sysRoleService = sysRoleService;
        this.sysDeptService = sysDeptService;
        this.sysPostService = sysPostService;
    }

    /**
     * 查询用户列表
     */
    @PostMapping("/page")
    @Operation(summary = "列表")
    @EventTrack(title = ModelType.SYSTEM, businessType = BusinessType.SEARCH, description = "列表")
    public ServerResponseEntity<IPage<SysUser>> page(SysUserSearchBo sysUserSearchBo, PageParam<SysUser> page) {
        IPage<SysUser> userPage = sysUserService.findByPage(page, sysUserSearchBo);
        return ServerResponseEntity.success(userPage);
    }

    /**
     * 查看用户信息
     */
    @PostMapping("/getInfo/{userId}")
    @Operation(summary = "细节")
    @EventTrack(title = ModelType.SYSTEM, businessType = BusinessType.SEARCH, description = "细节")
    public ServerResponseEntity<SysUser> info(@PathVariable("userId") Long userId) {
        SysUser sysUser = sysUserService.selectUserById(userId);
        return ServerResponseEntity.success(sysUser);
    }

    /**
     * 修改用户信息
     *
     * @param user
     * @return
     */
    @PostMapping("/update")
    @Operation(summary = "修改")
    @EventTrack(title = ModelType.SYSTEM, businessType = BusinessType.UPDATE, description = "修改")
    public ServerResponseEntity<Integer> update(@RequestBody SysUser user) {
        if (!sysUserService.checkUserNameUnique(user)) {
            return ServerResponseEntity.showFailMsg("修改用户'" + user.getUserName() + "'失败，登录账号已存在");
        }
//        else if (StringUtils.isNotEmpty(user.getPhoneNumber()) && !sysUserService.checkPhoneUnique(user)) {
//            return ServerResponseEntity.showFailMsg("修改用户'" + user.getUserName() + "'失败，手机号码已存在");
//        } else if (StringUtils.isNotEmpty(user.getEmail()) && !sysUserService.checkEmailUnique(user)) {
//            return ServerResponseEntity.showFailMsg("修改用户'" + user.getUserName() + "'失败，邮箱账号已存在");
//        }
        return ServerResponseEntity.success(sysUserService.updateUser(user));
    }


    /**
     * 新增用户信息
     */
    @PostMapping("/add")
    @Operation(summary = "新增")
    @EventTrack(title = ModelType.SYSTEM, businessType = BusinessType.INSERT, description = "新增")
    public ServerResponseEntity<Integer> add(@Validated @RequestBody SysUser sysUser) {
        if (!sysUserService.checkUserNameUnique(sysUser)) {
            return ServerResponseEntity.showFailMsg("新增用户'" + sysUser.getUserName() + "'失败，登录账号已存在");
        }
//        else if (StringUtils.isNotEmpty(sysUser.getPhoneNumber()) && !sysUserService.checkPhoneUnique(sysUser)) {
//            return ServerResponseEntity.showFailMsg("新增用户'" + sysUser.getUserName() + "'失败，手机号码已存在");
//        } else if (StringUtils.isNotEmpty(sysUser.getEmail()) && !sysUserService.checkEmailUnique(sysUser)) {
//            return ServerResponseEntity.showFailMsg("新增用户'" + sysUser.getUserName() + "'失败，邮箱账号已存在");
//        }
        // 设置操作人员
        sysUser.setCreateBy(SecurityUtils.getUsername());
        sysUser.setPassword("123456");
        Random random = new Random();
        sysUser.setSalt(String.valueOf(random.nextInt()));
        sysUser.setPassword(SecurityUtils.secureMd5(sysUser.getPassword() + sysUser.getSalt()));
        return ServerResponseEntity.success(sysUserService.insertUser(sysUser));
    }

    /**
     * 用户状态修改
     */
    @PostMapping("/changeStatus")
    @Operation(summary = "用户状态修改")
    @EventTrack(title = ModelType.SYSTEM, businessType = BusinessType.UPDATE, description = "用户状态修改")
    public ServerResponseEntity<Integer> changeStatus(@RequestBody SysUser sysUser) {
        // 数据权限校验
        Long currentUserId = SecurityUtils.getUserId();
        if (!sysUserService.checkUserDataScope(currentUserId, sysUser.getUserId())) {
            return ServerResponseEntity.showFailMsg("没有权限修改该用户状态");
        }
        sysUser.setUpdateBy(SecurityUtils.getUsername());
        return ServerResponseEntity.success(sysUserService.updateUserStatus(sysUser));
    }


    /**
     * 删除用户
     */
    @PostMapping("/remove/{userIds}")
    @Operation(summary = "删除用户")
    @EventTrack(title = ModelType.SYSTEM, businessType = BusinessType.DELETE, description = "删除用户")
    public ServerResponseEntity<Integer> remove(@PathVariable("userIds") Long[] userIds) {
        if (ArrayUtils.contains(userIds, 1L)) {
            throw new StorytellingBindException(ResultCode.USER_ADMIN_ERROR.message(), ResultCode.USER_ADMIN_ERROR.code());
        }
        return ServerResponseEntity.success(sysUserService.deleteUserByIds(userIds));
    }

    /**
     * 重置密码
     */
    @PostMapping("/resetPwd")
    @Operation(summary = "重置用户密码")
    @EventTrack(title = ModelType.SYSTEM, businessType = BusinessType.UPDATE, description = "重置用户密码")
    public ServerResponseEntity<Integer> resetPwd(String userName, String passWord) {
        return ServerResponseEntity.success(sysUserService.resetUserPwd(userName, passWord));
    }

    @PostMapping("/export")
    @Operation(summary = "导出")
    @EventTrack(title = ModelType.SYSTEM, businessType = BusinessType.EXPORT, description = "导出")
    public ServerResponseEntity<Void> export(HttpServletResponse response, SysUser user) {
        return ServerResponseEntity.success();
    }

    /**
     * 获取部门树
     */
    @PostMapping("/deptTree")
    @Operation(summary = "获取部门树")
    @EventTrack(title = ModelType.SYSTEM, businessType = BusinessType.SEARCH, description = "获取部门树")
    public ServerResponseEntity<List<TreeSelect>> deptTree(SysDept sysDept) {
        return ServerResponseEntity.success(sysDeptService.selectDeptTreeList(sysDept));
    }

    /**
     * 获取岗位树
     */
    @PostMapping("/postTree")
    @Operation(summary = "获取岗位下拉")
    @EventTrack(title = ModelType.SYSTEM, businessType = BusinessType.SEARCH, description = "获取岗位下拉")
    public ServerResponseEntity<List<SysPost>> postTree(SysPost sysPost) {
        return ServerResponseEntity.success(sysPostService.selectPostList(sysPost));
    }

    /**
     * 获取角色树
     */
    @PostMapping("/roleTree")
    @Operation(summary = "获取角色下拉")
    @EventTrack(title = ModelType.SYSTEM, businessType = BusinessType.SEARCH, description = "获取角色下拉")
    public ServerResponseEntity<List<SysRole>> roleTree(SysRole sysRole) {
        return ServerResponseEntity.success(sysRoleService.selectRoleList(sysRole));
    }


}
