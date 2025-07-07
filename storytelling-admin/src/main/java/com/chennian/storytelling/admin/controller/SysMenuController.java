package com.chennian.storytelling.admin.controller;

import com.chennian.storytelling.common.annotation.EventTrack;
import com.chennian.storytelling.common.enums.BusinessType;
import com.chennian.storytelling.common.enums.ModelType;

import com.chennian.security.admin.util.SecurityUtils;
import com.chennian.storytelling.bean.TreeSelect;
import com.chennian.storytelling.bean.model.SysMenu;
import com.chennian.storytelling.common.response.ServerResponseEntity;
import com.chennian.storytelling.common.utils.StringUtils;
import com.chennian.storytelling.service.SysMenuService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author by chennian
 * @date 2025/5/16.
 */
@RestController
@RequestMapping("/sys/menu")
@Tag(name = "菜单管理")
public class SysMenuController {

    private final SysMenuService sysMenuService;

    public SysMenuController(SysMenuService sysMenuService) {
        this.sysMenuService = sysMenuService;
    }

    /**
     * 获取菜单列表
     */
    @PostMapping("/list")
    @Operation(summary = "菜单列表")
    @EventTrack(title = ModelType.SYSTEM, businessType = BusinessType.SEARCH, description = "菜单列表")
    public ServerResponseEntity<List<SysMenu>> list(@RequestBody SysMenu menu) {
        List<SysMenu> menus = sysMenuService.selectList(menu, Long.valueOf(SecurityUtils.getUser()));
        return ServerResponseEntity.success(menus);
    }

    /**
     * 根据菜单编号获取详细信息
     */
    @PostMapping("/getInfo/{sysMenuId}")
    @Operation(summary = "细节")
    @EventTrack(title = ModelType.SYSTEM, businessType = BusinessType.SEARCH, description = "细节")
    public ServerResponseEntity<SysMenu> info(@PathVariable("sysMenuId") Long menuId) {
        return ServerResponseEntity.success(sysMenuService.selectMenuById(menuId));
    }

    @PostMapping("/update")
    @Operation(summary = "修改")
    @EventTrack(title = ModelType.SYSTEM, businessType = BusinessType.UPDATE, description = "修改")
    public ServerResponseEntity<Integer> update(@Validated @RequestBody SysMenu sysMenu) {
        if (!sysMenuService.checkMenuNameUnique(sysMenu)) {
            return ServerResponseEntity.showFailMsg("修改菜单'" + sysMenu.getName() + "'失败，菜单名称已存在");
        } else if (!StringUtils.ishttp(sysMenu.getPath())) {
            return ServerResponseEntity.showFailMsg("修改菜单'" + sysMenu.getName() + "'失败，地址必须以http(s)://开头");
        } else if (sysMenu.getId().equals(sysMenu.getParentId())) {
            return ServerResponseEntity.showFailMsg("修改菜单'" + sysMenu.getName() + "'失败，上级菜单不能选择自己");
        }
        return ServerResponseEntity.success(sysMenuService.updateMenu(sysMenu));
    }

    /**
     * 新增菜单
     */
    @PostMapping("/add")
    @Operation(summary = "新增菜单")
    @EventTrack(title = ModelType.SYSTEM, businessType = BusinessType.INSERT, description = "新增菜单")
    public ServerResponseEntity<Integer> add(@Validated @RequestBody SysMenu sysMenu) {
        if (!sysMenuService.checkMenuNameUnique(sysMenu)) {
            return ServerResponseEntity.showFailMsg("新增菜单'" + sysMenu.getName() + "'失败，菜单名称已存在");
        } else if (!StringUtils.ishttp(sysMenu.getPath())) {
            return ServerResponseEntity.showFailMsg("新增菜单'" + sysMenu.getName() + "'失败，地址必须以http(s)://开头");
        }
        // 写入操作人员
        sysMenu.setCreateBy(SecurityUtils.getUsername());
        return ServerResponseEntity.success(sysMenuService.insertMenu(sysMenu));
    }

    /**
     * 删除菜单
     */
    @PostMapping("/remove/{menuId}")
    @Operation(summary = "删除")
    @EventTrack(title = ModelType.SYSTEM, businessType = BusinessType.DELETE, description = "删除")
    public ServerResponseEntity<Integer> remove(@PathVariable("menuId") Long menuId) {
        if (sysMenuService.hasChildByMenuId(menuId)) {
            return ServerResponseEntity.showFailMsg("存在子菜单不允许删除");
        }
        if (sysMenuService.checkMenuExistRole(menuId)) {
            return ServerResponseEntity.showFailMsg("菜单已分配权限，不允许删除");
        }
        return ServerResponseEntity.success(sysMenuService.deleteMenuById(menuId));
    }

    /**
     * 获取菜单下拉树列表
     */
    @PostMapping("/treeselect")
    @Operation(summary = "获取菜单下拉树列表")
    @EventTrack(title = ModelType.SYSTEM, businessType = BusinessType.SEARCH, description = "获取菜单下拉树列表")
    public ServerResponseEntity<List<TreeSelect>> treeselect(SysMenu menu) {
        List<SysMenu> menus = sysMenuService.selectMenuList(menu, Long.valueOf(SecurityUtils.getUser()));
        return ServerResponseEntity.success(sysMenuService.buildMenuTreeSelect(menus));
    }

    /**
     * 加载对应角色菜单列表树
     */
    @PostMapping("/roleMenuTreeselect/{roleId}")
    @Operation(summary = "查询对应角色菜单树")
    @EventTrack(title = ModelType.SYSTEM, businessType = BusinessType.SEARCH, description = "查询对应角色菜单树")
    public ServerResponseEntity<Map<String, Object>> roleMenuTreeselect(@PathVariable("roleId") Long roleId) {
        List<SysMenu> menus = sysMenuService.selectMenuList(Long.valueOf(SecurityUtils.getUser()));
        Map<String, Object> resMap = new HashMap<>(16);
        resMap.put("checkedKeys", sysMenuService.selectMenuListByRoleId(roleId));
        resMap.put("menus", sysMenuService.buildMenuTreeSelect(menus));
        return ServerResponseEntity.success(resMap);
    }


    /**
     * 加载对应分区菜单列表树
     */
    @PostMapping("/partitionMenuTreeselect/{partitionId}")
    @Operation(summary = "查询对应角色菜单树")
    @EventTrack(title = ModelType.SYSTEM, businessType = BusinessType.SEARCH, description = "查询对应分区菜单树")
    public ServerResponseEntity<Map<String, Object>> partitionMenuTreeselect(@PathVariable("partitionId") Long partitionId) {
        List<SysMenu> menus = sysMenuService.selectMenuList(Long.valueOf(SecurityUtils.getUser()));
        Map<String, Object> resMap = new HashMap<>(16);
        resMap.put("checkedKeys", sysMenuService.selectMenuListByPartitionId(partitionId));
        resMap.put("menus", sysMenuService.buildMenuTreeSelect(menus));
        return ServerResponseEntity.success(resMap);
    }

}
