package com.chennian.storytelling.bean.enums;

import lombok.Getter;

/**
 * 员工状态枚举
 *
 * @author storytelling
 * @date 2025-01-27
 */
@Getter
public enum EmployeeStatusEnum {

    /**
     * 在职
     */
    ACTIVE(1, "在职"),

    /**
     * 离职
     */
    RESIGNED(2, "离职"),

    /**
     * 停薪留职
     */
    UNPAID_LEAVE(3, "停薪留职");

    private final Integer code;
    private final String name;

    EmployeeStatusEnum(Integer code, String name) {
        this.code = code;
        this.name = name;
    }

    /**
     * 根据code获取枚举
     */
    public static EmployeeStatusEnum getByCode(Integer code) {
        if (code == null) {
            return null;
        }
        for (EmployeeStatusEnum statusEnum : values()) {
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
        EmployeeStatusEnum statusEnum = getByCode(code);
        return statusEnum != null ? statusEnum.getName() : "未知状态";
    }

}