package com.chennian.storytelling.bean.enums;

import lombok.Getter;

/**
 * 项目状态枚举
 *
 * @author storytelling
 * @date 2025-01-27
 */
@Getter
public enum ProjectStatusEnum {

    /**
     * 未开始
     */
    NOT_STARTED(0, "未开始"),

    /**
     * 进行中
     */
    IN_PROGRESS(1, "进行中"),

    /**
     * 已暂停
     */
    PAUSED(2, "已暂停"),

    /**
     * 已完成
     */
    COMPLETED(3, "已完成"),

    /**
     * 已取消
     */
    CANCELLED(4, "已取消"),

    /**
     * 已归档
     */
    ARCHIVED(6, "已归档");

    private final Integer code;
    private final String name;

    ProjectStatusEnum(Integer code, String name) {
        this.code = code;
        this.name = name;
    }

    /**
     * 根据code获取枚举
     */
    public static ProjectStatusEnum getByCode(Integer code) {
        if (code == null) {
            return null;
        }
        for (ProjectStatusEnum statusEnum : values()) {
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
        ProjectStatusEnum statusEnum = getByCode(code);
        return statusEnum != null ? statusEnum.getName() : "未知状态";
    }

}