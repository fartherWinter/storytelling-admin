package com.chennian.storytelling.common.enums;

import lombok.Getter;

/**
 * 客户等级枚举
 * @author chennian
 */
@Getter
public enum CustomerLevelEnum {
    
    /** 普通客户 */
    NORMAL("normal", "普通", "普通客户"),
    
    /** 银牌客户 */
    SILVER("silver", "银牌", "银牌客户"),
    
    /** 金牌客户 */
    GOLD("gold", "金牌", "金牌客户"),
    
    /** 钻石客户 */
    DIAMOND("diamond", "钻石", "钻石客户");
    
    /** 编码 */
    private final String code;
    
    /** 名称 */
    private final String name;
    
    /** 描述 */
    private final String description;
    
    CustomerLevelEnum(String code, String name, String description) {
        this.code = code;
        this.name = name;
        this.description = description;
    }
    
    /**
     * 根据编码获取枚举
     * @param code 编码
     * @return 枚举实例
     */
    public static CustomerLevelEnum getByCode(String code) {
        for (CustomerLevelEnum level : values()) {
            if (level.getCode().equals(code)) {
                return level;
            }
        }
        return null;
    }
    
    /**
     * 根据名称获取枚举
     * @param name 名称
     * @return 枚举实例
     */
    public static CustomerLevelEnum getByName(String name) {
        for (CustomerLevelEnum level : values()) {
            if (level.getName().equals(name)) {
                return level;
            }
        }
        return null;
    }
    
    /**
     * 获取客户等级分布数据
     * @return 客户等级分布数据
     */
    public static int getDistributionCount(CustomerLevelEnum level) {
        switch (level) {
            case NORMAL:
                return 40;
            case SILVER:
                return 25;
            case GOLD:
                return 15;
            case DIAMOND:
                return 5;
            default:
                return 0;
        }
    }
}