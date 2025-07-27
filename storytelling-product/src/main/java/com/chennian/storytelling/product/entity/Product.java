package com.chennian.storytelling.product.entity;

import com.baomidou.mybatisplus.annotation.*;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import javax.validation.constraints.DecimalMin;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * 商品实体类
 * 
 * @author chennian
 * @since 2024-01-01
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@TableName("product")
public class Product implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 商品ID
     */
    @TableId(value = "product_id", type = IdType.ASSIGN_ID)
    private Long productId;

    /**
     * 商品编码
     */
    @NotBlank(message = "商品编码不能为空")
    @TableField("product_code")
    private String productCode;

    /**
     * 商品名称
     */
    @NotBlank(message = "商品名称不能为空")
    @TableField("product_name")
    private String productName;

    /**
     * 商品副标题
     */
    @TableField("subtitle")
    private String subtitle;

    /**
     * 商品描述
     */
    @TableField("description")
    private String description;

    /**
     * 商品详情
     */
    @TableField("detail")
    private String detail;

    /**
     * 商品主图
     */
    @TableField("main_image")
    private String mainImage;

    /**
     * 商品图片集合（JSON格式）
     */
    @TableField("images")
    private String images;

    /**
     * 分类ID
     */
    @NotNull(message = "分类ID不能为空")
    @TableField("category_id")
    private Long categoryId;

    /**
     * 品牌ID
     */
    @TableField("brand_id")
    private Long brandId;

    /**
     * 商品价格
     */
    @NotNull(message = "商品价格不能为空")
    @DecimalMin(value = "0.01", message = "商品价格必须大于0")
    @TableField("price")
    private BigDecimal price;

    /**
     * 市场价格
     */
    @TableField("market_price")
    private BigDecimal marketPrice;

    /**
     * 成本价格
     */
    @TableField("cost_price")
    private BigDecimal costPrice;

    /**
     * 商品重量（克）
     */
    @TableField("weight")
    private Integer weight;

    /**
     * 商品单位
     */
    @TableField("unit")
    private String unit;

    /**
     * 商品规格（JSON格式）
     */
    @TableField("specifications")
    private String specifications;

    /**
     * 商品属性（JSON格式）
     */
    @TableField("attributes")
    private String attributes;

    /**
     * 商品标签（JSON格式）
     */
    @TableField("tags")
    private String tags;

    /**
     * 商品状态：0-下架，1-上架，2-预售
     */
    @TableField("status")
    private Integer status;

    /**
     * 是否推荐：0-否，1-是
     */
    @TableField("is_recommend")
    private Integer isRecommend;

    /**
     * 是否热销：0-否，1-是
     */
    @TableField("is_hot")
    private Integer isHot;

    /**
     * 是否新品：0-否，1-是
     */
    @TableField("is_new")
    private Integer isNew;

    /**
     * 销量
     */
    @TableField("sales_count")
    private Integer salesCount;

    /**
     * 浏览量
     */
    @TableField("view_count")
    private Integer viewCount;

    /**
     * 收藏量
     */
    @TableField("favorite_count")
    private Integer favoriteCount;

    /**
     * 评论数
     */
    @TableField("comment_count")
    private Integer commentCount;

    /**
     * 评分
     */
    @TableField("rating")
    private BigDecimal rating;

    /**
     * 排序
     */
    @TableField("sort_order")
    private Integer sortOrder;

    /**
     * SEO标题
     */
    @TableField("seo_title")
    private String seoTitle;

    /**
     * SEO关键词
     */
    @TableField("seo_keywords")
    private String seoKeywords;

    /**
     * SEO描述
     */
    @TableField("seo_description")
    private String seoDescription;

    /**
     * 上架时间
     */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @TableField("publish_time")
    private LocalDateTime publishTime;

    /**
     * 下架时间
     */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @TableField("unpublish_time")
    private LocalDateTime unpublishTime;

    /**
     * 备注
     */
    @TableField("remark")
    private String remark;

    /**
     * 创建时间
     */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @TableField(value = "create_time", fill = FieldFill.INSERT)
    private LocalDateTime createTime;

    /**
     * 更新时间
     */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @TableField(value = "update_time", fill = FieldFill.INSERT_UPDATE)
    private LocalDateTime updateTime;

    /**
     * 创建者
     */
    @TableField(value = "create_by", fill = FieldFill.INSERT)
    private String createBy;

    /**
     * 更新者
     */
    @TableField(value = "update_by", fill = FieldFill.INSERT_UPDATE)
    private String updateBy;

    /**
     * 删除标志：0-未删除，1-已删除
     */
    @TableLogic
    @TableField("deleted")
    private Integer deleted;
}