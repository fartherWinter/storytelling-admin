package com.chennian.storytelling.inventory.controller;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.chennian.storytelling.common.result.Result;
import com.chennian.storytelling.inventory.entity.InventoryLog;
import com.chennian.storytelling.inventory.service.InventoryLogService;
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
 * 库存操作日志控制器
 * 
 * @author chennian
 * @since 2024-01-01
 */
@Tag(name = "库存操作日志", description = "库存操作日志相关接口")
@RestController
@RequestMapping("/api/inventory-log")
@RequiredArgsConstructor
@Validated
public class InventoryLogController {

    private final InventoryLogService inventoryLogService;

    @Operation(summary = "根据ID查询库存操作日志")
    @GetMapping("/{id}")
    public Result<InventoryLog> getInventoryLogById(
            @Parameter(description = "日志ID") @PathVariable @NotNull Long id) {
        return Result.success(inventoryLogService.getInventoryLogById(id));
    }

    @Operation(summary = "分页查询库存操作日志列表")
    @GetMapping("/page")
    public Result<IPage<InventoryLog>> getInventoryLogPage(
            @Parameter(description = "页码") @RequestParam(defaultValue = "1") Integer current,
            @Parameter(description = "每页大小") @RequestParam(defaultValue = "10") Integer size,
            @Parameter(description = "库存ID") @RequestParam(required = false) Long inventoryId,
            @Parameter(description = "商品ID") @RequestParam(required = false) Long productId,
            @Parameter(description = "仓库ID") @RequestParam(required = false) Long warehouseId,
            @Parameter(description = "SKU编码") @RequestParam(required = false) String skuCode,
            @Parameter(description = "操作类型") @RequestParam(required = false) Integer operationType,
            @Parameter(description = "操作结果") @RequestParam(required = false) Integer operationResult,
            @Parameter(description = "业务单号") @RequestParam(required = false) String businessNo,
            @Parameter(description = "业务类型") @RequestParam(required = false) Integer businessType,
            @Parameter(description = "操作人ID") @RequestParam(required = false) Long operatorId,
            @Parameter(description = "操作人姓名") @RequestParam(required = false) String operatorName,
            @Parameter(description = "开始时间") @RequestParam(required = false) @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss") LocalDateTime startTime,
            @Parameter(description = "结束时间") @RequestParam(required = false) @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss") LocalDateTime endTime) {
        Page<InventoryLog> page = new Page<>(current, size);
        return Result.success(inventoryLogService.getInventoryLogPage(page, inventoryId, productId, warehouseId, skuCode, operationType, operationResult, businessNo, businessType, operatorId, operatorName, startTime, endTime));
    }

    @Operation(summary = "根据库存ID查询操作日志列表")
    @GetMapping("/inventory/{inventoryId}")
    public Result<List<InventoryLog>> getInventoryLogListByInventoryId(
            @Parameter(description = "库存ID") @PathVariable @NotNull Long inventoryId) {
        return Result.success(inventoryLogService.getInventoryLogListByInventoryId(inventoryId));
    }

    @Operation(summary = "根据商品ID查询操作日志列表")
    @GetMapping("/product/{productId}")
    public Result<List<InventoryLog>> getInventoryLogListByProductId(
            @Parameter(description = "商品ID") @PathVariable @NotNull Long productId) {
        return Result.success(inventoryLogService.getInventoryLogListByProductId(productId));
    }

    @Operation(summary = "根据仓库ID查询操作日志列表")
    @GetMapping("/warehouse/{warehouseId}")
    public Result<List<InventoryLog>> getInventoryLogListByWarehouseId(
            @Parameter(description = "仓库ID") @PathVariable @NotNull Long warehouseId) {
        return Result.success(inventoryLogService.getInventoryLogListByWarehouseId(warehouseId));
    }

    @Operation(summary = "根据业务单号查询操作日志列表")
    @GetMapping("/business/{businessNo}")
    public Result<List<InventoryLog>> getInventoryLogListByBusinessNo(
            @Parameter(description = "业务单号") @PathVariable String businessNo) {
        return Result.success(inventoryLogService.getInventoryLogListByBusinessNo(businessNo));
    }

    @Operation(summary = "根据操作类型查询操作日志列表")
    @GetMapping("/operation-type/{operationType}")
    public Result<List<InventoryLog>> getInventoryLogListByOperationType(
            @Parameter(description = "操作类型") @PathVariable @NotNull Integer operationType,
            @Parameter(description = "开始时间") @RequestParam(required = false) @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss") LocalDateTime startTime,
            @Parameter(description = "结束时间") @RequestParam(required = false) @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss") LocalDateTime endTime) {
        return Result.success(inventoryLogService.getInventoryLogListByOperationType(operationType, startTime, endTime));
    }

