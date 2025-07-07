package com.chennian.storytelling.bean.enums;

import lombok.Getter;

/**
 * 菜单类型枚举
 *
 * @author storytelling
 * @date 2025-01-27
 */
@Getter
public enum MenuTypeEnum {

    /**
     * 目录
     */
    DIRECTORY(0, "目录"),

    /**
     * 菜单
     */
    MENU(1, "菜单"),

    /**
     * 按钮
     */
    BUTTON(2, "按钮");

    private final Integer code;
    private final String name;

    MenuTypeEnum(Integer code, String name) {
        this.code = code;
        this.name = name;
    }

    /**
     * 根据code获取枚举
     */
    public static MenuTypeEnum getByCode(Integer code) {
        if (code == null) {
            return null;
        }
        for (MenuTypeEnum typeEnum : values()) {
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
        MenuTypeEnum typeEnum = getByCode(code);
        return typeEnum != null ? typeEnum.getName() : "未知类型";
    }

}