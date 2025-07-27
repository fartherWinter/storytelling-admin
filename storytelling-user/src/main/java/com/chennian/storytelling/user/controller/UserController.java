package com.chennian.storytelling.user.controller;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.chennian.storytelling.common.result.Result;
import com.chennian.storytelling.user.entity.User;
import com.chennian.storytelling.user.service.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.util.List;

/**
 * 用户控制器
 * 
 * @author chennian
 * @since 2024-01-01
 */
@Slf4j
@RestController
@RequestMapping("/api/user")
@RequiredArgsConstructor
@Validated
@Tag(name = "用户管理", description = "用户相关接口")
public class UserController {

    private final UserService userService;

    @PostMapping("/register")
    @Operation(summary = "用户注册", description = "用户注册接口")
    public Result<Boolean> register(@Valid @RequestBody User user) {
        try {
            boolean result = userService.register(user);
            return Result.success(result, "注册成功");
        } catch (Exception e) {
            log.error("用户注册失败: {}", e.getMessage(), e);
            return Result.error(e.getMessage());
        }
    }

    @PostMapping("/login")
    @Operation(summary = "用户登录", description = "用户登录接口")
    public Result<User> login(@RequestParam String username, 
                             @RequestParam String password,
                             HttpServletRequest request) {
        try {
            String loginIp = getClientIp(request);
            User user = userService.login(username, password, loginIp);
            return Result.success(user, "登录成功");
        } catch (Exception e) {
            log.error("用户登录失败: {}", e.getMessage(), e);
            return Result.error(e.getMessage());
        }
    }

    @GetMapping("/info/{userId}")
    @Operation(summary = "获取用户信息", description = "根据用户ID获取用户信息")
    public Result<User> getUserInfo(@PathVariable @NotNull Long userId) {
        try {
            User user = userService.getById(userId);
            if (user != null) {
                user.setPassword(null); // 清除密码信息
            }
            return Result.success(user);
        } catch (Exception e) {
            log.error("获取用户信息失败: {}", e.getMessage(), e);
            return Result.error(e.getMessage());
        }
    }

    @GetMapping("/page")
    @Operation(summary = "分页查询用户", description = "分页查询用户列表")
    public Result<IPage<User>> getUserPage(
            @Parameter(description = "页码") @RequestParam(defaultValue = "1") Integer current,
            @Parameter(description = "每页大小") @RequestParam(defaultValue = "10") Integer size,
            @Parameter(description = "用户名") @RequestParam(required = false) String username,
            @Parameter(description = "昵称") @RequestParam(required = false) String nickname,
            @Parameter(description = "邮箱") @RequestParam(required = false) String email,
            @Parameter(description = "手机号") @RequestParam(required = false) String phone,
            @Parameter(description = "状态") @RequestParam(required = false) Integer status,
            @Parameter(description = "用户类型") @RequestParam(required = false) Integer userType) {
        try {
            Page<User> page = new Page<>(current, size);
            IPage<User> result = userService.getUserPage(page, username, nickname, email, phone, status, userType);
            // 清除密码信息
            result.getRecords().forEach(user -> user.setPassword(null));
            return Result.success(result);
        } catch (Exception e) {
            log.error("分页查询用户失败: {}", e.getMessage(), e);
            return Result.error(e.getMessage());
        }
    }

    @PostMapping("/create")
    @Operation(summary = "创建用户", description = "创建新用户")
    public Result<Boolean> createUser(@Valid @RequestBody User user,
                                     @RequestParam(required = false) List<Long> roleIds) {
        try {
            boolean result = userService.createUser(user, roleIds);
            return Result.success(result, "创建成功");
        } catch (Exception e) {
            log.error("创建用户失败: {}", e.getMessage(), e);
            return Result.error(e.getMessage());
        }
    }

    @PutMapping("/update")
    @Operation(summary = "更新用户", description = "更新用户信息")
    public Result<Boolean> updateUser(@Valid @RequestBody User user) {
        try {
            boolean result = userService.updateUser(user);
            return Result.success(result, "更新成功");
        } catch (Exception e) {
            log.error("更新用户失败: {}", e.getMessage(), e);
            return Result.error(e.getMessage());
        }
    }

    @DeleteMapping("/delete/{userId}")
    @Operation(summary = "删除用户", description = "根据用户ID删除用户")
    public Result<Boolean> deleteUser(@PathVariable @NotNull Long userId) {
        try {
            boolean result = userService.deleteUser(userId);
            return Result.success(result, "删除成功");
        } catch (Exception e) {
            log.error("删除用户失败: {}", e.getMessage(), e);
            return Result.error(e.getMessage());
        }
    }

    @DeleteMapping("/batch-delete")
    @Operation(summary = "批量删除用户", description = "批量删除用户")
    public Result<Boolean> batchDeleteUsers(@RequestBody @NotEmpty List<Long> userIds) {
        try {
            boolean result = userService.batchDeleteUsers(userIds);
            return Result.success(result, "批量删除成功");
        } catch (Exception e) {
            log.error("批量删除用户失败: {}", e.getMessage(), e);
            return Result.error(e.getMessage());
        }
    }

