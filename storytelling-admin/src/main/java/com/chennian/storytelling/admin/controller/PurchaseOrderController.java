package com.chennian.storytelling.admin.controller;

import com.chennian.storytelling.common.annotation.EventTrack;
import com.chennian.storytelling.common.enums.BusinessType;
import com.chennian.storytelling.common.enums.ModelType;
import com.chennian.storytelling.common.enums.OperatorType;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.chennian.storytelling.bean.model.PurchaseOrder;
import com.chennian.storytelling.bean.model.PurchaseOrderItem;
import com.chennian.storytelling.bean.model.Supplier;
import com.chennian.storytelling.common.response.ServerResponseEntity;
import com.chennian.storytelling.common.utils.PageParam;
import com.chennian.storytelling.service.PurchaseOrderItemService;
import com.chennian.storytelling.service.PurchaseOrderService;
import com.chennian.storytelling.service.SupplierService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * @author chennian
 * @date 2023/5/20
 */
@RestController
@RequestMapping("/erp/purchase")
@Tag(name = "采购管理")
public class PurchaseOrderController {

    @Autowired
    private PurchaseOrderService purchaseOrderService;
    
    @Autowired
    private PurchaseOrderItemService purchaseOrderItemService;
    
    @Autowired
    private SupplierService supplierService;

    /**
     * 查询采购订单列表
     */
    @PostMapping("/order/page")
    @Operation(summary = "采购订单列表")
    @EventTrack(title = ModelType.SYSTEM, businessType = BusinessType.SEARCH, operatorType = OperatorType.MANAGE, description = "采购订单列表")
    public ServerResponseEntity<IPage<PurchaseOrder>> page(PurchaseOrder purchaseOrder, PageParam<PurchaseOrder> page) {
        IPage<PurchaseOrder> orderPage = purchaseOrderService.findByPage(page, purchaseOrder);
        return ServerResponseEntity.success(orderPage);
    }

    /**
     * 获取采购订单详细信息
     */
    @PostMapping("/order/info/{orderId}")
    @Operation(summary = "采购订单详情")
    @EventTrack(title = ModelType.SYSTEM, businessType = BusinessType.SEARCH, operatorType = OperatorType.MANAGE, description = "采购订单详情")
    public ServerResponseEntity<PurchaseOrder> info(@PathVariable("orderId") Long orderId) {
        PurchaseOrder purchaseOrder = purchaseOrderService.selectOrderById(orderId);
        return ServerResponseEntity.success(purchaseOrder);
    }

    /**
     * 新增采购订单
     */
    @PostMapping("/order/add")
    @Operation(summary = "新增采购订单")
    @EventTrack(title = ModelType.SYSTEM, businessType = BusinessType.INSERT, operatorType = OperatorType.MANAGE, description = "新增采购订单")
    public ServerResponseEntity<Integer> add(@Validated @RequestBody PurchaseOrder purchaseOrder) {
        return ServerResponseEntity.success(purchaseOrderService.insertOrder(purchaseOrder));
    }

    /**
     * 修改采购订单
     */
    @PostMapping("/order/update")
    @Operation(summary = "修改采购订单")
    @EventTrack(title = ModelType.SYSTEM, businessType = BusinessType.UPDATE, operatorType = OperatorType.MANAGE, description = "修改采购订单")
    public ServerResponseEntity<Integer> update(@RequestBody PurchaseOrder purchaseOrder) {
        return ServerResponseEntity.success(purchaseOrderService.updateOrder(purchaseOrder));
    }

    /**
     * 删除采购订单
     */
    @PostMapping("/order/remove/{orderIds}")
    @Operation(summary = "删除采购订单")
    @EventTrack(title = ModelType.SYSTEM, businessType = BusinessType.DELETE, operatorType = OperatorType.MANAGE, description = "删除采购订单")
    public ServerResponseEntity<Integer> remove(@PathVariable("orderIds") Long[] orderIds) {
        return ServerResponseEntity.success(purchaseOrderService.deleteOrderByIds(orderIds));
    }

    /**
     * 审核采购订单
     */
    @PostMapping("/order/approve/{orderId}")
    @Operation(summary = "审核采购订单")
    @EventTrack(title = ModelType.SYSTEM, businessType = BusinessType.UPDATE, operatorType = OperatorType.MANAGE, description = "审核采购订单")
    public ServerResponseEntity<Integer> approve(@PathVariable("orderId") Long orderId) {
        return ServerResponseEntity.success(purchaseOrderService.approveOrder(orderId));
    }

    /**
     * 获取采购订单明细列表
     */
    @PostMapping("/orderItem/list/{orderId}")
    @Operation(summary = "采购订单明细列表")
    @EventTrack(title = ModelType.SYSTEM, businessType = BusinessType.SEARCH, operatorType = OperatorType.MANAGE, description = "采购订单明细列表")
    public ServerResponseEntity<List<PurchaseOrderItem>> itemList(@PathVariable("orderId") Long orderId) {
        List<PurchaseOrderItem> items = purchaseOrderItemService.selectItemsByOrderId(orderId);
        return ServerResponseEntity.success(items);
    }

    /**
     * 获取供应商列表
     */
    @PostMapping("/supplier/list")
    @Operation(summary = "供应商列表")
    @EventTrack(title = ModelType.SYSTEM, businessType = BusinessType.SEARCH, operatorType = OperatorType.MANAGE, description = "供应商列表")
    public ServerResponseEntity<List<Supplier>> supplierList(Supplier supplier) {
        List<Supplier> suppliers = supplierService.selectSupplierList(supplier);
        return ServerResponseEntity.success(suppliers);
    }
}