package com.chennian.storytelling.inventory.controller;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.chennian.storytelling.common.result.Result;
import com.chennian.storytelling.inventory.entity.Inventory;
import com.chennian.storytelling.inventory.service.InventoryService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

/**
 * 库存管理控制器
 * 
 * @author chennian
 * @since 2024-01-01
 */
@Tag(name = "库存管理", description = "库存管理相关接口")
@RestController
@RequestMapping("/api/inventory")
@RequiredArgsConstructor
@Validated
public class InventoryController {

    private final InventoryService inventoryService;

    @Operation(summary = "根据ID查询库存")
    @GetMapping("/{id}")
    public Result<Inventory> getInventoryById(
            @Parameter(description = "库存ID") @PathVariable @NotNull Long id) {
        return Result.success(inventoryService.getInventoryById(id));
    }

    @Operation(summary = "根据商品ID和仓库ID查询库存")
    @GetMapping("/product/{productId}/warehouse/{warehouseId}")
    public Result<Inventory> getInventoryByProductAndWarehouse(
            @Parameter(description = "商品ID") @PathVariable @NotNull Long productId,
            @Parameter(description = "仓库ID") @PathVariable @NotNull Long warehouseId) {
        return Result.success(inventoryService.getInventoryByProductAndWarehouse(productId, warehouseId));
    }

    @Operation(summary = "根据SKU编码查询库存")
    @GetMapping("/sku/{skuCode}")
    public Result<Inventory> getInventoryBySkuCode(
            @Parameter(description = "SKU编码") @PathVariable String skuCode) {
        return Result.success(inventoryService.getInventoryBySkuCode(skuCode));
    }

    @Operation(summary = "分页查询库存列表")
    @GetMapping("/page")
    public Result<IPage<Inventory>> getInventoryPage(
            @Parameter(description = "页码") @RequestParam(defaultValue = "1") Integer current,
            @Parameter(description = "每页大小") @RequestParam(defaultValue = "10") Integer size,
            @Parameter(description = "商品ID") @RequestParam(required = false) Long productId,
            @Parameter(description = "仓库ID") @RequestParam(required = false) Long warehouseId,
            @Parameter(description = "SKU编码") @RequestParam(required = false) String skuCode,
            @Parameter(description = "库存状态") @RequestParam(required = false) Integer status) {
        Page<Inventory> page = new Page<>(current, size);
        return Result.success(inventoryService.getInventoryPage(page, productId, warehouseId, skuCode, status));
    }

    @Operation(summary = "根据商品ID查询库存列表")
    @GetMapping("/product/{productId}")
    public Result<List<Inventory>> getInventoryListByProductId(
            @Parameter(description = "商品ID") @PathVariable @NotNull Long productId) {
        return Result.success(inventoryService.getInventoryListByProductId(productId));
    }

    @Operation(summary = "根据仓库ID查询库存列表")
    @GetMapping("/warehouse/{warehouseId}")
    public Result<List<Inventory>> getInventoryListByWarehouseId(
            @Parameter(description = "仓库ID") @PathVariable @NotNull Long warehouseId) {
        return Result.success(inventoryService.getInventoryListByWarehouseId(warehouseId));
    }

    @Operation(summary = "获取低库存商品列表")
    @GetMapping("/low-stock")
    public Result<List<Inventory>> getLowStockInventoryList() {
        return Result.success(inventoryService.getLowStockInventoryList());
    }

    @Operation(summary = "获取缺货商品列表")
    @GetMapping("/out-of-stock")
    public Result<List<Inventory>> getOutOfStockInventoryList() {
        return Result.success(inventoryService.getOutOfStockInventoryList());
    }

    @Operation(summary = "获取库存预警商品列表")
    @GetMapping("/warning")
    public Result<List<Inventory>> getInventoryWarningList() {
        return Result.success(inventoryService.getInventoryWarningList());
    }

    @Operation(summary = "创建库存")
    @PostMapping
    public Result<Boolean> createInventory(@Valid @RequestBody Inventory inventory) {
        return Result.success(inventoryService.createInventory(inventory));
    }

    @Operation(summary = "更新库存")
    @PutMapping("/{id}")
    public Result<Boolean> updateInventory(
            @Parameter(description = "库存ID") @PathVariable @NotNull Long id,
            @Valid @RequestBody Inventory inventory) {
        inventory.setInventoryId(id);
        return Result.success(inventoryService.updateInventory(inventory));
    }

