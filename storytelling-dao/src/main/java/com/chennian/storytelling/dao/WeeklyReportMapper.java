package com.chennian.storytelling.dao;

import com.chennian.storytelling.bean.dto.WeeklyReportDTO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.time.LocalDate;
import java.util.List;

/**
 * 周报表 Mapper 接口
 *
 * @author TraeAI
 */
@Mapper
public interface WeeklyReportMapper {

    /**
     * 插入周报
     *
     * @param weeklyReport 周报信息
     * @return 影响行数
     */
    int insertWeeklyReport(WeeklyReportDTO weeklyReport);

    /**
     * 更新周报
     *
     * @param weeklyReport 周报信息
     * @return 影响行数
     */
    int updateWeeklyReport(WeeklyReportDTO weeklyReport);

    /**
     * 根据ID删除周报
     *
     * @param id 周报ID
     * @return 影响行数
     */
    int deleteWeeklyReportById(@Param("id") Long id);

    /**
     * 根据ID获取周报
     *
     * @param id 周报ID
     * @return 周报信息
     */
    WeeklyReportDTO selectWeeklyReportById(@Param("id") Long id);

    /**
     * 根据用户ID获取周报列表
     *
     * @param userId 用户ID
     * @return 周报列表
     */
    List<WeeklyReportDTO> listWeeklyReportsByUserId(@Param("userId") Long userId);

    /**
     * 根据用户ID和周开始日期获取周报
     *
     * @param userId 用户ID
     * @param weekStartDate 周开始日期
     * @return 周报信息
     */
    WeeklyReportDTO selectWeeklyReportByUserIdAndDateRange(@Param("userId") Long userId, @Param("weekStartDate") LocalDate weekStartDate);
}