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
 * 商品收藏实体类
 * 
 * @author chennian
 * @date 2025-01-27
 */
@Data
@EqualsAndHashCode(callSuper = false)
@ToString
@TableName("mall_product_favorite")
public class MallProductFavorite implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 收藏ID
     */
    @TableId(value = "id", type = IdType.ASSIGN_ID)
    private Long id;

    /**
     * 用户ID
     */
    private Long userId;

    /**
     * 商品ID
     */
    private Long productId;

    /**
     * 商品名称
     */
    private String productName;

    /**
     * 商品主图
     */
    private String productImage;

    /**
     * 商品价格
     */
    private java.math.BigDecimal productPrice;

    /**
     * 收藏分组ID
     */
    private Long groupId;

    /**
     * 收藏分组名称
     */
    private String groupName;

    /**
     * 收藏备注
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
}