package com.chennian.storytelling.admin.controller.report;

import com.chennian.storytelling.bean.dto.ReportDTO;
import com.chennian.storytelling.service.ReportService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;

/**
 * 统一报告控制器，支持日报和周报
 */
@RestController
@RequestMapping("/report")
public class ReportController {

    private final ReportService reportService;

    public ReportController(@Autowired ReportService reportService) {
        this.reportService = reportService;
    }

    /**
     * 创建报告（type: daily/weekly）
     */
    @PostMapping
    public Long createReport(@RequestBody ReportDTO reportDTO) {
        return reportService.createReport(reportDTO);
    }

    /**
     * 更新报告
     */
    @PutMapping("/{id}")
    public boolean updateReport(@PathVariable Long id, @RequestBody ReportDTO reportDTO) {
        reportDTO.setId(id);
        return reportService.updateReport(reportDTO);
    }

    /**
     * 删除报告
     */
    @DeleteMapping("/{id}")
    public boolean deleteReport(@PathVariable Long id) {
        return reportService.deleteReport(id);
    }

    /**
     * 根据ID获取报告
     */
    @GetMapping("/{id}")
    public ReportDTO getReportById(@PathVariable Long id) {
        return reportService.getReportById(id);
    }

    /**
     * 根据用户ID和类型获取报告列表
     */
    @GetMapping("/user/{userId}")
    public List<ReportDTO> listReportsByUserId(@PathVariable Long userId, @RequestParam String type) {
        return reportService.listReportsByUserId(userId, type);
    }

    /**
     * 获取指定用户在指定日期的日报
     */
    @GetMapping("/user/{userId}/date/{reportDate}")
    public ReportDTO getDailyReportByUserIdAndDate(
            @PathVariable Long userId,
            @PathVariable @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate reportDate) {
        return reportService.getDailyReportByUserIdAndDate(userId, reportDate);
    }

    /**
     * 获取指定用户在指定周的周报
     */
    @GetMapping("/user/{userId}/week/{weekStartDate}")
    public ReportDTO getWeeklyReportByUserIdAndDateRange(
            @PathVariable Long userId,
            @PathVariable @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate weekStartDate) {
        return reportService.getWeeklyReportByUserIdAndDateRange(userId, weekStartDate);
    }

    /**
     * 提交报告并发起审批流程
     */
    @PostMapping("/submit")
    public Long submitReportWithWorkflow(@RequestBody ReportDTO reportDTO) {
        return reportService.submitReportWithWorkflow(reportDTO);
    }

    /**
     * 领导审批报告
     */
    @PostMapping("/approve/{id}")
    public boolean approveReport(@PathVariable Long id, @RequestParam boolean approve, @RequestParam(required = false) String comment) {
        return reportService.approveReport(id, approve, comment);
    }
}