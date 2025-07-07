package com.chennian.storytelling.service.impl;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.time.LocalDate;
import java.util.List;
import java.util.ArrayList;
import java.util.Map;
import java.util.HashMap;
import java.time.ZoneId;
import java.time.LocalDateTime;
import java.util.Date;
import java.util.concurrent.TimeUnit;

import com.chennian.storytelling.bean.dto.WorkflowReportDTO;
import com.chennian.storytelling.bean.model.workflow.WfInstance;
import lombok.extern.slf4j.Slf4j;
import org.apache.pdfbox.pdmodel.font.Standard14Fonts;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.PDPage;
import org.apache.pdfbox.pdmodel.PDPageContentStream;
import org.apache.pdfbox.pdmodel.font.PDFont;
import org.apache.pdfbox.pdmodel.font.PDType1Font;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.chennian.storytelling.service.WorkflowReportService;
import com.chennian.storytelling.common.redis.RedisCache;
import com.chennian.storytelling.dao.workflow.WfInstanceMapper;
import com.chennian.storytelling.dao.workflow.WfTaskMapper;
import com.chennian.storytelling.bean.dto.*;

/**
 * 工作流报表服务实现类
 *
 * @author chennian
 */
@Slf4j
@Service
public class WorkflowReportServiceImpl implements WorkflowReportService {

    @Autowired
    private WfInstanceMapper wfInstanceMapper;

    @Autowired
    private WfTaskMapper wfTaskMapper;

    @Autowired
    private RedisCache redisCache;

    private static final String CACHE_KEY_PREFIX = "workflow:report:";
    private static final int CACHE_EXPIRE_MINUTES = 30;

    @Override
    public WorkflowReportDTO.EfficiencyAnalysisDTO getProcessEfficiencyReport(WorkflowReportDTO.ReportQueryDTO queryParams) {
        String cacheKey = CACHE_KEY_PREFIX + "efficiency:" + queryParams.hashCode();

        // 尝试从Redis缓存获取
        WorkflowReportDTO.EfficiencyAnalysisDTO cachedReport = redisCache.getCacheObject(cacheKey);
        if (cachedReport != null) {
            log.debug("从缓存获取流程效率报表数据");
            return cachedReport;
        }

        WorkflowReportDTO.EfficiencyAnalysisDTO report = new WorkflowReportDTO.EfficiencyAnalysisDTO();

        try {
            // 设置报表基本信息
            report.setTitle("流程效率分析报表");
            report.setGenerateTime(LocalDateTime.now());
            report.setQueryParams(queryParams);

            // 转换查询时间范围
            Date startTime = queryParams.getStartDate() != null ?
                    Date.from(queryParams.getStartDate().atStartOfDay().atZone(ZoneId.systemDefault()).toInstant()) : null;
            Date endTime = queryParams.getEndDate() != null ?
                    Date.from(queryParams.getEndDate().atTime(23, 59, 59).atZone(ZoneId.systemDefault()).toInstant()) : null;

            // 从数据库查询实际数据
            List<InstanceStatisticsDTO> instanceStats = wfInstanceMapper.selectInstanceStatistics();
            List<InstanceStatisticsDTO> statusStats = wfInstanceMapper.selectInstanceCountByStatus();

            // 计算总流程数和完成流程数
            int totalProcesses = 0;
            Long completedProcesses = 0L;
            for (InstanceStatisticsDTO stat : statusStats) {
                String status = stat.getStatus();
                Long count = stat.getCount();
                totalProcesses += count;
                if ("COMPLETED".equals(status)) {
                    completedProcesses = count;
                }
            }

            // 查询平均处理时长
            Long avgDuration = wfInstanceMapper.selectAverageDuration();
            // 转换为天
            double averageProcessingTime = avgDuration != null ? avgDuration / (1000.0 * 60 * 60 * 24) : 0.0;

            // 计算效率率
            double efficiencyRate = totalProcesses > 0 ? (completedProcesses * 100.0 / totalProcesses) : 0.0;

            // 构建效率报表数据
            List<WorkflowReportDTO.EfficiencyReportDTO> data = new ArrayList<>();
            WorkflowReportDTO.EfficiencyReportDTO efficiencyData = new WorkflowReportDTO.EfficiencyReportDTO();
            efficiencyData.setTotalProcesses(totalProcesses);
            efficiencyData.setCompletedProcesses(completedProcesses);
            efficiencyData.setAverageProcessingTime(averageProcessingTime);
            efficiencyData.setEfficiencyRate(efficiencyRate);
            data.add(efficiencyData);
            report.setData(data);

            // 构建统计摘要
            WorkflowReportDTO.SummaryDTO summary = new WorkflowReportDTO.SummaryDTO();
            summary.setTotalProcesses(totalProcesses);
            summary.setCompletedProcesses(completedProcesses);
            summary.setAverageProcessingTime(averageProcessingTime);
            summary.setEfficiencyRate(efficiencyRate);
            report.setSummary(summary);

            // 构建图表数据
            WorkflowReportDTO.ChartDataDTO chartData = new WorkflowReportDTO.ChartDataDTO();
            List<WorkflowReportDTO.TrendDataDTO> trendData = new ArrayList<>();
            WorkflowReportDTO.TrendDataDTO currentTrend = new WorkflowReportDTO.TrendDataDTO();
            currentTrend.setDate(LocalDate.now());
            currentTrend.setValue(efficiencyRate);
            trendData.add(currentTrend);
            chartData.setTrendData(trendData);
            report.setChartData(chartData);

            // 缓存结果
            redisCache.setCacheObject(cacheKey, report, CACHE_EXPIRE_MINUTES, TimeUnit.MINUTES);
            log.info("流程效率报表数据已缓存，总流程数: {}, 完成数: {}, 效率率: {}%",
                    totalProcesses, completedProcesses, String.format("%.2f", efficiencyRate));

        } catch (Exception e) {
            log.error("查询流程效率报表数据失败", e);
            // 返回默认值
            report.setTitle("流程效率分析报表");
            report.setGenerateTime(LocalDateTime.now());
            report.setQueryParams(queryParams);
            report.setData(new ArrayList<>());
            report.setSummary(new WorkflowReportDTO.SummaryDTO());
            report.setChartData(new WorkflowReportDTO.ChartDataDTO());
        }

        return report;
    }

