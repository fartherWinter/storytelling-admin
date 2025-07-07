package com.chennian.storytelling.api.controller.mall;

import com.chennian.storytelling.common.annotation.EventTrack;
import com.chennian.storytelling.common.enums.BusinessType;
import com.chennian.storytelling.common.enums.ModelType;
import com.chennian.storytelling.common.response.ServerResponseEntity;
import com.chennian.storytelling.service.mall.MallWxPayService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;
import java.util.Map;

/**
 * 商城微信支付Controller
 * 
 * @author chennian
 * @date 2025-01-27
 */
@Slf4j
@RestController
@RequestMapping("/mall/wxpay")
@Tag(name = "商城微信支付管理")
public class MallWxPayController {

    private final MallWxPayService mallWxPayService;

    public MallWxPayController(MallWxPayService mallWxPayService) {
        this.mallWxPayService = mallWxPayService;
    }

    /**
     * 创建微信JSAPI支付订单（小程序/公众号）
     */
    @PostMapping("/jsapi/{orderId}")
    @Operation(summary = "创建微信JSAPI支付订单")
    @EventTrack(title = ModelType.MALL, businessType = BusinessType.INSERT, description = "创建微信JSAPI支付订单")
    public ServerResponseEntity<Map<String, String>> createJsApiPayOrder(
            @PathVariable Long orderId, 
            @RequestParam String openId) {
        try {
            String prepayId = mallWxPayService.createJsApiPayOrder(orderId, openId);
            
            Map<String, String> result = new HashMap<>();
            result.put("prepay_id", prepayId);
            result.put("order_id", orderId.toString());
            
            return ServerResponseEntity.success(result);
        } catch (Exception e) {
            log.error("创建微信JSAPI支付订单失败", e);
            return ServerResponseEntity.fail(500, e.getMessage());
        }
    }
    
    /**
     * 创建微信Native支付订单（扫码支付）
     */
    @PostMapping("/native/{orderId}")
    @Operation(summary = "创建微信Native支付订单")
    @EventTrack(title = ModelType.MALL, businessType = BusinessType.INSERT, description = "创建微信Native支付订单")
    public ServerResponseEntity<Map<String, String>> createNativePayOrder(@PathVariable Long orderId) {
        try {
            String codeUrl = mallWxPayService.createNativePayOrder(orderId);
            
            Map<String, String> result = new HashMap<>();
            result.put("code_url", codeUrl);
            result.put("order_id", orderId.toString());
            
            return ServerResponseEntity.success(result);
        } catch (Exception e) {
            log.error("创建微信Native支付订单失败", e);
            return ServerResponseEntity.fail(500, e.getMessage());
        }
    }
    
    /**
     * 查询微信支付订单状态
     */
    @GetMapping("/status/{orderSn}")
    @Operation(summary = "查询微信支付订单状态")
    @EventTrack(title = ModelType.MALL, businessType = BusinessType.SEARCH, description = "查询微信支付订单状态")
    public ServerResponseEntity<Map<String, String>> queryPayOrderStatus(@PathVariable String orderSn) {
        try {
            String tradeState = mallWxPayService.queryPayOrderStatus(orderSn);
            
            Map<String, String> result = new HashMap<>();
            result.put("order_sn", orderSn);
            result.put("trade_state", tradeState);
            
            return ServerResponseEntity.success(result);
        } catch (Exception e) {
            log.error("查询微信支付订单状态失败", e);
            return ServerResponseEntity.fail(500, e.getMessage());
        }
    }
    
    /**
     * 关闭微信支付订单
     */
    @PostMapping("/close/{orderSn}")
    @Operation(summary = "关闭微信支付订单")
    @EventTrack(title = ModelType.MALL, businessType = BusinessType.UPDATE, description = "关闭微信支付订单")
    public ServerResponseEntity<Void> closePayOrder(@PathVariable String orderSn) {
        try {
            boolean success = mallWxPayService.closePayOrder(orderSn);
            if (success) {
                return ServerResponseEntity.success();
            } else {
                return ServerResponseEntity.fail(500, "关闭支付订单失败");
            }
        } catch (Exception e) {
            log.error("关闭微信支付订单失败", e);
            return ServerResponseEntity.fail(500, e.getMessage());
        }
    }
    
    /**
     * 微信支付回调接口
     */
    @PostMapping("/callback")
    @Operation(summary = "微信支付回调")
    public String payCallback(HttpServletRequest request, @RequestBody String requestBody) {
        try {
            // 获取微信回调的签名信息
            String signature = request.getHeader("Wechatpay-Signature");
            String timestamp = request.getHeader("Wechatpay-Timestamp");
            String nonce = request.getHeader("Wechatpay-Nonce");
            
            log.info("收到微信支付回调，signature：{}，timestamp：{}，nonce：{}", signature, timestamp, nonce);
            
            // 处理回调
            String result = mallWxPayService.handlePayCallback(requestBody, signature, timestamp, nonce);
            
            return result;
        } catch (Exception e) {
            log.error("处理微信支付回调失败", e);
            return "FAIL";
        }
    }
}