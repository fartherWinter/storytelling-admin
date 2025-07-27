package com.chennian.storytelling.inventory.controller;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.chennian.storytelling.common.result.Result;
import com.chennian.storytelling.inventory.entity.Warehouse;
import com.chennian.storytelling.inventory.service.WarehouseService;
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
 * 仓库管理控制器
 * 
 * @author chennian
 * @since 2024-01-01
 */
@Tag(name = "仓库管理", description = "仓库管理相关接口")
@RestController
@RequestMapping("/api/warehouse")
@RequiredArgsConstructor
@Validated
public class WarehouseController {

    private final WarehouseService warehouseService;

    @Operation(summary = "根据ID查询仓库")
    @GetMapping("/{id}")
    public Result<Warehouse> getWarehouseById(
            @Parameter(description = "仓库ID") @PathVariable @NotNull Long id) {
        return Result.success(warehouseService.getWarehouseById(id));
    }

    @Operation(summary = "根据仓库编码查询仓库")
    @GetMapping("/code/{warehouseCode}")
    public Result<Warehouse> getWarehouseByCode(
            @Parameter(description = "仓库编码") @PathVariable String warehouseCode) {
        return Result.success(warehouseService.getWarehouseByCode(warehouseCode));
    }

    @Operation(summary = "分页查询仓库列表")
    @GetMapping("/page")
    public Result<IPage<Warehouse>> getWarehousePage(
            @Parameter(description = "页码") @RequestParam(defaultValue = "1") Integer current,
            @Parameter(description = "每页大小") @RequestParam(defaultValue = "10") Integer size,
            @Parameter(description = "仓库名称") @RequestParam(required = false) String warehouseName,
            @Parameter(description = "仓库编码") @RequestParam(required = false) String warehouseCode,
            @Parameter(description = "仓库类型") @RequestParam(required = false) Integer warehouseType,
            @Parameter(description = "所属区域") @RequestParam(required = false) String region,
            @Parameter(description = "仓库状态") @RequestParam(required = false) Integer status) {
        Page<Warehouse> page = new Page<>(current, size);
        return Result.success(warehouseService.getWarehousePage(page, warehouseName, warehouseCode, warehouseType, region, status));
    }

    @Operation(summary = "获取所有可用仓库")
    @GetMapping("/available")
    public Result<List<Warehouse>> getAllAvailableWarehouses() {
        return Result.success(warehouseService.getAllAvailableWarehouses());
    }

    @Operation(summary = "根据仓库类型查询仓库列表")
    @GetMapping("/type/{warehouseType}")
    public Result<List<Warehouse>> getWarehouseListByType(
            @Parameter(description = "仓库类型") @PathVariable @NotNull Integer warehouseType) {
        return Result.success(warehouseService.getWarehouseListByType(warehouseType));
    }

    @Operation(summary = "根据所属区域查询仓库列表")
    @GetMapping("/region/{region}")
    public Result<List<Warehouse>> getWarehouseListByRegion(
            @Parameter(description = "所属区域") @PathVariable String region) {
        return Result.success(warehouseService.getWarehouseListByRegion(region));
    }

    @Operation(summary = "获取默认仓库")
    @GetMapping("/default")
    public Result<Warehouse> getDefaultWarehouse() {
        return Result.success(warehouseService.getDefaultWarehouse());
    }

    @Operation(summary = "获取支持自动分配的仓库列表")
    @GetMapping("/auto-allocate")
    public Result<List<Warehouse>> getAutoAllocateWarehouses() {
        return Result.success(warehouseService.getAutoAllocateWarehouses());
    }

    @Operation(summary = "根据父仓库ID查询子仓库列表")
    @GetMapping("/{parentId}/children")
    public Result<List<Warehouse>> getChildWarehouses(
            @Parameter(description = "父仓库ID") @PathVariable @NotNull Long parentId) {
        return Result.success(warehouseService.getChildWarehouses(parentId));
    }

