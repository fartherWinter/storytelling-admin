package com.chennian.storytelling.bean.model;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.chennian.storytelling.bean.enums.EnableStatusEnum;
import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

/**
 * 产品信息表
 * @author chen
 * @TableName erp_product
 */
@TableName(value ="erp_product")
@Data
public class Product implements Serializable {
    /**
     * 产品ID
     */
    @TableId(value = "product_id", type = IdType.AUTO)
    private Long productId;

    /**
     * 产品编码
     */
    @TableField(value = "product_code")
    private String productCode;

    /**
     * 产品名称
     */
    @TableField(value = "product_name")
    private String productName;

    /**
     * 产品类别ID
     */
    @TableField(value = "category_id")
    private Long categoryId;

    /**
     * 规格型号
     */
    @TableField(value = "specification")
    private String specification;

    /**
     * 单位
     */
    @TableField(value = "unit")
    private String unit;

    /**
     * 采购价格
     */
    @TableField(value = "purchase_price")
    private BigDecimal purchasePrice;

    /**
     * 销售价格
     */
    @TableField(value = "sale_price")
    private BigDecimal salePrice;

    /**
     * 库存下限
     */
    @TableField(value = "min_stock")
    private Integer minStock;

    /**
     * 库存上限
     */
    @TableField(value = "max_stock")
    private Integer maxStock;

    /**
     * 状态
     */
    @TableField(value = "status")
    private Integer status;
    
    /**
     * 获取状态枚举
     */
    public EnableStatusEnum getStatusEnum() {
        return EnableStatusEnum.getByCode(this.status);
    }
    
    /**
     * 设置状态枚举
     */
    public void setStatusEnum(EnableStatusEnum statusEnum) {
        this.status = statusEnum != null ? Integer.valueOf(statusEnum.getCode()) : null;
    }

    /**
     * 备注
     */
    @TableField(value = "remark")
    private String remark;

    /**
     * 创建者
     */
    @TableField(value = "create_by")
    private String createBy;

    /**
     * 创建时间
     */
    @TableField(value = "create_time")
    private Date createTime;

    /**
     * 更新者
     */
    @TableField(value = "update_by")
    private String updateBy;

    /**
     * 更新时间
     */
    @TableField(value = "update_time")
    private Date updateTime;

    @TableField(exist = false)
    private static final long serialVersionUID = 1L;
}