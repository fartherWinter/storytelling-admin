package com.chennian.storytelling.common.enums;

import lombok.Getter;

/**
 * 客户类型枚举
 * @author chennian
 */
@Getter
public enum CustomerTypeEnum {
    
    /** 个人客户 */
    PERSONAL("personal", "个人", "个人客户"),
    
    /** 企业客户 */
    ENTERPRISE("enterprise", "企业", "企业客户"),
    
    /** 政府客户 */
    GOVERNMENT("government", "政府", "政府客户"),
    
    /** 其他类型客户 */
    OTHER("other", "其他", "其他类型客户");
    
    /** 编码 */
    private final String code;
    
    /** 名称 */
    private final String name;
    
    /** 描述 */
    private final String description;
    
    CustomerTypeEnum(String code, String name, String description) {
        this.code = code;
        this.name = name;
        this.description = description;
    }
    
    /**
     * 根据编码获取枚举
     * @param code 编码
     * @return 枚举实例
     */
    public static CustomerTypeEnum getByCode(String code) {
        for (CustomerTypeEnum type : values()) {
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
    public static CustomerTypeEnum getByName(String name) {
        for (CustomerTypeEnum type : values()) {
            if (type.getName().equals(name)) {
                return type;
            }
        }
        return null;
    }
    
    /**
     * 获取客户类型分布数据
     * @return 客户类型分布数据
     */
    public static int getDistributionCount(CustomerTypeEnum type) {
        switch (type) {
            case PERSONAL:
                return 25;
            case ENTERPRISE:
                return 45;
            case GOVERNMENT:
                return 10;
            case OTHER:
                return 5;
            default:
                return 0;
        }
    }
}