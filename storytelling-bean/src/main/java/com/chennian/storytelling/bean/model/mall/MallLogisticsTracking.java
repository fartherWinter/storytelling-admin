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
 * 
 * @author chennian
 * @date 2025-01-27
 */
@Data
@EqualsAndHashCode(callSuper = false)
@ToString
@TableName("mall_logistics_tracking")
public class MallLogisticsTracking implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 跟踪ID
     */
    @TableId(value = "id", type = IdType.ASSIGN_ID)
    private Long id;

    /**
     * 订单ID
     */
    private Long orderId;

    /**
     * 子订单ID
     */
    private Long subOrderId;

    /**
     * 物流公司编码
     */
    private String logisticsCode;

    /**
     * 物流公司名称
     */
    private String logisticsCompany;

    /**
     * 物流单号
     */
    private String logisticsNo;

    /**
     * 发货地址
     */
    private String fromAddress;

    /**
     * 收货地址
     */
    private String toAddress;

    /**
     * 物流状态：0-待揽收，1-已揽收，2-运输中，3-派送中，4-已签收，5-异常
     */
    private Integer status;

    /**
     * 物流轨迹，JSON格式
     */
    private String trackInfo;

    /**
     * 最新物流信息
     */
    private String latestInfo;

    /**
     * 最新物流时间
     */
    private LocalDateTime latestTime;

    /**
     * 签收时间
     */
    private LocalDateTime signTime;

    /**
     * 签收人
     */
    private String signer;

    /**
     * 异常原因
     */
    private String exceptionReason;

    /**
     * 异常处理状态：0-未处理，1-处理中，2-已处理
     */
    private Integer exceptionStatus;

    /**
     * 异常处理结果
     */
    private String exceptionResult;

    /**
     * 预计送达时间
     */
    private LocalDateTime expectedTime;

    /**
     * 是否加急：0-否，1-是
     */
    private Integer isUrgent;

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

    /**
     * 创建人
     */
    private String createBy;

    /**
     * 更新人
     */
    private String updateBy;
} 