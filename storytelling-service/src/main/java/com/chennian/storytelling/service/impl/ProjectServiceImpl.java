package com.chennian.storytelling.service.impl;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.chennian.storytelling.bean.model.project.*;
import com.chennian.storytelling.bean.vo.ProjectProgressVO;
import com.chennian.storytelling.bean.vo.ProjectStatisticsVO;
import com.chennian.storytelling.common.exception.StorytellingBindException;
import com.chennian.storytelling.common.utils.PageParam;
import com.chennian.storytelling.dao.project.*;
import com.chennian.storytelling.service.ProjectService;
import com.storytelling.common.core.domain.AjaxResult;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 项目管理服务实现类
 *
 * @author storytelling
 * @date 2024-01-01
 */
@Slf4j
@Service
public class ProjectServiceImpl implements ProjectService {

    @Autowired
    private ProjectMapper projectMapper;

    @Autowired
    private ProjectTaskMapper projectTaskMapper;

    @Autowired
    private ProjectMilestoneMapper projectMilestoneMapper;

    @Autowired
    private ProjectResourceMapper projectResourceMapper;

    @Autowired
    private ProjectRiskMapper projectRiskMapper;

    // ==================== 项目管理 ====================

    @Override
    public IPage<Project> getProjectList(PageParam<Project> page, Project project) {
        Page<Project> pageInfo = new Page<>(page.getCurrent(), page.getSize());
        return projectMapper.selectProjectList(pageInfo, project);
    }

    @Override
    public Project getProjectById(Long id) {
        return projectMapper.selectById(id);
    }

    @Override
    @Transactional
    public void addProject(Project project) {
        project.setCreateTime(LocalDateTime.now());
        project.setStatus(0); // 未开始
        project.setProgress(0.0); // 初始进度为0
        projectMapper.insert(project);
    }

    @Override
    @Transactional
    public void updateProject(Project project) {
        project.setUpdateTime(LocalDateTime.now());
        projectMapper.updateById(project);
    }

    @Override
    @Transactional
    public AjaxResult deleteProject(Long[] ids) {
        try {
            // 检查项目是否有关联的任务
            for (Long id : ids) {
                List<ProjectTask> tasks = projectTaskMapper.selectTasksByProjectId(id);
                if (!tasks.isEmpty()) {
                    return AjaxResult.error("项目ID: " + id + " 存在关联任务，无法删除");
                }
            }
            projectMapper.deleteByIds(Arrays.asList(ids));
            return AjaxResult.success("删除成功");
        } catch (Exception e) {
            return AjaxResult.error("删除失败: " + e.getMessage());
        }
    }

    @Override
    @Transactional
    public AjaxResult startProject(Long projectId) {
        try {
            Project project = projectMapper.selectById(projectId);
            if (project == null) {
                return AjaxResult.error("项目不存在");
            }
            if (project.getStatus() != 0) {
                return AjaxResult.error("只有未开始的项目才能启动");
            }
            
            project.setStatus(1); // 进行中
            project.setActualStartTime(LocalDateTime.now());
            project.setUpdateTime(LocalDateTime.now());
            projectMapper.updateById(project);
            
            return AjaxResult.success("项目启动成功");
        } catch (Exception e) {
            return AjaxResult.error("项目启动失败: " + e.getMessage());
        }
    }

    @Override
    @Transactional
    public AjaxResult pauseProject(Long projectId, String pauseReason) {
        try {
            Project project = projectMapper.selectById(projectId);
            if (project == null) {
                return AjaxResult.error("项目不存在");
            }
            if (project.getStatus() != 1) {
                return AjaxResult.error("只有进行中的项目才能暂停");
            }
            
            project.setStatus(2); // 已暂停
            project.setRemarks(project.getRemarks() + "\n暂停原因: " + pauseReason);
            project.setUpdateTime(LocalDateTime.now());
            projectMapper.updateById(project);
            
            return AjaxResult.success("项目暂停成功");
        } catch (Exception e) {
            return AjaxResult.error("项目暂停失败: " + e.getMessage());
        }
    }

