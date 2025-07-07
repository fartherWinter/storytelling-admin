package com.chennian.storytelling.bean.model;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * 库存预警实体类
 */
@Data
@EqualsAndHashCode(callSuper = false)
@ToString
@TableName("erp_inventory_alert")
public class InventoryAlert implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 预警ID
     */
    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

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
     * 仓库ID
     */
    private Long warehouseId;

    /**
     * 仓库名称
     */
    private String warehouseName;

    /**
     * 当前库存数量
     */
    private Integer currentStock;

    /**
     * 最小库存阈值
     */
    private Integer minStockThreshold;

    /**
     * 最大库存阈值
     */
    private Integer maxStockThreshold;

    /**
     * 预警类型：1-库存不足，2-库存过多，3-即将过期，4-已过期
     */
    private Integer alertType;

    /**
     * 预警级别：1-低，2-中，3-高，4-紧急
     */
    private Integer alertLevel;

    /**
     * 预警消息
     */
    private String alertMessage;

    /**
     * 建议操作
     */
    private String suggestedAction;

    /**
     * 预警状态：0-未处理，1-已处理，2-已忽略
     */
    private Integer status;

    /**
     * 处理人
     */
    private Long handledBy;

    /**
     * 处理时间
     */
    private LocalDateTime handledTime;

    /**
     * 处理备注
     */
    private String handledRemark;

    /**
     * 创建时间
     */
    private LocalDateTime createTime;

    /**
     * 更新时间
     */
    private LocalDateTime updateTime;
}