    @PutMapping("/status/{userId}")
    @Operation(summary = "更新用户状态", description = "启用/禁用用户")
    public Result<Boolean> updateUserStatus(@PathVariable @NotNull Long userId,
                                           @RequestParam @NotNull Integer status) {
        try {
            boolean result = userService.updateUserStatus(userId, status);
            return Result.success(result, "状态更新成功");
        } catch (Exception e) {
            log.error("更新用户状态失败: {}", e.getMessage(), e);
            return Result.error(e.getMessage());
        }
    }

    @PutMapping("/batch-status")
    @Operation(summary = "批量更新用户状态", description = "批量启用/禁用用户")
    public Result<Boolean> batchUpdateUserStatus(@RequestBody @NotEmpty List<Long> userIds,
                                                @RequestParam @NotNull Integer status) {
        try {
            boolean result = userService.batchUpdateUserStatus(userIds, status);
            return Result.success(result, "批量状态更新成功");
        } catch (Exception e) {
            log.error("批量更新用户状态失败: {}", e.getMessage(), e);
            return Result.error(e.getMessage());
        }
    }

    @PutMapping("/reset-password/{userId}")
    @Operation(summary = "重置密码", description = "管理员重置用户密码")
    public Result<Boolean> resetPassword(@PathVariable @NotNull Long userId,
                                        @RequestParam String newPassword) {
        try {
            boolean result = userService.resetPassword(userId, newPassword);
            return Result.success(result, "密码重置成功");
        } catch (Exception e) {
            log.error("重置密码失败: {}", e.getMessage(), e);
            return Result.error(e.getMessage());
        }
    }

    @PutMapping("/change-password/{userId}")
    @Operation(summary = "修改密码", description = "用户修改密码")
    public Result<Boolean> changePassword(@PathVariable @NotNull Long userId,
                                         @RequestParam String oldPassword,
                                         @RequestParam String newPassword) {
        try {
            boolean result = userService.changePassword(userId, oldPassword, newPassword);
            return Result.success(result, "密码修改成功");
        } catch (Exception e) {
            log.error("修改密码失败: {}", e.getMessage(), e);
            return Result.error(e.getMessage());
        }
    }

    @PutMapping("/assign-roles/{userId}")
    @Operation(summary = "分配角色", description = "为用户分配角色")
    public Result<Boolean> assignUserRoles(@PathVariable @NotNull Long userId,
                                          @RequestBody List<Long> roleIds) {
        try {
            boolean result = userService.assignUserRoles(userId, roleIds);
            return Result.success(result, "角色分配成功");
        } catch (Exception e) {
            log.error("分配角色失败: {}", e.getMessage(), e);
            return Result.error(e.getMessage());
        }
    }

    @GetMapping("/roles/{userId}")
    @Operation(summary = "获取用户角色", description = "获取用户的角色列表")
    public Result<List<Long>> getUserRoles(@PathVariable @NotNull Long userId) {
        try {
            List<Long> roleIds = userService.getUserRoleIds(userId);
            return Result.success(roleIds);
        } catch (Exception e) {
            log.error("获取用户角色失败: {}", e.getMessage(), e);
            return Result.error(e.getMessage());
        }
    }

    @GetMapping("/check-username")
    @Operation(summary = "检查用户名", description = "检查用户名是否存在")
    public Result<Boolean> checkUsername(@RequestParam String username,
                                        @RequestParam(required = false) Long excludeUserId) {
        try {
            boolean exists = userService.isUsernameExists(username, excludeUserId);
            return Result.success(exists);
        } catch (Exception e) {
            log.error("检查用户名失败: {}", e.getMessage(), e);
            return Result.error(e.getMessage());
        }
    }

    @GetMapping("/check-email")
    @Operation(summary = "检查邮箱", description = "检查邮箱是否存在")
    public Result<Boolean> checkEmail(@RequestParam String email,
                                     @RequestParam(required = false) Long excludeUserId) {
        try {
            boolean exists = userService.isEmailExists(email, excludeUserId);
            return Result.success(exists);
        } catch (Exception e) {
            log.error("检查邮箱失败: {}", e.getMessage(), e);
            return Result.error(e.getMessage());
        }
    }

    @GetMapping("/check-phone")
    @Operation(summary = "检查手机号", description = "检查手机号是否存在")
    public Result<Boolean> checkPhone(@RequestParam String phone,
                                     @RequestParam(required = false) Long excludeUserId) {
        try {
            boolean exists = userService.isPhoneExists(phone, excludeUserId);
            return Result.success(exists);
        } catch (Exception e) {
            log.error("检查手机号失败: {}", e.getMessage(), e);
            return Result.error(e.getMessage());
        }
    }

    /**
     * 获取客户端IP地址
     */
    private String getClientIp(HttpServletRequest request) {
        String ip = request.getHeader("X-Forwarded-For");
        if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getHeader("Proxy-Client-IP");
        }
        if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getHeader("WL-Proxy-Client-IP");
        }
        if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getRemoteAddr();
        }
        return ip;
    }
}