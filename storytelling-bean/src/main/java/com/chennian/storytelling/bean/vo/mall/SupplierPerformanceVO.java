package com.chennian.storytelling.bean.vo.mall;

import java.io.Serializable;

public class SupplierPerformanceVO implements Serializable {
    private Long supplierId;
    private Integer orderCount;
    private Double onTimeDeliveryRate;
    private Double qualityScoreAvg;
    private Double totalAmount;
    private String lastEvaluationDate;

    public Long getSupplierId() {
        return supplierId;
    }
    public void setSupplierId(Long supplierId) {
        this.supplierId = supplierId;
    }
    public Integer getOrderCount() {
        return orderCount;
    }
    public void setOrderCount(Integer orderCount) {
        this.orderCount = orderCount;
    }
    public Double getOnTimeDeliveryRate() {
        return onTimeDeliveryRate;
    }
    public void setOnTimeDeliveryRate(Double onTimeDeliveryRate) {
        this.onTimeDeliveryRate = onTimeDeliveryRate;
    }
    public Double getQualityScoreAvg() {
        return qualityScoreAvg;
    }
    public void setQualityScoreAvg(Double qualityScoreAvg) {
        this.qualityScoreAvg = qualityScoreAvg;
    }
    public Double getTotalAmount() {
        return totalAmount;
    }
    public void setTotalAmount(Double totalAmount) {
        this.totalAmount = totalAmount;
    }
    public String getLastEvaluationDate() {
        return lastEvaluationDate;
    }
    public void setLastEvaluationDate(String lastEvaluationDate) {
        this.lastEvaluationDate = lastEvaluationDate;
    }
}