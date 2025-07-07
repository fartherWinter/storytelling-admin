package com.chennian.storytelling.dao.manufacturing;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.chennian.storytelling.bean.model.manufacturing.Bom;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.MapKey;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;

/**
 * BOM物料清单Mapper接口
 * 
 * @author storytelling
 * @date 2024-01-01
 */
@Mapper
public interface BOMMapper extends BaseMapper<Bom> {
    
    /**
     * 更新BOM状态
     * 
     * @param bomId BOM ID
     * @param status BOM状态
     * @return 更新结果
     */
    int updateBomStatus(@Param("bomId") Long bomId, @Param("status") Integer status);
    
    /**
     * 根据产品ID查询BOM列表
     * 
     * @param productId 产品ID
     * @return BOM列表
     */
    List<Bom> selectByProductId(@Param("productId") Long productId);
    
    /**
     * 根据BOM状态查询BOM列表
     * 
     * @param status BOM状态
     * @return BOM列表
     */
    List<Bom> selectByStatus(@Param("status") Integer status);
    
    /**
     * 根据BOM类型查询BOM列表
     * 
     * @param bomType BOM类型
     * @return BOM列表
     */
    List<Bom> selectByBomType(@Param("bomType") Integer bomType);
    
    /**
     * 查询BOM统计信息
     * 
     * @param startDate 开始日期
     * @param endDate 结束日期
     * @param productId 产品ID（可选）
     * @return 统计信息
     */
    @MapKey("productId")
    Map<String, Object> selectBomStatistics(@Param("startDate") String startDate, 
                                            @Param("endDate") String endDate, 
                                            @Param("productId") Long productId);
    
    /**
     * 审核BOM
     * 
     * @param bomId BOM ID
     * @param reviewerId 审核人员ID
     * @param reviewerName 审核人员姓名
     * @param reviewTime 审核时间
     * @return 更新结果
     */
    int approveBom(@Param("bomId") Long bomId, 
                   @Param("reviewerId") Long reviewerId, 
                   @Param("reviewerName") String reviewerName, 
                   @Param("reviewTime") String reviewTime);
    
    /**
     * 发布BOM
     * 
     * @param bomId BOM ID
     * @param publisherId 发布人员ID
     * @param publisherName 发布人员姓名
     * @param publishTime 发布时间
     * @return 更新结果
     */
    int publishBom(@Param("bomId") Long bomId, 
                   @Param("publisherId") Long publisherId, 
                   @Param("publisherName") String publisherName, 
                   @Param("publishTime") String publishTime);
    
    /**
     * 冻结BOM
     * 
     * @param bomId BOM ID
     * @return 更新结果
     */
    int freezeBom(@Param("bomId") Long bomId);
    
    /**
     * 作废BOM
     * 
     * @param bomId BOM ID
     * @return 更新结果
     */
    int obsoleteBom(@Param("bomId") Long bomId);
    
    /**
     * 查询BOM版本历史
     * 
     * @param productId 产品ID
     * @param bomCode BOM编号
     * @return BOM版本历史
     */
    List<Bom> selectBomVersionHistory(@Param("productId") Long productId,
                                      @Param("bomCode") String bomCode);
    
    /**
     * 查询最新版本BOM
     * 
     * @param productId 产品ID
     * @param bomCode BOM编号
     * @return 最新版本BOM
     */
    Bom selectLatestBomVersion(@Param("productId") Long productId,
                               @Param("bomCode") String bomCode);
    
    /**
     * 查询有效BOM列表
     * 
     * @param productId 产品ID
     * @param effectiveDate 生效日期
     * @return 有效BOM列表
     */
    List<Bom> selectEffectiveBoms(@Param("productId") Long productId,
                                  @Param("effectiveDate") String effectiveDate);
    
    /**
     * 查询产品创新数据
     * 
     * @param startDate 开始日期
     * @param endDate 结束日期
     * @param productId 产品ID
     * @return 产品创新数据
     */
    @MapKey("productId")
    Map<String, Object> selectProductInnovationData(@Param("startDate") String startDate, 
                                                    @Param("endDate") String endDate, 
                                                    @Param("productId") Long productId);
}