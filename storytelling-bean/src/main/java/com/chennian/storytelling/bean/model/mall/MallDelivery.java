package com.chennian.storytelling.bean.model.mall;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * 商城配送实体类
 * 
 * @author chennian
 * @date 2025-01-27
 */
@Data
@EqualsAndHashCode(callSuper = false)
@ToString
@TableName("mall_delivery")
public class MallDelivery implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 配送ID
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
     * 收件人姓名
     */
    private String receiverName;

    /**
     * 收件人电话
     */
    private String receiverPhone;

    /**
     * 收件人省份
     */
    private String receiverProvince;

    /**
     * 收件人城市
     */
    private String receiverCity;

    /**
     * 收件人区县
     */
    private String receiverDistrict;

    /**
     * 收件人详细地址
     */
    private String receiverAddress;

    /**
     * 配送状态：0-待发货，1-已发货，2-运输中，3-已签收，4-异常
     */
    private Integer status;

    /**
     * 配送方式：1-快递，2-同城配送，3-自提
     */
    private Integer deliveryMethod;

    /**
     * 配送费用
     */
    private BigDecimal deliveryFee;

    /**
     * 预计送达时间
     */
    private LocalDateTime expectedTime;

    /**
     * 实际发货时间
     */
    private LocalDateTime deliveryTime;

    /**
     * 签收时间
     */
    private LocalDateTime receiveTime;

    /**
     * 配送备注
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