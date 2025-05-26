package com.chennian.storytelling.common.enums;

/**
 * @author by chennian
 * @date 2023/4/21 9:29
 */

public enum WxApi {
    /**
     * 微信
     */
    JSAPI_TICKET("https://api.weixin.qq.com/cgi-bin/ticket/getticket", "jspai_ticket获取"),
    /**
     * 微信登陆校验
     */
    AUTH_URL("https://api.weixin.qq.com/sns/jscode2session", "微信登录校验"),
    /**
     * 微信手机号
     */
    PHONE_NUMBER_URL("https://api.weixin.qq.com/wxa/business/getuserphonenumber?access_token=", "获取手机号"),
    /**
     * 获取微信token
     */
    ACCESS_TOKEN("https://api.weixin.qq.com/cgi-bin/token", "获取微信token"),
    /**
     * 微信native支付
     */
    WX_PAY_NATIVE("/v3/pay/partner/transactions/native", "微信native支付"),
    /**
     * 微信JSAPI支付
     */
    WX_PAY_JS("/v3/pay/transactions/jsapi", "微信jsp支付"),
    /**
     * 微信APP支付
     */
    WX_PAY_APP("/v3/pay/partner/transactions/app", "微信APP支付"),
    /**
     * 微信H5支付
     */
    WX_PAY_H5("/v3/pay/partner/transactions/app", "微信H5支付"),
    /**
     * 微信小程序支付
     */
    WX_PAY_APPLET("/v3/pay/partner/transactions/app", "微信小程序支付");
    private final String url;
    private final String info;


    WxApi(String code, String message) {
        this.url = code;
        this.info = message;
    }

    public String url() {
        return this.url;
    }

    public String info() {
        return this.info;
    }
}
