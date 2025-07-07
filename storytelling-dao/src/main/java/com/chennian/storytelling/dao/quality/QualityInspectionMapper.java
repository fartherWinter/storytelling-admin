package com.chennian.storytelling.dao.quality;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.chennian.storytelling.bean.model.quality.QualityInspection;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

/**
 * 质量检验Mapper接口
 *
 * @author storytelling
 * @date 2024-01-01
 */
@Mapper
public interface QualityInspectionMapper extends BaseMapper<QualityInspection> {

    /**
     * 根据产品ID查询检验记录
     *
     * @param productId 产品ID
     * @return 检验记录列表
     */
    @Select("SELECT * FROM quality_inspection WHERE product_no = #{productId} ORDER BY create_time DESC")
    List<QualityInspection> selectByProductId(@Param("productId") String productId);

    /**
     * 根据供应商查询检验记录
     *
     * @param supplier 供应商
     * @return 检验记录列表
     */
    @Select("SELECT * FROM quality_inspection WHERE supplier = #{supplier} ORDER BY create_time DESC")
    List<QualityInspection> selectBySupplier(@Param("supplier") String supplier);

    /**
     * 统计检验数据
     *
     * @param startDate 开始日期
     * @param endDate 结束日期
     * @param inspectionType 检验类型
     * @return 统计结果
     */
    @Select("SELECT " +
            "COUNT(*) as totalInspections, " +
            "SUM(qualified_quantity) as totalQualified, " +
            "SUM(unqualified_quantity) as totalUnqualified, " +
            "ROUND(SUM(qualified_quantity) / (SUM(qualified_quantity) + SUM(unqualified_quantity)) * 100, 2) as qualificationRate " +
            "FROM quality_inspection " +
            "WHERE create_time BETWEEN #{startDate} AND #{endDate} " +
            "AND (#{inspectionType} IS NULL OR inspection_type = #{inspectionType})")
    Map<String, Object> getInspectionStatistics(@Param("startDate") String startDate, 
                                               @Param("endDate") String endDate, 
                                               @Param("inspectionType") Integer inspectionType);

    /**
     * 获取合格率趋势
     *
     * @param startDate 开始日期
     * @param endDate 结束日期
     * @param productId 产品ID
     * @return 趋势数据
     */
    @Select("SELECT " +
            "DATE_FORMAT(create_time, '%Y-%m') as month, " +
            "ROUND(SUM(qualified_quantity) / (SUM(qualified_quantity) + SUM(unqualified_quantity)) * 100, 2) as rate " +
            "FROM quality_inspection " +
            "WHERE create_time BETWEEN #{startDate} AND #{endDate} " +
            "AND (#{productId} IS NULL OR product_no = #{productId}) " +
            "GROUP BY DATE_FORMAT(create_time, '%Y-%m') " +
            "ORDER BY month")
    List<Map<String, Object>> getQualificationRateTrend(@Param("startDate") String startDate, 
                                                        @Param("endDate") String endDate, 
                                                        @Param("productId") String productId);


    /**
     * 根据批次号查询检验记录
     *
     * @param batchNo 批次号
     * @return 检验记录列表
     */
    @Select("SELECT * FROM quality_inspection WHERE batch_no = #{batchNo} ORDER BY create_time DESC")
    List<QualityInspection> selectByBatchNo(@Param("batchNo") String batchNo);

    /**
     * 获取产品合格率分析
     *
     * @param startDate 开始日期
     * @param endDate 结束日期
     * @param productId 产品ID
     * @param supplierId 供应商ID
     * @return 分析结果
     */
    @Select("SELECT " +
            "product_name, " +
            "supplier, " +
            "COUNT(*) as inspectionCount, " +
            "SUM(qualified_quantity) as qualifiedTotal, " +
            "SUM(unqualified_quantity) as unqualifiedTotal, " +
            "ROUND(SUM(qualified_quantity) / (SUM(qualified_quantity) + SUM(unqualified_quantity)) * 100, 2) as passRate " +
            "FROM quality_inspection " +
            "WHERE create_time BETWEEN #{startDate} AND #{endDate} " +
            "AND (#{productId} IS NULL OR product_no = #{productId}) " +
            "AND (#{supplierId} IS NULL OR supplier = #{supplierId}) " +
            "GROUP BY product_name, supplier " +
            "ORDER BY passRate DESC")
    List<Map<String, Object>> getPassRateAnalysis(@Param("startDate") String startDate, 
                                                  @Param("endDate") String endDate, 
                                                  @Param("productId") String productId, 
                                                  @Param("supplierId") String supplierId);
}