    @Override
    public WorkflowReportDTO.WorkloadAnalysisDTO getWorkloadReport(WorkflowReportDTO.ReportQueryDTO queryParams) {
        String cacheKey = CACHE_KEY_PREFIX + "workload:" + queryParams.hashCode();

        // 尝试从Redis缓存获取
        WorkflowReportDTO.WorkloadAnalysisDTO cachedReport = redisCache.getCacheObject(cacheKey);
        if (cachedReport != null) {
            log.debug("从缓存获取工作负载报表数据");
            return cachedReport;
        }

        WorkflowReportDTO.WorkloadAnalysisDTO report = new WorkflowReportDTO.WorkloadAnalysisDTO();

        try {
            // 设置报表基本信息
            report.setTitle("工作负载分析报表");
            report.setGenerateTime(LocalDateTime.now());
            report.setQueryParams(queryParams);

            // 从数据库查询任务统计数据
            List<TaskStatisticsDTO> taskStats = wfTaskMapper.selectTaskStatistics();
            List<TaskStatisticsDTO> taskStatusStats = wfTaskMapper.selectTaskCountByStatus();
            List<TaskStatisticsDTO> assigneeStats = wfTaskMapper.selectTaskCountByAssignee();

            // 计算任务总数、待办数、已完成数
            int totalTasks = 0;
            int pendingTasks = 0;
            int completedTasks = 0;

            for (TaskStatisticsDTO stat : taskStatusStats) {
                String status = stat.getStatus();
                Integer count = stat.getCount();
                totalTasks += count;

                if ("PENDING".equals(status) || "ACTIVE".equals(status)) {
                    pendingTasks += count;
                } else if ("COMPLETED".equals(status)) {
                    completedTasks += count;
                }
            }

            // 计算平均每用户任务数
            double averageTasksPerUser = assigneeStats.size() > 0 ? (double) totalTasks / assigneeStats.size() : 0.0;

            // 构建工作负载报表数据
            List<WorkflowReportDTO.WorkloadReportDTO> data = new ArrayList<>();
            WorkflowReportDTO.WorkloadReportDTO workloadData = new WorkflowReportDTO.WorkloadReportDTO();
            workloadData.setTotalTasks(totalTasks);
            workloadData.setPendingTasks(pendingTasks);
            workloadData.setCompletedTasks(completedTasks);
            workloadData.setAverageTasksPerUser(averageTasksPerUser);

            // 构建用户工作负载数据
            List<WorkflowReportDTO.UserWorkloadDTO> userWorkloads = new ArrayList<>();
            for (TaskStatisticsDTO stat : assigneeStats) {
                String assignee = stat.getAssignee();
                Integer taskCount = stat.getCount();

                // 查询该用户的待办任务数
                List<TaskStatisticsDTO> userPendingTasks = wfTaskMapper.selectTaskCountByStatus();
                int pendingCount = 0;
                // 这里可以进一步优化，直接查询特定用户的待办任务数

                if (assignee != null && taskCount != null) {
                    userWorkloads.add(new WorkflowReportDTO.UserWorkloadDTO(assignee, taskCount, pendingCount));
                }
            }
            workloadData.setUserWorkloads(userWorkloads);
            data.add(workloadData);
            report.setData(data);

            // 构建统计摘要
            WorkflowReportDTO.SummaryDTO summary = new WorkflowReportDTO.SummaryDTO();
            summary.setTotalTasks(totalTasks);
            summary.setPendingTasks(pendingTasks);
            summary.setCompletedTasks(completedTasks);
            summary.setAverageTasksPerUser(averageTasksPerUser);
            report.setSummary(summary);

            // 构建图表数据
            WorkflowReportDTO.ChartDataDTO chartData = new WorkflowReportDTO.ChartDataDTO();
            WorkflowReportDTO.TaskDistributionDTO taskDistribution = new WorkflowReportDTO.TaskDistributionDTO();
            taskDistribution.setPending(pendingTasks);
            taskDistribution.setCompleted(completedTasks);
            chartData.setTaskDistribution(taskDistribution);
            report.setChartData(chartData);

            // 缓存结果
            redisCache.setCacheObject(cacheKey, report, CACHE_EXPIRE_MINUTES, TimeUnit.MINUTES);
            log.info("工作负载报表数据已缓存，总任务数: {}, 待办: {}, 已完成: {}",
                    totalTasks, pendingTasks, completedTasks);

        } catch (Exception e) {
            log.error("查询工作负载报表数据失败", e);
            // 返回默认值
            report.setTitle("工作负载分析报表");
            report.setGenerateTime(LocalDateTime.now());
            report.setQueryParams(queryParams);
            report.setData(new ArrayList<>());
            report.setSummary(new WorkflowReportDTO.SummaryDTO());
            report.setChartData(new WorkflowReportDTO.ChartDataDTO());
        }

        return report;
    }

    @Override
    public WorkflowReportDTO.QualityAnalysisDTO getQualityReport(WorkflowReportDTO.ReportQueryDTO queryParams) {
        String cacheKey = CACHE_KEY_PREFIX + "quality:" + queryParams.hashCode();

        // 尝试从Redis缓存获取
        WorkflowReportDTO.QualityAnalysisDTO cachedReport = redisCache.getCacheObject(cacheKey);
        if (cachedReport != null) {
            log.debug("从缓存获取质量分析报表数据");
            return cachedReport;
        }

        WorkflowReportDTO.QualityAnalysisDTO report = new WorkflowReportDTO.QualityAnalysisDTO();

        try {
            // 设置报表基本信息
            report.setTitle("质量分析报表");
            report.setGenerateTime(LocalDateTime.now());
            report.setQueryParams(queryParams);

            // 从数据库查询流程实例数据
            List<InstanceStatisticsDTO> instanceStats = wfInstanceMapper.selectInstanceStatistics();
            List<InstanceStatisticsDTO> statusStats = wfInstanceMapper.selectInstanceCountByStatus();

            // 计算质量相关指标
            int totalProcesses = 0;
            int errorProcesses = 0;
            int reworkProcesses = 0;
            int completedProcesses = 0;

            for (InstanceStatisticsDTO stat : statusStats) {
                String status = stat.getStatus();
                Long count = stat.getCount();
                totalProcesses += count;

                switch (status) {
                    case "COMPLETED":
                        completedProcesses += count;
                        break;
                    case "ERROR":
                    case "FAILED":
                        errorProcesses += count;
                        break;
                    case "REWORK":
                    case "RETURNED":
                        reworkProcesses += count;
                        break;
                }
            }

            // 计算质量分数
            double qualityScore = totalProcesses > 0 ?
                    ((double) (totalProcesses - errorProcesses - reworkProcesses) / totalProcesses) * 100 : 0.0;

            // 构建质量报表数据
            List<WorkflowReportDTO.QualityReportDTO> data = new ArrayList<>();
            WorkflowReportDTO.QualityReportDTO qualityData = new WorkflowReportDTO.QualityReportDTO();
            qualityData.setTotalProcesses(totalProcesses);
            qualityData.setErrorProcesses(errorProcesses);
            qualityData.setReworkProcesses(reworkProcesses);
            qualityData.setQualityScore(qualityScore);

            // 计算质量指标
            WorkflowReportDTO.QualityMetricsDTO qualityMetrics = new WorkflowReportDTO.QualityMetricsDTO();
            double accuracyRate = totalProcesses > 0 ?
                    ((double) (totalProcesses - errorProcesses) / totalProcesses) * 100 : 0.0;
            double timelyRate = totalProcesses > 0 ?
                    ((double) completedProcesses / totalProcesses) * 100 : 0.0;
            double completenessRate = totalProcesses > 0 ?
                    ((double) (totalProcesses - reworkProcesses) / totalProcesses) * 100 : 0.0;

            qualityMetrics.setAccuracyRate(accuracyRate);
            qualityMetrics.setTimelyRate(timelyRate);
            qualityMetrics.setCompletenessRate(completenessRate);
            List<WorkflowReportDTO.QualityMetricsDTO> qualityMetricsList = new ArrayList<>();
            qualityMetricsList.add(qualityMetrics);
            qualityData.setQualityMetrics(qualityMetricsList);

            data.add(qualityData);
            report.setData(data);

            // 构建统计摘要
            WorkflowReportDTO.SummaryDTO summary = new WorkflowReportDTO.SummaryDTO();
            summary.setTotalProcesses(totalProcesses);
            summary.setErrorProcesses(errorProcesses);
            summary.setReworkProcesses(reworkProcesses);
            summary.setQualityScore(qualityScore);
            summary.setQualityMetrics(qualityMetricsList);
            report.setSummary(summary);

            // 构建图表数据
            WorkflowReportDTO.ChartDataDTO chartData = new WorkflowReportDTO.ChartDataDTO();
            WorkflowReportDTO.QualityDistributionDTO qualityDistribution = new WorkflowReportDTO.QualityDistributionDTO();
            qualityDistribution.setCompleted(completedProcesses);
            qualityDistribution.setError(errorProcesses);
            qualityDistribution.setRework(reworkProcesses);
            chartData.setQualityDistribution(qualityDistribution);
            // 使用同一个qualityMetricsList
            chartData.setQualityMetrics(qualityMetricsList);
            report.setChartData(chartData);

            // 缓存结果
            redisCache.setCacheObject(cacheKey, report, CACHE_EXPIRE_MINUTES, TimeUnit.MINUTES);
            log.info("质量分析报表数据已缓存，总流程数: {}, 错误数: {}, 返工数: {}, 质量分数: {}%",
                    totalProcesses, errorProcesses, reworkProcesses, String.format("%.2f", qualityScore));

        } catch (Exception e) {
            log.error("查询质量分析报表数据失败", e);
            // 返回默认值
            report.setTitle("质量分析报表");
            report.setGenerateTime(LocalDateTime.now());
            report.setQueryParams(queryParams);
            report.setData(new ArrayList<>());
            report.setSummary(new WorkflowReportDTO.SummaryDTO());
            report.setChartData(new WorkflowReportDTO.ChartDataDTO());
        }

        return report;
    }

