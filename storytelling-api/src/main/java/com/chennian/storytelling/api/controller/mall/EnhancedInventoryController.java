package com.chennian.storytelling.api.controller.mall;

import com.chennian.storytelling.bean.model.InventoryLog;
import com.chennian.storytelling.common.response.ServerResponseEntity;
import com.chennian.storytelling.common.enums.InventoryErrorCode;
import com.chennian.storytelling.service.EnhancedInventoryService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
/**
 * 增强库存管理控制器
 */
@Api(tags = "增强库存管理")
@RestController
@RequestMapping("/api/inventory/enhanced")
public class EnhancedInventoryController {

    @Autowired
    private EnhancedInventoryService enhancedInventoryService;

    @ApiOperation("安全扣减库存")
    @PostMapping("/reduce-stock")
    public ServerResponseEntity<Boolean> safeReduceStock(
            @ApiParam("产品ID") @RequestParam Long productId,
            @ApiParam("仓库ID") @RequestParam Long warehouseId,
            @ApiParam("扣减数量") @RequestParam Integer quantity,
            @ApiParam("操作类型") @RequestParam String operationType,
            @ApiParam("关联订单号") @RequestParam(required = false) String relatedOrderNo,
            @ApiParam("操作人ID") @RequestParam Long operatorId,
            @ApiParam("备注") @RequestParam(required = false) String remark) {
        
        boolean success = enhancedInventoryService.safeReduceStock(
            productId, warehouseId, quantity, relatedOrderNo, operatorId);
        
        return success ? ServerResponseEntity.success(true) : ServerResponseEntity.fail(InventoryErrorCode.STOCK_REDUCE_FAILED);
    }

    @ApiOperation("安全增加库存")
    @PostMapping("/increase-stock")
    public ServerResponseEntity<Boolean> safeIncreaseStock(
            @ApiParam("产品ID") @RequestParam Long productId,
            @ApiParam("仓库ID") @RequestParam Long warehouseId,
            @ApiParam("增加数量") @RequestParam Integer quantity,
            @ApiParam("操作类型") @RequestParam String operationType,
            @ApiParam("关联订单号") @RequestParam(required = false) String relatedOrderNo,
            @ApiParam("操作人ID") @RequestParam Long operatorId,
            @ApiParam("备注") @RequestParam(required = false) String remark) {
        
        boolean success = enhancedInventoryService.safeIncreaseStock(
            productId, warehouseId, quantity, Integer.valueOf(operationType), relatedOrderNo, operatorId);
        
        return success ? ServerResponseEntity.success(true) : ServerResponseEntity.fail(InventoryErrorCode.STOCK_INCREASE_FAILED);
    }

    @ApiOperation("批量库存操作")
    @PostMapping("/batch-operation")
    public ServerResponseEntity<Boolean> batchStockOperation(
            @ApiParam("库存操作列表") @RequestBody List<EnhancedInventoryService.StockOperation> operations,
            @ApiParam("操作人ID") @RequestParam Long operatorId) {
        
        boolean success = enhancedInventoryService.batchSafeStockOperation(operations);
        return ServerResponseEntity.success(success);
    }

    @ApiOperation("从外部系统同步库存")
    @PostMapping("/sync-external")
    public ServerResponseEntity<Boolean> syncInventoryFromExternal(
            @ApiParam("产品ID") @RequestParam Long productId,
            @ApiParam("仓库ID") @RequestParam Long warehouseId,
            @ApiParam("外部系统库存数量") @RequestParam Integer externalQuantity,
            @ApiParam("外部系统名称") @RequestParam String externalSystem,
            @ApiParam("操作人ID") @RequestParam Long operatorId) {
        
        boolean success = enhancedInventoryService.syncInventoryFromExternalSystem(productId, warehouseId);
        
        return success ? ServerResponseEntity.success(true) : ServerResponseEntity.fail(InventoryErrorCode.STOCK_SYNC_FAILED);
    }

