package com.chennian.storytelling.bean.enums;

/**
 * 删除标志枚举
 * @author chennian
 * @date 2024-01-01
 */
public enum DelFlagEnum {
    
    NORMAL(0, "正常"),
    DELETED(2, "已删除");
    
    private final Integer code;
    private final String name;
    
    DelFlagEnum(Integer code, String name) {
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
    public static DelFlagEnum getByCode(Integer code) {
        if (code == null) {
            return null;
        }
        for (DelFlagEnum delFlagEnum : DelFlagEnum.values()) {
            if (delFlagEnum.getCode().equals(code)) {
                return delFlagEnum;
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
        DelFlagEnum delFlagEnum = getByCode(code);
        return delFlagEnum != null ? delFlagEnum.getName() : null;
    }
    
    /**
     * 判断是否正常（未删除）
     * @param code 编码
     * @return 是否正常
     */
    public static boolean isNormal(Integer code) {
        return NORMAL.getCode().equals(code);
    }
    
    /**
     * 判断是否已删除
     * @param code 编码
     * @return 是否已删除
     */
    public static boolean isDeleted(Integer code) {
        return DELETED.getCode().equals(code);
    }
}