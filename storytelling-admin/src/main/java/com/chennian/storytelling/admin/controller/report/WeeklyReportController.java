package com.chennian.storytelling.admin.controller.report;

import com.chennian.storytelling.bean.dto.WeeklyReportDTO;
import com.chennian.storytelling.service.WeeklyReportService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;

/**
 * 周报控制器
 * 
 * @author TraeAI
 */
@RestController
@RequestMapping("/report/weekly")
public class WeeklyReportController {

    @Autowired
    private WeeklyReportService weeklyReportService;

    /**
     * 创建周报
     */
    @PostMapping
    public Long createWeeklyReport(@RequestBody WeeklyReportDTO weeklyReportDTO) {
        return weeklyReportService.createWeeklyReport(weeklyReportDTO);
    }

    /**
     * 更新周报
     */
    @PutMapping("/{id}")
    public boolean updateWeeklyReport(@PathVariable Long id, @RequestBody WeeklyReportDTO weeklyReportDTO) {
        weeklyReportDTO.setId(id);
        return weeklyReportService.updateWeeklyReport(weeklyReportDTO);
    }

    /**
     * 删除周报
     */
    @DeleteMapping("/{id}")
    public boolean deleteWeeklyReport(@PathVariable Long id) {
        return weeklyReportService.deleteWeeklyReport(id);
    }

    /**
     * 根据ID获取周报
     */
    @GetMapping("/{id}")
    public WeeklyReportDTO getWeeklyReportById(@PathVariable Long id) {
        return weeklyReportService.getWeeklyReportById(id);
    }

    /**
     * 根据用户ID获取周报列表
     */
    @GetMapping("/user/{userId}")
    public List<WeeklyReportDTO> listWeeklyReportsByUserId(@PathVariable Long userId) {
        return weeklyReportService.listWeeklyReportsByUserId(userId);
    }
    
    /**
     * 获取指定用户在指定周的周报
     */
    @GetMapping("/user/{userId}/week/{weekStartDate}")
    public WeeklyReportDTO getWeeklyReportByUserIdAndDateRange(
            @PathVariable Long userId,
            @PathVariable @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate weekStartDate) {
        return weeklyReportService.getWeeklyReportByUserIdAndDateRange(userId, weekStartDate);
    }
    
}