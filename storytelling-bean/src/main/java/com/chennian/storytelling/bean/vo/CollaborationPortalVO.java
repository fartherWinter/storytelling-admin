package com.chennian.storytelling.bean.vo;

import lombok.Data;

import java.io.Serializable;
import java.util.List;

/**
 * 协同门户VO
 * 
 * @author chennian
 * @date 2023/12/15
 */
@Data
public class CollaborationPortalVO implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 活跃供应商数量
     */
    private Integer activeSuppliers;

    /**
     * 活跃客户数量
     */
    private Integer activeCustomers;

    /**
     * 待处理订单数量
     */
    private Integer pendingOrders;

    /**
     * 最近供应链事件
     */
    private List<SupplyChainEventVO> recentEvents;

    /**
     * 协同计划数量
     */
    private Integer collaborationPlans;

    /**
     * 供应链绩效指标
     */
    private Double performanceIndex;

    /**
     * 系统公告
     */
    private List<String> systemAnnouncements;
}