package com.chennian.storytelling.api.controller.mall;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.chennian.storytelling.bean.model.mall.LogisticsTracking;
import com.chennian.storytelling.bean.model.mall.LogisticsTrackingDetail;
import com.chennian.storytelling.common.response.ServerResponseEntity;

import com.chennian.storytelling.service.mall.LogisticsTrackingService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

/**
 * 物流跟踪控制器
 */
@Api(tags = "物流跟踪管理")
@RestController
@RequestMapping("/api/logistics")
public class LogisticsController {

    @Autowired
    private LogisticsTrackingService logisticsTrackingService;

    @ApiOperation("分页查询物流跟踪信息")
    @GetMapping("/tracking")
    public ServerResponseEntity<IPage<LogisticsTracking>> getLogisticsTrackingPage(
            @ApiParam("页码") @RequestParam(defaultValue = "1") Integer pageNum,
            @ApiParam("每页大小") @RequestParam(defaultValue = "10") Integer pageSize,
            @ApiParam("订单编号") @RequestParam(required = false) String orderNo,
            @ApiParam("运单号") @RequestParam(required = false) String trackingNo,
            @ApiParam("物流状态") @RequestParam(required = false) Integer logisticsStatus,
            @ApiParam("物流公司编码") @RequestParam(required = false) String logisticsCompanyCode) {

        IPage<LogisticsTracking> pageParam = logisticsTrackingService.getLogisticsTrackingPage(
            pageNum, pageSize, orderNo, trackingNo, logisticsStatus, logisticsCompanyCode);
        return ServerResponseEntity.success(pageParam);
    }

    @ApiOperation("根据订单ID获取物流跟踪信息")
    @GetMapping("/tracking/order/{orderId}")
    public ServerResponseEntity<LogisticsTracking> getLogisticsTrackingByOrderId(
            @ApiParam("订单ID") @PathVariable Long orderId) {
        
        LogisticsTracking tracking = logisticsTrackingService.getByOrderId(orderId);
        return tracking != null ? ServerResponseEntity.success(tracking) : ServerResponseEntity.showFailMsg("未找到物流跟踪信息");
    }

    @ApiOperation("根据运单号获取物流跟踪信息")
    @GetMapping("/tracking/no/{trackingNo}")
    public ServerResponseEntity<LogisticsTracking> getLogisticsTrackingByTrackingNo(
            @ApiParam("运单号") @PathVariable String trackingNo) {
        
        LogisticsTracking tracking = logisticsTrackingService.getByTrackingNumber(trackingNo);
        return tracking != null ? ServerResponseEntity.success(tracking) : ServerResponseEntity.showFailMsg("未找到物流跟踪信息");
    }

    @ApiOperation("获取物流跟踪详情")
    @GetMapping("/tracking/{trackingId}/details")
    public ServerResponseEntity<List<LogisticsTrackingDetail>> getLogisticsTrackingDetails(
            @ApiParam("物流跟踪ID") @PathVariable Long trackingId) {
        
        List<LogisticsTrackingDetail> details = logisticsTrackingService.getTrackingDetails(trackingId);
        return ServerResponseEntity.success(details);
    }

    @ApiOperation("创建物流跟踪记录")
    @PostMapping("/tracking")
    public ServerResponseEntity<Boolean> createLogisticsTracking(@RequestBody LogisticsTracking logisticsTracking) {
        boolean success = logisticsTrackingService.createLogisticsTracking(
            logisticsTracking.getOrderId(), 
            logisticsTracking.getOrderSn(), 
            logisticsTracking.getLogisticsCompanyCode(), 
            logisticsTracking.getLogisticsCompanyName(), 
            logisticsTracking.getTrackingNumber());
        return success ? ServerResponseEntity.success(true) : ServerResponseEntity.showFailMsg("创建物流跟踪记录失败");
    }

    @ApiOperation("更新物流状态")
    @PutMapping("/tracking/{trackingId}/status")
    public ServerResponseEntity<Boolean> updateLogisticsStatus(
            @ApiParam("物流跟踪ID") @PathVariable Long trackingId,
            @ApiParam("物流状态") @RequestParam String logisticsStatus,
            @ApiParam("状态描述") @RequestParam(required = false) String statusDescription,
            @ApiParam("当前位置") @RequestParam(required = false) String currentLocation) {
        
        boolean success = logisticsTrackingService.updateLogisticsStatus(
            trackingId, Integer.valueOf(logisticsStatus), statusDescription, currentLocation);
        return success ? ServerResponseEntity.success(true) : ServerResponseEntity.showFailMsg("更新物流状态失败");
    }

