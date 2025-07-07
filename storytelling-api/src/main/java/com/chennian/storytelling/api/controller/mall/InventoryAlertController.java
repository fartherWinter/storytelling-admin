package com.chennian.storytelling.api.controller.mall;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.chennian.storytelling.bean.model.InventoryAlert;
import com.chennian.storytelling.common.response.ServerResponseEntity;
import com.chennian.storytelling.common.enums.InventoryErrorCode;
import com.chennian.storytelling.service.InventoryAlertService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 库存预警管理控制器
 */
@Api(tags = "库存预警管理")
@RestController
@RequestMapping("/api/inventory/alert")
public class InventoryAlertController {

    @Autowired
    private InventoryAlertService inventoryAlertService;

    @ApiOperation("分页查询库存预警")
    @GetMapping("/page")
    public ServerResponseEntity<IPage<InventoryAlert>> getInventoryAlertPage(
            @ApiParam("页码") @RequestParam(defaultValue = "1") Integer pageNum,
            @ApiParam("每页大小") @RequestParam(defaultValue = "10") Integer pageSize,
            @ApiParam("预警类型") @RequestParam(required = false) Integer alertType,
            @ApiParam("预警级别") @RequestParam(required = false) Integer alertLevel,
            @ApiParam("处理状态") @RequestParam(required = false) Integer status,
            @ApiParam("仓库ID") @RequestParam(required = false) Long warehouseId) {
        
        IPage<InventoryAlert> pageParam = inventoryAlertService.getInventoryAlertPage(
            pageNum, pageSize, alertType, alertLevel, status, warehouseId);
        return ServerResponseEntity.success(pageParam);
    }

    @ApiOperation("获取未处理预警列表")
    @GetMapping("/unhandled")
    public ServerResponseEntity<List<InventoryAlert>> getUnhandledAlerts() {
        List<InventoryAlert> alerts = inventoryAlertService.getUnhandledAlerts();
        return ServerResponseEntity.success(alerts);
    }

    @ApiOperation("获取高优先级预警列表")
    @GetMapping("/high-priority")
    public ServerResponseEntity<List<InventoryAlert>> getHighPriorityAlerts() {
        List<InventoryAlert> alerts = inventoryAlertService.getHighPriorityAlerts();
        return ServerResponseEntity.success(alerts);
    }

    @ApiOperation("检查库存并生成预警")
    @PostMapping("/check")
    public ServerResponseEntity<Integer> checkInventoryAndGenerateAlerts(
            @ApiParam("产品ID") @RequestParam(required = false) Long productId,
            @ApiParam("仓库ID") @RequestParam(required = false) Long warehouseId) {
        
        int alertCount = inventoryAlertService.checkInventoryAndGenerateAlerts(productId, warehouseId);
        return ServerResponseEntity.success(alertCount);
    }

    @ApiOperation("手动创建库存预警")
    @PostMapping
    public ServerResponseEntity<Boolean> createInventoryAlert(
            @ApiParam("产品ID") @RequestParam Long productId,
            @ApiParam("仓库ID") @RequestParam Long warehouseId,
            @ApiParam("当前库存") @RequestParam Integer currentStock,
            @ApiParam("最小库存阈值") @RequestParam(required = false) Integer minStockThreshold,
            @ApiParam("最大库存阈值") @RequestParam(required = false) Integer maxStockThreshold,
            @ApiParam("预警类型") @RequestParam Integer alertType,
            @ApiParam("预警级别") @RequestParam Integer alertLevel) {
        
        boolean success = inventoryAlertService.createInventoryAlert(
            productId, warehouseId, currentStock, minStockThreshold, 
            maxStockThreshold, alertType, alertLevel);
        
        return success ? ServerResponseEntity.success(true) : ServerResponseEntity.fail(InventoryErrorCode.ALERT_CREATE_FAILED);
    }

    @ApiOperation("处理预警")
    @PutMapping("/{alertId}/handle")
    public ServerResponseEntity<Boolean> handleAlert(
            @ApiParam("预警ID") @PathVariable Long alertId,
            @ApiParam("处理人ID") @RequestParam Long handledBy,
            @ApiParam("处理备注") @RequestParam(required = false) String handledRemark) {
        
        boolean success = inventoryAlertService.handleAlert(alertId, handledBy, handledRemark);
        return success ? ServerResponseEntity.success(true) : ServerResponseEntity.fail(InventoryErrorCode.ALERT_PROCESS_FAILED);
    }

    @ApiOperation("忽略预警")
    @PutMapping("/{alertId}/ignore")
    public ServerResponseEntity<Boolean> ignoreAlert(
            @ApiParam("预警ID") @PathVariable Long alertId,
            @ApiParam("处理人ID") @RequestParam Long handledBy,
            @ApiParam("处理备注") @RequestParam(required = false) String handledRemark) {
        
        boolean success = inventoryAlertService.ignoreAlert(alertId, handledBy, handledRemark);
        return success ? ServerResponseEntity.success(true) : ServerResponseEntity.fail(InventoryErrorCode.ALERT_IGNORE_FAILED);
    }

