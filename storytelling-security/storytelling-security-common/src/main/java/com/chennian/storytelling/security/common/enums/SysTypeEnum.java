package com.chennian.storytelling.security.common.enums;

/**
 * @author by chennian
 * @date 2023/3/19 19:58
 */
public enum SysTypeEnum {
    /**
     * 普通用户系统
     */
    ORDINARY(0),

    /**
     * 后台
     */
    ADMIN(1),
    ;

    private final Integer value;

    public Integer value() {
        return value;
    }

    SysTypeEnum(Integer value) {
        this.value = value;
    }
}
