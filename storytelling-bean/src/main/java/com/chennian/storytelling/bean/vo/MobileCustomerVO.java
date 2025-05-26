package com.chennian.storytelling.bean.vo;

import lombok.Data;

import java.io.Serializable;
import java.util.List;
import java.util.Map;

/**
 * 移动端客户VO
 * 
 * @author chennian
 * @date 2023/12/15
 */
@Data
public class MobileCustomerVO implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 客户ID
     */
    private Long customerId;

    /**
     * 客户名称
     */
    private String customerName;

    /**
     * 联系人
     */
    private String contactPerson;

    /**
     * 联系电话
     */
    private String contactPhone;

    /**
     * 客户状态
     */
    private String status;

    /**
     * 最近订单
     */
    private Map<String, Object> recentOrder;

    /**
     * 未结订单数
     */
    private Long pendingOrders;

    /**
     * 客户等级
     */
    private String level;

    /**
     * 最近跟进记录
     */
    private List<Map<String, Object>> recentFollowups;
}