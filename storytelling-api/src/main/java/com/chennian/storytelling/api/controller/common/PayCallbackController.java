package com.chennian.storytelling.api.controller.common;

import com.chennian.storytelling.service.mall.MallWxPayService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;

/**
 * 支付回调Controller
 * 
 * @author chennian
 * @date 2025-01-27
 */
@Slf4j
@RestController
@RequestMapping("/common/pay")
@Tag(name = "支付回调管理")
public class PayCallbackController {
    
    @Autowired
    private MallWxPayService mallWxPayService;
    
    /**
     * 微信支付回调接口
     */
    @PostMapping("/wx/callback")
    @Operation(summary = "微信支付回调")
    public String wxPayCallback(HttpServletRequest request, @RequestBody String requestBody) {
        try {
            // 获取微信回调的签名信息
            String signature = request.getHeader("Wechatpay-Signature");
            String timestamp = request.getHeader("Wechatpay-Timestamp");
            String nonce = request.getHeader("Wechatpay-Nonce");
            String serial = request.getHeader("Wechatpay-Serial");
            
            log.info("收到微信支付回调，signature：{}，timestamp：{}，nonce：{}，serial：{}", 
                    signature, timestamp, nonce, serial);
            log.info("微信支付回调请求体：{}", requestBody);
            
            // 处理回调
            String result = mallWxPayService.handlePayCallback(requestBody, signature, timestamp, nonce);
            
            log.info("微信支付回调处理结果：{}", result);
            return result;
        } catch (Exception e) {
            log.error("处理微信支付回调失败", e);
            return "FAIL";
        }
    }
}