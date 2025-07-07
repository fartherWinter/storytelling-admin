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
 * 物流配送方式实体类
 * 
 * @author chennian
 * @date 2025-01-27
 */
@Data
@EqualsAndHashCode(callSuper = false)
@ToString
@TableName("mall_delivery_method")
public class MallDeliveryMethod implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 配送方式ID
     */
    @TableId(value = "id", type = IdType.ASSIGN_ID)
    private Long id;

    /**
     * 配送方式编码
     */
    private String methodCode;

    /**
     * 配送方式名称
     */
    private String methodName;

    /**
     * 配送类型：1-快递，2-EMS，3-平邮，4-自提，5-同城配送
     */
    private Integer methodType;

    /**
     * 配送范围：1-国内，2-国际，3-港澳台
     */
    private Integer deliveryScope;

    /**
     * 起步价
     */
    private BigDecimal startingPrice;

    /**
     * 基础运费
     */
    private BigDecimal baseFreight;

    /**
     * 重量单位：1-克，2-千克
     */
    private Integer weightUnit;

    /**
     * 首重重量
     */
    private BigDecimal firstWeight;

    /**
     * 首重价格
     */
    private BigDecimal firstWeightPrice;

    /**
     * 续重单位
     */
    private BigDecimal continuedWeight;

    /**
     * 续重价格
     */
    private BigDecimal continuedWeightPrice;

    /**
     * 是否支持指定配送时间：0-不支持，1-支持
     */
    private Integer supportTimeDelivery;

    /**
     * 配送时间类型：1-工作日，2-节假日，3-全天
     */
    private Integer deliveryTimeType;

    /**
     * 预计送达天数
     */
    private Integer estimatedDays;

    /**
     * 配送说明
     */
    private String deliveryDesc;

    /**
     * 特殊区域加价
     */
    private String specialAreaPrice;

    /**
     * 是否支持保价：0-不支持，1-支持
     */
    private Integer supportValueInsurance;

    /**
     * 保价费率（千分比）
     */
    private BigDecimal insuranceRate;

    /**
     * 最高保价金额
     */
    private BigDecimal maxInsuranceAmount;

    /**
     * 是否支持代收货款：0-不支持，1-支持
     */
    private Integer supportCashOnDelivery;

    /**
     * 代收货款费率（千分比）
     */
    private BigDecimal codRate;

    /**
     * 最高代收金额
     */
    private BigDecimal maxCodAmount;

    /**
     * 配送优先级：数字越小优先级越高
     */
    private Integer priority;

    /**
     * 状态：0-禁用，1-启用
     */
    private Integer status;

    /**
     * 是否默认：0-否，1-是
     */
    private Integer isDefault;

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