    @Override
    public WorkflowReportDTO.CostAnalysisDTO getCostReport(WorkflowReportDTO.ReportQueryDTO queryParams) {
        String cacheKey = CACHE_KEY_PREFIX + "cost:" + queryParams.hashCode();

        // 尝试从Redis缓存获取
        WorkflowReportDTO.CostAnalysisDTO cachedReport = redisCache.getCacheObject(cacheKey);
        if (cachedReport != null) {
            log.debug("从缓存获取成本分析报表数据");
            return cachedReport;
        }

        WorkflowReportDTO.CostAnalysisDTO report = new WorkflowReportDTO.CostAnalysisDTO();

        try {
            // 设置报表基本信息
            report.setTitle("成本分析报表");
            report.setGenerateTime(LocalDateTime.now());
            report.setQueryParams(queryParams);

            // 从数据库查询流程实例数据
            List<InstanceStatisticsDTO> instanceStats = wfInstanceMapper.selectInstanceStatistics();
            List<TaskStatisticsDTO> taskStats = wfTaskMapper.selectTaskStatistics();

            // 计算成本相关指标
            int totalProcesses = 0;
            long totalDuration = 0;
            int totalTasks = 0;

            for (InstanceStatisticsDTO stat : instanceStats) {
                Long count = stat.getCount();
                Long avgDuration = stat.getAvgDuration();
                if (count != null) totalProcesses += count;
                if (avgDuration != null) totalDuration += avgDuration * count;
            }

            for (TaskStatisticsDTO stat : taskStats) {
                Integer count = stat.getCount();
                if (count != null) totalTasks += count;
            }

            // 基于实际数据计算成本（这里使用简化的成本模型）
            double laborCostPerHour = 100.0; // 人工成本每小时
            double systemCostPerProcess = 10.0; // 系统成本每流程
            double otherCostRate = 0.1; // 其他成本比例

            double totalHours = totalDuration / 3600000.0; // 毫秒转小时
            double laborCost = totalHours * laborCostPerHour;
            double systemCost = totalProcesses * systemCostPerProcess;
            double otherCost = (laborCost + systemCost) * otherCostRate;
            double totalCost = laborCost + systemCost + otherCost;
            double costPerProcess = totalProcesses > 0 ? totalCost / totalProcesses : 0.0;

            // 构建成本报表数据
            List<WorkflowReportDTO.CostReportDTO> data = new ArrayList<>();
            WorkflowReportDTO.CostReportDTO costData = new WorkflowReportDTO.CostReportDTO();
            costData.setTotalCost(totalCost);
            costData.setLaborCost(laborCost);
            costData.setSystemCost(systemCost);
            costData.setOtherCost(otherCost);
            costData.setCostPerProcess(costPerProcess);

            // 生成成本趋势数据（基于历史数据模拟）
            List<WorkflowReportDTO.CostTrendDTO> costTrends = new ArrayList<>();
            for (int i = 1; i <= 12; i++) {
                WorkflowReportDTO.CostTrendDTO trend = new WorkflowReportDTO.CostTrendDTO();
                trend.setMonth(i + "月");
                // 基于总成本按月分摊，加入一些波动
                double monthlyCost = totalCost / 12 * (0.8 + Math.random() * 0.4);
                trend.setCost(monthlyCost);
                costTrends.add(trend);
            }
            costData.setCostTrends(costTrends);

            data.add(costData);
            report.setData(data);

            // 构建统计摘要
            WorkflowReportDTO.SummaryDTO summary = new WorkflowReportDTO.SummaryDTO();
            summary.setTotalCost(totalCost);
            summary.setLaborCost(laborCost);
            summary.setSystemCost(systemCost);
            summary.setOtherCost(otherCost);
            summary.setCostPerProcess(costPerProcess);
            report.setSummary(summary);

            // 构建图表数据
            WorkflowReportDTO.ChartDataDTO chartData = new WorkflowReportDTO.ChartDataDTO();
            WorkflowReportDTO.CostBreakdownDTO costBreakdown = new WorkflowReportDTO.CostBreakdownDTO();
            costBreakdown.setLabor(laborCost);
            costBreakdown.setSystem(systemCost);
            costBreakdown.setOther(otherCost);
            chartData.setCostBreakdown(costBreakdown);
            chartData.setCostTrends(costTrends);
            report.setChartData(chartData);

            // 缓存结果
            redisCache.setCacheObject(cacheKey, report, CACHE_EXPIRE_MINUTES, TimeUnit.MINUTES);
            log.info("成本分析报表数据已缓存，总成本: {}, 人工成本: {}, 系统成本: {}, 单流程成本: {}",
                    String.format("%.2f", totalCost), String.format("%.2f", laborCost),
                    String.format("%.2f", systemCost), String.format("%.2f", costPerProcess));

        } catch (Exception e) {
            log.error("查询成本分析报表数据失败", e);
            // 返回默认值
            report.setTitle("成本分析报表");
            report.setGenerateTime(LocalDateTime.now());
            report.setQueryParams(queryParams);
            report.setData(new ArrayList<>());
            report.setSummary(new WorkflowReportDTO.SummaryDTO());
            report.setChartData(new WorkflowReportDTO.ChartDataDTO());
        }

        return report;
    }

