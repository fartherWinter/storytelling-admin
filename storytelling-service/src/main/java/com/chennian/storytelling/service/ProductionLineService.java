package com.chennian.storytelling.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.chennian.storytelling.bean.model.manufacturing.ProductionLine;
import com.chennian.storytelling.common.utils.PageParam;
import org.springframework.transaction.annotation.Transactional;

import java.util.Map;

/**
 * 生产线管理服务接口
 * 
 * @author storytelling
 * @date 2024-01-01
 */
public interface ProductionLineService {

    /**
     * 查询生产线列表
     */
    IPage<ProductionLine> getProductionLineList(PageParam<ProductionLine> page, ProductionLine line);
    
    /**
     * 根据ID查询生产线信息
     */
    ProductionLine getProductionLineById(Long id);
    
    /**
     * 新增生产线
     */
    void addProductionLine(ProductionLine line);
    
    /**
     * 修改生产线信息
     */
    void updateProductionLine(ProductionLine line);
    
    /**
     * 删除生产线
     */
    void deleteProductionLine(Long[] ids);
    
    /**
     * 生产线状态变更
     */
    void changeProductionLineStatus(Long lineId, Integer status, String reason);

    /**
     * 删除生产线（单个）
     */
    @Transactional
    void deleteProductionLine(Long id);

    /**
     * 生产线状态变更（简化版）
     */
    @Transactional
    void changeProductionLineStatus(Long id, String status);

    /**
     * 生产线产能分析
     */
    Map<String, Object> getProductionLineCapacityAnalysis(Long lineId, String startDate, String endDate);
}