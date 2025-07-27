package com.chennian.storytelling.inventory.controller;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.chennian.storytelling.common.result.Result;
import com.chennian.storytelling.inventory.entity.InventoryRecord;
import com.chennian.storytelling.inventory.service.InventoryRecordService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

/**
 * 库存变动记录控制器
 * 
 * @author chennian
 * @since 2024-01-01
 */
@Tag(name = "库存变动记录", description = "库存变动记录相关接口")
@RestController
@RequestMapping("/api/inventory-record")
@RequiredArgsConstructor
@Validated
public class InventoryRecordController {

    private final InventoryRecordService inventoryRecordService;

    @Operation(summary = "根据ID查询库存变动记录")
    @GetMapping("/{id}")
    public Result<InventoryRecord> getInventoryRecordById(
            @Parameter(description = "记录ID") @PathVariable @NotNull Long id) {
        return Result.success(inventoryRecordService.getInventoryRecordById(id));
    }

    @Operation(summary = "分页查询库存变动记录列表")
    @GetMapping("/page")
    public Result<IPage<InventoryRecord>> getInventoryRecordPage(
            @Parameter(description = "页码") @RequestParam(defaultValue = "1") Integer current,
            @Parameter(description = "每页大小") @RequestParam(defaultValue = "10") Integer size,
            @Parameter(description = "库存ID") @RequestParam(required = false) Long inventoryId,
            @Parameter(description = "商品ID") @RequestParam(required = false) Long productId,
            @Parameter(description = "仓库ID") @RequestParam(required = false) Long warehouseId,
            @Parameter(description = "变动类型") @RequestParam(required = false) Integer changeType,
            @Parameter(description = "业务单号") @RequestParam(required = false) String businessNo,
            @Parameter(description = "操作人ID") @RequestParam(required = false) Long operatorId,
            @Parameter(description = "开始时间") @RequestParam(required = false) @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss") LocalDateTime startTime,
            @Parameter(description = "结束时间") @RequestParam(required = false) @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss") LocalDateTime endTime) {
        Page<InventoryRecord> page = new Page<>(current, size);
        return Result.success(inventoryRecordService.getInventoryRecordPage(page, inventoryId, productId, warehouseId, changeType, businessNo, operatorId, startTime, endTime));
    }

    @Operation(summary = "根据库存ID查询变动记录列表")
    @GetMapping("/inventory/{inventoryId}")
    public Result<List<InventoryRecord>> getInventoryRecordListByInventoryId(
            @Parameter(description = "库存ID") @PathVariable @NotNull Long inventoryId) {
        return Result.success(inventoryRecordService.getInventoryRecordListByInventoryId(inventoryId));
    }

    @Operation(summary = "根据商品ID查询变动记录列表")
    @GetMapping("/product/{productId}")
    public Result<List<InventoryRecord>> getInventoryRecordListByProductId(
            @Parameter(description = "商品ID") @PathVariable @NotNull Long productId) {
        return Result.success(inventoryRecordService.getInventoryRecordListByProductId(productId));
    }

    @Operation(summary = "根据仓库ID查询变动记录列表")
    @GetMapping("/warehouse/{warehouseId}")
    public Result<List<InventoryRecord>> getInventoryRecordListByWarehouseId(
            @Parameter(description = "仓库ID") @PathVariable @NotNull Long warehouseId) {
        return Result.success(inventoryRecordService.getInventoryRecordListByWarehouseId(warehouseId));
    }

    @Operation(summary = "根据业务单号查询变动记录列表")
    @GetMapping("/business/{businessNo}")
    public Result<List<InventoryRecord>> getInventoryRecordListByBusinessNo(
            @Parameter(description = "业务单号") @PathVariable String businessNo) {
        return Result.success(inventoryRecordService.getInventoryRecordListByBusinessNo(businessNo));
    }

    @Operation(summary = "根据变动类型查询变动记录列表")
    @GetMapping("/change-type/{changeType}")
    public Result<List<InventoryRecord>> getInventoryRecordListByChangeType(
            @Parameter(description = "变动类型") @PathVariable @NotNull Integer changeType,
            @Parameter(description = "开始时间") @RequestParam(required = false) @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss") LocalDateTime startTime,
            @Parameter(description = "结束时间") @RequestParam(required = false) @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss") LocalDateTime endTime) {
        return Result.success(inventoryRecordService.getInventoryRecordListByChangeType(changeType, startTime, endTime));
    }

