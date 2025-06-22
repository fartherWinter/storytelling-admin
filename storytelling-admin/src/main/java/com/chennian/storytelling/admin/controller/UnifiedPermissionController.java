package com.chennian.storytelling.admin.controller;

import com.chennian.security.admin.util.SecurityUtils;
import com.chennian.storytelling.service.IUnifiedPermissionService;
import com.chennian.storytelling.bean.dto.RoleDTO;
import com.chennian.storytelling.bean.dto.UserRoleDTO;
import com.chennian.storytelling.bean.model.SysPermission;
import com.chennian.storytelling.bean.model.SysRole;
import com.chennian.storytelling.bean.model.SysUser;
import com.chennian.storytelling.common.response.ServerResponseEntity;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.chennian.storytelling.service.SysUserService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import java.util.List;

/**
 * 统一权限管理控制器
 * 融合原有系统权限和工作流权限管理
 * 
 * @author chennian
 * @date 2024-01-20
 */
@Api(tags = "统一权限管理")
@RestController
@RequestMapping("/admin/unified-permission")
@Validated
public class UnifiedPermissionController {

    private final IUnifiedPermissionService unifiedPermissionService;
    private final SysUserService sysUserService;
    public UnifiedPermissionController(IUnifiedPermissionService unifiedPermissionService, SysUserService sysUserService) {
        this.unifiedPermissionService = unifiedPermissionService;
        this.sysUserService = sysUserService;
    }

    // =====================================================
    // 角色管理
    // =====================================================

    @ApiOperation("分页查询角色列表")
    @GetMapping("/roles")
    @PreAuthorize("hasAuthority('system:role:list')")
    public ServerResponseEntity<IPage<SysRole>> getRoleList(RoleDTO roleDTO) {
        return unifiedPermissionService.getRoleList(roleDTO);
    }

    @ApiOperation("根据角色ID查询角色信息")
    @GetMapping("/roles/{roleId}")
    @PreAuthorize("hasAuthority('system:role:query')")
    public ServerResponseEntity<SysRole> getRoleById(
            @ApiParam("角色ID") @PathVariable @NotNull Long roleId) {
        return unifiedPermissionService.getRoleById(roleId);
    }

    @ApiOperation("新增角色")
    @PostMapping("/roles")
    @PreAuthorize("hasAuthority('system:role:add')")
    public ServerResponseEntity<Void> saveRole(@Valid @RequestBody SysRole role) {
        SysUser sysUser = sysUserService.selectUserById(SecurityUtils.getUserId());
        role.setCreateBy(sysUser.getUserName());
        return unifiedPermissionService.saveRole(role);
    }

    @ApiOperation("修改角色")
    @PutMapping("/roles")
    @PreAuthorize("hasAuthority('system:role:edit')")
    public ServerResponseEntity<Void> updateRole(@Valid @RequestBody SysRole role) {
        SysUser sysUser = sysUserService.selectUserById(SecurityUtils.getUserId());
        role.setUpdateBy(sysUser.getUserName());
        return unifiedPermissionService.updateRole(role);
    }

    @ApiOperation("删除角色")
    @DeleteMapping("/roles/{roleId}")
    @PreAuthorize("hasAuthority('system:role:remove')")
    public ServerResponseEntity<Void> deleteRole(
            @ApiParam("角色ID") @PathVariable @NotNull Long roleId) {
        return unifiedPermissionService.deleteRole(roleId);
    }

    // =====================================================
    // 权限管理
    // =====================================================

    @ApiOperation("获取权限树")
    @GetMapping("/permissions/tree")
    @PreAuthorize("hasAuthority('system:permission:list')")
    public ServerResponseEntity<List<SysPermission>> getPermissionTree(
            @ApiParam("权限类型") @RequestParam(required = false) String permissionType) {
        return unifiedPermissionService.getPermissionTree(permissionType);
    }

