package com.chennian.storytelling.admin.controller;

import com.chennian.storytelling.common.annotation.EventTrack;
import com.chennian.storytelling.common.enums.BusinessType;
import com.chennian.storytelling.common.enums.ModelType;
import com.chennian.storytelling.common.enums.OperatorType;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.chennian.storytelling.bean.model.Customer;
import com.chennian.storytelling.bean.model.Product;
import com.chennian.storytelling.bean.model.SalesOrder;
import com.chennian.storytelling.bean.vo.MobileCustomerVO;
import com.chennian.storytelling.bean.vo.MobileProductVO;
import com.chennian.storytelling.bean.vo.MobileSalesOrderVO;
import com.chennian.storytelling.common.response.ServerResponseEntity;
import com.chennian.storytelling.common.utils.PageParam;
import com.chennian.storytelling.service.CustomerService;
import com.chennian.storytelling.service.ProductService;
import com.chennian.storytelling.service.SalesOrderService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author chennian
 * @date 2023/12/15
 */
@RestController
@RequestMapping("/erp/mobile")
@Tag(name = "移动端接口")
public class MobileApiController {

    private final CustomerService customerService;
    private final ProductService productService;
    private final SalesOrderService salesOrderService;

    public MobileApiController(CustomerService customerService, ProductService productService,
                             SalesOrderService salesOrderService) {
        this.customerService = customerService;
        this.productService = productService;
        this.salesOrderService = salesOrderService;
    }

    /**
     * 移动端客户列表（轻量版）
     */
    @GetMapping("/customers")
    @Operation(summary = "移动端客户列表")
    @EventTrack(title = ModelType.SYSTEM, businessType = BusinessType.SEARCH, operatorType = OperatorType.MOBILE, description = "移动端客户列表")
    public ServerResponseEntity<List<MobileCustomerVO>> customerList(@RequestParam(required = false) String keyword,
                                                                  @RequestParam(defaultValue = "1") Integer page,
                                                                  @RequestParam(defaultValue = "10") Integer size) {
        Map<String, Object> params = new HashMap<>();
        params.put("keyword", keyword);
        params.put("page", page);
        params.put("size", size);
        List<MobileCustomerVO> customers = customerService.getMobileCustomerList(params);
        return ServerResponseEntity.success(customers);
    }

    /**
     * 移动端客户详情（轻量版）
     */
    @GetMapping("/customers/{customerId}")
    @Operation(summary = "移动端客户详情")
    @EventTrack(title = ModelType.SYSTEM, businessType = BusinessType.SEARCH, operatorType = OperatorType.MOBILE, description = "移动端客户详情")
    public ServerResponseEntity<MobileCustomerVO> customerDetail(@PathVariable("customerId") Long customerId) {
        MobileCustomerVO customer = customerService.getMobileCustomerDetail(customerId);
        return ServerResponseEntity.success(customer);
    }

    /**
     * 移动端产品列表（轻量版）
     */
    @GetMapping("/products")
    @Operation(summary = "移动端产品列表")
    @EventTrack(title = ModelType.SYSTEM, businessType = BusinessType.SEARCH, operatorType = OperatorType.MOBILE, description = "移动端产品列表")
    public ServerResponseEntity<List<MobileProductVO>> productList(@RequestParam(required = false) String keyword,
                                                                @RequestParam(defaultValue = "1") Integer page,
                                                                @RequestParam(defaultValue = "10") Integer size) {
        Map<String, Object> params = new HashMap<>();
        params.put("keyword", keyword);
        params.put("page", page);
        params.put("size", size);
        List<MobileProductVO> products = productService.getMobileProductList(params);
        return ServerResponseEntity.success(products);
    }

