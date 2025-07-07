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
 * 物流区域实体类
 * 
 * @author chennian
 * @date 2025-01-27
 */
@Data
@EqualsAndHashCode(callSuper = false)
@ToString
@TableName("mall_delivery_area")
public class MallDeliveryArea implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 区域ID
     */
    @TableId(value = "id", type = IdType.ASSIGN_ID)
    private Long id;

    /**
     * 区域编码
     */
    private String areaCode;

    /**
     * 区域名称
     */
    private String areaName;

    /**
     * 父级区域编码
     */
    private String parentCode;

    /**
     * 区域级别：1-省，2-市，3-区/县，4-街道/镇
     */
    private Integer areaLevel;

    /**
     * 区域类型：1-普通区域，2-特殊区域，3-偏远地区，4-离岛
     */
    private Integer areaType;

    /**
     * 邮政编码
     */
    private String zipCode;

    /**
     * 城市编码
     */
    private String cityCode;

    /**
     * 区号
     */
    private String areaCode2;

    /**
     * 经度
     */
    private Double longitude;

    /**
     * 纬度
     */
    private Double latitude;

    /**
     * 排序号
     */
    private Integer sortNo;

    /**
     * 是否启用：0-禁用，1-启用
     */
    private Integer status;

    /**
     * 是否支持配送：0-不支持，1-支持
     */
    private Integer supportDelivery;

    /**
     * 配送限制说明
     */
    private String deliveryLimit;

    /**
     * 配送时效（小时）
     */
    private Integer deliveryHours;

    /**
     * 配送费用系数
     */
    private Double freightFactor;

    /**
     * 区域备注
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