package com.chennian.storytelling.dao.project;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.chennian.storytelling.bean.model.project.ProjectRisk;
import com.chennian.storytelling.bean.vo.mall.RiskCorrelationAnalysisVO;
import org.apache.ibatis.annotations.MapKey;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;


import java.util.List;
import java.util.Map;

/**
 * 项目风险管理Mapper接口
 *
 * @author storytelling
 * @date 2024-01-01
 */
@Mapper
public interface ProjectRiskMapper extends BaseMapper<ProjectRisk> {

    /**
     * 查询项目风险列表
     */
    IPage<ProjectRisk> selectRiskList(Page<ProjectRisk> page, @Param("risk") ProjectRisk risk);

    /**
     * 根据项目ID查询风险列表
     */
    @Select("SELECT * FROM project_risk WHERE project_id = #{projectId} AND del_flag = '0' ORDER BY risk_level DESC, create_time DESC")
    List<ProjectRisk> selectRisksByProjectId(@Param("projectId") Long projectId);

    /**
     * 根据风险名称查询风险
     */
    @Select("SELECT * FROM project_risk WHERE risk_name = #{riskName} AND project_id = #{projectId} AND del_flag = '0'")
    ProjectRisk selectByRiskName(@Param("projectId") Long projectId, @Param("riskName") String riskName);

    /**
     * 根据风险类型查询风险列表
     */
    @Select("SELECT * FROM project_risk WHERE risk_type = #{riskType} AND del_flag = '0'")
    IPage<ProjectRisk> selectByRiskType(Page<ProjectRisk> page, @Param("riskType") Integer riskType);

    /**
     * 根据风险状态查询风险列表
     */
    @Select("SELECT * FROM project_risk WHERE status = #{status} AND del_flag = '0'")
    IPage<ProjectRisk> selectByStatus(Page<ProjectRisk> page, @Param("status") Integer status);

    /**
     * 根据风险等级查询风险列表
     */
    @Select("SELECT * FROM project_risk WHERE risk_level = #{riskLevel} AND del_flag = '0'")
    IPage<ProjectRisk> selectByRiskLevel(Page<ProjectRisk> page, @Param("riskLevel") Integer riskLevel);

    /**
     * 根据负责人查询风险列表
     */
    @Select("SELECT * FROM project_risk WHERE owner_id = #{ownerId} AND del_flag = '0'")
    IPage<ProjectRisk> selectByOwnerId(Page<ProjectRisk> page, @Param("ownerId") Long ownerId);

    /**
     * 查询高风险项目风险
     */
    @Select("SELECT * FROM project_risk WHERE risk_level >= #{threshold} AND status IN (1, 2, 3) AND del_flag = '0' ORDER BY risk_level DESC")
    IPage<ProjectRisk> selectHighRisks(Page<ProjectRisk> page, @Param("threshold") Integer threshold);

    /**
     * 查询未处理的风险
     */
    @Select("SELECT * FROM project_risk WHERE status = 1 AND del_flag = '0' ORDER BY risk_level DESC, create_time ASC")
    IPage<ProjectRisk> selectUnhandledRisks(Page<ProjectRisk> page);

    /**
     * 查询超期未处理的风险
     */
    @Select("SELECT * FROM project_risk WHERE status = 1 AND expected_resolution_date < NOW() AND del_flag = '0'")
    IPage<ProjectRisk> selectOverdueRisks(Page<ProjectRisk> page);

    /**
     * 获取风险矩阵分析
     */
    @MapKey("riskLevel")
    Map<String, Object> selectRiskMatrixAnalysis(@Param("projectId") Long projectId);

    /**
     * 获取项目风险分析
     */
    @MapKey("riskType")
    Map<String, Object> selectProjectRiskAnalysis(@Param("projectId") Long projectId);

    /**
     * 统计风险数量按类型
     */
    @Select("SELECT risk_type, COUNT(*) as count FROM project_risk WHERE project_id = #{projectId} AND del_flag = '0' GROUP BY risk_type")
    @MapKey("riskType")
    Map<String, Object> selectRiskCountByType(@Param("projectId") Long projectId);

    /**
     * 统计风险数量按状态
     */
    @Select("SELECT status, COUNT(*) as count FROM project_risk WHERE project_id = #{projectId} AND del_flag = '0' GROUP BY status")
    @MapKey("status")
    Map<String, Object> selectRiskCountByStatus(@Param("projectId") Long projectId);

    /**
     * 统计风险数量按等级
     */
    @Select("SELECT CASE WHEN risk_level >= 15 THEN '高风险' WHEN risk_level >= 8 THEN '中风险' ELSE '低风险' END as level, COUNT(*) as count FROM project_risk WHERE project_id = #{projectId} AND del_flag = '0' GROUP BY CASE WHEN risk_level >= 15 THEN '高风险' WHEN risk_level >= 8 THEN '中风险' ELSE '低风险' END")
    @MapKey("riskLevel")
    Map<String, Object> selectRiskCountByLevel(@Param("projectId") Long projectId);

    /**
     * 统计风险数量按负责人
     */
    @Select("SELECT owner_name, COUNT(*) as count FROM project_risk WHERE project_id = #{projectId} AND del_flag = '0' GROUP BY owner_id, owner_name")
    @MapKey("ownerId")
    Map<String, Object> selectRiskCountByOwner(@Param("projectId") Long projectId);

    /**
     * 获取风险趋势分析
     */
    @MapKey("date")
    Map<String, Object> selectRiskTrendAnalysis(@Param("projectId") Long projectId, 
                                               @Param("startDate") String startDate, 
                                               @Param("endDate") String endDate, 
                                               @Param("period") String period);

    /**
     * 获取风险影响分析
     */
    @MapKey("impactType")
    Map<String, Object> selectRiskImpactAnalysis(@Param("projectId") Long projectId);

    /**
     * 获取风险应对效果分析
     */
    @MapKey("responseType")
    Map<String, Object> selectRiskResponseEffectivenessAnalysis(@Param("projectId") Long projectId);

    /**
     * 获取风险预警信息
     */
    @MapKey("riskId")
    List<Map<String, Object>> selectRiskWarnings(@Param("projectId") Long projectId);

    /**
     * 获取风险关联分析
     */
    List<RiskCorrelationAnalysisVO> selectRiskCorrelationAnalysis(@Param("projectId") Long projectId);

    /**
     * 检查风险名称是否存在
     */
    @Select("SELECT COUNT(*) FROM project_risk WHERE risk_name = #{riskName} AND project_id = #{projectId} AND del_flag = '0'")
    int checkRiskNameExists(@Param("projectId") Long projectId, @Param("riskName") String riskName);

    /**
     * 获取风险处理时效统计
     */
    @MapKey("riskId")
    Map<String, Object> selectRiskHandlingTimeStatistics(@Param("projectId") Long projectId);

    /**
     * 获取风险成本影响分析
     */
    @MapKey("riskId")
    Map<String, Object> selectRiskCostImpactAnalysis(@Param("projectId") Long projectId);

    /**
     * 获取风险概率分布
     */
    @MapKey("probabilityRange")
    Map<String, Object> selectRiskProbabilityDistribution(@Param("projectId") Long projectId);

    /**
     * 获取风险缓解策略效果
     */
    @MapKey("strategyType")
    Map<String, Object> selectRiskMitigationEffectiveness(@Param("projectId") Long projectId);

    /**
     * 获取所有项目风险统计
     */
    @MapKey("projectId")
    Map<String, Object> selectAllProjectsRisk(@Param("startDate") String startDate, 
                                              @Param("endDate") String endDate);
}