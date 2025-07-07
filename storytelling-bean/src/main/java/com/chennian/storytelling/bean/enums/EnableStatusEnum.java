package com.chennian.storytelling.bean.enums;

/**
 * 启用状态枚举
 * @author chennian
 * @date 2024-01-01
 */
public enum EnableStatusEnum {
    
    DISABLED(0, "禁用"),
    ENABLED(1, "启用");
    
    private final Integer code;
    private final String name;
    
    EnableStatusEnum(Integer code, String name) {
        this.code = code;
        this.name = name;
    }
    
    public Integer getCode() {
        return code;
    }
    
    public String getName() {
        return name;
    }
    
    /**
     * 根据code获取枚举
     * @param code 编码
     * @return 枚举
     */
    public static EnableStatusEnum getByCode(Integer code) {
        if (code == null) {
            return null;
        }
        for (EnableStatusEnum enableStatusEnum : EnableStatusEnum.values()) {
            if (enableStatusEnum.getCode().equals(code)) {
                return enableStatusEnum;
            }
        }
        return null;
    }
    
    /**
     * 根据code获取名称
     * @param code 编码
     * @return 名称
     */
    public static String getNameByCode(Integer code) {
        EnableStatusEnum enableStatusEnum = getByCode(code);
        return enableStatusEnum != null ? enableStatusEnum.getName() : null;
    }
    
    /**
     * 判断是否启用
     * @param code 编码
     * @return 是否启用
     */
    public static boolean isEnabled(Integer code) {
        return ENABLED.getCode().equals(code);
    }
    
    /**
     * 判断是否禁用
     * @param code 编码
     * @return 是否禁用
     */
    public static boolean isDisabled(Integer code) {
        return DISABLED.getCode().equals(code);
    }
}