package com.chennian.storytelling.bean.vo;

import com.chennian.storytelling.bean.model.mall.MallDelivery;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * 商城配送VO
 * 
 * @author chennian
 * @date 2025-01-27
 */
@Data
@EqualsAndHashCode(callSuper = true)
public class MallDeliveryVO extends MallDelivery {
    
    /**
     * 订单编号
     */
    private String orderSn;
    
    /**
     * 用户ID
     */
    private Long userId;
    
    /**
     * 用户名称
     */
    private String userName;
    
    /**
     * 用户手机号
     */
    private String userPhone;
    
    /**
     * 商品ID
     */
    private Long productId;
    
    /**
     * 商品名称
     */
    private String productName;
    
    /**
     * 商品图片
     */
    private String productPic;
    
    /**
     * 商品规格，JSON格式
     */
    private String specifications;
    
    /**
     * 购买数量
     */
    private Integer quantity;
    
    /**
     * 商品单价
     */
    private BigDecimal productPrice;
    
    /**
     * 订单总金额
     */
    private BigDecimal totalAmount;
    
    /**
     * 配送状态名称
     */
    private String statusName;
    
    /**
     * 配送方式名称
     */
    private String deliveryMethodName;
    
    /**
     * 配送时长（小时）
     */
    private Integer deliveryDuration;
    
    /**
     * 是否超时
     */
    private Boolean isTimeout;
    
    /**
     * 物流轨迹，JSON格式
     */
    private String logisticsTrack;
    
    /**
     * 最新物流信息
     */
    private String latestLogistics;
    
    /**
     * 最新物流时间
     */
    private LocalDateTime latestLogisticsTime;
    
    /**
     * 配送成功率
     */
    private BigDecimal successRate;
    
    /**
     * 平均配送时长
     */
    private BigDecimal avgDeliveryDuration;
    
    /**
     * 配送总单数
     */
    private Integer totalDeliveryCount;
    
    /**
     * 待发货单数
     */
    private Integer pendingCount;
    
    /**
     * 运输中单数
     */
    private Integer inTransitCount;
    
    /**
     * 已签收单数
     */
    private Integer receivedCount;
    
    /**
     * 异常单数
     */
    private Integer exceptionCount;
    
    /**
     * 同城配送范围
     */
    private String localDeliveryArea;
    
    /**
     * 自提点信息
     */
    private String pickupInfo;
} 