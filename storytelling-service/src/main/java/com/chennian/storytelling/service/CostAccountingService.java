package com.chennian.storytelling.service;

import com.chennian.storytelling.vo.ActivityBasedCostAnalysisVO;
import com.chennian.storytelling.vo.CostCenterAnalysisVO;
import com.chennian.storytelling.vo.ProductCostAnalysisVO;

/**
 * 成本会计服务接口
 * 负责成本中心分析、产品成本分析等功能
 * @author chen
 * @date 2023/6/15
 */
public interface CostAccountingService {
    
    /**
     * 获取成本中心分析
     * @param period 期间
     * @param costCenterId 成本中心ID
     * @return 成本中心分析数据
     */
    CostCenterAnalysisVO getCostCenterAnalysis(String period, Long costCenterId);
    
    /**
     * 获取产品成本分析
     * @param period 期间
     * @param productId 产品ID
     * @return 产品成本分析数据
     */
    ProductCostAnalysisVO getProductCostAnalysis(String period, Long productId);
    
    /**
     * 获取基于活动的成本分析
     * @param period 期间
     * @return 基于活动的成本分析数据
     */
    ActivityBasedCostAnalysisVO getActivityBasedCostAnalysis(String period);
}