package com.chennian.storytelling.bean.enums;

import lombok.Getter;

/**
 * 订单状态枚举
 *
 * @author storytelling
 * @date 2025-01-27
 */
@Getter
public enum OrderStatusEnum {

    /**
     * 待付款
     */
    PENDING_PAYMENT(0, "待付款"),

    /**
     * 待发货
     */
    PENDING_SHIPMENT(1, "待发货"),

    /**
     * 已发货
     */
    SHIPPED(2, "已发货"),

    /**
     * 已完成
     */
    COMPLETED(3, "已完成"),

    /**
     * 已关闭
     */
    CLOSED(4, "已关闭"),

    /**
     * 无效订单
     */
    INVALID(5, "无效订单");

    private final Integer code;
    private final String name;

    OrderStatusEnum(Integer code, String name) {
        this.code = code;
        this.name = name;
    }

    /**
     * 根据code获取枚举
     */
    public static OrderStatusEnum getByCode(Integer code) {
        if (code == null) {
            return null;
        }
        for (OrderStatusEnum statusEnum : values()) {
            if (statusEnum.getCode().equals(code)) {
                return statusEnum;
            }
        }
        return null;
    }

    /**
     * 根据code获取名称
     */
    public static String getNameByCode(Integer code) {
        OrderStatusEnum statusEnum = getByCode(code);
        return statusEnum != null ? statusEnum.getName() : "未知状态";
    }

}