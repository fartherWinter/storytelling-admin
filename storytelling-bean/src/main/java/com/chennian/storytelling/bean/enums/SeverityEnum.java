package com.chennian.storytelling.bean.enums;

/**
 * 严重程度枚举
 * @author chennian
 */
public enum SeverityEnum {
    
    LOW(1, "低"),
    MEDIUM(2, "中"),
    HIGH(3, "高"),
    CRITICAL(4, "严重");
    
    private final Integer code;
    private final String name;
    
    SeverityEnum(Integer code, String name) {
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
     * @return 枚举实例
     */
    public static SeverityEnum getByCode(Integer code) {
        if (code == null) {
            return null;
        }
        for (SeverityEnum severity : values()) {
            if (severity.getCode().equals(code)) {
                return severity;
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
        SeverityEnum severity = getByCode(code);
        return severity != null ? severity.getName() : null;
    }
    
    /**
     * 判断是否为严重级别
     * @param code 编码
     * @return 是否严重
     */
    public static boolean isCritical(Integer code) {
        return CRITICAL.getCode().equals(code);
    }
    
    /**
     * 判断是否为高严重程度或以上
     * @param code 编码
     * @return 是否高严重程度或以上
     */
    public static boolean isHighOrAbove(Integer code) {
        return code != null && code >= HIGH.getCode();
    }
}