    @ApiOperation("批量处理预警")
    @PutMapping("/batch-handle")
    public ServerResponseEntity<Integer> batchHandleAlerts(
            @ApiParam("预警ID列表") @RequestBody List<Long> alertIds,
            @ApiParam("处理人ID") @RequestParam Long handledBy,
            @ApiParam("处理备注") @RequestParam(required = false) String handledRemark) {
        
        int successCount = inventoryAlertService.batchHandleAlerts(alertIds, handledBy, handledRemark);
        return ServerResponseEntity.success(successCount);
    }

    @ApiOperation("获取预警统计信息")
    @GetMapping("/statistics")
    public ServerResponseEntity<Object> getAlertStatistics(
            @ApiParam("仓库ID") @RequestParam(required = false) Long warehouseId) {
        
        Object statistics = inventoryAlertService.getAlertStatistics(warehouseId);
        return ServerResponseEntity.success(statistics);
    }

    @ApiOperation("清理过期预警")
    @DeleteMapping("/cleanup")
    public ServerResponseEntity<Integer> cleanupExpiredAlerts(
            @ApiParam("过期天数") @RequestParam(defaultValue = "30") Integer days) {
        
        int cleanupCount = inventoryAlertService.cleanupExpiredAlerts(days.intValue());
        return ServerResponseEntity.success(cleanupCount);
    }

    @ApiOperation("发送预警通知")
    @PostMapping("/{alertId}/notify")
    public ServerResponseEntity<Boolean> sendAlertNotification(
            @ApiParam("预警ID") @PathVariable Long alertId) {
        
        boolean success = inventoryAlertService.sendAlertNotification(alertId);
        return success ? ServerResponseEntity.success(true) : ServerResponseEntity.fail(InventoryErrorCode.ALERT_NOTIFICATION_FAILED);
    }

    @ApiOperation("批量发送预警通知")
    @PostMapping("/batch-notify")
    public ServerResponseEntity<Integer> batchSendAlertNotifications(
            @ApiParam("预警级别") @RequestParam(required = false) Integer alertLevel) {
        
        int successCount = inventoryAlertService.batchSendAlertNotifications(alertLevel);
        return ServerResponseEntity.success(successCount);
    }

    @ApiOperation("获取预警详情")
    @GetMapping("/{alertId}")
    public ServerResponseEntity<InventoryAlert> getAlertById(
            @ApiParam("预警ID") @PathVariable Long alertId) {
        
        InventoryAlert alert = inventoryAlertService.getById(alertId);
        return alert != null ? ServerResponseEntity.success(alert) : ServerResponseEntity.fail(InventoryErrorCode.ALERT_NOT_FOUND);
    }

    @ApiOperation("获取预警类型列表")
    @GetMapping("/types")
    public ServerResponseEntity<Object> getAlertTypes() {
        // 返回预警类型枚举
        Object types = java.util.Map.of(
            "1", "库存不足",
            "2", "库存过多",
            "3", "库存异常",
            "4", "其他"
        );
        return ServerResponseEntity.success(types);
    }

    @ApiOperation("获取预警级别列表")
    @GetMapping("/levels")
    public ServerResponseEntity<Object> getAlertLevels() {
        // 返回预警级别枚举
        Object levels = java.util.Map.of(
            "1", "低",
            "2", "中",
            "3", "高",
            "4", "紧急"
        );
        return ServerResponseEntity.success(levels);
    }

    @ApiOperation("获取预警状态列表")
    @GetMapping("/statuses")
    public ServerResponseEntity<Object> getAlertStatuses() {
        // 返回预警状态枚举
        Object statuses = java.util.Map.of(
            "0", "未处理",
            "1", "已处理",
            "2", "已忽略"
        );
        return ServerResponseEntity.success(statuses);
    }

    @ApiOperation("根据产品获取预警列表")
    @GetMapping("/by-product")
    public ServerResponseEntity<List<InventoryAlert>> getAlertsByProduct(
            @ApiParam("产品ID") @RequestParam Long productId) {
        
        // 这里可以添加根据产品ID查询预警的逻辑
        return ServerResponseEntity.success(java.util.Collections.emptyList());
    }

    @ApiOperation("根据仓库获取预警列表")
    @GetMapping("/by-warehouse")
    public ServerResponseEntity<List<InventoryAlert>> getAlertsByWarehouse(
            @ApiParam("仓库ID") @RequestParam Long warehouseId) {
        
        // 这里可以添加根据仓库ID查询预警的逻辑
        return ServerResponseEntity.success(java.util.Collections.emptyList());
    }
}