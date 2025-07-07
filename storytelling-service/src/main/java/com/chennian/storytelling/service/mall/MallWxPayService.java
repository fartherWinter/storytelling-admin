package com.chennian.storytelling.service.mall;

import com.chennian.storytelling.bean.model.mall.MallOrder;

/**
 * 商城微信支付Service接口
 * 
 * @author chennian
 * @date 2025-01-27
 */
public interface MallWxPayService {
    
    /**
     * 创建微信支付订单 - JSAPI方式（小程序/公众号）
     * 
     * @param orderId 订单ID
     * @param openId 用户openId
     * @return 预支付交易会话标识（prepay_id）
     */
    String createJsApiPayOrder(Long orderId, String openId);
    
    /**
     * 创建微信支付订单 - Native方式（扫码支付）
     * 
     * @param orderId 订单ID
     * @return 二维码链接
     */
    String createNativePayOrder(Long orderId);
    
    /**
     * 查询微信支付订单状态
     * 
     * @param orderSn 商户订单号
     * @return 支付状态
     */
    String queryPayOrderStatus(String orderSn);
    
    /**
     * 关闭微信支付订单
     * 
     * @param orderSn 商户订单号
     * @return 是否成功
     */
    boolean closePayOrder(String orderSn);
    
    /**
     * 处理微信支付回调
     * 
     * @param requestBody 回调请求体
     * @param signature 签名
     * @param timestamp 时间戳
     * @param nonce 随机字符串
     * @return 处理结果
     */
    String handlePayCallback(String requestBody, String signature, String timestamp, String nonce);
}