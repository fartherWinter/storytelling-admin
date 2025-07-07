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
 * 
 * @author chennian
 * @date 2025-01-27
 */
@Data
@EqualsAndHashCode(callSuper = false)
@ToString
@TableName("mall_logistics_tracking_detail")
public class MallLogisticsTrackingDetail implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 详情ID
     */
    @TableId(value = "id", type = IdType.ASSIGN_ID)
    private Long id;

    /**
     * 跟踪ID
     */
    private Long trackingId;

    /**
     * 物流单号
     */
    private String logisticsNo;

    /**
     * 轨迹发生时间
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
     * 操作类型：1-揽收，2-运输，3-派送，4-签收，5-异常
     */
    private Integer operationType;

    /**
     * 操作结果：1-成功，2-失败，3-异常
     */
    private Integer operationResult;

    /**
     * 异常原因
     */
    private String exceptionReason;

    /**
     * 处理建议
     */
    private String handleSuggestion;

    /**
     * 联系电话
     */
    private String contactPhone;

    /**
     * 联系人
     */
    private String contactPerson;

    /**
     * 备注
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

    /**
     * 创建人
     */
    private String createBy;

    /**
     * 更新人
     */
    private String updateBy;
} 