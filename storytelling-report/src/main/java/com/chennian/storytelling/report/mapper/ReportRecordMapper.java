package com.chennian.storytelling.report.mapper;

import com.chennian.storytelling.bean.model.ReportRecord;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 报表记录数据访问层
 *
 * @author chennian
 */
@Mapper
public interface ReportRecordMapper {

    /**
     * 查询报表记录列表
     *
     * @param reportName 报表名称
     * @param templateId 模板ID
     * @param status     状态
     * @return 报表记录列表
     */
    List<ReportRecord> selectReportRecordList(@Param("reportName") String reportName,
                                             @Param("templateId") Long templateId,
                                             @Param("status") String status);

    /**
     * 根据ID查询报表记录
     *
     * @param id 记录ID
     * @return 报表记录
     */
    ReportRecord selectReportRecordById(Long id);

    /**
     * 新增报表记录
     *
     * @param record 报表记录
     * @return 影响行数
     */
    int insertReportRecord(ReportRecord record);

    /**
     * 修改报表记录
     *
     * @param record 报表记录
     * @return 影响行数
     */
    int updateReportRecord(ReportRecord record);

    /**
     * 删除报表记录
     *
     * @param id 记录ID
     * @return 影响行数
     */
    int deleteReportRecordById(Long id);

    /**
     * 批量删除报表记录
     *
     * @param ids 需要删除的记录ID数组
     * @return 影响行数
     */
    int deleteReportRecordByIds(Long[] ids);
    
    /**
     * 清理过期的报表记录
     *
     * @return 影响行数
     */
    int cleanExpiredReports();
}