package com.chennian.storytelling.common.enums;

import lombok.Getter;

/**
 * 客户地区枚举
 * @author chennian
 */
@Getter
public enum CustomerRegionEnum {
    
    /** 北京 */
    BEIJING("beijing", "北京", "北京地区"),
    
    /** 上海 */
    SHANGHAI("shanghai", "上海", "上海地区"),
    
    /** 广州 */
    GUANGZHOU("guangzhou", "广州", "广州地区"),
    
    /** 深圳 */
    SHENZHEN("shenzhen", "深圳", "深圳地区"),
    
    /** 其他地区 */
    OTHER("other", "其他", "其他地区");
    
    /** 编码 */
    private final String code;
    
    /** 名称 */
    private final String name;
    
    /** 描述 */
    private final String description;
    
    CustomerRegionEnum(String code, String name, String description) {
        this.code = code;
        this.name = name;
        this.description = description;
    }
    
    /**
     * 根据编码获取枚举
     * @param code 编码
     * @return 枚举实例
     */
    public static CustomerRegionEnum getByCode(String code) {
        for (CustomerRegionEnum region : values()) {
            if (region.getCode().equals(code)) {
                return region;
            }
        }
        return null;
    }
    
    /**
     * 根据名称获取枚举
     * @param name 名称
     * @return 枚举实例
     */
    public static CustomerRegionEnum getByName(String name) {
        for (CustomerRegionEnum region : values()) {
            if (region.getName().equals(name)) {
                return region;
            }
        }
        return null;
    }
    
    /**
     * 获取客户地区分布数据
     * @return 客户地区分布数据
     */
    public static int getDistributionCount(CustomerRegionEnum region) {
        switch (region) {
            case BEIJING:
                return 20;
            case SHANGHAI:
                return 18;
            case GUANGZHOU:
                return 15;
            case SHENZHEN:
                return 12;
            case OTHER:
                return 20;
            default:
                return 0;
        }
    }
}