    @Operation(summary = "根据操作人查询操作日志列表")
    @GetMapping("/operator/{operatorId}")
    public Result<List<InventoryLog>> getInventoryLogListByOperator(
            @Parameter(description = "操作人ID") @PathVariable @NotNull Long operatorId,
            @Parameter(description = "开始时间") @RequestParam(required = false) @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss") LocalDateTime startTime,
            @Parameter(description = "结束时间") @RequestParam(required = false) @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss") LocalDateTime endTime) {
        return Result.success(inventoryLogService.getInventoryLogListByOperator(operatorId, startTime, endTime));
    }

    @Operation(summary = "根据操作结果查询操作日志列表")
    @GetMapping("/operation-result/{operationResult}")
    public Result<List<InventoryLog>> getInventoryLogListByOperationResult(
            @Parameter(description = "操作结果") @PathVariable @NotNull Integer operationResult,
            @Parameter(description = "开始时间") @RequestParam(required = false) @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss") LocalDateTime startTime,
            @Parameter(description = "结束时间") @RequestParam(required = false) @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss") LocalDateTime endTime) {
        return Result.success(inventoryLogService.getInventoryLogListByOperationResult(operationResult, startTime, endTime));
    }

    @Operation(summary = "创建库存操作日志")
    @PostMapping
    public Result<Boolean> createInventoryLog(@Valid @RequestBody InventoryLog inventoryLog) {
        return Result.success(inventoryLogService.createInventoryLog(inventoryLog));
    }

    @Operation(summary = "批量创建库存操作日志")
    @PostMapping("/batch")
    public Result<Boolean> batchCreateInventoryLog(@RequestBody List<InventoryLog> inventoryLogList) {
        return Result.success(inventoryLogService.batchCreateInventoryLog(inventoryLogList));
    }

    @Operation(summary = "删除库存操作日志")
    @DeleteMapping("/{id}")
    public Result<Boolean> deleteInventoryLog(
            @Parameter(description = "日志ID") @PathVariable @NotNull Long id) {
        return Result.success(inventoryLogService.deleteInventoryLog(id));
    }

    @Operation(summary = "批量删除库存操作日志")
    @DeleteMapping("/batch")
    public Result<Boolean> batchDeleteInventoryLog(@RequestBody List<Long> logIds) {
        return Result.success(inventoryLogService.batchDeleteInventoryLog(logIds));
    }

    @Operation(summary = "根据时间范围删除库存操作日志")
    @DeleteMapping("/time-range")
    public Result<Boolean> deleteInventoryLogByTimeRange(
            @Parameter(description = "结束时间") @RequestParam @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss") LocalDateTime endTime) {
        return Result.success(inventoryLogService.deleteInventoryLogByTimeRange(endTime));
    }

    @Operation(summary = "清理过期日志")
    @DeleteMapping("/clean")
    public Result<Boolean> cleanExpiredLogs(
            @Parameter(description = "保留天数") @RequestParam @NotNull Integer retentionDays) {
        return Result.success(inventoryLogService.cleanExpiredLogs(retentionDays));
    }

    @Operation(summary = "获取操作统计信息")
    @GetMapping("/statistics/operation")
    public Result<Map<String, Object>> getOperationStatistics(
            @Parameter(description = "库存ID") @RequestParam(required = false) Long inventoryId,
            @Parameter(description = "开始时间") @RequestParam(required = false) @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss") LocalDateTime startTime,
            @Parameter(description = "结束时间") @RequestParam(required = false) @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss") LocalDateTime endTime) {
        return Result.success(inventoryLogService.getOperationStatistics(inventoryId, startTime, endTime));
    }

    @Operation(summary = "获取操作人统计信息")
    @GetMapping("/statistics/operator")
    public Result<List<Map<String, Object>>> getOperatorStatistics(
            @Parameter(description = "开始时间") @RequestParam(required = false) @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss") LocalDateTime startTime,
            @Parameter(description = "结束时间") @RequestParam(required = false) @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss") LocalDateTime endTime) {
        return Result.success(inventoryLogService.getOperatorStatistics(startTime, endTime));
    }

    @Operation(summary = "获取操作类型统计信息")
    @GetMapping("/statistics/operation-type")
    public Result<List<Map<String, Object>>> getOperationTypeStatistics(
            @Parameter(description = "开始时间") @RequestParam(required = false) @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss") LocalDateTime startTime,
            @Parameter(description = "结束时间") @RequestParam(required = false) @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss") LocalDateTime endTime) {
        return Result.success(inventoryLogService.getOperationTypeStatistics(startTime, endTime));
    }

    @Operation(summary = "获取操作结果统计信息")
    @GetMapping("/statistics/operation-result")
    public Result<Map<String, Object>> getOperationResultStatistics(
            @Parameter(description = "开始时间") @RequestParam(required = false) @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss") LocalDateTime startTime,
            @Parameter(description = "结束时间") @RequestParam(required = false) @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss") LocalDateTime endTime) {
        return Result.success(inventoryLogService.getOperationResultStatistics(startTime, endTime));
    }

