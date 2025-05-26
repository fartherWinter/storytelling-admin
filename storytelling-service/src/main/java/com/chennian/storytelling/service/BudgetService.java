package com.chennian.storytelling.service;

import java.util.Map;

/**
 * 预算与管控服务接口
 * 负责预算执行报告、预算创建与更新、预算可用性检查等功能
 * @author chen
 * @date 2023/6/15
 */
public interface BudgetService {
    
    /**
     * 获取预算执行报告
     * @param period 期间
     * @param departmentId 部门ID
     * @return 预算执行报告数据
     */
    Map<String, Object> getBudgetExecutionReport(String period, String departmentId);
    
    /**
     * 创建预算
     * @param period 期间
     * @param departmentId 部门ID
     * @param budgetItems 预算项目
     * @return 结果
     */
    int createBudget(String period, String departmentId, Map<String, Double> budgetItems);
    
    /**
     * 更新预算
     * @param budgetId 预算ID
     * @param budgetItems 预算项目
     * @return 结果
     */
    int updateBudget(Long budgetId, Map<String, Double> budgetItems);
    
    /**
     * 检查预算可用性
     * @param departmentId 部门ID
     * @param expenseType 费用类型
     * @param amount 金额
     * @return 是否可用
     */
    boolean checkBudgetAvailable(String departmentId, String expenseType, Double amount);
}