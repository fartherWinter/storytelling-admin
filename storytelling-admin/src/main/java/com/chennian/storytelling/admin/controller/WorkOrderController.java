package com.chennian.storytelling.admin.controller;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.chennian.storytelling.bean.model.manufacturing.WorkOrder;
import com.chennian.storytelling.common.annotation.EventTrack;
import com.chennian.storytelling.common.enums.BusinessType;
import com.chennian.storytelling.common.enums.ModelType;
import com.chennian.storytelling.common.enums.OperatorType;
import com.chennian.storytelling.common.response.ServerResponseEntity;
import com.chennian.storytelling.common.utils.PageParam;
import com.chennian.storytelling.service.WorkOrderService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

/**
 * 工单管理控制器
 * @author chennian
 * @date 2024/1/15
 */
@RestController
@RequestMapping("/erp/manufacturing/workorder")
@Tag(name = "工单管理")
public class WorkOrderController {

    private final WorkOrderService workOrderService;

    public WorkOrderController(WorkOrderService workOrderService) {
        this.workOrderService = workOrderService;
    }

    /**
     * 查询工单列表
     */
    @PostMapping("/page")
    @Operation(summary = "工单列表")
    @EventTrack(title = ModelType.SYSTEM, businessType = BusinessType.SEARCH, operatorType = OperatorType.MANAGE, description = "工单列表")
    public ServerResponseEntity<IPage<WorkOrder>> workOrderPage(WorkOrder workOrder, PageParam<WorkOrder> page) {
        IPage<WorkOrder> workOrderPage = workOrderService.getWorkOrderList(page, workOrder);
        return ServerResponseEntity.success(workOrderPage);
    }

    /**
     * 获取工单详细信息
     */
    @PostMapping("/info/{workOrderId}")
    @Operation(summary = "工单详情")
    @EventTrack(title = ModelType.SYSTEM, businessType = BusinessType.SEARCH, operatorType = OperatorType.MANAGE, description = "工单详情")
    public ServerResponseEntity<WorkOrder> workOrderInfo(@PathVariable("workOrderId") Long workOrderId) {
        WorkOrder workOrder = workOrderService.getWorkOrderById(workOrderId);
        return ServerResponseEntity.success(workOrder);
    }

    /**
     * 新增工单
     */
    @PostMapping("/add")
    @Operation(summary = "新增工单")
    @EventTrack(title = ModelType.SYSTEM, businessType = BusinessType.INSERT, operatorType = OperatorType.MANAGE, description = "新增工单")
    public ServerResponseEntity<Void> addWorkOrder(@Validated @RequestBody WorkOrder workOrder) {
        workOrderService.addWorkOrder(workOrder);
        return ServerResponseEntity.success();
    }

    /**
     * 修改工单
     */
    @PostMapping("/edit")
    @Operation(summary = "修改工单")
    @EventTrack(title = ModelType.SYSTEM, businessType = BusinessType.UPDATE, operatorType = OperatorType.MANAGE, description = "修改工单")
    public ServerResponseEntity<Void> editWorkOrder(@Validated @RequestBody WorkOrder workOrder) {
        workOrderService.updateWorkOrder(workOrder);
        return ServerResponseEntity.success();
    }

    /**
     * 删除工单
     */
    @PostMapping("/remove/{workOrderId}")
    @Operation(summary = "删除工单")
    @EventTrack(title = ModelType.SYSTEM, businessType = BusinessType.DELETE, operatorType = OperatorType.MANAGE, description = "删除工单")
    public ServerResponseEntity<Void> removeWorkOrder(@PathVariable Long workOrderId) {
        workOrderService.deleteWorkOrder(workOrderId);
        return ServerResponseEntity.success();
    }

    /**
     * 分配工单
     */
    @PostMapping("/assign")
    @Operation(summary = "分配工单")
    @EventTrack(title = ModelType.SYSTEM, businessType = BusinessType.UPDATE, operatorType = OperatorType.MANAGE, description = "分配工单")
    public ServerResponseEntity<Void> assignWorkOrder(@RequestParam Long workOrderId, @RequestParam Long operatorId) {
        workOrderService.assignWorkOrder(workOrderId, operatorId);
        return ServerResponseEntity.success();
    }

    /**
     * 开始工单
     */
    @PostMapping("/start")
    @Operation(summary = "开始工单")
    @EventTrack(title = ModelType.SYSTEM, businessType = BusinessType.UPDATE, operatorType = OperatorType.MANAGE, description = "开始工单")
    public ServerResponseEntity<Void> startWorkOrder(@RequestParam Long workOrderId) {
        workOrderService.startWorkOrder(workOrderId);
        return ServerResponseEntity.success();
    }

    /**
     * 完成工单
     */
    @PostMapping("/complete")
    @Operation(summary = "完成工单")
    @EventTrack(title = ModelType.SYSTEM, businessType = BusinessType.UPDATE, operatorType = OperatorType.MANAGE, description = "完成工单")
    public ServerResponseEntity<Void> completeWorkOrder(@RequestParam Long workOrderId) {
        workOrderService.completeWorkOrder(workOrderId);
        return ServerResponseEntity.success();
    }
}