    @ApiOperation("根据角色ID获取权限列表")
    @GetMapping("/permissions/role/{roleId}")
    @PreAuthorize("hasAuthority('system:permission:query')")
    public ServerResponseEntity<List<SysPermission>> getPermissionsByRoleId(
            @ApiParam("角色ID") @PathVariable @NotNull Long roleId) {
        return unifiedPermissionService.getPermissionsByRoleId(roleId);
    }

    @ApiOperation("新增权限")
    @PostMapping("/permissions")
    @PreAuthorize("hasAuthority('system:permission:add')")
    public ServerResponseEntity<Void> savePermission(@Valid @RequestBody SysPermission permission) {
        SysUser sysUser = sysUserService.selectUserById(SecurityUtils.getUserId());
        permission.setCreateBy(sysUser.getUserName());
        return unifiedPermissionService.savePermission(permission);
    }

    @ApiOperation("修改权限")
    @PutMapping("/permissions")
    @PreAuthorize("hasAuthority('system:permission:edit')")
    public ServerResponseEntity<Void> updatePermission(@Valid @RequestBody SysPermission permission) {
        SysUser sysUser = sysUserService.selectUserById(SecurityUtils.getUserId());
        permission.setUpdateBy(sysUser.getUserName());
        return unifiedPermissionService.updatePermission(permission);
    }

    @ApiOperation("删除权限")
    @DeleteMapping("/permissions/{permissionId}")
    @PreAuthorize("hasAuthority('system:permission:remove')")
    public ServerResponseEntity<Void> deletePermission(
            @ApiParam("权限ID") @PathVariable @NotNull Long permissionId) {
        return unifiedPermissionService.deletePermission(permissionId);
    }

    // =====================================================
    // 角色权限关联管理
    // =====================================================

    @ApiOperation("为角色分配权限")
    @PostMapping("/roles/{roleId}/permissions")
    @PreAuthorize("hasAuthority('system:role:edit')")
    public ServerResponseEntity<Void> assignPermissionsToRole(
            @ApiParam("角色ID") @PathVariable @NotNull Long roleId,
            @ApiParam("权限ID列表") @RequestBody @NotEmpty List<Long> permissionIds) {
        return unifiedPermissionService.assignPermissionsToRole(roleId, permissionIds);
    }

    // =====================================================
    // 用户角色管理
    // =====================================================

    @ApiOperation("根据用户ID获取角色列表")
    @GetMapping("/users/{userId}/roles")
    @PreAuthorize("hasAuthority('system:user:query')")
    public ServerResponseEntity<List<SysRole>> getRolesByUserId(
            @ApiParam("用户ID") @PathVariable @NotNull Long userId) {
        return unifiedPermissionService.getRolesByUserId(userId);
    }

    @ApiOperation("为用户分配角色")
    @PostMapping("/users/{userId}/roles")
    @PreAuthorize("hasAuthority('system:user:edit')")
    public ServerResponseEntity<Void> assignRolesToUser(
            @ApiParam("用户ID") @PathVariable @NotNull Long userId,
            @ApiParam("用户角色关联信息列表") @RequestBody @NotEmpty List<UserRoleDTO> userRoles) {
        return unifiedPermissionService.assignRolesToUser(userId, userRoles);
    }

    @ApiOperation("获取用户权限列表")
    @GetMapping("/users/{userId}/permissions")
    @PreAuthorize("hasAuthority('system:user:query')")
    public ServerResponseEntity<List<String>> getUserPermissions(
            @ApiParam("用户ID") @PathVariable @NotNull Long userId) {
        return unifiedPermissionService.getUserPermissions(userId);
    }

    @ApiOperation("检查用户是否有指定权限")
    @GetMapping("/users/{userId}/permissions/{permissionCode}/check")
    @PreAuthorize("hasAuthority('system:user:query')")
    public ServerResponseEntity<Boolean> hasPermission(
            @ApiParam("用户ID") @PathVariable @NotNull Long userId,
            @ApiParam("权限编码") @PathVariable @NotNull String permissionCode) {
        return unifiedPermissionService.hasPermission(userId, permissionCode);
    }

