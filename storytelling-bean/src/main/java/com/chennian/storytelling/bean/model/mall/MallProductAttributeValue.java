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
 * 商品属性值实体类
 * 
 * @author chennian
 * @date 2025-01-27
 */
@Data
@EqualsAndHashCode(callSuper = false)
@ToString
@TableName("mall_product_attribute_value")
public class MallProductAttributeValue implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 属性值ID
     */
    @TableId(value = "id", type = IdType.ASSIGN_ID)
    private Long id;

    /**
     * 商品ID
     */
    private Long productId;

    /**
     * 属性ID
     */
    private Long attributeId;

    /**
     * 属性名称
     */
    private String attributeName;

    /**
     * 属性值
     */
    private String attributeValue;

    /**
     * 属性值显示名称
     */
    private String attributeValueName;

    /**
     * 属性值图片
     */
    private String attributeValueImage;

    /**
     * 排序值，数值越大排序越靠前
     */
    private Integer sort;

    /**
     * 创建时间
     */
    private LocalDateTime createTime;

    /**
     * 更新时间
     */
    private LocalDateTime updateTime;
}