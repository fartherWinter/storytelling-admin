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
 * 商品属性实体类
 * 
 * @author chennian
 * @date 2025-01-27
 */
@Data
@EqualsAndHashCode(callSuper = false)
@ToString
@TableName("mall_product_attribute")
public class MallProductAttribute implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 属性ID
     */
    @TableId(value = "id", type = IdType.ASSIGN_ID)
    private Long id;

    /**
     * 属性名称
     */
    private String attributeName;

    /**
     * 属性编码
     */
    private String attributeCode;

    /**
     * 属性类型: 1-文本, 2-数字, 3-日期, 4-单选, 5-多选, 6-图片
     */
    private Integer attributeType;

    /**
     * 属性值列表，JSON格式存储可选值
     */
    private String attributeValues;

    /**
     * 属性单位
     */
    private String unit;

    /**
     * 是否必填: 0-否, 1-是
     */
    private Integer isRequired;

    /**
     * 是否用于搜索: 0-否, 1-是
     */
    private Integer isSearchable;

    /**
     * 是否用于筛选: 0-否, 1-是
     */
    private Integer isFilterable;

    /**
     * 是否显示在商品详情: 0-否, 1-是
     */
    private Integer isShowInDetail;

    /**
     * 排序值，数值越大排序越靠前
     */
    private Integer sort;

    /**
     * 状态: 0-禁用, 1-启用
     */
    private Integer status;

    /**
     * 属性描述
     */
    private String description;

    /**
     * 属性备注
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