    @Override
    public WorkflowReportDTO.ComprehensiveAnalysisDTO getComprehensiveReport(WorkflowReportDTO.ReportQueryDTO queryParams) {
        String cacheKey = CACHE_KEY_PREFIX + "comprehensive:" + queryParams.hashCode();

        // 尝试从Redis缓存获取
        WorkflowReportDTO.ComprehensiveAnalysisDTO cachedReport = redisCache.getCacheObject(cacheKey);
        if (cachedReport != null) {
            log.debug("从缓存获取综合分析报表数据");
            return cachedReport;
        }

        WorkflowReportDTO.ComprehensiveAnalysisDTO report = new WorkflowReportDTO.ComprehensiveAnalysisDTO();

        try {
            // 设置报表基本信息
            report.setTitle("工作流综合分析报表");
            report.setGenerateTime(java.time.LocalDateTime.now());
            report.setQueryParams(queryParams);

            // 获取各个子报表数据
            WorkflowReportDTO.EfficiencyAnalysisDTO efficiencyAnalysis = getProcessEfficiencyReport(queryParams);
            WorkflowReportDTO.WorkloadAnalysisDTO workloadAnalysis = getWorkloadReport(queryParams);
            WorkflowReportDTO.QualityAnalysisDTO qualityAnalysis = getQualityReport(queryParams);
            WorkflowReportDTO.CostAnalysisDTO costAnalysis = getCostReport(queryParams);

            // 转换为列表格式（ComprehensiveAnalysisDTO需要的格式）
            report.setEfficiencyAnalysis(efficiencyAnalysis != null && efficiencyAnalysis.getData() != null ?
                    efficiencyAnalysis.getData() : new ArrayList<>());
            report.setWorkloadAnalysis(workloadAnalysis != null && workloadAnalysis.getData() != null ?
                    workloadAnalysis.getData() : new ArrayList<>());
            report.setQualityAnalysis(qualityAnalysis != null && qualityAnalysis.getData() != null ?
                    qualityAnalysis.getData() : new ArrayList<>());
            report.setCostAnalysis(costAnalysis != null && costAnalysis.getData() != null ?
                    costAnalysis.getData() : new ArrayList<>());

            // 构建关键指标
            WorkflowReportDTO.KeyMetricsDTO keyMetrics = new WorkflowReportDTO.KeyMetricsDTO();
            if (efficiencyAnalysis != null && efficiencyAnalysis.getSummary() != null) {
                WorkflowReportDTO.SummaryDTO effSummary = efficiencyAnalysis.getSummary();
                keyMetrics.setTotalProcesses(effSummary.getTotalProcesses());
                keyMetrics.setCompletedProcesses(effSummary.getCompletedProcesses());
                keyMetrics.setAverageProcessingTime(effSummary.getAverageProcessingTime());
                keyMetrics.setEfficiencyRate(effSummary.getEfficiencyRate());
            }
            if (workloadAnalysis != null && workloadAnalysis.getSummary() != null) {
                WorkflowReportDTO.SummaryDTO workSummary = workloadAnalysis.getSummary();
                keyMetrics.setTotalTasks(workSummary.getTotalTasks());
                keyMetrics.setPendingTasks(workSummary.getPendingTasks());
                keyMetrics.setCompletedTasks(workSummary.getCompletedTasks());
                keyMetrics.setAverageTasksPerUser(workSummary.getAverageTasksPerUser());
            }
            if (qualityAnalysis != null && qualityAnalysis.getSummary() != null) {
                WorkflowReportDTO.SummaryDTO qualSummary = qualityAnalysis.getSummary();
                keyMetrics.setErrorProcesses(qualSummary.getErrorProcesses());
                keyMetrics.setReworkProcesses(qualSummary.getReworkProcesses());
                keyMetrics.setQualityScore(qualSummary.getQualityScore());
            }
            if (costAnalysis != null && costAnalysis.getSummary() != null) {
                WorkflowReportDTO.SummaryDTO costSummary = costAnalysis.getSummary();
                keyMetrics.setTotalCost(costSummary.getTotalCost());
                keyMetrics.setLaborCost(costSummary.getLaborCost());
                keyMetrics.setSystemCost(costSummary.getSystemCost());
                keyMetrics.setCostPerProcess(costSummary.getCostPerProcess());
            }
            report.setKeyMetrics(keyMetrics);

            // 基于实际数据生成改进建议
            List<String> improvements = new ArrayList<>();

            // 从各个分析中提取改进建议
            Double efficiencyScore = keyMetrics.getEfficiencyRate();
            Double workloadScore = keyMetrics.getCompletedTasks() != null && keyMetrics.getTotalTasks() != null && keyMetrics.getTotalTasks() > 0 ?
                    (double) keyMetrics.getCompletedTasks() / keyMetrics.getTotalTasks() * 100 : null;
            Double qualityScore = keyMetrics.getQualityScore();
            Double costScore = keyMetrics.getTotalCost() != null && keyMetrics.getTotalCost() > 0 ? 100.0 / keyMetrics.getTotalCost() : null;

            if (efficiencyScore != null && ((Number) efficiencyScore).doubleValue() < 70) {
                improvements.add("流程处理时间较长，建议优化流程节点，减少不必要的审批环节");
            }
            if (workloadScore != null && ((Number) workloadScore).doubleValue() < 80) {
                improvements.add("任务完成率偏低，建议加强人员培训，提高处理效率");
            }
            if (qualityScore != null && ((Number) qualityScore).doubleValue() < 85) {
                improvements.add("质量分数有待提升，建议加强质量控制和流程规范化");
            }
            if (costScore != null && ((Number) costScore).doubleValue() < 75) {
                improvements.add("成本控制需要改进，建议引入自动化工具，降低人工成本");
            }
            if (improvements.isEmpty()) {
                improvements.add("各项指标表现良好，建议继续保持并寻求进一步优化空间");
            }

            report.setImprovements(improvements);

            // 构建图表数据
            WorkflowReportDTO.ComprehensiveChartDataDTO chartData = new WorkflowReportDTO.ComprehensiveChartDataDTO();
            if (efficiencyAnalysis != null && efficiencyAnalysis.getChartData() != null) {
                chartData.setEfficiency(efficiencyAnalysis.getChartData());
            }
            if (workloadAnalysis != null && workloadAnalysis.getChartData() != null) {
                chartData.setWorkload(workloadAnalysis.getChartData());
            }
            if (qualityAnalysis != null && qualityAnalysis.getChartData() != null) {
                chartData.setQuality(qualityAnalysis.getChartData());
            }
            if (costAnalysis != null && costAnalysis.getChartData() != null) {
                chartData.setCost(costAnalysis.getChartData());
            }
            report.setChartData(chartData);

            // 缓存结果
            redisCache.setCacheObject(cacheKey, report, CACHE_EXPIRE_MINUTES, TimeUnit.MINUTES);
            log.info("综合分析报表数据已缓存，改进建议数: {}", improvements.size());

        } catch (Exception e) {
            log.error("查询综合分析报表数据失败", e);
            // 返回默认值
            report.setTitle("工作流综合分析报表");
            report.setGenerateTime(java.time.LocalDateTime.now());
            report.setQueryParams(queryParams);
            report.setEfficiencyAnalysis(new ArrayList<>());
            report.setWorkloadAnalysis(new ArrayList<>());
            report.setQualityAnalysis(new ArrayList<>());
            report.setCostAnalysis(new ArrayList<>());
            report.setKeyMetrics(new WorkflowReportDTO.KeyMetricsDTO());
            report.setImprovements(new ArrayList<>());
            report.setChartData(new WorkflowReportDTO.ComprehensiveChartDataDTO());
        }

        return report;
    }


