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
 * 商城分类实体类
 * 
 * @author chennian
 * @date 2025-01-27
 */
@Data
@EqualsAndHashCode(callSuper = false)
@ToString
@TableName("mall_category")
public class MallCategory implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 分类ID, 分片键
     */
    @TableId(value = "id", type = IdType.ASSIGN_ID)
    private Long id;

    /**
     * 分类名称
     */
    private String categoryName;

    /**
     * 分类编码
     */
    private String categoryCode;

    /**
     * 父级分类ID，0表示顶级分类
     */
    private Long parentId;

    /**
     * 分类层级，从1开始
     */
    private Integer level;

    /**
     * 分类图标
     */
    private String icon;

    /**
     * 分类图片
     */
    private String image;

    /**
     * 分类描述
     */
    private String description;

    /**
     * 排序值，数值越大排序越靠前
     */
    private Integer sort;

    /**
     * 状态: 0-禁用, 1-启用
     */
    private Integer status;

    /**
     * 是否显示在首页: 0-否, 1-是
     */
    private Integer isShow;

    /**
     * 分类关键词，用于SEO
     */
    private String keywords;

    /**
     * 分类备注
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