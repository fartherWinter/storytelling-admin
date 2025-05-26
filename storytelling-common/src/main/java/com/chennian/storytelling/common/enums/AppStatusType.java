package com.chennian.storytelling.common.enums;

/**
 * @Author chennian
 * @Date 2023/10/27 9:51
 */
public enum AppStatusType {
    UP(1, "上架"),
    DOWN(0, "下架"),
    DELETE(-1, "删除"),
    ;

    private final Integer type;
    private final String method;

    AppStatusType(Integer type, String method) {
        this.method = method;
        this.type = type;
    }

    public Integer getType() {
        return type;
    }

    public String getMethod() {
        return method;
    }
}
