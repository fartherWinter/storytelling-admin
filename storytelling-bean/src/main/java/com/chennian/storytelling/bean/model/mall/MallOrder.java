package com.chennian.storytelling.bean.model.mall;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.chennian.storytelling.bean.enums.DataStatusEnum;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
@EqualsAndHashCode(callSuper = false)
@ToString
@TableName("mall_order")
public class MallOrder implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 订单ID, 分片键
     */
    @TableId(value = "id", type = IdType.ASSIGN_ID)
    private Long id;

    /**
     * 订单编号, 唯一
     */
    private String orderSn;

    /**
     * 用户ID
     */
    private Long userId;

    /**
     * 订单总金额
     */
    private BigDecimal totalAmount;

    /**
     * 实际支付金额
     */
    private BigDecimal payAmount;

    /**
     * 运费金额
     */
    private BigDecimal freightAmount;

    /**
     * 支付方式: 1-支付宝, 2-微信支付, ...
     */
    private Integer payType;

    /**
     * 订单状态: 0->待付款；1->待发货；2->已发货；3->已完成；4->已关闭；5->无效订单
     */
    private Integer status;
    
    /**
     * 获取订单状态枚举
     */
    public DataStatusEnum getStatusEnum() {
        return DataStatusEnum.getByCode(this.status);
    }
    
    /**
     * 设置订单状态枚举
     */
    public void setStatusEnum(DataStatusEnum statusEnum) {
        this.status = statusEnum != null ? statusEnum.getCode() : null;
    }

    /**
     * 收货人姓名
     */
    private String receiverName;

    /**
     * 收货人电话
     */
    private String receiverPhone;

    /**
     * 省份/直辖市
     */
    private String receiverProvince;

    /**
     * 城市
     */
    private String receiverCity;

    /**
     * 区
     */
    private String receiverRegion;

    /**
     * 详细地址
     */
    private String receiverDetailAddress;

    /**
     * 订单备注
     */
    private String note;

    /**
     * 创建时间
     */
    private LocalDateTime createTime;

    /**
     * 支付时间
     */
    private LocalDateTime paymentTime;

    /**
     * 发货时间
     */
    private LocalDateTime deliveryTime;

    /**
     * 确认收货时间
     */
    private LocalDateTime receiveTime;

    /**
     * 评价时间
     */
    private LocalDateTime commentTime;

    /**
     * 更新时间
     */
    private LocalDateTime updateTime;
}