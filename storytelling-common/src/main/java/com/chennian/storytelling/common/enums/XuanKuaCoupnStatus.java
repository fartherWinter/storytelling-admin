package com.chennian.storytelling.common.enums;

/**
 * @author by chennian
 * @date 2023/4/20 15:55
 */
public enum XuanKuaCoupnStatus {
    WEIJIHUO("N", "未激活"),
    YIJIHUO("Y", "已激活"),
    YISUODING("L", "已锁定"),
    YIDONGJIE("F", "已冻结"),
    YIFEIQI("T", "已废弃"),
    YIGUOQI("E", "已过期"),
    YIYONGWAN("U", "已用完");

    private final String code;
    private final String info;

    XuanKuaCoupnStatus(String code, String info) {
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
