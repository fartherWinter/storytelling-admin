package com.chennian.storytelling.api.feign;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.chennian.storytelling.bean.model.mall.MallOrder;
import com.chennian.storytelling.bean.model.mall.MallSubOrder;
import com.chennian.storytelling.common.response.ServerResponseEntity;
import com.chennian.storytelling.common.utils.PageParam;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 订单服务Feign客户端
 * 调用 storytelling-order-service 微服务
 * 
 * @author chennian
 * @date 2025-01-27
 */
@FeignClient(name = "storytelling-order-service", path = "/order")
public interface OrderServiceClient {

    /**
     * 分页查询订单列表
     */
    @PostMapping("/page")
    ServerResponseEntity<IPage<MallOrder>> page(@RequestBody PageParam<MallOrder> pageParam);
    
    /**
     * 根据ID查询订单详情
     */
    @GetMapping("/info/{orderId}")
    ServerResponseEntity<MallOrder> info(@PathVariable Long orderId);
    
    /**
     * 根据用户ID分页查询订单
     */
    @PostMapping("/user/{userId}")
    ServerResponseEntity<IPage<MallOrder>> getUserOrders(
            @PathVariable Long userId, 
            @RequestBody PageParam<MallOrder> pageParam);
    
    /**
     * 创建订单
     */
    @PostMapping("/create")
    ServerResponseEntity<MallOrder> create(@RequestBody MallOrder mallOrder);
    
    /**
     * 取消订单
     */
    @PostMapping("/cancel/{orderId}")
    ServerResponseEntity<Void> cancel(@PathVariable Long orderId);
    
    /**
     * 发货
     */
    @PostMapping("/deliver/{orderId}")
    ServerResponseEntity<Void> deliver(@PathVariable Long orderId);
    
    /**
     * 确认收货
     */
    @PostMapping("/receive/{orderId}")
    ServerResponseEntity<Void> receive(@PathVariable Long orderId);
    
    /**
     * 查询订单的子订单列表
     */
    @GetMapping("/sub/{orderId}")
    ServerResponseEntity<List<MallSubOrder>> getSubOrders(@PathVariable Long orderId);
    
    /**
     * 分页查询子订单
     */
    @PostMapping("/sub/page")
    ServerResponseEntity<IPage<MallSubOrder>> subOrderPage(@RequestBody PageParam<MallSubOrder> pageParam);
    
    /**
     * 更新订单状态为已支付
     */
    @PostMapping("/pay/{orderId}")
    ServerResponseEntity<Void> updateOrderPaid(@PathVariable Long orderId, @RequestParam Integer payType);
}