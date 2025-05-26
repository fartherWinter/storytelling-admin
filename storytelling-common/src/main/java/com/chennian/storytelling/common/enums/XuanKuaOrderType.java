package com.chennian.storytelling.common.enums;

/**
 * @Author chennian
 * @Date 2023/8/11 15:13
 */
public enum XuanKuaOrderType {
    MOVIE("M", "电影"),

    MALL("S", "商城"),
    FINE_FOOD("MS", "美食"),
    LIFE_VOUCHER("G", "生活券"),
    DRAMA("D", "演出");

    private final String code;
    private final String info;

    XuanKuaOrderType(String code, String info) {
        this.code = code;
        this.info = info;
    }

    public String getCode() {
        return code;
    }

    public String getInfo() {
        return info;
    }
}
