package com.chennian.storytelling.bean.vo;

import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Map;

/**
 * 移动端产品VO
 * 
 * @author chennian
 * @date 2023/12/15
 */
@Data
public class MobileProductVO implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 产品ID
     */
    private Long productId;

    /**
     * 产品名称
     */
    private String productName;

    /**
     * 产品编码
     */
    private String productCode;

    /**
     * 产品分类
     */
    private String category;

    /**
     * 产品图片URL
     */
    private String imageUrl;

    /**
     * 销售价格
     */
    private BigDecimal price;

    /**
     * 库存数量
     */
    private Integer stock;

    /**
     * 产品状态
     */
    private String status;

    /**
     * 简短描述
     */
    private String briefDescription;

    /**
     * 销售统计
     */
    private Map<String, Object> salesStats;
}