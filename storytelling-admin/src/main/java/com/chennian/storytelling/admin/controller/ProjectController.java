package com.chennian.storytelling.admin.controller;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.chennian.storytelling.bean.vo.ProjectProgressVO;
import com.chennian.storytelling.bean.vo.ProjectStatisticsVO;
import com.chennian.storytelling.common.annotation.EventTrack;
import com.chennian.storytelling.common.enums.BusinessType;
import com.chennian.storytelling.common.enums.ModelType;
import com.chennian.storytelling.common.enums.OperatorType;
import com.chennian.storytelling.common.response.ServerResponseEntity;
import com.chennian.storytelling.common.utils.PageParam;
import com.chennian.storytelling.service.ProjectService;
import com.chennian.storytelling.bean.model.project.*;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

/**
 * @author chennian
 * @date 2024/1/15
 */
@RestController
@RequestMapping("/erp/project")
@Tag(name = "项目管理")
public class ProjectController {

    private final ProjectService projectService;

    public ProjectController(ProjectService projectService) {
        this.projectService = projectService;
    }

    // ==================== 项目管理 ====================
    
    /**
     * 查询项目列表
     */
    @PostMapping("/page")
    @Operation(summary = "项目列表")
    @EventTrack(title = ModelType.SYSTEM, businessType = BusinessType.SEARCH, operatorType = OperatorType.MANAGE, description = "项目列表")
    public ServerResponseEntity<IPage<Project>> projectPage(Project project, PageParam<Project> page) {
        IPage<Project> projectPage = projectService.getProjectList(page, project);
        return ServerResponseEntity.success(projectPage);
    }

    /**
     * 获取项目详细信息
     */
    @PostMapping("/info/{projectId}")
    @Operation(summary = "项目详情")
    @EventTrack(title = ModelType.SYSTEM, businessType = BusinessType.SEARCH, operatorType = OperatorType.MANAGE, description = "项目详情")
    public ServerResponseEntity<Project> projectInfo(@PathVariable("projectId") Long projectId) {
        Project project = projectService.getProjectById(projectId);
        return ServerResponseEntity.success(project);
    }

    /**
     * 新增项目
     */
    @PostMapping("/add")
    @Operation(summary = "新增项目")
    @EventTrack(title = ModelType.SYSTEM, businessType = BusinessType.INSERT, operatorType = OperatorType.MANAGE, description = "新增项目")
    public ServerResponseEntity<Void> addProject(@Validated @RequestBody Project project) {
        projectService.addProject(project);
        return ServerResponseEntity.success();
    }

    /**
     * 修改项目
     */
    @PostMapping("/edit")
    @Operation(summary = "修改项目")
    @EventTrack(title = ModelType.SYSTEM, businessType = BusinessType.UPDATE, operatorType = OperatorType.MANAGE, description = "修改项目")
    public ServerResponseEntity<Void> editProject(@Validated @RequestBody Project project) {
        projectService.updateProject(project);
        return ServerResponseEntity.success();
    }

    /**
     * 删除项目
     */
    @PostMapping("/remove/{projectIds}")
    @Operation(summary = "删除项目")
    @EventTrack(title = ModelType.SYSTEM, businessType = BusinessType.DELETE, operatorType = OperatorType.MANAGE, description = "删除项目")
    public ServerResponseEntity<Void> removeProject(@PathVariable Long[] projectIds) {
        projectService.deleteProject(projectIds);
        return ServerResponseEntity.success();
    }

    /**
     * 项目启动
     */
    @PostMapping("/start")
    @Operation(summary = "项目启动")
    @EventTrack(title = ModelType.SYSTEM, businessType = BusinessType.UPDATE, operatorType = OperatorType.MANAGE, description = "项目启动")
    public ServerResponseEntity<Void> startProject(@RequestParam Long projectId) {
        projectService.startProject(projectId);
        return ServerResponseEntity.success();
    }

    /**
     * 项目暂停
     */
    @PostMapping("/pause")
    @Operation(summary = "项目暂停")
    @EventTrack(title = ModelType.SYSTEM, businessType = BusinessType.UPDATE, operatorType = OperatorType.MANAGE, description = "项目暂停")
    public ServerResponseEntity<Void> pauseProject(@RequestParam Long projectId, 
                                                  @RequestParam(required = false) String reason) {
        projectService.pauseProject(projectId, reason);
        return ServerResponseEntity.success();
    }