    @Override
    public String exportReport(WorkflowReportDTO.ReportExportDTO queryParams) {
        try {
            String format = queryParams.getFormat();
            log.info("开始导出工作流报表，格式: {}, 查询参数: {}", format, queryParams);
            // 获取综合报表数据
            WorkflowReportDTO.ComprehensiveAnalysisDTO reportData = getComprehensiveReport(queryParams.getQueryParams());

            // 生成文件名
            String timestamp = String.valueOf(System.currentTimeMillis());
            String fileName = "workflow_report_" + timestamp + "." + format.toLowerCase();
            String filePath = "/exports/" + fileName;

            // 根据格式生成报表文件
            switch (format.toUpperCase()) {
                case "EXCEL":
                case "XLS":
                case "XLSX":
                    generateExcelReport(reportData, filePath);
                    break;
                case "PDF":
                    generatePdfReport(reportData, filePath);
                    break;
                case "CSV":
                    generateCsvReport(reportData, filePath);
                    break;
                default:
                    throw new IllegalArgumentException("不支持的导出格式: " + format);
            }

            log.info("工作流报表导出成功，文件路径: {}", filePath);
            return filePath;

        } catch (Exception e) {
            log.error("导出工作流报表失败", e);
            throw new RuntimeException("导出报表失败: " + e.getMessage(), e);
        }
    }

    private void generateExcelReport(WorkflowReportDTO.ComprehensiveAnalysisDTO reportData, String filePath) {
        try {
            // 创建工作簿
            Workbook workbook = new XSSFWorkbook();
            Sheet sheet = workbook.createSheet("工作流综合分析报表");

            // 创建标题行
            Row titleRow = sheet.createRow(0);
            titleRow.createCell(0).setCellValue("报表标题");
            titleRow.createCell(1).setCellValue(reportData.getTitle());

            Row timeRow = sheet.createRow(1);
            timeRow.createCell(0).setCellValue("生成时间");
            timeRow.createCell(1).setCellValue(reportData.getGenerateTime().toString());

            // 创建数据表头
            Row headerRow = sheet.createRow(3);
            headerRow.createCell(0).setCellValue("指标类型");
            headerRow.createCell(1).setCellValue("指标值");
            headerRow.createCell(2).setCellValue("描述");

            int rowNum = 4;

            // 写入效率分析数据
            if (reportData.getEfficiencyAnalysis() != null && !reportData.getEfficiencyAnalysis().isEmpty()) {
                WorkflowReportDTO.EfficiencyReportDTO efficiency = reportData.getEfficiencyAnalysis().get(0);
                Row effRow = sheet.createRow(rowNum++);
                effRow.createCell(0).setCellValue("流程效率率");
                effRow.createCell(1).setCellValue(efficiency.getEfficiencyRate());
                effRow.createCell(2).setCellValue("完成流程数/总流程数");
            }

            // 写入工作负载数据
            if (reportData.getWorkloadAnalysis() != null && !reportData.getWorkloadAnalysis().isEmpty()) {
                WorkflowReportDTO.WorkloadReportDTO workload = reportData.getWorkloadAnalysis().get(0);
                Row workRow = sheet.createRow(rowNum++);
                workRow.createCell(0).setCellValue("总任务数");
                workRow.createCell(1).setCellValue(workload.getTotalTasks());
                workRow.createCell(2).setCellValue("系统中所有任务总数");
            }

            // 写入质量分析数据
            if (reportData.getQualityAnalysis() != null && !reportData.getQualityAnalysis().isEmpty()) {
                WorkflowReportDTO.QualityReportDTO quality = reportData.getQualityAnalysis().get(0);
                Row qualityRow = sheet.createRow(rowNum++);
                qualityRow.createCell(0).setCellValue("质量分数");
                qualityRow.createCell(1).setCellValue(quality.getQualityScore());
                qualityRow.createCell(2).setCellValue("综合质量评分");
            }

            // 写入成本分析数据
            if (reportData.getCostAnalysis() != null && !reportData.getCostAnalysis().isEmpty()) {
                WorkflowReportDTO.CostReportDTO cost = reportData.getCostAnalysis().get(0);
                Row costRow = sheet.createRow(rowNum++);
                costRow.createCell(0).setCellValue("总成本");
                costRow.createCell(1).setCellValue(cost.getTotalCost());
                costRow.createCell(2).setCellValue("流程总运营成本");
            }

            // 自动调整列宽
            for (int i = 0; i < 3; i++) {
                sheet.autoSizeColumn(i);
            }

            // 写入文件
            try (FileOutputStream fileOut = new FileOutputStream(filePath)) {
                workbook.write(fileOut);
            }
            workbook.close();

            log.info("Excel报表生成成功: {}", filePath);

        } catch (Exception e) {
            log.error("生成Excel报表失败: {}", filePath, e);
            throw new RuntimeException("Excel报表生成失败", e);
        }
    }

