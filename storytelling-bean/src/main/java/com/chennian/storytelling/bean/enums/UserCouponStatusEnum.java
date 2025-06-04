package com.chennian.storytelling.bean.enums;

import lombok.Getter;

/**
 * 用户优惠券状态枚举
 *
 * @author chennian
 * @date 2025-01-27
 */
@Getter
public enum UserCouponStatusEnum {

    /**
     * 未使用
     */
    UNUSED(0, "未使用"),

    /**
     * 已使用
     */
    USED(1, "已使用"),

    /**
     * 已过期
     */
    EXPIRED(2, "已过期");

    private final Integer code;
    private final String name;

    UserCouponStatusEnum(Integer code, String name) {
        this.code = code;
        this.name = name;
    }

    /**
     * 根据code获取枚举
     */
    public static UserCouponStatusEnum getByCode(Integer code) {
        if (code == null) {
            return null;
        }
        for (UserCouponStatusEnum statusEnum : values()) {
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
        UserCouponStatusEnum statusEnum = getByCode(code);
        return statusEnum != null ? statusEnum.getName() : "未知状态";
    }

}