    /**
     * 项目完成
     */
    @PostMapping("/complete")
    @Operation(summary = "项目完成")
    @EventTrack(title = ModelType.SYSTEM, businessType = BusinessType.UPDATE, operatorType = OperatorType.MANAGE, description = "项目完成")
    public ServerResponseEntity<Void> completeProject(@RequestParam Long projectId,
                                                     @RequestParam(required = false) String summary) {
        projectService.completeProject(projectId, summary);
        return ServerResponseEntity.success();
    }

    /**
     * 项目归档
     */
    @PostMapping("/archive")
    @Operation(summary = "项目归档")
    @EventTrack(title = ModelType.SYSTEM, businessType = BusinessType.UPDATE, operatorType = OperatorType.MANAGE, description = "项目归档")
    public ServerResponseEntity<Void> archiveProject(@RequestParam Long projectId) {
        projectService.archiveProject(projectId);
        return ServerResponseEntity.success();
    }

    // ==================== 项目任务管理 ====================
    
    /**
     * 查询项目任务列表
     */
    @PostMapping("/task/page")
    @Operation(summary = "项目任务列表")
    @EventTrack(title = ModelType.SYSTEM, businessType = BusinessType.SEARCH, operatorType = OperatorType.MANAGE, description = "项目任务列表")
    public ServerResponseEntity<IPage<ProjectTask>> taskPage(ProjectTask task, PageParam<ProjectTask> page) {
        IPage<ProjectTask> taskPage = projectService.getProjectTaskList(page, task);
        return ServerResponseEntity.success(taskPage);
    }

    /**
     * 获取项目任务详细信息
     */
    @PostMapping("/task/info/{taskId}")
    @Operation(summary = "项目任务详情")
    @EventTrack(title = ModelType.SYSTEM, businessType = BusinessType.SEARCH, operatorType = OperatorType.MANAGE, description = "项目任务详情")
    public ServerResponseEntity<ProjectTask> taskInfo(@PathVariable("taskId") Long taskId) {
        ProjectTask task = projectService.getProjectTaskById(taskId);
        return ServerResponseEntity.success(task);
    }

    /**
     * 新增项目任务
     */
    @PostMapping("/task/add")
    @Operation(summary = "新增项目任务")
    @EventTrack(title = ModelType.SYSTEM, businessType = BusinessType.INSERT, operatorType = OperatorType.MANAGE, description = "新增项目任务")
    public ServerResponseEntity<Void> addTask(@Validated @RequestBody ProjectTask task) {
        projectService.addProjectTask(task);
        return ServerResponseEntity.success();
    }

    /**
     * 修改项目任务
     */
    @PostMapping("/task/edit")
    @Operation(summary = "修改项目任务")
    @EventTrack(title = ModelType.SYSTEM, businessType = BusinessType.UPDATE, operatorType = OperatorType.MANAGE, description = "修改项目任务")
    public ServerResponseEntity<Void> editTask(@Validated @RequestBody ProjectTask task) {
        projectService.updateProjectTask(task);
        return ServerResponseEntity.success();
    }

    /**
     * 删除项目任务
     */
    @PostMapping("/task/remove/{taskIds}")
    @Operation(summary = "删除项目任务")
    @EventTrack(title = ModelType.SYSTEM, businessType = BusinessType.DELETE, operatorType = OperatorType.MANAGE, description = "删除项目任务")
    public ServerResponseEntity<Void> removeTask(@PathVariable Long[] taskIds) {
        projectService.deleteProjectTask(taskIds);
        return ServerResponseEntity.success();
    }

    /**
     * 任务分配
     */
    @PostMapping("/task/assign")
    @Operation(summary = "任务分配")
    @EventTrack(title = ModelType.SYSTEM, businessType = BusinessType.UPDATE, operatorType = OperatorType.MANAGE, description = "任务分配")
    public ServerResponseEntity<Void> assignTask(@RequestParam Long taskId,
                                                @RequestParam Long assigneeId,
                                                @RequestParam(required = false) String remark) {
        projectService.assignTask(taskId, assigneeId, remark);
        return ServerResponseEntity.success();
    }

