package com.chennian.storytelling.api.feign;

import com.chennian.storytelling.common.response.ServerResponseEntity;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

/**
 * 支付服务Feign客户端
 * 调用 storytelling-payment-service 微服务
 * 
 * @author chennian
 * @date 2025-01-27
 */
@FeignClient(name = "storytelling-payment-service", path = "/payment")
public interface PaymentServiceClient {

    /**
     * 创建微信JSAPI支付订单
     */
    @PostMapping("/wx/jsapi/{orderId}")
    ServerResponseEntity<Map<String, String>> createJsApiPayOrder(
            @PathVariable Long orderId, 
            @RequestParam String openId);
    
    /**
     * 创建微信Native支付订单
     */
    @PostMapping("/wx/native/{orderId}")
    ServerResponseEntity<Map<String, String>> createNativePayOrder(@PathVariable Long orderId);
    
    /**
     * 查询微信支付订单状态
     */
    @GetMapping("/status/{orderSn}")
    ServerResponseEntity<Map<String, String>> queryPayOrderStatus(@PathVariable String orderSn);
    
    /**
     * 关闭微信支付订单
     */
    @PostMapping("/close/{orderSn}")
    ServerResponseEntity<Void> closePayOrder(@PathVariable String orderSn);
    
    /**
     * 微信支付回调接口
     */
    @PostMapping("/callback")
    String handlePayCallback(
            @RequestBody String requestBody,
            @RequestParam String signature,
            @RequestParam String timestamp,
            @RequestParam String nonce);
}