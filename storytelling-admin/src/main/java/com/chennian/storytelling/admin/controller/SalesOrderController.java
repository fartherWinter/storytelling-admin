package com.chennian.storytelling.admin.controller;

import com.chennian.storytelling.common.annotation.EventTrack;
import com.chennian.storytelling.common.enums.BusinessType;
import com.chennian.storytelling.common.enums.ModelType;
import com.chennian.storytelling.common.enums.OperatorType;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.chennian.storytelling.bean.model.SalesOrder;
import com.chennian.storytelling.bean.vo.SalesStatisticsVO;
import com.chennian.storytelling.common.response.ServerResponseEntity;
import com.chennian.storytelling.common.utils.PageParam;
import com.chennian.storytelling.service.SalesOrderService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

/**
 * @author chennian
 * @date 2023/5/20
 */
@RestController
@RequestMapping("/erp/sales")
@Tag(name = "销售管理")
public class SalesOrderController {

    private final SalesOrderService salesOrderService;
    public SalesOrderController(SalesOrderService salesOrderService) {
        this.salesOrderService = salesOrderService;
    }

    /**
     * 查询销售订单列表
     */
    @PostMapping("/order/page")
    @Operation(summary = "销售订单列表")
    @EventTrack(title = ModelType.SYSTEM, businessType = BusinessType.SEARCH, operatorType = OperatorType.MANAGE, description = "销售订单列表")
    public ServerResponseEntity<IPage<SalesOrder>> page(SalesOrder salesOrder, PageParam<SalesOrder> page) {
        IPage<SalesOrder> orderPage = salesOrderService.findByPage(page, salesOrder);
        return ServerResponseEntity.success(orderPage);
    }

    /**
     * 获取销售订单详细信息
     */
    @PostMapping("/order/info/{orderId}")
    @Operation(summary = "销售订单详情")
    @EventTrack(title = ModelType.SYSTEM, businessType = BusinessType.SEARCH, operatorType = OperatorType.MANAGE, description = "销售订单详情")
    public ServerResponseEntity<SalesOrder> info(@PathVariable("orderId") Long orderId) {
        SalesOrder salesOrder = salesOrderService.selectOrderById(orderId);
        return ServerResponseEntity.success(salesOrder);
    }

    /**
     * 新增销售订单
     */
    @PostMapping("/order/add")
    @Operation(summary = "新增销售订单")
    @EventTrack(title = ModelType.SYSTEM, businessType = BusinessType.INSERT, operatorType = OperatorType.MANAGE, description = "新增销售订单")
    public ServerResponseEntity<Integer> add(@Validated @RequestBody SalesOrder salesOrder) {
        return ServerResponseEntity.success(salesOrderService.insertOrder(salesOrder));
    }

    /**
     * 修改销售订单
     */
    @PostMapping("/order/update")
    @Operation(summary = "修改销售订单")
    @EventTrack(title = ModelType.SYSTEM, businessType = BusinessType.UPDATE, operatorType = OperatorType.MANAGE, description = "修改销售订单")
    public ServerResponseEntity<Integer> update(@RequestBody SalesOrder salesOrder) {
        return ServerResponseEntity.success(salesOrderService.updateOrder(salesOrder));
    }

    /**
     * 删除销售订单
     */
    @PostMapping("/order/remove/{orderIds}")
    @Operation(summary = "删除销售订单")
    @EventTrack(title = ModelType.SYSTEM, businessType = BusinessType.DELETE, operatorType = OperatorType.MANAGE, description = "删除销售订单")
    public ServerResponseEntity<Integer> remove(@PathVariable("orderIds") Long[] orderIds) {
        return ServerResponseEntity.success(salesOrderService.deleteOrderByIds(orderIds));
    }

    /**
     * 确认销售订单
     */
    @PostMapping("/order/confirm/{orderId}")
    @Operation(summary = "确认销售订单")
    @EventTrack(title = ModelType.SYSTEM, businessType = BusinessType.UPDATE, operatorType = OperatorType.MANAGE, description = "确认销售订单")
    public ServerResponseEntity<Integer> confirm(@PathVariable("orderId") Long orderId) {
        return ServerResponseEntity.success(salesOrderService.confirmOrder(orderId));
    }

    /**
     * 发货处理
     */
    @PostMapping("/order/ship/{orderId}")
    @Operation(summary = "订单发货")
    @EventTrack(title = ModelType.SYSTEM, businessType = BusinessType.UPDATE, operatorType = OperatorType.MANAGE, description = "订单发货")
    public ServerResponseEntity<Integer> ship(@PathVariable("orderId") Long orderId) {
        return ServerResponseEntity.success(salesOrderService.shipOrder(orderId));
    }

    /**
     * 完成订单
     */
    @PostMapping("/order/complete/{orderId}")
    @Operation(summary = "完成订单")
    @EventTrack(title = ModelType.SYSTEM, businessType = BusinessType.UPDATE, operatorType = OperatorType.MANAGE, description = "完成订单")
    public ServerResponseEntity<Integer> complete(@PathVariable("orderId") Long orderId) {
        return ServerResponseEntity.success(salesOrderService.completeOrder(orderId));
    }

    /**
     * 取消订单
     */
    @PostMapping("/order/cancel/{orderId}")
    @Operation(summary = "取消订单")
    @EventTrack(title = ModelType.SYSTEM, businessType = BusinessType.UPDATE, operatorType = OperatorType.MANAGE, description = "取消订单")
    public ServerResponseEntity<Integer> cancel(@PathVariable("orderId") Long orderId) {
        return ServerResponseEntity.success(salesOrderService.cancelOrder(orderId));
    }

    /**
     * 销售统计分析
     */
    @PostMapping("/statistics")
    @Operation(summary = "销售统计")
    @EventTrack(title = ModelType.SYSTEM, businessType = BusinessType.SEARCH, operatorType = OperatorType.MANAGE, description = "销售统计")
    public ServerResponseEntity<Object> statistics(@RequestParam(required = false) String dateRange) {
        Map<String, Object> params = new HashMap<>();
        if (dateRange != null) {
            params.put("dateRange", dateRange);
        }
        SalesStatisticsVO statistics = salesOrderService.getSalesStatistics(params);
        return ServerResponseEntity.success(statistics);
    }
}