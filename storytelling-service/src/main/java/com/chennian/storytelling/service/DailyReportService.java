package com.chennian.storytelling.service;

import com.chennian.storytelling.bean.dto.DailyReportDTO;
import java.util.List;

/**
 * 日报服务接口
 * 
 * @author TraeAI
 */
public interface DailyReportService {

    /**
     * 创建日报
     *
     * @param dailyReportDTO 日报信息
     * @return 创建的日报ID
     */
    Long createDailyReport(DailyReportDTO dailyReportDTO);

    /**
     * 更新日报
     *
     * @param dailyReportDTO 日报信息
     * @return 是否成功
     */
    boolean updateDailyReport(DailyReportDTO dailyReportDTO);

    /**
     * 删除日报
     *
     * @param id 日报ID
     * @return 是否成功
     */
    boolean deleteDailyReport(Long id);

    /**
     * 根据ID获取日报
     *
     * @param id 日报ID
     * @return 日报信息
     */
    DailyReportDTO getDailyReportById(Long id);

    /**
     * 根据用户ID获取日报列表
     *
     * @param userId 用户ID
     * @return 日报列表
     */
    List<DailyReportDTO> listDailyReportsByUserId(Long userId);
    
    /**
     * 获取指定用户在指定日期的日报
     *
     * @param userId 用户ID
     * @param reportDate 报告日期
     * @return 日报信息，如果不存在则返回null
     */
    DailyReportDTO getDailyReportByUserIdAndDate(Long userId, java.time.LocalDate reportDate);
}