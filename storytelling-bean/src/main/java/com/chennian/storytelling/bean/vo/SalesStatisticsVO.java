package com.chennian.storytelling.bean.vo;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

/**
 * 销售统计数据VO对象
 * @author chennian
 * @date 2023/5/20
 */
public class SalesStatisticsVO implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 订单总数
     */
    private Long totalOrders;

    /**
     * 草稿状态订单数量
     */
    private Long draftOrders;

    /**
     * 已确认状态订单数量
     */
    private Long confirmedOrders;

    /**
     * 已发货状态订单数量
     */
    private Long shippedOrders;

    /**
     * 已完成状态订单数量
     */
    private Long completedOrders;

    /**
     * 已取消状态订单数量
     */
    private Long cancelledOrders;

    /**
     * 销售金额数据
     */
    private List<Map<String, Object>> salesAmount;

    /**
     * 热销产品数据
     */
    private List<Map<String, Object>> hotProducts;

    /**
     * 平均订单金额
     */
    private BigDecimal averageOrderAmount;

    /**
     * 产品类别统计数据
     */
    private List<Map<String, Object>> categoryStats;

    public Long getTotalOrders() {
        return totalOrders;
    }

    public void setTotalOrders(Long totalOrders) {
        this.totalOrders = totalOrders;
    }

    public Long getDraftOrders() {
        return draftOrders;
    }

    public void setDraftOrders(Long draftOrders) {
        this.draftOrders = draftOrders;
    }

    public Long getConfirmedOrders() {
        return confirmedOrders;
    }

    public void setConfirmedOrders(Long confirmedOrders) {
        this.confirmedOrders = confirmedOrders;
    }

    public Long getShippedOrders() {
        return shippedOrders;
    }

    public void setShippedOrders(Long shippedOrders) {
        this.shippedOrders = shippedOrders;
    }

    public Long getCompletedOrders() {
        return completedOrders;
    }

    public void setCompletedOrders(Long completedOrders) {
        this.completedOrders = completedOrders;
    }

    public Long getCancelledOrders() {
        return cancelledOrders;
    }

    public void setCancelledOrders(Long cancelledOrders) {
        this.cancelledOrders = cancelledOrders;
    }

    public List<Map<String, Object>> getSalesAmount() {
        return salesAmount;
    }

    public void setSalesAmount(List<Map<String, Object>> salesAmount) {
        this.salesAmount = salesAmount;
    }

    public List<Map<String, Object>> getHotProducts() {
        return hotProducts;
    }

    public void setHotProducts(List<Map<String, Object>> hotProducts) {
        this.hotProducts = hotProducts;
    }

    public BigDecimal getAverageOrderAmount() {
        return averageOrderAmount;
    }

    public void setAverageOrderAmount(BigDecimal averageOrderAmount) {
        this.averageOrderAmount = averageOrderAmount;
    }

    public List<Map<String, Object>> getCategoryStats() {
        return categoryStats;
    }

    public void setCategoryStats(List<Map<String, Object>> categoryStats) {
        this.categoryStats = categoryStats;
    }
}