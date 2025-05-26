package com.chennian.storytelling.common.constant;

public class CUCCApiConstant {

    /**
     * 联通短信账号
     */
    public static final String ACCOUNT = "";

    /**
     * 联通短信账号
     */
    public static final String PASSWORD = "";

    /**
     * 虚拟接入码
     */
    public static final String FILM_ID = "";

    /**
     * 短信签名
     */
    public static final String SIGN_NAME = "";


    /**
     * 短信地址
     */
    public static final String MSG_URL = "";
    /**
     * 上行报告接口
     */
    public static final String MO_SM_URL="";
    /**
     * 短信验证码模板
     */
    public static final String CODE_TEMPLATE = "【商城】验证码 ${code} (十分钟内有效)，如非本人操作，请忽略本短信。";
    public static final String CODE_TEMPLATE_SAAS = "【${smsSignature}】验证码 ${code} (十分钟内有效)，如非本人操作，请忽略本短信。";

    /**
     * 购票成功短信模板
     */
    public static final String SUCCESS_TEMPLATE_SAAS = "【${smsSignature}】电影购票成功短信：您已成功订票：${movieinfo}, 时间：${showTime}, 影院：${cinemaName}, 坐位：${seatInfo} 取票码：${ticketinfo} 。";


    /**
     * 购票失败短信模板
     */
    public static final String FAIL_TEMPLATE_SAAS = "【${smsSignature}】尊敬的用户您好，很抱歉通知到您，经过漫长的等待，您购买的：${movieinfo}出票失败了，很感谢您的耐心等待，建议您更换影院购票！";

    /**
     * 生活券直充成功短信模板
     */
    public static final String LIFE_SUCCESS_TEMPLATE = "【天河点播】视听会员购买成功短信：您已成功为${rechargeAccount}充值了${productName} 。";
    public static final String LIFE_SUCCESS_TEMPLATE_SAAS = "【${smsSignature}】视听会员购买成功短信：您已成功为${rechargeAccount}充值了${productName} 。";


    /**
     * 生活券直充失败退款短信模板
     */
    public static final String LIFE_REFUND_TEMPLATE = "【天河点播】尊敬的用户您好，很抱歉通知到您，经过漫长的等待，您购买的：${productName}充值失败了，很感谢您的耐心等待，建议您更换产品充值！";
    public static final String LIFE_REFUND_TEMPLATE_SAAS = "【${smsSignature}】尊敬的用户您好，很抱歉通知到您，经过漫长的等待，您购买的：${productName}充值失败了，很感谢您的耐心等待，建议您更换产品充值！";


    /**
     * 接单提醒
     */
    public static final String MALL_ORDER_RECEIVE_SAAS = "【${smsSignature}】商户：${merchantName}有新订单：${orderCode}，请注意处理。";

    public static final String MALL_ORDER_SEND_SAAS = "【${smsSignature}】商户：${merchantName},订单：${orderCode}，已发货 快递单号为：${expressNo}。";

    public static final String MALL_ORDER_CANCEL_SAAS = "【${smsSignature}】商户：${merchantName},订单：${orderCode}，已被商户取消，款项稍后会原路退回。";

}
