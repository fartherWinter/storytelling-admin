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
 * 配送方式实体类
 */
@Data
@EqualsAndHashCode(callSuper = false)
@ToString
@TableName("mall_delivery_method")
public class DeliveryMethod implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 配送方式ID
     */
    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    /**
     * 配送方式名称
     */
    private String name;

    /**
     * 配送方式编码
     */
    private String code;

    /**
     * 配送方式描述
     */
    private String description;

    /**
     * 基础运费
     */
    private BigDecimal basePrice;

    /**
     * 首重（kg）
     */
    private BigDecimal firstWeight;

    /**
     * 续重价格（每kg）
     */
    private BigDecimal additionalPrice;

    /**
     * 免运费金额阈值
     */
    private BigDecimal freeShippingThreshold;

    /**
     * 预计送达时间（小时）
     */
    private Integer estimatedDeliveryHours;

    /**
     * 支持的地区（JSON格式存储）
     */
    private String supportedRegions;

    /**
     * 状态：0-禁用，1-启用
     */
    private Integer status;

    /**
     * 排序
     */
    private Integer sort;

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
    private Long createBy;

    /**
     * 更新人
     */
    private Long updateBy;
}