package com.chennian.storytelling.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.chennian.storytelling.bean.model.project.*;
import com.chennian.storytelling.bean.vo.ProjectProgressVO;
import com.chennian.storytelling.bean.vo.ProjectStatisticsVO;
import com.chennian.storytelling.common.utils.PageParam;
import com.storytelling.common.core.domain.AjaxResult;

import java.util.List;

/**
 * 项目管理服务接口
 *
 * @author storytelling
 * @date 2024-01-01
 */
public interface ProjectService {

    // ==================== 项目管理 ====================

    /**
     * 查询项目列表
     */
    IPage<Project> getProjectList(PageParam<Project> page, Project project);

    /**
     * 根据ID查询项目信息
     */
    Project getProjectById(Long id);

    /**
     * 新增项目
     */
    void addProject(Project project);

    /**
     * 修改项目信息
     */
    void updateProject(Project project);

    /**
     * 删除项目
     */
    AjaxResult deleteProject(Long[] ids);

    /**
     * 项目启动
     */
    AjaxResult startProject(Long projectId);

    /**
     * 项目暂停
     */
    AjaxResult pauseProject(Long projectId, String pauseReason);

    /**
     * 项目完成
     */
    AjaxResult completeProject(Long projectId, String completionSummary);

    /**
     * 项目取消
     */
    AjaxResult cancelProject(Long projectId, String cancelReason);

    /**
     * 项目进度更新
     */
    AjaxResult updateProjectProgress(Long projectId, Double progress);

    // ==================== 项目任务管理 ====================

    /**
     * 查询项目任务列表
     */
    IPage<ProjectTask> getProjectTaskList(PageParam<ProjectTask> page, ProjectTask task);

    /**
     * 根据ID查询项目任务信息
     */
    ProjectTask getProjectTaskById(Long id);

    /**
     * 新增项目任务
     */
    void addProjectTask(ProjectTask task);

    /**
     * 修改项目任务信息
     */
    void updateProjectTask(ProjectTask task);

    /**
     * 删除项目任务
     */
    void deleteProjectTask(Long[] ids);

    /**
     * 任务分配
     */
    void assignTask(Long taskId, Long assigneeId, String remark);

    /**
     * 任务开始
     */
    AjaxResult startTask(Long taskId);

    /**
     * 任务暂停
     */
    AjaxResult pauseTask(Long taskId, String pauseReason);

    /**
     * 任务完成
     */
    void completeTask(Long taskId, String completionNotes);

    /**
     * 任务进度更新
     */
    void updateTaskProgress(Long taskId, Double progress, String remark);

    /**
     * 根据项目ID查询任务列表
     */
    List<ProjectTask> getTasksByProjectId(Long projectId);

    /**
     * 获取任务甘特图数据
     */
    AjaxResult getTaskGanttData(Long projectId);

    // ==================== 项目里程碑管理 ====================

    /**
     * 查询项目里程碑列表
     */
    IPage<ProjectMilestone> getProjectMilestoneList(PageParam<ProjectMilestone> page, ProjectMilestone milestone);

    /**
     * 根据ID查询项目里程碑信息
     */
    ProjectMilestone getProjectMilestoneById(Long id);

    /**
     * 新增项目里程碑
     */
    void addProjectMilestone(ProjectMilestone milestone);

    /**
     * 修改项目里程碑信息
     */
    void updateProjectMilestone(ProjectMilestone milestone);

    /**
     * 删除项目里程碑
     */
    void deleteProjectMilestone(Long[] ids);

    /**
     * 里程碑达成
     */
    void achieveMilestone(Long milestoneId, String achievementNotes);

    /**
     * 根据项目ID查询里程碑列表
     */
    List<ProjectMilestone> getMilestonesByProjectId(Long projectId);

    // ==================== 项目资源管理 ====================

    /**
     * 查询项目资源列表
     */
    IPage<ProjectResource> getProjectResourceList(PageParam<ProjectResource> page, ProjectResource resource);

    /**
     * 根据ID查询项目资源信息
     */
    ProjectResource getProjectResourceById(Long id);

    /**
     * 新增项目资源
     */
    AjaxResult addProjectResource(ProjectResource resource);

    /**
     * 修改项目资源信息
     */
    AjaxResult updateProjectResource(ProjectResource resource);

    /**
     * 删除项目资源
     */
    AjaxResult deleteProjectResource(Long[] ids);

    /**
     * 资源分配
     */
    AjaxResult allocateResource(Long resourceId, Long projectId, Double quantity, String remark);

    /**
     * 资源释放
     */
    AjaxResult releaseResource(Long resourceId, String releaseReason);

    /**
     * 根据项目ID查询资源列表
     */
    List<ProjectResource> getResourcesByProjectId(Long projectId);

    /**
     * 资源使用率分析
     */
    AjaxResult getResourceUtilizationAnalysis(String startDate, String endDate, Integer resourceType);

    // ==================== 项目风险管理 ====================

    /**
     * 查询项目风险列表
     */
    IPage<ProjectRisk> getProjectRiskList(PageParam<ProjectRisk> page, ProjectRisk risk);

    /**
     * 根据ID查询项目风险信息
     */
    ProjectRisk getProjectRiskById(Long id);

    /**
     * 新增项目风险
     */
    void addProjectRisk(ProjectRisk risk);

    /**
     * 修改项目风险信息
     */
    void updateProjectRisk(ProjectRisk risk);

    /**
     * 删除项目风险
     */
    void deleteProjectRisk(Long[] ids);

    /**
     * 风险评估
     */
    AjaxResult assessRisk(Long riskId, Double probability, Integer impact);

    /**
     * 风险应对
     */
    AjaxResult respondToRisk(Long riskId, String mitigationStrategy, String contingencyPlan);

    /**
     * 风险关闭
     */
    void closeRisk(Long riskId, String closeReason);

    /**
     * 根据项目ID查询风险列表
     */
    List<ProjectRisk> getRisksByProjectId(Long projectId);

    /**
     * 风险矩阵分析
     */
    AjaxResult getRiskMatrixAnalysis(Long projectId);

    // ==================== 项目统计分析 ====================

    /**
     * 项目概览统计
     */
    ProjectStatisticsVO getProjectOverviewStatistics(String startDate, String endDate, String status);

    /**
     * 项目进度分析
     */
    ProjectProgressVO getProjectProgressAnalysis(Long projectId, String startDate, String endDate);

    /**
     * 项目成本分析
     */
    AjaxResult getProjectCostAnalysis(Long projectId, String startDate, String endDate);

    /**
     * 项目绩效分析
     */
    AjaxResult getProjectPerformanceAnalysis(String startDate, String endDate);

    /**
     * 项目资源分析
     */
    AjaxResult getProjectResourceAnalysis(Long projectId);

    /**
     * 项目风险分析
     */
    AjaxResult getProjectRiskAnalysis(Long projectId);

    /**
     * 项目团队绩效分析
     */
    AjaxResult getProjectTeamPerformanceAnalysis(Long projectId);

    /**
     * 项目交付物分析
     */
    AjaxResult getProjectDeliverableAnalysis(Long projectId);

    /**
     * 项目归档
     */
    void archiveProject(Long projectId);

    /**
     * 导出项目报表
     */
    AjaxResult exportProjectReport(String startDate, String endDate, Long projectId, String reportType);
}