package com.chennian.storytelling.bean.enums;

/**
 * 通用级别枚举
 * @author chennian
 */
public enum LevelEnum {
    
    LEVEL_1(1, "一级"),
    LEVEL_2(2, "二级"),
    LEVEL_3(3, "三级"),
    LEVEL_4(4, "四级"),
    LEVEL_5(5, "五级");
    
    private final Integer code;
    private final String name;
    
    LevelEnum(Integer code, String name) {
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
    public static LevelEnum getByCode(Integer code) {
        if (code == null) {
            return null;
        }
        for (LevelEnum level : values()) {
            if (level.getCode().equals(code)) {
                return level;
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
        LevelEnum level = getByCode(code);
        return level != null ? level.getName() : null;
    }
    
    /**
     * 判断是否为高级别（三级及以上）
     * @param code 编码
     * @return 是否高级别
     */
    public static boolean isHighLevel(Integer code) {
        return code != null && code >= LEVEL_3.getCode();
    }
    
    /**
     * 判断是否为顶级（五级）
     * @param code 编码
     * @return 是否顶级
     */
    public static boolean isTopLevel(Integer code) {
        return LEVEL_5.getCode().equals(code);
    }
}