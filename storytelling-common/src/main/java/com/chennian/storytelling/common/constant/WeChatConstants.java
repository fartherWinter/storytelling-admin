package com.chennian.storytelling.common.constant;

/**
 * @author by chennian
 * @date 2023/3/29 9:54
 */
public class WeChatConstants {
    /**
     * 登录认证url
     */
    public static final String WE_LOGIN_URL = "https://api.weixin.qq.com/sns/jscode2session";
    /**
     * 获取access_token
     */
    public static final String WE_ACCESS_TOKEN="https://api.weixin.qq.com/cgi-bin/token";
    /**
     * 微信AppId
     */
    public static final String APP_ID = "";
    /**
     * 微信AppSecret
     */
    public static final String APP_SECRET = "";
    /**
     * 微信支付评价接口
     */
    public static final String WE_PAY_COMMENT_URL = "https://api.weixin.qq.com/wxaapi/comment/mpcommentlist/get";
}
