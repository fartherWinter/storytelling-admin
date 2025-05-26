package com.chennian.storytelling.admin.controller;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.chennian.storytelling.bean.model.PurchaseOrder;
import com.chennian.storytelling.bean.model.Supplier;
import com.chennian.storytelling.bean.vo.CollaborationPortalVO;
import com.chennian.storytelling.bean.vo.SupplyChainEventVO;
import com.chennian.storytelling.common.response.ServerResponseEntity;
import com.chennian.storytelling.common.utils.PageParam;
import com.chennian.storytelling.service.CustomerService;
import com.chennian.storytelling.service.PurchaseOrderService;
import com.chennian.storytelling.service.SupplierService;
import com.chennian.storytelling.service.SupplyChainEventService;
import com.chennian.storytelling.service.SupplyChainPerformanceService;
import com.chennian.storytelling.service.SupplyChainPlanService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author chennian
 * @date 2023/12/15
 */
@RestController
@RequestMapping("/erp/supply-chain")
@Tag(name = "供应链协同平台")
public class SupplyChainController {

    private final SupplierService supplierService;
    private final CustomerService customerService;
    private final PurchaseOrderService purchaseOrderService;
    private final SupplyChainEventService supplyChainEventService;
    private final SupplyChainPlanService supplyChainPlanService;
    private final SupplyChainPerformanceService supplyChainPerformanceService;

    public SupplyChainController(SupplierService supplierService, CustomerService customerService,
                               PurchaseOrderService purchaseOrderService,
                               SupplyChainEventService supplyChainEventService,
                               SupplyChainPlanService supplyChainPlanService,
                               SupplyChainPerformanceService supplyChainPerformanceService) {
        this.supplierService = supplierService;
        this.customerService = customerService;
        this.purchaseOrderService = purchaseOrderService;
        this.supplyChainEventService = supplyChainEventService;
        this.supplyChainPlanService = supplyChainPlanService;
        this.supplyChainPerformanceService = supplyChainPerformanceService;
    }

    /**
     * 获取供应链协同门户概览
     */
    @GetMapping("/portal/overview")
    @Operation(summary = "协同门户概览")
    public ServerResponseEntity<CollaborationPortalVO> portalOverview() {
        CollaborationPortalVO overview = new CollaborationPortalVO();
        
        // 设置协同门户概览数据
        overview.setActiveSuppliers(supplierService.getActiveSupplierCount());
        overview.setActiveCustomers(customerService.getActiveCustomerCount());
        overview.setPendingOrders(purchaseOrderService.getPendingOrderCount());
        overview.setRecentEvents(getRecentSupplyChainEvents(5));
        
        return ServerResponseEntity.success(overview);
    }

    /**
     * 获取供应商协同门户数据
     */
    @GetMapping("/portal/supplier/{supplierId}")
    @Operation(summary = "供应商协同门户")
    public ServerResponseEntity<Map<String, Object>> supplierPortal(@PathVariable("supplierId") Long supplierId) {
        Map<String, Object> portalData = new HashMap<>();
        
        // 供应商基本信息
        portalData.put("supplier", supplierService.selectSupplierById(supplierId));
        
        // 待处理订单
        portalData.put("pendingOrders", purchaseOrderService.getSupplierPendingOrders(supplierId));
        
        // 历史订单
        portalData.put("orderHistory", purchaseOrderService.getSupplierOrderHistory(supplierId));
        
        // 绩效指标
        portalData.put("performanceMetrics", supplierService.getSupplierPerformanceMetrics(supplierId));
        
        // 协作消息
        portalData.put("collaborationMessages", supplierService.getCollaborationMessages(supplierId));
        
        return ServerResponseEntity.success(portalData);
    }

    /**
     * 获取客户协同门户数据
     */
    @GetMapping("/portal/customer/{customerId}")
    @Operation(summary = "客户协同门户")
    public ServerResponseEntity<Map<String, Object>> customerPortal(@PathVariable("customerId") Long customerId) {
        Map<String, Object> portalData = new HashMap<>();
        
        // 客户基本信息
        portalData.put("customer", customerService.selectCustomerById(customerId));
        
        // 订单状态
        portalData.put("orderStatus", customerService.getCustomerOrderStatus(customerId));
        
        // 历史订单
        portalData.put("orderHistory", customerService.getCustomerOrderStatus(customerId));
        
        // 产品推荐
        portalData.put("productRecommendations", customerService.getProductRecommendations(customerId));
        
        // 协作消息
        portalData.put("collaborationMessages", customerService.getCollaborationMessages(customerId));
        
        return ServerResponseEntity.success(portalData);
    }