    /**
     * 任务进度更新
     */
    @PostMapping("/task/progress")
    @Operation(summary = "任务进度更新")
    @EventTrack(title = ModelType.SYSTEM, businessType = BusinessType.UPDATE, operatorType = OperatorType.MANAGE, description = "任务进度更新")
    public ServerResponseEntity<Void> updateTaskProgress(@RequestParam Long taskId,
                                                        @RequestParam Double progress,
                                                        @RequestParam(required = false) String remark) {
        projectService.updateTaskProgress(taskId, progress, remark);
        return ServerResponseEntity.success();
    }

    /**
     * 任务完成
     */
    @PostMapping("/task/complete")
    @Operation(summary = "任务完成")
    @EventTrack(title = ModelType.SYSTEM, businessType = BusinessType.UPDATE, operatorType = OperatorType.MANAGE, description = "任务完成")
    public ServerResponseEntity<Void> completeTask(@RequestParam Long taskId,
                                                  @RequestParam(required = false) String summary) {
        projectService.completeTask(taskId, summary);
        return ServerResponseEntity.success();
    }

    /**
     * 根据项目获取任务列表
     */
    @GetMapping("/task/project/{projectId}")
    @Operation(summary = "项目任务列表")
    @EventTrack(title = ModelType.SYSTEM, businessType = BusinessType.SEARCH, operatorType = OperatorType.MANAGE, description = "项目任务列表")
    public ServerResponseEntity<List<ProjectTask>> getTasksByProject(@PathVariable Long projectId) {
        List<ProjectTask> tasks = projectService.getTasksByProjectId(projectId);
        return ServerResponseEntity.success(tasks);
    }

    // ==================== 项目里程碑管理 ====================
    
    /**
     * 查询项目里程碑列表
     */
    @PostMapping("/milestone/page")
    @Operation(summary = "项目里程碑列表")
    @EventTrack(title = ModelType.SYSTEM, businessType = BusinessType.SEARCH, operatorType = OperatorType.MANAGE, description = "项目里程碑列表")
    public ServerResponseEntity<IPage<ProjectMilestone>> milestonePage(ProjectMilestone milestone, PageParam<ProjectMilestone> page) {
        IPage<ProjectMilestone> milestonePage = projectService.getProjectMilestoneList(page, milestone);
        return ServerResponseEntity.success(milestonePage);
    }

    /**
     * 新增项目里程碑
     */
    @PostMapping("/milestone/add")
    @Operation(summary = "新增项目里程碑")
    @EventTrack(title = ModelType.SYSTEM, businessType = BusinessType.INSERT, operatorType = OperatorType.MANAGE, description = "新增项目里程碑")
    public ServerResponseEntity<Void> addMilestone(@Validated @RequestBody ProjectMilestone milestone) {
        projectService.addProjectMilestone(milestone);
        return ServerResponseEntity.success();
    }

    /**
     * 修改项目里程碑
     */
    @PostMapping("/milestone/edit")
    @Operation(summary = "修改项目里程碑")
    @EventTrack(title = ModelType.SYSTEM, businessType = BusinessType.UPDATE, operatorType = OperatorType.MANAGE, description = "修改项目里程碑")
    public ServerResponseEntity<Void> editMilestone(@Validated @RequestBody ProjectMilestone milestone) {
        projectService.updateProjectMilestone(milestone);
        return ServerResponseEntity.success();
    }

    /**
     * 删除项目里程碑
     */
    @PostMapping("/milestone/remove/{milestoneIds}")
    @Operation(summary = "删除项目里程碑")
    @EventTrack(title = ModelType.SYSTEM, businessType = BusinessType.DELETE, operatorType = OperatorType.MANAGE, description = "删除项目里程碑")
    public ServerResponseEntity<Void> removeMilestone(@PathVariable Long[] milestoneIds) {
        projectService.deleteProjectMilestone(milestoneIds);
        return ServerResponseEntity.success();
    }

    /**
     * 里程碑达成
     */
    @PostMapping("/milestone/achieve")
    @Operation(summary = "里程碑达成")
    @EventTrack(title = ModelType.SYSTEM, businessType = BusinessType.UPDATE, operatorType = OperatorType.MANAGE, description = "里程碑达成")
    public ServerResponseEntity<Void> achieveMilestone(@RequestParam Long milestoneId,
                                                      @RequestParam(required = false) String remark) {
        projectService.achieveMilestone(milestoneId, remark);
        return ServerResponseEntity.success();
    }

