package com.chennian.storytelling.dao;

import com.chennian.storytelling.bean.dto.DailyReportDTO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.time.LocalDate;
import java.util.List;

/**
 * 日报表 Mapper 接口
 *
 * @author TraeAI
 */
@Mapper
public interface DailyReportMapper {

    /**
     * 插入日报
     *
     * @param dailyReport 日报信息
     * @return 影响行数
     */
    int insertDailyReport(DailyReportDTO dailyReport);

    /**
     * 更新日报
     *
     * @param dailyReport 日报信息
     * @return 影响行数
     */
    int updateDailyReport(DailyReportDTO dailyReport);

    /**
     * 根据ID删除日报
     *
     * @param id 日报ID
     * @return 影响行数
     */
    int deleteDailyReportById(@Param("id") Long id);

    /**
     * 根据ID获取日报
     *
     * @param id 日报ID
     * @return 日报信息
     */
    DailyReportDTO selectDailyReportById(@Param("id") Long id);

    /**
     * 根据用户ID获取日报列表
     *
     * @param userId 用户ID
     * @return 日报列表
     */
    List<DailyReportDTO> listDailyReportsByUserId(@Param("userId") Long userId);

    /**
     * 根据用户ID和报告日期获取日报
     *
     * @param userId 用户ID
     * @param reportDate 报告日期
     * @return 日报信息
     */
    DailyReportDTO selectDailyReportByUserIdAndDate(@Param("userId") Long userId, @Param("reportDate") LocalDate reportDate);
}