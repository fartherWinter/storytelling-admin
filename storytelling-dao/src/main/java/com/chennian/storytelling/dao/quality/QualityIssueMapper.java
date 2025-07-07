package com.chennian.storytelling.dao.quality;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.chennian.storytelling.bean.model.quality.QualityIssue;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.util.List;
import java.util.Map;

/**
 * 质量问题Mapper接口
 *
 * @author storytelling
 * @date 2024-01-01
 */
@Mapper
public interface QualityIssueMapper extends BaseMapper<QualityIssue> {

    /**
     * 根据产品编号查询质量问题
     *
     * @param productNo 产品编号
     * @return 质量问题列表
     */
    @Select("SELECT * FROM quality_issue WHERE product_no = #{productNo} ORDER BY create_time DESC")
    List<QualityIssue> selectByProductNo(@Param("productNo") String productNo);

    /**
     * 根据问题状态查询
     *
     * @param status 问题状态
     * @return 质量问题列表
     */
    @Select("SELECT * FROM quality_issue WHERE status = #{status} ORDER BY create_time DESC")
    List<QualityIssue> selectByStatus(@Param("status") Integer status);

    /**
     * 根据问题等级查询
     *
     * @param issueLevel 问题等级
     * @return 质量问题列表
     */
    @Select("SELECT * FROM quality_issue WHERE issue_level = #{issueLevel} ORDER BY create_time DESC")
    List<QualityIssue> selectByIssueLevel(@Param("issueLevel") Integer issueLevel);

    /**
     * 根据负责人查询问题
     *
     * @param handlerId 负责人ID
     * @return 质量问题列表
     */
    @Select("SELECT * FROM quality_issue WHERE handler_id = #{handlerId} ORDER BY create_time DESC")
    List<QualityIssue> selectByHandlerId(@Param("handlerId") Long handlerId);

    /**
     * 获取问题统计信息
     *
     * @param startDate 开始日期
     * @param endDate 结束日期
     * @param issueType 问题类型
     * @param issueLevel 问题等级
     * @return 统计结果
     */
    @Select("SELECT " +
            "COUNT(*) as totalIssues, " +
            "SUM(CASE WHEN status = 4 THEN 1 ELSE 0 END) as resolvedIssues, " +
            "SUM(CASE WHEN status IN (0, 1, 2, 3) THEN 1 ELSE 0 END) as pendingIssues, " +
            "SUM(CASE WHEN issue_level = 4 THEN 1 ELSE 0 END) as criticalIssues " +
            "FROM quality_issue " +
            "WHERE create_time BETWEEN #{startDate} AND #{endDate} " +
            "AND (#{issueType} IS NULL OR issue_type = #{issueType}) " +
            "AND (#{issueLevel} IS NULL OR issue_level = #{issueLevel})")
    Map<String, Object> getIssueStatistics(@Param("startDate") String startDate, 
                                          @Param("endDate") String endDate, 
                                          @Param("issueType") Integer issueType, 
                                          @Param("issueLevel") Integer issueLevel);

    /**
     * 获取问题趋势分析
     *
     * @param startDate 开始日期
     * @param endDate 结束日期
     * @return 趋势数据
     */
    @Select("SELECT " +
            "DATE_FORMAT(create_time, '%Y-%m') as month, " +
            "COUNT(*) as issueCount, " +
            "SUM(CASE WHEN status = 4 THEN 1 ELSE 0 END) as resolvedCount " +
            "FROM quality_issue " +
            "WHERE create_time BETWEEN #{startDate} AND #{endDate} " +
            "GROUP BY DATE_FORMAT(create_time, '%Y-%m') " +
            "ORDER BY month")
    List<Map<String, Object>> getIssueTrend(@Param("startDate") String startDate, 
                                           @Param("endDate") String endDate);

    /**
     * 根据批次号查询问题
     *
     * @param batchNo 批次号
     * @return 质量问题列表
     */
    @Select("SELECT * FROM quality_issue WHERE batch_no = #{batchNo} ORDER BY create_time DESC")
    List<QualityIssue> selectByBatchNo(@Param("batchNo") String batchNo);

    /**
     * 获取问题分布统计
     *
     * @param startDate 开始日期
     * @param endDate 结束日期
     * @return 分布统计
     */
    @Select("SELECT " +
            "issue_type, " +
            "issue_level, " +
            "COUNT(*) as count " +
            "FROM quality_issue " +
            "WHERE create_time BETWEEN #{startDate} AND #{endDate} " +
            "GROUP BY issue_type, issue_level " +
            "ORDER BY count DESC")
    List<Map<String, Object>> getIssueDistribution(@Param("startDate") String startDate, 
                                                   @Param("endDate") String endDate);

    /**
     * 获取超期未处理的问题
     *
     * @param days 超期天数
     * @return 超期问题列表
     */
    @Select("SELECT * FROM quality_issue " +
            "WHERE status IN (0, 1, 2, 3) " +
            "AND DATEDIFF(NOW(), create_time) > #{days} " +
            "ORDER BY create_time ASC")
    List<QualityIssue> selectOverdueIssues(@Param("days") Integer days);

    /**
     * 根据发现阶段统计问题
     *
     * @param startDate 开始日期
     * @param endDate 结束日期
     * @return 阶段统计
     */
    @Select("SELECT " +
            "discovery_stage, " +
            "COUNT(*) as count " +
            "FROM quality_issue " +
            "WHERE create_time BETWEEN #{startDate} AND #{endDate} " +
            "GROUP BY discovery_stage " +
            "ORDER BY count DESC")
    List<Map<String, Object>> getIssuesByDiscoveryStage(@Param("startDate") String startDate, 
                                                        @Param("endDate") String endDate);
}