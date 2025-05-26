package com.chennian.storytelling.admin.controller;

import com.chennian.storytelling.bean.bo.ProductSearchBo;
import com.chennian.storytelling.common.annotation.EventTrack;
import com.chennian.storytelling.common.enums.BusinessType;
import com.chennian.storytelling.common.enums.ModelType;
import com.chennian.storytelling.common.enums.OperatorType;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.chennian.storytelling.bean.model.Inventory;
import com.chennian.storytelling.bean.model.Product;
import com.chennian.storytelling.common.response.ServerResponseEntity;
import com.chennian.storytelling.common.utils.PageParam;
import com.chennian.storytelling.service.InventoryService;
import com.chennian.storytelling.service.ProductService;
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
@RequestMapping("/erp/inventory")
@Tag(name = "库存管理")
public class InventoryController {

    private final InventoryService inventoryService;
    private final ProductService productService;

    public InventoryController(InventoryService inventoryService, ProductService productService) {
        this.inventoryService = inventoryService;
        this.productService = productService;
    }

    /**
     * 查询库存列表
     */
    @PostMapping("/page")
    @Operation(summary = "库存列表")
    @EventTrack(title = ModelType.SYSTEM, businessType = BusinessType.SEARCH, operatorType = OperatorType.MANAGE, description = "库存列表")
    public ServerResponseEntity<IPage<Inventory>> page(Inventory inventory, PageParam<Inventory> page) {
        IPage<Inventory> inventoryPage = inventoryService.findByPage(page, inventory);
        return ServerResponseEntity.success(inventoryPage);
    }

    /**
     * 获取库存详细信息
     */
    @PostMapping("/info/{inventoryId}")
    @Operation(summary = "库存详情")
    @EventTrack(title = ModelType.SYSTEM, businessType = BusinessType.SEARCH, operatorType = OperatorType.MANAGE, description = "库存详情")
    public ServerResponseEntity<Inventory> info(@PathVariable("inventoryId") Long inventoryId) {
        Inventory inventory = inventoryService.selectInventoryById(inventoryId);
        return ServerResponseEntity.success(inventory);
    }

    /**
     * 库存调整
     */
    @PostMapping("/adjust")
    @Operation(summary = "库存调整")
    @EventTrack(title = ModelType.SYSTEM, businessType = BusinessType.UPDATE, operatorType = OperatorType.MANAGE, description = "库存调整")
    public ServerResponseEntity<Integer> adjust(@Validated @RequestBody Inventory inventory) {
        return ServerResponseEntity.success(inventoryService.adjustInventory(inventory));
    }

    /**
     * 库存盘点
     */
    @PostMapping("/stocktake")
    @Operation(summary = "库存盘点")
    @EventTrack(title = ModelType.SYSTEM, businessType = BusinessType.UPDATE, operatorType = OperatorType.MANAGE, description = "库存盘点")
    public ServerResponseEntity<Integer> stocktake(@RequestBody List<Inventory> inventoryList) {
        return ServerResponseEntity.success(inventoryService.stocktakeInventory(inventoryList));
    }

    /**
     * 库存预警列表
     */
    @PostMapping("/warning/list")
    @Operation(summary = "库存预警列表")
    @EventTrack(title = ModelType.SYSTEM, businessType = BusinessType.SEARCH, operatorType = OperatorType.MANAGE, description = "库存预警列表")
    public ServerResponseEntity<List<Inventory>> warningList() {
        List<Inventory> warningList = inventoryService.selectWarningInventory();
        return ServerResponseEntity.success(warningList);
    }

    /**
     * 库存移动（调拨）
     */
    @PostMapping("/transfer")
    @Operation(summary = "库存调拨")
    @EventTrack(title = ModelType.SYSTEM, businessType = BusinessType.UPDATE, operatorType = OperatorType.MANAGE, description = "库存调拨")
    public ServerResponseEntity<Integer> transfer(@RequestBody Inventory inventory) {
        return ServerResponseEntity.success(inventoryService.transferInventory(inventory));
    }

    /**
     * 获取产品库存历史记录
     */
    @PostMapping("/history/{productId}")
    @Operation(summary = "库存历史记录")
    @EventTrack(title = ModelType.SYSTEM, businessType = BusinessType.SEARCH, operatorType = OperatorType.MANAGE, description = "库存历史记录")
    public ServerResponseEntity<List<Inventory>> history(@PathVariable("productId") Long productId) {
        List<Inventory> historyList = inventoryService.selectInventoryHistory(productId);
        return ServerResponseEntity.success(historyList);
    }

    /**
     * 获取产品列表
     */
    @PostMapping("/product/list")
    @Operation(summary = "产品列表")
    @EventTrack(title = ModelType.SYSTEM, businessType = BusinessType.SEARCH, operatorType = OperatorType.MANAGE, description = "产品列表")
    public ServerResponseEntity<IPage<Product>> productList(ProductSearchBo productSearchBo) {
        // 此处需要实现产品服务
        IPage<Product> products = productService.getProductList(productSearchBo);
        return ServerResponseEntity.success(products);
    }
}