package com.chennian.storytelling.admin.controller;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.chennian.storytelling.bean.model.manufacturing.ProductionPlan;
import com.chennian.storytelling.common.annotation.EventTrack;
import com.chennian.storytelling.common.enums.BusinessType;
import com.chennian.storytelling.common.enums.ModelType;
import com.chennian.storytelling.common.enums.OperatorType;
import com.chennian.storytelling.common.response.ServerResponseEntity;
import com.chennian.storytelling.common.utils.PageParam;
import com.chennian.storytelling.service.ProductionPlanService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

/**
 * 生产计划管理控制器
 * @author chennian
 * @date 2024/1/15
 */
@RestController
@RequestMapping("/erp/manufacturing/plan")
@Tag(name = "生产计划管理")
public class ProductionPlanController {

    private final ProductionPlanService productionPlanService;

    public ProductionPlanController(ProductionPlanService productionPlanService) {
        this.productionPlanService = productionPlanService;
    }

    /**
     * 查询生产计划列表
     */
    @PostMapping("/page")
    @Operation(summary = "生产计划列表")
    @EventTrack(title = ModelType.SYSTEM, businessType = BusinessType.SEARCH, operatorType = OperatorType.MANAGE, description = "生产计划列表")
    public ServerResponseEntity<IPage<ProductionPlan>> planPage(ProductionPlan plan, PageParam<ProductionPlan> page) {
        IPage<ProductionPlan> planPage = productionPlanService.getProductionPlanList(page, plan);
        return ServerResponseEntity.success(planPage);
    }

    /**
     * 获取生产计划详细信息
     */
    @PostMapping("/info/{planId}")
    @Operation(summary = "生产计划详情")
    @EventTrack(title = ModelType.SYSTEM, businessType = BusinessType.SEARCH, operatorType = OperatorType.MANAGE, description = "生产计划详情")
    public ServerResponseEntity<ProductionPlan> planInfo(@PathVariable("planId") Long planId) {
        ProductionPlan plan = productionPlanService.getProductionPlanById(planId);
        return ServerResponseEntity.success(plan);
    }

    /**
     * 新增生产计划
     */
    @PostMapping("/add")
    @Operation(summary = "新增生产计划")
    @EventTrack(title = ModelType.SYSTEM, businessType = BusinessType.INSERT, operatorType = OperatorType.MANAGE, description = "新增生产计划")
    public ServerResponseEntity<Void> addPlan(@Validated @RequestBody ProductionPlan plan) {
        productionPlanService.addProductionPlan(plan);
        return ServerResponseEntity.success();
    }

    /**
     * 修改生产计划
     */
    @PostMapping("/edit")
    @Operation(summary = "修改生产计划")
    @EventTrack(title = ModelType.SYSTEM, businessType = BusinessType.UPDATE, operatorType = OperatorType.MANAGE, description = "修改生产计划")
    public ServerResponseEntity<Void> editPlan(@Validated @RequestBody ProductionPlan plan) {
        productionPlanService.updateProductionPlan(plan);
        return ServerResponseEntity.success();
    }

    /**
     * 删除生产计划
     */
    @PostMapping("/remove/{planId}")
    @Operation(summary = "删除生产计划")
    @EventTrack(title = ModelType.SYSTEM, businessType = BusinessType.DELETE, operatorType = OperatorType.MANAGE, description = "删除生产计划")
    public ServerResponseEntity<Void> removePlan(@PathVariable Long planId) {
        productionPlanService.deleteProductionPlan(planId);
        return ServerResponseEntity.success();
    }

    /**
     * 审核生产计划
     */
    @PostMapping("/approve")
    @Operation(summary = "审核生产计划")
    @EventTrack(title = ModelType.SYSTEM, businessType = BusinessType.UPDATE, operatorType = OperatorType.MANAGE, description = "审核生产计划")
    public ServerResponseEntity<Void> approvePlan(@RequestParam Long planId, @RequestParam String approver) {
        productionPlanService.approveProductionPlan(planId, approver);
        return ServerResponseEntity.success();
    }

    /**
     * 执行生产计划
     */
    @PostMapping("/execute")
    @Operation(summary = "执行生产计划")
    @EventTrack(title = ModelType.SYSTEM, businessType = BusinessType.UPDATE, operatorType = OperatorType.MANAGE, description = "执行生产计划")
    public ServerResponseEntity<Void> executePlan(@RequestParam Long planId) {
        productionPlanService.executeProductionPlan(planId);
        return ServerResponseEntity.success();
    }

    /**
     * 完成生产计划
     */
    @PostMapping("/complete")
    @Operation(summary = "完成生产计划")
    @EventTrack(title = ModelType.SYSTEM, businessType = BusinessType.UPDATE, operatorType = OperatorType.MANAGE, description = "完成生产计划")
    public ServerResponseEntity<Void> completePlan(@RequestParam Long planId) {
        productionPlanService.completeProductionPlan(planId);
        return ServerResponseEntity.success();
    }
}