package com.chennian.storytelling.bean.vo;

import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

/**
 * 移动端销售订单VO
 * 
 * @author chennian
 * @date 2023/12/15
 */
@Data
public class MobileSalesOrderVO implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 订单ID
     */
    private Long orderId;

    /**
     * 订单编号
     */
    private String orderNo;

    /**
     * 客户ID
     */
    private Long customerId;

    /**
     * 客户名称
     */
    private String customerName;

    /**
     * 订单状态
     */
    private String status;

    /**
     * 订单金额
     */
    private BigDecimal amount;

    /**
     * 下单时间
     */
    private Long orderTime;

    /**
     * 预计交付时间
     */
    private Long expectedDeliveryTime;

    /**
     * 实际交付时间
     */
    private Long actualDeliveryTime;

    /**
     * 订单项列表（简化版）
     */
    private List<Map<String, Object>> orderItems;

    /**
     * 支付状态
     */
    private String paymentStatus;

    /**
     * 配送信息
     */
    private Map<String, Object> deliveryInfo;

    /**
     * 订单备注
     */
    private String remark;
}