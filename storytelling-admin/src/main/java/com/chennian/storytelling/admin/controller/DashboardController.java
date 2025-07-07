package com.chennian.storytelling.admin.controller;

import com.chennian.storytelling.common.annotation.EventTrack;
import com.chennian.storytelling.common.enums.BusinessType;
import com.chennian.storytelling.common.enums.ModelType;
import com.chennian.storytelling.common.enums.OperatorType;
import com.chennian.storytelling.bean.vo.CustomerStatisticsVO;
import com.chennian.storytelling.bean.vo.HotProductVO;
import com.chennian.storytelling.bean.vo.mall.InventoryStatisticsVO;
import com.chennian.storytelling.bean.vo.SalesStatisticsVO;
import com.chennian.storytelling.common.response.ServerResponseEntity;
import com.chennian.storytelling.service.CustomerService;
import com.chennian.storytelling.service.InventoryService;
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
@RequestMapping("/erp/dashboard")
@Tag(name = "数据可视化仪表盘")
public class DashboardController {

    private final SalesOrderService salesOrderService;
    private final InventoryService inventoryService;
    private final CustomerService customerService;
    private final ProductService productService;

    public DashboardController(SalesOrderService salesOrderService, InventoryService inventoryService,
                              CustomerService customerService, ProductService productService) {
        this.salesOrderService = salesOrderService;
        this.inventoryService = inventoryService;
        this.customerService = customerService;
        this.productService = productService;
    }

    /**
     * 获取仪表盘概览数据
     */
    @GetMapping("/overview")
    @Operation(summary = "仪表盘概览")
    @EventTrack(title = ModelType.SYSTEM, businessType = BusinessType.SEARCH, operatorType = OperatorType.MANAGE, description = "仪表盘概览")
    public ServerResponseEntity<Map<String, Object>> overview() {
        Map<String, Object> data = new HashMap<>();
        
        // 销售统计
        SalesStatisticsVO salesStats = salesOrderService.getSalesStatistics(new HashMap<>());
        data.put("sales", salesStats);
        
        // 库存统计
        InventoryStatisticsVO inventoryStats = inventoryService.getInventoryStatistics();
        data.put("inventory", inventoryStats);
        
        // 客户统计
        CustomerStatisticsVO customerStats = customerService.getCustomerStatistics();
        data.put("customer", customerStats);
        
        return ServerResponseEntity.success(data);
    }

    /**
     * 获取销售趋势数据
     */
    @GetMapping("/sales/trend")
    @Operation(summary = "销售趋势")
    @EventTrack(title = ModelType.SYSTEM, businessType = BusinessType.SEARCH, operatorType = OperatorType.MANAGE, description = "销售趋势")
    public ServerResponseEntity<Map<String, Object>> salesTrend(@RequestParam(required = false) String period) {
        Map<String, Object> params = new HashMap<>();
        if (period != null) {
            params.put("period", period); // daily, weekly, monthly, yearly
        }
        Map<String, Object> trendData = salesOrderService.getSalesTrend(params);
        return ServerResponseEntity.success(trendData);
    }

    /**
     * 获取热销产品数据
     */
    @GetMapping("/products/hot")
    @Operation(summary = "热销产品")
    @EventTrack(title = ModelType.SYSTEM, businessType = BusinessType.SEARCH, operatorType = OperatorType.MANAGE, description = "热销产品")
    public ServerResponseEntity<List<HotProductVO>> hotProducts(@RequestParam(required = false) Integer limit) {
        Map<String, Object> params = new HashMap<>();
        if (limit != null) {
            params.put("limit", limit);
        }
        List<HotProductVO> hotProductsData = productService.getHotProducts(params);
        return ServerResponseEntity.success(hotProductsData);
    }

    /**
     * 获取库存预警数据
     */
    @GetMapping("/inventory/alert")
    @Operation(summary = "库存预警")
    @EventTrack(title = ModelType.SYSTEM, businessType = BusinessType.SEARCH, operatorType = OperatorType.MANAGE, description = "库存预警")
    public ServerResponseEntity<Map<String, Object>> inventoryAlert() {
        Map<String, Object> alertData = inventoryService.getInventoryAlerts();
        return ServerResponseEntity.success(alertData);
    }

    /**
     * 获取客户分布数据
     */
    @GetMapping("/customers/distribution")
    @Operation(summary = "客户分布")
    @EventTrack(title = ModelType.SYSTEM, businessType = BusinessType.SEARCH, operatorType = OperatorType.MANAGE, description = "客户分布")
    public ServerResponseEntity<Map<String, Object>> customerDistribution() {
        Map<String, Object> distributionData = customerService.getCustomerDistribution();
        return ServerResponseEntity.success(distributionData);
    }

    /**
     * 获取移动端适配的仪表盘数据
     */
    @GetMapping("/mobile/overview")
    @Operation(summary = "移动端仪表盘概览")
    @EventTrack(title = ModelType.SYSTEM, businessType = BusinessType.SEARCH, operatorType = OperatorType.MANAGE, description = "移动端仪表盘概览")
    public ServerResponseEntity<Map<String, Object>> mobileOverview() {
        // 为移动端提供精简版的数据
        Map<String, Object> data = new HashMap<>();
        
        // 销售统计简化版
        Map<String, Object> params = new HashMap<>();
        params.put("simplified", true);
        SalesStatisticsVO salesStats = salesOrderService.getSalesStatistics(params);
        data.put("sales", salesStats);
        
        // 库存统计简化版
        InventoryStatisticsVO inventoryStats = inventoryService.getInventoryStatistics();
        data.put("inventory", inventoryStats);
        
        // 客户统计简化版
        CustomerStatisticsVO customerStats = customerService.getCustomerStatistics();
        data.put("customer", customerStats);
        
        return ServerResponseEntity.success(data);
    }
}