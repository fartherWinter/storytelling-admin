package com.chennian.storytelling.bean.enums;

import lombok.Getter;

/**
 * 通用状态枚举
 *
 * @author storytelling
 * @date 2025-01-27
 */
@Getter
public enum CommonStatusEnum {

    /**
     * 禁用
     */
    DISABLED(0, "禁用"),

    /**
     * 启用
     */
    ENABLED(1, "启用"),

    /**
     * 删除
     */
    DELETED(2, "删除");

    private final Integer code;
    private final String name;

    CommonStatusEnum(Integer code, String name) {
        this.code = code;
        this.name = name;
    }

    /**
     * 根据code获取枚举
     */
    public static CommonStatusEnum getByCode(Integer code) {
        if (code == null) {
            return null;
        }
        for (CommonStatusEnum statusEnum : values()) {
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
        CommonStatusEnum statusEnum = getByCode(code);
        return statusEnum != null ? statusEnum.getName() : "未知状态";
    }

}