package com.chennian.storytelling.common.enums;

/**
 * @author by chennian
 * @date 2023/8/7 15:18
 */
public enum AppOrderType {
    ORDER_OBLIGATION("待付款", 31),
    ORDER_TO_BE_SHIPPED("商城已付款", 32),
    ORDER_CHANNEL_NOTIFY("已发送下单信息到渠道", 33),
    ORDER_CHANNEL_PAY("渠道已付款(待发货)", 34),
    ORDER_SHIPPED("已发货", 35),

    ORDER_SUCCESS("订单已完成成功(已收货)", 36),
    ORDER_OVER_TIME("过期未支付失败(过期未支付)", 37),
    ORDER_HAND_MOVEMENT("手动取消订单", 38),
    ORDER_PAY_FAIL("微信支付失败", 39),
    ORDER_REFUND("订单退款", 40),
    ORDER_ADVANCE_PAY("微信预支付成功", 41),
    ORDER_CANCEL("下单失败", 42),
    ORDER_TAKING("已接单", 63),
    ORDER_DEDUCTION("抵扣", 23),
    ORDER_RETURN("退还", 24),
    ORDER_RECHARGE("充值", 25),
    MODULE_PRODUCT("商品模块", 51),
    MODULE_CAKE("蛋糕模块", 52),
    MODULE_LIFE_EQUITY("生活券模块", 53),
    WX_PAY("微信支付", 81),
    ;

    private final String type;
    private final Integer method;

    AppOrderType(String type, Integer method) {
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
