package com.chennian.storytelling.user.controller;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.chennian.storytelling.common.result.Result;
import com.chennian.storytelling.user.entity.Role;
import com.chennian.storytelling.user.entity.User;
import com.chennian.storytelling.user.service.RoleService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.util.List;

/**
 * 角色控制器
 * 
 * @author chennian
 * @since 2024-01-01
 */
@Slf4j
@RestController
@RequestMapping("/api/role")
@RequiredArgsConstructor
@Validated
@Tag(name = "角色管理", description = "角色相关接口")
public class RoleController {

    private final RoleService roleService;

    @GetMapping("/info/{roleId}")
    @Operation(summary = "获取角色信息", description = "根据角色ID获取角色信息")
    public Result<Role> getRoleInfo(@PathVariable @NotNull Long roleId) {
        try {
            Role role = roleService.getById(roleId);
            return Result.success(role);
        } catch (Exception e) {
            log.error("获取角色信息失败: {}", e.getMessage(), e);
            return Result.error(e.getMessage());
        }
    }

    @GetMapping("/page")
    @Operation(summary = "分页查询角色", description = "分页查询角色列表")
    public Result<IPage<Role>> getRolePage(
            @Parameter(description = "页码") @RequestParam(defaultValue = "1") Integer current,
            @Parameter(description = "每页大小") @RequestParam(defaultValue = "10") Integer size,
            @Parameter(description = "角色名称") @RequestParam(required = false) String roleName,
            @Parameter(description = "角色编码") @RequestParam(required = false) String roleCode,
            @Parameter(description = "状态") @RequestParam(required = false) Integer status) {
        try {
            Page<Role> page = new Page<>(current, size);
            IPage<Role> result = roleService.getRolePage(page, roleName, roleCode, status);
            return Result.success(result);
        } catch (Exception e) {
            log.error("分页查询角色失败: {}", e.getMessage(), e);
            return Result.error(e.getMessage());
        }
    }

    @GetMapping("/list")
    @Operation(summary = "获取可用角色列表", description = "获取所有可用的角色列表")
    public Result<List<Role>> getAvailableRoles() {
        try {
            List<Role> roles = roleService.getAvailableRoles();
            return Result.success(roles);
        } catch (Exception e) {
            log.error("获取可用角色列表失败: {}", e.getMessage(), e);
            return Result.error(e.getMessage());
        }
    }

    @GetMapping("/user-roles/{userId}")
    @Operation(summary = "获取用户角色", description = "根据用户ID获取角色列表")
    public Result<List<Role>> getUserRoles(@PathVariable @NotNull Long userId) {
        try {
            List<Role> roles = roleService.getRolesByUserId(userId);
            return Result.success(roles);
        } catch (Exception e) {
            log.error("获取用户角色失败: {}", e.getMessage(), e);
            return Result.error(e.getMessage());
        }
    }

    @PostMapping("/create")
    @Operation(summary = "创建角色", description = "创建新角色")
    public Result<Boolean> createRole(@Valid @RequestBody Role role) {
        try {
            boolean result = roleService.createRole(role);
            return Result.success(result, "创建成功");
        } catch (Exception e) {
            log.error("创建角色失败: {}", e.getMessage(), e);
            return Result.error(e.getMessage());
        }
    }

    @PutMapping("/update")
    @Operation(summary = "更新角色", description = "更新角色信息")
    public Result<Boolean> updateRole(@Valid @RequestBody Role role) {
        try {
            boolean result = roleService.updateRole(role);
            return Result.success(result, "更新成功");
        } catch (Exception e) {
            log.error("更新角色失败: {}", e.getMessage(), e);
            return Result.error(e.getMessage());
        }
    }

    @DeleteMapping("/delete/{roleId}")
    @Operation(summary = "删除角色", description = "根据角色ID删除角色")
    public Result<Boolean> deleteRole(@PathVariable @NotNull Long roleId) {
        try {
            boolean result = roleService.deleteRole(roleId);
            return Result.success(result, "删除成功");
        } catch (Exception e) {
            log.error("删除角色失败: {}", e.getMessage(), e);
            return Result.error(e.getMessage());
        }
    }