    /**
     * 根据项目获取里程碑列表
     */
    @GetMapping("/milestone/project/{projectId}")
    @Operation(summary = "项目里程碑列表")
    @EventTrack(title = ModelType.SYSTEM, businessType = BusinessType.SEARCH, operatorType = OperatorType.MANAGE, description = "项目里程碑列表")
    public ServerResponseEntity<List<ProjectMilestone>> getMilestonesByProject(@PathVariable Long projectId) {
        List<ProjectMilestone> milestones = projectService.getMilestonesByProjectId(projectId);
        return ServerResponseEntity.success(milestones);
    }

    // ==================== 项目资源管理 ====================
    
    /**
     * 查询项目资源列表
     */
    @PostMapping("/resource/page")
    @Operation(summary = "项目资源列表")
    @EventTrack(title = ModelType.SYSTEM, businessType = BusinessType.SEARCH, operatorType = OperatorType.MANAGE, description = "项目资源列表")
    public ServerResponseEntity<IPage<ProjectResource>> resourcePage(ProjectResource resource, PageParam<ProjectResource> page) {
        IPage<ProjectResource> resourcePage = projectService.getProjectResourceList(page, resource);
        return ServerResponseEntity.success(resourcePage);
    }

    /**
     * 新增项目资源
     */
    @PostMapping("/resource/add")
    @Operation(summary = "新增项目资源")
    @EventTrack(title = ModelType.SYSTEM, businessType = BusinessType.INSERT, operatorType = OperatorType.MANAGE, description = "新增项目资源")
    public ServerResponseEntity<Void> addResource(@Validated @RequestBody ProjectResource resource) {
        projectService.addProjectResource(resource);
        return ServerResponseEntity.success();
    }

    /**
     * 修改项目资源
     */
    @PostMapping("/resource/edit")
    @Operation(summary = "修改项目资源")
    @EventTrack(title = ModelType.SYSTEM, businessType = BusinessType.UPDATE, operatorType = OperatorType.MANAGE, description = "修改项目资源")
    public ServerResponseEntity<Void> editResource(@Validated @RequestBody ProjectResource resource) {
        projectService.updateProjectResource(resource);
        return ServerResponseEntity.success();
    }

    /**
     * 删除项目资源
     */
    @PostMapping("/resource/remove/{resourceIds}")
    @Operation(summary = "删除项目资源")
    @EventTrack(title = ModelType.SYSTEM, businessType = BusinessType.DELETE, operatorType = OperatorType.MANAGE, description = "删除项目资源")
    public ServerResponseEntity<Void> removeResource(@PathVariable Long[] resourceIds) {
        projectService.deleteProjectResource(resourceIds);
        return ServerResponseEntity.success();
    }

    /**
     * 资源分配
     */
    @PostMapping("/resource/allocate")
    @Operation(summary = "资源分配")
    @EventTrack(title = ModelType.SYSTEM, businessType = BusinessType.UPDATE, operatorType = OperatorType.MANAGE, description = "资源分配")
    public ServerResponseEntity<Void> allocateResource(@RequestParam Long resourceId,
                                                      @RequestParam Long projectId,
                                                      @RequestParam Double quantity,
                                                      @RequestParam(required = false) String remark) {
        projectService.allocateResource(resourceId, projectId, quantity, remark);
        return ServerResponseEntity.success();
    }

    // ==================== 项目风险管理 ====================
    
    /**
     * 查询项目风险列表
     */
    @PostMapping("/risk/page")
    @Operation(summary = "项目风险列表")
    @EventTrack(title = ModelType.SYSTEM, businessType = BusinessType.SEARCH, operatorType = OperatorType.MANAGE, description = "项目风险列表")
    public ServerResponseEntity<IPage<ProjectRisk>> riskPage(ProjectRisk risk, PageParam<ProjectRisk> page) {
        IPage<ProjectRisk> riskPage = projectService.getProjectRiskList(page, risk);
        return ServerResponseEntity.success(riskPage);
    }

    /**
     * 新增项目风险
     */
    @PostMapping("/risk/add")
    @Operation(summary = "新增项目风险")
    @EventTrack(title = ModelType.SYSTEM, businessType = BusinessType.INSERT, operatorType = OperatorType.MANAGE, description = "新增项目风险")
    public ServerResponseEntity<Void> addRisk(@Validated @RequestBody ProjectRisk risk) {
        projectService.addProjectRisk(risk);
        return ServerResponseEntity.success();
    }