    @Operation(summary = "获取错误操作统计信息")
    @GetMapping("/statistics/error")
    public Result<List<Map<String, Object>>> getErrorOperationStatistics(
            @Parameter(description = "开始时间") @RequestParam(required = false) @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss") LocalDateTime startTime,
            @Parameter(description = "结束时间") @RequestParam(required = false) @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss") LocalDateTime endTime) {
        return Result.success(inventoryLogService.getErrorOperationStatistics(startTime, endTime));
    }

    @Operation(summary = "获取慢操作统计信息")
    @GetMapping("/statistics/slow")
    public Result<List<Map<String, Object>>> getSlowOperationStatistics(
            @Parameter(description = "执行时间阈值(毫秒)") @RequestParam @NotNull Long executionTime,
            @Parameter(description = "开始时间") @RequestParam(required = false) @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss") LocalDateTime startTime,
            @Parameter(description = "结束时间") @RequestParam(required = false) @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss") LocalDateTime endTime) {
        return Result.success(inventoryLogService.getSlowOperationStatistics(executionTime, startTime, endTime));
    }

    @Operation(summary = "获取平均执行时间")
    @GetMapping("/statistics/average-execution-time")
    public Result<Double> getAverageExecutionTime(
            @Parameter(description = "操作类型") @RequestParam(required = false) Integer operationType,
            @Parameter(description = "开始时间") @RequestParam(required = false) @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss") LocalDateTime startTime,
            @Parameter(description = "结束时间") @RequestParam(required = false) @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss") LocalDateTime endTime) {
        return Result.success(inventoryLogService.getAverageExecutionTime(operationType, startTime, endTime));
    }

    @Operation(summary = "获取操作频率统计信息")
    @GetMapping("/statistics/frequency")
    public Result<List<Map<String, Object>>> getOperationFrequencyStatistics(
            @Parameter(description = "开始时间") @RequestParam(required = false) @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss") LocalDateTime startTime,
            @Parameter(description = "结束时间") @RequestParam(required = false) @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss") LocalDateTime endTime,
            @Parameter(description = "分组方式(hour/day/month)") @RequestParam(defaultValue = "day") String groupBy) {
        return Result.success(inventoryLogService.getOperationFrequencyStatistics(startTime, endTime, groupBy));
    }

    @Operation(summary = "记录库存操作日志")
    @PostMapping("/record")
    public Result<Boolean> recordInventoryLog(@RequestBody Map<String, Object> params) {
        Long inventoryId = params.get("inventoryId") != null ? Long.valueOf(params.get("inventoryId").toString()) : null;
        Integer operationType = (Integer) params.get("operationType");
        String operationDesc = (String) params.get("operationDesc");
        String beforeData = (String) params.get("beforeData");
        String afterData = (String) params.get("afterData");
        Integer operationResult = (Integer) params.get("operationResult");
        String errorMessage = (String) params.get("errorMessage");
        String businessNo = (String) params.get("businessNo");
        Integer businessType = params.get("businessType") != null ? (Integer) params.get("businessType") : null;
        Long executionTime = params.get("executionTime") != null ? Long.valueOf(params.get("executionTime").toString()) : null;
        String remark = (String) params.get("remark");
        
        return Result.success(inventoryLogService.recordInventoryLog(inventoryId, operationType, operationDesc, beforeData, afterData, operationResult, errorMessage, businessNo, businessType, executionTime, remark));
    }

    @Operation(summary = "导出库存操作日志")
    @PostMapping("/export")
    public Result<String> exportInventoryLog(@RequestBody Map<String, Object> params) {
        Long inventoryId = params.get("inventoryId") != null ? Long.valueOf(params.get("inventoryId").toString()) : null;
        Integer operationType = params.get("operationType") != null ? (Integer) params.get("operationType") : null;
        Integer operationResult = params.get("operationResult") != null ? (Integer) params.get("operationResult") : null;
        LocalDateTime startTime = params.get("startTime") != null ? LocalDateTime.parse(params.get("startTime").toString()) : null;
        LocalDateTime endTime = params.get("endTime") != null ? LocalDateTime.parse(params.get("endTime").toString()) : null;
        
        return Result.success(inventoryLogService.exportInventoryLog(inventoryId, operationType, operationResult, startTime, endTime));
    }

    @Operation(summary = "生成库存操作报表")
    @PostMapping("/report")
    public Result<Map<String, Object>> generateInventoryLogReport(@RequestBody Map<String, Object> params) {
        LocalDateTime startTime = LocalDateTime.parse(params.get("startTime").toString());
        LocalDateTime endTime = LocalDateTime.parse(params.get("endTime").toString());
        Integer reportType = (Integer) params.get("reportType");
        
        return Result.success(inventoryLogService.generateInventoryLogReport(startTime, endTime, reportType));
    }
}