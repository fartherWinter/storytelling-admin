package com.chennian.storytelling.bean.vo;

import com.chennian.storytelling.bean.model.mall.MallLogisticsTracking;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

/**
 * 物流跟踪VO类
 * 
 * @author chennian
 * @date 2025-01-27
 */
@Data
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
public class MallLogisticsTrackingVO extends MallLogisticsTracking {

    /**
     * 订单信息
     */
    private Map<String, Object> orderInfo;

    /**
     * 子订单信息
     */
    private Map<String, Object> subOrderInfo;

    /**
     * 物流轨迹列表
     */
    private List<TrackDetail> trackDetails;

    /**
     * 物流时效信息
     */
    private DeliveryTimeInfo timeInfo;

    /**
     * 物流轨迹详情
     */
    @Data
    public static class TrackDetail {
        /**
         * 轨迹时间
         */
        private LocalDateTime trackTime;

        /**
         * 轨迹信息
         */
        private String trackInfo;

        /**
         * 当前位置
         */
        private String location;

        /**
         * 操作人员
         */
        private String operator;

        /**
         * 操作类型
         */
        private String operationType;
    }

    /**
     * 物流时效信息
     */
    @Data
    public static class DeliveryTimeInfo {
        /**
         * 下单时间
         */
        private LocalDateTime orderTime;

        /**
         * 发货时间
         */
        private LocalDateTime shipTime;

        /**
         * 预计送达时间
         */
        private LocalDateTime expectedTime;

        /**
         * 实际送达时间
         */
        private LocalDateTime actualTime;

        /**
         * 运输时长（小时）
         */
        private Integer transportDuration;

        /**
         * 是否超时
         */
        private Boolean isOvertime;

        /**
         * 超时原因
         */
        private String overtimeReason;
    }
} 