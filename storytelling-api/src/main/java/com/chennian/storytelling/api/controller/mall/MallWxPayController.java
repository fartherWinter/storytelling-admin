package com.chennian.storytelling.api.controller.mall;

import com.chennian.storytelling.api.feign.PaymentServiceClient;
import com.chennian.storytelling.common.annotation.EventTrack;
import com.chennian.storytelling.common.enums.BusinessType;
import com.chennian.storytelling.common.enums.ModelType;
import com.chennian.storytelling.common.response.ServerResponseEntity;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.Map;

/**
 * 商城微信支付Controller - API网关
 * 通过Feign客户端调用支付微服务
 * 
 * @author chennian
 * @date 2025-01-27
 */
@Slf4j
@RestController
@RequestMapping("/mall/wxpay")
@Tag(name = "商城微信支付管理")
public class MallWxPayController {

    @Autowired
    private PaymentServiceClient paymentServiceClient;

    /**
     * 创建微信JSAPI支付订单（小程序/公众号）
     */
    @PostMapping("/jsapi/{orderId}")
    @Operation(summary = "创建微信JSAPI支付订单")
    @EventTrack(title = ModelType.MALL, businessType = BusinessType.INSERT, description = "创建微信JSAPI支付订单")
    public ServerResponseEntity<Map<String, String>> createJsApiPayOrder(
            @PathVariable Long orderId, 
            @RequestParam String openId) {
        return paymentServiceClient.createJsApiPayOrder(orderId, openId);
    }
    
    /**
     * 创建微信Native支付订单（扫码支付）
     */
    @PostMapping("/native/{orderId}")
    @Operation(summary = "创建微信Native支付订单")
    @EventTrack(title = ModelType.MALL, businessType = BusinessType.INSERT, description = "创建微信Native支付订单")
    public ServerResponseEntity<Map<String, String>> createNativePayOrder(@PathVariable Long orderId) {
        return paymentServiceClient.createNativePayOrder(orderId);
    }
    
    /**
     * 查询微信支付订单状态
     */
    @GetMapping("/status/{orderSn}")
    @Operation(summary = "查询微信支付订单状态")
    @EventTrack(title = ModelType.MALL, businessType = BusinessType.SEARCH, description = "查询微信支付订单状态")
    public ServerResponseEntity<Map<String, String>> queryPayOrderStatus(@PathVariable String orderSn) {
        return paymentServiceClient.queryPayOrderStatus(orderSn);
    }
    
    /**
     * 关闭微信支付订单
     */
    @PostMapping("/close/{orderSn}")
    @Operation(summary = "关闭微信支付订单")
    @EventTrack(title = ModelType.MALL, businessType = BusinessType.UPDATE, description = "关闭微信支付订单")
    public ServerResponseEntity<Void> closePayOrder(@PathVariable String orderSn) {
        return paymentServiceClient.closePayOrder(orderSn);
    }
    
    /**
     * 微信支付回调接口
     */
    @PostMapping("/callback")
    @Operation(summary = "微信支付回调")
    public String payCallback(HttpServletRequest request, @RequestBody String requestBody) {
        // 获取微信回调的签名信息
        String signature = request.getHeader("Wechatpay-Signature");
        String timestamp = request.getHeader("Wechatpay-Timestamp");
        String nonce = request.getHeader("Wechatpay-Nonce");
        
        return paymentServiceClient.handlePayCallback(requestBody, signature, timestamp, nonce);
    }
}