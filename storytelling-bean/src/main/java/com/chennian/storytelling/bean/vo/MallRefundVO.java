package com.chennian.storytelling.bean.vo;

import com.chennian.storytelling.bean.model.mall.MallRefund;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * 商城退款VO
 * 
 * @author chennian
 * @date 2025-01-27
 */
@Data
@EqualsAndHashCode(callSuper = true)
public class MallRefundVO extends MallRefund {
    
    /**
     * 订单编号
     */
    private String orderSn;
    
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
     * 订单支付金额
     */
    private BigDecimal payAmount;
    
    /**
     * 支付方式
     */
    private String payMethod;
    
    /**
     * 支付时间
     */
    private LocalDateTime payTime;
    
    /**
     * 退款状态名称
     */
    private String statusName;
    
    /**
     * 退款方式名称
     */
    private String refundMethodName;
    
    /**
     * 处理人名称
     */
    private String handlerName;
    
    /**
     * 处理人部门
     */
    private String handlerDepartment;
    
    /**
     * 退款处理时长（小时）
     */
    private Integer handleDuration;
    
    /**
     * 是否超时
     */
    private Boolean isTimeout;
    
    /**
     * 退款成功率
     */
    private BigDecimal successRate;
    
    /**
     * 平均处理时长
     */
    private BigDecimal avgHandleDuration;
    
    /**
     * 退款总金额
     */
    private BigDecimal totalRefundAmount;
    
    /**
     * 退款总笔数
     */
    private Integer totalRefundCount;
    
    /**
     * 待处理退款笔数
     */
    private Integer pendingCount;
    
    /**
     * 超时退款笔数
     */
    private Integer timeoutCount;
} 