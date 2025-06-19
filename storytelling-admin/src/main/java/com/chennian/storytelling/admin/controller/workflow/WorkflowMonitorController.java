package com.chennian.storytelling.admin.controller.workflow;

import com.chennian.storytelling.common.response.ServerResponseEntity;
import com.chennian.storytelling.service.WorkflowService;

import com.chennian.storytelling.bean.dto.TaskDTO;
import com.chennian.storytelling.bean.dto.WorkflowStatisticsDTO;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.HashMap;

/**
 * 工作流监控控制器
 * 提供工作流监控、统计和分析功能
 * 
 * @author chennian
 */
@Api(tags = "工作流监控管理")
@RestController
@RequestMapping("/sys/workflow/monitor")
@RequiredArgsConstructor
public class WorkflowMonitorController {

    private final WorkflowService workflowService;

    /**
     * 获取工作流仪表板数据
     */
    @ApiOperation("获取工作流仪表板数据")
    @GetMapping("/dashboard")
    public ServerResponseEntity<Map<String, Object>> getDashboardData(
            @RequestParam(value = "startTime", required = false) 
            @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime startTime,
            @RequestParam(value = "endTime", required = false) 
            @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime endTime) {
        
        Map<String, Object> dashboard = new HashMap<>();
        
        // 获取统计数据
        WorkflowStatisticsDTO statistics = workflowService.getWorkflowStatistics(startTime, endTime);
        dashboard.put("statistics", statistics);
        
        // 获取最近的流程实例（可以扩展）
        // dashboard.put("recentProcesses", recentProcesses);
        
        // 获取待办任务统计（可以扩展）
        // dashboard.put("pendingTasksStats", pendingTasksStats);
        
        return ServerResponseEntity.success(dashboard);
    }
    
    /**
     * 获取详细统计报告
     */
    @ApiOperation("获取详细统计报告")
    @GetMapping("/report")
    public ServerResponseEntity<WorkflowStatisticsDTO> getDetailedReport(
            @RequestParam(value = "startTime", required = false) 
            @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime startTime,
            @RequestParam(value = "endTime", required = false) 
            @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime endTime,
            @RequestParam(value = "groupBy", defaultValue = "day") String groupBy) {
        
        // 如果没有指定时间范围，默认查询最近30天
        if (startTime == null) {
            startTime = LocalDateTime.now().minusDays(30);
        }
        if (endTime == null) {
            endTime = LocalDateTime.now();
        }
        
        return ServerResponseEntity.success(workflowService.getWorkflowStatistics(startTime, endTime));
    }
    
    /**
     * 获取流程性能分析
     */
    @ApiOperation("获取流程性能分析")
    @GetMapping("/performance")
    public ServerResponseEntity<Map<String, Object>> getPerformanceAnalysis(
            @RequestParam(value = "processDefinitionKey", required = false) String processDefinitionKey,
            @RequestParam(value = "startTime", required = false) 
            @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime startTime,
            @RequestParam(value = "endTime", required = false) 
            @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime endTime) {
        
        Map<String, Object> performance = new HashMap<>();
        
        // 获取基础统计
        WorkflowStatisticsDTO statistics = workflowService.getWorkflowStatistics(startTime, endTime);
        performance.put("basicStats", statistics);
        
        // 这里可以添加更多性能分析逻辑
        // 比如：平均处理时间、瓶颈分析、异常流程分析等
        
        return ServerResponseEntity.success(performance);
    }
    
    /**
     * 获取用户工作负载分析
     */
    @ApiOperation("获取用户工作负载分析")
    @GetMapping("/workload")
    public ServerResponseEntity<Map<String, Object>> getUserWorkloadAnalysis(
            @RequestParam(value = "assignee", required = false) String assignee,
            @RequestParam(value = "startTime", required = false) 
            @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime startTime,
            @RequestParam(value = "endTime", required = false) 
            @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime endTime) {
        
        Map<String, Object> workload = new HashMap<>();
        
        if (assignee != null) {
            // 获取指定用户的历史任务
            List<TaskDTO> historyTasks = workflowService.findHistoryTasks(assignee, startTime, endTime);
            workload.put("historyTasks", historyTasks);
            workload.put("taskCount", historyTasks.size());
        }
        
        // 这里可以添加更多工作负载分析
        // 比如：任务分布、处理效率、超时任务等
        
        return ServerResponseEntity.success(workload);
    }
    
    /**
     * 获取异常流程监控
     */
    @ApiOperation("获取异常流程监控")
    @GetMapping("/exceptions")
    public ServerResponseEntity<Map<String, Object>> getExceptionMonitoring(
            @RequestParam(value = "startTime", required = false) 
            @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime startTime,
            @RequestParam(value = "endTime", required = false) 
            @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime endTime) {
        
        Map<String, Object> exceptions = new HashMap<>();
        
        // 获取统计数据
        WorkflowStatisticsDTO statistics = workflowService.getWorkflowStatistics(startTime, endTime);
        
        // 计算异常指标
        long terminatedProcesses = statistics.getTerminatedProcessInstances();
        long timeoutTasks = statistics.getOverdueTasks();
        
        exceptions.put("terminatedProcesses", terminatedProcesses);
        exceptions.put("timeoutTasks", timeoutTasks);
        exceptions.put("statistics", statistics);
        
        return ServerResponseEntity.success(exceptions);
    }
    
    /**
     * 导出统计报告
     */
    @ApiOperation("导出统计报告")
    @GetMapping("/export")
    public ServerResponseEntity<Map<String, Object>> exportReport(
            @RequestParam(value = "format", defaultValue = "json") String format,
            @RequestParam(value = "startTime", required = false) 
            @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime startTime,
            @RequestParam(value = "endTime", required = false) 
            @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime endTime) {
        
        Map<String, Object> exportData = new HashMap<>();
        
        // 获取完整统计数据
        WorkflowStatisticsDTO statistics = workflowService.getWorkflowStatistics(startTime, endTime);
        exportData.put("statistics", statistics);
        exportData.put("exportTime", LocalDateTime.now());
        exportData.put("format", format);
        
        // 根据格式返回不同的数据结构
        // 这里可以扩展支持Excel、PDF等格式
        
        return ServerResponseEntity.success(exportData);
    }
}