    @ApiOperation("批量同步库存")
    @PostMapping("/batch-sync")
    public ServerResponseEntity<Integer> batchSyncInventory(
            @ApiParam("同步请求列表") @RequestBody List<EnhancedInventoryService.InventorySyncRequest> syncRequests,
            @ApiParam("操作人ID") @RequestParam Long operatorId) {
        
        int successCount = enhancedInventoryService.batchSyncInventory(syncRequests);
        return ServerResponseEntity.success(successCount);
    }

    @ApiOperation("检查库存锁状态")
    @GetMapping("/lock-status/{productId}/{warehouseId}")
    public ServerResponseEntity<Boolean> checkInventoryLockStatus(
            @ApiParam("产品ID") @PathVariable Long productId,
            @ApiParam("仓库ID") @PathVariable Long warehouseId) {
        
        boolean isLocked = enhancedInventoryService.isInventoryLocked(productId, warehouseId);
        return ServerResponseEntity.success(isLocked);
    }

    @ApiOperation("释放库存锁")
    @PostMapping("/release-lock")
    public ServerResponseEntity<Boolean> releaseInventoryLock(
            @ApiParam("产品ID") @RequestParam Long productId,
            @ApiParam("仓库ID") @RequestParam Long warehouseId) {
        
        boolean success = enhancedInventoryService.releaseInventoryLock(productId, warehouseId);
        return ServerResponseEntity.success(success);
    }

    @ApiOperation("记录库存操作日志")
    @PostMapping("/log")
    public ServerResponseEntity<Boolean> recordInventoryLog(
            @ApiParam("产品ID") @RequestParam Long productId,
            @ApiParam("仓库ID") @RequestParam Long warehouseId,
            @ApiParam("操作类型") @RequestParam Integer operationType,
            @ApiParam("操作前数量") @RequestParam Integer beforeQuantity,
            @ApiParam("操作后数量") @RequestParam Integer afterQuantity,
            @ApiParam("变更数量") @RequestParam Integer changeQuantity,
            @ApiParam("关联订单号") @RequestParam(required = false) String relatedOrderNo,
            @ApiParam("操作原因") @RequestParam String reason,
            @ApiParam("备注") @RequestParam(required = false) String remark,
            @ApiParam("操作人ID") @RequestParam Long operatorId) {
        
        try {
            InventoryLog inventoryLog = new InventoryLog();
            inventoryLog.setProductId(productId);
            inventoryLog.setWarehouseId(warehouseId);
            inventoryLog.setOperationType(operationType);
            inventoryLog.setBeforeQuantity(beforeQuantity);
            inventoryLog.setAfterQuantity(afterQuantity);
            inventoryLog.setChangeQuantity(changeQuantity);
            inventoryLog.setDocumentNumber(relatedOrderNo);
            inventoryLog.setReason(reason);
            inventoryLog.setRemark(remark);
            inventoryLog.setOperatorId(operatorId);
            
            boolean success = enhancedInventoryService.recordInventoryLog(inventoryLog);
            return ServerResponseEntity.success(success);
        } catch (Exception e) {
            return ServerResponseEntity.fail(InventoryErrorCode.INVENTORY_LOG_RECORD_FAILED);
        }
    }

    @ApiOperation("分页查询库存操作日志")
    @GetMapping("/logs")
    public ServerResponseEntity<List<InventoryLog>> getInventoryLogs(
            @ApiParam("页码") @RequestParam(defaultValue = "1") Integer pageNum,
            @ApiParam("每页大小") @RequestParam(defaultValue = "10") Integer pageSize,
            @ApiParam("产品ID") @RequestParam(required = false) Long productId,
            @ApiParam("仓库ID") @RequestParam(required = false) Long warehouseId,
            @ApiParam("操作类型") @RequestParam(required = false) String operationType,
            @ApiParam("开始时间") @RequestParam(required = false) LocalDateTime startTime,
            @ApiParam("结束时间") @RequestParam(required = false) LocalDateTime endTime) {
        
        List<InventoryLog> logs = enhancedInventoryService.getInventoryOperationHistory(productId, warehouseId, 30);
        return ServerResponseEntity.success(logs);
    }

