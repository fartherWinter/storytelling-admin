package com.chennian.storytelling.dao.manufacturing;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.chennian.storytelling.bean.model.manufacturing.BOMItem;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.MapKey;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;

/**
 * BOM明细Mapper接口
 * 
 * @author storytelling
 * @date 2024-01-01
 */
@Mapper
public interface BOMItemMapper extends BaseMapper<BOMItem> {
    
    /**
     * 根据BOM ID查询BOM明细列表
     * 
     * @param bomId BOM ID
     * @return BOM明细列表
     */
    List<BOMItem> selectByBomId(@Param("bomId") Long bomId);
    
    /**
     * 根据物料ID查询BOM明细列表
     * 
     * @param materialId 物料ID
     * @return BOM明细列表
     */
    List<BOMItem> selectByMaterialId(@Param("materialId") Long materialId);
    
    /**
     * 根据物料类型查询BOM明细列表
     * 
     * @param bomId BOM ID
     * @param materialType 物料类型
     * @return BOM明细列表
     */
    List<BOMItem> selectByMaterialType(@Param("bomId") Long bomId, 
                                      @Param("materialType") Integer materialType);
    
    /**
     * 查询成本分析数据
     * 
     * @param startDate 开始日期
     * @param endDate 结束日期
     * @param bomId BOM ID
     * @return 成本分析数据列表
     */
    @MapKey("id")
    List<Map<String, Object>> selectCostAnalysisData(@Param("startDate") String startDate, 
                                                     @Param("endDate") String endDate, 
                                                     @Param("bomId") Long bomId);
    
    /**
     * 查询关键物料列表
     * 
     * @param bomId BOM ID
     * @return 关键物料列表
     */
    List<BOMItem> selectKeyMaterials(@Param("bomId") Long bomId);
    
    /**
     * 查询可选物料列表
     * 
     * @param bomId BOM ID
     * @return 可选物料列表
     */
    List<BOMItem> selectOptionalMaterials(@Param("bomId") Long bomId);
    
    /**
     * 根据工序查询BOM明细列表
     * 
     * @param bomId BOM ID
     * @param processCode 工序编号
     * @return BOM明细列表
     */
    List<BOMItem> selectByProcess(@Param("bomId") Long bomId, 
                                 @Param("processCode") String processCode);
    
    /**
     * 查询BOM项目统计信息
     * 
     * @param bomId BOM ID
     * @return BOM项目统计信息
     */
    @MapKey("bomId")
    Map<String, Object> selectBomItemStatistics(@Param("bomId") Long bomId);
    
    /**
     * 查询物料使用统计
     * 
     * @param startDate 开始日期
     * @param endDate 结束日期
     * @param materialId 物料ID
     * @return 物料使用统计
     */
    @MapKey("materialId")
    Map<String, Object> selectMaterialUsageStatistics(@Param("startDate") String startDate, 
                                                      @Param("endDate") String endDate, 
                                                      @Param("materialId") Long materialId);
    
    /**
     * 查询物料成本统计
     * 
     * @param bomId BOM ID
     * @return 成本统计
     */
    Map<String, Object> selectMaterialCostStatistics(@Param("bomId") Long bomId);
    
    /**
     * 批量删除BOM明细
     * 
     * @param bomId BOM ID
     * @return 删除结果
     */
    int deleteByBomId(@Param("bomId") Long bomId);
    
    /**
     * 更新物料用量
     * 
     * @param bomItemId BOM明细ID
     * @param requiredQuantity 需求数量
     * @param actualUsage 实际用量
     * @return 更新结果
     */
    int updateMaterialUsage(@Param("bomItemId") Long bomItemId, 
                           @Param("requiredQuantity") String requiredQuantity, 
                           @Param("actualUsage") String actualUsage);
    
    /**
     * 更新物料成本
     * 
     * @param bomItemId BOM明细ID
     * @param standardCost 标准成本
     * @param currentCost 当前成本
     * @return 更新结果
     */
    int updateMaterialCost(@Param("bomItemId") Long bomItemId, 
                          @Param("standardCost") String standardCost, 
                          @Param("currentCost") String currentCost);
    
    /**
     * 查询替代物料列表
     * 
     * @param materialId 原物料ID
     * @return 替代物料列表
     */
    List<BOMItem> selectSubstituteMaterials(@Param("materialId") Long materialId);
    
    /**
     * 查询物料库存需求
     * 
     * @param bomId BOM ID
     * @param productionQuantity 生产数量
     * @return 库存需求
     */
    @MapKey("materialId")
    List<Map<String, Object>> selectMaterialStockRequirement(@Param("bomId") Long bomId, 
                                                             @Param("productionQuantity") Integer productionQuantity);
    
    /**
     * 查询成本统计数据
     * 
     * @param startDate 开始日期
     * @param endDate 结束日期
     * @param productId 产品ID
     * @return 成本统计数据
     */
    @MapKey("month")
    Map<String, Object> selectCostStatistics(@Param("startDate") String startDate, 
                                             @Param("endDate") String endDate, 
                                             @Param("productId") Long productId);
    
    /**
     * 查询物料消耗数据
     * 
     * @param startDate 开始日期
     * @param endDate 结束日期
     * @param materialId 物料ID
     * @return 物料消耗数据
     */
    @MapKey("materialId")
    Map<String, Object> selectMaterialConsumption(@Param("startDate") String startDate, 
                                                  @Param("endDate") String endDate, 
                                                  @Param("materialId") Long materialId);
    
    /**
     * 查询库存周转数据
     * 
     * @param startDate 开始日期
     * @param endDate 结束日期
     * @param productId 产品ID
     * @return 库存周转数据
     */
    @MapKey("materialId")
    Map<String, Object> selectInventoryTurnover(@Param("startDate") String startDate, 
                                                @Param("endDate") String endDate, 
                                                @Param("productId") Long productId);
    
    /**
     * 查询供应商绩效数据
     * 
     * @param startDate 开始日期
     * @param endDate 结束日期
     * @param supplierId 供应商ID
     * @return 供应商绩效数据
     */
    @MapKey("supplierId")
    Map<String, Object> selectSupplierPerformance(@Param("startDate") String startDate, 
                                                  @Param("endDate") String endDate, 
                                                  @Param("supplierId") Long supplierId);
}