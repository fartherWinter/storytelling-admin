package com.chennian.storytelling.admin.controller;

import cn.dev33.satoken.annotation.SaCheckRole;
import cn.dev33.satoken.annotation.SaMode;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.chennian.security.admin.util.SecurityUtils;
import com.chennian.storytelling.bean.bo.SysPostSearchBo;
import com.chennian.storytelling.bean.model.SysPost;
import com.chennian.storytelling.common.response.ServerResponseEntity;
import com.chennian.storytelling.common.utils.PageParam;
import com.chennian.storytelling.service.SysPostService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * @author by chennian
 * @date 2025/5/7.
 */
@RestController
@RequestMapping("/sys/position")
@SaCheckRole(value = {"admin", "super admin"}, mode = SaMode.OR)
@Tag(name = "岗位管理")
public class SysPostController {
    private SysPostService sysPostService;
    public SysPostController(SysPostService sysPostService) {
        this.sysPostService = sysPostService;
    }


    /**
     * 岗位列表查询
     *
     * @return
     */
    @PostMapping("/page")
    @Operation(summary = "列表")
    public ServerResponseEntity<IPage<SysPost>> page(SysPostSearchBo sysPostSearchBo, PageParam<SysPost> page) {
        Integer user = SecurityUtils.getUser();
        IPage<SysPost> positionPage = sysPostService.page(page, new LambdaQueryWrapper<SysPost>()
                .eq(!sysPostSearchBo.getStatus().isEmpty(), SysPost::getStatus, sysPostSearchBo.getStatus())
                .like(!sysPostSearchBo.getPostName().isEmpty(), SysPost::getPostName, sysPostSearchBo.getPostName())
                .orderByAsc(SysPost::getPostSort)
        );
        return ServerResponseEntity.success(positionPage);
    }

    @PostMapping("/update")
    @Operation(summary = "修改岗位")
    public ServerResponseEntity<Integer> update(@Validated @RequestBody SysPost post) {
        if (!sysPostService.checkPostNameUnique(post)) {
            return ServerResponseEntity.showFailMsg("修改岗位'" + post.getPostName() + "'失败，岗位名称已存在");
        } else if (!sysPostService.checkPostCodeUnique(post)) {
            return ServerResponseEntity.showFailMsg("修改岗位'" + post.getPostName() + "'失败，岗位编码已存在");
        }
        return ServerResponseEntity.success(sysPostService.updatePost(post));
    }

    @PostMapping("/add")
    @Operation(summary = "新增")
    public ServerResponseEntity<Integer> add(@Validated @RequestBody SysPost post) {
        if (!sysPostService.checkPostNameUnique(post)) {
            return ServerResponseEntity.showFailMsg("新增岗位'" + post.getPostName() + "'失败，岗位名称已存在");
        } else if (!sysPostService.checkPostCodeUnique(post)) {
            return ServerResponseEntity.showFailMsg("新增岗位'" + post.getPostName() + "'失败，岗位编码已存在");
        }
        return ServerResponseEntity.success(sysPostService.insertPost(post));
    }

    /**
     * 删除岗位信息
     */
    @PostMapping("/remove/{postIds}")
    @Operation(summary = "删除")
    public ServerResponseEntity<Integer> del(@PathVariable Long[] postIds) {
        return ServerResponseEntity.success(sysPostService.deletePostByIds(postIds));
    }

    /**
     * 获取岗位信息内容
     */
    @PostMapping("/postInfo/{postId}")
    @Operation(summary = "岗位内容信息")
    public ServerResponseEntity<SysPost> info(@PathVariable("postId") String postId) {
        return ServerResponseEntity.success(sysPostService.getById(postId));
    }

    /**
     * 状态修改
     */
    @PostMapping("/changeStatus")
    @Operation(summary = "状态修改")
    public ServerResponseEntity<Integer> changeStatus(@RequestBody SysPost post) {
        return ServerResponseEntity.success(sysPostService.changeStatus(post));
    }

    /**
     * 获取岗位选择框
     */
    @PostMapping("/optionselect")
    @Operation(summary = "获取岗位下拉框")
    public ServerResponseEntity<List<SysPost>> optionselect() {
        List<SysPost> posts = sysPostService.selectPostAll();
        return ServerResponseEntity.success(posts);
    }
    /**
     * 导出岗位表单
     */
    @PostMapping("/export")
    @Operation(summary = "导出岗位表单数据")
    public ServerResponseEntity<Void>export(){
//        sysPostService.getPostExcel();
        return ServerResponseEntity.success();
    }
}