    @Override
    @Transactional
    public AjaxResult completeProject(Long projectId, String completionSummary) {
        try {
            Project project = projectMapper.selectById(projectId);
            if (project == null) {
                return AjaxResult.error("项目不存在");
            }
            if (project.getStatus() == 3) {
                return AjaxResult.error("项目已完成");
            }
            
            project.setStatus(3); // 已完成
            project.setProgress(100.0);
            project.setActualEndTime(LocalDateTime.now());
            project.setRemarks(project.getRemarks() + "\n完成总结: " + completionSummary);
            project.setUpdateTime(LocalDateTime.now());
            projectMapper.updateById(project);
            
            return AjaxResult.success("项目完成成功");
        } catch (Exception e) {
            return AjaxResult.error("项目完成失败: " + e.getMessage());
        }
    }

    @Override
    @Transactional
    public AjaxResult cancelProject(Long projectId, String cancelReason) {
        try {
            Project project = projectMapper.selectById(projectId);
            if (project == null) {
                return AjaxResult.error("项目不存在");
            }
            if (project.getStatus() == 3 || project.getStatus() == 4) {
                return AjaxResult.error("已完成或已取消的项目无法取消");
            }
            
            project.setStatus(4); // 已取消
            project.setRemarks(project.getRemarks() + "\n取消原因: " + cancelReason);
            project.setUpdateTime(LocalDateTime.now());
            projectMapper.updateById(project);
            
            return AjaxResult.success("项目取消成功");
        } catch (Exception e) {
            return AjaxResult.error("项目取消失败: " + e.getMessage());
        }
    }

    @Override
    @Transactional
    public AjaxResult updateProjectProgress(Long projectId, Double progress) {
        try {
            if (progress < 0 || progress > 100) {
                return AjaxResult.error("进度值必须在0-100之间");
            }
            
            Project project = projectMapper.selectById(projectId);
            if (project == null) {
                return AjaxResult.error("项目不存在");
            }
            
            project.setProgress(progress);
            project.setUpdateTime(LocalDateTime.now());
            projectMapper.updateById(project);
            
            return AjaxResult.success("项目进度更新成功");
        } catch (Exception e) {
            return AjaxResult.error("项目进度更新失败: " + e.getMessage());
        }
    }

    // ==================== 项目任务管理 ====================

    @Override
    public IPage<ProjectTask> getProjectTaskList(PageParam<ProjectTask> page, ProjectTask task) {
        Page<ProjectTask> pageInfo = new Page<>(page.getCurrent(), page.getSize());
        return projectTaskMapper.selectTaskList(pageInfo, task);
    }

    @Override
    public ProjectTask getProjectTaskById(Long id) {
        return projectTaskMapper.selectById(id);
    }

    @Override
    @Transactional
    public void addProjectTask(ProjectTask task) {
        task.setCreateTime(LocalDateTime.now());
        task.setStatus(0); // 未开始
        task.setProgress(0.0); // 初始进度为0
        projectTaskMapper.insert(task);
    }

    @Override
    @Transactional
    public void updateProjectTask(ProjectTask task) {
        task.setUpdateTime(LocalDateTime.now());
        projectTaskMapper.updateById(task);
    }

    @Override
    @Transactional
    public void deleteProjectTask(Long[] ids) {
        projectTaskMapper.deleteByIds(Arrays.asList(ids));
    }

    @Override
    @Transactional
    public void assignTask(Long taskId, Long assigneeId, String remark) {
        ProjectTask task = projectTaskMapper.selectById(taskId);
        if (task != null) {
            task.setAssigneeId(assigneeId);
            task.setRemarks(task.getRemarks() + "\n分配备注: " + remark);
            task.setUpdateTime(LocalDateTime.now());
            projectTaskMapper.updateById(task);
        }
    }

    @Override
    @Transactional
    public AjaxResult startTask(Long taskId) {
        try {
            ProjectTask task = projectTaskMapper.selectById(taskId);
            if (task == null) {
                return AjaxResult.error("任务不存在");
            }
            if (task.getStatus() != 0) {
                return AjaxResult.error("只有未开始的任务才能启动");
            }
            
            task.setStatus(1); // 进行中
            task.setActualStartTime(LocalDateTime.now());
            task.setUpdateTime(LocalDateTime.now());
            projectTaskMapper.updateById(task);
            
            return AjaxResult.success("任务启动成功");
        } catch (Exception e) {
            return AjaxResult.error("任务启动失败: " + e.getMessage());
        }
    }