    @Operation(summary = "创建仓库")
    @PostMapping
    public Result<Boolean> createWarehouse(@Valid @RequestBody Warehouse warehouse) {
        return Result.success(warehouseService.createWarehouse(warehouse));
    }

    @Operation(summary = "更新仓库")
    @PutMapping("/{id}")
    public Result<Boolean> updateWarehouse(
            @Parameter(description = "仓库ID") @PathVariable @NotNull Long id,
            @Valid @RequestBody Warehouse warehouse) {
        warehouse.setWarehouseId(id);
        return Result.success(warehouseService.updateWarehouse(warehouse));
    }

    @Operation(summary = "删除仓库")
    @DeleteMapping("/{id}")
    public Result<Boolean> deleteWarehouse(
            @Parameter(description = "仓库ID") @PathVariable @NotNull Long id) {
        return Result.success(warehouseService.deleteWarehouse(id));
    }

    @Operation(summary = "批量删除仓库")
    @DeleteMapping("/batch")
    public Result<Boolean> batchDeleteWarehouse(@RequestBody List<Long> warehouseIds) {
        return Result.success(warehouseService.batchDeleteWarehouse(warehouseIds));
    }

    @Operation(summary = "启用仓库")
    @PutMapping("/{id}/enable")
    public Result<Boolean> enableWarehouse(
            @Parameter(description = "仓库ID") @PathVariable @NotNull Long id) {
        return Result.success(warehouseService.enableWarehouse(id));
    }

    @Operation(summary = "禁用仓库")
    @PutMapping("/{id}/disable")
    public Result<Boolean> disableWarehouse(
            @Parameter(description = "仓库ID") @PathVariable @NotNull Long id) {
        return Result.success(warehouseService.disableWarehouse(id));
    }

    @Operation(summary = "批量更新仓库状态")
    @PutMapping("/batch/status")
    public Result<Boolean> batchUpdateWarehouseStatus(
            @RequestBody Map<String, Object> params) {
        @SuppressWarnings("unchecked")
        List<Long> warehouseIds = (List<Long>) params.get("warehouseIds");
        Integer status = (Integer) params.get("status");
        return Result.success(warehouseService.batchUpdateWarehouseStatus(warehouseIds, status));
    }

    @Operation(summary = "设置默认仓库")
    @PutMapping("/{id}/default")
    public Result<Boolean> setDefaultWarehouse(
            @Parameter(description = "仓库ID") @PathVariable @NotNull Long id) {
        return Result.success(warehouseService.setDefaultWarehouse(id));
    }

    @Operation(summary = "更新仓库排序")
    @PutMapping("/{id}/sort")
    public Result<Boolean> updateWarehouseSort(
            @Parameter(description = "仓库ID") @PathVariable @NotNull Long id,
            @RequestBody Map<String, Object> params) {
        Integer sortOrder = (Integer) params.get("sortOrder");
        return Result.success(warehouseService.updateWarehouseSort(id, sortOrder));
    }

    @Operation(summary = "移动仓库")
    @PutMapping("/{id}/move")
    public Result<Boolean> moveWarehouse(
            @Parameter(description = "仓库ID") @PathVariable @NotNull Long id,
            @RequestBody Map<String, Object> params) {
        Long newParentId = params.get("newParentId") != null ? Long.valueOf(params.get("newParentId").toString()) : null;
        return Result.success(warehouseService.moveWarehouse(id, newParentId));
    }

    @Operation(summary = "检查仓库编码是否存在")
    @GetMapping("/check/code/{warehouseCode}")
    public Result<Boolean> checkWarehouseCodeExists(
            @Parameter(description = "仓库编码") @PathVariable String warehouseCode) {
        return Result.success(warehouseService.checkWarehouseCodeExists(warehouseCode));
    }

    @Operation(summary = "检查仓库名称是否存在")
    @GetMapping("/check/name/{warehouseName}")
    public Result<Boolean> checkWarehouseNameExists(
            @Parameter(description = "仓库名称") @PathVariable String warehouseName) {
        return Result.success(warehouseService.checkWarehouseNameExists(warehouseName));
    }

