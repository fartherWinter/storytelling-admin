package com.chennian.storytelling.bean.enums;

import lombok.Getter;

/**
 * 优惠券状态枚举
 *
 * @author chennian
 * @date 2025-01-27
 */
@Getter
public enum CouponStatusEnum {

    /**
     * 未发布
     */
    UNPUBLISHED(0, "未发布"),

    /**
     * 已发布
     */
    PUBLISHED(1, "已发布"),

    /**
     * 已停用
     */
    DISABLED(2, "已停用");

    private final Integer code;
    private final String name;

    CouponStatusEnum(Integer code, String name) {
        this.code = code;
        this.name = name;
    }

    /**
     * 根据code获取枚举
     */
    public static CouponStatusEnum getByCode(Integer code) {
        if (code == null) {
            return null;
        }
        for (CouponStatusEnum statusEnum : values()) {
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
        CouponStatusEnum statusEnum = getByCode(code);
        return statusEnum != null ? statusEnum.getName() : "未知状态";
    }

}