    @Override
    @Transactional
    public AjaxResult pauseTask(Long taskId, String pauseReason) {
        try {
            ProjectTask task = projectTaskMapper.selectById(taskId);
            if (task == null) {
                return AjaxResult.error("任务不存在");
            }
            if (task.getStatus() != 1) {
                return AjaxResult.error("只有进行中的任务才能暂停");
            }
            
            task.setStatus(2); // 已暂停
            task.setRemarks(task.getRemarks() + "\n暂停原因: " + pauseReason);
            task.setUpdateTime(LocalDateTime.now());
            projectTaskMapper.updateById(task);
            
            return AjaxResult.success("任务暂停成功");
        } catch (Exception e) {
            return AjaxResult.error("任务暂停失败: " + e.getMessage());
        }
    }

    @Override
    @Transactional
    public void completeTask(Long taskId, String completionNotes) {
        ProjectTask task = projectTaskMapper.selectById(taskId);
        if (task != null) {
            task.setStatus(3); // 已完成
            task.setProgress(100.0);
            task.setActualEndTime(LocalDateTime.now());
            task.setRemarks(task.getRemarks() + "\n完成备注: " + completionNotes);
            task.setUpdateTime(LocalDateTime.now());
            projectTaskMapper.updateById(task);
        }
    }

    @Override
    @Transactional
    public void updateTaskProgress(Long taskId, Double progress, String remark) {
        ProjectTask task = projectTaskMapper.selectById(taskId);
        if (task != null) {
            task.setProgress(progress);
            task.setRemarks(task.getRemarks() + "\n进度更新: " + remark);
            task.setUpdateTime(LocalDateTime.now());
            projectTaskMapper.updateById(task);
        }
    }

    @Override
    public List<ProjectTask> getTasksByProjectId(Long projectId) {
        return projectTaskMapper.selectTasksByProjectId(projectId);
    }

    @Override
    public AjaxResult getTaskGanttData(Long projectId) {
        try {
            List<Map<String, Object>> ganttData = projectTaskMapper.selectTaskGanttData(projectId);
            return AjaxResult.success(ganttData);
        } catch (Exception e) {
            return AjaxResult.error("获取甘特图数据失败: " + e.getMessage());
        }
    }

    // ==================== 项目里程碑管理 ====================

    @Override
    public IPage<ProjectMilestone> getProjectMilestoneList(PageParam<ProjectMilestone> page, ProjectMilestone milestone) {
        Page<ProjectMilestone> pageInfo = new Page<>(page.getCurrent(), page.getSize());
        return projectMilestoneMapper.selectMilestoneList(pageInfo, milestone);
    }

    @Override
    public ProjectMilestone getProjectMilestoneById(Long id) {
        return projectMilestoneMapper.selectById(id);
    }

    @Override
    @Transactional
    public void addProjectMilestone(ProjectMilestone milestone) {
        milestone.setCreateTime(LocalDateTime.now());
        milestone.setStatus(0); // 未达成
        projectMilestoneMapper.insert(milestone);
    }

    @Override
    @Transactional
    public void updateProjectMilestone(ProjectMilestone milestone) {
        milestone.setUpdateTime(LocalDateTime.now());
        projectMilestoneMapper.updateById(milestone);
    }

    @Override
    @Transactional
    public void deleteProjectMilestone(Long[] ids) {
        projectMilestoneMapper.deleteByIds(Arrays.asList(ids));
    }

    @Override
    @Transactional
    public void achieveMilestone(Long milestoneId, String achievementNotes) {
        ProjectMilestone milestone = projectMilestoneMapper.selectById(milestoneId);
        if (milestone != null) {
            milestone.setStatus(1); // 已达成
            milestone.setActualCompletionTime(LocalDateTime.now());
            milestone.setRemarks(milestone.getRemarks() + "\n达成备注: " + achievementNotes);
            milestone.setUpdateTime(LocalDateTime.now());
            projectMilestoneMapper.updateById(milestone);
        }
    }

    @Override
    public List<ProjectMilestone> getMilestonesByProjectId(Long projectId) {
        return projectMilestoneMapper.selectMilestonesByProjectId(projectId);
    }

    // ==================== 项目资源管理 ====================