    @ApiOperation("添加物流跟踪详情")
    @PostMapping("/tracking/{trackingId}/details")
    public ServerResponseEntity<Boolean> addLogisticsTrackingDetail(
            @ApiParam("物流跟踪ID") @PathVariable Long trackingId,
            @RequestBody LogisticsTrackingDetail detail) {
        
        detail.setTrackingId(trackingId);
        boolean success = logisticsTrackingService.addLogisticsTrackingDetail(detail);
        return success ? ServerResponseEntity.success(true) : ServerResponseEntity.showFailMsg("添加物流跟踪详情失败");
    }

    @ApiOperation("同步物流信息")
    @PostMapping("/tracking/{trackingNumber}/sync")
    public ServerResponseEntity<Boolean> syncLogisticsInfo(
            @ApiParam("运单号") @PathVariable String trackingNumber) {
        
        boolean success = logisticsTrackingService.syncLogisticsInfo(trackingNumber);
        return success ? ServerResponseEntity.success(true) : ServerResponseEntity.showFailMsg("同步物流信息失败");
    }

    @ApiOperation("批量同步物流信息")
    @PostMapping("/tracking/batch-sync")
    public ServerResponseEntity<Integer> batchSyncLogisticsInfo(
            @ApiParam("物流跟踪ID列表") @RequestBody List<String> trackingIds) {
        
        int batchSyncLogisticsInfo = logisticsTrackingService.batchSyncLogisticsInfo(trackingIds);;
        return ServerResponseEntity.success(batchSyncLogisticsInfo);
    }

    @ApiOperation("获取异常物流订单")
    @GetMapping("/tracking/abnormal")
    public ServerResponseEntity<List<LogisticsTracking>> getAbnormalLogisticsOrders() {
        List<LogisticsTracking> abnormalOrders = logisticsTrackingService.getAbnormalLogistics();
        return ServerResponseEntity.success(abnormalOrders);
    }

    @ApiOperation("获取超时未送达订单")
    @GetMapping("/tracking/timeout")
    public ServerResponseEntity<List<LogisticsTracking>> getTimeoutOrders(
            @ApiParam("超时小时数") @RequestParam(defaultValue = "72") Integer timeoutHours) {
        
        List<LogisticsTracking> timeoutOrders = logisticsTrackingService.getOverdueDeliveries(timeoutHours);
        return ServerResponseEntity.success(timeoutOrders);
    }

    @ApiOperation("获取物流统计信息")
    @GetMapping("/statistics")
    public ServerResponseEntity<Object> getLogisticsStatistics(
            @ApiParam("开始时间") @RequestParam(required = false) LocalDateTime startTime,
            @ApiParam("结束时间") @RequestParam(required = false) LocalDateTime endTime) {
        
        // 这里可以添加统计逻辑
        // Object statistics = logisticsTrackingService.getLogisticsStatistics(startTime, endTime);
        return ServerResponseEntity.success("统计功能待实现");
    }

    @ApiOperation("根据运单号查询物流轨迹（公开接口）")
    @GetMapping("/public/track/{trackingNo}")
    public ServerResponseEntity<Object> trackByTrackingNo(
            @ApiParam("运单号") @PathVariable String trackingNo) {
        
        LogisticsTracking tracking = logisticsTrackingService.getByTrackingNumber(trackingNo);
        if (tracking == null) {
            return ServerResponseEntity.showFailMsg("未找到物流信息");
        }
        
        List<LogisticsTrackingDetail> details = logisticsTrackingService.getTrackingDetails(tracking.getId());
        
        // 构建返回数据
        Map<String, Object> result = Map.of(
            "tracking", tracking,
            "details", details
        );
        
        return ServerResponseEntity.success(result);
    }

    @ApiOperation("手动更新物流轨迹")
    @PostMapping("/tracking/manual-update")
    public ServerResponseEntity<Boolean> manualUpdateTracking(
            @ApiParam("运单号") @RequestParam String trackingNo,
            @ApiParam("轨迹时间") @RequestParam LocalDateTime trackTime,
            @ApiParam("轨迹描述") @RequestParam String description,
            @ApiParam("所在地点") @RequestParam(required = false) String location,
            @ApiParam("操作类型") @RequestParam(required = false) String operationType) {
        
        LogisticsTracking tracking = logisticsTrackingService.getByTrackingNumber(trackingNo);
        if (tracking == null) {
            return ServerResponseEntity.showFailMsg("未找到物流跟踪记录");
        }
        
        LogisticsTrackingDetail detail = new LogisticsTrackingDetail();
        detail.setTrackingId(tracking.getId());
        detail.setTrackingNumber(trackingNo);
        detail.setTrackTime(trackTime);
        detail.setDescription(description);
        detail.setLocation(location);
        detail.setOperationType(operationType != null ? Integer.valueOf(operationType) : null);
        detail.setCreateTime(LocalDateTime.now());
        
        boolean success = logisticsTrackingService.addLogisticsTrackingDetail(detail);
        return success ? ServerResponseEntity.success(true) : ServerResponseEntity.showFailMsg("添加物流轨迹失败");
    }
}