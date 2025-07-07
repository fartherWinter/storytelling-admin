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
 * 物流跟踪详情实体类
 */
@Data
@EqualsAndHashCode(callSuper = false)
@ToString
@TableName("mall_logistics_tracking_detail")
public class LogisticsTrackingDetail implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 详情记录ID
     */
    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    /**
     * 物流跟踪ID
     */
    private Long trackingId;

    /**
     * 运单号
     */
    private String trackingNumber;

    /**
     * 轨迹时间
     */
    private LocalDateTime trackTime;

    /**
     * 轨迹描述
     */
    private String description;

    /**
     * 所在地点
     */
    private String location;

    /**
     * 操作类型：1-揽收，2-运输，3-到达，4-派送，5-签收，6-异常
     */
    private Integer operationType;

    /**
     * 操作人员
     */
    private String operator;

    /**
     * 联系电话
     */
    private String contactPhone;

    /**
     * 备注
     */
    private String remark;

    /**
     * 创建时间
     */
    private LocalDateTime createTime;
}