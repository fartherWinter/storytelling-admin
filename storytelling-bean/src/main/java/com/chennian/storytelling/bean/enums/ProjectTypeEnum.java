package com.chennian.storytelling.bean.enums;

import lombok.Getter;

/**
 * 项目类型枚举
 *
 * @author storytelling
 * @date 2025-01-27
 */
@Getter
public enum ProjectTypeEnum {

    /**
     * 内部项目
     */
    INTERNAL(1, "内部项目"),

    /**
     * 外部项目
     */
    EXTERNAL(2, "外部项目"),

    /**
     * 研发项目
     */
    RESEARCH_DEVELOPMENT(3, "研发项目"),

    /**
     * 维护项目
     */
    MAINTENANCE(4, "维护项目");

    private final Integer code;
    private final String name;

    ProjectTypeEnum(Integer code, String name) {
        this.code = code;
        this.name = name;
    }

    /**
     * 根据code获取枚举
     */
    public static ProjectTypeEnum getByCode(Integer code) {
        if (code == null) {
            return null;
        }
        for (ProjectTypeEnum typeEnum : values()) {
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
        ProjectTypeEnum typeEnum = getByCode(code);
        return typeEnum != null ? typeEnum.getName() : "未知类型";
    }

}