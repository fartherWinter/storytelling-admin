package com.chennian.storytelling.dao.quality;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.chennian.storytelling.bean.model.quality.QualityImprovement;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.util.List;
import java.util.Map;

/**
 * 质量改进Mapper接口
 *
 * @author storytelling
 * @date 2024-01-01
 */
@Mapper
public interface QualityImprovementMapper extends BaseMapper<QualityImprovement> {

    /**
     * 根据改进类型查询
     *
     * @param improvementType 改进类型
     * @return 质量改进列表
     */
    @Select("SELECT * FROM quality_improvement WHERE improvement_type = #{improvementType} ORDER BY create_time DESC")
    List<QualityImprovement> selectByImprovementType(@Param("improvementType") Integer improvementType);

    /**
     * 根据状态查询
     *
     * @param status 状态
     * @return 质量改进列表
     */
    @Select("SELECT * FROM quality_improvement WHERE status = #{status} ORDER BY create_time DESC")
    List<QualityImprovement> selectByStatus(@Param("status") Integer status);

    /**
     * 根据优先级查询
     *
     * @param priority 优先级
     * @return 质量改进列表
     */
    @Select("SELECT * FROM quality_improvement WHERE priority = #{priority} ORDER BY create_time DESC")
    List<QualityImprovement> selectByPriority(@Param("priority") Integer priority);

    /**
     * 根据负责人查询
     *
     * @param responsiblePersonId 负责人ID
     * @return 质量改进列表
     */
    @Select("SELECT * FROM quality_improvement WHERE responsible_person_id = #{responsiblePersonId} ORDER BY create_time DESC")
    List<QualityImprovement> selectByResponsiblePersonId(@Param("responsiblePersonId") Long responsiblePersonId);

    /**
     * 获取改进项目统计
     *
     * @param startDate 开始日期
     * @param endDate 结束日期
     * @param improvementType 改进类型
     * @return 统计结果
     */
    @Select("SELECT " +
            "COUNT(*) as totalImprovements, " +
            "SUM(CASE WHEN status = 3 THEN 1 ELSE 0 END) as completedImprovements, " +
            "SUM(CASE WHEN status IN (0, 1, 2) THEN 1 ELSE 0 END) as ongoingImprovements, " +
            "SUM(CASE WHEN status = 4 THEN 1 ELSE 0 END) as cancelledImprovements " +
            "FROM quality_improvement " +
            "WHERE create_time BETWEEN #{startDate} AND #{endDate} " +
            "AND (#{improvementType} IS NULL OR improvement_type = #{improvementType})")
    Map<String, Object> getImprovementStatistics(@Param("startDate") String startDate, 
                                                 @Param("endDate") String endDate, 
                                                 @Param("improvementType") Integer improvementType);

    /**
     * 获取改进趋势分析
     *
     * @param startDate 开始日期
     * @param endDate 结束日期
     * @return 趋势数据
     */
    @Select("SELECT " +
            "DATE_FORMAT(create_time, '%Y-%m') as month, " +
            "COUNT(*) as improvementCount, " +
            "SUM(CASE WHEN status = 3 THEN 1 ELSE 0 END) as completedCount " +
            "FROM quality_improvement " +
            "WHERE create_time BETWEEN #{startDate} AND #{endDate} " +
            "GROUP BY DATE_FORMAT(create_time, '%Y-%m') " +
            "ORDER BY month")
    List<Map<String, Object>> getImprovementTrend(@Param("startDate") String startDate, 
                                                  @Param("endDate") String endDate);

    /**
     * 根据改进类型统计
     *
     * @param startDate 开始日期
     * @param endDate 结束日期
     * @return 类型统计
     */
    @Select("SELECT " +
            "improvement_type, " +
            "COUNT(*) as count, " +
            "SUM(CASE WHEN status = 3 THEN 1 ELSE 0 END) as completedCount " +
            "FROM quality_improvement " +
            "WHERE create_time BETWEEN #{startDate} AND #{endDate} " +
            "GROUP BY improvement_type " +
            "ORDER BY count DESC")
    List<Map<String, Object>> getImprovementByType(@Param("startDate") String startDate, 
                                                   @Param("endDate") String endDate);

    /**
     * 获取超期项目
     *
     * @param days 超期天数
     * @return 超期项目列表
     */
    @Select("SELECT * FROM quality_improvement " +
            "WHERE status IN (0, 1, 2) " +
            "AND planned_end_date IS NOT NULL " +
            "AND DATEDIFF(NOW(), planned_end_date) > #{days} " +
            "ORDER BY planned_end_date ASC")
    List<QualityImprovement> selectOverdueImprovements(@Param("days") Integer days);

    /**
     * 根据预期效果查询
     *
     * @param expectedEffect 预期效果关键词
     * @return 质量改进列表
     */
    @Select("SELECT * FROM quality_improvement WHERE expected_effect LIKE CONCAT('%', #{expectedEffect}, '%')")
    List<QualityImprovement> selectByExpectedEffect(@Param("expectedEffect") String expectedEffect);

    /**
     * 获取高优先级项目
     *
     * @return 高优先级项目列表
     */
    @Select("SELECT * FROM quality_improvement WHERE priority >= 3 AND status IN (0, 1, 2) ORDER BY priority DESC, create_time ASC")
    List<QualityImprovement> selectHighPriorityImprovements();

    /**
     * 根据实施阶段统计
     *
     * @param startDate 开始日期
     * @param endDate 结束日期
     * @return 阶段统计
     */
    @Select("SELECT " +
            "status, " +
            "COUNT(*) as count " +
            "FROM quality_improvement " +
            "WHERE create_time BETWEEN #{startDate} AND #{endDate} " +
            "GROUP BY status " +
            "ORDER BY status")
    List<Map<String, Object>> getImprovementByStatus(@Param("startDate") String startDate, 
                                                     @Param("endDate") String endDate);
}