    @Override
    public IPage<ProjectResource> getProjectResourceList(PageParam<ProjectResource> page, ProjectResource resource) {
        Page<ProjectResource> pageInfo = new Page<>(page.getCurrent(), page.getSize());
        return projectResourceMapper.selectResourceList(pageInfo, resource);
    }

    @Override
    public ProjectResource getProjectResourceById(Long id) {
        return projectResourceMapper.selectById(id);
    }

    @Override
    @Transactional
    public AjaxResult addProjectResource(ProjectResource resource) {
        try {
            resource.setCreateTime(LocalDateTime.now());
            resource.setStatus(1); // 可用
            projectResourceMapper.insert(resource);
            return AjaxResult.success("资源添加成功");
        } catch (Exception e) {
            return AjaxResult.error("资源添加失败: " + e.getMessage());
        }
    }

    @Override
    @Transactional
    public AjaxResult updateProjectResource(ProjectResource resource) {
        try {
            resource.setUpdateTime(LocalDateTime.now());
            projectResourceMapper.updateById(resource);
            return AjaxResult.success("资源更新成功");
        } catch (Exception e) {
            return AjaxResult.error("资源更新失败: " + e.getMessage());
        }
    }

    @Override
    @Transactional
    public AjaxResult deleteProjectResource(Long[] ids) {
        try {
            projectResourceMapper.deleteByIds(Arrays.asList(ids));
            return AjaxResult.success("资源删除成功");
        } catch (Exception e) {
            return AjaxResult.error("资源删除失败: " + e.getMessage());
        }
    }

    @Override
    @Transactional
    public AjaxResult allocateResource(Long resourceId, Long projectId, Double quantity, String remark) {
        try {
            ProjectResource resource = projectResourceMapper.selectById(resourceId);
            if (resource == null) {
                return AjaxResult.error("资源不存在");
            }
            if (resource.getAvailableQuantity() < quantity) {
                return AjaxResult.error("可用数量不足");
            }
            
            // 更新资源分配信息
            resource.setAllocatedQuantity(resource.getAllocatedQuantity() + quantity);
            resource.setAvailableQuantity(resource.getAvailableQuantity() - quantity);
            resource.setUpdateTime(LocalDateTime.now());
            projectResourceMapper.updateById(resource);
            
            return AjaxResult.success("资源分配成功");
        } catch (Exception e) {
            return AjaxResult.error("资源分配失败: " + e.getMessage());
        }
    }

    @Override
    @Transactional
    public AjaxResult releaseResource(Long resourceId, String releaseReason) {
        try {
            ProjectResource resource = projectResourceMapper.selectById(resourceId);
            if (resource == null) {
                return AjaxResult.error("资源不存在");
            }
            
            // 释放资源 - 将已分配的数量重新加回可用数量
            resource.setAvailableQuantity(resource.getAvailableQuantity() + resource.getAllocatedQuantity());
            resource.setAllocatedQuantity(0.0);
            resource.setRemarks(resource.getRemarks() + "\n释放原因: " + releaseReason);
            resource.setUpdateTime(LocalDateTime.now());
            projectResourceMapper.updateById(resource);
            
            return AjaxResult.success("资源释放成功");
        } catch (Exception e) {
            return AjaxResult.error("资源释放失败: " + e.getMessage());
        }
    }

    @Override
    public List<ProjectResource> getResourcesByProjectId(Long projectId) {
        return projectResourceMapper.selectResourcesByProjectId(projectId);
    }

    @Override
    public AjaxResult getResourceUtilizationAnalysis(String startDate, String endDate, Integer resourceType) {
        try {
            Map<String, Object> analysis = projectResourceMapper.selectResourceUtilizationAnalysis(startDate, endDate, resourceType);
            return AjaxResult.success(analysis);
        } catch (Exception e) {
            return AjaxResult.error("获取资源使用率分析失败: " + e.getMessage());
        }
    }

    // ==================== 项目风险管理 ====================

    @Override
    public IPage<ProjectRisk> getProjectRiskList(PageParam<ProjectRisk> page, ProjectRisk risk) {
        Page<ProjectRisk> pageInfo = new Page<>(page.getCurrent(), page.getSize());
        return projectRiskMapper.selectRiskList(pageInfo, risk);
    }

    @Override
    public ProjectRisk getProjectRiskById(Long id) {
        return projectRiskMapper.selectById(id);
    }

