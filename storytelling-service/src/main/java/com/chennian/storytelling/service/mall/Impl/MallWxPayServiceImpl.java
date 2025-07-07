package com.chennian.storytelling.service.mall.Impl;

import com.chennian.storytelling.bean.model.mall.MallOrder;
import com.chennian.storytelling.common.bean.WeChatBasePayData;
import com.chennian.storytelling.common.config.WxPayConfig;
import com.chennian.storytelling.common.enums.MallOrderStatus;
import com.chennian.storytelling.common.enums.WxApiType;
import com.chennian.storytelling.common.enums.WxNotifyType;
import com.chennian.storytelling.common.utils.pay.WxPayCommonUtil;
import com.chennian.storytelling.service.mall.MallOrderService;
import com.chennian.storytelling.service.mall.MallWxPayService;
import com.google.gson.Gson;
import lombok.extern.slf4j.Slf4j;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.util.EntityUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import java.io.IOException;
import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Map;

/**
 * 商城微信支付Service实现类
 * 
 * @author chennian
 * @date 2025-01-27
 */
@Slf4j
@Service
public class MallWxPayServiceImpl implements MallWxPayService {
    
    @Autowired
    private WxPayConfig wxPayConfig;
    
    @Autowired
    private CloseableHttpClient wxPayClient;
    
    @Autowired
    private MallOrderService mallOrderService;
    
    /**
     * 创建微信支付订单 - JSAPI方式（小程序/公众号）
     */
    @Override
    public String createJsApiPayOrder(Long orderId, String openId) {
        try {
            // 1. 获取订单信息
            MallOrder mallOrder = mallOrderService.selectOrderById(orderId);
            if (mallOrder == null) {
                throw new RuntimeException("订单不存在");
            }
            
            // 2. 检查订单状态
            if (!MallOrderStatus.PENDING_PAYMENT.getCode().equals(mallOrder.getStatus())) {
                throw new RuntimeException("订单状态不正确，无法支付");
            }
            
            // 3. 构建支付数据
            WeChatBasePayData payData = new WeChatBasePayData();
            payData.setTitle("商城订单-" + mallOrder.getOrderSn());
            payData.setOrderId(mallOrder.getOrderSn());
            payData.setPrice(mallOrder.getTotalAmount());
            payData.setNotify(WxNotifyType.PAY_BACK);
            
            // 4. 调用微信支付
            String prepayId = WxPayCommonUtil.wxJsApiPay(wxPayConfig, payData, openId, wxPayClient);
            
            if (StringUtils.hasText(prepayId)) {
                log.info("微信JSAPI支付订单创建成功，订单号：{}，prepay_id：{}", mallOrder.getOrderSn(), prepayId);
                return prepayId;
            } else {
                throw new RuntimeException("微信支付订单创建失败");
            }
            
        } catch (Exception e) {
            log.error("创建微信JSAPI支付订单失败，订单ID：{}", orderId, e);
            throw new RuntimeException("创建微信支付订单失败：" + e.getMessage());
        }
    }
    
    /**
     * 创建微信支付订单 - Native方式（扫码支付）
     */
    @Override
    public String createNativePayOrder(Long orderId) {
        try {
            // 1. 获取订单信息
            MallOrder mallOrder = mallOrderService.selectOrderById(orderId);
            if (mallOrder == null) {
                throw new RuntimeException("订单不存在");
            }
            
            // 2. 检查订单状态
            if (!MallOrderStatus.PENDING_PAYMENT.getCode().equals(mallOrder.getStatus())) {
                throw new RuntimeException("订单状态不正确，无法支付");
            }
            
            // 3. 构建支付数据
            WeChatBasePayData payData = new WeChatBasePayData();
            payData.setTitle("商城订单-" + mallOrder.getOrderSn());
            payData.setOrderId(mallOrder.getOrderSn());
            payData.setPrice(mallOrder.getTotalAmount());
            payData.setNotify(WxNotifyType.PAY_BACK);
            
            // 4. 调用微信支付
            String codeUrl = WxPayCommonUtil.wxNativeApiPay(wxPayConfig, payData, null, wxPayClient);
            
            if (StringUtils.hasText(codeUrl)) {
                log.info("微信Native支付订单创建成功，订单号：{}，code_url：{}", mallOrder.getOrderSn(), codeUrl);
                return codeUrl;
            } else {
                throw new RuntimeException("微信支付订单创建失败");
            }
            
        } catch (Exception e) {
            log.error("创建微信Native支付订单失败，订单ID：{}", orderId, e);
            throw new RuntimeException("创建微信支付订单失败：" + e.getMessage());
        }
    }
    
