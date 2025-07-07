package com.chennian.storytelling.bean.vo;

import com.chennian.storytelling.bean.model.mall.MallDeliveryArea;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

import java.util.List;

/**
 * 物流区域VO类
 * 
 * @author chennian
 * @date 2025-01-27
 */
@Data
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
public class MallDeliveryAreaVO extends MallDeliveryArea {

    /**
     * 子区域列表
     */
    private List<MallDeliveryAreaVO> children;

    /**
     * 父区域名称
     */
    private String parentName;

    /**
     * 区域全称（省市区）
     */
    private String fullName;

    /**
     * 区域级别名称
     */
    private String areaLevelName;

    /**
     * 区域类型名称
     */
    private String areaTypeName;

    /**
     * 配送范围描述
     */
    private String deliveryScope;

    /**
     * 预计配送时间范围
     */
    private String deliveryTimeRange;

    /**
     * 配送费用描述
     */
    private String freightDesc;

    /**
     * 是否为叶子节点
     */
    private Boolean isLeaf;

    /**
     * 区域路径（完整的父子层级关系）
     */
    private String treePath;

    /**
     * 关联的配送方式列表
     */
    private List<String> deliveryMethods;

    /**
     * 区域统计信息
     */
    private AreaStatistics statistics;

    @Data
    public static class AreaStatistics {
        /**
         * 订单总数
         */
        private Integer orderCount;

        /**
         * 配送成功率
         */
        private Double deliverySuccessRate;

        /**
         * 平均配送时长（小时）
         */
        private Double avgDeliveryHours;

        /**
         * 投诉率
         */
        private Double complaintRate;

        /**
         * 活跃商家数
         */
        private Integer activeShopCount;
    }
} 