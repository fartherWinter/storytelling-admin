package com.chennian.storytelling.admin.controller;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.chennian.storytelling.bean.model.manufacturing.ProductionLine;
import com.chennian.storytelling.common.annotation.EventTrack;
import com.chennian.storytelling.common.enums.BusinessType;
import com.chennian.storytelling.common.enums.ModelType;
import com.chennian.storytelling.common.enums.OperatorType;
import com.chennian.storytelling.common.response.ServerResponseEntity;
import com.chennian.storytelling.common.utils.PageParam;
import com.chennian.storytelling.service.ProductionLineService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

/**
 * 生产线管理控制器
 * @author chennian
 * @date 2024/1/15
 */
@RestController
@RequestMapping("/erp/manufacturing/line")
@Tag(name = "生产线管理")
public class ProductionLineController {

    private final ProductionLineService productionLineService;

    public ProductionLineController(ProductionLineService productionLineService) {
        this.productionLineService = productionLineService;
    }

    /**
     * 查询生产线列表
     */
    @PostMapping("/page")
    @Operation(summary = "生产线列表")
    @EventTrack(title = ModelType.SYSTEM, businessType = BusinessType.SEARCH, operatorType = OperatorType.MANAGE, description = "生产线列表")
    public ServerResponseEntity<IPage<ProductionLine>> linePage(ProductionLine line, PageParam<ProductionLine> page) {
        IPage<ProductionLine> linePage = productionLineService.getProductionLineList(page, line);
        return ServerResponseEntity.success(linePage);
    }

    /**
     * 获取生产线详细信息
     */
    @PostMapping("/info/{lineId}")
    @Operation(summary = "生产线详情")
    @EventTrack(title = ModelType.SYSTEM, businessType = BusinessType.SEARCH, operatorType = OperatorType.MANAGE, description = "生产线详情")
    public ServerResponseEntity<ProductionLine> lineInfo(@PathVariable("lineId") Long lineId) {
        ProductionLine line = productionLineService.getProductionLineById(lineId);
        return ServerResponseEntity.success(line);
    }

    /**
     * 新增生产线
     */
    @PostMapping("/add")
    @Operation(summary = "新增生产线")
    @EventTrack(title = ModelType.SYSTEM, businessType = BusinessType.INSERT, operatorType = OperatorType.MANAGE, description = "新增生产线")
    public ServerResponseEntity<Void> addLine(@Validated @RequestBody ProductionLine line) {
        productionLineService.addProductionLine(line);
        return ServerResponseEntity.success();
    }

    /**
     * 修改生产线
     */
    @PostMapping("/edit")
    @Operation(summary = "修改生产线")
    @EventTrack(title = ModelType.SYSTEM, businessType = BusinessType.UPDATE, operatorType = OperatorType.MANAGE, description = "修改生产线")
    public ServerResponseEntity<Void> editLine(@Validated @RequestBody ProductionLine line) {
        productionLineService.updateProductionLine(line);
        return ServerResponseEntity.success();
    }

    /**
     * 删除生产线
     */
    @PostMapping("/remove/{lineId}")
    @Operation(summary = "删除生产线")
    @EventTrack(title = ModelType.SYSTEM, businessType = BusinessType.DELETE, operatorType = OperatorType.MANAGE, description = "删除生产线")
    public ServerResponseEntity<Void> removeLine(@PathVariable Long lineId) {
        productionLineService.deleteProductionLine(lineId);
        return ServerResponseEntity.success();
    }

    /**
     * 启动生产线
     */
    @PostMapping("/start")
    @Operation(summary = "启动生产线")
    @EventTrack(title = ModelType.SYSTEM, businessType = BusinessType.UPDATE, operatorType = OperatorType.MANAGE, description = "启动生产线")
    public ServerResponseEntity<Void> startLine(@RequestParam Long lineId) {
        productionLineService.changeProductionLineStatus(lineId, "RUNNING");
        return ServerResponseEntity.success();
    }

    /**
     * 停止生产线
     */
    @PostMapping("/stop")
    @Operation(summary = "停止生产线")
    @EventTrack(title = ModelType.SYSTEM, businessType = BusinessType.UPDATE, operatorType = OperatorType.MANAGE, description = "停止生产线")
    public ServerResponseEntity<Void> stopLine(@RequestParam Long lineId) {
        productionLineService.changeProductionLineStatus(lineId, "STOPPED");
        return ServerResponseEntity.success();
    }

    /**
     * 维护生产线
     */
    @PostMapping("/maintain")
    @Operation(summary = "维护生产线")
    @EventTrack(title = ModelType.SYSTEM, businessType = BusinessType.UPDATE, operatorType = OperatorType.MANAGE, description = "维护生产线")
    public ServerResponseEntity<Void> maintainLine(@RequestParam Long lineId) {
        productionLineService.changeProductionLineStatus(lineId, "MAINTENANCE");
        return ServerResponseEntity.success();
    }
}