    @Operation(summary = "删除库存")
    @DeleteMapping("/{id}")
    public Result<Boolean> deleteInventory(
            @Parameter(description = "库存ID") @PathVariable @NotNull Long id) {
        return Result.success(inventoryService.deleteInventory(id));
    }

    @Operation(summary = "批量删除库存")
    @DeleteMapping("/batch")
    public Result<Boolean> batchDeleteInventory(@RequestBody List<Long> inventoryIds) {
        return Result.success(inventoryService.batchDeleteInventory(inventoryIds));
    }

    @Operation(summary = "启用库存")
    @PutMapping("/{id}/enable")
    public Result<Boolean> enableInventory(
            @Parameter(description = "库存ID") @PathVariable @NotNull Long id) {
        return Result.success(inventoryService.enableInventory(id));
    }

    @Operation(summary = "禁用库存")
    @PutMapping("/{id}/disable")
    public Result<Boolean> disableInventory(
            @Parameter(description = "库存ID") @PathVariable @NotNull Long id) {
        return Result.success(inventoryService.disableInventory(id));
    }

    @Operation(summary = "批量更新库存状态")
    @PutMapping("/batch/status")
    public Result<Boolean> batchUpdateInventoryStatus(
            @RequestBody Map<String, Object> params) {
        @SuppressWarnings("unchecked")
        List<Long> inventoryIds = (List<Long>) params.get("inventoryIds");
        Integer status = (Integer) params.get("status");
        return Result.success(inventoryService.batchUpdateInventoryStatus(inventoryIds, status));
    }

    @Operation(summary = "入库")
    @PostMapping("/inbound")
    public Result<Boolean> inboundInventory(@RequestBody Map<String, Object> params) {
        Long inventoryId = Long.valueOf(params.get("inventoryId").toString());
        BigDecimal quantity = new BigDecimal(params.get("quantity").toString());
        String businessNo = (String) params.get("businessNo");
        Integer businessType = (Integer) params.get("businessType");
        String remark = (String) params.get("remark");
        return Result.success(inventoryService.inboundInventory(inventoryId, quantity, businessNo, businessType, remark));
    }

    @Operation(summary = "批量入库")
    @PostMapping("/batch/inbound")
    public Result<Boolean> batchInboundInventory(@RequestBody Map<String, Object> params) {
        @SuppressWarnings("unchecked")
        List<Map<String, Object>> inboundList = (List<Map<String, Object>>) params.get("inboundList");
        String businessNo = (String) params.get("businessNo");
        Integer businessType = (Integer) params.get("businessType");
        String remark = (String) params.get("remark");
        return Result.success(inventoryService.batchInboundInventory(inboundList, businessNo, businessType, remark));
    }

    @Operation(summary = "出库")
    @PostMapping("/outbound")
    public Result<Boolean> outboundInventory(@RequestBody Map<String, Object> params) {
        Long inventoryId = Long.valueOf(params.get("inventoryId").toString());
        BigDecimal quantity = new BigDecimal(params.get("quantity").toString());
        String businessNo = (String) params.get("businessNo");
        Integer businessType = (Integer) params.get("businessType");
        String remark = (String) params.get("remark");
        return Result.success(inventoryService.outboundInventory(inventoryId, quantity, businessNo, businessType, remark));
    }

    @Operation(summary = "批量出库")
    @PostMapping("/batch/outbound")
    public Result<Boolean> batchOutboundInventory(@RequestBody Map<String, Object> params) {
        @SuppressWarnings("unchecked")
        List<Map<String, Object>> outboundList = (List<Map<String, Object>>) params.get("outboundList");
        String businessNo = (String) params.get("businessNo");
        Integer businessType = (Integer) params.get("businessType");
        String remark = (String) params.get("remark");
        return Result.success(inventoryService.batchOutboundInventory(outboundList, businessNo, businessType, remark));
    }

    @Operation(summary = "锁定库存")
    @PostMapping("/lock")
    public Result<Boolean> lockInventory(@RequestBody Map<String, Object> params) {
        Long inventoryId = Long.valueOf(params.get("inventoryId").toString());
        BigDecimal quantity = new BigDecimal(params.get("quantity").toString());
        String businessNo = (String) params.get("businessNo");
        Integer businessType = (Integer) params.get("businessType");
        String remark = (String) params.get("remark");
        return Result.success(inventoryService.lockInventory(inventoryId, quantity, businessNo, businessType, remark));
    }