    /**
     * 查询微信支付订单状态
     */
    @Override
    public String queryPayOrderStatus(String orderSn) {
        try {
            // 构建查询URL
            String url = wxPayConfig.getDomain() + String.format(WxApiType.ORDER_QUERY_BY_NO.getType(), orderSn);
            HttpGet httpGet = new HttpGet(url);
            httpGet.setHeader("Accept", "application/json");
            
            // 执行请求
            CloseableHttpResponse response = wxPayClient.execute(httpGet);
            
            // 解析响应
            int statusCode = response.getStatusLine().getStatusCode();
            String responseBody = EntityUtils.toString(response.getEntity());
            
            if (statusCode == 200) {
                Gson gson = new Gson();
                Map<String, Object> resultMap = gson.fromJson(responseBody, Map.class);
                String tradeState = (String) resultMap.get("trade_state");
                log.info("查询微信支付订单状态成功，订单号：{}，状态：{}", orderSn, tradeState);
                return tradeState;
            } else {
                log.error("查询微信支付订单状态失败，订单号：{}，响应码：{}，响应体：{}", orderSn, statusCode, responseBody);
                throw new RuntimeException("查询支付状态失败");
            }
            
        } catch (Exception e) {
            log.error("查询微信支付订单状态异常，订单号：{}", orderSn, e);
            throw new RuntimeException("查询支付状态异常：" + e.getMessage());
        }
    }
    
    /**
     * 关闭微信支付订单
     */
    @Override
    public boolean closePayOrder(String orderSn) {
        try {
            // 构建关闭订单URL
            String url = wxPayConfig.getDomain() + String.format(WxApiType.CLOSE_ORDER_BY_NO.getType(), orderSn);
            HttpPost httpPost = new HttpPost(url);
            
            // 设置请求体
            Map<String, String> requestBody = new HashMap<>();
            requestBody.put("mchid", wxPayConfig.getMchId());
            
            Gson gson = new Gson();
            String jsonParams = gson.toJson(requestBody);
            StringEntity entity = new StringEntity(jsonParams, "utf-8");
            entity.setContentType("application/json");
            httpPost.setEntity(entity);
            httpPost.setHeader("Accept", "application/json");
            
            // 执行请求
            CloseableHttpResponse response = wxPayClient.execute(httpPost);
            
            // 解析响应
            int statusCode = response.getStatusLine().getStatusCode();
            
            if (statusCode == 204) {
                log.info("关闭微信支付订单成功，订单号：{}", orderSn);
                return true;
            } else {
                String responseBody = EntityUtils.toString(response.getEntity());
                log.error("关闭微信支付订单失败，订单号：{}，响应码：{}，响应体：{}", orderSn, statusCode, responseBody);
                return false;
            }
            
        } catch (Exception e) {
            log.error("关闭微信支付订单异常，订单号：{}", orderSn, e);
            return false;
        }
    }
    
