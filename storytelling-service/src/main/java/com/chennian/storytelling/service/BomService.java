package com.chennian.storytelling.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.chennian.storytelling.bean.model.manufacturing.Bom;
import com.chennian.storytelling.common.utils.PageParam;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Map;

/**
 * BOM管理服务接口
 * 
 * @author storytelling
 * @date 2024-01-01
 */
public interface BomService {

    /**
     * 查询BOM列表
     */
    IPage<Bom> getBomList(PageParam<Bom> page, Bom bom);
    
    /**
     * 根据ID查询BOM信息
     */
    Bom getBomById(Long id);
    
    /**
     * 新增BOM
     */
    void addBom(Bom bom);
    
    /**
     * 修改BOM信息
     */
    void updateBom(Bom bom);
    
    /**
     * 删除BOM
     */
    void deleteBom(Long[] ids);
    
    /**
     * BOM审核
     */
    void auditBom(Long bomId, Integer auditStatus, String auditRemark);

    /**
     * 删除BOM（单个）
     */
    @Transactional
    void deleteBom(Long id);

    /**
     * BOM审核（简化版）
     */
    @Transactional
    void auditBom(Long id, String status);

    /**
     * BOM成本计算
     */
    Map<String, Object> calculateBomCost(Long bomId, String startDate, String endDate);
    
    /**
     * 根据产品查询BOM树
     */
    List<Map<String, Object>> getBomTreeByProduct(Long productId);

    /**
     * BOM版本管理
     */
    void createBomVersion(Long bomId, String version, String description);

    /**
     * BOM版本管理（简化版）
     */
    @Transactional
    void createBomVersion(Long id, String version);
}