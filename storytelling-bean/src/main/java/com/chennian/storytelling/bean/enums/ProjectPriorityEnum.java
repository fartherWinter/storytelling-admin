package com.chennian.storytelling.bean.enums;

import lombok.Getter;

/**
 * 项目优先级枚举
 *
 * @author storytelling
 * @date 2025-01-27
 */
@Getter
public enum ProjectPriorityEnum {

    /**
     * 低
     */
    LOW(1, "低"),

    /**
     * 中
     */
    MEDIUM(2, "中"),

    /**
     * 高
     */
    HIGH(3, "高"),

    /**
     * 紧急
     */
    URGENT(4, "紧急");

    private final Integer code;
    private final String name;

    ProjectPriorityEnum(Integer code, String name) {
        this.code = code;
        this.name = name;
    }

    /**
     * 根据code获取枚举
     */
    public static ProjectPriorityEnum getByCode(Integer code) {
        if (code == null) {
            return null;
        }
        for (ProjectPriorityEnum priorityEnum : values()) {
            if (priorityEnum.getCode().equals(code)) {
                return priorityEnum;
            }
        }
        return null;
    }

    /**
     * 根据code获取名称
     */
    public static String getNameByCode(Integer code) {
        ProjectPriorityEnum priorityEnum = getByCode(code);
        return priorityEnum != null ? priorityEnum.getName() : "未知优先级";
    }

}