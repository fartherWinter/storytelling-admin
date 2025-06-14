package com.chennian.storytelling.admin.controller;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.chennian.storytelling.bean.model.manufacturing.ProductionOrder;
import com.chennian.storytelling.common.annotation.EventTrack;
import com.chennian.storytelling.common.enums.BusinessType;
import com.chennian.storytelling.common.enums.ModelType;
import com.chennian.storytelling.common.enums.OperatorType;
import com.chennian.storytelling.common.response.ServerResponseEntity;
import com.chennian.storytelling.common.utils.PageParam;
import com.chennian.storytelling.service.ProductionOrderService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

/**
 * 生产订单管理控制器
 * @author chennian
 * @date 2024/1/15
 */
@RestController
@RequestMapping("/erp/manufacturing/order")
@Tag(name = "生产订单管理")
public class ProductionOrderController {

    private final ProductionOrderService productionOrderService;

    public ProductionOrderController(ProductionOrderService productionOrderService) {
        this.productionOrderService = productionOrderService;
    }

    /**
     * 查询生产订单列表
     */
    @PostMapping("/page")
    @Operation(summary = "生产订单列表")
    @EventTrack(title = ModelType.SYSTEM, businessType = BusinessType.SEARCH, operatorType = OperatorType.MANAGE, description = "生产订单列表")
    public ServerResponseEntity<IPage<ProductionOrder>> orderPage(ProductionOrder order, PageParam<ProductionOrder> page) {
        IPage<ProductionOrder> orderPage = productionOrderService.getProductionOrderList(page, order);
        return ServerResponseEntity.success(orderPage);
    }

    /**
     * 获取生产订单详细信息
     */
    @PostMapping("/info/{orderId}")
    @Operation(summary = "生产订单详情")
    @EventTrack(title = ModelType.SYSTEM, businessType = BusinessType.SEARCH, operatorType = OperatorType.MANAGE, description = "生产订单详情")
    public ServerResponseEntity<ProductionOrder> orderInfo(@PathVariable("orderId") Long orderId) {
        ProductionOrder order = productionOrderService.getProductionOrderById(orderId);
        return ServerResponseEntity.success(order);
    }

    /**
     * 新增生产订单
     */
    @PostMapping("/add")
    @Operation(summary = "新增生产订单")
    @EventTrack(title = ModelType.SYSTEM, businessType = BusinessType.INSERT, operatorType = OperatorType.MANAGE, description = "新增生产订单")
    public ServerResponseEntity<Void> addOrder(@Validated @RequestBody ProductionOrder order) {
        productionOrderService.addProductionOrder(order);
        return ServerResponseEntity.success();
    }

    /**
     * 修改生产订单
     */
    @PostMapping("/edit")
    @Operation(summary = "修改生产订单")
    @EventTrack(title = ModelType.SYSTEM, businessType = BusinessType.UPDATE, operatorType = OperatorType.MANAGE, description = "修改生产订单")
    public ServerResponseEntity<Void> editOrder(@Validated @RequestBody ProductionOrder order) {
        productionOrderService.updateProductionOrder(order);
        return ServerResponseEntity.success();
    }

    /**
     * 删除生产订单
     */
    @PostMapping("/remove/{orderId}")
    @Operation(summary = "删除生产订单")
    @EventTrack(title = ModelType.SYSTEM, businessType = BusinessType.DELETE, operatorType = OperatorType.MANAGE, description = "删除生产订单")
    public ServerResponseEntity<Void> removeOrder(@PathVariable Long orderId) {
        productionOrderService.deleteProductionOrder(orderId);
        return ServerResponseEntity.success();
    }

    /**
     * 开始生产
     */
    @PostMapping("/start")
    @Operation(summary = "开始生产")
    @EventTrack(title = ModelType.SYSTEM, businessType = BusinessType.UPDATE, operatorType = OperatorType.MANAGE, description = "开始生产")
    public ServerResponseEntity<Void> startProduction(@RequestParam Long orderId) {
        productionOrderService.startProductionOrder(orderId);
        return ServerResponseEntity.success();
    }

    /**
     * 暂停生产
     */
    @PostMapping("/pause")
    @Operation(summary = "暂停生产")
    @EventTrack(title = ModelType.SYSTEM, businessType = BusinessType.UPDATE, operatorType = OperatorType.MANAGE, description = "暂停生产")
    public ServerResponseEntity<Void> pauseProduction(@RequestParam Long orderId) {
        productionOrderService.pauseProductionOrder(orderId);
        return ServerResponseEntity.success();
    }
}