package com.chennian.storytelling.bean.vo.workflow;

import java.util.Map;

/**
 * 应收账款汇总信息VO
 * @author chen
 * @date 2023/6/15
 */
public class ReceivablesSummaryVO {
    
    /**
     * 应收账款总额
     */
    private Double totalAmount;
    
    /**
     * 账龄分布
     */
    private Map<String, Object> agingDistribution;
    
    /**
     * 客户分布
     */
    private Map<String, Object> customerDistribution;
    
    /**
     * 收款计划
     */
    private Map<String, Object> collectionPlan;
    
    /**
     * 统计指标
     */
    private ReceivablesMetricsVO metrics;
    
    /**
     * 应收账款统计指标VO
     */
    public static class ReceivablesMetricsVO {
        /**
         * 平均收款周期
         */
        private String averageCollectionDays;
        
        /**
         * 客户数量
         */
        private Object customerCount;
        
        /**
         * 本月新增应收
         */
        private Object currentMonthReceivable;
        
        /**
         * 本月已收
         */
        private Object currentMonthReceived;

        public String getAverageCollectionDays() {
            return averageCollectionDays;
        }

        public void setAverageCollectionDays(String averageCollectionDays) {
            this.averageCollectionDays = averageCollectionDays;
        }

        public Object getCustomerCount() {
            return customerCount;
        }

        public void setCustomerCount(Object customerCount) {
            this.customerCount = customerCount;
        }

        public Object getCurrentMonthReceivable() {
            return currentMonthReceivable;
        }

        public void setCurrentMonthReceivable(Object currentMonthReceivable) {
            this.currentMonthReceivable = currentMonthReceivable;
        }

        public Object getCurrentMonthReceived() {
            return currentMonthReceived;
        }

        public void setCurrentMonthReceived(Object currentMonthReceived) {
            this.currentMonthReceived = currentMonthReceived;
        }
    }

    public Double getTotalAmount() {
        return totalAmount;
    }

    public void setTotalAmount(Double totalAmount) {
        this.totalAmount = totalAmount;
    }

    public Map<String, Object> getAgingDistribution() {
        return agingDistribution;
    }

    public void setAgingDistribution(Map<String, Object> agingDistribution) {
        this.agingDistribution = agingDistribution;
    }

    public Map<String, Object> getCustomerDistribution() {
        return customerDistribution;
    }

    public void setCustomerDistribution(Map<String, Object> customerDistribution) {
        this.customerDistribution = customerDistribution;
    }

    public Map<String, Object> getCollectionPlan() {
        return collectionPlan;
    }

    public void setCollectionPlan(Map<String, Object> collectionPlan) {
        this.collectionPlan = collectionPlan;
    }

    public ReceivablesMetricsVO getMetrics() {
        return metrics;
    }

    public void setMetrics(ReceivablesMetricsVO metrics) {
        this.metrics = metrics;
    }
}