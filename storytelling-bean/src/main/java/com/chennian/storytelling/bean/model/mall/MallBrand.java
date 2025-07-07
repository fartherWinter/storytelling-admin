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
 * 商品品牌实体类
 * 
 * @author chennian
 * @date 2025-01-27
 */
@Data
@EqualsAndHashCode(callSuper = false)
@ToString
@TableName("mall_brand")
public class MallBrand implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 品牌ID
     */
    @TableId(value = "id", type = IdType.ASSIGN_ID)
    private Long id;

    /**
     * 品牌名称
     */
    private String brandName;

    /**
     * 品牌编码
     */
    private String brandCode;

    /**
     * 品牌英文名
     */
    private String brandEnglishName;

    /**
     * 品牌LOGO
     */
    private String logo;

    /**
     * 品牌图片
     */
    private String image;

    /**
     * 品牌描述
     */
    private String description;

    /**
     * 品牌故事
     */
    private String story;

    /**
     * 品牌官网
     */
    private String website;

    /**
     * 品牌国家/地区
     */
    private String country;

    /**
     * 成立年份
     */
    private Integer foundedYear;

    /**
     * 排序值，数值越大排序越靠前
     */
    private Integer sort;

    /**
     * 状态: 0-禁用, 1-启用
     */
    private Integer status;

    /**
     * 是否推荐: 0-否, 1-是
     */
    private Integer isRecommend;

    /**
     * 品牌关键词，用于SEO
     */
    private String keywords;

    /**
     * 品牌备注
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