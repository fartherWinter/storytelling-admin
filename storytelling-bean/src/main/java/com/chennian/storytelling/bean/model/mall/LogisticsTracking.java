package com.chennian.storytelling.bean.model.mall;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * 物流跟踪实体类
 */
@Data
@EqualsAndHashCode(callSuper = false)
@ToString
@TableName("mall_logistics_tracking")
public class LogisticsTracking implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 跟踪记录ID
     */
    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    /**
     * 订单ID
     */
    private Long orderId;

    /**
     * 订单编号
     */
    private String orderSn;

    /**
     * 物流公司编码
     */
    private String logisticsCompanyCode;

    /**
     * 物流公司名称
     */
    private String logisticsCompanyName;

    /**
     * 运单号
     */
    private String trackingNumber;

    /**
     * 物流状态：0-待发货，1-已发货，2-运输中，3-派送中，4-已签收，5-异常，6-退回
     */
    private Integer status;

    /**
     * 状态描述
     */
    private String statusDescription;

    /**
     * 当前位置
     */
    private String currentLocation;

    /**
     * 预计送达时间
     */
    private LocalDateTime estimatedDeliveryTime;

    /**
     * 实际送达时间
     */
    private LocalDateTime actualDeliveryTime;

    /**
     * 签收人
     */
    private String signedBy;

    /**
     * 备注
     */
    private String remark;

    /**
     * 创建时间
     */
    private LocalDateTime createTime;

    /**
     * 更新时间
     */
    private LocalDateTime updateTime;
}