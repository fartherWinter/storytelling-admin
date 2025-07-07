package com.chennian.storytelling.dao.quality;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.chennian.storytelling.bean.model.quality.QualityStandard;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.util.List;
import java.util.Map;

/**
 * 质量标准Mapper接口
 *
 * @author storytelling
 * @date 2024-01-01
 */
@Mapper
public interface QualityStandardMapper extends BaseMapper<QualityStandard> {

    /**
     * 根据产品ID查询质量标准
     *
     * @param productId 产品ID
     * @return 质量标准列表
     */
    @Select("SELECT * FROM quality_standard WHERE product_category = #{productId} AND status = 1 ORDER BY create_time DESC")
    List<QualityStandard> selectByProductId(@Param("productId") Long productId);

    /**
     * 根据标准类型查询
     *
     * @param standardType 标准类型
     * @return 质量标准列表
     */
    @Select("SELECT * FROM quality_standard WHERE standard_type = #{standardType} AND status = 1 ORDER BY create_time DESC")
    List<QualityStandard> selectByStandardType(@Param("standardType") Integer standardType);

    /**
     * 根据标准编号查询
     *
     * @param standardNo 标准编号
     * @return 质量标准
     */
    @Select("SELECT * FROM quality_standard WHERE standard_no = #{standardNo}")
    QualityStandard selectByStandardNo(@Param("standardNo") String standardNo);

    /**
     * 查询生效的标准
     *
     * @return 生效标准列表
     */
    @Select("SELECT * FROM quality_standard WHERE status = 1 ORDER BY create_time DESC")
    List<QualityStandard> selectEffectiveStandards();

    /**
     * 根据产品类别查询标准
     *
     * @param productCategory 产品类别
     * @return 质量标准列表
     */
    @Select("SELECT * FROM quality_standard WHERE product_category = #{productCategory} AND status = 1 ORDER BY version DESC")
    List<QualityStandard> selectByProductCategory(@Param("productCategory") String productCategory);

    /**
     * 获取标准统计信息
     *
     * @return 统计结果
     */
    @Select("SELECT " +
            "standard_type, " +
            "COUNT(*) as count, " +
            "SUM(CASE WHEN status = 1 THEN 1 ELSE 0 END) as effectiveCount " +
            "FROM quality_standard " +
            "GROUP BY standard_type")
    List<Map<String, Object>> getStandardStatistics();

    /**
     * 查询即将到期的标准
     *
     * @param days 天数
     * @return 即将到期的标准列表
     */
    @Select("SELECT * FROM quality_standard " +
            "WHERE status = 1 " +
            "AND expiry_date IS NOT NULL " +
            "AND DATEDIFF(expiry_date, NOW()) <= #{days} " +
            "AND DATEDIFF(expiry_date, NOW()) >= 0 " +
            "ORDER BY expiry_date ASC")
    List<QualityStandard> selectExpiringStandards(@Param("days") Integer days);

    /**
     * 根据适用范围查询标准
     *
     * @param applicableScope 适用范围
     * @return 质量标准列表
     */
    @Select("SELECT * FROM quality_standard WHERE applicable_scope LIKE CONCAT('%', #{applicableScope}, '%') AND status = 1")
    List<QualityStandard> selectByApplicableScope(@Param("applicableScope") String applicableScope);
}