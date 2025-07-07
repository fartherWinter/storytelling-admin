package com.chennian.storytelling.bean.model.mall;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * 物流费用规则实体类
 * 
 * @author chennian
 * @date 2025-01-27
 */
@Data
@EqualsAndHashCode(callSuper = false)
@ToString
@TableName("mall_freight_rule")
public class MallFreightRule implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 规则ID
     */
    @TableId(value = "id", type = IdType.ASSIGN_ID)
    private Long id;

    /**
     * 规则名称
     */
    private String ruleName;

    /**
     * 规则编码
     */
    private String ruleCode;

    /**
     * 配送方式ID
     */
    private Long deliveryMethodId;

    /**
     * 区域编码（可以是省、市、区县级别）
     */
    private String areaCode;

    /**
     * 计费类型：1-按重量，2-按件数，3-按体积，4-按距离
     */
    private Integer chargeType;

    /**
     * 首重/首件/首体积/首距离
     */
    private BigDecimal firstUnit;

    /**
     * 首重/首件/首体积/首距离费用
     */
    private BigDecimal firstFee;

    /**
     * 续重/续件/续体积/续距离单位
     */
    private BigDecimal additionalUnit;

    /**
     * 续重/续件/续体积/续距离费用
     */
    private BigDecimal additionalFee;

    /**
     * 最低消费
     */
    private BigDecimal minCharge;

    /**
     * 封顶费用（0表示不封顶）
     */
    private BigDecimal maxCharge;

    /**
     * 特殊时段加价规则（JSON格式）
     */
    private String timeExtraRule;

    /**
     * 天气加价规则（JSON格式）
     */
    private String weatherExtraRule;

    /**
     * 节假日加价规则（JSON格式）
     */
    private String holidayExtraRule;

    /**
     * 优先级（数字越小优先级越高）
     */
    private Integer priority;

    /**
     * 生效时间
     */
    private LocalDateTime effectiveTime;

    /**
     * 失效时间
     */
    private LocalDateTime expireTime;

    /**
     * 是否启用：0-禁用，1-启用
     */
    private Integer status;

    /**
     * 规则描述
     */
    private String description;

    /**
     * 创建时间
     */
    private LocalDateTime createTime;

    /**
     * 更新时间
     */
    private LocalDateTime updateTime;

    /**
     * 创建人
     */
    private String createBy;

    /**
     * 更新人
     */
    private String updateBy;
} 