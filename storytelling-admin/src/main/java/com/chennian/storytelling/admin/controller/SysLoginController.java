package com.chennian.storytelling.admin.controller;

import cn.dev33.satoken.stp.StpUtil;
import com.chennian.storytelling.bean.LoginBody;
import com.chennian.storytelling.bean.bo.SysPartitionRouterBo;
import com.chennian.storytelling.bean.model.SysMenu;
import com.chennian.storytelling.bean.model.SysUser;
import com.chennian.storytelling.bean.vo.RouterVo;
import com.chennian.storytelling.common.bean.Token;
import com.chennian.storytelling.common.annotation.EventTrack;
import com.chennian.storytelling.common.enums.BusinessType;
import com.chennian.storytelling.common.enums.ModelType;
import com.chennian.storytelling.common.response.ServerResponseEntity;
import com.chennian.storytelling.service.SysLoginService;
import com.chennian.storytelling.service.SysMenuService;
import com.chennian.storytelling.service.SysPermissionService;
import com.chennian.security.admin.util.SecurityUtils;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 *  登录验证
 *
 * @author by chennian
 * @date 2025/5/6.
 */
@RestController
@Tag(name = "登录管理")
@RequestMapping("/sys/login")
public class SysLoginController {
    private final SysMenuService menuService;

    private final SysPermissionService permissionService;

    private final SysLoginService sysLoginService;

    public SysLoginController(SysMenuService menuService, SysPermissionService permissionService, SysLoginService sysLoginService) {
        this.menuService = menuService;
        this.permissionService = permissionService;
        this.sysLoginService = sysLoginService;
    }

    @PostMapping("/login")
    @Operation(summary = "登录")
    @EventTrack(title = ModelType.SYSTEM, businessType = BusinessType.GRANT, description = "登录")
    public ServerResponseEntity<Token> login(@RequestBody LoginBody loginBody) {
        return ServerResponseEntity.success(sysLoginService.login(loginBody.getUserName(), loginBody.getPassWord(), loginBody.getCode(), loginBody.getUuid()));
    }

    @PostMapping("/logout")
    @Operation(summary = "登出")
    @EventTrack(title = ModelType.SYSTEM, businessType = BusinessType.FORCE, description = "登出")
    public ServerResponseEntity<String> logout() {
        StpUtil.logout();
        return ServerResponseEntity.success("登出成功");
    }

    /**
     * 获取用户信息
     */
    @PostMapping("/getInfo")
    @Operation(summary = "获取当前用户信息")
    @EventTrack(title = ModelType.SYSTEM, businessType = BusinessType.SEARCH, description = "获取当前用户信息")
    public ServerResponseEntity<Map<String, Object>> getInfo() {
        SysUser user = new SysUser();
        //获取角色集合
        Set<String> roles = permissionService.getRolePermission(user);
        //获取权限集合
        Set<String> permissions = permissionService.getMenuPermission(user);
        Map<String, Object> resMap = new HashMap<>();
        resMap.put("user", user);
        resMap.put("roles", roles);
        resMap.put("permission", permissions);
        return ServerResponseEntity.success(resMap);
    }

    @PostMapping("/getRouters")
    @Operation(summary = "路由信息")
    @EventTrack(title = ModelType.SYSTEM, businessType = BusinessType.SEARCH, description = "路由信息")
    public ServerResponseEntity<List<RouterVo>> router() {
        Long userId = Long.valueOf(SecurityUtils.getUser());
        List<SysMenu> sysMenus = menuService.selectMenuTreeByUserId(userId);
        List<RouterVo> routerVos = menuService.buildMenus(sysMenus);
        return ServerResponseEntity.success(routerVos);
    }

    /**
     * 分区路由信息
     */
    @PostMapping("/partition/getRouters")
    @Operation(summary = "分区路由信息")
    @EventTrack(title = ModelType.SYSTEM, businessType = BusinessType.SEARCH, description = "分区路由信息")
    public ServerResponseEntity<List<RouterVo>> partitionRouter(SysPartitionRouterBo bo) {
        SecurityUtils.getUser();
        List<SysMenu> sysMenus = menuService.selectMenuTreeByPartitionId(bo.getPartitionId());
        List<RouterVo> routerVos = menuService.buildMenus(sysMenus);
        return ServerResponseEntity.success(routerVos);
    }
}
