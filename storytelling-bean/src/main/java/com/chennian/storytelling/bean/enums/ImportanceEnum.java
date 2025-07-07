package com.chennian.storytelling.bean.enums;

/**
 * 重要性枚举
 * @author chennian
 */
public enum ImportanceEnum {
    
    LOW(1, "低"),
    MEDIUM(2, "中"),
    HIGH(3, "高"),
    CRITICAL(4, "关键");
    
    private final Integer code;
    private final String name;
    
    ImportanceEnum(Integer code, String name) {
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
    public static ImportanceEnum getByCode(Integer code) {
        if (code == null) {
            return null;
        }
        for (ImportanceEnum importance : values()) {
            if (importance.getCode().equals(code)) {
                return importance;
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
        ImportanceEnum importance = getByCode(code);
        return importance != null ? importance.getName() : null;
    }
    
    /**
     * 判断是否为关键重要性
     * @param code 编码
     * @return 是否关键
     */
    public static boolean isCritical(Integer code) {
        return CRITICAL.getCode().equals(code);
    }
    
    /**
     * 判断是否为高重要性或以上
     * @param code 编码
     * @return 是否高重要性或以上
     */
    public static boolean isHighOrAbove(Integer code) {
        return code != null && code >= HIGH.getCode();
    }
}