    @Operation(summary = "检查仓库是否有库存")
    @GetMapping("/{id}/has-inventory")
    public Result<Boolean> checkWarehouseHasInventory(
            @Parameter(description = "仓库ID") @PathVariable @NotNull Long id) {
        return Result.success(warehouseService.checkWarehouseHasInventory(id));
    }

    @Operation(summary = "检查是否有子仓库")
    @GetMapping("/{id}/has-children")
    public Result<Boolean> checkHasChildWarehouses(
            @Parameter(description = "仓库ID") @PathVariable @NotNull Long id) {
        return Result.success(warehouseService.checkHasChildWarehouses(id));
    }

    @Operation(summary = "获取仓库下的库存商品数量")
    @GetMapping("/{id}/inventory-count")
    public Result<Integer> getWarehouseInventoryCount(
            @Parameter(description = "仓库ID") @PathVariable @NotNull Long id) {
        return Result.success(warehouseService.getWarehouseInventoryCount(id));
    }

    @Operation(summary = "获取仓库的总库存数量")
    @GetMapping("/{id}/total-quantity")
    public Result<BigDecimal> getWarehouseTotalQuantity(
            @Parameter(description = "仓库ID") @PathVariable @NotNull Long id) {
        return Result.success(warehouseService.getWarehouseTotalQuantity(id));
    }

    @Operation(summary = "获取仓库的容量使用率")
    @GetMapping("/{id}/capacity-usage")
    public Result<BigDecimal> getWarehouseCapacityUsage(
            @Parameter(description = "仓库ID") @PathVariable @NotNull Long id) {
        return Result.success(warehouseService.getWarehouseCapacityUsage(id));
    }

    @Operation(summary = "获取仓库层级路径")
    @GetMapping("/{id}/hierarchy-path")
    public Result<String> getWarehouseHierarchyPath(
            @Parameter(description = "仓库ID") @PathVariable @NotNull Long id) {
        return Result.success(warehouseService.getWarehouseHierarchyPath(id));
    }

    @Operation(summary = "构建仓库树")
    @GetMapping("/tree")
    public Result<List<Map<String, Object>>> buildWarehouseTree() {
        return Result.success(warehouseService.buildWarehouseTree());
    }

    @Operation(summary = "获取仓库统计信息")
    @GetMapping("/statistics")
    public Result<Map<String, Object>> getWarehouseStatistics(
            @Parameter(description = "仓库类型") @RequestParam(required = false) Integer warehouseType,
            @Parameter(description = "所属区域") @RequestParam(required = false) String region) {
        return Result.success(warehouseService.getWarehouseStatistics(warehouseType, region));
    }

    @Operation(summary = "自动分配仓库")
    @PostMapping("/auto-allocate")
    public Result<Warehouse> autoAllocateWarehouse(@RequestBody Map<String, Object> params) {
        Long productId = Long.valueOf(params.get("productId").toString());
        BigDecimal quantity = new BigDecimal(params.get("quantity").toString());
        String region = (String) params.get("region");
        return Result.success(warehouseService.autoAllocateWarehouse(productId, quantity, region));
    }

    @Operation(summary = "获取仓库容量信息")
    @GetMapping("/{id}/capacity")
    public Result<Map<String, Object>> getWarehouseCapacityInfo(
            @Parameter(description = "仓库ID") @PathVariable @NotNull Long id) {
        return Result.success(warehouseService.getWarehouseCapacityInfo(id));
    }

    @Operation(summary = "检查仓库容量是否充足")
    @PostMapping("/{id}/check-capacity")
    public Result<Boolean> checkWarehouseCapacitySufficient(
            @Parameter(description = "仓库ID") @PathVariable @NotNull Long id,
            @RequestBody Map<String, Object> params) {
        BigDecimal requiredCapacity = new BigDecimal(params.get("requiredCapacity").toString());
        return Result.success(warehouseService.checkWarehouseCapacitySufficient(id, requiredCapacity));
    }
}