package com.chennian.storytelling.bean.enums;

/**
 * 有效性状态枚举
 * @author chennian
 */
public enum ValidStatusEnum {
    
    INVALID(0, "无效"),
    VALID(1, "有效"),
    EXPIRED(2, "已过期"),
    PENDING(3, "待生效");
    
    private final Integer code;
    private final String name;
    
    ValidStatusEnum(Integer code, String name) {
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
    public static ValidStatusEnum getByCode(Integer code) {
        if (code == null) {
            return null;
        }
        for (ValidStatusEnum status : values()) {
            if (status.getCode().equals(code)) {
                return status;
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
        ValidStatusEnum status = getByCode(code);
        return status != null ? status.getName() : null;
    }
    
    /**
     * 判断是否有效
     * @param code 编码
     * @return 是否有效
     */
    public static boolean isValid(Integer code) {
        return VALID.getCode().equals(code);
    }
    
    /**
     * 判断是否无效
     * @param code 编码
     * @return 是否无效
     */
    public static boolean isInvalid(Integer code) {
        return INVALID.getCode().equals(code);
    }
    
    /**
     * 判断是否已过期
     * @param code 编码
     * @return 是否已过期
     */
    public static boolean isExpired(Integer code) {
        return EXPIRED.getCode().equals(code);
    }
    
    /**
     * 判断是否可用（有效或待生效）
     * @param code 编码
     * @return 是否可用
     */
    public static boolean isAvailable(Integer code) {
        return VALID.getCode().equals(code) || PENDING.getCode().equals(code);
    }
}