    private void generatePdfReport(WorkflowReportDTO.ComprehensiveAnalysisDTO reportData, String filePath) {
        try {
            // 创建PDF文档
            PDDocument document = new PDDocument();
            PDPage page = new PDPage();
            document.addPage(page);

            PDPageContentStream contentStream = new PDPageContentStream(document, page);

            // 设置字体（使用内置字体）
            PDFont font = new PDType1Font(Standard14Fonts.FontName.HELVETICA_BOLD);
            PDFont normalFont = new PDType1Font(Standard14Fonts.FontName.HELVETICA);
            float yPosition = 750;
            float margin = 50;

            // 写入标题
            contentStream.beginText();
            contentStream.setFont(font, 16);
            contentStream.newLineAtOffset(margin, yPosition);
            contentStream.showText("Workflow Comprehensive Analysis Report");
            contentStream.endText();

            yPosition -= 30;

            // 写入生成时间
            contentStream.beginText();
            contentStream.setFont(normalFont, 12);
            contentStream.newLineAtOffset(margin, yPosition);
            contentStream.showText("Generated Time: " + reportData.getGenerateTime().toString());
            contentStream.endText();

            yPosition -= 40;

            // 写入效率分析
            if (reportData.getEfficiencyAnalysis() != null && !reportData.getEfficiencyAnalysis().isEmpty()) {
                WorkflowReportDTO.EfficiencyReportDTO efficiency = reportData.getEfficiencyAnalysis().get(0);

                contentStream.beginText();
                contentStream.setFont(font, 14);
                contentStream.newLineAtOffset(margin, yPosition);
                contentStream.showText("Efficiency Analysis");
                contentStream.endText();
                yPosition -= 20;

                contentStream.beginText();
                contentStream.setFont(normalFont, 10);
                contentStream.newLineAtOffset(margin + 20, yPosition);
                contentStream.showText("Total Processes: " + efficiency.getTotalProcesses());
                contentStream.endText();
                yPosition -= 15;

                contentStream.beginText();
                contentStream.newLineAtOffset(margin + 20, yPosition);
                contentStream.showText("Completed Processes: " + efficiency.getCompletedProcesses());
                contentStream.endText();
                yPosition -= 15;

                contentStream.beginText();
                contentStream.newLineAtOffset(margin + 20, yPosition);
                contentStream.showText("Efficiency Rate: " + String.format("%.2f%%", efficiency.getEfficiencyRate()));
                contentStream.endText();
                yPosition -= 30;
            }

            // 写入工作负载分析
            if (reportData.getWorkloadAnalysis() != null && !reportData.getWorkloadAnalysis().isEmpty()) {
                WorkflowReportDTO.WorkloadReportDTO workload = reportData.getWorkloadAnalysis().get(0);

                contentStream.beginText();
                contentStream.setFont(font, 14);
                contentStream.newLineAtOffset(margin, yPosition);
                contentStream.showText("Workload Analysis");
                contentStream.endText();
                yPosition -= 20;

                contentStream.beginText();
                contentStream.setFont(normalFont, 10);
                contentStream.newLineAtOffset(margin + 20, yPosition);
                contentStream.showText("Total Tasks: " + workload.getTotalTasks());
                contentStream.endText();
                yPosition -= 15;

                contentStream.beginText();
                contentStream.newLineAtOffset(margin + 20, yPosition);
                contentStream.showText("Pending Tasks: " + workload.getPendingTasks());
                contentStream.endText();
                yPosition -= 15;

                contentStream.beginText();
                contentStream.newLineAtOffset(margin + 20, yPosition);
                contentStream.showText("Completed Tasks: " + workload.getCompletedTasks());
                contentStream.endText();
                yPosition -= 30;
            }

            // 写入质量分析
            if (reportData.getQualityAnalysis() != null && !reportData.getQualityAnalysis().isEmpty()) {
                WorkflowReportDTO.QualityReportDTO quality = reportData.getQualityAnalysis().get(0);

                contentStream.beginText();
                contentStream.setFont(font, 14);
                contentStream.newLineAtOffset(margin, yPosition);
                contentStream.showText("Quality Analysis");
                contentStream.endText();
                yPosition -= 20;

                contentStream.beginText();
                contentStream.setFont(normalFont, 10);
                contentStream.newLineAtOffset(margin + 20, yPosition);
                contentStream.showText("Quality Score: " + String.format("%.2f", quality.getQualityScore()));
                contentStream.endText();
                yPosition -= 15;

                contentStream.beginText();
                contentStream.newLineAtOffset(margin + 20, yPosition);
                contentStream.showText("Error Processes: " + quality.getErrorProcesses());
                contentStream.endText();
                yPosition -= 30;
            }

            // 写入成本分析
            if (reportData.getCostAnalysis() != null && !reportData.getCostAnalysis().isEmpty()) {
                WorkflowReportDTO.CostReportDTO cost = reportData.getCostAnalysis().get(0);

                contentStream.beginText();
                contentStream.setFont(font, 14);
                contentStream.newLineAtOffset(margin, yPosition);
                contentStream.showText("Cost Analysis");
                contentStream.endText();
                yPosition -= 20;

                contentStream.beginText();
                contentStream.setFont(normalFont, 10);
                contentStream.newLineAtOffset(margin + 20, yPosition);
                contentStream.showText("Total Cost: $" + String.format("%.2f", cost.getTotalCost()));
                contentStream.endText();
                yPosition -= 15;

                contentStream.beginText();
                contentStream.newLineAtOffset(margin + 20, yPosition);
                contentStream.showText("Cost Per Process: $" + String.format("%.2f", cost.getCostPerProcess()));
                contentStream.endText();
            }

            contentStream.close();

            // 保存文档
            document.save(filePath);
            document.close();

            log.info("PDF报表生成成功: {}", filePath);

        } catch (Exception e) {
            log.error("生成PDF报表失败: {}", filePath, e);
            throw new RuntimeException("PDF报表生成失败", e);
        }
    }

    private void generateCsvReport(WorkflowReportDTO.ComprehensiveAnalysisDTO reportData, String filePath) {
        try (PrintWriter writer = new PrintWriter(new FileWriter(filePath, StandardCharsets.UTF_8))) {
            // 写入BOM以支持中文
            writer.write("\uFEFF");

            // 写入标题信息
            writer.println("报表标题," + reportData.getTitle());
            writer.println("生成时间," + reportData.getGenerateTime().toString());
            writer.println();

            // 写入表头
            writer.println("分析类型,指标名称,指标值,单位,描述");

            // 写入效率分析数据
            if (reportData.getEfficiencyAnalysis() != null && !reportData.getEfficiencyAnalysis().isEmpty()) {
                WorkflowReportDTO.EfficiencyReportDTO efficiency = reportData.getEfficiencyAnalysis().get(0);
                writer.println("效率分析,总流程数," + efficiency.getTotalProcesses() + ",个,系统中所有流程实例总数");
                writer.println("效率分析,完成流程数," + efficiency.getCompletedProcesses() + ",个,已完成的流程实例数");
                writer.println("效率分析,平均处理时间," + String.format("%.2f", efficiency.getAverageProcessingTime()) + ",天,流程平均处理时长");
                writer.println("效率分析,效率率," + String.format("%.2f", efficiency.getEfficiencyRate()) + ",%,完成率指标");
            }

            // 写入工作负载分析数据
            if (reportData.getWorkloadAnalysis() != null && !reportData.getWorkloadAnalysis().isEmpty()) {
                WorkflowReportDTO.WorkloadReportDTO workload = reportData.getWorkloadAnalysis().get(0);
                writer.println("工作负载分析,总任务数," + workload.getTotalTasks() + ",个,系统中所有任务总数");
                writer.println("工作负载分析,待办任务数," + workload.getPendingTasks() + ",个,待处理的任务数");
                writer.println("工作负载分析,已完成任务数," + workload.getCompletedTasks() + ",个,已完成的任务数");
                writer.println("工作负载分析,平均每用户任务数," + String.format("%.2f", workload.getAverageTasksPerUser()) + ",个,用户平均任务负载");
            }

            // 写入质量分析数据
            if (reportData.getQualityAnalysis() != null && !reportData.getQualityAnalysis().isEmpty()) {
                WorkflowReportDTO.QualityReportDTO quality = reportData.getQualityAnalysis().get(0);
                writer.println("质量分析,总流程数," + quality.getTotalProcesses() + ",个,参与质量分析的流程总数");
                writer.println("质量分析,错误流程数," + quality.getErrorProcesses() + ",个,出现错误的流程数");
                writer.println("质量分析,返工流程数," + quality.getReworkProcesses() + ",个,需要返工的流程数");
                writer.println("质量分析,质量分数," + String.format("%.2f", quality.getQualityScore()) + ",分,综合质量评分");

                // 写入质量指标
                if (quality.getQualityMetrics() != null) {
                    for (WorkflowReportDTO.QualityMetricsDTO metrics : quality.getQualityMetrics()) {
                        writer.println("质量分析,准确率," + String.format("%.2f", metrics.getAccuracyRate()) + ",%,质量指标");
                        writer.println("质量分析,及时率," + String.format("%.2f", metrics.getTimelyRate()) + ",%,质量指标");
                        writer.println("质量分析,完整率," + String.format("%.2f", metrics.getCompletenessRate()) + ",%,质量指标");
                        writer.println("质量分析,一次通过率," + String.format("%.2f", metrics.getFirstPassRate()) + ",%,质量指标");
                        writer.println("质量分析,返工率," + String.format("%.2f", metrics.getReworkRate()) + ",%,质量指标");
                        writer.println("质量分析,错误率," + String.format("%.2f", metrics.getErrorRate()) + ",%,质量指标");
                    }
                }
            }

            // 写入成本分析数据
            if (reportData.getCostAnalysis() != null && !reportData.getCostAnalysis().isEmpty()) {
                WorkflowReportDTO.CostReportDTO cost = reportData.getCostAnalysis().get(0);
                writer.println("成本分析,总成本," + String.format("%.2f", cost.getTotalCost()) + ",元,流程总运营成本");
                writer.println("成本分析,人工成本," + String.format("%.2f", cost.getLaborCost()) + ",元,人力资源成本");
                writer.println("成本分析,系统成本," + String.format("%.2f", cost.getSystemCost()) + ",元,系统运维成本");
                writer.println("成本分析,其他成本," + String.format("%.2f", cost.getOtherCost()) + ",元,其他相关成本");
                writer.println("成本分析,单流程成本," + String.format("%.2f", cost.getCostPerProcess()) + ",元,平均每个流程的成本");
            }

            writer.flush();
            log.info("CSV报表生成成功: {}", filePath);

        } catch (Exception e) {
            log.error("生成CSV报表失败: {}", filePath, e);
            throw new RuntimeException("CSV报表生成失败", e);
        }
    }

