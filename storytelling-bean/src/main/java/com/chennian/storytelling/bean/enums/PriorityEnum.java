package com.chennian.storytelling.bean.enums;

/**
 * 优先级枚举
 * @author chennian
 */
public enum PriorityEnum {
    
    LOW(1, "低"),
    MEDIUM(2, "中"),
    HIGH(3, "高"),
    URGENT(4, "紧急");
    
    private final Integer code;
    private final String name;
    
    PriorityEnum(Integer code, String name) {
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
    public static PriorityEnum getByCode(Integer code) {
        if (code == null) {
            return null;
        }
        for (PriorityEnum priority : values()) {
            if (priority.getCode().equals(code)) {
                return priority;
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
        PriorityEnum priority = getByCode(code);
        return priority != null ? priority.getName() : null;
    }
    
    /**
     * 判断是否为紧急优先级
     * @param code 编码
     * @return 是否紧急
     */
    public static boolean isUrgent(Integer code) {
        return URGENT.getCode().equals(code);
    }
    
    /**
     * 判断是否为高优先级或以上
     * @param code 编码
     * @return 是否高优先级或以上
     */
    public static boolean isHighOrAbove(Integer code) {
        return code != null && code >= HIGH.getCode();
    }
}