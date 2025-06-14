package com.chennian.storytelling.admin.controller.report;

import com.chennian.storytelling.bean.dto.DailyReportDTO;
import com.chennian.storytelling.service.DailyReportService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;

/**
 * 日报控制器
 * 
 * @author TraeAI
 */
@RestController
@RequestMapping("/report/daily")
public class DailyReportController {

    @Autowired
    private DailyReportService dailyReportService;

    /**
     * 创建日报
     */
    @PostMapping
    public Long createDailyReport(@RequestBody DailyReportDTO dailyReportDTO) {
        return dailyReportService.createDailyReport(dailyReportDTO);
    }

    /**
     * 更新日报
     */
    @PutMapping("/{id}")
    public boolean updateDailyReport(@PathVariable Long id, @RequestBody DailyReportDTO dailyReportDTO) {
        dailyReportDTO.setId(id);
        return dailyReportService.updateDailyReport(dailyReportDTO);
    }

    /**
     * 删除日报
     */
    @DeleteMapping("/{id}")
    public boolean deleteDailyReport(@PathVariable Long id) {
        return dailyReportService.deleteDailyReport(id);
    }

    /**
     * 根据ID获取日报
     */
    @GetMapping("/{id}")
    public DailyReportDTO getDailyReportById(@PathVariable Long id) {
        return dailyReportService.getDailyReportById(id);
    }

    /**
     * 根据用户ID获取日报列表
     */
    @GetMapping("/user/{userId}")
    public List<DailyReportDTO> listDailyReportsByUserId(@PathVariable Long userId) {
        return dailyReportService.listDailyReportsByUserId(userId);
    }
    
    /**
     * 获取指定用户在指定日期的日报
     */
    @GetMapping("/user/{userId}/date/{reportDate}")
    public DailyReportDTO getDailyReportByUserIdAndDate(
            @PathVariable Long userId,
            @PathVariable @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate reportDate) {
        return dailyReportService.getDailyReportByUserIdAndDate(userId, reportDate);
    }
    
}