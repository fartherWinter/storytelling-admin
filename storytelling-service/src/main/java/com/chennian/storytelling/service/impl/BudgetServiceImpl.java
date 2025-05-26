package com.chennian.storytelling.service.impl;

import com.chennian.storytelling.service.BudgetService;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 预算与管控服务实现类
 * 负责预算执行报告、预算创建与更新、预算可用性检查等功能
 * @author chen
 * @date 2023/6/15
 */
@Service
public class BudgetServiceImpl implements BudgetService {

    /**
     * 获取预算执行报告
     * @param period 期间
     * @param departmentId 部门ID
     * @return 预算执行报告数据
     */
    @Override
    public Map<String, Object> getBudgetExecutionReport(String period, String departmentId) {
        Map<String, Object> report = new HashMap<>();
        
        // 设置基本信息
        report.put("期间", period);
        report.put("部门ID", departmentId);
        
        // 根据部门ID获取不同的预算执行报告
        if ("D001".equals(departmentId)) {
            report.put("部门名称", "生产部门");
            
            // 预算执行总览
            Map<String, Object> summary = new HashMap<>();
            summary.put("预算总额", 5000000.00);
            summary.put("已执行金额", 3200000.00);
            summary.put("剩余预算", 1800000.00);
            summary.put("执行率", "64.0%");
            report.put("预算执行总览", summary);
            
            // 预算执行明细
            List<Map<String, Object>> details = new ArrayList<>();
            
            Map<String, Object> item1 = new HashMap<>();
            item1.put("预算项目", "原材料采购");
            item1.put("预算金额", 3000000.00);
            item1.put("已执行金额", 2000000.00);
            item1.put("剩余预算", 1000000.00);
            item1.put("执行率", "66.7%");
            details.add(item1);
            
            Map<String, Object> item2 = new HashMap<>();
            item2.put("预算项目", "设备维护");
            item2.put("预算金额", 800000.00);
            item2.put("已执行金额", 500000.00);
            item2.put("剩余预算", 300000.00);
            item2.put("执行率", "62.5%");
            details.add(item2);
            
            Map<String, Object> item3 = new HashMap<>();
            item3.put("预算项目", "人工成本");
            item3.put("预算金额", 1200000.00);
            item3.put("已执行金额", 700000.00);
            item3.put("剩余预算", 500000.00);
            item3.put("执行率", "58.3%");
            details.add(item3);
            
            report.put("预算执行明细", details);
            
        } else if ("D002".equals(departmentId)) {
            report.put("部门名称", "销售部门");
            
            // 预算执行总览
            Map<String, Object> summary = new HashMap<>();
            summary.put("预算总额", 3500000.00);
            summary.put("已执行金额", 2500000.00);
            summary.put("剩余预算", 1000000.00);
            summary.put("执行率", "71.4%");
            report.put("预算执行总览", summary);
            
            // 预算执行明细
            List<Map<String, Object>> details = new ArrayList<>();
            
            Map<String, Object> item1 = new HashMap<>();
            item1.put("预算项目", "市场推广");
            item1.put("预算金额", 1500000.00);
            item1.put("已执行金额", 1200000.00);
            item1.put("剩余预算", 300000.00);
            item1.put("执行率", "80.0%");
            details.add(item1);
            
            Map<String, Object> item2 = new HashMap<>();
            item2.put("预算项目", "销售佣金");
            item2.put("预算金额", 1000000.00);
            item2.put("已执行金额", 700000.00);
            item2.put("剩余预算", 300000.00);
            item2.put("执行率", "70.0%");
            details.add(item2);
            
            Map<String, Object> item3 = new HashMap<>();
            item3.put("预算项目", "差旅费");
            item3.put("预算金额", 500000.00);
            item3.put("已执行金额", 300000.00);
            item3.put("剩余预算", 200000.00);
            item3.put("执行率", "60.0%");
            details.add(item3);
            
            Map<String, Object> item4 = new HashMap<>();
            item4.put("预算项目", "客户关系维护");
            item4.put("预算金额", 500000.00);
            item4.put("已执行金额", 300000.00);
            item4.put("剩余预算", 200000.00);
            item4.put("执行率", "60.0%");
            details.add(item4);
            
            report.put("预算执行明细", details);
            
        } else if ("D003".equals(departmentId)) {
            report.put("部门名称", "研发部门");
            
            // 预算执行总览
            Map<String, Object> summary = new HashMap<>();
            summary.put("预算总额", 4000000.00);
            summary.put("已执行金额", 2200000.00);
            summary.put("剩余预算", 1800000.00);
            summary.put("执行率", "55.0%");
            report.put("预算执行总览", summary);
            
            // 预算执行明细
            List<Map<String, Object>> details = new ArrayList<>();
            
            Map<String, Object> item1 = new HashMap<>();
            item1.put("预算项目", "研发人员薪酬");
            item1.put("预算金额", 2500000.00);
            item1.put("已执行金额", 1400000.00);
            item1.put("剩余预算", 1100000.00);
            item1.put("执行率", "56.0%");
            details.add(item1);
            
            Map<String, Object> item2 = new HashMap<>();
            item2.put("预算项目", "研发设备");
            item2.put("预算金额", 1000000.00);
            item2.put("已执行金额", 500000.00);
            item2.put("剩余预算", 500000.00);
            item2.put("执行率", "50.0%");
            details.add(item2);
            
            Map<String, Object> item3 = new HashMap<>();
            item3.put("预算项目", "专利申请");
            item3.put("预算金额", 300000.00);
            item3.put("已执行金额", 150000.00);
            item3.put("剩余预算", 150000.00);
            item3.put("执行率", "50.0%");
            details.add(item3);
            
            Map<String, Object> item4 = new HashMap<>();
            item4.put("预算项目", "技术咨询");
            item4.put("预算金额", 200000.00);
            item4.put("已执行金额", 150000.00);
            item4.put("剩余预算", 50000.00);
            item4.put("执行率", "75.0%");
            details.add(item4);
            
            report.put("预算执行明细", details);
            
        } else {
            // 默认返回空数据
            report.put("部门名称", "未知部门");
            report.put("预算执行总览", new HashMap<>());
            report.put("预算执行明细", new ArrayList<>());
        }
        
        return report;
    }
    
