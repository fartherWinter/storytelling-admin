package com.chennian.storytelling.dao;

import com.chennian.storytelling.bean.dto.ReportDTO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.time.LocalDate;
import java.util.List;

/**
 * 报告Mapper，统一处理日报和周报
 */
@Mapper
public interface ReportMapper {
    /**
     * 插入报告
     */
    int insertReport(ReportDTO report);

    /**
     * 更新报告
     */
    int updateReport(ReportDTO report);

    /**
     * 根据ID删除报告
     */
    int deleteReportById(@Param("id") Long id);

    /**
     * 根据ID获取报告
     */
    ReportDTO selectReportById(@Param("id") Long id);

    /**
     * 根据用户ID和类型获取报告列表
     */
    List<ReportDTO> listReportsByUserId(@Param("userId") Long userId, @Param("type") String type);

    /**
     * 获取指定用户在指定日期的日报
     */
    ReportDTO selectDailyReportByUserIdAndDate(@Param("userId") Long userId, @Param("reportDate") LocalDate reportDate);

    /**
     * 获取指定用户在指定周的周报
     */
    ReportDTO selectWeeklyReportByUserIdAndDateRange(@Param("userId") Long userId, @Param("weekStartDate") LocalDate weekStartDate);
}