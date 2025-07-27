package com.chennian.storytelling.product.entity;

import com.baomidou.mybatisplus.annotation.*;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import javax.validation.constraints.NotBlank;
import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * 品牌实体类
 * 
 * @author chennian
 * @since 2024-01-01
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@TableName("brand")
public class Brand implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 品牌ID
     */
    @TableId(value = "brand_id", type = IdType.ASSIGN_ID)
    private Long brandId;

    /**
     * 品牌名称
     */
    @NotBlank(message = "品牌名称不能为空")
    @TableField("brand_name")
    private String brandName;

    /**
     * 品牌编码
     */
    @NotBlank(message = "品牌编码不能为空")
    @TableField("brand_code")
    private String brandCode;

    /**
     * 品牌英文名
     */
    @TableField("brand_name_en")
    private String brandNameEn;

    /**
     * 品牌LOGO
     */
    @TableField("logo")
    private String logo;

    /**
     * 品牌图片
     */
    @TableField("image")
    private String image;

    /**
     * 品牌描述
     */
    @TableField("description")
    private String description;

    /**
     * 品牌故事
     */
    @TableField("story")
    private String story;

    /**
     * 官方网站
     */
    @TableField("website")
    private String website;

    /**
     * 品牌状态：0-禁用，1-启用
     */
    @TableField("status")
    private Integer status;

    /**
     * 是否推荐：0-否，1-是
     */
    @TableField("is_recommend")
    private Integer isRecommend;

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

    /**
     * 商品数量（非数据库字段）
     */
    @TableField(exist = false)
    private Integer productCount;
}