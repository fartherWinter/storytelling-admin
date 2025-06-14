package com.chennian.storytelling.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.chennian.storytelling.bean.model.manufacturing.ProductionPlan;
import com.chennian.storytelling.common.utils.PageParam;
import org.springframework.transaction.annotation.Transactional;

/**
 * 生产计划管理服务接口
 * 
 * @author storytelling
 * @date 2024-01-01
 */
public interface ProductionPlanService {

    /**
     * 查询生产计划列表
     */
    IPage<ProductionPlan> getProductionPlanList(PageParam<ProductionPlan> page, ProductionPlan plan);
    
    /**
     * 根据ID查询生产计划信息
     */
    ProductionPlan getProductionPlanById(Long id);
    
    /**
     * 新增生产计划
     */
    void addProductionPlan(ProductionPlan plan);
    
    /**
     * 修改生产计划信息
     */
    void updateProductionPlan(ProductionPlan plan);
    
    /**
     * 删除生产计划
     */
    void deleteProductionPlan(Long[] ids);
    
    /**
     * 审核生产计划
     */
    void auditProductionPlan(Long planId, Integer auditStatus, String auditRemark);

    /**
     * 删除生产计划（单个）
     */
    @Transactional
    void deleteProductionPlan(Long id);

    /**
     * 审批生产计划
     */
    @Transactional
    void approveProductionPlan(Long id, String approver);

    /**
     * 生产计划执行
     */
    void executeProductionPlan(Long planId);
    
    /**
     * 生产计划完成
     */
    void completeProductionPlan(Long planId);
}