    @Operation(summary = "解锁库存")
    @PostMapping("/unlock")
    public Result<Boolean> unlockInventory(@RequestBody Map<String, Object> params) {
        Long inventoryId = Long.valueOf(params.get("inventoryId").toString());
        BigDecimal quantity = new BigDecimal(params.get("quantity").toString());
        String businessNo = (String) params.get("businessNo");
        Integer businessType = (Integer) params.get("businessType");
        String remark = (String) params.get("remark");
        return Result.success(inventoryService.unlockInventory(inventoryId, quantity, businessNo, businessType, remark));
    }

    @Operation(summary = "预占库存")
    @PostMapping("/reserve")
    public Result<Boolean> reserveInventory(@RequestBody Map<String, Object> params) {
        Long inventoryId = Long.valueOf(params.get("inventoryId").toString());
        BigDecimal quantity = new BigDecimal(params.get("quantity").toString());
        String businessNo = (String) params.get("businessNo");
        Integer businessType = (Integer) params.get("businessType");
        String remark = (String) params.get("remark");
        return Result.success(inventoryService.reserveInventory(inventoryId, quantity, businessNo, businessType, remark));
    }

    @Operation(summary = "释放预占库存")
    @PostMapping("/release")
    public Result<Boolean> releaseReservedInventory(@RequestBody Map<String, Object> params) {
        Long inventoryId = Long.valueOf(params.get("inventoryId").toString());
        BigDecimal quantity = new BigDecimal(params.get("quantity").toString());
        String businessNo = (String) params.get("businessNo");
        Integer businessType = (Integer) params.get("businessType");
        String remark = (String) params.get("remark");
        return Result.success(inventoryService.releaseReservedInventory(inventoryId, quantity, businessNo, businessType, remark));
    }

    @Operation(summary = "调拨库存")
    @PostMapping("/transfer")
    public Result<Boolean> transferInventory(@RequestBody Map<String, Object> params) {
        Long fromInventoryId = Long.valueOf(params.get("fromInventoryId").toString());
        Long toInventoryId = Long.valueOf(params.get("toInventoryId").toString());
        BigDecimal quantity = new BigDecimal(params.get("quantity").toString());
        String businessNo = (String) params.get("businessNo");
        String remark = (String) params.get("remark");
        return Result.success(inventoryService.transferInventory(fromInventoryId, toInventoryId, quantity, businessNo, remark));
    }

    @Operation(summary = "盘点库存")
    @PostMapping("/check")
    public Result<Boolean> checkInventory(@RequestBody Map<String, Object> params) {
        Long inventoryId = Long.valueOf(params.get("inventoryId").toString());
        BigDecimal actualQuantity = new BigDecimal(params.get("actualQuantity").toString());
        String businessNo = (String) params.get("businessNo");
        String remark = (String) params.get("remark");
        return Result.success(inventoryService.checkInventory(inventoryId, actualQuantity, businessNo, remark));
    }

    @Operation(summary = "检查SKU编码是否存在")
    @GetMapping("/check/sku/{skuCode}")
    public Result<Boolean> checkSkuCodeExists(
            @Parameter(description = "SKU编码") @PathVariable String skuCode) {
        return Result.success(inventoryService.checkSkuCodeExists(skuCode));
    }

    @Operation(summary = "检查商品和仓库组合是否存在")
    @GetMapping("/check/product/{productId}/warehouse/{warehouseId}")
    public Result<Boolean> checkProductWarehouseExists(
            @Parameter(description = "商品ID") @PathVariable @NotNull Long productId,
            @Parameter(description = "仓库ID") @PathVariable @NotNull Long warehouseId) {
        return Result.success(inventoryService.checkProductWarehouseExists(productId, warehouseId));
    }

    @Operation(summary = "获取商品总库存数量")
    @GetMapping("/product/{productId}/total-quantity")
    public Result<BigDecimal> getProductTotalQuantity(
            @Parameter(description = "商品ID") @PathVariable @NotNull Long productId) {
        return Result.success(inventoryService.getProductTotalQuantity(productId));
    }

    @Operation(summary = "获取商品可用库存数量")
    @GetMapping("/product/{productId}/available-quantity")
    public Result<BigDecimal> getProductAvailableQuantity(
            @Parameter(description = "商品ID") @PathVariable @NotNull Long productId) {
        return Result.success(inventoryService.getProductAvailableQuantity(productId));
    }

    @Operation(summary = "获取库存统计信息")
    @GetMapping("/statistics")
    public Result<Map<String, Object>> getInventoryStatistics(
            @Parameter(description = "仓库ID") @RequestParam(required = false) Long warehouseId) {
        return Result.success(inventoryService.getInventoryStatistics(warehouseId));
    }
}