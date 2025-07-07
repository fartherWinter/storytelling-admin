package com.chennian.storytelling.bean.enums;

import lombok.Getter;

/**
 * 性别枚举
 *
 * @author storytelling
 * @date 2025-01-27
 */
@Getter
public enum GenderEnum {

    /**
     * 男
     */
    MALE(1, "男"),

    /**
     * 女
     */
    FEMALE(2, "女");

    private final Integer code;
    private final String name;

    GenderEnum(Integer code, String name) {
        this.code = code;
        this.name = name;
    }

    /**
     * 根据code获取枚举
     */
    public static GenderEnum getByCode(Integer code) {
        if (code == null) {
            return null;
        }
        for (GenderEnum genderEnum : values()) {
            if (genderEnum.getCode().equals(code)) {
                return genderEnum;
            }
        }
        return null;
    }

    /**
     * 根据code获取名称
     */
    public static String getNameByCode(Integer code) {
        GenderEnum genderEnum = getByCode(code);
        return genderEnum != null ? genderEnum.getName() : "未知性别";
    }

}