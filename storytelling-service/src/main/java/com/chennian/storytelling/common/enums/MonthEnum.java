package com.chennian.storytelling.common.enums;

import lombok.Getter;

/**
 * 月份枚举
 * @author chennian
 */
@Getter
public enum MonthEnum {
    
    /** 一月 */
    JANUARY(1, "1月", 5),
    
    /** 二月 */
    FEBRUARY(2, "2月", 8),
    
    /** 三月 */
    MARCH(3, "3月", 12),
    
    /** 四月 */
    APRIL(4, "4月", 15),
    
    /** 五月 */
    MAY(5, "5月", 10),
    
    /** 六月 */
    JUNE(6, "6月", 18);
    
    /** 月份数值 */
    private final int value;
    
    /** 显示名称 */
    private final String name;
    
    /** 客户增长数量 */
    private final int growthCount;
    
    MonthEnum(int value, String name, int growthCount) {
        this.value = value;
        this.name = name;
        this.growthCount = growthCount;
    }
    
    /**
     * 根据月份值获取枚举
     * @param value 月份值
     * @return 枚举实例
     */
    public static MonthEnum getByValue(int value) {
        for (MonthEnum month : values()) {
            if (month.getValue() == value) {
                return month;
            }
        }
        return null;
    }
    
    /**
     * 根据名称获取枚举
     * @param name 名称
     * @return 枚举实例
     */
    public static MonthEnum getByName(String name) {
        for (MonthEnum month : values()) {
            if (month.getName().equals(name)) {
                return month;
            }
        }
        return null;
    }
}