    // =====================================================
    // 工作流权限管理
    // =====================================================

    @ApiOperation("获取工作流角色列表")
    @GetMapping("/workflow/roles")
    @PreAuthorize("hasAuthority('workflow:role:list')")
    public ServerResponseEntity<List<SysRole>> getWorkflowRoles() {
        return unifiedPermissionService.getWorkflowRoles();
    }

    @ApiOperation("获取工作流权限列表")
    @GetMapping("/workflow/permissions")
    @PreAuthorize("hasAuthority('workflow:permission:list')")
    public ServerResponseEntity<List<SysPermission>> getWorkflowPermissions() {
        return unifiedPermissionService.getWorkflowPermissions();
    }

    @ApiOperation("检查用户是否有工作流操作权限")
    @GetMapping("/workflow/users/{userId}/permissions/{processKey}/{operation}/check")
    @PreAuthorize("hasAuthority('workflow:permission:check')")
    public ServerResponseEntity<Boolean> hasWorkflowPermission(
            @ApiParam("用户ID") @PathVariable @NotNull Long userId,
            @ApiParam("流程标识") @PathVariable @NotNull String processKey,
            @ApiParam("操作类型") @PathVariable @NotNull String operation) {
        return unifiedPermissionService.hasWorkflowPermission(userId, processKey, operation);
    }

    // =====================================================
    // 当前用户权限查询
    // =====================================================

    @ApiOperation("获取当前用户权限列表")
    @GetMapping("/current-user/permissions")
    public ServerResponseEntity<List<String>> getCurrentUserPermissions() {
        Long userId = SecurityUtils.getUserId();
        return unifiedPermissionService.getUserPermissions(userId);
    }

    @ApiOperation("检查当前用户是否有指定权限")
    @GetMapping("/current-user/permissions/{permissionCode}/check")
    public ServerResponseEntity<Boolean> hasCurrentUserPermission(
            @ApiParam("权限编码") @PathVariable @NotNull String permissionCode) {
        Long userId = SecurityUtils.getUserId();
        return unifiedPermissionService.hasPermission(userId, permissionCode);
    }

    @ApiOperation("获取当前用户角色列表")
    @GetMapping("/current-user/roles")
    public ServerResponseEntity<List<SysRole>> getCurrentUserRoles() {
        Long userId = SecurityUtils.getUserId();
        return unifiedPermissionService.getRolesByUserId(userId);
    }

    @ApiOperation("检查当前用户是否有工作流操作权限")
    @GetMapping("/current-user/workflow/{processKey}/{operation}/check")
    public ServerResponseEntity<Boolean> hasCurrentUserWorkflowPermission(
            @ApiParam("流程标识") @PathVariable @NotNull String processKey,
            @ApiParam("操作类型") @PathVariable @NotNull String operation) {
        Long userId = SecurityUtils.getUserId();
        return unifiedPermissionService.hasWorkflowPermission(userId, processKey, operation);
    }

    // =====================================================
    // 权限统计和报表
    // =====================================================

    @ApiOperation("获取权限统计信息")
    @GetMapping("/statistics")
    @PreAuthorize("hasAuthority('system:permission:statistics')")
    public ServerResponseEntity<Object> getPermissionStatistics() {
        // TODO: 实现权限统计功能
        return ServerResponseEntity.success("权限统计功能待实现");
    }

    @ApiOperation("导出权限配置")
    @GetMapping("/export")
    @PreAuthorize("hasAuthority('system:permission:export')")
    public ServerResponseEntity<Object> exportPermissionConfig() {
        // TODO: 实现权限配置导出功能
        return ServerResponseEntity.success("权限配置导出功能待实现");
    }

    @ApiOperation("导入权限配置")
    @PostMapping("/import")
    @PreAuthorize("hasAuthority('system:permission:import')")
    public ServerResponseEntity<Object> importPermissionConfig() {
        // TODO: 实现权限配置导入功能
        return ServerResponseEntity.success("权限配置导入功能待实现");
    }
}