    @Override
    @Transactional
    public void addProjectRisk(ProjectRisk risk) {
        risk.setCreateTime(LocalDateTime.now());
        risk.setStatus(1); // 识别
        projectRiskMapper.insert(risk);
    }

    @Override
    @Transactional
    public void updateProjectRisk(ProjectRisk risk) {
        risk.setUpdateTime(LocalDateTime.now());
        projectRiskMapper.updateById(risk);
    }

    @Override
    @Transactional
    public void deleteProjectRisk(Long[] ids) {
        projectRiskMapper.deleteByIds(Arrays.asList(ids));
    }

    @Override
    @Transactional
    public AjaxResult assessRisk(Long riskId, Double probability, Integer impact) {
        try {
            ProjectRisk risk = projectRiskMapper.selectById(riskId);
            if (risk == null) {
                return AjaxResult.error("风险不存在");
            }
            
            risk.setProbability(probability);
            risk.setImpact(impact);
            risk.setRiskLevel(Math.round(probability * impact)); // 风险等级 = 概率 * 影响
            risk.setStatus(2); // 已评估
            risk.setUpdateTime(LocalDateTime.now());
            projectRiskMapper.updateById(risk);
            
            return AjaxResult.success("风险评估成功");
        } catch (Exception e) {
            return AjaxResult.error("风险评估失败: " + e.getMessage());
        }
    }

    @Override
    @Transactional
    public AjaxResult respondToRisk(Long riskId, String mitigationStrategy, String contingencyPlan) {
        try {
            ProjectRisk risk = projectRiskMapper.selectById(riskId);
            if (risk == null) {
                return AjaxResult.error("风险不存在");
            }
            
            risk.setResponseStrategy(Integer.valueOf(mitigationStrategy));
            risk.setContingencyPlan(contingencyPlan);
            risk.setStatus(3); // 应对中
            risk.setUpdateTime(LocalDateTime.now());
            projectRiskMapper.updateById(risk);
            
            return AjaxResult.success("风险应对策略设置成功");
        } catch (Exception e) {
            return AjaxResult.error("风险应对失败: " + e.getMessage());
        }
    }

    @Override
    @Transactional
    public void closeRisk(Long riskId, String closeReason) {
        ProjectRisk risk = projectRiskMapper.selectById(riskId);
        if (risk != null) {
            risk.setStatus(4); // 已关闭
            risk.setRemarks(risk.getRemarks() + "\n关闭原因: " + closeReason);
            risk.setUpdateTime(LocalDateTime.now());
            projectRiskMapper.updateById(risk);
        }
    }

    @Override
    public List<ProjectRisk> getRisksByProjectId(Long projectId) {
        return projectRiskMapper.selectRisksByProjectId(projectId);
    }

    @Override
    public AjaxResult getRiskMatrixAnalysis(Long projectId) {
        try {
            Map<String, Object> matrix = projectRiskMapper.selectRiskMatrixAnalysis(projectId);
            return AjaxResult.success(matrix);
        } catch (Exception e) {
            return AjaxResult.error("获取风险矩阵分析失败: " + e.getMessage());
        }
    }

    // ==================== 项目统计分析 ====================

    @Override
    public ProjectStatisticsVO getProjectOverviewStatistics(String startDate, String endDate, String status) {
        try {
            ProjectStatisticsVO statistics = projectMapper.selectProjectOverviewStatistics(startDate, endDate, status);
            return statistics;
        } catch (Exception e) {
            throw new StorytellingBindException("获取项目概览统计失败: " + e.getMessage());
        }
    }

    @Override
    public ProjectProgressVO getProjectProgressAnalysis(Long projectId, String startDate, String endDate) {
        try {
            return projectMapper.selectProjectProgressAnalysis(projectId, startDate, endDate);
        } catch (Exception e) {
            throw new StorytellingBindException("获取项目进度分析失败: " + e.getMessage());
        }
    }

    @Override
    public AjaxResult getProjectCostAnalysis(Long projectId, String startDate, String endDate) {
        try {
            Map<String, Object> analysis = projectMapper.selectProjectCostAnalysis(projectId, startDate, endDate);
            return AjaxResult.success(analysis);
        } catch (Exception e) {
            return AjaxResult.error("获取项目成本分析失败: " + e.getMessage());
        }
    }