    @Operation(summary = "根据操作人查询变动记录列表")
    @GetMapping("/operator/{operatorId}")
    public Result<List<InventoryRecord>> getInventoryRecordListByOperator(
            @Parameter(description = "操作人ID") @PathVariable @NotNull Long operatorId,
            @Parameter(description = "开始时间") @RequestParam(required = false) @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss") LocalDateTime startTime,
            @Parameter(description = "结束时间") @RequestParam(required = false) @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss") LocalDateTime endTime) {
        return Result.success(inventoryRecordService.getInventoryRecordListByOperator(operatorId, startTime, endTime));
    }

    @Operation(summary = "创建库存变动记录")
    @PostMapping
    public Result<Boolean> createInventoryRecord(@Valid @RequestBody InventoryRecord inventoryRecord) {
        return Result.success(inventoryRecordService.createInventoryRecord(inventoryRecord));
    }

    @Operation(summary = "批量创建库存变动记录")
    @PostMapping("/batch")
    public Result<Boolean> batchCreateInventoryRecord(@RequestBody List<InventoryRecord> inventoryRecordList) {
        return Result.success(inventoryRecordService.batchCreateInventoryRecord(inventoryRecordList));
    }

    @Operation(summary = "删除库存变动记录")
    @DeleteMapping("/{id}")
    public Result<Boolean> deleteInventoryRecord(
            @Parameter(description = "记录ID") @PathVariable @NotNull Long id) {
        return Result.success(inventoryRecordService.deleteInventoryRecord(id));
    }

    @Operation(summary = "批量删除库存变动记录")
    @DeleteMapping("/batch")
    public Result<Boolean> batchDeleteInventoryRecord(@RequestBody List<Long> recordIds) {
        return Result.success(inventoryRecordService.batchDeleteInventoryRecord(recordIds));
    }

    @Operation(summary = "根据时间范围删除库存变动记录")
    @DeleteMapping("/time-range")
    public Result<Boolean> deleteInventoryRecordByTimeRange(
            @Parameter(description = "结束时间") @RequestParam @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss") LocalDateTime endTime) {
        return Result.success(inventoryRecordService.deleteInventoryRecordByTimeRange(endTime));
    }

    @Operation(summary = "获取库存变动统计信息")
    @GetMapping("/statistics/inventory/{inventoryId}")
    public Result<Map<String, Object>> getInventoryChangeStatistics(
            @Parameter(description = "库存ID") @PathVariable @NotNull Long inventoryId,
            @Parameter(description = "开始时间") @RequestParam(required = false) @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss") LocalDateTime startTime,
            @Parameter(description = "结束时间") @RequestParam(required = false) @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss") LocalDateTime endTime) {
        return Result.success(inventoryRecordService.getInventoryChangeStatistics(inventoryId, startTime, endTime));
    }

    @Operation(summary = "获取商品变动统计信息")
    @GetMapping("/statistics/product/{productId}")
    public Result<Map<String, Object>> getProductChangeStatistics(
            @Parameter(description = "商品ID") @PathVariable @NotNull Long productId,
            @Parameter(description = "开始时间") @RequestParam(required = false) @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss") LocalDateTime startTime,
            @Parameter(description = "结束时间") @RequestParam(required = false) @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss") LocalDateTime endTime) {
        return Result.success(inventoryRecordService.getProductChangeStatistics(productId, startTime, endTime));
    }

    @Operation(summary = "获取仓库变动统计信息")
    @GetMapping("/statistics/warehouse/{warehouseId}")
    public Result<Map<String, Object>> getWarehouseChangeStatistics(
            @Parameter(description = "仓库ID") @PathVariable @NotNull Long warehouseId,
            @Parameter(description = "开始时间") @RequestParam(required = false) @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss") LocalDateTime startTime,
            @Parameter(description = "结束时间") @RequestParam(required = false) @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss") LocalDateTime endTime) {
        return Result.success(inventoryRecordService.getWarehouseChangeStatistics(warehouseId, startTime, endTime));
    }

    @Operation(summary = "获取入库统计信息")
    @GetMapping("/statistics/inbound")
    public Result<Map<String, Object>> getInboundStatistics(
            @Parameter(description = "仓库ID") @RequestParam(required = false) Long warehouseId,
            @Parameter(description = "开始时间") @RequestParam(required = false) @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss") LocalDateTime startTime,
            @Parameter(description = "结束时间") @RequestParam(required = false) @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss") LocalDateTime endTime) {
        return Result.success(inventoryRecordService.getInboundStatistics(warehouseId, startTime, endTime));
    }