    /**
     * 发送供应链协作消息
     */
    @PostMapping("/message/send")
    @Operation(summary = "发送协作消息")
    public ServerResponseEntity<Boolean> sendCollaborationMessage(@RequestBody Map<String, Object> message) {
        // 根据消息类型发送给供应商或客户
        String type = (String) message.get("type");
        if ("supplier".equals(type)) {
            Long supplierId = Long.valueOf(message.get("recipientId").toString());
            supplierService.sendCollaborationMessage(supplierId, message);
        } else if ("customer".equals(type)) {
            Long customerId = Long.valueOf(message.get("recipientId").toString());
            customerService.sendCollaborationMessage(customerId, message);
        }
        
        return ServerResponseEntity.success(true);
    }

    /**
     * 获取供应链事件列表
     */
    @GetMapping("/events")
    @Operation(summary = "供应链事件列表")
    public ServerResponseEntity<List<SupplyChainEventVO>> supplyChainEvents(
            @RequestParam(required = false) String eventType,
            @RequestParam(required = false) String dateRange,
            @RequestParam(defaultValue = "1") Integer page,
            @RequestParam(defaultValue = "10") Integer size) {
        
        Map<String, Object> params = new HashMap<>();
        params.put("eventType", eventType);
        params.put("dateRange", dateRange);
        params.put("page", page);
        params.put("size", size);
        
        List<SupplyChainEventVO> events = getSupplyChainEvents(params);
        return ServerResponseEntity.success(events);
    }

    /**
     * 创建供应链协同计划
     */
    @PostMapping("/plan/create")
    @Operation(summary = "创建协同计划")
    public ServerResponseEntity<Long> createCollaborationPlan(@RequestBody Map<String, Object> plan) {
        // 创建供应链协同计划
        Long planId = createSupplyChainPlan(plan);
        return ServerResponseEntity.success(planId);
    }

    /**
     * 获取供应链协同计划详情
     */
    @GetMapping("/plan/{planId}")
    @Operation(summary = "协同计划详情")
    public ServerResponseEntity<Map<String, Object>> collaborationPlanDetail(@PathVariable("planId") Long planId) {
        // 获取供应链协同计划详情
        Map<String, Object> planDetail = getSupplyChainPlanDetail(planId);
        return ServerResponseEntity.success(planDetail);
    }

    /**
     * 更新供应链协同计划状态
     */
    @PostMapping("/plan/update-status")
    @Operation(summary = "更新计划状态")
    public ServerResponseEntity<Boolean> updatePlanStatus(@RequestBody Map<String, Object> params) {
        Long planId = Long.valueOf(params.get("planId").toString());
        String status = (String) params.get("status");
        String comment = (String) params.get("comment");
        
        // 更新供应链协同计划状态
        boolean success = updateSupplyChainPlanStatus(planId, status, comment);
        return ServerResponseEntity.success(success);
    }

    /**
     * 获取供应链绩效指标
     */
    @GetMapping("/performance")
    @Operation(summary = "供应链绩效")
    public ServerResponseEntity<Map<String, Object>> supplyChainPerformance() {
        // 调用供应链绩效服务获取所有绩效指标
        Map<String, Object> performance = supplyChainPerformanceService.getAllPerformanceMetrics();
        return ServerResponseEntity.success(performance);
    }

    /**
     * 获取最近的供应链事件
     */
    private List<SupplyChainEventVO> getRecentSupplyChainEvents(int limit) {
        // 调用供应链事件服务，获取最近的事件
        return supplyChainEventService.getRecentEvents(limit);
    }

    /**
     * 获取供应链事件列表
     */
    private List<SupplyChainEventVO> getSupplyChainEvents(Map<String, Object> params) {
        // 调用供应链事件服务，根据参数查询事件列表
        return supplyChainEventService.getEventsByCondition(params);
    }

    /**
     * 创建供应链协同计划
     */
    private Long createSupplyChainPlan(Map<String, Object> plan) {
        // 调用供应链协同计划服务，创建计划并返回计划ID
        return supplyChainPlanService.createPlan(plan);
    }

    /**
     * 获取供应链协同计划详情
     */
    private Map<String, Object> getSupplyChainPlanDetail(Long planId) {
        // 调用供应链协同计划服务，获取计划详情
        return supplyChainPlanService.getPlanDetail(planId);
    }

    /**
     * 更新供应链协同计划状态
     */
    private boolean updateSupplyChainPlanStatus(Long planId, String status, String comment) {
        // 调用供应链协同计划服务，更新计划状态
        return supplyChainPlanService.updatePlanStatus(planId, status, comment);
    }
}