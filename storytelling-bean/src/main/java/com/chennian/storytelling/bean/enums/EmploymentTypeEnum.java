package com.chennian.storytelling.bean.enums;

import lombok.Getter;

/**
 * 用工类型枚举
 *
 * @author storytelling
 * @date 2025-01-27
 */
@Getter
public enum EmploymentTypeEnum {

    /**
     * 正式
     */
    FORMAL(1, "正式"),

    /**
     * 试用
     */
    PROBATION(2, "试用"),

    /**
     * 实习
     */
    INTERN(3, "实习"),

    /**
     * 兼职
     */
    PART_TIME(4, "兼职");

    private final Integer code;
    private final String name;

    EmploymentTypeEnum(Integer code, String name) {
        this.code = code;
        this.name = name;
    }

    /**
     * 根据code获取枚举
     */
    public static EmploymentTypeEnum getByCode(Integer code) {
        if (code == null) {
            return null;
        }
        for (EmploymentTypeEnum typeEnum : values()) {
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
        EmploymentTypeEnum typeEnum = getByCode(code);
        return typeEnum != null ? typeEnum.getName() : "未知类型";
    }

}