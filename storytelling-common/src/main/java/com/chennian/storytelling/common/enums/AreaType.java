package com.chennian.storytelling.common.enums;

/**
 * 区域层级数据
 *
 * @author by chennian
 * @date 2023/6/13 15:43
 */
public enum AreaType {
    COUNTRY(1, "国家层级"),
    PROVINCE(2, "省级层级"),
    CITY(3, "市级层级"),
    DISTRICT(4,"区级层次");


    private final Integer code;
    private final String message;

    AreaType(Integer code, String message) {
        this.code = code;
        this.message = message;
    }

    public Integer getCode() {
        return this.code;
    }

    public String getMessage() {
        return this.message;
    }
}