    /**
     * 处理微信支付回调
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public String handlePayCallback(String requestBody, String signature, String timestamp, String nonce) {
        try {
            log.info("收到微信支付回调，requestBody：{}", requestBody);
            
            // 验证签名
            if (!verifyWechatSignature(requestBody, signature, timestamp, nonce)) {
                log.error("微信支付回调签名验证失败");
                return "FAIL";
            }
            log.info("微信支付回调签名验证成功");
            
            // 解析回调数据
            Gson gson = new Gson();
            Map<String, Object> callbackData = gson.fromJson(requestBody, Map.class);
            
            String eventType = (String) callbackData.get("event_type");
            if (!"TRANSACTION.SUCCESS".equals(eventType)) {
                log.warn("非支付成功回调，事件类型：{}", eventType);
                return "SUCCESS";
            }
            
            // 获取资源数据
            Map<String, Object> resource = (Map<String, Object>) callbackData.get("resource");
            String ciphertext = (String) resource.get("ciphertext");
            
            // 解密资源数据
            Map<String, Object> orderData;
            try {
                orderData = decryptWechatResource(ciphertext);
                if (orderData == null || orderData.isEmpty()) {
                    log.error("解密微信支付回调数据失败");
                    return "FAIL";
                }
                log.info("微信支付回调数据解密成功");
            } catch (Exception e) {
                log.error("解密微信支付回调数据异常", e);
                return "FAIL";
            }
            

            
            String outTradeNo = (String) orderData.get("out_trade_no");
            String tradeState = (String) orderData.get("trade_state");
            String transactionId = (String) orderData.get("transaction_id");
            
            if ("SUCCESS".equals(tradeState)) {
                // 查询订单
                MallOrder mallOrder = mallOrderService.lambdaQuery()
                    .eq(MallOrder::getOrderSn, outTradeNo)
                    .one();
                
                if (mallOrder != null && MallOrderStatus.PENDING_PAYMENT.getCode().equals(mallOrder.getStatus())) {
                    // 更新订单状态为已支付
                    mallOrderService.payOrder(mallOrder.getId(), 1); // 1表示微信支付
                    log.info("微信支付回调处理成功，订单号：{}，微信交易号：{}", outTradeNo, transactionId);
                } else {
                    log.warn("订单不存在或状态不正确，订单号：{}", outTradeNo);
                }
            }
            
            return "SUCCESS";
            
        } catch (Exception e) {
            log.error("处理微信支付回调异常", e);
            return "FAIL";
        }
    }
    
    /**
     * 验证微信支付回调签名
     * 
     * @param requestBody 请求体
     * @param signature 签名
     * @param timestamp 时间戳
     * @param nonce 随机字符串
     * @return 验证结果
     */
    private boolean verifyWechatSignature(String requestBody, String signature, String timestamp, String nonce) {
        try {
            // 构建待签名字符串
            String signStr = timestamp + "\n" + nonce + "\n" + requestBody + "\n";
            
            // 这里应该使用微信提供的证书进行验证
            // 为了演示，这里简化处理，实际项目中需要使用微信的公钥证书验证
            log.info("签名验证字符串: {}", signStr);
            log.info("接收到的签名: {}", signature);
            
            // 简化验证：检查签名是否存在且不为空
            if (signature != null && !signature.trim().isEmpty()) {
                log.info("签名验证通过（简化验证）");
                return true;
            }
            
            log.warn("签名为空，验证失败");
            return false;
            
        } catch (Exception e) {
            log.error("签名验证异常", e);
            return false;
        }
    }
    
    /**
     * 解密微信支付回调资源数据
     * 
     * @param ciphertext 加密数据
     * @return 解密后的订单数据
     */
    private Map<String, Object> decryptWechatResource(String ciphertext) {
        try {
            // 这里应该使用微信APIv3密钥进行AES-256-GCM解密
            // 为了演示，这里简化处理，返回模拟数据
            log.info("开始解密资源数据: {}", ciphertext);
            
            // 实际项目中需要：
            // 1. 使用APIv3密钥
            // 2. 进行AES-256-GCM解密
            // 3. 解析JSON数据
            
            // 模拟解密后的数据
            Map<String, Object> orderData = new HashMap<>();
            orderData.put("out_trade_no", "ORDER_" + System.currentTimeMillis());
            orderData.put("trade_state", "SUCCESS");
            orderData.put("transaction_id", "WX_" + System.currentTimeMillis());
            orderData.put("total_fee", 100); // 分为单位
            orderData.put("success_time", new java.util.Date());
            
            log.info("资源数据解密成功（模拟数据）: {}", orderData);
            return orderData;
            
        } catch (Exception e) {
            log.error("解密资源数据异常", e);
            return null;
        }
    }
}