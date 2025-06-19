package com.chennian.storytelling.dao.workflow;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.chennian.storytelling.bean.model.workflow.WfReportConfig;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

/**
 * 工作流报表配置Mapper接口
 * 
 * @author storytelling
 * @date 2024-01-01
 */
@Mapper
public interface WfReportConfigMapper extends BaseMapper<WfReportConfig> {

    /**
     * 根据报表编码查询报表配置
     * 
     * @param reportCode 报表编码
     * @return 报表配置
     */
    WfReportConfig selectByReportCode(@Param("reportCode") String reportCode);

    /**
     * 根据报表类型查询报表配置列表
     * 
     * @param reportType 报表类型
     * @return 报表配置列表
     */
    List<WfReportConfig> selectByReportType(@Param("reportType") String reportType);

    /**
     * 根据启用状态查询报表配置列表
     * 
     * @param enabled 是否启用
     * @return 报表配置列表
     */
    List<WfReportConfig> selectByEnabled(@Param("enabled") Integer enabled);

    /**
     * 根据报表名称模糊查询
     * 
     * @param reportName 报表名称
     * @return 报表配置列表
     */
    List<WfReportConfig> selectByReportNameLike(@Param("reportName") String reportName);

    /**
     * 检查报表编码是否存在
     * 
     * @param reportCode 报表编码
     * @param excludeId 排除的报表ID
     * @return 存在数量
     */
    int checkReportCodeExists(@Param("reportCode") String reportCode, 
                             @Param("excludeId") String excludeId);

    /**
     * 检查报表名称是否存在
     * 
     * @param reportName 报表名称
     * @param excludeId 排除的报表ID
     * @return 存在数量
     */
    int checkReportNameExists(@Param("reportName") String reportName, 
                             @Param("excludeId") String excludeId);

    /**
     * 批量更新报表状态
     * 
     * @param reportIds 报表ID列表
     * @param enabled 启用状态
     * @param updatedBy 更新人
     * @return 更新行数
     */
    int batchUpdateReportStatus(@Param("reportIds") List<String> reportIds, 
                               @Param("enabled") Integer enabled, 
                               @Param("updatedBy") String updatedBy);

    /**
     * 查询所有报表类型
     * 
     * @return 报表类型列表
     */
    List<String> selectAllReportTypes();

    /**
     * 查询报表配置统计信息
     * 
     * @return 统计信息
     */
    List<java.util.Map<String, Object>> selectReportStatistics();

    /**
     * 查询报表配置详情
     * 
     * @param reportId 报表ID
     * @return 报表配置
     */
    WfReportConfig selectReportDetail(@Param("reportId") String reportId);

    /**
     * 根据报表ID列表批量查询报表配置
     * 
     * @param reportIds 报表ID列表
     * @return 报表配置列表
     */
    List<WfReportConfig> selectByReportIds(@Param("reportIds") List<String> reportIds);

    /**
     * 查询报表执行历史
     * 
     * @param reportId 报表ID
     * @param startTime 开始时间
     * @param endTime 结束时间
     * @return 执行历史
     */
    List<java.util.Map<String, Object>> selectReportExecutionHistory(@Param("reportId") String reportId,
                                                                     @Param("startTime") java.util.Date startTime,
                                                                     @Param("endTime") java.util.Date endTime);

    /**
     * 查询报表数据源配置
     * 
     * @param reportId 报表ID
     * @return 数据源配置
     */
    java.util.Map<String, Object> selectReportDataSource(@Param("reportId") String reportId);

    /**
     * 更新报表最后执行时间
     * 
     * @param reportId 报表ID
     * @param lastExecuteTime 最后执行时间
     * @return 更新行数
     */
    int updateLastExecuteTime(@Param("reportId") String reportId, 
                             @Param("lastExecuteTime") java.util.Date lastExecuteTime);

    /**
     * 根据报表类型统计报表数量
     * 
     * @return 统计信息
     */
    List<Map<String, Object>> selectReportCountByType();

    /**
     * 查询最近使用的报表配置
     * 
     * @param limit 限制数量
     * @return 报表配置列表
     */
    List<WfReportConfig> selectRecentlyUsedReports(@Param("limit") Integer limit);

    /**
     * 查询热门报表配置
     * 
     * @param limit 限制数量
     * @return 报表配置列表
     */
    List<WfReportConfig> selectPopularReports(@Param("limit") Integer limit);

    /**
     * 更新报表使用次数
     * 
     * @param reportId 报表ID
     * @return 更新行数
     */
    int updateReportUsageCount(@Param("reportId") String reportId);
}