    /**
     * 创建预算
     * @param period 期间
     * @param departmentId 部门ID
     * @param budgetItems 预算项目
     * @return 结果
     */
    @Override
    public int createBudget(String period, String departmentId, Map<String, Double> budgetItems) {
        // 模拟创建预算操作
        // 实际应用中，这里应该连接数据库进行实际的预算创建操作
        
        // 记录预算创建日志
        System.out.println("创建预算 - 期间: " + period + ", 部门ID: " + departmentId);
        System.out.println("预算项目: " + budgetItems);
        
        // 模拟成功创建，返回影响的行数
        return 1;
    }
    
    /**
     * 更新预算
     * @param budgetId 预算ID
     * @param budgetItems 预算项目
     * @return 结果
     */
    @Override
    public int updateBudget(Long budgetId, Map<String, Double> budgetItems) {
        // 模拟更新预算操作
        // 实际应用中，这里应该连接数据库进行实际的预算更新操作
        
        // 记录预算更新日志
        System.out.println("更新预算 - 预算ID: " + budgetId);
        System.out.println("预算项目: " + budgetItems);
        
        // 模拟成功更新，返回影响的行数
        return 1;
    }
    
    /**
     * 检查预算可用性
     * @param departmentId 部门ID
     * @param expenseType 费用类型
     * @param amount 金额
     * @return 是否可用
     */
    @Override
    public boolean checkBudgetAvailable(String departmentId, String expenseType, Double amount) {
        // 模拟检查预算可用性操作
        // 实际应用中，这里应该连接数据库进行实际的预算可用性检查操作
        
        // 记录预算可用性检查日志
        System.out.println("检查预算可用性 - 部门ID: " + departmentId + ", 费用类型: " + expenseType + ", 金额: " + amount);
        
        // 模拟预算检查逻辑
        boolean isAvailable = true;
        
        // 根据部门ID和费用类型进行简单判断
        if ("D001".equals(departmentId)) {
            if ("原材料采购".equals(expenseType) && amount > 1000000.00) {
                isAvailable = false;
            } else if ("设备维护".equals(expenseType) && amount > 300000.00) {
                isAvailable = false;
            } else if ("人工成本".equals(expenseType) && amount > 500000.00) {
                isAvailable = false;
            }
        } else if ("D002".equals(departmentId)) {
            if ("市场推广".equals(expenseType) && amount > 300000.00) {
                isAvailable = false;
            } else if ("销售佣金".equals(expenseType) && amount > 300000.00) {
                isAvailable = false;
            } else if ("差旅费".equals(expenseType) && amount > 200000.00) {
                isAvailable = false;
            } else if ("客户关系维护".equals(expenseType) && amount > 200000.00) {
                isAvailable = false;
            }
        } else if ("D003".equals(departmentId)) {
            if ("研发人员薪酬".equals(expenseType) && amount > 1100000.00) {
                isAvailable = false;
            } else if ("研发设备".equals(expenseType) && amount > 500000.00) {
                isAvailable = false;
            } else if ("专利申请".equals(expenseType) && amount > 150000.00) {
                isAvailable = false;
            } else if ("技术咨询".equals(expenseType) && amount > 50000.00) {
                isAvailable = false;
            }
        }
        
        return isAvailable;
    }
}