    @Override
    public AjaxResult getProjectPerformanceAnalysis(String startDate, String endDate) {
        try {
            Map<String, Object> analysis = projectMapper.selectProjectPerformanceAnalysis(startDate, endDate);
            return AjaxResult.success(analysis);
        } catch (Exception e) {
            return AjaxResult.error("获取项目绩效分析失败: " + e.getMessage());
        }
    }

    @Override
    public AjaxResult getProjectResourceAnalysis(Long projectId) {
        try {
            Map<String, Object> analysis = projectResourceMapper.selectProjectResourceAnalysis(projectId);
            return AjaxResult.success(analysis);
        } catch (Exception e) {
            return AjaxResult.error("获取项目资源分析失败: " + e.getMessage());
        }
    }

    @Override
    public AjaxResult getProjectRiskAnalysis(Long projectId) {
        try {
            Map<String, Object> analysis = projectRiskMapper.selectProjectRiskAnalysis(projectId);
            return AjaxResult.success(analysis);
        } catch (Exception e) {
            return AjaxResult.error("获取项目风险分析失败: " + e.getMessage());
        }
    }

    @Override
    public AjaxResult getProjectTeamPerformanceAnalysis(Long projectId) {
        try {
            Map<String, Object> analysis = projectTaskMapper.selectTeamPerformanceAnalysis(projectId);
            return AjaxResult.success(analysis);
        } catch (Exception e) {
            return AjaxResult.error("获取项目团队绩效分析失败: " + e.getMessage());
        }
    }

    @Override
    public AjaxResult getProjectDeliverableAnalysis(Long projectId) {
        try {
            Map<String, Object> analysis = projectMapper.selectProjectDeliverableAnalysis(projectId);
            return AjaxResult.success(analysis);
        } catch (Exception e) {
            return AjaxResult.error("获取项目交付物分析失败: " + e.getMessage());
        }
    }

    @Override
    public void archiveProject(Long projectId) {
        try {
            // 查询项目信息
            Project project = projectMapper.selectById(projectId);
            if (project == null) {
                throw new StorytellingBindException("项目不存在");
            }

            // 检查项目状态，只有已完成或已取消的项目才能归档
            if (project.getStatus() != 4 && project.getStatus() != 5) {
                throw new StorytellingBindException("只有已完成或已取消的项目才能归档");
            }

            // 更新项目状态为已归档
            project.setStatus(6);
            project.setUpdateTime(LocalDateTime.now());
            projectMapper.updateById(project);

            // 记录操作日志
            log.info("项目归档成功，项目ID: {}, 项目名称: {}", projectId, project.getProjectName());

        } catch (Exception e) {
            log.error("项目归档失败，项目ID: {}, 错误信息: {}", projectId, e.getMessage(), e);
            throw new StorytellingBindException("项目归档失败: " + e.getMessage());
        }
    }

    @Override
    public AjaxResult exportProjectReport(String startDate, String endDate, Long projectId, String reportType) {
        try {
            Map<String, Object> reportData = new HashMap<>();

            // 根据报告类型生成不同的报告数据
            switch (reportType) {
                case "overview":
                    // 项目概览报告
                    reportData = generateOverviewReport(startDate, endDate, projectId);
                    break;
                case "progress":
                    // 项目进度报告
                    reportData = generateProgressReport(startDate, endDate, projectId);
                    break;
                case "cost":
                    // 项目成本报告
                    reportData = generateCostReport(startDate, endDate, projectId);
                    break;
                case "resource":
                    // 项目资源报告
                    reportData = generateResourceReport(startDate, endDate, projectId);
                    break;
                case "risk":
                    // 项目风险报告
                    reportData = generateRiskReport(startDate, endDate, projectId);
                    break;
                case "comprehensive":
                    // 综合报告
                    reportData = generateComprehensiveReport(startDate, endDate, projectId);
                    break;
                default:
                    return AjaxResult.error("不支持的报告类型: " + reportType);
            }

            // 添加报告元数据
            reportData.put("reportType", reportType);
            reportData.put("startDate", startDate);
            reportData.put("endDate", endDate);
            reportData.put("projectId", projectId);
            reportData.put("generateTime", LocalDateTime.now());

            return AjaxResult.success("报告生成成功", reportData);
        } catch (Exception e) {
            log.error("导出项目报表失败，参数: startDate={}, endDate={}, projectId={}, reportType={}, 错误信息: {}", 
                    startDate, endDate, projectId, reportType, e.getMessage(), e);
            return AjaxResult.error("导出项目报表失败: " + e.getMessage());
        }
    }

