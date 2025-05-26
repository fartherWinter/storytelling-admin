package com.chennian.storytelling.common.enums;

/**
 * IP限流枚举类
 *
 * @author by chennian
 * @date 2023/7/17 10:18
 */
public enum IpLimitEnum {
    /**
     * 1秒可以请求多少次
     */
    IpLimit_10_1("10/1"),
    IpLimit_5_1("5/1"),

    IpLimit_1_1("1/1"),
    IpLimit_1_5("1/5"),
    IpLimit_1_10("1/10"),
    IpLimit_1_60("1/60"),
    ;

    private String limit;

    IpLimitEnum(final String limit) {
        this.limit = limit;
    }

    public String limit() {
        return this.limit;
    }
}
