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
 * 商城商品实体类
 * 
 * @author chennian
 * @date 2025-01-27
 */
@Data
@EqualsAndHashCode(callSuper = false)
@ToString
@TableName("mall_product")
public class MallProduct implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 商品ID, 分片键
     */
    @TableId(value = "id", type = IdType.ASSIGN_ID)
    private Long id;

    /**
     * 商品编码
     */
    private String productCode;

    /**
     * 商品名称
     */
    private String productName;

    /**
     * 商品分类ID
     */
    private Long categoryId;

    /**
     * 商品分类名称
     */
    private String categoryName;

    /**
     * 商品品牌ID
     */
    private Long brandId;

    /**
     * 商品品牌名称
     */
    private String brandName;

    /**
     * 商品主图
     */
    private String mainImage;

    /**
     * 商品图片，多张图片用逗号分隔
     */
    private String images;

    /**
     * 商品详情
     */
    private String description;

    /**
     * 商品规格
     */
    private String specification;

    /**
     * 商品单位
     */
    private String unit;

    /**
     * 商品重量（克）
     */
    private Integer weight;

    /**
     * 商品体积（立方厘米）
     */
    private Integer volume;

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
     * 浏览量
     */
    private Integer views;

    /**
     * 商品状态: 0-下架, 1-上架, 2-售罄
     */
    private Integer status;

    /**
     * 是否推荐: 0-否, 1-是
     */
    private Integer isRecommend;

    /**
     * 是否热销: 0-否, 1-是
     */
    private Integer isHot;

    /**
     * 是否新品: 0-否, 1-是
     */
    private Integer isNew;

    /**
     * 排序值，数值越大排序越靠前
     */
    private Integer sort;

    /**
     * 商品关键词，用于搜索
     */
    private String keywords;

    /**
     * 商品标签，多个标签用逗号分隔
     */
    private String tags;

    /**
     * 商品备注
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