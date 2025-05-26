package com.chennian.storytelling.common.enums;

import lombok.Getter;

/**
 * 客户跟进方式枚举
 * @author chennian
 */
@Getter
public enum CustomerFollowupTypeEnum {
    
    /** 电话跟进 */
    PHONE("1", "电话", "电话跟进"),
    
    /** 邮件跟进 */
    EMAIL("2", "邮件", "邮件跟进"),
    
    /** 拜访跟进 */
    VISIT("3", "拜访", "拜访跟进"),
    
    /** 其他方式跟进 */
    OTHER("4", "其他", "其他方式跟进");
    
    /** 编码 */
    private final String code;
    
    /** 名称 */
    private final String name;
    
    /** 描述 */
    private final String description;
    
    CustomerFollowupTypeEnum(String code, String name, String description) {
        this.code = code;
        this.name = name;
        this.description = description;
    }
    
    /**
     * 根据编码获取枚举
     * @param code 编码
     * @return 枚举实例
     */
    public static CustomerFollowupTypeEnum getByCode(String code) {
        for (CustomerFollowupTypeEnum type : values()) {
            if (type.getCode().equals(code)) {
                return type;
            }
        }
        return null;
    }
    
    /**
     * 根据名称获取枚举
     * @param name 名称
     * @return 枚举实例
     */
    public static CustomerFollowupTypeEnum getByName(String name) {
        for (CustomerFollowupTypeEnum type : values()) {
            if (type.getName().equals(name)) {
                return type;
            }
        }
        return null;
    }
    
    /**
     * 获取客户跟进方式分布数据
     * @return 客户跟进方式分布数据
     */
    public static int getDistributionCount(CustomerFollowupTypeEnum type) {
        switch (type) {
            case PHONE:
                return 45;
            case EMAIL:
                return 30;
            case VISIT:
                return 15;
            case OTHER:
                return 10;
            default:
                return 0;
        }
    }
}