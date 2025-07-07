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
 * 商品SKU实体类
 * 
 * @author chennian
 * @date 2025-01-27
 */
@Data
@EqualsAndHashCode(callSuper = false)
@ToString
@TableName("mall_product_sku")
public class MallProductSku implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * SKU ID
     */
    @TableId(value = "id", type = IdType.ASSIGN_ID)
    private Long id;

    /**
     * 商品ID
     */
    private Long productId;

    /**
     * SKU编码
     */
    private String skuCode;

    /**
     * SKU名称
     */
    private String skuName;

    /**
     * SKU规格属性，JSON格式存储
     */
    private String skuAttributes;

    /**
     * SKU图片
     */
    private String skuImage;

    /**
     * 市场价格
     */
    private BigDecimal marketPrice;

    /**
     * 销售价格
     */
    private BigDecimal salePrice;

    /**
     * 成本价格
     */
    private BigDecimal costPrice;

    /**
     * 库存数量
     */
    private Integer stock;

    /**
     * 库存预警值
     */
    private Integer stockWarning;

    /**
     * 销量
     */
    private Integer sales;

    /**
     * 重量（克）
     */
    private Integer weight;

    /**
     * 体积（立方厘米）
     */
    private Integer volume;

    /**
     * 条形码
     */
    private String barcode;

    /**
     * 状态: 0-禁用, 1-启用
     */
    private Integer status;

    /**
     * 是否默认SKU: 0-否, 1-是
     */
    private Integer isDefault;

    /**
     * 排序值，数值越大排序越靠前
     */
    private Integer sort;

    /**
     * SKU备注
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
     * 创建者
     */
    private String createBy;

    /**
     * 更新者
     */
    private String updateBy;
}