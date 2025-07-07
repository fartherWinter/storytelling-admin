package com.chennian.storytelling.bean.vo;

import com.chennian.storytelling.bean.model.mall.MallFreightRule;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

/**
 * 物流费用规则VO类
 * 
 * @author chennian
 * @date 2025-01-27
 */
@Data
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
public class MallFreightRuleVO extends MallFreightRule {

    /**
     * 配送方式名称
     */
    private String deliveryMethodName;

    /**
     * 区域名称
     */
    private String areaName;

    /**
     * 计费类型名称
     */
    private String chargeTypeName;

    /**
     * 规则状态名称
     */
    private String statusName;

    /**
     * 特殊时段加价规则（解析后的对象）
     */
    private List<TimeExtraRule> timeExtraRules;

    /**
     * 天气加价规则（解析后的对象）
     */
    private List<WeatherExtraRule> weatherExtraRules;

    /**
     * 节假日加价规则（解析后的对象）
     */
    private List<HolidayExtraRule> holidayExtraRules;

    /**
     * 费用计算示例
     */
    private List<FreightCalculationExample> calculationExamples;

    /**
     * 规则适用范围描述
     */
    private String applicationScope;

    /**
     * 规则变更历史
     */
    private List<RuleChangeHistory> changeHistory;

    @Data
    public static class TimeExtraRule {
        /**
         * 时段开始时间（HH:mm格式）
         */
        private String startTime;

        /**
         * 时段结束时间（HH:mm格式）
         */
        private String endTime;

        /**
         * 加价金额
         */
        private BigDecimal extraFee;

        /**
         * 加价比例
         */
        private BigDecimal extraRate;

        /**
         * 适用日期（星期几，1-7）
         */
        private List<Integer> applicableDays;
    }

    @Data
    public static class WeatherExtraRule {
        /**
         * 天气类型：1-雨天，2-雪天，3-台风，4-高温，5-寒潮
         */
        private Integer weatherType;

        /**
         * 天气类型名称
         */
        private String weatherTypeName;

        /**
         * 加价金额
         */
        private BigDecimal extraFee;

        /**
         * 加价比例
         */
        private BigDecimal extraRate;

        /**
         * 天气等级（如暴雨等级）
         */
        private String weatherLevel;
    }

    @Data
    public static class HolidayExtraRule {
        /**
         * 节假日名称
         */
        private String holidayName;

        /**
         * 开始日期
         */
        private String startDate;

        /**
         * 结束日期
         */
        private String endDate;

        /**
         * 加价金额
         */
        private BigDecimal extraFee;

        /**
         * 加价比例
         */
        private BigDecimal extraRate;
    }

    @Data
    public static class FreightCalculationExample {
        /**
         * 示例描述
         */
        private String description;

        /**
         * 计算参数
         */
        private Map<String, Object> parameters;

        /**
         * 基础运费
         */
        private BigDecimal baseFee;

        /**
         * 额外费用明细
         */
        private List<ExtraFeeDetail> extraFees;

        /**
         * 最终运费
         */
        private BigDecimal totalFee;
    }

    @Data
    public static class ExtraFeeDetail {
        /**
         * 费用类型
         */
        private String feeType;

        /**
         * 费用金额
         */
        private BigDecimal amount;

        /**
         * 计算说明
         */
        private String calculation;
    }

    @Data
    public static class RuleChangeHistory {
        /**
         * 变更时间
         */
        private String changeTime;

        /**
         * 变更人
         */
        private String operator;

        /**
         * 变更类型
         */
        private String changeType;

        /**
         * 变更内容
         */
        private String changeContent;

        /**
         * 变更原因
         */
        private String changeReason;
    }
} 