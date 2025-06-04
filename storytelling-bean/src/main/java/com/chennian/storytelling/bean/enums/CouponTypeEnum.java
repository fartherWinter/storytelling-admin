package com.chennian.storytelling.bean.enums;

import lombok.Getter;

/**
 * 优惠券类型枚举
 *
 * @author chennian
 * @date 2025-01-27
 */
@Getter
public enum CouponTypeEnum {

    /**
     * 满减券
     */
    FULL_REDUCTION(1, "满减券"),

    /**
     * 折扣券
     */
    DISCOUNT(2, "折扣券"),

    /**
     * 固定金额券
     */
    FIXED_AMOUNT(3, "固定金额券");

    private final Integer code;
    private final String name;

    CouponTypeEnum(Integer code, String name) {
        this.code = code;
        this.name = name;
    }

    /**
     * 根据code获取枚举
     */
    public static CouponTypeEnum getByCode(Integer code) {
        if (code == null) {
            return null;
        }
        for (CouponTypeEnum typeEnum : values()) {
            if (typeEnum.getCode().equals(code)) {
                return typeEnum;
            }
        }
        return null;
    }

    /**
     * 根据code获取名称
     */
    public static String getNameByCode(Integer code) {
        CouponTypeEnum typeEnum = getByCode(code);
        return typeEnum != null ? typeEnum.getName() : "未知类型";
    }

}