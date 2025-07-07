package com.chennian.storytelling.bean.vo.workflow;

import lombok.Data;

import java.util.List;

/**
 * 应付账款汇总信息VO
 * @author chen
 * @date 2023/6/15
 */
@Data
public class PayablesSummaryVO {
    
    /**
     * 应付账款总额
     */
    private Double totalAmount;
    
    /**
     * 账龄分布
     */
    private List<AgingDistribution> agingDistribution;
    
    /**
     * 供应商分布
     */
    private List<SupplierDistribution> supplierDistribution;
    
    /**
     * 付款计划
     */
    private List<PaymentPlan> paymentPlan;
    
    /**
     * 统计指标
     */
    private PayablesMetricsVO metrics;

    
    /**
     * 应付账款统计指标VO
     */
    public static class PayablesMetricsVO {
        /**
         * 平均付款周期
         */
        private String averagePaymentDays;
        
        /**
         * 供应商数量
         */
        private Object supplierCount;
        
        /**
         * 本月新增应付
         */
        private Object currentMonthPayable;
        
        /**
         * 本月已付
         */
        private Object currentMonthPaid;
        
        public String getAveragePaymentDays() {
            return averagePaymentDays;
        }
        
        public void setAveragePaymentDays(String averagePaymentDays) {
            this.averagePaymentDays = averagePaymentDays;
        }
        
        public Object getSupplierCount() {
            return supplierCount;
        }
        
        public void setSupplierCount(Object supplierCount) {
            this.supplierCount = supplierCount;
        }
        
        public Object getCurrentMonthPayable() {
            return currentMonthPayable;
        }
        
        public void setCurrentMonthPayable(Object currentMonthPayable) {
            this.currentMonthPayable = currentMonthPayable;
        }
        
        public Object getCurrentMonthPaid() {
            return currentMonthPaid;
        }
        
        public void setCurrentMonthPaid(Object currentMonthPaid) {
            this.currentMonthPaid = currentMonthPaid;
        }
    }


}