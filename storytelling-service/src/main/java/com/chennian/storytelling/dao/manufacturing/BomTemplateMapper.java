package com.chennian.storytelling.dao.manufacturing;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.chennian.storytelling.bean.model.manufacturing.BomTemplate;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.MapKey;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;

/**
 * BOM模板Mapper接口
 * 
 * @author storytelling
 * @date 2024-01-01
 */
@Mapper
public interface BomTemplateMapper extends BaseMapper<BomTemplate> {
    
    /**
     * 更新BOM模板状态
     * 
     * @param templateId 模板ID
     * @param status 状态
     * @return 更新结果
     */
    int updateTemplateStatus(@Param("templateId") Long templateId, @Param("status") Integer status);
    
    /**
     * 根据产品ID查询BOM模板列表
     * 
     * @param productId 产品ID
     * @return BOM模板列表
     */
    List<BomTemplate> selectByProductId(@Param("productId") Long productId);
    
    /**
     * 根据产品编号查询BOM模板列表
     * 
     * @param productCode 产品编号
     * @return BOM模板列表
     */
    List<BomTemplate> selectByProductCode(@Param("productCode") String productCode);
    
    /**
     * 根据模板状态查询BOM模板列表
     * 
     * @param status 状态
     * @return BOM模板列表
     */
    List<BomTemplate> selectByStatus(@Param("status") Integer status);
    
    /**
     * 查询BOM模板统计信息
     * 
     * @param startDate 开始日期
     * @param endDate 结束日期
     * @param productId 产品ID（可选）
     * @return 统计信息
     */
    @MapKey("templateId")
    Map<String, Object> selectTemplateStatistics(@Param("startDate") String startDate, 
                                                 @Param("endDate") String endDate, 
                                                 @Param("productId") Long productId);
    
    /**
     * 审核BOM模板
     * 
     * @param templateId 模板ID
     * @param reviewerId 审核人员ID
     * @param reviewerName 审核人员姓名
     * @param reviewTime 审核时间
     * @return 更新结果
     */
    int approveTemplate(@Param("templateId") Long templateId, 
                       @Param("reviewerId") Long reviewerId, 
                       @Param("reviewerName") String reviewerName, 
                       @Param("reviewTime") String reviewTime);
    
    /**
     * 发布BOM模板
     * 
     * @param templateId 模板ID
     * @param publisherId 发布人员ID
     * @param publisherName 发布人员姓名
     * @param publishTime 发布时间
     * @return 更新结果
     */
    int publishTemplate(@Param("templateId") Long templateId, 
                       @Param("publisherId") Long publisherId, 
                       @Param("publisherName") String publisherName, 
                       @Param("publishTime") String publishTime);
    
    /**
     * 冻结BOM模板
     * 
     * @param templateId 模板ID
     * @return 更新结果
     */
    int freezeTemplate(@Param("templateId") Long templateId);
    
    /**
     * 作废BOM模板
     * 
     * @param templateId 模板ID
     * @return 更新结果
     */
    int obsoleteTemplate(@Param("templateId") Long templateId);
    
    /**
     * 复制BOM模板
     * 
     * @param sourceTemplateId 源模板ID
     * @param newBomCode 新BOM编号
     * @param newBomName 新BOM名称
     * @param creatorId 创建人员ID
     * @param creatorName 创建人员姓名
     * @return 新模板ID
     */
    Long copyTemplate(@Param("sourceTemplateId") Long sourceTemplateId, 
                     @Param("newBomCode") String newBomCode, 
                     @Param("newBomName") String newBomName, 
                     @Param("creatorId") Long creatorId, 
                     @Param("creatorName") String creatorName);
    
    /**
     * 查询模板使用情况
     * 
     * @param templateId 模板ID
     * @return 使用情况
     */
    Map<String, Object> selectTemplateUsage(@Param("templateId") Long templateId);
    
    /**
     * 查询热门模板
     * 
     * @param limit 限制数量
     * @return 热门模板列表
     */
    List<BomTemplate> selectPopularTemplates(@Param("limit") Integer limit);
    
    /**
     * 查询最近使用的模板
     * 
     * @param userId 用户ID
     * @param limit 限制数量
     * @return 最近使用的模板列表
     */
    List<BomTemplate> selectRecentlyUsedTemplates(@Param("userId") Long userId, 
                                                  @Param("limit") Integer limit);
}