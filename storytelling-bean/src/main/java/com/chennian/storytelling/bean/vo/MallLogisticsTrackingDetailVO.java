package com.chennian.storytelling.bean.vo;

import com.chennian.storytelling.bean.model.mall.MallLogisticsTrackingDetail;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

import java.time.LocalDateTime;
import java.util.List;

/**
 * 物流跟踪详情VO类
 * 
 * @author chennian
 * @date 2025-01-27
 */
@Data
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
public class MallLogisticsTrackingDetailVO extends MallLogisticsTrackingDetail {

    /**
     * 操作类型名称
     */
    private String operationTypeName;

    /**
     * 操作结果名称
     */
    private String operationResultName;

    /**
     * 关联的物流跟踪信息
     */
    private MallLogisticsTrackingVO trackingInfo;

    /**
     * 异常处理记录
     */
    private List<ExceptionHandleRecord> exceptionHandles;

    /**
     * 位置坐标信息
     */
    private LocationInfo locationInfo;

    /**
     * 异常处理记录
     */
    @Data
    public static class ExceptionHandleRecord {
        /**
         * 处理时间
         */
        private LocalDateTime handleTime;

        /**
         * 处理人
         */
        private String handler;

        /**
         * 处理方式
         */
        private String handleMethod;

        /**
         * 处理结果
         */
        private String handleResult;

        /**
         * 处理状态：1-处理中，2-已处理，3-无法处理
         */
        private Integer handleStatus;
    }

    /**
     * 位置坐标信息
     */
    @Data
    public static class LocationInfo {
        /**
         * 经度
         */
        private Double longitude;

        /**
         * 纬度
         */
        private Double latitude;

        /**
         * 详细地址
         */
        private String address;

        /**
         * 省份
         */
        private String province;

        /**
         * 城市
         */
        private String city;

        /**
         * 区县
         */
        private String district;

        /**
         * 街道
         */
        private String street;
    }
} 