    @Operation(summary = "获取出库统计信息")
    @GetMapping("/statistics/outbound")
    public Result<Map<String, Object>> getOutboundStatistics(
            @Parameter(description = "仓库ID") @RequestParam(required = false) Long warehouseId,
            @Parameter(description = "开始时间") @RequestParam(required = false) @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss") LocalDateTime startTime,
            @Parameter(description = "结束时间") @RequestParam(required = false) @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss") LocalDateTime endTime) {
        return Result.success(inventoryRecordService.getOutboundStatistics(warehouseId, startTime, endTime));
    }

    @Operation(summary = "获取调拨统计信息")
    @GetMapping("/statistics/transfer")
    public Result<Map<String, Object>> getTransferStatistics(
            @Parameter(description = "仓库ID") @RequestParam(required = false) Long warehouseId,
            @Parameter(description = "开始时间") @RequestParam(required = false) @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss") LocalDateTime startTime,
            @Parameter(description = "结束时间") @RequestParam(required = false) @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss") LocalDateTime endTime) {
        return Result.success(inventoryRecordService.getTransferStatistics(warehouseId, startTime, endTime));
    }

    @Operation(summary = "获取盘点统计信息")
    @GetMapping("/statistics/check")
    public Result<Map<String, Object>> getCheckStatistics(
            @Parameter(description = "仓库ID") @RequestParam(required = false) Long warehouseId,
            @Parameter(description = "开始时间") @RequestParam(required = false) @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss") LocalDateTime startTime,
            @Parameter(description = "结束时间") @RequestParam(required = false) @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss") LocalDateTime endTime) {
        return Result.success(inventoryRecordService.getCheckStatistics(warehouseId, startTime, endTime));
    }

    @Operation(summary = "获取库存周转率")
    @GetMapping("/statistics/turnover/inventory/{inventoryId}")
    public Result<Double> getInventoryTurnoverRate(
            @Parameter(description = "库存ID") @PathVariable @NotNull Long inventoryId,
            @Parameter(description = "开始时间") @RequestParam @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss") LocalDateTime startTime,
            @Parameter(description = "结束时间") @RequestParam @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss") LocalDateTime endTime) {
        return Result.success(inventoryRecordService.getInventoryTurnoverRate(inventoryId, startTime, endTime));
    }

    @Operation(summary = "获取商品库存周转率")
    @GetMapping("/statistics/turnover/product/{productId}")
    public Result<Double> getProductInventoryTurnoverRate(
            @Parameter(description = "商品ID") @PathVariable @NotNull Long productId,
            @Parameter(description = "开始时间") @RequestParam @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss") LocalDateTime startTime,
            @Parameter(description = "结束时间") @RequestParam @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss") LocalDateTime endTime) {
        return Result.success(inventoryRecordService.getProductInventoryTurnoverRate(productId, startTime, endTime));
    }

    @Operation(summary = "导出库存变动记录")
    @PostMapping("/export")
    public Result<String> exportInventoryRecord(@RequestBody Map<String, Object> params) {
        Long inventoryId = params.get("inventoryId") != null ? Long.valueOf(params.get("inventoryId").toString()) : null;
        Integer changeType = params.get("changeType") != null ? (Integer) params.get("changeType") : null;
        String businessNo = (String) params.get("businessNo");
        LocalDateTime startTime = params.get("startTime") != null ? LocalDateTime.parse(params.get("startTime").toString()) : null;
        LocalDateTime endTime = params.get("endTime") != null ? LocalDateTime.parse(params.get("endTime").toString()) : null;
        return Result.success(inventoryRecordService.exportInventoryRecord(inventoryId, changeType, businessNo, startTime, endTime));
    }

    @Operation(summary = "生成库存变动报表")
    @PostMapping("/report")
    public Result<Map<String, Object>> generateInventoryChangeReport(@RequestBody Map<String, Object> params) {
        LocalDateTime startTime = LocalDateTime.parse(params.get("startTime").toString());
        LocalDateTime endTime = LocalDateTime.parse(params.get("endTime").toString());
        Integer reportType = (Integer) params.get("reportType");
        return Result.success(inventoryRecordService.generateInventoryChangeReport(startTime, endTime, reportType));
    }
}