    /**
     * 生成项目概览报告
     */
    private Map<String, Object> generateOverviewReport(String startDate, String endDate, Long projectId) {
        Map<String, Object> report = new HashMap<>();
        
        if (projectId != null) {
            // 单个项目概览
            Project project = projectMapper.selectById(projectId);
            report.put("project", project);
            report.put("tasks", projectTaskMapper.selectTasksByProjectId(projectId));
            report.put("milestones", projectMilestoneMapper.selectMilestonesByProjectId(projectId));
            report.put("resources", projectResourceMapper.selectResourcesByProjectId(projectId));
            report.put("risks", projectRiskMapper.selectRisksByProjectId(projectId));
        } else {
            // 多项目概览
            report.put("statistics", projectMapper.selectProjectOverviewStatistics(startDate, endDate, null));
            report.put("projects", projectMapper.selectProjectsByDateRange(startDate, endDate));
        }
        
        return report;
    }

    /**
     * 生成项目进度报告
     */
    private Map<String, Object> generateProgressReport(String startDate, String endDate, Long projectId) {
        Map<String, Object> report = new HashMap<>();
        
        if (projectId != null) {
            report.put("progressAnalysis", projectMapper.selectProjectProgressAnalysis(projectId, startDate, endDate));
            report.put("taskProgress", projectTaskMapper.selectTaskProgressByProject(projectId));
            report.put("milestoneProgress", projectMilestoneMapper.selectMilestoneProgressByProject(projectId));
        } else {
            report.put("allProjectsProgress", projectMapper.selectAllProjectsProgress(startDate, endDate));
        }
        
        return report;
    }

    /**
     * 生成项目成本报告
     */
    private Map<String, Object> generateCostReport(String startDate, String endDate, Long projectId) {
        Map<String, Object> report = new HashMap<>();
        
        if (projectId != null) {
            report.put("costAnalysis", projectMapper.selectProjectCostAnalysis(projectId, startDate, endDate));
            report.put("resourceCosts", projectResourceMapper.selectResourceCostsByProject(projectId));
        } else {
            report.put("allProjectsCost", projectMapper.selectAllProjectsCost(startDate, endDate));
        }
        
        return report;
    }

    /**
     * 生成项目资源报告
     */
    private Map<String, Object> generateResourceReport(String startDate, String endDate, Long projectId) {
        Map<String, Object> report = new HashMap<>();
        
        if (projectId != null) {
            report.put("resourceAnalysis", projectMapper.selectProjectResourceAnalysis(projectId));
            report.put("resourceUtilization", projectResourceMapper.selectResourceUtilizationByProject(projectId));
        } else {
            report.put("allResourcesUtilization", projectResourceMapper.selectAllResourcesUtilization(startDate, endDate));
        }
        
        return report;
    }

    /**
     * 生成项目风险报告
     */
    private Map<String, Object> generateRiskReport(String startDate, String endDate, Long projectId) {
        Map<String, Object> report = new HashMap<>();
        
        if (projectId != null) {
            report.put("riskAnalysis", projectRiskMapper.selectProjectRiskAnalysis(projectId));
            report.put("risks", projectRiskMapper.selectRisksByProjectId(projectId));
        } else {
            report.put("allProjectsRisk", projectRiskMapper.selectAllProjectsRisk(startDate, endDate));
        }
        
        return report;
    }

    /**
     * 生成综合报告
     */
    private Map<String, Object> generateComprehensiveReport(String startDate, String endDate, Long projectId) {
        Map<String, Object> report = new HashMap<>();
        
        // 综合所有类型的报告数据
        report.put("overview", generateOverviewReport(startDate, endDate, projectId));
        report.put("progress", generateProgressReport(startDate, endDate, projectId));
        report.put("cost", generateCostReport(startDate, endDate, projectId));
        report.put("resource", generateResourceReport(startDate, endDate, projectId));
        report.put("risk", generateRiskReport(startDate, endDate, projectId));
        
        return report;
    }
}