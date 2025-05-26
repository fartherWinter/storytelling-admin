package com.chennian.storytelling.common.enums;

/**
 * @author by chennian
 * @date 2023/8/9 9:14
 */
public enum AppOrderPayType {
    WX_PAY("微信支付", 1),
    SCORE_PAY("积分支付", 2),
    WX_SCORE_PAY("微信-积分支付", 3),
    WX_ADVANCE_PAY("微信预支付", 4),
    PAY("支付", 21),
    REFUND("退款", 22);
    private final String type;
    private final Integer method;

    AppOrderPayType(String type, Integer method) {
        this.method = method;
        this.type = type;
    }

    public String getType() {
        return type;
    }

    public Integer getMethod() {
        return method;
    }
}
