package com.chennian.storytelling.bean.vo;

import com.chennian.storytelling.bean.model.mall.MallDeliveryMethod;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

/**
 * 物流配送方式VO类
 * 
 * @author chennian
 * @date 2025-01-27
 */
@Data
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
public class MallDeliveryMethodVO extends MallDeliveryMethod {

    /**
     * 配送类型名称
     */
    private String methodTypeName;

    /**
     * 配送范围名称
     */
    private String deliveryScopeName;

    /**
     * 配送时间类型名称
     */
    private String deliveryTimeTypeName;

    /**
     * 特殊区域加价规则
     */
    private List<AreaPriceRule> areaPriceRules;

    /**
     * 配送时间规则
     */
    private List<TimeRule> timeRules;

    /**
     * 使用统计
     */
    private UsageStatistics usageStats;

    /**
     * 支持配送的区域列表
     */
    private List<String> supportedAreas;

    /**
     * 不支持配送的区域列表
     */
    private List<String> unsupportedAreas;

    /**
     * 特殊区域加价规则
     */
    @Data
    public static class AreaPriceRule {
        /**
         * 区域编码
         */
        private String areaCode;

        /**
         * 区域名称
         */
        private String areaName;

        /**
         * 加价金额
         */
        private BigDecimal extraPrice;

        /**
         * 加价说明
         */
        private String extraDesc;
    }

    /**
     * 配送时间规则
     */
    @Data
    public static class TimeRule {
        /**
         * 时间段编码
         */
        private String timeCode;

        /**
         * 时间段名称
         */
        private String timeName;

        /**
         * 开始时间（HH:mm）
         */
        private String startTime;

        /**
         * 结束时间（HH:mm）
         */
        private String endTime;

        /**
         * 额外费用
         */
        private BigDecimal extraFee;

        /**
         * 是否可用：0-不可用，1-可用
         */
        private Integer available;

        /**
         * 配送说明
         */
        private String timeDesc;
    }

    /**
     * 使用统计
     */
    @Data
    public static class UsageStatistics {
        /**
         * 使用次数
         */
        private Long usageCount;

        /**
         * 总配送费用
         */
        private BigDecimal totalFreight;

        /**
         * 平均配送时长（小时）
         */
        private Double avgDeliveryHours;

        /**
         * 准时率
         */
        private Double onTimeRate;

        /**
         * 好评率
         */
        private Double goodRate;

        /**
         * 月度统计
         */
        private Map<String, MonthlyStats> monthlyStats;
    }

    /**
     * 月度统计
     */
    @Data
    public static class MonthlyStats {
        /**
         * 使用次数
         */
        private Long usageCount;

        /**
         * 配送费用
         */
        private BigDecimal freight;

        /**
         * 准时率
         */
        private Double onTimeRate;

        /**
         * 好评率
         */
        private Double goodRate;
    }
} 