    /**
     * 移动端产品详情（轻量版）
     */
    @GetMapping("/products/{productId}")
    @Operation(summary = "移动端产品详情")
    @EventTrack(title = ModelType.SYSTEM, businessType = BusinessType.SEARCH, operatorType = OperatorType.MOBILE, description = "移动端产品详情")
    public ServerResponseEntity<MobileProductVO> productDetail(@PathVariable("productId") Long productId) {
        MobileProductVO product = productService.getMobileProductDetail(productId);
        return ServerResponseEntity.success(product);
    }

    /**
     * 移动端销售订单列表（轻量版）
     */
    @GetMapping("/orders")
    @Operation(summary = "移动端订单列表")
    @EventTrack(title = ModelType.SYSTEM, businessType = BusinessType.SEARCH, operatorType = OperatorType.MOBILE, description = "移动端订单列表")
    public ServerResponseEntity<List<MobileSalesOrderVO>> orderList(@RequestParam(required = false) String keyword,
                                                                 @RequestParam(required = false) String status,
                                                                 @RequestParam(defaultValue = "1") Integer page,
                                                                 @RequestParam(defaultValue = "10") Integer size) {
        Map<String, Object> params = new HashMap<>();
        params.put("keyword", keyword);
        params.put("status", status);
        params.put("page", page);
        params.put("size", size);
        List<MobileSalesOrderVO> orders = salesOrderService.getMobileOrderList(params);
        return ServerResponseEntity.success(orders);
    }

    /**
     * 移动端销售订单详情（轻量版）
     */
    @GetMapping("/orders/{orderId}")
    @Operation(summary = "移动端订单详情")
    @EventTrack(title = ModelType.SYSTEM, businessType = BusinessType.SEARCH, operatorType = OperatorType.MOBILE, description = "移动端订单详情")
    public ServerResponseEntity<MobileSalesOrderVO> orderDetail(@PathVariable("orderId") Long orderId) {
        MobileSalesOrderVO order = salesOrderService.getMobileOrderDetail(orderId);
        return ServerResponseEntity.success(order);
    }

    /**
     * 移动端快速创建客户
     */
    @PostMapping("/customers/quick-add")
    @Operation(summary = "移动端快速创建客户")
    @EventTrack(title = ModelType.SYSTEM, businessType = BusinessType.INSERT, operatorType = OperatorType.MOBILE, description = "移动端快速创建客户")
    public ServerResponseEntity<Long> quickAddCustomer(@RequestBody MobileCustomerVO customerVO) {
        Long customerId = customerService.quickAddCustomer(customerVO);
        return ServerResponseEntity.success(customerId);
    }

    /**
     * 移动端快速创建订单
     */
    @PostMapping("/orders/quick-add")
    @Operation(summary = "移动端快速创建订单")
    @EventTrack(title = ModelType.SYSTEM, businessType = BusinessType.INSERT, operatorType = OperatorType.MOBILE, description = "移动端快速创建订单")
    public ServerResponseEntity<Long> quickAddOrder(@RequestBody MobileSalesOrderVO orderVO) {
        Long orderId = salesOrderService.quickAddOrder(orderVO);
        return ServerResponseEntity.success(orderId);
    }

    /**
     * 移动端仪表盘数据
     */
    @GetMapping("/dashboard")
    @Operation(summary = "移动端仪表盘")
    @EventTrack(title = ModelType.SYSTEM, businessType = BusinessType.SEARCH, operatorType = OperatorType.MOBILE, description = "移动端仪表盘")
    public ServerResponseEntity<Map<String, Object>> mobileDashboard() {
        Map<String, Object> dashboardData = new HashMap<>();
        
        // 今日销售额
        dashboardData.put("todaySales", salesOrderService.getTodaySales());
        
        // 待处理订单数
        dashboardData.put("pendingOrders", salesOrderService.getPendingOrdersCount());
        
        // 库存预警数
        dashboardData.put("inventoryAlerts", productService.getInventoryAlertCount());
        
        // 最近客户活动
        dashboardData.put("recentCustomerActivities", customerService.getRecentActivities(5));
        
        return ServerResponseEntity.success(dashboardData);
    }
}