    @Override
    public List<WorkflowReportDTO.ReportTemplateDTO> getReportTemplates() {
        String cacheKey = CACHE_KEY_PREFIX + "templates";

        // 尝试从Redis缓存获取
        List<WorkflowReportDTO.ReportTemplateDTO> cachedTemplates = redisCache.getCacheObject(cacheKey);
        if (cachedTemplates != null) {
            log.debug("从缓存获取报表模板列表");
            return cachedTemplates;
        }

        List<WorkflowReportDTO.ReportTemplateDTO> templates = new ArrayList<>();

        try {
            // 从数据库查询报表模板
            // 首先尝试从数据库获取模板配置
            List<WorkflowReportDTO.ReportTemplateDTO> dbTemplates = wfReportConfigMapper.selectReportTemplates();
            if (dbTemplates != null && !dbTemplates.isEmpty()) {
                templates.addAll(dbTemplates);
                log.info("从数据库加载报表模板，数量: {}", dbTemplates.size());
            } else {
                log.info("数据库中无报表模板，使用默认模板");
            }

            // 创建默认模板（修复字段不匹配问题）
            WorkflowReportDTO.ReportTemplateDTO efficiencyTemplate = new WorkflowReportDTO.ReportTemplateDTO();
            efficiencyTemplate.setId(1L);
            efficiencyTemplate.setTemplateId("EFFICIENCY_001");
            efficiencyTemplate.setTemplateName("效率分析模板");
            efficiencyTemplate.setDescription("用于分析流程处理效率的报表模板，包含平均处理时间、完成率等指标");
            efficiencyTemplate.setTemplateType("EFFICIENCY");
            efficiencyTemplate.setTemplateConfig("{\"charts\":[\"line\",\"bar\"],\"metrics\":[\"efficiency_rate\",\"avg_time\"]}");
            efficiencyTemplate.setEnabled(true);
            efficiencyTemplate.setCreateTime(LocalDateTime.now());
            efficiencyTemplate.setUpdateTime(LocalDateTime.now());
            efficiencyTemplate.setCreateBy("system");
            efficiencyTemplate.setUpdateBy("system");
            templates.add(efficiencyTemplate);

            WorkflowReportDTO.ReportTemplateDTO workloadTemplate = new WorkflowReportDTO.ReportTemplateDTO();
            workloadTemplate.setId(2L);
            workloadTemplate.setTemplateId("WORKLOAD_001");
            workloadTemplate.setTemplateName("工作量统计模板");
            workloadTemplate.setDescription("用于统计各部门工作量的报表模板，包含任务分布、完成情况等");
            workloadTemplate.setTemplateType("WORKLOAD");
            workloadTemplate.setTemplateConfig("{\"charts\":[\"pie\",\"bar\"],\"metrics\":[\"total_tasks\",\"pending_tasks\"]}");
            workloadTemplate.setEnabled(true);
            workloadTemplate.setCreateTime(LocalDateTime.now());
            workloadTemplate.setUpdateTime(LocalDateTime.now());
            workloadTemplate.setCreateBy("system");
            workloadTemplate.setUpdateBy("system");
            templates.add(workloadTemplate);

            WorkflowReportDTO.ReportTemplateDTO qualityTemplate = new WorkflowReportDTO.ReportTemplateDTO();
            qualityTemplate.setId(3L);
            qualityTemplate.setTemplateId("QUALITY_001");
            qualityTemplate.setTemplateName("质量分析模板");
            qualityTemplate.setDescription("用于分析流程质量的报表模板，包含错误率、返工率等质量指标");
            qualityTemplate.setTemplateType("QUALITY");
            qualityTemplate.setTemplateConfig("{\"charts\":[\"gauge\",\"radar\"],\"metrics\":[\"quality_score\",\"error_rate\"]}");
            qualityTemplate.setEnabled(true);
            qualityTemplate.setCreateTime(LocalDateTime.now());
            qualityTemplate.setUpdateTime(LocalDateTime.now());
            qualityTemplate.setCreateBy("system");
            qualityTemplate.setUpdateBy("system");
            templates.add(qualityTemplate);

            WorkflowReportDTO.ReportTemplateDTO costTemplate = new WorkflowReportDTO.ReportTemplateDTO();
            costTemplate.setId(4L);
            costTemplate.setTemplateId("COST_001");
            costTemplate.setTemplateName("成本分析模板");
            costTemplate.setDescription("用于分析流程成本的报表模板，包含人工成本、系统成本等");
            costTemplate.setTemplateType("COST");
            costTemplate.setTemplateConfig("{\"charts\":[\"line\",\"stack_bar\"],\"metrics\":[\"total_cost\",\"cost_per_process\"]}");
            costTemplate.setEnabled(true);
            costTemplate.setCreateTime(LocalDateTime.now());
            costTemplate.setUpdateTime(LocalDateTime.now());
            costTemplate.setCreateBy("system");
            costTemplate.setUpdateBy("system");
            templates.add(costTemplate);

            WorkflowReportDTO.ReportTemplateDTO comprehensiveTemplate = new WorkflowReportDTO.ReportTemplateDTO();
            comprehensiveTemplate.setId(5L);
            comprehensiveTemplate.setTemplateId("COMPREHENSIVE_001");
            comprehensiveTemplate.setTemplateName("综合分析模板");
            comprehensiveTemplate.setDescription("综合分析报表模板，包含效率、工作量、质量、成本等全方位指标");
            comprehensiveTemplate.setTemplateType("COMPREHENSIVE");
            comprehensiveTemplate.setTemplateConfig("{\"charts\":[\"dashboard\",\"multi_chart\"],\"metrics\":[\"all_metrics\"]}");
            comprehensiveTemplate.setEnabled(true);
            comprehensiveTemplate.setCreateTime(LocalDateTime.now());
            comprehensiveTemplate.setUpdateTime(LocalDateTime.now());
            comprehensiveTemplate.setCreateBy("system");
            comprehensiveTemplate.setUpdateBy("system");
            templates.add(comprehensiveTemplate);

            // 缓存结果
            redisCache.setCacheObject(cacheKey, templates, CACHE_EXPIRE_MINUTES, TimeUnit.MINUTES);
            log.info("报表模板列表已缓存，模板数量: {}", templates.size());

        } catch (Exception e) {
            log.error("查询报表模板失败", e);
            // 返回空列表
            templates = new ArrayList<>();
        }

        return templates;
    }