    @ApiOperation("检查库存一致性")
    @PostMapping("/check-consistency")
    public ServerResponseEntity<EnhancedInventoryService.InventoryConsistencyResult> checkInventoryConsistency(
            @ApiParam("产品ID") @RequestParam Long productId,
            @ApiParam("仓库ID") @RequestParam Long warehouseId) {
        
        EnhancedInventoryService.InventoryConsistencyResult result = 
            enhancedInventoryService.checkInventoryConsistency(productId, warehouseId);
        
        return ServerResponseEntity.success(result);
    }

    @ApiOperation("修复库存一致性")
    @PostMapping("/fix-consistency")
    public ServerResponseEntity<Boolean> fixInventoryConsistency(
            @ApiParam("产品ID") @RequestParam Long productId,
            @ApiParam("仓库ID") @RequestParam Long warehouseId,
            @ApiParam("正确的库存数量") @RequestParam Integer correctQuantity,
            @ApiParam("操作人ID") @RequestParam Long operatorId,
            @ApiParam("修复原因") @RequestParam String reason) {
        
        boolean success = enhancedInventoryService.fixInventoryInconsistency(
            productId, warehouseId, operatorId);
        
        return success ? ServerResponseEntity.success(true) : ServerResponseEntity.fail(InventoryErrorCode.STOCK_CONSISTENCY_FIX_FAILED);
    }

    @ApiOperation("获取操作类型列表")
    @GetMapping("/operation-types")
    public ServerResponseEntity<Map<String, String>> getOperationTypes() {
        Map<String, String> types = enhancedInventoryService.getOperationTypes();
        return ServerResponseEntity.success(types);
    }

    @ApiOperation("获取库存操作统计")
    @GetMapping("/operation-statistics")
    public ServerResponseEntity<EnhancedInventoryService.OperationStatistics> getOperationStatistics(
            @ApiParam("产品ID") @RequestParam(required = false) Long productId,
            @ApiParam("仓库ID") @RequestParam(required = false) Long warehouseId,
            @ApiParam("开始时间") @RequestParam(required = false) String startTime,
            @ApiParam("结束时间") @RequestParam(required = false) String endTime) {
        
        LocalDateTime start = null;
        LocalDateTime end = null;
        
        try {
            if (startTime != null && !startTime.isEmpty()) {
                start = LocalDateTime.parse(startTime);
            }
            if (endTime != null && !endTime.isEmpty()) {
                end = LocalDateTime.parse(endTime);
            }
        } catch (Exception e) {
            return ServerResponseEntity.fail(InventoryErrorCode.INVALID_PARAMETER);
        }
        
        EnhancedInventoryService.OperationStatistics statistics = 
            enhancedInventoryService.getOperationStatistics(productId, warehouseId, start, end);
        
        return ServerResponseEntity.success(statistics);
    }

    @ApiOperation("批量检查库存一致性")
    @PostMapping("/batch-check-consistency")
    public ServerResponseEntity<List<EnhancedInventoryService.InventoryConsistencyResult>> batchCheckInventoryConsistency(
            @ApiParam("产品仓库对列表") @RequestBody List<Map<String, Long>> productWarehousePairs) {
        
        List<EnhancedInventoryService.InventoryConsistencyResult> results = 
            productWarehousePairs.stream()
                .map(pair -> enhancedInventoryService.checkInventoryConsistency(
                    pair.get("productId"), pair.get("warehouseId")))
                .toList();
        
        return ServerResponseEntity.success(results);
    }

    @ApiOperation("获取库存锁状态")
    @GetMapping("/lock-status")
    public ServerResponseEntity<EnhancedInventoryService.LockStatusDetail> getLockStatus(
            @ApiParam("产品ID") @RequestParam Long productId,
            @ApiParam("仓库ID") @RequestParam Long warehouseId) {
        
        EnhancedInventoryService.LockStatusDetail lockStatus = 
            enhancedInventoryService.getLockStatusDetail(productId, warehouseId);
        
        return ServerResponseEntity.success(lockStatus);
    }
}