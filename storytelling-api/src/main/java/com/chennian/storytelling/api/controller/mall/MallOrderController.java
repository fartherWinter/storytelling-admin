package com.chennian.storytelling.api.controller.mall;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.chennian.storytelling.api.feign.OrderServiceClient;
import com.chennian.storytelling.api.feign.PaymentServiceClient;
import com.chennian.storytelling.bean.model.mall.MallOrder;
import com.chennian.storytelling.bean.model.mall.MallSubOrder;
import com.chennian.storytelling.common.annotation.EventTrack;
import com.chennian.storytelling.common.enums.BusinessType;
import com.chennian.storytelling.common.enums.ModelType;
import com.chennian.storytelling.common.response.ServerResponseEntity;
import com.chennian.storytelling.common.utils.PageParam;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;
import java.util.HashMap;

/**
 * 商城订单API网关Controller
 * 通过Feign客户端调用订单和支付微服务
 * 
 * @author chennian
 * @date 2025-01-27
 */
@RestController
@RequestMapping("/mall/order")
@Tag(name = "商城订单管理")
public class MallOrderController {
    
    @Autowired
    private OrderServiceClient orderServiceClient;
    
    @Autowired
    private PaymentServiceClient paymentServiceClient;
    
    /**
     * 分页查询订单列表
     */
    @PostMapping("/page")
    @Operation(summary = "分页查询订单列表")
    @EventTrack(title = ModelType.MALL, businessType = BusinessType.SEARCH, description = "分页查询订单列表")
    public ServerResponseEntity<IPage<MallOrder>> page(@RequestBody PageParam<MallOrder> pageParam) {
        return orderServiceClient.page(pageParam);
    }
    
    /**
     * 根据ID查询订单详情
     */
    @GetMapping("/info/{orderId}")
    @Operation(summary = "查询订单详情")
    @EventTrack(title = ModelType.MALL, businessType = BusinessType.SEARCH, description = "查询订单详情")
    public ServerResponseEntity<MallOrder> info(@PathVariable Long orderId) {
        return orderServiceClient.info(orderId);
    }
    
    /**
     * 根据用户ID分页查询订单
     */
    @PostMapping("/user/{userId}")
    @Operation(summary = "根据用户ID查询订单")
    @EventTrack(title = ModelType.MALL, businessType = BusinessType.SEARCH, description = "根据用户ID查询订单")
    public ServerResponseEntity<IPage<MallOrder>> getUserOrders(@PathVariable Long userId, @RequestBody PageParam<MallOrder> pageParam) {
        return orderServiceClient.getUserOrders(userId, pageParam);
    }
    
    /**
     * 创建订单
     */
    @PostMapping("/create")
    @Operation(summary = "创建订单")
    @EventTrack(title = ModelType.MALL, businessType = BusinessType.INSERT, description = "创建订单")
    public ServerResponseEntity<MallOrder> create(@RequestBody MallOrder mallOrder) {
        return orderServiceClient.create(mallOrder);
    }
    
    /**
     * 支付订单
     */
    @PostMapping("/pay/{orderId}")
    @Operation(summary = "支付订单")
    @EventTrack(title = ModelType.MALL, businessType = BusinessType.UPDATE, description = "支付订单")
    public ServerResponseEntity<Object> pay(@PathVariable Long orderId, @RequestParam Integer payType, @RequestParam(required = false) String openId) {
        try {
            // 1 = 微信支付
            if (payType == 1) {
                if (openId != null && !openId.isEmpty()) {
                    // JSAPI支付（小程序/公众号）
                    ServerResponseEntity<Map<String, String>> response = paymentServiceClient.createJsApiPayOrder(orderId, openId);
                    if (response.isSuccess()) {
                        Map<String, String> result = new HashMap<>();
                        result.put("prepay_id", response.getData().get("prepay_id"));
                        result.put("pay_type", "jsapi");
                        return ServerResponseEntity.success(result);
                    }
                    return ServerResponseEntity.fail(response.getCode(), response.getMsg());
                } else {
                    // Native支付（扫码）
                    ServerResponseEntity<Map<String, String>> response = paymentServiceClient.createNativePayOrder(orderId);
                    if (response.isSuccess()) {
                        Map<String, String> result = new HashMap<>();
                        result.put("code_url", response.getData().get("code_url"));
                        result.put("pay_type", "native");
                        return ServerResponseEntity.success(result);
                    }
                    return ServerResponseEntity.fail(response.getCode(), response.getMsg());
                }
            } else {
                // 其他支付方式，直接更新订单状态
                orderServiceClient.updateOrderPaid(orderId, payType);
                return ServerResponseEntity.success();
            }
        } catch (Exception e) {
            return ServerResponseEntity.fail(500, "支付失败：" + e.getMessage());
        }
    }
    
    /**
     * 取消订单
     */
    @PostMapping("/cancel/{orderId}")
    @Operation(summary = "取消订单")
    @EventTrack(title = ModelType.MALL, businessType = BusinessType.UPDATE, description = "取消订单")
    public ServerResponseEntity<Void> cancel(@PathVariable Long orderId) {
        return orderServiceClient.cancel(orderId);
    }
    
    /**
     * 发货
     */
    @PostMapping("/deliver/{orderId}")
    @Operation(summary = "发货")
    @EventTrack(title = ModelType.MALL, businessType = BusinessType.UPDATE, description = "发货")
    public ServerResponseEntity<Void> deliver(@PathVariable Long orderId) {
        return orderServiceClient.deliver(orderId);
    }
    
    /**
     * 确认收货
     */
    @PostMapping("/receive/{orderId}")
    @Operation(summary = "确认收货")
    @EventTrack(title = ModelType.MALL, businessType = BusinessType.UPDATE, description = "确认收货")
    public ServerResponseEntity<Void> receive(@PathVariable Long orderId) {
        return orderServiceClient.receive(orderId);
    }
    
    /**
     * 查询订单的子订单列表
     */
    @GetMapping("/sub/{orderId}")
    @Operation(summary = "查询订单的子订单列表")
    @EventTrack(title = ModelType.MALL, businessType = BusinessType.SEARCH, description = "查询订单的子订单列表")
    public ServerResponseEntity<List<MallSubOrder>> getSubOrders(@PathVariable Long orderId) {
        return orderServiceClient.getSubOrders(orderId);
    }
    
    /**
     * 分页查询子订单
     */
    @PostMapping("/sub/page")
    @Operation(summary = "分页查询子订单")
    @EventTrack(title = ModelType.MALL, businessType = BusinessType.SEARCH, description = "分页查询子订单")
    public ServerResponseEntity<IPage<MallSubOrder>> subOrderPage(@RequestBody PageParam<MallSubOrder> pageParam) {
        return orderServiceClient.subOrderPage(pageParam);
    }
}