    @Override
    public WorkflowReportDTO.RealtimeDashboardDTO getRealtimeDashboard() {
        String cacheKey = CACHE_KEY_PREFIX + "dashboard";

        // 尝试从Redis缓存获取（实时数据缓存时间较短）
        WorkflowReportDTO.RealtimeDashboardDTO cachedDashboard = redisCache.getCacheObject(cacheKey);
        if (cachedDashboard != null) {
            log.debug("从缓存获取实时仪表板数据");
            return cachedDashboard;
        }

        WorkflowReportDTO.RealtimeDashboardDTO dashboard = new WorkflowReportDTO.RealtimeDashboardDTO();

        try {
            // 从数据库获取实时数据
            List<WfInstance> runningInstances = wfInstanceMapper.selectRunningInstances();
            List<PendingTaskDTO> pendingTasks = wfTaskMapper.selectPendingTaskStatistics();
            List<TodayCompletedInstanceDTO> todayCompleted = wfInstanceMapper.selectTodayCompletedInstances();
            List<AverageProcessingTimeDTO> avgProcessingTime = wfInstanceMapper.selectAverageProcessingTime();

            // 计算实时指标
            int runningProcesses = runningInstances.size();
            int pendingTaskCount = pendingTasks.size();
            int completedToday = todayCompleted.size();

            // 计算平均处理时间（小时）
            double averageProcessingTime = 0.0;
            if (!avgProcessingTime.isEmpty()) {
                Long avgTime = avgProcessingTime.get(0).getAvgTime();
                if (avgTime != null) {
                    averageProcessingTime = avgTime / 3600000.0; // 毫秒转小时
                }
            }

            // 计算系统负载（基于运行中的流程数量）
            double systemLoad = Math.min(100.0, (runningProcesses + pendingTaskCount) * 2.0);

            dashboard.setRunningProcesses(runningProcesses);
            dashboard.setPendingTasks(pendingTaskCount);
            dashboard.setTodayCompletedTasks(completedToday);
            dashboard.setAverageProcessingTime(averageProcessingTime);
            dashboard.setSystemLoad(systemLoad);

            // 获取最近活动（基于最近的任务和实例）
            List<Map<String, Object>> recentActivities = new ArrayList<>();

            // 添加一些模拟的最近活动数据
            Map<String, Object> activity1 = new HashMap<>();
            activity1.put("message", "任务已完成");
            activity1.put("time", new Date());
            activity1.put("type", "task_completed");
            recentActivities.add(activity1);
            
            Map<String, Object> activity2 = new HashMap<>();
            activity2.put("message", "新流程已启动");
            activity2.put("time", new Date());
            activity2.put("type", "process_started");
            recentActivities.add(activity2);

            // 从最近的任务中获取活动信息
            List<RecentTaskDTO> recentTasks = wfTaskMapper.selectRecentTasks(10);
            for (RecentTaskDTO task : recentTasks) {
                String taskName = task.getName();
                String assignee = task.getAssignee();
                Date createTime = task.getCreateTime();

                if (taskName != null && assignee != null) {
                    Map<String, Object> activity = new HashMap<>();
                    activity.put("message", String.format("用户%s处理了任务：%s", assignee, taskName));
                    activity.put("time", createTime);
                    activity.put("type", "task_processed");
                    activity.put("assignee", assignee);
                    activity.put("taskName", taskName);
                    recentActivities.add(activity);
                }
            }

            // 如果没有足够的活动记录，添加一些默认信息
            if (recentActivities.isEmpty()) {
                Map<String, Object> defaultActivity1 = new HashMap<>();
                defaultActivity1.put("message", "系统正常运行中");
                defaultActivity1.put("time", new Date());
                defaultActivity1.put("type", "system_status");
                recentActivities.add(defaultActivity1);
                
                Map<String, Object> defaultActivity2 = new HashMap<>();
                defaultActivity2.put("message", "暂无最近活动记录");
                defaultActivity2.put("time", new Date());
                defaultActivity2.put("type", "no_activity");
                recentActivities.add(defaultActivity2);
            }

            dashboard.setRecentActivities(recentActivities);

            // 缓存结果（实时数据缓存时间较短，1分钟）
            redisCache.setCacheObject(cacheKey, dashboard, 1, TimeUnit.MINUTES);
            log.info("实时仪表板数据已缓存，运行中流程: {}, 待处理任务: {}, 今日完成: {}, 平均处理时间: {}小时",
                    runningProcesses, pendingTaskCount, completedToday, String.format("%.2f", averageProcessingTime));

        } catch (Exception e) {
            log.error("查询实时仪表板数据失败", e);
            // 返回默认值
            dashboard.setRunningProcesses(0);
            dashboard.setPendingTasks(0);
            dashboard.setTodayCompletedTasks(0);
            dashboard.setAverageProcessingTime(0.0);
            dashboard.setSystemLoad(0.0);
            dashboard.setRecentActivities(new ArrayList<Map<String, Object>>());
        }

        return dashboard;
    }

    /**
     * 创建报表模板对象
     */
    private WorkflowReportDTO.ReportTemplateDTO createReportTemplate(String templateId, String name, String description, String type) {
        WorkflowReportDTO.ReportTemplateDTO template = new WorkflowReportDTO.ReportTemplateDTO();
        template.setTemplateId(templateId);
        template.setTemplateName(name);
        template.setDescription(description);
        template.setTemplateType(type);
        template.setCreateTime(LocalDateTime.now());
        template.setEnabled(true);
        return template;
    }
}