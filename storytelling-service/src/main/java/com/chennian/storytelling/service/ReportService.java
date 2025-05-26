package com.chennian.storytelling.service;

import com.chennian.storytelling.bean.dto.ReportDTO;
import java.time.LocalDate;
import java.util.List;

/**
 * 报告服务接口，统一处理日报和周报
 */
public interface ReportService {
    /**
     * 创建报告（日报/周报）
     * @param reportDTO 报告信息
     * @return 创建的报告ID
     */
    Long createReport(ReportDTO reportDTO);

    /**
     * 更新报告
     * @param reportDTO 报告信息
     * @return 是否成功
     */
    boolean updateReport(ReportDTO reportDTO);

    /**
     * 删除报告
     * @param id 报告ID
     * @return 是否成功
     */
    boolean deleteReport(Long id);

    /**
     * 根据ID获取报告
     * @param id 报告ID
     * @return 报告信息
     */
    ReportDTO getReportById(Long id);

    /**
     * 根据用户ID和类型获取报告列表
     * @param userId 用户ID
     * @param type 类型（daily/weekly）
     * @return 报告列表
     */
    List<ReportDTO> listReportsByUserId(Long userId, String type);

    /**
     * 获取指定用户在指定日期的日报
     * @param userId 用户ID
     * @param reportDate 报告日期
     * @return 日报信息
     */
    ReportDTO getDailyReportByUserIdAndDate(Long userId, LocalDate reportDate);

    /**
     * 获取指定用户在指定周的周报
     * @param userId 用户ID
     * @param weekStartDate 周开始日期
     * @return 周报信息
     */
    ReportDTO getWeeklyReportByUserIdAndDateRange(Long userId, LocalDate weekStartDate);

    /**
     * 提交报告并发起审批流程
     * @param reportDTO 报告信息
     * @return 创建的报告ID
     */
    Long submitReportWithWorkflow(ReportDTO reportDTO);

    /**
     * 审批报告（领导操作）
     * @param reportId 报告ID
     * @param approve 是否同意
     * @param comment 审批意见
     * @return 是否成功
     */
    boolean approveReport(Long reportId, boolean approve, String comment);
}