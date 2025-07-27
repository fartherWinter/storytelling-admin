package com.chennian.storytelling.product.entity;

import com.baomidou.mybatisplus.annotation.*;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.List;

/**
 * 商品分类实体类
 * 
 * @author chennian
 * @since 2024-01-01
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@TableName("category")
public class Category implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 分类ID
     */
    @TableId(value = "category_id", type = IdType.ASSIGN_ID)
    private Long categoryId;

    /**
     * 分类名称
     */
    @NotBlank(message = "分类名称不能为空")
    @TableField("category_name")
    private String categoryName;

    /**
     * 分类编码
     */
    @NotBlank(message = "分类编码不能为空")
    @TableField("category_code")
    private String categoryCode;

    /**
     * 父分类ID
     */
    @NotNull(message = "父分类ID不能为空")
    @TableField("parent_id")
    private Long parentId;

    /**
     * 分类层级
     */
    @TableField("level")
    private Integer level;

    /**
     * 分类路径（如：1,2,3）
     */
    @TableField("path")
    private String path;

    /**
     * 分类图标
     */
    @TableField("icon")
    private String icon;

    /**
     * 分类图片
     */
    @TableField("image")
    private String image;

    /**
     * 分类描述
     */
    @TableField("description")
    private String description;

    /**
     * 分类状态：0-禁用，1-启用
     */
    @TableField("status")
    private Integer status;

    /**
     * 是否显示：0-不显示，1-显示
     */
    @TableField("is_show")
    private Integer isShow;

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
     * 子分类列表（非数据库字段）
     */
    @TableField(exist = false)
    private List<Category> children;

    /**
     * 父分类名称（非数据库字段）
     */
    @TableField(exist = false)
    private String parentName;

    /**
     * 商品数量（非数据库字段）
     */
    @TableField(exist = false)
    private Integer productCount;
}