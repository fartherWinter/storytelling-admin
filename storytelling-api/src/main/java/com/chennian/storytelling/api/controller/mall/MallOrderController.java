package com.chennian.storytelling.api.controller.mall;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.chennian.storytelling.bean.model.mall.MallOrder;
import com.chennian.storytelling.bean.model.mall.MallSubOrder;
import com.chennian.storytelling.common.annotation.EventTrack;
import com.chennian.storytelling.common.enums.BusinessType;
import com.chennian.storytelling.common.enums.MallResponseEnum;
import com.chennian.storytelling.common.enums.ModelType;
import com.chennian.storytelling.common.response.ServerResponseEntity;
import com.chennian.storytelling.common.utils.PageParam;
import com.chennian.storytelling.service.mall.MallOrderService;
import com.chennian.storytelling.service.mall.MallSubOrderService;
import com.chennian.storytelling.service.mall.MallWxPayService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;
import java.util.HashMap;

/**
 * 商城订单Controller
 * 
 * @author chennian
 * @date 2025-01-27
 */
@RestController
@RequestMapping("/mall/order")
@Tag(name = "商城订单管理")
public class MallOrderController {
    
    @Autowired
    private MallOrderService mallOrderService;
    
    @Autowired
    private MallSubOrderService mallSubOrderService;
    
    @Autowired
    private MallWxPayService mallWxPayService;
    
    /**
     * 分页查询订单列表
     */
    @PostMapping("/page")
    @Operation(summary = "分页查询订单列表")
    @EventTrack(title = ModelType.MALL, businessType = BusinessType.SEARCH, description = "分页查询订单列表")
    public ServerResponseEntity<IPage<MallOrder>> page(@RequestBody PageParam<MallOrder> pageParam) {
        MallOrder mallOrder = pageParam.getRecords().get(0);
        IPage<MallOrder> page = mallOrderService.findByPage(pageParam, mallOrder);
        return ServerResponseEntity.success(page);
    }
    
    /**
     * 根据ID查询订单详情
     */
    @GetMapping("/info/{orderId}")
    @Operation(summary = "查询订单详情")
    @EventTrack(title = ModelType.MALL, businessType = BusinessType.SEARCH, description = "查询订单详情")
    public ServerResponseEntity<MallOrder> info(@PathVariable Long orderId) {
        MallOrder mallOrder = mallOrderService.selectOrderById(orderId);
        return ServerResponseEntity.success(mallOrder);
    }
    
    /**
     * 根据用户ID分页查询订单
     */
    @PostMapping("/user/{userId}")
    @Operation(summary = "根据用户ID查询订单")
    @EventTrack(title = ModelType.MALL, businessType = BusinessType.SEARCH, description = "根据用户ID查询订单")
    public ServerResponseEntity<IPage<MallOrder>> getUserOrders(@PathVariable Long userId, @RequestBody PageParam<MallOrder> pageParam) {
        IPage<MallOrder> page = mallOrderService.selectOrdersByUserId(userId, pageParam);
        return ServerResponseEntity.success(page);
    }
    
    /**
     * 创建订单
     */
    @PostMapping("/create")
    @Operation(summary = "创建订单")
    @EventTrack(title = ModelType.MALL, businessType = BusinessType.INSERT, description = "创建订单")
    public ServerResponseEntity<MallOrder> create(@RequestBody MallOrder mallOrder) {
        MallOrder order = mallOrderService.createOrder(mallOrder);
        if (order != null) {
            return ServerResponseEntity.success(order);
        } else {
            return ServerResponseEntity.fail(MallResponseEnum.ORDER_CREATE_FAIL.getCode(), MallResponseEnum.ORDER_CREATE_FAIL.getMessage());
        }
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
                    String prepayId = mallWxPayService.createJsApiPayOrder(orderId, openId);
                    Map<String, String> result = new HashMap<>();
                    result.put("prepay_id", prepayId);
                    result.put("pay_type", "jsapi");
                    return ServerResponseEntity.success(result);
                } else {
                    // Native支付（扫码）
                    String codeUrl = mallWxPayService.createNativePayOrder(orderId);
                    Map<String, String> result = new HashMap<>();
                    result.put("code_url", codeUrl);
                    result.put("pay_type", "native");
                    return ServerResponseEntity.success(result);
                }
            } else {
                // 其他支付方式，直接更新订单状态
                int success = mallOrderService.payOrder(orderId, payType);
                if (success > 0) {
                    return ServerResponseEntity.success();
                } else {
                    return ServerResponseEntity.fail(MallResponseEnum.ORDER_PAY_FAIL.getCode(), MallResponseEnum.ORDER_PAY_FAIL.getMessage());
                }
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
        int success = mallOrderService.cancelOrder(orderId);
        if (success>0) {
            return ServerResponseEntity.success();
        } else {
            return ServerResponseEntity.fail(MallResponseEnum.ORDER_CANCEL_FAIL.getCode(), MallResponseEnum.ORDER_CANCEL_FAIL.getMessage());
        }
    }
    
    /**
     * 发货
     */
    @PostMapping("/deliver/{orderId}")
    @Operation(summary = "发货")
    @EventTrack(title = ModelType.MALL, businessType = BusinessType.UPDATE, description = "发货")
    public ServerResponseEntity<Void> deliver(@PathVariable Long orderId) {
        boolean success = mallOrderService.deliverOrder(orderId);
        if (success) {
            return ServerResponseEntity.success();
        } else {
            return ServerResponseEntity.fail(MallResponseEnum.ORDER_DELIVER_FAIL.getCode(), MallResponseEnum.ORDER_DELIVER_FAIL.getMessage());
        }
    }
    
    /**
     * 确认收货
     */
    @PostMapping("/receive/{orderId}")
    @Operation(summary = "确认收货")
    @EventTrack(title = ModelType.MALL, businessType = BusinessType.UPDATE, description = "确认收货")
    public ServerResponseEntity<Void> receive(@PathVariable Long orderId) {
        boolean success = mallOrderService.confirmReceive(orderId);
        if (success) {
            return ServerResponseEntity.success();
        } else {
            return ServerResponseEntity.fail(MallResponseEnum.ORDER_RECEIVE_FAIL.getCode(),MallResponseEnum.ORDER_RECEIVE_FAIL.getMessage());
        }
    }
    
    /**
     * 查询订单的子订单列表
     */
    @GetMapping("/sub/{orderId}")
    @Operation(summary = "查询订单的子订单列表")
    @EventTrack(title = ModelType.MALL, businessType = BusinessType.SEARCH, description = "查询订单的子订单列表")
    public ServerResponseEntity<List<MallSubOrder>> getSubOrders(@PathVariable Long orderId) {
        List<MallSubOrder> subOrders = mallSubOrderService.selectSubOrdersByOrderId(orderId);
        return ServerResponseEntity.success(subOrders);
    }
    
    /**
     * 分页查询子订单
     */
    @PostMapping("/sub/page")
    @Operation(summary = "分页查询子订单")
    @EventTrack(title = ModelType.MALL, businessType = BusinessType.SEARCH, description = "分页查询子订单")
    public ServerResponseEntity<IPage<MallSubOrder>> subOrderPage(@RequestBody PageParam<MallSubOrder> pageParam) {
        MallSubOrder mallSubOrder = pageParam.getRecords().get(0);
        IPage<MallSubOrder> page = mallSubOrderService.findByPage(pageParam, mallSubOrder);
        return ServerResponseEntity.success(page);
    }
}