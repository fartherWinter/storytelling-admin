package com.chennian.storytelling.service;

import com.chennian.storytelling.bean.dto.WeeklyReportDTO;
import java.util.List;

/**
 * 周报服务接口
 * 
 * @author TraeAI
 */
public interface WeeklyReportService {

    /**
     * 创建周报
     *
     * @param weeklyReportDTO 周报信息
     * @return 创建的周报ID
     */
    Long createWeeklyReport(WeeklyReportDTO weeklyReportDTO);

    /**
     * 更新周报
     *
     * @param weeklyReportDTO 周报信息
     * @return 是否成功
     */
    boolean updateWeeklyReport(WeeklyReportDTO weeklyReportDTO);

    /**
     * 删除周报
     *
     * @param id 周报ID
     * @return 是否成功
     */
    boolean deleteWeeklyReport(Long id);

    /**
     * 根据ID获取周报
     *
     * @param id 周报ID
     * @return 周报信息
     */
    WeeklyReportDTO getWeeklyReportById(Long id);

    /**
     * 根据用户ID获取周报列表
     *
     * @param userId 用户ID
     * @return 周报列表
     */
    List<WeeklyReportDTO> listWeeklyReportsByUserId(Long userId);
    
    /**
     * 获取指定用户在指定周的周报
     *
     * @param userId 用户ID
     * @param weekStartDate 周开始日期
     * @return 周报信息，如果不存在则返回null
     */
    WeeklyReportDTO getWeeklyReportByUserIdAndDateRange(Long userId, java.time.LocalDate weekStartDate);
}