    /**
     * 修改项目风险
     */
    @PostMapping("/risk/edit")
    @Operation(summary = "修改项目风险")
    @EventTrack(title = ModelType.SYSTEM, businessType = BusinessType.UPDATE, operatorType = OperatorType.MANAGE, description = "修改项目风险")
    public ServerResponseEntity<Void> editRisk(@Validated @RequestBody ProjectRisk risk) {
        projectService.updateProjectRisk(risk);
        return ServerResponseEntity.success();
    }

    /**
     * 删除项目风险
     */
    @PostMapping("/risk/remove/{riskIds}")
    @Operation(summary = "删除项目风险")
    @EventTrack(title = ModelType.SYSTEM, businessType = BusinessType.DELETE, operatorType = OperatorType.MANAGE, description = "删除项目风险")
    public ServerResponseEntity<Void> removeRisk(@PathVariable Long[] riskIds) {
        projectService.deleteProjectRisk(riskIds);
        return ServerResponseEntity.success();
    }

    /**
     * 风险处理
     */
    @PostMapping("/risk/handle")
    @Operation(summary = "风险处理")
    @EventTrack(title = ModelType.SYSTEM, businessType = BusinessType.UPDATE, operatorType = OperatorType.MANAGE, description = "风险处理")
    public ServerResponseEntity<Void> handleRisk(@RequestParam Long riskId,
                                                @RequestParam String handleMethod,
                                                @RequestParam(required = false) String handleResult) {
        projectService.respondToRisk(riskId, handleMethod, handleResult);
        return ServerResponseEntity.success();
    }

    // ==================== 项目统计分析 ====================
    
    /**
     * 项目统计
     */
    @GetMapping("/statistics")
    @Operation(summary = "项目统计")
    @EventTrack(title = ModelType.SYSTEM, businessType = BusinessType.SEARCH, operatorType = OperatorType.MANAGE, description = "项目统计")
    public ServerResponseEntity<ProjectStatisticsVO> statistics(
            @RequestParam(required = false) String startDate,
            @RequestParam(required = false) String endDate,
            @RequestParam(required = false) String status) {
        ProjectStatisticsVO statistics = projectService.getProjectOverviewStatistics(startDate, endDate, status);
        return ServerResponseEntity.success(statistics);
    }

    /**
     * 项目进度分析
     */
    @GetMapping("/progress")
    @Operation(summary = "项目进度分析")
    @EventTrack(title = ModelType.SYSTEM, businessType = BusinessType.SEARCH, operatorType = OperatorType.MANAGE, description = "项目进度分析")
    public ServerResponseEntity<ProjectProgressVO> progress(
            @RequestParam(required = false) Long projectId,
            @RequestParam(required = false) String startDate,
            @RequestParam(required = false) String endDate) {
        ProjectProgressVO progress = projectService.getProjectProgressAnalysis(projectId, startDate, endDate);
        return ServerResponseEntity.success(progress);
    }

    /**
     * 项目成本分析
     */
    @GetMapping("/cost-analysis")
    @Operation(summary = "项目成本分析")
    @EventTrack(title = ModelType.SYSTEM, businessType = BusinessType.SEARCH, operatorType = OperatorType.MANAGE, description = "项目成本分析")
    public ServerResponseEntity<Map<String, Object>> costAnalysis(
            @RequestParam(required = false) Long projectId,
            @RequestParam(required = false) String startDate,
            @RequestParam(required = false) String endDate) {
        Map<String, Object> result = projectService.getProjectCostAnalysis(projectId, startDate, endDate);
        return ServerResponseEntity.success(result);
    }

    /**
     * 导出项目报表
     */
    @GetMapping("/export")
    @Operation(summary = "导出项目报表")
    @EventTrack(title = ModelType.SYSTEM, businessType = BusinessType.EXPORT, operatorType = OperatorType.MANAGE, description = "导出项目报表")
    public ServerResponseEntity<Map<String, Object>> exportProject(
            @RequestParam(required = false) String startDate,
            @RequestParam(required = false) String endDate,
            @RequestParam(required = false) Long projectId,
            @RequestParam String reportType) {
        Map<String, Object> result = projectService.exportProjectReport(startDate, endDate, projectId, reportType);
        return ServerResponseEntity.success(result);
    }
}