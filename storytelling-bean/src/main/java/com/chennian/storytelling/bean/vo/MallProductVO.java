package com.chennian.storytelling.bean.vo;

import com.chennian.storytelling.bean.model.mall.MallProduct;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.math.BigDecimal;

/**
 * 商城商品VO，扩展基础商品信息
 * 
 * @author chennian
 * @date 2025-01-27
 */
@Data
@EqualsAndHashCode(callSuper = true)
public class MallProductVO extends MallProduct {
    
    /**
     * 商品评分
     */
    private BigDecimal rating;
    
    /**
     * 评价数量
     */
    private Integer reviewCount;
    
    /**
     * 收藏数量
     */
    private Integer favoriteCount;
    
    /**
     * 商品参数，JSON格式
     */
    private String parameters;
    
    /**
     * 商品状态名称
     */
    private String statusName;
    
    /**
     * 是否可购买
     */
    private Boolean canBuy;
    
    /**
     * 是否有库存
     */
    private Boolean hasStock;
    
    /**
     * 商品SKU列表，JSON格式
     */
    private String skuList;
    
    /**
     * 商品属性列表，JSON格式
     */
    private String attributes;
    
    /**
     * 商品评价摘要，JSON格式
     */
    private String reviewSummary;
    
    /**
     * 促销信息，JSON格式
     */
    private String promotions;
    
    /**
     * 服务保障，JSON格式
     */
    private String services;
} 