    @DeleteMapping("/batch-delete")
    @Operation(summary = "批量删除角色", description = "批量删除角色")
    public Result<Boolean> batchDeleteRoles(@RequestBody @NotEmpty List<Long> roleIds) {
        try {
            boolean result = roleService.batchDeleteRoles(roleIds);
            return Result.success(result, "批量删除成功");
        } catch (Exception e) {
            log.error("批量删除角色失败: {}", e.getMessage(), e);
            return Result.error(e.getMessage());
        }
    }

    @PutMapping("/status/{roleId}")
    @Operation(summary = "更新角色状态", description = "启用/禁用角色")
    public Result<Boolean> updateRoleStatus(@PathVariable @NotNull Long roleId,
                                           @RequestParam @NotNull Integer status) {
        try {
            boolean result = roleService.updateRoleStatus(roleId, status);
            return Result.success(result, "状态更新成功");
        } catch (Exception e) {
            log.error("更新角色状态失败: {}", e.getMessage(), e);
            return Result.error(e.getMessage());
        }
    }

    @PutMapping("/batch-status")
    @Operation(summary = "批量更新角色状态", description = "批量启用/禁用角色")
    public Result<Boolean> batchUpdateRoleStatus(@RequestBody @NotEmpty List<Long> roleIds,
                                                @RequestParam @NotNull Integer status) {
        try {
            boolean result = roleService.batchUpdateRoleStatus(roleIds, status);
            return Result.success(result, "批量状态更新成功");
        } catch (Exception e) {
            log.error("批量更新角色状态失败: {}", e.getMessage(), e);
            return Result.error(e.getMessage());
        }
    }

    @GetMapping("/users/{roleId}")
    @Operation(summary = "获取角色用户", description = "获取角色下的用户列表")
    public Result<List<User>> getRoleUsers(@PathVariable @NotNull Long roleId) {
        try {
            List<User> users = roleService.getUsersByRoleId(roleId);
            // 清除密码信息
            users.forEach(user -> user.setPassword(null));
            return Result.success(users);
        } catch (Exception e) {
            log.error("获取角色用户失败: {}", e.getMessage(), e);
            return Result.error(e.getMessage());
        }
    }

    @GetMapping("/check-code")
    @Operation(summary = "检查角色编码", description = "检查角色编码是否存在")
    public Result<Boolean> checkRoleCode(@RequestParam String roleCode,
                                        @RequestParam(required = false) Long excludeRoleId) {
        try {
            boolean exists = roleService.isRoleCodeExists(roleCode, excludeRoleId);
            return Result.success(exists);
        } catch (Exception e) {
            log.error("检查角色编码失败: {}", e.getMessage(), e);
            return Result.error(e.getMessage());
        }
    }

    @GetMapping("/check-name")
    @Operation(summary = "检查角色名称", description = "检查角色名称是否存在")
    public Result<Boolean> checkRoleName(@RequestParam String roleName,
                                        @RequestParam(required = false) Long excludeRoleId) {
        try {
            boolean exists = roleService.isRoleNameExists(roleName, excludeRoleId);
            return Result.success(exists);
        } catch (Exception e) {
            log.error("检查角色名称失败: {}", e.getMessage(), e);
            return Result.error(e.getMessage());
        }
    }

    @GetMapping("/check-usage/{roleId}")
    @Operation(summary = "检查角色使用情况", description = "检查角色是否被使用")
    public Result<Boolean> checkRoleUsage(@PathVariable @NotNull Long roleId) {
        try {
            boolean inUse = roleService.isRoleInUse(roleId);
            return Result.success(inUse);
        } catch (Exception e) {
            log.error("检查角色使用情况失败: {}", e.getMessage(), e);
            return Result.error(e.getMessage());
        }
    }
}