package com.chennian.storytelling.service;

import java.util.Map;

/**
 * 供应链协同计划服务接口
 * 
 * @author chennian
 * @date 2023/12/15
 */
public interface SupplyChainPlanService {
    
    /**
     * 创建供应链协同计划
     * 
     * @param plan 计划信息，包含planName(计划名称)、startDate(开始日期)、endDate(结束日期)、
     *             description(描述)、participants(参与者)、milestones(里程碑)等
     * @return 计划ID
     */
    Long createPlan(Map<String, Object> plan);
    
    /**
     * 获取供应链协同计划详情
     * 
     * @param planId 计划ID
     * @return 计划详情
     */
    Map<String, Object> getPlanDetail(Long planId);
    
    /**
     * 更新供应链协同计划状态
     * 
     * @param planId 计划ID
     * @param status 状态
     * @param comment 备注
     * @return 是否成功
     */
    boolean updatePlanStatus(Long planId, String status, String comment);
    
    /**
     * 获取供应商相关的协同计划列表
     * 
     * @param supplierId 供应商ID
     * @return 计划列表
     */
    Map<String, Object> getSupplierPlans(Long supplierId);
    
    /**
     * 获取客户相关的协同计划列表
     * 
     * @param customerId 客户ID
     * @return 计划列表
     */
    Map<String, Object> getCustomerPlans(Long customerId);
}