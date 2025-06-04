package com.chennian.storytelling.bean.bo;

import lombok.Data;

import java.time.LocalDateTime;

/**
 * 优惠券搜索业务对象
 *
 * @author chennian
 * @date 2025-01-27
 */
@Data
public class MallCouponSearchBo {

    /**
     * 优惠券名称（模糊查询）
     */
    private String name;

    /**
     * 优惠券类型：1-满减券，2-折扣券，3-固定金额券
     */
    private Integer type;

    /**
     * 状态：0-未发布，1-已发布，2-已停用
     */
    private Integer status;

    /**
     * 创建时间开始
     */
    private LocalDateTime createTimeStart;

    /**
     * 创建时间结束
     */
    private LocalDateTime createTimeEnd;

    /**
     * 有效期开始时间
     */
    private LocalDateTime validStartTime;

    /**
     * 有效期结束时间
     */
    private LocalDateTime validEndTime;

    /**
     * 创建人
     */
    private String createBy;

    /**
     * 页码
     */
    private Integer pageNum = 1;

    /**
     * 每页大小
     */
    private Integer pageSize = 10;

}